package xyz.jadonfowler.minekraft.world;

import java.util.*;
import org.spacehq.mc.protocol.data.game.*;
import xyz.jadonfowler.minekraft.pos.*;

public class Block {

    public Chunk chunk;
    public Vector3d pos;
    public Material material;
    private static ArrayList<Block> cache = new ArrayList<Block>();

    private Block(Position p) {
        this(Vector3d.fromPosition(p));
    }

    private Block(double ax, double ay, double az) {
        this(new Vector3d(ax, ay, az).floor().round());
    }

    private Block(Vector3d p) {
        this.pos = p;
        this.chunk = ChunkColumn.getChunk(this);
        Vector3d b = new Vector3d(pos.x > 0 ? pos.x % 16 : 16 - (Math.abs(pos.x) % 16), pos.y > 0 ? pos.y % 16
                : 16 - (Math.abs(pos.y) % 16), pos.z > 0 ? pos.z % 16 : 16 - (Math.abs(pos.z) % 16));
        System.out.println(b);
        int id = 0;
        try {
            id = chunk.getBlocks().getBlock((int) b.x, (int) b.y, (int) b.z);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        material = Material.getMaterial(id);
        cache.add(this);
    }

    public Vector3d toChunkCoords() {
        return new Vector3d(pos.x / 16, pos.y / 16, pos.z / 16).floor().round();
    }

    public Block getRelative(int rx, int ry, int rz) {
        return new Block(pos.x + rx, pos.y + ry, pos.z + rz);
    }

    public int getTypeId() {
        return material.getId();
    }

    public static Block getBlock(Vector3d p) {
        for (Block b : cache)
            if (b.pos.equals(p)) return b;
        return new Block(p);
    }

    public static Block getBlock(double x, double y, double z) {
        return getBlock(new Vector3d(x, y, z));
    }
}
