package com.ruyicai.activity.buy.zc;

import java.util.List;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.zc.pojo.TeamInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FootBall4CJQAdapter extends FootBall6CBAdapter {

	private String titles[] = { "0", "1", "2", "3+", "0", "1", "2", "3+" };

	public FootBall4CJQAdapter(Context context) {
		super(context);
	}
	
	public FootBall4CJQAdapter(Context context, List<TeamInfo> list) {
		super(context);
		this.mTeamList = list;
	}

	@Override
	public int getCount() {
		if (mTeamList == null) {
			return 0;
		}
		return mTeamList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(
				R.layout.buy_jc_main_listview_item_others, null);
		View divider = (View) convertView.findViewById(R.id.jc_main_divider_up);
		TextView gameName = (TextView) convertView.findViewById(R.id.game_name);
		TextView gameNum = (TextView) convertView.findViewById(R.id.game_num);
		TextView gameDate = (TextView) convertView.findViewById(R.id.game_date);
		TextView gameTime = (TextView) convertView.findViewById(R.id.game_time);
		gameTime.setVisibility(View.VISIBLE);
		TextView homeTeam = (TextView) convertView
				.findViewById(R.id.home_team_name);
		TextView textVS = (TextView) convertView.findViewById(R.id.game_vs);
		TextView guestTeam = (TextView) convertView
				.findViewById(R.id.guest_team_name);
		TextView analysis = (TextView) convertView
				.findViewById(R.id.game_analysis);
		Button btnDan = (Button) convertView.findViewById(R.id.game_dan);
		final Button btnBet = (Button) convertView
				.findViewById(R.id.jc_main_list_item_button);
		final LinearLayout layout = (LinearLayout) convertView
				.findViewById(R.id.jc_play_detail_layout);
		if (position == 0) {
			divider.setVisibility(View.VISIBLE);
		} else {
			divider.setVisibility(View.GONE);
		}
		gameName.setText(mTeamList.get(position).getLeagueName());
		final TeamInfo info = mTeamList.get(position);
		String num = info.getTeamId();
		String date =PublicMethod.getTime(info.getDate());
		String time = PublicMethod.getEndTime(info.getDate()) + "(赛)";
//		String tiem = info.getTeamId() + "\n"
//				+ PublicMethod.getTime(info.getDate()) + "\n"
//				+ PublicMethod.getEndTime(info.getDate()) + " (赛)";
		gameNum.setText(num);
		gameDate.setText(date);
		gameTime.setText(time);
		homeTeam.setText(mTeamList.get(position).getHomeTeam());
		textVS.setText("VS");
		guestTeam.setText(mTeamList.get(position).getGuestTeam());
		btnDan.setVisibility(View.GONE);
		// 每次滑动判断是否打开，如果打开则显示
		if (info.buttonIsOpen) {
			showLayout(layout, position, info, btnBet);
		}
		Handler handler = new Handler();
		// 每次滑动更新投注显示
		handler.post(new Runnable() {

			@Override
			public void run() {
				String btnStr = "";
				int likNum = 0;
				if (info.check != null) {
					for (int i = 0; i < info.check.length; i++) {
						if (info.check[i].getChecked()) {
							btnStr += info.check[i].getChcekTitle() + "  ";
							if (isCheckIndex(info, 0, 3)
									&& isCheckIndex(info, 4, 7)) {
								likNum++;
							}
						}
					}
				}
				info.onClickNum = likNum;
				btnBet.setText(btnStr);
				int teamNum = getTeamNum(mTeamList);
				setTeamNum(teamNum/* mTeamList */);
			}
		});
		btnBet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				info.buttonIsOpen = !info.buttonIsOpen;
				showLayout(layout, position, info, btnBet);
			}
		});

		analysis.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				turnAnalysis(Constants.LOTNO_JQC, mTeamList.get(position)
						.getTeamId());
			}
		});
		return convertView;
	}

	@Override
	public int getZhuShu() {
		int value = 1;
		for (int i = 0; i < mTeamList.size(); i++) {
			int likNum = 0;
			TeamInfo info = mTeamList.get(i);
			if (info.check != null) {
				likNum = getLineSelectedNum(info, 0, 3)
						* getLineSelectedNum(info, 4, 7);
			}
			value *= likNum;
		}
		return value;
	}

	@Override
	public String getZhuMa() {
		StringBuffer zhuMaStr = new StringBuffer();
		for (int i = 0; i < mTeamList.size(); i++) {
			TeamInfo info = mTeamList.get(i);
			if (info.check != null) {
				zhuMaStr.append(getSelectedName(info, 0, 3));
				zhuMaStr.append(getSelectedName(info, 4, 7));
			}
		}
		String result = zhuMaStr.toString();
		result = result.substring(0, result.length() - 1);
		return result;
	}

	private String getSelectedName(TeamInfo info, int start, int end) {
		StringBuffer stringBuf = new StringBuffer();
		for (int i = 0; i < info.check.length; i++) {
			if (info.check[i].getChecked()) {
				if (i >= start && i <= end) {
					if (i == 3 || i == 7) {
						stringBuf.append(info.check[i].getChcekTitle().replace(
								"+", ""));
					} else {
						stringBuf.append(info.check[i].getChcekTitle());
					}
				}
			}
		}
		stringBuf.append(",");
		return stringBuf.toString();
	}

	@Override
	protected boolean isTouZhu() {
		for (int i = 0; i < mTeamList.size(); i++) {
			TeamInfo info = mTeamList.get(i);
			if (!isCheckIndex(info, 0, 3) || !isCheckIndex(info, 4, 7)) {
				return true;
			}
		}
		return false;
	}

	private void showLayout(LinearLayout layout, int index,
			final TeamInfo info, final Button btn) {
		LinearLayout detailLayout = (LinearLayout) mInflater.inflate(
				R.layout.buy_zc_4cjq_layout, null);
		if (layout.getChildCount() == 0) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (index == 0) {
				RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.FILL_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lParams.setMargins(0, PublicMethod.getPxInt(68.5f, mContext),
						0, 0);
				layout.setLayoutParams(lParams);
			}
			layout.addView(detailLayout, params);
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					String btnStr = "";
					int likNum = 0;
					if (info.check != null) {
						for (int i = 0; i < info.check.length; i++) {
							if (info.check[i].getChecked()) {
								btnStr += info.check[i].getChcekTitle() + "  ";
								if (isCheckIndex(info, 0, 3)
										&& isCheckIndex(info, 4, 7)) {
									likNum++;
								}
							}
						}
					}
					info.onClickNum = likNum;
					btn.setText(btnStr);
					int teamNum = getTeamNum(mTeamList);
					setTeamNum(teamNum/* mTeamList */);
				}
			};
			info.initView(titles, null, detailLayout, handler,
					Constants.LOTNO_JQC, mIssueState);
			layout.setVisibility(View.VISIBLE);
		} else {
			if (layout.getVisibility() == View.VISIBLE) {
				layout.setVisibility(View.GONE);
			} else {
				layout.setVisibility(View.VISIBLE);
			}
		}
	}

}
