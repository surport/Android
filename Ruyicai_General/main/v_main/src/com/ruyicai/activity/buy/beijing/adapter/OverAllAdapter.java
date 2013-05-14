package com.ruyicai.activity.buy.beijing.adapter;

import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.beijing.bean.OverAllAgainstInformation;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.code.jc.zq.FootBF;
import com.ruyicai.constant.Constants;
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
 * 显示全场总比分对阵信息适配器
 * 
 * @author Administrator
 * 
 */
public class OverAllAdapter extends BaseAdapter {
	private static final String TAG = "OverAllAdapter";
	private static final int SELECT_BUTTON_NUM = 25;
	/** 上下文对象 */
	private Context context;
	/** 选择按钮标题 */
	public static String selectButtonTitles[] = { "胜其它", "1:0", "2:0", "3:0",
			"4:0", "2:1", "3:1", "3:2", "4:1", "4:2", "平其他", "0:0", "1:1",
			"2:2", "3:3", "负其他", "0:1", "0:2", "0:3", "0:4", "1:2", "1:3",
			"1:4", "2:3", "2:4" };
	/** 显示全场总比分对阵信息集合 */
	private List<List<OverAllAgainstInformation>> overAllAgainstInformationList;

	public OverAllAdapter(Context context,
			List<List<OverAllAgainstInformation>> overAllAgainstInformationList) {
		this.context = context;
		this.overAllAgainstInformationList = overAllAgainstInformationList;
	}

	@Override
	public int getCount() {
		return overAllAgainstInformationList.size();
	}

	@Override
	public Object getItem(int position) {
		return overAllAgainstInformationList.get(position);
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

		initOverAllAgainstListShow(button, linearLayout, position);

		return convertView;
	}

