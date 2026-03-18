package pacman.model.GhostState;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;

//Defines the states of ghost for concrete ghost classes like frightened, chase and scatter to implement
public interface GhostState {
    public Vector2D getTarget();

    public void setDuration(int duration);

    public void setSpeed(double speed);

    public double getSpeed();

    public void resetTime();
}
