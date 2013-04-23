package com.ruyicai.compress;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressStr {
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static final byte[] compress(byte[] input) {
		if (input == null) {
			return null;
		}
		Deflater compressor = new Deflater();
		compressor.setLevel(Deflater.BEST_COMPRESSION);
		compressor.setInput(input);
		compressor.finish();
		int len = input.length;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(len);
		while (!compressor.finished()) {
			byte[] buf = new byte[len];
			int count = compressor.deflate(buf);
			if (count > 0) {
				bos.write(buf, 0, count);
			}
		}
		return bos.toByteArray();
	}

	/**
	 * 
	 * @param compressed
	 * @return
	 */
	public static final byte[] decompress(byte[] compressed) {
		if (compressed == null) {
			return null;
		}
		try {
			Inflater decompressor = new Inflater();
			decompressor.setInput(compressed);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = compressed.length << 2;
			while (!decompressor.finished()) {
				byte[] buf = new byte[len];
				int count = decompressor.inflate(buf);
				if (count > 0) {
					bos.write(buf, 0, count);
				}
			}
			return bos.toByteArray();
		} catch (DataFormatException e) {
			return compressed;
		}
	}

}
