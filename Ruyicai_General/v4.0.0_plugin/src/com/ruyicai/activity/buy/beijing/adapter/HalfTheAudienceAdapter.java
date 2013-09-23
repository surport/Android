package com.ruyicai.activity.buy.beijing.adapter;

import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.beijing.bean.HalfTheAudienceAgainstInformation;
import com.ruyicai.custom.checkbox.MyCheckBox;
import com.ruyicai.util.PublicMethod;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示半全场对阵信息适配器
 * 
 * @author Administrator
 * 
 */
public class HalfTheAudienceAdapter extends ParentAdapter {
	private static final String TAG = "HalfTheAudienceAdapter";
	private static final int SELECT_BUTTON_NUM = 9;
	protected static final int MAX_DAN = 5;

	/** 选择按钮标题 */
	public static String selectButtonTitles[] = { "胜胜", "胜平", "胜负", "平胜", "平平",
			"平负", "负胜", "负平", "负负" };
	/** 显示半全场对阵信息集合 */
	private List<List<HalfTheAudienceAgainstInformation>> halfTheAudienceAgainstInformationList;

	public HalfTheAudienceAdapter(
			Context context,
			List<List<HalfTheAudienceAgainstInformation>> halfTheAudienceAgainstInformationList) {
		super(context);
		this.halfTheAudienceAgainstInformationList = halfTheAudienceAgainstInformationList;
	}

	@Override
	public int getCount() {
		return halfTheAudienceAgainstInformationList.size();
	}

	@Override
	public Object getItem(int position) {
		return halfTheAudienceAgainstInformationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(
				R.layout.buy_jc_main_view_list_item, null);

		Button button = (Button) convertView
				.findViewById(R.id.buy_jc_main_view_list_item_btn);
		button.setBackgroundResource(R.drawable.buy_jc_item_btn_close);
		LinearLayout linearLayout = (LinearLayout) convertView
				.findViewById(R.id.buy_jc_main_view_list_item_linearLayout);

		initHalfTheAudienceAgainstListShow(button, linearLayout, position);

		return convertView;
	}

