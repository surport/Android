package com.ruyicai.view;

/**
 * @Title Marquee View类
 * @Description: 
 * @Copyright: Copyright (c) 2009
 * @Company: ChinaM
 * @author FanYaJun
 * @version 1.0
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class MyMarqueeView extends View {
	private final Paint mPaint = new Paint();
	// private final String mstrTitle="感受Android带给我们的新体验";
	private int iScreenWidth;
	private int iScreenHeight;
	private int iCurId;
	private String[] iSs;
	private int[] iIntLength;
	private int iStepPixel;

	private Paint p = null;
	private int iCurX;
	private int iCurY = 22;// 16;
	private int iCurStringNO;

	public MyMarqueeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// PublicVarAndFun.OutPutString("--->>>marquee1");
		mPaint.setColor(Color.GREEN);
	}

	public MyMarqueeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// PublicVarAndFun.OutPutString("--->>>marquee2");
		mPaint.setColor(Color.GREEN);
	}

	public void initMarqueeWidth(int aScreenWidth, int aHeight, int aStepPixel) {
		iScreenWidth = aScreenWidth;
		iScreenHeight = aHeight;
		iStepPixel = aStepPixel;

		return;
	}

	private void setPaint() {
		if (p == null) {
			p = new Paint();
			// String familyName = "宋体";
			// Typeface font =
			// Typeface.create(familyName,Typeface.NORMAL);//Typeface.BOLD);
			// p.setColor(Color.RED);
			p.setColor(Color.BLACK);
			// p.setTypeface(font);
			p
					.setTypeface(Typeface.create(Typeface.SANS_SERIF,
							Typeface.NORMAL));
			// p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
			p.setTextSize(19);
			// p.setUnderlineText(true);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(iScreenWidth, iScreenHeight);
	}

	public void initMarqueeString(String[] aSs) {
		// init
		setPaint();
		iCurStringNO = 0;
		iCurX = iScreenWidth;

		iCurId = 0;
		iSs = aSs;

		iIntLength = new int[iSs.length];
		for (int i = 0; i < iSs.length; i++) {
			float[] stringLength = new float[iSs[i].length()];
			p.getTextWidths(iSs[i], stringLength);
			// iIntLength[i]=0;
			float fTemp = 0;
			for (int i1 = 0; i1 < stringLength.length; i1++) {
				fTemp += stringLength[i1];
			}
			iIntLength[i] = (int) fTemp;
		}

		// PublicVarAndFun.OutPutString("--->>>iSs"+iSs.length);
	}

	public int getCurrentStringId() {
		return iCurId;
	}

	private void changeSite() {
		if (iCurX + iIntLength[iCurId] <= 0) {
			iCurId = (iCurId + 1) % iSs.length;
			iCurX = iScreenWidth;
		} else {
			iCurX -= iStepPixel;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawText(iSs[iCurId], iCurX, iCurY, p);
		changeSite();
	}
}
