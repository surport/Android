package com.ruyicai.android.tools;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 提供了相关的加密和解密方法： 1.DES加密和解密 2.Base64加密和解密
 * 
 * @author PengCX
 * @since RYC1.0 2013-2-25
 */
public class EncryptTools {
	/** Base64编码字符数组 */
	private static char[]		fBase64EncodeChars	= new char[] { 'A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	/** Base64解码数组 */
	private static byte[]		fBase64DecodeChars	= new byte[] { -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57,
			58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7,
			8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
			25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
			36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1,
			-1, -1, -1, -1							};

	/** DES加密密钥 */
	public static final String	DES_ENCRYPT_KEY		= "<>hj12@#$$%^~~ff";

	/**
	 * DES加密方法
	 * 
	 * @param aSrcString
	 *            源字符串
	 * @return 加密后字符串
	 */
	public static String desEncrypt(String aSrcString) {
		// XXX 加密方法是否可以更新
		byte[] encrypted = null;

		try {
			// 创建一个SecretKeySpec对象
			byte[] keyByte = DES_ENCRYPT_KEY.getBytes("GBK");
			// 获取数据执行并加密
			SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");

			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			encrypted = cipher.doFinal(aSrcString.getBytes());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return byteToHexadecimal(encrypted);
	}

	/**
	 * 将byte数组转换成十六进制字符串:如byte[] = {10,-10,150,-150}则返回“0AF58C69”
	 * 
	 * @param aEncrypted
	 *            byte数组
	 * @return 十六进制结果字符串
	 */
	private static String byteToHexadecimal(byte[] aEncrypted) {
		// XXX 解密方法是否可以更新

		String hexadecimalString = "";

		for (int encryted_i = 0; encryted_i < aEncrypted.length; encryted_i++) {
			/**
			 * 将byte（-128~127）转换成十六进制字符串：如果为正数，则数值不变；如果为负数
			 * ，则数值=256-绝对值。如10运算为10，补“0”,则十六进制为0A；-10 的运算为245，则十六进制编码为F5
			 */
			String temp = Integer.toHexString(aEncrypted[encryted_i] & 0XFF);

			// 如果十六进制为1位，则在前面补“0”
			if (temp.length() == 1) {
				temp = "0" + temp;
			}

			hexadecimalString = hexadecimalString + temp;
		}

		return hexadecimalString.toLowerCase();
	}

	/**
	 * DES解密方法
	 * 
	 * @param aEncrypString
	 *            加密的字符串
	 * @return 解密的结果字符串
	 */
	public static String desDecrypt(String aEncrypString) {
		String descryptString = null;

		try {
			byte[] keyByte = DES_ENCRYPT_KEY.getBytes("GBK");
			SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);

			byte[] encryptedByte = hexadecimalToByte(aEncrypString);

			byte[] decryptByte = cipher.doFinal(encryptedByte);

			descryptString = new String(decryptByte);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return descryptString;

	}

	/**
	 * 将十六进制字符串转换为byte数组
	 * 
	 * @param aEncrypString
	 *            十六进制字符串
	 * @return byte数组结果
	 */
	private static byte[] hexadecimalToByte(String aEncrypString) {
		byte[] encryptedByte;
		byte[] bytes = aEncrypString.getBytes();

		if ((bytes.length % 2) == 0) {
			encryptedByte = new byte[bytes.length / 2];

			for (int bytes_i = 0; bytes_i < bytes.length; bytes_i += 2) {
				String temp = new String(bytes, bytes_i, 2);
				encryptedByte[bytes_i / 2] = (byte) Integer.parseInt(temp, 16);
			}
		}
		// 如果加密的字符串数组的长度为奇数，则出现异常
		else {
			throw new IllegalArgumentException("DES加密的Byte数组错误...");
		}

		return encryptedByte;
	}

	/**
	 * 将byte数组使用Base64编码
	 * 
	 * @param aSrcByte
	 *            进行编码的byte数组
	 * @return Base64编码的字符串
	 */
	public static String encodeBase64(byte[] aSrcByte) {
		// XXX 寻找封装的Base64解密算法，不必自己实现
		StringBuffer stringBuffer = new StringBuffer();
		int dataLength = aSrcByte.length;
		int b1, b2, b3;

		int data_i = 0;

		while (data_i < dataLength) {
			b1 = aSrcByte[data_i++] & 0xff;
			if (data_i == dataLength) {
				stringBuffer.append(fBase64EncodeChars[b1 >>> 2]);
				stringBuffer.append(fBase64EncodeChars[(b1 & 0x3) << 4]);
				stringBuffer.append("==");
				break;
			}

			b2 = aSrcByte[data_i++] & 0xff;
			if (data_i == dataLength) {
				stringBuffer.append(fBase64EncodeChars[b1 >>> 2]);
				stringBuffer.append(fBase64EncodeChars[((b1 & 0x03) << 4)
						| ((b2 & 0xf0) >>> 4)]);
				stringBuffer.append(fBase64EncodeChars[(b2 & 0x0f) << 2]);
				stringBuffer.append("=");
				break;
			}

			b3 = aSrcByte[data_i++] & 0xff;
			stringBuffer.append(fBase64EncodeChars[b1 >>> 2]);
			stringBuffer.append(fBase64EncodeChars[((b1 & 0x03) << 4)
					| ((b2 & 0xf0) >>> 4)]);
			stringBuffer.append(fBase64EncodeChars[((b2 & 0x0f) << 2)
					| ((b3 & 0xc0) >>> 6)]);
			stringBuffer.append(fBase64EncodeChars[b3 & 0x3f]);
		}

		return stringBuffer.toString();
	}

	/**
	 * 将Base64编码字符串解码为byte数组
	 * 
	 * @param aBase64String
	 *            Base64编码字符串
	 * @return 解码的byte数组
	 */
	public static byte[] base64Decode(String aBase64String) {
		// XXX 寻找封装的Base64加密算法，不必自己实现
		byte[] encodeByte = aBase64String.getBytes();
		int byteLength = encodeByte.length;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
				byteLength);
		int b1, b2, b3, b4;

		int byte_i = 0;
		while (byte_i < byteLength) {
			do {
				b1 = fBase64DecodeChars[encodeByte[byte_i++]];
			} while (byte_i < byteLength && b1 == -1);
			if (b1 == -1) {
				break;
			}

			do {
				b2 = fBase64DecodeChars[encodeByte[byte_i++]];
			} while (byte_i < byteLength && b2 == -1);
			if (b2 == -1) {
				break;
			}
			byteArrayOutputStream
					.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

			do {
				b3 = encodeByte[byte_i++];
				if (b3 == 61) {
					return byteArrayOutputStream.toByteArray();
				}
				b3 = fBase64DecodeChars[b3];
			} while (byte_i < byteLength && b3 == -1);
			if (b3 == -1) {
				break;
			}
			byteArrayOutputStream
					.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

			do {
				b4 = encodeByte[byte_i++];
				if (b4 == 61) {
					return byteArrayOutputStream.toByteArray();
				}
				b4 = fBase64DecodeChars[b4];
			} while (byte_i < byteLength && b4 == -1);
			if (b4 == -1) {
				break;
			}
			byteArrayOutputStream.write((int) (((b3 & 0x03) << 6) | b4));
		}

		return byteArrayOutputStream.toByteArray();
	}
}
