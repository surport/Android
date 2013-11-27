package com.ruyicai.activity.join;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.palmdream.RuyicaiAndroid.R;

public class JoinPopuAdapter extends BaseAdapter {
	private Context context;
	private OnChickItem onChickItem;
	private List<String> listResource;
	private int index;
	public JoinPopuAdapter(Context context,OnChickItem onChickItem,List<String> listResource) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.onChickItem=onChickItem;
		this.listResource=listResource;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listResource.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listResource.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int pisition, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.popu_item,null);
		Button chickBtn = (Button) view.findViewById(R.id.itemBtn);
		chickBtn.setText(listResource.get(pisition).toString());
		chickBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onChickItem.onChickItem(v,pisition+1,listResource.get(pisition));
			}
		});
		if(index==-1){
			chickBtn.setBackgroundResource(R.drawable.shaixuan_normal);
		}else {
			if (pisition==index) {
				chickBtn.setBackgroundResource(R.drawable.shaixuan_chick);
			}else {
				chickBtn.setBackgroundResource(R.drawable.shaixuan_normal);
			}
		}
		return view;
	}

	public interface OnChickItem {
		public void onChickItem(View view,int position,String text);
	}
	
	public void setItemSelect(int index){
		this.index=index-1;
	}
}
