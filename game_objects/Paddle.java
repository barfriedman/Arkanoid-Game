package src.game_objects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private static final float WINDOW_LEFT_CORNER = 0;
    private final UserInputListener inputListener;
    private Vector2 windowDimensions;
    private int minDistFromEdge;




    /**
     *This constructor creates the paddle object.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                  Note that (0,0) is the top-left corner of the window.
     * @param dimensionsWidth and height in window coordinates.
     * @param renderable The renderable representing the object. Can be null, in which case
     *                   the GameObject will not be rendered.
     * @param inputListener  The input listener which waits for user inputs and acts on them.
     * @param windowDimensions The dimensions of the screen, to know the limits for paddle movements.
     * @param minDistFromEdge Minimum distance allowed for the paddle from the edge of the walls
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable, UserInputListener inputListener,
                  danogl.util.Vector2 windowDimensions, int minDistFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistFromEdge = minDistFromEdge;
    }

    /**
     * updates paddle location according to user input, without going out of the window
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
        Vector2 movementDir = Vector2.ZERO;
        float rightEdge = this.getTopLeftCorner().x()+getDimensions().x();
        float leftEdge = this.getTopLeftCorner().x();
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)
                && leftEdge > (WINDOW_LEFT_CORNER + this.minDistFromEdge) ){
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)
                && rightEdge < (this.windowDimensions.x()- this.minDistFromEdge)){
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
