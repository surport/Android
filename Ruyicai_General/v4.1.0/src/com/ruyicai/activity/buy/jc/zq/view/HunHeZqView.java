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
import com.ruyicai.activity.buy.jc.oddsprize.JCPrizePermutationandCombination;
import com.ruyicai.code.jc.zq.FootHun;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;

/**
 * 混合投注
 * 
 * @author win
 * 
 */
public class HunHeZqView extends JcMainView {

	protected int CHECK_TEAM = 8;// 最多串几场比赛
	JcInfoAdapter adapter;
	FootHun footHunCode;

	public HunHeZqView(Context context, BetAndGiftPojo betAndGift,
			Handler handler, LinearLayout layout, String type,
			boolean isDanguan, List<String> checkTeam) {
		super(context, betAndGift, handler, layout, type, isDanguan, checkTeam);
		footHunCode = new FootHun(context);
	}

	public boolean isHunHe() {
		return true;
	}

	@Override
	public String getTitle() {
		return context.getString(R.string.jczq_hunhe_guoguan_title).toString();
	}

	public void setDifferValue(JSONObject jsonItem, Info itemInfo)
			throws JSONException {
		itemInfo.setLevel(jsonItem.getString("v1"));
		itemInfo.vStrs = new String[54];
		int rqspf_lenght = 3;
		int spf_lenght = 3;
		int bqc_lenght = 9;
		int zjq_lenght = 8;
		int bf_lenght = 31;
		for (int j = 0; j < bqc_lenght; j++) {
			itemInfo.getVStrs()[j + rqspf_lenght+spf_lenght] = jsonItem.getString("half_v"
					+ FootHun.bqcType[j + rqspf_lenght+spf_lenght]);
		}
		for (int j = 0; j < zjq_lenght; j++) {
			itemInfo.getVStrs()[j + rqspf_lenght + spf_lenght + bqc_lenght] = jsonItem
					.getString("goal_v" + j);
		}
		for (int j = 0; j < bf_lenght; j++) {
			itemInfo.getVStrs()[j + rqspf_lenght + spf_lenght + bqc_lenght + zjq_lenght] = jsonItem
					.getString("score_v"
							+ FootHun.bqcType[j + rqspf_lenght + spf_lenght + bqc_lenght
									+ zjq_lenght]);
		}
		initTitles(itemInfo);
	}

	private void initTitles(final Info info) {
		info.vStrs[0] = info.getWin();
		info.vStrs[1] = info.getLevel();
		info.vStrs[2] = info.getFail();
		
		info.vStrs[3] = info.getLetV3Win();
		info.vStrs[4] = info.getLetV1Level();
		info.vStrs[5] = info.getLetV0Fail();
	}

	/**
	 * 获取单关投注的中奖金额最大金额和最小金额的字符串
	 * 
	 * @return
	 */
	public String getDanPrizeString(int muti) {

		return JCPrizePermutationandCombination.getNewDanGuanPrize(
				getOddsArraysValue(), muti);
	}

	@Override
	public int getTeamNum() {
		return CHECK_TEAM;
	}

	@Override
	public void setTeamNum(int checkTeam) {
		CHECK_TEAM = checkTeam;
	}

	@Override
	public BaseAdapter getAdapter() {
		return adapter;
	}

	@Override
	public String getLotno() {
		return Constants.LOTNO_JCZQ_HUN;
	}

	@Override
	public String getTypeTitle() {
		return context.getString(R.string.jczq_dialog_sfc_guoguan_title)
				.toString();
	}

	/**
	 * 
	 * 获取注码
	 * 
	 */
	public String getCode(String key, List<Info> listInfo) {

		return footHunCode.getCode(key, listInfo);
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
				int first = 0;
				int second = 0;
				int third = 0;
				int fourth = 0;
				int fifth = 0;
				codeStr += PublicMethod.stringToHtml(info.getWeeks() + " " + info.getTeamId(), 
						Constants.JC_TOUZHU_TITLE_TEXT_COLOR) + " ";
				codeStr += (info.getHome() + " vs " + info.getAway());
				for (int j = 0; j < info.check.length; j++) {
					if (info.check[j].isChecked()) {
						/**add by yejc 20130730 start*/
						int position = info.check[j].getPosition(); 
						String title = info.check[j].getChcekTitle();
						if (position >= 0 && position <= 2) { // 胜平负
							first++;
							if (first == 1) {
								codeStr += "<br>胜平负：";
							}
						} else if (position >= 3 && position <= 5) { // 让球胜平负
							second++;
							if (second == 1) {
								codeStr += "<br>让球胜平负：";
							}
							title = "让" + title;
						} else if (position >= 6 && position <= 14) { // 半全场
							third++;
							if (third == 1) {
								codeStr += "<br>半全场：";
							}
						} else if (position >= 15 && position <= 22) { // 总进球
							fourth++;
							if (fourth == 1) {
								codeStr += "<br>总进球：";
							}
						} else if (position >= 23 && position <= 53) { // 比分
							fifth++;
							if (fifth == 1) {
								codeStr += "<br>比分：";
							}
						}
						codeStr += PublicMethod.stringToHtml(title, Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
						/**add by yejc 20130730 end*/
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
					holder.layout.addView(addView(list.get(i), position, i));
				}
//				for (Info info : list) {
//					holder.layout.addView(addView(info));
//				}
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
		private View addView(final Info info, final int position, final int index) {
			View convertView = mInflater.inflate(
					R.layout.buy_jc_main_listview_item_others, null);
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
			TextView btn = (Button) convertView
					.findViewById(R.id.jc_main_list_item_button);
			TextView analysis = (TextView) convertView
					.findViewById(R.id.game_analysis);
			final Button btnDan = (Button) convertView
					.findViewById(R.id.game_dan);
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

			if (!info.getBtnStr().equals("")) {
				btn.setText(info.getBtnStr());
			}
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.setHunheZQ(true); //add by yejc 20130704
						info.createDialog(FootHun.titleStrs, true,
								info.getHome() + " VS " + info.getAway());
						
						/**add by yejc 20130704 start*/
						mPosition = position;
						mIndex = index;
						View view = info.getViewType();
						TextView tv = (TextView)view.findViewById(R.id.lq_rqspf_dialog_textview);
						tv.setText("主"+info.getLetPoint());
						/**add by yejc 20130704 end*/
					}
					isNoDan(info, btnDan);
				}
			});
			if (isDanguan || isHunHe()) {
				btnDan.setVisibility(Button.GONE);
			} else {
				btnDan.setVisibility(Button.VISIBLE);
				btnDan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (info.isDan()) {
							info.setDan(false);
							btnDan.setBackgroundResource(R.drawable.jc_btn);
						} else if (info.onclikNum > 0 && isDanCheckTeam()
								&& isDanCheck()) {
							info.setDan(true);
							btnDan.setBackgroundResource(R.drawable.jc_btn_b);
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
//		if (isDanguan) {
//			return "J00002_0";
//		} else {
//			return "J00002_1";
//		}
		return "playtype"; //这里返回只要不为""并且不与其他玩法重复即可
	}

	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		return footHunCode.getOddsList(listInfo);
	}

	public List<double[]> getMinOdds(List<Info> listInfo) {
		return footHunCode.getMinOddsList(listInfo);
	}

	/**
	 * 最多可以设胆7场比赛
	 */
	public boolean isDanCheckTeam() {
		return false;
	}

}
