package com.obstacle.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

// using it for behavior
public abstract class GameObjectBase {
    private float x;
    private float y;
    private float width = 1;
    private float height = 1;

    private Circle bounds;

    public GameObjectBase(float boundsRadius) {
        bounds = new Circle(x, y, boundsRadius);
    }


    public void drawDebug(ShapeRenderer renderer) {
        // draws center of circle
        renderer.x(bounds.x, bounds.y, 0.1f);
        renderer.circle(bounds.x, bounds.y, bounds.radius, 30);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void updateBounds() {
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        // moving the center, so that it correspond the texture drawing form bottom o, o
        bounds.setPosition( x + halfWidth, y + halfHeight);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
        updateBounds();
    }

    public void setY(float ySpeed) {
        this.y += ySpeed;
        updateBounds();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updateBounds();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Circle getBounds() {
        return bounds;
    }
}
