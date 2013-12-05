package com.ruyicai.activity.buy.jc.lq.view;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
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
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.activity.buy.jc.explain.lq.JcLqExplainActivity;
import com.ruyicai.code.jc.lq.BasketSF;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;

/**
 * 胜负类
 * 
 * @author Administrator
 * 
 */
public class SfView extends JcMainView {
	private final int MAX_TEAM = 8;
	protected BasketSF basketSf;
	protected JcInfoAdapter adapter;
	protected static final int B_SF = 0;
	public static final int B_RF = 1;
	public static final int B_DX = 2;

	public SfView(Context context, BetAndGiftPojo betAndGift, Handler handler,
			LinearLayout layout, String type, boolean isdanguan,
			List<String> checkTeam) {
		super(context, betAndGift, handler, layout, type, isdanguan, checkTeam);
		basketSf = new BasketSF(context);
	}

	@Override
	public String getLotno() {
		return Constants.LOTNO_JCLQ;
	}

	@Override
	public BaseAdapter getAdapter() {
		return adapter;
	}

	@Override
	public String getTitle() {
		if (isDanguan) {
			return context.getString(R.string.jclq_sf_danguan_title).toString();
		} else {
			return context.getString(R.string.jclq_sf_guoguan_title).toString();
		}
	}

	@Override
	public String getTypeTitle() {
		return context.getString(R.string.jclq_dialog_sf_guoguan_title)
				.toString();
	}

	@Override
	public String getCode(String key, List<Info> listInfo) {
		return basketSf.getCode(key, listInfo);
	}

