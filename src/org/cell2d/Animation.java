package org.cell2d;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>An Animation is a sequence of one or more Animatable <i>frames</i> that
 * may be instantiated in an AnimationInstance in order to be displayed one
 * after another and/or smoothly transitioned between. The frames are indexed by
 * the integers from 0 to getNumFrames() - 1 inclusive, and each has its own
 * duration in fracunits. Durations of 0 or less are interpreted as infinite.
 * </p>
 * 
 * <p>The frames of an Animation may be other Animations in addition to single
 * Sprites, which allows for the creation of multi-dimensional Animations in a
 * similar manner to multi-dimensional arrays.</p>
 * 
 * <p>In order to be of use, Animations need not represent linear progressions
 * of frames; they may also be collections of counterpart Animations that need
 * to be switched between without an AnimationInstance losing its place in them.
 * </p>
 * 
 * <p>Once created, Animations are static and immutable, with all movement
 * through time happening in AnimationInstances.</p>
 * 
 * <p>All of Animation's constructors treat a null Animatable as equivalent to
 * Sprite.BLANK, and thus no frame of an Animation may be null.</p>
 * @see Animatable
 * @see Sprite
 * @author Alex Heyman
 */
public class Animation implements Animatable {
    
    /**
     * A blank Animation with Sprite.BLANK, duration 0, as its only frame.
     */
    public static final Animation BLANK = new Animation();
    
    private final Animatable[] frames;
    private final long[] frameDurations;
    private final boolean[][] compatibilities;
    private final int level;
    
    private Animation() {
        this.frames = new Animatable[1];
        this.frames[0] = Sprite.BLANK;
        this.frameDurations = new long[1];
        this.frameDurations[0] = 0;
        compatibilities = new boolean[1][1];
        compatibilities[0][0] = true;
        level = 1;
    }
    
    private static Animatable[] arrayOf(Animatable animatable) {
        Animatable[] array = new Animatable[1];
        array[0] = animatable;
        return array;
    }
    
    private static long[] arrayOfFracunits(int length) {
        long[] array = new long[length];
        Arrays.fill(array, Frac.UNIT);
        return array;
    }
    
    /**
     * Constructs an Animation with the specified Animatable object, duration 0,
     * as its only frame.
     * @param frame The Animatable object out of which to make the Animation
     */
    public Animation(Animatable frame) {
        this(arrayOf(frame), 0);
    }
    
    /**
     * Constructs an Animation with the specified sequence of Animatable objects
     * as its frames. Each frame will have a duration of one fracunit.
     * @param frames The sequence of the Animation's frames
     */
    public Animation(Animatable... frames) {
        this(frames, arrayOfFracunits(frames.length));
    }
    
    /**
     * Constructs an Animation out of the Animatable objects in the specified
     * array of frames. Each frame will have a duration that is the value at its
     * corresponding index in the sequence of frame durations.
     * @param frames The array of the Animation's frames
     * @param frameDurations The sequence of the Animation's frame durations in
     * fracunits
     */
    public Animation(Animatable[] frames, long... frameDurations) {
        if (frames.length == 0) {
            throw new RuntimeException("Attempted to construct an empty Animation");
        }
        if (frames.length != frameDurations.length) {
            throw new RuntimeException("Attempted to construct an Animation with " + frames.length
                    + " frames, but " + frameDurations.length + " frame durations");
        }
        this.frames = frames.clone();
        for (int i = 0; i < this.frames.length; i++) {
            if (this.frames[i] == null) {
                this.frames[i] = Sprite.BLANK;
            }
        }
        this.frameDurations = frameDurations.clone();
        compatibilities = new boolean[this.frames.length][0];
        for (int i = this.frames.length - 1; i >= 0; i--) {
            compatibilities[i] = new boolean[i + 1];
            compatibilities[i][i] = true;
            for (int j = 0; j < i; j++) {
                compatibilities[i][j] = checkCompatibility(this.frames[i], this.frames[j]);
            }
        }
        int maxLevel = 0;
        for (Animatable frame : this.frames) {
            int frameLevel = frame.getLevel();
            if (frameLevel > maxLevel) {
                maxLevel = frameLevel;
            }
        }
        level = maxLevel + 1;
    }
    
