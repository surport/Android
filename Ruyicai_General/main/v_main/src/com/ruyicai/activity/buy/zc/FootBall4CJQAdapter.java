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

public class FootBall4CJQAdapter extends FootBall6CBAdapter{
	
	private String titles[] = {"0", "1", "2", "3+", "0", "1", "2", "3+"};
	
	public FootBall4CJQAdapter(Context context, List<TeamInfo> list) {
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
					R.layout.buy_jc_main_listview_item_others, null);
			holder = new ViewHolder();
			holder.divider = (View) convertView
					.findViewById(R.id.jc_main_divider_up);
			holder.gameName = (TextView) convertView
					.findViewById(R.id.game_name);
			holder.gameDate = (TextView) convertView
					.findViewById(R.id.game_date);
			holder.homeTeam = (TextView) convertView
					.findViewById(R.id.home_team_name);
			holder.textVS = (TextView) convertView
					.findViewById(R.id.game_vs);
			holder.guestTeam = (TextView) convertView
					.findViewById(R.id.guest_team_name);
			holder.analysis = (TextView) convertView
					.findViewById(R.id.game_analysis);
			holder.btnDan = (Button) convertView
					.findViewById(R.id.game_dan);
			holder.btnBet =  (Button) convertView
					.findViewById(R.id.jc_main_list_item_button);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.jc_play_detail_layout);
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
		copyHolder.gameDate.setText(mTeamList.get(position).getDate());
		copyHolder.homeTeam.setText(mTeamList.get(position).getHomeTeam());
		copyHolder.textVS.setText("VS");
		copyHolder.guestTeam.setText(mTeamList.get(position).getGuestTeam());
		copyHolder.btnDan.setVisibility(View.GONE);
		final TeamInfo info = mTeamList.get(position);
		copyHolder.btnBet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showLayout(copyHolder.layout, position, info, copyHolder.btnBet);
			}
		});
		
		copyHolder.analysis.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				turnAnalysis(Constants.LOTNO_JQC, mTeamList.get(position).getTeamId());
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
				likNum = getLineSelectedNum(info, 0, 3) * getLineSelectedNum(info, 4, 7);
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
						stringBuf.append(info.check[i].getChcekTitle().replace("+", ""));
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
		return false;
	}
	
	private void showLayout (LinearLayout layout, int index, 
			final TeamInfo info, final Button btn) {
		LinearLayout detailLayout = (LinearLayout) mInflater
				.inflate(R.layout.buy_zc_4cjq_layout, null);
		if (layout.getChildCount() == 0) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
					(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			if (index == 0) {
				RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams
						(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				lParams.setMargins(0, PublicMethod.getPxInt(68.5f, mContext), 0, 0);
				layout.setLayoutParams(lParams);
			}
			layout.addView(detailLayout, params);
			Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					String btnStr = "";
					int likNum = 0;
					if (info.check != null) {
						for (int i = 0; i < info.check.length; i++) {
							if (info.check[i].getChecked()) {
								btnStr += info.check[i].getChcekTitle()
										+ "  ";
								if (isCheckIndex(info, 0, 3) && isCheckIndex(info, 4, 7)) {
									likNum++;
								}
							}
						}
					}
					info.onClickNum = likNum;
					btn.setText(btnStr);
					setTeamNum(mTeamList);
				}
			};
			info.initView(titles, null, detailLayout, handler, false, Constants.LOTNO_JQC);
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
