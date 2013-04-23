/**
 * 
 */
package com.ruyicai.activity.usercenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.BetQueryInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 投注查询联网
 * @author Administrator 
 *
 */
public class BetQueryActivity  extends Activity implements HandlerMsg{
	private BetAndGiftPojo betPojo = new BetAndGiftPojo();
	private LinearLayout usecenerLinear;
	private Button returnButton;
	private Button kindButton;
	private TextView	titleTextView;
	private LinearLayout kind;//按彩种查询
	private Spinner betkindspinner;
	private String lotno="";
	private boolean isbetkindall=false;//下拉框选择查询全部彩种，默认为fasle,点击一次变为true;
	private String [] allcountries={"全部彩种","双色球","福彩3D","11选5","大乐透","时时彩","七乐彩","排列三","竞彩足球","排列5","七星彩","11运夺金","竞彩篮球","22选5","胜负彩","任选九","六场半","进球彩"};
	private int allpages=0,ssqpages=0,dpages=0,xuanpages=0,dltpages=0,sscpages=0,qlcpages=0,plspages=0,jczqpages=0,plwpages=0,qxcpages=0,djpages=0,jclqpages=0,twentypages=0,sfcpages=0,rxjpages=0,lcbpages=0,jqcpages=0;
	private int allindex=0,ssqindex=0,dindex=0,xuanindex=0,dltindex=0,sscindex=0,qlcindex=0,plsindex=0,jczqindex=0,plwindex=0,qxcindex=0,djindex=0,jclqindex=0,twentyindex=0,sfcindex=0,rxjindex=0,lcbindex=0,jqcindxe=0;
	String jsonString;
	final	String  BATCHCODE="batchCode",LOTMUTI="lotMulti",ORDERTIME="orderTime",PRIZEAMT = "prizeAmt" ,prizeState="prizeState",WINCODE="winCode"
		,BETCODE="betCode",LOTNO="lotNo",AMOUNT="amount",LOTNAME="lotName",PLAY="play",BET_CODE = "bet_code";
	final String ISREPEATBUY = "isRepeatBuy";
	MyHandler touzhuhandler = new MyHandler(this);
	private final int DIALOG1_KEY = 0;
	ListView queryinfolist;
	private String phonenum,sessionid,userno;
	List<BetQueryInfo> betdatalist = new ArrayList<BetQueryInfo>(); 
	List<BetQueryInfo> ssqdatalist = new ArrayList<BetQueryInfo>(); 
	List<BetQueryInfo> ddatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> qlcdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> xuandatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> dltdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> sscdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> plsdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> jcdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> plwdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> qxcdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> djdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> jclqdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> twentydatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> sfcdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> rxjdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> lcbdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> jqcdatalist = new ArrayList<BetQueryInfo>();

