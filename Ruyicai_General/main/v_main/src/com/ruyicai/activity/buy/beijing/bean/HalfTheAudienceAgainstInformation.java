package com.ruyicai.activity.buy.beijing.bean;

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
			System.arraycopy(getIsClicks(), 0, copyClicks, 0, getIsClicks().length);
			halfTheAudienceAgainstInformation.setIsClicks(copyClicks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return halfTheAudienceAgainstInformation;
	}
}
