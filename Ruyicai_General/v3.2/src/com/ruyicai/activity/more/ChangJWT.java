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
public class ChangJWT extends Activity{
	// 如意助手列表各项ID
	public final static int ID_WENTI1= 1;
	public final static int ID_WENTI2 = 2;
	public final static int ID_WENTI3 = 3;
	public final static int ID_WENTI4 = 4;
    public final static int ID_WENTI5 = 5;
    public final static int ID_WENTI6 = 6;
    public final static int ID_WENTI7 = 7;
    public final static int ID_WENTI8 = 8;
    public final static int ID_WENTI9 = 9;
    public final static int ID_WENTI10 = 10;
    public final static int ID_WENTI11 = 11;
    public final static int ID_WENTI12 = 12;
    public final static int ID_WENTI13 = 13;
    public final static int ID_WENTI14 = 14;
    public final static int ID_WENTI15 = 15;
    public final static int ID_WENTI16 = 16;
    public final static int ID_WENTI17 = 17;

    private List<Map<String, Object>> list;/* 列表适配器的数据源 */
    private TextView text;
    private static final String IICON = "IICON";
    private final static String TITLE = "TITLE"; /* 标题 */
    private View views ;
    private boolean isMain = true;
	private String[] titles = {"使用如意彩购彩都有哪些优势？",
			"使用客户端购买彩票安全吗？",
			"登录时忘记密码了怎么办？",
		    "为什么要填写真实姓名和身份证号？",
			"充值未及时到账怎么办？",
			"银联在线支付支持那些银行？",
			"如何更换绑定的手机号？",
			"提现后多久能到账？",
			"如何查看方案中奖详情？",	
			"各彩种的开奖时间是什么时候？",	
			"什么是昵称？",	
			"可以在如意彩注册多个账户吗？",	
			"如何修改登陆密码？",	
			"身份证号不小心填错了怎么办？",	
			"如何保护账户的资金安全？",	
			"合买都有哪些规则？",	
			"合买方案中奖后奖金如何分配？",	
			};
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
		textView.setText("常见问题");
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
				if (titles[0].equals(str)) {
					setContentView(showInfoView(ID_WENTI1));
				}else if (titles[1].equals(str)) {
					setContentView(showInfoView(ID_WENTI2));
				}else if (titles[2].equals(str)) {
					setContentView(showInfoView(ID_WENTI3));
				}else if (titles[3].equals(str)) {
					setContentView(showInfoView(ID_WENTI4));
				}else if (titles[4].equals(str)) {
					setContentView(showInfoView(ID_WENTI5));
				}else if (titles[5].equals(str)) {
					setContentView(showInfoView(ID_WENTI6));
				}else if (titles[6].equals(str)) {
					setContentView(showInfoView(ID_WENTI7));
				}else if (titles[7].equals(str)) {
					setContentView(showInfoView(ID_WENTI8));
				}else if (titles[8].equals(str)) {
					setContentView(showInfoView(ID_WENTI9));
				}else if (titles[9].equals(str)) {
					setContentView(showInfoView(ID_WENTI10));
				}else if (titles[10].equals(str)) {
					setContentView(showInfoView(ID_WENTI11));
				}else if (titles[11].equals(str)) {
					setContentView(showInfoView(ID_WENTI12));
				}else if (titles[12].equals(str)) {
					setContentView(showInfoView(ID_WENTI13));
				}else if (titles[13].equals(str)) {
					setContentView(showInfoView(ID_WENTI14));
				}else if (titles[14].equals(str)) {
					setContentView(showInfoView(ID_WENTI15));
				}else if (titles[15].equals(str)) {
					setContentView(showInfoView(ID_WENTI16));
				}else if (titles[16].equals(str)) {
					setContentView(showInfoView(ID_WENTI17));
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
			case ID_WENTI1:
				iFileName = "ruyihelper_WENTI1.html";
				title.setText("常见问题");
				break;
			case ID_WENTI2:
				iFileName = "ruyihelper_WENTI2.html";
				title.setText("常见问题");
				break;
			case ID_WENTI3:
				iFileName = "ruyihelper_WENTI3.html";
				title.setText("常见问题");
				break;
			case ID_WENTI4:
				iFileName = "ruyihelper_WENTI4.html";
				title.setText("常见问题");
				break;
			case ID_WENTI5:
				iFileName = "ruyihelper_WENTI5.html";
				title.setText("常见问题");
				break;
			case ID_WENTI6:
				iFileName = "unionpaysupportbanklist.html";
				title.setText("常见问题");
				break;
			case ID_WENTI7 :
				iFileName = "ruyihelper_WENTI6.html";
				title.setText("常见问题");
				break;
			case ID_WENTI8 :
				iFileName = "ruyihelper_WENTI7.html";
				title.setText("常见问题");
				break;
			case ID_WENTI9 :
				iFileName = "ruyihelper_WENTI8.html";
				title.setText("常见问题");
				break;
			case ID_WENTI10 :
				iFileName = "ruyihelper_WENTI9.html";
				title.setText("常见问题");
				break;
			case ID_WENTI11 :
				iFileName = "ruyihelper_WENTI10.html";
				title.setText("常见问题");
				break;
			case ID_WENTI12 :
				iFileName = "ruyihelper_WENTI11.html";
				title.setText("常见问题");
				break;
			case ID_WENTI13 :
				iFileName = "ruyihelper_WENTI12.html";
				title.setText("常见问题");
				break;
			case ID_WENTI14 :
				iFileName = "ruyihelper_WENTI13.html";
				title.setText("常见问题");
				break;
			case ID_WENTI15 :
				iFileName = "ruyihelper_WENTI14.html";
				title.setText("常见问题");
				break;
			case ID_WENTI16 :
				iFileName = "ruyihelper_WENTI15.html";
				title.setText("常见问题");
				break;
			case ID_WENTI17 :
				iFileName = "ruyihelper_WENTI16.html";
				title.setText("常见问题");
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
//					holder.title.setGravity(Gravity.CENTER);
					holder.title.setText(title);
					holder.icon.setBackgroundResource(R.drawable.xiangyou);
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
