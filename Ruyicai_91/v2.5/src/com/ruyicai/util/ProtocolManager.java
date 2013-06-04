package com.ruyicai.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


/**
 *@author haojie
 * 协议管理类<br>
 * 作用是处理通讯协议发送，接收的组装<br>
 * 通讯协议使用json格式<br>
 * 数据在联网层做了加密<br>
 * singleton 模式
 */
public class ProtocolManager {
	public final static String RECOMMANDER = "recommander";//
	/**
	 * 是否压缩
	 */
	public final static String ISZIP = "isCompress";
	
	private final static String TAG = "ProtocolManager";
	/**
	 *transactiontype
	 *在账户明细、 、中使用
	 */
	public static String TRANSACTIONTYPE = "transactiontype";
	/**
	 *startdate
	 *在账户明细、 、中使用
	 */
	public static String START_DATE = "startdate";
	/**
	 * bettype
	 * 投注和赠送接口中调用
	 */
	public static String BETTYPE = "bettype";
	
	/**
	 * enddate
	 * 在账户明细、、中使用
	 */
	public static String END_DATE = "enddate";
	/**
	 * pageindex
	 * 在账户明细、赠送查询、投注查询、中奖查询中使用
	 */
	public static String PAGEINDEX = "pageindex";
	/**
	 * type 
	 * 在账户明细、余额查询、投注查询、赠送查询、查询DNA、查询最近提现、追号查询接口
	 * 中奖查询中使用
	 */
	public static String TYPE = "type";
	
	
    /**
     * batchcode
     * 在投注和赠送接口、投注查询、赠送查询、中奖查询中使用
     */
	public static String BATCHCODE = "batchcode";
	/**
     * batchnum
     * 在投注和赠送接口、、中使用
     */
	public static String BATCHNUM = "batchnum";
	/**
     * bet_code
     * 在投注和赠送接口、、中使用
     */
	public static String BETCODE = "bet_code";
	/**
     * lotno
     * 在投注和赠送接口、投注查询、赠送查询、中奖查询中使用
     */
	public static String LOTNO = "lotno";
	/**
     * lotmulti
     * 在投注和赠送接口、、中使用
     */
	public static String LOTMULTI = "lotmulti";
	/**
     * sellway
     * 在投注和赠送接口、、中使用
     */
	public static String SELLWAY = "sellway";
	/**
	 * infoway 
	 * 通过咨询投注
	 */
	public static String INFOWAY = "infoway"; 
	/**
     * amount
     * 在投注和赠送接口、修改提现、中使用
     */
	public static String AMOUNT = "amount";
	/**
     * to_mobile_code
     * 在投注和赠送接口、、中使用
     */
	public static String TOMOBILECODE = "to_mobile_code";
	
	public static String ISSUPER = "issuperaddition";
	/**
     * advice
     * 在投注和赠送接口、、中使用
     */
	public static String ADVICE = "advice";
	/**
	 * tsubscribeNo
	 * 取消追号、中使用
	 */
	public static String TSUSBSCRIBEID = "tsubscribeNo";
	/**
	 * cashtype
	 * 取消提现、修改提现、中使用
	 */
	public static String CASHTYPE = "cashtype";
	/**
	 * old_pass
	 * 修改密码、中使用
	 */
	public  static   String OLDPASS = "old_pass";
	/**
	 * new_pass
	 * 修改密码、中使用
	 */
	public  static   String NEWPASS = "new_pass";
	/**
	 * araeaname
	 * 修改提现、中使用
	 */
	public static String ARAEANAME = "areaname";
	/**
	 * bankname
	 * 修改提现、中使用
	 */
	public static String BANKNAME = "bankname";
	
	public static String BANKCARDNO = "bankcardno";
	public static String CASHID = "cashdetailid";
	

	/**
	 * statinfo
	 * 软件更新接口中使用
	 */
	public static String GAME_STATINFO = "statinfo";
	/**
	 * downloadurl
	 */
	public static String DOWNLOAD_URL = "downloadurl";
	/**
	 * content
	 * 用户反馈中、中使用
	 */
	public static String FEEDBACK_CONTENT = "content";
	/**
	 * password
	 * 登录接口、注册接口中使用
	 */
	public static String PASSWORD = "password";
	
