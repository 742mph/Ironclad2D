package ironclad2D.level;

import ironclad2D.Animation;
import ironclad2D.IroncladGame;

public abstract class ThinkerObject extends AnimatedObject {
    
    private final LevelThinker thinker = new ObjectThinker();
    private Hitbox collisionHitbox;
    
    public ThinkerObject(Hitbox locatorHitbox, Hitbox overlapHitbox,
            Hitbox solidHitbox, Hitbox collisionHitbox, int drawLayer, Animation animation) {
        super(locatorHitbox, overlapHitbox, solidHitbox, drawLayer, animation);
        this.collisionHitbox = collisionHitbox;
    }
    
    @Override
    void addActions() {
        super.addActions();
        levelState.addThinker(thinker);
    }
    
    @Override
    void removeActions() {
        super.removeActions();
        levelState.removeThinker(thinker);
    }
    
    @Override
    void setTimeFactorActions(double timeFactor) {
        super.setTimeFactorActions(timeFactor);
        thinker.setTimeFactor(timeFactor);
    }
    
    @Override
    void removeNonLocatorHitboxes(Hitbox locatorHitbox) {
        super.removeNonLocatorHitboxes(locatorHitbox);
        locatorHitbox.removeChild(collisionHitbox);
    }
    
    @Override
    void addNonLocatorHitboxes(Hitbox locatorHitbox) {
        super.addNonLocatorHitboxes(locatorHitbox);
        locatorHitbox.addChild(collisionHitbox);
    }
    
    public final LevelThinker getThinker() {
        return thinker;
    }
    
    public final Hitbox getCollisionHitbox() {
        return collisionHitbox;
    }
    
    public void timeUnitActions(IroncladGame game, LevelState levelState) {}
    
    public final int getTimerValue(String name) {
        return thinker.getTimerValue(name);
    }
    
    public final void setTimerValue(String name, int value) {
        thinker.setTimerValue(name, value);
    }
    
    public void timedEventActions(IroncladGame game, LevelState levelState, String eventName) {}
    
    public void stepActions(IroncladGame game, LevelState levelState) {}
    
    public void reactToLevel(IroncladGame game, LevelState levelState) {}
    
    public void reactToInput(IroncladGame game, LevelState levelState) {}
    
    public void addedActions(IroncladGame game, LevelState levelState) {}
    
    public void removedActions(IroncladGame game, LevelState levelState) {}
    
    private class ObjectThinker extends LevelThinker {
        
        private ObjectThinker() {}
        
        @Override
        public final void timeUnitActions(IroncladGame game, LevelState levelState) {
            ThinkerObject.this.timeUnitActions(game, levelState);
        }
        
        @Override
        public final void timedEventActions(IroncladGame game, LevelState levelState, String eventName) {
            ThinkerObject.this.timedEventActions(game, levelState, eventName);
        }
        
        @Override
        public final void stepActions(IroncladGame game, LevelState levelState) {
            ThinkerObject.this.stepActions(game, levelState);
        }
        
        @Override
        public final void reactToLevel(IroncladGame game, LevelState levelState) {
            ThinkerObject.this.reactToLevel(game, levelState);
        }
        
        @Override
        public final void reactToInput(IroncladGame game, LevelState levelState) {
            ThinkerObject.this.reactToInput(game, levelState);
        }
        
        @Override
        public final void addedActions(IroncladGame game, LevelState levelState) {
            ThinkerObject.this.addedActions(game, levelState);
        }
        
        @Override
        public final void removedActions(IroncladGame game, LevelState levelState) {
            ThinkerObject.this.removedActions(game, levelState);
        }
        
    }
    
}
