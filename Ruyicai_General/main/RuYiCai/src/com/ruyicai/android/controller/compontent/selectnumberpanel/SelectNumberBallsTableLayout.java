package com.ruyicai.android.controller.compontent.selectnumberpanel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * 选号小球表格：容纳选号小球的表格
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-14
 */
public class SelectNumberBallsTableLayout extends TableLayout {
	/** 上下文对象 */
	private Context					_fContext;

	/** 选号面板选号小球的行数：默认为1行 */
	private int						_fSelectBallTableLayoutRowNum			= 1;
	/** 选号面板每行的列数:默认为9列 */
	private int						_fSelectBallTableLayoutColumOfPreRow	= 9;
	/** 选号面板最后一行的列数 */
	private int						_fSelectBallTableLayoutColumOfLastRow;

	/** 选号面板中选号小球数组 */
	private SelectNumberBall[]		_fSelectNumberBalls;
	/** 选号小球起始的数字：默认起始值数字为1 */
	private int						_fSelectNumberBallStartNum				= 1;
	/** 选号小球的总个数：默认为9个 */
	private int						_fSelectBallNum							= 9;
	/** 选号小球的类型：默认为红球 */
	private SelectNumberBallType	_fSelectNumberBallType					= SelectNumberBallType.REDBALL;
	/** 选号小球是否显示遗漏值：默认显示遗漏值 */
	private boolean					_fIsShowLossValue						= true;
	/** 选号小球遗漏值数组：默认全为0 */
	private int[]					_fLossValues;

	/**
	 * 获取选号小球的起始号码
	 * 
	 * @return 选号小球的起始号码
	 */
	public int get_fSelectNumberBallStartNum() {
		return _fSelectNumberBallStartNum;
	}

	/**
	 * 获取选号小球的个数
	 * 
	 * @return 选号小球的个数
	 */
	public int get_fSelectBallNum() {
		return _fSelectBallNum;
	}

	/**
	 * 设置选号小球的类型
	 * 
	 * @param _fSelectNumberBallType
	 *            选号小球的类型
	 */
	public void set_fSelectNumberBallType(
			SelectNumberBallType _fSelectNumberBallType) {
		this._fSelectNumberBallType = _fSelectNumberBallType;
	}

	/**
	 * 设置是否显示遗漏值标识
	 * 
	 * @param _fIsShowLossValue
	 *            显示遗漏值标识
	 */
	public void set_fIsShowLossValue(boolean _fIsShowLossValue) {
		this._fIsShowLossValue = _fIsShowLossValue;
	}

	/**
	 * 设置选号小球起始号码
	 * 
	 * @param _fSelectNumberBallStartNum
	 *            选号小球起始号码
	 */
	public void set_fSelectNumberBallStartNum(int _fSelectNumberBallStartNum) {
		this._fSelectNumberBallStartNum = _fSelectNumberBallStartNum;
	}

	/**
	 * 设置选号小球的个数
	 * 
	 * @param _fSelectBallNum
	 *            设置选号小球的个数
	 */
	public void set_fSelectBallNum(int _fSelectBallNum) {
		this._fSelectBallNum = _fSelectBallNum;
	}

	/**
	 * 构造函数
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aSelectNumberBallStartNum
	 *            选号小球起始号码
	 * @param aSelectBallNum
	 *            选号小球个数
	 * @param aSelectNumberBallType
	 *            选号小球类型
	 * @param aLossValues
	 *            遗漏值数组
	 * @param aIsShowLossValue
	 *            是否显示遗漏值
	 */
	public SelectNumberBallsTableLayout(Context aContext,
			int aSelectNumberBallStartNum, int aSelectBallNum,
			SelectNumberBallType aSelectNumberBallType, int[] aLossValues,
			boolean aIsShowLossValue) {
		super(aContext);

		setAttributes(aContext, aSelectNumberBallStartNum, aSelectBallNum,
				aSelectNumberBallType, aLossValues, aIsShowLossValue);

		caculateRowAndLastColum();

		createSelectNumberBalls();

		addSelectNumberBalls();
	}

