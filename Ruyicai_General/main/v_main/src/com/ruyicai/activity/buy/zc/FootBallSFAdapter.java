package com.ruyicai.activity.buy.zc;

import java.util.List;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.zc.pojo.TeamInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FootBallSFAdapter extends FootBallBaseAdapter{
	
	public FootBallSFAdapter(Context context) {
		super(context);
	}
	
	public FootBallSFAdapter(Context context, List<TeamInfo> list) {
		super(context);
		this.mTeamList = list;
	}
	
	@Override
	public int getCount() {
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
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.buy_jc_main_listview_item_other, null);
			holder = new ViewHolder();
			holder.divider = (View) convertView
					.findViewById(R.id.jc_main_divider_up);
			holder.gameName = (TextView) convertView
					.findViewById(R.id.game_name);
			holder.gameDate = (TextView) convertView
					.findViewById(R.id.game_date);
			holder.homeTeam = (TextView) convertView
					.findViewById(R.id.home_team_name);
			holder.homeOdds = (TextView) convertView
					.findViewById(R.id.home_team_odds);
			holder.textVS = (TextView) convertView
					.findViewById(R.id.game_vs);
			holder.textOdds = (TextView) convertView
					.findViewById(R.id.game_vs_odds);
			holder.guestTeam = (TextView) convertView
					.findViewById(R.id.guest_team_name);
			holder.guestOdds = (TextView) convertView
					.findViewById(R.id.guest_team_odds);
			holder.analysis = (TextView) convertView
					.findViewById(R.id.game_analysis);
			holder.btnDan = (Button) convertView
					.findViewById(R.id.game_dan);
			holder.homeLayout = (LinearLayout) convertView
					.findViewById(R.id.home_layout);
			holder.vsLayout = (LinearLayout) convertView
					.findViewById(R.id.vs_layout);
			holder.guestLayout = (LinearLayout) convertView
					.findViewById(R.id.guest_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ViewHolder copyHolder = holder;
		if (position == 0) {
			copyHolder.divider.setVisibility(View.VISIBLE);
		} else {
			copyHolder.divider.setVisibility(View.GONE);
		}
		copyHolder.gameName.setText(mTeamList.get(position).getLeagueName());
		String tiem = mTeamList.get(position).getTeamId() + "\n"+
				PublicMethod.getEndTime(mTeamList.get(position).getDate()) 
				+ " (赛)";
		copyHolder.gameDate.setText(tiem);
		copyHolder.homeTeam.setText(mTeamList.get(position).getHomeTeam());
		copyHolder.homeOdds.setText(mTeamList.get(position).getHomeOdds());
		copyHolder.textVS.setText("VS");
		copyHolder.textOdds.setText(mTeamList.get(position).getVsOdds());
		copyHolder.guestTeam.setText(mTeamList.get(position).getGuestTeam());
		copyHolder.guestOdds.setText(mTeamList.get(position).getGuestOdds());
		final TeamInfo info = mTeamList.get(position);
		
		setViewStyle(copyHolder.homeLayout, copyHolder.homeTeam,
				copyHolder.homeOdds, info.isWin());
		
		setViewStyle(copyHolder.guestLayout, copyHolder.guestTeam,
				copyHolder.guestOdds, info.isFail());
		
		setViewStyle(copyHolder.vsLayout, copyHolder.textVS,
				copyHolder.textOdds, info.isLevel());
		
		if (!"5".equals(mIssueState)) {
			copyHolder.homeLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					info.setWin(!info.isWin());
					setViewStyle(copyHolder.homeLayout, copyHolder.homeTeam,
							copyHolder.homeOdds, info.isWin());
					setClickNum(info.isWin(), info);
				}
			});
			
			copyHolder.guestLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					info.setFail(!info.isFail());
					setViewStyle(copyHolder.guestLayout, copyHolder.guestTeam,
							copyHolder.guestOdds, info.isFail());
					setClickNum(info.isFail(), info);
				}
			});

			copyHolder.vsLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					info.setLevel(!info.isLevel());
					setViewStyle(copyHolder.vsLayout, copyHolder.textVS,
							copyHolder.textOdds, info.isLevel());
					setClickNum(info.isLevel(), info);
				}
			});
			copyHolder.btnDan.setVisibility(View.GONE);
			copyHolder.analysis.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					turnAnalysis(Constants.LOTNO_SFC, mTeamList.get(position).getTeamId());
				}
			});
		}
		
		return convertView;
	}
	
	protected void setClickNum(boolean flag, TeamInfo info) {
		if (flag) {
			info.onClickNum++;
		} else {
			info.onClickNum--;
		}
		setTeamNum(mTeamList);
	}
	
	@Override
	public int getZhuShu() {
		int iReturnValue = 0;
		for (int i = 0; i < mTeamList.size(); i++) {
			TeamInfo info = mTeamList.get(i);
			if (i != 0) {
				iReturnValue *= getClickNum(info);
			} else {
				iReturnValue = getClickNum(info);
			}
		}
		return iReturnValue;
	}
	
	
	public void clearSelected() {
		for (int i = 0; i < mTeamList.size(); i++) {
			mTeamList.get(i).reSet();
		}
		notifyDataSetChanged();
	}
	
	@Override
	public String getZhuMa() {
		StringBuffer zhuMaStr = new StringBuffer();
		for (int i = 0; i < mTeamList.size(); i++) {
			zhuMaStr.append(mTeamList.get(i).getSelectedTeamString());
			if (i < mTeamList.size() - 1) {
				zhuMaStr.append(",");
			}
		}
		return zhuMaStr.toString();
	}
	
	public boolean isTouZhu() {
		for (int i = 0; i < mTeamList.size(); i++) {
			TeamInfo info = mTeamList.get(i);
			if (getClickNum(info) == 0) {
				return true;
			}
		}
		return false;
	}
	
	private int getClickNum(TeamInfo info) {
		int clickNum = 0;
		if (info.isWin()) {
			clickNum++;
		}

		if (info.isLevel()) {
			clickNum++;
		}

		if (info.isFail()) {
			clickNum++;
		}
		return clickNum;
	}
	
	@Override
	protected int getTeamNum(List<TeamInfo> list) {
		int teamNum = 0;
		for (int i = 0; i < list.size(); i++) {
			TeamInfo info = list.get(i);
			if (getClickNum(info) > 0) {
				teamNum++;
			}
		}
		return teamNum;
	}
	
}
