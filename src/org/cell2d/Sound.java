package org.cell2d;

import java.io.IOException;

/**
 * <p>A Sound is a sound effect. Like other Loadables, Sounds can be manually
 * loaded and unloaded into and out of memory. Loading may take a moment, but
 * while a Sound is not loaded, it cannot play.</p>
 * 
 * <p>Sounds can be played at different speeds from 0 up, with a speed of 0
 * making the Sound inaudible and a speed of 1 causing no speed change. They can
 * also be played at different volumes between 0 and 1, with a volume of 0
 * making the Sound inaudible and a volume of 1 causing no volume change.
 * Finally, the Sound class has a global volume between 0 and 1, by which the
 * effective volumes of all newly played Sounds are scaled. The global volume is
 * 1 by default.</p>
 * @author Alex Heyman
 */
public class Sound implements Loadable {
    
    private static double globalVolume = 1;
    
    /**
     * Returns the global sound volume.
     * @return The global sound volume
     */
    public static double getGlobalVolume() {
        return globalVolume;
    }
    
    /**
     * Sets the global sound volume to the specified value.
     * @param volume The new global sound volume
     */
    public static void setGlobalVolume(double volume) {
        globalVolume = Math.min(Math.max(volume, 0), 1);
    }
    
    private boolean loaded = false;
    private final String path;
    private Audio audio = null;
    
    /**
     * Constructs a Sound from an audio file. Files of WAV and OGG formats are
     * supported.
     * @param path The relative path to the audio file
     * @param load Whether this Sound should load upon creation
     */
    public Sound(String path, boolean load) {
        this.path = path;
        if (load) {
            load();
        }
    }
    
    @Override
    public final boolean isLoaded() {
        return loaded;
    }
    
    @Override
    public final boolean load() {
        if (!loaded) {
            loaded = true;
            try {
                audio = new Audio(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public final boolean unload() {
        if (loaded) {
            loaded = false;
            audio.unload();
            audio = null;
            return true;
        }
        return false;
    }
    
    /**
     * Returns whether this Sound is currently playing.
     * @return Whether this Sound is currently playing
     */
    public final boolean isPlaying() {
        return loaded && audio.isPlaying();
    }
    
    /**
     * Plays this Sound.
     * @param loop If true, this Sound will loop indefinitely until stopped;
     * otherwise, it will play once
     */
    public final void play(boolean loop) {
        play(1, 1, loop);
    }
    
    /**
     * Plays this Sound at the specified speed and volume.
     * @param speed The speed at which to play this Sound
     * @param volume The volume at which to play this Sound
     * @param loop If true, this Sound will loop indefinitely until stopped;
     * otherwise, it will play once
     */
    public final void play(double speed, double volume, boolean loop) {
        if (loaded) {
            audio.play(Math.max(speed, 0), Math.min(Math.max(volume, 0), 1)*globalVolume, loop);
        }
    }
    
    /**
     * Plays this Sound once.
     */
    public final void play() {
        play(1, 1, false);
    }
    
    /**
     * Plays this Sound once at the specified speed and volume.
     * @param speed The speed at which to play this Sound
     * @param volume The volume at which to play this Sound
     */
    public final void play(double speed, double volume) {
        play(speed, volume, false);
    }
    
    /**
     * Stops this Sound.
     */
    public final void stop() {
        if (loaded) {
            audio.stop();
        }
    }
    
}
