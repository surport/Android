package com.ruyicai.activity.usercenter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.info.LotInfoActivity;
import com.ruyicai.activity.info.LotInfoConcreteActivity;
import com.ruyicai.activity.info.LotInfoDomain;
import com.ruyicai.activity.more.FeedBack;
import com.ruyicai.activity.usercenter.UserScoreActivity.ScroeQueryAdapter;
import com.ruyicai.net.newtransaction.LetterQueryInterface;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;
/**
 * 用户反馈
 * @author Administrator
 *
 */
@SuppressWarnings({ "unused", "unused", "unused" })
public class FeedbackListActivity extends Activity{
	
	Button back,feedback;
	ListView feedbackList;
    List<Map<String,Object>> feedList = new ArrayList<Map<String,Object>>();
	final String CREATETIME = "createTime";
	final String REPLY = "reply";
	final String 	CONTENT = "content";
	private int[] linearId = {R.id.message,R.id.latter};
	private String[] titles = {"我的留言","站内信"};
	private LinearLayout message,latter;
	TabHost mTabHost;
	private LayoutInflater mInflater = null;
	View tabSpecLinearView;//子列表的ListView
	RWSharedPreferences shellRW;
	String userno;
    ListView tabSpecListView;//子列表的ListView
	String contentjson  ;
	String title;
	InfoAdapter adapter;
    ProgressBar progressbar;
	View view;
	int  latterPages=0;
	int  latterIndex=0;
	ProgressDialog dialog;
	public  List<LotInfoDomain> latterlist = new ArrayList<LotInfoDomain>();//如意活动
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mymessage);
		initBtn();			
		mTabHost = (TabHost) findViewById(R.id.usercenter_tab_host);
		mTabHost.setup();
		mInflater = LayoutInflater.from(this);
		for(int i=0;i<titles.length;i++){
			addTab(i); 
		}
		mTabHost.setOnTabChangedListener(scroeTabChangedListener);
		initLinear(message,linearId[0],initmessage());
        shellRW = new RWSharedPreferences(this, "addInfo");
		userno = shellRW.getStringValue("userno");		
	}
	
	
	/**
	 * TabHost切换监听器
	 */
	TabHost.OnTabChangeListener scroeTabChangedListener = new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {	
				if(tabId.equals(titles[0])){
//				initLinear(scroedetail, linearId[0], view);
				}else if(tabId.equals(titles[1])){
					if(latterlist.size()>0){
			    	initLinear(latter, linearId[1], initlatterview());	
					}else{
						getInfoNet(userno,latterIndex);
					 }
				 }
				
		}
	};
	
	/**
	 * 初始化LinearLayout,为TabHost下的LinearLayout添加View
	 * @param linear  要初始化的LinearLayout
	 * @param linearid LinearLayout对应的Id
	 * @param view    要添加的View
	 */
	private void initLinear(LinearLayout linear,int linearid,View view) {
		linear = (LinearLayout)findViewById(linearid);
		linear.removeAllViews();
		linear.addView(view);
	}
	
	private View  initlatterview (){
		tabSpecLinearView = (LinearLayout) mInflater.inflate(R.layout.usercenter_listview_layout, null);
		tabSpecListView = (ListView) tabSpecLinearView.findViewById(R.id.usercenter_listview_queryinfo);	
	    adapter = new InfoAdapter(this,latterlist);
		view = mInflater.inflate(R.layout.lookmorebtn, null);
	    progressbar=(ProgressBar)view.findViewById(R.id.getmore_progressbar);
	    tabSpecListView.addFooterView(view);
	    tabSpecListView.setAdapter(adapter);	
        view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				view.setEnabled(false);
				// TODO Auto-generated method stub
	            addmore();		
			}
		});
		return tabSpecLinearView;
	}
	public void addTab(int index){
		View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
		ImageView img = (ImageView) indicatorTab.findViewById(R.id.layout_nav_item);
		TextView title = (TextView) indicatorTab.findViewById(R.id.layout_nav_icon_title);
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		title.setText(titles[index]);
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titles[index]).setIndicator(indicatorTab).setContent(linearId[index]);
		mTabHost.addTab(tabSpec);
	}
	
    private   View initmessage(){
    	LayoutInflater inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tabSpecLinearView = (RelativeLayout) inflate.inflate(R.layout.usercenter_feedbacklist, null);
		feedbackList = (ListView) tabSpecLinearView.findViewById(R.id.usercenter_feedback_list);
		try {
			JSONArray feedBackArray = new JSONArray(this.getIntent().getStringExtra("feedBackArray"));
			initListViewAfterNet(feedBackArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		return tabSpecLinearView;  	
	}
	 private void initListViewAfterNet(JSONArray feedarray) {
			// TODO Auto-generated method stub
			for(int i=0;i<feedarray.length();i++){
				Map<String,Object> map = new HashMap<String, Object>();
				JSONObject item;
				try {
					item = feedarray.getJSONObject(i);
					map.put("createTime",item.getString(CREATETIME));
					map.put("reply",item.getString(REPLY));
					map.put("content",item.getString(CONTENT));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				feedList.add(map);
			}
			FeedbackListAdapter listAdapter = new FeedbackListAdapter(this,feedList);
			feedbackList.setAdapter(listAdapter);
		}
	private void initBtn(){

		feedback = (Button)findViewById(R.id.usercenter_feedback_submitbtn);
		feedback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(FeedbackListActivity.this, FeedBack.class);
				startActivity(intent1);
			}
		});
	}
	
	
	Handler handler=new Handler(){
	@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			}
		}
	};
	
	/**
	 * 点击向后按钮调用的方法
	 */
	private  void addmore(){
		
		 latterIndex++;
         if(latterIndex>latterPages-1){
        	 latterIndex=latterPages-1;
        	 progressbar.setVisibility(view.INVISIBLE);
//        	 tabSpecListView.removeFooterView(view);
			 Toast.makeText(FeedbackListActivity.this, R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();  
		   }else{  	    
	         getInfoNet(userno, latterIndex);
		   }
      
	}
	
	private void getInfoNet(final String userno1,final int pageIndex ) {
		if(latterIndex==0){
			showDialog(0);
		}else if(progressbar!=null){
		   progressbar.setVisibility(ProgressBar.VISIBLE);
		}
    	new Thread(new Runnable() {		
			@Override
			public void run() {
				String str = LetterQueryInterface.getInstance().letter(userno1, "10", String.valueOf(pageIndex)); //1彩民趣闻
				String strarry = null;
				try {
					JSONObject obj = new JSONObject(str);					
					strarry = obj.getString("result");
					latterPages = Integer.parseInt(obj.getString("totalPage"));
					JSONArray news = new JSONArray(strarry);				    
					for(int i=0;i<news.length();i++){
						JSONObject contentnews = news.getJSONObject(i);
						LotInfoDomain domain = new LotInfoDomain();
						domain.setTitle(contentnews.getString("title"));
						domain.setContent(contentnews.getString("content"));
						latterlist.add(domain);	
					}
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							if(dialog!=null){
								dismissDialog(0);
							}
							// TODO Auto-generated method stub
							
							if(progressbar!=null&&view!=null){
								progressbar.setVisibility(ProgressBar.GONE);
								view.setEnabled(true);
							}
							if(latterIndex==0){
					    	initLinear(latter, linearId[1], initlatterview());	
							}else{
								adapter.notifyDataSetChanged();
				          	}
						}
					});
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
                       handler.post(new Runnable() {
						
						@Override
						public void run() {
							if(dialog!=null){
								dismissDialog(0);
							}
							// TODO Auto-generated method stub
							if(progressbar!=null&&view!=null){
							progressbar.setVisibility(ProgressBar.GONE);
							view.setEnabled(true);
							}
						}
					});
					e.printStackTrace();	
				}
				
			}
		}).start();
	}
	
	
	public  class  InfoAdapter extends BaseAdapter{
    	private LayoutInflater mInflater; // 扩充主列表布局
		private List<LotInfoDomain> mList;
    	
        public InfoAdapter(Context context,List<LotInfoDomain> informationdata){
        	mInflater = LayoutInflater.from(context);
			mList = informationdata;
        }
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			final int index = position;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.informationitem, null);
				holder = new ViewHolder();
				holder.icon = (ImageView)convertView.findViewById(R.id.informationitemlable);
				holder.content = (TextView)convertView.findViewById(R.id.informationcontent);
				holder.layout = (RelativeLayout)convertView.findViewById(R.id.informationitemlayout);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();	
			}
			if(position%2==0){
				holder.layout.setBackgroundResource(R.drawable.zx_list_bg_white);	
			}else{
				holder.layout.setBackgroundResource(R.drawable.zx_list_bg_gray);
			}
			holder.icon.setImageResource(R.drawable.list_dash);
			holder.content.setText(Html.fromHtml((String)mList.get(position).getTitle()));
			holder.content.setTextColor(mList.get(position).getTextcolor());
			holder.content.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					TextView textview= (TextView) v;
					mList.get(index).setTextcolor(Color.RED);
			        textview.setTextColor(mList.get(index).getTextcolor());  
					contentjson = mList.get(index).getContent();
					title = mList.get(index).getTitle();
					turnContentActivity();	
					}
			});
			return convertView;
		}
		class ViewHolder {
			RelativeLayout layout;
			ImageView icon;
			TextView content;
		}
    	
    }
	
	private void turnContentActivity() {
		Intent intent = new Intent(FeedbackListActivity.this,MyLetterActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("content", contentjson);
		bundle.putString("title", title);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	private class FeedbackListAdapter extends BaseAdapter{
		
		private	LayoutInflater inflater; 
		private List<Map<String, Object>> mlist;
		
		public FeedbackListAdapter(Context context,List<Map<String, Object>> list ){
			inflater = LayoutInflater.from(context);
			this.mlist = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			String   createTime  = (String) mlist.get(position).get(CREATETIME);
			String  reply  = (String) mlist.get(position).get(REPLY);
			String  content = (String)mlist.get(position).get(CONTENT);
			if(convertView == null){
				convertView = inflater.inflate(R.layout.usercenter_feedback_listitem,null);
				holder = new ViewHolder();
				holder.time = (TextView)convertView.findViewById(R.id.feedback_time);
				holder.feedcontent = (TextView)convertView.findViewById(R.id.feededmessage);
				holder.reply = (TextView)convertView.findViewById(R.id.servicer_reply);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			String formatTime = "我于<font color=\"red\">"+createTime+"</font>留言道：";
			holder.time.setText(Html.fromHtml(formatTime));
			holder.feedcontent.setText(formatContent(content));
			if(reply.equals("null")||reply.equals("")){
				reply = "您的留言正在处理……";
			}
			holder.reply.setText(reply);
			convertView.setTag(holder);
			return convertView;
		}
		
		class ViewHolder{
			TextView time;
			TextView feedcontent;
			TextView reply;
		}
	}
	private String formatContent(String content){
		String formatcontent = "";
		formatcontent = content.split("\\|")[0];
		return formatcontent;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);//BY贺思明 2012-7-24
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);//BY贺思明 2012-7-24
	}
	
	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			dialog = new ProgressDialog(this);
			dialog.setMessage("网络连接中...");
			dialog.setIndeterminate(true);
			return dialog;
		}
		}
		return null;
	}
}
