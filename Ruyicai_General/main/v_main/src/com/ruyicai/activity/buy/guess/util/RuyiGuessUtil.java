package com.ruyicai.activity.buy.guess.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

public class RuyiGuessUtil {

	public static boolean isExist(String path, String fileName) {
		File directory = new File(path);
		if (directory.isDirectory() && !directory.exists()) {
			directory.mkdirs();
		}
		File file = new File(path + fileName);
		return file.exists();
	}
	
	public static String getSaveFilePath(String filePath) {
		return getRootFilePath() + filePath;
	}
	
	public static String getRootFilePath() {
		if (hasSDCard()) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			return sdcardDir.getParent() + "/" + sdcardDir.getName();// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data"; // filePath: /data/data/
		}
	}
	
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		} 
		return true;
	}

	public static Bitmap decodeFile(File f) {
		try {
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, option);

			final int REQUIRED_SIZE = 100;
			int width = option.outWidth;
			int height = option.outHeight;
			int scale = 1;
			while (true) {
				if (width / 2 < REQUIRED_SIZE
						|| height / 2 < REQUIRED_SIZE)
					break;
				width /= 2;
				height /= 2;
				scale *= 2;
			}

			BitmapFactory.Options option2 = new BitmapFactory.Options();
			option2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, option2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void downThread(final String remoteUrl) {
		new Thread(new Runnable() {
			public void run() {
			}
		}).start();
	}
	
	public static void downLoadImage(final File file, final String url, 
			final ImageView imageView) {
		new Thread(new Runnable() {
			public void run() {
				try {
					int buffer = 1024;
					URL imageUrl = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) imageUrl
							.openConnection();
					conn.setConnectTimeout(30000);
					conn.setReadTimeout(30000);
					conn.setInstanceFollowRedirects(true);
					InputStream inStream = conn.getInputStream();
					OutputStream outStream = new FileOutputStream(file);
					byte[] bytes = new byte[buffer];
					for (;;) {
						int count = inStream.read(bytes, 0, buffer);
						if (count == -1)
							break;
						outStream.write(bytes, 0, count);
					}
					outStream.close();
					final Bitmap bitmap = decodeFile(file);
					Activity a = (Activity) imageView.getContext();
					a.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if (bitmap != null) {
								imageView.setImageBitmap(bitmap);
							}
						}
					});
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
		
	}
}
