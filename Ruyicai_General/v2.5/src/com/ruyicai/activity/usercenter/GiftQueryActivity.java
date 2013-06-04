/**
 * 
 */
package com.ruyicai.activity.usercenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.net.newtransaction.GiftQueryInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * 增送查询
 * @author miao
 */
public class GiftQueryActivity extends Activity {

	private LayoutInflater mInflater = null;
	private LinearLayout usecenerLinear,usecenerLineargifted;
	private Button returnButton;
	private TextView	titleTextView;
	String jsonString;
	final	String  AMOUNT="amount",BETCODE="betCode",BATCHCODE="batchCode"
			,LOTNO="lotNo",ORDERTIME="orderTime",LOTMULTI="lotMulti";
	final  String GIFTEDUSERNO="toMobileId",GIFTUSERNO = "giftMobileId",USERNO = "userno";
	private final int DIALOG1_KEY = 0;
	private String phonenum,sessionid,userno;
	int[] linearId = {R.id.usercentergiftquery_gift,R.id.usercentergiftquery_gifted};
	List<Vector> giftdatalist = new ArrayList<Vector>(); 
	List<Vector> gifteddatalist = new ArrayList<Vector>(); 
	ProgressDialog dialog;
	String jsonobject;
	TabHost mTabHost;
	private String[] titles = {"赠彩的彩票","收到的彩票"};
	int  allPage,allPagegifted;
	int pageindex=1,pageindexgifted=1;
	int giftmaxpage,giftedmaxpage;
	boolean isfirst = true,isGifedfirst = true;
	ListView queryinfolist;
	TextView pagetextGift;
	TextView pagetext;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog.dismiss();
				Toast.makeText(GiftQueryActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				break;
			case 1:
				dialog.dismiss();
				isfirst = false;
				encodegiftedjson((String) msg.obj,giftdatalist,0);
				initList(pageindex,giftdatalist);
				break;
			case 2:
				isGifedfirst = false;
				dialog.dismiss();
				encodegiftedjson((String) msg.obj,gifteddatalist,1);
				if(gifteddatalist.size()!=0){
					initLinear(usecenerLineargifted,R.id.usercentergiftquery_gifted,initLinearViewgifted());
				}
//				initList(pageindexgifted,gifteddatalist);
				break;
			}
		}
	 };
	public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.usercenter_giftquery_layout);
			returnButton = (Button)findViewById(R.id.usercenter_giftquery_img_return);
			initReturn();
			jsonobject = this.getIntent().getStringExtra("giftjson");
			encodegiftedjson(jsonobject,giftdatalist,0);
			mTabHost = (TabHost) findViewById(R.id.usercenter_tab_host);
			mTabHost.setup();
			mInflater = LayoutInflater.from(this);
			for(int i=0;i<titles.length;i++){
				addTab(i); 
			}
			mTabHost.setOnTabChangedListener(giftTabChangedListener);
			if(giftdatalist.size()!=0){
				initLinear(usecenerLinear,R.id.usercentergiftquery_gift,initLinearView());
			}
	}
	/**
	 * Tab切换事件
	 */
	TabHost.OnTabChangeListener giftTabChangedListener = new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {	
			for(int i=0;i<titles.length;i++){
				if(tabId.equals(titles[0])){
					if(giftdatalist.size()!=0){
						initLinear(usecenerLinear,R.id.usercentergiftquery_gift,initLinearView());
					}
				}else{
					if(isGifedfirst){
						getWinDataNet(1,1);
						break;
					}else{
						if(gifteddatalist.size()!=0){
							initLinear(usecenerLineargifted,R.id.usercentergiftquery_gifted,initLinearViewgifted());
						}
					}
				}
			}
		}
	};
	/**
	 * 为TabHost添加TabSpec
	 * @param index
	 */
	public void addTab(int index){
		View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
		ImageView img = (ImageView) indicatorTab.findViewById(R.id.layout_nav_item);
		TextView title = (TextView) indicatorTab.findViewById(R.id.layout_nav_icon_title);
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		title.setText(titles[index]);
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titles[index]).setIndicator(indicatorTab).setContent(linearId[index]);
		mTabHost.addTab(tabSpec);
	}
	protected  void initReturn(){
		returnButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					finish();
			}
		});
	}
	 private void initPojo(){
			ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this, "addInfo");
			phonenum = shellRW.getUserLoginInfo("phonenum");
			sessionid = shellRW.getUserLoginInfo("sessionid");
			userno = shellRW.getUserLoginInfo("userno");
	}
	 /**
	  * 赠彩查询联网
	  * @param pageindexgift   页数
	  * @param type    类型  0代表赠送 1代表收到
	  */
	 private void getWinDataNet(final int pageindexgift,final int type) {
		 showDialog(0);
			new Thread(new Runnable() {
				public void run() {
				initPojo();
				BetAndWinAndTrackAndGiftQueryPojo winQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
				winQueryPojo.setUserno(userno);
				winQueryPojo.setSessionid(sessionid);
				winQueryPojo.setPhonenum(phonenum);
				winQueryPojo.setPageindex(String.valueOf(pageindexgift));
				winQueryPojo.setMaxresult("10");
				switch (type) {
				case 0:
					winQueryPojo.setType("gift");
					break;
				case 1:
					winQueryPojo.setType("gifted");
					break;
				}
				Message msg = new Message();
				jsonString = GiftQueryInterface.getInstance().giftQuery(winQueryPojo);
					try {
						JSONObject aa = new JSONObject(jsonString);
						String errcode = aa.getString("error_code");
						String message = aa.getString("message");
						if(errcode.equals("0000")){
								switch (type) {
								case 0:
									msg.what = 1;
									msg.obj = jsonString;
									handler.sendMessage(msg);
									break;
								case 1:
									msg.what = 2;
									msg.obj = jsonString;
									handler.sendMessage(msg);
									break;
								}
						}else{
							msg.what = 0;
							msg.obj = message;
							handler.sendMessage(msg);					
						}
					} catch (Exception e) {
					}
				}
			}).start();
	}
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
	private View initLinearViewgifted() {
		LayoutInflater inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(R.layout.usercenter_listview_layout, null);
		queryinfolist = (ListView) view.findViewById(R.id.usercenter_listview_queryinfo);
		pagetextGift = (TextView)view.findViewById(R.id.usercenter_text_pagetext);
		ImageButton subpagebtn = (ImageButton)view.findViewById(R.id.usercenter_btn_subpage);
		subpagebtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				pageindexgifted--;
	               if(pageindexgifted<1){
	            	   pageindexgifted = 1;
	   				   Toast.makeText(GiftQueryActivity.this, R.string.usercenter_hasgonefirst, Toast.LENGTH_SHORT).show();   
	               }else{
	            	   	List list = initAdapterlist(pageindexgifted,gifteddatalist);
		        		initListView(list);
	               }
	            pagetextGift.setText(initPageTextView(pageindexgifted,allPagegifted));
			}
		});
		ImageButton addpagebtn = (ImageButton)view.findViewById(R.id.usercenter_btn_addpage);
		addpagebtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(pageindexgifted > gifteddatalist.size()){
				  pageindexgifted = gifteddatalist.size();
				}
				pageindexgifted++;
	           if(pageindexgifted<allPagegifted+1){
		        	if(pageindexgifted>gifteddatalist.size()){
		        		getWinDataNet(pageindexgifted,1);
		        	}else{
		        		List list = initAdapterlist(pageindexgifted,gifteddatalist);
		        		initListView(list);
		        	}
			   }else{
				   pageindexgifted=allPagegifted;
				   Toast.makeText(GiftQueryActivity.this, R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();   
			   }
	           pagetextGift.setText(initPageTextView(pageindexgifted,allPagegifted));
			}
		});
		
		pagetextGift.setText(initPageTextView(pageindexgifted,allPagegifted));
		List list = initAdapterlist(pageindexgifted,gifteddatalist);
		initListView(list);
		return view;
	}
	

	private  View	initLinearView(){
		LayoutInflater inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(R.layout.usercenter_listview_layout, null);
		queryinfolist = (ListView) view.findViewById(R.id.usercenter_listview_queryinfo);
		pagetext = (TextView)view.findViewById(R.id.usercenter_text_pagetext);
		ImageButton subpagebtn = (ImageButton)view.findViewById(R.id.usercenter_btn_subpage);
		subpagebtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				 pageindex--;
	               if(pageindex<1){
	            	   pageindex = 1;
	   				Toast.makeText(GiftQueryActivity.this, R.string.usercenter_hasgonefirst, Toast.LENGTH_SHORT).show();   
	               }else{
	            		List list = initAdapterlist(pageindex,giftdatalist);
		        		initListView(list);
	               }
	       		pagetext.setText(initPageTextView(pageindex,allPage));
			}
		});
		ImageButton addpagebtn = (ImageButton)view.findViewById(R.id.usercenter_btn_addpage);
		addpagebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			   if(pageindex > giftdatalist.size()){
			      pageindex = giftdatalist.size();
			   }
               pageindex++;
	           if(pageindex<allPage+1){
		        	if(pageindex>giftdatalist.size()){
		        		getWinDataNet(pageindex,0);
		        	}else{
		        		List list = initAdapterlist(pageindex,giftdatalist);
		        		initListView(list);
		        	}
			   }else{
				   pageindex=allPage;
				Toast.makeText(GiftQueryActivity.this, R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();   
			   }
	           pagetext.setText(initPageTextView(pageindex,allPage));
			}
		});
		pagetext.setText(initPageTextView(pageindex,allPage));
		List list = initAdapterlist(pageindex,giftdatalist);
		initListView(list);
		return view;
	}
	/**
	 * 初始化列表
	 */
	public void initList(int pageindex,List giftdatalist){
		List list = initAdapterlist(pageindex,giftdatalist);
		initListView(list);
	}
	private String initPageTextView(int pageindex,int allpage){
		StringBuffer str = new StringBuffer();
		String zi_gong = getString(R.string.usercenter_gong);
		String zi_ye = getString(R.string.usercenter_page);
		String zi_di = getString(R.string.usercenter_di);
		str.append(zi_di).append(pageindex+"").append(zi_ye).append(zi_gong).append(String.valueOf(allpage)).append(zi_ye);
		return str.toString();
	}
	private void	initListView(List list){
//		pagetextGift.setText(initPageTextView(pageindexgifted,allPagegifted));
//		pagetext.setText(initPageTextView(pageindex,allPage));
		GiftQueryAdapter adapter = new GiftQueryAdapter(this,list);
		queryinfolist.setAdapter(adapter);
	}
	
	public AlertDialog lookDetailDialog(String detail){
		LayoutInflater factory = LayoutInflater.from(this);
		/*赠送查询的查看详情使用余额查询的布局*/
		View	view = factory.inflate(R.layout.usercenter_balancequery, null);
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		TextView  title = (TextView)view.findViewById(R.id.usercenter_balancequery_title);
		title.setText(R.string.usercenter_detailsTitle);
		TextView	detailTextView = (TextView)view.findViewById(R.id.balanceinfo);
		detailTextView.setText(detail);
		Button cancleLook = (Button)view.findViewById(R.id.usercenter_balancequery_back);
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(view);
		return dialog;
	}
	  public List<Map<String, Object>> initAdapterlist(int pageindex,List<Vector> giftdatalist){
		  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		  	int listid=pageindex-1;
			for (int i = 0; i < giftdatalist.get(listid).size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(LOTMULTI, ((GIftQueryInfo) giftdatalist.get(listid).get(i)).getLotMulti());
				map.put(BETCODE, ((GIftQueryInfo) giftdatalist.get(listid).get(i)).getBetCode());
				map.put(LOTNO, ((GIftQueryInfo) giftdatalist.get(listid).get(i)).getLotNo());
				map.put(AMOUNT, ((GIftQueryInfo) giftdatalist.get(listid).get(i)).getAmount());
				map.put(ORDERTIME, ((GIftQueryInfo) giftdatalist.get(listid).get(i)).getOrderTime());
				map.put(BATCHCODE, ((GIftQueryInfo) giftdatalist.get(listid).get(i)).getBatchCode());
				map.put(USERNO, ((GIftQueryInfo) giftdatalist.get(listid).get(i)).getUserNo());
				list.add(map);
			}
			return list;
	  }  
	
	  public void encodegiftedjson(String json,List<Vector> list,int type) {
		  try {
			  JSONObject  winprizejsonobj = new JSONObject(json);
			  if(type == 0){
				  allPage = Integer.parseInt(winprizejsonobj.getString("totalPage"));
			  }else{
				  allPagegifted = Integer.parseInt(winprizejsonobj.getString("totalPage"));
			  }
			  String winprizejsonstring = winprizejsonobj.getString("result");
				
			  JSONArray winprizejson = new JSONArray(winprizejsonstring);
			  Vector<GIftQueryInfo> winInfos = new Vector<GIftQueryInfo>(); 
			  for(int i=0;i<winprizejson.length();i++){
				  try{
					GIftQueryInfo winPrizeQueryinfo = new GIftQueryInfo();
					winPrizeQueryinfo.setBatchCode(winprizejson.getJSONObject(i).getString(BATCHCODE));
					winPrizeQueryinfo.setBetCode(winprizejson.getJSONObject(i).getString(BETCODE));
					winPrizeQueryinfo.setOrderTime(winprizejson.getJSONObject(i).getString(ORDERTIME));
					winPrizeQueryinfo.setLotNo(winprizejson.getJSONObject(i).getString(LOTNO));
					winPrizeQueryinfo.setAmount(winprizejson.getJSONObject(i).getString(AMOUNT));
					winPrizeQueryinfo.setLotMulti(winprizejson.getJSONObject(i).getString(LOTMULTI));
					if(type == 0){
						winPrizeQueryinfo.setUserNo(winprizejson.getJSONObject(i).getString(GIFTEDUSERNO));
					}else{
						winPrizeQueryinfo.setUserNo(winprizejson.getJSONObject(i).getString(GIFTUSERNO));
					}
					winInfos.add(winPrizeQueryinfo);
				}catch (Exception e) {
				}
			}
			list.add(winInfos);
		  	} catch (JSONException e) {
		  		try {
						JSONObject winprizejson = new JSONObject(json);
		  		} catch (JSONException e1) {
		  		}
		}
	  }
	class GIftQueryInfo{
		private String amount;
		private String orderTime;
		private String lotNo;
		private String lotMulti;
		private String userNo;
		private String batchCode;
		private String betCode;
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getOrderTime() {
			return orderTime;
		}
		public void setOrderTime(String orderTime) {
			this.orderTime = orderTime;
		}
		public String getLotMulti() {
			return lotMulti;
		}
		public void setLotMulti(String lotMulti) {
			this.lotMulti = lotMulti;
		}
		public String getUserNo() {
			return userNo;
		}
		public void setUserNo(String userNo) {
			this.userNo = userNo;
		}
		public String getBatchCode() {
			return batchCode;
		}
		public void setBatchCode(String batchCode) {
			this.batchCode = batchCode;
		}
		public String getLotNo() {
			return lotNo;
		}
		public void setLotNo(String lotNo) {
			this.lotNo = lotNo;
		}
		public String getBetCode() {
			return betCode;
		}
		public void setBetCode(String betCode) {
			this.betCode = betCode;
		}
	}
	/**
	 * 中奖查询的适配器
	 */
	public class GiftQueryAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater; // 扩充主列表布局
			private List<Map<String, Object>> mList;
			public GiftQueryAdapter(Context context, List<Map<String, Object>> list) {
				mInflater = LayoutInflater.from(context);
				mList = list;
			}
			public int getCount() {
				return mList.size();
			}
			public Object getItem(int position) {
				return mList.get(position);
			}
			public long getItemId(int position) {
				return position;
			}
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				String lotteryname = (String) mList.get(position).get(LOTNO);
				final String formatLottname = PublicMethod.toLotno(lotteryname);
				final String lotteryqihao = (String) mList.get(position).get(BATCHCODE);
				final String giftedUserNo = (String) mList.get(position).get(USERNO);
				final String orderTime = (String) mList.get(position).get(ORDERTIME);
				final String amount = (String) mList.get(position).get(AMOUNT);
				final String FormatAmount = PublicMethod.toYuan(amount);
				final String betcode = (String) mList.get(position).get(BETCODE);
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.usercenter_giftquery_listitem,null);
					holder = new ViewHolder();
					holder.lotteryname = (TextView) convertView.findViewById(R.id.usercenter_giftquery_leftone);
					holder.paymoney = (TextView) convertView.findViewById(R.id.usercenter_giftquery_amount);
					holder.giftphoneNum = (TextView) convertView.findViewById(R.id.usercenter_giftquery_lefttwo);
					holder.giftDate = (TextView) convertView.findViewById(R.id.usercenter_giftquery_leftthree);
					holder.lookdetail = (Button)convertView.findViewById(R.id.usercenter_giftquery_lookdetail);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.giftphoneNum.setText(getString(R.string.usercenter_giftPoneNum)+giftedUserNo);
				holder.lotteryname.setText(formatLottname);
				holder.paymoney.setText(getString(R.string.usercenter_giftAmount)+FormatAmount+getString(R.string.unit));
				holder.giftDate.setText(getString(R.string.time)+orderTime);
				holder.lookdetail.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						StringBuffer detailinfo = new StringBuffer();
						detailinfo.append(getString(R.string.usercenter_giftPoneNum)).append(giftedUserNo).append("\n\n")
							.append(getString(R.string.usercenter_giftTime)).append(orderTime).append("\n\n")
							.append(getString(R.string.usercenter_giftAmount)).append(FormatAmount).append("\n\n")
							.append(getString(R.string.usercenter_lotteryIssue)).append(lotteryqihao).append("\n\n")
							.append(getString(R.string.usercenter_lotterytypename)).append(formatLottname).append("\n\n")
							.append(getString(R.string.usercenter_betcode)).append("\n")
							.append(betcode);
						lookDetailDialog(""+detailinfo);
					}
				});
				convertView.setTag(holder);
				return convertView;
			}
			class ViewHolder {
				TextView lotteryname;//彩种名
				TextView giftphoneNum;//被赠送人姓名
				TextView paymoney;//付款金额
				TextView giftDate;//赠送日期
				Button   lookdetail;//查看详情按钮
			}
	}
	protected Dialog onCreateDialog(int id) {
		 dialog = new ProgressDialog(this);
       switch (id) {
           case DIALOG1_KEY: {
        	   dialog.setTitle(R.string.usercenter_netDialogTitle);
               dialog.setMessage(getString(R.string.usercenter_netDialogRemind));
               dialog.setIndeterminate(true);
               dialog.setCancelable(true);
               return dialog;
           }
       }
       return dialog;
   }
}