	/**
	 * 设置相关的属性
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aSelectNumberBallStartNum
	 *            选号小球起始的号码
	 * @param aSelectBallNum
	 *            选号小球的个数
	 * @param aSelectNumberBallType
	 *            选号小球的类型
	 * @param aLossValues
	 *            遗漏值数组
	 * @param aIsShowLossValue
	 *            是否显示遗漏值
	 */
	private void setAttributes(Context aContext, int aSelectNumberBallStartNum,
			int aSelectBallNum, SelectNumberBallType aSelectNumberBallType,
			int[] aLossValues, boolean aIsShowLossValue) {
		_fContext = aContext;
		_fSelectNumberBallStartNum = aSelectNumberBallStartNum;
		_fSelectBallNum = aSelectBallNum;
		_fSelectNumberBallType = aSelectNumberBallType;
		_fLossValues = aLossValues;
		_fIsShowLossValue = aIsShowLossValue;
	}

	/**
	 * 计算选号面板的行列
	 */
	private void caculateRowAndLastColum() {
		_fSelectBallTableLayoutRowNum = caculateRowOfPannel(_fSelectBallNum);
		_fSelectBallTableLayoutColumOfLastRow = caculateColumOfLastRow(_fSelectBallNum);
	}

	/**
	 * 创建选号小球的集合
	 */
	private void createSelectNumberBalls() {
		_fSelectNumberBalls = new SelectNumberBall[_fSelectBallNum];

		for (int ball_i = 0; ball_i < _fSelectBallNum; ball_i++) {
			SelectNumberBall selectNumberBall = new SelectNumberBall(_fContext);

			selectNumberBall.setNumber(String
					.valueOf(_fSelectNumberBallStartNum + ball_i));
			setSelectNumberBallLossValueWithPosition(selectNumberBall, ball_i,
					_fIsShowLossValue);
			selectNumberBall.setSelectNumberBallType(_fSelectNumberBallType);

			_fSelectNumberBalls[ball_i] = selectNumberBall;
		}
	}

	/**
	 * 根据小球位置，为小球设置遗漏值
	 * 
	 * @param selectNumberBall
	 *            选号小球
	 * @param ball_i
	 *            小球的位置
	 */
	private void setSelectNumberBallLossValueWithPosition(
			SelectNumberBall aSelectNumberBall, int aBall_i,
			boolean aIsShowLossValue) {
		// 如果没有遗漏值，默认为0，并不显示
		if (_fLossValues == null) {
			aSelectNumberBall.setLossValue("0", false);
		} else {
			aSelectNumberBall.setLossValue(
					String.valueOf(_fLossValues[aBall_i]), aIsShowLossValue);
		}
	}

	/**
	 * 添加选号小球
	 */
	private void addSelectNumberBalls() {
		for (int row_i = 0; row_i < _fSelectBallTableLayoutRowNum; row_i++) {
			// 创建每行选号小球
			TableRow tableRow = new TableRow(_fContext);

			for (int colum_j = 0; colum_j < _fSelectBallTableLayoutColumOfPreRow; colum_j++) {
				if (!isLastItem(_fSelectBallNum, row_i, colum_j)) {
					// 创建每列选号小球
					SelectNumberBall selectNumberBall = getSelectNumberBallWithRowAndColum(
							row_i, colum_j);
					android.widget.TableRow.LayoutParams tableRowLayoutParams = new android.widget.TableRow.LayoutParams();
					tableRowLayoutParams.setMargins(1, 1, 1, 1);
					tableRowLayoutParams.gravity = Gravity.LEFT;
					tableRow.addView(selectNumberBall, tableRowLayoutParams);
				} else {
					break;
				}
			}

			// 将行选号小球添加到列表中
			android.widget.TableLayout.LayoutParams tableLayoutLayoutParams = new android.widget.TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			addView(tableRow, tableLayoutLayoutParams);
		}
	}

