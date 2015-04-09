package xyz.jadonfowler.minekraft.protocol.event.session

public trait SessionEvent {
	
	fun call(listener : SessionListener)
	
}