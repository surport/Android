package com.ruyicai.activity.usercenter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.more.FeedBack;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.FeedBackListInterface;
/**
 * 用户反馈
 * @author Administrator
 *
 */
public class FeedbackListActivity extends Activity{
	
	Button back,feedback;
	ListView feedbackList;
    List<Map<String,Object>> feedList = new ArrayList<Map<String,Object>>();
	final String CREATETIME = "createTime";
	final String REPLY = "reply";
	final String 	CONTENT = "content";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_feedbacklist);
		feedbackList = (ListView)findViewById(R.id.usercenter_feedback_list);
		initBtn();
		try {
			JSONArray feedBackArray = new JSONArray(this.getIntent().getStringExtra("feedBackArray"));
			initListViewAfterNet(feedBackArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		back = (Button)findViewById(R.id.usercenter_feedbacklist_img_return);
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		feedback = (Button)findViewById(R.id.usercenter_feedback_submitbtn);
		feedback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(FeedbackListActivity.this, FeedBack.class);
				startActivity(intent1);
			}
		});
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
}
