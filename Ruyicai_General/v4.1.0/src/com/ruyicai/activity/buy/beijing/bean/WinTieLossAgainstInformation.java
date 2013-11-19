package com.ruyicai.activity.buy.beijing.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 北京单场胜平负对阵信息类：存储了对阵信息和赛事投注选择的信息
 * 
 * @author Administrator
 * 
 */
public class WinTieLossAgainstInformation extends AgainstInformation {
	/** 胜 */
	private String v0;
	/** 平 */
	private String v1;
	/** 负 */
	private String v3;

	/** 是否选择胜标识 */
	private boolean v0IsClick = false;
	/** 是否选择平标识 */
	private boolean v1IsClick = false;
	/** 是否选择负标识 */
	private boolean v3IsClick = false;
	/**是否选择胆*/
	private boolean isDan;

	private String letPoint;

	public String getV0() {
		return v0;
	}

	public void setV0(String v0) {
		this.v0 = v0;
	}

	public String getV1() {
		return v1;
	}

	public void setV1(String v1) {
		this.v1 = v1;
	}

	public String getV3() {
		return v3;
	}

	public void setV3(String v3) {
		this.v3 = v3;
	}

	public boolean isV0IsClick() {
		return v0IsClick;
	}

	public void setV0IsClick(boolean v0IsClick) {
		this.v0IsClick = v0IsClick;
	}

	public boolean isV1IsClick() {
		return v1IsClick;
	}

	public void setV1IsClick(boolean v1IsClick) {
		this.v1IsClick = v1IsClick;
	}

	public boolean isV3IsClick() {
		return v3IsClick;
	}

	public void setV3IsClick(boolean v3IsClick) {
		this.v3IsClick = v3IsClick;
	}

	public String getLetPoint() {
		return letPoint;
	}

	public void setLetPoint(String letPoint) {
		this.letPoint = letPoint;
	}

	public boolean isDan() {
		return isDan;
	}

	public void setDan(boolean isDan) {
		this.isDan = isDan;
	}

	/**
	 * 克隆胜平负对象
	 */
	public WinTieLossAgainstInformation clone() {
		WinTieLossAgainstInformation winTieLossAgainstInformation = null;

		try {
			winTieLossAgainstInformation = (WinTieLossAgainstInformation) super
					.clone();
			winTieLossAgainstInformation.setV0(getV0());
			winTieLossAgainstInformation.setV1(getV1());
			winTieLossAgainstInformation.setV3(getV3());
			winTieLossAgainstInformation.setV0IsClick(isV0IsClick());
			winTieLossAgainstInformation.setV1IsClick(isV1IsClick());
			winTieLossAgainstInformation.setV3IsClick(isV3IsClick());
			winTieLossAgainstInformation.setLetPoint(getLetPoint());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return winTieLossAgainstInformation;
	}

	/**
	 * 当前对阵赛事是否被选中
	 * 
	 * @return 是否被选中标识
	 */
	public boolean isSelected() {
		return (v0IsClick || v1IsClick || v3IsClick);
	}

	/**
	 * 获取当前对阵赛事投注的数量
	 * 
	 * @return 投注数量
	 */
	public int getClickNum() {
		int clickNum = 0;
		if (v0IsClick) {
			clickNum++;
		}

		if (v1IsClick) {
			clickNum++;
		}

		if (v3IsClick) {
			clickNum++;
		}

		return clickNum;
	}

	/**
	 * 解析让球胜平负对阵信息
	 * 
	 * @param againstJsonArray
	 *            对阵信息Json数组
	 * @return 对阵信息集合
	 * @throws JSONException
	 *             Json异常对象
	 */
	public static List<List<WinTieLossAgainstInformation>> analysisWinTieLossAgainstInformation(
			JSONArray againstJsonArray) throws JSONException {

		List<List<WinTieLossAgainstInformation>> winTieLossAgainstInformationList = new ArrayList<List<WinTieLossAgainstInformation>>();

		int jsonArrayLength = againstJsonArray.length();
		for (int jsonarray_i = 0; jsonarray_i < jsonArrayLength; jsonarray_i++) {
			List<WinTieLossAgainstInformation> winTieLossAgainstInformations = new ArrayList<WinTieLossAgainstInformation>();

			JSONArray againstWithDataJsonArray = againstJsonArray
					.getJSONArray(jsonarray_i);
			int againstWithDataLength = againstWithDataJsonArray.length();

			for (int json_i = 0; json_i < againstWithDataLength; json_i++) {
				JSONObject againstJsonObject = againstWithDataJsonArray
						.getJSONObject(json_i);

				WinTieLossAgainstInformation againstInformation = new WinTieLossAgainstInformation();
				againstInformation.setTeamId(againstJsonObject
						.getString("teamId"));
				againstInformation.setDayForamt(againstJsonObject
						.getString("dayForamt"));
				againstInformation.setHomeTeam(againstJsonObject
						.getString("homeTeam"));
				againstInformation.setGuestTeam(againstJsonObject
						.getString("guestTeam"));
				againstInformation.setLeague(againstJsonObject
						.getString("league"));
				againstInformation.setEndTime(againstJsonObject
						.getString("endTime"));
				againstInformation.setV0(againstJsonObject.getString("v0"));
				againstInformation.setV1(againstJsonObject.getString("v1"));
				againstInformation.setV3(againstJsonObject.getString("v3"));
				againstInformation.setLetPoint(againstJsonObject
						.getString("letPoint"));

				winTieLossAgainstInformations.add(againstInformation);
			}

			winTieLossAgainstInformationList.add(winTieLossAgainstInformations);
		}

		return winTieLossAgainstInformationList;
	}
}
