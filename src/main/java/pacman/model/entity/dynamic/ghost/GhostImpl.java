package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.GhostState.ChaseState;
import pacman.model.GhostState.FrightenedState;
import pacman.model.GhostState.GhostState;
import pacman.model.GhostState.ScatterState;
import pacman.model.Strategy.ChaseStrategy;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;

/**
 * Concrete implementation of Ghost entity in Pac-Man Game
 */
public class GhostImpl implements Ghost, Collectable {

    private static final int minimumDirectionCount = 8;
    private final Layer layer = Layer.FOREGROUND;
    private Image image;
    private Image normalImage;
    private Controllable pacman;
    private Ghost blinky;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private final Vector2D targetCorner;
    private KinematicState kinematicState;
    private GhostMode ghostMode;
    private Vector2D targetLocation;
    private Vector2D playerPosition;
    private Direction currentDirection;
    public boolean isCollectable;
    private Set<Direction> possibleDirections;
    private Map<GhostMode, Double> speeds;
    private GhostState frightenedState;
    private GhostState chaseState;
    private GhostState scatterState;
    private Image frightenedImage = new Image("maze/ghosts/frightened.png");
    private GhostState currentState;
    private int currentDirectionCount = 0;
    private ChaseStrategy chaseStrategy;
    private Vector2D queuedLocation;
    private char type;

    public GhostImpl(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, ChaseStrategy chaseStrategy, char type) {
        this.image = image;
        normalImage = image;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.ghostMode = ghostMode;
        this.type = type;
        this.possibleDirections = new HashSet<>();
        this.targetCorner = targetCorner;
        this.scatterState = new ScatterState(this);
        this.currentState = scatterState;
        this.targetLocation = getTargetLocation();
        this.currentDirection = null;
        this.chaseStrategy = chaseStrategy;
    }

    @Override
    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {this.image = image; }

