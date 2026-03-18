package pacman.model.entity.staticentity.collectable;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.staticentity.StaticEntityImpl;

import java.util.List;

//Decorator class for pellet objects
public abstract class PelletDecorator extends StaticEntityImpl implements Collectable{
    protected final Pellet decoratedPellet;

    //Constructor for PelletDecorator, which takes a pellet to decorate
    public PelletDecorator(Pellet decoratedPellet) {
        super(decoratedPellet.getBoundingBox(), decoratedPellet.getLayer(), decoratedPellet.getImage());
        this.decoratedPellet = decoratedPellet;
    }

    @Override
    public void collect() {
        decoratedPellet.collect();
    }

    @Override
    public void reset() {
        decoratedPellet.reset();
    }

    @Override
    public boolean isCollectable() {
        return decoratedPellet.isCollectable();
    }

    @Override
    public int getPoints() {
        return decoratedPellet.getPoints();
    }

    @Override
    public void enableFrightenedMode(List<Ghost> ghosts) {}
}
