package src.brick_strategies;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import src.BrickerGameManager;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;



public class StrategyFactory {
    private static final int INDEX_WITHOUT_BASIC = 1;
    private static final int NO_DOUBLES_COUNTED = 0;
    private static final int ONE_DOUBLE_COUNTED = 1;
    private static final int TWO_DOUBLES_COUNTED = 2;

    /**
     * all strategy options for the factory
     */
    private enum Strategies {
        BASIC, PUCK, BOT_PADDLE, CHANGE_CAMERA, LIVES, DOUBLE
    }

    private ImageReader imageReader;
    private SoundReader soundReader;
    private GameObjectCollection gameObjects;
    private UserInputListener inputListener;
    private WindowController windowController;
    private BrickerGameManager gameManager;
    private Counter lives;
    private int strategyCounter;

    /**
     * Constructor for StrategyFactory
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     * @param soundReader Contains a single method: readSound, which reads a wav file from disk.
     * @param gameObjects a GameObjectCollection of all game objects that are currently in the game
     * @param inputListener Contains a single method: isKeyPressed, which returns whether a
     *                      given key is currently pressed by the user or not.
     * @param windowController Contains an array of helpful, self explanatory methods concerning
     *                         the window.
     * @param gameManager instane of a game manager which calls this factory.
     * @param lives Counter of how many lives the player has at the moment
     */
    public StrategyFactory(ImageReader imageReader,
                           SoundReader soundReader,
                           GameObjectCollection gameObjects,
                           UserInputListener inputListener,
                           WindowController windowController,
                           BrickerGameManager gameManager, Counter lives){

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.gameObjects = gameObjects;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.gameManager = gameManager;
        this.lives = lives;
        this.strategyCounter = 0;
    }

    /**
     * creates the basic brick strategy.
     * @return the basic strategy created.
     */
    public CollisionStrategy createBasicStrategy(){
        CollisionStrategy strategy = new BasicCollisionStrategy(this.gameObjects);
        return strategy;
    }

    /**
     * This is the "main" method of the factory - it chooses randomly a strategy
     * and construct it- based on a given base strategy. In case double behavior was drawn, it has a hekper
     * function to create it.
     * @param strategy a basic strategy to build the strategy on.
     * @return the final strategy.
     */
    public CollisionStrategy getStrategy(CollisionStrategy strategy){
        Random rand =new Random();
        int index=0;
        switch (this.strategyCounter){
            case NO_DOUBLES_COUNTED:
                index= rand.nextInt(Strategies.values().length);
                break;
            case ONE_DOUBLE_COUNTED:
                index = ThreadLocalRandom
                        .current()
                        .nextInt(INDEX_WITHOUT_BASIC, Strategies.values().length);
            case TWO_DOUBLES_COUNTED:
                index = rand.nextInt(Strategies.values().length-1);
        }
        Strategies drawnStrategy = Strategies.values()[index];
        switch (drawnStrategy){
            case BASIC:
                break;
            case PUCK:
                strategy = new ExtraBallsStrategy(strategy,this.imageReader,this.soundReader);
                break;
            case BOT_PADDLE:
                strategy = new ExtraPaddleStrategy(
                        strategy,this.imageReader,this.inputListener,windowController);
                break;
            case CHANGE_CAMERA:
                strategy = new ChangeCameraStrategy(strategy,this.gameManager, windowController);
                break;
            case LIVES:
                strategy = new ReturnLifeStrategy(strategy,imageReader,lives);
                break;
            case DOUBLE:
                strategyCounter ++;
                strategy = makeDouble(strategy);
                break;
        }
        return strategy;
    }

    /**
     * helper function for getStrategy in case DOUBLE was drawn.
     * It draws 2 new strategies, and allows for a third one, recursively.
     * @param collisionStrategy the basic strategy to build on.
     * @return a double strategy.
     */
    private CollisionStrategy makeDouble(CollisionStrategy collisionStrategy){
        CollisionStrategy strategy1 = getStrategy(collisionStrategy);
        CollisionStrategy strategy2 = getStrategy(strategy1);
        return strategy2;
    }
}
