package de.melanx.boohoo.datagen;

import de.melanx.boohoo.loot.EnchantHighestRandomlyFunction;
import de.melanx.boohoo.registration.ModEntities;
import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.data.provider.EntityLootProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

@Datagen
public class LootTableProvider extends EntityLootProviderBase {

    public LootTableProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    protected void setup() {
        this.customLootTable(ModEntities.ghost, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .bonusRolls(1, 2)
                        .add(LootItem.lootTableItem(Items.BOOK)
                                .apply(EnchantRandomlyFunction.randomApplicableEnchantment())
                                .setWeight(19))
                        .add(LootItem.lootTableItem(Items.BOOK)
                                .apply(EnchantHighestRandomlyFunction.rollTheDice()))));
    }
}
