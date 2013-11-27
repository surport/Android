package com.ruyicai.activity.buy.ssq;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.miss.BuyViewItemMiss;
import com.ruyicai.activity.buy.miss.MainViewPagerAdapter;
import com.ruyicai.activity.buy.miss.NumViewItem;
import com.ruyicai.activity.buy.miss.ZixuanActivity;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.code.ssq.SsqZiZhiXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.SsqMissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicMethod;

/**
 * 双色球自选直选
 * 
 * @author Administrator
 * 
 */
public class SsqZiZhiXuan extends ZixuanActivity {
	private int redBallResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private int blueBallResId[] = { R.drawable.grey, R.drawable.blue };// 选区小球切换图片
	private SsqZiZhiXuanCode ssqCode = new SsqZiZhiXuanCode();
	BallTable redBallTable;
	BallTable blueBallTable;

	public void onCreate(Bundle savedInstanceState) {
		setAddViewMiss(((Ssq)getParent()).addView);
		super.onCreate(savedInstanceState);
		if (Constants.isDebug) {
			PublicMethod.outLog(this.getClass().getSimpleName(), "onCreate()");
		}
		setCode(ssqCode);
		setIsTen(true);
		initGallery();
		redBallTable = itemViewArray.get(0).areaNums[0].table;
		blueBallTable = itemViewArray.get(0).areaNums[1].table;
		getMissNet(new SsqMissJson(), MissConstant.SSQ_Miss,
				MissConstant.SSQ_ZI);
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

	/**
	 * 初始化选区界面
	 */
	public void initViewItem() {
		// TODO Auto-generated method stub
		// BuyViewItem buyView = new BuyViewItem(this,initArea());
		// NumViewItem numView = new NumViewItem(this,initArea());
		// itemViewArray.add(buyView);
		// itemViewArray.add(numView);
		// mGallery.setViews(buyView.createView(),buyView.height,numView.createView(),numView.height);
		// mGallery.setAreaWith(100);
	}

	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		AreaNum areaNums[] = new AreaNum[2];
		String redTitle = getResources().getString(
				R.string.ssq_zhixuan_text_red_title).toString();
		String blueTitle = getResources().getString(
				R.string.ssq_zhixuan_text_blue_title).toString();
		areaNums[0] = new AreaNum(33, 9, 6, 16, redBallResId, 0, 1, Color.RED,
				redTitle, true, true);
		areaNums[1] = new AreaNum(16, 9, 1, 16, blueBallResId, 0, 1,
				Color.BLUE, blueTitle, true, true);
		return areaNums;
	}

	/**
	 * 判断是否满足投注的投注条件
	 */
	public String isTouzhu() {
		String isTouzhu = "";
		int iZhuShu = getZhuShu();
		if (redBallTable.getHighlightBallNums() < 6
				&& blueBallTable.getHighlightBallNums() < 1) {
			isTouzhu = "请至少选择6个红球和1个蓝球";
		} else if (redBallTable.getHighlightBallNums() < 6) {
			isTouzhu = "请选择至少6个红球";
		} else if (blueBallTable.getHighlightBallNums() < 1) {
			isTouzhu = "请选择1个蓝球";
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
			red_zhuma_string = red_zhuma_string
					+ PublicMethod.isTen(redZhuMa[i]);
			if (i != redBallTable.getHighlightBallNOs().length - 1) {
				red_zhuma_string = red_zhuma_string + ",";
			}
		}
		String blue_zhuma_string = " ";
		int[] blueZhuMa = blueBallTable.getHighlightBallNOs();
		for (int i = 0; i < blueBallTable.getHighlightBallNOs().length; i++) {
			blue_zhuma_string = blue_zhuma_string
					+ PublicMethod.isTen(blueZhuMa[i]);
			if (i != blueBallTable.getHighlightBallNOs().length - 1) {
				blue_zhuma_string = blue_zhuma_string + ",";
			}
		}
		return "注码：" + red_zhuma_string + " | " + blue_zhuma_string;

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
		iZhuShu = (int) getSSQZXZhuShu(iRedHighlights, iBlueHighlights,
				iProgressBeishu);
		return iZhuShu;
	}

	/**
	 * 投注联网信息
	 */
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_SSQ);

	}

	/**
	 * 选择小球提示金额
	 */
	public String textSumMoney(AreaNum areaNum[], int iProgressBeishu) {
		// TODO Auto-generated method stub
		int iRedHighlights = areaNum[0].table.getHighlightBallNums();
		int iBlueHighlights = areaNum[1].table.getHighlightBallNums();
		String iTempString = "";
		int iZhuShu = 0;
		// 红球数 不足
		if (iRedHighlights < 6) {
			int num = 6 - iRedHighlights;
			return "至少还需" + num + "个红球";
		}
		// 红球数达到最低要求
		else if (iRedHighlights == 6) {
			if (iBlueHighlights < 1) {
				return "至少还需1个蓝球";
			} else if (iBlueHighlights == 1) {
				iZhuShu = (int) getSSQZXZhuShu(iRedHighlights, iBlueHighlights,
						iProgressBeishu);
				iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			} else {
				iZhuShu = (int) getSSQZXZhuShu(iRedHighlights, iBlueHighlights,
						iProgressBeishu);
				iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			}
		}
		// 红球复式
		else {
			if (iBlueHighlights < 1) {
				return "至少还需1个蓝球";
			} else if (iBlueHighlights == 1) {
				iZhuShu = (int) getSSQZXZhuShu(iRedHighlights, iBlueHighlights,
						iProgressBeishu);
				iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			} else {
				iZhuShu = (int) getSSQZXZhuShu(iRedHighlights, iBlueHighlights,
						iProgressBeishu);
				iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			}
		}
		return iTempString;
	}

	/**
	 * 复式玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * @param int aBlueBalls 篮球个数
	 * 
	 * @return long 注数
	 */
	private long getSSQZXZhuShu(int aRedBalls, int aBlueBalls,
			int iProgressBeishu) {
		long ssqZhuShu = 0L;
		if (aRedBalls > 0 && aBlueBalls > 0) {
			ssqZhuShu += (PublicMethod.zuhe(6, aRedBalls)
					* PublicMethod.zuhe(1, aBlueBalls) * iProgressBeishu);
		}
		return ssqZhuShu;
	}

	void setLotoNoAndType(CodeInfoMiss codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_SSQ);
		codeInfo.setTouZhuType("zhixuan");
	}

}
