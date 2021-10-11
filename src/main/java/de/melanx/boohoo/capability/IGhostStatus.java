package de.melanx.boohoo.capability;

import de.melanx.boohoo.ghost.Ghost;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface IGhostStatus {

    /**
     * @return The id of the ghost of the player.
     */
    @Nullable
    UUID getGhostId();

    /**
     * @param id The id of the ghost of the player.
     */
    void setGhostId(UUID id);

    /**
     * @return The serialized entity data to respawn the ghost.
     */
    @Nonnull
    CompoundTag getGhostData();

    /**
     * Sets the ghost entity data. Will be used for when the player logs out, that the ghost can be respawned.
     */
    void setGhostData(@Nullable Ghost ghost);

    /**
     * Sets the ghost entity data. Will be used for when the player logs out, that the ghost can be respawned.
     */
    void setGhostData(@Nullable CompoundTag tag);

    /**
     * @return Whether the player is hunted by a ghost at the moment.
     */
    boolean isGhosted();

    /**
     * Sets whether the player is hunted by a ghost at the moment.
     */
    void setGhosted(boolean ghosted);

    /**
     * @return The damage the player made to other entities.
     */
    double getDealtDamage();

    /**
     * Adds damage to the dealt damage counter.
     */
    void addDealtDamage(double damage);

    /**
     * Sets the damage for the dealt damage counter.
     */
    void setDealtDamage(double damage);

    /**
     * @return The damage the player got from other entities.
     */
    double getTakenDamage();

    /**
     * Adds damage to the taken damage counter.
     */
    void addTakenDamage(double damage);

    /**
     * Sets the damage for the taken damage counter.
     */
    void setTakenDamage(double damage);

    /**
     * @return The last day the player played. Used to check whether the data should be reset.
     */
    long getLastDay();

    /**
     * Sets the last day the player played.
     */
    void setLastDay(long day);

    /**
     * Resets the dealt and taken damage counter.
     */
    default void reset() {
        this.setDealtDamage(0);
        this.setTakenDamage(0);
    }

    /**
     * Resets the whole data except of the last day.
     */
    default void invalidate() {
        this.setGhostId(null);
        this.setGhosted(false);
        this.setGhostData(new CompoundTag());
        this.setDealtDamage(0);
        this.setTakenDamage(0);
    }

    /**
     * Copies the old data to the new instance. Used when respawning.
     *
     * @param old The data of the old player.
     */
    default void fromOld(IGhostStatus old) {
        this.setGhostId(old.getGhostId());
        this.setGhosted(old.isGhosted());
        this.setGhostData(old.getGhostData());
        this.setDealtDamage(old.getDealtDamage());
        this.setTakenDamage(old.getDealtDamage());
        this.setLastDay(old.getLastDay());
    }
}
