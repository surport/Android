package com.ruyicai.activity.buy.beijing.bean;


/**
 * 北京单场上下单双对阵信息类
 * 
 * @author Administrator
 * 
 */
public class UpDownSingleDoubleAgainstInformation extends AgainstInformation {
	private String sxds_v1;
	private String sxds_v2;
	private String sxds_v3;
	private String sxds_v4;
	
	private boolean v1IsClick;
	private boolean v2IsClick;
	private boolean v3IsClick;
	private boolean v4IsClick;

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
}
