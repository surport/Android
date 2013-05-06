package com.ruyicai.activity.buy.jc.explain.zq;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.ExplainInterface;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 推荐界面
 * @author win
 *
 */

public class RecommendActivity extends Activity {
	
	private String event;
	private String type = "news";
	private String pageindex = "0";
	private String maxresult = "10";
	private ListView jcNewsListView;
	private LayoutInflater inflater;
	private ProgressBar progressbar;
	private View view;
	private int latterPages = 0;
	private int latterIndex = 0;
	private ProgressDialog dialog;
	private JcNewsAdapter jcNewsAdapter;
	public List<JcNewsBean> jcNewsList = new ArrayList<JcNewsBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_jc_recommend_main);
		if(Constants.isDebug) {
			PublicMethod.outLog(getClass().getSimpleName(), "onCreate");
		}
		event = getIntent().getStringExtra(BuyActivityGroup.REQUEST_EVENT);
		inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.lookmorebtn, null);
		progressbar = (ProgressBar) view.findViewById(R.id.getmore_progressbar);
		getInfoFromNet();
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				view.setEnabled(false);
				addmore();
			}
		});
	}
	
	private void addmore() {
		latterIndex++;
		if (latterIndex > latterPages - 1) {
			latterIndex = latterPages - 1;
			progressbar.setVisibility(View.INVISIBLE);
			Toast.makeText(this, R.string.usercenter_hasgonelast,
					Toast.LENGTH_SHORT).show();
		} else {
			getInfoFromNet();
		}

	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			}
		}
	};
	
	private void getInfoFromNet() {
		if (latterIndex == 0) {
			showDialog(0);
		} else if (progressbar != null) {
			progressbar.setVisibility(ProgressBar.VISIBLE);
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String str = ExplainInterface.getRecommendStr(event, type, pageindex, maxresult);
				try {
					JSONObject obj = new JSONObject(str);
					String strArray = obj.getString("result");
					latterPages = Integer.parseInt(obj.getString("totalPage"));
					JSONArray news = new JSONArray(strArray);
					for (int i = 0; i < news.length(); i++) {
						JSONObject contentnews = news.getJSONObject(i);
						JcNewsBean bean = new JcNewsBean();
						bean.setTitle(contentnews.getString("title"));
						bean.setContent(contentnews.getString("content"));
						jcNewsList.add(bean);
					}
					
					handler.post(new Runnable() {

						@Override
						public void run() {
							if (dialog != null) {
								dismissDialog(0);
							}
							if (progressbar != null && view != null) {
								progressbar.setVisibility(View.INVISIBLE);
								view.setEnabled(true);
							}
							if (latterIndex == 0) {
								jcNewsAdapter = new JcNewsAdapter();
								jcNewsListView = (ListView)findViewById(R.id.buy_jc_recommend_main_listview);
								jcNewsListView.addFooterView(view);
								jcNewsListView.setAdapter(jcNewsAdapter);
							} else {
								jcNewsAdapter.notifyDataSetChanged();
							}
						}
					});
				} catch (JSONException e) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (dialog != null) {
								dismissDialog(0);
							}
							if (progressbar != null && view != null) {
								progressbar.setVisibility(View.INVISIBLE);
								view.setEnabled(true);
							}
						}
					});
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	private class JcNewsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return jcNewsList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.buy_jc_recommend_list_item, null);
				holder = new ViewHolder();
				holder.newsContent = (TextView)convertView.findViewById(R.id.jc_recommend_news_content);
				holder.newsTitle = (Button)convertView.findViewById(R.id.jc_recommend_news_title_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final ViewHolder viewHolder = holder;
			holder.newsContent.setText(jcNewsList.get(position).getContent());
			holder.newsTitle.setText(jcNewsList.get(position).getTitle());
			isOpen(jcNewsList, viewHolder, position);
			holder.newsTitle.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					jcNewsList.get(position).isOpen = !jcNewsList.get(position).isOpen;
					isOpen(jcNewsList, viewHolder, position);
				}
			});
			return convertView;
		}
		
	}
	
	private void isOpen(List<JcNewsBean> list, final ViewHolder holder, int position) {
		if (list.get(position).isOpen) {
			holder.newsContent.setVisibility(LinearLayout.VISIBLE);
			holder.newsTitle.setBackgroundResource(R.drawable.buy_jc_btn_open);
		} else {
			holder.newsContent.setVisibility(LinearLayout.GONE);
			holder.newsTitle.setBackgroundResource(R.drawable.buy_jc_btn_close);
		}
	}
	
	private class ViewHolder {
		Button newsTitle;
		TextView newsContent;
	}
	
	private class JcNewsBean {
		private String title;
		private String content;
		public boolean isOpen;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	}
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			dialog = new ProgressDialog(this);
			dialog.setMessage(getResources().getString(
					R.string.recommend_network_connection));
			dialog.setIndeterminate(true);
			return dialog;
		}
		}
		return null;
	}

}
