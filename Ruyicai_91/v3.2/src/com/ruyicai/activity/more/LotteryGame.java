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

/**
 * 
 * 帮助中心
 * @author Administrator
 *
 */
public class LotteryGame extends Activity{
	// 如意助手列表各项ID
	public final static int ID_SHUANGSEQIU = 1;/* 玩法介绍 */
	public final static int ID_FC3D = 2;/* 购彩流程 */
	public final static int ID_QLC = 3;/* 中奖领奖 */
	public final static int ID_DLT = 4;/* 密码找回 */
    public final static int ID_PL3 = 5;/* 客服电话 */
    public final static int ID_PL5 = 6;//关于本软件
    public final static int ID_QXC = 7;
    public final static int ID_22_5 = 8;
    public final static int ID_SSC = 9;
    public final static int ID_11_5 = 10;
    public final static int ID_11DJ = 11;
    public final static int ID_ZC = 12;
    public final static int ID_JCZQ = 13;
    public final static int ID_JCLQ = 14;
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
		textView.setText("彩票玩法");
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
				if ("双色球玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_SHUANGSEQIU));
				}
				if ("福彩3D玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_FC3D));
				}
				if ("七乐彩玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_QLC));
				}
				if ("大乐透玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_DLT));
				}
				if ("排列三玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_PL3));
				}
				if ("排列五玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_PL5));
				}if ("七星彩玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_QXC));
				}if ("22选5玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_22_5));
				}if ("时时彩玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_SSC));
				}if ("11选5玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_11_5));
				}if ("十一运夺金玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_11DJ));
				}if ("足彩玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_ZC));
				}if ("竞彩足球玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_JCZQ));
				}if ("竞彩篮球玩法介绍".equals(str)) {
					setContentView(showInfoView(ID_JCLQ));
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
			/* 玩法介绍 */
			case ID_SHUANGSEQIU:
				iFileName = "ruyihelper_SHUANGSEQIU.html";
				title.setText("双色球玩法介绍");
				break;
			/* 中奖领奖 */
			case ID_FC3D:
				iFileName = "ruyihelper_FC3D.html";
				title.setText("福彩3D玩法介绍");
				break;
			/* 购彩流程 */
			case ID_QLC:
				iFileName = "ruyihelper_QLC.html";
				title.setText("七乐彩玩法介绍");
				break;
			/* 密码找回 */
			case ID_DLT:
				iFileName = "ruyihelper_DLT.html";
				title.setText("大乐透玩法介绍");
				break;
			/* 客服电话 */
			case ID_PL3:
				iFileName = "ruyihelper_PL3.html";
				title.setText("排列3玩法介绍");
				break;
			case ID_PL5 :
				iFileName = "ruyihelper_PL5.html";
				title.setText("排列五玩法介绍");
				break;
			case ID_QXC :
				iFileName = "ruyihelper_QXC.html";
				title.setText("七星彩玩法介绍");
				break;
			case ID_22_5 :
				iFileName = "ruyihelper_22_5.html";
				title.setText("22选5玩法介绍");
				break;
			case ID_SSC :
				iFileName = "ruyihelper_SSC.html";
				title.setText("时时彩玩法介绍");
				break;
			case ID_11_5 :
				iFileName = "ruyihelper_11_5.html";
				title.setText("11选5玩法介绍");
				break;
			case ID_11DJ :
				iFileName = "ruyihelper_11DJ.html";
				title.setText("十一运夺金玩法介绍");
				break;
			case ID_ZC :
				iFileName = "ruyihelper_ZC.html";
				title.setText("足彩玩法介绍");
				break;
			case ID_JCZQ :
				iFileName = "ruyihelper_JCZQ.html";
				title.setText("竞彩足球玩法介绍");
				break;
			case ID_JCLQ :
				iFileName = "ruyihelper_JCLQ.html";
				title.setText("竞彩篮球玩法介绍");
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

			String[] titles = {"双色球玩法介绍",
					"福彩3D玩法介绍",
					"七乐彩玩法介绍",
				    "大乐透玩法介绍",
					"排列三玩法介绍",
					"排列五玩法介绍",
					"七星彩玩法介绍",
					"22选5玩法介绍",
					"时时彩玩法介绍",
					"11选5玩法介绍",
					"十一运夺金玩法介绍",
					"足彩玩法介绍",
					"竞彩足球玩法介绍",
					"竞彩篮球玩法介绍"
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
