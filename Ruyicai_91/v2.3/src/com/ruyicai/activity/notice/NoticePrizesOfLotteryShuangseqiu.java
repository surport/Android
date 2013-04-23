package com.ruyicai.activity.notice;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.common.RuyicaiActivityManager;

public class NoticePrizesOfLotteryShuangseqiu extends Activity {

	private static final String[] shuangseqiu_LotteryIssue = { "第2010026期",
			"第2010025期", "第2010024期", "第201023期" };
	private static final String[] shuangseqiu_NoticeDateAndTime = {
			"2010-06-01", "2010-06-01", "2010-06-01", "2010-06-01" };
	private static final String[] shuangseqiu_WinningNum = {
			"中奖号码：01,02,03,04,05,06,07", "中奖号码：01,02,03,04,05,06,07",
			"中奖号码：01,02,03,04,05,06,07", "中奖号码：01,02,03,04,05,06,07" };
	private static final String[] shuangseqiu_FinalPrizesDate = {
			"结奖日期：2010-08-01", "结奖日期：2010-08-01", "结奖日期：2010-08-01",
			"结奖日期：2010-08-01" };
	private static final String[] shuangseqiu_TotalSum = { "总金额：100万元",
			"总金额：100万元", "总金额：100万元", "总金额：100万元" };

	TextView noticePrizesTitle;
	TextView attention;

	public void onCreate(Bundle savedInstanceState) {
//		RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.notice_prizes_single_specific_main);

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.shuangseqiu_kaijianggonggao);

		attention = (TextView) findViewById(R.id.notice_prizes_single_specific_attention);
		attention.setText(R.string.shuangseqiu_attention);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		EfficientAdapter adapter = new EfficientAdapter(this);
		listview.setAdapter(adapter);
	}

	public static class EfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return shuangseqiu_LotteryIssue.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.notice_prizes_single_specific_layout, null);
				holder = new ViewHolder();
				holder.lotteryIssueView = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_issue_id);
				holder.noticeDateAndTimeView = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_noticedDate_id);

				holder.finalPrizesDateView = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_finalPrizesDate_id);
				holder.totalSumView = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_totalSum_id);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}


			if (position == 1) {
				holder.lotteryIssueView
						.setText(shuangseqiu_LotteryIssue[position]);
				holder.noticeDateAndTimeView
						.setText(shuangseqiu_NoticeDateAndTime[position]);
				holder.winningNumView.setText(shuangseqiu_WinningNum[position]);
				holder.finalPrizesDateView
						.setText(shuangseqiu_FinalPrizesDate[position]);
				holder.totalSumView.setText(shuangseqiu_TotalSum[position]);
			} else if (position == 2) {
				holder.lotteryIssueView
						.setText(shuangseqiu_LotteryIssue[position]);
				holder.noticeDateAndTimeView
						.setText(shuangseqiu_NoticeDateAndTime[position]);
				holder.winningNumView.setText(shuangseqiu_WinningNum[position]);
				holder.finalPrizesDateView
						.setText(shuangseqiu_FinalPrizesDate[position]);
				holder.totalSumView.setText(shuangseqiu_TotalSum[position]);
			} else if (position == 3) {
				holder.lotteryIssueView
						.setText(shuangseqiu_LotteryIssue[position]);
				holder.noticeDateAndTimeView
						.setText(shuangseqiu_NoticeDateAndTime[position]);
				holder.winningNumView.setText(shuangseqiu_WinningNum[position]);
				holder.finalPrizesDateView
						.setText(shuangseqiu_FinalPrizesDate[position]);
				holder.totalSumView.setText(shuangseqiu_TotalSum[position]);
			} else {
				holder.lotteryIssueView
						.setText(shuangseqiu_LotteryIssue[position]);
				holder.noticeDateAndTimeView
						.setText(shuangseqiu_NoticeDateAndTime[position]);
				holder.winningNumView.setText(shuangseqiu_WinningNum[position]);
				holder.finalPrizesDateView
						.setText(shuangseqiu_FinalPrizesDate[position]);
				holder.totalSumView.setText(shuangseqiu_TotalSum[position]);
			}

			return convertView;
		}

		static class ViewHolder {
			TextView lotteryIssueView;
			TextView noticeDateAndTimeView;
			TextView winningNumView;
			TextView finalPrizesDateView;
			TextView totalSumView;
		}
	}

}
