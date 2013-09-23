package com.ruyicai.activity.buy.beijing.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 北京单场上下单双对阵信息类
 * 
 * @author Administrator
 * 
 */
public class UpDownSingleDoubleAgainstInformation extends AgainstInformation {
	/** 上单 */
	private String sxds_v1;
	/** 上双 */
	private String sxds_v2;
	/** 下单 */
	private String sxds_v3;
	/** 下双 */
	private String sxds_v4;

	/** 上单是否选中 */
	private boolean v1IsClick;
	/** 上双是否选中 */
	private boolean v2IsClick;
	/** 下单是否选中 */
	private boolean v3IsClick;
	/** 下双是否选中 */
	private boolean v4IsClick;
	private boolean isDan;

	public String getSxds_v1() {
		return sxds_v1;
	}

	public void setSxds_v1(String sxds_v1) {
		this.sxds_v1 = sxds_v1;
	}

	public String getSxds_v2() {
		return sxds_v2;
	}

	public void setSxds_v2(String sxds_v2) {
		this.sxds_v2 = sxds_v2;
	}

	public String getSxds_v3() {
		return sxds_v3;
	}

	public void setSxds_v3(String sxds_v3) {
		this.sxds_v3 = sxds_v3;
	}

	public String getSxds_v4() {
		return sxds_v4;
	}

	public void setSxds_v4(String sxds_v4) {
		this.sxds_v4 = sxds_v4;
	}

	public boolean isV1IsClick() {
		return v1IsClick;
	}

	public void setV1IsClick(boolean v1IsClick) {
		this.v1IsClick = v1IsClick;
	}

	public boolean isV2IsClick() {
		return v2IsClick;
	}

	public void setV2IsClick(boolean v2IsClick) {
		this.v2IsClick = v2IsClick;
	}

	public boolean isV3IsClick() {
		return v3IsClick;
	}

	public void setV3IsClick(boolean v3IsClick) {
		this.v3IsClick = v3IsClick;
	}

	public boolean isV4IsClick() {
		return v4IsClick;
	}

	public void setV4IsClick(boolean v4IsClick) {
		this.v4IsClick = v4IsClick;
	}

	public boolean isDan() {
		return isDan;
	}

	public void setDan(boolean isDan) {
		this.isDan = isDan;
	}

	public UpDownSingleDoubleAgainstInformation clone() {
		UpDownSingleDoubleAgainstInformation upDownSingleDoubleAgainstInformation = null;

		try {
			upDownSingleDoubleAgainstInformation = (UpDownSingleDoubleAgainstInformation) super
					.clone();
			upDownSingleDoubleAgainstInformation.setSxds_v1(getSxds_v1());
			upDownSingleDoubleAgainstInformation.setSxds_v2(getSxds_v2());
			upDownSingleDoubleAgainstInformation.setSxds_v3(getSxds_v3());
			upDownSingleDoubleAgainstInformation.setSxds_v4(getSxds_v4());
			upDownSingleDoubleAgainstInformation.setV1IsClick(isV1IsClick());
			upDownSingleDoubleAgainstInformation.setV2IsClick(isV2IsClick());
			upDownSingleDoubleAgainstInformation.setV3IsClick(isV3IsClick());
			upDownSingleDoubleAgainstInformation.setV4IsClick(isV4IsClick());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return upDownSingleDoubleAgainstInformation;
	}

	public boolean isSelected() {
		return (v1IsClick || v2IsClick || v3IsClick || v4IsClick);
	}

	public int getClickNum() {
		int clickNum = 0;
		if (v1IsClick) {
			clickNum++;
		}

		if (v2IsClick) {
			clickNum++;
		}

		if (v3IsClick) {
			clickNum++;
		}

		if (v4IsClick) {
			clickNum++;
		}

		return clickNum;
	}

	public static List<List<UpDownSingleDoubleAgainstInformation>> analysisUpDownSingleDoubleAgainstInformation(
			JSONArray againstJsonArray) throws JSONException {
		List<List<UpDownSingleDoubleAgainstInformation>> upDownSigleDoubleagainstInformationList = new ArrayList<List<UpDownSingleDoubleAgainstInformation>>();

		int jsonArrayLength = againstJsonArray.length();
		for (int jsonarray_i = 0; jsonarray_i < jsonArrayLength; jsonarray_i++) {
			List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations = new ArrayList<UpDownSingleDoubleAgainstInformation>();
			JSONArray againstWithDataJsonArray = againstJsonArray
					.getJSONArray(jsonarray_i);
			int againstWithDataLength = againstWithDataJsonArray.length();

			for (int json_i = 0; json_i < againstWithDataLength; json_i++) {
				JSONObject againstJsonObject = againstWithDataJsonArray
						.getJSONObject(json_i);

				UpDownSingleDoubleAgainstInformation againstInformation = new UpDownSingleDoubleAgainstInformation();
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
				againstInformation.setSxds_v1(againstJsonObject
						.getString("sxds_v1"));
				againstInformation.setSxds_v2(againstJsonObject
						.getString("sxds_v2"));
				againstInformation.setSxds_v3(againstJsonObject
						.getString("sxds_v3"));
				againstInformation.setSxds_v4(againstJsonObject
						.getString("sxds_v4"));

				upDownSingleDoubleAgainstInformations.add(againstInformation);
			}
			upDownSigleDoubleagainstInformationList
					.add(upDownSingleDoubleAgainstInformations);
		}
		return upDownSigleDoubleagainstInformationList;
	}
}
