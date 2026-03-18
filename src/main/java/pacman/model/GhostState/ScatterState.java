package pacman.model.GhostState;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;

//Concrete scatter state
public class ScatterState implements GhostState {
    private Ghost ghost;
    private int duration;
    private int frameElapsed = 0;
    private double speed;
    public ScatterState(Ghost ghost) {
        this.ghost = ghost;
    }

    public Vector2D getTarget() {
        frameElapsed += 1;
        //If the duration of this mode is up, transition to the chase state
        if (frameElapsed / 30 > duration) {
            frameElapsed = 0;
            ghost.setState(ghost.getChaseState());
        }
        //Returns the target vector to the defined corner
        return ghost.getTargetCorner();
    }

    public void setDuration(int duration) {this.duration = duration;}

    public void setSpeed(double speed) {this.speed = speed;}

    public double getSpeed() {return speed;}

    public void resetTime() {frameElapsed = 0;}

}
