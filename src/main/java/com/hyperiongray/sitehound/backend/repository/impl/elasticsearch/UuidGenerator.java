package com.hyperiongray.sitehound.backend.repository.impl.elasticsearch;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tomas on 2/16/16.
 */
public class UuidGenerator{

	public static String hash(String url){
		MessageDigest messageDigest = null;
		String hexString;
		try{
			messageDigest = MessageDigest.getInstance("MD5");
			byte[] digest = messageDigest.digest(url.getBytes("UTF-8"));
			hexString = new String(Hex.encodeHex(digest));
			String id = hexString;
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return hexString;

	}
}
