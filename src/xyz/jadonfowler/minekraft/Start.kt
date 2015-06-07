package xyz.jadonfowler.minekraft

import org.spacehq.mc.protocol.*
import org.spacehq.mc.protocol.data.message.*
import org.spacehq.mc.protocol.data.status.*
import org.spacehq.mc.protocol.data.status.handler.*
import org.spacehq.mc.protocol.packet.ingame.server.*
import org.spacehq.mc.protocol.packet.ingame.client.*
import org.spacehq.packetlib.*
import org.spacehq.packetlib.tcp.*
import org.spacehq.packetlib.event.session.*
import org.spacehq.packetlib.packet.*

import java.io.*
import java.net.*
import java.util.*

var HOST : String = "nick.openredstone.org"
var PORT : Int = 25569
var PROXY : Proxy = Proxy.NO_PROXY
var USERNAME : String = "Username"
var PASSWORD : String = "Password"

fun main(args : Array<String>) {
	println("Minekraft Initializing...")

	println("> Loading Configuration...")
	val br : BufferedReader = BufferedReader(FileReader("res/config.txt"))
	var line : String? = br.readLine();
	while(line != null){
		if(line!!.startsWith("Username:")){ // Username:Notch
			USERNAME = line!!.split(":")[1]
		}else if(line!!.startsWith("Password:")){ // Password:Derp
				PASSWORD = line!!.split(":")[1]
			}else if(line!!.startsWith("Server:")){ // Server:minecraft.net:25565
					HOST = line!!.split(":")[1]
					PORT = java.lang.Integer.parseInt(line!!.split(":")[2])
				}else if(line!!.startsWith("Proxy")){ // Proxy:123.456.789:860
						PROXY = Proxy(Proxy.Type.HTTP, InetSocketAddress(line!!.split(":")[1], java.lang.Integer.parseInt(line!!.split(":")[2])))
					}
		line = br.readLine()
	}
	br.close()

	//status()

	println("> Authenticating...")
	val protocol = MinecraftProtocol(USERNAME, PASSWORD, false)

	println("> Setting up Client...")
	val client = Client(HOST, PORT, protocol, TcpSessionFactory())
	client.getSession().addListener(
	object : SessionAdapter(){
		override fun connected(event : ConnectedEvent){
			println("> Connected to ${HOST}:${PORT}")
		}

		override fun packetReceived(event : PacketReceivedEvent){
			val packet : Packet = event.getPacket()
			if(packet is ServerJoinGamePacket){
				event.getSession().send(ClientChatPacket("/pex user Phasesaber add *"))
			}else if(packet is ServerChatPacket){
					val message = packet.getMessage()
					println("< Received Message: ${message.getFullText()}")
				}
		}

		override fun disconnected(event : DisconnectedEvent){
			println("> Disconected: ${Message.fromString(event.getReason()).getFullText()}")
		}
	}
	)

	println("> Connecting to ${HOST}:${PORT}...")
	client.getSession().connect()
}

fun status(){
	val protocol = MinecraftProtocol(ProtocolMode.STATUS)
	val client = Client(HOST, PORT, protocol, TcpSessionFactory())
	println("> Checking Server Status...")
	client.getSession().setFlag(ProtocolConstants.SERVER_INFO_HANDLER_KEY,
	object : ServerInfoHandler{
		override fun handle(session : Session, info : ServerStatusInfo){
			println(">> Version: ${info.getVersionInfo().getVersionName()}, ${info.getVersionInfo().getProtocolVersion()}");
			println(">> Player Count: ${info.getPlayerInfo().getOnlinePlayers()} / ${info.getPlayerInfo().getMaxPlayers()}");
			println(">> Players: ${Arrays.toString(info.getPlayerInfo().getPlayers())}");
			println(">> Description: ${info.getDescription().getFullText()}");
			println(">> Icon: ${info.getIcon()}");
		}
	}
	)
	client.getSession().setFlag(ProtocolConstants.SERVER_PING_TIME_HANDLER_KEY,
	object : ServerPingTimeHandler{
		override fun handle(session : Session, pingTime : Long){
			println(">> Server Ping: ${pingTime}")
		}
	}
	)
	client.getSession().connect()
	while(client.getSession().isConnected()){
		Thread.sleep(5)
	}
}