package com.patriots.simov.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.patriots.simov.utils.Collider;

public class Switch
{

    private Vector2 position;

    public Collider getCollider()
    {
        return collider;
    }

    public enum SwitchActions
    {
        OFF,
        ON
    }

    public SwitchActions switchActions;
    private TextureAtlas switchAtlas;
    private Animation switchAnimation;

    private Collider collider, colliderForPlayer;

    private Vector3 input;
    private boolean playOnce;

    public Switch()
    {
        position = new Vector2(1200,70);
        collider = new Collider(position, 70, 100);
        colliderForPlayer = new Collider(new Vector2(position.x - 40, position.y - 15), 140, 100);

        switchAtlas = new TextureAtlas(Gdx.files.internal("TurnOff.pack"));
        switchAnimation = new Animation(0.1f, switchAtlas.getRegions());

        input = Vector3.Zero;

        playOnce = false;
        switchActions = SwitchActions.OFF;
    }

    private TextureRegion getFrameFromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame;

        switch (switchActions)
        {
            case OFF:
            default:
                frame = switchAnimation.getKeyFrame(0.2f, true);
                break;
            case ON:
                frame = switchAnimation.getKeyFrame(0.1f, true);
                break;
        }

        return frame;
    }

    public void Update(OrthographicCamera camera, Player player, Laser laser)
    {
        input = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
        camera.unproject(input);

        if(player.getPlayerCollider().getRectangle().overlaps(colliderForPlayer.getRectangle()))
        {
            if(Gdx.input.isTouched())
            {
                if(collider.getRectangle().contains(new Vector2(input.x, input.y)) && !playOnce)
                {
                    switchActions = switchActions.ON;
                    laser.laserActions = laser.laserActions.OFF;
                    playOnce = true;

                }
            }
        }

        if(player.playerAction == player.playerAction.DYING)
        {
            playOnce = false;
            switchActions = switchActions.OFF;
        }
        //System.out.println(switchActions);

    }

    public void Draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
    {
        spriteBatch.begin();
        spriteBatch.draw(getFrameFromCurrentAnimation(Gdx.graphics.getDeltaTime()), position.x, position.y, 70, 100);
        spriteBatch.end();

        /*
        shapeRenderer.begin();
        collider.DrawRectangle(shapeRenderer);
        colliderForPlayer.DrawRectangle(shapeRenderer);
        shapeRenderer.end();
        */
    }

}
