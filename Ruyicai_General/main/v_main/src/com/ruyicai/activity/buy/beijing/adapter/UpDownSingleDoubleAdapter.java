package com.ruyicai.activity.buy.beijing.adapter;

import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.bean.UpDownSingleDoubleAgainstInformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 显示上下单双对阵信息适配器
 * 
 * @author Administrator
 * 
 */
public class UpDownSingleDoubleAdapter extends BaseAdapter {
	private static final String TAG = "UpDownSingleDoubleAdapter";
	/** 上下文对象 */
	private Context context;
	/** 显示上下的那双对阵信息集合 */
	private List<List<UpDownSingleDoubleAgainstInformation>> upDownSingleDoubleAgainstInformationList;

	public UpDownSingleDoubleAdapter(
			Context context,
			List<List<UpDownSingleDoubleAgainstInformation>> upDownSingleDoubleAgainstInformationList) {
		this.context = context;
		this.upDownSingleDoubleAgainstInformationList = upDownSingleDoubleAgainstInformationList;
	}

	@Override
	public int getCount() {
		return upDownSingleDoubleAgainstInformationList.size();
	}

	@Override
	public Object getItem(int position) {
		return upDownSingleDoubleAgainstInformationList.get(position);
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

		initUpDownSingleDoubleAgainstListShow(button, linearLayout, position);

		return convertView;
	}

