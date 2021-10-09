package de.melanx.boohoo.registration;

import de.melanx.boohoo.Boohoo;
import de.melanx.boohoo.ghost.GhostModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "boohoo", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBasedRegistration {

    public static ModelLayerLocation GHOST_LAYER = new ModelLayerLocation(Boohoo.getInstance().resource("ghost"), "ghost");

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GHOST_LAYER, GhostModel::createBodyLayer);
    }
}
