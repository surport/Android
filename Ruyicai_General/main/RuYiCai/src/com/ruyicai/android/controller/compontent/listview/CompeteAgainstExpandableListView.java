package com.ruyicai.android.controller.compontent.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * 竞彩对阵列表类
 * 
 * @author Administrator
 * @since RYC1.0 2013-8-6
 */
public class CompeteAgainstExpandableListView extends ExpandableListView
		implements OnScrollListener {

	public CompeteAgainstExpandableListView(Context context) {
		super(context);
		super.setOnScrollListener(this);
	}

	public CompeteAgainstExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setOnScrollListener(this);
	}

	public CompeteAgainstExpandableListView(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		super.setOnScrollListener(this);
	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int position = view.pointToPosition(0, 0);
		if (position != AdapterView.INVALID_POSITION) {
			CompeteAgainstExpandableListView competeAgainstExpandableListView = (CompeteAgainstExpandableListView) view;
			long pos = competeAgainstExpandableListView
					.getExpandableListPosition(position);
			int groupPosition = ExpandableListView.getPackedPositionChild(pos);
			int childPosition = ExpandableListView.getPackedPositionChild(pos);

			if (childPosition < 0) {
				groupPosition = -1;
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

}
