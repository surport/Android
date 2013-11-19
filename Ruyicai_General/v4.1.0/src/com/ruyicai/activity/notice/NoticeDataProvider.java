package com.ruyicai.activity.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import android.content.Context;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 为开奖信息提供数据
 * 
 * @author haojie
 * 
 */
public class NoticeDataProvider {

	public final static String LOTTERYTYPE = "LOTTERYTYPE";
	public final static String WINNINGNUM = "WINNINGNUM";
	public final static String DATE = "DATA";
	public final static String ISSUE = "ISSUE";
	public final static String FINALDATE = "FINALDATE";
	public final static String MONEYSUM = "MONEYSUM";
	public final static String TRYCODE = "tryCode";
	private Context mContext;

	/**
	 * 主列表中相应的数据
	 */
	protected static List<Map<String, Object>> getListForMainListViewSimpleAdapter(
			Context context) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 应该在这里判断彩种--每个都需要判断的
		RWSharedPreferences shellRW = new RWSharedPreferences(context,
				ShellRWConstants.CAIZHONGSETTING);
		// zlm 7.16 代码修改：添加开奖日期---双色球
		if (shellRW.getStringValue("ssq").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[0]);
				map.put(WINNINGNUM, Constants.ssqJson.get("winCode"));
				map.put(DATE, Constants.ssqJson.getString("openTime"));
				map.put(ISSUE, Constants.ssqJson.get("batchCode"));
				list.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// zlm 7.16 代码修改：添加开奖日期---福彩3D
		if (shellRW.getStringValue("fc3d").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[1]);
				map.put(WINNINGNUM, Constants.fc3dJson.get("winCode"));
				map.put(DATE, Constants.fc3dJson.getString("openTime"));
				map.put(ISSUE, Constants.fc3dJson.get("batchCode"));
				map.put(TRYCODE, Constants.fc3dJson.get("tryCode"));
				list.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// zlm 7.16 代码修改：添加开奖日期---七乐彩
		if (shellRW.getStringValue("qlc").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[2]);
				map.put(WINNINGNUM, Constants.qlcJson.get("winCode"));
				map.put(DATE, Constants.qlcJson.getString("openTime"));
				map.put(ISSUE, Constants.qlcJson.get("batchCode"));
				list.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// 8.9 添加：超级大乐透
		if (shellRW.getStringValue("cjdlt").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[3]);
				map.put(WINNINGNUM, Constants.dltJson.get("winCode"));
				map.put(DATE, Constants.dltJson.getString("openTime"));
				map.put(ISSUE, Constants.dltJson.get("batchCode"));
				list.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// 8.9 添加：排列三
		if (shellRW.getStringValue("pl3").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[4]);
				map.put(WINNINGNUM, Constants.pl3Json.get("winCode"));
				map.put(DATE, Constants.pl3Json.getString("openTime"));
				map.put(ISSUE, Constants.pl3Json.get("batchCode"));
				list.add(map);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		// 添加：排列五
		if (shellRW.getStringValue("pl5").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[5]);
				map.put(WINNINGNUM, Constants.pl5Json.get("winCode"));
				map.put(DATE, Constants.pl5Json.getString("openTime"));
				map.put(ISSUE, Constants.pl5Json.get("batchCode"));
				list.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// 添加：七星彩
		if (shellRW.getStringValue("qxc").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[6]);
				map.put(WINNINGNUM, Constants.qxcJson.get("winCode"));
				map.put(DATE, Constants.qxcJson.getString("openTime"));
				map.put(ISSUE, Constants.qxcJson.get("batchCode"));
				list.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (shellRW.getStringValue("22-5").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				// 添加: 22选5
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[7]);
				map.put(WINNINGNUM, Constants.twentyJson.get("winCode"));
				map.put(DATE, Constants.twentyJson.getString("openTime"));
				map.put(ISSUE, Constants.twentyJson.get("batchCode"));
				list.add(map);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		// 添加：时时彩
		if (shellRW.getStringValue("ssc").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[8]);
				map.put(WINNINGNUM, Constants.sscJson.get("winCode"));
				map.put(DATE, Constants.sscJson.getString("openTime"));
				map.put(ISSUE, Constants.sscJson.get("batchCode"));
				list.add(map);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		// 添加: 11-5
		if (shellRW.getStringValue("11-5").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[9]);
				map.put(WINNINGNUM, Constants.dlcJson.get("winCode"));
				map.put(DATE, Constants.dlcJson.getString("openTime"));
				map.put(ISSUE, Constants.dlcJson.get("batchCode"));
				list.add(map);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		// 添加: 11运夺金
		if (shellRW.getStringValue("11-ydj").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[10]);
				map.put(WINNINGNUM, Constants.ydjJson.get("winCode"));
				map.put(DATE, Constants.ydjJson.getString("openTime"));
				map.put(ISSUE, Constants.ydjJson.get("batchCode"));
				list.add(map);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		// 添加：广东11-5
		if (shellRW.getStringValue("gd-11-5").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[11]);
				map.put(WINNINGNUM, Constants.gd115Json.get("winCode"));
				map.put(DATE, Constants.gd115Json.getString("openTime"));
				map.put(ISSUE, Constants.gd115Json.get("batchCode"));
				list.add(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (shellRW.getStringValue("gd-10").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				// 添加：广东快乐十分
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[15]);
				map.put(WINNINGNUM, Constants.gd10Json.get("winCode"));
				map.put(DATE, Constants.gd10Json.getString("openTime"));
				map.put(ISSUE, Constants.gd10Json.get("batchCode"));
				list.add(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 添加：内蒙快单
		if (shellRW.getStringValue("nmk3").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[16]);
				map.put(WINNINGNUM, Constants.nmk3Json.get("winCode"));
				map.put(DATE, Constants.nmk3Json.getString("openTime"));
				map.put(ISSUE, Constants.nmk3Json.get("batchCode"));
				list.add(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 添加：重庆11选5
		if (shellRW.getStringValue("cq-11-5").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, "cq-11-5");
				map.put(WINNINGNUM, Constants.cq11x5Json.get("winCode"));
				map.put(DATE, Constants.cq11x5Json.getString("openTime"));
				map.put(ISSUE, Constants.cq11x5Json.get("batchCode"));
				list.add(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 四种玩法 胜负彩/任选9/六场半/进球彩 合成足彩只要在彩种设置里关闭足彩 下面四种就都不显示了
		if (shellRW.getStringValue("zc").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			// 添加：胜负彩
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[12]);
				map.put(WINNINGNUM, Constants.sfcJson.get("winCode"));
				map.put(DATE, Constants.sfcJson.getString("openTime"));
				map.put(ISSUE, Constants.sfcJson.get("batchCode"));
				list.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		// 添加：竞彩足球
		if (shellRW.getStringValue("jcz").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[13]);
				list.add(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 添加：竞彩篮球
		if (shellRW.getStringValue("jcl").toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[14]);
				list.add(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**add by yejc 20130506 start*/
		if (shellRW.getStringValue(NoticeMainActivity.iGameName[17]).toString()
				.equals(Constants.CAIZHONG_OPEN)) {
			try {
				map = new HashMap<String, Object>();
				map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[17]);
				list.add(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**add by yejc 20130506 end*/
		
		return list;
	}
}
