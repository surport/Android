package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.util.ProtocolManager;

/**
 * 用户充值的接口
 * 
 * @author miao
 */
public class RechargeInterface {

	private static String COMMAND = "recharge";

	private static RechargeInterface instance = null;

	private RechargeInterface() {
	}

	public synchronized static RechargeInterface getInstance() {
		if (instance == null) {
			instance = new RechargeInterface();
		}
		return instance;
	}

	/**
	 * 充值的方法
	 * 
	 * @param userno
	 *            用户编号
	 * @param amount
	 *            用户充值金额，单位为分
	 * @param cardtype
	 *            卡类型 <br>
	 *            0203:移动充值卡<br>
	 *            0206:联通充值卡<br>
	 *            0221:电信充值卡<br>
	 *            0101:招商银行<br>
	 *            0102:建设银行<br>
	 *            0103:工商银行<br>
	 *            0204:征途卡<br>
	 *            0201:骏网一卡通<br>
	 *            0202:盛大卡<br>
	 * 
	 * @param rechargetype
	 *            充值类型 <br>
	 *            01:银行卡电话充值,<br>
	 *            02:手机充值卡充值<br>
	 *            03:手机银行充值<br>
	 *            04:游戏点卡充值<br>
	 *            05:支付宝充值<br>
	 * 
	 * @param cardno
	 *            卡号
	 * @param cardpwd
	 *            对应卡号”cardno“的密码
	 * @param sessionid
	 * @param name
	 *            持卡人姓名
	 * @param certid
	 *            身份证号
	 * @param araeaname
	 * @return error_code|message<br/>
	 *         070002 |未登录<br/>
	 *         000000 |充值请求已受理，请稍后查询余额<br/>
	 *         001200 |卡号或密码错误 <br/>
	 *         001300 |请求已提交<br/>
	 *         001400 |请填写详细信息<br/>
	 *         001500 |暂不支持卡号<br/>
	 *         999999 |操作失败<br/>
	 *         transation_id 交易id<br/>
	 *         return_url 支付宝响应地址
	 */
	public static String recharge(RechargePojo chargePojo) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, chargePojo.getUserno());
			jsonProtocol.put(ProtocolManager.AMOUNT, chargePojo.getAmount()
					+ "00");
			jsonProtocol
					.put(ProtocolManager.CARDTYPE, chargePojo.getCardtype());
			jsonProtocol.put(ProtocolManager.RECHARGETYPE,
					chargePojo.getRechargetype());
			jsonProtocol.put(ProtocolManager.CARDNO, chargePojo.getCardno());
			jsonProtocol.put(ProtocolManager.CARDPWD, chargePojo.getCardpwd());
			jsonProtocol.put(ProtocolManager.NAME, chargePojo.getName());
			jsonProtocol.put(ProtocolManager.CERTID, chargePojo.getCertid());
			jsonProtocol.put(ProtocolManager.ADDRESSNAME,
					chargePojo.getAddressname());
			jsonProtocol.put(ProtocolManager.SESSIONID,
					chargePojo.getSessionid());
			jsonProtocol.put(ProtocolManager.PHONE_NUM,
					chargePojo.getPhonenum());
			jsonProtocol
					.put(ProtocolManager.ISWHITE, chargePojo.getIsIswhite());
			jsonProtocol.put(ProtocolManager.BANKADDRESS,
					chargePojo.getBankaddress());
			jsonProtocol
					.put(ProtocolManager.BANKNAME, chargePojo.getBankName());
			/**add by yejc 20130527 start*/
			jsonProtocol.put(ProtocolManager.BANKID, chargePojo.getBankId());
			jsonProtocol.put(ProtocolManager.MOBILEID, chargePojo.getMobileId());
			/**add by yejc 20130527 end*/
			jsonProtocol.put(ProtocolManager.BANKACCOUNT,
					chargePojo.getBankAccount());
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
	
	public static String exChangeGold(String userno, String sessionid, String type, String code) {
		String result = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.SESSIONID, sessionid);
			jsonProtocol.put(ProtocolManager.RECHARGETYPE, type);
			jsonProtocol.put(ProtocolManager.COUPONCODE, code);
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

}
