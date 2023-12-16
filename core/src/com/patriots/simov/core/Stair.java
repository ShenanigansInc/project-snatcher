package com.patriots.simov.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.patriots.simov.utils.Collider;

public class Stair
{
    private Vector2 position;
    private Sprite sprite;

    public Collider getCollider() {
        return collider;
    }

    private Collider collider;

    public Collider getAuxCollider() {
        return auxCollider;
    }

    private Collider auxCollider;

    public Stair(Vector2 position, Texture texture)
    {
        this.position = position;
        sprite = new Sprite(texture, texture.getWidth(), texture.getHeight());
        collider = new Collider(new Vector2(position.x + texture.getWidth() / 4, position.y - 5), sprite.getWidth() / 2, sprite.getHeight() * 1.5f);
        auxCollider = new Collider(new Vector2(position.x, position.y), sprite.getWidth(), 2);
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
        shapeRenderer.setColor(Color.BLUE);
        auxCollider.DrawRectangle(shapeRenderer);
        shapeRenderer.end();
    }

}
