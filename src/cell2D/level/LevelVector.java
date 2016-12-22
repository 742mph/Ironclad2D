package cell2D.level;

import org.newdawn.slick.util.FastTrig;

public class LevelVector {
    
    private double x, y, lengthSquared, length, angle, angleX, angleY;
    
    public LevelVector() {
        clear();
    }
    
    public LevelVector(double x, double y) {
        setCoordinates(x, y);
    }
    
    public LevelVector(double angle) {
        this.angle = modAngle(angle);
        double radians = Math.toRadians(angle);
        angleX = FastTrig.cos(radians);
        angleY = -FastTrig.sin(radians);
        x = angleX;
        y = angleY;
        lengthSquared = 1;
        length = 1;
    }
    
    public LevelVector(LevelVector vector) {
        copy(vector);
    }
    
    public final LevelVector clear() {
        x = 0;
        y = 0;
        lengthSquared = 0;
        length = 0;
        angle = 0;
        angleX = 1;
        angleY = 0;
        return this;
    }
    
    public final LevelVector copy(LevelVector vector) {
        x = vector.x;
        y = vector.y;
        lengthSquared = vector.lengthSquared;
        length = vector.length;
        angle = vector.angle;
        angleX = vector.angleX;
        angleY = vector.angleY;
        return this;
    }
    
    public final double getX() {
        return x;
    }
    
    public final LevelVector setX(double x) {
        this.x = x;
        return updateToMatchCoordinates();
    }
    
    public final double getY() {
        return y;
    }
    
    public final LevelVector setY(double y) {
        this.y = y;
        return updateToMatchCoordinates();
    }
    
    public final LevelVector setCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
        return updateToMatchCoordinates();
    }
    
    private LevelVector updateToMatchCoordinates() {
        lengthSquared = x*x + y*y;
        length = Math.sqrt(lengthSquared);
        double radians = Math.atan2(-y, x);
        angle = modAngle(Math.toDegrees(radians));
        angleX = FastTrig.cos(radians);
        angleY = -FastTrig.sin(radians);
        return this;
    }
    
    public final LevelVector flip() {
        return flip(true, true);
    }
    
    public final LevelVector flipX() {
        return flip(true, false);
    }
    
    public final LevelVector flipY() {
        return flip(false, true);
    }
    
    public final LevelVector flip(boolean xFlip, boolean yFlip) {
        if (xFlip) {
            x = -x;
            angle = modAngle(180 - angle);
            angleX = -angleX;
        }
        if (yFlip) {
            y = -y;
            angle = modAngle(360 - angle);
            angleY = -angleY;
        }
        return this;
    }
    
    public final double getLengthSquared() {
        return lengthSquared;
    }
    
    public final double getLength() {
        return length;
    }
    
    public final LevelVector setLength(double length) {
        if (length < 0) {
            flip();
            length = -length;
        }
        x = length*angleX;
        y = length*angleY;
        this.length = length;
        lengthSquared = length*length;
        return this;
    }
    
    public final LevelVector scale(double scaleFactor) {
        return setLength(length*scaleFactor);
    }
    
    private double modAngle(double angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }
    
    public final double getAngle() {
        return angle;
    }
    
    public final double getAngleX() {
        return angleX;
    }
    
    public final double getAngleY() {
        return angleY;
    }
    
    public final LevelVector setAngle(double angle) {
        this.angle = modAngle(angle);
        double radians = Math.toRadians(angle);
        angleX = FastTrig.cos(radians);
        angleY = -FastTrig.sin(radians);
        x = length*angleX;
        y = length*angleY;
        return this;
    }
    
    public final LevelVector changeAngle(double angle) {
        return setAngle(this.angle + angle);
    }
    
    public final LevelVector add(LevelVector vector) {
        return setCoordinates(x + vector.x, y + vector.y);
    }
    
    public final LevelVector add(double x, double y) {
        return setCoordinates(this.x + x, this.y + y);
    }
    
    public static final LevelVector add(LevelVector first, LevelVector second) {
        return new LevelVector(first.x + second.x, first.y + second.y);
    }
    
    public final LevelVector sub(LevelVector vector) {
        return setCoordinates(x - vector.x, y - vector.y);
    }
    
    public final LevelVector sub(double x, double y) {
        return setCoordinates(this.x - x, this.y - y);
    }
    
    public static final LevelVector sub(LevelVector first, LevelVector second) {
        return new LevelVector(first.x - second.x, first.y - second.y);
    }
    
    public final double dotProduct(LevelVector vector) {
        return x*vector.x + y*vector.y;
    }
    
    public static final double dotProduct(LevelVector first, LevelVector second) {
        return first.x*second.x + first.y*second.y;
    }
    
    public final LevelVector relativeTo(Hitbox hitbox) {
        return flip(hitbox.getAbsXFlip(), hitbox.getAbsYFlip()).changeAngle(hitbox.getAbsAngle());
    }
    
    public final LevelVector relativeTo(LevelObject object) {
        return relativeTo(object.getLocatorHitbox());
    }
    
}