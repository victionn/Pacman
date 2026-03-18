package pacman.model.GhostState;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;

import java.util.Random;

//Concrete frightened state
public class FrightenedState implements GhostState {
    public Ghost ghost;
    public int duration;
    private Controllable pacman;
    private double speed;
    private static final int RIGHT_X_POSITION_OF_MAP = 448;
    private static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
    private static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;
    private final Random random = new Random();
    public int frameElapsed = 0;

    public  FrightenedState(Ghost ghost, Controllable pacman) {
        this.ghost = ghost;
        this.pacman = pacman;
    }
    public Vector2D getTarget() {
        frameElapsed += 1;
        //If the duration of this mode is up, transition to the chase state
        if (frameElapsed / 30 > duration + 1) {
            frameElapsed = 0;
            //Reset the multiplier for eating ghosts
            ghost.resetMultiplier();
            //Transition to the scatter state
            ghost.setState(ghost.getScatterState());
        }
        int randomX = random.nextInt(RIGHT_X_POSITION_OF_MAP);
        int randomY = TOP_Y_POSITION_OF_MAP + random.nextInt(BOTTOM_Y_POSITION_OF_MAP - TOP_Y_POSITION_OF_MAP);
        //Returns a random vector on the map. Movement implementation ensures ghost does not move opposite unless have to
        return new Vector2D(randomX, randomY);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void resetTime() {
        frameElapsed = 0;
    }

 }
