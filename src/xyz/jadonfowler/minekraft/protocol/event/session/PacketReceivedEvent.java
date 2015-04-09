package xyz.jadonfowler.minekraft.protocol.event.session;

import xyz.jadonfowler.minekraft.protocol.Session;
import xyz.jadonfowler.minekraft.protocol.packet.Packet;

public class PacketReceivedEvent implements SessionEvent {

	private Session session;
	private Packet packet;
	
	public PacketReceivedEvent(Session s, Packet p){
		session = s;
		packet = p;
	}
	
	public Session getSession(){
		return session;
	}
	
	public <p extends Packet> p getPacket(){
		try{ 
			return (p) packet;
		}catch(Exception e){
			throw new IllegalArgumentException("The packet should be: " + packet.getClass().getName());
		}
	}
	
	@Override
	public void call(SessionListener arg0) {
		// TODO Auto-generated method stub

	}

}
