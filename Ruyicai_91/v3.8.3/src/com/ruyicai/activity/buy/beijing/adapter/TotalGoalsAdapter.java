package com.ruyicai.activity.buy.beijing.adapter;

import java.util.List;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.beijing.bean.TotalGoalsAgainstInformation;
import com.ruyicai.custom.checkbox.MyCheckBox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示总进球数对阵信息适配器
 * 
 * @author Administrator
 * 
 */
public class TotalGoalsAdapter extends BaseAdapter {
	private static final String TAG = "TotalGoalsAdapter";
	/** 上下文对象 */
	private Context context;

	/** 显示总进球数对阵信息集合 */
	private List<List<TotalGoalsAgainstInformation>> totalGoalsAgainstInformationList;
	/** 选择按钮标题 */
	public static String selectButtonTitles[] = { "0", "1", "2", "3", "4", "5",
			"6", "7+" };

	/** 选择对话框选择按钮的个数 */
	private static final int SELECT_BUTTON_NUM = 8;

	public TotalGoalsAdapter(
			Context context,
			List<List<TotalGoalsAgainstInformation>> totalGoalsAgainstInformationList) {
		this.context = context;
		this.totalGoalsAgainstInformationList = totalGoalsAgainstInformationList;
	}

	@Override
	public int getCount() {
		return totalGoalsAgainstInformationList.size();
	}

	@Override
	public Object getItem(int position) {
		return totalGoalsAgainstInformationList.get(position);
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
		button.setBackgroundResource(R.drawable.buy_jc_btn_close);
		LinearLayout linearLayout = (LinearLayout) convertView
				.findViewById(R.id.buy_jc_main_view_list_item_linearLayout);

		initTotalGoalsAgainstListShow(button, linearLayout, position);

		return convertView;
	}

