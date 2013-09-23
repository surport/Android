package com.ruyicai.net.newtransaction;

import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;
import com.ruyicai.util.PublicMethod;

/**
 * 软件升级联网事物
 * 
 * @author haojie {"errorCode":"A030000", "clientnametype":"000001",
 *         "currentversion":"2.2",
 *         "downurl":"http://219.148.162.68/upload/client/1305623731500.apk"
 *         "news":"提示信息" "currentBatchCode":"{"F47102"：{当前期号,结束时间}}" }
 */
public class SoftwareUpdateInterface {
	private static final String TAG = "SoftwareUpdateInterface";

	private static String COMMAND = "softwareupdate";

	private SoftwareUpdateInterface() {

	}

	private static SoftwareUpdateInterface instance = null;

	public synchronized static SoftwareUpdateInterface getInstance() {
		if (instance == null) {
			instance = new SoftwareUpdateInterface();
		}
		return instance;
	}

	/**
	 * 检测是否更新新版本 statInfo 统计信息
	 */
	public String softwareupdate(JSONObject statInfo) {
		String reValue = "";
		try {
			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.IS_EMULATOR,
					PublicMethod.isEmulator());

			if (statInfo != null) {
				// 有统计信息
				jsonProtocol.put(ProtocolManager.GAME_STATINFO, statInfo);
			}

			reValue = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (Exception e) {
		}

		return reValue;

	}

	/**
	 * 检测是否更新新版本 statInfo 统计信息
	 */
	public String softwareupdate(JSONObject statInfo, String randomNumber,String alias) {
		String reValue = "";
		try {
			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.IS_EMULATOR,
					PublicMethod.isEmulator());
			jsonProtocol.put(ProtocolManager.RANDOMNUMBER, randomNumber);
			jsonProtocol.put(ProtocolManager.ALIAS, alias);

			if (statInfo != null) {
				// 有统计信息
				jsonProtocol.put(ProtocolManager.GAME_STATINFO, statInfo);
			}
			Log.e("HomeActivity=======", "=========" + jsonProtocol.toString());
			reValue = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (Exception e) {
		}

		return reValue;

	}

}
