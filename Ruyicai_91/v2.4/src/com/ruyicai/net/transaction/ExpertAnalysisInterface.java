package com.ruyicai.net.transaction;

import java.util.Random;

import org.json.JSONObject;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.Constants;
import com.ruyicai.util.URLEncoder;

public class ExpertAnalysisInterface {
	
	private static ExpertAnalysisInterface instance = null;
	
	private ExpertAnalysisInterface() {

	}
	
	public synchronized static ExpertAnalysisInterface getInstance(){
		if(instance == null){
			instance = new ExpertAnalysisInterface();
		}
		return instance;
	}
	
	/**
	 * 客户端专家分析请求//已测试
	 * @param
	 */
	public String analysis(String sessionid) {
		String action = "user.do";
		String method = "analysis";
		String reValue = "";
		String para = "";
		try {
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			para = URLEncoder.encode(paras.toString());
			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action + "?method="+ method + "&jsonString=" + para);
			
		} catch (Exception e) {
			
		}
		return reValue;
	}
}
