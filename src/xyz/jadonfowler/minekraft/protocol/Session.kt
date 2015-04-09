package xyz.jadonfowler.minekraft.protocol

import xyz.jadonfowler.minekraft.protocol.packet.*

public trait Session {
	
	fun connect()
	
	fun connect(wait : Boolean)
	
	fun getHost() : String
	
	fun getPort() : Int
	
	fun getPacketProtocol() : PacketProtocol
	
	fun getFlags() : Map<String, Any>
	
	fun hasFlag(key : String) : Boolean
	
	fun getFlag<T>(key : String) : T
	
	fun setFlag(key : String, value : Any)
	
	fun addListener(listener : SessionListener)
	
	fun removeListener(listener : SessionListener)
	
	fun callEvent(event : SessionEvent)
	
	fun getCompressionThreshold() : Int
	
	fun setCompressionThreshold(threshold : Int)
	
	fun getReadTimeout() : Int
	
	fun setReadTimeout(timeout : Int)
	
	fun getWriteTimeout() : Int
	
	fun setWriteTimeout(timeout : Int)
	
	fun getTimeoutHandler() : TimeoutHandler
	
	fun setTimeoutHandler(handler : TimeoutHandler)
	
	fun isConnected() : Boolean
	
	fun send(packet : Packet)
	
	fun disconnect(reason : String)
	
}