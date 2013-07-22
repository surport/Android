package com.ruyicai.activity.buy.jc.lq.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.activity.buy.jc.explain.lq.JcLqExplainActivity;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.code.jc.lq.BasketSF;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.QueryJcInfoInterface;
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
		// TODO Auto-generated method stub
		return Constants.LOTNO_JCLQ;
	}

	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		return adapter;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		if (isDanguan) {
			return context.getString(R.string.jclq_sf_danguan_title).toString();
		} else {
			return context.getString(R.string.jclq_sf_guoguan_title).toString();
		}
	}

	@Override
	public String getTypeTitle() {
		// TODO Auto-generated method stub
		return context.getString(R.string.jclq_dialog_sf_guoguan_title)
				.toString();
	}

	@Override
	public String getCode(String key, List<Info> listInfo) {
		// TODO Auto-generated method stub
		return basketSf.getCode(key, listInfo);
	}

	@Override
	public String getAlertCode(List<Info> listInfo) {
		String codeStr = "";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				codeStr += info.getAway() + " vs " + info.getHome() + "(主)"+"：";
				if (info.isWin()) {
					codeStr += "主胜";
				}
				if (info.isFail()) {
					codeStr += "主负";
				}
				if (info.isDan()) {
					codeStr += "(胆)";
				}
				codeStr += "\n\n";
			}

		}
		return codeStr;
	}

	@Override
	public int getTeamNum() {
		// TODO Auto-generated method stub
		return MAX_TEAM;
	}

	/**
	 * 初始化列表
	 */
	public void initListView(ListView listview, Context context,
			List<List> listInfo) {
		// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		int index;

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			index = position;
			final ArrayList<Info> list = (ArrayList<Info>) mList.get(position);
			convertView = mInflater.inflate(
					R.layout.buy_jc_main_view_list_item, null);
			final ViewHolder holder = new ViewHolder();
			holder.btn = (Button) convertView
					.findViewById(R.id.buy_jc_main_view_list_item_btn);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.buy_jc_main_view_list_item_linearLayout);
			holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_close);
			if (list.size() == 0) {
				holder.btn.setVisibility(Button.GONE);
			} else {
				isOpen(list, holder);
				holder.btn.setText(list.get(0).getTime() + "  " + list.size()
						+ context.getString(R.string.jc_main_btn_text));
				holder.btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						list.get(0).isOpen = !list.get(0).isOpen;
						isOpen(list, holder);
					}
				});
				for (Info info : list) {
					holder.layout.addView(addView(info, type)/*addLayout(info)*/);
				}
			}
			return convertView;

		}

		private void isOpen(final ArrayList<Info> list, final ViewHolder holder) {
			if (list.get(0).isOpen) {
				holder.layout.setVisibility(LinearLayout.VISIBLE);
				holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_open);
			} else {
				holder.layout.setVisibility(LinearLayout.GONE);
				holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_close);
			}
		}
		
		
		// add by yejc 20130402
		private View addView(final Info info, int type) {
			View convertView = mInflater.inflate(
					R.layout.buy_jc_main_listview_item_other, null);
			TextView gameName = (TextView) convertView
					.findViewById(R.id.game_name);
			TextView gameDate = (TextView) convertView
					.findViewById(R.id.game_date);

			final TextView homeTeam = (TextView) convertView
					.findViewById(R.id.home_team_name);
//			homeTeam.getPaint().setFakeBoldText(true);
			final TextView homeOdds = (TextView) convertView
					.findViewById(R.id.home_team_odds);
			final TextView textVS = (TextView) convertView
					.findViewById(R.id.game_vs);
			final TextView textOdds = (TextView) convertView
					.findViewById(R.id.game_vs_odds);
			final TextView guestTeam = (TextView) convertView
					.findViewById(R.id.guest_team_name);
//			guestTeam.getPaint().setFakeBoldText(true);
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
			String date = getWeek(info.getWeeks()) + " " + info.getTeamId()
					+ "\n" + PublicMethod.getEndTime(info.getTimeEnd()) + " " + "(截)";
			gameDate.setText(date);
			homeTeam.setText(info.getAway() + "(客)");

			final boolean isB_DX = (type == B_DX);
			if(isB_DX) {
				homeOdds.setText("大" + info.getBig());
				guestOdds.setText("小" + info.getSmall());
				textOdds.setText(info.getBasePoint());
				textOdds.setTextColor(context.getResources().getColor(R.color.red));
			} else if(type == B_RF) {
				guestOdds.setText(info.getLetWin());
				homeOdds.setText(info.getLetFail());
				String letPoint = info.getLetPoint();
				if (letPoint != null && letPoint.length()>0) {
					textOdds.setText(letPoint);
					if ("-".equals(letPoint.subSequence(0, 1))) {
						textOdds.setTextColor(context.getResources().getColor(R.color.red));
					} else if ("+".equals(letPoint.subSequence(0, 1))) {
						textOdds.setTextColor(context.getResources().getColor(R.color.gree_black));
					} else {
						textOdds.setTextColor(context.getResources().getColor(android.R.color.black));
					}
				}
				/**add by yejc 20130416 start */
			} else {
				guestOdds.setText(info.getWin());
				homeOdds.setText(info.getFail());
			}
			/**add by yejc 20130416 end*/

			guestTeam.setText(info.getHome() + "(主)");

			gameName.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (context instanceof JcMainActivity) {
						JcMainActivity activity = (JcMainActivity) context;
						activity.createTeamDialog();
					}
				}
			});

			if (info.isWin()) {
				setBackground(guestLayout, guestTeam, guestOdds, info.isWin(), isB_DX);
//				guestOdds.setBackgroundResource(R.drawable.team_name_bj_yellow);
//				guestLayout.setBackgroundResource(R.drawable.team_name_bj_yellow);
//				guestTeam.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
			} else {
				setBackground(guestLayout, guestTeam, guestOdds, false, isB_DX);
//				guestLayout.setBackgroundResource(R.drawable.team_name_bj);
//				guestTeam.setBackgroundResource(R.drawable.team_name_bj_top);
			}
			if (info.isFail()) {
				setBackground(homeLayout, homeTeam, homeOdds, info.isFail(), isB_DX);
//				homeOdds.setBackgroundResource(R.drawable.team_name_bj_yellow);
//				homeLayout.setBackgroundResource(R.drawable.team_name_bj_yellow);
//				homeTeam.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
			} else {
				setBackground(homeLayout, homeTeam, homeOdds, false, isB_DX);
//				homeLayout.setBackgroundResource(R.drawable.team_name_bj);
//				homeTeam.setBackgroundResource(R.drawable.team_name_bj_top);
			}
			vsLayout.setBackgroundResource(android.R.color.transparent);
			textVS.setBackgroundResource(android.R.color.transparent);
			/* Modify the display order*/
			guestLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.setWin(!info.isWin());
						if (info.isWin()) {
							info.onclikNum++;
							setBackground(guestLayout, guestTeam, guestOdds, info.isWin(), isB_DX);
//							homeOdds.setBackgroundResource(R.drawable.team_name_bj_yellow);
//							homeLayout.setBackgroundResource(R.drawable.team_name_bj_yellow);
//							homeTeam.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
						} else {
							info.onclikNum--;
							setBackground(guestLayout, guestTeam, guestOdds, false, isB_DX);
//							homeOdds.setBackgroundResource(android.R.color.transparent);
//							homeLayout.setBackgroundResource(R.drawable.team_name_bj);
//							homeTeam.setBackgroundResource(R.drawable.team_name_bj_top);
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
							setBackground(homeLayout, homeTeam, homeOdds, info.isFail(), isB_DX);
//							guestOdds.setBackgroundResource(R.drawable.team_name_bj_yellow);
//							guestLayout.setBackgroundResource(R.drawable.team_name_bj_yellow);
//							guestTeam.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
						} else {
							info.onclikNum--;
							setBackground(homeLayout, homeTeam, homeOdds, false, isB_DX);
//							guestOdds.setBackgroundResource(android.R.color.transparent);
//							guestLayout.setBackgroundResource(R.drawable.team_name_bj);
//							guestTeam.setBackgroundResource(R.drawable.team_name_bj_top);
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
							btnDan.setBackgroundResource(R.drawable.jc_btn);
						} else if (info.onclikNum > 0 && isDanCheckTeam()
								&& isDanCheck()) {
							info.setDan(true);
							btnDan.setBackgroundResource(R.drawable.jc_btn_b);
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
				TextView odds, boolean flag, boolean isDX) {
			if (isDX) {
				if (flag) {
					odds.setBackgroundResource(R.drawable.jc_lq_dx_bj); 
				} else {
					odds.setBackgroundResource(android.R.color.transparent);
				}
			} else {
				if (flag) {
					layout.setBackgroundResource(R.drawable.team_name_bj_yellow);
					team.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
				} else {
					layout.setBackgroundResource(R.drawable.team_name_bj);
					team.setBackgroundResource(R.drawable.team_name_bj_top);
				}
			}
		}

		// end
		
		
		

		private View addLayout(final Info info) {
			View convertView;
			convertView = mInflater.inflate(R.layout.buy_lq_main_listview_item,
					null);
			final ViewHolder holder = new ViewHolder();
			TextView time = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_time);
			TextView team = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team);
			TextView home = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team_name1);
			TextView away = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team_name2);
			TextView score = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team_score);
			TextView timeEnd = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_time_end);
			final Button btnFail = (Button) convertView
					.findViewById(R.id.jc_main_list_item_button1);
			final Button btnWin = (Button) convertView
					.findViewById(R.id.jc_main_list_item_button3);
			final Button btnDan = (Button) convertView
					.findViewById(R.id.jc_main_list_item_button_dan);
			final Button btnXi = (Button) convertView
					.findViewById(R.id.buy_jc_main_list_item_btn_xi);

			time.setText(info.getTime() + "  "
					+ context.getString(R.string.jc_main_team_id_title)
					+ info.getTeamId());
			team.setText(info.getTeam());
			home.setText(info.getAway() + "(客)");
			away.setText(info.getHome() + "(主)");
			timeEnd.setText(info.getTimeEnd());
			if (type == B_RF) {
				score.setText(info.getLetPoint());
				btnFail.setText("主负" + info.getLetFail());
				btnWin.setText("主胜" + info.getLetWin());
			} else if (type == B_DX) {
				btnFail.setText("大" + info.getBig());
				btnWin.setText("小" + info.getSmall());
				score.setText(info.getBasePoint());
			} else {
				btnFail.setText("主负" + info.getFail());
				btnWin.setText("主胜" + info.getWin());
			}
			if (info.isFail()) {
				btnFail.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				btnFail.setBackgroundResource(R.drawable.jc_btn);
			}
			if (info.isWin()) {
				btnWin.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				btnWin.setBackgroundResource(R.drawable.jc_btn);
			}
			btnFail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.setFail(!info.isFail());
						if (info.isFail()) {
							info.onclikNum++;
							btnFail.setBackgroundResource(R.drawable.jc_btn_b);
						} else {
							info.onclikNum--;
							btnFail.setBackgroundResource(R.drawable.jc_btn);
						}
						isNoDan(info, btnDan);
						setTeamNum();
					}
				}
			});
			btnWin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (info.onclikNum > 0 || isCheckTeam()) {
						info.setWin(!info.isWin());
						if (info.isWin()) {
							info.onclikNum++;
							btnWin.setBackgroundResource(R.drawable.jc_btn_b);
						} else {
							info.onclikNum--;
							btnWin.setBackgroundResource(R.drawable.jc_btn);
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
						// TODO Auto-generated method stub
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
			btnXi.setVisibility(Button.VISIBLE);
			btnXi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					trunExplain(getEvent(Constants.JCBASKET, info));
				}
			});
			return convertView;
		}

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
		// TODO Auto-generated method stub
		if (isDanguan) {
			return "J00005_0";
		} else {
			return "J00005_1";
		}
	}

	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		// TODO Auto-generated method stub
		return basketSf.getOddsList(listInfo, B_SF);
	}

}
