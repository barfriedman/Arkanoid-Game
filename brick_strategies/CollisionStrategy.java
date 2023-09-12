package src.brick_strategies;

import danogl.collisions.GameObjectCollection;

public interface CollisionStrategy {

    /**
     * When a brick detects a collision, this method is called and activates the current strategy.
     *
     * @param collidedObj   - - the object that was collided (the brick)
     * @param colliderObj   - - the object that collided with the brick (the ball).
     * @param bricksCounter -- num of bricks remain
     */
    public void onCollision(danogl.GameObject collidedObj,
                            danogl.GameObject colliderObj, danogl.util.Counter bricksCounter);

    /**
     * gettr function for the game object collection of the game
     * @return GameObjectCollection -- all game objects that are currently in the game.
     */
    public GameObjectCollection getGameObjectCollection();
}
