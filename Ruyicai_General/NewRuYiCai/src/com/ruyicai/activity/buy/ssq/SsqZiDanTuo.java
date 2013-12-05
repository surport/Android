/**
 * 
 */
package com.ruyicai.activity.buy.ssq;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.miss.BuyViewItemMiss;
import com.ruyicai.activity.buy.miss.MainViewPagerAdapter;
import com.ruyicai.activity.buy.miss.NumViewItem;
import com.ruyicai.activity.buy.miss.ZixuanActivity;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.code.ssq.SsqZiDanTuoCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.SsqMissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

/**
 * 双色球自选胆拖
 * 
 * @author Administrator
 * 
 */
public class SsqZiDanTuo extends ZixuanActivity {
	private int redBallResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private int blueBallResId[] = { R.drawable.grey, R.drawable.blue };// 选区小球切换图片
	private SsqZiDanTuoCode ssqCode = new SsqZiDanTuoCode();
	BallTable redBallTable;
	BallTable redTuoBallTable;
	BallTable blueBallTable;
	ZixuanActivity zixuan;

	public void onCreate(Bundle savedInstanceState) {
		setAddViewMiss(((Ssq)getParent()).addView);
		super.onCreate(savedInstanceState);
		if (Constants.isDebug) {
			PublicMethod.outLog(this.getClass().getSimpleName(), "onCreate()");
		}
		zixuan = this;
		setCode(ssqCode);
		setIsTen(true);
		// initViewItem();
		initGallery();
		// LinearLayout thirdLinear =
		// (LinearLayout)findViewById(R.id.buy_zixuan_linear_third);
		// thirdLinear.setVisibility(LinearLayout.VISIBLE);
		redBallTable = itemViewArray.get(0).areaNums[0].table;
		redTuoBallTable = itemViewArray.get(0).areaNums[1].table;
		blueBallTable = itemViewArray.get(0).areaNums[2].table;
		getMissNet(new SsqMissJson(), MissConstant.SSQ_Miss,
				MissConstant.SSQ_DAN);
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
		BuyViewItemMiss buyView = new BuyViewItemMiss(this, initArea());
		NumViewItem numView = new NumViewItem(this, initArea());
		itemViewArray.add(buyView);
		itemViewArray.add(numView);
		// mGallery.setViews(buyView.createView(),buyView.height,numView.createView(),numView.height);
		// mGallery.setAreaWith(100);
	}

	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		AreaNum areaNums[] = new AreaNum[3];
		String danma = getResources().getString(
				R.string.ssq_dantuo_text_red_danma_title).toString();
		String tuoma = getResources().getString(
				R.string.ssq_dantuo_text_red_tuoma_title).toString();
		String blue = getResources().getString(
				R.string.ssq_dantuo_text_blue_title).toString();

