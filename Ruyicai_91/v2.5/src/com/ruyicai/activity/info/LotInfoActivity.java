package com.ruyicai.activity.info;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.net.newtransaction.InformationInterface;
import com.ruyicai.util.Constants;
import com.ruyicai.util.RandomMessage;

/**
 * 彩票资讯
 * @author haojie
 *
 */
public class LotInfoActivity extends TabActivity {
	private String[] mSubTabTagArray = new String[] { "a", "b", "c", "d" };
	public static TabHost mTabHost = null;
	private LayoutInflater mInflater = null;
	private View view;
	private ProgressDialog progressdialog;
	String str = new String("");
    private List<LotInfoDomain> informationdata=null;
    private Handler handler = new Handler();
    private ListView listview;
    public static int newsinfotype=1;//* 1->彩民趣闻 * 2->专家分析* 3->足彩天地 * 4->如意活动

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   setContentView(R.layout.information);
    	mInflater = LayoutInflater.from(this);
    	mTabHost = (TabHost) getTabHost();
	//	mTabHost.setup();
        view =mInflater.inflate(R.layout.information, mTabHost.getTabContentView(),true);
		initTabWidge();
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				for(int i=0;i<mSubTabTagArray.length;i++){
					if(tabId.equals(mSubTabTagArray[i])){
						listview=null;
						informationdata=null;
						newsinfotype=i+1;
						getContent(i+1);
					}
				}
			}
		});
		mTabHost.setCurrentTab(0);
		getContent(1);
    }
    
	private TabHost.TabSpec buddySpec = null;
	private TabHost.TabSpec sessionListSpec = null;
	private TabHost.TabSpec addrListSpec = null;
	private TabHost.TabSpec settingSpec = null;

	private void initTabWidge() {
		View indicatorTab1 = mInflater.inflate(R.layout.layout_nav_iteminfo, null);
	    TextView title1 =((TextView) indicatorTab1.findViewById(R.id.layout_nav_icon_title));
	    title1.setTextSize(15);
	    title1.setText("彩民趣闻");
		indicatorTab1.setBackgroundResource(R.drawable.tab_buy_selector);
		buddySpec = mTabHost.newTabSpec(mSubTabTagArray[0]).setIndicator(indicatorTab1).setContent(R.id.informationlist1);
		mTabHost.addTab(buddySpec);
		View indicatorTab2 = mInflater.inflate(R.layout.layout_nav_iteminfo, null);
		TextView title2=((TextView) indicatorTab2.findViewById(R.id.layout_nav_icon_title));
		title2.setTextSize(15);
		title2.setText("专家推荐");
		indicatorTab2.setBackgroundResource(R.drawable.tab_buy_selector);
		sessionListSpec = mTabHost.newTabSpec(mSubTabTagArray[1]).setIndicator(indicatorTab2).setContent(R.id.informationlist2);
		mTabHost.addTab(sessionListSpec);

		View indicatorTab3 = mInflater.inflate(R.layout.layout_nav_iteminfo, null);
	    TextView title3=	((TextView) indicatorTab3.findViewById(R.id.layout_nav_icon_title));
	    title3.setTextSize(15);
	    title3.setText("足彩天地");
		indicatorTab3.setBackgroundResource(R.drawable.tab_buy_selector);
		addrListSpec = mTabHost.newTabSpec(mSubTabTagArray[2]).setIndicator(indicatorTab3).setContent(R.id.informationlist3);
		mTabHost.addTab(addrListSpec);

		View indicatorTab4 = mInflater.inflate(R.layout.layout_nav_iteminfo, null);
		TextView title4 =((TextView) indicatorTab4.findViewById(R.id.layout_nav_icon_title));
		title4.setTextSize(15);
		title4.setText("91活动");
		indicatorTab4.setBackgroundResource(R.drawable.tab_buy_selector);
		settingSpec = mTabHost.newTabSpec(mSubTabTagArray[3]).setIndicator(indicatorTab4).setContent(R.id.informationlist4);
		mTabHost.addTab(settingSpec);
	}
	
	
	/**
	 * 资讯list适配器
	 * @author Administrator
	 *
	 */
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
			final int  index = position;
			holder.icon.setImageResource(R.drawable.list_dash);
			holder.content.setText((String)mList.get(position).getTitle());
			holder.content.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				   Intent intent = new Intent(LotInfoActivity.this,LotInfoConcreteActivity.class);
				   Bundle bundle = new Bundle();
				   bundle.putInt("index", index);
				   intent.putExtras(bundle);
	               startActivity(intent);
	               TextView textview= (TextView) v;
	               textview.setTextColor(Color.RED);
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
     
    /**
     * 联网获取资讯信息
     * type 类型  
     * 1->彩民趣闻
     * 2->专家分析
     * 3->足彩天地
     * 4->如意活动
     */
    private void getContent( final int type){
    	if(type == 1){
    		//判断constants 的list 是不是空的
    		if(Constants.quwenInfoList.size()>0){
    			//已经取到了新闻
    				handler.post(new Runnable() {
					@Override
					public void run() {
						listview = (ListView)view.findViewById(R.id.infolist1);
						informationdata = Constants.quwenInfoList;
				        InfoAdapter adapter = new InfoAdapter(LotInfoActivity.this,informationdata);
				        listview.setAdapter(adapter);     	
				        listview.setDividerHeight(0);
					}
				});
    			return;
    		}
    	}else if(type == 2){
    		//判断constants 的list 是不是空的
    		if(Constants.zhuanjiaInfoList.size()>0){
    			//已经取到了新闻
    				handler.post(new Runnable() {
					@Override
					public void run() {
						listview = (ListView)view.findViewById(R.id.infolist2);
						informationdata = Constants.zhuanjiaInfoList;
				        InfoAdapter adapter = new InfoAdapter(LotInfoActivity.this,informationdata);
				        listview.setAdapter(adapter);     	
				        listview.setDividerHeight(0);
					}
				});
    			return;
    		}
    	}else if(type == 3){
    		//判断constants 的list 是不是空的
    		if(Constants.footballInfoList.size()>0){
    			//已经取到了新闻
    				handler.post(new Runnable() {
					@Override
					public void run() {
						listview = (ListView)view.findViewById(R.id.infolist3);
						informationdata = Constants.footballInfoList;
				        InfoAdapter adapter = new InfoAdapter(LotInfoActivity.this,informationdata);
				        listview.setAdapter(adapter);     	
				        listview.setDividerHeight(0);
					}
				});
    			return;
    		}
    	}else if(type == 4){
    		//判断constants 的list 是不是空的
    		if(Constants.huodongInfoList.size()>0){
    			//已经取到了新闻
    				handler.post(new Runnable() {
					@Override
					public void run() {
						listview = (ListView)view.findViewById(R.id.infolist4);
						informationdata = Constants.huodongInfoList;
				        InfoAdapter adapter = new InfoAdapter(LotInfoActivity.this,informationdata);
				        listview.setAdapter(adapter);     	
				        listview.setDividerHeight(0);
					}
				});
    			return;
    		}
    	}
    	
    	
    	LotInfoActivity.this.CreateDialog().show();
    	final int conttenttype = type;
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				String str = InformationInterface.getInstance().getInformation(String.valueOf(conttenttype)); //1彩民趣闻
				String strarry = null;
				try {
					JSONObject obj = new JSONObject(str);
					strarry = obj.getString("news");
					JSONArray news = new JSONArray(strarry);
				    
					for(int i=0;i<news.length();i++){
						JSONObject contentnews = news.getJSONObject(i);
						LotInfoDomain domain = new LotInfoDomain();
						domain.setTitle(contentnews.getString("title"));
						domain.setContent(contentnews.getString("content"));
						if(type == 1){
							Constants.quwenInfoList.add(domain);
						}else if(type == 2){
							Constants.zhuanjiaInfoList.add(domain);
						}else if(type == 3){
							Constants.footballInfoList.add(domain);
						}else if(type == 4){
							Constants.huodongInfoList.add(domain);
						}
					}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				    handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						progressdialog.dismiss();
						Toast.makeText(LotInfoActivity.this, "获取信息失败，请稍候再试", Toast.LENGTH_SHORT);	
					}
				});
				}
				
				progressdialog.dismiss();
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						switch(conttenttype){
						case 1:
						listview = (ListView)view.findViewById(R.id.infolist1);
						informationdata = Constants.quwenInfoList;
						break;
						case 2:
						listview = (ListView)view.findViewById(R.id.infolist2);
						informationdata = Constants.zhuanjiaInfoList;

							break;
						case 3:
						listview = (ListView)view.findViewById(R.id.infolist3);
						informationdata = Constants.footballInfoList;

							break;
						case 4:
						listview = (ListView)view.findViewById(R.id.infolist4);
						informationdata = Constants.huodongInfoList;
							break;
						}
				        InfoAdapter adapter = new InfoAdapter(LotInfoActivity.this,informationdata);
				        listview.setAdapter(adapter);     	
				        listview.setDividerHeight(0);
					}
				});
			//	Toast.makeText(LotInfoDetailActivity.this, strarry, Toast.LENGTH_LONG);
			}
		}).start();
    	
    }
    
	/**
	 * 网络连接提示框
	 */
	protected ProgressDialog CreateDialog() {
		    String waitMessage = new RandomMessage().getWaitingMessage();
			progressdialog = new ProgressDialog(this.getParent());
			progressdialog.setMessage(waitMessage);
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
	} 
	
	  /**
     * 重写返回
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		   case KeyEvent.KEYCODE_BACK:
	        ExitDialogFactory.createExitDialog(this);
           break;
		}
		return false;
	}
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