	@Override
	public String getAlertCode(List<Info> listInfo) {
		String codeStr = "";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				codeStr += PublicMethod.stringToHtml(info.getWeeks() + " " + info.getTeamId(), 
						Constants.JC_TOUZHU_TITLE_TEXT_COLOR) + "  ";
				if (Constants.LOTNO_JCLQ_RF.equals(getLotno())) {
					if (!"".equals(info.getLetPoint())) {
						codeStr += info.getAway() + " (" +info.getLetPoint() + ") " + info.getHome() + "(主)"+"<br>让分胜负：";
					} else {
						codeStr += info.getAway() + " vs " + info.getHome() + "(主)"+"<br>胜负：";
					}
				} else {
					codeStr += info.getAway() + " vs " + info.getHome() + "(主)"+"<br>胜负：";
				}
				
				if (info.isWin()) {
					codeStr += PublicMethod.stringToHtml("主胜", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				if (info.isFail()) {
					codeStr += PublicMethod.stringToHtml("主负", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				if (info.isDan()) {
					codeStr += PublicMethod.stringToHtml("(胆)", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				codeStr += "<br>";
			}

		}
		return codeStr;
	}

	@Override
	public int getTeamNum() {
		return MAX_TEAM;
	}

	/**
	 * 初始化列表
	 */
	public void initListView(ListView listview, Context context,
			List<List> listInfo) {
		adapter = new JcInfoAdapter(context, listInfo, B_SF);
		listview.setAdapter(adapter);
	}

	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<List> mList;
		private int type;

		public JcInfoAdapter(Context context, List<List> list, int type) {
			mInflater = LayoutInflater.from(context);
			mList = list;
			this.type = type;
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
					holder.layout.addView(addView(list.get(i), type, i));
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
		private View addView(final Info info, final int type, int index) {
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
			TextView gameNum = (TextView) convertView.findViewById(R.id.game_num);
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
			String time = PublicMethod.getEndTime(info.getTimeEnd()) + " " + "(截)";
//			String date = getWeek(info.getWeeks()) + " " + info.getTeamId()
//					+ "\n" + PublicMethod.getEndTime(info.getTimeEnd()) + " " + "(截)";
			gameNum.setText(num);
			gameDate.setText(date);
			gameTime.setText(time);
			homeTeam.setText(info.getAway() + "(客)");

			final boolean isB_DX = (type == B_DX);
			if(isB_DX) {
				homeOdds.setText("大" + info.getBig());
				guestOdds.setText("小" + info.getSmall());
				textOdds.setText(info.getBasePoint());
				textOdds.setTextColor(red);
				textVS.setVisibility(View.GONE);
			} else if(type == B_RF) {
				guestOdds.setText(info.getLetWin());
				homeOdds.setText(info.getLetFail());
				String letPoint = info.getLetPoint();
				if (letPoint != null && letPoint.length()>0) {
					textOdds.setText(letPoint);
					setOddsColor(textOdds);
				}
				textVS.setVisibility(View.GONE);
				/**add by yejc 20130416 start */
			} else {
				guestOdds.setText(info.getWin());
				homeOdds.setText(info.getFail());
				textVS.setBackgroundResource(android.R.color.transparent);
				textVS.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT));
				textOdds.setVisibility(View.GONE);
			}
			/**add by yejc 20130416 end*/
			guestTeam.setText(info.getHome() + "(主)");

			if (info.isFail()) {
				setBackground(guestLayout, guestTeam, guestOdds, info.isFail(), type);
			} else {
				setBackground(guestLayout, guestTeam, guestOdds, false, type);
			}
			if (info.isWin()) {
				setBackground(homeLayout, homeTeam, homeOdds, info.isWin(), type);
			} else {
				setBackground(homeLayout, homeTeam, homeOdds, false, type);
			}
			vsLayout.setBackgroundResource(android.R.color.transparent);
			/* Modify the display order*/
			guestLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.setWin(!info.isWin());
						if (info.isWin()) {
							info.onclikNum++;
							setBackground(guestLayout, guestTeam, guestOdds, info.isWin(), type);
						} else {
							info.onclikNum--;
							setBackground(guestLayout, guestTeam, guestOdds, false, type);
						}
						isNoDan(info, btnDan);
						setTeamNum();
					}
				}
			});
			homeLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.setFail(!info.isFail());
						if (info.isFail()) {
							info.onclikNum++;
							setBackground(homeLayout, homeTeam, homeOdds, info.isFail(), type);
						} else {
							info.onclikNum--;
							setBackground(homeLayout, homeTeam, homeOdds, false, type);
						}
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
					trunExplain(getEvent(Constants.JCBASKET, info));
				}
			});

			return convertView;
		}
		
		
		private void setBackground(LinearLayout layout, TextView team,
				TextView odds, boolean flag, int type) {
			if (flag) {
				layout.setBackgroundResource(R.drawable.team_name_bj_yellow);
				team.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
				team.setTextColor(white);
				odds.setTextColor(white);
			} else {
				layout.setBackgroundResource(android.R.color.transparent);
				team.setBackgroundResource(android.R.color.transparent);
				team.setTextColor(black);
				if (type == B_DX) {
					odds.setTextColor(red);
				} else if (type == B_RF){
					setOddsColor(odds);
				} else {
					odds.setTextColor(oddsColor);
				}
				
			}
		}
		// end
		
		class ViewHolder {
			Button btn;
			LinearLayout layout;
		}
	}

	/**
	 * 跳转到分析界面
	 */
	public void trunExplain(String event) {
		JcLqExplainActivity.Lq_TYPE = JcLqExplainActivity.LQ_SF;
		Intent intent = new Intent(context, JcLqExplainActivity.class);
		intent.putExtra("event", event);
		context.startActivity(intent);
	}

	@Override
	public String getPlayType() {
		if (isDanguan) {
			return "J00005_0";
		} else {
			return "J00005_1";
		}
	}

	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		return basketSf.getOddsList(listInfo, B_SF);
	}
	
	private void setOddsColor(TextView tv) {
		String text = tv.getText().toString();
		if (text != null && text.length() != 0) {
			if ("-".equals(text.substring(0, 1))) {
				tv.setTextColor(red);
			} else if ("+".equals(text.substring(0, 1))) {
				tv.setTextColor(green);
			} else {
				tv.setTextColor(oddsColor);
			}
		}
	}

}
