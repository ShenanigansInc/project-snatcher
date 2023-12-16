package com.patriots.simov.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.patriots.simov.utils.Collider;

public class Camera
{
    private Vector2 colliderPos;
    private Collider collider;
    private Vector2 cameraPosition;

    public Collider getCollider()
    {
        return collider;
    }

    private TextureAtlas cameraAtlas;
    private Animation cameraAnimation;

    private float stateTime = 0;
    private float countAux = 0;

    public enum CameraActions
    {
        Stop,
        Move
    }

    private CameraActions cameraActions;
    private float colliderCount = 0;
    boolean moveLeft, stop;

    public Camera(Vector2 cameraPosition)
    {
        this.cameraPosition = cameraPosition;
        cameraAtlas = new TextureAtlas(Gdx.files.internal("camerapack.pack"));
        cameraAnimation = new Animation(0.1f, cameraAtlas.getRegions());

        cameraActions = CameraActions.Move;

        colliderPos = new Vector2(this.cameraPosition.x + 120, this.cameraPosition.y);
        collider = new Collider(colliderPos, 130, 20);

        moveLeft = true;
        stop = false;

    }

    public void Update(float deltaTime)
    {
        if(stateTime == 0)
            collider.Update(new Vector2(this.cameraPosition.x + 120, this.cameraPosition.y));
        else if(stateTime >= 0.1f && stateTime < 0.2f)
            collider.Update(new Vector2(this.cameraPosition.x + 60, this.cameraPosition.y));
        else if(stateTime >= 0.2f && stateTime < 0.3f)
            collider.Update(new Vector2(this.cameraPosition.x, this.cameraPosition.y));
        else if(stateTime >= 0.3f && stateTime < 0.4f)
            collider.Update(new Vector2(this.cameraPosition.x + 60, this.cameraPosition.y));
        else if(stateTime >= 0.4f && stateTime < 0.5f)
            collider.Update(new Vector2(this.cameraPosition.x + 120, this.cameraPosition.y));
        else if(stateTime >= 0.5f && stateTime < 0.6f)
            collider.Update(new Vector2(this.cameraPosition.x + 210, this.cameraPosition.y));
        else if(stateTime >= 0.6f && stateTime < 0.7f)
            collider.Update(new Vector2(this.cameraPosition.x + 260, this.cameraPosition.y));
        else if(stateTime >= 0.7f && stateTime < 0.8f)
            collider.Update(new Vector2(this.cameraPosition.x + 210, this.cameraPosition.y));

    }

    private TextureRegion getFrameFromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame;

        //stateTime = 0.5f;
        //frame = cameraAnimation.getKeyFrame(stateTime, true);


        //0.2 é igual à camera a apontar para a esquerda
        //0.6 é igual à camera a apontar para a direita

        switch (cameraActions)
        {
            case Move:
                default:
                stateTime += deltaTime * 0.6f;
                frame = cameraAnimation.getKeyFrame(stateTime, true);
                if(stateTime >= 0.2f && stateTime <= 0.22f)
                {
                    countAux = 0;
                    cameraActions = CameraActions.Stop;
                }
                else if(stateTime >= 0.6f && stateTime <= 0.62f)
                {
                    countAux = 0;
                    cameraActions = CameraActions.Stop;
                }
                else if(stateTime > 0.8f)
                {
                    stateTime = 0;
                }



                break;
            case Stop:
                countAux += deltaTime;

                frame = cameraAnimation.getKeyFrame(stateTime, true);

                if(countAux > 3f)
                {
                    cameraActions = CameraActions.Move;
                }
                break;
        }

        return frame;
    }

    public void CheckCollisionWithPlayer(Player player)
    {
        if(collider.getRectangle().overlaps(player.getPlayerCollider().getRectangle()) && player.playerAction != player.playerAction.HIDING)
            if(!player.isDisableGameOver())
                player.playerAction = player.playerAction.DYING;
    }

    public void Draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
    {
        spriteBatch.begin();
        spriteBatch.draw(getFrameFromCurrentAnimation(Gdx.graphics.getDeltaTime()), cameraPosition.x, cameraPosition.y, 400, 160);
        spriteBatch.end();

        /*
        shapeRenderer.begin();
        collider.DrawRectangle(shapeRenderer);
        shapeRenderer.end();
        */
    }

}
