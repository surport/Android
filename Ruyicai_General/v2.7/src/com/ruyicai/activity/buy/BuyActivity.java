/**
 * 
 */
package com.ruyicai.activity.buy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.dlt.Dlt;
import com.ruyicai.activity.buy.fc3d.Fc3d;
import com.ruyicai.activity.buy.jc.Jc;
import com.ruyicai.activity.buy.pl3.PL3;
import com.ruyicai.activity.buy.pl5.PL5;
import com.ruyicai.activity.buy.qlc.Qlc;
import com.ruyicai.activity.buy.qxc.QXC;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.ssq.Ssq;
import com.ruyicai.activity.buy.zc.FootballChooseNineLottery;
import com.ruyicai.activity.buy.zc.FootballGoalsLottery;
import com.ruyicai.activity.buy.zc.FootballSFLottery;
import com.ruyicai.activity.buy.zc.FootballSixSemiFinal;
import com.ruyicai.activity.join.JoinHallActivity;
import com.ruyicai.activity.more.HelpCenter;
import com.ruyicai.activity.more.LuckChoose;
import com.ruyicai.activity.usercenter.NewUserCenter;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.net.newtransaction.PrizeRankInterface;
import com.ruyicai.pojo.Lights;
import com.ruyicai.util.Constants;
import com.ruyicai.util.FlingGallery;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * 购彩大厅界面
 * 
 * @author Administrator
 * 
 */
public class BuyActivity extends Activity implements OnClickListener {
	
	
	
	private String messageflage = null;
	String messagetitle;
	String messagedetail;
	String messageerrorcode;
	private String messageidflag = null;
	private JSONObject obj;
	private Dialog dialog;
	private int SCREENMAX = 9;// 屏幕最大图标数
	private int SCREENUM = 4;// 屏幕最大数
	private int SCREEALL = 0;// 屏幕总图标数
	private int PRIZERANKSCREEN = 1;//新加中獎排行
	private int HEIGHT = 0;
	private int radioWeight = 0;
	ProgressDialog progressdialog;
	
