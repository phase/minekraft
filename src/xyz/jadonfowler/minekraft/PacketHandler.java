package xyz.jadonfowler.minekraft;

import java.util.*;
import org.spacehq.mc.protocol.data.game.*;
import org.spacehq.mc.protocol.data.game.values.*;
import org.spacehq.mc.protocol.data.game.values.world.block.*;
import org.spacehq.mc.protocol.data.message.*;
import org.spacehq.mc.protocol.packet.ingame.client.*;
import org.spacehq.mc.protocol.packet.ingame.client.player.*;
import org.spacehq.mc.protocol.packet.ingame.server.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.*;
import org.spacehq.mc.protocol.packet.ingame.server.world.*;
import org.spacehq.packetlib.event.session.*;
import xyz.jadonfowler.minekraft.entity.*;
import xyz.jadonfowler.minekraft.world.*;

public class PacketHandler extends SessionAdapter {
    
    @Override public void packetReceived(PacketReceivedEvent event) {
        if (event.getPacket() instanceof ServerJoinGamePacket) {
            event.getSession().send(new ClientChatPacket("PhaseBot has joined the game."));
            Minekraft.getInstace().thePlayer.entityId = event.<ServerJoinGamePacket> getPacket().getEntityId();
        }
        else if (event.getPacket() instanceof ServerPlayerPositionRotationPacket) {
            Minekraft.getInstace().thePlayer.pos.x = event.<ServerPlayerPositionRotationPacket> getPacket().getX();
            Minekraft.getInstace().thePlayer.pos.y = event.<ServerPlayerPositionRotationPacket> getPacket().getY();
            Minekraft.getInstace().thePlayer.pos.z = event.<ServerPlayerPositionRotationPacket> getPacket().getZ();
            Minekraft.getInstace().thePlayer.pitch = event.<ServerPlayerPositionRotationPacket> getPacket().getPitch();
            Minekraft.getInstace().thePlayer.yaw = event.<ServerPlayerPositionRotationPacket> getPacket().getYaw();
            System.out.println("Err, My Position: " + Minekraft.getInstace().thePlayer.pos.x + "," + Minekraft.getInstace().thePlayer.pos.y + ","
                    + Minekraft.getInstace().thePlayer.pos.z);
            event.getSession().send(
                    new ClientPlayerPositionRotationPacket(false, Minekraft.getInstace().thePlayer.pos.x, Minekraft.getInstace().thePlayer.pos.y,
                            Minekraft.getInstace().thePlayer.pos.z, Minekraft.getInstace().thePlayer.pitch, Minekraft.getInstace().thePlayer.yaw));
        }
        else if (event.getPacket() instanceof ServerSpawnObjectPacket) {
            int entityId = event.<ServerSpawnObjectPacket> getPacket().getEntityId();
            double x = event.<ServerSpawnObjectPacket> getPacket().getX();
            double y = event.<ServerSpawnObjectPacket> getPacket().getY();
            double z = event.<ServerSpawnObjectPacket> getPacket().getZ();
            String type = event.<ServerSpawnObjectPacket> getPacket().getType().toString();
            new Entity(entityId, type, x, y, z);
        }
        else if (event.getPacket() instanceof ServerSpawnPlayerPacket) {
            int entityId = event.<ServerSpawnPlayerPacket> getPacket().getEntityId();
            double x = event.<ServerSpawnPlayerPacket> getPacket().getX();
            double y = event.<ServerSpawnPlayerPacket> getPacket().getY();
            double z = event.<ServerSpawnPlayerPacket> getPacket().getZ();
            UUID u = event.<ServerSpawnPlayerPacket> getPacket().getUUID();
            new EntityPlayer(entityId, u, x, y, z);
        }
        else if (event.getPacket() instanceof ServerSpawnMobPacket) {
            int entityId = event.<ServerSpawnMobPacket> getPacket().getEntityId();
            double x = event.<ServerSpawnMobPacket> getPacket().getX();
            double y = event.<ServerSpawnMobPacket> getPacket().getY();
            double z = event.<ServerSpawnMobPacket> getPacket().getZ();
            String type = event.<ServerSpawnMobPacket> getPacket().getType().toString();
            new Entity(entityId, type, x, y, z);
        }
        else if (event.getPacket() instanceof ServerDestroyEntitiesPacket) {
            for (int i : event.<ServerDestroyEntitiesPacket> getPacket().getEntityIds()) {
                if (Entity.entities.containsKey(i)) {
                    Entity e = Entity.byId(i);
                    if (e instanceof EntityPlayer) EntityPlayer.players.remove((EntityPlayer) e);
                    e.remove();
                }
            }
        }
        else if (event.getPacket() instanceof ServerUpdateHealthPacket) {
            if (event.<ServerUpdateHealthPacket> getPacket().getHealth() <= 0) {
                event.getSession().send(new ClientRequestPacket(ClientRequest.RESPAWN));
            }
        }
        else if (event.getPacket() instanceof ServerEntityPositionRotationPacket) {
            ServerEntityMovementPacket p = event.<ServerEntityPositionRotationPacket> getPacket();
            int id = p.getEntityId();
            Entity e = Entity.byId(id);
            e.pos.x += p.getMovementX();
            e.pos.y += p.getMovementY();
            e.pos.z += p.getMovementZ();
            e.pitch = p.getPitch();
            e.yaw = p.getYaw();
        }
        else if (event.getPacket() instanceof ServerEntityTeleportPacket) {
            ServerEntityTeleportPacket p = event.<ServerEntityTeleportPacket> getPacket();
            int id = p.getEntityId();
            Entity e = Entity.byId(id);
            e.pos.x = p.getX();
            e.pos.y = p.getY();
            e.pos.z = p.getZ();
            e.pitch = p.getPitch();
            e.yaw = p.getYaw();
        }
        else if (event.getPacket() instanceof ServerChunkDataPacket) {
            ServerChunkDataPacket p = event.<ServerChunkDataPacket> getPacket();
            new ChunkColumn(p.getX(), p.getZ(), p.getChunks());
        }
        else if (event.getPacket() instanceof ServerMultiChunkDataPacket) {
            for (int i = 0; i < event.<ServerMultiChunkDataPacket> getPacket().getColumns(); i++) {
                int chunkX = event.<ServerMultiChunkDataPacket> getPacket().getX(i);
                int chunkZ = event.<ServerMultiChunkDataPacket> getPacket().getZ(i);
                new ChunkColumn(chunkX, chunkZ, event.<ServerMultiChunkDataPacket> getPacket().getChunks(i));
            }
        }
        else if (event.getPacket() instanceof ServerMultiBlockChangePacket) {
            ServerMultiBlockChangePacket packet = event.<ServerMultiBlockChangePacket> getPacket();
            for (BlockChangeRecord r : packet.getRecords()) {
                Position p = r.getPosition();
                int id = r.getBlock();
                ChunkColumn.setBlock(p, id / 16);
            }
        }
        else if (event.getPacket() instanceof ServerBlockChangePacket) {
            Position p = event.<ServerBlockChangePacket> getPacket().getRecord().getPosition();
            int id = event.<ServerBlockChangePacket> getPacket().getRecord().getBlock();
            ChunkColumn.setBlock(p, id / 16);
        }
        else if (event.getPacket() instanceof ServerChatPacket) {
            Message message = event.<ServerChatPacket> getPacket().getMessage();
            String text = message.getFullText();
            //TODO Render chat
            System.out.println(text);
        }
    }

    @Override public void disconnected(DisconnectedEvent event) {
        System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
    }
}
