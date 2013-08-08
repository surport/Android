package com.ruyicai.activity.buy.beijing.adapter;

import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.beijing.bean.UpDownSingleDoubleAgainstInformation;
import com.ruyicai.util.PublicMethod;

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
 * 显示上下单双对阵信息适配器
 * 
 * @author Administrator
 * 
 */
public class UpDownSingleDoubleAdapter extends ParentAdapter {
	private static final String TAG = "UpDownSingleDoubleAdapter";
	protected static final int MAX_DAN = 5;

	/** 显示上下的那双对阵信息集合 */
	private List<List<UpDownSingleDoubleAgainstInformation>> upDownSingleDoubleAgainstInformationList;

	public UpDownSingleDoubleAdapter(
			Context context,
			List<List<UpDownSingleDoubleAgainstInformation>> upDownSingleDoubleAgainstInformationList) {
		super(context);
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
		gameDate.append("编号:")
				.append(upDownSingleDoubleAgainstInformation.getTeamId())
				.append("\n")
				.append(PublicMethod.getEndTime(upDownSingleDoubleAgainstInformation.getEndTime()))
				.append("(截)");
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

		// 胆
		final Button danTextButton = (Button) itemView
				.findViewById(R.id.game_dan);
		if (!upDownSingleDoubleAgainstInformation.isDan()) {
			danTextButton.setBackgroundResource(R.drawable.jc_btn);
		} else {
			danTextButton.setBackgroundResource(R.drawable.jc_btn_b);
		}
		danTextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (upDownSingleDoubleAgainstInformation.isDan()) {
					upDownSingleDoubleAgainstInformation.setDan(false);
					danTextButton.setBackgroundResource(R.drawable.jc_btn);
				} else {
					if (isSelectDanLegal()) {
						upDownSingleDoubleAgainstInformation.setDan(true);
						danTextButton
								.setBackgroundResource(R.drawable.jc_btn_b);
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
				if (upDownSingleDoubleAgainstInformation.getClickNum() > 0) {
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

				for (int list_i = 0; list_i < upDownSingleDoubleAgainstInformationList
						.size(); list_i++) {
					List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations = upDownSingleDoubleAgainstInformationList
							.get(list_i);
					for (int list_j = 0; list_j < upDownSingleDoubleAgainstInformations
							.size(); list_j++) {
						UpDownSingleDoubleAgainstInformation upDownSingleDoubleAgainstInformation = upDownSingleDoubleAgainstInformations
								.get(list_j);
						if (upDownSingleDoubleAgainstInformation.getClickNum() > 0) {
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

				for (int list_i = 0; list_i < upDownSingleDoubleAgainstInformationList
						.size(); list_i++) {
					List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations = upDownSingleDoubleAgainstInformationList
							.get(list_i);
					for (int list_j = 0; list_j < upDownSingleDoubleAgainstInformations
							.size(); list_j++) {
						UpDownSingleDoubleAgainstInformation upDownSingleDoubleAgainstInformation = upDownSingleDoubleAgainstInformations
								.get(list_j);
						if (upDownSingleDoubleAgainstInformation.isDan()) {
							selectDanNum++;
						}
					}
				}

				return selectDanNum;
			}
		});

		// 上单
		final LinearLayout upSingleLinearLayout = (LinearLayout) itemView
				.findViewById(R.id.upsingle);
		upSingleLinearLayout.setTag(false);
		if ((upDownSingleDoubleAgainstInformation.isV1IsClick()) == true) {
			upSingleLinearLayout
					.setBackgroundResource(R.drawable.upsingle_click);
		} else {
			upSingleLinearLayout
					.setBackgroundResource(R.drawable.upsingle_normal);
		}
		upSingleLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| upDownSingleDoubleAgainstInformation.isSelected()) {
					if ((upDownSingleDoubleAgainstInformation.isV1IsClick()) == false) {
						upSingleLinearLayout
								.setBackgroundResource(R.drawable.upsingle_click);
						upSingleLinearLayout.setTag(true);
						upDownSingleDoubleAgainstInformation.setV1IsClick(true);
					} else {
						upSingleLinearLayout
								.setBackgroundResource(R.drawable.upsingle_normal);
						upSingleLinearLayout.setTag(false);
						upDownSingleDoubleAgainstInformation
								.setV1IsClick(false);
						// 取消胆按钮
						if (upDownSingleDoubleAgainstInformation.isDan()
								&& !upDownSingleDoubleAgainstInformation
										.isSelected()) {
							upDownSingleDoubleAgainstInformation.setDan(false);
							danTextButton
									.setBackgroundResource(R.drawable.jc_btn);
						}
					}
					((BeiJingSingleGameActivity) context)
							.refreshSelectNumAndDanNum();
				} else {
					Toast.makeText(context, "您最多可以选择10场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		// 上单赔率
		TextView upSingleTextView = (TextView) itemView
				.findViewById(R.id.upsingle_textview);
		StringBuilder upSingleString = new StringBuilder();
		upSingleString.append("(")
				.append(upDownSingleDoubleAgainstInformation.getSxds_v1())
				.append(")");
		upSingleTextView.setText(upSingleString);

		// 上双
		final LinearLayout upDoubleLinearLayout = (LinearLayout) itemView
				.findViewById(R.id.updouble);
		if ((upDownSingleDoubleAgainstInformation.isV2IsClick()) == true) {
			upDoubleLinearLayout
					.setBackgroundResource(R.drawable.upsingle_click);
		} else {
			upDoubleLinearLayout
					.setBackgroundResource(R.drawable.upsingle_normal);
		}
		upDoubleLinearLayout.setTag(false);
		upDoubleLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| upDownSingleDoubleAgainstInformation.isSelected()) {
					if ((upDownSingleDoubleAgainstInformation.isV2IsClick()) == false) {
						upDoubleLinearLayout
								.setBackgroundResource(R.drawable.upsingle_click);
						upDoubleLinearLayout.setTag(true);
						upDownSingleDoubleAgainstInformation.setV2IsClick(true);
					} else {
						upDoubleLinearLayout
								.setBackgroundResource(R.drawable.upsingle_normal);
						upDoubleLinearLayout.setTag(false);
						upDownSingleDoubleAgainstInformation
								.setV2IsClick(false);
						// 取消胆按钮
						if (upDownSingleDoubleAgainstInformation.isDan()
								&& !upDownSingleDoubleAgainstInformation
										.isSelected()) {
							upDownSingleDoubleAgainstInformation.setDan(false);
							danTextButton
									.setBackgroundResource(R.drawable.jc_btn);
						}
					}
					((BeiJingSingleGameActivity) context)
							.refreshSelectNumAndDanNum();
				} else {
					Toast.makeText(context, "您最多可以选择10场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		// 上双赔率

		TextView upDoubleTextView = (TextView) itemView
				.findViewById(R.id.updouble_textview);
		StringBuilder upDoubleString = new StringBuilder();
		upDoubleString.append("(")
				.append(upDownSingleDoubleAgainstInformation.getSxds_v2())
				.append(")");
		upDoubleTextView.setText(upDoubleString);

		// 下单
		final LinearLayout downSingleLinearLayout = (LinearLayout) itemView
				.findViewById(R.id.downsingle);
		if ((upDownSingleDoubleAgainstInformation.isV3IsClick()) == true) {
			downSingleLinearLayout
					.setBackgroundResource(R.drawable.upsingle_click);
		} else {
			downSingleLinearLayout
					.setBackgroundResource(R.drawable.upsingle_normal);
		}
		downSingleLinearLayout.setTag(false);
		downSingleLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| upDownSingleDoubleAgainstInformation.isSelected()) {
					if ((upDownSingleDoubleAgainstInformation.isV3IsClick()) == false) {
						downSingleLinearLayout
								.setBackgroundResource(R.drawable.upsingle_click);
						downSingleLinearLayout.setTag(true);
						upDownSingleDoubleAgainstInformation.setV3IsClick(true);
					} else {
						downSingleLinearLayout
								.setBackgroundResource(R.drawable.upsingle_normal);
						downSingleLinearLayout.setTag(false);
						upDownSingleDoubleAgainstInformation
								.setV3IsClick(false);
						// 取消胆按钮
						if (upDownSingleDoubleAgainstInformation.isDan()
								&& !upDownSingleDoubleAgainstInformation
										.isSelected()) {
							upDownSingleDoubleAgainstInformation.setDan(false);
							danTextButton
									.setBackgroundResource(R.drawable.jc_btn);
						}
					}
					((BeiJingSingleGameActivity) context)
							.refreshSelectNumAndDanNum();
				} else {
					Toast.makeText(context, "您最多可以选择10场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// 下单赔率
		TextView downSingleTextView = (TextView) itemView
				.findViewById(R.id.downsingle_textview);
		StringBuilder downSingleString = new StringBuilder();
		downSingleString.append("(")
				.append(upDownSingleDoubleAgainstInformation.getSxds_v3())
				.append(")");
		downSingleTextView.setText(downSingleString);

		// 下双
		final LinearLayout downDoubleLayout = (LinearLayout) itemView
				.findViewById(R.id.downdouble);
		downDoubleLayout.setTag(false);

		if ((upDownSingleDoubleAgainstInformation.isV4IsClick()) == true) {
			downDoubleLayout.setBackgroundResource(R.drawable.upsingle_click);
		} else {
			downDoubleLayout.setBackgroundResource(R.drawable.upsingle_normal);

		}

		downDoubleLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((BeiJingSingleGameActivity) context)
						.isSelectedEventNumLegal()
						|| upDownSingleDoubleAgainstInformation.isSelected()) {
					if ((upDownSingleDoubleAgainstInformation.isV4IsClick()) == false) {
						downDoubleLayout
								.setBackgroundResource(R.drawable.upsingle_click);
						downDoubleLayout.setTag(true);
						upDownSingleDoubleAgainstInformation.setV4IsClick(true);
					} else {
						downDoubleLayout
								.setBackgroundResource(R.drawable.upsingle_normal);
						downDoubleLayout.setTag(false);
						upDownSingleDoubleAgainstInformation
								.setV4IsClick(false);

						// 取消胆按钮
						if (upDownSingleDoubleAgainstInformation.isDan()
								&& !upDownSingleDoubleAgainstInformation
										.isSelected()) {
							upDownSingleDoubleAgainstInformation.setDan(false);
							danTextButton
									.setBackgroundResource(R.drawable.jc_btn);
						}
					}
					((BeiJingSingleGameActivity) context)
							.refreshSelectNumAndDanNum();
				} else {
					Toast.makeText(context, "您最多可以选择10场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// 下双赔率
		TextView downDoubleTextView = (TextView) itemView
				.findViewById(R.id.downdouble_textview);
		StringBuilder downDoubleString = new StringBuilder();
		downDoubleString.append("(")
				.append(upDownSingleDoubleAgainstInformation.getSxds_v4())
				.append(")");
		downDoubleTextView.setText(downDoubleString);

		/** close by yejc 20130514 start */
		 // 析
		 TextView analysisTextView = (TextView) itemView
		 .findViewById(R.id.game_analysis);
		 analysisTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				trunExplain(getEvent(upDownSingleDoubleAgainstInformation));
			}
		 });

		return itemView;
	}
}
