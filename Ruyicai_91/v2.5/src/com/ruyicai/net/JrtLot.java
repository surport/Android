package com.ruyicai.net;

import java.util.Random;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.util.Constants;
import com.ruyicai.util.URLEncoder;

public class JrtLot {

	private static String baseUrl = Constants.SERVER_URL; // 服务器
	// 服务器
//	private static String messageUrl = Constants.MSG_URL;
	private static String lotServer=Constants.LOT_SERVER;
	public static String versionInfo = "2.3";// 当前版本号

	/**
	 * 客户端开奖查询
	 * @param play_name   彩票种类
	 * @param term_code   彩票期号
	 * @return
	 */
	public static String lotterySelect(String play_name) {
		String action = "twininfoQuery.do";
		String method = "lotterySelect";
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
			// paras.put("mobile_code", "mobile_code");
			paras.put("play_name", play_name);
			para = URLEncoder.encode(paras.toString());

			String re = InternetUtils.GetMethodOpenHttpConnect(baseUrl + action + "?method="+ method + "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
		}
		return reValue;
	}
	/**
	 * 客户端当前期号请求
	 * @param lotNo
	 */
	public static String getLotNo(String lotNo) {
		String action = "touzhu.do";
		String method = "getLotNo";
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
			paras.put("lotNo", lotNo);
			para = URLEncoder.encode(paras.toString());
			reValue = InternetUtils.GetMethodOpenHttpConnect(baseUrl + action + "?method="
					+ method + "&jsonString=" + para);
		} catch (Exception e) {
			
		}
		return reValue;
	}

	/**
	 * 客户端足彩当前期号请求
	 * @param lotNo
	 */
	public static String getZCLotNo(String lotNo) {
		String action = "zcAction.do";
		String method = "getNewIssue";
		String reValue = "";
		String para = "";
		try {
			JSONObject paras = new JSONObject();

			paras.put("lotno", lotNo);
			para = URLEncoder.encode(paras.toString());
			reValue = InternetUtils.GetMethodOpenHttpConnect(baseUrl + action + "?method="
					+ method + "&jsonString=" + para);
		} catch (Exception e) {
			
		}
		return reValue;
	}

	
	/**
	 * 取消追号
	 * @param mobileCode
	 * @param sessionid
	 * @param tsubscribeId
	 * @return
	 */
	public static String cancelTrankingNumber(String mobileCode,
			String sessionid, String tsubscribeId) {
			String action = "lotTCBet.do";
			String method = "trankingNumberUpdate";
		String reValue = "";
		try {
			String para = "";
			JSONObject paras = new JSONObject();

			paras.put("mobileCode", mobileCode);
			paras.put("tsubscribeId", tsubscribeId);

			para = URLEncoder.encode(paras.toString());

			reValue = InternetUtils.GetMethodOpenHttpConnect(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);

		} catch (Exception e) {
		}
		return reValue;
	}

	/**
	 * 完善信息
	 * @param sessionid         用户登录后的id
	 * @param idcardno          身份证号
	 * @return
	 */
	public static String perfectIfo(String sessionid, String idcardno) {
		String action = "idcardadd.do";
		String method = "add";
		try {
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("idcardno", idcardno);
			para = URLEncoder.encode(paras.toString());
			String re = InternetUtils.GetMethodOpenHttpConnect(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);

			// 对返回码进行处理
			JSONObject object;
			object = new JSONObject(re);
			String error_code = object.getString("error_code");

			if (error_code.equals("000000")) {// 修改成功
				Toast.makeText(JrtLot.context, "修改成功", Toast.LENGTH_SHORT)
						.show();
			} else if (error_code.equals("000001")) {// 身份证为空，完善信息对话框
				Toast.makeText(JrtLot.context, "身份证为空", Toast.LENGTH_SHORT)
						.show();
			} else if (error_code.equals("000002")) {// 原来的身份证错误请重新修改，修改信息对话框
				Toast.makeText(JrtLot.context, "原来的身份证错误请重新修改",
						Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("000003")) {// 修改失败
				Toast.makeText(JrtLot.context, "修改失败", Toast.LENGTH_SHORT)
						.show();
			} else if (error_code.equals("070002")) {// 用户未登录
				Toast.makeText(JrtLot.context, "用户未登录", Toast.LENGTH_SHORT)
						.show();
			} else if (error_code.equals("000004")) {// 身份证已被使用
				Toast.makeText(JrtLot.context, "身份证号码已被使用", Toast.LENGTH_SHORT)
						.show();
			} else if (error_code.equals("000005")) {// 年龄不足18周岁
				Toast.makeText(context, "年龄不足18周岁", Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
		}

		return null;
	}

	public static String sessionid;
	public static Context context;

	/**
	 * 完善信息对话框
	 */
	public static void perfectIfoDialog(String sessionid) {
		JrtLot.sessionid = sessionid;
		LayoutInflater inflater = LayoutInflater.from(context);
		final View textEntryView = inflater.inflate(R.layout.perfect_message_dialog, null);
		final EditText name = (EditText) textEntryView
				.findViewById(R.id.et_perfect_message_name);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.perfect_message_title);
		builder.setView(textEntryView);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						perfectIfo(JrtLot.sessionid, name.getText().toString());

					}

				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
	    public void onClick(DialogInterface dialog, int which) {

					}

				});
		builder.show();
	}

	/**
	 * 修改信息对话框
	 */
	public static void changePerfectIfoDialog(String sessionid) {
		JrtLot.sessionid = sessionid;
		LayoutInflater inflater = LayoutInflater.from(context);
		final View textEntryView = inflater.inflate(
				R.layout.perfect_message_dialog, null);
		final EditText name = (EditText) textEntryView
				.findViewById(R.id.et_perfect_message_name);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.change_message_title);
		builder.setView(textEntryView);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						perfectIfo(JrtLot.sessionid, name.getText().toString());

					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.show();
	}

	/**
	 * DNA充值账户绑定查询请求
	 * @param mobile
	 * @param sessionid
	 * @return
	 */
	public static String checkType(String mobile, String sessionid) {
		String action = "dnabind.do";
		String method = "bindInfoFind";
		String reValue = "";
		try {
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("Mobile", mobile);

			para = URLEncoder.encode(paras.toString());
			reValue = InternetUtils.GetMethodOpenHttpConnect(baseUrl + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);

		} catch (Exception e) {
		}

		return reValue;
	}
	

}
