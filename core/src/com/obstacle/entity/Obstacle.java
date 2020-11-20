package com.obstacle.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class Obstacle extends GameObjectBase {

    private static final float BOUNDS_RADIUS = 0.4f; // world units
    private static final float SIZE = 2 * BOUNDS_RADIUS;


    private float ySpeed = 0.1f;

    public Obstacle() {
        super(BOUNDS_RADIUS);
    }

    public boolean isPlayerColliding(Player player) {
        Circle playerBounds = player.getBounds();
        // player with obstacle
        return Intersector.overlaps(playerBounds, getBounds());
    }

    public void update() {
        setPosition(getX(), getY() - ySpeed);
    }

    public float getWidth() { return SIZE;}
}
