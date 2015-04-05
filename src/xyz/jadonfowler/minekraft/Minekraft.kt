package xyz.jadonfowler.minekraft

import java.net.Proxy

import org.spacehq.mc.protocol.MinecraftProtocol
import org.spacehq.mc.protocol.data.message.Message
import org.spacehq.mc.protocol.packet.ingame.server.*
import org.spacehq.mc.protocol.packet.ingame.client.*
import org.spacehq.packetlib.*
import org.spacehq.packetlib.tcp.*
import org.spacehq.packetlib.event.session.*
import org.spacehq.packetlib.packet.*

	val HOST = "localhost"
	val PORT = 26656
	val PROXY : Proxy = Proxy.NO_PROXY
	val USERNAME = "Username"
	val PASSWORD = "Password"

	fun main(args : Array<String>) {
		println("Minekraft Initializing...")
		val protocol = MinecraftProtocol(USERNAME, PASSWORD, false)
		val client = Client(HOST, PORT, protocol, TcpSessionFactory(PROXY))
		client.getSession().addListener(
			object : SessionAdapter(){
				override fun connected(event : ConnectedEvent){
					println("> Connected!")
				}
				
				override fun packetReceived(event : PacketReceivedEvent){
					val packet : Packet = event.getPacket()
					if(packet is ServerJoinGamePacket){
						event.getSession().send(ClientChatPacket("This is a Minekraft Client!"))
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
		client.getSession().connect()
	}