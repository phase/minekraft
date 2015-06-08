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

import xyz.jadonfowler.minekraft.Minekraft;

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

	Minekraft(HOST, PORT, PROXY, USERNAME, PASSWORD);
}
