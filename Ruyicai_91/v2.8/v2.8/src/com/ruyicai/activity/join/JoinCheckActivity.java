/**
 * 
 */
package com.ruyicai.activity.join;

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
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.QueryJoinCheckInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * @author Administrator
 * 
 */
public class JoinCheckActivity extends Activity implements HandlerMsg {
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private final static String INFO = "INFO";
	private int allPage;
	private int newPage;// 当前页从0开始f
	ImageButton imgUp, imgDown;		
	TextView pageText;
	private ProgressDialog progressdialog;
	MyHandler handler = new MyHandler(this);// 自定义handler
	String phonenum, sessionid, userno;
	// Vector<JoinCheck> checkInfos = new Vector<JoinCheck>();
	List<Vector> listPages = new ArrayList();
	JSONObject json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.join_info_check);
		init();
		joinCheckNet();
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
		imgUp = (ImageButton) findViewById(R.id.join_img_up);
		imgUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newPage--;
				if (newPage < 0) {
					newPage = 0;
					Toast.makeText(JoinCheckActivity.this, "已至首页",
							Toast.LENGTH_SHORT).show();
				} else {
					initList();
				}
			}
		});
		imgDown = (ImageButton) findViewById(R.id.join_img_down);
		imgDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(newPage > listPages.size() - 1){
				   newPage = listPages.size() - 1;
				}
				newPage++;
				if (newPage < allPage) {
					if (newPage > listPages.size() - 1) {
						joinCheckNet();
					} else {
						initList();
					}
				} else {
					newPage = allPage - 1;
					Toast.makeText(JoinCheckActivity.this, "已至尾页",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		pageText = (TextView) findViewById(R.id.join_text_page);
		getTextPage(-1, 0);

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				JoinCheckActivity.this, "addInfo");
		phonenum = shellRW.getUserLoginInfo("phonenum");
		sessionid = shellRW.getUserLoginInfo("sessionid");
		userno = shellRW.getUserLoginInfo("userno");
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
				checkInfo.setCommisionPrizeAmt(obj.getString("commisionPrizeAmt"));
				try{
					checkInfo.setZhuma(obj.getString("content"));
				}catch(Exception e){
					
				}
				checkInfos.add(checkInfo);
			}
			listPages.add(checkInfos);
		} catch (Exception e) {

		}
	}

	/**
	 * 合买详情框
	 */
	public void  detailDalog(int position) {
		JoinCheck info = (JoinCheck) listPages.get(newPage).get(position);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.join_check_detail, null);
		final AlertDialog detailDialog = new AlertDialog.Builder(this).create();
        TextView lotno = (TextView) v.findViewById(R.id.join_check_detail_text_lotno);
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
        lotno.append(info.getTitle());
        time.append(info.getTime());
        id.append(info.getCaseid());
        allAtm.append(info.getAllAtm());
        renAtm.append(info.getAtm());
        baoAtm.append(info.getBaoAtm());
        progress.append(info.getProgress());
        result.setTextColor(setColor(info.getDisplayState()));
        result.append(info.getResult());
        zhuma.append(info.getZhuma());
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
		pageText.setText(getTextPage(newPage, allPage));
		ListView listview = (ListView) findViewById(R.id.join_listview);
		// 数据源
		list = getListForJoinCheckAdapter();
		JoinCheckAdapter adapter = new JoinCheckAdapter(this, list);
		listview.setAdapter(adapter);
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
	protected List<Map<String, Object>> getListForJoinCheckAdapter() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);

		for (int i = 0; i < listPages.get(newPage).size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(INFO, ((JoinCheck) listPages.get(newPage).get(i)));
			list.add(map);
		}

		return list;

	}

	/**
	 * 用户中心的适配器
	 */
	public class JoinCheckAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;

		public JoinCheckAdapter(Context context, List<Map<String, Object>> list) {
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
			JoinCheck info = (JoinCheck) mList.get(position).get(INFO);
			String icon = info.getTitle();
			String result = info.getResult();
			String id = info.getCaseid();
			String time = info.getTime();
			String atm = info.getAtm();
			String state = info.getDisplayState();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.join_check_listview_item, null);
				holder = new ViewHolder();
				holder.icon = (TextView) convertView.findViewById(R.id.join_check_item_text_icon);
				holder.id = (TextView) convertView.findViewById(R.id.join_check_item_text_id);
				holder.result = (TextView) convertView.findViewById(R.id.join_check_item_text_result);
				holder.atm = (TextView) convertView.findViewById(R.id.join_check_item_text_amt);
				holder.time = (TextView) convertView.findViewById(R.id.join_check_item_text_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.icon.setText(icon);
			setResultColor(state, result, holder.result);
			holder.id.setText(id);
			holder.time.setText(time);
			holder.atm.setText(atm);
			return convertView;
		}

		class ViewHolder {
			TextView icon;
			TextView result;
			TextView id;
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

	/**
	 * 联网查询
	 */
	public void joinCheckNet() {
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = QueryJoinCheckInterface.queryLotJoinCheck(userno,
						phonenum, "" + newPage, JoinHallActivity.PAGENUM);
				Log.e("str===", ""+str);
				try {
					json = new JSONObject(str);
					String msg = json.getString("message");
					String error = json.getString("error_code");
					handler.handleMsg(error, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}

		});
		t.start();
	}

	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			return progressdialog;
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
		initList();
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
		
		public String getPrizeAmt() {
			return "￥"+PublicMethod.toYuan(prizeAmt);
		}

		public void setPrizeAmt(String prizeAmt) {
			this.prizeAmt = prizeAmt;
		}

		public String getCommisionPrizeAmt() {
			return "￥"+PublicMethod.toYuan(commisionPrizeAmt);
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
			this.title = PublicMethod.toLotno(title);
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
