package com.ruyicai.activity.buy.beijing.bean;

/**
 * 北京单场总进球对阵信息类
 * 
 * @author Administrator
 * 
 */
public class TotalGoalsAgainstInformation extends AgainstInformation {
	private String goal_v0;
	private String goal_v1;
	private String goal_v2;
	private String goal_v3;
	private String goal_v4;
	private String goal_v5;
	private String goal_v6;
	private String goal_v7;

	private boolean[] isClicks = { false, false, false, false, false, false,
			false, false };

	public String getGoal_v0() {
		return goal_v0;
	}

	public void setGoal_v0(String goal_v0) {
		this.goal_v0 = goal_v0;
	}

	public String getGoal_v1() {
		return goal_v1;
	}

	public void setGoal_v1(String goal_v1) {
		this.goal_v1 = goal_v1;
	}

	public String getGoal_v2() {
		return goal_v2;
	}

	public void setGoal_v2(String goal_v2) {
		this.goal_v2 = goal_v2;
	}

	public String getGoal_v3() {
		return goal_v3;
	}

	public void setGoal_v3(String goal_v3) {
		this.goal_v3 = goal_v3;
	}

	public String getGoal_v4() {
		return goal_v4;
	}

	public void setGoal_v4(String goal_v4) {
		this.goal_v4 = goal_v4;
	}

	public String getGoal_v5() {
		return goal_v5;
	}

	public void setGoal_v5(String goal_v5) {
		this.goal_v5 = goal_v5;
	}

	public String getGoal_v6() {
		return goal_v6;
	}

	public void setGoal_v6(String goal_v6) {
		this.goal_v6 = goal_v6;
	}

	public String getGoal_v7() {
		return goal_v7;
	}

	public void setGoal_v7(String goal_v7) {
		this.goal_v7 = goal_v7;
	}

	public boolean[] getIsClicks() {
		return isClicks;
	}

	public void setIsClicks(boolean[] isClicks) {
		this.isClicks = isClicks;
	}

	public TotalGoalsAgainstInformation clone() {
		TotalGoalsAgainstInformation totalGoalsAgainstInformation = null;

		try {
			totalGoalsAgainstInformation = (TotalGoalsAgainstInformation) super
					.clone();
			totalGoalsAgainstInformation.setGoal_v0(getGoal_v0());
			totalGoalsAgainstInformation.setGoal_v1(getGoal_v1());
			totalGoalsAgainstInformation.setGoal_v2(getGoal_v2());
			totalGoalsAgainstInformation.setGoal_v3(getGoal_v3());
			totalGoalsAgainstInformation.setGoal_v4(getGoal_v4());
			totalGoalsAgainstInformation.setGoal_v5(getGoal_v5());
			totalGoalsAgainstInformation.setGoal_v6(getGoal_v6());
			totalGoalsAgainstInformation.setGoal_v7(getGoal_v7());
			
			boolean[] copyClicks = new boolean[getIsClicks().length];
			System.arraycopy(getIsClicks(), 0, copyClicks, 0, getIsClicks().length);
			totalGoalsAgainstInformation.setIsClicks(copyClicks);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return totalGoalsAgainstInformation;
	}
}
