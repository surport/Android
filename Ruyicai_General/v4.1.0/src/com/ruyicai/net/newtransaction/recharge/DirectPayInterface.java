package com.ruyicai.net.newtransaction.recharge;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.ProtocolManager;

public class DirectPayInterface {
	private static String COMMAND = "betLot";
	private static DirectPayInterface instance = null;

	private DirectPayInterface() {

	}

	public synchronized static DirectPayInterface getInstance() {
		if (instance == null) {
			instance = new DirectPayInterface();
		}
		return instance;
	}

	public String directPay(BetAndGiftPojo betAndGift, boolean isAlipaySecure) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();

		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.BETTYPE, "saveorder");
			jsonProtocol.put(ProtocolManager.USERNO, betAndGift.getUserno());
			jsonProtocol
					.put(ProtocolManager.PHONENUM, betAndGift.getPhonenum());
			jsonProtocol.put(ProtocolManager.BETCODE, betAndGift.getBet_code());
			jsonProtocol.put(ProtocolManager.BATCHCODE,
					betAndGift.getBatchcode());
			jsonProtocol.put(ProtocolManager.LOTNO, betAndGift.getLotno());
			jsonProtocol
					.put(ProtocolManager.LOTMULTI, betAndGift.getLotmulti());
			jsonProtocol.put(ProtocolManager.AMOUNT, betAndGift.getAmount());
			if (betAndGift.getAmt() != 0) {
				jsonProtocol.put(ProtocolManager.ONEAMOUNT, betAndGift.getAmt()
						+ "00");
			} else {
				jsonProtocol.put(ProtocolManager.ONEAMOUNT,
						betAndGift.getOneAmount());

			}

			jsonProtocol.put(ProtocolManager.ISSELLWAYS,
					betAndGift.getIsSellWays());
			if (isAlipaySecure) {
				jsonProtocol.put(ProtocolManager.RECHARGETYPE, "07");

			} else {
				jsonProtocol.put(ProtocolManager.RECHARGETYPE, "05");
			}
			jsonProtocol.put(ProtocolManager.CARDTYPE, "0300");
			jsonProtocol.put(ProtocolManager.BANKACCOUNT, "4");

			if (Constants.LOTNO_JCZQ.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCZQ_RQSPF.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCZQ_ZQJ.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCZQ_BF.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCZQ_BF.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCZQ_BQC.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCZQ_HUN.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCLQ.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCLQ_RF.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCLQ_SFC.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCLQ_DXF.equals(betAndGift.getLotno())
					|| Constants.LOTNO_JCLQ_HUN.equals(betAndGift.getLotno())) {
				jsonProtocol.put("expectPrizeAmt", betAndGift.getPredictMoney());
			}
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
}
