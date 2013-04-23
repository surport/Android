package com.ruyicai.custom.jc.button;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;
/**
 * 竞彩button按钮的自定义
 * @author Administrator
 *
 */
public class MyButton extends Button{
	private boolean isOnClick = false;//button是否被点击
	private int[] bgId = {R.drawable.jc_btn,R.drawable.jc_btn_b};//默认背景
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Context context;
	private String textContent;
	public MyButton(Context context){
    	super(context);
    	this.context = context;
    	initBtn();
    }
	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		initBtn();
	}
    public boolean isOnClick() {
		return isOnClick;
	}
	public void setOnClick(boolean isOnClick) {
		this.isOnClick = isOnClick;
	}
	public void initBg(int[] bgId){
		this.bgId = bgId;
	}
	/**
	 * 初始化button
	 */
	public void initBtn(){
		isOnClick = false;
		setBackgroundResource(bgId[0]);
	}
	/**
	 * 按钮被点击之后做的事情
	 */
	public void onAction(){
		isOnClick = !isOnClick;
		if(isOnClick){
			setBackgroundResource(bgId[1]);
		}else{
			setBackgroundResource(bgId[0]);
		}
	}
	/**
	 * 切换背景图片
	 */
	public void switchBg(){
		if(isOnClick){
			setBackgroundResource(bgId[1]);
		}else{
			setBackgroundResource(bgId[0]);
		}
	}
	public void setBtnText(String text){
		textContent = text;
		postInvalidate();
	}
	public String getBtnText(){
		return textContent;
	}
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setTypeface(null);
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(PublicMethod.getPxInt(15, context));
		canvas.drawText(textContent, 5, PublicMethod.getPxInt(23, context), mPaint);
		
	}	

}
