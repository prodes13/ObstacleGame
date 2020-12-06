package com.obstacle;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Logger;
import com.obstacle.screen.loading.LoadingScreen;

public class ObstacleGame extends Game {

	private AssetManager assetManager;
	// don't make asset manager static it causes bugs and memory leaks

	@Override
	public void create() {
		Gdx.app.setLogLevel((Application.LOG_DEBUG));

		assetManager = new AssetManager();
		assetManager.getLogger().setLevel(Logger.DEBUG);

		// passing our game .. it means this
		setScreen(new LoadingScreen(this));
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}

