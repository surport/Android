package com.ruyicai.activity.buy;

import android.app.Application;

import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;

/**
 * 存投注数据
 * 
 * @author win
 * 
 */
public class ApplicationAddview extends Application {
	private AddViewMiss addviewmiss;

	public AddViewMiss getAddviewmiss() {
		return addviewmiss;
	}

	public void setAddviewmiss(AddViewMiss addviewmiss) {
		this.addviewmiss = addviewmiss;
	}

	private AddView addview;
	private BetAndGiftPojo pojo;
	private String htextzhuma;
	private int hightball;
	private int hzhushu;

	public String getHtextzhuma() {
		return htextzhuma;
	}

	public void setHtextzhuma(String htextzhuma) {
		this.htextzhuma = htextzhuma;
	}

	public int getHightball() {
		return hightball;
	}

	public void setHightball(int hightball) {
		this.hightball = hightball;
	}

	public int getHzhushu() {
		return hzhushu;
	}

	public void setHzhushu(int hzhushu) {
		this.hzhushu = hzhushu;
	}

	public BetAndGiftPojo getPojo() {
		return pojo;
	}

	public void setPojo(BetAndGiftPojo pojo) {
		this.pojo = pojo;
	}

	public AddView getAddview() {
		return addview;
	}

	public void setAddview(AddView addview) {
		this.addview = addview;
	}

}
