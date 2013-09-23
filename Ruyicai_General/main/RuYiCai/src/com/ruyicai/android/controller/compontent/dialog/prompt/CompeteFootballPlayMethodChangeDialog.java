package com.ruyicai.android.controller.compontent.dialog.prompt;

import com.ruyicai.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * 竞彩足球玩法切换对话框
 * 
 * @author xiang_000
 * @since RYC1.0 2013-5-5
 */
public class CompeteFootballPlayMethodChangeDialog extends PromptDialog {
	/** 胜平负单选按钮组 */
	private RadioGroup	_fWinTieLossPassRadioGroup;
	/** 胜平负过关按钮 */
	private RadioButton	_fWinTieLossPassRadioButton;
	/** 胜平负单关按钮 */
	private RadioButton	_fWinTieLossSingleRadioButton;

	/** 总进球单选按钮组 */
	private RadioGroup	_fTotalGoalPassRadioGroup;
	/** 总进球过关按钮 */
	private RadioButton	_fTotalGoalPassRadioButton;
	/** 总进球单关按钮 */
	private RadioButton	_fTotalGoalSingleRadioButton;

	/** 比分单选按钮组 */
	private RadioGroup	_fScorePassRadioGroup;
	/** 比分过关按钮 */
	private RadioButton	_fScorePassRadioButton;
	/** 比分单关按钮 */
	private RadioButton	_fScoreSingleRadioButton;

	/** 半全场单选按钮组 */
	private RadioGroup	_fHalfAudiencePassRadioGroup;
	/** 半全场过关按钮 */
	private RadioButton	_fHalfAudiencePassRadioButton;
	/** 半全场单关按钮 */
	private RadioButton	_fHalfAudienceSingleRadioButton;

	/** 混合投注单选按钮组 */
	private RadioGroup	_fMixBetRadioGroup;
	/** 混合投注按钮 */
	private RadioButton	_fMixBetRadioButton;

	/** 是否已经更新单选按钮的选中状态标识 */
	private boolean		_fAlreadyChanged	= false;

	public CompeteFootballPlayMethodChangeDialog(Context aContext) {
		super(aContext);
	}

	@Override
	public void set_fTitleString() {
		_fTitleString = _fResources
				.getString(R.string.competefootball_playmethodchange_title);
	}

