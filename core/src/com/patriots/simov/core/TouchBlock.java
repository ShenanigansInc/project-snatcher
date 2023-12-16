package com.patriots.simov.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.patriots.simov.utils.Collider;

import java.util.ArrayList;
import java.util.List;

public class TouchBlock
{
    Vector3 input;
    Sprite box;
    Vector2 boxPosition, boxMeasures;
    float maxCircleRadius;
    Collider boxCollider;

    public List<SoundWave> getSoundwaves()
    {
        return soundwaves;
    }

    List<SoundWave> soundwaves = new ArrayList<SoundWave>();
    Texture soundWaveTexture;

    public TouchBlock(Texture boxTex, Texture circleTex, Vector2 boxPosition, float maxCircleRadius)
    {
        input = new Vector3();
        this.maxCircleRadius = maxCircleRadius;

        soundWaveTexture = circleTex;

        this.boxPosition = boxPosition;
        box = new Sprite(boxTex, boxTex.getWidth(), boxTex.getHeight());
        boxMeasures = new Vector2(boxTex.getWidth(), boxTex.getHeight());

        boxCollider = new Collider(this.boxPosition, boxMeasures.x, boxMeasures.y);
    }

    public void Update(float deltaTime, OrthographicCamera camera)
    {
        input = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
        camera.unproject(input);

        boxCollider.Update(boxPosition);

        if(Gdx.input.justTouched())
        {
            if(boxCollider.CheckCollisionRectangle(new Vector2(input.x, input.y)))
            {
                SoundWave soundwave = new SoundWave(soundWaveTexture,new Vector2(input.x, input.y), maxCircleRadius );
                soundwaves.add(soundwave);
            }
        }

        for (SoundWave soundWave : soundwaves)
        {
            soundWave.Update(deltaTime);

            if (soundWave.getRadius() > soundWave.getMaxCircleRadius() && soundwaves.size() != 0)
            {
                soundwaves.remove(soundWave);
                break;
            }

        }

    }

    public void Draw(SpriteBatch spriteBatch)
    {
        spriteBatch.begin();
        spriteBatch.draw(box, boxPosition.x, boxPosition.y);

        for (SoundWave soundWave : soundwaves)
        {
            soundWave.Draw(spriteBatch);
        }
        spriteBatch.end();


    }

    public void DrawColl(ShapeRenderer shapeRenderer)
    {
        for (SoundWave soundWave : soundwaves)
        {
            soundWave.DrawCollider(shapeRenderer);
        }
    }



}
