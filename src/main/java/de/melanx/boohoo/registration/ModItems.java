package de.melanx.boohoo.registration;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;

@RegisterClass
public class ModItems {

    public static final Item ghostSpawnEgg = new ForgeSpawnEggItem(() -> ModEntities.ghost, 0xe5f9fe, 0xeff5f4, new Item.Properties().tab(CreativeModeTab.TAB_MISC));
}
