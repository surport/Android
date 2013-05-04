package com.ruyicai.activity.buy.beijing.adapter;

import java.util.List;

import com.lthj.unipay.plugin.bt;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.beijing.bean.TotalGoalsAgainstInformation;
import com.ruyicai.custom.checkbox.MyCheckBox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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
	String selectButtonTitles[] = { "0", "1", "2", "3", "4", "5", "6", "7+" };

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

	private void initTotalGoalsAgainstListShow(final Button button,
			final LinearLayout linearLayout, int position) {
		final List<TotalGoalsAgainstInformation> totalGoalsAgainstInformations = totalGoalsAgainstInformationList
				.get(position);

		if (totalGoalsAgainstInformations.size() == 0) {
			// 如果没有对阵，不显示对阵列表展开按钮
			button.setVisibility(View.GONE);
		} else {
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
	 * @param totalGoalsAgainstInformations
	 * @param viewHoler
	 */
	private void showTotalGoalsAgainstList(Button button,
			LinearLayout linearLayout,
			List<TotalGoalsAgainstInformation> totalGoalsAgainstInformations) {
		if (totalGoalsAgainstInformations.get(0).isShow()) {
			linearLayout.setVisibility(View.VISIBLE);
			button.setBackgroundResource(R.drawable.buy_jc_btn_open);

			int size = totalGoalsAgainstInformations.size();
			for (int info_i = 0; info_i < size; info_i++) {
				View itemView = getTotalGoalsAgainstListItemView(totalGoalsAgainstInformations
						.get(info_i));
				linearLayout.addView(itemView);
			}
		} else {
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
			TotalGoalsAgainstInformation totalGoalsAgainstInformation) {
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
		gameDate.append(totalGoalsAgainstInformation.getTeamId()).append(
				totalGoalsAgainstInformation.getEndTime());
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

		StringBuilder selectedString = new StringBuilder();
		boolean[] isClicks = totalGoalsAgainstInformation.getIsClicks();
		for (int button_i = 0; button_i < SELECT_BUTTON_NUM; button_i++) {
			if (isClicks[button_i] == true) {
				selectedString.append(selectButtonTitles[button_i]).append(" ");
			}
		}

		if (selectedString.length() != 0) {
			bettingButton.setText(selectedString.toString());
		}else {
			bettingButton.setText("");
		}

		bettingButton
				.setOnClickListener(new TotalGoalsAgainstListButtonOnClickListener());
		// 析
		TextView analysisTextView = (TextView) itemView
				.findViewById(R.id.game_analysis);
		// 胆
		Button danTextButton = (Button) itemView.findViewById(R.id.game_dan);

		return itemView;
	}

	/**
	 * 总进球数对阵列表按钮事件监听实现类
	 * 
	 * @author Administrator
	 * 
	 */
	class TotalGoalsAgainstListButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.jc_main_list_item_button:
				createTotalGoalsSelectDialog(v);
				break;

			case R.id.game_analysis:
				Toast.makeText(context, "分析按钮", 1).show();
				break;

			case R.id.game_dan:
				Toast.makeText(context, "胆按钮", 1).show();
				break;

			}
		}

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

		Button okButton = (Button) dialogView.findViewById(R.id.ok);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int button_i = 0; button_i < SELECT_BUTTON_NUM; button_i++) {
					MyCheckBox selectButton = selectButtons[button_i];
					if (selectButton.getChecked() == true) {
						totalGoalsAgainstInformation.getIsClicks()[button_i] = true;
					} else {
						totalGoalsAgainstInformation.getIsClicks()[button_i] = false;
					}

				}
				selectDialog.dismiss();
				((BeiJingSingleGameActivity) context)
						.refreshAgainstInformationShow(false, false);
			}
		});
		Button cancelButton = (Button) dialogView.findViewById(R.id.canel);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectDialog.dismiss();
			}
		});
	}
}