	@Override
	public void set_fContentView() {
		LayoutInflater layoutInflater = (LayoutInflater) _fContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_fContentView = layoutInflater.inflate(
				R.layout.compete_playmethodchange_content, null);

		_fWinTieLossPassRadioGroup = (RadioGroup) _fContentView
				.findViewById(R.id.competefootball_radiogroup_wintielosspass);
		_fWinTieLossPassRadioGroup
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioGroupOnCheckedChangeListener());
		_fTotalGoalPassRadioGroup = (RadioGroup) _fContentView
				.findViewById(R.id.competefootball_radiogroup_totalgoalpass);
		_fTotalGoalPassRadioGroup
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioGroupOnCheckedChangeListener());
		_fScorePassRadioGroup = (RadioGroup) _fContentView
				.findViewById(R.id.competefootball_radiogroup_scorepass);
		_fScorePassRadioGroup
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioGroupOnCheckedChangeListener());
		_fHalfAudiencePassRadioGroup = (RadioGroup) _fContentView
				.findViewById(R.id.competefootball_radiogroup_halfaudiencepass);
		_fHalfAudiencePassRadioGroup
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioGroupOnCheckedChangeListener());
		_fMixBetRadioGroup = (RadioGroup) _fContentView
				.findViewById(R.id.competefootball_radiogroup_mixbet);
		_fMixBetRadioGroup
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioGroupOnCheckedChangeListener());

		_fWinTieLossPassRadioButton = (RadioButton) _fContentView
				.findViewById(R.id.competefootball_radiobutton_wintielosspass);
		_fWinTieLossPassRadioButton
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioButtonOnCheckedChangeListener());
		_fWinTieLossSingleRadioButton = (RadioButton) _fContentView
				.findViewById(R.id.competefootball_radiobutton_wintielosssingle);
		_fWinTieLossSingleRadioButton
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioButtonOnCheckedChangeListener());
		_fTotalGoalPassRadioButton = (RadioButton) _fContentView
				.findViewById(R.id.competefootball_radiobutton_totalgoalpass);
		_fTotalGoalPassRadioButton
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioButtonOnCheckedChangeListener());
		_fTotalGoalSingleRadioButton = (RadioButton) _fContentView
				.findViewById(R.id.competefootball_radiobutton_totalgoalsingle);
		_fTotalGoalSingleRadioButton
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioButtonOnCheckedChangeListener());
		_fScorePassRadioButton = (RadioButton) _fContentView
				.findViewById(R.id.competefootball_radiobutton_scorepass);
		_fScorePassRadioButton
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioButtonOnCheckedChangeListener());
		_fScoreSingleRadioButton = (RadioButton) _fContentView
				.findViewById(R.id.competefootball_radiobutton_scoresingle);
		_fScoreSingleRadioButton
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioButtonOnCheckedChangeListener());
		_fHalfAudiencePassRadioButton = (RadioButton) _fContentView
				.findViewById(R.id.competefootball_radiobutton_halfaudiencepass);
		_fHalfAudiencePassRadioButton
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioButtonOnCheckedChangeListener());
		_fHalfAudienceSingleRadioButton = (RadioButton) _fContentView
				.findViewById(R.id.competefootball_radiobutton_halfaudiencesingle);
		_fHalfAudienceSingleRadioButton
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioButtonOnCheckedChangeListener());
		_fMixBetRadioButton = (RadioButton) _fContentView
				.findViewById(R.id.competefootball_radiobutton_mixbet);
		_fMixBetRadioButton
				.setOnCheckedChangeListener(new CompeteFootballPlayMethodChangeRadioButtonOnCheckedChangeListener());
	}

	@Override
	public void set_fPositiveButton() {

	}

	@Override
	public void set_fOtherButton() {

	}

	@Override
	public void set_fNegativeButton() {

	}

	/**
	 * 竞彩足球玩法切换单选按钮组选中监听实现类
	 * 
	 * @author Administrator
	 * @since RYC1.0 2013-7-4
	 */
	class CompeteFootballPlayMethodChangeRadioGroupOnCheckedChangeListener
			implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (!_fAlreadyChanged) {
				_fAlreadyChanged = true;

				if (group == _fWinTieLossPassRadioGroup) {
					_fTotalGoalPassRadioGroup.clearCheck();
					_fScorePassRadioGroup.clearCheck();
					_fHalfAudiencePassRadioGroup.clearCheck();
					_fMixBetRadioGroup.clearCheck();
				} else if (group == _fTotalGoalPassRadioGroup) {
					_fWinTieLossPassRadioGroup.clearCheck();
					_fScorePassRadioGroup.clearCheck();
					_fHalfAudiencePassRadioGroup.clearCheck();
					_fMixBetRadioGroup.clearCheck();
				} else if (group == _fScorePassRadioGroup) {
					_fWinTieLossPassRadioGroup.clearCheck();
					_fTotalGoalPassRadioGroup.clearCheck();
					_fHalfAudiencePassRadioGroup.clearCheck();
					_fMixBetRadioGroup.clearCheck();
				} else if (group == _fHalfAudiencePassRadioGroup) {
					_fWinTieLossPassRadioGroup.clearCheck();
					_fTotalGoalPassRadioGroup.clearCheck();
					_fScorePassRadioGroup.clearCheck();
					_fMixBetRadioGroup.clearCheck();
				} else if (group == _fMixBetRadioGroup) {
					_fWinTieLossPassRadioGroup.clearCheck();
					_fTotalGoalPassRadioGroup.clearCheck();
					_fScorePassRadioGroup.clearCheck();
					_fHalfAudiencePassRadioGroup.clearCheck();
				}

				_fAlreadyChanged = false;
			}
		}
	}

	class CompeteFootballPlayMethodChangeRadioButtonOnCheckedChangeListener
			implements android.widget.CompoundButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.competefootball_radiobutton_wintielosspass:
				Toast.makeText(_fContext, "1", 1).show();
				break;
			case R.id.competefootball_radiobutton_wintielosssingle:
				Toast.makeText(_fContext, "2", 1).show();
				break;
			case R.id.competefootball_radiobutton_totalgoalpass:
				Toast.makeText(_fContext, "3", 1).show();
				break;
			case R.id.competefootball_radiobutton_totalgoalsingle:
				Toast.makeText(_fContext, "4", 1).show();
				break;
			case R.id.competefootball_radiobutton_scorepass:
				Toast.makeText(_fContext, "5", 1).show();
				break;
			case R.id.competefootball_radiobutton_scoresingle:
				Toast.makeText(_fContext, "6", 1).show();
				break;
			case R.id.competefootball_radiobutton_halfaudiencepass:
				Toast.makeText(_fContext, "7", 1).show();
				break;
			case R.id.competefootball_radiobutton_halfaudiencesingle:
				Toast.makeText(_fContext, "8", 1).show();
				break;
			case R.id.competefootball_radiobutton_mixbet:
				Toast.makeText(_fContext, "9", 1).show();
				break;
			}

			dismiss();
		}

	}
}
