package ink.myumoon.epiphanyextra.viscriptquests;

import com.viscriptquests.event.neoforge.QuestEvent;
import com.viscriptquests.quest.data.runtime.ObjectiveStatus;
import com.viscriptquests.quest.data.runtime.PlayerQuestState;
import com.viscriptquests.quest.data.runtime.QuestStatus;
import com.viscriptquests.quest.data.runtime.TaskObjectiveProgress;
import com.viscriptquests.quest.data.runtime.TaskProgress;
import com.viscriptquests.quest.data.runtime.TaskStatus;
import com.viscriptquests.quest.runtime.QuestManager;
import com.viscriptquests.util.QuestFileHelper;
import com.viscriptquests.util.ViScriptQuestsServerUtil;
import ink.myumoon.epiphany.api.AptitudeSourceManager;
import ink.myumoon.epiphany.api.EpiphanyManager;
import ink.myumoon.epiphany.api.ModuleManager;
import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.reward.RewardSource;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.viscriptquests.attachment.ViScriptQuestsRewardData;
import ink.myumoon.epiphanyextra.viscriptquests.registry.ViScriptQuestsAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** The only class in EpiphanyExtra that directly references ViScriptQuests types. */
final class ViScriptQuestsInternal {
    private static final ResourceLocation QUEST_STARTED = id("viscript_quests_quest_started");
    private static final ResourceLocation QUEST_COMPLETED = id("viscript_quests_quest_completed");
    private static final ResourceLocation TASK_COMPLETED = id("viscript_quests_task_completed");
    private static final ResourceLocation OBJECTIVE_COMPLETED = id("viscript_quests_objective_completed");
    private static final ResourceLocation REWARD_GRANTED = id("viscript_quests_reward_granted");
    private static final Set<String> DIAGNOSTICS = ConcurrentHashMap.newKeySet();

    private ViScriptQuestsInternal() {}

    static void registerEvents() {
        NeoForge.EVENT_BUS.addListener(ViScriptQuestsInternal::onQuestStarted);
        NeoForge.EVENT_BUS.addListener(ViScriptQuestsInternal::onQuestCompleted);
        NeoForge.EVENT_BUS.addListener(ViScriptQuestsInternal::onTaskCompleted);
        NeoForge.EVENT_BUS.addListener(ViScriptQuestsInternal::onObjectiveCompleted);
        NeoForge.EVENT_BUS.addListener(ViScriptQuestsInternal::onRewardGranted);
        EpiphanyExtra.LOGGER.info("Registered ViScriptQuests lifecycle event bridge");
    }

    static boolean hasQuest(ServerPlayer player, String questId, String status) {
        try {
            PlayerQuestState quest = ViScriptQuestsServerUtil.getQuest(player, normalizeQuest(questId));
            QuestStatus expected = parseQuestStatus(status);
            return quest != null && expected != null && quest.status == expected;
        } catch (RuntimeException exception) {
            warnOnce("quest:" + questId, "Failed to evaluate ViScriptQuests Quest condition for " + questId, exception);
            return false;
        }
    }

    static boolean hasTask(ServerPlayer player, String questId, String taskId, String status) {
        try {
            TaskProgress task = ViScriptQuestsServerUtil.getTask(player, normalizeQuest(questId), normalizeId(taskId));
            TaskStatus expected = parseTaskStatus(status);
            return task != null && expected != null && task.status == expected;
        } catch (RuntimeException exception) {
            warnOnce("task:" + questId + ":" + taskId,
                    "Failed to evaluate ViScriptQuests Task condition for " + questId + "/" + taskId, exception);
            return false;
        }
    }

    static boolean hasObjective(ServerPlayer player, String questId, String taskId, String objectiveId,
                                Optional<String> status, Optional<Comparison> comparison,
                                Optional<Integer> amount) {
        try {
            if (status.isEmpty() && (comparison.isEmpty() || amount.isEmpty())) return false;
            if (amount.isPresent() && amount.get() < 0) return false;
            TaskProgress task = ViScriptQuestsServerUtil.getTask(player, normalizeQuest(questId), normalizeId(taskId));
            TaskObjectiveProgress objective = task == null
                    ? null : task.findObjectiveProgress(normalizeId(objectiveId)).orElse(null);
            if (objective == null) {
                warnOnce("objective:" + questId + ":" + taskId + ":" + objectiveId,
                        "Unknown ViScriptQuests Objective " + questId + "/" + taskId + "/" + objectiveId, null);
                return false;
            }
            if (status.isPresent()) {
                ObjectiveStatus expected = parseObjectiveStatus(status.get());
                if (expected == null || objective.status != expected) return false;
            }
            return comparison.isEmpty() || comparison.get().test(objective.currentAmount, amount.orElseThrow());
        } catch (RuntimeException exception) {
            warnOnce("objective:" + questId + ":" + taskId + ":" + objectiveId,
                    "Failed to evaluate ViScriptQuests Objective condition", exception);
            return false;
        }
    }

