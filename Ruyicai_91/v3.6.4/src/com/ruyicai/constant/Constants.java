package com.ruyicai.constant;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.ruyicai.activity.info.LotInfoDomain;
/**
 * 静态常量参数类
 * @author Administrator
 *
 */   
public class Constants {             	
   
	public static final String APPNAME = "RUYICAI";	
	public static  int MEMUTYPE= 0;
	
	public static final String SERVER_URL = "http://www.ruyicai.com/jrtLot/";//jrtLot正式线
//    public static final String SERVER_URL = "http://202.43.152.170:8080/jrtLot/";//测试 线

    
	public static String LOT_SERVER = "http://www.ruyicai.com/lotserver/RuyicaiServlet";//正式线
//	public static String LOT_SERVER = "http://202.43.152.170:8080/lotserver/RuyicaiServlet";//测试线
   
    /**
     * 彩种设置
     * 
     */
    public static final String CAIZHONG_OPEN = "on";
    public static final String CAIZHONG_CLOSE = "off";
    /**
     * 支付宝
     */
	public final static String server_url = "https://msp.alipay.com/x.htm";//支付宝快捷支付server_url
	public static final String ALIPAY_PLUGIN_NAME = "alipay_plugin223_0309.apk";	
	public static final String sinaweibo="https://api.weibo.com/2/users/show.json";//新浪微博获取昵称
	/**
	 * 打新包需要修改
	 */
	public static String SOFTWARE_VERSION = "3.6.4";	
	public static String MERCHANT_PACKAGE = "com.palmdream.RuyicaiAndroid91";//商户包名
	/**
	 * 新浪分享修改
	 */
	public static String CONSUMER_KEY = "2806185439";// 替换为开发者的appkey，例如"1646212960";
	public static String CONSUMER_SECRET = "f9acd48bfcb796da21f55015a2752ede";// 替换为开发者的appkey，例
	public static String CONSUMER_URL = "http://soft.sj.91.com/91lottery/index.shtml";
	/**
	 * 腾讯微博
	 */
	public static String kAppKey = "801184275";
	public static String kAppSecret = "3439c0843c81965196b165b09bb6edf3";
	public static String kAppRedirectURI = "http://www.ruyicai.com";


	
	
	
	public static boolean hasLogin = false;//用户是否已经登录标示
	public static String sessionId = "";
	public static final String KEY = "<>hj12@#$$%^~~ff";
	public static final String  NOTIFICATION_MARKS = "marks";
	public static String IMEI = "";
	public static String ISCOMPRESS = "1";//压缩请求参数值 


	public static String PHONE_SIM = "";
	public static String DRAWBALANCE = "drawbalance";
	
	public static String MACHINE_ID = "";
	public static String PLATFORM_ID = "android";
	public static boolean isProxyConnect = false;   //是否代理服务器联网
	public static String mProxyHost;               //代理服务器地址
	public static int mProxyPort = 0;              //代理服务器端口
	
	//设置开奖订阅后台联网的时间周期（单位 ms）
	public static int PRIZECIRCLETIME = 10*60*1000;
	// 登录的用户余额
	public static String LOGIN_USER_BALANCE = "";
	
	public static String type="";

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
	public static List<JSONObject> ydjList = new ArrayList<JSONObject>();
	public static List<JSONObject> twentylist = new ArrayList<JSONObject>();
	public static List<JSONObject> dlcList = new ArrayList<JSONObject>();
	public static List<JSONObject> pl5List = new ArrayList<JSONObject>();
	public static List<JSONObject> qxcList = new ArrayList<JSONObject>();
	public static List<JSONObject> gd115List = new ArrayList<JSONObject>();
	public static List<JSONObject> gd10List = new ArrayList<JSONObject>();
	public static String noticeJcz = "" ;
	public static String noticeJcl = "" ;
	
	public static  boolean isInitTop = false;//设置mainGroup是否initTop，当用户是自动登录时isInitTop = true，mainGroup界面initTop；否则……
	//开奖信息json
	public static JSONObject noticAllJson = null;
	public static JSONObject ssqJson = null;
	public static JSONObject fc3dJson =  null;
	public static JSONObject qlcJson =  null;
	public static JSONObject pl3Json =  null;
	public static JSONObject dltJson =  null;
	public static JSONObject sscJson =  null;
	public static JSONObject sfcJson =  null;
	public static JSONObject rx9Json =  null;
	public static JSONObject half6Json =  null;
	public static JSONObject jqcJson =  null;
	public static JSONObject ydjJson =  null;
	public static JSONObject twentyJson = null;
	public static JSONObject dlcJson =  null;
	public static JSONObject pl5Json =  null;
	public static JSONObject qxcJson =  null;
	public static JSONObject gd115Json =  null;
	public static JSONObject gd10Json =  null;
	
	public static JSONObject todayjosn=null;
	//用户反馈
	public static String feedBackData = "";
	public static JSONArray feedBackJSONArray = null;
	//中奖排行List
	public static   String prizeRankJSON = "";
	public static   String buydataJSON = "";

	
	//用户分享内容
	public static String shareContent = "Hi，我刚使用了如意彩手机客户端买彩票，很方便呢！" +
			"你也试试吧，彩票随身投，大奖时时有！中奖了记的要请客啊！下载地址:http://wap.ruyicai.com/w/client/download.jspx";
	
