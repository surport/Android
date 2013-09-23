package com.ruyicai.android.controller.activity.home.buylotteryhall.compete;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.compontent.bar.CompeteBar;
import com.ruyicai.android.controller.compontent.bar.CompeteBarInterface;
import com.ruyicai.android.controller.compontent.bar.TitleBar;
import com.ruyicai.android.controller.compontent.bar.TitleBarInterface;
import com.ruyicai.android.controller.compontent.dialog.DialogFactory;
import com.ruyicai.android.controller.compontent.dialog.DialogType;
import com.ruyicai.android.model.bean.lottery.compete.against.AgainstInfo;

/**
 * 竞彩选号页面父类
 * 
 * @author Administrator
 * @since RYC1.0 2013-7-4
 */
public abstract class CompeteActivity extends BaseActivity implements
		TitleBarInterface, CompeteBarInterface {
	/** 标题栏 */
	@InjectView(R.id.compete_titlebar)
	protected TitleBar				_fTitleBar;
	/** 竞彩栏 */
	@InjectView(R.id.compete_competebar)
	private CompeteBar				_fCompeteBar;
	/** 竞彩对阵列表 */
	@InjectView(R.id.compete_againstlist)
	private ExpandableListView		_fAgainstListExpandableListView;
	/** 竞彩对阵适配器 */
	private ExpandableListAdapter	_fAgainstExpandableListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compete_activity);

		// 设置标题栏接口
		_fTitleBar.set_fTitleBarInterface(this);
		_fTitleBar.initTitleBarShow();

		// 设置竞彩栏
		_fCompeteBar.set_fCompeteBarInterface(this);
		_fCompeteBar.initCompeteBarShow();

		// 设置竞彩对阵列表
		List<String> groups = new ArrayList<String>();
		groups.add("2013-07-29 星期一 3场比赛");
		groups.add("2013-07-30 星期二 2场比赛");

		List<List<AgainstInfo>> againstLists = new ArrayList<List<AgainstInfo>>();
		for (int i = 0; i < 3; i++) {
			List<AgainstInfo> againsts = new ArrayList<AgainstInfo>();
			for (int j = 0; j < 3; j++) {
				AgainstInfo againstInfo = new AgainstInfo();
				againstInfo.setfLeague("俱乐部赛");
				againstInfo.setfHomeTeam("香港杰志");
				againstInfo.setfGuestTeam("曼彻斯特联");
				againstInfo.setfEndTime("07-29 19:55");

				againsts.add(againstInfo);
			}
			againstLists.add(againsts);
		}

		_fAgainstExpandableListAdapter = new CompeteExpandableListAdapter(this,
				groups, againstLists);
		_fAgainstListExpandableListView
				.setAdapter(_fAgainstExpandableListAdapter);
	}

	@Override
	public void setTitleButton() {

	}

	@Override
	public void setPlayMethodChangeButton() {
		_fCompeteBar._fPlayMehodChangeButton
				.setOnClickListener(new CompeteFootballCompeteBarOnClickListener());
	}

	@Override
	public void setEventSelectButton() {
		_fCompeteBar._fEventSelectButton
				.setOnClickListener(new CompeteFootballCompeteBarOnClickListener());
	}

	@Override
	public void setRealTimeScoreButton() {
		_fCompeteBar._fRunTimeScoreButton
				.setOnClickListener(new CompeteFootballCompeteBarOnClickListener());
	}

	/**
	 * 竞彩栏点击事件监听实现类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-5-4
	 */
	class CompeteFootballCompeteBarOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.competebar_button_playmethodchange:
				DialogFactory dialogFactory = new DialogFactory(
						CompeteActivity.this);
				Dialog dialog = (Dialog) dialogFactory
						.assemblePromptDialogWithType(DialogType.COMPETEFOOTBALL_PLAYMETHODCHANGE_DIALOG);
				dialog.show();
				break;
			case R.id.competebar_button_eventchange:
				Toast.makeText(CompeteActivity.this, "赛事选择按钮", 1).show();
				break;
			case R.id.competebar_button_realtimescore:
				Toast.makeText(CompeteActivity.this, "即时比分按钮", 1).show();
				break;
			}
		}

	}

	/**
	 * 对阵列表适配器
	 * 
	 * @author Administrator
	 * @since RYC1.0 2013-7-11
	 */
	class CompeteExpandableListAdapter<T> extends BaseExpandableListAdapter {
		/** 上下文对象 */
		private Context					_fcontext;
		/** 显示分组信息集合 */
		private List<String>			_fgroups;
		/** 显示对阵信息结合 */
		private List<List<AgainstInfo>>	_fagainstLists;

		/**
		 * 構造方法
		 * 
		 * @param groups
		 *            分組的名称集合
		 * @param againstLists
		 *            对阵列表集合
		 */
		public CompeteExpandableListAdapter(Context context,
				List<String> groups, List<List<AgainstInfo>> againstLists) {
			super();
			this._fcontext = context;
			this._fgroups = groups;
			this._fagainstLists = againstLists;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return this._fagainstLists.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LayoutInflater layoutInflater = (LayoutInflater) _fcontext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View chileView = null;
			if (childPosition == 0) {
				chileView = layoutInflater.inflate(
						R.layout.compete_againstlist_wintielossitem, null);
			} else if (childPosition == 1) {
				chileView = layoutInflater.inflate(
						R.layout.compete_againstlist_totalgoalsitem, null);
			} else if (childPosition == 2) {
				chileView = layoutInflater.inflate(
						R.layout.compete_againstlist_updownsingledoubleitem,
						null);
			}

			return chileView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this._fagainstLists.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return this._fgroups.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return this._fgroups.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView groupTitle = new TextView(_fcontext);
			groupTitle.setGravity(Gravity.CENTER_VERTICAL);
			groupTitle.setTextColor(Color.BLACK);
			groupTitle.setTextSize(15.0f);
			groupTitle.setPadding(10, 0, 0, 0);
			if (isExpanded) {
				groupTitle
						.setBackgroundResource(R.drawable.compete_grouptitle_down);
			} else {
				groupTitle
						.setBackgroundResource(R.drawable.compete_grouptitle_up);
			}
			groupTitle.setText((CharSequence) getGroup(groupPosition));
			return groupTitle;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}
