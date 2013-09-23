package com.ruyicai.android.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * 提供了网络连接的相关工具方法
 * 
 * @author PengCX
 * @since RYC1.0 2013-2-25
 */
public class HttpTools {
	public static final String	TAG						= "HttpTools";
	/** Http连接超时时间　 */
	private static final int	HTTP_CONNECT_TIMEOUT	= 3000;
	/** Socket连接超时时间 */
	private static final int	HTTP_SOCKET_TIMEOUT		= 3000;
	/** Socket缓存大小 */
	private static final int	SOCKET_BUFFER_SIZE		= 8192;

	/** 网络连接正常状态码 */
	private static final int	CONNECT_NORMAL_STATE	= 200;

	/** 数据编码方式 */
	private static final String	DATA_ENCODE_CHARSET		= "UTF-8";

	/** GET方式标识 */
	public static final int		GET_METHOD_ID			= 0;
	/** POST方式标识 */
	public static final int		POST_METHOD_ID			= 1;

	/**
	 * 连接互联网指定的URL，获取连接结果
	 * 
	 * @param context
	 *            连接网络的页面上下文对象
	 * @return 连接结果字符串
	 */
	public static String connectingIntenetForResult(String aUrl, int aMethodID,
			String aData) {
		String resultString = null;

		switch (aMethodID) {
		case GET_METHOD_ID:
			// Get方式获取数据
			resultString = getMethodGetData(aUrl);
			break;
		case POST_METHOD_ID:
			// Post方式获取数据
			resultString = postMethodGetData(aUrl, aData);
			break;
		}

		LogTools.showLog(TAG, "网络返回数据：" + resultString, LogTools.INFO);
		return resultString;
	}

	/**
	 * 已POST方式从网络获取数据
	 * 
	 * @param aUrl
	 *            url字符串
	 * @return 获取数据结果字符串
	 */
	private static String postMethodGetData(String aUrl, String aData) {
		// XXX 是否能简化和优化联网流程和代码
		// TODO 网络代理处理

		// 创建 HttpParams以用来设置 HTTP 参数（这一部分不是必需的）
		HttpParams httpParams = new BasicHttpParams();
		// 设置连接超时和Socket超时，以及 Socket 缓存大小
		HttpConnectionParams.setConnectionTimeout(httpParams,
				HTTP_CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, HTTP_SOCKET_TIMEOUT);
		HttpConnectionParams
				.setSocketBufferSize(httpParams, SOCKET_BUFFER_SIZE);

		HttpResponse response = null;
		try {
			// 创建 HttpPost方法
			HttpPost httpPost = new HttpPost();
			// 设置HttpPost的URL对象
			httpPost.setURI(new URI(aUrl));
			// 设置HttpPost的HttpEntity对象
			InputStream inputStream = new StringBufferInputStream(
					EncryptTools.desEncrypt(aData));
			HttpEntity httpEntity = new InputStreamEntity(inputStream,
					inputStream.available());
			httpPost.setEntity(httpEntity);

			// 创建一个HttpClient实例
			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 获取网络状态号码：如果返回正常状态码，则从response对象中读取数据
		String resultString = null;
		int stateCode = response.getStatusLine().getStatusCode();

		if (stateCode == CONNECT_NORMAL_STATE) {
			resultString = readDataFromInputResponse(response);
		} else {
			return "网络异常连接异常...";
		}

		return resultString;
	}

	/**
	 * 从Response对象中读取结果字符串
	 * 
	 * @param aResponse
	 *            response对象
	 * @return 结果字符串
	 */
	private static String readDataFromInputResponse(HttpResponse aResponse) {
		String resultString = null;

		try {
			InputStream inputStream = aResponse.getEntity().getContent();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream, DATA_ENCODE_CHARSET));
			StringBuffer stringBuffer = new StringBuffer();
			String tempString = "";
			while ((tempString = bufferedReader.readLine()) != null) {
				stringBuffer.append(tempString);

			}

			String decryptString = EncryptTools.desDecrypt(stringBuffer
					.toString());
			byte[] compressedData = EncryptTools.base64Decode(decryptString);
			byte[] decompressData = DataTools.decopressData(compressedData);

			resultString = new String(decompressData, "utf-8");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultString;
	}

	/**
	 * 以GET方式从网络获取数据
	 * 
	 * @param aUrl
	 *            url字符串
	 * @return 获取数据结果字符串
	 */
	private static String getMethodGetData(String aUrl) {
		return " ";
	}
}
