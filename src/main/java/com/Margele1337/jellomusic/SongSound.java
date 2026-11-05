package com.margele1337.jellomusic;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

public class SongSound extends PositionedSound {

    public SongSound(String soundName) {
        super(new ResourceLocation(soundName));
        this.volume = Config.volume / 100F;
    }

    @Override
    public ISound.AttenuationType getAttenuationType() {
        return ISound.AttenuationType.NONE;
    }

    @Override
    public float getPitch() {
        return Config.pitch / 100F;
    }
}
