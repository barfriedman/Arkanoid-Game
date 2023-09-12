package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.game_objects.Puck;

import java.util.Random;

public class ExtraBallsStrategy extends StrategyDecorator {

    private static final int NUM_PUCKS = 3;
    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    private static final String COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final float PUCK_DIMS_FACTOR = 3;
    private static final float PUCK_SPEED = 100;
    private static final float REVERSE_SPEED = -1;
    private CollisionStrategy strategyToDecorate;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private GameObjectCollection gameObjects;

    /**
     * gettr function for the game object collection of the game
     *
     * @return GameObjectCollection -- all game objects that are currently in the game.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjects;
    }

    /**
     * @param strategyToDecorate the strategy to build the extra strategy for.
     * @param imageReader        Contains a single method: readImage, which reads an image from disk.
     * @param soundReader        Contains a single method: readSound, which reads a wav file from disk.
     */
    public ExtraBallsStrategy(CollisionStrategy strategyToDecorate, ImageReader imageReader,
                              SoundReader soundReader) {
        super(strategyToDecorate);
        this.strategyToDecorate = strategyToDecorate;

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.gameObjects = strategyToDecorate.getGameObjectCollection();
    }


    /**
     * extends the collision action- create a Puck with random start direction.
     *
     * @param collidedObj   - - the object that was collided (the brick)
     * @param colliderObj   - - the object that collided with the brick (the ball).
     * @param bricksCounter -- num of bricks remain
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        this.strategyToDecorate.onCollision(collidedObj, colliderObj, bricksCounter);
//        for (int i = 0; i < NUM_PUCKS; i++) {
        Vector2 center = new Vector2(
                collidedObj.getCenter().x(), collidedObj.getCenter().y());
        //if adding 3 pucs, add to x +i
        Vector2 dims = new Vector2(
                collidedObj.getDimensions().x() / PUCK_DIMS_FACTOR,
                collidedObj.getDimensions().x() / PUCK_DIMS_FACTOR);
        Renderable puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        Puck puck = new Puck(collidedObj.getTopLeftCorner(), dims, puckImage, collisionSound);
        puck.setCenter(center);
        Vector2 puckSpeed = initPuckSpeed();
        puck.setVelocity(puckSpeed);
        this.gameObjects.addGameObject(puck);
//        }
    }

    /**
     * Initialize the puck's speed on both axes.
     *
     * @return a vector of the puck speed on X and Y
     */
    private Vector2 initPuckSpeed() {
        float puckSpeedY = PUCK_SPEED;
        float puckSpeedX = PUCK_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            puckSpeedX *= REVERSE_SPEED;
        }
        if (rand.nextBoolean()) {
            puckSpeedY *= REVERSE_SPEED;
        }
        return new Vector2(puckSpeedX, puckSpeedY);

    }
}
