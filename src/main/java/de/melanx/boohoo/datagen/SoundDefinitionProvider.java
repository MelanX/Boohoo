package de.melanx.boohoo.datagen;

import de.melanx.boohoo.Boohoo;
import de.melanx.boohoo.registration.ModSounds;
import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

@Datagen
public class SoundDefinitionProvider extends SoundDefinitionsProvider {

    public SoundDefinitionProvider(ModX mod, DataGenerator generator, ExistingFileHelper helper) {
        super(generator, mod.modid, helper);
    }

    @Override
    public void registerSounds() {
        this.add(ModSounds.ghostHurt,
                definition().with(
                        sound(Boohoo.getInstance().resource("ghost/hurt0")).volume(1.3).pitch(0.3),
                        sound(Boohoo.getInstance().resource("ghost/hurt1")).pitch(0.8),
                        sound(Boohoo.getInstance().resource("ghost/hurt2")).pitch(0.8)
                ).subtitle("subtitle.boohoo.ghost.hurt"));
        this.add(ModSounds.ghostAmbient,
                definition().with(
                        sound(Boohoo.getInstance().resource("ghost/ambient0")).volume(0.3).pitch(0.8),
                        sound(Boohoo.getInstance().resource("ghost/ambient1")).volume(0.3)
                ).subtitle("subtitle.boohoo.ghost.ambient"));
        this.add(ModSounds.ghostTeleport,
                definition().with(
                        sound(Boohoo.getInstance().resource("ghost/teleport0")),
                        sound(Boohoo.getInstance().resource("ghost/teleport1"))
                ).subtitle("subtitle.boohoo.ghost.teleport"));
        this.add(ModSounds.ghostDeath,
                definition().with(
                        sound(Boohoo.getInstance().resource("ghost/death0")).volume(1.7),
                        sound(Boohoo.getInstance().resource("ghost/death1")).volume(1.7),
                        sound(Boohoo.getInstance().resource("ghost/death2")).volume(0.9)
                ).subtitle("subtitle.boohoo.ghost.death"));
    }
}
