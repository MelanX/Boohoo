package de.melanx.boohoo;

import de.melanx.boohoo.capability.GhostCapability;
import de.melanx.boohoo.ghost.Ghost;
import de.melanx.boohoo.registration.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;
import java.util.UUID;

public class EventHandler {

    private static final UUID GHOST_MAX_HEALTH_ID = UUID.fromString("067349e4-5adc-402e-aeff-9d4ccd9db9ba");

    @SubscribeEvent
    public void attachCapabilityToPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            GhostCapability provider = new GhostCapability();
            event.addCapability(Boohoo.getInstance().resource("ghosting"), provider);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDealDamage(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            player.getCapability(GhostCapability.INSTANCE).ifPresent(cap -> {
                float amount = event.getAmount();
                if (event.getSource().getEntity() instanceof Ghost ghost && Objects.equals(ghost.getTargetId(), player.getUUID())) {
                    amount /= ModConfig.ghostMultiplier;
                }

                cap.addDealtDamage(amount);
            });
            return;
        }

        if (event.getEntityLiving() instanceof ServerPlayer player) {
            player.getCapability(GhostCapability.INSTANCE).ifPresent(cap -> {
                float amount = event.getAmount();
                if (event.getSource().getEntity() instanceof Ghost ghost && Objects.equals(ghost.getTargetId(), player.getUUID())) {
                    amount *= ModConfig.ghostMultiplier;
                }

                cap.addTakenDamage(amount);
            });
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.world.isClientSide) {
            return;
        }

        ServerLevel level = (ServerLevel) event.world;
        ServerLevel overworld = level.getServer().overworld();
        if (level.getDifficulty() != Difficulty.PEACEFUL
                && overworld.getDayTime() % 20 == 0) {
            level.players().forEach(player -> {
                player.getCapability(GhostCapability.INSTANCE).ifPresent(cap -> {
                    if (overworld.isNight()
                            && player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL
                            && !cap.isGhosted()
                            && player.getRandom().nextDouble() < ModConfig.spawnChance) {
                        Ghost ghost = ModEntities.ghost.create(level);
                        if (ghost == null) {
                            return;
                        }

                        AttributeModifier modifier = new AttributeModifier(GHOST_MAX_HEALTH_ID, "Player based health boost", Math.max(0, (cap.getDealtDamage() - cap.getTakenDamage()) * ModConfig.healthMultiplier), AttributeModifier.Operation.ADDITION);

                        ghost.setTarget(player);
                        ghost.setPos(player.getPosition(1).add(level.random.nextInt(3) - 1.5, level.random.nextInt(3), level.random.nextInt(3) - 1.5));
                        //noinspection ConstantConditions
                        ghost.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(modifier);
                        ghost.setHealth(ghost.getMaxHealth());
                        cap.setGhosted(true);
                        cap.setGhostId(ghost.getUUID());
                        level.addFreshEntity(ghost);
                    }

                    if (cap.getLastDay() != level.getDayTime() / 24000L) {
                        cap.invalidate();
                        cap.setLastDay(level.getDayTime() / 24000L);
                    }
                });
            });
        }
    }

    @SubscribeEvent
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            player.getCapability(GhostCapability.INSTANCE).ifPresent(cap -> {
                if (cap.isGhosted() && cap.getGhostId() != null) {
                    ServerLevel level = (ServerLevel) event.getPlayer().level;

                    Entity entity = level.getEntity(cap.getGhostId());
                    if (entity != null) {
                        level.removeEntity(entity);
                    }

                    Ghost ghost = ModEntities.ghost.create(level);
                    if (ghost == null) {
                        return;
                    }

                    ghost.deserializeNBT(cap.getGhostData());

                    level.addFreshEntity(ghost);
                }
            });
        }
    }

    @SubscribeEvent
    public void onLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            player.getCapability(GhostCapability.INSTANCE).ifPresent(cap -> {
                if (cap.getGhostId() != null) {
                    Entity entity = ((ServerLevel) player.level).getEntities().get(cap.getGhostId());
                    if (entity instanceof Ghost ghost) {
                        cap.setGhostData(ghost);
                        entity.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        Player old = event.getOriginal();
        Player jesus = event.getPlayer();

        old.reviveCaps();
        old.getCapability(GhostCapability.INSTANCE).ifPresent(original -> {
            jesus.getCapability(GhostCapability.INSTANCE).ifPresent(cap -> {
                cap.fromOld(original);
                cap.reset();
            });
        });
        old.invalidateCaps();
    }
}
