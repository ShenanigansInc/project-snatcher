package com.patriots.simov.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.patriots.simov.utils.Collider;


import java.util.ArrayList;
import java.util.List;

public class Guard
{
    private float guardWidth;
    private float guardHeight;
    private Collider guardColliderLeft, guardColliderRight, visionCollider, bodyCollider;
    private Vector2 guardPosition;

    private Vector2 waypointDestination, waypointLeft, waypointRight, waypointSoundSource;
    boolean moveLeft, startPosition, moveStartPoint;
    private Vector2 startPoint = Vector2.Zero;

    private boolean goForSound = false;
    private boolean goForHallway = false;
    private boolean goLeft = false;

    public void setSoundwaves(List<SoundWave> soundwaves)
    {
        this.soundwaves = soundwaves;
    }

    private List<SoundWave> soundwaves = new ArrayList<SoundWave>();

    public enum GuardPatrol
    {
        MoveLeft,
        MoveRight,
        MoveToStartPoint,
        CheckNoise,
        CheckArea,
        PATROLAREA
    }

    float stopTime;
    float distance;

    public GuardPatrol guardAction;

    private TextureAtlas guardWalkAtlas;
    private Animation guardWalkAnimation;
    private TextureAtlas guardIdleAtlas;
    private Animation guardIdleAnimation;
    private TextureAtlas guardLookUpAtlas;
    private Animation guardLookUpAnimation;

    private TextureAtlas radarAtlas;
    private Animation radarAnimation;
    private float stateTime = 0, turnAround = 0;
    private Vector2 radarPosition, radarSize;
    private boolean lookingRight, lookingLeft;
    private float subToRadarPosition;
    private boolean lookingUp;

    public boolean isRegularGuard() {
        return regularGuard;
    }

    boolean regularGuard;


    public Guard(Texture texture, Vector2 guardPosition, boolean regularGuard)
    {

        this.regularGuard = regularGuard;
        this.guardPosition = guardPosition;
        startPoint = new Vector2(1420, 250);

        guardWidth = 45;
        guardHeight = 126;

        guardColliderLeft = new Collider(new Vector2(guardPosition.x, guardPosition.y), guardWidth/4);
        guardColliderRight = new Collider(new Vector2(guardPosition.x + guardWidth/2 , guardPosition.y), guardWidth/4);

        waypointLeft = new Vector2(135, 0);
        waypointRight = new Vector2(1775, 0);

        waypointDestination = new Vector2(0, 0);
        waypointSoundSource = new Vector2(0, 0);

        moveLeft = true;
        startPosition = false;
        moveStartPoint = false;

        guardWalkAtlas = new TextureAtlas(Gdx.files.internal("Guard_WalkAnimation.pack"));
        guardWalkAnimation = new Animation(0.1f, guardWalkAtlas.getRegions());

        guardIdleAtlas = new TextureAtlas(Gdx.files.internal("Guard_IdleAnimation.pack"));
        guardIdleAnimation = new Animation(0.1f, guardIdleAtlas.getRegions());

        guardLookUpAtlas = new TextureAtlas(Gdx.files.internal("Guard_CheckAnimation.pack"));
        guardLookUpAnimation = new Animation(0.1f, guardLookUpAtlas.getRegions());

        if(regularGuard)
        {
            guardAction = GuardPatrol.MoveToStartPoint;
        }
        else
        {
            guardAction = GuardPatrol.PATROLAREA;
            waypointDestination = new Vector2(135, 0);
        }

        subToRadarPosition = 20;
        radarPosition = new Vector2(guardPosition.x + guardWidth/2, guardPosition.y - subToRadarPosition);
        radarAtlas = new TextureAtlas(Gdx.files.internal("radarpack.pack"));
        radarAnimation = new Animation(0.1f, radarAtlas.getRegions());
        radarSize = new Vector2(250, 170);

        visionCollider = new Collider(new Vector2(guardPosition.x + guardWidth/2, guardPosition.y), radarSize.x - 20, guardHeight);
        bodyCollider = new Collider(new Vector2(guardPosition.x + guardWidth/3, guardPosition.y), guardWidth / 2, guardHeight);

        lookingRight = true;
        lookingLeft = false;

        lookingUp = false;

    }

