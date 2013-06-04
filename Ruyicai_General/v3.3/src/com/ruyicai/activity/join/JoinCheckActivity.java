/**
 * 
 */
package com.ruyicai.activity.join;

import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.QueryJoinCheckInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 合买查询
 * @author Administrator
 * 
 */
public class JoinCheckActivity extends Activity implements HandlerMsg {
	private List<JoinCheck> list;/* 列表适配器的数据源 */
	private final static String INFO = "INFO";
	private int allPage;
	private int newPage=0;// 当前页从0开始f
	Button imgUp, imgDown;		
	private ProgressDialog progressdialog;
	MyHandler handler = new MyHandler(this);// 自定义handler
	String phonenum, sessionid, userno;
	Vector<JoinCheck> checkInfosall = new Vector<JoinCheck>();
	JSONObject json;
	JoinCheckAdapter adapter;
	ProgressBar progressbar;
	ProgressDialog dialog;
	ListView listview;
	View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.join_info_check);
		init();
		initUserInfo();
		joinCheckNet();
	}
	
	private void initUserInfo(){
		  RWSharedPreferences shellRW = new RWSharedPreferences(
					JoinCheckActivity.this, "addInfo");
			        phonenum = shellRW.getStringValue("phonenum");
			        sessionid = shellRW.getStringValue("sessionid");
			        userno = shellRW.getStringValue("userno");
	}

	/**
	 * 初始化组件
	 */
	public void init() {
		TextView title = (TextView) findViewById(R.id.join_text_title);
		Button imgRetrun = (Button) findViewById(R.id.join_img_return);
		title.setText("合买查询");
		imgRetrun.setBackgroundResource(R.drawable.returnselecter);
		// ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		imgDown = (Button) findViewById(R.id.join_img_down);
		imgDown.setVisibility(View.GONE);

	}
    private void getMore(){
    	newPage++;
		if (newPage < allPage) {
				netting();
		} else {
			newPage = allPage - 1;
			progressbar.setVisibility(view.INVISIBLE);
			listview.removeFooterView(view);
			Toast.makeText(JoinCheckActivity.this, "已至尾页",
					Toast.LENGTH_SHORT).show();
		}
    }
	/**
	 * 初始化数据
	 */
	public void setValue() {
		try {
			Vector<JoinCheck> checkInfos = new Vector<JoinCheck>();
			JSONArray array = json.getJSONArray("result");
			allPage = Integer.parseInt(json.getString("totalPage"));
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				JoinCheck checkInfo = new JoinCheck();
				checkInfo.setTitle(obj.getString("lotNo"));
				checkInfo.setCaseid(obj.getString("caseLotId"));
				checkInfo.setResult(obj.getString("displayStateMemo"));
				checkInfo.setAtm(obj.getString("amt"));
				checkInfo.setTime(obj.getString("buyTime"));
				checkInfo.setDisplayState(obj.getString("displayState"));
				checkInfo.setAllAtm(obj.getString("totalAmt"));
				checkInfo.setBaoAtm(obj.getString("safeAmt"));
				checkInfo.setProgress(obj.getString("progress"));
				checkInfo.setPrizeAmt(obj.getString("prizeAmt"));
				checkInfo.setCommisionPrizeAmt(obj.getString("commisionRatio")+"%");
				checkInfo.setStarter(obj.getString("starter"));
				checkInfo.setPrizeState(obj.getString("prizeState"));
				checkInfo.setWinCode(obj.getString("winCode"));
				checkInfo.setBetchcode(obj.getString("batchCode"));
				try{
					checkInfo.setZhuma(obj.getString("content"));
				}catch(Exception e){
					
				}
				
				checkInfos.add(checkInfo);
				checkInfosall.add(checkInfo);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 合买详情框
	 */
	public void  detailDalog(int position) {
		JoinCheck info = (JoinCheck) checkInfosall.get(position);
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.join_check_detail, null);
		final AlertDialog detailDialog = new AlertDialog.Builder(this).create();
        TextView lotno = (TextView) v.findViewById(R.id.join_check_detail_text_lotno);
        TextView becthcode = (TextView) v.findViewById(R.id.join_check_detail_text_betchcode);
        TextView time = (TextView) v.findViewById(R.id.join_check_detail_text_time);
        TextView id = (TextView) v.findViewById(R.id.join_check_detail_text_id);
        TextView allAtm = (TextView) v.findViewById(R.id.join_check_detail_text_all_atm);
        TextView renAtm = (TextView) v.findViewById(R.id.join_check_detail_text_ren_atm);
        TextView baoAtm = (TextView) v.findViewById(R.id.join_check_detail_text_bao_atm);
        TextView progress = (TextView) v.findViewById(R.id.join_check_detail_text_progress);
        TextView result = (TextView) v.findViewById(R.id.join_check_detail_text_result);
        TextView zhuma = (TextView) v.findViewById(R.id.join_check_detail_text_zhuma);
        TextView prize = (TextView) v.findViewById(R.id.join_check_detail_text_prize_atm);
        TextView startPrize = (TextView) v.findViewById(R.id.join_check_detail_text_start_prize_atm);
        TextView winCode = (TextView) v.findViewById(R.id.join_check_detail_text_kaijiang);
        String prizeState =info.getPrizeState();
        lotno.append(info.getTitle());
        if(info.getBetchcode().equals("null")||info.getBetchcode().equals("")){
        	becthcode.setVisibility(view.GONE);	
        }else{
        becthcode.append(info.getBetchcode());
        }
        time.append(info.getTime());
        id.append(info.getCaseid());
        allAtm.append(info.getAllAtm());
        renAtm.append(info.getAtm());
        baoAtm.append(info.getBaoAtm());
        progress.append(info.getProgress());
        result.setTextColor(setColor(info.getDisplayState()));
        result.append(info.getResult());
        zhuma.append(info.getZhuma());
        if(prizeState.equals("0")){
        winCode.append("未开奖");
        }else{
        winCode.append(info.getWinCode());
        }
        prize.append(info.getPrizeAmt());
        startPrize.append(info.getCommisionPrizeAmt());
        Button cancel = (Button) v.findViewById(R.id.join_check_detail_cancel);
        cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				detailDialog.cancel();
			}
		});
        detailDialog .show();
        detailDialog .getWindow().setContentView(v);

	}

	/**
	 * 初始化列表
	 */
	public void initList() {
	    LayoutInflater	mInflater = LayoutInflater.from(this);
	    view = mInflater.inflate(R.layout.lookmorebtn, null);
	    progressbar=(ProgressBar)view.findViewById(R.id.getmore_progressbar);
	    listview = (ListView) findViewById(R.id.join_listview);
		listview.addFooterView(view);
     	// 数据源
		list = checkInfosall;
		adapter = new JoinCheckAdapter(this, list);
		listview.setAdapter(adapter);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				view.setEnabled(false);
	            getMore();

				
			}
		});
		/* 列表的点击后的背景 */
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				detailDalog(position);
			}

		};
		listview.setOnItemClickListener(clickListener);
		listview.setDividerHeight(1);
	}

	public String getTextPage(int newPage, int allPage) {
		return "第" + (newPage + 1) + "页" + "  共" + allPage + "页";
	}

	/**
	 * 获得用户中心列表适配器的数据源
	 * 
	 * @return
	 */
