package xyz.jadonfowler.minekraft.protocol.packet

import xyz.jadonfowler.minekraft.protocol.io.*

public trait PacketHeader {
	
	fun isLengthVariable() : Boolean
	
	fun getLengthSize() : Int
	
	fun getLengthSize(length : Int) : Int
	
	fun readLength(dataIn : NetDataIn, available : Int) : Int
	
	fun writeLength(dataOut : NetDataOut, length : Int)
	
	fun readPacketId(dataIn : NetDataIn) : Int
	
	fun writePacketId(dataOut : NetDataOut, packetId : Int)
	
}