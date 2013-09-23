package com.ruyicai.android.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * 该类提供了相关的数据处理方法： 1.数据的解压缩
 * 
 * @author PengCX
 * @since RYC1.0 2013-2-25
 */
public class DataTools {
	/**
	 * 解压压缩的byte数组数据
	 * 
	 * @param aCompressedData
	 *            压缩的数据
	 * @return 解压后的数据
	 */
	public static byte[] decopressData(byte[] aCompressedData) {
		// XXX 解压缩实现是否可以更新
		ByteArrayOutputStream byteArrayOutputStream = null;
		/** 解压后的数据 */
		byte[] decompressData = null;

		// 创建并重置Inflater对象，设置压缩数据
		Inflater inflater = new Inflater();
		inflater.reset();
		inflater.setInput(aCompressedData);

		try {
			byte[] tempByte = new byte[1024];
			byteArrayOutputStream = new ByteArrayOutputStream(
					aCompressedData.length);

			// 如果数据还没有解压完毕，继续解压数据
			while (!inflater.finished()) {
				int inflateLength = inflater.inflate(tempByte);
				byteArrayOutputStream.write(tempByte, 0, inflateLength);
			}

			decompressData = byteArrayOutputStream.toByteArray();
		} catch (DataFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				byteArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return decompressData;
	}
}
