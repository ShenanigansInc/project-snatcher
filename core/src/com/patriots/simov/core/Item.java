package com.patriots.simov.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.patriots.simov.utils.Collider;

import java.util.Random;

public class Item
{
    private Vector2 position, itemMeasures;
    private TextureAtlas itemAtlas;
    private Animation itemAnimation;

    private Collider collider;

    public Collider getCollider()
    {
        return collider;
    }

    private Random random;
    private float stateTime = 0;

    public Item(Vector2 position)
    {
        this.position = position;
        itemMeasures = new Vector2(60, 40);
        itemAtlas = new TextureAtlas(Gdx.files.internal("ItemPack.pack"));
        itemAnimation = new Animation(0.1f, itemAtlas.getRegions());

        collider = new Collider(new Vector2(this.position.x + itemMeasures.x / 4,this.position.y + itemMeasures.y / 4),
                itemMeasures.x / 2, itemMeasures.y / 2);

    }

    public void Update(Player player)
    {

    }

    private TextureRegion getFrameFromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame = itemAnimation.getKeyFrame(stateTime, true);

        if(stateTime < 0.3f)
            stateTime += deltaTime * 0.5f;
        else stateTime = 0;

        return frame;
    }

    public void Draw(SpriteBatch spriteBatch)
    {
        spriteBatch.begin();
        spriteBatch.draw(getFrameFromCurrentAnimation(Gdx.graphics.getDeltaTime()), position.x, position.y, itemMeasures.x, itemMeasures.y);
        spriteBatch.end();
    }

    public void DrawCollider(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin();
        collider.DrawRectangle(shapeRenderer);
        shapeRenderer.end();
    }




}
