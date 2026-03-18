package pacman.model.entity.dynamic.ghost;

import pacman.model.GhostState.ChaseState;
import pacman.model.GhostState.GhostState;
import pacman.model.Strategy.ChaseStrategy;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;

import java.util.Map;

/**
 * Represents Ghost entity in Pac-Man Game
 */
public interface Ghost extends DynamicEntity, PlayerPositionObserver {

    /**
     * Sets the mode of the Ghost used to calculate target position
     *
     * @param ghostMode mode of the Ghost
     */

    //Gets the chase strategy for the movement
    public ChaseStrategy getChaseStrategy();

    public void setState(GhostState state);

    public void setChaseState(Controllable pacman);

    public void setFrightenedState(Controllable pacman);


    public GhostState getChaseState();

    public GhostState getScatterState();

    public GhostState getFrightenedState();

    public Vector2D getTargetCorner();

    public void setStateDuration(Map<GhostMode, Integer> lengths);

    public void setStateSpeed(Map<GhostMode, Double> lengths);

    public void resetMultiplier();

    void setBlinky(Ghost ghost);

    public char getType();

    public Ghost getBlinky();



}
