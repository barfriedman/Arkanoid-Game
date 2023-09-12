package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.game_objects.Ball;
import src.game_objects.BallCollisionCount;
import src.game_objects.Puck;

public class ChangeCameraStrategy extends StrategyDecorator{
    private static final int ALLOWED_BALL_COLLISIONS = 4;
    private static final float CAMERA_ZOOM_FACTOR = 1.2f;
    private GameObjectCollection gameObjects;
    private CollisionStrategy strategyToDecorate;
    private BrickerGameManager gameManager;
    private WindowController windowController;
    private Ball ball;

    /**
     *
     * @param strategyToDecorate
     * @param gameManager
     * @param windowController
     */
    public ChangeCameraStrategy(CollisionStrategy strategyToDecorate,
                                BrickerGameManager gameManager,
                                WindowController windowController) {
        super(strategyToDecorate);
        this.strategyToDecorate = strategyToDecorate;
        this.gameManager = gameManager;
        this.windowController = windowController;
        this.gameObjects = strategyToDecorate.getGameObjectCollection();
    }

    /**
     *
     * @param collidedObj   - - the object that was collided (the brick)
     * @param colliderObj   - - the object that collided with the brick (the ball).
     * @param bricksCounter -- num of bricks remain
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        this.strategyToDecorate.onCollision(collidedObj,colliderObj,bricksCounter);
        if(this.gameManager.getCamera() == null && !(colliderObj instanceof Puck) ){
            gameManager.setCamera(
                    new Camera(
                            colliderObj, //object to follow

                            Vector2.ZERO, //follow the center of the object
                            windowController.getWindowDimensions().mult(CAMERA_ZOOM_FACTOR), //widen the frame a bit
                            windowController.getWindowDimensions() //share the window dimensions
                    )
            );
            BallCollisionCount collisionCount = new BallCollisionCount(
                    Vector2.ZERO, Vector2.ZERO,null, (Ball) colliderObj, ALLOWED_BALL_COLLISIONS,this);
            gameObjects.addGameObject(collisionCount);
        }
    }

    /**
     * Order the game manager to set the camera back to default.
     */
    public void setBackCamera(){
        gameManager.setCamera(null);
    }

    /**
     * gettr function for the game object collection of the game
     * @return GameObjectCollection -- all game objects that are currently in the game.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return null;
    }
}
