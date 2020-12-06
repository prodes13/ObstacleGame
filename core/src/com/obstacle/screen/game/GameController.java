package com.obstacle.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.obstacle.config.DifficultyLevel;
import com.obstacle.config.GameConfig;
import com.obstacle.entity.Background;
import com.obstacle.entity.Obstacle;
import com.obstacle.entity.Player;

import static com.obstacle.config.DifficultyLevel.EASY;

public class GameController {
    // -- constants
    public static final Logger log = new Logger(GameScreen.class.getName(), Logger.DEBUG);

    // -- attributes
    private Player player;
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private float obstacleTimer;
    private float scoreTimer;

    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;

    private Background background;
    private DifficultyLevel difficultyLevel = EASY;
    private Pool<Obstacle> obstaclePool;

    // calculate position
    private final float startPlayerX = (GameConfig.WORLD_WIDTH - GameConfig.PLAYER_SIZE) / 2f;
    private final float startPlayerY = 1 - GameConfig.PLAYER_SIZE;

    public int getLives() {
        return lives;
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public Background getBackground() { return background; }

    public int getDisplayScore() {
        return displayScore;
    }

    // -- constructor
    public GameController() {
        init();
    }

    // init
    private void init() {
// create player
        player = new Player();

//		float startPlayerX = 12;
//		float startPlayerY = 12;

        player.setPosition(startPlayerX, startPlayerY);

        // initialize our obstacle pool
        obstaclePool = Pools.get(Obstacle.class, 40);

        // create background
        background = new Background();
        background.setPosition(0, 0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

    }

    public void update(float delta) {
        if (isGameOver()) {
            log.debug("Game Over!!!");
            return;
        }
        updatePlayer();
        updateObstacles(delta);
        updateScore(delta);
        updateDisplayScore(delta);

        if(isPlayerCollidingWithObstacle()) {
//			alive = false;
            log.debug("Collision detected!!!");
            lives--;

            if(isGameOver()) {
                log.debug("Game Over!");
            } else {
                restart();
            }
        }
    }

    private void restart() {
        obstaclePool.freeAll(obstacles);
        obstacles.clear();
        player.setPosition(startPlayerX, startPlayerY);
    }

    private void updateScore(float delta) {
        scoreTimer += delta;
        if(scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            score += MathUtils.random(1, 5);
            scoreTimer = 0.0f;
        }
    }
    // trick to achieve score effect
    private void updateDisplayScore(float delta) {
        if(displayScore < score) {
            displayScore = Math.min(
                    score,
                    displayScore + (int)(60 * delta)
            );
//			basically plus one
        }
    }

    private void updateObstacles(float delta) {
        for(Obstacle obstacle : obstacles) {
            obstacle.update();
        }

        createNewObstacle(delta);
        removePassedObstacles();
    }

    private void removePassedObstacles() {
        if(obstacles.size > 0) {
            // we check for size, because .first() throw exception otherwise
            Obstacle first = obstacles.first();
            // size of the obstacle
            float minObstacleY = - GameConfig.OBSTACLE_SIZE;
            if(first.getY() < minObstacleY) {
                obstacles.removeValue(first, true);
                obstaclePool.free(first);
            }
        }
    }

    private void createNewObstacle(float delta) {
        obstacleTimer += delta;
        if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
            Obstacle obstacle = obstaclePool.obtain();

            float min = obstacle.getWidth() / 2;
            float max = GameConfig.WORLD_WIDTH - GameConfig.OBSTACLE_SIZE;
            float obstacleX = MathUtils.random(min, max);
            float obstacleY = GameConfig.WORLD_HEIGHT;


            obstacle.setYSpeed(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);

            obstacles.add(obstacle);
            obstacleTimer = 0f;


        }
    }

    private void updatePlayer() {
//		log.debug("Player coordinates:  " + player.getX() + "  |  " + player.getY());
        float xSpeed = 0;
        float ySpeed = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = GameConfig.MAX_PLAYER_X_SPEED;
        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;
        }

//        else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            ySpeed = GameConfig.MAX_PLAYER_X_SPEED;
//        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            ySpeed = -GameConfig.MAX_PLAYER_X_SPEED;
//        }

        player.setX(player.getX() + xSpeed);
//        setY(ySpeed);

        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {

//		float playerX = player.getX();
        float playerY = player.getY();

//		if(playerX < player.getWidth() / 2f) {
//			playerX = player.getWidth() / 2f;
//		} else if(playerX > GameConfig.WORLD_WIDTH - player.getWidth() / 2f) {
//			playerX = GameConfig.WORLD_WIDTH - player.getWidth() / 2f;
//		}

        // or you can use this directly
        float playerX = MathUtils.clamp(player.getX(), 0, GameConfig.WORLD_WIDTH - player.getWidth());
//		 clamp value, min, max

        if(playerY < 0) {
            playerY = 0;
        } else if(playerY > GameConfig.WORLD_HEIGHT) {
            playerY = GameConfig.WORLD_HEIGHT;
        }

        player.setPosition(playerX, playerY);
    }

    public boolean isGameOver() {
//        return false;
        return lives <= 0;
    }

    private boolean isPlayerCollidingWithObstacle() {

        for(Obstacle obstacle : obstacles) {
//			hitting it just one time...
            if(obstacle.isNotHit() && obstacle.isPlayerColliding(player)) {
                return true;
            }
        }

        return false;
    }


}
