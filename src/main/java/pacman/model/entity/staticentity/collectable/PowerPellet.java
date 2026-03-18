package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.staticentity.StaticEntityImpl;

import java.util.List;

/**
 * Represents a Power Pellet in the Pac-Man game.
 */
//Concrete pellet decorator
public class PowerPellet extends PelletDecorator {
    public PowerPellet(Pellet decoratedPellet) {
        super(decoratedPellet);
        BoundingBox originalBoundingBox = super.getBoundingBoxProtected();
        double leftX = originalBoundingBox.getLeftX();
        double topY = originalBoundingBox.getTopY();
        //Make the power pellet twice as big
        BoundingBox newBoundingBox = new BoundingBoxImpl(
                new Vector2D(leftX - 8, topY - 8),
                originalBoundingBox.getWidth() * 2,
                originalBoundingBox.getHeight() * 2
        );
        super.setBoundingBox(newBoundingBox);

    }

    @Override
    public void collect() {
        decoratedPellet.collect();
        setLayer(Layer.INVISIBLE);
    }


    public void enableFrightenedMode(List<Ghost> ghosts) {
        //Set the state of all ghosts to the frightened state
        for (Ghost g : ghosts) {
            g.setState(g.getFrightenedState());
        }
    }
    @Override
    public int getPoints() {
        //Get the normal pellet points (10), + 40 to make it 50
        return decoratedPellet.getPoints() + 40;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    //Reset the power pellet to the original state
    @Override
    public void reset() {
        setLayer(Layer.BACKGROUND);
        decoratedPellet.reset();
    }
}
