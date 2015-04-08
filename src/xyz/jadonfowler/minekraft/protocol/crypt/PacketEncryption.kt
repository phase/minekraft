package xyz.jadonfowler.minekraft.protocol.crypt

import java.lang.Exception

public trait PacketEncryption {
	
	fun getDecryptOutputSize(length : Int) : Int
	
	fun getEncryptOutputSize(length : Int) : Int
	
	fun decrypt(input : ByteArray, inputOffset : Int, inputLength : Int, output : ByteArray, outputOffset : Int) : Int
	
	fun encrypt(input : ByteArray, inputOffset : Int, inputLength : Int, output : ByteArray, outputOffset : Int) : Int
	
}