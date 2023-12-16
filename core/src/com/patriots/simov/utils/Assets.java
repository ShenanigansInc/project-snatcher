package com.patriots.simov.utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class Assets
{
    public Texture leftNormal;
    public Texture leftPressed;
    public Texture rightNormal;
    public Texture rightPressed;
    public Texture upNormal;
    public Texture upPressed;
    public Texture downNormal;
    public Texture downPressed;
    public Texture cameraButtonTexture;
    public Texture sewer1, sewer2;
    public Texture trap;

    public Texture woodTexture;
    public Texture soundWaveTexture;

    public Texture floorTexture;
    public Texture wallTexture;

    public Texture background;
    public Texture startBackground;

    public Texture stairs;

    public Texture guard;

    public  void Load()
    {
        Images();

    }

    private void Images()
    {
        leftNormal = new Texture("LeftNormal.png");
        leftPressed = new Texture("LeftPressed.png");
        rightNormal = new Texture("RightNormal.png");
        rightPressed = new Texture("RightPressed.png");
        upNormal = new Texture("UpNormal.png");
        upPressed = new Texture("UpPressed.png");
        downNormal = new Texture("DownNormal.png");
        downPressed = new Texture("DownPressed.png");
        cameraButtonTexture = new Texture("camera.png");

        woodTexture = new Texture("TouchTexture.png");
        soundWaveTexture = new Texture("Touch.png");

        floorTexture = new Texture("floor.png");
        wallTexture = new Texture("wall.png");

        background = new Texture("background3.png");
        startBackground = new Texture("fallTexture.png");

        stairs = new Texture("Stairs.png");

        sewer1 = new Texture("WinTextureBack.png");
        sewer2 = new Texture("WinTextureFront.png");
        trap = new Texture("trap.png");
    }
}
