package com.ruyicai.android.model.bean.lottery.compete.against;

/**
 * 竞彩对阵信息基类
 * 
 * @author Administrator
 * @since RYC1.0 2013-7-11
 */
public class AgainstInfo {
	/** 对阵序号 */
	private String	_fTeamId;
	/** 比赛时间 */
	private String	_fDayFormat;
	/** 主队名称 */
	private String	_fHomeTeam;
	/** 客队名称 */
	private String	_fGuestTeam;
	/** 赛事名称 */
	private String	_fLeague;
	/** 截止时间 */
	private String	_fEndTime;

	public String getfTeamId() {
		return _fTeamId;
	}

	public void setfTeamId(String aTeamId) {
		this._fTeamId = aTeamId;
	}

	public String getfDayFormat() {
		return _fDayFormat;
	}

	public void setfDayFormat(String aDayFormat) {
		this._fDayFormat = aDayFormat;
	}

	public String getfHomeTeam() {
		return _fHomeTeam;
	}

	public void setfHomeTeam(String aHomeTeam) {
		this._fHomeTeam = aHomeTeam;
	}

	public String getfGuestTeam() {
		return _fGuestTeam;
	}

	public void setfGuestTeam(String aGuestTeam) {
		this._fGuestTeam = aGuestTeam;
	}

	public String getfLeague() {
		return _fLeague;
	}

	public void setfLeague(String aLeague) {
		this._fLeague = aLeague;
	}

	public String getfEndTime() {
		return _fEndTime;
	}

	public void setfEndTime(String aEndTime) {
		this._fEndTime = aEndTime;
	}

}
