package xyz.jadonfowler.minekraft.protocol.io.bytebuf;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import xyz.jadonfowler.minekraft.protocol.io.NetDataOut;

public class ByteBufNetDataOut implements NetDataOut {

	private ByteBuf buf;

	public ByteBufNetDataOut(ByteBuf b) {
		buf = b;
	}

	@Override
	public void writeBoolean(boolean b)  {
		buf.writeBoolean(b);
	}

	@Override
	public void writeByte(int b)  {
		buf.writeByte(b);
	}

	@Override
	public void writeShort(int s)  {
		buf.writeShort(s);
	}

	@Override
	public void writeChar(int c)  {
		buf.writeChar(c);
	}

	@Override
	public void writeInt(int i)  {
		buf.writeInt(i);
	}

	@Override
	public void writeVarInt(int i)  {
		while((i & ~0x7F) != 0) {
			writeByte((i & 0x7F) | 0x80);
			i >>>= 7;
		}

		writeByte(i);
	}

	@Override
	public void writeLong(long l)  {
		buf.writeLong(l);
	}

	@Override
	public void writeVarLong(long l)  {
		while((l & ~0x7F) != 0) {
			writeByte((int) (l & 0x7F) | 0x80);
			l >>>= 7;
		}

		writeByte((int) l);
	}

	@Override
	public void writeFloat(float f)  {
		buf.writeFloat(f);
	}

	@Override
	public void writeDouble(double d)  {
		buf.writeDouble(d);
	}

	@Override
	public void writePrefixedBytes(byte b[])  {
		buf.writeShort(b.length);
		buf.writeBytes(b);
	}

	@Override
	public void writeBytes(byte b[])  {
		buf.writeBytes(b);
	}

	@Override
	public void writeBytes(byte b[], int length)  {
		buf.writeBytes(b, 0, length);
	}

	@Override
	public void writeString(String s)  {
		if(s == null) {
			throw new IllegalArgumentException("String cannot be null!");
		}

		byte[] bytes = null;
		try {
			bytes = s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bytes.length > 32767) {
			throw new IllegalArgumentException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
		} else {
			writeVarInt(bytes.length);
			writeBytes(bytes);
		}
	}

	@Override
	public void writeUUID(UUID uuid)  {
		writeLong(uuid.getMostSignificantBits());
		writeLong(uuid.getLeastSignificantBits());
	}

	@Override
	public void flush()  {
	}
}