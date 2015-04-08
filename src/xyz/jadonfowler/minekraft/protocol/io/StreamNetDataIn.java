package xyz.jadonfowler.minekraft.protocol.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * NetDataIn implementation using InputStream.
 */
public class StreamNetDataIn implements NetDataIn{
	
	private InputStream in;
	
	public StreamNetDataIn(InputStream input){
		in = input;
	}
	
	@Override
	public boolean readBoolean(){
		return readByte() == 1;
	}
	
	@Override
	public byte readByte(){
		return (byte) readUnsignedByte();
	}
	
	@Override
	public int readUnsignedByte(){
		int b = -1;
		try {
			b = in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(b < 0)
			try {
				throw new EOFException();
			} catch (EOFException e) {
				e.printStackTrace();
			}
		return b;
	}
	
	@Override
	public short readShort(){
		return (short) readUnsignedShort();
	}
	
	@Override
	public int readUnsignedShort(){
		int b1 = readUnsignedByte();
		int b2 = readUnsignedByte();
		return (b1 << 8) + (b2 << 0);
	}
	
	@Override
	public char readChar(){
		return (char) readUnsignedShort();
	}
	
	@Override
	public int readInt(){
		int b1 = readUnsignedByte();
		int b2 = readUnsignedByte();
		int b3 = readUnsignedByte();
		int b4 = readUnsignedByte();
		return (b1 << 24) + (b2 << 16) + (b3 << 8) + (b4 << 0);
	}
	
	@Override
	public int readVarInt(){
		int value = 0;
		int size = 0;
		int b;
		while(((b = readByte()) & 0x80) == 0x80){
			value |= (b & 0x7f) << (size++ * 7);
			if(size > 5)
				try {
					throw new IOException("VarInt must be less than 5.");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return value | ((b & 0x7F) << (size * 7));
	}
	
	@Override
	public long readLong(){
		byte[] read = readBytes(8);
		return ((long) read[0] << 56) + ((long) (read[1] & 255) << 48) + ((long) (read[2] & 255) << 40) + ((long) (read[3] & 255) << 32) + ((long) (read[4] & 255) << 24) + ((read[5] & 255) << 24) + ((read[5] & 255) << 16) + ((read[6] & 255) << 8) + ((read[7] & 255) << 0);
	}
	
	@Override
	public long readVarLong(){
		int value = 0;
		int size = 0;
		int b;
		while(((b = readByte()) & 0x80) == 0x80){
			value |= (b & 0x7f) << (size++ * 7);
			if(size > 10)
				try {
					throw new IOException("VarLong must be less than 10.");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return value | ((b & 0x7F) << (size * 7));
	}
	
	@Override
	public float readFloat(){
		return Float.intBitsToFloat(readInt());
	}
	
	@Override
	public double readDouble(){
		return Double.longBitsToDouble(readLong());
	}
	
	@Override
	public byte[] readPrefixedBytes(){
		return readBytes(readShort());
	}
	
	@Override
	public byte[] readBytes(int length){
		if(length < 0) throw new IllegalArgumentException("Array can't have a length less than 0.");
		
		byte[] b = new byte[length];
		int n = 0;
		while(n < length){
			int count = 0;
			try {
				count = in.read(b, n, length - n);
			} catch (IOException e) {
				e.printStackTrace();
			}
			n += count;
		}
		return b;
	}
	
	@Override
	public int readBytes(byte[] b){
		try {
			return in.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int readBytes(byte[] b, int offset, int length){
		try {
			return in.read(b, offset, length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return length;
	}
	
	@Override
	public String readString(){
		try {
			return new String(readBytes(readVarInt()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public UUID readUUID(){
		return new UUID(readLong(), readLong());
	}
	
	@Override
	public int available(){
		try {
			return in.available();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}