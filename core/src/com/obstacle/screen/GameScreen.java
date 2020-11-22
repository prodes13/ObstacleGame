package com.obstacle.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacle.assets.AssetPaths;
import com.obstacle.config.GameConfig;
import com.obstacle.entity.Obstacle;
import com.obstacle.entity.Player;
import com.obstacle.util.GdxUtils;
import com.obstacle.util.ViewportUtils;
import com.obstacle.util.debug.DebugCameraController;

public class GameScreen implements Screen {

	public static final Logger log = new Logger(GameScreen.class.getName(), Logger.DEBUG);

	private OrthographicCamera camera;
	private Viewport viewport;
	private ShapeRenderer renderer;

// second camera for the hud
	private OrthographicCamera hudCamera;
	private Viewport hudViewport;

	private SpriteBatch batch;
	private BitmapFont font;
	private BitmapFont scoreFont;
	private final GlyphLayout layout = new GlyphLayout();

	private Player player;
	private Array<Obstacle> obstacles = new Array<Obstacle>();
	private float obstacleTimer;
	private float scoreTimer;

	private int lives = GameConfig.LIVES_START;
	private int score;

	private DebugCameraController debugCameraController;

	@Override
	public void show () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
		renderer = new ShapeRenderer();

		hudCamera = new OrthographicCamera();
		hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal(AssetPaths.UI_FONT1));
		scoreFont = new BitmapFont(Gdx.files.internal(AssetPaths.UI_FONT2));
		// create player
		player = new Player();

		// calculate position
		float startPlayerX = GameConfig.WORLD_WIDTH / 2;
		float startPlayerY = 1;

//		float startPlayerX = 12;
//		float startPlayerY = 12;

		player.setPosition(startPlayerX, startPlayerY);

		// create deebug camera controller
		debugCameraController = new DebugCameraController();
		debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
	}

	@Override
	public void render (float delta) {
		// not inside alive, because we want to be able to move camera!
		debugCameraController.handleDebugInput(delta);
		debugCameraController.applyTo(camera);

		// clear screen
		GdxUtils.clearScreen();

		// update world
		update(delta);

		// render ui/hud
		renderUi();

		// render debug graphics
		renderDebug();
	}

	private void update(float delta) {
		updatePlayer();
		updateObstacles(delta);
		updateScore(delta);

		if(isPlayerCollidingWithObstacle()) {
//			alive = false;
			log.debug("Collision detected!!!");
			lives--;
		}
	}

	private boolean isPlayerCollidingWithObstacle() {

		for(Obstacle obstacle : obstacles) {
			if(obstacle.isPlayerColliding(player)) {
				return true;
			}
 		}

		return false;
	}

	private void updateScore(float delta) {
		scoreTimer += delta;
		if(scoreTimer >= GameConfig.SCORE_MAX_TIME) {
			score += MathUtils.random(1, 5);
			scoreTimer = 0.0f;
		}
	}

	private void updateObstacles(float delta) {
		for(Obstacle obstacle : obstacles) {
			obstacle.update();
		}

		createNewObstacle(delta);
	}

	private void createNewObstacle(float delta) {
		obstacleTimer += delta;
		if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
			float min = 0f;
			float max = GameConfig.WORLD_WIDTH;
			float obstacleX = MathUtils.random(min, max);
			float obstacleY = GameConfig.WORLD_HEIGHT;

			Obstacle obstacle = new Obstacle();
			obstacle.setPosition(obstacleX, obstacleY);

			obstacles.add(obstacle);
			obstacleTimer = 0f;


		}
	}

	private void updatePlayer() {
		log.debug("Player coordinates:  " + player.getX() + "  |  " + player.getY());
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

	private void renderUi() {
		batch.setProjectionMatrix(hudCamera.combined);
		batch.begin();

		String livesText = "LIVES: " + lives;
		layout.setText(font, livesText);

		font.draw(batch, livesText,
				20,
				GameConfig.HUD_HEIGHT - layout.height);

		String scoreText = "SCORE: " + score;
		layout.setText(scoreFont, scoreText);

		scoreFont.draw(batch, scoreText,
				GameConfig.HUD_WIDTH - layout.width - 20,
				GameConfig.HUD_HEIGHT - layout.height);

		batch.end();
	}

	private void renderDebug() {
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
			drawDebug();
		renderer.end();
		ViewportUtils.drawGrid(viewport, renderer);

	}

	public void drawDebug() {
		player.drawDebug(renderer);
		for(Obstacle obstacle : obstacles) {
			obstacle.drawDebug(renderer);
		}
	}

	@Override

	public void dispose () {
		renderer.dispose();
		batch.dispose();
		font.dispose();
		scoreFont.dispose();
	}

    @Override
    public void resize(int width, int height) {
		viewport.update(width, height, true);
		hudViewport.update(width, height, true);
		ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