	/**
	 * 是否是最后一個选项
	 * 
	 * @param _fBallNum2
	 * @param row_i
	 * @param colum_j
	 * @return
	 */
	private boolean isLastItem(int aBallNum, int aRow, int aColum) {
		int itemIndex = aRow * _fSelectBallTableLayoutColumOfPreRow + aColum;

		if (itemIndex == aBallNum) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据行和列获取相应的小球对象
	 * 
	 * @param aRow
	 *            小球的行数
	 * @param aColum
	 *            小球的列数
	 */
	private SelectNumberBall getSelectNumberBallWithRowAndColum(int aRow,
			int aColum) {
		return _fSelectNumberBalls[(aRow * _fSelectBallTableLayoutColumOfPreRow)
				+ aColum];
	}

	/**
	 * 计算选号面板的行
	 * 
	 * @param _aBallNum
	 * @return
	 */
	private int caculateRowOfPannel(int aBallNum) {
		if (isFullRow(aBallNum)) {
			return aBallNum / _fSelectBallTableLayoutColumOfPreRow;
		} else {
			return aBallNum / _fSelectBallTableLayoutColumOfPreRow + 1;
		}
	}

	/**
	 * 是否是满行
	 * 
	 * @param _aBallNum
	 *            选号小球的个数
	 * @return 是否是满行标识
	 */
	private boolean isFullRow(int _aBallNum) {
		return _aBallNum % _fSelectBallTableLayoutColumOfPreRow == 0;
	}

	/**
	 * 计算选号面板的列
	 * 
	 * @param _aBallNum
	 * @return
	 */
	private int caculateColumOfLastRow(int aBallNum) {

		if (isFullRow(aBallNum)) {
			return _fSelectBallTableLayoutColumOfPreRow;
		} else {
			return aBallNum % _fSelectBallTableLayoutColumOfPreRow;
		}
	}

	/**
	 * 获取选中小球的号码
	 * 
	 * @return 选中小球号码集合
	 */
	public List getSelectedBallNumbers() {
		List<Integer> selectedNumberList = new ArrayList<Integer>();

		for (int ball_i = 0; ball_i < _fSelectBallNum; ball_i++) {
			SelectNumberBall selectNumberBall = _fSelectNumberBalls[ball_i];

			if (selectNumberBall.isSelected()) {
				selectedNumberList.add(selectNumberBall.getNumber());
			}
		}

		return selectedNumberList;
	}

	/**
	 * 清空选中的号码小球
	 */
	public void clearSelectedNumberBalls() {
		for (int ball_i = 0; ball_i < _fSelectBallNum; ball_i++) {
			SelectNumberBall selectNumberBall = _fSelectNumberBalls[ball_i];

			if (selectNumberBall.isSelected()) {
				selectNumberBall.cancelSelected();
			}
		}
	}

	/**
	 * 选定指定的小球，小组数组中是指定的小球的位置，如{0,3,4}指定第0,3和4个小球选中（注意，小球的个数从0个开始）
	 * 
	 * @param aSpecifiedNumber
	 *            指定小球的号码
	 */
	public void selectSpecifiedNumberBalls(List<Integer> aSpecifiedNumbers) {
		// 清空当前选取的小球
		clearSelectedNumberBalls();

		// 遍历所有小球，并根据指定的号码集合设置相应的小球为选中
		int specifiedNum = aSpecifiedNumbers.size();
		for (int ball_i = 0; ball_i < specifiedNum; ball_i++) {
			// 注意：小球对象数组是从0开始，小球编号是从1开始，所有做了-1的处理
			_fSelectNumberBalls[aSpecifiedNumbers.get(ball_i) - 1]
					.setSelected();
		}
	}
}