		areaNums[0] = new AreaNum(33, 8, 1, 5, redBallResId, 0, 1, Color.RED,
				danma, true, false);
		areaNums[1] = new AreaNum(33, 8, 2, 19, redBallResId, 0, 1, Color.RED,
				tuoma, true, false);
		areaNums[2] = new AreaNum(16, 8, 1, 16, blueBallResId, 0, 1,
				Color.BLUE, blue, true, false);
		return areaNums;
	}

	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId) {
		int aBallId = iBallId;
		for (int j = 0; j < itemViewArray.size(); j++) {
			AreaNum[] areaNums = itemViewArray.get(j).areaNums;
			int nBallId = 0;
			iBallId = aBallId;
			for (int i = 0; i < areaNums.length; i++) {
				nBallId = iBallId;
				iBallId = iBallId - areaNums[i].areaNum;
				if (iBallId < 0) {
					if (i == 0) {
						int isHighLight = areaNums[0].table.changeBallState(
								areaNums[0].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT
								&& areaNums[1].table.getOneBallStatue(nBallId) != 0) {
							areaNums[1].table.clearOnBallHighlight(nBallId);
							toast.setText(getResources().getString(
									R.string.ssq_toast_danma_title));
							toast.show();
						}

					} else if (i == 1) {
						int isHighLight = areaNums[1].table.changeBallState(
								areaNums[1].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT
								&& areaNums[0].table.getOneBallStatue(nBallId) != 0) {
							areaNums[0].table.clearOnBallHighlight(nBallId);

							toast.setText(getResources().getString(
									R.string.ssq_toast_tuoma_title));
							toast.show();
						}
					} else {
						areaNums[2].table.changeBallState(
								areaNums[2].chosenBallSum, nBallId);

					}

					break;
				}
			}
		}
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
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
		String red_tuo_zhuma_string = " ";
		int[] redTuoZhuMa = redTuoBallTable.getHighlightBallNOs();
		for (int i = 0; i < redTuoBallTable.getHighlightBallNOs().length; i++) {
			red_tuo_zhuma_string = red_tuo_zhuma_string
					+ PublicMethod.isTen(redTuoZhuMa[i]);
			if (i != redTuoBallTable.getHighlightBallNOs().length - 1)
				red_tuo_zhuma_string = red_tuo_zhuma_string + ",";
		}
		return "注码:\n" + "胆码区：" + red_zhuma_string + "\n" + "拖码区："
				+ red_tuo_zhuma_string + "\n" + "蓝球区：" + blue_zhuma_string;

	}

	/**
	 * 计算注数
	 */
	public int getZhuShu() {
		int iZhuShu = 0;
		int iRedHighlights = redBallTable.getHighlightBallNums();
		int iRedTuoHighlights = redTuoBallTable.getHighlightBallNums();
		int iBlueHighlights = blueBallTable.getHighlightBallNums();
		iZhuShu = (int) getSSQDTZhuShu(iRedHighlights, iRedTuoHighlights,
				iBlueHighlights, iProgressBeishu);
		return iZhuShu;
	}

	/**
	 * 投注联网
	 */
	public void touzhuNet() {
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_SSQ);
	}

	/**
	 * 选择小球提示金额
	 */
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		int iRedHighlights = areaNum[0].table.getHighlightBallNums();
		int iRedTuoHighlights = areaNum[1].table.getHighlightBallNums();
		int iBlueHighlights = areaNum[2].table.getHighlightBallNums();
		String iTempString = "";

		if (iRedHighlights + iRedTuoHighlights < 7) {
			int num = 7 - iRedHighlights - iRedTuoHighlights;
			if (iRedHighlights == 0) {
				return "至少选择1个胆码";
			} else {
				return "至少还需要" + num + "个拖码";
			}
		} else if (iRedHighlights == 0) {
			return "至少选择1个胆码";
		} else if (iBlueHighlights < 1) {
			return "至少还需要1个蓝球";
		} else {
			int iZhuShu = (int) getSSQDTZhuShu(iRedHighlights,
					iRedTuoHighlights, iBlueHighlights, iProgressBeishu);
			iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";

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
	private long getSSQDTZhuShu(int aRedBalls, int aRedTuoBalls,
			int aBlueBalls, int iProgressBeishu) {
		long ssqZhuShu = 0L;
		if (aRedBalls > 0 && aRedTuoBalls > 0 && aBlueBalls > 0) {
			ssqZhuShu += (PublicMethod.zuhe(6 - aRedBalls, aRedTuoBalls)
					* PublicMethod.zuhe(1, aBlueBalls) * iProgressBeishu);
		}
		return ssqZhuShu;
	}

	/**
	 * 判断是否满足投注条件
	 * 
	 * @return
	 */
	public String isTouzhu() {
		String isTouzhu = "";
		// TODO Auto-generated method stub
		int iZhuShu = getZhuShu();
		// //周黎鸣 7.4 代码修改：添加登陆的判断

		int redNumber = redBallTable.getHighlightBallNums();
		int redTuoNumber = redTuoBallTable.getHighlightBallNums();
		int blueNumber = blueBallTable.getHighlightBallNums();
		if (redNumber + redTuoNumber > 25 || redNumber + redTuoNumber < 7
				|| redNumber < 1 || redNumber > 5 || blueNumber < 1
				|| blueNumber > 16 || redTuoNumber < 2 || redTuoNumber > 20) {
			isTouzhu = "请选择:\n1~5个红色胆码；\n" + " 2~20个红色拖码；\n" + " 1~16个蓝色球；\n"
					+ " 胆码与拖码个数之和在7~25之间";
		} else if (iZhuShu * 2 > 100000) {
			isTouzhu = "false";
		} else {
			isTouzhu = "true";
		}
		return isTouzhu;
	}

	/**
	 * 显示在输入框的注码
	 * 
	 */
	public void showEditText() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		String zhumas = "";
		int num = 0;// 高亮小球数
		int length = 0;
		for (int j = 0; j < itemViewArray.get(0).areaNums.length; j++) {
			BallTable ballTable = itemViewArray.get(0).areaNums[j].table;
			int[] zhuMa = ballTable.getHighlightBallNOs();
			if (j == 1) {
				zhumas += " # ";
			} else if (j == 2) {
				zhumas += " | ";
			}
			for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
				if (isTen) {
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
				} else {
					zhumas += zhuMa[i] + "";
				}

				if (i != ballTable.getHighlightBallNOs().length - 1) {
					zhumas += ",";
				}
			}
			num += zhuMa.length;
		}
		if (num == 0) {
			editZhuma.setText("");
		} else {
			builder.append(zhumas);
			List<String> zhuma = new ArrayList<String>();
			String zhumaStr[] = zhumas.split("\\#");
			for (int j = 0; j < zhumaStr.length; j++) {
				String zhuMa[] = zhumaStr[j].split("\\|");
				for (int n = 0; n < zhuMa.length; n++) {
					zhuma.add(zhuMa[n]);
				}
			}
			for (int i = 0; i < zhuma.size(); i++) {
				if (i != 0) {
					length += zhuma.get(i).length() + 1;
				} else {
					length += zhuma.get(i).length();
				}
				if (i != zhuma.size() - 1) {
					builder.setSpan(new ForegroundColorSpan(Color.BLACK),
							length, length + 1, Spanned.SPAN_COMPOSING);
				}
				builder.setSpan(new ForegroundColorSpan(
						itemViewArray.get(0).areaNums[i].textColor), length
						- zhuma.get(i).length(), length, Spanned.SPAN_COMPOSING);

			}
			editZhuma.setText(builder, BufferType.EDITABLE);
		}
	}

	void setLotoNoAndType(CodeInfoMiss codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_SSQ);
		codeInfo.setTouZhuType("dantuo");
	}
}
