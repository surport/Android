/**
 * 
 */
package com.ruyicai.activity.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.util.PublicMethod;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 帮助中心
 * @author Administrator
 *
 */
public class TeSGN extends Activity{
	// 如意助手列表各项ID
	public final static int ID_JIFEN = 1;/* 积分 */
	public final static int ID_DINGYUE = 2;/*开奖消息订阅*/
	public final static int ID_DUANXIN = 3;/* 专家荐号短信*/
	public final static int ID_ZENGSONG = 4;/* 赠送彩票 */
    private List<Map<String, Object>> list;/* 列表适配器的数据源 */
    private TextView text;
    private static final String IICON = "IICON";
    private final static String TITLE = "TITLE"; /* 标题 */
    private View views ;
    private boolean isMain = true;
    Activity activity;
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(showView());
		}
	   /**
	    * 创建帮助中心界面
	    * @return
	    */
	   public View showView(){
		isMain = true;
		LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		views= (LinearLayout) inflate.inflate(R.layout.ruyihelper_listview, null);
		ListView listview = (ListView) views.findViewById(R.id.ruyihelper_listview_ruyihelper_id);
		Button btnreturn = (Button) views.findViewById(R.id.ruyizhushou_btn_return);
		TextView textView = (TextView) views.findViewById(R.id.ruyipackage_title);
		textView.setText("特色功能");
		btnreturn.setBackgroundResource(R.drawable.returnselecter);
		btnreturn.setOnClickListener(new ImageView.OnClickListener() {

			public void onClick(View v) {
			    finish();
			}

		});

		// 数据源
		list = getListForRuyiHelperAdapter();
		HelpCenterAdapter adapter = new HelpCenterAdapter(this,list);
		// 适配器		
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				text = (TextView) view.findViewById(R.id.ruyihelper_icon_text);
				String str = text.getText().toString();
				MoreActivity.iQuitFlag = 20;
				/* 积分*/
				if ("积分".equals(str)) {
					setContentView(showInfoView(ID_JIFEN));
				}
				/* 开奖信息订阅 */
				if ("开奖消息订阅".equals(str)) {
					setContentView(showInfoView( ID_DINGYUE));
				}
				/* 专家荐号短信点播*/
				if ("专家荐号短信点播".equals(str)) {
					setContentView(showInfoView(ID_DUANXIN));
				}
				/* 如意助手之密码找回 */
				if ("赠送彩票".equals(str)) {
					setContentView(showInfoView(ID_ZENGSONG));
				}
//				/* 如意助手之客服电话 */
//				if (getString(R.string.ruyihelper_kefudianhua).equals(str)) {
//					setContentView(showInfoView(ID_KEFUDIANHUA));
//				}
//				if(getString(R.string.ruyihelper_about).equals(str)){
//					setContentView(showInfoView(ID_ABOUT));
//				}if("彩票玩法".equals(str)){
////					setContentView(showInfoView(ID_ABOUT));
//				}
			}

		};
		listview.setOnItemClickListener(clickListener);
		return views;
	   }
	   /**
	    * 帮助中心子列表
	    * @param aInfoFlag
	    * @return
	    */
	   protected View showInfoView(int aInfoFlag) {
		   isMain = false;
			LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View view = (LinearLayout) inflate.inflate(R.layout.layout_ruyizhushou, null);
			Button btnreturn = (Button) view.findViewById(R.id.ruyizhushou_btn_return);
			TextView title = (TextView) view.findViewById(R.id.ruyipackage_title);
			btnreturn.setBackgroundResource(R.drawable.returnselecter);
		    btnreturn.setOnClickListener(new Button.OnClickListener() {
		    	
					public void onClick(View v) {
						setContentView(showView());
					}
				});
			WebView webView = (WebView) view.findViewById(R.id.ruyipackage_webview);
			String iFileName = null;
			switch (aInfoFlag) {
			/* 玩法介绍 */
			case ID_JIFEN:
				iFileName = "ruyihelper_JIFEN.html";
				title.setText("积分");
				break;
			/* 中奖领奖 */
			case ID_DINGYUE:
				iFileName = "ruyihelper_DINGYUE.html";
				title.setText("开奖消息订阅");
				break;
			/* 购彩流程 */
			case ID_DUANXIN:
				iFileName = "ruyihelper_DUANXIN.html";
				title.setText("专家荐号短信点播");
				break;
			/* 密码找回 */
			case ID_ZENGSONG:
				iFileName = "ruyihelper_ZENGSONG.html";
				title.setText("赠送彩票");
				break;
			}
			String url = "file:///android_asset/" + iFileName;
			webView.loadUrl(url);
			return view;
		}
		/**
		 *  获得如意助手列表适配器的数据源
		 * @return
		 */
		protected List<Map<String, Object>> getListForRuyiHelperAdapter() {

			String[] titles = {"积分",
					"开奖消息订阅",
					"专家荐号短信点播",
				    "赠送彩票",
					};
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);

			for (int i = 0; i < titles.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(TITLE, titles[i]);
				list.add(map);
			}

			return list;

		}
		
		/**
		 * 帮助中心的适配器
		 */
		public class HelpCenterAdapter extends BaseAdapter {

			private LayoutInflater mInflater; // 扩充主列表布局
			private List<Map<String, Object>> mList;

			public HelpCenterAdapter(Context context, List<Map<String, Object>> list) {
				mInflater = LayoutInflater.from(context);
				mList = list;

			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mList.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return mList.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			int index;

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				index = position;
				ViewHolder holder = null;
				String title = (String) mList.get(position).get(TITLE);
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.ruyihelper_listview_icon_item,null);
					holder = new ViewHolder();
					holder.title = (TextView) convertView.findViewById(R.id.ruyihelper_icon_text);
//					holder.title.setGravity(Gravity.CENTER);
					holder.icon=(ImageView)convertView.findViewById(R.id.ruyihelper_iicon);
					holder.icon.setBackgroundResource(R.drawable.xiangyou);
					holder.title.setText(title);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				return convertView;
			}
			class ViewHolder {
				TextView title;
				ImageView icon;
			}
		}
	    /**
	     * 重写放回建
	     */
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			switch (keyCode) {
			   case 4:
		        if(isMain){
		        	finish();
		        }else{
		        	setContentView(showView());
		        }
	           break;
			}
			return false;
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
}
