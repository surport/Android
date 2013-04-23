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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.QueryJoinInfoInterface;
import com.ruyicai.util.PublicMethod;

/**
 * @author Administrator
 *
 */
public class JoinInfoActivity extends Activity implements HandlerMsg{
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private final static String INFO = "INFO"; 
	public final static String ID = "id"; 

	private int allPage;
	private int newPage;//当前页从0开始
	private String issue = "",lotno = "";
	String orderBy,orderDir;
	ImageButton imgUp,imgDown;
	List<Vector> listPages = new ArrayList();
	TextView pageText;
	Button progress,allAtm,atm;//排序按钮
	CheckBox  check;//升序降序排序
	private ProgressDialog progressdialog;
	MyHandler handler = new MyHandler(this);//自定义handler
	Handler handlerTwo = new Handler();
	JSONObject json;
	public static boolean isRefresh = false;
	ListView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.join_info_check);
		getInfo();
		init();	
		buttonOnclik();
		joinInfokNet(orderBy,orderDir);
	}
	/**
	 * 从上一界面获取信息
	 */
	public void getInfo(){
		Intent intent = getIntent();
		if(intent!=null){
			lotno = intent.getStringExtra(JoinHallActivity.LOTNO);
			issue = intent.getStringExtra(JoinHallActivity.ISSUE);
		}
	}
	/**
	 * 初始化组件
	 */
	public void init() {
		TextView title = (TextView) findViewById(R.id.join_text_title);
		Button imgRetrun = (Button) findViewById(R.id.join_img_return);
		title.setText("合买大厅");
		title.append("-"+PublicMethod.toLotno(lotno));
		imgRetrun.setBackgroundResource(R.drawable.returnselecter);
		// ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		LinearLayout top = (LinearLayout) findViewById(R.id.join_info_check_linear_top);
		top.setVisibility(LinearLayout.VISIBLE);
		progress = (Button) findViewById(R.id.join_info_btn_progress);
		progress.setBackgroundResource(R.drawable.join_info_btn_selecter);
		allAtm = (Button) findViewById(R.id.join_info_btn_all_atm);
		allAtm.setBackgroundResource(R.drawable.join_info_btn_selecter);
		atm = (Button) findViewById(R.id.join_info_btn_atm);
		atm.setBackgroundResource(R.drawable.join_info_btn_selecter);
		check = (CheckBox) findViewById(R.id.jion_info_check);
	    check.setButtonDrawable(R.drawable.join_info_check_select);
	    listview = (ListView) findViewById(R.id.join_listview);
		imgUp = (ImageButton) findViewById(R.id.join_img_up);
		imgUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
               newPage--;
               if(newPage<0){
            	newPage = 0;
   				Toast.makeText(JoinInfoActivity.this, "已至首页", Toast.LENGTH_SHORT).show();   
               }else{
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
	           if(newPage<allPage){
	        	if(newPage>listPages.size()-1){
	        		joinInfokNet(orderBy,orderDir);
	        	}else{
	        		initList();
	        	}
			   }else{
				newPage=allPage-1;
				Toast.makeText(JoinInfoActivity.this, "已至尾页", Toast.LENGTH_SHORT).show();   
			   }
			}
		});
		pageText = (TextView) findViewById(R.id.join_text_page);
        pageText.setText(getTextPage(-1,0));
	}

	/**
	 * 顶部按钮事件
	 */
	public void buttonOnclik(){
		progress.setBackgroundResource(R.drawable.join_info_btn_b);
		orderBy = QueryJoinInfoInterface.PROGRESS;
		orderDir = QueryJoinInfoInterface.DESC;
		progress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progress.setBackgroundResource(R.drawable.join_info_btn_b);
				allAtm.setBackgroundResource(R.drawable.join_info_btn);
				atm.setBackgroundResource(R.drawable.join_info_btn);
				orderBy = QueryJoinInfoInterface.PROGRESS;
				initOrder();
			}
		});

		allAtm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progress.setBackgroundResource(R.drawable.join_info_btn);
				allAtm.setBackgroundResource(R.drawable.join_info_btn_b);
				atm.setBackgroundResource(R.drawable.join_info_btn);
				orderBy = QueryJoinInfoInterface.TOTALAMT;
				initOrder();
			}
		});
		atm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progress.setBackgroundResource(R.drawable.join_info_btn);
				allAtm.setBackgroundResource(R.drawable.join_info_btn);
				atm.setBackgroundResource(R.drawable.join_info_btn_b);
				orderBy = QueryJoinInfoInterface.MINAMT;
				initOrder();
			}
		});
		// 实现记住密码 和 复选框的状态
	    check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
                         if(isChecked){
                         	 orderDir = QueryJoinInfoInterface.ASC;
                         }else{
                        	 orderDir = QueryJoinInfoInterface.DESC;
                         }
                         initOrder();
					}
				});
	}
	public void initOrder(){
		   newPage = 0;
		   allPage = 0;
	       listPages.clear();
           joinInfokNet(orderBy,orderDir);
	}
	/**
	 * 联网查询
	 */
	public void joinInfokNet(final String orderBy,final String orderDir){
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
//				"2011598"
					str = QueryJoinInfoInterface.queryLotJoinInfo(lotno,issue,orderBy,orderDir,""+newPage,JoinHallActivity.PAGENUM);
					Log.e("str===", str);
					try {
						json = new JSONObject(str);
						String msg = json.getString("message");
						String error = json.getString("error_code");
						if(!error.equals("0000")){
							handlerTwo.post(new Runnable() {
								@Override
								public void run() {
									initList();
								}
							});
						}
						handler.handleMsg(error,msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					progressdialog.dismiss();
			}

		});
		t.start();
	}
	/**
	 * 初始化列表数据
	 */
    public void setValue(){
		try{
			Vector<Info> checkInfos = new Vector<Info>();
			JSONArray array = json.getJSONArray("result");
			allPage = Integer.parseInt(json.getString("totalPage"));
			for(int i=0;i<array.length();i++){
				JSONObject obj = array.getJSONObject(i);
				Info info = new Info();
                info.setAllAtm(obj.getString("totalAmt"));
                info.setAtm(obj.getString("buyAmt"));
                info.setName(obj.getString("starter"));
                info.setProgress(obj.getString("progress"));
                info.setId(obj.getString("caseLotId"));
                JSONObject displayIcon = obj.getJSONObject("displayIcon");
                try{
                    info.setCup(displayIcon.getString("cup"));
                }catch(Exception e){
                    
                }
                try{
                	info.setDiamond(displayIcon.getString("diamond"));
                }catch(Exception e){
                    
                }
                try{
                	info.setStarNum(displayIcon.getString("goldStar"));
                }catch(Exception e){
                    
                }
                try{
                	info.setCrown(displayIcon.getString("crown"));
                }catch(Exception e){
                    
                }
                info.setSafe(obj.getString("safeRate"));
            	checkInfos.add(info);
			}
			listPages.add(checkInfos);
		}catch(Exception e){
			
		}
    }
	/**
	 * 初始化列表
	 */
	public void initList() {

	    pageText.setText(getTextPage(newPage,allPage));
		// 数据源
		list = getListForJoinInfoAdapter();
		JoinInfoAdapter adapter = new  JoinInfoAdapter(this, list);
		listview.setAdapter(adapter);
		/* 列表的点击后的背景 */
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Info info = (Info) listPages.get(newPage).get(position);
				if(info.getAtm().equals(info.getAllAtm())){
					Toast.makeText(JoinInfoActivity.this, "该方案已经满员,请您选择其他方案！", Toast.LENGTH_SHORT).show();  
				}else{
					Intent intent = new Intent(JoinInfoActivity.this, JoinDetailActivity.class);
					intent.putExtra(ID, info.getId());
			        intent.putExtra(JoinHallActivity.LOTNO, lotno);
			        intent.putExtra(JoinHallActivity.ISSUE, issue);
					startActivity(intent);
//			        finish();
				}
			}

		};
		listview.setOnItemClickListener(clickListener);
	}
    public String getTextPage(int newPage,int allPage){
    	if((newPage+1)>allPage){
    		return "第"+allPage+"页"+"  共"+allPage+"页";
    	}else{
    		return "第"+(newPage+1)+"页"+"  共"+allPage+"页";
    	}
    }

	/**
	 * 获得用户中心列表适配器的数据源
	 * 
	 * @return
	 */
	protected List<Map<String, Object>> getListForJoinInfoAdapter() {


		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if(listPages.size()!=0){
        	for (int i = 0; i < listPages.get(newPage).size(); i++) {
    			Map<String, Object> map = new HashMap<String, Object>();
    			Info info = (Info) listPages.get(newPage).get(i);
    			map.put(INFO, info);
    			list.add(map);
    		}
        }
		return list;

	}

	/**
	 * 用户中心的适配器
	 */
	public class JoinInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;

		public  JoinInfoAdapter(Context context, List<Map<String, Object>> list) {
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
			Info info = (Info) mList.get(position).get(INFO);

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.join_info_listview_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.join_info_item_text_name);
				holder.starNum = (LinearLayout) convertView.findViewById(R.id.join_info_item_linear_star);
				holder.progress = (TextView) convertView.findViewById(R.id.join_info_item_text_progress);
				holder.allAtm = (TextView) convertView.findViewById(R.id.join_info_item_text_all_amt);
				holder.atm = (TextView) convertView.findViewById(R.id.join_info_item_text_amt);
				holder.safe = (TextView) convertView.findViewById(R.id.join_info_item_safe);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText("发起人:"+info.getName());
			holder.progress.setText(info.getProgress());
			holder.allAtm.setText("￥"+info.getAllAtm());
			holder.atm.setText("￥"+info.getAtm());
			holder.safe.setText(info.getSafe());
			PublicMethod.createStar(holder.starNum,info.getCrown(),info.getCup(),info.getDiamond(),
					                info.getStarNum(),JoinInfoActivity.this);
			return convertView;
		}

		class ViewHolder {
			TextView name;
			LinearLayout starNum;
			TextView progress;
			TextView allAtm;
			TextView atm;
			TextView safe;
		}
	}

	/**
	 *  网络连接框
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

	/* (non-Javadoc)
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_0000()
	 */
	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		setValue();
		initList();
	}
	/* (non-Javadoc)
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_000000()
	 */
	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.ruyicai.handler.HandlerMsg#getContext()
	 */
	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
	/**
	 * 合买查询内部类
	 * @author Administrator
	 */
	class Info{
		String name;
		String progress;
		String allAtm;
		String atm;
		String id;
		String safe = "";
		String crown = "0";//皇冠
		String cup = "0";//奖杯
		String diamond = "0";//钻石
		String starNum = "0";//星
		public String getSafe() {
			return safe;
		}
		public void setSafe(String safe) {
			this.safe = "+"+safe+"%";
		}
		public String getCrown() {
			return crown;
		}
		public void setCrown(String crown) {
			this.crown = crown;
		}
		public String getCup() {
			return cup;
		}
		public void setCup(String cup) {
			this.cup = cup;
		}
		public String getDiamond() {
			return diamond;
		}
		public void setDiamond(String diamond) {
			this.diamond = diamond;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getStarNum() {
			return starNum;
		}
		public void setStarNum(String starNum) {
			this.starNum = starNum;
		}
		public String getProgress() {
			return progress;
		}
		public void setProgress(String progress) {
			this.progress = progress+"%";
		}
		public String getAllAtm() {
			return allAtm;
		}
		public void setAllAtm(String allAtm) {
			
			this.allAtm = Integer.toString(Integer.parseInt(allAtm)/100);
		}
		public String getAtm() {
			return atm;
		}
		public void setAtm(String atm) {
			this.atm = Integer.toString(Integer.parseInt(atm)/100);
		}

		public Info(){

		}
		
	}
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isRefresh){
			initOrder();
		}
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
