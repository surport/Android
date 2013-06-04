package com.palmdream.netintface;

//#if PolishDevice != Motorola/A1200
public class iHttpAuto_CMNET implements Runnable {
	// #else
	// # public class iHttpAuto_CMNET {
	// #endif

	private String url = null;

	public void setUrl(String URL) {
		this.url = URL;
	}

	public void run() {
		try {
			iHttp http = new iHttp();
			http.setRetValue(http.getViaHttpConnection_CMNET(this.url));
			// Log.d("tag","yes");
			if (iHttp.retValue != null && iHttp.retValue.length() > 0)
				http.setConnectionMethord(iHttp.CMNET);
		} catch (Exception e) {
			// TODO: handle exception
		}
		url = null;
	}
}