    static boolean grant(ServerPlayer player, String questId) {
        try {
            String normalized = normalizeQuest(questId);
            return !normalized.isEmpty() && QuestManager.grant(player, normalized);
        } catch (RuntimeException exception) {
            warnOnce("grant:" + questId, "Failed to grant ViScriptQuests Quest " + questId, exception);
            return false;
        }
    }

    static boolean complete(ServerPlayer player, String questId) {
        try {
            String normalized = normalizeQuest(questId);
            return !normalized.isEmpty() && QuestManager.complete(player, normalized);
        } catch (RuntimeException exception) {
            warnOnce("complete:" + questId, "Failed to complete ViScriptQuests Quest " + questId, exception);
            return false;
        }
    }

    static boolean triggerCustom(ServerPlayer player, String triggerId, RewardSource source) {
        try {
            String normalized = normalizeId(triggerId);
            if (normalized.isEmpty()) return false;
            ViScriptQuestsRewardData data = player.getData(ViScriptQuestsAttachments.REWARDS);
            if (data.has(source)) return true;
            if (!QuestManager.triggerCustom(player, normalized)) return false;
            player.setData(ViScriptQuestsAttachments.REWARDS, data.with(source, normalized));
            return true;
        } catch (RuntimeException exception) {
            warnOnce("trigger:" + triggerId, "Failed to trigger ViScriptQuests custom target " + triggerId, exception);
            return false;
        }
    }

    private static void onQuestStarted(QuestEvent.QuestStarted event) {
        grantForOnline(event, QUEST_STARTED, event.getQuestId());
    }

    private static void onQuestCompleted(QuestEvent.QuestCompleted event) {
        grantForOnline(event, QUEST_COMPLETED, event.getQuestId());
    }

    private static void onTaskCompleted(QuestEvent.TaskCompleted event) {
        grantForOnline(event, TASK_COMPLETED, event.getTarget());
    }

    private static void onObjectiveCompleted(QuestEvent.ObjectiveCompleted event) {
        grantForOnline(event, OBJECTIVE_COMPLETED, event.getTarget());
    }

    private static void onRewardGranted(QuestEvent.RewardGranted event) {
        try {
            ServerPlayer player = event.getPlayer();
            if (player == null || player instanceof FakePlayer) return;
            ResourceLocation target = parseTarget(event.getTarget(), "reward");
            if (target == null) return;
            AptitudeSourceManager.grant(player, REWARD_GRANTED, target, null);
            checkAutoUnlock(player);
        } catch (Exception exception) {
            EpiphanyExtra.LOGGER.error("ViScriptQuests RewardGranted bridge failed", exception);
        }
    }

    private static void grantForOnline(QuestEvent event, ResourceLocation behavior, String rawTarget) {
        try {
            ResourceLocation target = parseTarget(rawTarget, behavior.getPath());
            if (target == null) return;
            Set<UUID> seen = new HashSet<>();
            for (ServerPlayer player : event.getOnlineMembers()) {
                if (player == null || player instanceof FakePlayer || !seen.add(player.getUUID())) continue;
                AptitudeSourceManager.grant(player, behavior, target, null);
                checkAutoUnlock(player);
            }
        } catch (Exception exception) {
            EpiphanyExtra.LOGGER.error("ViScriptQuests lifecycle bridge failed for " + behavior, exception);
        }
    }

    private static void checkAutoUnlock(ServerPlayer player) {
        ModuleManager.checkAutoUnlock(player);
        EpiphanyManager.checkAutoUnlock(player);
    }

    private static ResourceLocation parseTarget(String rawTarget, String diagnosticKey) {
        if (rawTarget == null || rawTarget.isBlank()) {
            warnOnce("target:" + diagnosticKey, "ViScriptQuests event did not provide an aptitude target", null);
            return null;
        }
        ResourceLocation target = ResourceLocation.tryParse(rawTarget);
        if (target == null) {
            warnOnce("target:" + rawTarget, "Invalid ViScriptQuests aptitude target '" + rawTarget + "'", null);
        }
        return target;
    }

    private static String normalizeQuest(String value) {
        if (value == null || value.isBlank()) return "";
        String normalized = QuestFileHelper.normalizeQuestId(value.trim());
        return normalized == null ? "" : normalized;
    }

    private static String normalizeId(String value) {
        return value == null ? "" : value.trim();
    }

    private static QuestStatus parseQuestStatus(String value) {
        try { return QuestStatus.valueOf(normalizeId(value).toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException exception) { return null; }
    }

    private static TaskStatus parseTaskStatus(String value) {
        try { return TaskStatus.valueOf(normalizeId(value).toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException exception) { return null; }
    }

    private static ObjectiveStatus parseObjectiveStatus(String value) {
        try { return ObjectiveStatus.valueOf(normalizeId(value).toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException exception) { return null; }
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(EpiphanyExtra.MODID, path);
    }

    private static void warnOnce(String key, String message, Throwable exception) {
        if (DIAGNOSTICS.add(key)) {
            if (exception == null) EpiphanyExtra.LOGGER.warn(message);
            else EpiphanyExtra.LOGGER.warn(message, exception);
        }
    }
}
