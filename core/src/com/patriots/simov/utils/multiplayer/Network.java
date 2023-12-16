package com.patriots.simov.utils.multiplayer;

import java.io.IOException;
import java.net.InetAddress;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.patriots.simov.core.Trap;

public class Network extends Listener
{
    Client client;
    String ip = "192.168.43.38";
    int port = 27960;

    public void connect()
    {
        client = new Client();
        client.getKryo().register(PacketUpdatePlayerPosition.class);
        client.getKryo().register(PacketAddPlayer.class);
        client.getKryo().register(PacketRemovePlayer.class);
        client.getKryo().register(PacketAddTrap.class);
        client.addListener(this);

        InetAddress address = null;

        //while (address == null)
            address = client.discoverHost(port, 10000);

        System.out.println(address);

        client.start();
        try
        {
            client.connect(10000, address, port, port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void received(Connection c, Object o)
    {
        if(o instanceof PacketAddPlayer)
        {
            PacketAddPlayer packet = (PacketAddPlayer) o;
            MPPlayer newPlayer = new MPPlayer();
            ClientSession.players.put(packet.id, newPlayer);
        }
        else if(o instanceof PacketRemovePlayer)
        {
            PacketRemovePlayer packet = (PacketRemovePlayer) o;
            ClientSession.players.remove(packet.id);
        }
        else if(o instanceof PacketUpdatePlayerPosition)
        {
            PacketUpdatePlayerPosition packet = (PacketUpdatePlayerPosition) o;
            ClientSession.players.get(packet.id).x = packet.x;
            ClientSession.players.get(packet.id).y = packet.y;
            ClientSession.playerPos = new Vector2(packet.x, packet.y);
        }
        else if(o instanceof PacketAddTrap)
        {
            PacketAddTrap packet = (PacketAddTrap) o;

            ClientSession.players.get(packet.id).xTrap = packet.x;
            ClientSession.players.get(packet.id).yTrap = packet.y;
            ClientSession.enemyTraps.add(new Trap(new Vector2(packet.x, packet.y)));
            ClientSession.enemyTrapPos = new Vector2(packet.x, packet.y);
        }
    }
}
