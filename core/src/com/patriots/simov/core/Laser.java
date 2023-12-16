package com.patriots.simov.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.patriots.simov.utils.Collider;

public class Laser
{
    private Vector2 position;
    private Sprite sprite;

    public Collider getCollider()
    {
        return collider;
    }

    public enum LaserActions
    {
        OFF,
        TURNINGON,
        ON
    }

    public LaserActions laserActions;
    private TextureAtlas laserAtlas;
    private Animation laserAnimation;

    private Collider collider;
    private float stateTime = 0;
    private float animationTime;
    private boolean animated, turnOn;
    private Vector2 colliderSize;
    float startAnimation, countAux;

    public Laser(Vector2 position, boolean turnOn, boolean animated, float startAnimation)
    {
        this.position = position;
        this.turnOn = turnOn;
        this.animated = animated;
        this.startAnimation = startAnimation;

        colliderSize = new Vector2(8, 140);
        collider = new Collider(new Vector2(position.x + 4, position.y), colliderSize.x, colliderSize.y);

        laserAtlas = new TextureAtlas(Gdx.files.internal("LaserPack.pack"));
        laserAnimation = new Animation(0.1f, laserAtlas.getRegions());

        stateTime = 0;
        countAux = 0;

        if(turnOn)
            laserActions = LaserActions.ON;
        else
            laserActions = LaserActions.OFF;

        if(animated)
            laserActions = LaserActions.OFF;

    }

    private TextureRegion getFrameFromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame;

        switch (laserActions)
        {
            case OFF:
            default:
                frame = laserAnimation.getKeyFrame(0, true);
                turnOn = false;
                break;
            case TURNINGON:
                frame = laserAnimation.getKeyFrame(stateTime, false);
                turnOn = true;
                if(stateTime > 0.3f)
                {
                    laserActions = LaserActions.ON;
                }
                break;
            case ON:
                frame = laserAnimation.getKeyFrame(0.3f, true);
                turnOn = true;
                break;
        }

        if(laserActions == LaserActions.TURNINGON)
            stateTime += deltaTime * 10;
        else stateTime = 0;

        return frame;
    }

    public void Update(float deltaTime)
    {
        if(animated)
        {
            if(countAux < 5)
                countAux += deltaTime;

            if(countAux > startAnimation)
            {
                animationTime += deltaTime;

                if (animationTime >= 1.95f && animationTime <= 1.97f)
                {
                    laserActions = LaserActions.TURNINGON;
                }
                else if (animationTime > 5)
                {
                    laserActions = LaserActions.OFF;
                    animationTime = 0;
                }
            }
        }
    }

    public void checkCollision(Player player)
    {
        if(turnOn)
        {
            if(collider.getRectangle().overlaps(player.getPlayerCollider().getRectangle()))
            {
                if(!player.isDisableGameOver())
                    player.playerAction = player.playerAction.DYING;
            }
        }
    }

    public void Draw(SpriteBatch spriteBatch)
    {
        spriteBatch.begin();
        spriteBatch.draw(getFrameFromCurrentAnimation(Gdx.graphics.getDeltaTime()), position.x, position.y, 16, 146);
        spriteBatch.end();
    }

    public void DrawCollider(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin();
        collider.DrawRectangle(shapeRenderer);
        shapeRenderer.end();
    }

}
