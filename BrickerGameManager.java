package src;

import danogl.components.CoordinateSpace;
import src.brick_strategies.*;
import src.brick_strategies.CollisionStrategy;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.game_objects.*;


import java.awt.event.KeyEvent;
//import java.util.List;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final float BALL_SPEED = 200;
    private static final float REVERSE_SPEED = -1;
    private static final int BORDER_WIDTH = 10;
    private static final int NUM_BRICKS_IN_ROW = 8;
    private static final float BALL_DIM = 20;
    private static final float BRICK_WIDTH = 20;
    private static final int NUM_OF_BRICK_ROWS = 7;
    private static final int LIFE_INIT = 3;
    private static final int MAX_LIFE = 4;

    private static final int NUMERIC_DIM = 15;
    private static final float HEART_DIM = 15;
    private static final String LOST_MESSAGE = "You Lost!";
    private static final String WIN_MESSAGE = "You Won!";
    private static final String PLAY_AGAIN_MSG = " Play again?";
    private static final int MIN_NUM_OF_LIVES = 1;
    private static final int NUM_OF_BRICKS_FOR_WIN = 0;
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_SMALL.JPEG";
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String BRICKER_TITLE = "Bicker";
    private static final float GRAPHIC_COUNTER_DIST_FROM_EDGE = (2 * BORDER_WIDTH) + NUMERIC_DIM;
    private static final float NUMERIC_LOCATION_X = 2 * BORDER_WIDTH;
    private static final float LIVES_DIST_FROM_BOTTOM = (NUMERIC_DIM + 6);
    private static final float PADDLE_DIM_Y = 17;
    private static final float PADDLE_DIM_X = 150;
    private static final float WINDOW_DIM_Y = 500;
    private static final float WINDOW_DIM_X = 700;
    private static final int PADDLE_DIST_FROM_BOTTOM = 35;
    private static final float CENTER_FACTOR = 0.5F;
    private static final float BRICKS_BOUDRIES = (4 * BORDER_WIDTH);
    private static final float BRICKS_Y_DIM_FACTOR = (BRICK_WIDTH + 2);
    private static final float BRICKS_START_X = (BORDER_WIDTH * 2);
    private static final String EMPTY_STR = "";
    private static final int NO_BRICKS = 0;


    private Ball ball;
    private Vector2 windowDims;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private Counter lives;
    private Counter bricksCounter;


    /**
     * constructs a new BrickerGameManager object
     *
     * @param windowTitle
     * @param windowDims  dimensions of the window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDims) {
        super(windowTitle, windowDims);

    }

    /**
     * initialize all objects needed for a Bricker Game
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDims = windowController.getWindowDimensions();
        this.lives = new Counter(LIFE_INIT);

        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        createBall(imageReader, soundReader, windowController);

        createPaddle(imageReader, inputListener);

        createBorders(windowDims);

        createBricks(imageReader);

        createBackground(imageReader);

        createNumericLifeCounter();

        createGraphicLifeCounter(imageReader);
    }

    /**
     * check if the user lost one point, lost the game or won the game and update accordingly.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkLifeLost();
        checkGameEnd();
    }

    /**
     * check if the user lost the ball (if it fell without catching it)
     */
    private void checkLifeLost() {
        float ballHeight = ball.getCenter().y();
        if (ballHeight > windowDims.y()) {
            this.lives.decrement();
            setBall();
        }
    }

    /**
     * check if the game is over - a game is over if all user's life are lost, if all the bricks
     * are gone or if 'w' was pressed.
     */
    private void checkGameEnd() {
        String prompt = EMPTY_STR;
        if (lives.value() < MIN_NUM_OF_LIVES) {
            prompt = LOST_MESSAGE;
        }
        if (this.bricksCounter.value() == NUM_OF_BRICKS_FOR_WIN
                || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = WIN_MESSAGE;
        }
        if (!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_MSG;
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * add background image to the game window
     *
     * @param imageReader Contains a single method: readImage,
     *                    which reads an image from disk.
     */
    private void createBackground(ImageReader imageReader) {
        Renderable backgroundImage = imageReader.readImage(
                BACKGROUND_IMAGE_PATH, false);
        GameObject background = new GameObject(Vector2.ZERO, windowDims, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * create a Numeric Life Counter object for the game
     */
    private void createNumericLifeCounter() {
        Vector2 numericLocation = new Vector2(
                NUMERIC_LOCATION_X, windowDims.y() - LIVES_DIST_FROM_BOTTOM);
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(this.lives,
                numericLocation, new Vector2(NUMERIC_DIM, NUMERIC_DIM), gameObjects());
        this.gameObjects().addGameObject(numericLifeCounter, Layer.BACKGROUND);
    }

    /**
     * create a Graphic Life Counter object for the game, including hearts
     */
    private void createGraphicLifeCounter(ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(
                new Vector2(GRAPHIC_COUNTER_DIST_FROM_EDGE, windowDims.y() - LIVES_DIST_FROM_BOTTOM),
                new Vector2(HEART_DIM, HEART_DIM), this.lives, heartImage, this.gameObjects(), MAX_LIFE);
        this.gameObjects().addGameObject(graphicLifeCounter, Layer.BACKGROUND);
    }

    /**
     * @param imageReader   Contains a single method: readImage,
     *                      which reads an image from disk.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      *                         a given key is currently pressed by the user or not.
     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, false);
        GameObject paddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_DIM_X, PADDLE_DIM_Y),
                paddleImage, inputListener, windowDims, BORDER_WIDTH);
        paddle.setCenter(
                new Vector2(windowDims.x() / 2,
                        (int) windowDims.y() - PADDLE_DIST_FROM_BOTTOM));
        this.gameObjects().addGameObject(paddle);
    }

    /**
     * Create all the bricks of the game, using a factory to give the bricks strategies for collision.
     *
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     */
    private void createBricks(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
        Vector2 brickDims = new Vector2(
                (windowDims.x() - BRICKS_BOUDRIES) / NUM_BRICKS_IN_ROW, BRICK_WIDTH);
        StrategyFactory strategyFactory = new StrategyFactory(
                imageReader, this.soundReader, this.gameObjects(),
                inputListener, windowController, this, lives);
        CollisionStrategy basicStrategy = strategyFactory.createBasicStrategy();
        bricksCounter = new Counter(NO_BRICKS);
        for (int j = 0; j < NUM_OF_BRICK_ROWS; j++) {
            float yBrickLocation = BRICK_WIDTH + (j * BRICKS_Y_DIM_FACTOR);
            for (int i = 0; i < NUM_BRICKS_IN_ROW; i++) {
                CollisionStrategy strategy = strategyFactory.getStrategy(basicStrategy);
                bricksCounter.increment();
                Brick brick = new Brick(
                        new Vector2(
                                BRICKS_START_X + brickDims.x() * i, yBrickLocation),
                        brickDims, brickImage, strategy, bricksCounter);
                this.gameObjects().addGameObject(brick);
            }
        }
    }

    /**
     * Create transparent borders for the game so the objects cant go out of the window from the
     * sided or from the ceiling.
     *
     * @param windowDimensions
     */
    private void createBorders(Vector2 windowDimensions) {
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        null)
        );
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        null)
        );
        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO,
                        new Vector2(windowDimensions.x(), BORDER_WIDTH), null)
        );
    }

    /**
     * Set the ball's speed and location.
     */
    private void setBall() {
        float ballVelY = BALL_SPEED;
        float ballVelX = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= REVERSE_SPEED;
        }
        if (rand.nextBoolean()) {
            ballVelY *= REVERSE_SPEED;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        Vector2 windowDims = windowController.getWindowDimensions();
        ball.setCenter(windowDims.mult(CENTER_FACTOR));

    }

    /**
     * Create a ball object for the game.
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from disk.
     * @param windowController Contains an array of helpful, self explanatory methods concerning
     *                         the window.
     */
    private void createBall(ImageReader imageReader,
                            SoundReader soundReader, WindowController windowController) {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        this.ball = new Ball(Vector2.ZERO, new Vector2(BALL_DIM, BALL_DIM), ballImage, collisionSound);
        setBall();
        this.gameObjects().addGameObject(ball);
    }


    /**
     * the main function of the game. initializes the game and run it.
     *
     * @param args arguments from user.
     */
    public static void main(String[] args) {
        new BrickerGameManager(BRICKER_TITLE, new Vector2(WINDOW_DIM_X, WINDOW_DIM_Y)).run();
    }
}
