package xyz.jadonfowler.minekraft.protocol.io

import java.util.UUID

public trait NetDataOut {
	
	fun writeBoolean(b : Boolean)
	
	fun writeByte(b : Int)
	
	fun writeShort(s : Int)
	
	fun writeChar(c : Int)
	
	fun writeInt(i : Int)
	
	fun writeVarInt(i : Int)
	
	fun writeLong(l : Long)
	
	fun writeVarLong(l : Long)
	
	fun writeFloat(f : Float)
	
	fun writeDouble(d : Double)
	
	fun writePrefixedBytes(b : ByteArray)
	
	fun writeBytes(b : ByteArray)
	
	fun writeBytes(b : ByteArray, length : Int)
	
	fun writeString(s : String)
	
	fun writeUUID(u : UUID)
	
	fun flush()
	
}