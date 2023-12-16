package com.patriots.simov.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.patriots.simov.utils.Collider;

public class Trap
{

    public Vector2 getPosition() {
        return position;
    }

    private Vector2 position;
    private Collider collider;

    public Collider getCollider()
    {
        return collider;
    }

    public Trap(Vector2 position)
    {
        this.position = position;
        collider = new Collider(this.position, 50, 50);

    }

    public void Update()
    {
    }

    public void Draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        collider.DrawRectangle(shapeRenderer);
        shapeRenderer.end();
    }

}
