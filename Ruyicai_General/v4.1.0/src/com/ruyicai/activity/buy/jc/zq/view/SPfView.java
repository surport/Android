package com.ruyicai.activity.buy.jc.zq.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.code.jc.zq.FootSpf;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;

/**
 * 胜负类
 * 
 * @author Administrator
 * 
 */
public class SPfView extends JcMainView {
	private final int MAX_TEAM = 8;
	JcInfoAdapter adapter;
	FootSpf footSpfCode;
	public static boolean isRQSPF = false;

	public SPfView(Context context, BetAndGiftPojo betAndGift, Handler handler,
			LinearLayout layout, String type, boolean isdanguan,
			List<String> checkTeam) {
		super(context, betAndGift, handler, layout, type, isdanguan, checkTeam);
		footSpfCode = new FootSpf(context);
	}

	@Override
	public int getTeamNum() {
		return MAX_TEAM;
	}

	@Override
	public String getLotno() {
		return Constants.LOTNO_JCZQ;
	}

	@Override
	public BaseAdapter getAdapter() {
		return adapter;
	}

	@Override
	public String getTitle() {
		if (isDanguan) {
			return context.getString(R.string.jczq_sf_danguan_title).toString();
		} else {
			return context.getString(R.string.jczq_sf_guoguan_title).toString();
		}
	}

	@Override
	public String getTypeTitle() {
		return context.getString(R.string.jczq_dialog_sf_guoguan_title)
				.toString();
	}

	public void setDifferValue(JSONObject jsonItem, Info itemInfo)
			throws JSONException {
		itemInfo.setLevel(jsonItem.getString("v1"));
	}

	/**
	 * 
	 * 获取注码
	 * 
	 */
	public String getCode(String key, List<Info> listInfo) {
		return footSpfCode.getCode(key, listInfo);
	}

