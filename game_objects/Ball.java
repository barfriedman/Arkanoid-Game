package src.game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Ball extends GameObject {
    private Sound collisionSound;
    private int collisionCount;

    /**
     * Construct a new GameObject instance and also saves the strategy given.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCount = 0;
    }


    /**
     * This is an override method for GameObject's onCollisionEnter.
     * When the game detects a collision between the two objects,
     * it activates the strategy of the brick.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        this.collisionCount++;
//        System.out.printf("Ball's collision count= %d\n", this.collisionCount);
        super.onCollisionEnter(other, collision);
        // flip the ball
        Vector2 newVel = getVelocity().flipped(collision.getNormal());

        setVelocity(newVel);
        collisionSound.play();
    }

    /**
     * getter for CollisionCount field
     * @return -- int, the number of collisions a ball instance had since the game started.
     */
    public int getCollisionCount() {
        return this.collisionCount;
    }
}
