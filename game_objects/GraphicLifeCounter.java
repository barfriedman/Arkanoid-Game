package src.game_objects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {
    private static final int HEART_LOCATION_FACTOR = 5;
    private static final int HEART_DIM = 15;
    private static final int INIT_NUM_OF_LIVES = 3;
    private Vector2 dimensions;
    private Counter livesCounter;
    private Renderable renderable;
    private GameObjectCollection gameObjectsCollection;
    private int numOfLives;
    private GameObject [] allHearts;

    /**
     * This is the constructor for the graphic lives counter.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     *                      the top left corner of the left most heart
     * @param dimensions    the dimension of each heart
     * @param livesCounter    the counter which holds current lives count
     * @param renderable    The renderable representing the object.the image renderable
     *                      of the hearts
     * @param gameObjectsCollection the collection of all game objects currently in the game
     * @param numOfLives    Number of current lives
     */
    public GraphicLifeCounter(Vector2 topLeftCorner,
                              Vector2 dimensions,
                              danogl.util.Counter livesCounter, Renderable renderable,
                              danogl.collisions.GameObjectCollection gameObjectsCollection,
                              int numOfLives) {
        super(topLeftCorner, Vector2.ZERO, renderable);
        this.dimensions = dimensions;
        this.livesCounter = livesCounter;
        this.renderable = renderable;
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = INIT_NUM_OF_LIVES;
        allHearts = new GameObject[numOfLives];
        for (int i = 0; i < this.numOfLives; i++) {
            GameObject heart = new GameObject(
                    new Vector2(
                            this.getTopLeftCorner().x() + HEART_DIM * i +(i*HEART_LOCATION_FACTOR),
                            this.getTopLeftCorner().y()),
                    this.dimensions,this.renderable);
            allHearts[i] = heart;
            gameObjectsCollection.addGameObject(heart,Layer.BACKGROUND);
        }
    }

    /**
     * Create a heart object and add it to the game.
     */
    private void addHeart(){

        GameObject heart = new GameObject(
                new Vector2(
                        this.getTopLeftCorner().x() + HEART_DIM * numOfLives
                                +(numOfLives*HEART_LOCATION_FACTOR),
                        this.getTopLeftCorner().y()),
                dimensions,renderable);
        allHearts[numOfLives] = heart;
        this.numOfLives++;
        gameObjectsCollection.addGameObject(heart,Layer.BACKGROUND);
    }

    /**
     *updates the hearts according to lives counter of the game
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     *                  ##unused in this override!##
     */
    @Override
    public void update(float deltaTime) {
        if (livesCounter.value() < this.numOfLives){
            this.gameObjectsCollection.removeGameObject(
                    allHearts[livesCounter.value()],Layer.BACKGROUND);
            numOfLives--;
        }
        else {
            if (livesCounter.value() > numOfLives) {
                addHeart();
            }
        }
    }
}
