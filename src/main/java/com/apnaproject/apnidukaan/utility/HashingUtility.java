package com.apnaproject.apnidukaan.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashingUtility {

	/**
	 * This method is to hash the given data using a hashing algorithm.
	 * 
	 * @param data
	 * 
	 * @return hashed data
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public static String getHashValue(String data) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");

		md.update(data.getBytes());

		byte[] byteData = md.digest();

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {

			String hex = Integer.toHexString(0xff & byteData[i]);

			if (hex.length() == 1)
				hexString.append('0');

			hexString.append(hex);
		}

		return hexString.toString();
	}
}