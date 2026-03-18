package pacman.model.Strategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
//Strategy interface for different chase strategies, implemented by concrete strategies
public interface ChaseStrategy {
    //Gets the Vector2D position of the target location
    Vector2D getTarget(Vector2D ghostPosition, Vector2D pacManPosition, Direction currentDirection, Vector2D blinkyPosition);
    //Ensures the target location is within the map
    Vector2D stayInGrid(Vector2D targetPosition);

}
