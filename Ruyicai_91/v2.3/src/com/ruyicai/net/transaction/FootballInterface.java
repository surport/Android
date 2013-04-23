package com.ruyicai.net.transaction;

import org.json.JSONObject;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.URLEncoder;

public class FootballInterface {
	
	private static FootballInterface instance;
	
	private FootballInterface() {
		
	}
	
	public static synchronized FootballInterface getInstance(){
		if(instance == null){
			instance = new FootballInterface();
		}
		return instance;
	}
	
	/**
	 * 获得足彩对阵
	 * @param case_caseId
	 * @param sessionid
	 * @return
	 */
	public static String getZCData(String lotno, String batchCode) {
		String action = "zcAction.do";
		String method = "getFlData";
		String reValue = "";
		try {
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("lotno", lotno);
			paras.put("batchCode", batchCode);
			para = URLEncoder.encode(paras.toString());
			String re = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action
					+ ";jsessionid=" + Constants.sessionId + "?method=" + method
					+ "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}
	
	
	/**
	 * 获得足彩详细信息
	 * @param case_caseId
	 * @param sessionid
	 * @return
	 */
	public String getZCInfo(String lotno, String batchCode, String num) {
		String action = "zcAction.do";
		String method = "getFlDataInfo";
		String reValue = "";
		try {
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("lotno", lotno);
			paras.put("batchCode", batchCode);
			paras.put("num", num);
			para = URLEncoder.encode(paras.toString());
			String re = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action
					+ ";jsessionid=" + Constants.sessionId + "?method=" + method
					+ "&jsonString=" + para);

			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}
}