    public static String source="3";//source String	微博分享类型	可选	1:新闻;2:开奖;3:下载地址;4:合买;5:发起合买

	// 彩票资讯 List
	
	public static List<LotInfoDomain> quwenInfoList = new ArrayList<LotInfoDomain>();//彩票趣闻
	public static List<LotInfoDomain> zhuanjiaInfoList = new ArrayList<LotInfoDomain>();//专家分析
	public static List<LotInfoDomain> footballInfoList = new ArrayList<LotInfoDomain>();//足彩天地
	public static List<LotInfoDomain> huodongInfoList = new ArrayList<LotInfoDomain>();//如意活动

	//订阅信息存放在啥repreference中的标示
	public static String[] orderPrize = {"orderSSQ","orderFC3D","orderQLC","orderDLT","orderQXC","orderPL3","orderPL5","order225"};
	
	public static long lastNoticeTime = 0;
	public static final long NOTICE_CACHE_TIME_SECOND = 120;// 开奖信息缓存60秒,如果超过60秒则重新联网获取数据

	public static JSONObject currentLotnoInfo = new JSONObject();
               
	public static final String LOTNO_SSQ = "F47104"; // 双色球
	public static final String LOTNO_QLC = "F47102"; // 七乐彩
	public static final String LOTNO_FC3D = "F47103"; // 福彩3D
	public static final String LOTNO_GD115 = "T01014"; // 广东11-5
	
	
	public static final String LOTNO_eleven = "T01012"; // 11运夺金
	public static final String LOTNO_ten = "T01015"; // 快乐十分
	public static final String LOTNO_11_5 = "T01010"; // 11选5
	public static final String LOTNO_GD_11_5 = "T01014"; // 广东11选5
	public static final String LOTNO_SSC = "T01007"; // 时时彩
	public static final String LOTNO_DLT = "T01001"; // 大乐透
	public static final String LOTNO_PL3 = "T01002"; // 排列三
	public static final String LOTNO_QXC = "T01009"; // 七星彩
	public static final String LOTNO_PL5 = "T01011"; // 排列五
	public static final String LOTNO_JC= "J00001"; // 竞彩

	public static final String LOTNO_ZC = "ZC"; // 进球彩
	public static final String LOTNO_JQC = "T01005"; // 进球彩
	public static final String LOTNO_LCB = "T01006"; // 足彩六场半
	public static final String LOTNO_SFC = "T01003"; // 足彩胜负彩
	public static final String LOTNO_RX9 = "T01004"; // 足彩任选9
    public static final String LOTNO_22_5= "T01013"; //体彩22选5
	public static final String LOTNO_JCZQ = "J00001"; // 竞彩足球胜负
	public static final String LOTNO_JCZQ_ZQJ = "J00003"; // 竞彩足球总进球
	public static final String LOTNO_JCZQ_BF = "J00002"; // 竞彩足球比分
	public static final String LOTNO_JCZQ_BQC = "J00004"; // 竞彩足球半全场
	public static final String LOTNO_JCLQ = "J00005"; // 竞彩篮球胜负
	public static final String LOTNO_JCLQ_RF = "J00006"; // 竞彩篮球让分胜负
	public static final String LOTNO_JCLQ_SFC = "J00007"; // 竞彩篮球胜分差
	public static final String LOTNO_JCLQ_DXF = "J00008"; // 竞彩篮球大小分
	public static final String LOTNO_JCL = "JC_L";//合买查询中查询所有竞彩篮球的标示
	public static final String LOTNO_JCZ = "JC_Z";//合买查询中查询所有竞彩足球的标示
	
	public static final int SSC_TWOSTAR_ZHIXUAN=1;
	public static final int SSC_TWOSTAR_ZUXUAN=2;
	public static final int SSC_TWOSTAR_HEZHI=3;
	public static final int SSC_FIVESTAR_ZHIXUAN=4;
	public static final int SSC_FIVESTAR_TONGXUAN=5;
	
	/**
	 * 中奖排行的一些标示
	 */
	public static final String  WEEK = "weekArray";
	public static final String  MONTH = "monthArray";
	public static final String  TOTAL = "totalArray";
	
	public static final String SSQLABEL = "ssq";
	public static final String FC3DLABEL = "fc3d";
	public static final String QLCLABEL = "qlc";
	public static final String PL3LABEL = "pl3";
	public static final String PL5LABEL = "pl5";
	public static final String QXCLABEL = "qxc";
	public static final String DLTLABEL = "cjdlt";
	public static final String SSCLABEL = "ssc";
	public static final String DLCLABEL = "11-5";
	public static final String YDJLABEL = "11-ydj";
	public static final String TWENTYBEL ="22-5";
	public static final String SFCLABEL = "sfc";
	public static final String RXJLABEL = "rxj";
	public static final String LCBLABEL = "lcb";
	public static final String GD115 = "gd115";
	
	/**
	 * 竞彩足球TYpe标示
	 */
	public static final String JCFOOT = "1";
	/**
	 * 竞彩篮球Type标示
	 */
	public static final String JCBASKET = "0";
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
	public static String IMSI;
	public static String MAC;
	
	public static final String SUCCESS_CODE="0000";
}
