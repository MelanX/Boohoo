package de.melanx.boohoo.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import de.melanx.boohoo.Boohoo;
import de.melanx.boohoo.registration.ModLootFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchantHighestRandomlyFunction extends LootItemConditionalFunction {

    private static final Random RANDOM = new Random();

    protected EnchantHighestRandomlyFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    @Override
    protected ItemStack run(@Nonnull ItemStack stack, @Nonnull LootContext context) {
        List<Enchantment> enchantments = new ArrayList<>();
        for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
            if (enchantment.canEnchant(stack)) {
                enchantments.add(enchantment);
            }
        }

        if (!enchantments.isEmpty()) {
            Enchantment random = enchantments.get(RANDOM.nextInt(enchantments.size()));
            stack.enchant(random, random.getMaxLevel() + (RANDOM.nextBoolean() ? 1 : 0));
        }

        Boohoo.getInstance().logger.error("No valid enchantment for item {}", stack);

        return stack;
    }

    @Nonnull
    @Override
    public LootItemFunctionType getType() {
        return ModLootFunctions.ENCHANT_HIGHEST_RANDOMLY;
    }

    public static LootItemConditionalFunction.Builder<?> rollTheDice() {
        return simpleBuilder(EnchantHighestRandomlyFunction::new);
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<EnchantHighestRandomlyFunction> {

        @Nonnull
        @Override
        public EnchantHighestRandomlyFunction deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context, @Nonnull LootItemCondition[] conditions) {
            return new EnchantHighestRandomlyFunction(conditions);
        }
    }
}
