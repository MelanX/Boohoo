package de.melanx.boohoo;

import de.melanx.boohoo.ghost.GhostRenderer;
import de.melanx.boohoo.registration.EventBasedRegistration;
import de.melanx.boohoo.registration.ModEntities;
import io.github.noeppi_noeppi.libx.mod.registration.ModXRegistration;
import io.github.noeppi_noeppi.libx.mod.registration.RegistrationBuilder;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

@Mod("boohoo")
public final class Boohoo extends ModXRegistration {

    private static Boohoo instance;

    public Boohoo() {
        super("boohoo", new CreativeModeTab("boohoo") {
            @Nonnull
            @Override
            public ItemStack makeIcon() {
                return ItemStack.EMPTY;
            }
        });
        instance = this;
        MinecraftForge.EVENT_BUS.register(new EventBasedRegistration());
    }

    @Override
    protected void initRegistration(RegistrationBuilder builder) {
        builder.setVersion(1);
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {

    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.ghost, GhostRenderer::new);
    }

    @Nonnull
    public static Boohoo getInstance() {
        return instance;
    }

    @Nonnull
    public static CreativeModeTab getTab() {
        //noinspection ConstantConditions
        return instance.tab;
    }
}
