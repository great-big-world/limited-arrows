package dev.creoii.greatbigworld.limitedarrows.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
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
    @Unique private static final EntityDataAccessor<Integer> gbw$ARROWS = SynchedEntityData.defineId(AbstractSkeleton.class, EntityDataSerializers.INT);

    protected AbstractSkeletonEntityMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(gbw$ARROWS, Integer.MAX_VALUE);
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void gbw$initRandomArrowCount(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        this.entityData.set(gbw$ARROWS, random.nextIntBetweenInclusive(10, 20)); // TODO: Config
    }

    @Inject(method = "performRangedAttack", at = @At("TAIL"))
    private void gbw$subtractArrow(LivingEntity target, float pullProgress, CallbackInfo ci) {
        entityData.set(gbw$ARROWS, (entityData.get(gbw$ARROWS) - 1));
        if (entityData.get(gbw$ARROWS) <= 0) {
            setItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW), ItemStack.EMPTY);
            reassessWeaponGoal();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("arrows", entityData.get(gbw$ARROWS));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(gbw$ARROWS, input.getIntOr("arrows", Integer.MAX_VALUE));
    }
}
