package com.ruyicai.activity.buy.beijing.bean;

/**
 * 北京单场胜平负对阵信息类
 * 
 * @author Administrator
 * 
 */
public class WinTieLossAgainstInformation extends AgainstInformation {
	private String v0;
	private String v1;
	private String v3;

	private boolean v0IsClick = false;
	private boolean v1IsClick = false;
	private boolean v3IsClick = false;

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
}
