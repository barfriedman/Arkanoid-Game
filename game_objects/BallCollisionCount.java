package src.game_objects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

public class BallCollisionCount extends GameObject {

    private final int initCollisions;
    private Ball ball;
    private int collisionsAllowed;
    private ChangeCameraStrategy cameraStrategy;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public BallCollisionCount(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                              Ball ball, int collisionsAllowed, ChangeCameraStrategy cameraStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.ball = ball;
        this.collisionsAllowed = collisionsAllowed;
        this.cameraStrategy = cameraStrategy;
        this.initCollisions = ball.getCollisionCount();

    }

    /**
     * every moment check the amount of collisions with the ball. if "collisionsAllowed"
     * collisions past since the instance was created (meaning since the ChangeCamera strategy was called),
     * set the camera back to normal.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (ball.getCollisionCount()- this.initCollisions == this.collisionsAllowed) {
            this.cameraStrategy.setBackCamera();
        }
    }
}

