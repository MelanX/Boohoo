package de.melanx.boohoo;

import io.github.noeppi_noeppi.libx.annotation.config.RegisterConfig;
import io.github.noeppi_noeppi.libx.config.Config;
import io.github.noeppi_noeppi.libx.config.validator.IntRange;

@RegisterConfig
public class ModConfig {

    @Config("The chance per second to spawn a ghost at night nearby a player")
    public static double spawnChance = 0.01;

    @Config("Duration in ticks the ghost will stay in the world after the player died.")
    @IntRange(max = Integer.MAX_VALUE - 1)
    public static int vanishCounter = 1200;

    @Config("Should the ghost disappear when turning into day?")
    public static boolean disappearAtDay = true;
}
