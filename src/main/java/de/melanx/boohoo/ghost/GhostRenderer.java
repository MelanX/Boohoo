package de.melanx.boohoo.ghost;

import de.melanx.boohoo.Boohoo;
import de.melanx.boohoo.registration.EventBasedRegistration;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class GhostRenderer extends MobRenderer<Ghost, GhostModel> {

    private static final ResourceLocation TEXTURE = Boohoo.getInstance().resource("textures/entity/ghost/ghost.png");

    public GhostRenderer(EntityRendererProvider.Context context) {
        super(context, new GhostModel(context.bakeLayer(EventBasedRegistration.GHOST_LAYER)), 0);
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull Ghost ghost) {
        return TEXTURE;
    }
}
