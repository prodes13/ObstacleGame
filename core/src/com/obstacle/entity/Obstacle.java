package com.obstacle.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import com.obstacle.config.GameConfig;

public class Obstacle extends GameObjectBase implements Pool.Poolable {

    private float ySpeed = GameConfig.MEDIUM_OBSTACLE_SPEED;
    private boolean hit;

    public Obstacle() {
        super(GameConfig.OBSTACLE_BOUNDS_RADIUS);
        setSize(GameConfig.OBSTACLE_SIZE, GameConfig.OBSTACLE_SIZE);
    }

    public boolean isPlayerColliding(Player player) {
        Circle playerBounds = player.getBounds();
        // player with obstacle
        boolean overlaps = Intersector.overlaps(playerBounds, getBounds());

//      better way for hitting states
        hit = overlaps;

        return overlaps;
    }

    public boolean isNotHit() {
        return !hit;
    }

    public void update() {
        setPosition(getX(), getY() - ySpeed);
    }

    public void setYSpeed(float obstacleSpeed) {
        this.ySpeed = obstacleSpeed;
    }

    @Override
    public void reset() {
        hit = false;
    }
}