	/**
	 * 初始化全场总比分对阵列表的显示
	 * 
	 * @param button
	 *            下拉按钮
	 * @param linearLayout
	 *            对阵容器布局
	 * @param position
	 *            列表索引
	 */
	private void initOverAllAgainstListShow(final Button button,
			final LinearLayout linearLayout, int position) {
		final List<OverAllAgainstInformation> overAllAgainstInformations = overAllAgainstInformationList
				.get(position);

		if (overAllAgainstInformations.size() == 0) {
			// 如果没有对阵，不显示对阵列表展开按钮
			button.setVisibility(View.GONE);
		} else {
			StringBuffer buttonString = new StringBuffer();
			buttonString
					.append(overAllAgainstInformations.get(0).getDayForamt())
					.append(" ").append(overAllAgainstInformations.size())
					.append("场比赛");
			button.setText(buttonString);

			// 如果有对阵，显示对阵列表
			showOverAllAgainstList(button, linearLayout,
					overAllAgainstInformations);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 修改当前显示的对阵列表的是否显示标识
					boolean isShow = overAllAgainstInformations.get(0).isShow();
					overAllAgainstInformations.get(0).setShow(!isShow);

					// 显示当前对阵列表
					showOverAllAgainstList(button, linearLayout,
							overAllAgainstInformations);
				}
			});
		}
	}

	/**
	 * 显示全场总比分对阵列表
	 * 
	 * @param button
	 *            下拉按钮
	 * @param linearLayout
	 *            对阵填充布局
	 * @param totalGoalsAgainstInformations
	 *            全场总比分对阵信息
	 */
	private void showOverAllAgainstList(Button button,
			LinearLayout linearLayout,
			List<OverAllAgainstInformation> totalGoalsAgainstInformations) {
		// 如果真开对阵
		if (totalGoalsAgainstInformations.get(0).isShow()) {
			linearLayout.setVisibility(View.VISIBLE);
			button.setBackgroundResource(R.drawable.buy_jc_btn_open);

			int size = totalGoalsAgainstInformations.size();
			for (int info_i = 0; info_i < size; info_i++) {
				View itemView = getOverAllAgainstListItemView(totalGoalsAgainstInformations
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
	 * 获取全场总比分对阵列表单元视图
	 * 
	 * @param overAllAgainstInformation
	 *            显示全场总比分对阵信息对象
	 * @return
	 */
	private View getOverAllAgainstListItemView(
			final OverAllAgainstInformation overAllAgainstInformation) {
		View itemView = LayoutInflater.from(context).inflate(
				R.layout.buy_jc_main_listview_item_others, null);

		// 赛事
		TextView leagueTextView = (TextView) itemView
				.findViewById(R.id.game_name);
		leagueTextView.setText(overAllAgainstInformation.getLeague());

		// 比赛时间
		TextView gameDateTextView = (TextView) itemView
				.findViewById(R.id.game_date);
		StringBuffer gameDate = new StringBuffer();
		gameDate.append("编号：").append(overAllAgainstInformation.getTeamId())
				.append("\n").append(overAllAgainstInformation.getEndTime())
				.append("(截)");
		gameDateTextView.setText(gameDate);

		// 主队
		final TextView homeTeamTextView = (TextView) itemView
				.findViewById(R.id.home_team_name);
		homeTeamTextView.setText(overAllAgainstInformation.getHomeTeam());

		// vs文本
		final TextView vsTextView = (TextView) itemView
				.findViewById(R.id.game_vs);

		// 客队
		final TextView guestTeamTextView = (TextView) itemView
				.findViewById(R.id.guest_team_name);
		guestTeamTextView.setText(overAllAgainstInformation.getGuestTeam());

		// 投注按钮
		TextView bettingButton = (Button) itemView
				.findViewById(R.id.jc_main_list_item_button);
		bettingButton.setTag(overAllAgainstInformation);
		bettingButton.setEllipsize(TextUtils.TruncateAt.END);
		// 根据选中的信息，设置投注按钮的文本
		StringBuilder selectedString = new StringBuilder();
		boolean[] isClicks = overAllAgainstInformation.getIsClicks();
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
						|| overAllAgainstInformation.isSelected()) {
					createOverAllSelectDialog(v);
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
//
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
	 * 创建半全场选择对话框
	 * 
	 * @param v
	 */
	public void createOverAllSelectDialog(View v) {
		View dialogView = LayoutInflater.from(context).inflate(
				R.layout.buy_lq_sfc_dialog, null);
		final Dialog selectDialog = new AlertDialog.Builder(context).create();

		// 标题
		TextView titleTextView = (TextView) dialogView
				.findViewById(R.id.layout_main_text_title);
		final OverAllAgainstInformation overAllAgainstInformation = (OverAllAgainstInformation) v
				.getTag();
		StringBuilder titleString = new StringBuilder();
		titleString.append(overAllAgainstInformation.getHomeTeam())
				.append(" VS ")
				.append(overAllAgainstInformation.getGuestTeam());
		titleTextView.setText(titleString);

		/** 选择按钮的资源Id */
		int[] selectButtonIds = { R.id.lq_sfc_dialog_check01,
				R.id.lq_sfc_dialog_check02, R.id.lq_sfc_dialog_check03,
				R.id.lq_sfc_dialog_check04, R.id.lq_sfc_dialog_check05,
				R.id.lq_sfc_dialog_check06, R.id.lq_sfc_dialog_check07,
				R.id.lq_sfc_dialog_check08, R.id.lq_sfc_dialog_check09,
				R.id.lq_sfc_dialog_check010, R.id.lq_sfc_dialog_check014,
				R.id.lq_sfc_dialog_check015, R.id.lq_sfc_dialog_check016,
				R.id.lq_sfc_dialog_check017, R.id.lq_sfc_dialog_check018,
				R.id.lq_sfc_dialog_check019, R.id.lq_sfc_dialog_check020,
				R.id.lq_sfc_dialog_check021, R.id.lq_sfc_dialog_check022,
				R.id.lq_sfc_dialog_check023, R.id.lq_sfc_dialog_check024,
				R.id.lq_sfc_dialog_check025, R.id.lq_sfc_dialog_check026,
				R.id.lq_sfc_dialog_check027, R.id.lq_sfc_dialog_check028 };
		/** 选择按钮sp */
		String selectButtonSPs[] = { overAllAgainstInformation.getScore_v90(),
				overAllAgainstInformation.getScore_v10(),
				overAllAgainstInformation.getScore_v20(),
				overAllAgainstInformation.getScore_v30(),
				overAllAgainstInformation.getScore_v40(),
				overAllAgainstInformation.getScore_v21(),
				overAllAgainstInformation.getScore_v31(),
				overAllAgainstInformation.getScore_v32(),
				overAllAgainstInformation.getScore_v41(),
				overAllAgainstInformation.getScore_v42(),
				overAllAgainstInformation.getScore_v99(),
				overAllAgainstInformation.getScore_v00(),
				overAllAgainstInformation.getScore_v11(),
				overAllAgainstInformation.getScore_v22(),
				overAllAgainstInformation.getScore_v33(),
				overAllAgainstInformation.getScore_v09(),
				overAllAgainstInformation.getScore_v01(),
				overAllAgainstInformation.getScore_v02(),
				overAllAgainstInformation.getScore_v03(),
				overAllAgainstInformation.getScore_v04(),
				overAllAgainstInformation.getScore_v12(),
				overAllAgainstInformation.getScore_v13(),
				overAllAgainstInformation.getScore_v14(),
				overAllAgainstInformation.getScore_v23(),
				overAllAgainstInformation.getScore_v24() };

		/** 选择对话框选择按钮 */
		final MyCheckBox[] selectButtons = new MyCheckBox[SELECT_BUTTON_NUM];
		for (int button_i = 0; button_i < SELECT_BUTTON_NUM; button_i++) {
			selectButtons[button_i] = (MyCheckBox) dialogView
					.findViewById(selectButtonIds[button_i]);
			selectButtons[button_i].setVisibility(CheckBox.VISIBLE);
			selectButtons[button_i].setCheckText(selectButtonSPs[button_i]);
			selectButtons[button_i].setPosition(button_i);
			selectButtons[button_i].setChecked(overAllAgainstInformation
					.getIsClicks()[button_i]);
			selectButtons[button_i].setCheckTitle(selectButtonTitles[button_i]);
		}

		LinearLayout layout1 = (LinearLayout) dialogView
				.findViewById(R.id.jc_check_dialog_layout2);
		LinearLayout layout2 = (LinearLayout) dialogView
				.findViewById(R.id.jc_check_dialog_layout3);
		layout1.setVisibility(LinearLayout.VISIBLE);
		layout2.setVisibility(LinearLayout.VISIBLE);

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
						overAllAgainstInformation.getIsClicks()[button_i] = true;
					} else {
						overAllAgainstInformation.getIsClicks()[button_i] = false;
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
				boolean[] isClicks = overAllAgainstInformation.getIsClicks();
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
