package com.patriots.simov.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.patriots.simov.utils.Collider;

public class SoundWave
{
    Sprite soundWave;
    Vector2 soundWavePosition, soundWaveColPos;

    public float getMaxCircleRadius()
    {
        return maxCircleRadius;
    }

    float maxCircleRadius;

    public float getRadius() {
        return radius;
    }

    float radius;
    Collider soundWaveCollider;

    public SoundWave(Texture img, Vector2 position, float maxCircleRadius)
    {
        soundWave = new Sprite(img, img.getWidth(), img.getHeight());
        radius = 50;
        soundWaveColPos = position;
        soundWavePosition = new Vector2(soundWaveColPos.x - radius/2, soundWaveColPos.y - radius/2);

        this.maxCircleRadius = maxCircleRadius;

        soundWaveCollider = new Collider(Vector2.Zero, 0);

    }

    public void Update(float deltaTime)
    {
        radius += deltaTime * 300;
        soundWavePosition = new Vector2(soundWaveColPos.x - radius/2, soundWaveColPos.y - radius/2);
        soundWaveCollider.Update(soundWaveColPos, radius / 2);
    }

    public void Draw(SpriteBatch spriteBatch)
    {

        spriteBatch.draw(soundWave, soundWavePosition.x, soundWavePosition.y, radius, radius);

    }

    public void DrawCollider(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        soundWaveCollider.DrawCircle(shapeRenderer);
        shapeRenderer.end();
    }

}
