package org.cell2d.space;

import org.cell2d.CellVector;
import org.cell2d.Frac;

/**
 * <p>A CircleHitbox is a circular Hitbox with its origin at its center. A
 * CircleHitbox's radius cannot be negative.</p>
 * @author Alex Heyman
 */
public class CircleHitbox extends Hitbox {
    
    private long radius;
    
    /**
     * Constructs a CircleHitbox with the specified relative position and
     * radius.
     * @param relPosition This CircleHitbox's relative position
     * @param radius This CircleHitbox's radius
     */
    public CircleHitbox(CellVector relPosition, long radius) {
        this(relPosition.getX(), relPosition.getY(), radius);
        
    }
    
    /**
     * Constructs a CircleHitbox with the specified relative position and
     * radius.
     * @param relX The x-coordinate of this CircleHitbox's relative position
     * @param relY The y-coordinate of this CircleHitbox's relative position
     * @param radius This CircleHitbox's radius
     */
    public CircleHitbox(long relX, long relY, long radius) {
        super(relX, relY);
        if (!setRadius(radius)) {
            throw new RuntimeException("Attempted to give a CircleHitbox a negative radius (about "
                    + Frac.toDouble(radius) + " fracunits)");
        }
    }
    
    @Override
    public final CircleHitbox getCopy() {
        return new CircleHitbox(0, 0, radius);
    }
    
    /**
     * Returns this CircleHitbox's radius.
     * @return This CircleHitbox's radius
     */
    public final long getRadius() {
        return radius;
    }
    
    /**
     * Sets this CircleHitbox's radius to the specified value.
     * @param radius The new radius
     * @return Whether the new radius was valid and the change was successful
     */
    public final boolean setRadius(long radius) {
        if (radius >= 0) {
            this.radius = radius;
            updateBoundaries();
            return true;
        }
        return false;
    }
    
    @Override
    public final long getLeftEdge() {
        return getAbsX() - radius;
    }
    
    @Override
    public final long getRightEdge() {
        return getAbsX() + radius;
    }
    
    @Override
    public final long getTopEdge() {
        return getAbsY() - radius;
    }
    
    @Override
    public final long getBottomEdge() {
        return getAbsY() + radius;
    }
    
}
