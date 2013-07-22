package com.ruyicai.activity.buy.twentytwo;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.zixuan.BuyViewItem;
import com.ruyicai.activity.buy.zixuan.ZixuanActivity;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.code.twenty.TwentyDanTuoCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class TwentyDanTuo extends ZixuanActivity {
	private int redBallResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private AreaInfo areaInfos[] = new AreaInfo[2];// 2个选区
	private TwentyDanTuoCode Code = new TwentyDanTuoCode();
	BallTable redBallTable;
	BallTable redTuoBallTable;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCode(Code);
		setIsTen(true);
		initViewItem();
		redBallTable = itemViewArray.get(0).areaNums[0].table;
		redTuoBallTable = itemViewArray.get(0).areaNums[1].table;
	}

	/**
	 * 初始化选区界面
	 */
	public void initViewItem() {
		// TODO Auto-generated method stub
		BuyViewItem buyView = new BuyViewItem(this, initArea());
		itemViewArray.add(buyView);
		layoutView.addView(buyView.createView());
	}

	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		AreaNum areaNums[] = new AreaNum[2];
		String danma = getResources().getString(
				R.string.ssq_dantuo_text_red_danma_title).toString();
		String tuoma = getResources().getString(
				R.string.ssq_dantuo_text_red_tuoma_title).toString();
		areaNums[0] = new AreaNum(22, 8, 4, redBallResId, 0, 1, Color.RED,
				danma);
		areaNums[1] = new AreaNum(22, 8, 20, redBallResId, 0, 1, Color.RED,
				tuoma);
		return areaNums;
	}

	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */

	public void isBallTable(int iBallId) {
		AreaNum[] areaNums = itemViewArray.get(0).areaNums;
		int nBallId = 0;
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
				}

				break;
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
	 * 判断是否满足投注条件
	 */
	public String isTouzhu() {
		String isTouzhu = "";
		int iZhuShu = getZhuShu();
		// //周黎鸣 7.4 代码修改：添加登陆的判断

		int redNumber = redBallTable.getHighlightBallNums();
		int redTuoNumber = redTuoBallTable.getHighlightBallNums();
		if (redNumber + redTuoNumber > 25 || redNumber + redTuoNumber < 6
				|| redNumber < 1 || redNumber > 4 || redTuoNumber < 2
				|| redTuoNumber > 21) {
			isTouzhu = "请选择:\n1~4个胆码；\n" + " 5~21个拖码；\n" + " 胆码与拖码个数之和不小于6个";
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
		String red_tuo_zhuma_string = " ";
		int[] redTuoZhuMa = redTuoBallTable.getHighlightBallNOs();
		for (int i = 0; i < redTuoBallTable.getHighlightBallNOs().length; i++) {
			red_tuo_zhuma_string = red_tuo_zhuma_string
					+ PublicMethod.isTen(redTuoZhuMa[i]);
			if (i != redTuoBallTable.getHighlightBallNOs().length - 1)
				red_tuo_zhuma_string = red_tuo_zhuma_string + ",";
		}
		return "注码:\n" + "胆码区：" + red_zhuma_string + "\n" + "拖码区："
				+ red_tuo_zhuma_string;

		//
	}

	/**
	 * 计算注数
	 */
	public int getZhuShu() {
		int iZhuShu = 0;
		int iRedHighlights = redBallTable.getHighlightBallNums();
		int iRedTuoHighlights = redTuoBallTable.getHighlightBallNums();
		iZhuShu = (int) getTwentyDTZhuShu(iRedHighlights, iRedTuoHighlights,
				iProgressBeishu);
		return iZhuShu;
	}

	/**
	 * 投注联网
	 */
	public void touzhuNet() {
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_22_5);
	}

	/**
	 * 选择小球提示金额
	 */
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		int iRedHighlights = areaNum[0].table.getHighlightBallNums();
		int iRedTuoHighlights = areaNum[1].table.getHighlightBallNums();
		String iTempString = "";

		if (iRedHighlights + iRedTuoHighlights < 6) {
			int num = 6 - iRedHighlights - iRedTuoHighlights;
			if (iRedHighlights == 0) {
				return "至少选择1个胆码";
			} else {
				return "至少还需要" + num + "个拖码";
			}
		} else if (iRedHighlights == 0) {
			return "至少选择1个胆码";
		} else {
			int iZhuShu = (int) getTwentyDTZhuShu(iRedHighlights,
					iRedTuoHighlights, iProgressBeishu);
			iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";

		}
		return iTempString;
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
			if (j != 0) {
				zhumas += " # ";
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
			String zhuma[] = zhumas.split("\\#");
			for (int i = 0; i < zhuma.length; i++) {
				if (i != 0) {
					length += zhuma[i].length() + 1;
				} else {
					length += zhuma[i].length();
				}
				if (i != zhuma.length - 1) {
					builder.setSpan(new ForegroundColorSpan(Color.BLACK),
							length, length + 1, Spanned.SPAN_COMPOSING);
				}
				builder.setSpan(new ForegroundColorSpan(
						itemViewArray.get(0).areaNums[i].textColor), length
						- zhuma[i].length(), length, Spanned.SPAN_COMPOSING);

			}
			editZhuma.setText(builder, BufferType.EDITABLE);
		}
	}

	/**
	 * 复式玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * @return long 注数
	 */
	private long getTwentyDTZhuShu(int aRedBalls, int aRedTuoBalls,
			int iProgressBeishu) {
		long ssqZhuShu = 0L;
		if (aRedBalls > 0 && aRedTuoBalls > 0) {
			ssqZhuShu += (PublicMethod.zuhe(5 - aRedBalls, aRedTuoBalls) * iProgressBeishu);
		}
		return ssqZhuShu;
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_22_5);
		codeInfo.setTouZhuType("dantuo");
	}
}
