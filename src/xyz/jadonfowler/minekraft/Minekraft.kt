package xyz.jadonfowler.minekraft

import java.net.Proxy

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
import java.util.*

	var HOST = "oc.tc"
	var PORT : Int = 26656
	val PROXY : Proxy = Proxy.NO_PROXY
	var USERNAME = "Username"
	var PASSWORD = "Password"

	fun main(args : Array<String>) {
		println("Minekraft Initializing...")
		
		val br : BufferedReader = BufferedReader(FileReader("res/config.txt"))
		var line : String? = br.readLine();
		while(line != null){
			if(line!!.startsWith("Username:")){
				USERNAME = line!!.split(":")[1]
			}else if(line!!.startsWith("Password:")){
				PASSWORD = line!!.split(":")[1]
			}else if(line!!.startsWith("Server:")){
				HOST = line!!.split(":")[1]
			}else if(line!!.startsWith("Port")){
				PORT = java.lang.Integer.parseInt(line!!.split(":")[1])
			}
			line = br.readLine()
		}
		br.close()
		
		status()
		
		println("> Authenticating...")
		val protocol = MinecraftProtocol(USERNAME, PASSWORD, false)
		
		println("> Setting up Client...")
		val client = Client(HOST, PORT, protocol, TcpSessionFactory(PROXY))
		client.getSession().addListener(
			object : SessionAdapter(){
				override fun connected(event : ConnectedEvent){
					println("> Connected!")
				}
				
				override fun packetReceived(event : PacketReceivedEvent){
					val packet : Packet = event.getPacket()
					if(packet is ServerJoinGamePacket){
						event.getSession().send(ClientChatPacket("This is a Minekraft Client! https://github.com/phase/minekraft/"))
					}else if(packet is ServerChatPacket){
						val message = packet.getMessage()
						println("Received Message: ${message.getFullText()}")
					}
				}
			
				override fun disconnected(event : DisconnectedEvent){
					println("Disconected: ${Message.fromString(event.getReason()).getFullText()}")
				}
			}
		)
	
		println("> Connecting...")
		client.getSession().connect()
	}

	fun status(){
		val protocol = MinecraftProtocol(ProtocolMode.STATUS)
		val client = Client(HOST, PORT, protocol, TcpSessionFactory(PROXY))
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