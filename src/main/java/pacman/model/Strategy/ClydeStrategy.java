package pacman.model.Strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ClydeStrategy implements ChaseStrategy {
    //Distance for Ghost to go to corner
    private static final double MIN_DISTANCE = 8 * 16;
    private static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;

    //Movement Strategy for Clyde
    public Vector2D getTarget(Vector2D ghostPosition, Vector2D pacmanPosition, Direction currentDirection, Vector2D blinkyPosition) {
        //Get the distance between Pacman and Clyde based on straight line
        double distance = Vector2D.calculateEuclideanDistance(ghostPosition, pacmanPosition);
        //If the distance is more than 8 grid spaces away, target pacman
        if (distance > MIN_DISTANCE) { return stayInGrid(pacmanPosition); }
        //Else, go to the bottom left of the map
        else { return new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP); }
    }

    public Vector2D stayInGrid(Vector2D targetPosition) {
        double clampedX = Math.max(0, Math.min(targetPosition.getX(), 28 * 16));
        double clampedY = Math.max(0, Math.min(targetPosition.getY(), 36 * 16));
        return new Vector2D(clampedX, clampedY);
    }
}
