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
 * 图标列表选项适配器：列表项由：头图标，选项文本和详情图标组成。用户登录页面微博登陆列表
 * 
 * @author Administrator
 * @since RYC1.0 2013-4-3
 */
public class IconListViewAdapter extends BaseAdapter {
	/** 上下文对象 */
	private Context	_fContext;
	/** 图标资源Id数组 */
	private int[]	_fIconResourceIds;
	/** 字符串资源Id数组 */
	private int[]	_fStringResourceIds;

	/**
	 * 构造函数
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aIconResourceIds
	 *            图标资源Id数组
	 * @param aStringResourceIds
	 *            字符串资源Id数组
	 */
	public IconListViewAdapter(Context aContext, int[] aIconResourceIds,
			int[] aStringResourceIds) {
		super();
		_fContext = aContext;
		_fIconResourceIds = aIconResourceIds;
		_fStringResourceIds = aStringResourceIds;
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
			aConvertView = fInflater.inflate(R.layout.icon_listview_item, null);

			viewHoler._fIconImageView = (ImageView) aConvertView
					.findViewById(R.id.iconlistviewitem_imageview_icon);
			viewHoler._fTextView = (TextView) aConvertView
					.findViewById(R.id.iconlistviewitem_textview_text);
			aConvertView.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) aConvertView.getTag();
		}

		viewHoler._fIconImageView
				.setImageResource(_fIconResourceIds[aPosition]);
		viewHoler._fTextView.setText(_fStringResourceIds[aPosition]);

		return aConvertView;
	}

	static class ViewHoler {
		ImageView	_fIconImageView;
		TextView	_fTextView;
	}

}
