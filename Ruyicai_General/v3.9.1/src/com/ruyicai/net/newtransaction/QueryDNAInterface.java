package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 查询DNA充值接口
 * 
 * @author miao
 * 
 */
public class QueryDNAInterface {

	private static String COMMAND = "AllQuery";

	private static QueryDNAInterface instance = null;

	private QueryDNAInterface() {

	}

	public synchronized static QueryDNAInterface getInstance() {
		if (instance == null) {
			instance = new QueryDNAInterface();
		}
		return instance;

	}

	/**
	 * 查询ＤＮＡ充值的方法
	 * 
	 * @param phonenum
	 *            　公共常量：用户手机号
	 * @param sessionid
	 *            　公共常量：服务器返回的ｓｅｓｓｉｏｎｉｄ
	 * @param userno
	 *            　公共常量：用户的userno
	 * @return error_code
	 *         如果error_code为"000047"为message="无记录"；如果error_code为"070002",
	 *         message="未登录"
	 *         如果error_code为"999999",message="操作失败";如果error_code为"000000"
	 *         ,message="成功" 返回json{ bindstate 绑定状态, name
	 *         姓名,bankcardno银行卡号,certid身份证号
	 *         ,addressname户口所在地,binddate绑定时间,araeaname银行地址,phonenum手机号}
	 */
	public static String queryDNA(String phonenum, String sessionid,
			String userno) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.TYPE, "dna");
			jsonProtocol.put(ProtocolManager.SESSIONID, sessionid);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";

	}

}
