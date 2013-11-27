package com.ruyicai.activity.buy.beijing.bean;

/*
 * 北京单场对阵信息基类
 */
public class AgainstInformation implements Cloneable {
	private String teamId;
	private String dayForamt;
	private String homeTeam;
	private String guestTeam;
	private String league;
	private String endTime;
	// 该信息是否被展开显示
	private boolean isShow = true;

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getDayForamt() {
		return dayForamt;
	}

	public void setDayForamt(String dayForamt) {
		this.dayForamt = dayForamt;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public AgainstInformation clone() {
		AgainstInformation againstInformation = null;

		try {
			againstInformation = (AgainstInformation) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return againstInformation;
	}

	public boolean isSelected() {
		return false;
	}
}
