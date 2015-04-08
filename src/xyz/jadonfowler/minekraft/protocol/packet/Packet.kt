package xyz.jadonfowler.minekraft.protocol.packet

import xyz.jaodnfowler.minekraft.protocol.io.*

public trait Packet {
	
	fun read(in : NetDataIn)
	
	fun write(out : NetDataOut)
	
	fun hasPriority() : Boolean
	
}