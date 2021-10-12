package de.melanx.boohoo.ghost;

import de.melanx.boohoo.ModConfig;
import de.melanx.boohoo.registration.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Ghost extends Monster {

    private int vanishCounter = Integer.MAX_VALUE;
    private UUID targetId = null;

    public Ghost(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.moveControl = new GhostMoveControl(this);
        this.xpReward = 7;
    }

    public static AttributeSupplier defaultAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.MAX_HEALTH, 30.0)
                .build();
    }

    @Nonnull
    @Override
    protected PathNavigation createNavigation(@Nonnull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(@Nonnull BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };

        navigation.setCanPassDoors(true);
        navigation.setCanFloat(true);
        navigation.setCanOpenDoors(false);
        return navigation;
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);

        if (ModConfig.disappearAtDay && this.vanishCounter > ModConfig.vanishCounter && this.level.isDay()) {
            this.vanishCounter = ModConfig.vanishCounter;
        }

        // drop inventory and remove ghost from world
        if (this.vanishCounter <= 0) {
            this.kill();
        }

        // count downwards
        if (this.vanishCounter != Integer.MAX_VALUE) {
            this.vanishCounter--;
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1, true));
        this.goalSelector.addGoal(4, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new Ghost.GhostRandomMoveGoal());
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level.isClientSide) {
            if (this.targetId == null && this.getTarget() != null) {
                this.targetId = this.getTarget().getUUID();
            } else if (this.targetId != null && this.getTarget() == null) {
                Entity entity = ((ServerLevel) this.level).getEntity(this.targetId);
                if (entity instanceof Player player) {
                    this.setTarget(player);
                }
            }
        }

        if (this.getTarget() instanceof Player player) {
            // teleport to player when player changed dimension
            if (!this.level.isClientSide) {
                if (this.getTarget().level != this.level) {
                    this.changeDimension((ServerLevel) this.getTarget().level);
                    this.teleportTowards(this.getTarget());
                }
            }

            // teleport random to player
            if (this.isLookingAtMe(player)) {
                this.teleportTowards(player);
            }

            // player dead -> take one item, fly away and activate vanish counter
            if (!player.isAlive()) {
                if (ModConfig.stealItems && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                    AABB bb = player.getBoundingBox().deflate(3, 3, 3);
                    List<ItemEntity> itemEntities = this.level.getEntitiesOfClass(ItemEntity.class, bb);
                    Optional<ItemEntity> item = itemEntities.stream().findAny();
                    ItemStack steal = ItemStack.EMPTY;
                    if (item.isPresent()) {
                        ItemEntity itemEntity = item.get();
                        steal = itemEntity.getItem();
                        itemEntity.remove(RemovalReason.DISCARDED);
                    }
                    this.setItemInHand(InteractionHand.MAIN_HAND, steal);
                }

                this.vanishCounter = ModConfig.vanishCounter;
            }
        }
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.get("VanishCounter") != null) {
            this.vanishCounter = tag.getInt("VanishCounter");
        }

        if (tag.get("TargetId") != null) {
            this.targetId = tag.getUUID("TargetId");
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("VanishCounter", this.vanishCounter);

        if (this.targetId != null) {
            tag.putUUID("TargetId", this.targetId);
        }
    }

    // [Vanilla copy]
    // checks if the player is looking at the ghost
    protected boolean isLookingAtMe(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        if (helmet.isEnderMask(player, null)) {
            return false;
        } else {
            Vec3 view = player.getViewVector(1).normalize();
            Vec3 diff = new Vec3(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
            double length = diff.length();
            diff = diff.normalize();
            double dot = view.dot(diff);
            return dot > 1 - 0.025 / length && player.hasLineOfSight(this);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    protected boolean teleport() {
        if (!this.level.isClientSide && this.isAlive()) {
            double x = this.getX() + (this.random.nextDouble() - 0.5) * 5;
            double y = this.getX() + (this.random.nextInt(20) - 10);
            double z = this.getZ() + (this.random.nextDouble() - 0.5) * 5;

            return this.teleport(x, y, z);
        }

        return false;
    }

    // [Vanilla copy]
    protected boolean teleportTowards(@Nonnull Entity entity) {
        Vec3 vec3 = new Vec3(this.getX() - entity.getX(), this.getY(0.5D) - entity.getEyeY(), this.getZ() - entity.getZ()).normalize();
        double range = 16.0D;
        double x = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3.x * range;
        double y = this.getY() + (double) (this.random.nextInt(16) - 8) - vec3.y * range;
        double z = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3.z * range;

        return this.teleport(x, y, z);
    }

    private boolean teleport(double x, double y, double z) {
        int counter = 0;
        boolean teleported = false;
        while (!teleported && counter++ < 100) {
            teleported = this.randomTeleport(x, y, z, false);
        }

        if (teleported) {
            this.playSound(this.getTeleportSound(), this.getSoundVolume(), this.getVoicePitch());
        }

        return teleported;
    }

    private void disappear() {
        this.getAllSlots().forEach(stack -> {
            if (!stack.isEmpty()) {
                this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack));
            }
        });
        this.remove(RemovalReason.DISCARDED);
    }

    @Nullable
    public UUID getTargetId() {
        return this.targetId;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource source) {
        return ModSounds.ghostHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ghostDeath;
    }

    @Nonnull
    @Override
    protected SoundEvent getFallDamageSound(int height) {
        return ModSounds.ghostHurt;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ghostAmbient;
    }

    @Nonnull
    protected SoundEvent getTeleportSound() {
        return ModSounds.ghostTeleport;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 160;
    }

    @Override
    protected int getExperienceReward(@Nonnull Player player) {
        return this.xpReward *= Math.abs(player.getHealth() - player.getMaxHealth());
    }

    private class GhostMoveControl extends MoveControl {

        public GhostMoveControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Ghost ghost = Ghost.this;
                Vec3 vec3 = new Vec3(this.wantedX - ghost.getX(), this.wantedY - ghost.getY(), this.wantedZ - ghost.getZ());
                double length = vec3.length();
                ghost.setDeltaMovement(ghost.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05 / length)));
                LivingEntity target = ghost.getTarget();
                if (this.isInWall(ghost)) {
                    if (target != null) {
                        ghost.teleportTowards(target);
                    } else {
                        ghost.teleport();
                    }
                } else if (length < ghost.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                } else if (target == null) {
                    Vec3 deltaMovement = ghost.getDeltaMovement();
                    ghost.setYRot(-((float) Mth.atan2(deltaMovement.x, deltaMovement.z) * (180F / (float) Math.PI)));
                } else {
                    double diffX = target.getX() - ghost.getX();
                    double diffZ = target.getZ() - ghost.getZ();
                    ghost.setYRot(-((float) Mth.atan2(diffX, diffZ) * (180F / (float) Math.PI)));
                }

                ghost.yBodyRot = ghost.getYRot();
            }
        }

        // [Vanilla copy]
        // Vanilla check if the ghost is in a wall, ignores noPhysics
        public boolean isInWall(Ghost ghost) {
            float width = ghost.getDimensions(ghost.getPose()).width * 0.8F;
            AABB aabb = AABB.ofSize(ghost.getEyePosition(), width, 1, width);
            return ghost.level.getBlockCollisions(ghost, aabb,
                    (state, pos) -> state.isSuffocating(ghost.level, pos)).findAny().isPresent();
        }
    }

    class GhostRandomMoveGoal extends Goal {

        public GhostRandomMoveGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            return !Ghost.this.getMoveControl().hasWanted() && Ghost.this.random.nextInt(7) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = Ghost.this.blockPosition();

            for (int i = 0; i < 3; ++i) {
                BlockPos pos = blockpos.offset(Ghost.this.random.nextInt(15) - 7, Ghost.this.random.nextInt(11) - 5, Ghost.this.random.nextInt(15) - 7);
                if (Ghost.this.level.isEmptyBlock(pos)) {
                    Ghost.this.moveControl.setWantedPosition((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 0.25D);
                    if (Ghost.this.getTarget() == null) {
                        Ghost.this.getLookControl().setLookAt((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }
}
