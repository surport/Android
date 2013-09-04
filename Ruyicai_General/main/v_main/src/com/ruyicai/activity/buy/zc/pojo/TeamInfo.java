package com.ruyicai.activity.buy.zc.pojo;

import android.os.Handler;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.custom.checkbox.MyCheckBox;

/**
 * 足彩球队信息pojo类
 * 
 */
public class TeamInfo {
	private String homeTeam;  //主队名称
	private String guestTeam; //客队名称
	private String homeOdds; //主队胜赔率
	private String guestOdds; //客队胜赔率
	private String vsOdds; //主客队平赔率
	private String leagueName; //联赛名字
	private String date; //开赛时间
	private String teamId; //赛事Id
	private boolean isWin = false; 
	private boolean isLevel = false;
	private boolean isFail = false;
	private boolean isHalfWin = false; 
	private boolean isHalfLevel = false;
	private boolean isHalfFail = false;
	private boolean isHomeV0 = false; 
	private boolean isHomeV1 = false;
	private boolean isHomeV2 = false;
	private boolean isHomeV3 = false; 
	private boolean isGuestV0 = false;
	private boolean isGuestV1 = false;
	private boolean isGuestV2 = false;
	private boolean isGuestV3 = false;
	private boolean isDan = false;
	public int onClickNum = 0;
	public MyCheckBox[] check;
	private int[] viewIds = {R.id.lq_sfc_dialog_check01,
			R.id.lq_sfc_dialog_check02, R.id.lq_sfc_dialog_check03,
			R.id.lq_sfc_dialog_check04, R.id.lq_sfc_dialog_check05,
			R.id.lq_sfc_dialog_check06, R.id.lq_sfc_dialog_check07,
			R.id.lq_sfc_dialog_check08,};
	public boolean isDan() {
		return isDan;
	}
	public void setDan(boolean isDan) {
		this.isDan = isDan;
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
	public String getHomeOdds() {
		return homeOdds;
	}
	public void setHomeOdds(String homeOdds) {
		this.homeOdds = homeOdds;
	}
	public String getGuestOdds() {
		return guestOdds;
	}
	public void setGuestOdds(String guestOdds) {
		this.guestOdds = guestOdds;
	}
	public String getVsOdds() {
		return vsOdds;
	}
	public void setVsOdds(String vsOdds) {
		this.vsOdds = vsOdds;
	}
	public String getLeagueName() {
		return leagueName;
	}
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public boolean isWin() {
		return isWin;
	}
	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}
	public boolean isLevel() {
		return isLevel;
	}
	public void setLevel(boolean isLevel) {
		this.isLevel = isLevel;
	}
	public boolean isFail() {
		return isFail;
	}
	public void setFail(boolean isFail) {
		this.isFail = isFail;
	}
	public boolean isHalfWin() {
		return isHalfWin;
	}
	public void setHalfWin(boolean isHalfWin) {
		this.isHalfWin = isHalfWin;
	}
	public boolean isHalfLevel() {
		return isHalfLevel;
	}
	public void setHalfLevel(boolean isHalfLevel) {
		this.isHalfLevel = isHalfLevel;
	}
	public boolean isHalfFail() {
		return isHalfFail;
	}
	public void setHalfFail(boolean isHalfFail) {
		this.isHalfFail = isHalfFail;
	}
	public boolean isHomeV0() {
		return isHomeV0;
	}
	public void setHomeV0(boolean isHomeV0) {
		this.isHomeV0 = isHomeV0;
	}
	public boolean isHomeV1() {
		return isHomeV1;
	}
	public void setHomeV1(boolean isHomeV1) {
		this.isHomeV1 = isHomeV1;
	}
	public boolean isHomeV2() {
		return isHomeV2;
	}
	public void setHomeV2(boolean isHomeV2) {
		this.isHomeV2 = isHomeV2;
	}
	public boolean isHomeV3() {
		return isHomeV3;
	}
	public void setHomeV3(boolean isHomeV3) {
		this.isHomeV3 = isHomeV3;
	}
	public boolean isGuestV0() {
		return isGuestV0;
	}
	public void setGuestV0(boolean isGuestV0) {
		this.isGuestV0 = isGuestV0;
	}
	public boolean isGuestV1() {
		return isGuestV1;
	}
	public void setGuestV1(boolean isGuestV1) {
		this.isGuestV1 = isGuestV1;
	}
	public boolean isGuestV2() {
		return isGuestV2;
	}
	public void setGuestV2(boolean isGuestV2) {
		this.isGuestV2 = isGuestV2;
	}
	public boolean isGuestV3() {
		return isGuestV3;
	}
	public void setGuestV3(boolean isGuestV3) {
		this.isGuestV3 = isGuestV3;
	}
	
	
	public String getSelectedTeamString() {
		StringBuffer team = new StringBuffer();
		if (isWin) {
			team.append("3");
		}

		if (isLevel) {
			team.append("1");
		}

		if (isFail) {
			team.append("0");
		}
		return team.toString().trim();
	}
	
	public boolean isSelected() {
		return (isWin || isLevel || isFail);
	}
	
	public void reSet() {
		isWin = false;
		isLevel = false;
		isFail = false;
		isDan = false;
		onClickNum = 0;
	}
	
	public void initView(String titles[], String odds[], LinearLayout layout, 
			Handler handler, boolean flag, String lotno) {
		check = new MyCheckBox[titles.length];
		for (int i = 0; i < check.length; i++) {
			check[i] = (MyCheckBox) layout.findViewById(viewIds[i]);
			check[i].setVisibility(CheckBox.VISIBLE);
			if (flag) {
				check[i].setCheckText("" + odds[i]);
			} else {
				check[i].setHorizontal(true);
			}
			
			check[i].setPosition(i);
			check[i].setCheckTitle(titles[i]);
			check[i].setHandler(handler);
			check[i].setLotno(lotno);
		}
	}
}