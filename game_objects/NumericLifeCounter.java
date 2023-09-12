package src.game_objects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {
    private static final int THREE_LIVES = 3;
    private static final int TWO_LIVES = 2;
    private static final int ONE_LIFE = 1;
    private static final int FOUR_LIVES = 4;
    private Counter livesCounter;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions  Width and height in window coordinates.
     * @param renderable  The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param gameObjectCollection- - the collection of all game objects currently in the game //todo why needed?
     */
    public NumericLifeCounter(danogl.util.Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                               danogl.collisions.GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        TextRenderable textRenderable = new TextRenderable(String.valueOf(THREE_LIVES));
        textRenderable.setColor(Color.green);
        this.renderer().setRenderable(textRenderable);
    }

    /**
     * updates the numeric life counter representation in the game
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     *                  ##unused!##
     */
    @Override
    public void update(float deltaTime) {
        if (livesCounter.value() == FOUR_LIVES){
            TextRenderable textRenderable = new TextRenderable(String.valueOf(FOUR_LIVES));
            textRenderable.setColor(Color.GREEN);
            this.renderer().setRenderable(textRenderable);
        }
        if (livesCounter.value() == THREE_LIVES){
            TextRenderable textRenderable = new TextRenderable(String.valueOf(THREE_LIVES));
            textRenderable.setColor(Color.GREEN);
            this.renderer().setRenderable(textRenderable);
        }
        if (livesCounter.value() == TWO_LIVES){
            TextRenderable textRenderable = new TextRenderable(String.valueOf(TWO_LIVES));
            textRenderable.setColor(Color.yellow);
            this.renderer().setRenderable(textRenderable);
        }
        if (livesCounter.value() == ONE_LIFE){
            TextRenderable textRenderable = new TextRenderable(String.valueOf(ONE_LIFE));
            textRenderable.setColor(Color.red);
            this.renderer().setRenderable(textRenderable);
        }
    }
}
