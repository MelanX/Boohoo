package de.melanx.boohoo.registration;

import de.melanx.boohoo.Boohoo;
import de.melanx.boohoo.loot.EnchantHighestRandomlyFunction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ModLootFunctions {

    public static final LootItemFunctionType ENCHANT_HIGHEST_RANDOMLY = register("enchant_highest_randomly", new EnchantHighestRandomlyFunction.Serializer());

    public static void init() {
        Boohoo.getInstance().logger.info("Registered loot functions.");
    }

    @SuppressWarnings("SameParameterValue")
    private static LootItemFunctionType register(String name, Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, Boohoo.getInstance().resource(name), new LootItemFunctionType(serializer));
    }
}