	/**
	 * 初始化总进球数对阵列表的显示
	 * 
	 * @param button
	 *            下拉按钮
	 * @param linearLayout
	 *            对阵填充布局
	 * @param position
	 *            列表索引
	 */
	private void initTotalGoalsAgainstListShow(final Button button,
			final LinearLayout linearLayout, int position) {
		final List<TotalGoalsAgainstInformation> totalGoalsAgainstInformations = totalGoalsAgainstInformationList
				.get(position);

		if (totalGoalsAgainstInformations.size() == 0) {
			// 如果没有对阵，不显示对阵列表展开按钮
			button.setVisibility(View.GONE);
		} else {
			// 显示列表按钮文本信息
			StringBuffer buttonString = new StringBuffer();
			buttonString
					.append(totalGoalsAgainstInformations.get(0).getDayForamt())
					.append(" ").append(totalGoalsAgainstInformations.size())
					.append("场比赛");
			button.setText(buttonString);

			// 如果有对阵，显示对阵列表
			showTotalGoalsAgainstList(button, linearLayout,
					totalGoalsAgainstInformations);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 修改当前显示的对阵列表的是否显示标识
					boolean isShow = totalGoalsAgainstInformations.get(0)
							.isShow();
					totalGoalsAgainstInformations.get(0).setShow(!isShow);

					// 显示当前对阵列表
					showTotalGoalsAgainstList(button, linearLayout,
							totalGoalsAgainstInformations);
				}
			});
		}
	}

	/**
	 * 显示总进球数对阵列表
	 * 
	 * @param button
	 *            下拉按钮
	 * @param linearLayout
	 *            对阵填充布局
	 * @param totalGoalsAgainstInformations
	 *            总进球对阵信息集合
	 */
	private void showTotalGoalsAgainstList(Button button,
			LinearLayout linearLayout,
			List<TotalGoalsAgainstInformation> totalGoalsAgainstInformations) {
		// 如果展开对阵列表
		if (totalGoalsAgainstInformations.get(0).isShow()) {
			linearLayout.setVisibility(View.VISIBLE);
			button.setBackgroundResource(R.drawable.buy_jc_btn_open);

			int size = totalGoalsAgainstInformations.size();
			for (int info_i = 0; info_i < size; info_i++) {
				View itemView = getTotalGoalsAgainstListItemView(totalGoalsAgainstInformations
						.get(info_i));
				linearLayout.addView(itemView);
			}
		}
		// 如果不展开
		else {
			linearLayout.setVisibility(LinearLayout.GONE);
			button.setBackgroundResource(R.drawable.buy_jc_btn_close);
		}
	}

	/**
	 * 获取总进球数对阵列表单元的视图对象
	 * 
	 * @param totalGoalsAgainstInformation2
	 * @return
	 */
	private View getTotalGoalsAgainstListItemView(
			final TotalGoalsAgainstInformation totalGoalsAgainstInformation) {
		View itemView = LayoutInflater.from(context).inflate(
				R.layout.buy_jc_main_listview_item_others, null);

		// 联赛名称
		TextView leagueTextView = (TextView) itemView
				.findViewById(R.id.game_name);
		leagueTextView.setText(totalGoalsAgainstInformation.getLeague());
		// 比赛时间
		TextView gameDateTextView = (TextView) itemView
				.findViewById(R.id.game_date);
		StringBuffer gameDate = new StringBuffer();
		gameDate.append("编号：").append(totalGoalsAgainstInformation.getTeamId())
				.append("\n").append(totalGoalsAgainstInformation.getEndTime()).append("(截)");
		gameDateTextView.setText(gameDate);
		// 主队
		TextView homeTeamTextView = (TextView) itemView
				.findViewById(R.id.home_team_name);
		homeTeamTextView.setText(totalGoalsAgainstInformation.getHomeTeam());
		// VS
		TextView vsTextView = (TextView) itemView.findViewById(R.id.game_vs);
		// 客队
		TextView guestTeamTextView = (TextView) itemView
				.findViewById(R.id.guest_team_name);
		guestTeamTextView.setText(totalGoalsAgainstInformation.getGuestTeam());
		// 投注按钮
		Button bettingButton = (Button) itemView
				.findViewById(R.id.jc_main_list_item_button);
		bettingButton.setTag(totalGoalsAgainstInformation);
		bettingButton.setEllipsize(TextUtils.TruncateAt.END);

		// 根据选中的信息，设置投注按钮的文本
		StringBuilder selectedString = new StringBuilder();
		boolean[] isClicks = totalGoalsAgainstInformation.getIsClicks();
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
						|| totalGoalsAgainstInformation.isSelected()) {
					createTotalGoalsSelectDialog(v);
				} else {
					Toast.makeText(context, "您最多可以选择10场比赛进行投注！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		/**close by yejc 20130514 start*/
//		// 析
//		TextView analysisTextView = (TextView) itemView
//				.findViewById(R.id.game_analysis);
//		analysisTextView.setVisibility(View.GONE);
//		// 胆
//		Button danTextButton = (Button) itemView.findViewById(R.id.game_dan);
//		danTextButton.setVisibility(View.GONE);
		/**close by yejc 20130514 end*/
		
		/**add by yejc 20130514 start*/
		LinearLayout analysisAndDanLayout = (LinearLayout)itemView
				.findViewById(R.id.linearLayout3);
		analysisAndDanLayout.setVisibility(View.GONE);
		/**add by yejc 20130514 end*/

		return itemView;
	}

	/**
	 * 创建总进球选择对话框
	 * 
	 * @param v
	 */
	public void createTotalGoalsSelectDialog(View v) {
		View dialogView = LayoutInflater.from(context).inflate(
				R.layout.buy_lq_sfc_dialog, null);
		final Dialog selectDialog = new AlertDialog.Builder(context).create();

		// 标题
		TextView titleTextView = (TextView) dialogView
				.findViewById(R.id.layout_main_text_title);
		final TotalGoalsAgainstInformation totalGoalsAgainstInformation = (TotalGoalsAgainstInformation) v
				.getTag();
		StringBuilder titleString = new StringBuilder();
		titleString.append(totalGoalsAgainstInformation.getHomeTeam())
				.append(" VS ")
				.append(totalGoalsAgainstInformation.getGuestTeam());
		titleTextView.setText(titleString);

		/** 选择按钮的资源Id */
		int[] selectButtonIds = { R.id.lq_sfc_dialog_check01,
				R.id.lq_sfc_dialog_check02, R.id.lq_sfc_dialog_check03,
				R.id.lq_sfc_dialog_check04, R.id.lq_sfc_dialog_check05,
				R.id.lq_sfc_dialog_check06, R.id.lq_sfc_dialog_check07,
				R.id.lq_sfc_dialog_check08 };
		/** 选择按钮sp */
		String selectButtonSPs[] = { totalGoalsAgainstInformation.getGoal_v0(),
				totalGoalsAgainstInformation.getGoal_v1(),
				totalGoalsAgainstInformation.getGoal_v2(),
				totalGoalsAgainstInformation.getGoal_v3(),
				totalGoalsAgainstInformation.getGoal_v4(),
				totalGoalsAgainstInformation.getGoal_v5(),
				totalGoalsAgainstInformation.getGoal_v6(),
				totalGoalsAgainstInformation.getGoal_v7() };
		/** 选择对话框选择按钮 */
		final MyCheckBox[] selectButtons = new MyCheckBox[SELECT_BUTTON_NUM];
		for (int button_i = 0; button_i < SELECT_BUTTON_NUM; button_i++) {
			selectButtons[button_i] = (MyCheckBox) dialogView
					.findViewById(selectButtonIds[button_i]);
			selectButtons[button_i].setVisibility(CheckBox.VISIBLE);
			selectButtons[button_i].setCheckText(selectButtonSPs[button_i]);
			selectButtons[button_i].setPosition(button_i);
			selectButtons[button_i].setChecked(totalGoalsAgainstInformation
					.getIsClicks()[button_i]);
			selectButtons[button_i].setCheckTitle(selectButtonTitles[button_i]);
		}

		selectDialog.show();
		selectDialog.getWindow().setContentView(dialogView);

		// 确定按钮
		Button okButton = (Button) dialogView.findViewById(R.id.ok);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 根据选中的按钮保存选中的相关信息，刷新对阵信息，刷新选中场次，取消对话框
				for (int button_i = 0; button_i < SELECT_BUTTON_NUM; button_i++) {
					MyCheckBox selectButton = selectButtons[button_i];
					if (selectButton.getChecked() == true) {
						totalGoalsAgainstInformation.getIsClicks()[button_i] = true;
					} else {
						totalGoalsAgainstInformation.getIsClicks()[button_i] = false;
					}

				}
				((BeiJingSingleGameActivity) context)
						.refreshAgainstInformationShow(false, false);
				((BeiJingSingleGameActivity) context).refreshSelectNum();

				selectDialog.dismiss();
			}
		});

		// 取消按钮
		Button cancelButton = (Button) dialogView.findViewById(R.id.canel);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 清空所以选中的按钮，并刷新对阵列表和选中比赛场次的个数，取消对话框
				boolean[] isClicks = totalGoalsAgainstInformation.getIsClicks();
				for (int i = 0; i < isClicks.length; i++) {
					isClicks[i] = false;
				}

				((BeiJingSingleGameActivity) context)
						.refreshAgainstInformationShow(false, false);
				((BeiJingSingleGameActivity) context).refreshSelectNum();

				selectDialog.dismiss();
			}
		});
	}
}
