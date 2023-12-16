package com.patriots.simov.UI;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameManager extends ApplicationAdapter
{
	private SpriteBatch spriteBatch;
	private StateManager stateManager;

	@Override
	public void create ()
	{
		spriteBatch = new SpriteBatch();
		stateManager = new StateManager();

		stateManager.push(new MenuState(stateManager));
	}

	@Override
	public void render ()
	{
		Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stateManager.update(Gdx.graphics.getDeltaTime());
		stateManager.render(spriteBatch);
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}
}