package com.patriots.simov.utils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Collider
{
    public Rectangle getRectangle() {
        return rectangle;
    }

    Rectangle rectangle;

    public Circle getCircle() {
        return circle;
    }

    Circle circle;
    Vector2 position, measure, auxPos;
    float radius;


    public Collider(Vector2 position, float width, float height)
    {
        this.position = position;
        measure = new Vector2(width, height);

        rectangle = new Rectangle(position.x, position.y, measure.x, measure.y);

    }

    public Collider(Vector2 position, float radius)
    {
        this.radius = radius;
        this.position = new Vector2(position.x + this.radius, position.y + this.radius);
        circle = new Circle(this.position, this.radius);
    }

    public void Update(Vector2 position)
    {
        this.position = position;
        rectangle.setPosition(this.position);
        rectangle.setWidth(measure.x);
        rectangle.setHeight(measure.y);
    }

    public void Update(Vector2 position, float radius)
    {

        this.radius = radius;
        this.position = new Vector2(position.x, position.y);
        circle.setPosition(this.position);
        circle.setRadius(this.radius);

    }

    public void DrawCircle(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.circle(position.x, position.y, radius);
    }

    public void DrawRectangle(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.rect(position.x, position.y, measure.x, measure.y);
    }

    public boolean CheckCollisionCircle(Vector2 pos)
    {
        if (circle.contains(pos))
            return true;

        return false;
    }

    public boolean CheckCollisionRectangle(Vector2 pos)
    {
        if (rectangle.contains(pos))
            return true;

        return false;
    }

    public boolean CheckCollisionRectangle(Rectangle rec)
    {
        if (rectangle.overlaps(rec))
            return true;

        return false;
    }




}