	private void initUpDownSingleDoubleAgainstListShow(final Button button,
			final LinearLayout linearLayout, int position) {
		final List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations = upDownSingleDoubleAgainstInformationList
				.get(position);

		if (upDownSingleDoubleAgainstInformations.size() == 0) {
			// 如果没有对阵，不显示对阵列表展开按钮
			button.setVisibility(View.GONE);
		} else {
			StringBuffer buttonString = new StringBuffer();
			buttonString
					.append(upDownSingleDoubleAgainstInformations.get(0)
							.getDayForamt()).append(" ")
					.append(upDownSingleDoubleAgainstInformations.size())
					.append("场比赛");
			button.setText(buttonString);

			// 如果有对阵，显示对阵列表
			showUpDownSingleDoubleAgainstList(button, linearLayout,
					upDownSingleDoubleAgainstInformations);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 修改当前显示的对阵列表的是否显示标识
					boolean isShow = upDownSingleDoubleAgainstInformations.get(
							0).isShow();
					upDownSingleDoubleAgainstInformations.get(0).setShow(
							!isShow);

					// 显示当前对阵列表
					showUpDownSingleDoubleAgainstList(button, linearLayout,
							upDownSingleDoubleAgainstInformations);
				}
			});
		}
	}

	private void showUpDownSingleDoubleAgainstList(
			Button button,
			LinearLayout linearLayout,
			List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations) {
		if (upDownSingleDoubleAgainstInformations.get(0).isShow()) {
			linearLayout.setVisibility(View.VISIBLE);
			button.setBackgroundResource(R.drawable.buy_jc_btn_open);

			int size = upDownSingleDoubleAgainstInformations.size();
			for (int info_i = 0; info_i < size; info_i++) {
				View itemView = getUpDownSingleDoubleAgainstListItemView(upDownSingleDoubleAgainstInformations
						.get(info_i));
				linearLayout.addView(itemView);
			}
		} else {
			linearLayout.setVisibility(LinearLayout.GONE);
			button.setBackgroundResource(R.drawable.buy_jc_btn_close);
		}
	}

	private View getUpDownSingleDoubleAgainstListItemView(
			final UpDownSingleDoubleAgainstInformation upDownSingleDoubleAgainstInformation) {
		View itemView = LayoutInflater.from(context).inflate(
				R.layout.buy_jc_main_listview_item_sxdu, null);
		// 联赛名称
		TextView leagueTextView = (TextView) itemView
				.findViewById(R.id.game_name);
		leagueTextView
				.setText(upDownSingleDoubleAgainstInformation.getLeague());
		// 比赛时间
		TextView gameDateTextView = (TextView) itemView
				.findViewById(R.id.game_date);
		StringBuffer gameDate = new StringBuffer();
		gameDate.append(upDownSingleDoubleAgainstInformation.getTeamId())
				.append(upDownSingleDoubleAgainstInformation.getEndTime());
		gameDateTextView.setText(gameDate);
		// 主队
		TextView homeTeamTextView = (TextView) itemView
				.findViewById(R.id.home_team_name);
		homeTeamTextView.setText(upDownSingleDoubleAgainstInformation
				.getHomeTeam());
		// VS
		TextView vsTextView = (TextView) itemView.findViewById(R.id.game_vs);
		// 客队
		TextView guestTeamTextView = (TextView) itemView
				.findViewById(R.id.guest_team_name);
		guestTeamTextView.setText(upDownSingleDoubleAgainstInformation
				.getGuestTeam());
		// 上单
		final TextView upSingleTextView = (TextView) itemView
				.findViewById(R.id.upsingle);

		StringBuilder upSingleString = new StringBuilder();
		upSingleString.append("上单(")
				.append(upDownSingleDoubleAgainstInformation.getSxds_v1())
				.append(")");
		upSingleTextView.setText(upSingleString);

		upSingleTextView.setTag(false);
		upSingleTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((upDownSingleDoubleAgainstInformation.isV1IsClick()) == false) {
					upSingleTextView
							.setBackgroundResource(R.drawable.upsingle_click);
					upSingleTextView.setTag(true);
					upDownSingleDoubleAgainstInformation.setV1IsClick(true);
				} else {
					upSingleTextView
							.setBackgroundResource(R.drawable.upsingle_normal);
					upSingleTextView.setTag(false);
					upDownSingleDoubleAgainstInformation.setV1IsClick(false);
				}
			}
		});
		// 上双
		final TextView upDoubleTextView = (TextView) itemView
				.findViewById(R.id.updouble);

		StringBuilder upDoubleString = new StringBuilder();
		upDoubleString.append("上双(")
				.append(upDownSingleDoubleAgainstInformation.getSxds_v2())
				.append(")");
		upDoubleTextView.setText(upDoubleString);

		upDoubleTextView.setTag(false);
		upDoubleTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((upDownSingleDoubleAgainstInformation.isV2IsClick()) == false) {
					upDoubleTextView
							.setBackgroundResource(R.drawable.upsingle_click);
					upDoubleTextView.setTag(true);
					upDownSingleDoubleAgainstInformation.setV2IsClick(true);
				} else {
					upDoubleTextView
							.setBackgroundResource(R.drawable.upsingle_normal);
					upDoubleTextView.setTag(false);
					upDownSingleDoubleAgainstInformation.setV2IsClick(false);
				}
			}
		});
		// 下单
		final TextView downSingleTextView = (TextView) itemView
				.findViewById(R.id.downsingle);

		StringBuilder downSingleString = new StringBuilder();
		downSingleString.append("下单(")
				.append(upDownSingleDoubleAgainstInformation.getSxds_v3())
				.append(")");
		downSingleTextView.setText(downSingleString);

		downSingleTextView.setTag(false);
		downSingleTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((upDownSingleDoubleAgainstInformation.isV3IsClick()) == false) {
					downSingleTextView
							.setBackgroundResource(R.drawable.upsingle_click);
					downSingleTextView.setTag(true);
					upDownSingleDoubleAgainstInformation.setV3IsClick(true);
				} else {
					downSingleTextView
							.setBackgroundResource(R.drawable.upsingle_normal);
					downSingleTextView.setTag(false);
					upDownSingleDoubleAgainstInformation.setV3IsClick(false);
				}
			}
		});
		// 下双
		final TextView downDoubleTextView = (TextView) itemView
				.findViewById(R.id.downdouble);

		StringBuilder downDoubleString = new StringBuilder();
		downDoubleString.append("下双(")
				.append(upDownSingleDoubleAgainstInformation.getSxds_v4())
				.append(")");
		downDoubleTextView.setText(downDoubleString);

		downDoubleTextView.setTag(false);
		downDoubleTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((upDownSingleDoubleAgainstInformation.isV4IsClick()) == false) {
					downDoubleTextView
							.setBackgroundResource(R.drawable.upsingle_click);
					downDoubleTextView.setTag(true);
					upDownSingleDoubleAgainstInformation.setV4IsClick(true);
				} else {
					downDoubleTextView
							.setBackgroundResource(R.drawable.upsingle_normal);
					downDoubleTextView.setTag(false);
					upDownSingleDoubleAgainstInformation.setV4IsClick(false);
				}
			}
		});

		// 析
		TextView analysisTextView = (TextView) itemView
				.findViewById(R.id.game_analysis);
		// 胆
		Button danTextButton = (Button) itemView.findViewById(R.id.game_dan);

		return itemView;
	}

}
