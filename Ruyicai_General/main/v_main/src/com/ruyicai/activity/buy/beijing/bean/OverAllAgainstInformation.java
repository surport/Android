package com.ruyicai.activity.buy.beijing.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 全场总比分对阵信息类
 * 
 * @author Administrator
 * 
 */
public class OverAllAgainstInformation extends AgainstInformation {
	private String score_v10;
	private String score_v20;
	private String score_v21;
	private String score_v30;
	private String score_v31;
	private String score_v32;
	private String score_v40;
	private String score_v41;
	private String score_v42;
	private String score_v00;
	private String score_v11;
	private String score_v22;
	private String score_v33;
	private String score_v01;
	private String score_v02;
	private String score_v12;
	private String score_v03;
	private String score_v13;
	private String score_v23;
	private String score_v04;
	private String score_v14;
	private String score_v24;
	private String score_v90;
	private String score_v99;
	private String score_v09;

	private boolean[] isClicks = { false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false,
			false };

	public String getScore_v10() {
		return score_v10;
	}

	public void setScore_v10(String score_v10) {
		this.score_v10 = score_v10;
	}

	public String getScore_v20() {
		return score_v20;
	}

	public void setScore_v20(String score_v20) {
		this.score_v20 = score_v20;
	}

	public String getScore_v21() {
		return score_v21;
	}

	public void setScore_v21(String score_v21) {
		this.score_v21 = score_v21;
	}

	public String getScore_v30() {
		return score_v30;
	}

	public void setScore_v30(String score_v30) {
		this.score_v30 = score_v30;
	}

	public String getScore_v31() {
		return score_v31;
	}

	public void setScore_v31(String score_v31) {
		this.score_v31 = score_v31;
	}

	public String getScore_v32() {
		return score_v32;
	}

	public void setScore_v32(String score_v32) {
		this.score_v32 = score_v32;
	}

	public String getScore_v40() {
		return score_v40;
	}

	public void setScore_v40(String score_v40) {
		this.score_v40 = score_v40;
	}

	public String getScore_v41() {
		return score_v41;
	}

	public void setScore_v41(String score_v41) {
		this.score_v41 = score_v41;
	}

	public String getScore_v42() {
		return score_v42;
	}

	public void setScore_v42(String score_v42) {
		this.score_v42 = score_v42;
	}

	public String getScore_v00() {
		return score_v00;
	}

	public void setScore_v00(String score_v00) {
		this.score_v00 = score_v00;
	}

	public String getScore_v11() {
		return score_v11;
	}

	public void setScore_v11(String score_v11) {
		this.score_v11 = score_v11;
	}

	public String getScore_v22() {
		return score_v22;
	}

	public void setScore_v22(String score_v22) {
		this.score_v22 = score_v22;
	}

	public String getScore_v33() {
		return score_v33;
	}

	public void setScore_v33(String score_v33) {
		this.score_v33 = score_v33;
	}

	public String getScore_v01() {
		return score_v01;
	}

	public void setScore_v01(String score_v01) {
		this.score_v01 = score_v01;
	}

	public String getScore_v02() {
		return score_v02;
	}

	public void setScore_v02(String score_v02) {
		this.score_v02 = score_v02;
	}

	public String getScore_v12() {
		return score_v12;
	}

	public void setScore_v12(String score_v12) {
		this.score_v12 = score_v12;
	}

	public String getScore_v03() {
		return score_v03;
	}

	public void setScore_v03(String score_v03) {
		this.score_v03 = score_v03;
	}

	public String getScore_v13() {
		return score_v13;
	}

	public void setScore_v13(String score_v13) {
		this.score_v13 = score_v13;
	}

	public String getScore_v23() {
		return score_v23;
	}

	public void setScore_v23(String score_v23) {
		this.score_v23 = score_v23;
	}

	public String getScore_v04() {
		return score_v04;
	}

	public void setScore_v04(String score_v04) {
		this.score_v04 = score_v04;
	}

	public String getScore_v14() {
		return score_v14;
	}

	public void setScore_v14(String score_v14) {
		this.score_v14 = score_v14;
	}