//	protected List<Map<String, Object>> getListForJoinCheckAdapter() {
//
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
//       for(int j=0;j<listPages.size();j++){
//		for (int i = 0; i < listPages.get(j).size(); i++) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put(INFO, ((JoinCheck) listPages.get(j).get(i)));
//			list.add(map);
//		}
//       }
//		return list;
//
//	}

	/**
	 * 用户中心的适配器
	 */
	public class JoinCheckAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<JoinCheck> mList;

		public JoinCheckAdapter(Context context, List<JoinCheck> list) {
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
			JoinCheck info = (JoinCheck) mList.get(position);
			String icon = info.getTitle();
			String result = info.getResult();
			String starter = info.getStarter();//发起人
			String time = info.getTime();
			String atm = info.getAtm();
			String state = info.getDisplayState();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.join_check_listview_item, null);
				holder = new ViewHolder();
				holder.icon = (TextView) convertView.findViewById(R.id.join_check_item_text_icon);
				holder.starter = (TextView) convertView.findViewById(R.id.join_check_item_text_id);
				holder.result = (TextView) convertView.findViewById(R.id.join_check_item_text_result);
				holder.atm = (TextView) convertView.findViewById(R.id.join_check_item_text_amt);
				holder.time = (TextView) convertView.findViewById(R.id.join_check_item_text_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.icon.setText(icon);
			setResultColor(state, result, holder.result);
			holder.starter.setText(starter);
			holder.time.setText(time);
			holder.atm.setText(atm);
			return convertView;
		}

		class ViewHolder {
			TextView icon;
			TextView result;
			TextView starter;
			TextView time;
			TextView atm;
		}
	}

	/**
	 * 设置颜色
	 */
	public void setResultColor(String state, String resultStr, TextView result) {
		SpannableStringBuilder builder = new SpannableStringBuilder();

		resultStr = "(" + resultStr + ")";
		builder.append(resultStr);
		builder.setSpan(new ForegroundColorSpan(setColor(state)), 1,
				resultStr.length() - 1, Spanned.SPAN_COMPOSING);
		result.setText(builder, BufferType.EDITABLE);
	}
	public int setColor(String state){
		int color = 0xff666666;
		if (state.equals("1")) {// 认购中
			color = 0xff006600;
		} else if (state.equals("2")) {// 满员
			color = 0xffcc0000;
		} else if (state.equals("3")) {// 成功
			color = 0xff006600;
		} else if (state.equals("4")) {// 撤单
			color = 0xff666666;
		} else if (state.equals("5")) {// 流单
			color = 0xff666666;
		} else if (state.equals("6")) {// 已中奖
			color = 0xffcc0000;
		}
		return color;
	}
    
	private void netting(){
		if(progressbar!=null){
			progressbar.setVisibility(ProgressBar.VISIBLE);
		}
		final Handler tHandler = new Handler();
		new Thread(new Runnable() {
			@Override
		public void run() {
		String str = "00";
		str = QueryJoinCheckInterface.queryLotJoinCheck(userno,phonenum, "" + newPage, JoinHallActivity.PAGENUM);
		tHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(progressbar!=null){
					progressbar.setVisibility(ProgressBar.INVISIBLE);
					view.setEnabled(true);
				}
				if(dialog!=null){
					dialog.dismiss();
				}
			}
		 });
		try {
			json = new JSONObject(str);
			String msg = json.getString("message");
			String error = json.getString("error_code");
			handler.handleMsg(error, msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		}
	  }).start();
	}

	/**
	 * 联网查询
	 */
	
	public void joinCheckNet() {
		if(newPage==0){
		  showDialog(0); 	
		}
		netting();	
	}

	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			dialog = new ProgressDialog(this);
			dialog.setMessage("网络连接中...");
			dialog.setIndeterminate(true);
			return dialog;
		}
		}
		return null;
	}

	/**
	 * 取消网络连接框
	 */
	public void dismissDialog() {
		// TODO Auto-generated method stub
		progressdialog.dismiss();
	}

	class CheckInfo {

		public CheckInfo() {

		}
	}

	public void errorCode_0000() {
		// TODO Auto-generated method stub
		setValue();
	    if(newPage==0){
	    	initList();
	    }else{
	    	adapter.notifyDataSetChanged();		
	    }
		
		
	}

	public void errorCode_000000() {
		// TODO Auto-generated method stub

	}

	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	class JoinCheck {
		String title = "";
		String caseid = "";
		String result = "";
		String time = "2011-8-22 15:20";
		String atm = "";
		String displayState = "";
		String allAtm = "";
		String baoAtm = "";
		String progress = "";
		String zhuma = "";
		String prizeAmt = "";
		String commisionPrizeAmt = "";
		String starter = "";
		String prizeState="";
		String winCode="";
		String betchcode="";
		
		public String getBetchcode() {
			return betchcode;
		}

		public void setBetchcode(String betchcode) {
			this.betchcode = betchcode;
		}

		public String getPrizeState() {
			return prizeState;
		}

		public void setPrizeState(String prizeState) {
			this.prizeState = prizeState;
		}

		public String getWinCode() {
			return winCode;
		}

		public void setWinCode(String winCode) {
			this.winCode = winCode;
		}

		public String getStarter() {
			return "发起人："+starter;
		}

		public void setStarter(String starter) {
			this.starter = starter;
		}

		public String getPrizeAmt() {
			return "￥"+PublicMethod.toYuan(prizeAmt);
		}

		public void setPrizeAmt(String prizeAmt) {
			this.prizeAmt = prizeAmt;
		}

		public String getCommisionPrizeAmt() {
			return commisionPrizeAmt;
		}

		public void setCommisionPrizeAmt(String commisionPrizeAmt) {
			this.commisionPrizeAmt = commisionPrizeAmt;
		}

		public JoinCheck() {

		}
		
		public String getZhuma() {
			return zhuma;
		}

		public void setZhuma(String zhuma) {
			this.zhuma = zhuma;
		}

		public String getAllAtm() {
			return "￥"+PublicMethod.toIntYuan(allAtm);
		}

		public void setAllAtm(String allAtm) {
			this.allAtm = allAtm;
		}

		public String getBaoAtm() {
			return "￥"+PublicMethod.toIntYuan(baoAtm);
		}

		public void setBaoAtm(String baoAtm) {
			this.baoAtm = baoAtm;
		}

		public String getProgress() {
			return progress+"%";
		}

		public void setProgress(String progress) {
			this.progress = progress;
		}

		public String getDisplayState() {
			return displayState;
		}

		public void setDisplayState(String displayState) {
			this.displayState = displayState;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = PublicMethod.toLotnohemai(title);
		}

		public String getCaseid() {
			return caseid;
		}

		public void setCaseid(String caseid) {
			this.caseid = caseid;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getAtm() {
			return atm;
		}

		public void setAtm(String atm) {
			this.atm = "￥"+Integer.toString(Integer.parseInt(atm) / 100);
			;
		}
	}
}
