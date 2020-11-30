package com.obstacle.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacle.assets.AssetDescriptors;
import com.obstacle.assets.AssetPaths;
import com.obstacle.config.GameConfig;
import com.obstacle.entity.Background;
import com.obstacle.entity.Obstacle;
import com.obstacle.entity.Player;
import com.obstacle.util.GdxUtils;
import com.obstacle.util.ViewportUtils;
import com.obstacle.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {
    // -- atributes
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
    private DebugCameraController debugCameraController;
    private final GameController controller;
    private final AssetManager assetManager;

    // textures
    private Texture playerTexture;
    private Texture obstacleTexture;
    private Texture backgroundTexture;

    // -- constructor
    public GameRenderer(AssetManager assetManager, GameController controller) {
        this.assetManager = assetManager;
        this.controller = controller;
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        batch = new SpriteBatch();
        font = assetManager.get(AssetDescriptors.FONT1);
        scoreFont = assetManager.get(AssetDescriptors.FONT2);


        // create deebug camera controller
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        // textures
        // Gdx.files.internal points to the assets folder
        playerTexture = assetManager.get(AssetDescriptors.PLAYER);
        obstacleTexture = assetManager.get(AssetDescriptors.OBSTACLE);
        backgroundTexture = assetManager.get(AssetDescriptors.BACKGROUND);
    }

    private void renderGamePlay() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // draw background
        Background background = controller.getBackground();
        batch.draw(backgroundTexture,
                background.getX(),
                background.getY(),
                background.getWidth(),
                background.getHeight());

        // draw obstacles
        for(Obstacle obstacle : controller.getObstacles()) {
            batch.draw(obstacleTexture, obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }

        // draw player
        Player player = controller.getPlayer();
        batch.draw(playerTexture, player.getX(), player.getY(), player.getWidth(), player.getHeight());

        batch.end();
    }

    public void render(float delta) {
        // not inside alive, because we want to be able to move camera!
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        if(Gdx.input.isTouched() && !controller.isGameOver()) {
            Vector2 screenTouch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            // projecting screen to viewport
            Vector2 worldTouch = viewport.unproject(new Vector2(screenTouch));
            System.out.println("worldTouch = " + worldTouch);

            Player player = controller.getPlayer();
            worldTouch.x = MathUtils.clamp(worldTouch.x, 0, GameConfig.WORLD_WIDTH - player.getWidth());
            player.setX(worldTouch.x);
        }

        // clear screen
        GdxUtils.clearScreen();

        renderGamePlay();

        // render ui/hud
        renderUi();

        // render debug graphics
        renderDebug();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void dispose () {
        renderer.dispose();
        batch.dispose();
    }

    private void renderUi() {
        hudViewport.apply();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        String livesText = "LIVES: " + controller.getLives();
        layout.setText(font, livesText);

        font.draw(batch, livesText,
                20,
                GameConfig.HUD_HEIGHT - layout.height);

        String scoreText = "SCORE: " + controller.getDisplayScore();
        layout.setText(scoreFont, scoreText);

        scoreFont.draw(batch, scoreText,
                GameConfig.HUD_WIDTH - layout.width - 20,
                GameConfig.HUD_HEIGHT - layout.height);

        batch.end();
    }

    private void renderDebug() {
        // fixing problems that can arise in future
        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        renderer.end();
        ViewportUtils.drawGrid(viewport, renderer);

    }

    public void drawDebug() {
        Player player = controller.getPlayer();
        player.drawDebug(renderer);

        Array<Obstacle> obstacles = controller.getObstacles();

        for(Obstacle obstacle : obstacles) {
            obstacle.drawDebug(renderer);
        }
    }

}
