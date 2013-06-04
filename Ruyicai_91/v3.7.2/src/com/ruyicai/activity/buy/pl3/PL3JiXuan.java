/**
 * 
 */
package com.ruyicai.activity.buy.pl3;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.PL3ZhiXuanBalls;
import com.ruyicai.jixuan.PL3Zu3Balls;
import com.ruyicai.pojo.AreaNum;

/**
 * 排列三机选
 * 
 * @author Administrator
 * 
 */
public class PL3JiXuan extends DanshiJiXuan implements BuyImplement,
		OnCheckedChangeListener {
	private LinearLayout topLinearOne;
	private LinearLayout topLinearTwo;
	private RadioGroup topButton;
	private String topTitle[] = { "直选机选", "组三机选" };
	public PL3ZhiXuanBalls zhixuanBalls = new PL3ZhiXuanBalls();
	public PL3Zu3Balls zu3Balls = new PL3Zu3Balls();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		topLinearOne = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top_one);
		topLinearTwo = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top_two);
		topLinearOne.setVisibility(LinearLayout.VISIBLE);
		topLinearTwo.setVisibility(LinearLayout.VISIBLE);
		initRadioGroup();
	}

	/**
	 * 初始化单选按钮组
	 */
	public void initRadioGroup() {
		topButton = (RadioGroup) findViewById(R.id.buy_zixuan_radiogroup_top);
		for (int i = 0; i < topTitle.length; i++) {
			RadioButton radio = new RadioButton(this);
			radio.setText(topTitle[i]);
			radio.setTextColor(Color.BLACK);
			radio.setTextSize(13);
			radio.setId(i);
			radio.setButtonDrawable(R.drawable.radio_select);
			radio.setPadding(Constants.PADDING, 0, 10, 0);
			topButton.addView(radio);
		}
		topButton.setOnCheckedChangeListener(this);
		topButton.check(0);
	}

	/**
	 * 直选
	 */
	private void createZhiXuan() {
		createView(zhixuanBalls, this, false);
	}

	/**
	 * 组3
	 */
	private void createZu3() {
		createView(zu3Balls, this, false);
	}

	/**
	 * 重写RadioGroup监听方法onCheckedChanged
	 * 
	 * @param RadioGroup
	 *            RadioGroup
	 * @param int checkedId 当前被选择的RadioId
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (group.getId()) {
		case R.id.buy_zixuan_radiogroup_top:
			switch (checkedId) {
			case 0:// 直选
				createZhiXuan();
				break;
			case 1:// 组3
				createZu3();
				break;
			}
		}
	}

	/**
	 * 判断是否满足投注条件
	 */
	public void isTouzhu() {
		// TODO Auto-generated method stub

	}

	/**
	 * 点击小球提示金额
	 * 
	 * @param areaNum
	 * @param iProgressBeishu
	 * @return
	 */
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 投注联网
	 */
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("1");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_PL3);
	}

}
