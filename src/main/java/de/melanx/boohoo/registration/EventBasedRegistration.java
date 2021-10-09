package de.melanx.boohoo.registration;

import de.melanx.boohoo.Boohoo;
import de.melanx.boohoo.ghost.Ghost;
import de.melanx.boohoo.ghost.GhostModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "boohoo", bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventBasedRegistration {

    public static ModelLayerLocation GHOST_LAYER = new ModelLayerLocation(Boohoo.getInstance().resource("ghost"), "ghost");

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ghost, Ghost.defaultAttributes());
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GHOST_LAYER, GhostModel::createBodyLayer);
    }

    @SubscribeEvent
    public void onSpawn(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.Phase.END
                && player.level instanceof ServerLevel level
                && level.getDifficulty() != Difficulty.PEACEFUL
                && level.isNight()) {
            if (level.getDayTime() % 20 == 0 && player.getRandom().nextDouble() < 0.001 && player.getPersistentData().get("Ghosted") != null) {
                Ghost ghost = ModEntities.ghost.create(level);
                if (ghost == null) {
                    return;
                }

                ghost.setTarget(player);
                ghost.setPos(player.getPosition(1).add(0, 2, 0));
                player.getPersistentData().putBoolean("Ghosted", true);
                player.getPersistentData().putUUID("Ghost", ghost.getUUID());
                level.addFreshEntity(ghost);
            }
        }
    }
}
