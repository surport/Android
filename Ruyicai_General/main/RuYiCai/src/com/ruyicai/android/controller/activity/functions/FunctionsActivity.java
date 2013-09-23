package com.ruyicai.android.controller.activity.functions;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.inject.Inject;
import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.activity.home.HomeActivity;
import com.ruyicai.android.controller.adapter.viewpager.ViewPagerAdapter;
import com.ruyicai.android.model.bean.PhoneInfo;
import com.ruyicai.android.tools.ImageTools;

/**
 * 应用程序新功能展示页面: 1.通过新功能图片展示应用的新功能
 * 
 * @author PengCX
 * @since RYC1.0 2013-2-22
 */
public class FunctionsActivity extends BaseActivity implements OnClickListener {
	/** 功能展示页面数目 */
	private static final int	FUNCTIONS_PAGE_NUM	= 5;

	/** 图片资源id数组 */
	private int[]				functionImages		= {
			R.drawable.functions_iamgeview_function1,
			R.drawable.functions_iamgeview_function2,
			R.drawable.functions_iamgeview_function3,
			R.drawable.functions_iamgeview_function4,
			R.drawable.functions_iamgeview_function5 };

	/** 显示新功能介绍 */
	@InjectView(R.id.functions_viewpager_introduce)
	private ViewPager			_fFunctionsViewPager;

	/** 引用对象：WindowManager */
	@Inject
	WindowManager				_fWindowManager;
	/** 引用对象：LayoutInflater */
	@Inject
	LayoutInflater				_fLayoutInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.functions_activity);

		List<View> functionsaList = getPageViewList();
		// 设置ViewPager的适配器
		setViewPageAdapter(functionsaList);
	}

	/**
	 * 获取显示功能页面的视图集合
	 * 
	 * @return 功能页面的视图集合
	 */
	private List<View> getPageViewList() {
		// 功能页面视图数组
		List<View> functionsaList = new ArrayList<View>();

		// 设置功能页面视图图片和按钮属性
		for (int function_i = 0; function_i < FUNCTIONS_PAGE_NUM; function_i++) {
			// 获取功能页面视图独享
			View functionView = _fLayoutInflater.inflate(
					R.layout.functions_viewpager_item, null);

			// 设置功能图片
			setFunctionImage(functionView, function_i);

			// 如果是最后一个功能页面，显示开始体验按钮，并跳转主页面
			if (isLastPage(function_i)) {
				setStartButton(functionView);
			}

			functionsaList.add(functionView);
		}

		return functionsaList;
	}

	private void setViewPageAdapter(List<View> functionsaList) {
		// 设置viewPager适配器
		ViewPagerAdapter functionPageAdapter = new ViewPagerAdapter(
				functionsaList);
		_fFunctionsViewPager.setAdapter(functionPageAdapter);

		// 设置默认显示页面
		_fFunctionsViewPager.setCurrentItem(0);
	}

	/**
	 * 设置开始体验按钮属性
	 * 
	 * @param _fFunctionItemButton
	 *            开始体验按钮
	 */
	private void setStartButton(View functionView) {
		ImageButton _fFunctionItemButton = (ImageButton) functionView
				.findViewById(R.id.functions_imagebutton_start);

		_fFunctionItemButton.setVisibility(View.VISIBLE);
		_fFunctionItemButton.setClickable(true);
		_fFunctionItemButton.setOnClickListener(this);
	}

	/**
	 * 判断是否是最后一个功能页面
	 * 
	 * @param function_i
	 *            页面索引
	 * @return 是否是最后一个功能页面
	 */
	private boolean isLastPage(int function_i) {
		return function_i == FUNCTIONS_PAGE_NUM - 1;
	}

	/**
	 * 设置功能页面的背景图片
	 * 
	 * @param function_i
	 *            功能页面索引
	 * @param _fFunctionItemImageView
	 *            功能显示ImageView对象
	 */
	private void setFunctionImage(View functionView, int function_i) {
		// 获取并设置功能页面背景
		ImageView _fFunctionItemImageView = (ImageView) functionView
				.findViewById(R.id.functions_imageview_viewpageitem);

		PhoneInfo phoneInfo = PhoneInfo.getInstance(this);
		int screenWidth = _fWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = _fWindowManager.getDefaultDisplay().getHeight();

		Bitmap functionBitmap = ImageTools
				.scaleBitmapFromResourceBaseDestinationSize(getResources(),
						functionImages[function_i], screenWidth, screenHeight);

		_fFunctionItemImageView.setImageBitmap(functionBitmap);
	}

	/**
	 * 处理页面中的按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(FunctionsActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
}
