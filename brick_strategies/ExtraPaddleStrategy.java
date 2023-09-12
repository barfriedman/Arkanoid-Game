package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.game_objects.BotPaddle;

public class ExtraPaddleStrategy extends StrategyDecorator {

    private static final float PADDLE_DIM_Y = 17;
    private static final float PADDLE_DIM_X = 150;
    private static final String BOT_PADDLE_IMAGE_PATH = "assets/botGood.png";
    private static final float CENTER_FACTOR = 0.5F;

    private GameObjectCollection gameObjects;

    private CollisionStrategy strategyToDecorate;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private Vector2 windowDims;
    private static final int DIST_FROM_EDGE = 15;

    /**
     * Constructor for the extra paddle strategy.
     *
     * @param strategyToDecorate the strategy to build the extra strategy for.
     * @param imageReader        Contains a single method: readImage, which reads an image from disk.
     * @param soundReader        Contains a single method: readSound, which reads a wav file from disk.
     * @param windowController   Contains an array of helpful, self explanatory methods concerning
     *                           the window.
     */
    public ExtraPaddleStrategy(CollisionStrategy strategyToDecorate,
                               ImageReader imageReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super(strategyToDecorate);
        this.strategyToDecorate = strategyToDecorate;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDims = windowController.getWindowDimensions();
        this.gameObjects = strategyToDecorate.getGameObjectCollection();
    }

    /**
     * specifies beavior on collision - create a BotPaddle object in the middle of the game window,
     * if there isn't one yet.
     *
     * @param collidedObj   - - the object that was collided (the brick)
     * @param colliderObj   - - the object that collided with the brick (a ball/puck).
     * @param bricksCounter -- num of bricks remain
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        this.strategyToDecorate.onCollision(collidedObj, colliderObj, bricksCounter);
        if (!BotPaddle.isInitiated()) {
            Vector2 dims = new Vector2(PADDLE_DIM_X, PADDLE_DIM_Y);
            Renderable botPaddleImage = this.imageReader.readImage(
                    BOT_PADDLE_IMAGE_PATH, false);

            BotPaddle botPaddle = new BotPaddle(Vector2.ZERO, dims,
                    botPaddleImage, this.inputListener, this.windowDims, DIST_FROM_EDGE, gameObjects);
            botPaddle.setCenter(windowDims.mult(CENTER_FACTOR));

            this.gameObjects.addGameObject(botPaddle);
        }
    }

    /**
     * gettr function for the game object collection of the game
     *
     * @return GameObjectCollection -- all game objects that are currently in the game.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjects;
    }
}
