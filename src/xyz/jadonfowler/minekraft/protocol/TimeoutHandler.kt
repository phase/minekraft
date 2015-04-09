package xyz.jadonfowler.minekraft.protocol

public trait TimeoutHandler {
	
	fun onTimeout(session : Session, type : TimeoutType)
	
}