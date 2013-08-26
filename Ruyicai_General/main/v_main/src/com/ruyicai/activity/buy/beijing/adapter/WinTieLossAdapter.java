package com.ruyicai.activity.buy.beijing.adapter;

import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.beijing.bean.WinTieLossAgainstInformation;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 让球胜平负对阵列表适配器
 * 
 * @author Administrator
 * 
 */
public class WinTieLossAdapter extends ParentAdapter {
	private static final String TAG = "WinTieLossAdapter";
	/** 最大设胆个数 */
	protected static final int MAX_DAN = 13;
	/** 显示的让球胜平负对阵信息集合 */
	private List<List<WinTieLossAgainstInformation>> winTieLossAgainstInformationList;

	public WinTieLossAdapter(
			Context context,
			List<List<WinTieLossAgainstInformation>> winTieLossAgainstInformationList) {
		super(context);
		this.winTieLossAgainstInformationList = winTieLossAgainstInformationList;
	}

	@Override
	public int getCount() {
		return winTieLossAgainstInformationList.size();
	}

	@Override
	public Object getItem(int position) {
		return winTieLossAgainstInformationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = LayoutInflater.from(context).inflate(
				R.layout.buy_jc_main_view_list_item, null);

		Button upDownButton = (Button) convertView
				.findViewById(R.id.buy_jc_main_view_list_item_btn);
		upDownButton.setBackgroundResource(R.drawable.buy_jc_item_btn_close);
		LinearLayout linearLayout = (LinearLayout) convertView
				.findViewById(R.id.buy_jc_main_view_list_item_linearLayout);

		initWinTieLossAgainstListShow(upDownButton, linearLayout, position);

		return convertView;
	}