    public void Update(float deltaTime)
    {
        GuardActions(deltaTime);
        guardColliderLeft.Update(new Vector2(guardPosition.x, guardPosition.y));
        guardColliderRight.Update(new Vector2(guardPosition.x + guardWidth / 2, guardPosition.y));
    }

    Vector3 input;
    public void Update(float deltaTime, OrthographicCamera camera)
    {
        input = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
        camera.unproject(input);

        CheckCollisionWithSoundWaves();

        GuardActions(deltaTime);
        guardColliderLeft.Update(new Vector2(guardPosition.x + guardWidth / 4, guardPosition.y + guardHeight/2),guardWidth/4);
        guardColliderRight.Update(new Vector2(guardPosition.x + guardWidth - guardWidth / 4, guardPosition.y + guardHeight/2), guardWidth/4);


        bodyCollider.Update(new Vector2(guardPosition.x + guardWidth/3, guardPosition.y));

        if(lookingLeft && !lookingRight)
        {
            visionCollider.Update(new Vector2(guardPosition.x + guardWidth/2 - radarSize.x + 20, guardPosition.y));
            radarPosition = new Vector2(guardPosition.x + guardWidth/2 - radarSize.x, guardPosition.y - subToRadarPosition);
        }
        else if (!lookingLeft && lookingRight)
        {
            visionCollider.Update(new Vector2(guardPosition.x + guardWidth / 2, guardPosition.y));
            radarPosition = new Vector2(guardPosition.x + guardWidth/2, guardPosition.y - subToRadarPosition);
        }
    }



    private void GuardActions(float deltaTime)
    {
        switch (guardAction)
        {
            case CheckArea:
                default:
                stopTime += deltaTime;

                if(stopTime > 5f)
                {
                    stopTime = 0;

                    waypointDestination = startPoint;
                    goForHallway = false;
                    guardAction = GuardPatrol.MoveToStartPoint;

                }
                break;
            case MoveLeft:
                goLeft = true;

                /*if(guardPosition.x >= waypointSoundSource.x && goForSound && !goForHallway)
                    waypointDestination = waypointSoundSource;
                else if(waypointDestination != waypointSoundSource && !goForSound && goForHallway)*/
                waypointDestination = waypointLeft;

                moveLeft = true;
                lookingRight = false;
                lookingLeft = true;
                distance = Vector2.dst(guardPosition.x, guardPosition.y, waypointDestination.x, guardPosition.y);
                guardPosition.add(-100 * deltaTime, 0);

                if(distance < 1)
                    guardAction = GuardPatrol.CheckArea;
                break;
            case MoveRight:

                goLeft = false;

                /*if(guardPosition.x <= waypointSoundSource.x && goForSound && !goForHallway)
                    waypointDestination = waypointSoundSource;
                else if(waypointDestination != waypointSoundSource && !goForSound && goForHallway)*/
                    waypointDestination = waypointRight;

                moveLeft = false;
                lookingRight = true;
                lookingLeft = false;
                distance = Vector2.dst(guardPosition.x, guardPosition.y, waypointDestination.x, guardPosition.y);
                guardPosition.add(100 * deltaTime, 0);

                if(distance < 1)
                    guardAction = GuardPatrol.CheckArea;
                break;
            case MoveToStartPoint:
                distance = Vector2.dst(guardPosition.x, guardPosition.y, startPoint.x, guardPosition.y);


                if(distance > 1)
                {
                    if (moveLeft)
                    {
                        lookingRight = true;
                        lookingLeft = false;
                        guardPosition.add(100 * deltaTime, 0);
                    }

                    else
                    {
                        lookingRight = false;
                        lookingLeft = true;
                        guardPosition.add(-100 * deltaTime, 0);
                    }
                }
                else
                {
                    turnAround += deltaTime;

                    if(lookingRight && !lookingLeft && turnAround > 2f)
                    {
                        turnAround = 0;
                        lookingRight = false;
                        lookingLeft = true;
                    }
                    else if(!lookingRight && lookingLeft && turnAround > 2f)
                    {
                        turnAround = 0;
                        lookingRight = true;
                        lookingLeft = false;
                    }


                }

                break;
            case CheckNoise:

                if(goLeft)
                {
                    distance = Vector2.dst(guardPosition.x, guardPosition.y, waypointDestination.x, guardPosition.y);

                    if(distance > 1)
                    {
                        guardPosition.add(-100 * deltaTime, 0);
                        lookingRight = false;
                        lookingLeft = true;
                    }

                    else stopTime += deltaTime;

                    if(stopTime > 5)
                    {
                        waypointDestination = waypointLeft;
                        stopTime= 0;
                        guardAction = guardAction.MoveLeft;

                    }

                }
                else
                {
                    distance = Vector2.dst(guardPosition.x, guardPosition.y, waypointDestination.x, guardPosition.y);

                    if(distance > 1)
                    {
                        guardPosition.add(100 * deltaTime, 0);
                        lookingRight = true;
                        lookingLeft = false;
                    }
                    else stopTime += deltaTime;

                    if(stopTime > 5)
                    {
                        waypointDestination = waypointRight;
                        stopTime= 0;
                        guardAction = guardAction.MoveRight;

                    }

                }
                break;

        }
    }

