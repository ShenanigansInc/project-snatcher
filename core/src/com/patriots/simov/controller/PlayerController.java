package com.patriots.simov.controller;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.patriots.simov.core.Player;
import com.patriots.simov.utils.Collider;

import org.omg.PortableInterceptor.ORBInitializerOperations;

public class PlayerController
{
    Collider leftCollider, rightCollider, cameraCollider, upCollider, downCollider;
    Texture rightN, rightP, leftN, leftP, cameraTexture, upN, upP, downN, downP;
    Sprite rightButton, leftButton, cameraSprite, upButton, downButton;
    Vector2 rightButtonPos, leftButtonPos, cameraPos, upButtonPos, downButtonPos;
    Vector3 input;
    float radius, buttonRadius;
    boolean upDownVisible;

    private Player player;

    boolean pressLeft;
    boolean pressRight;
    boolean pressCameraButton;
    boolean pressUp;

    public boolean isPressDown() {
        return pressDown;
    }

    boolean pressDown;

    private Matrix4 calibrationMatrix = new Matrix4();

    public Vector3 getCurrentAccelerometerValues() {
        return currentAccelerometerValues;
    }

    private Vector3 currentAccelerometerValues;


    public boolean isPressCameraButton() {
        return pressCameraButton;
    }

    public boolean isPressLeft()
    {
        return pressLeft;
    }

    public boolean ispressRight()
    {
        return pressRight;
    }

    public boolean isPressUp()
    {
        return pressUp;
    }

    public boolean isUpDownVisible() {
        return upDownVisible;
    }

    public void setUpDownVisible(boolean upDownVisible) {
        this.upDownVisible = upDownVisible;
    }

    private OrthographicCamera camera;

    //CHEAT
    private GlyphLayout glyphLayout;
    private BitmapFont font;
    private String message = "CHEATING";
    //CHEAT

    public PlayerController(Texture rightN, Texture rightP, Texture leftN, Texture leftP, Texture upN, Texture upP, Texture downN, Texture downP,Texture cameraTexture, OrthographicCamera camera, Player player)
    {
        //CHEAT
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(3);
        glyphLayout = new GlyphLayout();
        this.camera = camera;
        //CHEAT

        this.rightN = rightN;
        this.rightP = rightP;
        this.leftN = leftN;
        this.leftP = leftP;
        this.upN = upN;
        this.upP = upP;
        this.downN = downN;
        this.downP = downP;
        this.cameraTexture = cameraTexture;

        rightButton = new Sprite(this.rightN, this.rightN.getWidth(), this.rightN.getHeight());
        leftButton = new Sprite(this.leftN, this.leftN.getWidth(), this.leftN.getHeight());
        cameraSprite = new Sprite(this.cameraTexture, this.cameraTexture.getWidth(), this.cameraTexture.getHeight());
        upButton = new Sprite(this.upN, this.upN.getWidth(), this.upN.getHeight());
        downButton = new Sprite(this.downN, this.downN.getWidth(), this.downN.getHeight());


        radius = leftButton.getWidth() * 1.5f;
        buttonRadius = radius / 2;
        leftButtonPos = new Vector2(camera.position.x - camera.viewportWidth/2 + 10, camera.position.y - camera.viewportHeight/2 + 10);
        rightButtonPos = new Vector2(leftButtonPos.x + radius,leftButtonPos.y);

        cameraPos = new Vector2(camera.position.x + camera.viewportWidth/2 - radius - 10, camera.position.y - camera.viewportHeight/2 + 10);

        upButtonPos = new Vector2(cameraPos.x - radius * 2.1f, leftButtonPos.y);
        downButtonPos = new Vector2(upButtonPos.x + radius , leftButtonPos.y);


        leftCollider = new Collider(leftButtonPos, buttonRadius);
        rightCollider = new Collider(rightButtonPos, buttonRadius);
        cameraCollider = new Collider(cameraPos, radius/2);
        upCollider = new Collider(upButtonPos, buttonRadius);
        downCollider = new Collider(downButtonPos, buttonRadius);

        input = new Vector3();

        this.player = player;

    }