	private int top = 20;
	private List<String> mLabelArray = new ArrayList<String>();
	private List<GalleryViewItem> itemViewArray = new ArrayList<GalleryViewItem>();
	private int[] imageViews = { R.drawable.ico_buy, R.drawable.ico_double,
			R.drawable.ico_3d, R.drawable.ico_115, R.drawable.ico_super,
			R.drawable.ico_timec, R.drawable.ico_seven, R.drawable.ico_three,
			R.drawable.icon_jc, R.drawable.icon_pl5,
			R.drawable.icon_qxc,  R.drawable.zc_sfc,  R.drawable.zc_rx9,  R.drawable.zc_6cb,  R.drawable.zc_jqc};
	private String[] imageTitle = { "合买大厅", "双色球", "福彩3D", "11选5", "大乐透",
			"时时彩", "七乐彩", "排列三", "竞彩足球", "排列五", "七星彩", "胜负彩" , "任选九" , "六场半" , "进球彩" };
	private final Class[] cla = { JoinHallActivity.class, Ssq.class,
			Fc3d.class, Dlc.class, Dlt.class, Ssc.class, Qlc.class, PL3.class,
			Jc.class, PL5.class, QXC.class, FootballSFLottery.class, FootballChooseNineLottery.class, FootballSixSemiFinal.class, FootballGoalsLottery.class };
	private int[] imgViewsId = { R.id.mainpage_hemai_sign,
			R.id.mainpage_ssq_sign, R.id.mainpage_fc3d_sign,
			R.id.mainpage_11x5_sign, R.id.mainpage_dlt_sign,
			R.id.mainpage_ssc_sign, R.id.mainpage_qlc_sign,
			R.id.mainpage_pl3_sign, R.id.mainpage_zucai_sign};
	private int[] textViewId = { R.id.mainpage_hemai_text,
			R.id.mainpage_ssq_text, R.id.mainpage_fc3d_text,
			R.id.mainpage_11x5_text, R.id.mainpage_dlt_text,
			R.id.mainpage_ssc_text, R.id.mainpage_qlc_text,
			R.id.mainpage_pl3_text, R.id.mainpage_zucai_text};
	private FlingGallery mGallery;
	private Lights lights;
	float startX = 0;
	float endX = 0;
	float newX = 0;
	float oldX = 0;
	float move = 0;
	boolean isMove = false;
	
	
	
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 4:
				showmessageDialog();
				break;
			}
		}

	};
	private LayoutInflater layoutinflater;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.buy_activity);
		HEIGHT = getWindowManager().getDefaultDisplay().getWidth();//屏幕的高度
		mGallery = (FlingGallery) findViewById(R.id.buy_activity_fling_gallery);
		initNumber();
		initLights();
		initGallery();
		initImgView();
		initRollingText();
		dialogMsg();
	}

	/**
	 * 根据不同手机分辨率，初始化屏幕图标数据
	 */
	public void initNumber() {
		SCREEALL = imageViews.length;
		if (HEIGHT == 240) {
			SCREENMAX = 6;
		} else if (HEIGHT == 320) {
			SCREENMAX = 9;
			top = 5;
			radioWeight = 23;
		} else if (HEIGHT == 480) {
			SCREENMAX = 9;
			top = 25;
			radioWeight = 40;

		}

		int num = SCREEALL / SCREENMAX;
		if (num * SCREENMAX < SCREEALL) {
			num += 1;
		}
		num += PRIZERANKSCREEN;//彩种屏幕数加上新加的非购彩页面的数量 
		for (int i = 0; i < num; i++) {
			mLabelArray.add("" + i);
		}
		SCREENUM = mLabelArray.size();
	}

	// 活动提示框
	private void showmessageDialog() {
		ShellRWSharesPreferences shellcheck = new ShellRWSharesPreferences(
				BuyActivity.this, "UserMessage");
		View view = layoutinflater.from(BuyActivity.this).inflate(
				R.layout.tanchuxinxi, null);
		TextView msg = (TextView) view.findViewById(R.id.tanchuxinxi_msg);
		msg.setText(messagedetail);
		dialog = new AlertDialog.Builder(BuyActivity.this).setView(view)
				.setTitle(messagetitle).setNeutralButton("确定", null).show();
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);

	}

	/**
	 * 初始化滑动
	 */
	public void initGallery() {
		mGallery.setIsGalleryCircular(false);
		mGallery.setPaddingWidth(10);
		mGallery.setLights(lights);
		mGallery.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_list_item_1, mLabelArray) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Log.e("itemViewArray.position",""+position);
				if(itemViewArray.size()<SCREENUM){
					Log.e("itemViewArray.size",""+itemViewArray.size());
					
					itemViewArray.add(new GalleryViewItem(getApplicationContext(), position));
				}
					return itemViewArray.get(position);
				
			}
		});
		mGallery.setPosition(1);
	}
	/**
	 * 初始化翻幕灯
	 */
	public void initLights() {
		lights = new Lights(this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.buy_activity_light_layout);
		int[] images = { R.drawable.buy_radio, R.drawable.buy_radio_b };
		lights.createViews(SCREENUM, images, layout);
		lights.isDefault(Constants.FlingGalleryDefaultPosition);
	}

	/**
	 * 初始化imageView按钮
	 */
	public void initImgView() {
		int[] buttons = { R.id.mainpage_usercenter, R.id.mainpage_luck_sign,
				R.id.mainpage_help };
		for (int j = 0; j < 3; j++) {
			Button button = (Button) findViewById(buttons[j]);
			button.setBackgroundResource(R.drawable.join_info_btn_selecter);
			button.setOnClickListener(this);
		}
	}

	/**
	 * 初始化组件,滚动数据
	 */
	public void initRollingText() {
		ViewFlipper mFlipper = ((ViewFlipper) this .findViewById(R.id.notice_other_flipper));
		String str[] = splitStr(Constants.NEWS, 23);
		for (int i = 0; i < str.length; i++) {
			mFlipper.addView(addTextByText(str[i]));
		}
		mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_in));
		mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_out));
		mFlipper.startFlipping();
	}

	/**
	 * 拆分滚动信息字符串 格式为逗号隔开
	 */
	public String[] splitStr(String str, int with) {
		String strss[] = str.split(",");
		if (strss.length == 0) {
			int indexs = str.length() / with + 1;
			String strs[] = new String[indexs];
			for (int i = 0; i < indexs; i++) {
				if (i == indexs - 1) {
					strs[i] = str.substring(i * with, str.length());
				} else {
					strs[i] = str.substring(i * with, with * (i + 1));
				}
			}
			return strs;
		}
		return strss;
	}

	public View addTextByText(String text) {
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setGravity(1);
		tv.setTextSize(15);
		tv.setTextColor(Color.BLACK);
		return tv;
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onResume() {
		super.onResume();
		Constants.MEMUTYPE = 0;
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGallery.onGalleryTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mainpage_usercenter:
			tendToUserCenter();
			break;
		case R.id.mainpage_luck_sign:
			tendToLuckCenter();
			break;
		case R.id.mainpage_help:
			tendToHelpCenter();
			break;
		}
	}

	public void tendToUserCenter() {
		Intent intent = new Intent(BuyActivity.this, NewUserCenter.class);
		startActivity(intent);
	}

	public void tendToHelpCenter() {
		Intent intent = new Intent(BuyActivity.this, HelpCenter.class);
		startActivity(intent);
	}

	public void tendToLuckCenter() {
		Intent intent = new Intent(BuyActivity.this, LuckChoose.class);
		startActivity(intent);
	}

	private void dialogMsg() {
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(BuyActivity.this, "UserMessage");
		messageflage = shellRW.getPreferencesValue("tanchumessage");
		messageidflag = shellRW.getPreferencesValue("id");
		if (!PublicConst.MESSAGE.equals("")) {
			try {
				obj = new JSONObject(PublicConst.MESSAGE);
				String id = obj.getString("id");
				messagetitle = obj.getString("title");
				messagedetail = obj.getString("message");
				if (messageidflag == null) {
					shellRW.putPreferencesValue("id", id);
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else if (!messageidflag.equals(id)) {
					shellRW.putPreferencesValue("id", id);
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				}

			} catch (JSONException e) {

			}
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
			ExitDialogFactory.createExitDialog(this);
			break;
		}
		return false;
	}
	
	private class GalleryViewItem extends TableLayout {
		/*
		 *中奖排行中的一些参数
		 */
		private  String prizeRankJSON = "";
		private final String NAME = "name";
		private final String PRIZEAMT = "prizeAmt";
		
		TabHost prizeRankTab ;
		
		String[] prizerankTitles = {getString(R.string.week),getString(R.string.month),getString(R.string.total)};


		ListView monthView,weekView,totalView;
		int[] listViewId = {R.id.prizeRank_week,R.id.prizeRank_month,R.id.prizeRank_total};
		View view ;//主View
		LayoutInflater mInflater = null;
		
		final Handler handler2 = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					Log.e("111111","1111111111");
					progressdialog.dismiss();
					prizeRankJSON = msg.obj.toString();
					initPrizeRank();
					break;
				}
			}

		};
		
		/**
		 * 初始化数据
		 * @param rankType  中奖排行类型
		 */
		private List<Map> initPrizeRankListData(String rankType){
			
			
			List<Map> list = new ArrayList<Map>();
			try{
				JSONObject aa = new JSONObject(prizeRankJSON);
				String rankStr  = aa.getString(rankType);
				JSONArray  prizeRankArray = new JSONArray(rankStr);
				
				for(int i = 0;i<prizeRankArray.length();i++ ){
					try{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(NAME,prizeRankArray.getJSONObject(i).getString("name"));
						map.put(PRIZEAMT,prizeRankArray.getJSONObject(i).getString("prizeAmt"));
						list.add(map);
					}catch(Exception e){
					}
				}
			
			}catch (Exception e) {
			}
			
			return list;
		}
		
		private void getPrizeRankData(){
			 showDialog(0);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					String prizeRankData = PrizeRankInterface.getInstance().prizeRankQuery();
					if(prizeRankData!= ""&&prizeRankData!=null){
							Log.e("laile","laile");
							msg.what = 0;
							msg.obj = prizeRankData;
							handler2.sendMessage(msg);
					};
				}
			}).start();
			
		}

		public GalleryViewItem(Context context, int position) {
			super(context);
			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = (LinearLayout) inflate.inflate( R.layout.buy_activity_btn1, null);
			if(position == 0){
				Log.v("prizeRankJSON",""+prizeRankJSON);
				if(prizeRankJSON == ""||	prizeRankJSON == null){
					getPrizeRankData();
				}else{
					initPrizeRank();
				}
			}else{
				initBtn(view, position);
			}
			this.addView(view);
		}
		/**
		 * 初始化中奖排行的一些参数（TAB）
		 * @param view
		 */
		private void initPrizeRank() {
			Log.v("333333333view",""+view);
			prizeRankTab = (TabHost)view.findViewById(R.id.buyactivity_tab_host);
			prizeRankTab.setVisibility(TabHost.VISIBLE);
			prizeRankTab.setup();
			mInflater = LayoutInflater.from(BuyActivity.this);
			for(int i=0;i<prizerankTitles.length;i++){
				addTab(i); 
			}
			prizeRankTab.setOnTabChangedListener(accountTabChangedListener);
			initPrizeList(weekView,listViewId[0],initPrizeRankListData(Constants.WEEK));
		}
		 /**
		  * 为TabHost添加TabSpec
		  * @param index
		  */
		public void addTab(int index){
			View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
			ImageView img = (ImageView) indicatorTab.findViewById(R.id.layout_nav_item);
			TextView title = (TextView) indicatorTab.findViewById(R.id.layout_nav_icon_title);
			img.setBackgroundResource(R.drawable.tab_buy_selector);
			title.setText(prizerankTitles[index]);
			TabHost.TabSpec tabSpec = prizeRankTab.newTabSpec(prizerankTitles[index]).setIndicator(indicatorTab).setContent(listViewId[index]);
			prizeRankTab.addTab(tabSpec);
		}
	
		TabHost.OnTabChangeListener accountTabChangedListener = new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {	
				for(int i=0;i<prizerankTitles.length;i++){
					if(tabId.equals(prizerankTitles[0])){
						initPrizeList(weekView,listViewId[0],initPrizeRankListData(Constants.WEEK));
					}else if(tabId.equals(prizerankTitles[1])){
						initPrizeList(monthView,listViewId[1],initPrizeRankListData(Constants.MONTH));
					}else if(tabId.equals(prizerankTitles[2])){
						initPrizeList(totalView,listViewId[2],initPrizeRankListData(Constants.TOTAL));
					}
				}
			}
		};
		/**
		 * 初始化中奖排行界面
		 * @param listview  listView
		 * @param listviewid listView的ID
		 * @param list    中奖排行的数据  
		 */
		protected void initPrizeList( ListView listview,int listviewid,List<Map> list) {
			Log.v("222222222view",""+view);
			listview = (ListView)findViewById(listviewid);
			Log.e("2222222listview2",""+listview);
			Log.e("HEIGHT",""+HEIGHT);
			switch (HEIGHT) {
			case 240:
				listview.setPadding(0, 0, 0, 100);
				break;
			case 320:
				listview.setPadding(0, 0, 0, 60);
				break;
			case 480:
				listview.setPadding(0, 0, 0, 30);
				break;
			default:
				listview.setPadding(0, 0, 0, 0);
				break;
			}
			listview.setSelector(R.color.transparent);
			listview.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent ev) {
					final int action = ev.getAction();
					newX = ev.getX();
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						endX = 0;
						isMove = false;
						startX = ev.getX();
						move = ev.getX();
						break;
					case MotionEvent.ACTION_UP:
						endX = ev.getX() + move;
						break;
					case MotionEvent.ACTION_MOVE:
						float deltaX = newX - startX;
						if (deltaX > 0) {// 向右移动
							// move = Math.max(newX, oldX);
							move += deltaX;
							ev.setLocation(move, ev.getY());
						} else if (deltaX < 0) {// 向左移动
							// move = Math.min(newX, oldX);
							move += deltaX;
							ev.setLocation(move, ev.getY());
						}
						if (Math.abs(deltaX) > 40) {
							isMove = true;
						}
						break;
					}
					if (action == MotionEvent.ACTION_UP) {
						float x = Math.abs(move - startX);
						if (x < 15 && !isMove) {
							return false;
						} else {
							return mGallery.onGalleryTouchEvent(ev);
						}
					} else {
						return mGallery.onGalleryTouchEvent(ev);
					}
				}
			});
			BuyActivityAdapter adapter = new BuyActivityAdapter(BuyActivity.this,list);
			listview.setSelected(false);
			listview.setAdapter(adapter);
			if(listview.isFocused()){
				listview.setItemsCanFocus(false);
			}else{
				listview.setItemsCanFocus(true);
			}
		}

		/**
		 * 中奖排行ListViewAdapter
		 * @author miao
		 */
		private class BuyActivityAdapter extends BaseAdapter{
			 int lastX;//触摸停止是的X坐标
			 int curX;//触摸中的X坐标
			 int totalMove = 0;
			 boolean firstDown = true;//开关
			 int duration = 150;
			
			LayoutInflater mAdapterInflater = null;
			List<Map> mList = null;
			
			public  BuyActivityAdapter(Context context, List<Map> list) {
				mAdapterInflater = LayoutInflater.from(context);
				mList = list;
			}

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
			public View getView(int position,  View convertView, ViewGroup parent) {
				
				
				final String prizeName =  (String) mList.get(position).get(NAME);
				final String prizeAmt = (String) mList.get(position).get(PRIZEAMT);
				PrizeViewHolder holder = new PrizeViewHolder();
				if (convertView == null) {
					convertView = mAdapterInflater.inflate(R.layout.prizerank_listitem,null);
					holder = new PrizeViewHolder();
					holder.prizeId = (TextView) convertView.findViewById(R.id.prizeRank_id);
					holder.prizerName= (TextView) convertView.findViewById(R.id.prizeRank_name);
					holder.prizeNumber = (TextView) convertView.findViewById(R.id.prizeRank_number);
				} else {
					holder = (PrizeViewHolder) convertView.getTag();
				}
				int rank =  position+1;
				setPrizeRankIDBg(holder.prizeId,rank);
				holder.prizeId.setText(rank+"");
				holder.prizerName.setText(prizeName);
				String prizeAmtHtml = "中奖金额："+"<font color=\"#ff0000\"><B>"+prizeAmt+"</B></font>"+"元";//以Html格式设置颜色
				holder.prizeNumber.setText(Html.fromHtml(prizeAmtHtml));
				convertView.setTag(holder);
				return convertView;
			}
			class PrizeViewHolder {
				TextView prizeId;
				TextView prizerName;
				TextView prizeNumber;
			}
			private void setPrizeRankIDBg(TextView btn,int ID){
				switch (ID) {
				case 1:
					btn.setBackgroundResource(R.drawable.prizerank_1);
					break;
				case 2:
					btn.setBackgroundResource(R.drawable.prizerank_2);
					break;

				case 3:
					btn.setBackgroundResource(R.drawable.prizerank_3);
					break;

				default:
					btn.setBackgroundResource(R.drawable.prizerank_other);
					break;
				}
				
			}
			
		}
		
		
		public void initBtn(View view, int position) {
			int length = SCREENMAX;
			int imgpostion = position -1;//设定初始化img数
			Log.v("SCREENUM",""+SCREENUM);
			if(position<SCREENUM){
				if(SCREENUM>position+1){
					length = SCREENMAX;
				}else{
					length = SCREEALL - SCREENMAX * (imgpostion);
				}
			}
			LinearLayout top1 = (LinearLayout) view .findViewById(R.id.layout1_top);
			LinearLayout top2 = (LinearLayout) view .findViewById(R.id.layout2_top);
			LinearLayout top3 = (LinearLayout) view .findViewById(R.id.layout3_top);
			if (HEIGHT == 480) {
				top1.setPadding(0, top, 0, 0);
			}
			top2.setPadding(0, top, 0, 0);
			top3.setPadding(0, top, 0, 0);
			for (int i = 0; i < length; i++) {
				final int index = i + SCREENMAX * (imgpostion);
				ImageView imgView = (ImageView) view .findViewById(imgViewsId[i]);
				imgView.setVisibility(ImageView.VISIBLE);
				imgView.setImageResource(imageViews[index]);
				imgView.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent ev) {
						// TODO Auto-generated method stub

						final int action = ev.getAction();
						newX = ev.getX();
						switch (action) {
						case MotionEvent.ACTION_DOWN:
							endX = 0;
							isMove = false;
							startX = ev.getX();
							move = ev.getX();
							break;
						case MotionEvent.ACTION_UP:
							endX = ev.getX() + move;
							break;
						case MotionEvent.ACTION_MOVE:
							float deltaX = newX - startX;
							if (deltaX > 0) {// 向右移动
								// move = Math.max(newX, oldX);
								move += deltaX;
								ev.setLocation(move, ev.getY());
							} else if (deltaX < 0) {// 向左移动
								// move = Math.min(newX, oldX);
								move += deltaX;
								ev.setLocation(move, ev.getY());
							}
							if (Math.abs(deltaX) > 40) {
								isMove = true;
							}
							break;

						}
						if (action == MotionEvent.ACTION_UP) {
							float x = Math.abs(move - startX);
							if (x < 15 && !isMove) {
								Intent intent = new Intent(BuyActivity.this, cla[index]);
								startActivity(intent);
								return false;
							} else {
								return mGallery.onGalleryTouchEvent(ev);
							}
						} else {
							return mGallery.onGalleryTouchEvent(ev);
						}
					}
				});
				TextView textTitle = (TextView) view.findViewById(textViewId[i]);
				textTitle.setVisibility(TextView.VISIBLE);
				textTitle.setText(imageTitle[index]);
			}
		}
	}
	protected Dialog onCreateDialog(int id) {
		progressdialog = new ProgressDialog(this);
      switch (id) {
          case 0: {
        	  progressdialog.setTitle(R.string.usercenter_netDialogTitle);
        	  progressdialog.setMessage(getString(R.string.usercenter_netDialogRemind));
        	  progressdialog.setIndeterminate(true);
        	  progressdialog.setCancelable(true);
              return progressdialog;
          }
      }
      return progressdialog;
  }
	


}
