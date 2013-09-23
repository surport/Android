package com.ruyicai.android.controller.compontent.pagerindicator;

import com.ruyicai.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 购彩大厅页面指示器控件：用于指示当前显示页面的位置，默认显示三页，当前页为第二页
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-27
 */
public class BuyLotteryHallPageIndicator extends LinearLayout {
	/** 上下文对象 */
	private Context	_fContext;
	/** 页面的数量：默认显示三页 */
	private int		_fPageNums		= 3;
	/** 当前显示的页面：默认显示第二页 */
	private int		_fPagePresent	= 1;

	public BuyLotteryHallPageIndicator(Context aContext) {
		super(aContext);
		_fContext = aContext;
		initWithPageNumsAndPagePresent(_fPageNums, _fPagePresent);
	}

	public BuyLotteryHallPageIndicator(Context aContext, AttributeSet aAttrs) {
		super(aContext, aAttrs);
		_fContext = aContext;
		initWithPageNumsAndPagePresent(_fPageNums, _fPagePresent);
	}

	/**
	 * 设置当前显示的页数
	 * 
	 * @param aPresentPage
	 *            当前显示的页数
	 */
	public void setPresentPage(int aPresentPage) {
		_fPagePresent = aPresentPage;
		initWithPageNumsAndPagePresent(_fPageNums, _fPagePresent);
	}

	/**
	 * 设置显示的页数数目
	 * 
	 * @param aPageNums
	 *            显示的页数数目
	 */
	public void setPageNums(int aPageNums) {
		_fPageNums = aPageNums;
		initWithPageNumsAndPagePresent(_fPageNums, _fPagePresent);
	}

	/**
	 * 使用页面的数量初始化页面指示器
	 * 
	 * @param aPageNums
	 *            页面的数量
	 * @param aPagePresent
	 *            当前页数
	 */
	public void initWithPageNumsAndPagePresent(int aPageNums, int aPagePresent) {
		// 保存页面数目和当前页属性
		_fPageNums = aPageNums;
		_fPagePresent = aPagePresent;

		// 移除所有的指示灯
		removeAllViews();

		for (int page_i = 0; page_i < _fPageNums; page_i++) {
			// 创建指示灯图片视图
			ImageView pageImageView = new ImageView(_fContext);

			// 如果是当前页，则设置为当前页指示灯；如果不是，则设置其它页指示灯
			if (page_i == _fPagePresent) {
				pageImageView
						.setBackgroundResource(R.drawable.buylotteryhall_pageindicator_present);
			} else {
				pageImageView
						.setBackgroundResource(R.drawable.buylotteryhall_pageindicator_other);
			}

			// 设置指示灯图片的长宽，边距和权重属性
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.weight = 1.0f;
			layoutParams.leftMargin = 8;
			layoutParams.rightMargin = 8;
			// 将指示灯图片添加到布局中
			addView(pageImageView, layoutParams);
		}
	}
}
