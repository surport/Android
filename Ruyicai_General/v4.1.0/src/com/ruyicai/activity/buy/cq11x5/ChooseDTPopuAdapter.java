package com.ruyicai.activity.buy.cq11x5;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.palmdream.RuyicaiAndroid.R;

public class ChooseDTPopuAdapter extends BaseAdapter {
	private Context context;
	private OnDtChickItem onChickItem;
	private List<String> listResource;
	private int index;

	public ChooseDTPopuAdapter(Context context, OnDtChickItem onChickItem,
			List<String> listResource) {
		this.context = context;
		this.onChickItem = onChickItem;
		this.listResource = listResource;
	}

	@Override
	public int getCount() {
		return listResource.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listResource.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int pisition, View arg1, ViewGroup arg2) {
		View view = LayoutInflater.from(context).inflate(R.layout.popu_item,
				null);
		Button chickBtn = (Button) view.findViewById(R.id.itemBtn);
		chickBtn.setText(listResource.get(pisition).toString());
		chickBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onChickItem.onChickItem(v, pisition, listResource.get(pisition));
			}
		});	
		if(index==-1){
			chickBtn.setBackgroundResource(R.drawable.shaixuanbutton_normal);
		}else {
			if (pisition == index) {
				chickBtn.setBackgroundResource(R.drawable.shaixuanbutton_click);
			} else {
				chickBtn.setBackgroundResource(R.drawable.shaixuanbutton_normal);
			}
		}
		return view;
	}

	public interface OnDtChickItem {
		public void onChickItem(View view, int position, String text);
	}

	public void setItemSelect(int index) {
		this.index = index;
	}
}
