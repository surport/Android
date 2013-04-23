/**
 * 
 */
package com.ruyicai.activity.buy;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.notice.NoticeBlueBallActivity;
import com.ruyicai.activity.notice.NoticeInfoActivity;
import com.ruyicai.activity.notice.NoticeRedBallActivity;
import com.ruyicai.util.PublicMethod;

/**
 * @author Administrator
 */
public class BuyActivityGroup extends ActivityGroup {
	protected TabHost mTabHost = null;
	protected LayoutInflater mInflater = null;
	private String[] titles ;
	private String[] topTitles ;
	private Class[] allId ;
	protected TabHost.TabSpec firstSpec = null;
	public TextView title;//标题
	protected TextView issue;//期号
	protected TextView time;//截止时间
	private Button imgRetrun;//返回购彩大厅按钮
	private RelativeLayout relativeLayout;
    
    TabWidget tabWidget = null;
	  
	Field mBottomLeftStrip;  
	Field mBottomRightStrip;  

	

    @Override     
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.buy_main_group);
		mTabHost = (TabHost) findViewById(R.id.tab_host);
		tabWidget = mTabHost.getTabWidget();  
		mTabHost.setup(getLocalActivityManager());
		mInflater = LayoutInflater.from(this);
		initView();
		//监听tab切换事件
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {	
				for(int i=0;i<titles.length;i++){
					if(tabId.equals(titles[i])){
						title.setText(topTitles[i]);
					}
				}
			}
		});
	 }    
    /**
     * 初始化数据
     * 
     */
    public void initInfo(String[] titles,String[] topTitles,Class[] allId ){
    	this.titles = titles;
    	this.topTitles = topTitles;
    	this.allId = allId;
    }
	/**
	 * 初始化组件
	 */
	private void initView(){
		relativeLayout =(RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		issue = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		imgRetrun = (Button) findViewById(R.id.layout_main_img_return);
		imgRetrun.setBackgroundResource(R.drawable.returnselecter);
	    //ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});		
	}
	/**
	 * 是否隐藏期号
	 */
	public void isIssue(boolean isVisble){
		if(isVisble){
			relativeLayout.setVisibility(RelativeLayout.INVISIBLE);
		}else{
			relativeLayout.setVisibility(RelativeLayout.GONE);
		}
	}
	/**
	 * 赋值给当前期
	 * @param type彩种编号
	 */
	public void setIssue(String type){
		// 获取期号和截止时间
		JSONObject ssqLotnoInfo = PublicMethod.getCurrentLotnoBatchCode(type);
		if (ssqLotnoInfo != null) {
			// 成功获取到了期号信息
			try {
				String issueStr = ssqLotnoInfo.getString("batchCode");
				String timeStr = ssqLotnoInfo.getString("endTime");
				issue.setText("第" + issueStr + "期");
				time.setText("截止时间：" + timeStr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// 没有获取到期号信息,重新联网获取期号
		    PublicMethod.getIssue(type,issue,time,new Handler());
		}
	}
	/**
	 * 添加标签
	 * @param index 
	 * @param id
	 */
	public void addTab(int index){
		View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
		ImageView img = (ImageView) indicatorTab.findViewById(R.id.layout_nav_item);
		TextView title = (TextView) indicatorTab.findViewById(R.id.layout_nav_icon_title);
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		title.setText(titles[index]);
		firstSpec = mTabHost.newTabSpec(titles[index]).setIndicator(indicatorTab).setContent(new Intent(BuyActivityGroup.this, allId[index]));
		mTabHost.addTab(firstSpec);
	}
	/**
	 * 设置当前页
	 * @param index
	 */
	public void setTab(int index){
		mTabHost.setCurrentTab(index);
		title.setText(topTitles[index]);
	}
	/**
	 * 初始化开奖列表
	 * @param titles   彩种列表名   e.g. {"大乐透开奖公告","大乐透开奖公告","大乐透开奖公告"};
	 * @param topTitles  彩种组列表名 e.g. {"红球走势","蓝球走势","开奖号码"};
	 * @param allId   类ID  e.g.{NoticeRedBallActivity.class,NoticeBlueBallActivity.class,NoticeInfoActivity.class}
	 */
    public void init(String titles[],String topTitles[],Class allId[]){
    	this.titles = titles;
    	this.topTitles = topTitles;
    	this.allId = allId;
    	for(int i=0;i<titles.length;i++){
    		addTab(i);
    	}
    }  
    public void removeTabs(){
    	mTabHost.clearAllTabs();
    }
}

