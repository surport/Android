/**
 * @Title 顶部RadioButton的Drawable生成类
 * @Description: 
 * @Copyright: Copyright (c) 2009
 * @Company: PalmDream
 * @author FanYaJun
 * @version 1.0 20100618
 */

package com.palmdream.RuyicaiAndroid;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;

public class RadioStateDrawable extends Drawable {

	private Bitmap bitmap;
	private Bitmap highlightBitmap;
	private Shader shader;
	private Shader textShader;
	Context context;
	public static int width;
	public static int screen_width;

	public static int other_width;
	public static int other_screen_width;

	private boolean highlight;
	private String label;

	private boolean iWithBitmap;
	public static final int SHADE_GRAY = 0;
	public static final int SHADE_BLUE = 1;
	public static final int SHADE_MAGENTA = 2;
	public static final int SHADE_YELLOW = 3;
	public static final int SHADE_GREEN = 4;
	public static final int SHADE_RED = 5;
	public static final int SHADE_ORANGE = 6;
	public static final int NOW_COLOR = 7;

	public RadioStateDrawable(Context context, String label, boolean highlight,
			int shade) {
		super();

		iWithBitmap = false;

		this.highlight = highlight;
		this.context = context;
		this.label = label;
		setShade(shade);

		highlightBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bottom_bar_highlight);
	}

	public RadioStateDrawable(Context context, int imageID, String label,
			boolean highlight, int shade) {
		super();
		iWithBitmap = true;
		this.highlight = highlight;
		this.context = context;
		this.label = label;
		InputStream is = context.getResources().openRawResource(imageID);
		bitmap = BitmapFactory.decodeStream(is).extractAlpha();
		setShade(shade);

		highlightBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bottom_bar_highlight);
	}

	public RadioStateDrawable(Context context, int imageID, String label,
			boolean highlight, int startGradientColor, int endGradientColor) {
		super();
		iWithBitmap = true;
		this.highlight = highlight;
		this.context = context;
		this.label = label;
		InputStream is = context.getResources().openRawResource(imageID);
		bitmap = BitmapFactory.decodeStream(is).extractAlpha();
		int[] shades = new int[] { startGradientColor, endGradientColor };
		shader = new LinearGradient(0, 0, 0, bitmap.getHeight(), shades, null,
				Shader.TileMode.MIRROR);
	}

	public void setShade(int shade) {
		PublicMethod.myOutput("---setShade:" + iWithBitmap);
		int[] shades = new int[2];
		switch (shade) {
		case SHADE_GRAY: {
			shades = new int[] { Color.LTGRAY, Color.DKGRAY };
			break;
		}
		case SHADE_BLUE: {
			shades = new int[] { Color.CYAN, Color.BLUE };
			break;
		}
		case SHADE_RED: {
			shades = new int[] { Color.MAGENTA, Color.RED };
			break;
		}
		case SHADE_MAGENTA: {
			shades = new int[] { Color.MAGENTA, Color.rgb(292, 52, 100) };
			break;
		}
		case SHADE_YELLOW: {
			shades = new int[] { Color.YELLOW, Color.rgb(255, 126, 0) };
			break;
		}
		case SHADE_ORANGE: {
			shades = new int[] { Color.rgb(255, 126, 0), Color.rgb(255, 90, 0) };
			break;
		}
		case SHADE_GREEN: {
			shades = new int[] { Color.GREEN, Color.rgb(0, 79, 4) };
			break;
		}
		case NOW_COLOR: {
			// shades = new int[]{Color.rgb(254, 107, 19), Color.rgb(255,90,0)
			// };
			// shades = new int[]{Color.rgb(245, 129, 10), Color.rgb(255,90,0)
			// };
			shades = new int[] { Color.rgb(245, 129, 10),
					Color.rgb(245, 129, 10) };
			break;
		}
		}
		if (iWithBitmap)
			shader = new LinearGradient(0, 0, 0, bitmap.getHeight(), shades,
					null, Shader.TileMode.MIRROR);

		if (highlight)
			// textShader = new LinearGradient(0, 0, 0, 10, new
			// int[]{Color.WHITE, Color.LTGRAY}, null, Shader.TileMode.MIRROR);
			textShader = new LinearGradient(0, 0, 0, 10, new int[] {
					Color.rgb(245, 129, 10), Color.LTGRAY }, null,
					Shader.TileMode.MIRROR);
		else
			textShader = new LinearGradient(0, 0, 0, 10, new int[] {
					Color.LTGRAY, Color.DKGRAY }, null, Shader.TileMode.MIRROR);
	}

	@Override
	public void draw(Canvas canvas) {
		PublicMethod.myOutput("---draw:" + iWithBitmap);
		if (iWithBitmap) {
			int bwidth = bitmap.getWidth();
			int bheight = bitmap.getHeight();
			/*
			 * if (width==0) { if (screen_width==0) screen_width = 320;
			 * width=screen_width/5; }
			 */
			int x = (width - bwidth) / 2;
			int y = 2;

			canvas.drawColor(Color.TRANSPARENT);
			Paint p = new Paint();

			p.setColor(Color.WHITE);
			p.setStyle(Paint.Style.FILL);
			// p.setTextSize(10);
			p.setTextSize(12);
			p.setTypeface(Typeface.DEFAULT_BOLD);
			p.setFakeBoldText(true);
			p.setTextAlign(Align.CENTER);
			p.setShader(textShader);
			p.setAntiAlias(true);
			canvas.drawText(label, width / 2, y + bheight + 8, p);

			p.setShader(shader);
			canvas.drawBitmap(bitmap, x, y, p);
		} else {
			PublicMethod.myOutput("---draw_true:" + iWithBitmap
					+ " other_width:" + other_width + " screen_width:"
					+ screen_width);
			// int bwidth = bitmap.getWidth();
			// int bheight = bitmap.getHeight();
			/*
			 * if (width==0) { if (screen_width==0) screen_width = 320;
			 * width=screen_width/5; }
			 */
			int x = other_width / 2;
			int y = 2;

			canvas.drawColor(Color.TRANSPARENT);
			Paint p = new Paint();

			int iTextSize = 20;
			p.setColor(Color.GREEN);
			p.setStyle(Paint.Style.FILL);
			p.setTextSize(iTextSize);
			p.setTypeface(Typeface.DEFAULT_BOLD);
			p.setFakeBoldText(true);
			p.setTextAlign(Align.CENTER);
			p.setShader(textShader);
			p.setAntiAlias(true);
			canvas.drawText(label, other_width / 2, y + 10 + iTextSize, p);
			PublicMethod.myOutput("---draw text");
		}
	}

	@Override
	public int getOpacity() {
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}

}
