package pacman.model.Strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class SpeedyStrategy implements ChaseStrategy{
    public static final int PIXELS_AHEAD = 4 * 16;
    //Movement strategy for Pinky
    public Vector2D getTarget(Vector2D ghostPosition, Vector2D pacmanPosition, Direction currentDirection, Vector2D blinkyPosition) {
        //Depending on PacMan direction, target four grid spaces ahead of Pacman
        return switch (currentDirection) {
            case UP -> stayInGrid(pacmanPosition.add(new Vector2D(0, -PIXELS_AHEAD)));
            case DOWN -> stayInGrid(pacmanPosition.add(new Vector2D(0, PIXELS_AHEAD)));
            case LEFT -> stayInGrid(pacmanPosition.add(new Vector2D(-PIXELS_AHEAD, 0)));
            case RIGHT -> stayInGrid(pacmanPosition.add(new Vector2D(PIXELS_AHEAD, 0)));
        };
    }
    public Vector2D stayInGrid(Vector2D targetPosition) {
        double clampedX = Math.max(0, Math.min(targetPosition.getX(), 28 * 16));
        double clampedY = Math.max(0, Math.min(targetPosition.getY(), 36 * 16));
        return new Vector2D(clampedX, clampedY);
    }
}
