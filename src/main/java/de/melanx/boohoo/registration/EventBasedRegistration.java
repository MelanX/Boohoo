package de.melanx.boohoo.registration;

import de.melanx.boohoo.Boohoo;
import de.melanx.boohoo.capability.GhostCapability;
import de.melanx.boohoo.capability.IGhostStatus;
import de.melanx.boohoo.ghost.Ghost;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "boohoo", bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventBasedRegistration {

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ghost, Ghost.defaultAttributes());
    }

    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.register(IGhostStatus.class);
        GhostCapability.INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
        });
    }

    @SubscribeEvent
    public void attachCapabilityToPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            GhostCapability provider = new GhostCapability();
            event.addCapability(Boohoo.getInstance().resource("ghosting"), provider);
        }
    }
}
