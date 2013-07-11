package com.ruyicai.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;
import android.util.Log;

/**
 * 下载开机图片
 * 
 * @author Administrator
 * 
 */
public class DownLoadImg {
	public static String LOCAL_DIR = "/ruyicai/";
	public static final String IMG_NAME = "cp1.png";
	private static final int BUFF_SIZE = 1024 * 64;
	private static final int TIME = 5000;// 联网超时间5秒
	private static final int STOPTIME = 500;// 下载休息时间
	private static String fileDirectory = "";

	public DownLoadImg(String packageName) {
		LOCAL_DIR += packageName + "/";
		getSdCardPath();
	}

	/**
	 * 启动下载开机图片线程
	 */
	public void downThread(final String remoteUrl) {
		new Thread(new Runnable() {
			public void run() {
				try {
					download(remoteUrl);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 创建一个SD卡上的文件夹
	public void getSdCardPath() {
		File sdcardDir = Environment.getExternalStorageDirectory();
		String path = sdcardDir.getParent() + "/" + sdcardDir.getName();
		fileDirectory = path + LOCAL_DIR;
		createFile();
	}

	public void createFile() {
		try {
			// 1.判断是否存在sdcard
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				File path = new File(fileDirectory);// 目录
				if (!path.exists()) {
					// 2.创建目录，可以在应用启动的时候创建
					path.mkdirs();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载方法
	 * 
	 * @param remoteUrl
	 * @param localPath
	 * @throws IOException
	 */
	private static void download(String remoteUrl) throws IOException {

		InputStream is = null;
		OutputStream os = null;

		URL downloadURL = null;

		try {
			File sdcardDir = Environment.getExternalStorageDirectory();
			String path = sdcardDir.getParent() + "/" + sdcardDir.getName();
			File tmpFile = new File(path + LOCAL_DIR + IMG_NAME + ".tmp");
			FileOutputStream fos = new FileOutputStream(tmpFile);

			HttpURLConnection httpURLConn = null;
			downloadURL = new URL(remoteUrl);
			httpURLConn = (HttpURLConnection) downloadURL.openConnection();
			httpURLConn.setConnectTimeout(TIME);
			httpURLConn.setRequestMethod("GET");
			httpURLConn.setChunkedStreamingMode(BUFF_SIZE);
			int fileLength = httpURLConn.getContentLength();
			if (fileLength == -1) {
				return;
			}
			is = httpURLConn.getInputStream();
			byte[] buf = new byte[BUFF_SIZE];
			int total = 0;
			int len = 0;
			while (true) {
				try {
					Thread.sleep(STOPTIME);// 下载休息时间
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				len = is.read(buf, 0, BUFF_SIZE);
				if (len == -1) {
					break;
				}
				total += len;
				fos.write(buf, 0, len);
			}
			fos.flush();
			is.close();
			fos.close();
			httpURLConn.disconnect();
			// 下载成功后，修改文件名
			tmpFile.renameTo(new File(path + LOCAL_DIR + IMG_NAME));
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex3) {
					ex3.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException ex1) {
				}
			}
		}
	}
}
