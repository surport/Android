package com.ruyicai.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.ruyicai.activity.info.LotInfoDomain;

import android.graphics.Bitmap;
/**
 * 静态常量参数类
 * @author Administrator
 *
 */
public class Constants {             
   
	public static final String APPNAME = "RUYICAI";	
	public static  int MEMUTYPE= 0;
//    public static final String SERVER_URL = "http://219.148.162.70:8000/jrtLot/";  
	public static final String SERVER_URL = "http://www.ruyicai.com/jrtLot/";
//	public static final String SERVER_URL = "http://192.168.0.114:8000/lotserver";
	public static String LOT_SERVER = "http://www.ruyicai.com/lotserver/RuyicaiServlet";
//	public static String LOT_SERVER = "http://123.125.150.108/lotserver/RuyicaiServlet";
//	public static String LOT_SERVER = "http://219.148.162.70:8000/lotserver/RuyicaiServlet";
//	public static String LOT_SERVER = "http://192.168.0.114:8000/lotserver/RuyicaiServlet";//金龙
	public static String sessionId = "";
	public static final String KEY = "<>hj12@#$$%^~~ff";
	public static final String  NOTIFICATION_MARKS = "marks";
	public static String IMEI = "";
	public static String IMSI = "";
	public static String ISCOMPRESS = "1";//压缩请求参数值 
	public static String SOFTWARE_VERSION = "2.7";	
	public static String SMS_CENTER = "";
	public static String DRAWBALANCE = "drawbalance";
	public static String SHELLRWPHONE = "phonenum";//sharepreference中的电话号码
	public static String SHELLRWSESSIONID = "sessionid";//sharepreference中的sessionid
	public static String SHELLRWUSERNO = "userno";//sharepreference中的userno
//	public static String COOP_ID = "304";//n多网
//	public static String COOP_ID = "545";//n多网
//	public static String COOP_ID = "232";//tompda
//	public static String COOP_ID = "208";//优亿市场
//	public static String COOP_ID = "497";//历趣
//	public static String COOP_ID = "373";//当乐
//	public static String COOP_ID = "238";//泡椒
//	public static String COOP_ID = "383";//软吧
//	public static String COOP_ID = "166";//机锋网
//	public static String COOP_ID = "151";//安卓market
//	public static String COOP_ID = "543";//搜狐
//	public static String COOP_ID = "625";//安丰网
//	public static String COOP_ID = "279";//掌上应用汇	
//	public static String COOP_ID = "370";//木蚂蚁
//	public static String COOP_ID = "450";//coolmart
//	public static String COOP_ID = "184";//欧了吧
//	public static String COOP_ID = "643";//anzhi
//	public static String COOP_ID = "535";//qqtencent
//	public static String COOP_ID = "534";//wab官网
//	public static String COOP_ID = "533";//wap官网
//	public static String COOP_ID = "537";//官网升级
//	public static String COOP_ID = "232";//tompda
//	public static String COOP_ID = "208";//eoe
//	public static String COOP_ID = "497";//liqu
//	public static String COOP_ID = "373";//dangle
//	public static String COOP_ID = "238";//泡椒
//	public static String COOP_ID = "383";//softbar
//	public static String COOP_ID = "370";//mumayi
//	public static String COOP_ID = "279";//zhangshang
//	public static String COOP_ID = "625";//anfeng
//	public static String COOP_ID = "543";//sohu
//	public static String COOP_ID = "166";//gfan
//	public static String COOP_ID = "561";//feiliu
//	public static String COOP_ID = "641";//安卓市场
//	public static String COOP_ID = "293";//奇虎360
//	public static String COOP_ID = "640";//eoe
//	public static String COOP_ID = "291";//安智网
//	public static String COOP_ID = "278";//掌软
//	public static String COOP_ID = "503";//jrtLot旧版
//	public static String COOP_ID = "371";//魅族
//	public static String COOP_ID = "639";//安机市场
//	public static String COOP_ID = "648";//能助手
//	public static String COOP_ID = "554";//moto平台
//	public static String COOP_ID = "647";//安卓星空
//	public static String COOP_ID = "644";//大汉精品
//	public static String COOP_ID = "646";//360
//	public static String COOP_ID = "679";//wandoujia
//	public static String COOP_ID = "652";//开奇网
//	public static String COOP_ID = "655";//多么市场
//	public static String COOP_ID = "685";//棒棒
//	public static String COOP_ID = "692";//墨迹天气
//	public static String COOP_ID = "677";//opera
//	public static String COOP_ID = "664";//安机市场
//	public static String COOP_ID = "642";//宝软
//	public static String COOP_ID = "693";//摩秀
//	public static String COOP_ID = "690";//星空晨
//	public static String COOP_ID = "689";//时代
//	public static String COOP_ID = "676";//华为智慧云
//	public static String COOP_ID = "659";//公信市场
//	public static String COOP_ID = "658";//酷玩
//	public static String COOP_ID = "698";//有米无线
	public static String COOP_ID = "699";//有米无线2
	
