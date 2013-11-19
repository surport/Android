package com.ruyicai.activity.buy.miss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.activity.buy.zixuan.JiXuanBtn;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicMethod;

/**
 * 遗漏值界面
 * 
 * @author Administrator
 * 
 */

public class NumViewItem extends BuyViewItemMiss {
	BaseActivity zixuan;
	public List<List> missList = new ArrayList<List>();
	public boolean isHigh = false;

	public NumViewItem(BaseActivity zixuan, AreaNum areaNums[]) {
		super(zixuan, areaNums);
		this.zixuan = zixuan;
	}

	public NumViewItem(BaseActivity zixuan, AreaNum areaNums[],
			List<List> missList) {
		super(zixuan, areaNums);
		this.zixuan = zixuan;
		this.missList = missList;
	}

	public NumViewItem(BaseActivity zixuan, AreaNum areaNums[],
			List<List> missList, boolean isHigh) {
		super(zixuan, areaNums);
		this.zixuan = zixuan;
		this.missList = missList;
		this.isHigh = isHigh;
	}

	/**
	 * 创建购彩界面
	 * 
	 * @param areaInfos
	 *            选球区
	 */
	public View createView() {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (RelativeLayout) inflate.inflate(R.layout.buy_zixuan_new_view, null);
		int iScreenWidth = PublicMethod.getDisplayWidth(context);
		int layoutIds[] = { R.id.buy_zixuan_linear_one,
				R.id.buy_zixuan_linear_two, R.id.buy_zixuan_linear_third,
				R.id.buy_zixuan_linear_four, R.id.buy_zixuan_linear_five,
				R.id.buy_zixuan_linear_six, R.id.buy_zixuan_linear_seven };
		int tableLayoutIds[] = { R.id.buy_zixuan_table_one,
				R.id.buy_zixuan_table_two, R.id.buy_zixuan_table_third,
				R.id.buy_zixuan_table_four, R.id.buy_zixuan_table_five,
				R.id.buy_zixuan_table_six, R.id.buy_zixuan_table_seven };
		int textViewIds[] = { R.id.buy_zixuan_text_one,
				R.id.buy_zixuan_text_two, R.id.buy_zixuan_text_third,
				R.id.buy_zixuan_text_four, R.id.buy_zixuan_text_five,
				R.id.buy_zixuan_text_six, R.id.buy_zixuan_text_seven };
		height = 0;
		// 初始化选区
		for (int i = 0; i < areaNums.length; i++) {
			AreaNum areaNum = areaNums[i];
			areaNum.initView(tableLayoutIds[i], textViewIds[i], layoutIds[i],
					view);
			if (i != 0) {
				areaNum.aIdStart = areaNums[i - 1].areaNum
						+ areaNums[i - 1].aIdStart;
			}
			if (missList == null || missList.size() == 0) {
				areaNum.table = PublicMethod.makeBallTable(areaNum.tableLayout,
						iScreenWidth, areaNum.areaNum, areaNum.ballResId,
						areaNum.aIdStart, areaNum.aBallViewText, false,
						context, areaNum.isNum, null, onclick);
			} else {
				areaNum.table = PublicMethod.makeBallTable(areaNum.tableLayout,
						iScreenWidth, areaNum.areaNum, areaNum.ballResId,
						areaNum.aIdStart, areaNum.aBallViewText, false,
						context, areaNum.isNum, missList.get(i), onclick);
			}
			areaNum.init();
			if (isHigh) {
				Button btn = new Button(zixuan);
				Button btnDw = new Button(zixuan);
				if (areaNum.isJxBtn) {
					btn.setVisibility(Button.VISIBLE);
					btnDw.setVisibility(Button.VISIBLE);
					areaNum.jixuanBtn = new JiXuanBtn(areaNum.isRed, btn,
							btnDw, areaNum.chosenBallSum, zixuan, view,
							areaNum.table, areaNum.areaMin, i);
				} else {
					btn.setVisibility(Button.GONE);
					btnDw.setVisibility(Button.GONE);
					areaNum.jixuanBtn = new JiXuanBtn(areaNum.isRed, btn,
							btnDw, areaNum.chosenBallSum, zixuan, view,
							areaNum.table, areaNum.areaMin, i);
				}
				if (areaNum.isSensor) {
					zixuan.areaNums = areaNums;
				}
			}
		}
		return view;
	}

}
