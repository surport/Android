package com.ruyicai.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.ChannelConstants;
import com.ruyicai.constant.Constants;

import android.util.Log;

/**
 * @author haojie 协议管理类<br>
 *         作用是处理通讯协议发送，接收的组装<br>
 *         通讯协议使用json格式<br>
 *         数据在联网层做了加密<br>
 *         singleton 模式
 */
public class ProtocolManager {
	/**
	 * 是否压缩
	 */
	public final static String ISZIP = "isCompress";
	/**
	 * 获取咨询借口新加字段
	 */
	public final static String NEWSTYPE = "newsType";
	/**
	 * 活动中心借口新加字段
	 */
	public final static String ID = "activityId";
	/**
	 * 赠送彩票新加字段
	 */
	public final static String GIFTID = "presentId";
	/**
	 * 赠送彩票新加字段
	 */
	public final static String GIFTCODE = "securityCode";
	/**
	 * 获取咨询借口新加字段_-<br>
	 * <B>新闻列表子项id</B>
	 */
	public final static String NEWSID = "newsId";

	private final static String TAG = "ProtocolManager";
	/**
	 * transactiontype 在账户明细、 、中使用
	 */
	public static String TRANSACTIONTYPE = "transactiontype";
	/**
	 * startdate 在账户明细、 、中使用
	 */
	public static String START_DATE = "startdate";
	/**
	 * bettype 投注和赠送接口中调用
	 */
	public static String BETTYPE = "bettype";

	/**
	 * enddate 在账户明细、、中使用
	 */
	public static String END_DATE = "enddate";
	/**
	 * pageindex 在账户明细、赠送查询、投注查询、中奖查询中使用
	 */
	public static String PAGEINDEX = "pageindex";
	/**
	 * type 在账户明细、余额查询、投注查询、赠送查询、查询DNA、查询最近提现、追号查询接口 中奖查询中使用
	 */
	public static String TYPE = "type";


	public static String EVENT = "event";

	/**
	 * batchcode 在投注和赠送接口、投注查询、赠送查询、中奖查询中使用
	 */
	public static String BATCHCODE = "batchcode";
	/**
	 * batchnum 在投注和赠送接口、、中使用
	 */
	public static String BATCHNUM = "batchnum";
	/**
	 * bet_code 在投注和赠送接口、、中使用
	 */
	public static String BETCODE = "bet_code";
	/**
	 * lotno 在投注和赠送接口、投注查询、赠送查询、中奖查询中使用
	 */
	public static String LOTNO = "lotno";
	/**
	 * lotmulti 在投注和赠送接口、、中使用
	 */
	public static String LOTMULTI = "lotmulti";
	/**
	 * sellway 在投注和赠送接口、、中使用
	 */
	public static String SELLWAY = "sellway";
	/**
	 * infoway 通过咨询投注
	 */
	public static String INFOWAY = "infoway"; 
	
	/**
	 *按昵称搜索
	 */
	public static String INFO  =  "info";
	/**
	 * amount 在投注和赠送接口、修改提现、中使用
	 */
	public static String AMOUNT = "amount";
	/**
	 * to_mobile_code 在投注和赠送接口、、中使用
	 */
	public static String TOMOBILECODE = "to_mobile_code";

	public static String ISSUPER = "issuperaddition";
	/**
	 * advice 在投注和赠送接口、、中使用
	 */
	public static String ADVICE = "advice";
	/**
	 * advice 在投注和赠送接口、、中使用
	 */
	public static String BLESSING = "blessing";
	/**
	 * 中奖后停止追号
	 */
	public static String PRIZEEND = "prizeend";
	/**
	 * 新版本增加多注头，isSellWays用在新版本的投注查询中以便标示
	 */
	public static String ISSELLWAYS = "isSellWays";
	/**
	 * tsubscribeNo 取消追号、中使用
	 */
	public static String TSUSBSCRIBEID = "tsubscribeNo";
	/**
	 * cashtype 取消提现、修改提现、中使用
	 */
	public static String CASHTYPE = "cashtype";
	/**
	 * old_pass 修改密码、中使用
	 */
	public static String OLDPASS = "old_pass";
	/**
	 * new_pass 修改密码、中使用
	 */
	public static String NEWPASS = "new_pass";
	/**
	 * araeaname 修改提现、中使用
	 */
	public static String ARAEANAME = "areaname";
	/**
	 * bankname 修改提现、中使用
	 */
	public static String BANKNAME = "bankname";
	public static String BANKCARDNO = "bankcardno";
	public static String CASHID = "cashdetailid";

