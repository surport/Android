package com.ruyicai.activity.buy.zc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.zc.pojo.TeamInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.FootballLotteryAdvanceBatchcode;
import com.ruyicai.net.transaction.FootballInterface;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class FootballSFLottery extends FootballFourteen implements OnSeekBarChangeListener,HandlerMsg{
	private String codeStr;
	private RadioButton  check;
	private RadioButton  joinCheck;
	
	
	MyHandler handlertouzhu = new MyHandler((HandlerMsg) this);//自定义handler
	String lotno = Constants.LOTNO_SFC;
	MyHandler touzhuhandler = new MyHandler(this);
	int lieNum;
	private final static String TEAM1 = "TEAM1";
	private final static String TEAM2 = "TEAM2";
	private final static String SCORES1 = "SCORES1";
	private final static String SCORES2 = "SCORES2";
	String inflater = Context.LAYOUT_INFLATER_SERVICE;

	LayoutInflater layoutInflater;
	ListViewDemo listViewDemo;
	ScrollView mHScrollView;
	LinearLayout buyView;

	ListView mlist;
	TextView mTextSumMoney;
	List<Map<String, Object>> list;

	SeekBar mSeekBarBeishu;
	TextView mTextBeishu;
	int iProgressBeishu = 1;
	ImageButton sfc_btn_touzhu;
	private  Vector<BallTable> ballTables = new Vector<BallTable>();

	int index;
	// 进度条
	private static final int DIALOG1_KEY = 0;
	private ProgressDialog progressdialog;
	private JSONObject obj;
	String re;
	private Vector<TeamInfo> teamInfos = new Vector<TeamInfo>();	
	private String[] bactchCodes;//预售期的期号数组
	private List<Object> bactchArray = new ArrayList<Object>();//这个list中存放预售期期号和截止时间的信息
	String advanceBatchCodeData;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBatchCode(Constants.LOTNO_SFC);
		initBatchCodeView();
//		getData(qihaoxinxi[2],qihaoxinxi[0]);
		showDialog(DIALOG1_KEY);
		getZCAdvanceBatchCodeData(Constants.LOTNO_SFC);
		
	}
	
	/**
	 * 消息处理函数
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = msg.getData().getString("get");
			switch (msg.what) {
			case 0:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG).show();
				break;
			case 1:
				progressdialog.dismiss();
				initList();
				break;
			case 2:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "发生异常！", Toast.LENGTH_LONG).show();
				break;
			case 3:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "传入参数不是JSON格式！",Toast.LENGTH_LONG).show();
				break;
			case 4:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "系统正在维护请稍候再试", Toast.LENGTH_LONG).show();
				break;
			case 5:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "传入期号错误！", Toast.LENGTH_LONG).show();
				break;

			case 6:
				progressdialog.dismiss();
				FootballContantDialog.alertIssueNOFQueue(FootballSFLottery.this);
				break;
			case 7:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "转码异常！", Toast.LENGTH_LONG).show();
				break;
			case 8:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG).show();
				break;
			case 9:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败余额不足！",Toast.LENGTH_LONG).show();

				break;
			case 10:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "获取信息失败！", Toast.LENGTH_LONG).show();
				break;
			case 11:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "彩票投注中！", Toast.LENGTH_LONG).show();
				break;
			case 12:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注成功，出票成功！",Toast.LENGTH_LONG).show();

				for (int i = 0; i < ballTables.size(); i++) {
					ballTables.get(i).clearAllHighlights();
				}
				Intent intent = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent);
				break;
			case 14:
				progressdialog.dismiss();
				if(isFinishing() == false){
				    PublicMethod.showDialog(FootballSFLottery.this);
				}
				for (int i = 0; i < ballTables.size(); i++) {
					ballTables.get(i).clearAllHighlights();
				}
				changeTextSumMoney(getZhuShu());
				Intent intent1 = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent1);
				break;
			case 15:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(FootballSFLottery.this, UserLogin.class);
				startActivity(intentSession);
				break;
			case 16:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG)	.show();
				break;
			case 17:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败！", Toast.LENGTH_LONG).show();
				break;
			case 18:
				// 将图片的背景设置回原来的图案表示可点击
				sfc_btn_touzhu.setImageResource(R.drawable.imageselecter);
				break;
			case 19:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "没有期号可以投注！",Toast.LENGTH_LONG).show();
				break;
			case 20:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "传入场次错误！", Toast.LENGTH_LONG).show();
				break;
			case 21:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "返回对阵为空！", Toast.LENGTH_LONG).show();
				break;
			case 22:
				progressdialog.dismiss();
				alertZC(re);
				break;
			case 23:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注期号不存在，或已过期！",Toast.LENGTH_LONG).show();
				break;
			case 24:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), msg.obj+"",Toast.LENGTH_SHORT).show();
				break;
			case 25:
				progressdialog.dismiss();
				if(isOne){
					isOne = false;
					getTeamInfo(0);
				}else{
					showBatchcodesDialog(bactchCodes);
				}
				break;
			}
		}
	};

	static int iScreenWidth;
	/**
	 * 初始化列表
	 */
	public void initList() {

		mlist = (ListView) findViewById(R.id.buy_footballlottery_list);
		list = getListForMainListAdapter();
		ballTables.clear();//每次初始化足彩选区列表就清空BallTable的 Vector中的数据
		listViewDemo = new ListViewDemo(this, list);
		mlist.setAdapter(listViewDemo);
		
	}

	public void createVeiw() {
		mSeekBarBeishu = (SeekBar) findViewById(R.id.buy_footballlottery_seekbar_muti);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		iProgressBeishu = 1;
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mTextBeishu = (TextView) findViewById(R.id.buy_footballlottery_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		setSeekWhenAddOrSub(R.id.buy_footballlottery_img_subtract_beishu, null, -1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.buy_footballlottery_img_add_beishu, null, 1,mSeekBarBeishu, true);

		sfc_btn_touzhu = (ImageButton) findViewById(R.id.buy_footballlottery_img_touzhu);
		sfc_btn_touzhu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginTouZhu(); // 1表示当前为单式
			}
		});
	}

	/**
	 * 网络连接提示框
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
	 * 投注提示框中的信息
	 */
	private String getTouzhuAlertText() {
		int iZhuShu = getZhuShu();
		return  "注数：" + iZhuShu
				/ mSeekBarBeishu.getProgress() + "注    "  +"倍数：" +mSeekBarBeishu.getProgress() + "倍    "+ "金额："
				+ (iZhuShu * 2) + "元";

	}
	/**
	 * 假设数组的id为ai 每个小球的id为ai*10+小球.Resid 这样就能保证小球id的唯一性
	 */
	/** 小球起始id */
	public static final int SHENGFC_START_ID = 0x83000001;
	public int iAllBallWidth;
    public View views[] = new View[14];
	public class ListViewDemo extends BaseAdapter {

		private Context context;
		private List<Map<String, Object>> mList;
		private LayoutInflater mInflater; // 扩充主列表布局
		
		public ListViewDemo(Context context, List<Map<String, Object>> list) {
			this.context = context;
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		@Override
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
		public  View getView(final int position, View convertView, ViewGroup parent) {
			

			int[] aResId = { R.drawable.grey, R.drawable.red };
			
			int START_ID;
			
			final int index = position;
			START_ID = SHENGFC_START_ID + position * 3;
			String team1 = (String) mList.get(position).get("TEAM1");
			String team2 = (String) mList.get(position).get("TEAM2");
			String scores1 = (String) mList.get(position).get("SCORES1");
			String scores2 = (String) mList.get(position).get("SCORES2");
		
			ViewHolder holder = null;
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.buy_football_sforchosenine_listitem, parent,false);
            holder.lie = ((TextView) convertView.findViewById(R.id.lienum));
			holder.teamname = (TextView) convertView.findViewById(R.id.teamname);
			holder.teamrank = (TextView) convertView.findViewById(R.id.teamrank);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.shengfucai_layout);
			holder.info = (ImageView) convertView.findViewById(R.id.fenxi);	
			LinearLayout linearSF =(LinearLayout)convertView.findViewById(R.id.sforchoosenine_item);
			setFootballListItemBackground(linearSF, position);
			int aFieldWidth = iScreenWidth/3;
			BallTable shengfcRow = null;
			

			shengfcRow = makeBallTable(holder.layout, R.id.shengfucai_ball, aResId,START_ID);
			if( ballTables.size() < mList.size() ){
				 ballTables.add(shengfcRow);
			}
			Vector<OneBallView> BallViews = shengfcRow.getBallViews();
			for (int i = 0; i < BallViews.size(); i++) {
				final OneBallView ball = BallViews.get(i);
				ball.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						ball.startAnim();
						ball.changeBallColor();
						changeTextSumMoney(getZhuShu());
					}
				});
			}
            
			if (position < 9) {
				holder.lie.setText((String.valueOf(position + 1)) + "  ");
			} else {
				holder.lie.setText((String.valueOf(position + 1)));
			}

			if (team1.length() == 2) {
				team1 = "　" + team1;
			}
			if (team2.length() == 2) {
				team2 += "　";
			}

			holder.teamname.setText(team1 + "VS" + team2);
			try {
				if (scores1 != null){
					holder.teamrank.setText("  " + scores1 + "   " + scores2);
				}

			} catch (Exception e) {
				
			}

			holder.info.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					getInfo(index);
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView lie;
			TextView teamname;
			TextView teamrank;
			ImageView info;
			LinearLayout layout;
		}

	}

	/**
	 * 主列表中相应的数据
	 */
	private List<Map<String, Object>> getListForMainListAdapter() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < teamInfos.size(); i++) {
			map = new HashMap<String, Object>();
			map.put(TEAM1, teamInfos.get(i).hTeam);
			map.put(TEAM2, teamInfos.get(i).vTeam);
			map.put(SCORES1, teamInfos.get(i).hRankNum);
			map.put(SCORES2, teamInfos.get(i).vRankNum);
			list.add(map);
		}

		return list;
	}

	/**
	 * 胜负彩的注数计算方法
	 */
	private int getZhuShu() {
		int iReturnValue = 0;
		int beishu = mSeekBarBeishu.getProgress();
		for (int i = 0; i < ballTables.size(); i++) {
			if (i != 0) {
				iReturnValue *= ballTables.get(i).getHighlightBallNums();
			} else {
				iReturnValue = ballTables.get(i).getHighlightBallNums();
			}
		}
		return iReturnValue * beishu;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {
		case R.id.buy_footballlottery_seekbar_muti:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			changeTextSumMoney(getZhuShu());
			break;
		default:
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
	
	/**
	 * 获取注码
	 */
	public  String getZhuma() {
		StringBuffer t_str = new StringBuffer();
		for (int i = 0; i < ballTables.size(); i++) {
			int balls[] = ballTables.get(i).getHighlightBallNOs();
			for (int j = 0; j < balls.length; j++) {
				t_str.append(balls[j]);
			}
			if (i < ballTables.size() - 1) {
				t_str.append(",");
			}
		}
		return t_str.toString();
	}


	public void beginTouZhu() {
		int iZhuShu = getZhuShu();//注数是注数*倍数的结果
		RWSharedPreferences pre = new RWSharedPreferences(FootballSFLottery.this, "addInfo");
		sessionid = pre.getStringValue("sessionid");
		if (sessionid == null || sessionid.equals("")) {
			Intent intentSession = new Intent(FootballSFLottery.this, UserLogin.class);
			startActivityForResult(intentSession, 0);
			sfc_btn_touzhu.setClickable(true);
		} else {
			if (isTouZhu()) {
				Toast.makeText(FootballSFLottery.this, "请至少选择一注！", Toast.LENGTH_SHORT) .show();
			} else if (iZhuShu * 2 > 20000) {
				DialogExcessive();
			} else {
				String sTouzhuAlert = "";
//				sTouzhuAlert = getTouzhuAlertText();
//				alert(sTouzhuAlert, getFormatZhuma());
				initBetPojo();
				toorderdetail();
			}
		}
	}

	void toorderdetail(){
		 ApplicationAddview app=(ApplicationAddview)getApplicationContext();
		  app.setPojo(betPojo);
	      Intent intent =new Intent(FootballSFLottery.this,Footballorderdail.class);
	      intent.putExtra("tpye", "zc");
		  intent.putExtra("zhuma", getZhuma());
		  startActivity(intent);
		  for (int i = 0; i < ballTables.size(); i++) {
				ballTables.get(i).clearAllHighlights();
			}
	}
	
	public boolean isTouZhu() {
		for (int i = 0; i < ballTables.size(); i++) {
			if (ballTables.get(i).getHighlightBallNums() == 0) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 加减按钮事件监听方法
	 */
	private void setSeekWhenAddOrSub(int idFind, View iV, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						iProgressBeishu++;
						if (iProgressBeishu > 99){
							iProgressBeishu = 99;
						}
						mSeekBar.setProgress(iProgressBeishu);
					} else {
						iProgressBeishu--;
						if (iProgressBeishu < 1){
							iProgressBeishu = 1;
						}
						mSeekBar.setProgress(iProgressBeishu);
					}
				}
			}
		});
	}

	/**
	 * 分析数据解析
	 * @param re
	 */
	private void alertZC(String re) {
		// 解析数据
		JSONObject re1;
		JSONObject obj;
		String hTeam8 = "";
		String vTeam8 = "";
		String avgOdds = "";
		String title = "";
		try {
			re1 = new JSONObject(re);
			obj = re1.getJSONObject("value");
			hTeam8 = obj.getString("HTeam8");
			vTeam8 = obj.getString("VTeam8");
			avgOdds = obj.getString("avgOdds");
//			title += obj.getString("num");
			title += obj.getString("HTeam");
			title += "VS";
			title += obj.getString("VTeam");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		LayoutInflater factory = LayoutInflater.from(this);
		View view = factory.inflate(R.layout.football_anaylese, null);
		TextView row1_1 = (TextView) view.findViewById(R.id.sheng_zhishu);
		TextView row1_2 = (TextView) view.findViewById(R.id.zhudui_jinshi_sheng);
		TextView row1_3 = (TextView) view.findViewById(R.id.kedui_jinshi_sheng);
		TextView row2_1 = (TextView) view.findViewById(R.id.ping_zhishu);
		TextView row2_2 = (TextView) view.findViewById(R.id.zhudui_jinshi_ping);
		TextView row2_3 = (TextView) view.findViewById(R.id.kedui_jinshi_ping);
		TextView row3_1 = (TextView) view.findViewById(R.id.fu_zhishu);
		TextView row3_2 = (TextView) view.findViewById(R.id.zhudui_jinshi_fu);
		TextView row3_3 = (TextView) view.findViewById(R.id.kedui_jinshi_fu);
		TextView row4_1 = (TextView) view.findViewById(R.id.zhudui_jinshi_jinqiu);
		TextView row4_2 = (TextView) view.findViewById(R.id.kedui_jinshi_jinqiu);
		if(!hTeam8.equals("")){
			String hteam[] = hTeam8.split("\\|");
			row1_2.setText(hteam[0]);
			row2_2.setText(hteam[1]);
			row3_2.setText(hteam[2]);
			row4_1.setText(hteam[3] + "|" + hteam[4]);
		}
		if(!vTeam8.equals("")){
			String vteam[] = vTeam8.split("\\|");
			row1_3.setText(vteam[0]);
			row2_3.setText(vteam[1]);
			row3_3.setText(vteam[2]);
			row4_2.setText(vteam[3] + "|" + vteam[4]);
		}
		if(!avgOdds.equals("")) {
			String avg[] = avgOdds.split("\\|");
			row1_1.setText(avg[0].substring(1));
			row2_1.setText(avg[1].substring(1));
			row3_1.setText(avg[2].substring(1));
		}
		Builder dialog = new AlertDialog.Builder(this).setTitle(title).setView(
				view).setPositiveButton("取消",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
		dialog.show();
	}
	/**
	 * 获取对阵矩阵
	 */
	public void getData(final String lotno,final String batchCode) {
		showDialog(DIALOG1_KEY);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String error_code = "00";
				re = FootballInterface.getInstance().getZCData(lotno, batchCode);
				try {
					obj = new JSONObject(re);
					error_code = obj.getString("error_code");
					if (error_code.equals("000000")) {
						teamInfos.clear();
						JSONArray value = obj.getJSONArray("value");
						for (int i = 0; i < value.length(); i++) {
							JSONObject re = value.getJSONObject(i);
							TeamInfo team = new TeamInfo();
							team.hTeam = re.getString("HTeam");
							team.vTeam = re.getString("VTeam");
							String rank = re.getString("leagueRank");
							team.num = re.getString("num");
							if (rank != null&&!rank.equals("")) {
								String str[] = rank.split("\\|");
								team.hRankNum = str[0];
								team.vRankNum = str[1];
							}
							teamInfos.add(team);
						}
					}
				} catch (Exception e) {
				}

				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);

				} else if (error_code.equals("000000")) {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} else if (error_code.equals("100000")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else if (error_code.equals("200001")) {
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				} else if (error_code.equals("200002")) {
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else if (error_code.equals("200003")) {
					Message msg = new Message();
					msg.what = 5;
					handler.sendMessage(msg);
				} else if (error_code.equals("200005")) {
					Message msg = new Message();
					msg.what = 6;
					handler.sendMessage(msg);
				} else if (error_code.equals("200008")) {
					Message msg = new Message();
					msg.what = 7;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	/** 获取分析的数据 */
	public void getInfo(final int index) {
		showDialog(DIALOG1_KEY);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String error_code = "00";
				re = FootballInterface.getInstance().getZCInfo(qihaoxinxi[2], batchCode,teamInfos.get(index).num);
				try {
					obj = new JSONObject(re);
					error_code = obj.getString("error_code");
				} catch (Exception e) {

				}
				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				} else if (error_code.equals("000000")) {
					Message msg = new Message();
					msg.what = 22;
					handler.sendMessage(msg);
				} else if (error_code.equals("100000")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else if (error_code.equals("200001")) {
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				} else if (error_code.equals("200002")) {
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else if (error_code.equals("200003")) {
					Message msg = new Message();
					msg.what = 5;
					handler.sendMessage(msg);
				} else if (error_code.equals("200005")) {
					Message msg = new Message();
					msg.what = 6;
					handler.sendMessage(msg);
				} else if (error_code.equals("200008")) {
					Message msg = new Message();
					msg.what = 7;
					handler.sendMessage(msg);
				} else if (error_code.equals("200004")) {
					Message msg = new Message();
					msg.what = 20;
					handler.sendMessage(msg);
				} else if (error_code.equals("200006")) {
					Message msg = new Message();
					msg.what = 21;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	protected void onStart(){
		super.onStart();
	}
	protected void onPause(){
		super.onPause();
	}
	protected void onStop(){
		super.onStop();
	}
	protected void onDestroy(){
		super.onDestroy();
	for (int i = 0; i < ballTables.size(); i++) {
				ballTables.get(i).clearAllHighlights();
			}
	}
	protected void onResume(){
		super.onResume();
	}
	private void initBetPojo() {
		RWSharedPreferences pre = new RWSharedPreferences(FootballSFLottery.this, "addInfo");
		sessionid = pre.getStringValue("sessionid");
		phonenum = pre.getStringValue("phonenum");
		userno = pre.getStringValue("userno");
		betPojo.setPhonenum(phonenum);
		betPojo.setSessionid(sessionid);
		betPojo.setUserno(userno);
		betPojo.setBet_code(getZhuma());
		betPojo.setLotno(lotno);
		betPojo.setBatchnum("1");
		betPojo.setBatchcode(batchCode);
		betPojo.setLotmulti(String.valueOf(iProgressBeishu));
		betPojo.setBettype("bet");
		betPojo.setAmount(getZhuShu()*200+"");
		betPojo.setZhushu(getZhuShu()+"");
	}
	public void toActivity(String zhuma){
		  ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		   try {
		   ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		            objStream.writeObject(betPojo);
		  } catch (IOException e) {
		   return;  // should not happen, so donot do error handling
		  }
		  
		  Intent intent = new Intent(FootballSFLottery.this, GiftActivity.class);
		  intent.putExtra("info", byteStream.toByteArray());
		  intent.putExtra("zhuma", zhuma);
		  startActivity(intent); 
	}
	private String getFormatZhuma(){
		return "第" + batchCode + "期\n" + "截止日期：" + qihaoxinxi[1] + "\n"
				+ "选号结果：\n" + getZhuma();
	}
	/**
	 * 单复式投注调用函数
	 * @param string  显示框信息
	 * @return
	 */
	public void alert(String string,final String zhuma) {
		codeStr = zhuma;
		isGift = false;
		isJoin = false;
		isTouzhu = true;
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v= inflater.inflate(R.layout.alert_dialog_touzhu, null);
		LinearLayout layout = (LinearLayout)v.findViewById(R.id.alert_dialog_touzhu_linear_qihao_beishu);
		layout.setVisibility(LinearLayout.GONE);
		final AlertDialog dialog = new AlertDialog.Builder(this).setTitle("您选择的是").create();
		dialog.show();
		TextView text =(TextView) v.findViewById(R.id.alert_dialog_touzhu_text_one);
		text.setText(string);
		TextView textZhuma =(TextView) v.findViewById(R.id.alert_dialog_touzhu_text_zhuma);
		textZhuma.setText(zhuma);
		Button cancel = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_ok);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
				initBetPojo();
				// TODO Auto-generated method stub
				if(isGift){
					toActivity(zhuma);
				}else if(isJoin){
					toJoinActivity();
				}else if(isTouzhu){
					touZhuNet();
				}
			}
		});
		check = (RadioButton) v.findViewById(R.id.alert_dialog_touzhu_check);
		joinCheck = (RadioButton) v.findViewById(R.id.alert_dialog_join_check);
		RadioButton touzhuCheck = (RadioButton) v.findViewById(R.id.alert_dialog_touzhu1_check);
		touzhuCheck.setChecked(true);
		TextView textAlert = (TextView) v.findViewById(R.id.alert_dialog_touzhu_text_alert);
		check.setPadding(50, 0, 0, 0);
		check.setButtonDrawable(R.drawable.check_select);
		// 实现记住密码 和 复选框的状态
		check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    isGift = isChecked;
		   }
		});
		joinCheck.setPadding(50, 0, 0, 0);
		joinCheck.setButtonDrawable(R.drawable.check_select);
		// 实现记住密码 和 复选框的状态
		joinCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		@Override
	    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                       isJoin = isChecked;
			}
		});
		touzhuCheck.setPadding(50, 0, 0, 0);
	    touzhuCheck.setButtonDrawable(R.drawable.check_select);
		// 实现记住密码 和 复选框的状态
	    touzhuCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
                    isTouzhu = isChecked;
			}
		});

		dialog.getWindow().setContentView(v);
	}
	/**
	 * 投注联网
	 */
	public void touZhuNet(){
		showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
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
				progressdialog.dismiss();
			}

		});
		t.start();
	}
	public void toJoinActivity(){
		  ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		   try {
		   ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		            objStream.writeObject(betPojo);
		  } catch (IOException e) {
		   return;  // should not happen, so donot do error handling
		  }
		  
		  Intent intent = new Intent(FootballSFLottery.this,JoinStartActivity.class);
		  intent.putExtra("info", byteStream.toByteArray());
		  startActivity(intent); 
	
	
	}
	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		for (int i = 0; i < ballTables.size(); i++) {
			ballTables.get(i).clearAllHighlights();
		}
		String lotnoString=PublicMethod.toLotno(lotno);
		PublicMethod.showDialog(FootballSFLottery.this,lotnoString+codeStr);
	}

	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
	
	
	private class AdvanceBatchCode{
		private String BatchCode;
		private String EndTime;

		public String getBatchCode() {
			return BatchCode;
		}
		public void setBatchCode(String batchCode) {
			BatchCode = batchCode;
		}
		public String getEndTime() {
			return EndTime;
		}
		public void setEndTime(String endTime) {
			EndTime = endTime;
		}
	}
	private void  showBatchcodesDialog(String[] batchCodes){
		AlertDialog batchCodedialog =  new AlertDialog.Builder(FootballSFLottery.this)
	      .setTitle("胜负彩预售期")
	      .setItems(batchCodes, new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int which) {
	              /* User clicked so do some stuff */
	        	  getTeamInfo(which);
	          }
	      })
	      .create();
		batchCodedialog.show();
	}
	private void getTeamInfo(int which) {
		AdvanceBatchCode batchMsg = (AdvanceBatchCode)bactchArray.get(which); 
    	  switch (which) {
			case 0:
				layout_football_issue.setTextColor(0xffcc0000);
				break;

			default:
				layout_football_issue.setTextColor(0xff000000);
				break;
    	  }
    	  batchCode = bactchCodes[which];
    	  layout_football_issue.setText(batchMsg.getBatchCode());
    	  layout_football_time.setText(batchMsg.getEndTime());
    
    	  if(list!=null){
    		  list.clear();
    	  }
    	  getData(Constants.LOTNO_SFC, bactchCodes[which]);
	}
	private void getZCAdvanceBatchCodeData(final String Lotno){
		progressdialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				advanceBatchCodeData = FootballLotteryAdvanceBatchcode.getInstance().getAdvanceBatchCodeList(Lotno);
				try {
					JSONObject advanceBatchCode = new JSONObject(advanceBatchCodeData);
					String errorCode = advanceBatchCode.getString("error_code");
					String message = advanceBatchCode.getString("message");
					if(errorCode.equals("0000")){
						JSONArray batchCodeArray = advanceBatchCode.getJSONArray("result");
						bactchArray.clear();
						bactchCodes = new String[batchCodeArray.length()];
						for(int i = 0 ;i<batchCodeArray.length();i++){
							JSONObject item = batchCodeArray.getJSONObject(i);
							AdvanceBatchCode aa = new AdvanceBatchCode();
//							batchCode = item.getString("batchCode");
							aa.setBatchCode(formatBatchCode(item.getString("batchCode")));
							aa.setEndTime(formatEndtime(item.getString("endTime")));
							bactchCodes[i] = item.getString("batchCode");
							bactchArray.add(aa);
							if(qihaoxinxi[1]!=null||qihaoxinxi[1].equals("")){
				        		qihaoxinxi[1] = item.getString("endTime");
				        	}
						}
						Message msg = handler.obtainMessage();
						msg.what = 25;
						msg.obj =  message;
						handler.sendMessage(msg);
					}else{
						  Message msg = handler.obtainMessage();
						  msg.what = 24;
						  msg.obj =  message;
						  handler.sendMessage(msg);
//						Toast.makeText(FootballChooseNineLottery.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}
		}).start();
	}
	
	@Override
	void initBatchCodeView() {
		// TODO Auto-generated method stub
		layout_football_issue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getZCAdvanceBatchCodeData(Constants.LOTNO_SFC);
			}
		});
//		layout_football_issue.setTextColor(0xffcc0000);
//		layout_football_issue.setText(formatBatchCode(qihaoxinxi[0]));
//		layout_football_time.setText(formatEndtime(qihaoxinxi[1]));
	}
	
}