	/**
	 * cardtype
	 * 充值接口中使用
	 */
	public static  String CARDTYPE = "cardtype";
	/**
	 * rechargetype
	 * 充值接口中使用
	 */
	public static  String RECHARGETYPE = "rechargetype";
	/**
	 * cardno
	 * 充值接口中使用
	 */
	public static  String CARDNO = "cardno";
	/**
	 * cardpwd
	 * 充值接口中使用
	 */
	public static  String CARDPWD = "cardpwd";
	/**
	 * name
	 * 充值接口、注册接口中使用
	 */
	public static  String NAME = "name";
	public static  String CARD = "ruyicardnumber";
	public static  String CARDPASSWORD = "ruyicardpassword";
	/**
	 * certid
	 * 充值接口、注册中使用、身份证绑定
	 */
	public static  String CERTID = "certid";
	/**
	 * araeaname
	 * 充值接口中使用
	 */
	public static  String ADDRESSNAME = "addressname";
	/**
	 * 每页显示数
	 * 账户明细、赠送查询、投注查询、中奖查询
	 * maxresult
	 */
	public static  String MAXRESULT = "maxresult";
	/**
	 * 合买状态
	 */
	public static  String STATE = "state";
	public static  String ORDERBY = "orderBy";
	public static  String ORDERDIR = "orderDir";
	public static  String CASEID = "caseid";
	public static  String SAFEAMT = "safeAmt";

	public static String USERNO = "userno";
	public static String SESSIONID = "sessionid";
	public static String PHONE_NUM = "phonenum";
	public static String COMMAND = "command";
	public static String IS_EMULATOR = "isemulator";
	public static String IMEI = "imei";
	public static String IMSI = "imsi";
	public static String SOFTWARE_VERSION = "softwareversion";
	public static String SMS_CENTER = "smscenter";
	public static String COOP_ID = "coopid";
	public static String MACHINE_ID = "machineid";
	public static String PLATFORM = "platform";
    public static String INFORMATION_TYPE = "type";
    public static String ISWHITE="iswhite";
    public static String BANKADDRESS  ="bankaddress";
    /**
     * 发起合买
     */
    public static String TOTALAMT  ="totalAmt";
    public static String ONEAMOUNT  ="oneAmount";
    public static String BUYAMT  ="buyAmt";
    public static String RATION  ="commisionRation";
    public static String VISIBILITY  ="visibility";
    public static String MINAMT  ="minAmt";
    public static String DESCRIPTION  ="description";
	
	//种类为高频彩
	public static String HIGHFREQENCYTPYE = "highFrequency";
	
	private static JSONObject defaultJsonObject  = null;
	
	private static ProtocolManager protocolManager = null;
	private ProtocolManager() {
		
	}
	
	public synchronized static ProtocolManager getInstance(){
		if(protocolManager == null){
			protocolManager = new ProtocolManager();			
		}
		defaultJsonObject = new JSONObject();
		return protocolManager;
	}
	/**
	 * 获取除userno,sessionid,phonenum公共参数(imsi,imei,softwareversion,machineid,coopid,smscenter,platform)
	 * @return 含有公共参数的jsonobject
	 */
    public JSONObject getDefaultJsonProtocol(){
    	try {
    		if(Constants.IMEI == null){
    			defaultJsonObject.put(IMEI, "");
			}else{
				defaultJsonObject.put(IMEI, Constants.IMEI);
			}
			if(Constants.IMSI == null){
				defaultJsonObject.put(IMSI, "");
			}else{
			    defaultJsonObject.put(IMSI, Constants.IMSI);
			}
			defaultJsonObject.put(SOFTWARE_VERSION, Constants.SOFTWARE_VERSION);
			defaultJsonObject.put(MACHINE_ID, Constants.MACHINE_ID);
			defaultJsonObject.put(COOP_ID, Constants.COOP_ID);
			defaultJsonObject.put(SMS_CENTER, Constants.SMS_CENTER);
			defaultJsonObject.put(PLATFORM, Constants.PLATFORM_ID);
			defaultJsonObject.put(ISZIP, Constants.ISCOMPRESS);
			
		} catch (JSONException e) {
			Log.e(TAG, "获取公共参数出错啦");
		}
    	return defaultJsonObject;
    }
}
