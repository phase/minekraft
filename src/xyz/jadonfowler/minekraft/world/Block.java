package xyz.jadonfowler.minekraft.world;

import java.util.*;
import org.spacehq.mc.protocol.data.game.*;
import xyz.jadonfowler.minekraft.pos.*;

public class Block {

    public Chunk chunk;
    public Vector3d position;
    public Material material;
    private static ArrayList<Block> cache = new ArrayList<Block>();

    private Block(Position p) {
        this(Vector3d.fromPosition(p));
    }

    private Block(double ax, double ay, double az) {
        this(new Vector3d(ax, ay, az).floor().round());
    }

    private Block(Vector3d p) {
        this.position = p;
        this.chunk = ChunkColumn.getChunk(this);
        Vector3d b = new Vector3d(position.x > 0 ? position.x % 16 : 16 - (Math.abs(position.x) % 16), position.y > 0 ? position.y % 16
                : 16 - (Math.abs(position.y) % 16), position.z > 0 ? position.z % 16 : 16 - (Math.abs(position.z) % 16));
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
        return new Vector3d(position.x / 16, position.y / 16, position.z / 16).floor().round();
    }

    public Block getRelative(int rx, int ry, int rz) {
        return new Block(position.x + rx, position.y + ry, position.z + rz);
    }

    public int getTypeId() {
        return material.getId();
    }
    
    public boolean isAir(){
        return material == Material.AIR;
    }
    
    public static int getId(Vector3d position){
        Chunk chunk = ChunkColumn.getChunk(position);
        Vector3d b = new Vector3d(position.x > 0 ? position.x % 16 : 16 - (Math.abs(position.x) % 16), position.y > 0 ? position.y % 16
                : 16 - (Math.abs(position.y) % 16), position.z > 0 ? position.z % 16 : 16 - (Math.abs(position.z) % 16));
        return chunk.getBlocks().getBlock((int) b.x, (int) b.y, (int) b.z);
    }
    
    public static Block getBlock(Vector3d p) {
        for (Block b : cache)
            if (b.position.equals(p)) return b;
        return new Block(p);
    }

    public static Block getBlock(double x, double y, double z) {
        return getBlock(new Vector3d(x, y, z));
    }
}