    private static Animatable[] spriteSheetToFrames(
            SpriteSheet spriteSheet, int x1, int y1, int x2, int y2, boolean columns) {
        if (x1 < 0 || x2 >= spriteSheet.getWidth() || y1 < 0 || y2 >= spriteSheet.getHeight()
                || x2 < x1 || y2 < y1) {
            throw new RuntimeException("Attempted to construct an Animation from a SpriteSheet region"
                    + " defined by invalid coordinates (" + x1 + ", " + y1 + "), (" + x2 + ", " + y2 + ")");
        }
        Animatable[] frames = new Animatable[(x2 - x1 + 1)*(y2 - y1 + 1)];
        int i = 0;
        if (columns) {
            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    frames[i] = spriteSheet.getSprite(x, y);
                    i++;
                }
            }
        } else {
            for (int y = y1; y <= y2; y++) {
                for (int x = x1; x <= x2; x++) {
                    frames[i] = spriteSheet.getSprite(x, y);
                    i++;
                }
            }
        }
        return frames;
    }
    
    /**
     * Constructs an Animation with the Sprites in a rectangular region of a
     * SpriteSheet as its frames. Each frame will have a duration of one
     * fracunit.
     * @param spriteSheet The SpriteSheet out of which to make the Animation
     * @param x1 The x-coordinate, in Sprites, of the region's left edge
     * @param y1 The y-coordinate, in Sprites, of the region's top edge
     * @param x2 The x-coordinate, in Sprites, of the region's right edge
     * @param y2 The y-coordinate, in Sprites, of the region's bottom edge
     * @param columns If true, the Sprites will be read from the SpriteSheet in
     * columns from top to bottom, the columns going from left to right.
     * Otherwise, they will be read in rows from left to right, the rows going
     * from top to bottom. The Sprites will appear in the Animation in the order
     * in which they are read.
     */
    public Animation(SpriteSheet spriteSheet, int x1, int y1, int x2, int y2, boolean columns) {
        this(spriteSheetToFrames(spriteSheet, x1, y1, x2, y2, columns));
    }
    
    /**
     * Constructs an Animation with the Sprites in a rectangular region of a
     * SpriteSheet as its frames. Each frame will have a duration that is the
     * value at its corresponding index in the sequence of frame durations.
     * @param spriteSheet The SpriteSheet out of which to make the Animation
     * @param x1 The x-coordinate, in Sprites, of the region's left edge
     * @param y1 The y-coordinate, in Sprites, of the region's top edge
     * @param x2 The x-coordinate, in Sprites, of the region's right edge
     * @param y2 The y-coordinate, in Sprites, of the region's bottom edge
     * @param columns If true, the Sprites will be read from the SpriteSheet in
     * columns from top to bottom, the columns going from left to right.
     * Otherwise, they will be read in rows from left to right, the rows going
     * from top to bottom. The Sprites will appear in the Animation in the order
     * in which they are read.
     * @param frameDurations The sequence of the Animation's frame durations
     */
    public Animation(SpriteSheet spriteSheet, int x1, int y1, int x2, int y2, boolean columns,
            long... frameDurations) {
        this(spriteSheetToFrames(spriteSheet, x1, y1, x2, y2, columns), frameDurations);
    }
    
    private boolean checkCompatibility(Animatable frame1, Animatable frame2) {
        if (frame1.getLevel() == 0 && frame2.getLevel() == 0) {
            return true;
        } else if (frame1.getLevel() != frame2.getLevel()
                || frame1.getNumFrames() != frame2.getNumFrames()) {
            return false;
        }
        for (int i = 0; i < frame1.getNumFrames(); i++) {
            if (frame1.getFrameDuration(i) != frame2.getFrameDuration(i)
                    || !checkCompatibility(frame1.getFrame(i), frame2.getFrame(i))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public final int getLevel() {
        return level;
    }
    
    @Override
    public final int getNumFrames() {
        return frames.length;
    }
    
    @Override
    public final Animatable getFrame(int index) {
        if (index < 0 || index >= frames.length) {
            throw new IndexOutOfBoundsException("Attempted to get an Animatable's frame at invalid index "
                    + index);
        }
        return frames[index];
    }
    
    @Override
    public final long getFrameDuration(int index) {
        if (index < 0 || index >= frameDurations.length) {
            throw new IndexOutOfBoundsException("Attempted to get an Animatable's frame duration at invalid"
                    + " index " + index);
        }
        return frameDurations[index];
    }
    
    @Override
    public final boolean framesAreCompatible(int index1, int index2) {
        if (index1 < 0 || index1 >= frames.length
                || index2 < 0 || index2 >= frames.length) {
            throw new IndexOutOfBoundsException("Attempted to get an Animatable's frame compatibility at"
                    + " invalid pair of indices (" + index1 + ", " + index2 + ")");
        }
        if (index2 > index1) {
            return compatibilities[index2][index1];
        }
        return compatibilities[index1][index2];
    }
    
    private void addSpritesToSet(Set<Sprite> sprites) {
        for (int i = 0; i < frames.length; i++) {
            Animatable frame = frames[i];
            if (frame instanceof Sprite) {
                sprites.add((Sprite)frame);
            } else {
                ((Animation)frame).addSpritesToSet(sprites);
            }
        }
    }
    
    @Override
    public final Set<Sprite> getSprites() {
        Set<Sprite> sprites = new HashSet<>();
        addSpritesToSet(sprites);
        return Collections.unmodifiableSet(sprites);
    }
    
    @Override
    public final Animation getFilteredCopy(Filter filter, boolean load) {
        Animatable[] copyFrames = new Animatable[frames.length];
        for (int i = 0; i < frames.length; i++) {
            copyFrames[i] = frames[i].getFilteredCopy(filter, load);
        }
        return new Animation(copyFrames, frameDurations);
    }
    
    /**
     * Returns a Drawable instantiation of this Animation - that is, a new
     * AnimationInstance of this Animation, with its indices and speeds at each
     * level starting at 0.
     * @return A Drawable instantiation of this Animation
     */
    @Override
    public final Drawable getInstance() {
        return new AnimationInstance(this);
    }
    
}
