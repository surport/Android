package com.ruyicai.activity.buy.ssc;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.code.ssc.ThreeStarCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.SscMissJson;
import com.ruyicai.json.miss.SscZMissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicMethod;

public class SscThreeStar extends ZixuanAndJiXuan {
	public boolean isjixuan = false;
	public static final int SSC_TYPE = 3;
	public static SscThreeStar self;
	public int THREE_START_TYPE = 0; 
	private String showMessage = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lotnoStr = Constants.LOTNO_SSC;
		childtype = new String[] { "直选" ,"组三","组六"};
		setContentView(R.layout.sscbuyview);
		sscCode = new ThreeStarCode();
		self = this;
		highttype = "SSC";
		theMethodYouWantToCall();
	}

	public void theMethodYouWantToCall() {
		init();
		//childtypes.setVisibility(View.GONE);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		onCheckAction(checkedId);

	}

	public void onCheckAction(int checkedId) {
		switch (checkedId) {
		case 0:
			setDirectSelect(checkedId);
			break;
		case 1:
			setGroupThree(checkedId);
			break;
		case 2:
			setGroupSix(checkedId);
			break;
		}
	}
	/**
	 * direct select
	 * @param checkedId
	 */
    private void setDirectSelect(int checkedId) {
		radioId = 0;
		isjixuan = false;
		THREE_START_TYPE = Constants.SSC_THREE;
		BallTable shitable;
		BallTable getable;
		iProgressBeishu = 1;
		iProgressQishu = 1;
		String baititle = "百位区：";
		String shititle = "十位区：";
		String getitle = "个位区：";
		AreaNum[] areaNums = new AreaNum[3];
		areaNums[0] = new AreaNum(10, 10, 1, 11, BallResId, 0, 0,
				Color.RED, baititle, false, true);
		areaNums[1] = new AreaNum(10, 10, 1, 11, BallResId, 0, 0,
				Color.RED, shititle, false, true);
		areaNums[2] = new AreaNum(10, 10, 1, 11, BallResId, 0, 0,
				Color.RED, getitle, false, true);
		createViewNew(areaNums, sscCode, ZixuanAndJiXuan.THREE, true,
				checkedId);
		BallTable = areaNums[0].table;
		shitable = areaNums[1].table;
		getable = areaNums[2].table;
		isMissNet(new SscMissJson(), MissConstant.SSC_5X_ZX, false);// 获取遗漏值
		isMissNet(new SscZMissJson(), MissConstant.SSC_MV_3ZHI_ZH, true);// 获取遗漏值
    }
    /**
     * group three
     * @param checkedId
     */
    private void setGroupThree(int checkedId) {
		isjixuan = false;
		THREE_START_TYPE = Constants.SSC_THREE_GROUP_THREE;
		iProgressBeishu = 1;
		iProgressQishu = 1;
		areaNums = new AreaNum[1];
		String titlezu = PublicMethod.getResourcesMes(SscThreeStar.this, R.string.please_choose_number);
		areaNums[0] = new AreaNum(10, 10,2, 11, BallResId, 0, 0, Color.RED,
				titlezu, false, false);
		createView(areaNums, sscCode,THREE_START_TYPE, true, checkedId,false);
		BallTable = areaNums[0].table;
    }
    /**
     * group six
     * @param checkedId
     */
    private void setGroupSix(int checkedId) {
		isjixuan = false;
		THREE_START_TYPE = Constants.SSC_THREE_GROUP_SIX;
		iProgressBeishu = 1;
		iProgressQishu = 1;
		areaNums = new AreaNum[1];
		String titlezu = PublicMethod.getResourcesMes(SscThreeStar.this, R.string.please_choose_number);
		areaNums[0] = new AreaNum(10, 10, 3, 11, BallResId, 0, 0, Color.RED,
				titlezu, false, false);
		createView(areaNums, sscCode,THREE_START_TYPE, true, checkedId,false);
		BallTable = areaNums[0].table;
    }
	public String getZhuma() {
		String zhuma = "";
		sscCode.ssc_type = THREE_START_TYPE;
		zhuma = sscCode.zhuma(areaNums, iProgressBeishu, 0);
		return zhuma;
	}

	@Override
	public String getZhuma(Balls ball) {
		String zhuma = "";
		zhuma = ball.getZhuma(null, SSC_TYPE);
		return zhuma;
	}

	public int getZhuShu() {
		int iReturnValue = 0;
		if (isjixuan) {
			int beishu = iProgressBeishu;
			iReturnValue = balls.size() * beishu;
		} else {
			int beishu = iProgressBeishu;
			switch (THREE_START_TYPE) {
				case Constants.SSC_THREE:
					int bai = areaNums[0].table.getHighlightBallNums();
					int shi = areaNums[1].table.getHighlightBallNums();
					int ge = areaNums[2].table.getHighlightBallNums();
					//int beishu = iProgressBeishu;
					iReturnValue = bai * shi * ge * beishu;
					break;
				case Constants.SSC_THREE_GROUP_THREE:
					iReturnValue = callGroupThreeZhuShuMethod(getHighlightBallNums()) * beishu ;
					break;
				case Constants.SSC_THREE_GROUP_SIX:
					iReturnValue = callGroupSixZhuShuMethod(getHighlightBallNums()) * beishu ;
	                break;
			}
		}
		return iReturnValue;
	}
	/**
	 * 组三的注数
	 * @param one
	 * @return
	 */
    private int callGroupThreeZhuShuMethod(int one) {
    	return (2 * (int)PublicMethod.zuhe(2, one));
    }
    /**
     * 组六的注数
     * @param one
     * @return
     */
    private int callGroupSixZhuShuMethod(int one) {
    	return  (int)PublicMethod.zuhe(3, one); 	
    }
	protected void onResume() {
		super.onResume();
		lotnoStr = Constants.LOTNO_SSC;
	}

	@Override
	public String isTouzhu() {
		String isTouzhu = "";
		if (isMove && itemViewArray.get(newPosition).isZHmiss) {
			int onClickNum = getClickNum();
			if (onClickNum == 0) {
				isTouzhu = "请至少选择一注！";
			} else {
				isTouzhu = "true";
			}
		} else {
            if (!this.checkBallNum()) {
            	isTouzhu = this.showMessage;
            } else {
            	isTouzhu = "true";
            }
		}
		return isTouzhu;
	}

	public String getZxAlertZhuma() {
		int iZhuShu = getZhuShu();
		int[] bai = areaNums[0].table.getHighlightBallNOs();
		int[] shi = areaNums[1].table.getHighlightBallNOs();
		int[] ge = areaNums[2].table.getHighlightBallNOs();
		return "注码：" + "\n" + "百位：" + getStrZhuMa(bai) + "\n" + "十位："
				+ getStrZhuMa(shi) + "\n" + "个位：" + getStrZhuMa(ge);

	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		String iTempString = "";
		if (isMove && itemViewArray.get(getNewPosition()).isZHmiss) {
			int onClickNum = getClickNum();
			if (onClickNum == 0) {
				iTempString = getResources().getString(
						R.string.please_choose_number);
			} else {
				iTempString = "共" + onClickNum + "注，共" + (onClickNum * 2) + "元";
			}
		} else {
            if (!checkBallNum()) { 
            	return iTempString = this.showMessage;
            }
			int iZhuShu = getZhuShu();

			if (iZhuShu != 0) {
				iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			} else {
				iTempString = getResources().getString(
						R.string.please_choose_number);
			}
		}
		return iTempString;
	}
	/**
	 * 选择球数量
	 * @return
	 */
	private boolean checkBallNum() {
		showMessage = "";
		switch (THREE_START_TYPE) {
			case Constants.SSC_THREE:
				int bai = areaNums[0].table.getHighlightBallNums();
				int shi = areaNums[1].table.getHighlightBallNums();
				int ge = areaNums[2].table.getHighlightBallNums();
				int iZhuShu = getZhuShu();
				if (bai == 0 | shi == 0 | ge == 0) {
					showMessage = "请至少选择一注！";
					return false;
				} else if (iZhuShu > MAX_ZHU) {
					showMessage = "false";
					return false;
				} else {
					showMessage = "true";
				}
				break;
			case Constants.SSC_THREE_GROUP_THREE:
				if (getHighlightBallNums() < 2) {
					this.showMessage = "至少还需要" + (2-getHighlightBallNums())+"个球";
					return false;
				}
				break;
			case Constants.SSC_THREE_GROUP_SIX:
				if (getHighlightBallNums() < 3) {
					this.showMessage = "至少还需要" + (3-getHighlightBallNums())+"个球";
					return false;
				}
	            break;
		}
		return true;
	}
    private int getHighlightBallNums() {
    	return areaNums[0].table.getHighlightBallNums();
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
		for (int j = 0; j < areaNums.length; j++) {
			BallTable ballTable = areaNums[j].table;
			int[] zhuMa = ballTable.getHighlightBallNOs();
			if (j == 0) {
				zhumas += "-,-,";
			} else {
				zhumas += " , ";
			}
			for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
				if (highttype.equals("SSC")) {
					zhumas += (zhuMa[i]) + "";
				} else if (highttype.equals("DLC")) {
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
				}
			}
			num += zhuMa.length;
		}
		if (num == 0) {
			editZhuma.setText("");
			showEditTitle(this.type);
		} else {
			builder.append(zhumas);
			String zhuma[] = zhumas.split("\\,");
			for (int i = 0; i < zhuma.length; i++) {
				if (i != 0) {
					length += zhuma[i].length() + 1;
				} else {
					length += zhuma[i].length();
				}
				if (i != zhuma.length - 1 && !zhuma[i].equals("-")) {
					builder.setSpan(new ForegroundColorSpan(Color.BLACK),
							length, length + 1, Spanned.SPAN_COMPOSING);
				}
				builder.setSpan(new ForegroundColorSpan(Color.RED), length
						- zhuma[i].length(), length, Spanned.SPAN_COMPOSING);
			}
			editZhuma.setText(builder, BufferType.EDITABLE);
			showEditTitle(NULL);
		}
	}

	@Override
	public void touzhuNet() {
		int zhuShu = getZhuShu();
		if (isjixuan) {
			betAndGift.setSellway("1");
		} else {
			betAndGift.setSellway("0");
		}// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_SSC);
		betAndGift.setBet_code(getZhuma());
		betAndGift.setAmount("" + zhuShu * 200);
		betAndGift.setBatchcode(Ssc.batchCode);
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_SSC);
		codeInfo.setTouZhuType("3start");
	}
}
