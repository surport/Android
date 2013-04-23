package com.ruyicai.activity.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;

/**
 * 为开奖信息提供数据
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
	public final static String TRYCODE="tryCode";
	
	
	/**
	 * 主列表中相应的数据
	 */
	protected static List<Map<String, Object>> getListForMainListViewSimpleAdapter() {
		// 添加：排列三、超级大乐透
		
		

		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();


		try {
			// zlm 7.16 代码修改：添加开奖日期
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[0]);
			map.put(WINNINGNUM, Constants.ssqJson.get("winCode"));
			map.put(DATE, Constants.ssqJson.getString("openTime"));
			map.put(ISSUE, Constants.ssqJson.get("batchCode"));
			list.add(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			// zlm 7.16 代码修改：添加开奖日期
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[1]);
			map.put(WINNINGNUM, Constants.fc3dJson.get("winCode"));
			map.put(DATE, Constants.fc3dJson.getString("openTime"));
			map.put(ISSUE, Constants.fc3dJson.get("batchCode"));
			map.put(TRYCODE, Constants.fc3dJson.get("tryCode"));
			list.add(map);
		} catch (JSONException e) {

		}

		try {
			// zlm 7.16 代码修改：添加开奖日期
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[2]);
			map.put(WINNINGNUM, Constants.qlcJson.get("winCode"));
			map.put(DATE, Constants.qlcJson.getString("openTime"));
			map.put(ISSUE, Constants.qlcJson.get("batchCode"));
			list.add(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			// 8.9 添加：超级大乐透
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[3]);
			map.put(WINNINGNUM, Constants.dltJson.get("winCode"));
			map.put(DATE, Constants.dltJson.getString("openTime"));
			map.put(ISSUE, Constants.dltJson.get("batchCode"));
			list.add(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			// 8.9 添加：排列三
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[4]);
			map.put(WINNINGNUM, Constants.pl3Json.get("winCode"));
			map.put(DATE, Constants.pl3Json.getString("openTime"));
			map.put(ISSUE, Constants.pl3Json.get("batchCode"));
			list.add(map);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			// 添加：排列五
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[5]);
			map.put(WINNINGNUM, Constants.pl5Json.get("winCode"));
			map.put(DATE, Constants.pl5Json.getString("openTime"));
			map.put(ISSUE, Constants.pl5Json.get("batchCode"));
			list.add(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			// 添加：七星彩
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[6]);
			map.put(WINNINGNUM, Constants.qxcJson.get("winCode"));
			map.put(DATE, Constants.qxcJson.getString("openTime"));
			map.put(ISSUE, Constants.qxcJson.get("batchCode"));
			list.add(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
		try {
			// 添加：时时彩
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[8]);
			map.put(WINNINGNUM, Constants.sscJson.get("winCode"));
			map.put(DATE, Constants.sscJson.getString("openTime"));
			map.put(ISSUE, Constants.sscJson.get("batchCode"));
			list.add(map);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			// 添加: 11-5
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[9]);
			map.put(WINNINGNUM, Constants.dlcJson.get("winCode"));
			map.put(DATE, Constants.dlcJson.getString("openTime"));
			map.put(ISSUE, Constants.dlcJson.get("batchCode"));
			list.add(map);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			// 添加: 11运夺金
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[10]);
			map.put(WINNINGNUM, Constants.ydjJson.get("winCode"));
			map.put(DATE, Constants.ydjJson.getString("openTime"));
			map.put(ISSUE, Constants.ydjJson.get("batchCode"));
			list.add(map);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		try {
			// 添加：胜负彩
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[11]);
			map.put(WINNINGNUM, Constants.sfcJson.get("winCode"));
			map.put(DATE, Constants.sfcJson.getString("openTime"));
			map.put(ISSUE, Constants.sfcJson.get("batchCode"));
			list.add(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			// 添加：任选九彩
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[12]);
			map.put(WINNINGNUM, Constants.rx9Json.get("winCode"));
			map.put(DATE, Constants.rx9Json.getString("openTime"));
			map.put(ISSUE, Constants.rx9Json.get("batchCode"));
			list.add(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			// 添加：六场半
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[13]);
			map.put(WINNINGNUM, Constants.half6Json.get("winCode"));
			map.put(DATE, Constants.half6Json.getString("openTime"));
			map.put(ISSUE, Constants.half6Json.get("batchCode"));
			list.add(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try {
			// 添加：进球彩
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[14]);
			map.put(WINNINGNUM, Constants.jqcJson.get("winCode"));
			map.put(DATE, Constants.jqcJson.getString("openTime"));
			map.put(ISSUE, Constants.jqcJson.get("batchCode"));
			list.add(map);
		} catch (JSONException e) {
//			e.printStackTrace();
		}
		try {
			// 添加：竞彩足球
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[15]);
			list.add(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// 添加：竞彩篮球
			map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, NoticeMainActivity.iGameName[16]);
			list.add(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	

}
