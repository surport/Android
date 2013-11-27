/**
 * @Title 小球 View类
 * @Description: 
 * @Copyright: Copyright (c) 2009
 * @Company: PalmDream
 * @author FanYaJun
 * @version 1.0 20100618
 */
package com.ruyicai.pojo;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.cq11x5.Cq11Xuan5;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

public class OneBallView extends ImageView {
	// 成员变量
	private int iWidth = 0;
	private int iHeight = 0;

	private String iShowString;
	private Paint p = null;
	private int textcolor[] = { Color.BLACK, Color.WHITE };
	private int iResId[];
	private Bitmap bitmaps[];
	private int iShowId = 0;

	private int initColor = 0;

	private int iShowStringX = 0;
	private int iShowStringY = 0;
	private int color = Color.WHITE;
	private Context context;
	
	private boolean isTextTranslate;//是否小球文字透明

	public String getiShowString() {
		return iShowString;
	}
	
	//add by yejc 20130327
	private boolean flag = false;
	public OneBallView(Context context, boolean flag) {
		super(context);
		this.context = context;
		this.flag = flag;
	}
	//end

	public OneBallView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

	}

	public OneBallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

	}

	public OneBallView(Context context) {
		super(context);
		this.context = context;

	}

	public OneBallView(Context context, int initColor) {
		super(context);
		this.context = context;
		this.initColor = initColor;
		if (initColor == 1) {
			this.textcolor[0] = Color.WHITE;
			this.textcolor[1] = Color.WHITE;
		}else if(initColor == 2){
			isTextTranslate =  true;
		}

	}

	private void onClick() {
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeBallColor();
			}
		});
	}

	/**
	 * 初始化小球宽高
	 * 
	 * @param int aWidth 宽度
	 * @param int aHeight 高度
	 * @return int 没什么用处
	 */
	public int initBallWidthHeight(int aWidth, int aHeight) {
		// 错误的参数
		if (aWidth <= 0 || aHeight <= 0)
			return -1;

		iWidth = aWidth;
		iHeight = aHeight;
		return 0;
	}

	/**
	 * 初始化小球
	 * 
	 * @param int aWidth 宽度
	 * @param int aHeight 高度
	 * @param String
	 *            sShowString 显示String
	 * @param int aResId[] 图片IDs
	 * @return int 没什么用处
	 */
	public int initBall(int aWidth, int aHeight, String sShowString,
			int aResId[]) {
		initBallWidthHeight(aWidth, aHeight);
		initBallShowString(sShowString);
		initBallBackground(aResId);
		return 0;
	}

	/**
	 * 初始化小球
	 * 
	 * @param int aWidth 宽度
	 * @param int aHeight 高度
	 * @param String
	 *            sShowString 显示String
	 * @param int aResId[] 图片IDs
	 * @return int 没什么用处
	 */
	public int initBall(int aWidth, int aHeight, String sShowString,
			int aResId[], int color) {
		this.color = color;
		initBallWidthHeight(aWidth, aHeight);
		initBallShowString(sShowString);
		initBallBackground(aResId);
		return 0;
	}

	/**
	 * 初始化小球
	 * 
	 * @param String
	 *            sShowString 显示String
	 * @return int 没什么用处
	 */
	public int initBallShowString(String aShowString) {
		// 错误的参数
		if (aShowString == null || aShowString.equalsIgnoreCase(""))
			return -1;

		iShowString = aShowString;
		setPaint(); // 设置画笔
		return 0;
	}

	Bitmap resizeBitmap = null;

	/**
	 * 初始化小球
	 * 
	 * @param int aResId[] 图片IDs
	 * @return int 没什么用处
	 */
	public int initBallBackground(int aResId[]) {
		if (aResId.length <= 0) {
			return -1;
		}

		iResId = aResId;
		bitmaps = new Bitmap[aResId.length];
		for (int i = 0; i < aResId.length; i++) {
			// 缩放图片
			Bitmap bitmap = getBitmapFromRes(iResId[i]);

			bitmaps[i] = bitmap;
		}
		return 0;
	}

	/**
	 * 回收图片资源
	 */
	public void recycleBitmaps() {
		if (bitmaps == null) {
			return;
		}

		for (int i = 0; i < bitmaps.length; i++) {
			if (bitmaps[i] != null && bitmaps[i].isRecycled() == false) {
				bitmaps[i].recycle();
				bitmaps[i] = null;
			}
		}
	}

	/*
	 * 设置画笔
	 * 
	 * @return void
	 */
	private void setPaint() {
		if (p == null) {
			p = new Paint();
			p.setFlags(Paint.ANTI_ALIAS_FLAG);
			p.setColor(color);
			p.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
			float width = Float.valueOf(Constants.SCREEN_WIDTH);
			float textSize;
			if (width > 480) {
				textSize = 22 * (width / Float.valueOf(480));
			} else {
				textSize = 24 * (width / Float.valueOf(480));
			}
			p.setTextSize(textSize);

		}
		float[] stringLength = new float[iShowString.length()];
		p.getTextWidths(iShowString, stringLength);
		float fTemp = 0;
		for (int i1 = 0; i1 < stringLength.length; i1++) {
			fTemp += stringLength[i1];
		}
		int height = PublicMethod.getDisplayHeight(context);
		iShowStringX = (int) ((iWidth - fTemp) / 2);
		if (height < 480) {
			iShowStringY = (iHeight + 10) / 2;
		} else {
			iShowStringY = (iHeight + 16) / 2;
		}

	}

	/*
	 * 显示下一张图片
	 * 
	 * @return void
	 */
	public void changeBallColor() {
		iShowId = (iShowId + 1) % iResId.length;
		this.invalidate();
	}

	/**
	 * 将小球设置成灰色背景（主要针对福彩3D组三单复式玩法）
	 */
	public void setGrey() {
		iShowId = 0;
		invalidate();
	}

	protected Bitmap getBitmapFromRes(int aResId) {
		Resources res = this.getContext().getResources();
		InputStream is = res.openRawResource(aResId);
		Bitmap bitmap = null;
		int width = 0;
		int height = 0;
		float sw;
		float sh;
		Matrix matrix;
		switch (aResId) {
		case R.drawable.grey:
		case R.drawable.nmk3_hezhi_normal:
			if (bitmap == null) {
				bitmap = new BitmapDrawable(is).getBitmap();
				Constants.grey = bitmap;
			}
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			sw = ((float) iWidth) / width;
			sh = ((float) iHeight) / height;
			matrix = new Matrix();
			matrix.postScale(sw, sh);
			if (sw != 1 && sh != 1) {
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				Constants.grey = bitmap;
			}
			bitmap = Constants.grey;
			break;
		case R.drawable.red:
		case R.drawable.nmk3_hezhi_click:
			if (bitmap == null) {
				bitmap = new BitmapDrawable(is).getBitmap();
				Constants.red = bitmap;
			}
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			sw = ((float) iWidth) / width;
			sh = ((float) iHeight) / height;
			matrix = new Matrix();
			matrix.postScale(sw, sh);
			if (sw != 1 && sh != 1) {
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				Constants.red = bitmap;
			}
			bitmap = Constants.red;
			break;
		case R.drawable.blue:
			if (bitmap == null) {
				bitmap = new BitmapDrawable(is).getBitmap();
				Constants.blue = bitmap;
			}
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			sw = ((float) iWidth) / width;
			sh = ((float) iHeight) / height;
			matrix = new Matrix();
			matrix.postScale(sw, sh);
			if (sw != 1 && sh != 1) {
				// Log.v("bitmap", "bitmap");
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				Constants.blue = bitmap;
			}
			bitmap = Constants.blue;
			break;

		case R.drawable.nmk3_normal:
			if (bitmap == null) {
				bitmap = new BitmapDrawable(is).getBitmap();
				Constants.grey_long = bitmap;
			}
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			sw = ((float) iWidth) / width;
			sh = ((float) iHeight) / height;
			matrix = new Matrix();
			matrix.postScale(sw, sh);
			if (sw != 1 && sh != 1) {
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				Constants.grey_long = bitmap;
			}
			bitmap = Constants.grey_long;
			break;

		case R.drawable.nmk3_click:
			if (bitmap == null) {
				bitmap = new BitmapDrawable(is).getBitmap();
				Constants.red_long = bitmap;
			}
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			sw = ((float) iWidth) / width;
			sh = ((float) iHeight) / height;
			matrix = new Matrix();
			matrix.postScale(sw, sh);
			if (sw != 1 && sh != 1) {
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				Constants.red_long = bitmap;
			}
			bitmap = Constants.red_long;
			break;
			default :
				if (bitmap == null) {
					bitmap = new BitmapDrawable(is).getBitmap();
					Constants.grey_long = bitmap;
				}
				width = bitmap.getWidth();
				height = bitmap.getHeight();
				sw = ((float) iWidth) / width;
				sh = ((float) iHeight) / height;
				matrix = new Matrix();
				matrix.postScale(sw, sh);
				if (sw != 1 && sh != 1) {
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
							matrix, true);
					Constants.grey_long = bitmap;
				}
				bitmap = Constants.grey_long;
		}

		return bitmap;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(iWidth, iHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawBitmap(bitmaps[iShowId], 0, 0, null);
		if (flag) {
			p.setColor(Color.BLACK);
		} else {
			p.setColor((textcolor[iShowId]));
		}
		
		if(isTextTranslate){
			p.setColor(Color.TRANSPARENT);
		}
		
		if(context instanceof Cq11Xuan5){
			canvas.drawText(iShowString, iShowStringX, iShowStringY - 7, p);
		}else{
			canvas.drawText(iShowString, iShowStringX, iShowStringY, p);
		}
		
	}

	public int getShowId() {
		return iShowId;
	}

	public void startAnim() {
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.push_up_in);
		this.startAnimation(anim);

	}

	/**
	 * 获取小球的号码
	 */
	public String getNum() {
		if (iShowString == "3+") {
			iShowString = "3";
		}
		return iShowString;
	}

	public void setShowId(int id) {
		iShowId = id;
	}

}
