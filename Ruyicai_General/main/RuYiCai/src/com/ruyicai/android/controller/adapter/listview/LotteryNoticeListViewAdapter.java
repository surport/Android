package com.ruyicai.android.controller.adapter.listview;

import com.ruyicai.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 开奖列表适配器：包含彩种图标，彩种名称，开奖日期，开奖期号和开奖号码。在开奖公告页面中使用。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-4
 */
public class LotteryNoticeListViewAdapter extends BaseAdapter {
	/** 上下文对象 */
	private Context		_fContext;
	/** 彩种图标图片资源Id数组 */
	private int[]		_fLotteryIconResourceIds;
	/** 彩种名称字符串资源Id数组 */
	private String[]	_fLotteryNameStrings;
	/** 开奖日期数组 */
	private String[]	_fNoticeDateStrings;
	/** 期号数组 */
	private String[]	_fNoticeIssueStrings;

	/**
	 * 构造函数
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aLotteryIconResourceIds
	 *            图标图片资源Id数组
	 * @param aLotteryNameStrings
	 *            彩种字符串名称资源Id数组
	 * @param aNoticeDatesStrings
	 *            开奖日期字符串数组
	 * @param aNoticeIssueStrings
	 *            开奖期号字符串数组
	 */
	public LotteryNoticeListViewAdapter(Context aContext,
			int[] aLotteryIconResourceIds, String[] aLotteryNameStrings,
			String[] aNoticeDatesStrings, String[] aNoticeIssueStrings) {
		super();
		_fContext = aContext;
		_fLotteryIconResourceIds = aLotteryIconResourceIds;
		_fLotteryNameStrings = aLotteryNameStrings;
		_fNoticeDateStrings = aNoticeDatesStrings;
		_fNoticeIssueStrings = aNoticeIssueStrings;
	}

	@Override
	public int getCount() {
		return _fLotteryIconResourceIds.length;
	}

	@Override
	public Object getItem(int aPosition) {
		return _fLotteryIconResourceIds[aPosition];
	}

	@Override
	public long getItemId(int aPosition) {
		return aPosition;
	}

	@Override
	public View getView(int aPosition, View aConvertView, ViewGroup aParent) {
		ViewHoler viewHoler = null;
		if (viewHoler == null) {
			viewHoler = new ViewHoler();

			LayoutInflater fInflater = LayoutInflater.from(_fContext);
			aConvertView = fInflater.inflate(
					R.layout.lotterynotice_listview_item, null);

			viewHoler._fLotteryIconImageView = (ImageView) aConvertView
					.findViewById(R.id.lotterynoticelistviewitem_imageview_lotteyico);
			viewHoler._fLotteryNameTextView = (TextView) aConvertView
					.findViewById(R.id.lotterynoticelistviewitem_textview_lotteryname);
			viewHoler._fNoticeDaTextView = (TextView) aConvertView
					.findViewById(R.id.lotterynoticelistviewitem_textview_noticedate);
			viewHoler._fNoticeIssueTextView = (TextView) aConvertView
					.findViewById(R.id.lotterynoticelistviewitem_textview_noticeissue);

			aConvertView.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) aConvertView.getTag();
		}

		viewHoler._fLotteryIconImageView
				.setImageResource(_fLotteryIconResourceIds[aPosition]);
		viewHoler._fLotteryNameTextView
				.setText(_fLotteryNameStrings[aPosition]);
		viewHoler._fNoticeDaTextView.setText(_fNoticeDateStrings[aPosition]);
		viewHoler._fNoticeIssueTextView
				.setText(_fNoticeIssueStrings[aPosition]);

		return aConvertView;
	}

	static class ViewHoler {
		/** 彩种图标 */
		private ImageView	_fLotteryIconImageView;
		/** 彩种名称 */
		private TextView	_fLotteryNameTextView;
		/** 开奖日期 */
		private TextView	_fNoticeDaTextView;
		/** 开奖期号 */
		private TextView	_fNoticeIssueTextView;
	}
}