package com.ruyicai.android.controller.compontent.slideseekbar;

import com.ruyicai.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SlideSeekBar extends LinearLayout {
	/** 上下文对象 */
	private Context		_fContext;
	/** 减少按钮 */
	private ImageButton	_fReduceImageButton;
	/** 滚动条 */
	private SeekBar		_fSlideSeekBar;
	/** 增加按钮 */
	private ImageButton	_fAddImageButton;
	/** 值文本框 */
	private EditText	_fValueEditText;

	{
		// 初始化代码块
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.slideseek_bar, this);

		_fReduceImageButton = (ImageButton) findViewById(R.id.slideseekbar_imagebutton_reduce);
		_fReduceImageButton
				.setOnClickListener(new SlideSeekBarButtonOnClickListener());
		_fSlideSeekBar = (SeekBar) findViewById(R.id.slideseekbar_seekbar);
		_fSlideSeekBar
				.setOnSeekBarChangeListener(new SlideSeekBarOnSeekBarChangedListener());
		_fAddImageButton = (ImageButton) findViewById(R.id.slideseekbar_imagebutton_add);
		_fAddImageButton
				.setOnClickListener(new SlideSeekBarButtonOnClickListener());
		_fValueEditText = (EditText) findViewById(R.id.slideseekbar_edittext_value);
		_fValueEditText.setText(String.valueOf(_fSlideSeekBar.getProgress()));
	}

	public SlideSeekBar(Context aContext) {
		super(aContext);
		_fContext = aContext;
	}

	public SlideSeekBar(Context aContext, AttributeSet aAttrs) {
		super(aContext, aAttrs);
		_fContext = aContext;
	}

	/**
	 * 滑动栏按钮事件监听实现类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-20
	 */
	class SlideSeekBarButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int progress = _fSlideSeekBar.getProgress();

			switch (v.getId()) {
			case R.id.slideseekbar_imagebutton_add:
				if (progress < _fSlideSeekBar.getMax()) {
					progress++;
				}
				break;
			case R.id.slideseekbar_imagebutton_reduce:
				if (progress > 1) {
					progress--;
				}
				break;
			}

			_fSlideSeekBar.setProgress(progress);
			_fValueEditText.setText(String.valueOf(progress));
		}
	}

	/**
	 * 滑动栏滑动事件监听实现类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-20
	 */
	class SlideSeekBarOnSeekBarChangedListener implements
			OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			_fValueEditText
					.setText(String.valueOf(_fSlideSeekBar.getProgress()));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

	}

}