	/**
	 * 初始化胜平负对阵列表的显示
	 * 
	 * @param button
	 *            下拉按钮
	 * @param linearLayout
	 *            对阵填充布局
	 * @param position
	 *            列表索引
	 */
	private void initWinTieLossAgainstListShow(final Button button,
			final LinearLayout linearLayout, int position) {
		// 获取当前显示的对阵列表集合
		final List<WinTieLossAgainstInformation> winTieLossAgainstInformations = winTieLossAgainstInformationList
				.get(position);

		if (winTieLossAgainstInformations.size() == 0) {
			// 如果没有对阵，不显示对阵列表展开按钮
			button.setVisibility(View.GONE);
		} else {
			StringBuffer buttonString = new StringBuffer();
			buttonString
					.append(winTieLossAgainstInformations.get(0).getDayForamt())
					.append(" ").append(winTieLossAgainstInformations.size())
					.append("场比赛");
			button.setText(buttonString);

			// 如果有对阵，显示对阵列表
			showWinTieLossAgainstList(button, linearLayout,
					winTieLossAgainstInformations);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 修改当前显示的对阵列表的是否显示标识
					boolean isShow = winTieLossAgainstInformations.get(0)
							.isShow();
					winTieLossAgainstInformations.get(0).setShow(!isShow);

					// 显示当前对阵列表
					showWinTieLossAgainstList(button, linearLayout,
							winTieLossAgainstInformations);
				}
			});
		}
	}

	/**
	 * 显示胜平负对阵里列表
	 * 
	 * @param button
	 * @param linearLayout
	 * @param winTieLossAgainstInformations
	 */
	private void showWinTieLossAgainstList(Button button,
			LinearLayout linearLayout,
			List<WinTieLossAgainstInformation> winTieLossAgainstInformations) {
		// 如果对阵列表需要展开
		if (winTieLossAgainstInformations.get(0).isShow()) {
			linearLayout.setVisibility(View.VISIBLE);
			button.setBackgroundResource(R.drawable.buy_jc_item_btn_open);

			int size = winTieLossAgainstInformations.size();
			for (int info_i = 0; info_i < size; info_i++) {
				View itemView = getWinTieLossAgainstListItemView(winTieLossAgainstInformations
						.get(info_i), info_i);
				linearLayout.addView(itemView);
			}
		}
		// 不展开
		else {
			linearLayout.setVisibility(LinearLayout.GONE);
			button.setBackgroundResource(R.drawable.buy_jc_item_btn_close);
		}
	}

	/**
	 * 获取让球胜平负对阵列表单元的视图对象
	 * 
	 * @param info_i
	 *            显示对阵信息的索引
	 * @param winTieLossAgainstInformations
	 * @return 视图对象
	 */
	private View getWinTieLossAgainstListItemView(
			final WinTieLossAgainstInformation winTieLossAgainstInformation, int index) {
		View itemView = LayoutInflater.from(context).inflate(
				R.layout.buy_jc_main_listview_item_other, null);
		/**add by yejc 20130823 start*/
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
		leagueTextView.setText(winTieLossAgainstInformation.getLeague());
		// 比赛时间
		TextView gameDateTextView = (TextView) itemView
				.findViewById(R.id.game_date);
		StringBuffer gameDate = new StringBuffer();
		gameDate.append("编号:").append(winTieLossAgainstInformation.getTeamId())
				.append("\n").append(PublicMethod.getEndTime(winTieLossAgainstInformation.getEndTime()))
				.append("(截)");
		gameDateTextView.setText(gameDate);

		// 主队
		final TextView homeTeamTextView = (TextView) itemView
				.findViewById(R.id.home_team_name);
		homeTeamTextView.setText(winTieLossAgainstInformation.getHomeTeam());

		// 主队赔率
		final TextView v0TextView = (TextView) itemView
				.findViewById(R.id.home_team_odds);
		/** modify by pengcx 20130514 start */
		v0TextView.setText(winTieLossAgainstInformation.getV3());
		/** modify by pengcx 20130514 end */
		// VS
		final TextView vsTextView = (TextView) itemView
				.findViewById(R.id.game_vs);
		
		/** modify by pengcx 20130514 start */
		if (!"".equals(winTieLossAgainstInformation.getLetPoint()) 
			&& !"0".equals(winTieLossAgainstInformation.getLetPoint())) {
			vsTextView.setText(winTieLossAgainstInformation.getLetPoint());
		}
		/** modify by pengcx 20130514 end */
		// VS赔率
		final TextView v1TextView = (TextView) itemView
				.findViewById(R.id.game_vs_odds);
		v1TextView.setText(winTieLossAgainstInformation.getV1());
		// 客队
		final TextView guestTeamTextView = (TextView) itemView
				.findViewById(R.id.guest_team_name);
		guestTeamTextView.setText(winTieLossAgainstInformation.getGuestTeam());
		// 客队赔率
		final TextView v3textTextView = (TextView) itemView
				.findViewById(R.id.guest_team_odds);
		/** modify by pengcx 20130514 start */
		v3textTextView.setText(winTieLossAgainstInformation.getV0());
		/** modify by pengcx 20130514 end */

		// 析
		TextView analysisTextView = (TextView) itemView
				.findViewById(R.id.game_analysis);
		analysisTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				trunExplain(getEvent(winTieLossAgainstInformation));
			}
		});
		// 胆
		final Button danTextButton = (Button) itemView
				.findViewById(R.id.game_dan);
		if (!winTieLossAgainstInformation.isDan()) {
			danTextButton.setBackgroundResource(android.R.color.transparent);
			danTextButton.setTextColor(black);
		} else {
			danTextButton.setBackgroundResource(R.drawable.jc_btn_b);
			danTextButton.setTextColor(white);
		}

		danTextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (winTieLossAgainstInformation.isDan()) {
					winTieLossAgainstInformation.setDan(false);
					danTextButton.setBackgroundResource(android.R.color.transparent);
					danTextButton.setTextColor(black);
				} else {
					if (isSelectDanLegal()) {
						winTieLossAgainstInformation.setDan(true);
						danTextButton
								.setBackgroundResource(R.drawable.jc_btn_b);
						danTextButton.setTextColor(white);
					}
				}

				((BeiJingSingleGameActivity) context)
						.refreshSelectNumAndDanNum();
			}

			/**
			 * 选择胆是否合法
			 *
			 * @return 是否合法标识
			 */
			private boolean isSelectDanLegal() {
				if (winTieLossAgainstInformation.getClickNum() > 0) {
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

			/**
			 * 获取选择的球队的个数
			 *
			 * @return 选择的球队的个数
			 */
			private int getSelectedTeamNum() {
				int selectTeamNum = 0;

				for (int list_i = 0; list_i < winTieLossAgainstInformationList
						.size(); list_i++) {
					List<WinTieLossAgainstInformation> winTieLossAgainstInformations = winTieLossAgainstInformationList
							.get(list_i);
					for (int list_j = 0; list_j < winTieLossAgainstInformations
							.size(); list_j++) {
						WinTieLossAgainstInformation winTieLossAgainstInformation = winTieLossAgainstInformations
								.get(list_j);
						if (winTieLossAgainstInformation.getClickNum() > 0) {
							selectTeamNum++;
						}
					}
				}

				return selectTeamNum;
			}

			/**
			 * 获取选择的胆的个数
			 *
			 * @return 选择胆的个数
			 */
			private int getSelectDanNum() {
				int selectDanNum = 0;

				for (int list_i = 0; list_i < winTieLossAgainstInformationList
						.size(); list_i++) {
					List<WinTieLossAgainstInformation> winTieLossAgainstInformations = winTieLossAgainstInformationList
							.get(list_i);
					for (int list_j = 0; list_j < winTieLossAgainstInformations
							.size(); list_j++) {
						WinTieLossAgainstInformation winTieLossAgainstInformation = winTieLossAgainstInformations
								.get(list_j);
						if (winTieLossAgainstInformation.isDan()) {
							selectDanNum++;
						}
					}
				}

				return selectDanNum;
			}
		});

		// 主队“按钮”布局
		final LinearLayout homeTeamLayout = (LinearLayout) itemView
				.findViewById(R.id.home_layout);
		/**modify by yejc 20130823 start*/
		setViewStyle(homeTeamLayout, homeTeamTextView, v0TextView,
				winTieLossAgainstInformation.isV0IsClick());
		/**modify by yejc 20130823 start*/

		homeTeamLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 如果选择的赛事场次合或者该场次已经被选中了，就可以继续选择
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| winTieLossAgainstInformation.isSelected()) {
					// 根据当前的选中状态设置背景图片，并改变相关的选中属性
					if (!winTieLossAgainstInformation.isV0IsClick()) {
						winTieLossAgainstInformation.setV0IsClick(true);
					} else {
						winTieLossAgainstInformation.setV0IsClick(false);

						// 取消胆按钮
						if (winTieLossAgainstInformation.isDan()
								&& !winTieLossAgainstInformation.isSelected()) {
							winTieLossAgainstInformation.setDan(false);
							danTextButton
									.setBackgroundResource(android.R.color.transparent);
							danTextButton.setTextColor(black);
						}
					}
					setViewStyle(homeTeamLayout, homeTeamTextView, v0TextView,
							winTieLossAgainstInformation.isV0IsClick());

					((BeiJingSingleGameActivity) context)
							.refreshSelectNumAndDanNum();
				} else {
					Toast.makeText(context, "您最多可以选择15场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		// vs“按钮”布局
		final LinearLayout vsLayout = (LinearLayout) itemView
				.findViewById(R.id.vs_layout);
		/**modify by yejc 20130823 start*/
		setViewStyle(vsLayout, vsTextView, v1TextView,
				winTieLossAgainstInformation.isV1IsClick());
		/**modify by yejc 20130823 end*/
		
		vsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果选择的赛事场次合或者该场次已经被选中了，就可以继续选择
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| winTieLossAgainstInformation.isSelected()) {
					// 根据当前的选中状态设置背景图片，并改变相关的选中属性
					if (!winTieLossAgainstInformation.isV1IsClick()) {
						winTieLossAgainstInformation.setV1IsClick(true);
					} else {
						winTieLossAgainstInformation.setV1IsClick(false);
						// 取消胆按钮
						if (winTieLossAgainstInformation.isDan()
								&& !winTieLossAgainstInformation.isSelected()) {
							winTieLossAgainstInformation.setDan(false);
							danTextButton
									.setBackgroundResource(android.R.color.transparent);
							danTextButton.setTextColor(black);
						}
					}
					/**add by yejc 20130823 start*/
					setViewStyle(vsLayout, vsTextView, v1TextView,
							winTieLossAgainstInformation.isV1IsClick());
					/**add by yejc 20130823 end*/
					((BeiJingSingleGameActivity) context)
							.refreshSelectNumAndDanNum();
				} else {
					Toast.makeText(context, "您最多可以选择15场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		// 客队“按钮”布局
		final LinearLayout guestTeamLayout = (LinearLayout) itemView
				.findViewById(R.id.guest_layout);
		/**modify by yejc 20130823 start*/
		setViewStyle(guestTeamLayout, guestTeamTextView, v3textTextView,
				winTieLossAgainstInformation.isV3IsClick());
		/**modify by yejc 20130823 end*/

		guestTeamLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果选择的赛事场次合或者该场次已经被选中了，就可以继续选择
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| winTieLossAgainstInformation.isSelected()) {
					// 根据当前的选中状态设置背景图片，并改变相关的选中属性
					if (!winTieLossAgainstInformation.isV3IsClick()) {
						winTieLossAgainstInformation.setV3IsClick(true);
					} else {
						winTieLossAgainstInformation.setV3IsClick(false);

						// 取消胆按钮
						if (winTieLossAgainstInformation.isDan()
								&& !winTieLossAgainstInformation.isSelected()) {
							winTieLossAgainstInformation.setDan(false);
							danTextButton
									.setBackgroundResource(android.R.color.transparent);
							danTextButton.setTextColor(black);
						}
					}
					/**modify by yejc 20130823 start*/
					setViewStyle(guestTeamLayout, guestTeamTextView, v3textTextView,
							winTieLossAgainstInformation.isV3IsClick());
					/**modify by yejc 20130823 end*/
					((BeiJingSingleGameActivity) context)
							.refreshSelectNumAndDanNum();
				} else {
					Toast.makeText(context, "您最多可以选择15场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		return itemView;
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
