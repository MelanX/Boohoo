package de.melanx.boohoo;

import de.melanx.boohoo.ghost.GhostRenderer;
import de.melanx.boohoo.registration.ModEntities;
import de.melanx.boohoo.registration.ModLootFunctions;
import io.github.noeppi_noeppi.libx.mod.registration.ModXRegistration;
import io.github.noeppi_noeppi.libx.mod.registration.RegistrationBuilder;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

@Mod("boohoo")
public final class Boohoo extends ModXRegistration {

    private static Boohoo instance;

    public Boohoo() {
        super("boohoo", null);
        instance = this;
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Override
    protected void initRegistration(RegistrationBuilder builder) {
        builder.setVersion(1);
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        ModLootFunctions.init();
    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.ghost, GhostRenderer::new);
    }

    @Nonnull
    public static Boohoo getInstance() {
        return instance;
    }
}
