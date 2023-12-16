package com.patriots.simov.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.patriots.simov.utils.Collider;

public class MenuState extends State
{
    private Texture sPBText, mPBText;
    private Sprite singlePlayerButton, multiPlayerButton;
    private Collider sPBCollider, mPBCollider;
    private Vector3 input;
    private Vector2 sPBPosition, mPBPosition;
    private float sPBWidth, sPBHeight, mPBWidth, mPBHeight;

    private TextureAtlas menu1Atlas;
    private Animation menu1Animation;
    private float stateTime1 = 0;

    private TextureAtlas menu2Atlas;
    private Animation menu2Animation;
    private float stateTime2 = 0;

    private TextureAtlas menu3Atlas;
    private Animation menu3Animation;
    private float stateTime3 = 0;

    public MenuState(StateManager stateManager)
    {
        super(stateManager);
        worldCamera.setToOrtho(false);
        sPBText = new Texture("singleplayerB1.png");
        mPBText = new Texture("MultiplayerB1.png");
        singlePlayerButton = new Sprite(sPBText);
        multiPlayerButton = new Sprite(mPBText);

        /*
        sPBWidth = singlePlayerButton.getWidth();
        sPBHeight = singlePlayerButton.getHeight();
        mPBWidth = multiPlayerButton.getWidth();
        mPBHeight = multiPlayerButton.getHeight();
        */

        sPBWidth = worldCamera.viewportWidth / 2;
        sPBHeight = worldCamera.viewportHeight / 6;
        mPBWidth = worldCamera.viewportWidth / 2;
        mPBHeight = worldCamera.viewportHeight / 6;

        sPBPosition = new Vector2(worldCamera.viewportWidth / 2 - sPBWidth / 2, worldCamera.viewportHeight / 4);
        mPBPosition = new Vector2(worldCamera.viewportWidth / 2 - mPBWidth / 2, worldCamera.viewportHeight / 4 - mPBHeight);

        sPBCollider = new Collider(sPBPosition, sPBWidth, sPBHeight);
        mPBCollider = new Collider(mPBPosition, mPBWidth, mPBHeight);

        menu1Atlas = new TextureAtlas(Gdx.files.internal("menu_Animation1.pack"));
        menu1Animation = new Animation(0.1f, menu1Atlas.getRegions());

        menu2Atlas = new TextureAtlas(Gdx.files.internal("menu_Animation2.pack"));
        menu2Animation = new Animation(0.1f, menu2Atlas.getRegions());

        menu3Atlas = new TextureAtlas(Gdx.files.internal("MenuPack.pack"));
        menu3Animation = new Animation(0.1f, menu3Atlas.getRegions());
    }

    @Override
    public void handleInput()
    {
        input = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
        worldCamera.unproject(input);

        if(Gdx.input.justTouched())
        {
            if(sPBCollider.CheckCollisionRectangle(new Vector2(input.x, input.y)))
            {
                stateManager.set(new FirstLevel(stateManager, false, false));
            }

            else if(mPBCollider.CheckCollisionRectangle(new Vector2(input.x, input.y)))
            {
                stateManager.push(new MultiplayerMenuState(stateManager));
            }
        }
    }

    @Override
    public void update(float deltaTime)
    {
        handleInput();
    }

    private TextureRegion getFrameMenu1FromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame = menu1Animation.getKeyFrame(stateTime1, true);


        if(stateTime1 < 2.4f)
            stateTime1 += deltaTime * 0.5f;
        else stateTime1 = 0;

        return frame;
    }

    private TextureRegion getFrameMenu2FromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame = menu2Animation.getKeyFrame(stateTime2, true);


        if(stateTime2 < 0.7f)
            stateTime2 += deltaTime * 0.5f;
        else stateTime2 = 0;

        return frame;
    }

    private TextureRegion getFrameMenu3FromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame = menu3Animation.getKeyFrame(stateTime3, true);


        if(stateTime3 < 0.2f)
            stateTime3 += deltaTime * 0.5f;
        else stateTime3 = 0;

        return frame;
    }



    @Override
    public void render(SpriteBatch spriteBatch)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(worldCamera.combined);

        spriteBatch.begin();
        spriteBatch.draw(getFrameMenu3FromCurrentAnimation(Gdx.graphics.getDeltaTime()), 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);
        spriteBatch.draw(getFrameMenu1FromCurrentAnimation(Gdx.graphics.getDeltaTime()), 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);
        spriteBatch.draw(getFrameMenu2FromCurrentAnimation(Gdx.graphics.getDeltaTime()), 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);

        //spriteBatch.draw(background, 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);
        spriteBatch.draw(singlePlayerButton, sPBPosition.x, sPBPosition.y, sPBWidth, sPBHeight);
        spriteBatch.draw(multiPlayerButton, mPBPosition.x, mPBPosition.y, mPBWidth, mPBHeight);
        spriteBatch.end();
    }

    @Override
    public void dispose()
    {
    }
}

