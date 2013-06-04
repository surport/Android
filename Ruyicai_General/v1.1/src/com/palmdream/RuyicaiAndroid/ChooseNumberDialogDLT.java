package com.palmdream.RuyicaiAndroid;
import com.palmdream.RuyicaiAndroid.R;
import com.palmdream.RuyicaiAndroid.R.id;
import com.palmdream.RuyicaiAndroid.R.layout;
import com.palmdream.RuyicaiAndroid.R.string;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class ChooseNumberDialogDLT extends Dialog implements OnClickListener,SeekBar.OnSeekBarChangeListener{
	private Button okButton;
	private Button cancelButton;
	MyDialogListener iListener;
	
	int iWhichLayout; // 0 复式
					  // 1 胆拖
	
	SeekBar mSeekBarRed;
	SeekBar mSeekBarRedTuo;
	SeekBar mSeekBarBlue;
	SeekBar mSeekBarBlueTuo;
	SeekBar mSeekBarBlue12;
	
	int iProgressRed=5;
	int iProgressRedTuo=1;
	int iProgressBlue=1;
	int iProgressBlueTuo=1;
	int iProgressBlue12=2;
	
	TextView iDialogTip;
	TextView iTVRed;
	TextView iTVRedTuo;
	TextView iTVBlue;
	TextView iTVBlueTuo;
	TextView iTVBlue12;
	
	public ChooseNumberDialogDLT(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ChooseNumberDialogDLT(Context context,int aWhichDialog,MyDialogListener aListener) {
		super(context);
		// TODO Auto-generated constructor stub
		// 复式
		iWhichLayout = aWhichDialog;
		this.iListener=aListener;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(iWhichLayout==0){
			setContentView(R.layout.choose_number_dlt_fushi_dialog);
			iDialogTip = (TextView)findViewById(R.id.ssq_fushi_dialog_game_type_tip_change);
			updateDialogTips();
			iProgressBlue=2;
			iTVRed = (TextView)findViewById(R.id.ssq_fushi_dialog_tv_number_change);
			iTVRed.setText(""+iProgressRed);
			iTVBlue = (TextView)findViewById(R.id.ssq_fushi_dialog_tv_blue_ball_number_change);
			iTVBlue.setText(""+iProgressBlue);
		
			mSeekBarRed=(SeekBar)findViewById(R.id.ssq_fushi_dialog_seekbar_red);
			mSeekBarRed.setProgress(iProgressRed);
			mSeekBarRed.setOnSeekBarChangeListener(this);
			mSeekBarBlue=(SeekBar)findViewById(R.id.ssq_fushi_dialog_blue_ball_seekbar);
			mSeekBarBlue.setOnSeekBarChangeListener(this);
			mSeekBarBlue.setProgress(iProgressBlue);
			/*
	    	 * 点击加减图标，对seekbar进行设置： 
	    	 * @param   ImageButton imageButton
	    	 * 			final SeekBar mSeekBar
	    	 * 			final int isAdd, 1表示加 -1表示减
	    	 * @return void
	    	 */
			ImageButton ssq_fushi_red_add = (ImageButton)findViewById(R.id.ssq_fushi_dialog_red_add);
			ImageButton ssq_fushi_red_subtract = (ImageButton)findViewById(R.id.ssq_fushi_dialog_red_subtract);
			ImageButton ssq_fushi_blue_add = (ImageButton)findViewById(R.id.ssq_fushi_dialog_blue_add);
			ImageButton ssq_fushi_blue_subtract = (ImageButton)findViewById(R.id.ssq_fushi_dialog_blue_subtract);
			
			setSeekWhenAddOrSub(ssq_fushi_red_add,mSeekBarRed,1,0);
			setSeekWhenAddOrSub(ssq_fushi_red_subtract,mSeekBarRed,-1,0);
		
			setSeekWhenAddOrSub(ssq_fushi_blue_add,mSeekBarBlue,1,1);
			setSeekWhenAddOrSub(ssq_fushi_blue_subtract,mSeekBarBlue,-1,1);

			setTitle(R.string.choose_number_dialog_title_fushi);
		}
		else if(iWhichLayout==1){
			setContentView(R.layout.choose_number_dlt_dantuo_dialog);
			iDialogTip = (TextView)findViewById(R.id.ssq_dantuo_dialog_tv_tips);
			updateDialogTips();
		
			iProgressRed=0;
			iProgressBlue=0;
			iTVRed = (TextView)findViewById(R.id.ssq_dantuo_dialog_reddan_tv_number_change);
			iTVRed.setText(""+iProgressRed);
			
			iTVRedTuo = (TextView)findViewById(R.id.ssq_dantuo_dialog_red_tuo_number_change);
			iTVRedTuo.setText(""+iProgressRedTuo);
			
			iTVBlue = (TextView)findViewById(R.id.ssq_dantuo_dialog_blue_tv_number_change);
			iTVBlue.setText(""+iProgressBlue);
			
			iTVBlueTuo = (TextView)findViewById(R.id.ssq_dantuo_dialog_blue_tuo_number_change);
			iTVBlueTuo.setText(""+iProgressBlueTuo);
		
			mSeekBarRed=(SeekBar)findViewById(R.id.ssq_dantuo_dialog_seek_reddan);
			mSeekBarRed.setProgress(iProgressRed);
			mSeekBarRed.setOnSeekBarChangeListener(this);
			
			mSeekBarRedTuo=(SeekBar)findViewById(R.id.ssq_dantuo_dialog_seek_redtuo);
			mSeekBarRedTuo.setProgress(iProgressRedTuo);
			mSeekBarRedTuo.setOnSeekBarChangeListener(this);
			
			mSeekBarBlue=(SeekBar)findViewById(R.id.ssq_dantuo_dialog_seek_blue);
			mSeekBarBlue.setOnSeekBarChangeListener(this);
			mSeekBarBlue.setProgress(iProgressBlue);
			
			mSeekBarBlueTuo=(SeekBar)findViewById(R.id.ssq_dantuo_dialog_seek_bluetuo);
			mSeekBarBlueTuo.setProgress(iProgressBlueTuo);
			mSeekBarBlueTuo.setOnSeekBarChangeListener(this);
	       
			ImageButton ssq_dantuo_red_subtract = (ImageButton)findViewById(R.id.ssq_dantuo_dialog_reddan_subtract);
			ImageButton ssq_dantuo_red_add = (ImageButton)findViewById(R.id.ssq_dantuo_dialog_reddan_add);
			ImageButton ssq_dantuo_red_tuo_subtract = (ImageButton)findViewById(R.id.ssq_dantuo_dialog_redtuo_subtract);
			ImageButton ssq_dantuo_red_tuo_add = (ImageButton)findViewById(R.id.ssq_dantuo_dialog_redtuo_add);
			ImageButton ssq_dantuo_blue_subtract = (ImageButton)findViewById(R.id.ssq_dantuo_dialog_blue_subtract);
			ImageButton ssq_dantuo_blue_add = (ImageButton)findViewById(R.id.ssq_dantuo_dialog_blue_add);
			ImageButton ssq_dantuo_blue_tuo_subtract = (ImageButton)findViewById(R.id.ssq_dantuo_dialog_bluetuo_subtract);
			ImageButton ssq_dantuo_blue_tuo_add = (ImageButton)findViewById(R.id.ssq_dantuo_dialog_bluetuo_add);
			
			setSeekWhenAddOrSub(ssq_dantuo_red_add,mSeekBarRed,1,0);
			setSeekWhenAddOrSub(ssq_dantuo_red_subtract,mSeekBarRed,-1,0);
		
			setSeekWhenAddOrSub(ssq_dantuo_red_tuo_add,mSeekBarRedTuo,1,2);
			setSeekWhenAddOrSub(ssq_dantuo_red_tuo_subtract,mSeekBarRedTuo,-1,2);
			
			setSeekWhenAddOrSub(ssq_dantuo_blue_add,mSeekBarBlue,1,1);
			setSeekWhenAddOrSub(ssq_dantuo_blue_subtract,mSeekBarBlue,-1,1);
			
			setSeekWhenAddOrSub(ssq_dantuo_blue_tuo_add,mSeekBarBlueTuo,1,3);
			setSeekWhenAddOrSub(ssq_dantuo_blue_tuo_subtract,mSeekBarBlueTuo,-1,3);
		
			setTitle(R.string.choose_number_dialog_title_dantuo);

		}else{

			setContentView(R.layout.choose_number_dlt_twoin12_dialog);
			iDialogTip = (TextView)findViewById(R.id.ssq_fushi_dialog_game_type_tip_change);
			updateDialogTips();
		
			iTVBlue12 = (TextView)findViewById(R.id.ssq_fushi_dialog_tv_blue_ball_number_change);
			iTVBlue12.setText(""+iProgressBlue12);
		
			mSeekBarBlue12=(SeekBar)findViewById(R.id.ssq_twoin12_dialog_blue_ball_seekbar);
			mSeekBarBlue12.setOnSeekBarChangeListener(this);
			mSeekBarBlue12.setProgress(iProgressBlue12);
			/*
	    	 * 点击加减图标，对seekbar进行设置： 
	    	 * @param   ImageButton imageButton
	    	 * 			final SeekBar mSeekBar
	    	 * 			final int isAdd, 1表示加 -1表示减
	    	 * @return void
	    	 */
			ImageButton ssq_fushi_blue_add = (ImageButton)findViewById(R.id.ssq_fushi_dialog_blue_add);
			ImageButton ssq_fushi_blue_subtract = (ImageButton)findViewById(R.id.ssq_fushi_dialog_blue_subtract);
			
			setSeekWhenAddOrSub(ssq_fushi_blue_add,mSeekBarBlue12,1,1);
			setSeekWhenAddOrSub(ssq_fushi_blue_subtract,mSeekBarBlue12,-1,1);

			setTitle(R.string.choose_number_dialog_title_fushi);
		
		}
		okButton = (Button) findViewById(R.id.okButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	public void updateDialogTips(){
		if(iWhichLayout==0){
			if(iProgressRed<=5){
				if(iProgressBlue==1)
					iDialogTip.setText(R.string.choose_number_dialog_tip1);
				else
					iDialogTip.setText(R.string.choose_number_dialog_tip3);
			}
			else{
				if(iProgressBlue==2)
					iDialogTip.setText(R.string.choose_number_dialog_tip2);
				else
					iDialogTip.setText(R.string.choose_number_dialog_tip4);
			}
		}
		else{
			iDialogTip.setText(R.string.choose_number_dialog_tip9);
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub    	
		PublicMethod.myOutput("------******"+seekBar.getId());
    	switch(seekBar.getId()){
    	case R.id.ssq_fushi_dialog_seekbar_red:
    		if(progress<5)
        		seekBar.setProgress(5);
    		iProgressRed=seekBar.getProgress();
    		iTVRed.setText(""+iProgressRed);
    		updateDialogTips();
    		break;
    	case R.id.ssq_fushi_dialog_blue_ball_seekbar:
    		if(progress<2)
        		seekBar.setProgress(2);
    		iProgressBlue=seekBar.getProgress();
    		iTVBlue.setText(""+iProgressBlue);
    		updateDialogTips();
    		break;
    	case R.id.ssq_dantuo_dialog_seek_reddan:
			// if(progress<1)
			// seekBar.setProgress(1);
    		iProgressRed=seekBar.getProgress();

    		if(iProgressRedTuo+iProgressRed >22){
    			iProgressRedTuo=22-iProgressRed;
    			mSeekBarRedTuo.setProgress(iProgressRedTuo);
    			iTVRedTuo.setText(""+iProgressRedTuo);
    		}
    		iTVRed.setText(""+iProgressRed);
    		
    		updateDialogTips();
    		break;
    	case R.id.ssq_dantuo_dialog_seek_redtuo:
    		if(progress<1)
        		seekBar.setProgress(1);
    		iProgressRedTuo=seekBar.getProgress();
    		if(iProgressRedTuo+iProgressRed >22){
    			iProgressRed=22-iProgressRedTuo;
				mSeekBarRed.setProgress(iProgressRed);
    			iTVRed.setText(""+iProgressRed);
    		}
    		iTVRedTuo.setText(""+iProgressRedTuo);
    		updateDialogTips();
    		break;
    	case R.id.ssq_dantuo_dialog_seek_blue:
			// if(progress<1)
			// seekBar.setProgress(1);
    		iProgressBlue=seekBar.getProgress();
    		if(iProgressBlueTuo+iProgressBlue >12){
    			iProgressBlueTuo=12-iProgressBlue;
    			mSeekBarBlueTuo.setProgress(iProgressBlueTuo);
    			iTVBlueTuo.setText(""+iProgressBlueTuo);
    		}
    		iTVBlue.setText(""+iProgressBlue);
    		updateDialogTips();
    		break;
    	case R.id.ssq_dantuo_dialog_seek_bluetuo:
    		if(progress<1)
        		seekBar.setProgress(1);
    		iProgressBlueTuo=seekBar.getProgress();
    		if(iProgressBlueTuo+iProgressBlue >12){
    			iProgressBlue=12-iProgressBlueTuo;
    			mSeekBarBlue.setProgress(iProgressBlue);
    			iTVBlue.setText(""+iProgressBlue);
    		}
    		iTVBlueTuo.setText(""+iProgressBlueTuo);
    		updateDialogTips();
    		break;
    	case R.id.ssq_twoin12_dialog_blue_ball_seekbar:
    		if(progress<2)
        		seekBar.setProgress(2);
    		iProgressBlue12=seekBar.getProgress();
    		iTVBlue12.setText(""+iProgressBlue12);
    		updateDialogTips();
    		break;
    	default:
    		break;
    	}
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//PublicMethod.myOutput("--->>>"+v.getId());
		PublicMethod.myOutput("-----onClick"+v.getId());
        switch (v.getId()) {
        case R.id.okButton:
        	int[] iReturnInts=null;
        	if(iWhichLayout==0){
        		iReturnInts = new int [2];
        		iReturnInts[0]=iProgressRed;
        		iReturnInts[1]=iProgressBlue;
        	}
        	else if(iWhichLayout==1){
        		iReturnInts = new int [4];
        		iReturnInts[0]= iProgressRed;
        		iReturnInts[1]= iProgressRedTuo;
        		iReturnInts[2]=	iProgressBlue;
        		iReturnInts[3]=	iProgressBlueTuo;
        	}
        	else {
        		iReturnInts = new int [1];
        		iReturnInts[0]=	iProgressBlue12;
        	}
        	iListener.onOkClick(iReturnInts);
             dismiss();
             break;
       case R.id.cancelButton:
    	   iListener.onCancelClick();
             cancel();
             break;
        }
	}
	//whichGroup 判断当前是哪个seekbar 0是红球胆码或者红球 1是篮球 2是红球拖码
	private void setSeekWhenAddOrSub(ImageButton imageButton,final SeekBar seekBar,final int isAdd,final int whichGroup){
		imageButton.setOnClickListener(new ImageButton.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(whichGroup == 0){
						if(isAdd == 1)
							seekBar.setProgress(++iProgressRed);
						else
							seekBar.setProgress(--iProgressRed);
					}
				else if(whichGroup == 1){

						if(isAdd == 1)
							seekBar.setProgress(++iProgressBlue);
						else
							seekBar.setProgress(--iProgressBlue);
					
					}	
				else if(whichGroup == 2){

						if(isAdd == 1)
							seekBar.setProgress(++iProgressRedTuo);
						else
							seekBar.setProgress(--iProgressRedTuo);
					
					}else if(whichGroup == 3){

						if(isAdd == 1)
							seekBar.setProgress(++iProgressBlueTuo);
						else
							seekBar.setProgress(--iProgressBlueTuo);
					
					}
				}
		});

	}
}