    @Override
    public void update() {
        //Queue the target location for ghost to move to. This is called every update to ensure the duration of ghosts are consistent
        queuedLocation = getTargetLocation();
        this.updateDirection();
        this.kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());
    }

    private void updateDirection() {
        // Ghosts update their target location when they reach an intersection
        if (Maze.isAtIntersection(this.possibleDirections)) {
            this.targetLocation = queuedLocation;
        }

        Direction newDirection = selectDirection(possibleDirections);

        // Ghosts have to continue in a direction for a minimum time before changing direction
        if (this.currentDirection != newDirection) {
            this.currentDirectionCount = 0;
        }
        this.currentDirection = newDirection;

        switch (currentDirection) {
            case LEFT -> this.kinematicState.left();
            case RIGHT -> this.kinematicState.right();
            case UP -> this.kinematicState.up();
            case DOWN -> this.kinematicState.down();
        }
    }

    //Based on the state, get the target location for the ghost to move to
    private Vector2D getTargetLocation() {
        return currentState.getTarget();

    }

    private Direction selectDirection(Set<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        // ghosts have to continue in a direction for a minimum time before changing direction
        if (currentDirection != null && currentDirectionCount < minimumDirectionCount) {
            currentDirectionCount++;
            return currentDirection;
        }

        Map<Direction, Double> distances = new HashMap<>();

        for (Direction direction : possibleDirections) {
            // ghosts never choose to reverse travel
            if (currentDirection == null || direction != currentDirection.opposite()) {
                distances.put(direction, Vector2D.calculateEuclideanDistance(this.kinematicState.getPotentialPosition(direction), this.targetLocation));
            }
        }

        // only go the opposite way if trapped
        if (distances.isEmpty()) {
            return currentDirection.opposite();
        }

        // select the direction that will reach the target location fastest
        return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public void setState(GhostState state)
    {
        //Reset the duration count for the current state
        currentState.resetTime();
        //if the state is transitioning to Frightened
        if (state instanceof FrightenedState) {
            setImage(frightenedImage);
            setCollectable(true);
        }
        else {
            setImage(normalImage);
            setCollectable(false);
        }
        currentState = state;
        //Set the speed of the ghost to the current state
        this.kinematicState.setSpeed(currentState.getSpeed());
        this.currentDirectionCount = minimumDirectionCount;
    }

    //Sets the chase state for the ghost
    public void setChaseState(Controllable pacman) {chaseState = new ChaseState(this, pacman);}

    //Sets the frightened state for the ghost
    public void setFrightenedState(Controllable pacman) {
        frightenedState = new FrightenedState(this, pacman);
        this.pacman = pacman;
    }


    public GhostState getChaseState() {return chaseState;}

    public GhostState getScatterState() {return scatterState;}

    public GhostState getFrightenedState() {return frightenedState;}

    //Sets the duration for each state
    public void setStateDuration(Map<GhostMode, Integer> lengths) {
        scatterState.setDuration(lengths.get(GhostMode.SCATTER));
        chaseState.setDuration(lengths.get(GhostMode.CHASE));
        frightenedState.setDuration(lengths.get(GhostMode.FRIGHTENED));
    }

    //Sets the speed of each state
    public void setStateSpeed(Map<GhostMode, Double> lengths) {
        scatterState.setSpeed(lengths.get(GhostMode.SCATTER));
        chaseState.setSpeed(lengths.get(GhostMode.CHASE));
        frightenedState.setSpeed(lengths.get(GhostMode.FRIGHTENED));
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(), kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        //If ghost collides with Pacman
        if (level.isPlayer(renderable)) {
            //If ghost is in frightened state, call pacman to collect the ghost
            if (currentState instanceof FrightenedState) {
                ((Pacman)renderable).collectGhost(level, this);
                reset();
            //Otherwise, deduct life / end game
            } else {
                level.handleLoseLife();
            }
        }
    }

    //Set blinky ghost used for Bashful/Inky chase strategy
    public void setBlinky(Ghost ghost) {this.blinky = ghost;}


    @Override
    public void update(Vector2D playerPosition) {this.playerPosition = playerPosition;}

    @Override
    public Vector2D getPositionBeforeLastUpdate() {return this.kinematicState.getPreviousPosition();}

    @Override
    public double getHeight() {return this.boundingBox.getHeight();}

    @Override
    public double getWidth() {return this.boundingBox.getWidth();}

    @Override
    public Vector2D getPosition() {return this.kinematicState.getPosition();}

    //Resets the ghost eating multiplier. Called when frightened state ends
    public void resetMultiplier() {
        ((Pacman)pacman).resetMultiplier();
    }

    @Override
    public void setPosition(Vector2D position) {this.kinematicState.setPosition(position);}

    public Vector2D getTargetCorner() {return targetCorner;}

    @Override
    public Layer getLayer() {return this.layer;}

    @Override
    public BoundingBox getBoundingBox() {return this.boundingBox;}

    public ChaseStrategy getChaseStrategy() {return chaseStrategy;}

    @Override
    public void reset() {
        // return ghost to starting position
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .build();
        this.boundingBox.setTopLeft(startingPosition);
        setState(scatterState);
        //Wait for one second, then begin the ghost to move
        this.kinematicState.setSpeed(0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                kinematicState.setSpeed(scatterState.getSpeed());
                getScatterState().resetTime();
            }
        }, 1000);
        this.currentDirectionCount = minimumDirectionCount;
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {this.possibleDirections = possibleDirections;}

    @Override
    public Direction getDirection() {return this.kinematicState.getDirection();}

    @Override
    public Vector2D getCenter() {return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());}

    public void enableFrightenedMode(List<Ghost> ghosts) {};

    public void collect() {this.isCollectable = false;}

    public boolean isCollectable() {return isCollectable;}

    //Initial points from being consumed in frightened mode
    public int getPoints() {return 200;}

    //Gets the type of ghost
    public char getType() {return type;}

    //Gets the instance of blinky ghost
    public Ghost getBlinky() {return blinky;}

    //Sets whether the ghost is collectable or not
    public void setCollectable(boolean value) {isCollectable = value;}

}
