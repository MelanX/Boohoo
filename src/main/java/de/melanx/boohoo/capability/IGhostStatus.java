package de.melanx.boohoo.capability;

import de.melanx.boohoo.ghost.Ghost;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface IGhostStatus {
    @Nullable
    UUID getGhostId();

    void setGhostId(UUID id);

    @Nonnull
    CompoundTag getGhostData();

    void setGhostData(@Nullable Ghost ghost);

    void setGhostData(@Nullable CompoundTag tag);

    boolean isGhosted();

    void setGhosted(boolean ghosted);

    double getDealtDamage();

    void addDealtDamage(double damage);

    void setDealtDamage(double damage);

    double getTakenDamage();

    void addTakenDamage(double damage);

    void setTakenDamage(double damage);

    long getLastDay();

    void setLastDay(long day);

    default void reset() {
        this.setDealtDamage(0);
        this.setTakenDamage(0);
    }

    default void invalidate() {
        this.setGhostId(null);
        this.setGhosted(false);
        this.setGhostData(new CompoundTag());
        this.setDealtDamage(0);
        this.setTakenDamage(0);
    }

    default void fromOld(IGhostStatus old) {
        this.setGhostId(old.getGhostId());
        this.setGhosted(old.isGhosted());
        this.setGhostData(old.getGhostData());
        this.setDealtDamage(old.getDealtDamage());
        this.setTakenDamage(old.getDealtDamage());
        this.setLastDay(old.getLastDay());
    }
}