	private int typekind =0;
	Context context = this ;
	ProgressDialog dialog;
	String jsonobject;
	WinPrizeAdapter adapter;
	View view;
	ProgressBar progressbar;
	boolean isfirst = false;
	 Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if(dialog!=null){
					dialog.dismiss();
				}
				Toast.makeText(BetQueryActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				break;
			case 1:	
				encodejson((String) msg.obj);
				if(getNewPage()==0){
					if(dialog!=null){
					dialog.dismiss();
					}	
					selecttypelist(typekind);
				}else{
					adapter.notifyDataSetChanged();
				}
				
				break;
			case 2:
				if(dialog!=null){
					dialog.dismiss();
				}
				Toast.makeText(BetQueryActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				selecttypelist(typekind);
			}
		}
	 };
	 private void setNewPage(int page){
			switch(typekind){
			case 0:
			    allindex =page ;
				break;
			case 1:
				ssqindex =page ;
				break;
			case 2:
			    dindex =page;
				break;
			case 3:
				xuanindex =page ;
				break;
			case 4:
			    dltindex =page;
				break;
			case 5:
				sscindex =page;
				break;
			case 6:
				qlcindex =page;
				break;
			case 7:
			    plsindex =page;
				break;
			case 8:
				jczqindex =page;
				break;
			case 9:
				plwindex =page;
				break;
			case 10:
			    qxcindex =page;
				break;
			case 11:
			    djindex =page;
				break;
			case 12:
			   jclqindex =page;
				break;
			case 13:
				twentyindex=page;
				break;
			case 14:
				sfcindex =page;
				break;
			case 15:
				rxjindex=page;
				break;
			case 16:
			    lcbindex=page;
			    break;
			case 17:
				jqcindxe=page;
				break;
			}
		}
	 private int getNewPage(){
			int page=0;
			switch(typekind){
			case 0:
				page = allindex ;
				break;
			case 1:
				page = ssqindex ;
				break;
			case 2:
				page = dindex ;
				break;
			case 3:
				page = xuanindex ;
				break;
			case 4:
				page = dltindex ;
				break;
			case 5:
				page = sscindex;
				break;
			case 6:
				page = qlcindex;
				break;
			case 7:
				page = plsindex;
				break;
			case 8:
				page = jczqindex;
				break;
			case 9:
				page = plwindex;
				break;
			case 10:
				page = qxcindex;
				break;
			case 11:
				page = djindex;
				break;
			case 12:
				page = jclqindex;
				break;
			case 13:
				page =twentyindex;
				break;
			case 14:
				page = sfcindex;
				break;
			case 15:
				page=  rxjindex;
				break;
			case 16:
				page = lcbindex;
				break;
			case 17:
				page =jqcindxe;
				break;
			}
			return page;
		}
		private void setAllPage(int page){
		    
			switch(typekind){
			case 0:
				allpages = page;
				break;
			case 1:
				ssqpages = page;
				break;
			case 2:
				dpages = page;
				break;
			case 3:
				xuanpages = page;
				break;
			case 4:
				dltpages = page;
				break;
			case 5:
				sscpages = page;
				break;
			case 6:
				qlcpages = page;
				break;
			case 7:
				plspages = page;
				break;
			case 8:
				jczqpages = page;
				break;
			case 9:
				plwpages = page;
				break;
			case 10:
				qxcpages = page;
				break;
			case 11:
				djpages = page;
				break;
			case 12:
				jclqpages = page;
				break;
			case 13:
				twentypages=page;
				break;
			case 14:
				sfcpages =page;
				break;
			case 15:
				rxjpages=page;
				break;
			case 16:
				lcbpages =page;
				break;
			case 17:
				jqcpages = page;
				break;
			}
		}
		private int getAllPage(){
			int page=0;
			switch(typekind){
			case 0:
				page = allpages;
				break;
			case 1:
				page =ssqpages;
				break;
			case 2:
				page =dpages;
				break;
			case 3:
				page =xuanpages ;
				break;
			case 4:
				page =dltpages ;
				break;
			case 5:
				page =sscpages ;
				break;
			case 6:
				page =qlcpages ;
				break;
			case 7:
				page =plspages ;
				break;
			case 8:
				page =jczqpages ;
				break;
			case 9:
				page =plwpages;
				break;
			case 10:
				page =qxcpages ;
				break;
			case 11:
				page =djpages;
				break;
			case 12:
				page =jclqpages ;
				break;
			case 13:
				page = twentypages;
			    break;
			case 14:
				page = sfcpages;
			    break;
			case 15:
				page = rxjpages;
				break;
			case 16:
				page = lcbpages;
				break;
			case 17:
				page = jqcpages;
				break;
			}
			return page;
		}
	public void selecttypelist(int type){
		switch(type){
		case 0:
			initListView(queryinfolist, betdatalist);
		break;
		case 1:
			initListView(queryinfolist, ssqdatalist);
			break;
		case 2:
			initListView(queryinfolist, ddatalist);
			break;
		case 3:
			initListView(queryinfolist, xuandatalist);
			break;
		case 4:
			initListView(queryinfolist, dltdatalist);
			break;
		case 5:
			initListView(queryinfolist, sscdatalist);
			break;
		case 6:
			initListView(queryinfolist, qlcdatalist);
			break;
		case 7:
			initListView(queryinfolist, plsdatalist);
			break;
		case 8:
			initListView(queryinfolist, jcdatalist);
			break;
		case 9:
			initListView(queryinfolist, plwdatalist);
			break;
		case 10:
			initListView(queryinfolist, qxcdatalist);
			break;
		case 11:
			initListView(queryinfolist, djdatalist);
			break;
		case 12:
			initListView(queryinfolist, jclqdatalist);
			break;
		case 13:
			initListView(queryinfolist, twentydatalist);
			break;
		case 14:
			initListView(queryinfolist, sfcdatalist);
			break;
		case 15:
			initListView(queryinfolist, rxjdatalist);
			break;
		case 16:
			initListView(queryinfolist, lcbdatalist);
			break;
		case 17:
			initListView(queryinfolist, jqcdatalist);
			break;
		
		}
		
	}
	
	
	public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.usercenter_mainlayoutold);
			returnButton = (Button)findViewById(R.id.layout_usercenter_img_return);
			titleTextView = (TextView)findViewById(R.id.usercenter_mainlayou_text_title);
			returnButton.setBackgroundResource(R.drawable.returnselecter);
			titleTextView.setText(R.string.usercenter_bettingDetails);
			returnButton.setText(R.string.returnlastpage);
			kind = (LinearLayout)findViewById(R.id.betkind);
			kind.setVisibility(View.VISIBLE);
			betkindspinner=(Spinner)findViewById(R.id.bet_kind_spinner);
			jsonobject = this.getIntent().getStringExtra("betjson");
			encodejson(jsonobject);
			isfirst = true;
			initLinear();
			initspinner();
			initReturn();
	}
	
  //按彩种查询投注记录
	private void initspinner(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allcountries);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		betkindspinner.setAdapter(adapter);
		betkindspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
                int position = betkindspinner.getSelectedItemPosition();
                typekind=position;
                if(position==0){
                lotno = "";
                if(isbetkindall){
                	if(view!=null){
                	}
                	selecttypelist(position);
                }
                }
                if(position==1){
                	isbetkindall = true;
                	lotno =Constants.LOTNO_SSQ;
                	if(ssqdatalist.size()>0){
                	selecttypelist(position);
                	}else{
                	getWinDataNet(0);
                	}
                }else if(position==2){
                	isbetkindall = true;
                	lotno =Constants.LOTNO_FC3D;
                	if(ddatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==3){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_11_5;
                  	if(xuandatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==4){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_DLT;
                  	if(dltdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==5){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_SSC;
                  	if(sscdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==6){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_QLC;
                  	if(qlcdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==7){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_PL3;
                  	if(plsdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==8){
                	isbetkindall = true;
                	lotno = "JC_Z";
                  	if(jcdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==9){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_PL5;
                  	if(plwdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==10){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_QXC;
                  	if(qxcdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==11){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_eleven;
                  	if(djdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==12){
                	isbetkindall = true;
                	lotno = "JC_L";
                  	if(jclqdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==13){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_22_5;
                  	if(jclqdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==14){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_SFC;
                  	if(sfcdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==15){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_RX9;
                  	if(rxjdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==16){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_LCB;
                  	if(lcbdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }else if(position==17){
                	isbetkindall = true;
                	lotno = Constants.LOTNO_JQC;
                  	if(jqcdatalist.size()>0){
                    	selecttypelist(position);
                    	}else{
                    	getWinDataNet(0);
                    }
                }
                
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	protected  void initReturn(){
		returnButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					finish();
			}
		});
	}
	 private void initPojo(){
			RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
			phonenum = shellRW.getStringValue("phonenum");
			sessionid = shellRW.getStringValue("sessionid");
			userno = shellRW.getStringValue("userno");
	}
	 /**
	  * 获取投注查询联网
	  * @param pageindex
	  */
	Handler thandler = new Handler();
	private void netting(final int pageindex){
		initPojo();
		progressbar.setVisibility(ProgressBar.VISIBLE);	
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BetAndWinAndTrackAndGiftQueryPojo betQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
				betQueryPojo.setUserno(userno);
				betQueryPojo.setSessionid(sessionid);
				betQueryPojo.setPhonenum(phonenum);
				betQueryPojo.setPageindex(String.valueOf(pageindex));
				betQueryPojo.setLotno(lotno);
				betQueryPojo.setMaxresult("10");
				betQueryPojo.setType("bet");
				
					Message msg = handler.obtainMessage();
					jsonString = BetQueryInterface.getInstance().betQuery(betQueryPojo);;
					thandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							progressbar.setVisibility(view.INVISIBLE);
							view.setEnabled(true);
						}
					});
					try {
						JSONObject aa = new JSONObject(jsonString);
						String errcode = aa.getString("error_code");
						String message = aa.getString("message");
						if(errcode.equals("0000")){
								msg.what = 1;
								msg.obj = jsonString;
								setNewPage(pageindex);
								handler.sendMessage(msg);
								
						}if(errcode.equals("0047")){
								msg = handler.obtainMessage();
							    msg.what = 2;
							    msg.obj = message;
							    handler.sendMessage(msg); 
						}else{
								msg = handler.obtainMessage();
								msg.what = 0;
								msg.obj = message;
								handler.sendMessage(msg);
						}
					} catch (Exception e) {
					}
			}
		}).start();
	}
	private void getWinDataNet(final int pageindex){
		showDialog(0);
		netting(pageindex);
			
	}
	private void initLinear(){
		usecenerLinear = (LinearLayout)findViewById(R.id.usercenterContent);
		usecenerLinear.addView(initLinearView());
		
	}
	private  View	initLinearView(){
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewlist = (LinearLayout) inflate.inflate(R.layout.usercenter_listview_layout, null);
	    queryinfolist = (ListView) viewlist.findViewById(R.id.usercenter_listview_queryinfo);
	    LayoutInflater mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    view = mInflater.inflate(R.layout.lookmorebtn,null);
	    progressbar=(ProgressBar)view.findViewById(R.id.getmore_progressbar);
	    queryinfolist.addFooterView(view);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				view.setEnabled(false);
	            addmore();
				
			}
		});
	    initListView(queryinfolist, betdatalist);
		return viewlist;
	}

	
	private void addmore(){
		int pageIndex = getNewPage();
		int allpagenum = getAllPage();
		isfirst = false;
		pageIndex++;
        if(pageIndex<allpagenum){	
            netting(pageIndex);
		   }else{
			    progressbar.setVisibility(view.INVISIBLE);
//				queryinfolist.removeFooterView(view);
				pageIndex=allpagenum-1;
				view.setEnabled(true);
			Toast.makeText(BetQueryActivity.this, R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();   
		   }
        
		
	}
	private void	initListView(ListView listview,List list){
	    adapter = new WinPrizeAdapter(context,list);
     	// 数据源
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
   
	  public void encodejson(String json) {
		  
		  try {
			  JSONObject  winprizejsonobj = new JSONObject(json);
			  int  allPage = Integer.parseInt(winprizejsonobj.getString("totalPage"));
			  String winprizejsonstring = winprizejsonobj.getString("result");
			  setAllPage(allPage);	
			  JSONArray winprizejson = new JSONArray(winprizejsonstring);
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
					betQueryinfo.setPrizeState(winprizejson.getJSONObject(i).getString(prizeState));
					betQueryinfo.setWin_code(winprizejson.getJSONObject(i).getString(WINCODE));
					betQueryinfo.setRepeatBuy(winprizejson.getJSONObject(i).getBoolean(ISREPEATBUY));
					if(typekind==0){
						betdatalist.add(betQueryinfo);	
					}else if(typekind==1){
						ssqdatalist.add(betQueryinfo);
					}else if(typekind==2){
						ddatalist.add(betQueryinfo);
					}else if(typekind==3){
						xuandatalist.add(betQueryinfo);
					}else if(typekind==4){
						dltdatalist.add(betQueryinfo);
					}else if(typekind==5){
						sscdatalist.add(betQueryinfo);
					}else if(typekind==6){
						qlcdatalist.add(betQueryinfo);
					}else if(typekind==7){
						plsdatalist.add(betQueryinfo);
					}else if(typekind==8){
						jcdatalist.add(betQueryinfo);
					}else if(typekind==9){
						plwdatalist.add(betQueryinfo);
					}else if(typekind==10){
						qxcdatalist.add(betQueryinfo);
					}else if(typekind==11){
						djdatalist.add(betQueryinfo);
					}else if(typekind==12){
						jclqdatalist.add(betQueryinfo);
					}else if(typekind==13){
						twentydatalist.add(betQueryinfo);
					}else if(typekind==14){
						sfcdatalist.add(betQueryinfo);
					}else if(typekind==15){
						rxjdatalist.add(betQueryinfo);
					}else if(typekind==16){
						lcbdatalist.add(betQueryinfo);
					}else if(typekind==17){
						jqcdatalist.add(betQueryinfo);
					}
					
				}catch (Exception e) {
				}
			 }
			 
		  	 } catch (JSONException e) {
		  		try {
						JSONObject winprizejson = new JSONObject(json);
		  		} catch (JSONException e1) {
		  		}
		    }
	     }
	class BetQueryInfo{
		public boolean isRepeatBuy() {
			return isRepeatBuy;
		}
		public void setRepeatBuy(boolean isRepeatBuy) {
			this.isRepeatBuy = isRepeatBuy;
		}
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
		private String prizeState;
		private String win_code;
		private boolean isRepeatBuy;
		public String getWin_code() {
			return win_code;
		}
		public void setWin_code(String win_code) {
			this.win_code = win_code;
		}
		public String getPrizeState() {
			return prizeState;
		}
		public void setPrizeState(String prizeState) {
			this.prizeState = prizeState;
		}
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
			private List<BetQueryInfo> mList;
			public WinPrizeAdapter(Context context, List<BetQueryInfo> list) {
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
				int  isJC = 0;
				//如果是竞彩则彩种的期号是不能显示的，在这里要加上所有的竞彩彩种编号
				if(lotno.equals("J00001")||lotno.equals("J00002")||lotno.equals("J00003")||lotno.equals("J00004")||lotno.equals(Constants.LOTNO_JCLQ
						)||lotno.equals(Constants.LOTNO_JCLQ_DXF)||lotno.equals(Constants.LOTNO_JCLQ_RF)
						||lotno.equals(Constants.LOTNO_JCLQ_SFC)){
					isJC = 1;
				}else{
					isJC = 0;
				}
				final boolean isRepeatBuy = (Boolean)mList.get(position).isRepeatBuy();
				final String lotno = (String) mList.get(position).getLotNo();
				final String prizeqihao = (String) mList.get(position).getBatchCode();
				final String amount = (String) mList.get(position).getAmount();
				final String playFashion = (String) mList.get(position).getPlay();
				final String fPayMoney = PublicMethod.formatMoney(amount); 
				final String lotName = (String) mList.get(position).getLotName();
				final String lotMulti = (String) mList.get(position).getLotMulti();
				final String prizemoney = (String) mList.get(position).getPrizeAmt();
				final String formatPrizeName = PublicMethod.formatMoney(prizemoney);
				final String ordertime = (String) mList.get(position).getOrdertime();
				final String betcode = (String) mList.get(position).getBetCode();
				final String bet_code = (String) mList.get(position).getBet_code();
				final String prize_State = (String)mList.get(position).getPrizeState();
				final String win_code = (String)mList.get(position).getWin_code();
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
				noBuyAgain(holder.buyagain,holder.prizeqihao,isRepeatBuy,isJC);
				String payString = getString(R.string.usercenter_winprize_payMoney);//投注金额字
				
				String prizeString = getString(R.string.usercenter_prizeMoney);//中奖金额字
				holder.lotteryname.setText(lotName);
				
				holder.paymoney .setText(payString+fPayMoney);
				if(prize_State.equals("0")){
				holder.prizemoney.setText(prizeString+"未开奖");	
				}else{
				holder.prizemoney.setText(prizeString+formatPrizeName);	
				}
				
				holder.lookdetail.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						StringBuffer detailinfo = new StringBuffer();
						
						if(lotno.equals("J00001")||lotno.equals("J00002")||lotno.equals("J00003")||lotno.equals("J00004")||lotno.equals(Constants.LOTNO_JCLQ
								)||lotno.equals(Constants.LOTNO_JCLQ_DXF)||lotno.equals(Constants.LOTNO_JCLQ_RF)
								||lotno.equals(Constants.LOTNO_JCLQ_SFC)){
							if(prize_State.equals("0")){
							detailinfo.append(getString(R.string.usercenter_lotterytypename)).append(lotName).append("\n\n")
//							.append(getString(R.string.usercenter_playStyle)).append(playFashion).append("\n\n")
							.append(getString(R.string.usercenter_bettingTime)).append(ordertime).append("\n\n")
							.append(getString(R.string.usercenter_winprize_payMoney)).append(fPayMoney).append("\n\n")
							.append(getString(R.string.usercenter_prizeMoney)).append("未开奖").append("\n\n")
							.append(getString(R.string.usercenter_betcode)).append("\n")
							.append(betcode).append("\n\n");
							}else{
							detailinfo.append(getString(R.string.usercenter_lotterytypename)).append(lotName).append("\n\n")
//								.append(getString(R.string.usercenter_playStyle)).append(playFashion).append("\n\n")
								.append(getString(R.string.usercenter_bettingTime)).append(ordertime).append("\n\n")
								.append(getString(R.string.usercenter_winprize_payMoney)).append(fPayMoney).append("\n\n")
								.append(getString(R.string.usercenter_prizeMoney)).append(formatPrizeName).append("\n\n")
								.append(getString(R.string.usercenter_betcode)).append("\n")
								.append(betcode).append("\n");
							}
						}else{
							if(prize_State.equals("0")){
								detailinfo.append(getString(R.string.usercenter_lotterytypename)).append(lotName).append("\n\n")
//								.append(getString(R.string.usercenter_playStyle)).append(playFashion).append("\n\n")
								.append(getString(R.string.usercenter_lotteryIssue)).append(prizeqihao).append("\n\n")
								.append(getString(R.string.usercenter_bettingTime)).append(ordertime).append("\n\n")
								.append(getString(R.string.usercenter_winprize_payMoney)).append(fPayMoney).append("\n\n")
								.append(getString(R.string.usercenter_prizeMoney)).append("未开奖").append("\n\n")
								.append(getString(R.string.usercenter_betcode)).append("\n")
								.append(betcode).append("\n")
								.append(getString(R.string.usercenter_wincode)).append("未开奖");
							}else {
							detailinfo.append(getString(R.string.usercenter_lotterytypename)).append(lotName).append("\n\n")
//							.append(getString(R.string.usercenter_playStyle)).append(playFashion).append("\n\n")
							.append(getString(R.string.usercenter_lotteryIssue)).append(prizeqihao).append("\n\n")
							.append(getString(R.string.usercenter_bettingTime)).append(ordertime).append("\n\n")
							.append(getString(R.string.usercenter_winprize_payMoney)).append(fPayMoney).append("\n\n")
							.append(getString(R.string.usercenter_prizeMoney)).append(formatPrizeName).append("\n\n")
							.append(getString(R.string.usercenter_betcode)).append("\n")
							.append(betcode).append("\n\n")
							.append(getString(R.string.usercenter_wincode)).append(win_code);
							}
						}
						lookDetailDialog(""+detailinfo);
					}
				});
				holder.buyagain.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						initBetPojo(bet_code,  lotMulti, lotno, amount);
						StringBuffer buyAgainMessage = new StringBuffer();
						if(lotno.equals("J00001")||lotno.equals("J00002")||lotno.equals("J00003")||lotno.equals("J00004")||lotno.equals(Constants.LOTNO_JCLQ)||lotno.equals(Constants.LOTNO_JCLQ_DXF)||lotno.equals(Constants.LOTNO_JCLQ_RF)||lotno.equals(Constants.LOTNO_JCLQ_SFC)){
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
	private void noBuyAgain(Button a,TextView qihao,boolean isRepeatBuy,int isJC){
	    if(isRepeatBuy == false){
	    	if(isJC == 1){
	    		qihao.setVisibility(TextView.GONE);
	    	}else{
	    		qihao.setVisibility(TextView.VISIBLE);
	    	}
			a.setEnabled(false);
			a.setBackgroundResource(R.drawable.buyagainunenable);
	    }else{
			qihao.setVisibility(TextView.VISIBLE);
			a.setBackgroundResource(R.drawable.usercenter_selector_buyagain);
			a.setEnabled(true);
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
		betPojo.setAmt(0);
		betPojo.setIsSellWays("1");
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
