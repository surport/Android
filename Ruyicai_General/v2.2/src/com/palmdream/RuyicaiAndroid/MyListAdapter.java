package com.palmdream.RuyicaiAndroid;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList;
	private String[] mIndex;

	public static final int BALL_WIDTH = 28;
	private static int[] aRedColorResId = { R.drawable.red};
	private static int[] aBlueColorResId = { R.drawable.blue };

	public MyListAdapter(Context context, String[] index,
			List<Map<String, Object>> list) {
		mInflater = LayoutInflater.from(context);
		mList = list;
		mIndex = index;
	}

	public int getCount() {
		return mList.size();
		// TODO Auto-generated method stub
		// return 0;
	}

	public Object getItem(int position) {
		return mList.get(position);
		// TODO Auto-generated method stub
		// return null;
	}

	public long getItemId(int position) {
		return position;
		// TODO Auto-generated method stub
		// return 0;
	}

	// public View getView(int arg0, View arg1, ViewGroup arg2) {
	public View getView(int position, View convertView, ViewGroup parent) {
		// if (convertView == null) {
		String iGameType = (String) mList.get(position).get(mIndex[0]);
		String iNumbers = (String) mList.get(position).get(mIndex[1]);
		String iDate = (String) mList.get(position).get(mIndex[2]);
		String iIssueNo = (String) mList.get(position).get(mIndex[3]);

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.notice_prizes_main_layout,
					null);

			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView
					.findViewById(R.id.notice_prezes_lotteryNameId);
			holder.numbers = (LinearLayout) convertView
					.findViewById(R.id.ball_linearlayout);

			holder.date = (TextView) convertView
					.findViewById(R.id.notice_prizes_dateAndTimeId);
			holder.date.setText(iDate);
			holder.issue = (TextView) convertView
					.findViewById(R.id.notice_prizes_issueId);
			holder.issue.setText(iIssueNo);
			if (iGameType.equalsIgnoreCase("ssq")) {
				holder.name.setText("Ë«É«Çò");
				holder.icon.setImageResource(R.drawable.shuangseqiu);

				int i1;
				String iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 6; i1++) {
					iShowNumber = iNumbers.substring(i1 * 2, i1 * 2 + 2);
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
							aRedColorResId);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					holder.numbers.addView(tempBallView, lp);
				}

				iShowNumber = iNumbers.substring(12, 14);
				tempBallView = new OneBallView(convertView.getContext());
				tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
						aBlueColorResId);

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				holder.numbers.addView(tempBallView, lp);
			} else if (iGameType.equalsIgnoreCase("fc3d")) {
				holder.name.setText("¸£²Ê3D");
				holder.icon.setImageResource(R.drawable.fucai);
				int i1;
				String iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 3; i1++) {
					iShowNumber = iNumbers.substring(i1 * 2, i1 * 2 + 2);
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
							aRedColorResId);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					holder.numbers.addView(tempBallView, lp);
				}
			} else {
				holder.name.setText("ÆßÀÖ²Ê");
				holder.icon.setImageResource(R.drawable.qilecai);
				int i1;
				String iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 7; i1++) {
					iShowNumber = iNumbers.substring(i1 * 2, i1 * 2 + 2);
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
							aRedColorResId);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					holder.numbers.addView(tempBallView, lp);
				}
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	private class ViewHolder {
		// String type="BS";
		ImageView icon;
		TextView name;
		LinearLayout numbers;
		TextView date;
		TextView issue;
	}
}
