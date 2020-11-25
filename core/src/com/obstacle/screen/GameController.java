package com.obstacle.screen;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.obstacle.config.DifficultyLevel;
import com.obstacle.config.GameConfig;
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

    private DifficultyLevel difficultyLevel = EASY;

    public int getLives() {
        return lives;
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

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

        // calculate position
        float startPlayerX = GameConfig.WORLD_WIDTH / 2;
        float startPlayerY = 1;

//		float startPlayerX = 12;
//		float startPlayerY = 12;

        player.setPosition(startPlayerX, startPlayerY);
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
        }
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
            float minObstacleY = - obstacles.first().getWidth();
            if(first.getY() < minObstacleY) {
                obstacles.removeValue(first, true);
            }
        }
    }

    private void createNewObstacle(float delta) {
        obstacleTimer += delta;
        if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
            float min = 0f;
            float max = GameConfig.WORLD_WIDTH;
            float obstacleX = MathUtils.random(min, max);
            float obstacleY = GameConfig.WORLD_HEIGHT;

            Obstacle obstacle = new Obstacle();
            obstacle.setYSpeed(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);

            obstacles.add(obstacle);
            obstacleTimer = 0f;


        }
    }

    private void updatePlayer() {
//		log.debug("Player coordinates:  " + player.getX() + "  |  " + player.getY());
        player.update();
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
        float playerX = MathUtils.clamp(player.getX(), player.getWidth() /2f, GameConfig.WORLD_WIDTH - player.getWidth() / 2f);
//		 clamp value, min, max

        if(playerY < 0) {
            playerY = 0;
        } else if(playerY > GameConfig.WORLD_HEIGHT) {
            playerY = GameConfig.WORLD_HEIGHT;
        }

        player.setPosition(playerX, playerY);
    }

    private boolean isGameOver() {
        return false;
//        return lives <= 0;
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
