package com.palmdream.netintface;


//import info.iamkebo.demo.Demo.MyHostnameVerifier;
//import info.iamkebo.demo.Demo.MyTrustManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

import com.palmdream.RuyicaiAndroid.PublicMethod;

public class CopyOfiHttp {
	private int iTimeOut=5000;
	private StringBuffer buffer = new StringBuffer();
	public static int CMUNKNOWN = 0;
	public static int CMWAP = 1;
	public static int CMNET = 2;
	public static int conMethord = 0;
	String proxy = "10.0.0.172";

	// 根据适配机型是否支持https进行下面的设置
	boolean CMNET_USE_HTTPS =false;//true;////////////
	boolean CMWAP_USE_HTTPS =false;//true;////////////

	/**
	 * Process the type.
	 * 
	 * @param type
	 *            that type
	 */
	private void processType(String type) {
	}

	/**
	 * Process the data one character at a time.
	 * 
	 * @param b
	 *            one byte of data
	 */
	private void process(byte b) {
		buffer.append((char) b);
	}

	/**
	 * Process the data from the array.
	 * 
	 * @param b
	 *            an array of bytes.
	 */
	private void process(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			process(b[i]);
		}
	}

	/**
	 * Add request properties for the configuration, profiles, and locale of
	 * this system.
	 * 
	 * @param con
	 *            current HttpConnection to apply request headers
	 */
	private void setRequestHeaders(HttpURLConnection con) {
		try {
			/*
			String conf = System.getProperty("microedition.configuration");
			String prof = System.getProperty("microedition.profiles");
			PublicMethod.myOutput("----- "+prof);
			int space = prof.indexOf(" ");

			if (space != -1) {
				prof = prof.substring(0, space - 1);
			}

			String locale = System.getProperty("microedition.locale");

			String ua = "Profile/" + prof + " Configuration/" + conf;
			*/
			String ua="Profile/MIDP2.0 Configuration/CDLC1.1";//需要确认ua的定义方法
			
			 //con.setRequestProperty("User-Agent", ua);
              // con.setRequestProperty("User_Agent",  "Android Application:"+Z.APP_VERSION);
               con.setRequestProperty("User-Agent", ua); 
			/*if (locale != null) {
				 con.setRequestProperty("Content-Language", locale);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Post a request with some headers and content to the server and process
	 * the headers and content.
	 * <p>
	 * Connector.open is used to open url and a HttpConnection is returned. The
	 * request method is set to POST and request headers set. A simple command
	 * is written and flushed. The HTTP headers are read and processed. If the
	 * length is available, it is used to read the data in bulk. From the
	 * StreamConnection the InputStream is opened. It is used to read every
	 * character until end of file (-1). If an exception is thrown the
	 * connection and stream is closed.
	 * 
	 * @param url
	 *            the URL to process.
	 */
	// public String postViaHttpConnection(String url,String parameter){
	// int status = 0;
	// HttpConnection con = null;
	// InputStream is = null;
	// OutputStream os = null;
	//
	// try {
	// con = (HttpConnection)Connector.open(url);
	//
	// // Set the request method and headers
	// con.setRequestMethod(HttpConnection.POST);
	// con.setRequestProperty("If-Modified-Since", "29 Oct 1999 19:43:31 GMT");
	// con.setRequestProperty("User-Agent",
	// "Profile/MIDP-1.0 Configuration/CLDC-1.0");
	// con.setRequestProperty("Content-Language", "en-US");
	//
	// // Getting the output stream may flush the headers
	// os = con.openOutputStream();
	// //os.write("LIST games\n".getBytes());
	// if(parameter!=null)
	// os.write(parameter.getBytes());
	// os.flush(); // Optional, openInputStream will flush
	//
	// // Get the status code, causing the connection to be made
	// status = con.getResponseCode();
	//
	// // Any 500 status number (500, 501) means there was a server error
	// if ((status == HttpConnection.HTTP_NOT_IMPLEMENTED) ||
	// (status == HttpConnection.HTTP_VERSION) ||
	// (status == HttpConnection.HTTP_INTERNAL_ERROR) ||
	// (status == HttpConnection.HTTP_GATEWAY_TIMEOUT) ||
	// (status == HttpConnection.HTTP_BAD_GATEWAY)) {
	// System.err.print("WARNING: Server error status [" + status + "] ");
	// System.err.println("returned for url [" + url + "]");
	//
	// if (is != null) {
	// is.close();
	// }
	//
	// if (os != null) {
	// os.close();
	// }
	//
	// if (con != null) {
	// con.close();
	// }
	//
	// return "";
	// }
	//
	// // Only HTTP_OK (200) means the content is returned.
	// if (status != HttpConnection.HTTP_OK) {
	// throw new IOException("Response status not OK [" + status + "]");
	// }
	//
	// // Open the InputStream and get the ContentType
	// is = con.openInputStream();
	//
	// String type = con.getType();
	// processType(type);
	//
	// // Get the length and process the data
	// int len = (int)con.getLength();
	//
	// if (len > 0) {
	// byte[] data = new byte[len];
	// int actual = is.read(data);
	// process(data);
	// } else {
	// int ch;
	//
	// while ((ch = is.read()) != -1) {
	// process((byte)ch);
	// }
	// }
	// }catch (Exception e){
	// e.printStackTrace();
	// } finally {
	// if (is != null) {
	// try {
	// is.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// if (os != null) {
	// try {
	// os.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// if (con != null) {
	// try {
	// con.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	//       
	// return buffer.toString();
	// }

	public synchronized void setConnectionMethord(int newConMethord) {
		if (CopyOfiHttp.conMethord == CopyOfiHttp.CMUNKNOWN)
			CopyOfiHttp.conMethord = newConMethord;
	}

	public synchronized void setRetValue(String newRetValue) {
		if (newRetValue != null && newRetValue.length() > 0)
			CopyOfiHttp.retValue = newRetValue;
	}

	public static String retValue = "";

	public String getViaHttpConnection(String url) {

		String reValue = "";
		try {
			if (conMethord == CMWAP){
				PublicMethod.myOutput("----------getViaHttpConnection cmwap");//("tag","----------getViaHttpConnection cmwap");
				reValue = getViaHttpConnection_CMWAP(url,false);
			}
				
			
			else if (conMethord == CMNET){
				PublicMethod.myOutput("----------getViaHttpConnection cmnet");//Log.d("tag","----------getViaHttpConnection cmnet");
				reValue = getViaHttpConnection_CMNET(url);
			}
			else {
				PublicMethod.myOutput("----------getViaHttpConnection else");
				
				iHttpAuto_CMNET autoCheckNET = new iHttpAuto_CMNET();
				autoCheckNET.setUrl(url);
				autoCheckNET.run();
//				PublicMethod.myOutput("conMethord=" + conMethord);

				// --------------------------------------------------------
				// iHttpAuto_CMWAP autoCheckWAP = new iHttpAuto_CMWAP();
				// autoCheckWAP.setUrl(url);
				// //Thread tWap = new Thread(autoCheckWAP);
				// //tWap.start();
				// autoCheckWAP.run();
				//			   
				// ---------------------------------------------------------

				if (CopyOfiHttp.conMethord == CopyOfiHttp.CMUNKNOWN) {
					iHttpAuto_CMWAP autoCheckWAP = new iHttpAuto_CMWAP();
					PublicMethod.myOutput("---------iHttp.conMethord == iHttp.CMUNKNOWN");
					autoCheckWAP.setUrl(url);
					autoCheckWAP.run();
				}
				// while(conMethord==CMUNKNOWN){
				// Thread.sleep(300);
				// }
				reValue = retValue;
			}
		} catch (Exception e) {
			// TODO: handle exception
			//iHttp.conMethord=0;
		}
		return reValue;
	}

	/**
	 * Read the HTTP headers and the data using HttpConnection. Check the
	 * response code to insure successful retrieval.
	 * <p>
	 * Connector.open is used to open url and a HttpConnection is returned. The
	 * HTTP headers are read and processed. If the length is available, it is
	 * used to read the data in bulk. From the HttpConnection the InputStream is
	 * opened. It is used to read every character until end of file (-1). If an
	 * exception is thrown the connection and stream is closed.
	 * 
	 * @param url
	 *            the URL to process.
	 */
	public String getViaHttpConnection_CMNET(String url) {
		HttpURLConnection con = null;
		InputStream is = null;
		OutputStream os = null;
		String reValue = "";
		buffer = new StringBuffer();
		try {
			if (this.CMNET_USE_HTTPS) {
				reValue = getViaHttpsConnection_CMNET(url);
			} else {
				if (url.startsWith("https://")) {
					url = "http://" + url.substring(8);
				}

				try {
					int status = -1;

					// Open the connection and check for re-directs
					while (true) {
						//con = (HttpURLConnection) Connector.open(url,
							//	Connector.READ, true);
						URL myurl=new URL(url);
						con=(HttpURLConnection)myurl.openConnection();
						Log.d("tag", "-------getViaHttpConnection_CMNET(String url)");
						//con=(HttpURLConnection)myurl.
						con.setDoInput(true);
						con.setDoOutput(true);
						con.setConnectTimeout(iTimeOut);
						con.setReadTimeout(iTimeOut);
						//PublicMethod.myOutput("------------ihttp timeout");
						// Debug = "cmnet";
						// con = (HttpsConnection)Connector.open(url);
						
						
						//#if isSetReqHeader == true
						setRequestHeaders(con);
						//#endif
					

						// SecurityInfo si=con.getSecurityInfo();
						// Certificate c=si.getServerCertificate();
						// String subject =c.getSubject();
						// Get the status code, causing the connection to be
						// made
						status = con.getResponseCode();
						//Log.d("tag", "-------ok");
					
						if (/*(status == HttpURLConnection.HTTP_TEMP_REDIRECT)
								||*/ (status == HttpURLConnection.HTTP_MOVED_TEMP)
								|| (status == HttpURLConnection.HTTP_MOVED_PERM)) {
							// Get the new location and close the connection
							url = con.getHeaderField("location");
							//con.close();
							con.disconnect();
                            
							PublicMethod.myOutput("Redirecting to " + url);
						} else {
							break;
						}
					}

					// Any 500 status number (500, 501) means there was a server
					// error
					if ((status == HttpURLConnection.HTTP_NOT_IMPLEMENTED)
							|| (status == HttpURLConnection.HTTP_VERSION)
							|| (status == HttpURLConnection.HTTP_INTERNAL_ERROR)
							|| (status == HttpURLConnection.HTTP_GATEWAY_TIMEOUT)
							|| (status == HttpURLConnection.HTTP_BAD_GATEWAY)) {
						System.out.print("WARNING: Server error status ["
								+ status + "] ");
						PublicMethod.myOutput("returned for url [" + url + "]");

						if (is != null) {
							is.close();
						}

						if (os != null) {
							os.close();
						}

						if (con != null) {
							//con.close();
							con.disconnect();
						}

					} else {

						// Only HTTP_OK (200) means the content is returned.
						if (status != HttpURLConnection.HTTP_OK) {
							throw new IOException("Response status not OK ["
									+ status + "]");
						}

						// Get the ContentType
						try {
							String type = con.getContentType();
							processType(type);
						} catch (Exception e) {
						}

						// open the InputStream
						is = con.getInputStream();
						String str=GetStringFromInputStream(is);
//						PublicMethod.myOutput("------------GetStringFromInputStream(is):"+str);
						is=GetInputStreamFromString(str);
						buffer.setLength(0);

						// Get the length and process the data
						int len = (int) con.getContentLength();

						if (len > 0) {
							byte[] data = new byte[len];
							int actual = is.read(data);
							process(data);
						} else {
							int ch;

							while ((ch = is.read()) != -1) {
								process((byte) ch);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				//	iHttp.conMethord=1;
					PublicMethod.myOutput("----------iHttp.conMethord"+CopyOfiHttp.conMethord);
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (con != null) {
						try {
							//con.close();
							con.disconnect();
						} catch (Exception e) {
							//e.printStackTrace();
						}
					}
				}
				if (buffer != null)
					reValue = buffer.toString();
				
			}
		} catch (Exception e) {
			//iHttp.conMethord=1;
		}
		PublicMethod.myOutput("-------getViaHttpConnection_CMNET-----"+reValue);
		return reValue;
		
	}

	public String getViaHttpsConnection_CMNET(String url) {
		HttpsURLConnection con = null;
		InputStream is = null;
		OutputStream os = null;
		String reValue = "";
		buffer = new StringBuffer();

		try {
			if (!this.CMNET_USE_HTTPS) {
				reValue = getViaHttpConnection_CMNET(url);
			} else {
				if (url.startsWith("http://")) {
					url = "https://" + url.substring(7);
				}

				try {
					int status = -1;
					// Open the connection and check for re-directs
					while (true) {
						//con = (HttpsURLConnection) Connector.open(url,
							//	Connector.READ, true);
						SSLContext sc = SSLContext.getInstance("TLS");//使用TLS协议
			    		sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
			    		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			    		HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
						URL myurl=new URL(url);
						con=(HttpsURLConnection)myurl.openConnection();
						Log.d("tag", "-------getViaHttpsConnection_CMNET");
						con.setDoInput(true);
						con.setDoOutput(true);
						con.setConnectTimeout(iTimeOut);
						con.setReadTimeout(iTimeOut);
						// Debug = "cmnet";
						// con = (HttpsConnection)Connector.open(url);
						
						//#if isSetReqHeader == true
						setRequestHeaders(con);
						//#endif
						

						// SecurityInfo si=con.getSecurityInfo();
						// Certificate c=si.getServerCertificate();
						// String subject =c.getSubject();
						// Get the status code, causing the connection to be
						// made
						status = con.getResponseCode();
						if (/*(status == HttpsURLConnection.HTTP_TEMP_REDIRECT)
								||*/ (status == HttpsURLConnection.HTTP_MOVED_TEMP)
								|| (status == HttpsURLConnection.HTTP_MOVED_PERM)) {
							// Get the new location and close the connection
							url = con.getHeaderField("location");
							//con.close();
							con.disconnect();

							PublicMethod.myOutput("Redirecting to " + url);
						} else {
							break;
						}
					}

					// Any 500 status number (500, 501) means there was a server
					// error
					if ((status == HttpsURLConnection.HTTP_NOT_IMPLEMENTED)
							|| (status == HttpsURLConnection.HTTP_VERSION)
							|| (status == HttpsURLConnection.HTTP_INTERNAL_ERROR)
							|| (status == HttpsURLConnection.HTTP_GATEWAY_TIMEOUT)
							|| (status == HttpsURLConnection.HTTP_BAD_GATEWAY)) {
						System.out.print("WARNING: Server error status ["
								+ status + "] ");
						PublicMethod.myOutput("returned for url [" + url + "]");

						if (is != null) {
							is.close();
						}

						if (os != null) {
							os.close();
						}

						if (con != null) {
							//con.close();
							con.disconnect();
						}

					} else {

						// Only HTTP_OK (200) means the content is returned.
						if (status != HttpsURLConnection.HTTP_OK) {
							throw new IOException("Response status not OK ["
									+ status + "]");
						}

						// Get the ContentType
						try {
							String type = con.getContentType();
							processType(type);
						} catch (Exception e) {
						}

						// open the InputStream
						is = con.getInputStream();
						String str=GetStringFromInputStream(is);
//						PublicMethod.myOutput("------------GetStringFromInputStream(is):"+str);
						is=GetInputStreamFromString(str);
						buffer.setLength(0);

						// Get the length and process the data
						int len = (int) con.getContentLength();

						if (len > 0) {
							byte[] data = new byte[len];
							int actual = is.read(data);
							process(data);
						} else {
							int ch;

							while ((ch = is.read()) != -1) {
								process((byte) ch);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				//	iHttp.conMethord=1;
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
							PublicMethod.myOutput("----------e8");
						}
					}

					if (con != null) {
						try {
							//con.close();
							con.disconnect();
						} catch (Exception e) {
							e.printStackTrace();
							PublicMethod.myOutput("----------e7");
						}
					}
				}
				if (buffer != null)
					reValue = buffer.toString();
			}
		} catch (Exception e) {
			// TODO: handle exception
			//iHttp.conMethord=1;
			PublicMethod.myOutput("----------e6");
		}
		PublicMethod.myOutput("-------getViaHttpsConnection_CMNET"+reValue);
		return reValue;
	}

	    
	public String getViaHttpConnection_CMWAP(String url,boolean check) {
		HttpURLConnection con = null;
		InputStream is = null;
		OutputStream os = null;
		String reValue = "";
		buffer = new StringBuffer();

		try {
			if (this.CMWAP_USE_HTTPS) {
				reValue = getViaHttpsConnection_CMWAP(url,check);
			} else {
				if (url.startsWith("https://")) {
					url = "http://" + url.substring(8);
				}

				try {
					int status = -1;
					// Open the connection and check for re-directs
					while (true) {

						if (proxy != null && url.length() > 7
								&& url.substring(0, 7).equals("http://")) {
							int ind = url.indexOf('/', 7);
							String proxyURL = url.substring(0, 7) + proxy + "/";
							if (ind >= 0) {
								proxyURL += url.substring(ind + 1);
							}
							String requestProperty = (ind >= 0) 
								? url.substring(7, ind) : url.substring(7);

							// PublicMethod.myOutput("a:"+proxyURL+"|");
							// PublicMethod.myOutput("b:"+requestProperty+"|");
							//con = (HttpURLConnection) Connector.open(proxyURL,
							//	Connector.READ, true);
								URL myurl=new URL(proxyURL);
								con=(HttpURLConnection)myurl.openConnection();
								Log.d("tag", "-------getViaHttpConnection_CMWAP");
								con.setDoInput(true);
								con.setDoOutput(true);
								con.setConnectTimeout(iTimeOut);
								con.setReadTimeout(iTimeOut);
								
								
								
							// Debug = "cmWap";
								
							con.addRequestProperty("X-Online-Host",
									requestProperty);
							con.addRequestProperty("Accept", "*/*");

						} else if (proxy != null && url.length() > 8
								&& url.substring(0, 8).equals("https://")) {

							int ind = url.indexOf('/', 8);
							String proxyURL = url.substring(0, 8) + proxy + "/";
							if (ind >= 0) {
								proxyURL += url.substring(ind + 1);
							}
							//con = (HttpURLConnection) Connector.open(proxyURL,
									//Connector.READ, true);
							URL myurl=new URL(proxyURL);
							con=(HttpURLConnection)myurl.openConnection();
							con.setDoInput(true);
							con.setDoOutput(true);
							con.setConnectTimeout(iTimeOut);
							con.setReadTimeout(iTimeOut);
							con.addRequestProperty("X-Online-Host",
									(ind >= 0) ? url.substring(8, ind) : url
											.substring(8));
							con.addRequestProperty("Accept", "*/*");

						} else {
							//con = (HttpURLConnection) Connector.open(url,
									//Connector.READ, true);
							URL myurl=new URL(url);
							con=(HttpURLConnection)myurl.openConnection();
							con.setDoInput(true);
							con.setDoOutput(true);
							con.setConnectTimeout(iTimeOut);
							con.setReadTimeout(6000);
						}

						//#if isSetReqHeader == true
						setRequestHeaders(con);
						//#endif

						// Get the status code, causing the connection to be
						// made
						status = con.getResponseCode();
						
						if(check){
							con = null;
							return "";
						}
						
						if (/*(status == HttpURLConnection.HTTP_TEMP_REDIRECT)
								||*/(status == HttpURLConnection.HTTP_MOVED_TEMP)
								|| (status == HttpURLConnection.HTTP_MOVED_PERM)) {
							// Get the new location and close the connection
							url = con.getHeaderField("location");
							//con.close();
							con.disconnect();

							PublicMethod.myOutput("Redirecting to " + url);
						} else {
							break;
						}
					}

					// Any 500 status number (500, 501) means there was a server
					// error
					if ((status == HttpURLConnection.HTTP_NOT_IMPLEMENTED)
							|| (status == HttpURLConnection.HTTP_VERSION)
							|| (status == HttpURLConnection.HTTP_INTERNAL_ERROR)
							|| (status == HttpURLConnection.HTTP_GATEWAY_TIMEOUT)
							|| (status == HttpURLConnection.HTTP_BAD_GATEWAY)) {
						System.out.print("WARNING: Server error status ["
								+ status + "] ");
						PublicMethod.myOutput("returned for url [" + url + "]");

						if (is != null) {
							is.close();
						}

						if (os != null) {
							os.close();
						}

						if (con != null) {
							//con.close();
							con.disconnect();
						}
					} else {

						// Only HTTP_OK (200) means the content is returned.
						if (status != HttpURLConnection.HTTP_OK) {
							throw new IOException("Response status not OK ["
									+ status + "]");
						}

						// Get the ContentType
						try {
							String type = con.getContentType();
							processType(type);
						} catch (Exception e) {
						}
 
						// open the InputStream
						is = con.getInputStream();
						String str=GetStringFromInputStream(is);
						PublicMethod.myOutput("------------GetStringFromInputStream(is):"+str);
						is=GetInputStreamFromString(str);
						buffer.setLength(0);

						// Get the length and process the data
						int len = (int) con.getContentLength();

						if (len > 0) {
							byte[] data = new byte[len];
							int actual = is.read(data);
							process(data);
						} else {
							int ch;

							while ((ch = is.read()) != -1) {
								process((byte) ch);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (con != null) {
						try {
							//con.close();
							con.disconnect();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if (buffer != null)
					reValue = buffer.toString();
			}
		} catch (Exception e) {
			//iHttp.conMethord=2;
		}
		PublicMethod.myOutput("-----------------getViaHttpConnection_CMWAP"+reValue);
		return reValue;
	}

	public String getViaHttpsConnection_CMWAP(String url,boolean check) {
		HttpsURLConnection con = null;
		InputStream is = null;
		OutputStream os = null;
		String reValue = "";
		buffer = new StringBuffer();

		try {
			if (!this.CMWAP_USE_HTTPS) {
				reValue = getViaHttpConnection_CMWAP(url,check);
			} else {
				if (url.startsWith("http://")) {
					url = "https://" + url.substring(7);
				}

				try {
					int status = -1;
					// Open the connection and check for re-directs
					while (true) {

						if (proxy != null && url.length() > 7
								&& url.substring(0, 7).equals("http://")) {
							int ind = url.indexOf('/', 7);
							String proxyURL = url.substring(0, 7) + proxy + "/";
							if (ind >= 0) {
								proxyURL += url.substring(ind + 1);
							}
							String requestProperty = (ind >= 0) ? url
									.substring(7, ind) : url.substring(7);

							// PublicMethod.myOutput("a:"+proxyURL+"|");
							// PublicMethod.myOutput("b:"+requestProperty+"|");
						//	con = (HttpsURLConnection) Connector.open(proxyURL,
								//	Connector.READ, true);
									SSLContext sc = SSLContext.getInstance("TLS");//使用TLS协议
						    		sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
						    		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
						    		HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
									URL myurl=new URL(proxyURL);
									con=(HttpsURLConnection)myurl.openConnection();
									//Log.d("tag","--------------httpscmwap------------");
									con.setDoInput(true);
									con.setDoOutput(true);
									con.setConnectTimeout(10000);	
									con.setReadTimeout(iTimeOut);
									
							// Debug = "cmWap";
							con.setRequestProperty("X-Online-Host",
									requestProperty);
							con.setRequestProperty("Accept", "*/*");

						} else if (proxy != null && url.length() > 8
								&& url.substring(0, 8).equals("https://")) {

							int ind = url.indexOf('/', 8);
							String proxyURL = url.substring(0, 8) + proxy + "/";
							if (ind >= 0) {
								proxyURL += url.substring(ind + 1);
							}
							//con = (HttpsConnection) Connector.open(proxyURL,
									//Connector.READ, true);
							SSLContext sc = SSLContext.getInstance("TLS");//使用TLS协议
				    		sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
				    		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				    		HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
							URL myurl=new URL(proxyURL);
							con=(HttpsURLConnection)myurl.openConnection();
							Log.d("tag", "-------getViaHttpsConnection_CMWAP(String url,boolean check)");
							con.setDoInput(true);
							con.setConnectTimeout(iTimeOut);	
							String requestProperty=(ind >= 0) ? url.substring(8, ind) : url
									.substring(8);
							con.setRequestProperty("X-Online-Host",
									(ind >= 0) ? url.substring(8, ind) : url
											.substring(8));
							//con.setRequestProperty("X-Online-Host",requestProperty);
							con.setRequestProperty("Accept", "*/*");

						} else {
							//con = (HttpsConnection) Connector.open(url,
									//Connector.READ, true);
							URL myurl=new URL(url);
							con=(HttpsURLConnection)myurl.openConnection();
							con.setDoInput(true);
							con.setDoOutput(true);
							con.setConnectTimeout(iTimeOut);
							con.setReadTimeout(iTimeOut);
						}

						//#if isSetReqHeader == true
						setRequestHeaders(con);
						//#endif
				

						// Get the status code, causing the connection to be
						// made
						status = con.getResponseCode();
						
						if(check){
							con = null;
							return "";
						}
						
						if (//(status == HttpsURLConnection.HTTP_TEMP_REDIRECT)|| 
								(status == HttpsURLConnection.HTTP_MOVED_TEMP)
								|| (status == HttpsURLConnection.HTTP_MOVED_PERM)) {
							// Get the new location and close the connection
							url = con.getHeaderField("location");
							//con.close();
							con.disconnect();

							PublicMethod.myOutput("Redirecting to " + url);
						} else {
							break;
						}
					}

					// Any 500 status number (500, 501) means there was a server
					// error
					if ((status == HttpsURLConnection.HTTP_NOT_IMPLEMENTED)
							|| (status == HttpsURLConnection.HTTP_VERSION)
							|| (status == HttpsURLConnection.HTTP_INTERNAL_ERROR)
							|| (status == HttpsURLConnection.HTTP_GATEWAY_TIMEOUT)
							|| (status == HttpsURLConnection.HTTP_BAD_GATEWAY)) {
						System.out.print("WARNING: Server error status ["
								+ status + "] ");
						PublicMethod.myOutput("returned for url [" + url + "]");

						if (is != null) {
							is.close();
						}

						if (os != null) {
							os.close();
						}

						if (con != null) {
							//con.close();
							con.disconnect();
						}
					} else {

						// Only HTTP_OK (200) means the content is returned.
						if (status != HttpsURLConnection.HTTP_OK) {
							throw new IOException("Response status not OK ["
									+ status + "]");
						}

						// Get the ContentType
						try {
							String type = con.getContentType();
							processType(type);
						} catch (Exception e) {
							//PublicMethod.myOutput("----------e2");
						}

						// open the InputStream
						is = con.getInputStream();
						buffer.setLength(0);

						// Get the length and process the data
						int len = (int) con.getContentLength();

						if (len > 0) {
							byte[] data = new byte[len];
							int actual = is.read(data);
							process(data);
						} else {
							int ch;

							while ((ch = is.read()) != -1) {
								process((byte) ch);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					//iHttp.conMethord=2;
					PublicMethod.myOutput("----------e1");
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							//PublicMethod.myOutput("----------e3");
						}
					}

					if (con != null) {
						try {
							//con.close();
							con.disconnect();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							//PublicMethod.myOutput("----------e4");
						}
					}
				}
				if (buffer != null)
					reValue = buffer.toString();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return reValue;
	}
	 private class MyHostnameVerifier implements HostnameVerifier{

			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				
				return true;
			}
	    }
	    
	    private class MyTrustManager implements X509TrustManager{

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
	    	
	    }
	    public String GetStringFromInputStream(InputStream aIS)
	    {
	    	String iS=null;
	    	try{
	    		BufferedInputStream bis=new BufferedInputStream(aIS);
	    		ByteArrayBuffer baf=new ByteArrayBuffer(50);
	    		int current=0;
	    		while((current=bis.read())!=-1){
	    			baf.append((byte)current);
	    		}
	    		iS=new String(baf.toByteArray());	
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		return null;
	    	}
	    	return iS;
	    }
	    public static InputStream GetInputStreamFromString(String aS)
	    {
	    	InputStream iIS=new ByteArrayInputStream(aS.getBytes());
	    	return iIS;
	    }


}
