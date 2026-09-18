package ink.myumoon.epiphanyextra.reskillable;

import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.reward.RewardSource;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.reskillable.attachment.ReskillableRewardData;
import ink.myumoon.epiphanyextra.reskillable.registry.ReskillableAttachments;
import net.bandit.reskillable.Configuration;
import net.bandit.reskillable.common.capabilities.SkillModel;
import net.minecraft.server.level.ServerPlayer;

/** The only class in EpiphanyExtra that directly references Reskillable classes. */
final class ReskillableInternal {
    private ReskillableInternal() {}

    static boolean hasSkillLevel(ServerPlayer player, String skill,
                                 Comparison comparison, int expectedLevel) {
        try {
            if (expectedLevel < 1 || !Configuration.isKnownSkill(skill)) return false;
            SkillModel model = SkillModel.get(player);
            return model != null && comparison.test(model.getSkillLevel(skill), expectedLevel);
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate Reskillable skill {}", skill, exception);
            return false;
        }
    }

    static void applySkillLevel(ServerPlayer player, String skill, int requestedLevels,
                                RewardSource source) {
        try {
            if (requestedLevels <= 0 || !Configuration.isKnownSkill(skill)) return;
            SkillModel model = SkillModel.get(player);
            if (model == null) return;

            ReskillableRewardData data = player.getData(ReskillableAttachments.RESKILLABLE_REWARDS);
            ReskillableRewardData.Grant existing = data.get(source);
            if (existing != null && existing.skill().equals(skill)
                    && existing.requestedLevels() == requestedLevels) return;
            if (existing != null) {
                removeGrant(player, model, data, existing);
                data = player.getData(ReskillableAttachments.RESKILLABLE_REWARDS);
            }

            int current = model.getSkillLevel(skill);
            int roomBySkill = Math.max(0, Configuration.getMaxLevel() - current);
            int maxSpendable = Configuration.getMaxSpendableLevels();
            int roomByTotal = maxSpendable < 0
                    ? Integer.MAX_VALUE
                    : Math.max(0, maxSpendable - model.getTotalSpentLevels());
            int granted = Math.min(requestedLevels, Math.min(roomBySkill, roomByTotal));
            if (granted > 0) {
                model.setSkillLevel(skill, current + granted);
                sync(player, model);
            }

            player.setData(ReskillableAttachments.RESKILLABLE_REWARDS,
                    data.with(source, new ReskillableRewardData.Grant(
                            source.ownerKind(), source.ownerId(), source.rewardSlot(), source.legacyId(),
                            skill, requestedLevels, granted)));
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to apply Reskillable skill reward {}", skill, exception);
        }
    }

    static void removeSkillLevel(ServerPlayer player, RewardSource source) {
        try {
            ReskillableRewardData data = player.getData(ReskillableAttachments.RESKILLABLE_REWARDS);
            ReskillableRewardData.Grant grant = data.get(source);
            if (grant == null) return;

            SkillModel model = SkillModel.get(player);
            if (model != null) removeGrant(player, model, data, grant);
            else player.setData(ReskillableAttachments.RESKILLABLE_REWARDS, data.without(source));
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to remove Reskillable skill reward", exception);
        }
    }

    private static void removeGrant(ServerPlayer player, SkillModel model,
                                    ReskillableRewardData data,
                                    ReskillableRewardData.Grant grant) {
        int current = model.getSkillLevel(grant.skill());
        int target = Math.max(1, current - grant.grantedLevels());
        if (target != current) {
            model.setSkillLevel(grant.skill(), target);
            sync(player, model);
        }
        player.setData(ReskillableAttachments.RESKILLABLE_REWARDS,
                data.without(grant.source()));
    }

    private static void sync(ServerPlayer player, SkillModel model) {
        model.updateSkillAttributeBonuses(player);
        model.syncSkills(player);
    }
}
