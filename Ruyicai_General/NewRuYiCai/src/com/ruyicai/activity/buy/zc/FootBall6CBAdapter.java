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

public class FootBall6CBAdapter extends FootBallBaseAdapter {

	private String titles[] = { "胜", "平", "负", "胜", "平", "负" };

	public FootBall6CBAdapter(Context context) {
		super(context);
	}

	public FootBall6CBAdapter(Context context, List<TeamInfo> list) {
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
		ViewHolder holder;

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
//		gameDate.setText(tiem);
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
							if (isCheckIndex(info, 0, 2)
									&& isCheckIndex(info, 3, 5)) {
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
				turnAnalysis(Constants.LOTNO_LCB, mTeamList.get(position)
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
				likNum = getLineSelectedNum(info, 0, 2)
						* getLineSelectedNum(info, 3, 5);
			}
			value *= likNum;
		}
		return value;
	}

	public void clearSelected() {
		for (int i = 0; i < mTeamList.size(); i++) {
			TeamInfo info = mTeamList.get(i);
			if (info.check != null) {
				for (int j = 0; j < info.check.length; j++) {
					if (info.check[j].getChecked()) {
						info.check[j].setChecked(false);
					}
				}
				info.onClickNum = 0;
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public String getZhuMa() {
		StringBuffer zhuMaStr = new StringBuffer();
		for (int i = 0; i < mTeamList.size(); i++) {
			TeamInfo info = mTeamList.get(i);
			if (info.check != null) {
				zhuMaStr.append(getSelectedName(info, 0, 2));
				zhuMaStr.append(getSelectedName(info, 3, 5));
			}
		}
		String result = zhuMaStr.toString();
		result = result.substring(0, result.length() - 1);
		return result;
	}

	protected int getLineSelectedNum(TeamInfo info, int start, int end) {
		int clickNum = 0;
		for (int i = start; i <= end; i++) {
			if (info.check[i].getChecked()) {
				clickNum++;
			}
		}
		return clickNum;
	}

	private String getSelectedName(TeamInfo info, int start, int end) {
		StringBuffer stringBuf = new StringBuffer();
		for (int i = 0; i < info.check.length; i++) {
			if (info.check[i].getChecked()) {
				if (i >= start && i <= end) {
					switch (i) {
					case 0:
					case 3:
						stringBuf.append("3");
						break;

					case 1:
					case 4:
						stringBuf.append("1");
						break;

					case 2:
					case 5:
						stringBuf.append("0");
						break;
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
			if (!isCheckIndex(info, 0, 2) || !isCheckIndex(info, 3, 5)) {
				return true;
			}
		}
		return false;
	}

	protected boolean isCheckIndex(TeamInfo info, int start, int end) {
		boolean isIndex = false;
		if (info.check != null) {
			for (int i = 0; i < info.check.length; i++) {
				if (info.check[i].getChecked()) {
					if (i >= start && i <= end) {
						isIndex = true;
						break;
					}
				}
			}
		}
		return isIndex;
	}

	@Override
	protected int getTeamNum(List<TeamInfo> list) {
		int teamNum = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).onClickNum > 0) {
				teamNum++;
			}
		}
		return teamNum;
	}

	private void showLayout(LinearLayout layout, int index,
			final TeamInfo info, final Button btn) {
		// 如果为空，即重新添加列表项的时候，则添加
		if (layout.getChildCount() == 0) {
			LinearLayout detailLayout = (LinearLayout) mInflater.inflate(
					R.layout.buy_zc_lcb_layout, null);
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
								if (isCheckIndex(info, 0, 2)
										&& isCheckIndex(info, 3, 5)) {
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
			String odds[] = new String[6];
			odds[0] = info.getHomeOdds();
			odds[1] = info.getVsOdds();
			odds[2] = info.getGuestOdds();
			odds[3] = info.getHomeOdds();
			odds[4] = info.getVsOdds();
			odds[5] = info.getGuestOdds();
			info.initView(titles, odds, detailLayout, handler,
					Constants.LOTNO_LCB, mIssueState);
			layout.setVisibility(View.VISIBLE);
		} else {
			// 如果没有重新添加列表项
			if (layout.getVisibility() == View.VISIBLE) {
				layout.setVisibility(View.GONE);
			} else {
				layout.setVisibility(View.VISIBLE);
			}
		}
	}
}
