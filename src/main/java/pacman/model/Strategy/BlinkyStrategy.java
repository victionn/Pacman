package pacman.model.Strategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class BlinkyStrategy implements ChaseStrategy {
    //Concrete movement strategy for Blinky
    public Vector2D getTarget(Vector2D ghostPosition, Vector2D pacManPosition, Direction currentDirection, Vector2D blinkyPosition) {
        //Return Pacmans position
        return stayInGrid(pacManPosition);
    }

    public Vector2D stayInGrid(Vector2D targetPosition) {
        double clampedX = Math.max(0, Math.min(targetPosition.getX(), 28 * 16));
        double clampedY = Math.max(0, Math.min(targetPosition.getY(), 36 * 16));
        return new Vector2D(clampedX, clampedY);
    }
}
