package com.palmdream.netintface;

//#if PolishDevice != Motorola/A1200
public class iHttpAuto_CMWAP implements Runnable {
	// #else
	// # public class iHttpAuto_CMWAP {
	// #endif

	private String url = null;

	public void setUrl(String URL) {
		this.url = URL;
	}

	public void run() {
		try {
			iHttp http = new iHttp();
			String ret = http.getViaHttpConnection_CMWAP(this.url, true);
			// if(iHttp.conMethord==iHttp.CMUNKNOWN && ret!=null &&
			// ret.length()>0){
			Thread.sleep(100);
			ret = http.getViaHttpConnection_CMWAP(this.url, false);
			http.setRetValue(ret);
			if (iHttp.retValue != null && iHttp.retValue.length() > 0)
				http.setConnectionMethord(iHttp.CMWAP);
			// }
		} catch (Exception e) {
			// TODO: handle exception
		}
		url = null;
	}

}
