package pacman.model.factories;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.staticentity.collectable.PowerPellet;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for delegating createRenderable requests to the appropriate registered concrete factory.
 */
public class RenderableFactoryRegistryImpl implements RenderableFactoryRegistry {

    private final Map<Character, RenderableFactory> factoryRegistry = new HashMap<>();

    @Override
    public void registerFactory(
            char renderableType,
            RenderableFactory factory
    ) {
        if (factoryRegistry.containsKey(renderableType)) {
            throw new IllegalStateException(
                    String.format("Duplicate registration of factory for renderable type %s\n", renderableType));
        }

        factoryRegistry.put(renderableType, factory);
    }

    @Override
    public Renderable createRenderable(
            char renderableType,
            Vector2D position
    ) {
        //If renderable is a power pellet, decorate it with the power pellet class
        if (renderableType == RenderableType.POWERPELLET) {
            Renderable pellet = factoryRegistry.get(RenderableType.PELLET).createRenderable(position);
            return new PowerPellet((Pellet) pellet);
        }
        if (!factoryRegistry.containsKey(renderableType)) {
            return null;
        }
        //Power pellet will be made by the normal pellet class
        RenderableFactory factory = factoryRegistry.get(renderableType);
        return factory.createRenderable(position);
    }
}



