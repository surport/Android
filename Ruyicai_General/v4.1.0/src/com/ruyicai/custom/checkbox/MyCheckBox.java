package com.ruyicai.custom.checkbox;

import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
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
	private String title = "";
	private String text = "";
	private Context context;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean isChecked = false;
	private int height;
	private int position;
	private boolean isHorizontal = false;// 标题和赔率是否是横向
	/**add by yejc 20130816 start */
	private Handler handler = null; 
	private int oddsPaintColorArray[] = {Color.BLACK, Color.BLACK};
	private int textPaintColorArray[] = {Color.BLACK, Color.BLACK};
	private String lotno = "";
	/**add by yejc 20130816 start */

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
	
	public boolean getHorizontal(){
		return this.isHorizontal;
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
	
	/**add by yejc 20130816 start*/
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	public Handler getHandler(){
		return this.handler;
	}
	
	
	public int[] getOddsPaintColorArray() {
		return oddsPaintColorArray;
	}

	public void setOddsPaintColorArray(int[] oddsPaintColorArray) {
		this.oddsPaintColorArray = oddsPaintColorArray;
	}

	public int[] getTextPaintColorArray() {
		return textPaintColorArray;
	}

	public void setTextPaintColorArray(int[] textPaintColorArray) {
		this.textPaintColorArray = textPaintColorArray;
	}
	public String getLotno() {
		return lotno;
	}

	public void setLotno(String lotno) {
		this.lotno = lotno;
	}

	/**add by yejc 20130816 end*/

	private void onChecked() {
		setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isCheck) {
				isChecked = isCheck;
				/**add by yejc 20130816 start*/
				if (handler != null) {
					handler.sendEmptyMessage(0);
				}
				/**add by yejc 20130816 end*/
			}
		});
	}

	public void setCheckText(String text) {
		this.text = text;
	}
	
	public String getCheckText(){
		return this.text;
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
//		mPaint.setFakeBoldText(true);
		if (isChecked) {
			mPaint.setColor(textPaintColorArray[1]);
		} else {
			mPaint.setColor(textPaintColorArray[0]);
		}
		
		float width = getWidth();// 组建的宽度
		int titleSize = 13;
		int contentSize = 13;// 赔率大小
		int titleY = 18;// 标题y坐标
		int contentY = 42;// 标题y坐标

		if (isHorizontal) {
			if (Constants.LOTNO_JQC.equals(lotno)) {
				titleY = 30;
				contentY = 30;
			} else {
				titleY = 25;
				contentY = 25;
			}
			if (title.equals("")) {
				contentSize = 18;
			}
		} else {
			float standardWidth = PublicMethod.getDisplayWidth(context)/480.0f * 100;
			
			if (width < standardWidth) {
				contentSize = 13;
			} else {
				if (title.length() > 3) {
					titleSize = 13;
				} 
			}
		}
		// 标题
		mPaint.setTextSize(PublicMethod.getPxInt(titleSize, context));
		/**add by yejc 20130819 start*/
		float titleWidth = 0.0f;
		if (title != null) {
			titleWidth = mPaint.measureText(title);
		}
		float oddsWidth = 0.0f;
		if (text != null) {
			oddsWidth = mPaint.measureText(text);
		}
		
		float space = (width - titleWidth - oddsWidth)/3;
		if (isHorizontal) {
			if (text != null && !"".equals(text)) {
				canvas.drawText(title, space,
						PublicMethod.getPxInt(titleY, context), mPaint);
			} else {
				canvas.drawText(title, (width - titleWidth)/2,
						PublicMethod.getPxInt(titleY, context), mPaint);
			}
			
		} else {
			canvas.drawText(title, (width - titleWidth)/2,
					PublicMethod.getPxInt(titleY, context), mPaint);
		}
		
		// 赔率
		mPaint.setTextSize(PublicMethod.getPxInt(contentSize, context));
		if (isChecked) {
			mPaint.setColor(oddsPaintColorArray[1]);
		} else {
			mPaint.setColor(oddsPaintColorArray[0]);
		}
		if (isHorizontal) {
			if ((Constants.LOTNO_JCLQ_HUN.equals(lotno)) && 
					(position == 3 || position == 6)) {
				canvas.drawText(text, (width - oddsWidth)/2.5f,
						PublicMethod.getPxInt(contentY, context), mPaint);
			} else {
				canvas.drawText(text, 2*space + titleWidth,
						PublicMethod.getPxInt(contentY, context), mPaint);
			}
		} else {
			canvas.drawText(text, (width - oddsWidth)/2,
					PublicMethod.getPxInt(contentY, context), mPaint);
		}
	}

}
