package com.ruyicai.activity.buy.jc.zq.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.zq.FootBQC;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;

/**
 * 半全场
 * 
 * @author Administrator
 * 
 */
public class BQCView extends JcMainView {
	private final int MAX_TEAM = 4;
	JcInfoAdapter adapter;
	FootBQC footbqcCode;

	public BQCView(Context context, BetAndGiftPojo betAndGift, Handler handler,
			LinearLayout layout, String type, boolean isdanguan,
			List<String> checkTeam) {
		super(context, betAndGift, handler, layout, type, isdanguan, checkTeam);
		footbqcCode = new FootBQC(context);
		
	}

	public void setDifferValue(JSONObject jsonItem, Info itemInfo)
			throws JSONException {
		itemInfo.vStrs = new String[9];
		for (int j = 0; j < 9; j++) {
			itemInfo.getVStrs()[j] = jsonItem.getString("half_v"
					+ FootBQC.bqcType[j]);
		}
	}

	@Override
	public int getTeamNum() {
		return MAX_TEAM;
	}

	@Override
	public String getLotno() {
		return Constants.LOTNO_JCZQ_BQC;
	}

	@Override
	public BaseAdapter getAdapter() {
		return adapter;
	}

	@Override
	public String getTitle() {
		if (isDanguan) {
			return context.getString(R.string.jczq_dxf_danguan_title)
					.toString();
		} else {
			return context.getString(R.string.jczq_dxf_guoguan_title)
					.toString();
		}
	}

	@Override
	public String getTypeTitle() {
		return context.getString(R.string.jczq_dialog_dxf_guoguan_title)
				.toString();
	}

	/**
	 * 
	 * 获取注码
	 * 
	 */
	public String getCode(String key, List<Info> listInfo) {
		return footbqcCode.getCode(key, listInfo);
	}

	/**
	 * 
	 * 获取投注框提示注码
	 * 
	 */
	public String getAlertCode(List<Info> listInfo) {
		String codeStr = "";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				codeStr += PublicMethod.stringToHtml(info.getWeeks() + " " + info.getTeamId(), 
						Constants.JC_TOUZHU_TITLE_TEXT_COLOR) + " ";
				codeStr += info.getHome() + " vs " + info.getAway() + "<br>半全场：";
				for (int j = 0; j < info.check.length; j++) {
					if (info.check[j].isChecked()) {
						codeStr += PublicMethod.stringToHtml(info.check[j].getChcekTitle(), Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
					}
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
		adapter.notifyDataSetChanged();
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

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ArrayList<Info> list = (ArrayList<Info>) mList.get(position);
			convertView = mInflater.inflate(
					R.layout.buy_jc_main_view_list_item, null);
			final ViewHolder holder = new ViewHolder();
			holder.btn = (Button) convertView
					.findViewById(R.id.buy_jc_main_view_list_item_btn);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.buy_jc_main_view_list_item_linearLayout);
			holder.btn.setBackgroundResource(R.drawable.buy_jc_item_btn_close);
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
					holder.layout.addView(addView(list.get(i), i));
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
		private View addView(final Info info, final int index) {
			View convertView = mInflater.inflate(
					R.layout.buy_jc_main_listview_item_others, null);
			View divider = (View)convertView.findViewById(R.id.jc_main_divider_up);
			if (index == 0) {
				divider.setVisibility(View.VISIBLE);
			} else {
				divider.setVisibility(View.GONE);
			}
			TextView gameName = (TextView) convertView
					.findViewById(R.id.game_name);
			TextView gameNum = (TextView) convertView.findViewById(R.id.game_num);
			TextView gameDate = (TextView) convertView
					.findViewById(R.id.game_date);
			TextView gameTime = (TextView) convertView.findViewById(R.id.game_time);
			final TextView homeTeam = (TextView) convertView
					.findViewById(R.id.home_team_name);
			final TextView guestTeam = (TextView) convertView
					.findViewById(R.id.guest_team_name);

			final Button btn = (Button) convertView
					.findViewById(R.id.jc_main_list_item_button);

			TextView analysis = (TextView) convertView
					.findViewById(R.id.game_analysis);
			final Button btnDan = (Button) convertView
					.findViewById(R.id.game_dan);
			final LinearLayout layout = (LinearLayout) convertView
					.findViewById(R.id.jc_play_detail_layout);

			gameName.setText(info.getTeam());
			String num = info.getTeamId();
			String date = PublicMethod.getTime(info.getTimeEnd());
			String time = PublicMethod.getEndTime(info.getTimeEnd()) + " "
					+ "(截)";
//			String date = getWeek(info.getWeeks()) + " " + info.getTeamId()
//					+ "\n" + PublicMethod.getEndTime(info.getTimeEnd()) + " "
//					+ "(截)";
			gameNum.setText(num);
			gameDate.setText(date);
			gameTime.setText(time);
			homeTeam.setText(info.getHome());

			gameName.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (context instanceof JcMainActivity) {
						JcMainActivity activity = (JcMainActivity) context;
						activity.createTeamDialog();
					}
				}
			});
			guestTeam.setText(info.getAway());

			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LinearLayout detailLayout = (LinearLayout) mFactory
							.inflate(R.layout.buy_jc_zq_bqc_layout, null);
					showLayout(layout, detailLayout, index, info, FootBQC.titleStrs, btn);
					isNoDan(info, btnDan);
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
			analysis.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					trunExplain(getEvent(Constants.JCFOOT, info),
							info.getHome(), info.getAway());
				}
			});

			/** add by pnegcx 20130624 start */
			if (info.isDan()) {
				btnDan.setBackgroundResource(R.drawable.jc_btn_b);
				btnDan.setTextColor(white);
			} else {
				btnDan.setBackgroundResource(android.R.color.transparent);
				btnDan.setTextColor(black);
			}
			/** add by pnegcx 20130624 end */

			return convertView;
		}

		// end

		class ViewHolder {
			Button btn;
			LinearLayout layout;

		}
	}

	@Override
	public String getPlayType() {
		if (isDanguan) {
			return "J00004_0";
		} else {
			return "J00004_1";
		}
	}

	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		return footbqcCode.getOddsList(listInfo);
	}

	/**
	 * 最多可以设胆7场比赛
	 */
	public boolean isDanCheckTeam() {
		int teamNum = initDanCheckedNum();
		if (teamNum < MAX_TEAM - 1) {
			return true;
		} else {
			Toast.makeText(context, "您最多可以选择" + (MAX_TEAM - 1) + "场比赛进行设胆！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

}
