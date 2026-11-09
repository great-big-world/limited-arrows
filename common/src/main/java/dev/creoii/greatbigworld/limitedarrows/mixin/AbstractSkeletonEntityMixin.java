package dev.creoii.greatbigworld.limitedarrows.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonEntityMixin extends Monster {
    @Shadow public abstract void reassessWeaponGoal();
    @Unique private int gbw$arrows = Integer.MAX_VALUE;

    protected AbstractSkeletonEntityMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void gbw$initRandomArrowCount(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        gbw$arrows = random.nextIntBetweenInclusive(10, 20); // TODO: Config
    }

    @Inject(method = "performRangedAttack", at = @At("TAIL"))
    private void gbw$subtractArrow(LivingEntity target, float pullProgress, CallbackInfo ci) {
        if (!level().isClientSide()) {
            if (--gbw$arrows <= 0) {
                setItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW), ItemStack.EMPTY);
                reassessWeaponGoal();
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("arrows", gbw$arrows);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        gbw$arrows = input.getIntOr("arrows", Integer.MAX_VALUE);
    }

    @Override
    public void dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled, ResourceKey<LootTable> lootTable) {
        dropFromLootTable(level, source, playerKilled, lootTable, itemStack -> {
            if (itemStack.is(ItemTags.ARROWS) && gbw$arrows <= 0) return;
            spawnAtLocation(level, itemStack);
        });
    }
}
