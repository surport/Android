package com.ruyicai.activity.buy.cq11x5;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.cq11x5.HistoryNumberActivity.PrizeInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

/**
 * 自定义模拟选号控件类:使用Row,NumberRow,SelectRow,Cell,NumberCell,
 * LotteryCell等类分别对控件中的行和单元格抽抽象，并使用集合List<Row>,List<Cell>数组保存各个抽象的对象
 * 
 * @author PengCX
 * 
 */
public class HistoryNumberView extends View {
	private static final String TAG = "SimulateSelectNumberView";

//	private static final float STANDARD_SCREEN_HEIGHT = 1000.0f;
	// 控件缩放比例值
	private static float ratio;

	static Paint paint;

	private List<Row> viewRows;
	private static int viewRowNum;

	private boolean hasDownloadPrizeInfo = false;

	public List<Row> getViewRows() {
		return viewRows;
	}

	public void setPrizeInfos(List<PrizeInfo> prizeInfos) {
		// 遍历List<Row>集合，设置开奖信息属性
		for (int row_i = 0; row_i < viewRowNum - 1; row_i++) {
			Row childRow = viewRows.get(row_i+1);
			PrizeInfo prizeInfo = prizeInfos.get(row_i);

			for (int colum_i = 0; colum_i < Row.columNum; colum_i++) {
				Cell childCell = childRow.getRowCells().get(colum_i);

				if (childCell instanceof BatchCodeCell) {
					((BatchCodeCell) childCell).setBatchCode(prizeInfo
							.getBatchCode());
				} 
				else if (childCell instanceof LotteryNumberCell) {
					((LotteryNumberCell) childCell).setNumber(prizeInfo
							.getWinCodeByColum(colum_i));
				}
			}
		}

		hasDownloadPrizeInfo = true;

		invalidate();
	}
	
	private Activity activity;
	private int screenWith;

	public HistoryNumberView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获取屏幕宽度
		this.activity=(Activity) context;
		screenWith = PublicMethod.getDisplayWidth(activity);
//		Toast.makeText(activity, screenWith+"", Toast.LENGTH_SHORT).show();

		setViewAttributes();

		initViewPaint();

		initViewRows();

