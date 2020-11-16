package com.obstacle.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacle.config.GameConfig;
import com.obstacle.entity.Player;
import com.obstacle.util.GdxUtils;
import com.obstacle.util.ViewportUtils;

public class GameScreen implements Screen {

	public static final Logger log = new Logger(GameScreen.class.getName(), Logger.DEBUG);

	private OrthographicCamera camera;
	private Viewport viewport;
	private ShapeRenderer renderer;
	private Player player;

	@Override
	public void show () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
		renderer = new ShapeRenderer();

		// create player
		player = new Player();

		// calculate position
		float startPlayerX = GameConfig.WORLD_WIDTH / 2;
		float startPlayerY = 1;

		player.setPosition(startPlayerX, startPlayerY);
	}

	@Override
	public void render (float delta) {
		// update world
		update(delta);

		// clear screen
		GdxUtils.clearScreen();

		// render debug graphics
		renderDebug();
	}

	private void update(float delta) {
		updatePlayer();

	}

	private void updatePlayer() {
		log.debug("Player COORDS:  " + player.getX() + "  |  " + player.getY());
		player.update();

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
	}

	@Override

	public void dispose () {
		renderer.dispose();
	}

    @Override
    public void resize(int width, int height) {
		viewport.update(width, height, true);
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
