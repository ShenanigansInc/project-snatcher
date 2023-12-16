package com.patriots.simov.core;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.patriots.simov.utils.Collider;

public class Checkpoint
{
    public Vector3 getPosition() {
        return position;
    }

    private Vector3 position;

    public Collider getCollider() {
        return collider;
    }

    private Collider collider;

    public Checkpoint(Vector3 position)
    {
        this.position = position;

        collider = new Collider(new Vector2(this.position.x, this.position.y), 0, 120);
    }

    public void update(Collider col)
    {
        collider.Update(new Vector2(position.x, position.y));
        //collider.CheckCollisionRectangle(col.getRectangle());
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin();

        collider.DrawRectangle(shapeRenderer);

        shapeRenderer.end();
    }
}
