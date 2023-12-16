package com.patriots.simov.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.patriots.simov.utils.Collider;

public class MultiplayerMenuState extends State
{
    private Texture hostGameButtonTexture, joinGameButtonTexture;
    private Sprite hostGameButton, joinGameButton;
    private Collider hostGameButtonCollider, joinGameButtonCollider;
    private Vector3 input;
    private Vector2 hostGameButtonPosition, joinGameButtonPosition;
    private float hostGameButtonWidth, hostGameButtonHeight, joinGameButtonWidth, joinGameButtonHeight;

    private TextureAtlas multiAtlas;
    private Animation multiAnimation;
    private float stateTime = 0;

    public MultiplayerMenuState(StateManager stateManager)
    {
        super(stateManager);

        worldCamera.setToOrtho(false);
        hostGameButtonTexture = new Texture("host.png");
        joinGameButtonTexture = new Texture("join.png");
        hostGameButton = new Sprite(hostGameButtonTexture);
        joinGameButton = new Sprite(joinGameButtonTexture);

        hostGameButtonWidth = worldCamera.viewportWidth / 2;
        hostGameButtonHeight = worldCamera.viewportHeight / 6;
        joinGameButtonWidth = worldCamera.viewportWidth / 2;
        joinGameButtonHeight = worldCamera.viewportHeight / 6;

        hostGameButtonPosition = new Vector2(worldCamera.viewportWidth / 2 - hostGameButtonWidth / 2, worldCamera.viewportHeight / 4);
        joinGameButtonPosition = new Vector2(worldCamera.viewportWidth / 2 - joinGameButtonWidth / 2, worldCamera.viewportHeight / 4 - joinGameButtonHeight);

        hostGameButtonCollider = new Collider(hostGameButtonPosition, hostGameButtonWidth, hostGameButtonHeight);
        joinGameButtonCollider = new Collider(joinGameButtonPosition, joinGameButtonWidth, joinGameButtonHeight);

        multiAtlas = new TextureAtlas(Gdx.files.internal("MultiPlayerPack.pack"));
        multiAnimation = new Animation(0.1f, multiAtlas.getRegions());
    }

    @Override
    public void handleInput()
    {
        input = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
        worldCamera.unproject(input);

        if(Gdx.input.justTouched())
        {
            if(hostGameButtonCollider.CheckCollisionRectangle(new Vector2(input.x, input.y)))
            {
                System.out.println("HOST GAME");
                stateManager.set(new FirstLevel(stateManager, true, true));
            }
            else if(joinGameButtonCollider.CheckCollisionRectangle(new Vector2(input.x, input.y)))
            {
                System.out.println("JOIN GAME");
                stateManager.set(new FirstLevel(stateManager, false, true));
            }
        }

        Gdx.input.setCatchBackKey(true);

        if(Gdx.input.isKeyPressed(Input.Keys.BACK))
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
        TextureRegion frame = multiAnimation.getKeyFrame(stateTime, true);


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

        spriteBatch.draw(hostGameButton, hostGameButtonPosition.x, hostGameButtonPosition.y, hostGameButtonWidth, hostGameButtonHeight);
        spriteBatch.draw(joinGameButton, joinGameButtonPosition.x, joinGameButtonPosition.y, joinGameButtonWidth, joinGameButtonHeight);
        spriteBatch.end();
    }

    @Override
    public void dispose()
    {

    }
}