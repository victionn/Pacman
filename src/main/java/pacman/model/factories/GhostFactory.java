package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.Strategy.*;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;

import java.util.Arrays;
import java.util.List;

/**
 * Concrete renderable factory for Ghost objects
 */
public class GhostFactory implements RenderableFactory {

    private static final int RIGHT_X_POSITION_OF_MAP = 448;
    private static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
    private static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;
    private static final Image BLINKY_IMAGE = new Image("maze/ghosts/blinky.png");
    private static final Image INKY_IMAGE = new Image("maze/ghosts/inky.png");
    private static final Image CLYDE_IMAGE = new Image("maze/ghosts/clyde.png");
    private static final Image PINKY_IMAGE = new Image("maze/ghosts/pinky.png");
    private static final Image GHOST_IMAGE = BLINKY_IMAGE;
    private final char type;
    List<Vector2D> targetCorners = Arrays.asList(
            new Vector2D(0, TOP_Y_POSITION_OF_MAP),
            new Vector2D(RIGHT_X_POSITION_OF_MAP, TOP_Y_POSITION_OF_MAP),
            new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP),
            new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP)
    );

    public GhostFactory(char type) {
        this.type = type;
    }

    public Vector2D getTargetCorner() {
        //Gets the vector of the target corner according to the ghost
        return switch (type) {
            case 'b' -> targetCorners.get(1);
            case 's' -> targetCorners.get(0);
            case 'i' -> targetCorners.get(3);
            case 'c' -> targetCorners.get(2);
            default -> targetCorners.get(3);
        };
    }

    public Image getGhostImage() {
        //Gets the image corresponding to the ghost being created
        return switch (type) {
            case 'b' -> BLINKY_IMAGE;
            case 's' -> PINKY_IMAGE;
            case 'i' -> INKY_IMAGE;
            case 'c' -> CLYDE_IMAGE;
            default -> BLINKY_IMAGE;
        };
    }

    //Gets the chase strategy according to the ghost
    public ChaseStrategy getStrategy() {
        return switch (type) {
            case 's' -> new SpeedyStrategy();
            case 'i' -> new InkyStrategy();
            case 'c' -> new ClydeStrategy();
            case 'b' -> new BlinkyStrategy();
            default -> new InkyStrategy();
        };
    }

    @Override
    public Renderable createRenderable(
            Vector2D position
    ) {
        try {
            position = position.add(new Vector2D(4, -4));

            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    GHOST_IMAGE.getHeight(),
                    GHOST_IMAGE.getWidth()
            );

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(position)
                    .build();
            //Creates a new ghost given their chase strategy, and the type
            return new GhostImpl(
                    getGhostImage(),
                    boundingBox,
                    kinematicState,
                    GhostMode.SCATTER,
                    getTargetCorner(),
                    getStrategy(),
                    type
            );
        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ghost configuration | %s ", e));
        }
    }


}
