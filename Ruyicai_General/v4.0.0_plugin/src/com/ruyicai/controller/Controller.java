package com.ruyicai.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.FeedBackListInterface;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.net.newtransaction.JoinStartInterface;
import com.ruyicai.net.newtransaction.ThirdPartyLoginInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.ProtocolManager;
import com.ruyicai.util.RWSharedPreferences;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class Controller {
    private static final String TAG = "Controller";
    private static Controller sInstance;
    private Context mContext;
    private JSONObject jsonObj;
    protected ProgressDialog dialog;
    protected Controller(Context _context) {
        mContext = _context;
    }

    /**
     * Gets or creates the singleton instance of Controller.
     */
    public static Controller getInstance(Context _context) {
        if (sInstance == null) {
            sInstance = new Controller(_context);
        } else {
        	sInstance.mContext = _context;
        }
        return sInstance;
    }
    
	/**
	 * 获取期号
	 * 
	 * @param type
	 * @return
	 */
	public String toNetIssue(String type) {
		// 成功获取到了期号信息
		String issueStr = "";
		try {
			issueStr = GetLotNohighFrequency.getInstance().getInfo(type);
			JSONObject allIssue = new JSONObject(issueStr);
			/**add by fansm 20130819 start*/
			String error_code = allIssue.getString("error_code");
			/**add by fansm 20130819 end*/
			if (error_code.equals("0000")) {
				// 成功获取到了期号信息
				issueStr = allIssue.getString("batchcode");
			} else{
				issueStr = "";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return issueStr;
	}
	/**
	 * 获得高频彩旗号和剩余时间
	 * @param lotno
	 */
	public void getIssueJSONObject(final MyHandler handler,final String lotno) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String re = "";
				re = GetLotNohighFrequency.getInstance().getInfo(lotno);
				if (!"".equalsIgnoreCase(re)) {
					try {
						JSONObject obj = new JSONObject(re);
						String error_code = obj.getString("error_code");
						if (error_code.equals("0000")) {
							// 成功获取到了期号信息
							String msg = obj.getString("message");
							setRtnJSONObject(obj);
							handler.handleMsg(error_code, msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
	/**
	 * 投注action
	 */
   public void doBettingAction(final MyHandler handler,final BetAndGiftPojo betAndGift) {
	   if (dialog != null) return;
	   dialog = UserCenterDialog.onCreateDialog(mContext,mContext.getResources().getString(R.string.recommend_network_connection));
	   dialog.show();
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
				try {
					str = BetAndGiftInterface.getInstance().betOrGift(betAndGift);
					JSONObject obj = new JSONObject(str);
					final String msg = obj.getString("message");
					final String error = obj.getString("error_code");
					setRtnJSONObject(obj);
					handler.handleMsg(error, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				} finally {
					dialog.dismiss();
					dialog = null;
				}
			}
		});
		t.start();
   }
	/**
	 * 投注action
	 */
  public void doBettingJoinAction(final MyHandler handler,final BetAndGiftPojo betAndGift) { 
	   if (dialog != null) return;
	   dialog = UserCenterDialog.onCreateDialog(mContext,mContext.getResources().getString(R.string.recommend_network_connection));
	   dialog.show();
	   
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				try {
					str = JoinStartInterface.getInstance().joinStart(betAndGift);
					JSONObject obj = new JSONObject(str);
					final String msg = obj.getString("message");
					final String error = obj.getString("error_code");
					setRtnJSONObject(obj);
					handler.handleMsg(error, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				} finally {
					dialog.dismiss();
					dialog = null;
				}
			}
		});
		t.start();
  }
	/**
	 * 获取期号
	 * @param handler
	 * @param type
	 * @return
	 */
	public void toNetIssue(final MyHandler handler,final String type) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String issue = toNetIssue(type);
				handler.handleMsg("0000",issue);
			}
		}).start();
	}
   /**
    * set return obj
    * @param obj
    */
   public void setRtnJSONObject(JSONObject obj) {
	   this.jsonObj = obj;
   }
   
   /**
    * get return obj
    * @param obj
    */
   public JSONObject getRtnJSONObject() {
	   return this.jsonObj;
   }
   
   
   /**
	 * 查询提现记录详情
	 * 
	 */
	public void queryCashDetail(final MyHandler handler, final String cashdetailId) {
		if (dialog != null && dialog.isShowing()) return;
		dialog = UserCenterDialog.onCreateDialog(mContext, mContext .getResources()
				.getString(R.string.recommend_network_connection));
		dialog.show();
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
				str = queryCashNet(cashdetailId);
				try {
					JSONObject obj = new JSONObject(str);
					final String msg = obj.getString("message");
					final String error = obj.getString("error_code");
					setRtnJSONObject(obj);
					handler.handleMsg(error, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		});
		t.start();
	}

	public String queryCashNet(String cashdetailId) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, "getCash");
			jsonProtocol.put(ProtocolManager.CASHTYPE, "cashDetail");
			jsonProtocol.put(ProtocolManager.CASHDETAILID, cashdetailId);
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取支付宝签名
	 * @return
	 */
	public String getAlipaySign() {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, "login");
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "alipaySign");
			
			String result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			JSONObject obj = new JSONObject(result);
			return obj.getString("value");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 查询email的绑定状态
	 * @param lotno
	 * @return
	 */
	public void queryOrderEmail(final Handler handler, final String lotno,
			final String userno) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				JSONObject jsonProtocol = ProtocolManager.getInstance()
						.getDefaultJsonProtocol();
				try {
					jsonProtocol.put(ProtocolManager.COMMAND, "message");
					jsonProtocol.put(ProtocolManager.REQUESTTYPE,
							"selectOrderEmail");
					jsonProtocol.put(ProtocolManager.LOTNO, lotno);
					jsonProtocol.put(ProtocolManager.USERNO, userno);
					String returnInfo = InternetUtils
							.GetMethodOpenHttpConnectSecurity(
									Constants.LOT_SERVER,
									jsonProtocol.toString());
					JSONObject obj = new JSONObject(returnInfo);
					String error_code = obj.getString("error_code");
					if (error_code.equals("0000")) {
						String result = obj.getString("result");
						Message msg = new Message();
						msg.what = 1;
						msg.obj = result;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}).start();

	}
	
	/**
	 * 订单邮件开发设置
	 * @param lotno
	 * @return
	 */
	public void setOrderEmail(final Handler handler, final String lotno, final String state, final String userNo) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				JSONObject jsonProtocol = ProtocolManager.getInstance()
						.getDefaultJsonProtocol();
				try {
					jsonProtocol.put(ProtocolManager.COMMAND, "message");
					jsonProtocol.put(ProtocolManager.REQUESTTYPE, "orderEmail");
					jsonProtocol.put(ProtocolManager.LOTNO, lotno);
					jsonProtocol.put(ProtocolManager.STATE, state);
					jsonProtocol.put(ProtocolManager.USERNO, userNo);
					String result = InternetUtils.GetMethodOpenHttpConnectSecurity(
							Constants.LOT_SERVER, jsonProtocol.toString());
					JSONObject obj = new JSONObject(result);
					String error_code = obj.getString("error_code");
					Message msg = new Message();
					if (error_code.equals("0000")) {
						Bundle data = new Bundle();
						data.putString("state", state);
						data.putString("lotno", lotno);
						msg.what = 2;
						msg.setData(data);
					} else {
						msg.what = 3;
					}
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	
	/**
	 * 读取账户充值的显示状态
	 */
	public void readReChargeCenterState() {
		final RWSharedPreferences shellRW = new RWSharedPreferences(mContext,
				ShellRWConstants.ACCOUNT_DISPAY_STATE);
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("ch0001", Constants.YINLIAN_SOUND_DISPLAY_STATE);// 银联语音
//		map.put("ch0002", Constants.PHONE_RECHARGE_CARD_DISPLAY_STATE);//手机充值卡充值
//		map.put("ch0005", Constants.ZHIFUBAO_RECHARGE_DISPLAY_STATE); //支付宝充值
//		map.put("ch0006", Constants.YINLIAN_CARD_DISPLAY_STATE); //银联充值
//		map.put("ch0007", Constants.ZHIFUBAO_SECURE_PAYMENT_DISPLAY_STATE);//支付宝安全支付
//		map.put("ch0008", Constants.BANK_RECHARGE_DISPLAY_STATE);//银行充值
//		map.put("ch0010", Constants.LAKALA_PAYMENT_DISPLAY_STATE); //拉卡拉充值
//		map.put("ch0011", Constants.UMPAY_DISPLAY_STATE);  //联动优势
//		map.put("ch0012", Constants.UMPAY_PHONE_DISPLAY_STATE);  //联动优势话费充值
		final String rechargeType[] = {"ch0005","ch0007"};
		final String rechargeTitle[] = {
				Constants.ZHIFUBAO_RECHARGE_DISPLAY_STATE,
				Constants.ZHIFUBAO_SECURE_PAYMENT_DISPLAY_STATE};
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject jsonObject = RechargeDescribeInterface.getInstance()
						.rechargeShowState();
				try {
					if (jsonObject != null) {
						String error_code = jsonObject.getString("error_code");
						if (error_code.equals("0000")) {
							JSONArray jsonArray = jsonObject.getJSONArray("result");
							for (int i = 0; i < rechargeType.length; i++) {
								outer : for (int j = 0; j<jsonArray.length(); j++) {
									JSONObject json = jsonArray.getJSONObject(j);
									if (json.has(rechargeType[i])) {
										shellRW.putBooleanValue(rechargeTitle[i], true);
										break outer;
									} else {
										shellRW.putBooleanValue(rechargeTitle[i], false);
									}
								}
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	

	
	
	public void getFeedbackListNet(final Handler handler, final String userno) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Constants.feedBackData = FeedBackListInterface.getInstance()
						.getFeedbackList("0", "10", userno);
				try {
					Message msg = new Message();
					JSONObject feedjson = new JSONObject(Constants.feedBackData);
					String errorCode = feedjson.getString("error_code");
					// add by yejc 20130411
					if (feedjson.has("result")) {
						Constants.feedBackJSONArray = feedjson
								.getJSONArray("result");
					}
					msg.what = 11;
					msg.obj = Constants.feedBackJSONArray;
					// end
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 第三方登录接口
	 * @param handler
	 * @param token
	 * @param plat
	 */
	public void thirdPartyLogin(final Handler handler, final String token,
			final String plat) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				JSONObject jsonObject = ThirdPartyLoginInterface.getInstance()
						.thirdPartyLogin(token, plat);
				try {
					String errorCode = jsonObject.getString("error_code");
					if("0".equals(errorCode)){
						Message message = new Message();
						message.what = 6;
						message.obj = jsonObject;
						handler.sendMessage(message);
					}else{
						Message message = new Message();
						message.what = 7;
						message.obj = jsonObject;
						handler.sendMessage(message);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
