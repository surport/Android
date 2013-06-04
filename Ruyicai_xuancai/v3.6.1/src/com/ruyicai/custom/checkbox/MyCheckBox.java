package com.ruyicai.custom.checkbox;


import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MyCheckBox extends CheckBox {
	private static final String NAMESPACE = "http://www.ruyicai.com/res/custom";
	private static final String NAME = "http://schemas.android.com/apk/res/android";
	private static final String ATTR_TITLE = "check_title";
	private static final String ATTR_HEIGHT = "layout_height";
	private static final int DEFAULTVALUE_DEGREES = 0;
	private String title;
	private String text;
	private Context context;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean isChecked = false;
	private int height;
	private int position;

	public MyCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		title = context.getString(attrs.getAttributeResourceValue(NAMESPACE, ATTR_TITLE,DEFAULTVALUE_DEGREES));
		onChecked();
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public boolean getChecked(){
		return isChecked;
	}
	private void onChecked() {
		setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isCheck) {
				// TODO Auto-generated method stub
				isChecked = isCheck;
			}
		});
	}
	public void setCheckText(String text){
		this.text = text;
	}
	public void setCheckTitle(String title){
		this.title = title;
	}
	public String getChcekTitle(){
		return title;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setTypeface(null);
		mPaint.setColor(Color.BLACK);
		int width = PublicMethod.getDisplayWidth(context);
//		if(width >= 480){
//			mPaint.setTextSize(18);
//			canvas.drawText(title, 5, 25, mPaint);
//			mPaint.setTextSize(22);
//			mPaint.setColor(Color.GRAY);
//			canvas.drawText(text, 45, 62, mPaint);
//		}else if(width <= 320){
//			mPaint.setTextSize(13);
//			canvas.drawText(title, 0, 18, mPaint);
//			mPaint.setTextSize(15);
//			mPaint.setColor(Color.GRAY);
//			canvas.drawText(text, 20, 42, mPaint);
//		}
		if(title.length()>3){
			mPaint.setTextSize(PublicMethod.getPxInt(13, context));
			canvas.drawText(title,PublicMethod.getPxInt(1, context), PublicMethod.getPxInt(18, context), mPaint);
		}else if(title.length()==1){
			mPaint.setTextSize(PublicMethod.getPxInt(15, context));
			canvas.drawText(title,PublicMethod.getPxInt(35, context), PublicMethod.getPxInt(18, context), mPaint);
		}else if(title.length()==2){
			mPaint.setTextSize(PublicMethod.getPxInt(15, context));
			canvas.drawText(title,PublicMethod.getPxInt(25, context), PublicMethod.getPxInt(18, context), mPaint);
		}else if(title.length()==3){
			mPaint.setTextSize(PublicMethod.getPxInt(15, context));
			canvas.drawText(title,PublicMethod.getPxInt(20, context), PublicMethod.getPxInt(18, context), mPaint);
		}
		mPaint.setTextSize(PublicMethod.getPxInt(16, context));
		mPaint.setColor(Color.GRAY);
		canvas.drawText(text,PublicMethod.getPxInt(20, context), PublicMethod.getPxInt(42, context), mPaint);
		
	}	

}
