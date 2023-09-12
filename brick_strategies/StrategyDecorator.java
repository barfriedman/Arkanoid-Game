package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public abstract class StrategyDecorator implements CollisionStrategy{
    private CollisionStrategy strategyToDecorate;

    @Override
    public abstract void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter);

    /**
     * gettr function for the game object collection of the game
     * @return GameObjectCollection -- all game objects that are currently in the game.
     */
    @Override
    public abstract GameObjectCollection getGameObjectCollection();

    /**
     * Constructor for the strategy decorator
     * @param strategyToDecorate the strategy to build the extra strategy for.
     */
    public StrategyDecorator(CollisionStrategy strategyToDecorate) {

        this.strategyToDecorate = strategyToDecorate;
    }
}
