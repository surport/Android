package com.ruyicai.android.controller.activity.home.lotterynotice;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.adapter.listview.LotteryNoticeListViewAdapter;
import com.ruyicai.android.controller.compontent.bar.TitleBar;
import com.ruyicai.android.controller.compontent.bar.TitleBarInterface;
import com.ruyicai.android.model.bean.lottery.LotteryType;

public class LotteryNoticeActivity extends BaseActivity {

	/** 引用视图：开奖公告标题栏 */
	@InjectView(R.id.lotteynotice_title_bar)
	private TitleBar	_fTitleBar;
	/** 引用视图：开奖公告列表 */
	@InjectView(R.id.lotterynotice_listview_notice)
	private ListView	_fNoticeListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lotterynotice_activity);

		_fTitleBar.set_fTitleBarInterface(new LotteryNoticeTitltBarInterface());
		_fTitleBar.initTitleBarShow();

		int[] lotteryIconResourceIds = getNoticeListViewIconResoruceIds();
		String[] lotteryNameStrings = LotteryType.getAllLotteryNames();
		String[] noticeDateStrings = getNoticeListViewNoticeDataStrings();
		String[] noticeIssuesStrings = getNoticeListViewNoticeIssueStrings();

		LotteryNoticeListViewAdapter lotteryNoticeListViewAdapter = new LotteryNoticeListViewAdapter(
				this, lotteryIconResourceIds, lotteryNameStrings,
				noticeDateStrings, noticeIssuesStrings);
		_fNoticeListView.setAdapter(lotteryNoticeListViewAdapter);
	}

	/**
	 * 获取开奖公告列表开奖期号字符串数组
	 * 
	 * @return 开奖公告期号字符串数组
	 */
	private String[] getNoticeListViewNoticeIssueStrings() {
		String[] noticeIssues = new String[16];

		noticeIssues[0] = "第20091232期";
		noticeIssues[1] = "第20091232期";
		noticeIssues[2] = "第20091232期";
		noticeIssues[3] = "第20091232期";
		noticeIssues[4] = "第20091232期";
		noticeIssues[5] = "第20091232期";
		noticeIssues[6] = "第20091232期";
		noticeIssues[7] = "第20091232期";
		noticeIssues[8] = "第20091232期";
		noticeIssues[9] = "第20091232期";
		noticeIssues[10] = "第20091232期";
		noticeIssues[11] = "第20091232期";
		noticeIssues[12] = "第20091232期";
		noticeIssues[13] = "第20091232期";
		noticeIssues[14] = "第20091232期";
		noticeIssues[15] = "第20091232期";

		return noticeIssues;
	}

	/**
	 * 获取开奖公告列表开奖日期字符串数组
	 * 
	 * @return 开奖日期字符串数组
	 */
	private String[] getNoticeListViewNoticeDataStrings() {
		String[] noticeDates = new String[16];

		noticeDates[0] = "开奖日期：2013-12-4";
		noticeDates[1] = "开奖日期：2013-12-4";
		noticeDates[2] = "开奖日期：2013-12-4";
		noticeDates[3] = "开奖日期：2013-12-4";
		noticeDates[4] = "开奖日期：2013-12-4";
		noticeDates[5] = "开奖日期：2013-12-4";
		noticeDates[6] = "开奖日期：2013-12-4";
		noticeDates[7] = "开奖日期：2013-12-4";
		noticeDates[8] = "开奖日期：2013-12-4";
		noticeDates[9] = "开奖日期：2013-12-4";
		noticeDates[10] = "开奖日期：2013-12-4";
		noticeDates[11] = "开奖日期：2013-12-4";
		noticeDates[12] = "开奖日期：2013-12-4";
		noticeDates[13] = "开奖日期：2013-12-4";
		noticeDates[14] = "开奖日期：2013-12-4";
		noticeDates[15] = "开奖日期：2013-12-4";

		return noticeDates;
	}

	/**
	 * 获取开奖列表图标图片资源Id数组
	 * 
	 * @return 图标图片资源Id数组
	 */
	private int[] getNoticeListViewIconResoruceIds() {
		int[] _iconResourceIds = new int[LotteryType.getAllLotteryNums()];

		_iconResourceIds[0] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[1] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[2] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[3] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[4] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[5] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[6] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[7] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[8] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[9] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[10] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[11] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[12] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[13] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[14] = R.drawable.lotterynotice_imageview_arrangefive;
		_iconResourceIds[15] = R.drawable.lotterynotice_imageview_arrangefive;

		return _iconResourceIds;
	}

	class LotteryNoticeTitltBarInterface implements TitleBarInterface {
		@Override
		public void setTitleTextView() {
			_fTitleBar._fTitleTextView
					.setText(R.string.lotterynotice_titlebar_text);
		}

		@Override
		public void setTitleButton() {
			_fTitleBar._fLoginOrRegistButton.setVisibility(View.VISIBLE);
			_fTitleBar._fLoginOrRegistButton
					.setText(R.string.lotterynotice_titlebar_buttontext);
			_fTitleBar._fLoginOrRegistButton
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Toast.makeText(LotteryNoticeActivity.this,
									"你点击了开奖公告的登录按钮", Toast.LENGTH_LONG).show();
						}
					});
		}
	}

}
