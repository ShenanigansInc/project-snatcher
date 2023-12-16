package com.patriots.simov.utils.multiplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerSession extends Listener
{
    static Server server;
    static final int port = 27960;
    static Map<Integer, PlayerForServer> players = new HashMap<Integer, PlayerForServer>();

    public static void main() throws IOException
    {
        server = new Server();
        server.getKryo().register(PacketUpdatePlayerPosition.class);
        server.getKryo().register(PacketAddPlayer.class);
        server.getKryo().register(PacketRemovePlayer.class);
        server.getKryo().register(PacketAddTrap.class);
        server.bind(port, port);
        server.start();
        server.addListener(new ServerSession());
        System.out.println("The server is ready");
    }

    public void connected(Connection c)
    {
        PlayerForServer player = new PlayerForServer();
        player.x = 0;
        player.y = 0;
        player.xTrap = 0;
        player.yTrap = 0;
        player.c = c;

        PacketAddPlayer packet = new PacketAddPlayer();
        packet.id = c.getID();
        server.sendToAllExceptTCP(c.getID(), packet);

        for(PlayerForServer p : players.values())
        {
            PacketAddPlayer packet2 = new PacketAddPlayer();
            packet2.id = p.c.getID();
            c.sendTCP(packet2);
        }

        players.put(c.getID(), player);
        System.out.println("Connection received.");
    }

    public void received(Connection c, Object o)
    {
        if(o instanceof PacketUpdatePlayerPosition)
        {
            PacketUpdatePlayerPosition packet = (PacketUpdatePlayerPosition) o;
            players.get(c.getID()).x = packet.x;
            players.get(c.getID()).y = packet.y;

            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
            //System.out.println("received and sent an update position packet");

        }
        else if(o instanceof PacketAddTrap)
        {
            PacketAddTrap packet = (PacketAddTrap) o;
            players.get(c.getID()).xTrap = packet.x;
            players.get(c.getID()).yTrap = packet.y;

            System.out.println(packet.x + ": " +  packet.y);
            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
            System.out.println("added trap");
        }
    }

    public void disconnected(Connection c)
    {
        players.remove(c.getID());
        PacketRemovePlayer packet = new PacketRemovePlayer();
        packet.id = c.getID();
        server.sendToAllExceptTCP(c.getID(), packet);
        System.out.println("Connection dropped.");
    }

}
