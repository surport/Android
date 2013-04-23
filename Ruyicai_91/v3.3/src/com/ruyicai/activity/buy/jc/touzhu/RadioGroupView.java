package com.ruyicai.activity.buy.jc.touzhu;

import java.util.ArrayList;
import java.util.List;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.graphics.Color;
import android.test.IsolatedContext;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;

/**
 * 投注选择项组类
 * @author Administrator
 *
 */
public class RadioGroupView {
	private Context context;
	private int checkNum;//选项组个数
	private final int LineNum = 4;
	private List<RadioButton> radioBtns = new ArrayList<RadioButton>();
	private int radioTextId[] = {R.string.jc_touzhu_radio3_3,R.string.jc_touzhu_radio3_4,R.string.jc_touzhu_radio4_4,R.string.jc_touzhu_radio4_5,R.string.jc_touzhu_radio4_6
			                  ,R.string.jc_touzhu_radio4_11,R.string.jc_touzhu_radio5_5,R.string.jc_touzhu_radio5_6,R.string.jc_touzhu_radio5_10,R.string.jc_touzhu_radio5_16
			                  ,R.string.jc_touzhu_radio5_20,R.string.jc_touzhu_radio5_26,R.string.jc_touzhu_radio6_6,R.string.jc_touzhu_radio6_7,R.string.jc_touzhu_radio6_15
			                  ,R.string.jc_touzhu_radio6_20,R.string.jc_touzhu_radio6_22,R.string.jc_touzhu_radio6_35,R.string.jc_touzhu_radio6_42,R.string.jc_touzhu_radio6_50
			                  ,R.string.jc_touzhu_radio6_57,R.string.jc_touzhu_radio7_7,R.string.jc_touzhu_radio7_8,R.string.jc_touzhu_radio7_21,R.string.jc_touzhu_radio7_35
			                  ,R.string.jc_touzhu_radio7_120,R.string.jc_touzhu_radio8_8,R.string.jc_touzhu_radio8_9,R.string.jc_touzhu_radio8_28,R.string.jc_touzhu_radio8_56
			                  ,R.string.jc_touzhu_radio8_70,R.string.jc_touzhu_radio8_247};
	private TouzhuDialog touzhuDialog;
	private int oneAmt = 2;//单注金额
	private boolean isDan = false;
	private int maxTeam = 8;//最大串关数，8代表8串一
	CheckBox checks[] = new CheckBox[7];
	