	public String getScore_v24() {
		return score_v24;
	}

	public void setScore_v24(String score_v24) {
		this.score_v24 = score_v24;
	}

	public String getScore_v90() {
		return score_v90;
	}

	public void setScore_v90(String score_v90) {
		this.score_v90 = score_v90;
	}

	public String getScore_v99() {
		return score_v99;
	}

	public void setScore_v99(String score_v99) {
		this.score_v99 = score_v99;
	}

	public String getScore_v09() {
		return score_v09;
	}

	public void setScore_v09(String score_v09) {
		this.score_v09 = score_v09;
	}

	public boolean[] getIsClicks() {
		return isClicks;
	}

	public void setIsClicks(boolean[] isClicks) {
		this.isClicks = isClicks;
	}

	public OverAllAgainstInformation clone() {
		OverAllAgainstInformation overAllAgainstInformation = null;

		try {
			overAllAgainstInformation = (OverAllAgainstInformation) super
					.clone();
			overAllAgainstInformation.setScore_v00(getScore_v00());
			overAllAgainstInformation.setScore_v10(getScore_v10());
			overAllAgainstInformation.setScore_v20(getScore_v20());
			overAllAgainstInformation.setScore_v21(getScore_v21());
			overAllAgainstInformation.setScore_v30(getScore_v30());
			overAllAgainstInformation.setScore_v31(getScore_v31());
			overAllAgainstInformation.setScore_v32(getScore_v32());
			overAllAgainstInformation.setScore_v40(getScore_v40());
			overAllAgainstInformation.setScore_v41(getScore_v41());
			overAllAgainstInformation.setScore_v42(getScore_v42());
			overAllAgainstInformation.setScore_v00(getScore_v00());
			overAllAgainstInformation.setScore_v11(getScore_v11());
			overAllAgainstInformation.setScore_v22(getScore_v22());
			overAllAgainstInformation.setScore_v33(getScore_v33());
			overAllAgainstInformation.setScore_v01(getScore_v01());
			overAllAgainstInformation.setScore_v02(getScore_v02());
			overAllAgainstInformation.setScore_v12(getScore_v12());
			overAllAgainstInformation.setScore_v03(getScore_v03());
			overAllAgainstInformation.setScore_v13(getScore_v13());
			overAllAgainstInformation.setScore_v23(getScore_v23());
			overAllAgainstInformation.setScore_v04(getScore_v04());
			overAllAgainstInformation.setScore_v14(getScore_v14());
			overAllAgainstInformation.setScore_v24(getScore_v24());
			overAllAgainstInformation.setScore_v90(getScore_v90());
			overAllAgainstInformation.setScore_v99(getScore_v99());
			overAllAgainstInformation.setScore_v09(getScore_v09());
			boolean[] copyClicks = new boolean[getIsClicks().length];
			System.arraycopy(getIsClicks(), 0, copyClicks, 0,
					getIsClicks().length);
			overAllAgainstInformation.setIsClicks(copyClicks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return overAllAgainstInformation;
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

	public static List<List<OverAllAgainstInformation>> analysisOverAllAgainstInformation(
			JSONArray againstJsonArray) throws JSONException {
		List<List<OverAllAgainstInformation>> overAllagainstInformationList = new ArrayList<List<OverAllAgainstInformation>>();
		int jsonArrayLength = againstJsonArray.length();

		for (int jsonarray_i = 0; jsonarray_i < jsonArrayLength; jsonarray_i++) {
			List<OverAllAgainstInformation> overAllAgainstInformations = new ArrayList<OverAllAgainstInformation>();
			JSONArray againstWithDataJsonArray = againstJsonArray
					.getJSONArray(jsonarray_i);
			int againstWithDataLength = againstWithDataJsonArray.length();

			for (int json_i = 0; json_i < againstWithDataLength; json_i++) {
				JSONObject againstJsonObject = againstWithDataJsonArray
						.getJSONObject(json_i);

				OverAllAgainstInformation againstInformation = new OverAllAgainstInformation();
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
				againstInformation.setScore_v10(againstJsonObject
						.getString("score_v10"));
				againstInformation.setScore_v20(againstJsonObject
						.getString("score_v20"));
				againstInformation.setScore_v21(againstJsonObject
						.getString("score_v21"));
				againstInformation.setScore_v30(againstJsonObject
						.getString("score_v30"));
				againstInformation.setScore_v31(againstJsonObject
						.getString("score_v31"));
				againstInformation.setScore_v32(againstJsonObject
						.getString("score_v32"));
				againstInformation.setScore_v40(againstJsonObject
						.getString("score_v40"));
				againstInformation.setScore_v41(againstJsonObject
						.getString("score_v41"));
				againstInformation.setScore_v42(againstJsonObject
						.getString("score_v42"));
				againstInformation.setScore_v00(againstJsonObject
						.getString("score_v00"));
				againstInformation.setScore_v11(againstJsonObject
						.getString("score_v11"));
				againstInformation.setScore_v22(againstJsonObject
						.getString("score_v22"));
				againstInformation.setScore_v33(againstJsonObject
						.getString("score_v33"));
				againstInformation.setScore_v01(againstJsonObject
						.getString("score_v01"));
				againstInformation.setScore_v02(againstJsonObject
						.getString("score_v02"));
				againstInformation.setScore_v12(againstJsonObject
						.getString("score_v12"));
				againstInformation.setScore_v03(againstJsonObject
						.getString("score_v03"));
				againstInformation.setScore_v13(againstJsonObject
						.getString("score_v13"));
				againstInformation.setScore_v23(againstJsonObject
						.getString("score_v23"));
				againstInformation.setScore_v04(againstJsonObject
						.getString("score_v04"));
				againstInformation.setScore_v14(againstJsonObject
						.getString("score_v14"));
				againstInformation.setScore_v24(againstJsonObject
						.getString("score_v24"));
				againstInformation.setScore_v90(againstJsonObject
						.getString("score_v90"));
				againstInformation.setScore_v99(againstJsonObject
						.getString("score_v99"));
				againstInformation.setScore_v09(againstJsonObject
						.getString("score_v09"));
				overAllAgainstInformations.add(againstInformation);
			}
			overAllagainstInformationList.add(overAllAgainstInformations);
		}
		return overAllagainstInformationList;
	}

	/**
	 * 获取选中的按钮SP值
	 * 
	 * @param click_i
	 *            选中的按钮索引
	 * @return SP值
	 */
	public Double getSelectedSp(int click_i) {
		String selectedSP = "";
		switch (click_i) {
		case 0:
			selectedSP = getScore_v90();
			break;
		case 1:
			selectedSP = getScore_v10();
			break;
		case 2:
			selectedSP = getScore_v20();
			break;
		case 3:
			selectedSP = getScore_v21();
			break;
		case 4:
			selectedSP = getScore_v30();
			break;
		case 5:
			selectedSP = getScore_v31();
			break;
		case 6:
			selectedSP = getScore_v32();
			break;
		case 7:
			selectedSP = getScore_v40();
			break;
		case 8:
			selectedSP = getScore_v41();
			break;
		case 9:
			selectedSP = getScore_v42();
			break;
		case 10:
			selectedSP = getScore_v99();
			break;
		case 11:
			selectedSP = getScore_v00();
			break;
		case 12:
			selectedSP = getScore_v11();
			break;
		case 13:
			selectedSP = getScore_v22();
			break;
		case 14:
			selectedSP = getScore_v33();
			break;
		case 15:
			selectedSP = getScore_v09();
			break;
		case 16:
			selectedSP = getScore_v01();
			break;
		case 17:
			selectedSP = getScore_v02();
			break;
		case 18:
			selectedSP = getScore_v12();
			break;
		case 19:
			selectedSP = getScore_v03();
			break;
		case 20:
			selectedSP = getScore_v13();
			break;
		case 21:
			selectedSP = getScore_v23();
			break;
		case 22:
			selectedSP = getScore_v04();
			break;
		case 23:
			selectedSP = getScore_v14();
			break;
		case 24:
			selectedSP = getScore_v24();
			break;
		}
		return Double.valueOf(selectedSP);
	}
}
