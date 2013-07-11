package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.AccountDetailQueryPojo;
import com.ruyicai.util.ProtocolManager;

/**
 * 账户明细查询接口
 * 
 * @author miao
 * 
 */
public class AccountDetailQueryInterface {

	private static String COMMAND = "accountdetail";

	private static AccountDetailQueryInterface instance = null;

	private AccountDetailQueryInterface() {
	}

	public synchronized static AccountDetailQueryInterface getInstance() {
		if (instance == null) {
			instance = new AccountDetailQueryInterface();
		}
		return instance;
	}

	/**
	 * 账户明细查询方法
	 * 
	 * @param userno
	 *            用户编号 用户注册成功返回的用户编号<br>
	 * @param startline
	 *            开始条数<br>
	 * @param stopline
	 *            截止条数<br>
	 * @param pageindex
	 *            第几页，比如第二页,pageindex=2<br>
	 * @param sessionid
	 * <br>
	 * @param type
	 *            1:充值，<br>
	 *            2:支付,<br>
	 *            3:派奖,<br>
	 *            4:提现<br>
	 * @param accountDetailPojo
	 * @return error_code | message <br>
	 *         000000 |成功<br>
	 *         000047 |无记录<br>
	 *         999999 |操作失败<br>
	 *         type_memo : 交易类型<br>
	 *         amount :变动金额 金额单位为分<br>
	 *         plattime : 交易时间 <br>
	 */
	public String accountDetailQuery(AccountDetailQueryPojo accountDetailPojo) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO,
					accountDetailPojo.getUserno());
			jsonProtocol.put(ProtocolManager.SESSIONID,
					accountDetailPojo.getSessionid());
			jsonProtocol.put(ProtocolManager.MAXRESULT,
					accountDetailPojo.getMaxresult());
			jsonProtocol.put(ProtocolManager.PAGEINDEX,
					accountDetailPojo.getPageindex());
			jsonProtocol.put(ProtocolManager.PAGEINDEX,
					accountDetailPojo.getPageindex());
			jsonProtocol.put(ProtocolManager.TYPE, accountDetailPojo.getType());
			jsonProtocol.put(ProtocolManager.TRANSACTIONTYPE,
					accountDetailPojo.getTransactiontype());
			jsonProtocol.put(ProtocolManager.PHONE_NUM,
					accountDetailPojo.getPhonenum());

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
