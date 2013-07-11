package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.pojo.ShouyiPojo;
import com.ruyicai.util.ProtocolManager;

/**
 * 竞彩对阵查询
 * 
 * @author Administrator
 * 
 */
public class ShouyiDataInterface {
	private static String COMMAND = "betLot";
	private static ShouyiDataInterface instance = null;

	private ShouyiDataInterface() {
	}

	public synchronized static ShouyiDataInterface getInstance() {
		if (instance == null) {
			instance = new ShouyiDataInterface();
		}
		return instance;
	}

	/**
	 * 查询参与合买的方法 jcType:1代表足球0代表篮球
	 */
	public String getshouyidada(ShouyiPojo pojo) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.BETTYPE, "yield");
			jsonProtocol.put(ProtocolManager.BETCODE, pojo.getBetcode());
			jsonProtocol.put(ProtocolManager.BATCHNUM, pojo.getBatchnum());
			jsonProtocol.put(ProtocolManager.BATCHCODE, pojo.getBatchcode());
			jsonProtocol.put(ProtocolManager.LOTNO, pojo.getLotno());
			jsonProtocol.put(ProtocolManager.LOTMULTI, pojo.getLotmulti());
			jsonProtocol.put("betNum", pojo.getBetNum());
			jsonProtocol.put("wholeYield", pojo.getWholeYield());
			jsonProtocol.put("beforeBatchNum", pojo.getBeforeBatchNum());
			jsonProtocol.put("beforeYield", pojo.getBeforeYield());
			jsonProtocol.put("afterYield", pojo.getAfterYield());
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
