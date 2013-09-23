package com.ruyicai.android.controller.adapter.listview;

import com.ruyicai.android.R;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 简单列表适配器：表象的内容由列表字符串和详情图标组成.用于登陆页面新用户注册等列表
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-2
 */
public class SimpleListViewAdapter extends BaseAdapter {
	/** 上下文对象 */
	private Context	_fContext;
	/** 选项字符串资源Id数组 */
	private int[]	_fStringResourceIds;

	/**
	 * 构造方法
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aStringResourceIds
	 *            选项字符串资源Id数组
	 */
	public SimpleListViewAdapter(Context aContext, int[] aStringResourceIds) {
		super();
		_fContext = aContext;
		_fStringResourceIds = aStringResourceIds;
	}

	@Override
	public int getCount() {
		return _fStringResourceIds.length;
	}

	@Override
	public Object getItem(int aPosition) {
		return _fStringResourceIds[aPosition];
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
			aConvertView = fInflater.inflate(R.layout.simple_listview_item,
					null);

			viewHoler._fTextView = (TextView) aConvertView
					.findViewById(R.id.simplelistviewitem_textview_text);
			aConvertView.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) aConvertView.getTag();
		}

		// 如果是登录页面的客服热线选项，则为了标红客服电话，处理Html格式字符串加颜色处理
		if (_fStringResourceIds[aPosition] == R.string.login_listviewitem_customerhotline
				|| _fStringResourceIds[aPosition] == R.string.more_listitem_customerhotline) {
			viewHoler._fTextView
					.setText(Html
							.fromHtml("客服热线&#160;&#160;<font color=\"#F33333\">400-665-1000</font>"));
		} else {
			viewHoler._fTextView.setText(_fStringResourceIds[aPosition]);
		}

		return aConvertView;
	}

	static class ViewHoler {
		TextView	_fTextView;
	}

}
