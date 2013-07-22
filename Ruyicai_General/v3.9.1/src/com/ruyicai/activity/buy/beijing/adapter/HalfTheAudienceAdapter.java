package com.ruyicai.activity.buy.beijing.adapter;

import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.beijing.bean.HalfTheAudienceAgainstInformation;
import com.ruyicai.custom.checkbox.MyCheckBox;
import com.ruyicai.util.PublicMethod;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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

	/** 选择按钮标题 */
	public static String selectButtonTitles[] = { "胜胜", "胜平", "胜负", "平胜", "平平",
			"平负", "负胜", "负平", "负负" };
	/** 显示半全场对阵信息集合 */
	private List<List<HalfTheAudienceAgainstInformation>> halfTheAudienceAgainstInformationList;

	public HalfTheAudienceAdapter(
			Context context,
			List<List<HalfTheAudienceAgainstInformation>> halfTheAudienceAgainstInformationList) {
		super();
		this.context = context;
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
		button.setBackgroundResource(R.drawable.buy_jc_btn_close);
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
			button.setBackgroundResource(R.drawable.buy_jc_btn_open);

			int size = halfTheAudienceAgainstInformations.size();
			for (int info_i = 0; info_i < size; info_i++) {
				View itemView = getHalfTheAudienceAgainstListItemView(halfTheAudienceAgainstInformations
						.get(info_i));
				linearLayout.addView(itemView);
			}
		} else {
			linearLayout.setVisibility(LinearLayout.GONE);
			button.setBackgroundResource(R.drawable.buy_jc_btn_close);
		}
	}

	private View getHalfTheAudienceAgainstListItemView(
			final HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation) {
		View itemView = LayoutInflater.from(context).inflate(
				R.layout.buy_jc_main_listview_item_others, null);
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
					createHalfTheAudienceSelectDialog(v);
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
		// 胆
		Button danTextButton = (Button) itemView.findViewById(R.id.game_dan);
		danTextButton.setVisibility(View.INVISIBLE);
		
		return itemView;
	}

	private void createHalfTheAudienceSelectDialog(View v) {
		View dialogView = LayoutInflater.from(context).inflate(
				R.layout.buy_lq_sfc_dialog, null);
		final Dialog selectDialog = new AlertDialog.Builder(context).create();

		// 标题
		TextView titleTextView = (TextView) dialogView
				.findViewById(R.id.layout_main_text_title);
		final HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation = (HalfTheAudienceAgainstInformation) v
				.getTag();
		StringBuilder titleString = new StringBuilder();
		titleString.append(halfTheAudienceAgainstInformation.getHomeTeam())
				.append(" VS ")
				.append(halfTheAudienceAgainstInformation.getGuestTeam());
		titleTextView.setText(titleString);

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
		final MyCheckBox[] selectButtons = new MyCheckBox[SELECT_BUTTON_NUM];
		for (int button_i = 0; button_i < SELECT_BUTTON_NUM; button_i++) {
			selectButtons[button_i] = (MyCheckBox) dialogView
					.findViewById(selectButtonIds[button_i]);
			selectButtons[button_i].setVisibility(CheckBox.VISIBLE);
			selectButtons[button_i].setCheckText(selectButtonSPs[button_i]);
			selectButtons[button_i].setPosition(button_i);
			selectButtons[button_i]
					.setChecked(halfTheAudienceAgainstInformation.getIsClicks()[button_i]);
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
						halfTheAudienceAgainstInformation.getIsClicks()[button_i] = true;
					} else {
						halfTheAudienceAgainstInformation.getIsClicks()[button_i] = false;
					}
				}
				selectDialog.dismiss();
				((BeiJingSingleGameActivity) context)
						.refreshAgainstInformationShow(false, false);
				((BeiJingSingleGameActivity) context).refreshSelectNum();
			}
		});
		Button cancelButton = (Button) dialogView.findViewById(R.id.canel);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean[] isClicks = halfTheAudienceAgainstInformation
						.getIsClicks();
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
