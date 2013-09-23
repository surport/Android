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
	private String homeTeam; // 主队名称
	private String guestTeam; // 客队名称
	private String homeOdds; // 主队胜赔率
	private String guestOdds; // 客队胜赔率
	private String vsOdds; // 主客队平赔率
	private String leagueName; // 联赛名字
	private String date; // 开赛时间
	private String teamId; // 赛事Id
	private boolean isWin = false;
	private boolean isLevel = false;
	private boolean isFail = false;
	private boolean isDan = false;
	public int onClickNum = 0;
	public MyCheckBox[] check;
	private int[] viewIds = { R.id.lq_sfc_dialog_check01,
			R.id.lq_sfc_dialog_check02, R.id.lq_sfc_dialog_check03,
			R.id.lq_sfc_dialog_check04, R.id.lq_sfc_dialog_check05,
			R.id.lq_sfc_dialog_check06, R.id.lq_sfc_dialog_check07,
			R.id.lq_sfc_dialog_check08, };
	public boolean buttonIsOpen;

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
			Handler handler, String lotno, String state) {
		// 如果是第一次初始化的时候，初始化按钮信息
		if (check == null) {
			check = new MyCheckBox[titles.length];
			for (int i = 0; i < check.length; i++) {
				check[i] = (MyCheckBox) layout.findViewById(viewIds[i]);
				check[i].setVisibility(CheckBox.VISIBLE);
				if (Constants.LOTNO_LCB.equals(lotno)) {
					check[i].setCheckText("" + odds[i]);
				} else {
					check[i].setHorizontal(true);
				}

				check[i].setPosition(i);
				check[i].setCheckTitle(titles[i]);
				check[i].setHandler(handler);
				check[i].setLotno(lotno);

				if ("5".equals(state)) {
					check[i].setClickable(false);
				} else {
					check[i].setClickable(true);
					check[i].setHandler(handler);
				}
			}
		} else {
			// 如果是第一次之后初始化，将原来按钮的状态恢复
			MyCheckBox[] tempCheck = new MyCheckBox[check.length];
			for (int i = 0; i < check.length; i++) {
				tempCheck[i] = (MyCheckBox) layout.findViewById(viewIds[i]);

				tempCheck[i].setVisibility(check[i].getVisibility());

				if (Constants.LOTNO_LCB.equals(lotno)) {
					tempCheck[i].setCheckText(check[i].getCheckText());
				} else {
					tempCheck[i].setHorizontal(check[i].getHorizontal());
				}

				tempCheck[i].setPosition(check[i].getPosition());
				tempCheck[i].setCheckTitle(check[i].getChcekTitle());
				tempCheck[i].setHandler(handler);
				tempCheck[i].setLotno(check[i].getLotno());
				tempCheck[i].setChecked(check[i].isChecked());

				if ("5".equals(state)) {
					tempCheck[i].setClickable(false);
				} else {
					tempCheck[i].setClickable(true);
					tempCheck[i].setHandler(handler);
				}
			}

			check = tempCheck;
		}
	}
}