package xyz.jadonfowler.minekraft.protocol.packet

import xyz.jadonfowler.minekraft.protocol.io.*

public class DefaultPacketHeader : PacketHeader {
	
	override fun isLengthVariable() : Boolean{
		return true
	}
	
	override fun getLengthSize() : Int{
		return 5
	}
	
	override fun getLengthSize(length : Int) : Int{
		return varIntLength(length);
	}
	
	override fun readLength(dataIn : NetDataIn, available : Int) : Int{
		return dataIn.readVarInt()
	}
	
	override fun writeLength(dataOut : NetDataOut, length : Int){
		dataOut writeVarInt length
	}
	
	override fun readPacketId(dataIn : NetDataIn) : Int{
		return dataIn.readVarInt()
	}
	
	override fun writePacketId(dataOut : NetDataOut, packetId : Int){
		dataOut writeVarInt packetId
	}
	
	fun varIntLength(i : Int) : Int{
		if((i and -128) == 0) return 1
		else if((i and -16384) == 0) return 2
		else if ((i and -2097152) == 0) return 3
		else if ((i and -268435456) == 0) return 4
		else return 5
	}
	
}