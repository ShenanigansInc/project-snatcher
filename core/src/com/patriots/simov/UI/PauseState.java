package com.patriots.simov.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PauseState extends State
{

    private TextureAtlas pauseAtlas;
    private Animation pauseAnimation;
    private float stateTime = 0;

    public PauseState(StateManager stateManager)
    {
        super(stateManager);
        worldCamera.setToOrtho(false);

        pauseAtlas = new TextureAtlas(Gdx.files.internal("PausePack.pack"));
        pauseAnimation = new Animation(0.1f, pauseAtlas.getRegions());

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void handleInput()
    {
        if(Gdx.input.justTouched())
        {
            stateManager.pop();
        }
    }

    @Override
    public void update(float deltaTime)
    {
        handleInput();
    }

    private TextureRegion getFrameFromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame = pauseAnimation.getKeyFrame(stateTime, true);


        if(stateTime < 0.61f)
            stateTime += deltaTime;
        else stateTime = 0;

        return frame;
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {
        spriteBatch.setProjectionMatrix(worldCamera.combined);

        spriteBatch.begin();

        spriteBatch.draw(getFrameFromCurrentAnimation(Gdx.graphics.getDeltaTime()), 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);

        spriteBatch.end();
    }

    @Override
    public void dispose()
    {
        pauseAtlas.dispose();
    }
}
