package com.patriots.simov.utils.multiplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Listener;
import com.patriots.simov.core.Player;
import com.patriots.simov.core.Trap;

public class ClientSession extends Listener
{
    private Player player;
    static Network network = new Network();
    static Map<Integer,MPPlayer> players = new HashMap<Integer,MPPlayer>();
    static public List<Trap> enemyTraps = new ArrayList<Trap>();
    public List<Trap> publicEnemyTraps = new ArrayList<Trap>();
    static public Vector2 enemyTrapPos;
    static public Vector2 playerPos = new Vector2(0, 0);


    public ClientSession(Player player)
    {
        this.player = player;

        network.connect();
    }

    public void update()
    {
        //Update position
        if(player.networkPosition != player.getPosition())
        {
            //Send the player's position
            PacketUpdatePlayerPosition packet = new PacketUpdatePlayerPosition();
            packet.x = player.getPosition().x;
            packet.y = player.getPosition().y;
            network.client.sendUDP(packet);

            player.networkPosition.x = player.getPosition().x;
        }

        //Add trap
        if(player.isJustAddedTrap())
        {
            PacketAddTrap packet = new PacketAddTrap();
            packet.x = player.lastTrap.x;
            packet.y = player.lastTrap.y;
            System.out.println(packet.x + ": " +  packet.y);
            network.client.sendUDP(packet);

            player.setJustAddedTrap(false);
        }


    }

    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
    {
        /*
        //Render other players
        spriteBatch.begin();

        for(MPPlayer mpPlayer : players.values())
        {
            spriteBatch.draw(new Texture("guard.png"), mpPlayer.x, mpPlayer.y);
        }

        spriteBatch.end();
        /*

        //shapeRenderer.begin();

        /*
        for (MPPlayer mpPlayer : players.values())
        {
            if(mpPlayer.xTrap != 0 && mpPlayer.yTrap != 0)
                shapeRenderer.rect(mpPlayer.xTrap, mpPlayer.yTrap, 50, 50);
        }
        */
        publicEnemyTraps = enemyTraps;
        for (Trap trap : enemyTraps)
        {
            //trap.Draw(spriteBatch, shapeRenderer);
        }

        //shapeRenderer.end();
    }
}
