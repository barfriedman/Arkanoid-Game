package src.game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class BotPaddle extends Paddle {
    private static final int MAX_NUM_HITS = 3;

    private Counter hitsCounter;
    private GameObjectCollection gameObjects;
    private static boolean isThereBotPaddle = false;

    /**
     * This constructor creates the paddle object.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener    The input listener which waits for user inputs and acts on them.
     * @param windowDimensions The dimensions of the screen, to know the limits for paddle movements.
     * @param minDistFromEdge  Minimum distance allowed for the paddle from the edge of the walls
     */
    public BotPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                     UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge,
                     GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);
        this.gameObjects = gameObjects;
        this.hitsCounter = new Counter();
        this.isThereBotPaddle = true;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /**
     * when the botPaddle collides with another object (which can be ball or puck)
     * the number of hits is increased, so the field HitsCounter is updated.
     * if the MAX_NUM_HITS is reached, the botPaddle is removed.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        this.hitsCounter.increment();
        if (this.hitsCounter.value() == MAX_NUM_HITS) {
            this.gameObjects.removeGameObject(this);
            this.isThereBotPaddle = false;
        }
    }

    /**
     * Check if there is a botPaddle in the game already.
     *
     * @return true of there is currently a BotPaddle object in the game, false otherwise
     */
    public static boolean isInitiated() {
        return isThereBotPaddle;
    }
}