    private void CheckCollisionWithSoundWaves()
    {
        for (SoundWave soundWave : soundwaves)
        {
            boolean leftSide = soundWave.soundWaveCollider.getCircle().overlaps(guardColliderLeft.getCircle());//*/
            boolean rightSide = soundWave.soundWaveCollider.getCircle().overlaps(guardColliderRight.getCircle());

            if(leftSide && guardAction != GuardPatrol.CheckNoise)
            {
                waypointSoundSource = new Vector2(soundWave.soundWaveColPos.x, guardPosition.y);
                goLeft = true;
                guardAction = GuardPatrol.CheckNoise;
                waypointDestination = waypointSoundSource;
                //soundWave.radius = soundWave.maxCircleRadius;
                stopTime = 0;
                soundwaves.remove(soundWave);
                return;
            }
            else if(rightSide && guardAction != GuardPatrol.CheckNoise)
            {
                waypointSoundSource = new Vector2(soundWave.soundWaveColPos.x, guardPosition.y);
                goLeft = false;
                guardAction = GuardPatrol.CheckNoise;
                waypointDestination = waypointSoundSource;
                //soundWave.radius = soundWave.maxCircleRadius;
                stopTime = 0;
                soundwaves.remove(soundWave);
                return;
            }
        }
    }

    public void CheckCollisionWithPlayer(Player player)
    {
        if(bodyCollider.getRectangle().overlaps(player.getPlayerCollider().getRectangle()) && player.playerAction != player.playerAction.HIDING)
            if(!player.isDisableGameOver())
                player.playerAction = player.playerAction.DYING;

        if(visionCollider.CheckCollisionRectangle(player.getPlayerCollider().getRectangle()) && player.playerAction != player.playerAction.HIDING)
        {
            if(guardAction != guardAction.CheckArea && guardAction != guardAction.CheckNoise)
            {
                if(!player.isDisableGameOver())
                player.playerAction = player.playerAction.DYING;
            }
        }


    }


    private TextureRegion getFrameFromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame = radarAnimation.getKeyFrame(stateTime, true);

        if(lookingLeft && !lookingRight && !frame.isFlipX())
        {
            frame.flip(true, false);
        }
        if(!lookingLeft && lookingRight && frame.isFlipX())
        {
            frame.flip(true, false);
        }

        if(stateTime < 4)
            stateTime += deltaTime *0.5f;
        else stateTime = 0;

