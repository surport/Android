package com.ruyicai.activity.buy.miss.onclik;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.activity.buy.miss.ZHmissViewItem;
import com.ruyicai.activity.buy.miss.ZHmissViewItem.BtnArea;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.OneBallView;

public class ButtonOnclik extends BaseOnclik{
	private float[] areaNumTopY;//每个选区顶部的对应的y值 
	private float[] areaNumBotY;//每个选区底部的对应的y值 
	ZHmissViewItem missView;
	public List<MyButton> missBtnList;
	public List<BtnArea> areaList;
	private LinearLayout radioLayout;
	private int radioHeight;
	private int layoutTop;
	private int width;
	private int padding;
	
	public ButtonOnclik(ZHmissViewItem missView){
      this.missView = missView;
      this.missBtnList = missView.missBtnList;
      this.areaList = missView.areaList;
	  areaNumTopY = new float[areaList.size()];
	  areaNumBotY = new float[areaList.size()];
	}
	public void setRadioLayout(LinearLayout radioLayout) {
		this.radioLayout = radioLayout;
	}
	public void setLayoutMian(View layoutMain) {
		// TODO Auto-generated method stub
		this.layoutMain = layoutMain;
	}
	public void onClik(float x, float y) {
		// TODO Auto-generated method stub
		initInfo(); 
        int index = getIndex(x,y);
        if(index!=-1&&index<missBtnList.size()){
        	missBtnList.get(index).onAction();
        	missView.zixuan.changeTextSumMoney();
        }
	}
	private int getIndex(float x, float y){
		 int yIndex = getHIndex(y);
		 int xIndex = getLIndex(x);
		 if(yIndex ==-1||xIndex == -1){
			 return -1;
		 }else{
	         return yIndex*missView.LINE_NUM+xIndex;
		 }
	}
	 /**
	  * 根据y值判断是小球的行数和选区数
	  * @return -1代表不是任何一个选区
	  */
	 public int getHIndex(float y){
		 int indexs = -1 ;
		 for(int i=0;i<areaNumTopY.length;i++){
	          if(y>areaNumTopY[i] &&y<areaNumBotY[i]){
	        	  indexs = i;
	        	  return indexs;
	          }else{
	     		 indexs = -1;
	          }
		 } 
		 return indexs; 
	 }
	 /**
	  * 根据x值判断是哪个小球
	  * @return
	  */
	 public int getLIndex(float x){
		int index = (int) (x/(width+2*padding));
		return index; 
	 }
	private void initInfo() {
		 if(layoutTop == 0){
			 if(radioLayout != null){
				 radioHeight = radioLayout.getTop();
			 }
			 if(layoutMain!=null){
				 layoutHeight = layoutMain.getTop();
			 }
			 initAreaNumY();
			 layoutTop = areaList.get(0).getLayoutArea().getTop();
			 width = areaList.get(0).getWidth();
			 padding = areaList.get(0).getPadding();
		 }
	}
    public void initAreaNumY(){
		 for(int i=0;i<areaList.size();i++){
			 areaNumTopY[i] = areaList.get(i).getLayoutArea().getTop() + radioHeight + layoutHeight;
			 areaNumBotY[i] = areaNumTopY[i] + areaList.get(i).getHeight();
		 }
	}

}
