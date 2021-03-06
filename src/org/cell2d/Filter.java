package org.cell2d;

import org.cell2d.celick.Image;

/**
 * <p>A Filter represents an operation that transforms an image, such as
 * replacing some of its colors with others. A Filter can be applied to a Sprite
 * or SpriteSheet to create a new Sprite or SpriteSheet, or it can be included
 * in a set of Filters that is provided to a Sprite or SpriteSheet upon its
 * creation, allowing that Sprite or that SpriteSheet's Sprites to be drawn to
 * a Graphics context using that Filter.</p>
 * @see Sprite
 * @see SpriteSheet
 * @author Alex Heyman
 */
public interface Filter {
    
    /**
     * Returns the transformation through this Filter of the specified
     * <a href="https://cell2d.gitbook.io/cell2d-documentation/general/celick">Celick</a>
     * image.
     * @param image The Image to be transformed
     * @return The transformation of the specified Image
     */
    Image getFilteredImage(Image image);
    
}