        return frame;
    }


    private TextureRegion getFrameFromGuardCurrentAnimation(float deltaTime)
    {
        TextureRegion frame;

        if(guardAction == guardAction.CheckArea || guardAction == guardAction.CheckNoise || lookingUp)
            frame = guardLookUpAnimation.getKeyFrame(stateTime, false);
        else if(!(guardAction == guardAction.MoveToStartPoint && distance <= 1))
            frame = guardWalkAnimation.getKeyFrame(stateTime, true);
        else
            frame = guardIdleAnimation.getKeyFrame(stateTime, true);

        if(lookingLeft && !lookingRight && !frame.isFlipX())
        {
            frame.flip(true, false);
        }
        if(!lookingLeft && lookingRight && frame.isFlipX())
        {
            frame.flip(true, false);
        }

        if(stateTime < 4)
            stateTime += deltaTime *0.5f;
        else stateTime = 0;

        return frame;
    }
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        spriteBatch.begin();
        if(guardAction != guardAction.CheckArea && guardAction != guardAction.CheckNoise && !lookingUp)
            spriteBatch.draw(getFrameFromCurrentAnimation(Gdx.graphics.getDeltaTime()), radarPosition.x, radarPosition.y, radarSize.x, radarSize.y);
        spriteBatch.draw(getFrameFromGuardCurrentAnimation(Gdx.graphics.getDeltaTime()), guardPosition.x, guardPosition.y, guardWidth, guardHeight);

        spriteBatch.end();


        /*
        //Desenhar os platforms, só para debug
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        guardColliderLeft.DrawCircle(shapeRenderer);
        shapeRenderer.setColor(0, 1, 0, 1);
        guardColliderRight.DrawCircle(shapeRenderer);
        visionCollider.DrawRectangle(shapeRenderer);
        bodyCollider.DrawRectangle(shapeRenderer);

        shapeRenderer.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(0, 1, 0, 1);
        //shapeRenderer.polygon(polygon.getVertices());
        shapeRenderer.end();
        */
    }

    public void Update2(float deltaTime)
    {
        distance = Vector2.dst(guardPosition.x, guardPosition.y, waypointDestination.x, guardPosition.y);

        if(distance > 1)
        {
            if(waypointDestination.x == 135)
            {
                guardPosition.add(-100 * deltaTime, 0);
                lookingLeft = true;
                lookingRight = false;
            }

            else if(waypointDestination.x == 1700)
            {
                guardPosition.add(100 * deltaTime, 0);
                lookingLeft = false;
                lookingRight = true;
            }
        }
        else
        {
            stopTime += deltaTime;

            if(stopTime < 5)
            {
                guardPosition.x = waypointDestination.x;
                lookingUp = true;
            }
            else
            {
                stopTime = 0;
                lookingUp = false;
                if(waypointDestination.x == 135)
                {
                    waypointDestination.x = 1700;
                    lookingLeft = true;
                    lookingRight = false;
                }

                else if(waypointDestination.x == 1700)
                {
                    waypointDestination.x = 135;
                    lookingLeft = false;
                    lookingRight = true;
                }
            }
        }

        guardColliderLeft.Update(new Vector2(guardPosition.x + guardWidth / 4, guardPosition.y + guardHeight/2),guardWidth/4);
        guardColliderRight.Update(new Vector2(guardPosition.x + guardWidth - guardWidth / 4, guardPosition.y + guardHeight/2), guardWidth/4);


        bodyCollider.Update(new Vector2(guardPosition.x + guardWidth/3, guardPosition.y));

        if(lookingLeft && !lookingRight)
        {
            visionCollider.Update(new Vector2(guardPosition.x + guardWidth/2 - radarSize.x, guardPosition.y));
            radarPosition = new Vector2(guardPosition.x + guardWidth/2 - radarSize.x, guardPosition.y - subToRadarPosition);
        }
        else if (!lookingLeft && lookingRight)
        {
            visionCollider.Update(new Vector2(guardPosition.x + guardWidth / 2, guardPosition.y));
            radarPosition = new Vector2(guardPosition.x + guardWidth/2, guardPosition.y - subToRadarPosition);
        }
    }

    public void ResetGuard(Vector2 guardPosition, boolean regularGuard)
    {

        this.regularGuard = regularGuard;
        this.guardPosition = guardPosition;
        startPoint = new Vector2(1420, 250);

        moveLeft = true;
        startPosition = false;
        moveStartPoint = false;

        if(regularGuard)
        {
            guardAction = GuardPatrol.MoveToStartPoint;
            waypointDestination = startPoint;
        }
        else
        {
            guardAction = GuardPatrol.PATROLAREA;
            waypointDestination = new Vector2(135, 0);
        }

        lookingRight = true;
        lookingLeft = false;
        lookingUp = false;
    }

}