    public void Update(float deltaTime, OrthographicCamera camera)
    {
        input = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
        camera.unproject(input);

        if(!upDownVisible)
        {
            pressUp = false;
            pressDown = false;
        }


        /*if(Gdx.input.justTouched())
        {
            if(cameraCollider.CheckCollisionCircle(new Vector2(input.x, input.y)))
            {
                pressCameraButton = true;
                calibrateCamera();
            }
        }*/

        if(Gdx.input.isTouched())
        {
            if (leftCollider.CheckCollisionCircle(new Vector2(input.x, input.y)) && player.playerAction != player.playerAction.FALLING)//&& player.isGrounded())
            {
                player.playerAction = player.playerAction.WALKING;

                pressLeft = true;
                pressRight = false;
            }
            else if (rightCollider.CheckCollisionCircle(new Vector2(input.x, input.y)) && player.playerAction != player.playerAction.FALLING)//&& player.isGrounded())
            {
                player.playerAction = player.playerAction.WALKING;

                pressLeft = false;
                pressRight = true;
            }
            else
            {
                //player.playerAction = player.playerAction.IDLE;

                pressLeft = false;
                pressRight = false;
            }

            if(cameraCollider.CheckCollisionCircle(new Vector2(input.x, input.y)))
            {
                pressCameraButton = true;
                controlCameraPeek();
            }

            if(upDownVisible && upCollider.CheckCollisionCircle(new Vector2(input.x, input.y)))
            {
                pressUp = true;
                pressDown = false;
            }
            else if(upDownVisible && downCollider.CheckCollisionCircle(new Vector2(input.x, input.y)))
            {
                pressDown = true;
                pressUp = false;
            }
            else
            {
                pressUp = false;
                pressDown = false;
            }
        }
        else
        {
            if(player.playerAction != player.playerAction.STAIRS)
                player.playerAction = player.playerAction.IDLE;

            pressLeft = false;
            pressRight = false;
            pressCameraButton = false;
            pressUp = false;
            pressDown = false;
        }


        if(pressLeft)
        {
            leftButton.setTexture(this.leftP);
        }
        else
        {
            leftButton.setTexture(this.leftN);
        }

        if(pressRight)
        {
            rightButton.setTexture(this.rightP);
        }
        else
        {
            rightButton.setTexture(this.rightN);
        }

        if(pressUp)
        {
            upButton.setTexture(this.upP);
        }
        else
        {
            upButton.setTexture(this.upN);
        }
        if(pressDown)
        {
            downButton.setTexture(this.downP);
        }
        else
        {
            downButton.setTexture(this.downN);
        }
    }

    private void calibrateCamera()
    {
        Vector3 calibrationVector = new Vector3(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY(), Gdx.input.getAccelerometerZ());

        Quaternion rotationQuaternion = new Quaternion().setFromCross(Vector3.Z, calibrationVector.nor());

        Matrix4 auxMatrix = new Matrix4(Vector3.Zero, rotationQuaternion, new Vector3(1, 1, 1));

        calibrationMatrix = auxMatrix.inv();
    }

    private void controlCameraPeek()
    {
        currentAccelerometerValues = new Vector3(Gdx.input.getAccelerometerX()/10 * 90, Gdx.input.getAccelerometerY()/10 * 90, Gdx.input.getAccelerometerZ()/10 * 90);

        //currentAccelerometerValues.mul(calibrationMatrix);
    }

    int value = 0;
    public boolean isLaydown()
    {
        value = Math.round(Gdx.input.getAccelerometerX());

        if(value < 3 && value > -15)
            return true;

        return false;
    }

    public void Draw(SpriteBatch spriteBatch,ShapeRenderer shapeRenderer)
    {
        spriteBatch.begin();
        spriteBatch.draw(rightButton, rightButtonPos.x, rightButtonPos.y, radius, radius);
        spriteBatch.draw(leftButton, leftButtonPos.x, leftButtonPos.y, radius, radius);
        spriteBatch.draw(cameraSprite, cameraPos.x, cameraPos.y, radius, radius);

        if(upDownVisible)
        {
            spriteBatch.draw(upButton, upButtonPos.x, upButtonPos.y, radius, radius);
            spriteBatch.draw(downButton, downButtonPos.x, downButtonPos.y, radius, radius);
        }

        spriteBatch.end();

        //shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        /*leftCollider.DrawCircle(shapeRenderer);
        rightCollider.DrawCircle(shapeRenderer);
        cameraCollider.DrawCircle(shapeRenderer);*/
        shapeRenderer.end();

        //CHEAT
        glyphLayout.setText(font, message);

        spriteBatch.begin();
        if(player.isDisableGameOver())
            font.draw(spriteBatch, glyphLayout, rightButtonPos.x + 120, rightButtonPos.y + 30);
        spriteBatch.end();
        //CHEAT
    }

}