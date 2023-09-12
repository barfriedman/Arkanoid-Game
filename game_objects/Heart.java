package src.game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Heart extends GameObject {
    private static final int MAX_LIFE = 4;
    private Counter livesCounter;
    private GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Counter livesCounter,
                 GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.livesCounter = livesCounter;
        this.gameObjects = gameObjects;
    }

    /**
     * the heart should only collide with the paddle.
     * @param other The other GameObject.
     * @return true if the other object is Paddle (also not BotPaddle)
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return (other instanceof Paddle && (!(other instanceof BotPaddle)));
    }


    /**
     * when the heart collides (it can only happen with the paddle), a life is added to the player,
     * for maximum of MAX_LIFE.
     * @param other The GameObject with which a collision occurred. (paddle)
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (this.livesCounter.value() < MAX_LIFE){
            this.livesCounter.increment();
        }
        this.gameObjects.removeGameObject(this);
    }
}
