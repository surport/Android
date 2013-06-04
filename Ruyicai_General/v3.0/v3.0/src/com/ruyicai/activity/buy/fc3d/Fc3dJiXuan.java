/**
 * 
 */
package com.ruyicai.activity.buy.fc3d;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.Fc3dZhiXuanBalls;
import com.ruyicai.jixuan.Fc3dZu3Balls;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.Constants;

/**
 * @author Administrator
 *
 */
public class Fc3dJiXuan extends DanshiJiXuan implements BuyImplement,OnCheckedChangeListener{
	private LinearLayout topLinearOne;
	private LinearLayout topLinearTwo;
	private RadioGroup topButton;
	private String topTitle[]={"直选机选","组三机选"};
	public Fc3dZhiXuanBalls zhixuanBalls = new  Fc3dZhiXuanBalls();
	public Fc3dZu3Balls zu3Balls = new  Fc3dZu3Balls();
	boolean isZhiXuan = true;
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
	public void initRadioGroup(){
		topButton = (RadioGroup) findViewById(R.id.buy_zixuan_radiogroup_top);
		for(int i=0;i<topTitle.length;i++){
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
	private void createZhiXuan(){
		createView(zhixuanBalls,this,false);
	}
	/**
	 * 组3
	 */
	private void createZu3(){
		createView(zu3Balls,this,false);
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
				isZhiXuan = true;
				createZhiXuan();
				break;
			case 1:// 组3
				isZhiXuan = false;
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
		int iZhuShu = balls.size()* iProgressBeishu;
		betAndGift.setSellway("1");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_FC3D);
	}

}
