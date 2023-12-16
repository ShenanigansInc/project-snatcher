package com.patriots.simov.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.patriots.simov.core.Guard;
import com.patriots.simov.core.Player;

import java.util.List;

public class GameOverState extends State
{
    private TextureAtlas gameOverAtlas;
    private Animation gameOverAnimation;
    private float stateTime = 0;

    private boolean killApp = false;

    public GameOverState(StateManager stateManager, Vector3 checkpointPosition, Player player, List<Guard> guards)
    {
        super(stateManager);
        worldCamera.setToOrtho(false);

        gameOverAtlas = new TextureAtlas(Gdx.files.internal("GameOverPack.pack"));
        gameOverAnimation = new Animation(0.1f, gameOverAtlas.getRegions());

        player.setPosition(new Vector3(checkpointPosition.x - player.getPlayerWidth()/2, checkpointPosition.y, 0));
        player.playerAction = Player.PlayerActions.IDLE;
        player.setDead(false);

        for (Guard g : guards)
        {
            if(g.isRegularGuard())
                g.ResetGuard(new Vector2(1420, 250), true);
            else g.ResetGuard(new Vector2(500, 450), false);
        }
    }

    public GameOverState(StateManager stateManager, boolean killApp)
    {
        super(stateManager);
        worldCamera.setToOrtho(false);

        gameOverAtlas = new TextureAtlas(Gdx.files.internal("GameOverPack.pack"));
        gameOverAnimation = new Animation(0.1f, gameOverAtlas.getRegions());

        this.killApp = killApp;
    }


    @Override
    public void handleInput()
    {
        if(Gdx.input.justTouched())
        {
            if (!killApp)
            {
                stateManager.pop();
            }
            else
            {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void update(float deltaTime)
    {
        handleInput();
    }

    private TextureRegion getFrameFromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame = gameOverAnimation.getKeyFrame(stateTime, true);


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
        gameOverAtlas.dispose();
    }
}
