package xyz.jadonfowler.minekraft.protocol.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class StreamNetDataOut implements NetDataOut{

	private OutputStream out;

	public StreamNetDataOut(OutputStream o) {
		out = o;
	}

	@Override
	public void writeBoolean(boolean b)  {
		writeByte(b ? 1 : 0);
	}

	@Override
	public void writeByte(int b)  {
		try {
			out.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeShort(int s)  {
		writeByte((byte) ((s >>> 8) & 0xFF));
		writeByte((byte) ((s >>> 0) & 0xFF));
	}

	@Override
	public void writeChar(int c)  {
		 writeByte((byte) ((c >>> 8) & 0xFF));
		 writeByte((byte) ((c >>> 0) & 0xFF));
	}

	@Override
	public void writeInt(int i)  {
		 writeByte((byte) ((i >>> 24) & 0xFF));
		 writeByte((byte) ((i >>> 16) & 0xFF));
		 writeByte((byte) ((i >>> 8) & 0xFF));
		 writeByte((byte) ((i >>> 0) & 0xFF));
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
		 writeByte((byte) (l >>> 56));
		 writeByte((byte) (l >>> 48));
		 writeByte((byte) (l >>> 40));
		 writeByte((byte) (l >>> 32));
		 writeByte((byte) (l >>> 24));
		 writeByte((byte) (l >>> 16));
		 writeByte((byte) (l >>> 8));
		 writeByte((byte) (l >>> 0));
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
		 writeInt(Float.floatToIntBits(f));
	}

	@Override
	public void writeDouble(double d)  {
		 writeLong(Double.doubleToLongBits(d));
	}

	@Override
	public void writePrefixedBytes(byte b[])  {
		 writeShort(b.length);
		 writeBytes(b);
	}

	@Override
	public void writeBytes(byte b[])  {
		 writeBytes(b, b.length);
	}

	@Override
	public void writeBytes(byte b[], int length)  {
		 try {
			out.write(b, 0, length);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			e.printStackTrace();
		}
		if(bytes.length > 32767) {
			try {
				throw new IOException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		 try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
