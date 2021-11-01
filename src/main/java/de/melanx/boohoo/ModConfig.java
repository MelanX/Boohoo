package de.melanx.boohoo;

import io.github.noeppi_noeppi.libx.annotation.config.RegisterConfig;
import io.github.noeppi_noeppi.libx.config.Config;
import io.github.noeppi_noeppi.libx.config.validator.DoubleRange;
import io.github.noeppi_noeppi.libx.config.validator.IntRange;
import io.github.noeppi_noeppi.libx.util.ResourceList;
import net.minecraft.resources.ResourceLocation;

@RegisterConfig
public class ModConfig {

    @Config("The chance per second to spawn a ghost at night nearby a player")
    public static double spawnChance = 0.001;

    @Config("The multiplier for calculating the additional health for a ghost")
    public static double healthMultiplier = 1;

    @Config("Duration in ticks the ghost will stay in the world after the player died.")
    @IntRange(max = Integer.MAX_VALUE - 1)
    public static int vanishCounter = 1200;

    @Config("Should the ghost disappear when turning into day?")
    public static boolean disappearAtDay = true;

    @Config("Should the ghost steal one item stack once the player is dead?")
    public static boolean stealItems = true;

    @Config({"Ghost multiplier when the players ghost takes or deals damage.",
            "Increases the amount when taking damage.",
            "Decreases the amount when dealing damage."})
    @DoubleRange(max = 10)
    public static double ghostMultiplier = 1;

    @Config("A list of dimensions where no ghost should spawn")
    public static ResourceList forbiddenDimensions = new ResourceList(false, b -> {
        b.simple(new ResourceLocation("bingolobby", "lobby"));
    });
}
