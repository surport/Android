/**
 * 
 */
package com.ruyicai.activity.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.interfaces.ReturnPage;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;

/**
 * 更多界面
 * @author Administrator
 *
 */
public class MoreActivity extends Activity implements ReturnPage,HandlerMsg{
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private TextView text;
	private static final String IICON = "IICON";
	private final static String TITLE = "TITLE"; /* 标题 */
	private LuckChoose luckChoose ;//幸运选号
	private HelpCenter helpCenter;//帮助中心
	private SystemSet  systemSet;//系统设置
	private FeedBack  feedBack ;//用户反馈
//	private CompanyInfo  companyInfo = new CompanyInfo(this);//公司简介
	private Context context;
	private ProgressDialog progressdialog;
	private RelativeLayout relativeLayout;
	public static int iQuitFlag = 0; // 代表退出
	MyHandler handler = new MyHandler(this);//自定义handler
	String textStr;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		initView();
	}
	/**
	 * 初始化子列表对象
	 */
	private void initView(){
//		luckChoose = new LuckChoose(this);//幸运选号
//		helpCenter = new HelpCenter(this);//帮助中心
		systemSet = new SystemSet(this);//系统设置
//		feedBack = new FeedBack(this);//用户反馈
	}
	
	/**
	 *  “更多”选项列表
	 */
	public  void showMoreListView() {
		iQuitFlag = 0;//当前主界面
		setContentView(R.layout.ruyihelper_listview);
		relativeLayout = (RelativeLayout) findViewById(R.id.ruyihelper_listview_relative);
		relativeLayout.setVisibility(RelativeLayout.GONE);
		// 数据源
		list = getListForMoreAdapter();

		ListView listview = (ListView) findViewById(R.id.ruyihelper_listview_ruyihelper_id);

		// 适配器
		SimpleAdapter adapter = new SimpleAdapter(this, list,R.layout.ruyihelper_listview_icon_item, new String[] {
						TITLE, IICON }, new int[] { R.id.ruyihelper_icon_text,R.id.ruyihelper_iicon });

		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);

		/* 列表的点击后的背景 */
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				text = (TextView) view.findViewById(R.id.ruyihelper_icon_text);
				textStr = text.getText().toString();
				relativeLayout.setVisibility(RelativeLayout.VISIBLE);
				iQuitFlag = 10;//当前主界面下一级
				onClickListener(textStr);
			}

		};
		listview.setOnItemClickListener(clickListener);

	}
	/**
	 * 列表点击实现方法
	 * @param str
	 */
	public void onClickListener(String str){
		/* 幸运选号 */
		if (getString(R.string.xingyunxuanhao).equals(str)) {
			switchView(luckChoose.showView());
		}
		/* 帮助中心 */
		if (getString(R.string.bangzhuzhongxin).equals(str)) {
			switchView(helpCenter.showView());
		}
		/* 系统设置 */
		if (getString(R.string.xitongshezhi).equals(str)) {
			switchView(systemSet.showView());
		}
		/* 用户反馈 */
		if (getString(R.string.yonghufankui).equals(str)) {
	        switchView(feedBack.showView());
		}
		/* 公司介绍 */
		if (getString(R.string.gongsijianjie).equals(str)) {
//            switchView(companyInfo.showView());
		}
	}

	/**
	 *  获得“更多”列表适配器的数据源
	 * @return
	 */
	protected List<Map<String, Object>> getListForMoreAdapter() {

		String[] titles = {
				getString(R.string.xingyunxuanhao),
				getString(R.string.yonghuzhongxin),
				getString(R.string.bangzhuzhongxin),
				getString(R.string.xitongshezhi),
				getString(R.string.yonghufankui),
				getString(R.string.gongsijianjie) };
		int it = R.drawable.xiangyou;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TITLE, titles[i]);
			map.put(IICON, it);

			list.add(map);

		}

		return list;
	}
    /**
     * 切换view
     * 
     */
	public void switchView(View view){
		setContentView(view);
	}

	/**
	 *  网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			return progressdialog;
		}
		}
		return null;
	}
	/**
	 * 获得当前界面context
	 */
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
    /**
     * 返回到当前界面
     */
	public void returnMain() {
		// TODO Auto-generated method stub
		showMoreListView();
	}
    /**
     *  取消网络连接框
     */
	public void dismissDialog() {
		// TODO Auto-generated method stub
		progressdialog.dismiss();
	}
	 /**
     *  显示网络连接框
     */
	public void showDialog() {
		// TODO Auto-generated method stub
		showDialog(0);
	}
    /**
     * 返回码处理方法
     */
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("Constants.MEMUTYPE", Constants.MEMUTYPE+"");
		if(Constants.MEMUTYPE==0){
			showMoreListView();
		}else if(Constants.MEMUTYPE==1){
//			switchView(userCenter.showView());	
		}else if(Constants.MEMUTYPE==2){
		    switchView(feedBack.showView());
		}else if(Constants.MEMUTYPE==3){
			switchView(helpCenter.showView());
		}else if(Constants.MEMUTYPE==4){
//		switchView(companyInfo.showView());
		}else if(Constants.MEMUTYPE==5){
		switchView(luckChoose.showView());
		}
		
	}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	/* (non-Javadoc)
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_000000()
	 */
	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 从上一个activity返回当前activity执行的方法
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
	Log.e("MoreActivity===", "resultCode=="+resultCode);
	Log.e("MoreActivity===", "RESULT_OK=="+RESULT_OK);
       switch(resultCode){
       case RESULT_OK:
//           userCenter.isUserCenterDetail();
    	   break;
       }
	}
    /**
     * 重写放回建
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e("MoreActivity===", "keyCode=="+keyCode);
		// TODO Auto-generated method stub

		switch (keyCode) {
		   case 4:
	    	   switch(iQuitFlag){
	    	   case 0://当前更多主界面
	    		   ExitDialogFactory.createExitDialog(this);
	    		   break;
	     	   case 10://当前更多主界面下一级
	     		  showMoreListView();//返回主界面
	     		   break;
	     	   case 20://当前帮助中心下一级
	     		  switchView(helpCenter.showView());
	     		  break;
	     	   case 30://当前用户中心下一级
//	     		  switchView(userCenter.showView());
	    		   break;
	    	   
	    	   }
			  break;
		}
		return false;
	}
}
