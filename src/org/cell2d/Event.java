package org.cell2d;

/**
 * <p>An Event represents a sequence of actions that can be taken as part of the
 * mechanics of a GameState. It is useful to create an Event as a lambda
 * expression to simplify code.</p>
 * @see EventGroup
 * @see Thinker#setTimerValue(org.cell2d.Event, int)
 * @param <T> The type of CellGame that uses the GameStates that can involve
 * this Event
 * @param <U> The type of GameState that can involve this Event
 * @author Alex Heyman
 */
public interface Event<T extends CellGame, U extends GameState<T,U,?>> {
    
    /**
     * Actions that this Event consists in taking.
     * @param game The CellGame of the GameState that this Event is involved in
     * @param state The GameState that this Event is involved in
     */
    void actions(T game, U state);
    
}
