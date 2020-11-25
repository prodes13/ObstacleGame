package com.obstacle.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.obstacle.config.GameConfig;

public class Obstacle extends GameObjectBase {

    private static final float BOUNDS_RADIUS = 0.4f; // world units
    private static final float SIZE = 2 * BOUNDS_RADIUS;


    private float ySpeed = GameConfig.MEDIUM_OBSTACLE_SPEED;
    private boolean hit;

    public Obstacle() {
        super(BOUNDS_RADIUS);
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

    public float getWidth() { return SIZE;}

    public void setYSpeed(float obstacleSpeed) {
        this.ySpeed = obstacleSpeed;
    }
}