		getViewBitmaps();

	}

	private void getViewBitmaps() {

		// 获取目标尺寸的位图资源
		LotteryNumberCell.whiteGridBitmap = getBitmapFromResource(
				R.drawable.notice_center_wite, NumberCell.cellWidth,
				NumberRow.rowHight);
		LotteryNumberCell.grayGridBitmap = getBitmapFromResource(
				R.drawable.notice_center_grey, NumberCell.cellWidth,
				NumberRow.rowHight);
		LotteryNumberCell.redBallBitmap = getBitmapFromResource(
				R.drawable.notice_ball_red, (NumberCell.cellWidth-8*ratio),
				(NumberRow.rowHight-8*ratio));
		LotteryNumberCell.blueBallBitmap = getBitmapFromResource(
				R.drawable.notice_ball_blue, NumberCell.cellWidth,
				NumberRow.rowHight);
		BatchCodeCell.singleTitleBitmap = getBitmapFromResource(
				R.drawable.choose_detail, BatchCodeCell.cellWidth,
				NumberRow.rowHight);
		BatchCodeCell.doubleTitleBitmap = getBitmapFromResource(
				R.drawable.choose_detail, BatchCodeCell.cellWidth,
				NumberRow.rowHight);
		BatchCodeCell.titleSelectBitmap = getBitmapFromResource(
				R.drawable.choose_detail, BatchCodeCell.cellWidth,
				SelectRow.rowHight);
		NumberCell.redSelectBitmap = getBitmapFromResource(
				R.drawable.simulate_selectnumber_bottomred,
				NumberCell.cellWidth, SelectRow.rowHight);
//		NumberCell.blueSelectBitmap = getBitmapFromResource(
//				R.drawable.simulate_selectnumber_bottomblue,
//				NumberCell.cellWidth, SelectRow.rowHight);
		NumberCell.normalSelectBitmap = getBitmapFromResource(
				R.drawable.choose_detail, NumberCell.cellWidth, SelectRow.rowHight);
	}

	private Bitmap getBitmapFromResource(int resourceId, float f,
			float g) {
		Resources resources = getContext().getResources();

		InputStream inputStream = resources.openRawResource(resourceId);
		Bitmap bitmap = new BitmapDrawable(inputStream).getBitmap();

		Matrix matrix = new Matrix();

		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();

		float scaleWidth = ((float) f) / bitmapWidth;
		float scaleHeight = ((float) g) / bitmapHeight;

		matrix.postScale(scaleWidth, scaleHeight);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight,
				matrix, true);

		return bitmap;
	}

	private void setViewAttributes() {
		HistoryNumberView.viewRowNum = 11;

		Row.columNum = Row.redColumNum + 1;

		ratio = caculateRatio();

		// 根据比例设置控件相关的属性
		NumberRow.rowHight = (int) ((int) 58 * ratio);

		SelectRow.rowHight = (int) ((int) 58 * ratio);

		BatchCodeCell.cellWidth = (int) ((int) 130 * ratio);

		NumberCell.cellWidth = (int) ((int) 58 * ratio);

	}

	/**
	 * 计算控件高度的缩放比例=当前的屏幕高度/1200pix标准高度
	 */
	private float caculateRatio() {
		int height = Constants.SCREEN_HEIGHT;
		//机型适配
		float STANDARD_SCREEN_HEIGHT=0;
		if(screenWith==320){
			STANDARD_SCREEN_HEIGHT=1150;
		}else if(screenWith==480){
			STANDARD_SCREEN_HEIGHT=1280;
		}else if(screenWith==720){
			STANDARD_SCREEN_HEIGHT=1360;
		}else if(screenWith==800){
			STANDARD_SCREEN_HEIGHT=1250;
		}else if(screenWith==1080){
			STANDARD_SCREEN_HEIGHT=1380;
		}else{
			STANDARD_SCREEN_HEIGHT=1200;
		}

		float ratio = height / STANDARD_SCREEN_HEIGHT;

		return ratio;
	}

	private void initViewPaint() {
		paint = new Paint();

		paint.setTextSize(30 * ratio);
		paint.setColor(Color.BLACK);
		paint.setTextAlign(Align.CENTER);
		paint.setAntiAlias(true);
		paint.setSubpixelText(true);
	}

	private void initViewRows() {
		viewRows = new ArrayList<HistoryNumberView.Row>();

		// 初始化viewRows集合中的行对象
		for (int row_i = 0; row_i < viewRowNum; row_i++) {
			Row chileRow = null;

			if (row_i == 0) {
				chileRow = new SelectRow(row_i);
			} else {
				chileRow = new NumberRow(row_i);
			}

			viewRows.add(chileRow);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measuredWidth(widthMeasureSpec),
				measuredHeight(heightMeasureSpec));
	}

	private int measuredWidth(int measureSpec) {
		int result = 0;

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = BatchCodeCell.cellWidth + (Row.columNum - 1)
					* NumberCell.cellWidth;
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

	private int measuredHeight(int measureSpec) {
		int result = 0;

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (viewRowNum - 1) * NumberRow.rowHight + SelectRow.rowHight;
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		onDrawViewBackground(canvas);

		if ( hasDownloadPrizeInfo) {
			onDrawPrizeInfoAndLossValue(canvas);
		}
	}

	private void onDrawViewBackground(Canvas canvas) {

		// 遍历行集合，绘制行背景
		for (int row_i = 0; row_i < viewRowNum; row_i++) {
			Row chileRow = viewRows.get(row_i);

			if (chileRow instanceof NumberRow) {
				((NumberRow) chileRow).onDrawBackground(canvas);
			} else if (chileRow instanceof SelectRow) {
				((SelectRow) chileRow).onDrawBackground(canvas);
			}
		}

	}

	private void onDrawPrizeInfoAndLossValue(Canvas canvas) {
		// 遍历集合对象，绘制开奖信息和遗漏值信息
		for (int row_i = 1; row_i < viewRowNum ; row_i++) {
			Row chileRow = viewRows.get(row_i);

			for (int colum_i = 0; colum_i < Row.columNum; colum_i++) {
				Cell chileCell = chileRow.getRowCells().get(colum_i);

				if (chileCell instanceof BatchCodeCell) {
					((BatchCodeCell) chileCell).onDrawBatchCode(canvas);
				} 
				else if (chileCell instanceof LotteryNumberCell) {
					((LotteryNumberCell) chileCell).onDrawLossValue(canvas);

					if (isWinCodeCell(chileCell)
							&& !(chileCell instanceof BatchCodeCell)) {
						((LotteryNumberCell) chileCell).onDrawPrizeInfo(canvas);
					}
				}
			}
		}
		for (int row_i = 1; row_i < viewRowNum ; row_i++) {
			Row chileRow = viewRows.get(row_i);

			for (int colum_i = 0; colum_i < Row.columNum; colum_i++) {
				Cell chileCell = chileRow.getRowCells().get(colum_i);

				if (chileCell instanceof BatchCodeCell) {
					if(row_i==3){
						((BatchCodeCell) chileCell).onDrawBatchCode(canvas);
					}
					if (row_i != 0) {
						((BatchCodeCell) chileCell).onDrawBatchCode(canvas);
					}

				}
			}
		}

	}

	private boolean isWinCodeCell(Cell chileCell) {
		return ((LotteryNumberCell) chileCell).getNumber() != -1;
	}

	private static boolean isBatchCodeCell(int colum_i) {
		return colum_i == 0;
	}

	protected static boolean isRedBallCell(int colum_i) {
		return colum_i > 0 && colum_i <= Row.redColumNum;
	}

	/**
	 * 将数字格式化为两位：如"1"格式化为"01","2"格式化为"02"等
	 */
	protected static String formatNumberToTwoPlace(int number) {
		String formatString = null;

//		if (number < 10) {
//			formatString = "0" + number;
//		} else {
			formatString = String.valueOf(number);
//		}

		return formatString;
	}

	/**
	 * 行类:View控件中行的基类
	 */
	static class Row {
		int whichRow;

		static int columNum;
		static int redColumNum = 11;
		//static int blueColumNum = 0;

		private List<Cell> rowCells;

		public List<Cell> getRowCells() {
			return rowCells;
		}

		public void setRowCells(List<Cell> cells) {
			this.rowCells = cells;
		}

		public Row(int row) {
			super();

			this.whichRow = row;
			rowCells = new ArrayList<HistoryNumberView.Cell>();
		}
	}

	/**
	 * 中奖号码行类：指除了选号行之外的行
	 */
	static class NumberRow extends Row {
		static int rowHight;

		public NumberRow(int whichRow) {
			super(whichRow);

			// 遍历列集合，初始化单元格对象
			for (int colum_i = 0; colum_i < columNum; colum_i++) {
				Cell cell = null;

				if (isBatchCodeCell(colum_i)) {
					cell = new BatchCodeCell(whichRow, colum_i);
				} else if (isRedBallCell(colum_i)) {
					cell = new LotteryNumberCell(Cell.RED_BALL, whichRow,
							colum_i);
				} 

				getRowCells().add(cell);
			}
		}

		public void onDrawBackground(Canvas canvas) {
			List<Cell> cells = getRowCells();
			int cellsLength = cells.size();

			// 遍历列集合，绘制单元格背景
			for (int i = 0; i < cellsLength; i++) {
				Cell cell = cells.get(i);

				if (cell instanceof BatchCodeCell) {
					((BatchCodeCell) cell).onDrawbackground(canvas);
				} else if (cell instanceof LotteryNumberCell) {
					((LotteryNumberCell) cell).onDrawBackground(canvas);
				}
			}
		}
	}

	/**
	 * 选号行：选择号码的行
	 */
	static class SelectRow extends Row {
		static int rowHight;

		public SelectRow(int row) {
			super(row);

			// 遍历列集合，初始化单元格对象
			for (int colum_i = 0; colum_i < columNum; colum_i++) {
				Cell cell = null;

				if (isBatchCodeCell(colum_i)) {
					cell = new BatchCodeCell("期号", row, colum_i);
				} else if (isRedBallCell(colum_i)) {
					cell = new NumberCell(Cell.RED_BALL, row, colum_i, colum_i);
				} 

				getRowCells().add(cell);
			}
		}

		public void onDrawBackground(Canvas canvas) {
			List<Cell> cells = getRowCells();
			int cellsLenth = cells.size();

			for (int i = 0; i < cellsLenth; i++) {
				Cell cell = cells.get(i);

				if (cell instanceof BatchCodeCell) {
					((BatchCodeCell) cell).onDrawbackground(canvas);
				} else if (cell instanceof NumberCell) {
					((NumberCell) cell).onDrawBackground(canvas);
				}
			}
		}
	}

	/**
	 * 单元格基类
	 */
	static class Cell {
		private int type;
		private static final int RED_BALL = 1;
		private static final int BLUE_BALL = 2;

		// 单元格号码:如果没有中奖默认为-1，有中奖则为相应的中奖号码
		private int number;

		// 用于图片对齐
		protected float alignLeft;
		protected float alignTop;

		// 用于文本居中
		protected float textAlignLeft;
		protected float textAlignTop;

		private int whichRow;
		private int whichColum;

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getWhichRow() {
			return whichRow;
		}

		public void setWhichRow(int whichRow) {
			this.whichRow = whichRow;
		}

		public int getWhichColum() {
			return whichColum;
		}

		public void setWhichColum(int whichColum) {
			this.whichColum = whichColum;
		}

		public Cell(int row, int colum) {
			this.whichRow = row;
			this.whichColum = colum;
		}

		public Cell(int type, int row, int colum) {
			this.type = type;
			this.whichRow = row;
			this.whichColum = colum;
		}

		public Cell(int type, int row, int colum, int number) {
			super();
			this.type = type;
			this.whichRow = row;
			this.whichColum = colum;
			this.number = number;
		}

		/**
		 * 计算单元格上对齐坐标=行数*号码行高
		 */
		protected float caculateAlignTop() {
			int row = getWhichRow();

			return (float) row * NumberRow.rowHight;
		}

		/**
		 * 计算单元格左对齐坐标=期号列宽+（列数-1）*号码单元格列宽
		 */
		protected float caculateAlignLeft() {
			int colum = getWhichColum();

			if (isBatchCodeCell(colum)) {
				return 0;
			} else {
				return (float) BatchCodeCell.cellWidth + (colum - 1)
						* NumberCell.cellWidth;
			}
		}

		/**
		 * 计算文字基准X坐标:如果是第一列，计算基准为标题单元格宽度；如果是其它列，为数字单元格宽度
		 */
		protected float caculateTextAlighLeft(float left) {
			float textBaseX;

			if (whichColum == 0) {
				textBaseX = (BatchCodeCell.cellWidth / 2) + left;
			} else {
				textBaseX = (NumberCell.cellWidth / 2) + left;
			}

			return textBaseX;
		}

		/**
		 * 计算文字基准Y坐标，如果是选号行，则以选号行高位基准；如为号码行，则以号码行高为基准。
		 */
		protected float caculateTextAlignTop(float top) {
			FontMetrics fontMetrics = paint.getFontMetrics();

			float textBaseY;
			float fontHeight = fontMetrics.bottom - fontMetrics.top;

			if (whichRow == viewRowNum) {
				textBaseY = (SelectRow.rowHight
						- (SelectRow.rowHight - fontHeight) / 2 - fontMetrics.bottom)
						+ top;
			} else {
				textBaseY = (NumberRow.rowHight
						- (NumberRow.rowHight - fontHeight) / 2 - fontMetrics.bottom)
						+ top;
			}

			return textBaseY;
		}

	}

	/**
	 * 期号单元格类
	 */
	static class BatchCodeCell extends Cell {
		static Bitmap singleTitleBitmap;
		static Bitmap doubleTitleBitmap;
		static Bitmap titleSelectBitmap;

		private String batchCode;
		static int cellWidth;

		public void setBatchCode(String batchCode) {
			this.batchCode = batchCode;
		}

		public BatchCodeCell(int row, int colum) {
			super(row, colum);
		}

		public BatchCodeCell(String batchCode, int row, int colum) {
			super(row, colum);
			this.batchCode = batchCode;
		}

		public void onDrawbackground(Canvas canvas) {
			alignLeft = caculateAlignLeft();
			alignTop = caculateAlignTop();

			// 奇偶行绘制不同图片
			if (getWhichRow() % 2 == 0) {
				canvas.drawBitmap(doubleTitleBitmap, alignLeft, alignTop, null);
			} else {
				canvas.drawBitmap(singleTitleBitmap, alignLeft, alignTop, null);
			}

			textAlignLeft = caculateTextAlighLeft(alignLeft);
			textAlignTop = caculateTextAlignTop(alignTop);

			// 如果是选号标题，则绘制选号字符串
			if (batchCode != null && batchCode.equals("期号")) {
				canvas.drawBitmap(titleSelectBitmap, alignLeft, alignTop, null);

				paint.setColor(Color.BLACK);
				canvas.drawText("", textAlignLeft, textAlignTop, paint);
			}
		}

		public void onDrawBatchCode(Canvas canvas) {
			String childCode = batchCode.substring(8, batchCode.length());
			paint.setColor(Color.BLACK);
			canvas.drawText(childCode+"期", textAlignLeft, textAlignTop, paint);
		}
		

		// add by zhangkaikai for the color of qihao is baise bug
		public void onDrawBathCode(Canvas canvas, Paint paint) {
			canvas.drawText(batchCode, textAlignLeft, textAlignTop, paint);
		}
	}

	/**
	 * 选号单元格类：包括选号单元格和号码单元格
	 */
	static class NumberCell extends Cell {
		private boolean isTouched = false;

		static Bitmap redSelectBitmap;
//		static Bitmap blueSelectBitmap;
		static Bitmap normalSelectBitmap;

		static int cellWidth;

		public boolean isTouched() {
			return isTouched;
		}

		public void setTouched(boolean touched) {
			this.isTouched = touched;
		}

		public NumberCell(int type, int row, int colum) {
			super(type, row, colum);
		}

		public NumberCell(int type, int row, int colum, int number) {
			super(type, row, colum, number);
		}

		public void onDrawBackground(Canvas canvas) {
			float left = caculateAlignLeft();
			float top = caculateAlignTop();

			paint.setColor(Color.BLACK);
			canvas.drawBitmap(normalSelectBitmap, left, top, null);

			textAlignLeft = caculateTextAlighLeft(left);
			textAlignTop = caculateTextAlignTop(top);

			// 绘制小球上的号码
			String numberString = formatNumberToTwoPlace(getNumber());
			canvas.drawText(numberString, textAlignLeft, textAlignTop, paint);
		}
	}

	/**
	 * 彩票号码单元格类
	 */
	static class LotteryNumberCell extends Cell {
		private int loss;

		static Bitmap whiteGridBitmap;
		static Bitmap grayGridBitmap;
		static Bitmap redBallBitmap;
		static Bitmap blueBallBitmap;

		public int getLoss() {
			return loss;
		}

		public void setLoss(int loss) {
			this.loss = loss;
		}

		public LotteryNumberCell(int type, int row, int colum) {
			super(type, row, colum);
		}

		public void onDrawBackground(Canvas canvas) {
			int row = getWhichRow();
			int colum = getWhichColum();

			alignLeft = caculateAlignLeft();
			alignTop = caculateAlignTop();

			// 奇偶行不同的绘制规则
			if (row % 2 == 0) {
				// 奇偶列不同的绘制图片
				if (colum % 2 == 0) {
					canvas.drawBitmap(grayGridBitmap, alignLeft, alignTop, null);
				} else {
					canvas.drawBitmap(whiteGridBitmap, alignLeft, alignTop,
							null);
				}
			} else {
				if (colum % 2 == 0) {
					canvas.drawBitmap(whiteGridBitmap, alignLeft, alignTop,
							null);
				} else {
					canvas.drawBitmap(grayGridBitmap, alignLeft, alignTop, null);
				}
			}
		}

		public void onDrawLossValue(Canvas canvas) {
			textAlignLeft = caculateTextAlighLeft(alignLeft);
			textAlignTop = caculateTextAlignTop(alignTop);

			paint.setColor(Color.BLACK);
//			canvas.drawText(String.valueOf(loss), textAlignLeft, textAlignTop,
//					paint);
		}

		public void onDrawPrizeInfo(Canvas canvas) {
			int type = getType();

			// 绘制小球
			if (type == Cell.RED_BALL) {
				canvas.drawBitmap(redBallBitmap, (alignLeft+4*ratio), (alignTop+4*ratio), null);
			} 

			// 绘制球号
			paint.setColor(Color.WHITE);
			String ballString = String.valueOf(getNumber());
			canvas.drawText(ballString, textAlignLeft, textAlignTop, paint);
		}
	}

}