	public static String MACHINE_ID = "";
	public static String PLATFORM_ID = "android";
	
	public static boolean isProxyConnect = false;   //是否代理服务器联网
	public static String mProxyHost;               //代理服务器地址
	public static int mProxyPort = 0;              //代理服务器端口
	
	
	// 登录的用户余额
	public static String LOGIN_USER_BALANCE = "";

	// 开奖信息list
	public static List<JSONObject> ssqNoticeList = new ArrayList<JSONObject>();
	public static List<JSONObject> fc3dList = new ArrayList<JSONObject>();
	public static List<JSONObject> qlcList = new ArrayList<JSONObject>();
	public static List<JSONObject> pl3List = new ArrayList<JSONObject>();
	public static List<JSONObject> dltList = new ArrayList<JSONObject>();
	public static List<JSONObject> sscList = new ArrayList<JSONObject>();
	public static List<JSONObject> sfcList = new ArrayList<JSONObject>();
	public static List<JSONObject> rx9List = new ArrayList<JSONObject>();
	public static List<JSONObject> half6List = new ArrayList<JSONObject>();
	public static List<JSONObject> jqcList = new ArrayList<JSONObject>();
	public static List<JSONObject> dlcList = new ArrayList<JSONObject>();
	public static List<JSONObject> pl5List = new ArrayList<JSONObject>();
	public static List<JSONObject> qxcList = new ArrayList<JSONObject>();
	public static String noticeJc = "" ;

	// 彩票资讯 List
	
	public static List<LotInfoDomain> quwenInfoList = new ArrayList<LotInfoDomain>();//彩票趣闻
	public static List<LotInfoDomain> zhuanjiaInfoList = new ArrayList<LotInfoDomain>();//专家分析
	public static List<LotInfoDomain> footballInfoList = new ArrayList<LotInfoDomain>();//足彩天地
	public static List<LotInfoDomain> huodongInfoList = new ArrayList<LotInfoDomain>();//如意活动
	
	
	public static long lastNoticeTime = 0;
	public static final long NOTICE_CACHE_TIME_SECOND = 120;// 开奖信息缓存60秒,如果超过60秒则重新联网获取数据

	public static JSONObject currentLotnoInfo = new JSONObject();
               
	public static final String LOTNO_SSQ = "F47104"; // 双色球
	public static final String LOTNO_QLC = "F47102"; // 七乐彩
	public static final String LOTNO_FC3D = "F47103"; // 福彩3D

	public static final String LOTNO_11_5 = "T01010"; // 11选5
	public static final String LOTNO_SSC = "T01007"; // 实时彩
	public static final String LOTNO_DLT = "T01001"; // 大乐透
	public static final String LOTNO_PL3 = "T01002"; // 排列三
	public static final String LOTNO_QXC = "T01009"; // 七星彩
	public static final String LOTNO_PL5 = "T01011"; // 排列五
	public static final String LOTNO_JC= "J00001"; // 竞彩

	public static final String LOTNO_JQC = "T01005"; // 进球彩
	public static final String LOTNO_LCB = "T01006"; // 足彩六场半
	public static final String LOTNO_SFC = "T01003"; // 足彩胜负彩
	public static final String LOTNO_RX9 = "T01004"; // 足彩任选9
	/*
	 * 中獎排行的一些标示
	 */
	/**
	 * 滑动试图的初始化值
	 */
	public static final int FlingGalleryDefaultPosition = 0;
	public static final String  WEEK = "weekArray";
	public static final String  MONTH = "monthArray";
	public static final String  TOTAL = "totalArray";
	
	public static final int SSC_TWOSTAR_ZHIXUAN=1;
	public static final int SSC_TWOSTAR_ZUXUAN=2;
	public static final int SSC_TWOSTAR_HEZHI=3;
	public static final int SSC_FIVESTAR_ZHIXUAN=4;
	public static final int SSC_FIVESTAR_TONGXUAN=5;

	public static String NEWS = ""; // 客户端首页提示语信息

	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;
    public static int SCREEN_DENSITYDPI=0;
    public static float SCREEN_DENSITY=0f;

	public static final String GAME_CLASS = "RUYICAI_GAME_CLASS";

	public static final String GAME_CLICK_SUM = "GAME_CLICK_SUM";

	public static final int STAT_INFO_CACHE_NUM = 10;
	
	public static final String ACCOUNTRECHARGE_EIXT_TYPE="ACCOUNTRECHARGE_EIXT_TYPE_CHANGE";
	
	public static final int  RUYIHELPERSHOWLISTTYPE=0;
	
	public static Bitmap grey = null;
	public static Bitmap red = null;
	public static Bitmap blue = null;
	public static int PADDING= 40;
	public static String avdiceStr[] = null;
	
}
