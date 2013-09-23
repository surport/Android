package com.ruyicai.android.controller.adapter.listview;

import com.ruyicai.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 详情列表表项适配器：包含图标，表项名称，表项描述和详情图片。用户充值页面充值列表使用。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-3
 */
public class DescriptionListViewAdapter extends BaseAdapter {
	/** 上下文对象 */
	private Context	_fContext;
	/** 表项图标图片资源Id数组 */
	private int[]	_fIconResourceIds;
	/** 表项标题字符串资源Id数组 */
	private int[]	_fIitemStringResourceIds;
	/** 表项描述字符串资源Id数组 */
	private int[]	_fDescriptionResourceIds;

	public DescriptionListViewAdapter(Context aContexts,
			int[] aIconResourceIds, int[] aIitemStringResourceIds,
			int[] aDescriptionResourceIds) {
		super();
		_fContext = aContexts;
		_fIconResourceIds = aIconResourceIds;
		_fIitemStringResourceIds = aIitemStringResourceIds;
		_fDescriptionResourceIds = aDescriptionResourceIds;
	}

	@Override
	public int getCount() {
		return _fIconResourceIds.length;
	}

	@Override
	public Object getItem(int aPosition) {
		return _fIconResourceIds[aPosition];
	}

	@Override
	public long getItemId(int aPosition) {
		return aPosition;
	}

	@Override
	public View getView(int aPosition, View aConvertView, ViewGroup aParent) {
		ViewHoler viewHoler = null;

		if (aConvertView == null) {
			viewHoler = new ViewHoler();

			LayoutInflater fInflater = LayoutInflater.from(_fContext);
			aConvertView = fInflater.inflate(
					R.layout.description_listview_item, null);

			viewHoler._fIconImageView = (ImageView) aConvertView
					.findViewById(R.id.descriptionlistviewitem_imageview_icon);
			viewHoler._fItemTextView = (TextView) aConvertView
					.findViewById(R.id.descriptionlistviewitem_textview_item);
			viewHoler._fDescriptionTextView = (TextView) aConvertView
					.findViewById(R.id.descriptionlistviewitem_textview_description);

			aConvertView.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) aConvertView.getTag();
		}

		viewHoler._fIconImageView
				.setImageResource(_fIconResourceIds[aPosition]);
		viewHoler._fItemTextView.setText(_fIitemStringResourceIds[aPosition]);
		viewHoler._fDescriptionTextView
				.setText(_fDescriptionResourceIds[aPosition]);

		return aConvertView;
	}

	static class ViewHoler {
		ImageView	_fIconImageView;
		TextView	_fItemTextView;
		TextView	_fDescriptionTextView;
	}
}
