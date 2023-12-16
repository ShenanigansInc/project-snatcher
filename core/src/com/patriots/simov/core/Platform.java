package com.patriots.simov.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.patriots.simov.utils.Collider;

public class Platform
{
    public Vector2 getPosition() {
        return position;
    }

    private Vector2 position;

    public Sprite getSprite() {
        return sprite;
    }

    private Sprite sprite;

    public Collider getCollider() {
        return collider;
    }

    private Collider collider;

    public Platform(Vector2 position, Texture texture)
    {
        this.position = position;
        sprite = new Sprite(texture, texture.getWidth(), texture.getHeight());
        collider = new Collider(position, sprite.getWidth(), sprite.getHeight());
    }

    public void Draw(SpriteBatch spriteBatch)
    {
        spriteBatch.begin();
        spriteBatch.draw(sprite, position.x, position.y);
        spriteBatch.end();
    }

    public void DrawCollider(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin();
        collider.DrawRectangle(shapeRenderer);
        shapeRenderer.end();
    }
}
