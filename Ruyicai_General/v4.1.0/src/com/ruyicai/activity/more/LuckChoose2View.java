package com.ruyicai.activity.more;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.palmdream.RuyicaiAndroid.R;

/**
 * 自定义的转盘View
 */
public class LuckChoose2View extends SurfaceView implements
		SurfaceHolder.Callback {

	public float screenHeight, screenWidth, centerX, centerY;// 屏幕的宽和高,
	// 左上坐标;
	// 中心X，中心Y

	/**
	 * 几种幸运选号状态
	 */
	public static final int ZHUANGTAI_XINGZUO = 0, ZHUANGTAI_SHENGXIAO = 1,
			ZHUANGTAI_XINGMING = 2, ZHUANGTAI_SHENGRI = 3;

	/**
	 * 转盘选中项名字
	 */	
	public String[][] zhuanpanNeirong = { { "白羊", "鼠" }, { "金牛", "牛" },
			{ "双子", "虎" }, { "巨蟹", "兔" }, { "狮子", "龙" }, { "处女", "蛇" },
			{ "天秤", "马" }, { "天蝎", "羊" }, { "射手", "猴" }, { "摩羯", "鸡" },
			{ "水瓶", "狗" }, { "双鱼", "猪" }, };

	/**
	 * 当前幸运状态
	 */
	public static int zhuangtaiDangqian = ZHUANGTAI_XINGZUO;

	/**
	 * 图片旋转角度
	 */
	public static int degrees;
	/**add by pengcx 20130806 start*/
	public boolean isScale;
	/**add by pengcx 20130806 end*/
	/**
	 * LuckChoose2引用， 用于回调
	 */
	private LuckChoose2 luckChoose2;

	/**
	 * 设置LuckChoose2对象
	 */
	public void setLuckChoose2Instance(LuckChoose2 luckChoose2) {
		this.luckChoose2 = luckChoose2;
	}
	
	/**
	 * 按钮事件
	 */
	public boolean anniuAction(int x, int y) {
		int gapCenter = bmSelectBig.getWidth() >> 1;
		for (int i = 0; i < anniuZhongxinZuobiao.length; i++) {
			if (x > anniuZhongxinZuobiao[i][0] - gapCenter
					&& x < anniuZhongxinZuobiao[i][0] + gapCenter
					&& y > anniuZhongxinZuobiao[i][1] - gapCenter
					&& y < anniuZhongxinZuobiao[i][1] + gapCenter) {

				luckChoose2.alertDialog(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * 转动时点击无效
	 */
	private boolean isZhuandong;

	/**
	 * 笔触点击事件
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean pointerPressed(double x, double y) {
		// 彩种，玩法，倍数 3个按钮事件
		if (!isZhuandong && anniuAction((int) x, (int) y)) {
			return true;
		}

		double distanceX = x - centerX;
		double distanceY = y - centerY;

		// 开始选号按钮事件
		double distanceXY = distanceX * distanceX + (distanceY - 40)
				* (distanceY - 40);

		int gapCenter = bmSelectBig.getWidth() >> 1;
		// 选号事件
		if (distanceXY < gapCenter * gapCenter) {

			// 转盘
			if (LuckChoose2View.anniuXuanzhongId[1] == 0
					|| LuckChoose2View.anniuXuanzhongId[1] == 1) {
				zhuandong();

				// 姓名和生日选号
			} else {
				luckChoose2.xuanhaoXingmingHuoShengri();
			}
			return true;
		}

		switch (LuckChoose2View.anniuXuanzhongId[1]) {
		// 转盘
		case 0:
		case 1:
			// 转盘事件
			distanceXY = distanceX * distanceX + distanceY * distanceY;

			// 转盘的有效点击区域
			gapCenter = bmSmallPlate.getWidth() >> 1;
			if (distanceXY < gapCenter * gapCenter) {
				return true;
			}
			gapCenter = bmLuckPlate.getWidth() >> 1;
			if (distanceXY > gapCenter * gapCenter) {
				return true;
			}

			// 得到角度
			double jiaodu = Math.atan2(distanceX, distanceY) * 180 / Math.PI
					+ 180;

			// 处理笔触
			pointerSelected(jiaodu);
			break;

		// 输入框
		case 2:
		case 3:
			// 输入框事件
			int boxWidth = bmInputBox.getWidth() >> 1;
			int boxHeight2 = bmInputBox.getHeight() >> 1;
			if (distanceX > -boxWidth && distanceX < boxWidth
					&& distanceY > -strHeightGap - 6 - boxHeight2
					&& distanceY < -strHeightGap - 6 + boxHeight2) {

				luckChoose2.shurukuangShijian();
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * 按钮的背景球左上角坐标
	 */
	public int[][] anniuZhongxinZuobiao = { { 86, 136 }, { 236, 96 },
			{ 386, 136 }, };

	/**
	 * 屏幕分辨率 480x800 时的字体, 安钮文字距转盘中心的垂直间距
	 */
	private int fontSize = 20, strHeightGap = 40;

	/**
	 * 按钮选中项id
	 */
	public static int[] anniuXuanzhongId = { 0, 0, 0, };

	/**
	 * 按钮名字
	 */
	public static String[] anniuMingzi = { "双色球", "星座", "1注" };

	/**
	 * 转动状态
	 */

	private Canvas mCanvas;
	private SurfaceViewThread myThread;
	private SurfaceHolder holder;
	private boolean surfaceExist = false;

	public LuckChoose2View(Context context, AttributeSet attr) {
		super(context, attr);
		initial();
	}

	/**
	 * 姓名, 转盘选中内容
	 */
	public static String xingming, xingzuoHuoShengxiao;

	public void initial() {
		// 创建一个新的SurfaceHolder， 并分配这个类作为它的回调(callback)
		holder = getHolder();
		holder.addCallback(this);

		new Paint();

	}

	/**
	 * 设置焦点
	 */
	public void pointerSelected() {
		pointerSelected(selectedIndex);
	}

	public void pointerSelected(double jiaodu) {
		jiaodu += degrees;

		jiaodu %= 360;
		pointerSelected(LuckChoose2.getIndexFromJiaodu((int) jiaodu / 15));
	}

	private Bitmap bmLuckPlate, bmSelector, bmNames, bmSmallPlate, bmBigPlate,
			bmSelectBig, bmInputBox;

	private Bitmap bmLuckChooseBackground;

	{
		bmLuckPlate = BitmapFactory.decodeResource(getResources(),
				R.drawable.luck_plate);
		bmSelector = BitmapFactory.decodeResource(getResources(),
				R.drawable.luck_selector);

		// 姓名选号与生日选号用大盘
		if (anniuXuanzhongId[1] == 2 || anniuXuanzhongId[1] == 3) {
			bmSmallPlate = BitmapFactory.decodeResource(getResources(),
					R.drawable.smallplate2);
		} else {
			bmSmallPlate = BitmapFactory.decodeResource(getResources(),
					R.drawable.smallplate);
		}

		bmBigPlate = BitmapFactory.decodeResource(getResources(),
				R.drawable.bigplate);
		bmSelectBig = BitmapFactory.decodeResource(getResources(),
				R.drawable.selectbig);

		bmInputBox = BitmapFactory.decodeResource(getResources(),
				R.drawable.inputbox);

		bmLuckChooseBackground = BitmapFactory.decodeResource(getResources(),
				R.drawable.luckchoose_background);
		System.gc();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// 屏幕按钮可点击
		isZhuandong = false;

		surfaceExist = true;

		// 高度赋值
		if (screenWidth == 0) {
			screenWidth = getWidth();
			screenHeight = getHeight();

			centerX = screenWidth / 2;
			centerY = screenHeight / 2 + 50;

			tupianShuofang();

			// 设置间隙
			setZhuanpanNeirong(anniuXuanzhongId[1] == 1);
		}
		
		/** add by pengcx 20130802 start*/
		if(!isScale){
			float scaleWidth = screenWidth / 480.0f;
			float scaleHeight = screenHeight / 800.0f;
			for(int i = 0; i < anniuZhongxinZuobiao.length; i++){
				anniuZhongxinZuobiao[i][0] = (int) (anniuZhongxinZuobiao[i][0] * scaleWidth);
				anniuZhongxinZuobiao[i][1] = (int) (anniuZhongxinZuobiao[i][1] * scaleHeight);

			}
			// 字体大小，距转盘中心边距缩放
			fontSize *= scaleWidth;
			paint.setTextSize(fontSize);
			
			isScale = true;
		}
		
		/** add by 20130802 20130726 end*/
	
		myThread = new SurfaceViewThread();
		myThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		surfaceExist = false;

		myThread.isRunning = false;
		myThread = null;
	}

	/**
	 * 图片缩放率, 宽高不一样时取小的
	 */
	public static float scaleWidth = 1, scaleHeight = 1;

	/**
	 * 转盘的缩放率，宽高缩放不成比例造成的
	 */
	public static float scaleCircle;

	/**
	 * 根据屏幕缩小图片
	 */
	private void tupianShuofang() {

		// 图片比屏幕宽时，-10 是 float除时有误差
		if (screenWidth < bmLuckChooseBackground.getWidth() - 10) {

			// 计算缩放率，新尺寸除原始尺寸
			scaleWidth = ((float) screenWidth)
					/ bmLuckChooseBackground.getWidth();
			scaleHeight = ((float) screenHeight)
					/ bmLuckChooseBackground.getHeight();

			/**
			 * 上面3个图片背景坐标, tip: 手机读取图片时图片宽高会变,即使图片放在 drawable 文件夹下
			 */
			if (screenWidth < 480) {
				for (int i = 0; i < anniuZhongxinZuobiao.length; i++) {
					anniuZhongxinZuobiao[i][0] *= scaleWidth
							* (bmLuckChooseBackground.getWidth() / (float) 480);
					anniuZhongxinZuobiao[i][1] *= scaleHeight
							* (bmLuckChooseBackground.getHeight() / (float) 800);
				}

				centerY *= scaleHeight
						* (bmLuckChooseBackground.getHeight() / (float) 800);

				strHeightGap *= scaleHeight;
			}

			// 创建操作图片用的matrix对象
			Matrix matrix = new Matrix();

			// 设置大转盘的缩放率
			if (anniuZhongxinZuobiao[1][1]
					+ (bmSelectBig.getHeight() + bmBigPlate.getHeight())
					* scaleWidth / 2 > centerY) {
				scaleCircle = scaleHeight;

				centerY = screenHeight - bmBigPlate.getHeight() / 2
						* scaleCircle - 6;
			} else {
				scaleCircle = scaleWidth;
			}

			// 缩放图片动作
			matrix.setScale(scaleCircle, scaleCircle);

			Bitmap temp = bmLuckPlate;
			temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
					temp.getHeight(), matrix, true);
			bmLuckPlate = temp;
			System.gc();

			temp = bmSelector;
			temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
					temp.getHeight(), matrix, true);
			bmSelector = temp;
			System.gc();

			temp = bmSmallPlate;
			temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
					temp.getHeight(), matrix, true);
			bmSmallPlate = temp;
			System.gc();

			temp = bmBigPlate;
			temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
					temp.getHeight(), matrix, true);
			bmBigPlate = temp;
			System.gc();

			temp = bmLuckChooseBackground;
			matrix.setScale(scaleWidth, scaleHeight);
			temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
					temp.getHeight(), matrix, true);
			bmLuckChooseBackground = temp;
			System.gc();

			// 输入框宽度与scaleCircle 一致
			matrix.setScale(scaleCircle, scaleHeight);
			temp = bmInputBox;
			temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
					temp.getHeight(), matrix, true);
			bmInputBox = temp;
			System.gc();

			/**
			 * 按钮图片按原宽度缩放
			 */
			matrix.setScale(scaleWidth, scaleWidth);
			temp = bmSelectBig;
			temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
					temp.getHeight(), matrix, true);
			bmSelectBig = temp;
			System.gc();

		}
	}

	/**
	 * 是否是属相
	 */
	public boolean isShuxian;

	/**
	 * 设置转盘内容是星座或生肖
	 */
	public void setZhuanpanNeirong(boolean isShengxiao) {

		// 0：星座，1：生肖; 2, 3与转盘无关
		if (anniuXuanzhongId[1] > 1) {
			return;
		}

		// 属相
		if (isShengxiao) {
			bmNames = BitmapFactory.decodeResource(getResources(),
					R.drawable.luck_chinese_zodiac);

			// 星座
		} else {
			bmNames = BitmapFactory.decodeResource(getResources(),
					R.drawable.luck_constallation);
		}

		// 缩小图片
		if (scaleWidth < 1) {
			Bitmap temp = bmNames;
			Matrix matrix = new Matrix();
			matrix.setScale(scaleCircle, scaleCircle);
			temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
					temp.getHeight(), matrix, true);
			bmNames = temp;
		}
		System.gc();

		setXuanZhongMingzi();

	}

	/**
	 * 设置输入框的背景图为大图
	 * 
	 * @param isBig
	 */
	public void setInputBackPng(boolean isBig) {
		if (isBig) {
			bmSmallPlate = BitmapFactory.decodeResource(getResources(),
					R.drawable.smallplate2);
		} else {
			bmSmallPlate = BitmapFactory.decodeResource(getResources(),
					R.drawable.smallplate);
		}

		// 缩小图片
		if (scaleWidth < 1) {
			Bitmap temp = bmSmallPlate;
			Matrix matrix = new Matrix();
			matrix.setScale(scaleCircle, scaleCircle);
			temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
					temp.getHeight(), matrix, true);
			bmSmallPlate = temp;
		}
	}

	/**
	 * 转动转盘，速度可叠加
	 */
	public static transient int zhuandongTime = 0;

	public static final int SHIJIAN_MAN = 100, SHIJIAN_KUAI = 50,
			JIAODU_DEFAULT = 1;

	/**
	 * 转动的时间间隔，及转动角度长
	 */
	private int zhuangdongShijian = SHIJIAN_MAN,
			zhuangdongJiaodu = JIAODU_DEFAULT;

	/**
	 * 选中的索引
	 */
	private static int selectedIndex;

	/**
	 * 屏幕刷新线程
	 */
	public class SurfaceViewThread extends Thread {

		public boolean isRunning = true;

		public void run() {

			// 转盘慢慢转动
			while (isRunning) {
				updateViewWithSleep();

				// 空闲时速度
				if (zhuandongTime <= 0) {
					degrees += zhuangdongJiaodu;
				}

				while (degrees >= 360) {
					degrees -= 360;
				}
			}
		}

		public SurfaceViewThread() {
		}

	}

	/**
	 * 画圆形图片时以中心点为坐标偏移
	 */
	private int gapCenter;

	Paint paint = new Paint();
	{
		paint.setTextSize(fontSize);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
	}

	public void updateView() {
		// 不在屏幕时不绘画
		if (!surfaceExist) {
			return;
		}
		SurfaceHolder surfaceHolder = holder;
		mCanvas = surfaceHolder.lockCanvas();

		mCanvas.drawBitmap(bmLuckChooseBackground, 0, 0, null);

		// 根据当前幸运状态绘画
		switch (anniuXuanzhongId[1]) {
		case ZHUANGTAI_XINGZUO:
		case ZHUANGTAI_SHENGXIAO:
			drawZhuanpan();
			break;

		case ZHUANGTAI_XINGMING:
			break;

		case ZHUANGTAI_SHENGRI:
			break;
		}

		gapCenter = (bmSmallPlate.getWidth() >> 1);
		mCanvas.drawBitmap(bmSmallPlate, centerX - gapCenter, centerY
				- gapCenter, null);

		// 开始按钮
		gapCenter = bmSelectBig.getWidth() >> 1;
		mCanvas.drawBitmap(bmSelectBig, centerX - gapCenter, centerY
				- gapCenter + strHeightGap, null);

		// 绘画盘中信息
		switch (anniuXuanzhongId[1]) {
		case ZHUANGTAI_XINGZUO:
		case ZHUANGTAI_SHENGXIAO:
			// 绘画当前选中盘内容
			paint.setColor(0xffff0000);
			mCanvas.drawText(xingzuoHuoShengxiao,
					centerX - (paint.measureText(xingzuoHuoShengxiao) / 2),
					centerY - strHeightGap, paint);
			break;

		case ZHUANGTAI_XINGMING:
			gapCenter = bmInputBox.getWidth() >> 1;
			mCanvas.drawBitmap(bmInputBox, centerX - gapCenter, centerY
					- strHeightGap - 6 - (bmInputBox.getHeight() >> 1), null);

			String text;
			if (xingming == null || "".equals(xingming)) {
				text = "请输入姓名";
				paint.setColor(Color.GRAY);
			} else {
				text = xingming;
				paint.setColor(Color.RED);
			}

			mCanvas.drawText(text, centerX - (paint.measureText(text) / 2),
					centerY - strHeightGap, paint);
			break;

		case ZHUANGTAI_SHENGRI:
			gapCenter = bmInputBox.getWidth() >> 1;
			mCanvas.drawBitmap(bmInputBox, centerX - gapCenter, centerY
					- strHeightGap - 6 - (bmInputBox.getHeight() >> 1), null);

			paint.setColor(Color.RED);
			text = LuckChoose2.year + "-" + (LuckChoose2.month + 1) + "-"
					+ LuckChoose2.day;
			mCanvas.drawText(text, centerX - (paint.measureText(text) / 2),
					centerY - strHeightGap, paint);
			break;
		}

		// 绘画 "开始选号"
		paint.setColor(0xffffffff);
		mCanvas.drawText("开始", centerX - fontSize, centerY + strHeightGap,
				paint);
		mCanvas.drawText("选号", centerX - fontSize, centerY + strHeightGap
				+ fontSize, paint);

		// 画三个按钮
		int gapCenter = bmSelectBig.getWidth() >> 1;
		for (int i = 0; i < anniuMingzi.length; i++) {
			mCanvas.drawBitmap(bmSelectBig, anniuZhongxinZuobiao[i][0]
					- gapCenter, anniuZhongxinZuobiao[i][1] - gapCenter, null);
			mCanvas.drawText(anniuMingzi[i], anniuZhongxinZuobiao[i][0]
					- (paint.measureText(anniuMingzi[i]) / 2),
					anniuZhongxinZuobiao[i][1] + (fontSize >> 1), paint);
		}

		// 解锁Canvas，并渲染当前图像
		surfaceHolder.unlockCanvasAndPost(mCanvas);
	}

	/**
	 * 通过笔触选择了焦点
	 * 
	 * @param index
	 */
	public void pointerSelected(int index) {
		selectedIndex = index;
		// 星座
		setXuanZhongMingzi();

		updateView();
	}

	/**
	 * 刷新屏幕且休息, updateView捕捉空指针
	 */
	private void updateViewWithSleep() {
		try {
			updateView();
			Thread.sleep(zhuangdongShijian);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 转盘绘画
	 */
	private void drawZhuanpan() {
		gapCenter = (bmBigPlate.getWidth() >> 1);
		mCanvas.drawBitmap(bmBigPlate, centerX - gapCenter,
				centerY - gapCenter, null);

		// *********************************画每个区域的颜色块*********************************

		mCanvas.save();
		mCanvas.rotate(degrees, centerX, centerY);
		gapCenter = bmLuckPlate.getWidth() >> 1;
		mCanvas.drawBitmap(bmLuckPlate, centerX - gapCenter, centerY
				- gapCenter, null);
		mCanvas.restore();

		mCanvas.save();
		mCanvas.rotate(degrees + 30 * selectedIndex, centerX, centerY);
		gapCenter = bmSelector.getWidth() >> 1;
		mCanvas.drawBitmap(bmSelector, centerX - gapCenter, centerY
				- bmSelector.getHeight(), null);
		mCanvas.restore();

		if (bmNames != null) {
			mCanvas.save();
			mCanvas.rotate(degrees, centerX, centerY);
			gapCenter = bmNames.getWidth() >> 1;
			mCanvas.drawBitmap(bmNames, centerX - gapCenter, centerY
					- gapCenter, null);
			mCanvas.restore();
		}
	}

	/**
	 * 外部线程操作界面
	 */
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			luckChoose2.alertMessage();
		}

	};

	public void zhuandong() {
		isZhuandong = true;

		// 开始选号转动
		new Thread() {
			public void run() {
				zhuandongTime++;

				// 加速
				for (int i = 1; i < 10; i += 2) {
					degrees += i * i;
					if (degrees >= 360) {
						degrees -= 360;
					}

					updateViewWithSleep();
				}

				// 减速
				for (int i = 9; i > 0; i -= 2) {
					for (int j = 0; j < 2; j++) {
						degrees += i * i;
						if (degrees >= 360) {
							degrees -= 360;
						}

						updateViewWithSleep();
					}
				}

				zhuandongTime--;

				// 动画完成
				if (surfaceExist) {

					// 所有的转动结束后出幸运号码
					if (zhuandongTime <= 0) {
						handler.sendEmptyMessage(0);
						isZhuandong = false;
					}
				}

			}
		}.start();
	}

	/**
	 * 选中项名字
	 */
	public void setXuanZhongMingzi() {
		xingzuoHuoShengxiao = zhuanpanNeirong[selectedIndex][anniuXuanzhongId[1]];
	}
}