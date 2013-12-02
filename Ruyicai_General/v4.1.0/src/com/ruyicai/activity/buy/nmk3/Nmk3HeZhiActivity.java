package com.ruyicai.activity.buy.nmk3;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.Nmk3MissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicMethod;

/**
 * Nmk3HeZhiActivity：内蒙古快三和值
 * 
 * @author PengCX
 * 
 */
public class Nmk3HeZhiActivity extends ZixuanAndJiXuan implements OnCheckedChangeListener{

	protected int BallResId[] = { R.drawable.nmk3_hezhi_normal, R.drawable.nmk3_hezhi_click };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setAddView(((Nmk3Activity) getParent()).addView);
		super.onCreate(savedInstanceState);
		lotno = Constants.LOTNO_NMK3;
		childtype = new String[] { "直选" };
		highttype = "NMK3-HE";
		init();
		//来自2013-10-16徐培松 start
		childtypes.setVisibility(View.GONE);
		zixuanLayout.setBackgroundResource(R.color.transparent);
		//。。。end

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		onCheckAction(checkedId);
	}
	@Override
	protected void onResume() {
		super.onResume();
//		sensor.stopAction();
//		baseSensor.stopAction();
		editZhuma.setText(textSumMoney(areaNums, iProgressBeishu));
		editZhuma.setTextColor(Color.BLACK);
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		int zhuShu = getZhuShu();
		return "您已选择了" + zhuShu + "注，共" + zhuShu * 2 + "元";
	}
	/**
	 * 设置投注金额提示
	 */
	public void showEditText(){
		editZhuma.setText(textSumMoney(areaNums, iProgressBeishu));
		showEditTitle(NULL);
	}
	@Override
	public String isTouzhu() {
		// 点击号码篮后触发判断投注的合法性
		if (getZhuShu() == 0) {
			return "请至少选择一注";
		} else if (getZhuShu() > 10000) {
			return "false";
		} else {
			return "true";
		}
	}

	@Override
	public int getZhuShu() {
		// 返回当前投注的注数
		return areaNums[0].table.getHighlightBallNums();
	}

	@Override
	public String getZhuma() {
		// 拼接投注的注码格式，用户投注与后台使用
		String zhuMa = "";

		// 获取注码的各个部分
		String playMethodPart = getPlayMethodPart();
		String mutiplePart = getMutiplePart();
		String numberNumsPart = getNumberNumsPart();
		String numbersPart = getNumbersPart();
		String endFlagPart = "^";

		// 拼接注码
		zhuMa = playMethodPart + mutiplePart + numberNumsPart + numbersPart
				+ endFlagPart;

		return zhuMa;
	}

	/**
	 * 获取号码部分
	 * 
	 * @return 号码部分
	 */
	private String getNumbersPart() {
		// 获取高亮小球号码数组
		int[] numbers = areaNums[0].table.getHighlightBallNOs();
		StringBuffer numbersPart = new StringBuffer();

		// 循环号码数组，并拼接
		for (int num_i = 0; num_i < numbers.length; num_i++) {
			numbersPart.append(PublicMethod.getZhuMa(numbers[num_i]));
		}

		return numbersPart.toString();
	}

	/**
	 * 获取号码个数部分
	 * 
	 * @return 号码个数部分
	 */
	private String getNumberNumsPart() {

		return PublicMethod
				.getZhuMa(areaNums[0].table.getHighlightBallNOs().length);

	}

	/**
	 * 获取倍数字段
	 * 
	 * @return 倍数部分
	 */
	private String getMutiplePart() {
		// 获取注码的时候默认使用1倍，在投注详情界面的倍数才对后台有效
		return "0001";
	}

	/**
	 * 获取玩法部分
	 * 
	 * @return 玩法部分
	 */
	private String getPlayMethodPart() {
		return "10";
	}

	@Override
	public String getZhuma(Balls ball) {
		return null;
	}

	@Override
	public void touzhuNet() {
		// 设置投注信息彩种，注码，金额和期号等投注信息
		betAndGift.setLotno(Constants.LOTNO_NMK3);
		betAndGift.setBet_code(getZhuma());
		int zhuShu = getZhuShu();
		betAndGift.setAmount("" + zhuShu * 200);
		betAndGift.setBatchcode(Nmk3Activity.batchCode);
	}

	@Override
	public void onCheckAction(int checkedId) {
		switch (checkedId) {
		case 0:
			lotnoStr=Constants.LOTNO_NMK3;
			initArea();
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_HEZHI, true,
					checkedId, true);
			isMissNet(new Nmk3MissJson(), MissConstant.NMK3_HEZHI, false);// 获取遗漏值
			break;
		}
	}

	/**
	 * 初始化选号控件
	 */
	public AreaNum[] initArea() {
		areaNums = new AreaNum[1];
		areaNums[0] = new AreaNum(14, 7, 1, 14, BallResId, 0, 4, Color.RED,"猜开奖号码相加的和，奖金9-80元！", false, true);
		return areaNums;
	}

	/*
	 * 设置投注信息类的彩种编号和投注类型
	 */
	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_NMK3);
		codeInfo.setTouZhuType("hezhi");
	}

	/**
	 * 显示投注奖金
	 */
	public void showBetMoney(View v) {
		String numbers = ((OneBallView) v).getiShowString();
		String prompt = null;
		if (numbers.equals("4")) {
			prompt = "此和值的奖金为80元";
		} else if (numbers.equals("5")) {
			prompt = "此和值的奖金为40元";
		} else if (numbers.equals("6")) {
			prompt = "此和值的奖金为25元";
		} else if (numbers.equals("7")) {
			prompt = "此和值的奖金为16元";
		} else if (numbers.equals("8")) {
			prompt = "此和值的奖金为12元";
		} else if (numbers.equals("9")) {
			prompt = "此和值的奖金为10元";
		} else if (numbers.equals("10")) {
			prompt = "此和值的奖金为9元";
		} else if (numbers.equals("11")) {
			prompt = "此和值的奖金为9元";
		} else if (numbers.equals("12")) {
			prompt = "此和值的奖金为10元";
		} else if (numbers.equals("13")) {
			prompt = "此和值的奖金为12元";
		} else if (numbers.equals("14")) {
			prompt = "此和值的奖金为16元";
		} else if (numbers.equals("15")) {
			prompt = "此和值的奖金为25元";
		} else if (numbers.equals("16")) {
			prompt = "此和值的奖金为40元";
		} else if (numbers.equals("17")) {
			prompt = "此和值的奖金为80元";
		}

		if (toast == null) {
			if(((OneBallView) v).getShowId() == 1){
				toast = Toast.makeText(this, prompt, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			}
			
		} else {
			if(((OneBallView) v).getShowId() == 1){
				toast.setText(prompt);
				toast.show();
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		System.out.println(isChecked);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeMediaPlayer();
		NmkAnimation.flag = true;
	}
}
