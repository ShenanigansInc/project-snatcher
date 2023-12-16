package com.patriots.simov.UI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State
{
    protected OrthographicCamera worldCamera;
    protected OrthographicCamera interfaceCamera;
    protected Vector3 mouse;
    protected StateManager stateManager;

    public State(StateManager gameStateManager)
    {
        this.stateManager = gameStateManager;
        worldCamera = new OrthographicCamera();
        interfaceCamera = new OrthographicCamera();
        mouse = new Vector3();
    }

    public abstract void handleInput();
    public abstract void update(float deltaTime);
    public abstract void render(SpriteBatch spriteBatch);
    public abstract void dispose();
}