	private void initHalfTheAudienceAgainstListShow(final Button button,
			final LinearLayout linearLayout, int position) {
		final List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations = halfTheAudienceAgainstInformationList
				.get(position);

		if (halfTheAudienceAgainstInformations.size() == 0) {
			// 如果没有对阵，不显示对阵列表展开按钮
			button.setVisibility(View.GONE);
		} else {
			StringBuffer buttonString = new StringBuffer();
			buttonString
					.append(halfTheAudienceAgainstInformations.get(0)
							.getDayForamt()).append(" ")
					.append(halfTheAudienceAgainstInformations.size())
					.append("场比赛");
			button.setText(buttonString);

			// 如果有对阵，显示对阵列表
			showHalfTheAudienceAgainstList(button, linearLayout,
					halfTheAudienceAgainstInformations);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 修改当前显示的对阵列表的是否显示标识
					boolean isShow = halfTheAudienceAgainstInformations.get(0)
							.isShow();
					halfTheAudienceAgainstInformations.get(0).setShow(!isShow);

					// 显示当前对阵列表
					showHalfTheAudienceAgainstList(button, linearLayout,
							halfTheAudienceAgainstInformations);
				}
			});
		}
	}

	private void showHalfTheAudienceAgainstList(
			Button button,
			LinearLayout linearLayout,
			List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations) {
		if (halfTheAudienceAgainstInformations.get(0).isShow()) {
			linearLayout.setVisibility(View.VISIBLE);
			button.setBackgroundResource(R.drawable.buy_jc_item_btn_open);

			int size = halfTheAudienceAgainstInformations.size();
			for (int info_i = 0; info_i < size; info_i++) {
				View itemView = getHalfTheAudienceAgainstListItemView(halfTheAudienceAgainstInformations
						.get(info_i), info_i);
				linearLayout.addView(itemView);
			}
		} else {
			linearLayout.setVisibility(LinearLayout.GONE);
			button.setBackgroundResource(R.drawable.buy_jc_item_btn_close);
		}
	}

	private View getHalfTheAudienceAgainstListItemView(
			final HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation, final int index) {
		View itemView = LayoutInflater.from(context).inflate(
				R.layout.buy_jc_main_listview_item_others, null);
		/**add by yejc 20130823 start*/
		final LinearLayout layout = (LinearLayout) itemView
				.findViewById(R.id.jc_play_detail_layout);
		View divider = (View)itemView.findViewById(R.id.jc_main_divider_up);
		if (index == 0) {
			divider.setVisibility(View.VISIBLE);
		} else {
			divider.setVisibility(View.GONE);
		}
		/**add by yejc 20130823 end*/
		// 联赛名称
		TextView leagueTextView = (TextView) itemView
				.findViewById(R.id.game_name);
		leagueTextView.setText(halfTheAudienceAgainstInformation.getLeague());
		// 比赛时间
		TextView gameDateTextView = (TextView) itemView
				.findViewById(R.id.game_date);
		StringBuffer gameDate = new StringBuffer();
		gameDate.append("编号:")
				.append(halfTheAudienceAgainstInformation.getTeamId())
				.append("\n")
				.append(PublicMethod.getEndTime(halfTheAudienceAgainstInformation.getEndTime())).append("(截)");
		
		gameDateTextView.setText(gameDate);
		// 主队
		TextView homeTeamTextView = (TextView) itemView
				.findViewById(R.id.home_team_name);
		homeTeamTextView.setText(halfTheAudienceAgainstInformation
				.getHomeTeam());
		// VS
		TextView vsTextView = (TextView) itemView.findViewById(R.id.game_vs);
		// 客队
		TextView guestTeamTextView = (TextView) itemView
				.findViewById(R.id.guest_team_name);
		guestTeamTextView.setText(halfTheAudienceAgainstInformation
				.getGuestTeam());

		// 胆
		final Button danTextButton = (Button) itemView
				.findViewById(R.id.game_dan);
		if (halfTheAudienceAgainstInformation.isDan()) {
			danTextButton.setBackgroundResource(R.drawable.jc_btn_b);
			danTextButton.setTextColor(white);
		} else {
			danTextButton.setBackgroundResource(android.R.color.transparent);
			danTextButton.setTextColor(black);
		}

		danTextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (halfTheAudienceAgainstInformation.isDan()) {
					halfTheAudienceAgainstInformation.setDan(false);
					danTextButton.setBackgroundResource(android.R.color.transparent);
					danTextButton.setTextColor(black);
				} else {
					if (isSelectDanLegal()) {
						halfTheAudienceAgainstInformation.setDan(true);
						danTextButton
								.setBackgroundResource(R.drawable.jc_btn_b);
						danTextButton.setTextColor(white);
					}
				}

				((BeiJingSingleGameActivity) context).refreshSelectNumAndDanNum();
			}

			private boolean isSelectDanLegal() {
				if (halfTheAudienceAgainstInformation.getClickNum() > 0) {
					int selectDanNum = getSelectDanNum();
					if (selectDanNum < MAX_DAN) {
						int selectedTeamNum = getSelectedTeamNum();

						if (selectedTeamNum < 3) {
							Toast.makeText(context, "请您至少选择3场比赛，才能设胆",
									Toast.LENGTH_SHORT).show();
							return false;
						} else if (selectDanNum < (selectedTeamNum - 2)) {
							return true;
						} else {
							Toast.makeText(context,
									"胆码不能超过" + (selectedTeamNum - 2) + "个",
									Toast.LENGTH_SHORT).show();
							return false;
						}
					} else {
						Toast.makeText(context,
								"您最多可以选择" + MAX_DAN + "场比赛进行设胆！",
								Toast.LENGTH_SHORT).show();
						return false;
					}
				} else {
					return false;
				}
			}

			private int getSelectedTeamNum() {
				int selectTeamNum = 0;

				for (int list_i = 0; list_i < halfTheAudienceAgainstInformationList
						.size(); list_i++) {
					List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations = halfTheAudienceAgainstInformationList
							.get(list_i);
					for (int list_j = 0; list_j < halfTheAudienceAgainstInformations
							.size(); list_j++) {
						HalfTheAudienceAgainstInformation hahalfTheAudienceAgainstInformation = halfTheAudienceAgainstInformations
								.get(list_j);
						if (hahalfTheAudienceAgainstInformation.getClickNum() > 0) {
							selectTeamNum++;
						}
					}
				}

				return selectTeamNum;
			}

			private int getSelectDanNum() {
				int selectDanNum = 0;

				for (int list_i = 0; list_i < halfTheAudienceAgainstInformationList
						.size(); list_i++) {
					List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations = halfTheAudienceAgainstInformationList
							.get(list_i);
					for (int list_j = 0; list_j < halfTheAudienceAgainstInformations
							.size(); list_j++) {
						HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation = halfTheAudienceAgainstInformations
								.get(list_j);
						if (halfTheAudienceAgainstInformation.isDan()) {
							selectDanNum++;
						}
					}
				}

				return selectDanNum;
			}
		});

		// 投注按钮
		Button bettingButton = (Button) itemView
				.findViewById(R.id.jc_main_list_item_button);
		bettingButton.setTag(halfTheAudienceAgainstInformation);
		bettingButton.setEllipsize(TextUtils.TruncateAt.END);

		StringBuilder selectedString = new StringBuilder();
		boolean[] isClicks = halfTheAudienceAgainstInformation.getIsClicks();
		for (int button_i = 0; button_i < SELECT_BUTTON_NUM; button_i++) {
			if (isClicks[button_i] == true) {
				selectedString.append(selectButtonTitles[button_i]).append(" ");
			}
		}

		if (selectedString.length() != 0) {
			bettingButton.setText(selectedString.toString());
		} else {
			bettingButton.setText("");
		}
		bettingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| halfTheAudienceAgainstInformation.isSelected()) {
					createHalfTheAudienceSelectDialog(v, layout, index);
				} else {
					Toast.makeText(context, "您最多可以选择10场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 析
		TextView analysisTextView = (TextView) itemView
				.findViewById(R.id.game_analysis);
		analysisTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				trunExplain(getEvent(halfTheAudienceAgainstInformation));
			}
		});
		
		return itemView;
	}

	private void createHalfTheAudienceSelectDialog(final View v, LinearLayout layout, int index) {
		if (layout.getChildCount() == 0) {
			View dialogView = LayoutInflater.from(context).inflate(
					R.layout.buy_jc_zq_bqc_layout, null);
			final MyCheckBox[] selectButtons = new MyCheckBox[SELECT_BUTTON_NUM];
			final HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation = (HalfTheAudienceAgainstInformation) v
					.getTag();
			StringBuilder titleString = new StringBuilder();
			titleString.append(halfTheAudienceAgainstInformation.getHomeTeam())
					.append(" VS ")
					.append(halfTheAudienceAgainstInformation.getGuestTeam());
			
			Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					String btnStr = "";
					for (int i = 0; i < halfTheAudienceAgainstInformation.getIsClicks().length; i++) {
						if (selectButtons[i].getChecked()) {
							btnStr += selectButtons[i].getChcekTitle()
									+ "  ";
							halfTheAudienceAgainstInformation.getIsClicks()[i] = true;
						} else {
							halfTheAudienceAgainstInformation.getIsClicks()[i] = false;
						}
					}
					((Button)v).setText(btnStr);
					
					((BeiJingSingleGameActivity) context)
							.refreshSelectNumAndDanNum();

					if (halfTheAudienceAgainstInformation.isDan()
							&& !halfTheAudienceAgainstInformation.isSelected()) {
						halfTheAudienceAgainstInformation.setDan(false);
					}
				}

			};

			/** 选择按钮的资源Id */
			int[] selectButtonIds = { R.id.lq_sfc_dialog_check01,
					R.id.lq_sfc_dialog_check02, R.id.lq_sfc_dialog_check03,
					R.id.lq_sfc_dialog_check04, R.id.lq_sfc_dialog_check05,
					R.id.lq_sfc_dialog_check06, R.id.lq_sfc_dialog_check07,
					R.id.lq_sfc_dialog_check08, R.id.lq_sfc_dialog_check09, };
			/** 选择按钮sp */
			String selectButtonSPs[] = {
					halfTheAudienceAgainstInformation.getHalf_v33(),
					halfTheAudienceAgainstInformation.getHalf_v31(),
					halfTheAudienceAgainstInformation.getHalf_v30(),
					halfTheAudienceAgainstInformation.getHalf_v13(),
					halfTheAudienceAgainstInformation.getHalf_v11(),
					halfTheAudienceAgainstInformation.getHalf_v10(),
					halfTheAudienceAgainstInformation.getHalf_v03(),
					halfTheAudienceAgainstInformation.getHalf_v01(),
					halfTheAudienceAgainstInformation.getHalf_v00() };

			/** 选择对话框选择按钮 */
			
			for (int button_i = 0; button_i < SELECT_BUTTON_NUM; button_i++) {
				selectButtons[button_i] = (MyCheckBox) dialogView
						.findViewById(selectButtonIds[button_i]);
				selectButtons[button_i].setVisibility(CheckBox.VISIBLE);
				selectButtons[button_i].setCheckText(selectButtonSPs[button_i]);
				selectButtons[button_i].setPosition(button_i);
				selectButtons[button_i]
						.setChecked(halfTheAudienceAgainstInformation.getIsClicks()[button_i]);
				selectButtons[button_i].setCheckTitle(selectButtonTitles[button_i]);
				selectButtons[button_i].setHandler(handler);
			}
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
					(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			if (index == 0) {
				RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams
						(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				lParams.setMargins(0, PublicMethod.getPxInt(68.5f, context), 0, 0);
				layout.setLayoutParams(lParams);
			}
			layout.addView(dialogView, params);
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
