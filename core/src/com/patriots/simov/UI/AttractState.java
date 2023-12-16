package com.patriots.simov.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AttractState extends State
{
    private Texture background;

    public AttractState(StateManager stateManager)
    {
        super(stateManager);
        worldCamera.setToOrtho(false);
        background = new Texture(Gdx.files.internal("attract2.png"));
    }


    @Override
    public void handleInput()
    {
        if(Gdx.input.justTouched())
        {
            stateManager.set(new MenuState(stateManager));
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