	/**
	 * statinfo 软件更新接口中使用
	 */
	public static String GAME_STATINFO = "statinfo";
	/**
	 * downloadurl
	 */
	public static String DOWNLOAD_URL = "downloadurl";
	/**
	 * content 用户反馈中、中使用
	 */
	public static String FEEDBACK_CONTENT = "content";
	/**
	 * password 登录接口、注册接口中使用
	 */
	public static String PASSWORD = "password";

	public static String ISAUTOLOGIN = "isAutoLogin";

	/**
	 * cardtype 充值接口中使用
	 */
	public static String CARDTYPE = "cardtype";
	/**
	 * rechargetype 充值接口中使用
	 */
	public static String RECHARGETYPE = "rechargetype";
	
	public static String COUPONCODE = "couponCode";

	/**
	 * cardno 充值接口中使用
	 */
	public static String CARDNO = "cardno";
	/**
	 * cardpwd 充值接口中使用
	 */
	public static String CARDPWD = "cardpwd";
	/**
	 * cardpwd 充值接口中使用
	 */
	public static String BANKACCOUNT = "bankAccount";
	/**
	 * name 充值接口、注册接口中使用
	 */
	public static String NAME = "name";
	public static String AGENCYNO = "agencyNo";
	public static String ISBINDPHONE = "isBindPhone";
	/**
	 * recommender 注册接口中使用
	 */
	public static String RECOMMENDER = "recommender";
	/**
	 * certid 身份证号 充值接口、注册中使用、身份证绑定
	 */
	public static String CERTID = "certid";
	/**
	 * araeaname 充值接口中使用
	 */
	public static String ADDRESSNAME = "addressname";
	/**
	 * 每页显示数 账户明细、赠送查询、投注查询、中奖查询 maxresult
	 */
	public static String MAXRESULT = "maxresult";
	/**
	 * 绑定手机号时要绑定的手机号字段
	 */
	public static String BINDPHONENUM = "bindPhoneNum";
	/**
	 * 绑定手机号时要提交的手机验证码字段
	 */
	public static String SECURITYCODE = "securityCode";
	/**
	 * 自动登录时返回的随机数
	 */
	public static String RANDOMNUMBER = "randomNumber";
	/**
	 * 登陆的时候，设置的别名
	 */
	public static String ALIAS = "alias";
	
	public static String BANKID = "bankId"; //add by yejc 20130527
	public static String MOBILEID = "mobileid"; 
	public static String CASHDETAILID = "cashdetailid";
	public static String BEIDAN_COMMAND = "beiDan";

	/**
	 * new instance type
	 */
	public static String REQUESTTYPE = "requestType";
	/**
	 * 合买状态
	 */
	public static String STATE = "state";
	public static String ORDERBY = "orderBy";
	public static String ORDERDIR = "orderDir";
	public static String CASEID = "caseid";
	public static String SAFEAMT = "safeAmt";

