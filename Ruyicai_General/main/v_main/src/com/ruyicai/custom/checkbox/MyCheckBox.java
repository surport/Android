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
	private boolean isHorizontal = false;// 标题和赔率是否是横向

	public MyCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		title = context.getString(attrs.getAttributeResourceValue(NAMESPACE,
				ATTR_TITLE, DEFAULTVALUE_DEGREES));
		onChecked();
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

	public void setHorizontal(boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean getChecked() {
		return isChecked;
	}

	private void onChecked() {
		setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isCheck) {
				// TODO Auto-generated method stub
				isChecked = isCheck;
			}
		});
	}

	public void setCheckText(String text) {
		this.text = text;
	}

	public void setCheckTitle(String title) {
		this.title = title;
	}

	public String getChcekTitle() {
		return title;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setTypeface(null);
		mPaint.setColor(Color.BLACK);
		float width = getWidth();// 组建的宽度
		int titleSize = 13;
		int contentSize = 13;// 赔率大小
		int titleX = 20;// 标题x坐标
		int titleY = 18;// 标题y坐标
		int contentX = 20;// 标题x坐标
		int contentY = 42;// 标题y坐标

		if (isHorizontal) {
			titleX = 10;
			titleY = 25;
			contentX = 45;
			contentY = 25;
			if (title.equals("")) {
				contentX = 20;
				contentSize = 18;
			}
		} else {
			if (width < 100) {
				if (text.length() > 3) {
					titleX = 3;
					contentX = 3;
				} else {
					titleX = 10;
					contentX = 10;
				}
				contentSize = 13;
			} else {
				if (title.length() > 3) {
					titleSize = 13;
					titleX = 1;
				} else if (title.length() == 1) {
					titleX = 15;
				} else if (title.length() == 2) {
					titleX = 25;
				}
			}
		}
		// 标题
		mPaint.setTextSize(PublicMethod.getPxInt(titleSize, context));
		canvas.drawText(title, PublicMethod.getPxInt(titleX, context),
				PublicMethod.getPxInt(titleY, context), mPaint);
		// 赔率
		mPaint.setTextSize(PublicMethod.getPxInt(contentSize, context));
		mPaint.setColor(Color.GRAY);
		canvas.drawText(text, PublicMethod.getPxInt(contentX, context),
				PublicMethod.getPxInt(contentY, context), mPaint);

	}

}
