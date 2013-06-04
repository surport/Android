package com.ruyicai.activity.buy.dlt;
 
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.DanshiJiXuan;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.DltSingleBalls;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.Constants;
 
public class DltRandomChose extends DanshiJiXuan implements BuyImplement{
     
    private  ToggleButton  zhuijiatouzhu;
    private int singleLotteryValue = 2;
     
    public DltSingleBalls dltSingleBalls = new DltSingleBalls();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout toggleLinear = (LinearLayout)findViewById(R.id.buy_zixuan_linear_toggle);
        toggleLinear.setVisibility(LinearLayout.VISIBLE);
        zhuijiatouzhu = (ToggleButton)findViewById(R.id.dlt_zhuijia);
        zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {             
 
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
    			if(isChecked){
					singleLotteryValue = 3;
					betAndGift.setIssuper("0");
				}else{
					singleLotteryValue = 2;
					betAndGift.setIssuper("");
				}
                setOneValue(singleLotteryValue);
                 
            }
        });
        createView(dltSingleBalls,this,true);
    }

    @Override
    public void isTouzhu() {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
        // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void touzhuNet() {
		betAndGift.setSellway("1");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_DLT);
		betAndGift.setAmt(singleLotteryValue);
		
	}
 
} 