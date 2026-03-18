package pacman.model.GhostState;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Controllable;

//Concrete chase state
public class ChaseState implements GhostState {
    private Ghost ghost;
    private int duration;
    private double speed;
    private int frameElapsed = 0;
    private Controllable pacman;

    public ChaseState(Ghost ghost, Controllable pacman) {
        this.ghost = ghost;
        this.pacman = pacman;
    }
    public Vector2D getTarget() {
        frameElapsed += 1;
        //If the duration of this mode is up, transition to the chase state
        if (frameElapsed / 30 > duration) {
            frameElapsed = 0;
            ghost.setState(ghost.getScatterState());
        }
        Vector2D blinkyPosition;
        //If the ghost is not a bashful/inky, then it will not have the reference to the Blinky ghost
        if (ghost.getBlinky()!= null) {
            blinkyPosition = ghost.getBlinky().getPosition();
        } else {
            blinkyPosition = null;
        }
        //Gets the chase target location using the chase strategy for the ghost
        return ghost.getChaseStrategy().getTarget(ghost.getPosition(), pacman.getPosition(), pacman.getDirection(), blinkyPosition);
    }

    public void setDuration(int duration) {this.duration = duration;}

    public void setSpeed(double speed) {this.speed = speed;}

    public double getSpeed() {return speed;}

    public void resetTime() {frameElapsed = 0;}

}
