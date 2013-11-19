package com.ruyicai.activity.buy.beijing.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 北京单场半全场对阵信息类
 * 
 * @author Administrator
 * 
 */
public class HalfTheAudienceAgainstInformation extends AgainstInformation {
	private String half_v00;
	private String half_v01;
	private String half_v03;
	private String half_v11;
	private String half_v10;
	private String half_v13;
	private String half_v33;
	private String half_v31;
	private String half_v30;
	private boolean isDan;

	private boolean[] isClicks = { false, false, false, false, false, false,
			false, false, false };

	public String getHalf_v00() {
		return half_v00;
	}

	public void setHalf_v00(String half_v00) {
		this.half_v00 = half_v00;
	}

	public String getHalf_v01() {
		return half_v01;
	}

	public void setHalf_v01(String half_v01) {
		this.half_v01 = half_v01;
	}

	public String getHalf_v03() {
		return half_v03;
	}

	public void setHalf_v03(String half_v03) {
		this.half_v03 = half_v03;
	}

	public String getHalf_v11() {
		return half_v11;
	}

	public void setHalf_v11(String half_v11) {
		this.half_v11 = half_v11;
	}

	public String getHalf_v10() {
		return half_v10;
	}

	public void setHalf_v10(String half_v10) {
		this.half_v10 = half_v10;
	}

	public String getHalf_v13() {
		return half_v13;
	}

	public void setHalf_v13(String half_v13) {
		this.half_v13 = half_v13;
	}

	public String getHalf_v33() {
		return half_v33;
	}

	public void setHalf_v33(String half_v33) {
		this.half_v33 = half_v33;
	}

	public String getHalf_v31() {
		return half_v31;
	}

	public void setHalf_v31(String half_v31) {
		this.half_v31 = half_v31;
	}

	public String getHalf_v30() {
		return half_v30;
	}

	public void setHalf_v30(String half_v30) {
		this.half_v30 = half_v30;
	}

	public boolean[] getIsClicks() {
		return isClicks;
	}

	public void setIsClicks(boolean[] isClicks) {
		this.isClicks = isClicks;
	}

	public boolean isDan() {
		return isDan;
	}

	public void setDan(boolean isDan) {
		this.isDan = isDan;
	}

	public HalfTheAudienceAgainstInformation clone() {
		HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation = null;

		try {
			halfTheAudienceAgainstInformation = (HalfTheAudienceAgainstInformation) super
					.clone();
			halfTheAudienceAgainstInformation.setHalf_v00(getHalf_v00());
			halfTheAudienceAgainstInformation.setHalf_v01(getHalf_v01());
			halfTheAudienceAgainstInformation.setHalf_v03(getHalf_v03());
			halfTheAudienceAgainstInformation.setHalf_v11(getHalf_v11());
			halfTheAudienceAgainstInformation.setHalf_v10(getHalf_v10());
			halfTheAudienceAgainstInformation.setHalf_v13(getHalf_v13());
			halfTheAudienceAgainstInformation.setHalf_v33(getHalf_v33());
			halfTheAudienceAgainstInformation.setHalf_v31(getHalf_v31());
			halfTheAudienceAgainstInformation.setHalf_v30(getHalf_v30());

			boolean[] copyClicks = new boolean[getIsClicks().length];
			System.arraycopy(getIsClicks(), 0, copyClicks, 0,
					getIsClicks().length);
			halfTheAudienceAgainstInformation.setIsClicks(copyClicks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return halfTheAudienceAgainstInformation;
	}

	public boolean isSelected() {
		for (int i = 0; i < isClicks.length; i++) {
			if (isClicks[i]) {
				return true;
			}
		}

		return false;
	}

	public int getClickNum() {
		int clickNum = 0;
		for (int i = 0; i < isClicks.length; i++) {
			if (isClicks[i]) {
				clickNum++;
			}
		}

		return clickNum;
	}

	public static List<List<HalfTheAudienceAgainstInformation>> analysisHalfTheAudienceAgainstInformation(
			JSONArray againstJsonArray) {

		List<List<HalfTheAudienceAgainstInformation>> halfTheAudienceagainstInformationList = new ArrayList<List<HalfTheAudienceAgainstInformation>>();

		int jsonarrayLength = againstJsonArray.length();
		try {
			for (int jsonarray_i = 0; jsonarray_i < jsonarrayLength; jsonarray_i++) {
				List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations = new ArrayList<HalfTheAudienceAgainstInformation>();

				JSONArray againstWithDataJsonArray = againstJsonArray
						.getJSONArray(jsonarray_i);
				int againstWithDataLength = againstWithDataJsonArray.length();

				for (int json_i = 0; json_i < againstWithDataLength; json_i++) {
					JSONObject againstJsonObject = againstWithDataJsonArray
							.getJSONObject(json_i);

					HalfTheAudienceAgainstInformation againstInformation = new HalfTheAudienceAgainstInformation();
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
					againstInformation.setHalf_v00(againstJsonObject
							.getString("half_v00"));
					againstInformation.setHalf_v01(againstJsonObject
							.getString("half_v01"));
					againstInformation.setHalf_v03(againstJsonObject
							.getString("half_v03"));
					againstInformation.setHalf_v11(againstJsonObject
							.getString("half_v11"));
					againstInformation.setHalf_v10(againstJsonObject
							.getString("half_v10"));
					againstInformation.setHalf_v13(againstJsonObject
							.getString("half_v13"));
					againstInformation.setHalf_v33(againstJsonObject
							.getString("half_v33"));
					againstInformation.setHalf_v31(againstJsonObject
							.getString("half_v31"));
					againstInformation.setHalf_v30(againstJsonObject
							.getString("half_v30"));

					halfTheAudienceAgainstInformations.add(againstInformation);
				}
				halfTheAudienceagainstInformationList
						.add(halfTheAudienceAgainstInformations);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return halfTheAudienceagainstInformationList;
	}

	/**add by pengcx 20130709 start*/
	/**
	 * 获取选中按钮的SP值
	 * 
	 * @param click_i
	 *            选中按钮的索引
	 * @return SP值
	 */
	public Double getSelectedSp(int click_i) {
		String selectedSP = "0";

		switch (click_i) {
		case 0:
			selectedSP = getHalf_v33();
			break;
		case 1:
			selectedSP = getHalf_v31();
			break;
		case 2:
			selectedSP = getHalf_v30();
			break;
		case 3:
			selectedSP = getHalf_v13();
			break;
		case 4:
			selectedSP = getHalf_v11();
			break;
		case 5:
			selectedSP = getHalf_v10();
			break;
		case 6:
			selectedSP = getHalf_v03();
			break;
		case 7:
			selectedSP = getHalf_v01();
			break;
		case 8:
			selectedSP = getHalf_v00();
			break;
		}

		return Double.valueOf(selectedSP);
	}
	
	/**add by pengcx 20130709 end*/
}
