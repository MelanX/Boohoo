package de.melanx.boohoo.capability;

import de.melanx.boohoo.Boohoo;
import de.melanx.boohoo.ghost.Ghost;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class GhostCapability implements IGhostStatus, ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<IGhostStatus> INSTANCE = null;

    public final LazyOptional<IGhostStatus> holder = LazyOptional.of(() -> this);
    private UUID ghostId;
    private CompoundTag ghostData = new CompoundTag();
    private boolean isGhosted;

    @Nullable
    @Override
    public UUID getGhostId() {
        return this.ghostId;
    }

    @Override
    public void setGhostId(UUID id) {
        if (this.ghostId != null && id != null) {
            Boohoo.getInstance().logger.error("Replaced old id {} with new id {}", this.ghostId, id);
        }
        this.ghostId = id;
    }

    @Nonnull
    @Override
    public CompoundTag getGhostData() {
        return this.ghostData;
    }

    @Override
    public void setGhostData(Ghost ghost) {
        this.ghostData = ghost != null ? ghost.serializeNBT() : new CompoundTag();
    }

    @Override
    public void setGhostData(@Nullable CompoundTag tag) {
        this.ghostData = tag != null ? tag : new CompoundTag();
    }

    @Override
    public boolean isGhosted() {
        return this.isGhosted;
    }

    @Override
    public void setGhosted(boolean ghosted) {
        this.isGhosted = ghosted;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == INSTANCE) {
            return INSTANCE.orEmpty(cap, this.holder);
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (this.ghostId != null) {
            tag.putUUID("GhostId", this.ghostId);
        }

        tag.put("GhostData", this.ghostData);
        tag.putBoolean("IsGhosted", this.isGhosted);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.get("GhostId") != null) {
            this.ghostId = tag.getUUID("GhostId");
        }

        this.ghostData = tag.getCompound("GhostData");
        this.isGhosted = tag.getBoolean("IsGhosted");
    }
}
