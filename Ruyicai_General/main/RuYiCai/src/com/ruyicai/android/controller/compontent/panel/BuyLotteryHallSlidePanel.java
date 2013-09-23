package com.ruyicai.android.controller.compontent.panel;

import java.util.List;

import com.ruyicai.android.model.bean.lottery.Lottery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * 购彩大厅彩种展示面板控件 1.提供彩种的图标 2.提供彩种的标题 3.提供彩种的开奖和停售情况 4、自动根据屏幕计算行和列
 * 
 * @author PengCX
 * @since RYC1.0 2013-3-20
 */
public class BuyLotteryHallSlidePanel extends TableLayout {
	/** 上下文对象 */
	private Context	_fContext;

	/** 每行面板项目的个数，默认为3个选项 */
	private int		_fPreRowItemNum	= 3;
	/** 面板选项的行数 */
	private int		_fRowNum;
	/** 面板选项的最后一行列数 */
	private int		_fColumOfLastRow;
	/** 面板选项的个数 */
	private int		_fItemNum;

	public BuyLotteryHallSlidePanel(Context aContext) {
		super(aContext);
		_fContext = aContext;
	}

	public BuyLotteryHallSlidePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 使用项目数组初始化彩种展示面板显示控件的显示
	 * 
	 * @param aItemList
	 *            项目数据对象
	 * @return 显示视图对象数据
	 */
	public void initBuyLotteryHallPanelWithList(List<Object> aItemList) {
		_fItemNum = aItemList.size();
		// 计算面板的行数和最后一行的列数
		caculatePanelRowAndColum();

		for (int row_i = 0; row_i < _fRowNum; row_i++) {
			// 创建每行
			TableRow tableRow = new TableRow(_fContext);

			for (int colum_j = 0; colum_j < _fPreRowItemNum; colum_j++) {
				if (!isLastItem(_fItemNum, row_i, colum_j)) {
					// 创建每列面板选项
					BuyLotteryHallSlidePanelItem buyLotteryHallPanelItem = initPanelItem(
							aItemList, row_i, colum_j);
					android.widget.TableRow.LayoutParams layoutParams = new android.widget.TableRow.LayoutParams();
					layoutParams.weight = 1.0f;
					tableRow.addView(buyLotteryHallPanelItem, layoutParams);
				} else {
					break;
				}
			}
			// 将行添加到列表中
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			layoutParams.weight = 1.0f;
			addView(tableRow, layoutParams);
		}
	}

	/**
	 * 是否是最后一个选项
	 * 
	 * @param aList
	 *            选项列表
	 * @param aRow
	 * @param aColum
	 * @return是否是最后一个选项标识
	 */
	private boolean isLastItem(int aItemNum, int aRow, int aColum) {
		int itemIndex = aRow * _fPreRowItemNum + aColum;

		if (itemIndex == aItemNum) {
			return true;
		} else {
			return false;
		}
	}

	private BuyLotteryHallSlidePanelItem initPanelItem(List<Object> aItemList,
			int row_i, int colum_j) {
		// 创建面板选项对象
		BuyLotteryHallSlidePanelItem buyLotteryHallPanelItem = new BuyLotteryHallSlidePanelItem(
				_fContext);
		// 根据行列获取选项对象
		Object itemObject = getItemWithRowAndColum(aItemList, row_i, colum_j);

		// 使用选项对象初始化面板选项
		if (itemObject instanceof Lottery) {
			buyLotteryHallPanelItem
					.initBuyLotteryHallPanelItemShowWithLottery((Lottery) itemObject);
		} else {
			buyLotteryHallPanelItem
					.initBuyLotteryHallPanelItemShowWithItemName((String) itemObject);
		}

		return buyLotteryHallPanelItem;
	}

	/**
	 * 计算面板中选项的行数和最后一行的列数
	 */
	private void caculatePanelRowAndColum() {
		_fRowNum = caculateRowOfPannel(_fItemNum);
		_fColumOfLastRow = caculateColumOfLastRow(_fItemNum);
	}

	/**
	 * 使用行和列数获取选项对象
	 * 
	 * @param aItemList
	 *            选项数组
	 * @param row_i
	 *            行数
	 * @param colum_j
	 *            列数
	 * @return 选项对象
	 */
	private Object getItemWithRowAndColum(List<Object> aItemList, int row_i,
			int colum_j) {
		return aItemList.get(row_i * _fPreRowItemNum + colum_j);
	}

	/**
	 * 计算最后一行的列数
	 * 
	 * @param itemNum
	 *            选项的个数
	 * @return 最后一行的列数
	 */
	private int caculateColumOfLastRow(int aItemNum) {
		if (isFullRow(aItemNum)) {
			return _fPreRowItemNum;
		} else {
			return aItemNum % _fPreRowItemNum;
		}
	}

	/**
	 * 计算面板中的行数，使用“取整的原则“：如7个选项，则为2行多1个选项，返回3行
	 * 
	 * @param aItemNum
	 *            选项个数
	 * @return 行数
	 */
	private int caculateRowOfPannel(int aItemNum) {
		if (isFullRow(aItemNum)) {
			return aItemNum / _fPreRowItemNum;
		} else {
			return aItemNum / _fPreRowItemNum + 1;
		}
	}

	/**
	 * 是否是满的行
	 * 
	 * @param aItemNum
	 *            选项的个数
	 * @return 是否是满行标识
	 */
	private boolean isFullRow(int aItemNum) {
		return aItemNum % _fPreRowItemNum == 0;
	}
}