	/**
	 * 获取倍率
	 */
	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		return footSpfCode.getOddsList(listInfo);
	}

	/**
	 * 
	 * 获取注码
	 * 
	 */
	public String getAlertCode(List<Info> listInfo) {
		String codeStr = "";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				codeStr += PublicMethod.stringToHtml(info.getWeeks() + " " + info.getTeamId(), 
						Constants.JC_TOUZHU_TITLE_TEXT_COLOR) +  " ";
				codeStr += (info.getHome() + " vs " + info.getAway() + "<br>胜平负：");
				if (info.isWin()) {
					codeStr += PublicMethod.stringToHtml("胜", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				if (info.isLevel()) {
					codeStr += PublicMethod.stringToHtml("平", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				if (info.isFail()) {
					codeStr += PublicMethod.stringToHtml("负", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				if (info.isDan()) {
					codeStr += PublicMethod.stringToHtml("(胆)", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}

				codeStr += "<br>";
			}

		}
		return codeStr;
	}

	/**
	 * 初始化列表
	 */
	public void initListView(ListView listview, Context context,
			List<List> listInfo) {
		adapter = new JcInfoAdapter(context, listInfo);
		listview.setAdapter(adapter);
	}

	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<List> mList;

		public JcInfoAdapter(Context context, List<List> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;

		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		int index;

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			index = position;
			final ArrayList<Info> list = (ArrayList<Info>) mList.get(position);
			convertView = mInflater.inflate(
					R.layout.buy_jc_main_view_list_item, null);
			final ViewHolder holder = new ViewHolder();
			holder.btn = (Button) convertView
					.findViewById(R.id.buy_jc_main_view_list_item_btn);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.buy_jc_main_view_list_item_linearLayout);
			holder.btn.setBackgroundResource(R.drawable.buy_jc_item_btn_close);
			holder.itemLayout = (LinearLayout) convertView
					.findViewById(R.id.linearLayout1);
			if (list.size() == 0) {
				holder.btn.setVisibility(Button.GONE);
			} else {
				isOpen(list, holder);
				holder.btn.setText(list.get(0).getTime() + "  " + list.size()
						+ context.getString(R.string.jc_main_btn_text));
				holder.btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						list.get(0).isOpen = !list.get(0).isOpen;
						isOpen(list, holder);
					}
				});
				for (int i = 0; i < list.size(); i++) {
					holder.layout.addView(addLayout(list.get(i), i));
				}
			}
			return convertView;
		}

		private void isOpen(final ArrayList<Info> list, final ViewHolder holder) {
			if (list.get(0).isOpen) {
				holder.layout.setVisibility(LinearLayout.VISIBLE);
				holder.btn.setBackgroundResource(R.drawable.buy_jc_item_btn_open);
			} else {
				holder.layout.setVisibility(LinearLayout.GONE);
				holder.btn.setBackgroundResource(R.drawable.buy_jc_item_btn_close);
			}
		}

		// add by yejc 20130402
		private View addLayout(final Info info, int index) {
			View convertView = mInflater.inflate(
					R.layout.buy_jc_main_listview_item_other, null);
			View divider = (View)convertView.findViewById(R.id.jc_main_divider_up);
			if (index == 0) {
				divider.setVisibility(View.VISIBLE);
			} else {
				divider.setVisibility(View.GONE);
			}
			TextView gameName = (TextView) convertView
					.findViewById(R.id.game_name);
			TextView gateNum = (TextView) convertView.findViewById(R.id.game_num);
			
			TextView gameDate = (TextView) convertView
					.findViewById(R.id.game_date);
			TextView gameTime = (TextView) convertView.findViewById(R.id.game_time);
			final TextView homeTeam = (TextView) convertView
					.findViewById(R.id.home_team_name);
			final TextView homeOdds = (TextView) convertView
					.findViewById(R.id.home_team_odds);
			final TextView textVS = (TextView) convertView
					.findViewById(R.id.game_vs);
			final TextView textOdds = (TextView) convertView
					.findViewById(R.id.game_vs_odds);
			final TextView guestTeam = (TextView) convertView
					.findViewById(R.id.guest_team_name);
			final TextView guestOdds = (TextView) convertView
					.findViewById(R.id.guest_team_odds);
			TextView analysis = (TextView) convertView
					.findViewById(R.id.game_analysis);
			final Button btnDan = (Button) convertView
					.findViewById(R.id.game_dan);
			final LinearLayout homeLayout = (LinearLayout) convertView
					.findViewById(R.id.home_layout);
			final LinearLayout vsLayout = (LinearLayout) convertView
					.findViewById(R.id.vs_layout);
			final LinearLayout guestLayout = (LinearLayout) convertView
					.findViewById(R.id.guest_layout);
			
			gameName.setText(info.getTeam());
			String num = info.getTeamId();
			String date = PublicMethod.getTime(info.getTimeEnd());
			String time = PublicMethod.getEndTime(info.getTimeEnd()) + " "
					+ "(截)";
//			String date = getWeek(info.getWeeks()) + " " + info.getTeamId()
//					+ "\n" + PublicMethod.getEndTime(info.getTimeEnd()) + " "
//					+ "(截)";
			gateNum.setText(num);
			gameDate.setText(date);
			gameTime.setText(time);
			homeTeam.setText(info.getHome());

			if (isRQSPF) {
				if (!"".equals(info.getLetPoint())) {
					textVS.setText(info.getLetPoint());
				} else {
					textVS.setText("0");
				}
				homeOdds.setText(info.getLetV3Win());
				textOdds.setText(info.getLetV1Level());
				guestOdds.setText(info.getLetV0Fail());
			} else {
//				textVS.setText("0");
				textVS.setText("VS");
				homeOdds.setText(info.getWin());
				textOdds.setText(info.getLevel());
				guestOdds.setText(info.getFail());
			}
			guestTeam.setText(info.getAway());

			setViewStyle(homeLayout, homeTeam, homeOdds, info.isWin());
			
			setViewStyle(guestLayout, guestTeam, guestOdds, info.isFail());
			
			setViewStyle(vsLayout, textVS, textOdds, info.isLevel());

			/** add by pnegcx 20130624 start */
			if (info.isDan()) {
				btnDan.setBackgroundResource(R.drawable.jc_btn_b);
				btnDan.setTextColor(white);
			} else {
				btnDan.setBackgroundResource(android.R.color.transparent);
				btnDan.setTextColor(black);
			}
			/** add by pnegcx 20130624 end */

			homeLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.setWin(!info.isWin());
						if (info.isWin()) {
							info.onclikNum++;
						} else {
							info.onclikNum--;
						}
						setViewStyle(homeLayout, homeTeam, homeOdds, info.isWin());
						isNoDan(info, btnDan);
						setTeamNum();
					}
				}
			});
			vsLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.setLevel(!info.isLevel());
						if (info.isLevel()) {
							info.onclikNum++;
						} else {
							info.onclikNum--;
						}
						setViewStyle(vsLayout, textVS, textOdds, info.isLevel());
						isNoDan(info, btnDan);
						setTeamNum();
					}
				}
			});
			guestLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.setFail(!info.isFail());
						if (info.isFail()) {
							info.onclikNum++;
						} else {
							info.onclikNum--;
						}
						setViewStyle(guestLayout, guestTeam, guestOdds, info.isFail());
						isNoDan(info, btnDan);
						setTeamNum();
					}
				}
			});
			if (isDanguan) {
				btnDan.setVisibility(Button.GONE);
			} else {
				btnDan.setVisibility(Button.VISIBLE);
				btnDan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (info.isDan()) {
							info.setDan(false);
							btnDan.setBackgroundResource(android.R.color.transparent);
							btnDan.setTextColor(black);
						} else if (info.onclikNum > 0 && isDanCheckTeam()
								&& isDanCheck()) {
							info.setDan(true);
							btnDan.setBackgroundResource(R.drawable.jc_btn_b);
							btnDan.setTextColor(white);
						}
					}
				});
			}
			analysis.setVisibility(Button.VISIBLE);
			analysis.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					trunExplain(getEvent(Constants.JCFOOT, info),
							info.getHome(), info.getAway());
				}
			});

			return convertView;
		}

		// end

		class ViewHolder {
			TextView time;
			TextView team;
			TextView home;
			TextView away;
			TextView score;
			TextView timeEnd;
			Button btn1;
			Button btn3;
			Button btn;
			LinearLayout layout;
			LinearLayout itemLayout;

		}
	}

	@Override
	public String getPlayType() {
		if (isDanguan) {
			return "J00001_0";
		} else {
			return "J00001_1";
		}
	}
	
	/**add by yejc 20130823 start*/
	private void setViewStyle(LinearLayout teamLayout, TextView team, TextView odds, boolean isChecked) {
		if (isChecked) {
			teamLayout.setBackgroundResource(R.drawable.team_name_bj_yellow);
			team.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
			team.setTextColor(white);
			odds.setTextColor(white);
		} else {
			teamLayout.setBackgroundResource(android.R.color.transparent);
			team.setBackgroundResource(android.R.color.transparent);
			team.setTextColor(black);
			odds.setTextColor(oddsColor);
		}
	}
	/**add by yejc 20130823 end*/

}
