package com.patriots.simov.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.patriots.simov.controller.PlayerController;
import com.patriots.simov.core.Camera;
import com.patriots.simov.core.Checkpoint;
import com.patriots.simov.core.Guard;
import com.patriots.simov.core.Item;
import com.patriots.simov.core.Switch;
import com.patriots.simov.core.Trap;
import com.patriots.simov.core.Laser;
import com.patriots.simov.core.Platform;
import com.patriots.simov.core.Player;
import com.patriots.simov.core.Stair;
import com.patriots.simov.core.TouchBlock;
import com.patriots.simov.utils.Assets;
import com.patriots.simov.utils.Collider;
import com.patriots.simov.utils.multiplayer.ClientSession;
import com.patriots.simov.utils.multiplayer.ServerSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirstLevel extends State
{

    private Player player;
    private ShapeRenderer shapeRenderer;
    private ShapeRenderer interfaceShapeRenderer;
    private SpriteBatch interfaceSpriteBatch;
    private Vector3 fixedTouchPosition;

    private Assets gameAssets;
    private PlayerController playerController;
    private TouchBlock touchBlock;

    private List<Platform> platforms = new ArrayList<Platform>();
    private List<Platform> walls = new ArrayList<Platform>();


    private float peekAngleY, peekAngleX, peekAngleZ;
    private Sprite background1, fallBackground;
    private Vector2 positionBG1,positionBG2, positionBG3, positionBG4, backgroundScale;

    float wallWidth, wallHeight, groundWidth, groundHeight;
    Stair stair1, stair2, stair3;

    boolean firstTime;

    Guard guard, guard2;
    Laser laser, laser2, laser3;

    private List<Guard> guards = new ArrayList<Guard>();
    private List<Laser> lasers = new ArrayList<Laser>();
    private List<Stair> stairs = new ArrayList<Stair>();
    private List<Trap> traps = new ArrayList<Trap>();
    private List<Item> items = new ArrayList<Item>();

    private Checkpoint checkpoint1, checkpoint2;
    private Vector3 currentCheckpoint = new Vector3(0, 0, 0);
    private List<Checkpoint> checkpoints = new ArrayList<Checkpoint>();

    private boolean isServer = false;
    private boolean isClient = false;

    private ClientSession clientSession;
    private ServerSession serverSession;
    private Camera cameraSearch,cameraSearch2;

    private Laser specialLaser;
    private Vector3 input;
    private Collider backgroundCollider;
    private float addTrapTimer;
    private int maxTraps = 5, countTraps = 0;

    private Item item;
    private Collider finishCollider;

    private int itemTotal = 0;
    private int itemsFound = 0;
    private boolean canWin = false;
    private GlyphLayout glyphLayout;
    private BitmapFont font;
    private String message = "TESTING";

    private Switch switchLaser;


    private TextureAtlas exitAtlas;
    private Animation exitAnimation;
    private Vector2 exitPosition;
    private Collider exitCollider;

    private float startCountdown = 0;
    private float endingCountdown = 0;
    private boolean beginCountdown = false;

    enum WindowState
    {
        OPEN,
        CLOSED
    }

    private WindowState windowState;
    private List<Trap> enemyTraps = new ArrayList<Trap>();

    public FirstLevel(StateManager gsm, boolean isServer, boolean isClient)
    {
        super(gsm);

        font = new BitmapFont();
        font.setColor(Color.PINK);
        font.getData().setScale(3);
        glyphLayout = new GlyphLayout();

        shapeRenderer = new ShapeRenderer();
        interfaceShapeRenderer = new ShapeRenderer();
        interfaceSpriteBatch = new SpriteBatch();
        worldCamera.setToOrtho(false);
        interfaceCamera.setToOrtho(false);
        interfaceCamera.position.x = 0;
        interfaceCamera.position.y = 0;
        interfaceCamera.position.z = 0;
        worldCamera.viewportHeight *= 0.5f;
        worldCamera.viewportWidth *= 0.5f;

        fixedTouchPosition = new Vector3();

        gameAssets = new Assets();
        gameAssets.Load();

        LoadLevelAssets();

        for (Item i:items)
        {
            itemTotal++;
        }

        message = itemsFound + "/" + itemTotal;

        player = new Player(-150, 500, platforms, walls, false, worldCamera);
        playerController = new PlayerController(gameAssets.rightNormal, gameAssets.rightPressed, gameAssets.leftNormal, gameAssets.leftPressed,
                gameAssets.upNormal, gameAssets.upPressed, gameAssets.downNormal, gameAssets.downPressed, gameAssets.cameraButtonTexture, interfaceCamera, player);

        Gdx.input.setCatchBackKey(true);

        firstTime = true;

        guard = new Guard(gameAssets.guard, new Vector2(1420, 250), true);
        guard2 = new Guard(gameAssets.guard, new Vector2(500, 450), false);
        guards.add(guard);
        guards.add(guard2);

        checkpoint1 = new Checkpoint(new Vector3(80, 80, 0));
        checkpoints.add(checkpoint1);
        checkpoint2 = new Checkpoint(new Vector3(890, 80, 0));
        checkpoints.add(checkpoint2);

        //NETWORK STUFF
        this.isServer = isServer;
        this.isClient = isClient;
        if(isServer)
        {
            serverSession = new ServerSession();

            try
            {
                serverSession.main();
            }
            catch (IOException e)
            {}
        }
        if(isClient)
            clientSession = new ClientSession(player);
        //NETWORK STUFF
    }

    @Override
    public void handleInput()
    {
        /*
        if(Gdx.input.isTouched())
        {
            fixedTouchPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            worldCamera.unproject(fixedTouchPosition);

            player.setTouchPosition(fixedTouchPosition);
        }
        */
    }

    @Override
    public void update(float deltaTime)
    {
        if (firstTime)
        {
            worldCamera.position.x = -200;
            worldCamera.position.y = -200;
            worldCamera.update();

            startCountdown += deltaTime;
            if (startCountdown >= 4)
            {
                firstTime = false;
            }
        }
        else
        {
            input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            worldCamera.unproject(input);

            if(isClient)
                addTraps();
        /*
        if(player.getPosition().y < groundHeight -2 && firstTime)
        {
            player.setPosition(new Vector3(50, 93, 0));
            firstTime = false;
        }
        */

            if (player.playerAction != player.playerAction.DYING && player.playerAction != player.playerAction.HIDING
                    && player.playerAction != player.playerAction.UNHIDING)
                playerController.Update(deltaTime, interfaceCamera);

            player.update(deltaTime, playerController.isPressLeft(), playerController.ispressRight(),
                    playerController.isPressCameraButton(), playerController.isPressUp(), playerController.isPressDown());

            for (Stair s : stairs) {
                if (player.checkStairsCollision(s)) {
                    playerController.setUpDownVisible(true);
                    break;
                } else playerController.setUpDownVisible(false);
            }


            if (playerController.isPressCameraButton())
            {
                player.canHide = false;

                peekAngleX = playerController.getCurrentAccelerometerValues().y;
                peekAngleY = playerController.getCurrentAccelerometerValues().x;
                peekAngleZ = playerController.getCurrentAccelerometerValues().z;

                worldCamera.position.x = player.getPosition().x + player.getPlayerWidth() / 2 + (int)peekAngleX * 3;
                worldCamera.position.y = player.getPosition().y + player.getPlayerHeight() / 2 + 140 - (int)peekAngleY * 3;
            }
            else
            {
                player.canHide = playerController.isLaydown();

                peekAngleX = 0;
                peekAngleY = 0;
                peekAngleZ = 0;

                worldCamera.position.x = player.getPosition().x + player.getPlayerWidth() / 2;
                worldCamera.position.y = player.getPosition().y + player.getPlayerHeight() / 2;
            }

            worldCamera.update();

            interfaceCamera.update();

            touchBlock.Update(deltaTime, worldCamera);

            if(player.playerAction != Player.PlayerActions.DYING)
            {
                for (Guard g : guards)
                {
                    g.setSoundwaves(touchBlock.getSoundwaves());
                    g.CheckCollisionWithPlayer(player);
                    if(g.isRegularGuard())
                        g.Update(deltaTime, worldCamera);
                    else g.Update2(deltaTime);
                }
            }

            for (Laser l : lasers)
            {
                l.Update(deltaTime);
                l.checkCollision(player);
            }

            for (Item i : items)
            {
                if (i.getCollider().getRectangle().overlaps(player.getPlayerCollider().getRectangle()))
                {
                    items.remove(i);
                    itemsFound++;
                    message = itemsFound + "/" + itemTotal;

                    if(itemsFound >= itemTotal)
                    {
                        canWin = true;
                        windowState = windowState.OPEN;
                    }
                    break;
                }
            }

            cameraSearch.Update(deltaTime);
            cameraSearch.CheckCollisionWithPlayer(player);

            cameraSearch2.Update(deltaTime);
            cameraSearch2.CheckCollisionWithPlayer(player);

            for (Checkpoint check : checkpoints) {
                if (check.getCollider().CheckCollisionRectangle(player.getPlayerCollider().getRectangle()))
                    currentCheckpoint = check.getPosition();
            }

            if(player.playerAction == player.playerAction.DYING)
            {
                specialLaser.laserActions = specialLaser.laserActions.ON;
            }
            switchLaser.Update(worldCamera, player, specialLaser);
            //System.out.println(currentCheckpoint);

            if (!isServer && !isClient)
            {
                if (Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyPressed(Input.Keys.BACK)) {
                    stateManager.push(new PauseState(stateManager));
                }

                if(Gdx.input.isKeyJustPressed(Input.Keys.W))
                {
                    stateManager.set(new WinState(stateManager));
                }

                /*
                if (Gdx.input.isKeyJustPressed(Input.Keys.M))
                {
                    bgMusic.stop();
                    stateManager.push(new GameOverState(stateManager, currentCheckpoint, player,guards));
                }
                */
            }

            if (player.isDead())
            {

                if(!isServer && !isClient)
                {
                    stateManager.push(new GameOverState(stateManager, currentCheckpoint, player, guards));
                }
                else
                {
                    player.setPosition(new Vector3(60, 80, 0));
                    player.playerAction = Player.PlayerActions.IDLE;
                    player.setDead(false);

                    for (Guard g : guards)
                    {
                        if(g.isRegularGuard())
                            g.ResetGuard(new Vector2(1420, 250), true);
                        else g.ResetGuard(new Vector2(500, 450), false);
                    }
                }
            }

            /*
            if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
                specialLaser.laserActions = specialLaser.laserActions.OFF;
            }
            */

            touchTraps();

            if(exitCollider.getRectangle().contains(player.getNextPosition().x, player.getPosition().y) && !canWin)
            {
                player.setPosition(new Vector3(player.getPosition().sub(player.getVelocity().x * deltaTime, 0, 0)));
            }

            if (finishCollider.getRectangle().overlaps(player.getPlayerCollider().getRectangle()) && canWin)
            {
                beginCountdown = true;
            }

            if(beginCountdown)
            {
                endingCountdown += deltaTime;

                if(endingCountdown >= 6.7f)
                    stateManager.set(new WinState(stateManager));
            }

            //NETWORK STUFF
            if (isClient)
            {
                clientSession.update();

                if(clientSession.playerPos.x >= 1970)
                    stateManager.set(new GameOverState(stateManager, true));
                else if(player.getPosition().x >= 1970)
                    stateManager.set(new WinState(stateManager));

                //out.println(clientSession.playerPos.x + ", " + clientSession.playerPos.y);
            }
            //NETWORK STUFF

            AddEnemyTraps();
        }
    }
    @Override
    public void render(SpriteBatch spriteBatch)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(worldCamera.combined);
        shapeRenderer.setProjectionMatrix(worldCamera.combined);
        shapeRenderer.setAutoShapeType(true);
        interfaceSpriteBatch.setProjectionMatrix(interfaceCamera.combined);
        interfaceShapeRenderer.setProjectionMatrix(interfaceCamera.combined);
        interfaceShapeRenderer.setAutoShapeType(true);


        spriteBatch.begin();
        spriteBatch.draw(background1, positionBG1.x, positionBG1.y, backgroundScale.x, backgroundScale.y);
        spriteBatch.draw(background1, positionBG2.x, positionBG2.y, backgroundScale.x, backgroundScale.y);
        spriteBatch.draw(background1, positionBG3.x, positionBG3.y, backgroundScale.x, backgroundScale.y);
        spriteBatch.draw(fallBackground, positionBG4.x, positionBG4.y);
        spriteBatch.end();

        /*
        shapeRenderer.begin();
        backgroundCollider.DrawRectangle(shapeRenderer);
        finishCollider.DrawRectangle(shapeRenderer);
        shapeRenderer.end();
        */

        for (Platform plat : platforms)
        {
            plat.Draw(spriteBatch);
            //plat.DrawCollider(shapeRenderer);
        }

        for (Platform w : walls)
        {
            w.Draw(spriteBatch);
            //w.DrawCollider(shapeRenderer);
        }

        for (Stair s : stairs)
        {
            s.Draw(spriteBatch);
            //s.DrawCollider(shapeRenderer);
        }

        touchBlock.Draw(spriteBatch);
        //touchBlock.DrawColl(shapeRenderer);

        /*
        for (Checkpoint check : checkpoints)
        {
            check.render(shapeRenderer);
        }
        */

        switchLaser.Draw(spriteBatch, shapeRenderer);

        spriteBatch.begin();
        spriteBatch.draw(gameAssets.sewer1, groundWidth * 10, -gameAssets.sewer1.getHeight()/4, gameAssets.sewer1.getWidth() / 3, gameAssets.sewer2.getHeight() /3);
        spriteBatch.end();

        for (Trap i : traps)
        {
            spriteBatch.begin();
            spriteBatch.draw(gameAssets.trap, i.getPosition().x, i.getPosition().y, 50, 50);
            spriteBatch.end();

            //i.Draw(spriteBatch, shapeRenderer);
        }

        player.render(spriteBatch, shapeRenderer);

        spriteBatch.begin();
        spriteBatch.draw(gameAssets.sewer2, groundWidth * 10, -gameAssets.sewer1.getHeight()/4, gameAssets.sewer1.getWidth() / 3, gameAssets.sewer2.getHeight() /3);
        spriteBatch.end();

        playerController.Draw(interfaceSpriteBatch, interfaceShapeRenderer);

        for (Guard g : guards)
        {
            g.render(spriteBatch, shapeRenderer);
        }

        for (Laser l : lasers)
        {
            l.Draw(spriteBatch);
            //l.DrawCollider(shapeRenderer);
        }



        for (Trap i : enemyTraps)
        {
            //i.Draw(spriteBatch, shapeRenderer);
        }

        for (Item i : items)
        {
            i.Draw(spriteBatch);
            //i.DrawCollider(shapeRenderer);
        }

        spriteBatch.begin();
        spriteBatch.draw(exitWindowGetFrameFromCurrentAnimation(Gdx.graphics.getDeltaTime()), exitPosition.x, exitPosition.y);
        spriteBatch.end();

        /*
        shapeRenderer.begin();
        exitCollider.DrawRectangle(shapeRenderer);
        shapeRenderer.end();
        */

        cameraSearch.Draw(spriteBatch, shapeRenderer);
        cameraSearch2.Draw(spriteBatch, shapeRenderer);

        if(isClient)
            clientSession.render(spriteBatch, shapeRenderer);

        glyphLayout.setText(font, message);

        interfaceSpriteBatch.begin();
        font.draw(interfaceSpriteBatch, glyphLayout, interfaceCamera.position.x - interfaceCamera.viewportWidth / 2 + 10, interfaceCamera.position.y + interfaceCamera.viewportHeight/2 - 10);
        interfaceSpriteBatch.end();


    }

    @Override
    public void dispose()
    {
        player.dispose();
    }

    private void LoadLevelAssets()
    {
        groundWidth = gameAssets.floorTexture.getWidth();
        groundHeight = gameAssets.floorTexture.getHeight();

        wallHeight = gameAssets.wallTexture.getHeight();
        wallWidth = gameAssets.wallTexture.getWidth();

        touchBlock = new TouchBlock(gameAssets.woodTexture, gameAssets.soundWaveTexture, new Vector2(groundWidth * 6.5f, 260), 300);

        for(int h = 0; h < 4; h++)
        {
            for (int i = 0; i < 10; i++)
            {
                platforms.add(new Platform(new Vector2(groundWidth * i, h * 200), gameAssets.floorTexture));
            }
        }

        platforms.add(new Platform(new Vector2(-groundWidth, 0), gameAssets.floorTexture));
        platforms.add(new Platform(new Vector2(groundWidth * 9.2f, 2 * 200), gameAssets.floorTexture));

        walls.add(new Platform(new Vector2(groundWidth * 5.5f, 50), gameAssets.wallTexture));
        walls.add(new Platform(new Vector2(groundWidth * 5.5f, 150), gameAssets.wallTexture));

        for (int i = 0; i < 6; i++)
        {
            walls.add(new Platform(new Vector2(-groundWidth, i * wallHeight), gameAssets.wallTexture));
            if(i != 0 && i != 1)
                walls.add(new Platform(new Vector2(0, i * wallHeight), gameAssets.wallTexture));
            if(i != 4 && i != 5)
            walls.add(new Platform(new Vector2((groundWidth * 10) - wallWidth, i * wallHeight), gameAssets.wallTexture));
        }

        background1 = new Sprite(gameAssets.background, gameAssets.background.getWidth(), gameAssets.background.getHeight());
        fallBackground = new Sprite(gameAssets.startBackground, gameAssets.startBackground.getWidth(), gameAssets.startBackground.getHeight());
        positionBG1 = new Vector2(wallWidth, groundHeight);
        positionBG2 = new Vector2(wallWidth, wallHeight * 2 + groundHeight);
        positionBG3 = new Vector2(wallWidth, wallHeight * 3.99f + groundHeight);
        positionBG4 = new Vector2(-groundWidth, groundHeight);

        backgroundScale = new Vector2(background1.getWidth() * 0.98f, background1.getHeight() * 1.45f);

        stair1 = new Stair(new Vector2(groundWidth * 4.5f, groundHeight), gameAssets.stairs);
        stairs.add(stair1);
        stair2 = new Stair(new Vector2(groundWidth * 8, groundHeight), gameAssets.stairs);
        stairs.add(stair2);
        stair3 = new Stair(new Vector2(groundWidth, groundHeight * 3.70f + groundHeight), gameAssets.stairs);
        stairs.add(stair3);


        laser = new Laser(new Vector2(groundWidth * 2f, groundHeight + 400), false, true, 0.25f);
        lasers.add(laser);
        laser2 = new Laser(new Vector2(groundWidth * 2.5f, groundHeight + 400), false, true, 0.45f);
        lasers.add(laser2);
        laser3 = new Laser(new Vector2(groundWidth * 7f, groundHeight), false, true, 0.65f);
        lasers.add(laser3);
        lasers.add(new Laser(new Vector2(groundWidth * 3f, groundHeight + 400), false, true, 0.85f));
        lasers.add(new Laser(new Vector2(groundWidth * 2.5f, groundHeight), false, true, 0.65f));
        lasers.add(new Laser(new Vector2(groundWidth * 1f, groundHeight), false, true, 0.45f));

        cameraSearch = new Camera(new Vector2(groundWidth * 4f, groundHeight + 400));
        cameraSearch2 = new Camera(new Vector2(groundWidth * 6.5f, groundHeight + 400));

        specialLaser = new Laser(new Vector2(groundWidth * 3f , groundHeight + 200), false, false, 0f);
        specialLaser.laserActions = specialLaser.laserActions.ON;
        lasers.add(specialLaser);

        backgroundCollider = new Collider(new Vector2(wallWidth , groundHeight), groundWidth * 10 - wallWidth * 2 , wallHeight * 6 - groundHeight);
        addTrapTimer = 0;

        items.add(new Item(new Vector2(1150, 500)));
        items.add(new Item(new Vector2(70, 300)));
        items.add(new Item(new Vector2(50, 500)));
        items.add(new Item(new Vector2(1000, 90)));
        items.add(new Item(new Vector2(1800, 90)));
        items.add(new Item(new Vector2(1900, 300)));
        items.add(new Item(new Vector2(700, 300)));

        exitAtlas = new TextureAtlas(Gdx.files.internal("exitDoor.pack"));
        exitAnimation = new Animation(0.1f, exitAtlas.getRegions());
        exitPosition = new Vector2(groundWidth * 9.9f, wallHeight * 3.5f);
        exitCollider = new Collider(exitPosition, exitAnimation.getKeyFrame(0.2f).getRegionWidth(), exitAnimation.getKeyFrame(0.2f).getRegionHeight());
        windowState = WindowState.CLOSED;

        finishCollider = new Collider(new Vector2(groundWidth * 9.9f, wallHeight * 4.5f), 100, 200);

        switchLaser = new Switch();
    }

    private void addTraps()
    {
        if(Gdx.input.isTouched())
        {
            if (backgroundCollider.getRectangle().contains(new Vector2(input.x, input.y)))
            {
                if (!playerController.isPressLeft() && !playerController.ispressRight() && !playerController.isPressCameraButton()
                        && !playerController.isPressUp() && !playerController.isPressDown() && countTraps < maxTraps)
                {
                    addTrapTimer += Gdx.graphics.getDeltaTime();

                    if (addTrapTimer >= 2)
                    {
                        addTrapTimer = 0;
                        countTraps++;
                        Gdx.input.vibrate(1000);
                        traps.add(new Trap(new Vector2(input.x - 25, input.y - 25)));
                        player.setJustAddedTrap(true);
                        player.lastTrap = new Vector2(input.x - 25, input.y - 25);
                    }
                }
            }
        }
        else addTrapTimer = 0;

    }

    private void touchTraps()
    {
        for (Trap i : enemyTraps)
        {
            if(player.getPlayerCollider().CheckCollisionRectangle(i.getCollider().getRectangle()))
            {
                player.setPosition(new Vector3(60, 80, 0));
                enemyTraps.remove(i);
                break;
            }
        }
    }

    private TextureRegion exitWindowGetFrameFromCurrentAnimation(float deltaTime)
    {
        TextureRegion frame;

        switch (windowState)
        {
            case CLOSED:
            default:
                frame = exitAnimation.getKeyFrame(0, false);
                break;
            case OPEN:
                frame = exitAnimation.getKeyFrame(0.2f, false);
                break;
        }

        return frame;
    }

    private void AddEnemyTraps()
    {
        if(ClientSession.enemyTrapPos != null)
        {
            for(Trap enemyTrap: enemyTraps)
            {
                if(enemyTrap.getPosition() == ClientSession.enemyTrapPos)
                    return;
            }

            enemyTraps.add(new Trap(ClientSession.enemyTrapPos));
            ClientSession.enemyTrapPos = null;
        }
    }

}

/*
    private void KeyboardTest()
    {


        textListener = new Input.TextInputListener()
        {
            @Override
            public void input(String input)
            {
               // System.out.println(input);
                item.message += input;
            }

            @Override
            public void canceled()
            {
                System.out.println("Aborted");
                playOnce = false;
            }
        };

        Gdx.input.getTextInput(textListener, "Please Insert Password", "", "");

    }*/