	public RadioGroupView(Context context,TouzhuDialog touzhuDialog){
		this.context = context;
		this.touzhuDialog = touzhuDialog;
		setMaxTeam(this.touzhuDialog.getTeamNum());
	}
	public View createView(boolean isDan,int teamNum){
		this.isDan = isDan;
		if(isDan){
			return createDanView(teamNum);
		}else{
			return createDuoView(teamNum);
		}
	}
	/**
	 * 设置最大串关数
	 */
	public void setMaxTeam(int teamNum){
		maxTeam = teamNum;
	}
	/**
	 * 创建单式机选界面
	 * @param number
	 * @return
	 */
    public View createDanView(int teamNum){
    	touzhuDialog.zhuShu = 0;
    	touzhuDialog.setAlertText();
    	if(teamNum>maxTeam){
    		teamNum = maxTeam;
    	}
    	int danNum = getNum(teamNum,true);
       	LinearLayout layoutMain = new LinearLayout(context);
       	layoutMain.setOrientation(LinearLayout.VERTICAL);
    	if(danNum>LineNum){
    		int num = danNum%LineNum;
    		int line = danNum/LineNum;
    		for(int i=0;i<line;i++){
    		   addLine(layoutMain,i,LineNum);
    		}
    		if(num!=0){
    		   addLine(layoutMain,line,num);
    		}
    	}else{
    		LinearLayout layoutOne = new LinearLayout(context);
			addLine(layoutMain,0,danNum);
    	}
    	
    	return  layoutMain;
    }
    /**
     * 加载每一行的单选按钮
     * @param layoutMain
     * @param line
     * @param lineNum
     */
	private void addLine(LinearLayout layoutMain, int line,int lineNum) {
		LinearLayout layoutOne = new LinearLayout(context);
		int id =0;
		for(int j=0;j<lineNum;j++){
			id = line*this.LineNum+j;
		    RadioButton radio = new RadioButton(context);
		    radio.setText(radioTextId[id]);
		    radio.setTextColor(Color.BLACK);
		    radio.setTextSize(PublicMethod.getPxInt(10,context));
		    radio.setId(id);
		    radio.setButtonDrawable(R.drawable.radio_select);
		    radio.setPadding(PublicMethod.getPxInt(25,context), 0, 0, 0);
		    int withPx = PublicMethod.getPxInt(75,context);//将dip换算成px
		    radio.setLayoutParams(new LayoutParams(withPx, LayoutParams.WRAP_CONTENT));
		    radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						touzhuDialog.zhuShu = getRadioZhu(buttonView.getText().toString());
						touzhuDialog.setAlertText();
						setRadioPrize(buttonView.getText().toString());
						clearRadio(buttonView);	
					}
				}

			
			});
		    radioBtns.add(radio);
		    layoutOne.addView(radio);
		}
		layoutMain.addView(layoutOne);
	}
	/**
	 * 清空单选按钮
	 * @param buttonView
	 */
	private void clearRadio(CompoundButton buttonView) {
		for(RadioButton radio:radioBtns){
			if(radio.isChecked()&&radio.getId()!=buttonView.getId()){
				radio.setChecked(false);
			}
		}
	}

	/**
	 * 创建多选界面
	 * @param visiableNum
	 * @return
	 */
    public View createDuoView(int teamNum){
    	touzhuDialog.zhuShu = 0;
    	touzhuDialog.setAlertText();
    	if(teamNum>maxTeam){
    		teamNum = maxTeam;
    	}
    	int num = getNum(teamNum,false);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View v= inflater.inflate(R.layout.buy_jc_touzhu_group_duo, null);
		int[] checkId = {R.id.checkBox1,R.id.checkBox2,R.id.checkBox3,R.id.checkBox4,R.id.checkBox5,R.id.checkBox6,R.id.checkBox7};
        for(int i=0;i<checkId.length;i++){
        	checks[i] = (CheckBox) v.findViewById(checkId[i]);
        	checks[i].setId(i);
        	if(i>=num){
        		checks[i].setVisibility(CheckBox.GONE);
        	}else{
        		checks[i].setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						int checknum = buttonView.getId();
						if(isChecked){
							touzhuDialog.freedomMaxprize += touzhuDialog.getFreedomMaxPrize(checknum+2);
							touzhuDialog.zhuShu+=touzhuDialog.getZhushu(buttonView.getId()+2);
						}else{
							touzhuDialog.freedomMaxprize -= touzhuDialog.getFreedomMaxPrize(checknum+2);
							touzhuDialog.zhuShu-=touzhuDialog.getZhushu(buttonView.getId()+2);
						}
						int mixPrize = isMixChecked();
						if(mixPrize==0){
							touzhuDialog.freedomMixprize = 0;
						}else{
							touzhuDialog.freedomMixprize = touzhuDialog.getFreedomMixPrize(mixPrize);
						}
						touzhuDialog.setAlertText();
						touzhuDialog.setPrizeText();
					}
				});
        	}
        }
    	return v;
    }
    
    public int isMixChecked(){
    	for(int i=0;i<checks.length;i++){
    		if(checks[i].isChecked()){
    			return i+2;
    		}
    	}
    	return 0;
    }
	/**
	 * 根据选则的球队计算单选按钮数
	 * @param num
	 * @return
	 */
	public int getNum(int teamNum,boolean isDan){
		//isDan=true是多串过关
		int num = 0;
		switch(teamNum){
		case 2:
			if(isDan){
				num = 0;
			}else{
				num = 1;
			}
			break;
		case 3:
			if(isDan){
				num = 2;
			}else{
				num = 2;
			}
			break;
		case 4:
			if(isDan){
				num = 6;
			}else{
				num = 3;
			}
			break;
		case 5:
			if(isDan){
				num = 12;
			}else{
				num = 4;
			}
			break;
		case 6:
			if(isDan){
				num = 21;
			}else{
				num = 5;
			}
			break;
		case 7:
			if(isDan){
				num = 26;
			}else{
				num = 6;
			}
			break;
		case 8:
			if(isDan){
				num = 32;
			}else{
				num = 7;
			}
			break;
		default:
			if(isDan){
				num = 32;
			}else{
				num = 7;
			}
		}
		return num;
	}
	/**
	 * 获得投注时的注码
	 * @return
	 */
	public String getBetCode(){
		if(isDan){
			return getRadioCode();
		}else {
			return getCheckCode();
		}
	}
	/**
	 * 获得多选注码
	 * @param check
	 * @return
	 */
	private String getCheckCode() {
		String betCode = "";
		for(CheckBox check:checks){
			if(check.isChecked()){
			  betCode += touzhuDialog.getBetCode(check.getText().toString())+"_"+PublicMethod.isTen(touzhuDialog.getBeishu())+"_"+oneAmt*100+"_"+touzhuDialog.getZhushu(check.getId()+2)*oneAmt*100+"!";
			}
		}
		return betCode.substring(0, betCode.length()-1);
	}
	/**
	 * 获得单选注码
	 * @param check
	 * @return
	 */
	private String getRadioCode() {
		String betCode = "";
		for(RadioButton radio:radioBtns){
			if(radio.isChecked()){
			  betCode += touzhuDialog.getBetCode(radio.getText().toString())+"_"+PublicMethod.isTen(touzhuDialog.getBeishu())+"_"+oneAmt*100+"_"+getRadioZhu(radio.getText().toString())*oneAmt*100+"!";
			}
		}
		return betCode.substring(0, betCode.length()-1);
	}
	private int getRadioZhu(String radioText){
		int zhuShu = 0;
		if(radioText.equals(getString(R.string.jc_touzhu_radio3_3))){
			zhuShu = touzhuDialog.getDuoZhushu(3,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio3_4))){
			zhuShu = touzhuDialog.getDuoZhushu(3,2)+touzhuDialog.getDuoZhushu(3,3);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio4_4))){
			zhuShu = touzhuDialog.getDuoZhushu(4,3);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio4_5))){
			zhuShu = touzhuDialog.getDuoZhushu(4,3)+touzhuDialog.getDuoZhushu(4,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio4_6))){
			zhuShu = touzhuDialog.getDuoZhushu(4,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio4_11))){
			zhuShu = touzhuDialog.getDuoZhushu(4,2)+touzhuDialog.getDuoZhushu(4,3)+touzhuDialog.getDuoZhushu(4,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_5))){
			zhuShu = touzhuDialog.getDuoZhushu(5,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_6))){
			zhuShu = touzhuDialog.getDuoZhushu(5,4)+touzhuDialog.getDuoZhushu(5,5);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_10))){
			zhuShu = touzhuDialog.getDuoZhushu(5,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_16))){
			zhuShu = touzhuDialog.getDuoZhushu(5,3)+touzhuDialog.getDuoZhushu(5,4)+touzhuDialog.getDuoZhushu(5,5);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_20))){
			zhuShu = touzhuDialog.getDuoZhushu(5,2)+touzhuDialog.getDuoZhushu(5,3);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_26))){
			zhuShu = touzhuDialog.getDuoZhushu(5,2)+touzhuDialog.getDuoZhushu(5,3)+touzhuDialog.getDuoZhushu(5,4)
					+touzhuDialog.getDuoZhushu(5,5);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_6))){
			zhuShu = touzhuDialog.getDuoZhushu(6,5);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_7))){
			zhuShu = touzhuDialog.getDuoZhushu(6,5)+touzhuDialog.getDuoZhushu(6,6);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_15))){
			zhuShu = touzhuDialog.getDuoZhushu(6,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_20))){
			zhuShu = touzhuDialog.getDuoZhushu(6,3);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_22))){
			zhuShu = touzhuDialog.getDuoZhushu(6,4)+touzhuDialog.getDuoZhushu(6,5)+touzhuDialog.getDuoZhushu(6,6);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_35))){
			zhuShu = touzhuDialog.getDuoZhushu(6,2)+touzhuDialog.getDuoZhushu(6,3);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_42))){
			zhuShu = touzhuDialog.getDuoZhushu(6,3)+touzhuDialog.getDuoZhushu(6,4)+touzhuDialog.getDuoZhushu(6,5)
					+touzhuDialog.getDuoZhushu(6,6);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_50))){
			zhuShu = touzhuDialog.getDuoZhushu(6,2)+touzhuDialog.getDuoZhushu(6,3)+touzhuDialog.getDuoZhushu(6,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_57))){
			zhuShu = touzhuDialog.getDuoZhushu(6,2)+touzhuDialog.getDuoZhushu(6,3)+touzhuDialog.getDuoZhushu(6,4)
					+touzhuDialog.getDuoZhushu(6,5)+touzhuDialog.getDuoZhushu(6,6);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio7_7))){
			zhuShu = touzhuDialog.getDuoZhushu(7,6);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio7_8))){
			zhuShu = touzhuDialog.getDuoZhushu(7,6)+touzhuDialog.getDuoZhushu(7,7);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio7_21))){
			zhuShu = touzhuDialog.getDuoZhushu(7,5);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio7_35))){
			zhuShu = touzhuDialog.getDuoZhushu(7,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio7_120))){
			zhuShu = touzhuDialog.getDuoZhushu(7,2)+touzhuDialog.getDuoZhushu(7,3)+touzhuDialog.getDuoZhushu(7,4)
					+touzhuDialog.getDuoZhushu(7,5)+touzhuDialog.getDuoZhushu(7,6)+touzhuDialog.getDuoZhushu(7,7);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_8))){
			zhuShu = touzhuDialog.getDuoZhushu(8,7);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_9))){
			zhuShu = touzhuDialog.getDuoZhushu(8,7)+touzhuDialog.getDuoZhushu(8,8);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_28))){
			zhuShu = touzhuDialog.getDuoZhushu(8,6);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_56))){
			zhuShu = touzhuDialog.getDuoZhushu(8,5);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_70))){
			zhuShu = touzhuDialog.getDuoZhushu(8,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_247))){
			zhuShu = touzhuDialog.getDuoZhushu(8,2)+touzhuDialog.getDuoZhushu(8,3)+touzhuDialog.getDuoZhushu(8,4)
					+touzhuDialog.getDuoZhushu(8,5)+touzhuDialog.getDuoZhushu(8,6)+touzhuDialog.getDuoZhushu(8,7)
					+touzhuDialog.getDuoZhushu(8,8);
		}
		return zhuShu;
	}
	private void setRadioPrize(String radioText){
		if(radioText.equals(getString(R.string.jc_touzhu_radio3_3))){
		    touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(3,2);
		    touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(3,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio3_4))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(3,2)+touzhuDialog.getFreedomDuoMaxPrize(3,3);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(3,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio4_4))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(4,3);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(4,3);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio4_5))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(4,3)+touzhuDialog.getFreedomDuoMaxPrize(4,4);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(4,3);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio4_6))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(4,2);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(4,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio4_11))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(4,2)+touzhuDialog.getFreedomDuoMaxPrize(4,3)+touzhuDialog.getFreedomDuoMaxPrize(4,4);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(4,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_5))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(5,4);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(5,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_6))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(5,4)+touzhuDialog.getFreedomDuoMaxPrize(5,5);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(5,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_10))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(5,2);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(5,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_16))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(5,3)+touzhuDialog.getFreedomDuoMaxPrize(5,4)+touzhuDialog.getFreedomDuoMaxPrize(5,5);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(5,3);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_20))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(5,2)+touzhuDialog.getFreedomDuoMaxPrize(5,3);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(5,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio5_26))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(5,2)+touzhuDialog.getFreedomDuoMaxPrize(5,3)+touzhuDialog.getFreedomDuoMaxPrize(5,4)
					+touzhuDialog.getFreedomDuoMaxPrize(5,5);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(5,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_6))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(6,5);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(6,5);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_7))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(6,5)+touzhuDialog.getFreedomDuoMaxPrize(6,6);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(6,5);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_15))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(6,2);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(6,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_20))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(6,3);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(6,3);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_22))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(6,4)+touzhuDialog.getFreedomDuoMaxPrize(6,5)+touzhuDialog.getFreedomDuoMaxPrize(6,6);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(6,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_35))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(6,2)+touzhuDialog.getFreedomDuoMaxPrize(6,3);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(6,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_42))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(6,3)+touzhuDialog.getFreedomDuoMaxPrize(6,4)+touzhuDialog.getFreedomDuoMaxPrize(6,5)
					+touzhuDialog.getFreedomDuoMaxPrize(6,6);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(6,3);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_50))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(6,2)+touzhuDialog.getFreedomDuoMaxPrize(6,3)+touzhuDialog.getFreedomDuoMaxPrize(6,4);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(6,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio6_57))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(6,2)+touzhuDialog.getFreedomDuoMaxPrize(6,3)+touzhuDialog.getFreedomDuoMaxPrize(6,4)
					+touzhuDialog.getFreedomDuoMaxPrize(6,5)+touzhuDialog.getFreedomDuoMaxPrize(6,6);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(6,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio7_7))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(7,6);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(7,6);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio7_8))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(7,6)+touzhuDialog.getFreedomDuoMaxPrize(7,7);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(7,6);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio7_21))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(7,5);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(7,5);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio7_35))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(7,4);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(7,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio7_120))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(7,2)+touzhuDialog.getFreedomDuoMaxPrize(7,3)+touzhuDialog.getFreedomDuoMaxPrize(7,4)
					+touzhuDialog.getFreedomDuoMaxPrize(7,5)+touzhuDialog.getFreedomDuoMaxPrize(7,6)+touzhuDialog.getFreedomDuoMaxPrize(7,7);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(7,2);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_8))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(8,7);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(8,7);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_9))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(8,7)+touzhuDialog.getFreedomDuoMaxPrize(8,8);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(8,7);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_28))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(8,6);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(8,6);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_56))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(8,5);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(8,5);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_70))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(8,4);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(8,4);
		}else if(radioText.equals(getString(R.string.jc_touzhu_radio8_247))){
			touzhuDialog.freedomMaxprize = touzhuDialog.getFreedomDuoMaxPrize(8,2)+touzhuDialog.getFreedomDuoMaxPrize(8,3)+touzhuDialog.getFreedomDuoMaxPrize(8,4)
					+touzhuDialog.getFreedomDuoMaxPrize(8,5)+touzhuDialog.getFreedomDuoMaxPrize(8,6)+touzhuDialog.getFreedomDuoMaxPrize(8,7)
					+touzhuDialog.getFreedomDuoMaxPrize(8,8);
			touzhuDialog.freedomMixprize = touzhuDialog.getFreedomDuoMixPrize(8,2);
		}
		touzhuDialog.setPrizeText();
	}
	private String getString(int id) {
		return context.getString(id);
	}
}
