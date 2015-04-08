package xyz.jadonfowler.minekraft.protocol.io.bytebuf;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import xyz.jadonfowler.minekraft.protocol.io.NetDataIn;

public class ByteBufNetDataIn implements NetDataIn {

	private ByteBuf buf;

	public ByteBufNetDataIn(ByteBuf b) {
		buf = b;
	}

	@Override
	public boolean readBoolean()  {
		return buf.readBoolean();
	}

	@Override
	public byte readByte()  {
		return buf.readByte();
	}

	@Override
	public int readUnsignedByte()  {
		return buf.readUnsignedByte();
	}

	@Override
	public short readShort()  {
		return buf.readShort();
	}

	@Override
	public int readUnsignedShort()  {
		return buf.readUnsignedShort();
	}

	@Override
	public char readChar()  {
		return buf.readChar();
	}

	@Override
	public int readInt()  {
		return buf.readInt();
	}

	@Override
	public int readVarInt()  {
		int value = 0;
		int size = 0;
		int b;
		while(((b = readByte()) & 0x80) == 0x80) {
			value |= (b & 0x7F) << (size++ * 7);
			if(size > 5) {
				throw new IllegalArgumentException("VarInt too long (length must be <= 5)");
			}
		}

		return value | ((b & 0x7F) << (size * 7));
	}

	@Override
	public long readLong()  {
		return buf.readLong();
	}

	@Override
	public long readVarLong()  {
		int value = 0;
		int size = 0;
		int b;
		while(((b = readByte()) & 0x80) == 0x80) {
			value |= (b & 0x7F) << (size++ * 7);
			if(size > 10) {
				throw new IllegalArgumentException("VarLong too long (length must be <= 10)");
			}
		}

		return value | ((b & 0x7F) << (size * 7));
	}

	@Override
	public float readFloat()  {
		return buf.readFloat();
	}

	@Override
	public double readDouble()  {
		return buf.readDouble();
	}

	@Override
	public byte[] readPrefixedBytes()  {
		short length = buf.readShort();
		return readBytes(length);
	}

	@Override
	public byte[] readBytes(int length)  {
		if(length < 0) {
			throw new IllegalArgumentException("Array cannot have length less than 0.");
		}

		byte b[] = new byte[length];
		buf.readBytes(b);
		return b;
	}

	@Override
	public int readBytes(byte[] b)  {
		return readBytes(b, 0, b.length);
	}

	@Override
	public int readBytes(byte[] b, int offset, int length)  {
		int readable = buf.readableBytes();
		if(readable <= 0) {
			return -1;
		}

		if(readable < length) {
			length = readable;
		}

		buf.readBytes(b, offset, length);
		return length;
	}

	@Override
	public String readString()  {
		int length = readVarInt();
		byte bytes[] = readBytes(length);
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UUID readUUID()  {
		return new UUID(readLong(), readLong());
	}

	@Override
	public int available()  {
		return buf.readableBytes();
	}

}
