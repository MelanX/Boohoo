package de.melanx.boohoo.registration;

import de.melanx.boohoo.Boohoo;
import de.melanx.boohoo.ghost.Ghost;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

@RegisterClass
public class ModEntities {

    public static final EntityType<Ghost> ghost = EntityType.Builder.of(Ghost::new, MobCategory.MONSTER).sized(0.6F, 1.99F).build(Boohoo.getInstance().modid + "_ghost");
}
