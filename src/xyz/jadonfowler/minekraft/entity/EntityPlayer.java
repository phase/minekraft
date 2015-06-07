package xyz.jadonfowler.minekraft.entity;

import java.util.*;
import xyz.jadonfowler.minekraft.mojang.*;

public class EntityPlayer extends Entity {

    public static ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
    public UUID uuid;
    public String name;

    public EntityPlayer(int i, UUID uuid, double x, double y, double z) {
        this(i, uuid, x, y, z, 0, 0);
    }

    public EntityPlayer(int i, UUID uuid, double x, double y, double z, float yaw, float pitch) {
        super(i, "PLAYER", x, y, z);
        this.uuid = uuid;
        NameFetcher nf = new NameFetcher(Arrays.asList(uuid));
        try {
            name = nf.call().get(uuid);
        }
        catch (Exception e) {
            name = "NULL";
        }
        players.add(this);
    }

    public static EntityPlayer byUUID(UUID u) {
        for (EntityPlayer p : players)
            if (p.uuid.equals(u)) return p;
        return null;
    }
}
