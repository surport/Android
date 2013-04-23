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
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.zc.FootballSFLottery;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.BetQueryInterface;
import com.ruyicai.net.newtransaction.WinQueryInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * 投注查询联网
 * @author Administrator
 *
 */
public class BetQueryActivity  extends Activity implements HandlerMsg{
	private BetAndGiftPojo betPojo = new BetAndGiftPojo();
	private LinearLayout usecenerLinear;
	private Button returnButton;
	private TextView	titleTextView;
	String jsonString;
	final	String  BATCHCODE="batchCode",LOTMUTI="lotMulti",ORDERTIME="orderTime",PRIZEAMT = "prizeAmt"
		,BETCODE="betCode",LOTNO="lotNo",AMOUNT="amount",LOTNAME="lotName",PLAY="play",BET_CODE = "bet_code",ISJC="isJC";
	MyHandler touzhuhandler = new MyHandler(this);
	private final int DIALOG1_KEY = 0;
	ListView queryinfolist;
	private String phonenum,sessionid,userno;
	List<Vector> betdatalist = new ArrayList<Vector>(); 
	Context context = this ;
	ProgressDialog dialog;
	String jsonobject;
	int  allPage;
	int pageindex;
	TextView pagetext;
	boolean isfirst = false;
	 Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog.dismiss();
				Toast.makeText(BetQueryActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				break;
			case 1:
				dialog.dismiss();
				encodejson((String) msg.obj);
				List list = initAdapterlist(pageindex);
        		initListView(queryinfolist,list);
				break;
			}
		}
	 };
	public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.usercenter_mainlayout);
			
			returnButton = (Button)findViewById(R.id.layout_usercenter_img_return);
			titleTextView = (TextView)findViewById(R.id.usercenter_mainlayou_text_title);
			returnButton.setBackgroundResource(R.drawable.returnselecter);
			titleTextView.setText(R.string.usercenter_bettingDetails);
			returnButton.setText(R.string.returnlastpage);
			jsonobject = this.getIntent().getStringExtra("betjson");
			encodejson(jsonobject);
			isfirst = true;
			initLinear();
			initReturn();
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
	  * 获取投注查询联网
	  * @param pageindex
	  */
	private void	getWinDataNet(final int pageindex){
		showDialog(0);
		new Thread(new Runnable() {
			public void run() {
			initPojo();
			BetAndWinAndTrackAndGiftQueryPojo betQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
			betQueryPojo.setUserno(userno);
			betQueryPojo.setSessionid(sessionid);
			betQueryPojo.setPhonenum(phonenum);
			betQueryPojo.setPageindex(String.valueOf(pageindex));
			betQueryPojo.setMaxresult("10");
			betQueryPojo.setType("bet");
			
				Message msg = new Message();
				jsonString = BetQueryInterface.getInstance().betQuery(betQueryPojo);;
				try {
					JSONObject aa = new JSONObject(jsonString);
					String errcode = aa.getString("error_code");
					Log.v("error_code","--------"+errcode);
					String message = aa.getString("message");
					if(errcode.equals("0000")){
							msg.what = 1;
							msg.obj = jsonString;
							handler.sendMessage(msg);
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
	private void initLinear(){
		usecenerLinear = (LinearLayout)findViewById(R.id.usercenterContent);
		usecenerLinear.addView(initLinearView());
	}
	private  View	initLinearView(){
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(R.layout.usercenter_listview_layout, null);
		 queryinfolist = (ListView) view.findViewById(R.id.usercenter_listview_queryinfo);
		pagetext = (TextView)view.findViewById(R.id.usercenter_text_pagetext);
		ImageButton subpagebtn = (ImageButton)view.findViewById(R.id.usercenter_btn_subpage);
		subpagebtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				isfirst = false;
				 pageindex--;
	               if(pageindex<0){
	            	   pageindex = 0;
	   				Toast.makeText(BetQueryActivity.this, R.string.usercenter_hasgonefirst, Toast.LENGTH_SHORT).show();   
	               }else{
	            	   	List list = initAdapterlist(pageindex);
		        		initListView(queryinfolist,list);
	               }
	             
//	       		pagetext.setText(initPageTextView(pageindex));
			}
		});
		ImageButton addpagebtn = (ImageButton)view.findViewById(R.id.usercenter_btn_addpage);
		addpagebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isfirst = false;
			   if(pageindex >betdatalist.size()-1){
				   pageindex = betdatalist.size()-1;
			   }
               pageindex++;
	           if(pageindex<allPage){
		        	if(pageindex>betdatalist.size()-1){
		        		getWinDataNet(pageindex);
		        	}else{
		        		List list = initAdapterlist(pageindex);
		        		initListView(queryinfolist,list);
		        	}
			   }else{
				   pageindex=allPage-1;
				Toast.makeText(BetQueryActivity.this, R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();   
			   }
//	           pagetext.setText(initPageTextView(pageindex));
			}
		});
		
//		pagetext.setText(initPageTextView(pageindex));
		if(!isfirst){
			List list = initAdapterlist(pageindex);
			initListView(queryinfolist,list);
		}
		List list = initAdapterlist(pageindex);
		initListView(queryinfolist,list);
		return view;
	}
	private String initPageTextView(int pageindex){
		StringBuffer str = new StringBuffer();
		int pageindexText = pageindex + 1;
		String zi_gong = getString(R.string.usercenter_gong);
		String zi_ye = getString(R.string.usercenter_page);
		String zi_di = getString(R.string.usercenter_di);
		str.append(zi_di).append(pageindexText).append(zi_ye).append(zi_gong).append(String.valueOf(allPage)).append(zi_ye);
		return str.toString();
	}
	private void	initListView(ListView listview,List list){
		pagetext.setText(initPageTextView(pageindex));
		WinPrizeAdapter adapter = new WinPrizeAdapter(context,list);
		listview.setAdapter(adapter);
		
	}
	public void lookDetailDialog(String detail){
		LayoutInflater factory = LayoutInflater.from(this);
		/*中奖查询的查看详情使用余额查询的布局*/
		View	view = factory.inflate(R.layout.usercenter_balancequery, null);
		final	AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.getWindow();
		TextView  title = (TextView)view.findViewById(R.id.usercenter_balancequery_title);
		title.setText(R.string.usercenter_detailsTitle);
		TextView remind = (TextView)view.findViewById(R.id.usercenter_remind_text);
		remind.setVisibility(TextView.GONE);
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
	}
	  public List<Map<String, Object>> initAdapterlist(int pageindex){
		  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		  try{
			for (int i = 0; i < betdatalist.get(pageindex).size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(BATCHCODE, ((BetQueryInfo) betdatalist.get(pageindex).get(i)).getBatchCode());
				map.put(BETCODE, ((BetQueryInfo) betdatalist.get(pageindex).get(i)).getBetCode());
				String lotno = ((BetQueryInfo) betdatalist.get(pageindex).get(i)).getLotNo();
				int isJC = 0;
				map.put(LOTNO, lotno);
				if(lotno.equals("J00001")){
					isJC = 1;
				}else{
					isJC = 0;
				}
				map.put(ISJC, isJC);
				map.put(LOTNAME, ((BetQueryInfo) betdatalist.get(pageindex).get(i)).getLotName());
				map.put(PRIZEAMT, ((BetQueryInfo) betdatalist.get(pageindex).get(i)).getPrizeAmt());
				map.put(AMOUNT, ((BetQueryInfo) betdatalist.get(pageindex).get(i)).getAmount());
				map.put(ORDERTIME, ((BetQueryInfo) betdatalist.get(pageindex).get(i)).getOrdertime());
				map.put(LOTMUTI, ((BetQueryInfo) betdatalist.get(pageindex).get(i)).getLotMulti());
				map.put(PLAY, ((BetQueryInfo) betdatalist.get(pageindex).get(i)).getPlay());
				map.put(BET_CODE, ((BetQueryInfo) betdatalist.get(pageindex).get(i)).getBet_code());
				list.add(map);
			}
			}catch (Exception e) {
				
			}
			return list;
	  }  
	
	  public void encodejson(String json) {
		  
		  try {
			  JSONObject  winprizejsonobj = new JSONObject(json);
			  allPage = Integer.parseInt(winprizejsonobj.getString("totalPage"));
			  String winprizejsonstring = winprizejsonobj.getString("result");
				
			  JSONArray winprizejson = new JSONArray(winprizejsonstring);
			  Log.v("1111111111111111",""+winprizejson);
			  Vector<BetQueryInfo> winInfos = new Vector<BetQueryInfo>(); 
			  for(int i=0;i<winprizejson.length();i++){
				try{
					BetQueryInfo betQueryinfo = new BetQueryInfo();
					betQueryinfo.setBatchCode(winprizejson.getJSONObject(i).getString(BATCHCODE));
					betQueryinfo.setBetCode(winprizejson.getJSONObject(i).getString(BETCODE));
					betQueryinfo.setOrdertime(winprizejson.getJSONObject(i).getString(ORDERTIME));
					betQueryinfo.setLotNo(winprizejson.getJSONObject(i).getString(LOTNO));
					betQueryinfo.setPrizeAmt(winprizejson.getJSONObject(i).getString(PRIZEAMT));
					betQueryinfo.setLotName(winprizejson.getJSONObject(i).getString(LOTNAME));
					betQueryinfo.setAmount(winprizejson.getJSONObject(i).getString(AMOUNT));
					betQueryinfo.setLotMulti(winprizejson.getJSONObject(i).getString(LOTMUTI));
					betQueryinfo.setPlay(winprizejson.getJSONObject(i).getString(PLAY));
					betQueryinfo.setBet_code(winprizejson.getJSONObject(i).getString(BET_CODE));
					winInfos.add(betQueryinfo);
				}catch (Exception e) {
					Log.v("22222222222","解析中奖查询json串错误");
					Log.v("22222222222","json串："+winprizejson);
				}
			 }
			 betdatalist.add(winInfos);
		  	 } catch (JSONException e) {
		  		try {
						JSONObject winprizejson = new JSONObject(json);
						Log.v("winprizejson",""+winprizejson);
		  		} catch (JSONException e1) {
		  				Log.v("NETBack",jsonobject);
		  		}
		    }
	     }
	class BetQueryInfo{
		private String amount;
		private String prizeAmt;
		private String batchCode;
		private String lotNo;
		private String betCode;
		private String ordertime;
		private String lotName;
		private String play;
		private String orderTime;
		private String lotMulti;
		private String bet_code;
		public String getBet_code() {
			return bet_code;
		}
		public void setBet_code(String betCode) {
			bet_code = betCode;
		}
		public String getPrizeAmt() {
			return prizeAmt;
		}
		public void setPrizeAmt(String prizeAmt) {
			this.prizeAmt = prizeAmt;
		}
		public String getLotName() {
			return lotName;
		}
		public void setLotName(String lotName) {
			this.lotName = lotName;
		}
		public String getPlay() {
			return play;
		}
		public void setPlay(String play) {
			this.play = play;
		}
		public String getOrderTime() {
			return orderTime;
		}
		public void setOrderTime(String orderTime) {
			this.orderTime = orderTime;
		}
		private String getOrdertime() {
			return ordertime;
		}
		private void setOrdertime(String ordertime) {
			this.ordertime = ordertime;
		}
		private String getLotMulti() {
			return lotMulti;
		}
		private void setLotMulti(String lotMulti) {
			this.lotMulti = lotMulti;
		}
		private String getAmount() {
			return amount;
		}
		private void setAmount(String amount) {
			this.amount = amount;
		}
		private String getBatchCode() {
			return batchCode;
		}
		private void setBatchCode(String batchCode) {
			this.batchCode = batchCode;
		}
		private String getLotNo() {
			return lotNo;
		}
		private void setLotNo(String lotNo) {
			this.lotNo = lotNo;
		}
		private String getBetCode() {
			return betCode;
		}
		private void setBetCode(String betCode) {
			this.betCode = betCode;
		}
	}
	/**
	 * 中奖查询的适配器
	 */
	public class WinPrizeAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater; // 扩充主列表布局
			private List<Map<String, Object>> mList;
			public WinPrizeAdapter(Context context, List<Map<String, Object>> list) {
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
				final String lotno = (String) mList.get(position).get(LOTNO);
				final int  isJC = (Integer)mList.get(position).get(ISJC);
				final String prizeqihao = (String) mList.get(position).get(BATCHCODE);
				final String amount = (String) mList.get(position).get(AMOUNT);
				final String playFashion = (String) mList.get(position).get(PLAY);
				final String fPayMoney = PublicMethod.formatMoney(amount); 
				final String lotName = (String) mList.get(position).get(LOTNAME);
				final String lotMulti = (String) mList.get(position).get(LOTMUTI);
				final String prizemoney = (String) mList.get(position).get(PRIZEAMT);
				final String formatPrizeName = PublicMethod.formatMoney(prizemoney);
				final String ordertime = (String) mList.get(position).get(ORDERTIME);
				final String betcode = (String) mList.get(position).get(BETCODE);
				final String bet_code = (String) mList.get(position).get(BET_CODE);
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.usercenter_listitem_winprize_query,null);
					holder = new ViewHolder();
					holder.lotteryname = (TextView) convertView.findViewById(R.id.usercenter_winprize_lotteryname);
					holder.prizeqihao = (TextView) convertView.findViewById(R.id.usercenter_winprize_prizeqihao);
					holder.paymoney = (TextView) convertView.findViewById(R.id.usercenter_winprize_paymoney);
					holder.prizemoney = (TextView) convertView.findViewById(R.id.usercenter_winprize_prizemoney);
					holder.buyagain = (Button)convertView.findViewById(R.id.usercenter_winprize_buyagain);
					holder.lookdetail = (Button)convertView.findViewById(R.id.usercenter_winprize_querydetail);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.prizeqihao.setText("(期号:"+prizeqihao+")");
				noBuyAgain(holder.buyagain,holder.prizeqihao,isJC);
				String payString = getString(R.string.usercenter_winprize_payMoney);//投注金额字
				String prizeString = getString(R.string.usercenter_prizeMoney);//中奖金额字
				holder.lotteryname.setText(lotName);
				
				holder.paymoney .setText(payString+fPayMoney);
				holder.prizemoney.setText(prizeString+formatPrizeName);
				holder.lookdetail.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						StringBuffer detailinfo = new StringBuffer();
						
						if(lotno.equals("J00001")){
							detailinfo.append(getString(R.string.usercenter_lotterytypename)).append(lotName).append("\n\n")
							.append(getString(R.string.usercenter_playStyle)).append(playFashion).append("\n\n")
							.append(getString(R.string.usercenter_bettingTime)).append(ordertime).append("\n\n")
							.append(getString(R.string.usercenter_winprize_payMoney)).append(fPayMoney).append("\n\n")
							.append(getString(R.string.usercenter_prizeMoney)).append(formatPrizeName).append("\n\n")
							.append(getString(R.string.usercenter_betcode)).append("\n")
							.append(betcode);
						}else{
							detailinfo.append(getString(R.string.usercenter_lotterytypename)).append(lotName).append("\n\n")
							.append(getString(R.string.usercenter_playStyle)).append(playFashion).append("\n\n")
							.append(getString(R.string.usercenter_lotteryIssue)).append(prizeqihao).append("\n\n")
							.append(getString(R.string.usercenter_bettingTime)).append(ordertime).append("\n\n")
							.append(getString(R.string.usercenter_winprize_payMoney)).append(fPayMoney).append("\n\n")
							.append(getString(R.string.usercenter_prizeMoney)).append(formatPrizeName).append("\n\n")
							.append(getString(R.string.usercenter_betcode)).append("\n")
							.append(betcode);
						}
						lookDetailDialog(""+detailinfo);
					}
				});
				holder.buyagain.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						initBetPojo(bet_code,  lotMulti, lotno, amount);
						StringBuffer buyAgainMessage = new StringBuffer();
						if(lotno.equals("J00001")){
							buyAgainMessage.append(getString(R.string.usercenter_lotterytypename)).append(lotName).append("\n\n")
							.append(getString(R.string.usercenter_winprize_payMoney)).append(fPayMoney).append("\n\n")
							.append(getString(R.string.usercenter_lotMulti)).append(lotMulti).append("\n\n")
							.append(getString(R.string.usercenter_betcode)).append("\n").append(betcode);
						}else{
							buyAgainMessage.append(getString(R.string.usercenter_lotterytypename)).append(lotName).append("\n\n")
							.append(getString(R.string.usercenter_lotteryIssue)).append(prizeqihao).append("\n\n")
							.append(getString(R.string.usercenter_winprize_payMoney)).append(fPayMoney).append("\n\n")
							.append(getString(R.string.usercenter_lotMulti)).append(lotMulti).append("\n\n")
							.append(getString(R.string.usercenter_betcode)).append("\n").append(betcode);
						}
						alert(buyAgainMessage+"");
					}
				});
				return convertView;
			}
			class ViewHolder {
				TextView lotteryname;
				TextView prizeqihao;
				TextView paymoney;
				TextView prizemoney;
				Button   lookdetail;
				Button   buyagain;
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
	private void noBuyAgain(Button a,TextView qihao,int isJC){
		switch(isJC){
		case 1:
			qihao.setVisibility(TextView.GONE);
			a.setEnabled(false);
			a.setBackgroundResource(R.drawable.buyagainunenable);
			break;
		case 0:
			qihao.setVisibility(TextView.VISIBLE);
			a.setBackgroundResource(R.drawable.usercenter_selector_buyagain);
			a.setEnabled(true);
			break;
		}
			
	}
	/**
	 * 单复式投注调用函数
	 * @param string  显示框信息
	 * @return
	 */
	private void alert(String string) {
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v= inflater.inflate(R.layout.usercenter_buyagain_layout, null);
		final AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.whatisyourselect).setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).setNegativeButton(R.string.cancle,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}

						}).create();
		dialog.show();
		TextView text =(TextView) v.findViewById(R.id.buyagain_touzhu_message);
		text.setText(string);
		Button cancel = (Button) v.findViewById(R.id.buyagain_touzhu_button_cancel);
		cancel.setBackgroundResource(R.drawable.join_info_btn_selecter);
		cancel.setText(R.string.cancel);
		Button ok = (Button) v.findViewById(R.id.buyagain_touzhu_button_ok);
		ok.setBackgroundResource(R.drawable.join_info_btn_selecter);
		ok.setText(R.string.ok);
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				touZhuNet();
			}
		});
		dialog.getWindow().setContentView(v);
	}
	private void initBetPojo(String zhuma,String lotMulti,String lotno,String amount) {
		initPojo();
		betPojo.setPhonenum(phonenum);
		betPojo.setSessionid(sessionid);
		betPojo.setUserno(userno);
		betPojo.setBet_code(zhuma);
		betPojo.setLotno(lotno);
		betPojo.setBatchnum("1");
		betPojo.setLotmulti(lotMulti);
		betPojo.setBettype("bet");
		betPojo.setAmount(amount);
	}
	/**
	 * 投注联网
	 */
	public void touZhuNet(){
		showDialog(DIALOG1_KEY); 
		Thread t = new Thread(new Runnable() {
			String str = "00";
			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betPojo);
				try {
					JSONObject obj = new JSONObject(str);		
					String message = obj.getString("message");
					String error = obj.getString("error_code");
					touzhuhandler.handleMsg(error,message);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}

		});
		t.start();
	}

	public void errorCode_0000() {
		Toast.makeText(this, R.string.betSuccess, Toast.LENGTH_SHORT).show();
	}
	public void errorCode_000000() {
	}
	public Context getContext() {
		return this;
	}
}
