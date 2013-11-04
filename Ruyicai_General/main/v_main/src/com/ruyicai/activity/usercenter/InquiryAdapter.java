package com.ruyicai.activity.usercenter;

import com.palmdream.RuyicaiAndroid.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class InquiryAdapter extends BaseAdapter {

	private Context mContext = null;
	private String[] mInfo = null;
	private OnChickItem mOnChickItem = null;
	private int mType = -1;
	private int mIndex = 0;
	
	public InquiryAdapter(Context context, String[] info, OnChickItem onChickItem, int type) {
		mContext = context;
		mInfo = info;
		mOnChickItem = onChickItem;
		mType = type;
	}

	@Override
	public int getCount() {
		if (mInfo != null) {
			return mInfo.length;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mInfo[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.inquiry_view_item,null);
		}
		Button btn = (Button)convertView;
		btn.setText(mInfo[position]);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mOnChickItem.onChickItem(position, mType);
			}
		});
		if (position == mIndex) {
			btn.setBackgroundResource(R.drawable.inquiry_state_bg_click);
		} else {
			btn.setBackgroundResource(R.drawable.inquiry_state_bg_normal);
		}
		return btn;
	}
	
	public interface OnChickItem {
		public void onChickItem(int position, int type);
	}

	public void setItemSelect(int index){
		this.mIndex = index;
	}
}
