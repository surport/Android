package com.ruyicai.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ToolsAesCrypt {
	public static String Decrypt(String sSrc, String sKey) throws Exception {
		try {
			byte[] raw = sKey.getBytes("GBK");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = hex2byte(sSrc.getBytes());
			try {
				byte[] original = cipher.doFinal(encrypted1);
				return new String(original);
			} catch (Exception e) {
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
	}

	public static String Encrypt(String sSrc, String sKey) throws Exception {

		byte[] raw = sKey.getBytes("GBK");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
		return byte2hex(encrypted).toLowerCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("闀垮害涓嶆槸鍋舵暟");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		// System.out.println(hs);
		return hs.toUpperCase();
	}

	public static byte[] encrypt2Bytes(byte[] src, String sKey) {
		try {
			// byte[] raw = sKey.getBytes("GBK");
			byte[] raw = sKey.getBytes("UTF-8");
			;
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			byte[] encrypted = cipher.doFinal(src);
			return encrypted;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static byte[] decrypt2Bytes(byte[] src, String sKey)
			throws Exception {
		try {

			byte[] raw = sKey.getBytes("UTF-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			// byte[] encrypted1 = hex2byte(src);
			byte[] encrypted1 = src;
			try {
				byte[] original = cipher.doFinal(encrypted1);
				return original;
			} catch (Exception e) {
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
	}

	public static void main(String[] args) {
		byte bytes[] = "你好".getBytes();

		for (int i = 0; i < bytes.length; i++) {
			System.out.println(bytes[i]);
		}
	}
}