	public static String USERNO = "userno";
	public static String SESSIONID = "sessionid";
	public static String PHONE_NUM = "phonenum";
	/**add by pengcx 20130604 start*/
	public static String EMAIL = "email";
	/**add by pengcx 20130604 end*/
	public static String COMMAND = "command";
	public static String IS_EMULATOR = "isemulator";
	public static String IMEI = "imei";
	public static String IMSI = "imsi";
	public static String MAC = "mac";
	public static String SOFTWARE_VERSION = "softwareversion";
	public static String PHONE_SIM = "phoneSIM";
	public static String COOP_ID = "coopid";
	public static String MACHINE_ID = "machineid";
	public static String PLATFORM = "platform";
	public static String INFORMATION_TYPE = "type";
	public static String ISWHITE = "iswhite";
	public static String BANKADDRESS = "bankaddress";
	/**
	 * 发起合买
	 */
	public static String TOTALAMT = "totalAmt";
	public static String ONEAMOUNT = "oneAmount";
	public static String BUYAMT = "buyAmt";
	public static String RATION = "commisionRation";
	public static String VISIBILITY = "visibility";
	public static String MINAMT = "minAmt";
	public static String DESCRIPTION = "description";
	/**
	 * 定制跟单
	 */
	public static String STARTER_USERNO = "starterUserNo"; // 发起人用户编号
	public static String TIMES = "times"; // 跟单次数
	public static String JOIN_AMT = "joinAmt"; // 跟单金额
	public static String PERCENT = "percent"; // 跟单百分比
	public static String MAX_AMT = "maxAmt"; // 百分比跟单最大金额
	public static String SAFE_AMT = "safeAmt"; // 止损金额
	public static String JOIN_TYPE = "joinType";// 跟单类型 0:金额跟单;1:百分比跟单
	public static String FORCE_JOIN = "forceJoin";// 是否强制跟单 0:不强制跟单;1:强制跟单
	public static String MODIFY_ID = "id";
	/**
	 * 竞彩
	 */
	public static String PHONENUM = "phonenum";
	public static String BINPHONENUM = "bindPhoneNum";
	/**
	 * 找回密码
	 */
	public static String JCTYPE = "jingcaiType";
	/**
	 * Date
	 */
	public static String DATE = "date";
	public static String JCVALUETYPE = "jingcaiValueType";
	// 种类为高频彩
	public static String HIGHFREQENCYTPYE = "highFrequency";

	private static ProtocolManager protocolManager = null;

	private ProtocolManager() {

	}

	public synchronized static ProtocolManager getInstance() {
		if (protocolManager == null) {
			protocolManager = new ProtocolManager();
		}
		return protocolManager;
	}

	/**
	 * 获取除userno,sessionid,phonenum公共参数(imsi,imei,softwareversion,machineid,
	 * coopid,smscenter,platform)
	 * 
	 * @return 含有公共参数的jsonobject
	 */
	public JSONObject getDefaultJsonProtocol() {
		JSONObject defaultJsonObject = new JSONObject();
		try {
			if (Constants.IMEI == null) {
				defaultJsonObject.put(IMEI, "");
			} else {
				defaultJsonObject.put(IMEI, Constants.IMEI);
			}
			if (Constants.IMSI == null) {
				defaultJsonObject.put(IMSI, "");
			} else {
				defaultJsonObject.put(IMSI, Constants.IMSI);
			}
			if (Constants.MAC == null) {
				defaultJsonObject.put(MAC, "");
			} else {
				defaultJsonObject.put(MAC, Constants.MAC);
			}
			defaultJsonObject.put(SOFTWARE_VERSION, Constants.SOFTWARE_VERSION);
			defaultJsonObject.put(MACHINE_ID, Constants.MACHINE_ID);
			defaultJsonObject.put(COOP_ID, ChannelConstants.COOP_ID);
			defaultJsonObject.put(PHONE_SIM, Constants.PHONE_SIM);
			defaultJsonObject.put(PLATFORM, Constants.PLATFORM_ID);
			defaultJsonObject.put(ISZIP, Constants.ISCOMPRESS);
			return defaultJsonObject;
		} catch (JSONException e) {
			Log.e(TAG, "获取公共参数出错啦");
		}
		return defaultJsonObject;
	}

}
