package com.ruyicai.activity.buy.beijing.adapter;

import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.beijing.bean.WinTieLossAgainstInformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
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
public class WinTieLossAdapter extends BaseAdapter {
	private static final String TAG = "WinTieLossAdapter";
	/** 上下文对象 */
	private Context context;
	/** 显示的让球胜平负对阵信息集合 */
	private List<List<WinTieLossAgainstInformation>> winTieLossAgainstInformationList;

	public WinTieLossAdapter(
			Context context,
			List<List<WinTieLossAgainstInformation>> winTieLossAgainstInformationList) {
		this.context = context;
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
		upDownButton.setBackgroundResource(R.drawable.buy_jc_btn_close);
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
			button.setBackgroundResource(R.drawable.buy_jc_btn_open);

			int size = winTieLossAgainstInformations.size();
			for (int info_i = 0; info_i < size; info_i++) {
				View itemView = getWinTieLossAgainstListItemView(winTieLossAgainstInformations
						.get(info_i));
				linearLayout.addView(itemView);
			}
		}
		// 不展开
		else {
			linearLayout.setVisibility(LinearLayout.GONE);
			button.setBackgroundResource(R.drawable.buy_jc_btn_close);
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
			final WinTieLossAgainstInformation winTieLossAgainstInformation) {
		View itemView = LayoutInflater.from(context).inflate(
				R.layout.buy_jc_main_listview_item_other, null);

		// 联赛名称
		TextView leagueTextView = (TextView) itemView
				.findViewById(R.id.game_name);
		leagueTextView.setText(winTieLossAgainstInformation.getLeague());
		// 比赛时间
		TextView gameDateTextView = (TextView) itemView
				.findViewById(R.id.game_date);
		StringBuffer gameDate = new StringBuffer();
		gameDate.append("编号：").append(winTieLossAgainstInformation.getTeamId())
				.append("\n").append(winTieLossAgainstInformation.getEndTime())
				.append("(截)");
		gameDateTextView.setText(gameDate);

		// 主队
		final TextView homeTeamTextView = (TextView) itemView
				.findViewById(R.id.home_team_name);
		homeTeamTextView.setText(winTieLossAgainstInformation.getHomeTeam());

		// 主队赔率
		TextView v0TextView = (TextView) itemView
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
		TextView v1TextView = (TextView) itemView
				.findViewById(R.id.game_vs_odds);
		v1TextView.setText(winTieLossAgainstInformation.getV1());
		// 客队
		final TextView guestTeamTextView = (TextView) itemView
				.findViewById(R.id.guest_team_name);
		guestTeamTextView.setText(winTieLossAgainstInformation.getGuestTeam());
		// 客队赔率
		TextView v3textTextView = (TextView) itemView
				.findViewById(R.id.guest_team_odds);
		/** modify by pengcx 20130514 start */
		v3textTextView.setText(winTieLossAgainstInformation.getV0());
		/** modify by pengcx 20130514 end */

		/** close by yejc 20130514 start */
		// // 析
		// TextView analysisTextView = (TextView) itemView
		// .findViewById(R.id.game_analysis);
		// analysisTextView.setVisibility(View.GONE);
		// // 胆
		// Button danTextButton = (Button) itemView.findViewById(R.id.game_dan);
		// danTextButton.setVisibility(View.GONE);
		/** close by yejc 20130514 end */

		/** add by yejc 20130514 start */
		LinearLayout analysisAndDanLayout = (LinearLayout) itemView
				.findViewById(R.id.linearLayout3);
		analysisAndDanLayout.setVisibility(View.GONE);
		View view1 = (View)itemView.findViewById(R.id.divider_01);
		View view2 = (View)itemView.findViewById(R.id.divider_02);
		LayoutParams lp = view1.getLayoutParams();
		lp.width = 15;
		view1.setLayoutParams(lp);
		view2.setLayoutParams(lp);
		/**add by yejc 20130514 end*/

		// 主队“按钮”布局
		final LinearLayout homeTeamLayout = (LinearLayout) itemView
				.findViewById(R.id.home_layout);
		homeTeamLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 如果选择的赛事场次合或者该场次已经被选中了，就可以继续选择
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| winTieLossAgainstInformation.isSelected()) {
					// 根据当前的选中状态设置背景图片，并改变相关的选中属性
					if (!winTieLossAgainstInformation.isV0IsClick()) {
						homeTeamLayout
								.setBackgroundResource(R.drawable.team_name_bj_yellow);
						homeTeamTextView
								.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
						winTieLossAgainstInformation.setV0IsClick(true);
					} else {
						homeTeamLayout
								.setBackgroundResource(R.drawable.team_name_bj);
						homeTeamTextView
								.setBackgroundResource(R.drawable.team_name_bj_top);
						winTieLossAgainstInformation.setV0IsClick(false);
					}
					((BeiJingSingleGameActivity) context).refreshSelectNum();
				} else {
					Toast.makeText(context, "您最多可以选择15场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		// vs“按钮”布局
		final LinearLayout vsLayout = (LinearLayout) itemView
				.findViewById(R.id.vs_layout);
		vsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果选择的赛事场次合或者该场次已经被选中了，就可以继续选择
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| winTieLossAgainstInformation.isSelected()) {
					// 根据当前的选中状态设置背景图片，并改变相关的选中属性
					if (!winTieLossAgainstInformation.isV1IsClick()) {
						vsLayout.setBackgroundResource(R.drawable.team_name_bj_yellow);
						vsTextView
								.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
						winTieLossAgainstInformation.setV1IsClick(true);
					} else {
						vsLayout.setBackgroundResource(R.drawable.team_name_bj);
						vsTextView
								.setBackgroundResource(R.drawable.team_name_bj_top);
						winTieLossAgainstInformation.setV1IsClick(false);
					}
					((BeiJingSingleGameActivity) context).refreshSelectNum();
				} else {
					Toast.makeText(context, "您最多可以选择15场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		// 客队“按钮”布局
		final LinearLayout guestTeamLayout = (LinearLayout) itemView
				.findViewById(R.id.guest_layout);
		guestTeamLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果选择的赛事场次合或者该场次已经被选中了，就可以继续选择
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| winTieLossAgainstInformation.isSelected()) {
					// 根据当前的选中状态设置背景图片，并改变相关的选中属性
					if (!winTieLossAgainstInformation.isV3IsClick()) {
						guestTeamLayout
								.setBackgroundResource(R.drawable.team_name_bj_yellow);
						guestTeamTextView
								.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
						winTieLossAgainstInformation.setV3IsClick(true);
					} else {
						guestTeamLayout
								.setBackgroundResource(R.drawable.team_name_bj);
						guestTeamTextView
								.setBackgroundResource(R.drawable.team_name_bj_top);
						winTieLossAgainstInformation.setV3IsClick(false);
					}
					((BeiJingSingleGameActivity) context).refreshSelectNum();
				} else {
					Toast.makeText(context, "您最多可以选择15场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		return itemView;
	}
}
