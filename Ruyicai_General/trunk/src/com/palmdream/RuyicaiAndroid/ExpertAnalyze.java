package com.palmdream.RuyicaiAndroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ExpertAnalyze extends Activity {

	private static final String[] expertAnalyzeTypeName = { "双色球分析", "福彩3D分析",
			"七乐彩分析" };
	private static final String[] expertAnalyzeMore = { ">>>>", ">>>>", ">>>>" };

	TextView typeName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.expert_analyze_main_layout);

		ListView listview = (ListView) findViewById(R.id.expert_analyze_listview_id);

		ExpertAnalyzeEfficientAdapter adapter = new ExpertAnalyzeEfficientAdapter(
				this);
		listview.setAdapter(adapter);

		// 设置点击监听
		OnItemClickListener clickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				typeName = (TextView) view
						.findViewById(R.id.expert_analyze_typename_id);
				String str = typeName.getText().toString();
				// 点击福彩3D跳转到福彩3D子列表中
				if (getString(R.string.shuangseqiufenxi).equals(str)) {
					Intent ea_intent1 = new Intent(ExpertAnalyze.this,
							NoticePrizesOfLottery.class);
					startActivity(ea_intent1);
				}
				// 点击七乐彩跳转到七乐彩子列表中
				if (getString(R.string.fucaifenxi).equals(str)) {
					Intent ea_intent2 = new Intent(ExpertAnalyze.this,
							NoticePrizesOfLottery.class);
					startActivity(ea_intent2);
				}
				// 点击双色球跳转到双色球子列表中
				if (getString(R.string.qilecaifenxi).equals(str)) {
					Intent ea_intent3 = new Intent(ExpertAnalyze.this,
							NoticePrizesOfLottery.class);
					startActivity(ea_intent3);
				}
			}

		};

		listview.setOnItemClickListener(clickListener);
	}

	public static class ExpertAnalyzeEfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private Bitmap mShuangSeQiu;
		private Bitmap mFuCai;
		private Bitmap mQiLeCai;

		public ExpertAnalyzeEfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

			mShuangSeQiu = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.shuangseqiu);
			mFuCai = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.fucai);
			mQiLeCai = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.qilecai);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return expertAnalyzeTypeName.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.expert_analyze_listview, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView
						.findViewById(R.id.expert_analyze_icon_id);
				holder.expertAnalyzeTypeNameView = (TextView) convertView
						.findViewById(R.id.expert_analyze_typename_id);
				// holder.expertAnalyzeMoreView = (TextView)
				// convertView.findViewById(R.id.expert_analyze_more_id);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == 1) {
				holder.icon.setImageBitmap(mShuangSeQiu);
				holder.expertAnalyzeTypeNameView
						.setText(expertAnalyzeTypeName[position]);
				// holder.expertAnalyzeMoreView.setText(expertAnalyzeMore[position]);
			} else if (position == 2) {
				holder.icon.setImageBitmap(mFuCai);
				holder.expertAnalyzeTypeNameView
						.setText(expertAnalyzeTypeName[position]);
				// holder.expertAnalyzeMoreView.setText(expertAnalyzeMore[position]);
			} else {
				holder.icon.setImageBitmap(mQiLeCai);
				holder.expertAnalyzeTypeNameView
						.setText(expertAnalyzeTypeName[position]);
				// holder.expertAnalyzeMoreView.setText(expertAnalyzeMore[position]);
			}

			return convertView;
		}

		static class ViewHolder {
			ImageView icon;
			TextView expertAnalyzeTypeNameView;
			// TextView expertAnalyzeMoreView;
		}
	}

}
