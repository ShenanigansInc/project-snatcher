package com.patriots.simov.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WinState extends State
{
    private Texture background;

    public WinState(StateManager stateManager)
    {
        super(stateManager);
        worldCamera.setToOrtho(false);
        background = new Texture(Gdx.files.internal("Winner.png"));

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void handleInput()
    {
        if(Gdx.input.justTouched())
        {
            Gdx.app.exit();
        }
    }

    @Override
    public void update(float deltaTime)
    {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {
        spriteBatch.setProjectionMatrix(worldCamera.combined);

        spriteBatch.begin();

        spriteBatch.draw(background, 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);

        spriteBatch.end();
    }

    @Override
    public void dispose()
    {
        background.dispose();
    }
}
