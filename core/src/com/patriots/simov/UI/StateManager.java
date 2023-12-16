package com.patriots.simov.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;

public class StateManager
{
    private Stack<State> states;

    public StateManager()
    {
        states = new Stack<State>();
    }

    public void push(State state)
    {
        states.push(state);
    }

    public void pop()
    {
        states.pop().dispose();
    }

    public void set(State state)
    {
        states.pop().dispose();
        states.push(state);
    }

    public void update(float deltaTime)
    {
        states.peek().update(deltaTime);
    }

    public void render(SpriteBatch spriteBatch)
    {
        states.peek().render(spriteBatch);
    }
}
