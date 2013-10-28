package com.ruyicai.activity.buy.jc.explain.zq;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.palmdream.RuyicaiAndroid.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * 欧指和亚盘的界面的基类
 * 
 * @author Administrator
 * 
 */
public abstract class BaseListActivity extends Activity {

	protected ListView listMain;
	protected BaseAdapter adapter;
	protected Context context;
	protected List<ExplainInfo> listInfo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jc_score_list);
		context = this;
		initList();
	}

	public abstract void initList();

	protected List getScoreInfo(JSONArray jsonArray) {
		listInfo = new ArrayList<ExplainInfo>();
		for (int i = 0; i < jsonArray.length(); i++) {
			ExplainInfo info = new ExplainInfo();
			listInfo.add(info);
		}
		return listInfo;
	}

	public class ExplainInfo {
		private String firstGoal;// 初盘亚盘
		private String firstUpodds;// 初盘胜水位
		private String firstDownodds;// 初盘负水位
		private String goal;// 即时亚盘
		private String upOdds;// 即时胜
		private String downOdds;// 即时负
		private String companyName;// 公司名

		private String fanHuanLu;// 返还率
		private String homeWinLu;// 主胜率
		private String standoffLu;// 平局率
		private String guestWinLu;// 客胜率
		private String k_h;// 主凯指
		private String k_s;// 平凯指
		private String k_g;// 客凯指
		private String homeWin;// 即时主胜赔率
		private String standoff;// 即时平局赔率
		private String guestWin;// 即时客胜赔率

		public boolean isOpen = false;

		public ExplainInfo() {

		}

		public String getHomeWin() {
			return homeWin;
		}

		public void setHomeWin(String homeWin) {
			this.homeWin = homeWin;
		}

		public String getStandoff() {
			return standoff;
		}

		public void setStandoff(String standoff) {
			this.standoff = standoff;
		}

		public String getGuestWin() {
			return guestWin;
		}

		public void setGuestWin(String guestWin) {
			this.guestWin = guestWin;
		}

		public String getFanHuanLu() {
			return fanHuanLu + "%";
		}

		public void setFanHuanLu(String fanHuanLu) {
			this.fanHuanLu = fanHuanLu;
		}

		public String getHomeWinLu() {
			return homeWinLu + "%";
		}

		public void setHomeWinLu(String homeWinLu) {
			this.homeWinLu = homeWinLu;
		}

		public String getStandoffLu() {
			return standoffLu + "%";
		}

		public void setStandoffLu(String standoffLu) {
			this.standoffLu = standoffLu;
		}

		public String getGuestWinLu() {
			return guestWinLu + "%";
		}

		public void setGuestWinLu(String guestWinLu) {
			this.guestWinLu = guestWinLu;
		}

		public String getK_h() {
			return k_h;
		}

		public void setK_h(String k_h) {
			this.k_h = k_h;
		}

		public String getK_s() {
			return k_s;
		}

		public void setK_s(String k_s) {
			this.k_s = k_s;
		}

		public String getK_g() {
			return k_g;
		}

		public void setK_g(String k_g) {
			this.k_g = k_g;
		}

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getFirstGoal() {
			return firstGoal;
		}

		public void setFirstGoal(String firstGoal) {
			this.firstGoal = firstGoal;
		}

		public String getFirstUpodds() {
			return firstUpodds;
		}

		public void setFirstUpodds(String firstUpodds) {
			this.firstUpodds = firstUpodds;
		}

		public String getFirstDownodds() {
			return firstDownodds;
		}

		public void setFirstDownodds(String firstDownodds) {
			this.firstDownodds = firstDownodds;
		}

		public String getGoal() {
			return goal;
		}

		public void setGoal(String goal) {
			this.goal = goal;
		}

		public String getUpOdds() {
			return upOdds;
		}

		public void setUpOdds(String upOdds) {
			this.upOdds = upOdds;
		}

		public String getDownOdds() {
			return downOdds;
		}

		public void setDownOdds(String downOdds) {
			this.downOdds = downOdds;
		}

		public boolean isOpen() {
			return isOpen;
		}

		public void setOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}

	}
}
