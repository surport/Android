package com.ruyicai.activity.buy.zixuan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicMethod;

/**
 * 遗漏值界面
 * 
 * @author Administrator
 * 
 */

public class NumViewItem extends BuyViewItem {
	ZixuanActivity zixuan;

	public NumViewItem(ZixuanActivity zixuan, AreaNum areaNums[]) {
		super(zixuan, areaNums);
		this.zixuan = zixuan;
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
		View view = (LinearLayout) inflate.inflate(R.layout.buy_zixuan_view,
				null);
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
		// 初始化选区
		for (int i = 0; i < areaNums.length; i++) {
			AreaNum areaNum = areaNums[i];
			areaNum.initView(tableLayoutIds[i], textViewIds[i], layoutIds[i],
					view);
			if (i != 0) {
				areaNum.aIdStart = areaNums[i - 1].areaNum
						+ areaNums[i - 1].aIdStart;
			}
			areaNum.table = PublicMethod.makeBallTable(areaNum.tableLayout,
					iScreenWidth, areaNum.areaNum, areaNum.ballResId,
					areaNum.aIdStart, areaNum.aBallViewText, false, context,
					areaNum.isNum, null, null);
			areaNum.init();
		}
		// initBallOnclik();
		return view;
	}
}
