package com.obstacle;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.obstacle.screen.GameScreen;

public class ObstacleGame extends Game {
	@Override
	public void create() {
		Gdx.app.setLogLevel((Application.LOG_DEBUG));
		setScreen(new GameScreen());
	}
}

