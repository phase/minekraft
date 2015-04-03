package xyz.jadonfowler.minekraft

import org.spacehq.*

import kotlin.concurrent.*

import java.net.Proxy
import java.util.Arrays

//val CREATE_SERVER = true;
val VERIFY_USERS = false;
val HOST = "127.0.0.1";
val PORT = 25565;
val PROXY : Proxy = Proxy.NO_PROXY;
val USERNAME = "Username";
val PASSWORD = "Password";

fun main(args : ArrayList<String>){
    println("Minekraft initializing...")
  /*if(CREATE_SERVER){
        var server = Server(HOST, PORT, MinecraftProtocol.class, TcpSessionFactory(PROXY))
        server.setGlobalFlag(ProtocolConstants.VERFIFY_USERS_KEY, VERIFY_USERS)
        server.setGlobalFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY, 
            object : ServerInfoBuilder(){
                override fun buildInfo(session : Session) : ServerStatusInfo {
                    return ServerStatusInfo(
                        version = VersionInfo(ProtocolConstants.GAME_VERSION, ProtocolConstants.PROTOCOL_VERSION),
                        players = PlayerInfo(100, 0, GameProfile[0]),
                        description = new TextMessage("Minekraft Server!"),
                        icon = null
                    )
                }
            }
    }*/
    var protocol = MinecraftProtocol(ProtocolMode.STATUS)
    var client = Client(HOST, PORT, protocol, TcpSessionFactory(PROXY))
    client.getSession().setFlag(ProtocolConstants.SERVER_INFO_HANDLER_KEY, 
        object : ServerInfoHandler() {
            override fun handle(session : Session, info : ServerStatusInfo) {
                println("Version: ${info.getVersionInfo().getVersionName()}, ${info.getVersionInfo().getProtocolVersion()}");
                println("Player Count: ${info.getPlayerInfo().getOnlinePlayers()} / ${info.getPlayerInfo().getMaxPlayers()}");
                println("Players: ${Arrays.toString(info.getPlayerInfo().getPlayers())}");
                println("Description: ${info.getDescription().getFullText()}");
                println("Icon: ${info.getIcon()}");
            }
        })

    client.getSession().setFlag(ProtocolConstants.SERVER_PING_TIME_HANDLER_KEY,
        object : ServerPingTimeHandler() {
            override fun handle(session : Session, pingTime : Long) {
                println("Server ping took ${pingTime} ms");
        }
    });

    client.getSession().connect()
    while(client.getSession().isConnected()) {
        try {
            Thread.sleep(5)
        } catch(InterruptedException e) {
            e.printStackTrace()
        }
    }

    login()
}

fun login(){
    val protocol = MinecraftProtocol(USERNAME, PASSWORD, false)
    val client = Client(HOST, PORT, protocol, TcpSessionFactory(PROXY))
    client.getSession().addListener(
        object : SessionAdapter(){
            override fun packetReceived(event : PacketReceivedEvent){
                if(event.getPacket() is ServerJoinGamePacket){
                    event.getSession().send(ClientChatPacket("This is a Minekraft Client!"))
                }else if(event.getPacket() is ServerChatPacket){
                    val message = event.<ServerChatPacket>getPacket().getMessage()
                    println("Received Message: ${message.getFullText()}")
                    if(message is TranslationMessage){
                        println("Received Translation Components: ${Arrays.toString(message.getTranslationParams())}")
                    }
                    event.getSession().disconnect("Done") 
                }
            }

            override fun disconnected(event : DisconnectedEvent){
                println("Disconnected: ${Message.fromString(event.getReason()).getFullText()}")
            }
        }
    )
    client.getSession().connect()
}
