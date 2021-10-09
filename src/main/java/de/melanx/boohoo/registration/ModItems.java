package de.melanx.boohoo.registration;

import de.melanx.boohoo.Boohoo;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

@RegisterClass
public class ModItems {

    public static final Item ghostSpawnEgg = new SpawnEggItem(ModEntities.ghost, 0x000000, 0xFFFFFF, new Item.Properties().tab(Boohoo.getTab()));
}
