package com.ruyicai.activity.buy.dlt;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.miss.BuyViewItemMiss;
import com.ruyicai.activity.buy.miss.MainViewPagerAdapter;
import com.ruyicai.activity.buy.miss.NumViewItem;
import com.ruyicai.activity.buy.miss.ZixuanActivity;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.code.dlt.DltTwoInDozenCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.json.miss.DltMissJson;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicMethod;

public class DltTwoInDozenSelect extends ZixuanActivity {

	private int redBallResId[] = { R.drawable.grey, R.drawable.red };

	AreaInfo[] dltTwoInDozenAreaInfos = new AreaInfo[1];
	/**
	 * 大乐透胆托红球区
	 */
	BallTable redBallTable;
	/**
	 * 实例化大乐透直选注码类
	 */
	DltTwoInDozenCode dltTwoInDozencode = new DltTwoInDozenCode();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCode(dltTwoInDozencode);
		setIsTen(true);
		initGallery();
		redBallTable = itemViewArray.get(0).areaNums[0].table;
		getMissNet(new DltMissJson(), MissConstant.DLT_Miss,
				MissConstant.DLT_12_2);
	}

	@Override
	public void initViewItem() {
		// TODO Auto-generated method stub

	}

	/**
	 * 初始化滑动
	 */
	public void initGallery() {
		BuyViewItemMiss buyView = new BuyViewItemMiss(this, initArea());
		NumViewItem numView = new NumViewItem(this, initArea());
		// 添加需要左右划屏效果的视图到缓存容器中
		itemViewArray.add(buyView);
		itemViewArray.add(numView);
		// 设置 ViewPager 的 Adapter
		MainViewPagerAdapter MianAdapter = new MainViewPagerAdapter(null);
		View view = numView.createView();
		numView.leftBtn(view);
		MianAdapter.addView(buyView.createView());
		MianAdapter.addView(view);
		viewPagerContainer.setAdapter(MianAdapter);
		// 设置第一显示页面
		viewPagerContainer.setCurrentItem(0);
	}

	@Override
	public AreaNum[] initArea() {
		AreaNum areaNums[] = new AreaNum[1];
		String redTitle = getResources().getString(R.string.zi_xuanzedezhuma)
				.toString();
		areaNums[0] = new AreaNum(12, 8, 2, 12, redBallResId, 0, 1, Color.RED,
				redTitle, false, false);
		return areaNums;
	}

	@Override
	public String isTouzhu() {
		// TODO Auto-generated method stub
		String isTouzhu = "";
		int iRedHighlights = redBallTable.getHighlightBallNums();
		int iZhuShu = (int) getDltTwoInDozenZhuShu(iRedHighlights, 1);
		if (redBallTable.getHighlightBallNums() < 2) {
			isTouzhu = "请至少选择2个小球	";
		} else if (iZhuShu * 2 > 100000) {
			isTouzhu = "false";
		} else {
			isTouzhu = "true";
		}
		return isTouzhu;
	}

	/**
	 * 投注提示框中的信息(注码)
	 * 
	 * @return
	 */
	public String getZhuma() {
		String red_zhuma_string = " ";
		int[] redZhuMa = redBallTable.getHighlightBallNOs();
		for (int i = 0; i < redBallTable.getHighlightBallNOs().length; i++) {
			red_zhuma_string += PublicMethod.getZhuMa(redZhuMa[i]);
			if (i != redBallTable.getHighlightBallNOs().length - 1) {
				red_zhuma_string += ",";
			}
		}
		return "注码：\n" + red_zhuma_string;

	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		int iRedHighlights = areaNum[0].table.getHighlightBallNums();
		StringBuffer iTempString = new StringBuffer();
		int iZhuShu = 0;
		// 红球数 不足
		if (iRedHighlights < 2) {
			int num = 2 - iRedHighlights;
			return "至少还需" + num + "个红球";
		} else {
			iZhuShu = (int) getDltTwoInDozenZhuShu(iRedHighlights,
					iProgressBeishu);
			iTempString.append("共").append(iZhuShu).append("注，共")
					.append(iZhuShu * 2).append("元");
		}

		return iTempString.toString();
	}

	/**
	 * 大乐透十二选二 的计算方法
	 * 
	 * @param iRedHighlights
	 * @param iProgressBeishu
	 * @return
	 */
	private long getDltTwoInDozenZhuShu(int iRedHighlights, int iProgressBeishu) {
		long dltZhuShu = 0L;
		dltZhuShu += (PublicMethod.zuhe(2, iRedHighlights) * iProgressBeishu);
		return dltZhuShu;
	}

	@Override
	public void touzhuNet() {
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_DLT);
	}

	@Override
	public int getZhuShu() {
		// TODO Auto-generated method stub
		int iRedHighlights = itemViewArray.get(0).areaNums[0].table
				.getHighlightBallNums();
		int iZhuShu = (int) getDltTwoInDozenZhuShu(iRedHighlights,
				iProgressBeishu);
		return iZhuShu;
	}

	void setLotoNoAndType(CodeInfoMiss codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_DLT);
		codeInfo.setTouZhuType("12xuan2");
	}
}
