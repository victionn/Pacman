package pacman.model.Strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class InkyStrategy implements ChaseStrategy {

    public Vector2D getTarget(Vector2D ghostPosition, Vector2D pacManPosition, Direction currentDirection, Vector2D blinkyPosition) {
        //Gets the target two grids in front Pacman
        Vector2D twoPositionsAhead = getTwoPositionPacman(pacManPosition, currentDirection);
        //Gets the vector two grinds in front of pacman to blinky
        Vector2D twoPositionsAheadToBlinky = twoPositionsAhead.subtract(blinkyPosition);
        //Multiplies the vector by 2
        return stayInGrid(blinkyPosition.add(new Vector2D(twoPositionsAheadToBlinky.getX() * 2, twoPositionsAheadToBlinky.getY() * 2)));
    }

    private Vector2D getTwoPositionPacman(Vector2D pacManPosition, Direction currentDirection) {
        //Returns the position two grids in front of Pacman
        return switch (currentDirection) {
            case UP -> stayInGrid(new Vector2D(pacManPosition.getX(), pacManPosition.getY() - 2 * 16));
            case DOWN -> stayInGrid(new Vector2D(pacManPosition.getX(), pacManPosition.getY() + 2 * 16));
            case LEFT -> stayInGrid(new Vector2D(pacManPosition.getX() - 2 * 16, pacManPosition.getY()));
            case RIGHT -> stayInGrid(new Vector2D(pacManPosition.getX() + 2 * 16, pacManPosition.getY()));
            default -> pacManPosition;
        };
    }
    public Vector2D stayInGrid(Vector2D targetPosition) {
        double clampedX = Math.max(0, Math.min(targetPosition.getX(), 28 * 16));
        double clampedY = Math.max(0, Math.min(targetPosition.getY(), 36 * 16));
        return new Vector2D(clampedX, clampedY);
    }

}
