package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class BasicCollisionStrategy implements CollisionStrategy{
    private GameObjectCollection gameObjects;

    /**
     * Constructor of the basic strategy
     * @param gameObjects - An object which holds all game objects of the game running.
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * gettr function for the game object collection of the game
     * @return GameObjectCollection -- all game objects that are currently in the game.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjects;
    }

    /**
     * When a brick detects a collision, this method is called and activates the current strategy.
     * It decrements the number of active bricks on the screen and
     * removes the current brick from the screen.
     * @param collidedObj   - - the object that was collided (the brick)
     * @param colliderObj   - - the object that collided with the brick (the ball).
     * @param bricksCounter -- num of bricks remain
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        if(this.gameObjects.removeGameObject(collidedObj)){
            bricksCounter.decrement();
        }
    }
}
