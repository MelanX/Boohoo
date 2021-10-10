package de.melanx.boohoo.registration;

import de.melanx.boohoo.Boohoo;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.sounds.SoundEvent;

@RegisterClass
public class ModSounds {

    public static final SoundEvent ghostHurt = new SoundEvent(Boohoo.getInstance().resource("ghost.hurt"));
    public static final SoundEvent ghostAmbient = new SoundEvent(Boohoo.getInstance().resource("ghost.ambient"));
    public static final SoundEvent ghostTeleport = new SoundEvent(Boohoo.getInstance().resource("ghost.teleport"));
    public static final SoundEvent ghostDeath = new SoundEvent(Boohoo.getInstance().resource("ghost.death"));
}
