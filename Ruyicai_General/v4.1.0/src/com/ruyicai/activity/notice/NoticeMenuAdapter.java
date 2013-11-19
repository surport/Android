package com.ruyicai.activity.notice;

import java.util.List;

import com.palmdream.RuyicaiAndroid.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;


 public class NoticeMenuAdapter  extends BaseAdapter {
    private List<String> data=null;
    private LayoutInflater inflater;
    private Context context;
    private OnClickItem onclickitem;
    int index=0;
    

	public NoticeMenuAdapter(Context context,OnClickItem onclickitem, List<String> jc_data) {
	   this.context=context;
	   this.onclickitem=onclickitem;
	   this.data=jc_data;
	}

		@Override
		public int getCount() {
			return data.size();  
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view=inflater.inflate(R.layout.popu_item, null);
			Button btn = (Button) view.findViewById(R.id.itemBtn);
			btn.setText(data.get(position).toString());
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onclickitem.onChickItem(v, position);
				}
			});
			
			if(index==-1){
				btn.setBackgroundResource(R.drawable.normal_notice);
			}else{
				if(index==position){
					btn.setBackgroundResource(R.drawable.click_notice);
				}else{
					btn.setBackgroundResource(R.drawable.normal_notice);
				}
			}
			return view;
		}
		
		
		public interface OnClickItem {
			public void onChickItem(View view, int position);
		}

		public void setItemSelect(int index) {
			this.index = index;
		}
		
	}