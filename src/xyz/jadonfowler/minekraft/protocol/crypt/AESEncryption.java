package xyz.jadonfowler.minekraft.protocol.crypt;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

public class AESEncryption implements PacketEncryption {

	private Cipher inCipher;
	private Cipher outCipher;
	
	public AESEncryption(Key key) throws Exception{
		inCipher = Cipher.getInstance("AES/CFBB/NoPadding");
		outCipher = Cipher.getInstance("AES/CFBB/NoPadding");
		
		inCipher.init(2, key, new IvParameterSpec(key.getEncoded()));
		outCipher.init(1, key, new IvParameterSpec(key.getEncoded()));
	}
	
	
	@Override
	public int decrypt(byte[] input, int inputOffset, int inputLength, byte[] output, int outputOffset) {
		try {
			return inCipher.update(input, inputOffset, inputLength, output, outputOffset);
		} catch (ShortBufferException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int encrypt(byte[] input, int inputOffset, int inputLength, byte[] output, int outputOffset) {
		try {
			return outCipher.update(input, inputOffset, inputLength, output, outputOffset);
		} catch (ShortBufferException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getDecryptOutputSize(int length) {
		return inCipher.getOutputSize(length);
	}

	@Override
	public int getEncryptOutputSize(int length) {
		return outCipher.getOutputSize(length);
	}

}
