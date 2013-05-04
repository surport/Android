package com.ruyicai.activity.buy.beijing.adapter;

import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.bean.WinTieLossAgainstInformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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

	public List<List<WinTieLossAgainstInformation>> getWinTieLossAgainstInformationList() {
		return winTieLossAgainstInformationList;
	}

	public void setWinTieLossAgainstInformationList(
			List<List<WinTieLossAgainstInformation>> winTieLossAgainstInformationList) {
		this.winTieLossAgainstInformationList = winTieLossAgainstInformationList;
	}

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

	class ViewHoler {
		Button button;
		LinearLayout linearLayout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = LayoutInflater.from(context).inflate(
				R.layout.buy_jc_main_view_list_item, null);

		Button button = (Button) convertView
				.findViewById(R.id.buy_jc_main_view_list_item_btn);
		button.setBackgroundResource(R.drawable.buy_jc_btn_close);
		LinearLayout linearLayout = (LinearLayout) convertView
				.findViewById(R.id.buy_jc_main_view_list_item_linearLayout);

		initWinTieLossAgainstListShow(button, linearLayout, position);

		return convertView;
	}

	/**
	 * 初始化对阵列表的显示
	 * 
	 * @param position
	 *            当前显示的对阵列表索引
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
	 * 显示胜平负对阵列表
	 * 
	 * @param winTieLossAgainstInformations
	 * @param viewHoler
	 * 
	 * @param viewHoler
	 * @param winTieLossAgainstInformations
	 */
	private void showWinTieLossAgainstList(Button button,
			LinearLayout linearLayout,
			List<WinTieLossAgainstInformation> winTieLossAgainstInformations) {
		if (winTieLossAgainstInformations.get(0).isShow()) {
			linearLayout.setVisibility(View.VISIBLE);
			button.setBackgroundResource(R.drawable.buy_jc_btn_open);

			int size = winTieLossAgainstInformations.size();
			for (int info_i = 0; info_i < size; info_i++) {
				View itemView = getWinTieLossAgainstListItemView(winTieLossAgainstInformations
						.get(info_i));
				linearLayout.addView(itemView);
			}
		} else {
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
		gameDate.append(winTieLossAgainstInformation.getTeamId()).append("\n")
		.append(winTieLossAgainstInformation.getEndTime())
		.append("(截)");
		gameDateTextView.setText(gameDate);

		// 主队
		final TextView homeTeamTextView = (TextView) itemView
				.findViewById(R.id.home_team_name);
		homeTeamTextView.setText(winTieLossAgainstInformation.getHomeTeam());
		// 主队赔率
		TextView v0TextView = (TextView) itemView
				.findViewById(R.id.home_team_odds);
		v0TextView.setText(winTieLossAgainstInformation.getV0());
		// VS
		final TextView vsTextView = (TextView) itemView
				.findViewById(R.id.game_vs);
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
		v3textTextView.setText(winTieLossAgainstInformation.getV3());
		// 析
		TextView analysisTextView = (TextView) itemView
				.findViewById(R.id.game_analysis);
		analysisTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, "分析按钮", 1).show();
			}
		});
		// 胆
		Button danTextButton = (Button) itemView.findViewById(R.id.game_dan);
		danTextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, "胆按钮", 1).show();
			}
		});

		// 主队“按钮”布局
		final LinearLayout homeTeamLayout = (LinearLayout) itemView
				.findViewById(R.id.home_layout);
		homeTeamLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((winTieLossAgainstInformation.isV0IsClick()) == false) {
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
			}
		});
		// vs“按钮”布局
		final LinearLayout vsLayout = (LinearLayout) itemView
				.findViewById(R.id.vs_layout);
		vsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((winTieLossAgainstInformation.isV1IsClick()) == false) {
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

			}
		});
		// 客队“按钮”布局
		final LinearLayout guestTeamLayout = (LinearLayout) itemView
				.findViewById(R.id.guest_layout);
		guestTeamLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((winTieLossAgainstInformation.isV3IsClick()) == false) {
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

			}
		});

		return itemView;
	}
}
