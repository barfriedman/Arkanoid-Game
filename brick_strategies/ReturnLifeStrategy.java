package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.game_objects.Heart;

public class ReturnLifeStrategy extends StrategyDecorator{
    private static final Vector2 HEART_VELOCITY = new Vector2(0,100);
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final float HEART_DIM = 15;
    private GameObjectCollection gameObjects;
    private Counter lifeCounter;
    private ImageReader imageReader;
    private CollisionStrategy strategyToDecorate;

    /**
     * Constructor for the return life strategy.
     * @param strategyToDecorate the strategy to build the extra strategy for.
     * @param imageReader     Contains a single method: readImage, which reads an image from disk.
     * @param lifeCounter Counter of how many lives the player has at the moment.
     */
    public ReturnLifeStrategy(CollisionStrategy strategyToDecorate,
                              ImageReader imageReader,
                              Counter lifeCounter) {
        super(strategyToDecorate);
        this.strategyToDecorate = strategyToDecorate;
        this.gameObjects = strategyToDecorate.getGameObjectCollection();
        this.imageReader = imageReader;
        this.gameObjects = strategyToDecorate.getGameObjectCollection();
        this.lifeCounter = lifeCounter;
    }

    /**
     * Create a new falling heart object and add it to the game.
     * @param collidedObj   - - the object that was collided (the brick)
     * @param colliderObj   - - the object that collided with the brick (the ball).
     * @param bricksCounter -- num of bricks remain
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        this.strategyToDecorate.onCollision(collidedObj,colliderObj,bricksCounter);
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        Vector2 center = new Vector2(
                collidedObj.getCenter().x(),collidedObj.getCenter().y());
        Heart heart = new Heart(
                Vector2.ZERO,new Vector2(HEART_DIM,HEART_DIM),heartImage,lifeCounter,gameObjects);
        heart.setCenter(center);
        heart.setVelocity(HEART_VELOCITY);
        this.gameObjects.addGameObject(heart);
    }



    /**
     * getter function for the game object collection of the game
     * @return GameObjectCollection -- all game objects that are currently in the game.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return this.gameObjects;
    }
}
