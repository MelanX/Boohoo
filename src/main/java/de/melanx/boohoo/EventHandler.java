package de.melanx.boohoo;

import de.melanx.boohoo.capability.GhostCapability;
import de.melanx.boohoo.capability.IGhostStatus;
import de.melanx.boohoo.ghost.Ghost;
import de.melanx.boohoo.registration.ModEntities;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.atomic.AtomicReference;

public class EventHandler {

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.Phase.END
                && player.level instanceof ServerLevel level
                && level.getDifficulty() != Difficulty.PEACEFUL
                && level.isNight()) {
            if (level.getDayTime() % 20 == 0 && player.getRandom().nextDouble() < 0.01) {
                player.getCapability(GhostCapability.INSTANCE).ifPresent(cap -> {
                    if (!cap.isGhosted()) {
                        Ghost ghost = ModEntities.ghost.create(level);
                        if (ghost == null) {
                            return;
                        }

                        ghost.setTarget(player);
                        ghost.setPos(player.getPosition(1).add(0, 2, 0));
                        cap.setGhosted(true);
                        cap.setGhostId(ghost.getUUID());
                        level.addFreshEntity(ghost);
                    }
                });
            }
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
        if (!event.isWasDeath()) {
            Player old = event.getOriginal();
            Player jesus = event.getPlayer();

            AtomicReference<IGhostStatus> status = new AtomicReference<>(null);
            old.getCapability(GhostCapability.INSTANCE).ifPresent(status::set);

            jesus.getCapability(GhostCapability.INSTANCE).ifPresent(cap -> {
                cap.fromOld(status.get());
            });
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Ghost ghost && !ghost.level.isClientSide) {
            MinecraftServer server = ghost.level.getServer();
            //noinspection ConstantConditions
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                player.getCapability(GhostCapability.INSTANCE).ifPresent(cap -> {
                    if (cap.getGhostId() == ghost.getUUID()) {
                        cap.invalidate();
                    }
                });
            }
        }
    }
}
