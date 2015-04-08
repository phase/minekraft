package xyz.jadonfowler.minekraft.protocol.io

import java.util.UUID

public trait NetDataIn {
	
	fun readBoolean() : Boolean
	
	fun readByte() : Byte
	
	fun readUnsignedByte() : Int
	
	fun readShort() : Short
	
	fun readUnsignedShort() : Int
	
	fun readChar() : Char
	
	fun readInt() : Int
	
	/** VarInt is a type of into with only necessary bytes used to save bandwith. */
	fun readVarInt() : Int
	
	fun readLong() : Long
	
	fun readVarLong() : Long
	
	fun readFloat() : Float
	
	fun readDouble() : Double
	
	fun readPrefixedBytes() : ByteArray
	
	fun readBytes(length : Int) : ByteArray
	
	fun readBytes(b : ByteArray) : Int
	
	fun readBytes(b : ByteArray, offset : Int, length : Int) : Int
	
	fun readString() : String
	
	fun readUUID() : UUID
	
	fun available() : Int
	
}