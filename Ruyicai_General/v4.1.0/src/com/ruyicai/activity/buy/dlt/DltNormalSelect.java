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
import com.ruyicai.activity.buy.ssq.Ssq;
import com.ruyicai.code.dlt.DltNormalSelectCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.json.miss.DltMissJson;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicMethod;

public class DltNormalSelect extends ZixuanActivity {

	private int redBallResId[] = { R.drawable.grey, R.drawable.red };
	private int blueBallResId[] = { R.drawable.grey, R.drawable.blue };
	private ToggleButton zhuijiatouzhu;
	private int singleLotteryValue = 2;

	AreaInfo[] dltNormalAreaInfos = new AreaInfo[2];
	/**
	 * 大乐透直选红球区和篮球区
	 */
	BallTable redBallTable, blueBallTable;
	/**
	 * 实例化大乐透直选注码类
	 */
	DltNormalSelectCode dltNormalcode = new DltNormalSelectCode();

	public void onCreate(Bundle savedInstanceState) {
		setAddViewMiss(((Dlt)getParent()).addView);
		super.onCreate(savedInstanceState);
		setCode(dltNormalcode);
		setIsTen(true);
		initGallery();
		redBallTable = itemViewArray.get(0).areaNums[0].table;
		blueBallTable = itemViewArray.get(0).areaNums[1].table;
		getMissNet(new DltMissJson(), MissConstant.DLT_Miss,
				MissConstant.DLT_ZI);
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

	public AreaNum[] initArea() {
		AreaNum areaNums[] = new AreaNum[2];
		String redTitle = getResources().getString(R.string.front_field)
				.toString();
		String blueTitle = getResources().getString(R.string.rear_field)
				.toString();
		areaNums[0] = new AreaNum(35, 9, 5, 18, redBallResId, 0, 1, Color.RED,
				redTitle, true, true);
		areaNums[1] = new AreaNum(12, 9, 2, 12, blueBallResId, 0, 1,
				Color.BLUE, blueTitle, true, true);
		return areaNums;
	}

	private void initZhuiJia(View view) {
		LinearLayout toggleLinear = (LinearLayout) view
				.findViewById(R.id.buy_zixuan_linear_toggle);
		toggleLinear.setVisibility(LinearLayout.VISIBLE);
		zhuijiatouzhu = (ToggleButton) view.findViewById(R.id.dlt_zhuijia);
		zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					singleLotteryValue = 3;
					betAndGift.setIssuper("0");
				} else {
					singleLotteryValue = 2;
					betAndGift.setIssuper("");
				}
				changeTextSumMoney();
			}
		});
	}

	/**
	 * 初始化选区界面
	 */
	public void initViewItem() {
		// TODO Auto-generated method stub
		// BuyViewItem buyView = new BuyViewItem(this, initDltNormalArea());
		// itemViewArray.add(buyView);
		// View view = buyView.createView();
		// layoutView.addView(view);
	}

	/**
	 * 判断是否满足投注条件
	 */
	public String isTouzhu() {
		String isTouzhu = "";
		int iZhuShu = getZhuShu();
		if (redBallTable.getHighlightBallNums() < 5
				&& blueBallTable.getHighlightBallNums() < 2) {
			isTouzhu = "请至少选择5个红球和2个蓝球	";
		} else if (redBallTable.getHighlightBallNums() < 5) {
			isTouzhu = "请选择至少5个红球";
		} else if (blueBallTable.getHighlightBallNums() < 2) {
			isTouzhu = "请选择2个蓝球";
		} else if (iZhuShu * 2 > 100000) {
			isTouzhu = "false";
		} else {
			isTouzhu = "true";
		}
		return isTouzhu;
	}

	/**
	 * 计算注数
	 */
	public int getZhuShu() {
		int iZhuShu = 0;
		int iRedHighlights = itemViewArray.get(0).areaNums[0].table
				.getHighlightBallNums();
		int iBlueHighlights = itemViewArray.get(0).areaNums[1].table
				.getHighlightBallNums();
		iZhuShu = (int) getDltNormalZhuShu(iRedHighlights, iBlueHighlights,
				iProgressBeishu);
		return iZhuShu;
	}

	/**
	 * 复式玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * @param int aBlueBalls 篮球个数
	 * @param int iProgressBeishu 倍数提示框
	 * @return long 注数
	 */
	private long getDltNormalZhuShu(int aRedBalls, int aBlueBalls,
			int iProgressBeishu) {
		long dltZhuShu = 0L;
		if (aRedBalls > 0 && aBlueBalls > 0) {
			dltZhuShu += (PublicMethod.zuhe(5, aRedBalls)
					* PublicMethod.zuhe(2, aBlueBalls) * iProgressBeishu);
		}
		return dltZhuShu;
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
			red_zhuma_string = red_zhuma_string
					+ PublicMethod.getZhuMa(redZhuMa[i]);
			if (i != redBallTable.getHighlightBallNOs().length - 1) {
				red_zhuma_string = red_zhuma_string + ",";
			}
		}
		String blue_zhuma_string = " ";
		int[] blueZhuMa = blueBallTable.getHighlightBallNOs();
		for (int i = 0; i < blueBallTable.getHighlightBallNOs().length; i++) {
			blue_zhuma_string = blue_zhuma_string
					+ PublicMethod.getZhuMa(blueZhuMa[i]);
			if (i != blueBallTable.getHighlightBallNOs().length - 1) {
				blue_zhuma_string = blue_zhuma_string + ",";
			}
		}
		return "注码：\n" + red_zhuma_string + " | " + blue_zhuma_string;
	}

	/**
	 * 点击小球后的提示信息
	 */
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		int iRedHighlights = areaNum[0].table.getHighlightBallNums();
		int iBlueHighlights = areaNum[1].table.getHighlightBallNums();
		String iTempString = "";
		int iZhuShu = 0;
		// 红球数 不足
		if (iRedHighlights < 5) {
			int num = 5 - iRedHighlights;
			return "至少还需" + num + "个红球";
		} else if (iRedHighlights >= 5) {
			if (iBlueHighlights < 2) {
				int bluenum = 2 - iBlueHighlights;
				return "至少还需" + bluenum + "个蓝球";
			} else {
				iZhuShu = (int) getDltNormalZhuShu(iRedHighlights,
						iBlueHighlights, iProgressBeishu);
				iTempString = "共" + iZhuShu + "注，共"
						+ (iZhuShu * singleLotteryValue) + "元";
			}
		}
		return iTempString;
	}

	/**
	 * 投注联网
	 */
	public void touzhuNet() {
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_DLT);
		betAndGift.setZhui(true);
	}

	void setLotoNoAndType(CodeInfoMiss codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_DLT);
		codeInfo.setTouZhuType("zhixuan");
	}
}
