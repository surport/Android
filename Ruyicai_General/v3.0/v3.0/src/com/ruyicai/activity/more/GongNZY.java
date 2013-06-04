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

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.util.PublicMethod;

/**
 * 
 * 帮助中心
 * @author Administrator
 *
 */
public class GongNZY extends Activity{
	// 如意助手列表各项ID
	public final static int ID_ZHUCE = 1;/* 注册登录 */
	public final static int ID_MIMA = 2;/* 忘记密码 */
	public final static int ID_CHONGZHI = 3;/* 充值*/
	public final static int ID_LINGJIANG = 4;/* 领奖提现 */
    public final static int ID_TOUZHU = 5;/* 投注 */
    public final static int ID_HEMAI = 6;//合买
    public final static int ID_ZHUIHAO = 7;//追号
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
		textView.setText("功能指引");
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
				/* 如意助手之玩法介绍 */
				if ("注册登录".equals(str)) {
					setContentView(showInfoView(ID_ZHUCE));
				}
				/* 如意助手之中奖领奖 */
				if ("忘记密码".equals(str)) {
					setContentView(showInfoView( ID_MIMA));
				}
				/* 如意助手之购彩流程 */
				if ("充值".equals(str)) {
					setContentView(showInfoView(ID_CHONGZHI));
				}
				/* 如意助手之密码找回 */
				if ("领奖提现".equals(str)) {
					setContentView(showInfoView(ID_LINGJIANG));
				}
				/* 如意助手之客服电话 */
				if ("投注".equals(str)) {
					setContentView(showInfoView(ID_TOUZHU));
				}
				if("合买".equals(str)){
					setContentView(showInfoView(ID_HEMAI));
				}if("追号".equals(str)){
					setContentView(showInfoView(ID_ZHUIHAO));
				}
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
			/* 注册登录 */
			case ID_ZHUCE:
				iFileName = "ruyihelper_registlogin.html";
				title.setText("注册登录");
				break;
			/* 忘记密码 */
			case ID_MIMA:
				iFileName = "ruyihelper_MIMA.html";
				title.setText("忘记密码");
				break;
			/* 充值 */
			case ID_CHONGZHI:
				iFileName = "ruyihelper_CHONGZHI.html";
				title.setText("充值");
				break;
			/* 领奖提现 */
			case ID_LINGJIANG:
				iFileName = "ruyihelper_LINGJIANG.html";
				title.setText("领奖提现");
				break;
			/* 投注 */
			case ID_TOUZHU:
				iFileName = "ruyihelper_TOUZHU.html";
				title.setText("投注");
				break;
		    //合买
			case ID_HEMAI :
				iFileName = "ruyihelper_HEMAI.html";
				title.setText("合买");
				break;
			//追号
			case ID_ZHUIHAO :
				iFileName = "ruyihelper_ZHUIHAO.html";
				title.setText("追号");
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

			String[] titles = {"注册登录",
					"忘记密码",
					"充值",
				    "领奖提现",
					"投注",
					"合买",
					"追号",
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
				
					convertView = mInflater.inflate(R.layout.ruyihelper_listview_icon_item,null);
					holder = new ViewHolder();
					holder.title = (TextView) convertView.findViewById(R.id.ruyihelper_icon_text);
					holder.icon=(ImageView)convertView.findViewById(R.id.ruyihelper_iicon);
					holder.icon.setBackgroundResource(R.drawable.xiangyou);
//					holder.title.setGravity(Gravity.CENTER);
					holder.title.setText(title);
					convertView.setTag(holder);
					holder = (ViewHolder) convertView.getTag();
				
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
}
