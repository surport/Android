	package com.ruyicai.net.transaction;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.URLEncoder;

public class BettingInterface {
	
	String error_code = "00";
	String amount;// 购彩金额
	String term_begin_datetime;// 开奖日期
	String deposit_sum;// 账户余额
	String sell_term_code;// 彩票期数
	String error_code_DYJ = "00";

	/**
	 * 投注新接口
	 * @param bet_code     注码
	 * @param count        期数
	 * @param amount       投注总金额
	 * @param sessionid    登录号
	 */
	private static BettingInterface bettingInterface = null;
	private final static String TAG = "BettingInterface";
	
	private BettingInterface() {
		
	}
	public synchronized static BettingInterface getInstance(){
		if(bettingInterface == null){
			bettingInterface = new BettingInterface();
		}
		return bettingInterface;
	}
	
	
	/**
	 * 调用lotserver处理投注请求
	 * 此方法连接lotserver服务器,最终调用的是新的lotserver
	 * 支持所有投注格式
	 * @return
	 */
	public String bettingLotServer(){
		return null;
	}
	
    /**
     * 提交服务器下注
     */
	public String bettingNew(String bet_code, String count, String amount,String sessionid) {
				Log.e(TAG, "bet_code="+bet_code);
		Log.e(TAG, "count="+count);
		Log.e(TAG, "amount="+amount);
		Log.e(TAG, "sessionid="+sessionid);
		
		JSONObject obj = null;
		try {
			String re = addNumber(bet_code, count, amount, "Y",	sessionid);
			obj = new JSONObject(re);
			error_code = obj.getString("error_code");
		} catch (JSONException e) {

		}
		return error_code;
	}

	// 体彩投注接口 20100712 陈晨
	public String[] BettingTC(String mobileCode, String lotNo, String betCode,
			String lotMulti, String amount, String oneMoney, String batchnum,
			String sessionid) {

		JSONObject obj = null;
		String error_code = "00";
		String error_code_DYJ = "00";
		try {
			String re = bettingTC(mobileCode, lotNo, betCode,
					lotMulti, amount + "00", oneMoney, batchnum, sessionid);
			obj = new JSONObject(re);
			error_code = obj.getString("error_code");
			error_code_DYJ = obj.getString("error_code_DYJ");
		} catch (JSONException e) {
			if (error_code.equals("00") && obj != null) {
				try {
					error_code_DYJ = obj.getString("error_code_DYJ");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		}
		String[] errorCode = { error_code, error_code_DYJ };
		return errorCode;

	}

	// 体彩投注接口 20100712 陈晨
	public String[] BettingTC(String mobileCode, String lotNo, String betCode,
			String lotMulti, String amount, String oneMoney, String batchnum,
			String sessionid, String batchCode) {

		JSONObject obj = null;
		String error_code = "00";
		String error_code_DYJ = "00";
		try {
			String re = bettingTC(mobileCode, lotNo, betCode,
					lotMulti, amount + "00", oneMoney, batchnum, sessionid,
					batchCode);
			obj = new JSONObject(re);
			error_code = obj.getString("error_code");
			error_code_DYJ = obj.getString("error_code_DYJ");
		} catch (JSONException e) {
			if (error_code.equals("00") && obj != null) {
				try {
					error_code_DYJ = obj.getString("error_code_DYJ");
				} catch (JSONException e1) {
					
				}
			}

		}
		String[] errorCode = { error_code, error_code_DYJ };
		return errorCode;

	}
	
	// 体彩投注接口 陈晨 8.12
	/**
	 * 客户端时时彩投注请求
	 * @param mobile_code
	 * @param bet_code          投注码
	 * @param amount            购彩金额
	 * @param sessionid
	 * @return
	 */
	public static String bettingTC(String mobileCode, String lotNo,
			String betCode, String lotMulti, String amount, String oneMoney,
			String batchnum, String sessionid, String batchcode) {
		String action = "lotTCBet.do";
		String method = "addZh";
		String reValue = "";
		try {
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobileCode", mobileCode);
			// paras.put("sessionid", sessionid);
			paras.put("lotNo", lotNo);
			paras.put("betCode", betCode);
			paras.put("lotMulti", lotMulti);
			paras.put("amount", amount);
			paras.put("oneMoney", oneMoney);
			paras.put("batchnum", batchnum);
			paras.put("batchcode", batchcode);// 期号
			paras.put("channel", Constants.COOP_ID);
			
			para = URLEncoder.encode(paras.toString());
			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}
	
	/**
	 * 客户端投注追号请求
	 * @param bet_code           投注码
	 * @param count              追号期数
	 * @param amount             投注金额
	 * @param type               当期是否投注 Y表示当期就投注。即投注+追号。N表示当期不投注，即只执行追号定制。
	 * @param sessionid
	 * @return
	 */
	public static String addNumber(String bet_code, String count, String amount, String type, String sessionid) {
		String action = "addNumAttemper.do";
		String method = "addNumber";
		String reValue = "";
		try {
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("bet_code", bet_code);
			paras.put("amount", amount);
			paras.put("Type", type);
			paras.put("count", count);
			paras.put("channel", Constants.COOP_ID);
			
			para = URLEncoder.encode(paras.toString());
			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
		} catch (Exception e) {
			Log.e(TAG, "BettingInterfaceError");
		}

		return reValue;
	}
	
	// 体彩投注接口 陈晨 8.12
	/**
	 * 客户端体彩投注请求
	 * @param mobile_code
	 * @param bet_code    投注码
	 * @param amount      购彩金额
	 * @param sessionid
	 * @return
	 */
	public static String bettingTC(String mobileCode, String lotNo,
			String betCode, String lotMulti, String amount, String oneMoney,
			String batchnum, String sessionid) {
		String action = "lotTCBet.do";
		String method = "addZh";
		String reValue = "";
		try {
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("mobileCode", mobileCode);
			paras.put("lotNo", lotNo);
			paras.put("betCode", betCode);
			paras.put("lotMulti", lotMulti);
			paras.put("amount", amount);
			paras.put("oneMoney", oneMoney);
			paras.put("batchnum", batchnum);

			paras.put("channel", Constants.COOP_ID);

			para = URLEncoder.encode(paras.toString());
			
			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action+ ";jsessionid=" + sessionid + "?method=" + method+ "&jsonString=" + para);

		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}
}
