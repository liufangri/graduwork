/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sxy.graduwork.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author sxy
 */
public class MD5 {

	private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String Md5_32(String source) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] result = md.digest(source.getBytes());
			// result = token.getBytes();
			char[] resultCharArray = new char[result.length * 2];
			int index = 0;
			for (byte b : result) {
				resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
				resultCharArray[index++] = hexDigits[b & 0xf];
			}
			resultString = new String(resultCharArray);
		} catch (NoSuchAlgorithmException ex) {
			// Do Nothing
		}
		return resultString;

	}

	public static String Md5_16(String source) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] result = md.digest(source.getBytes());
			// result = token.getBytes();
			char[] resultCharArray = new char[result.length * 2];
			int index = 0;
			for (byte b : result) {
				resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
				resultCharArray[index++] = hexDigits[b & 0xf];
			}
			resultString = new String(resultCharArray);
			resultString = resultString.substring(8, 24);
		} catch (NoSuchAlgorithmException ex) {
			// Do Nothing
		}
		return resultString;
	}

}
