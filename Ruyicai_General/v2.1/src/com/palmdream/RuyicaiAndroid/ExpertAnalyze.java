package com.palmdream.RuyicaiAndroid;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.RuyiHelper.ExpertAnalyzeEfficientAdapter;
import com.palmdream.netintface.iHttp;
import com.palmdream.netintface.jrtLot;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class ExpertAnalyze extends Activity implements MyDialogListener {

	public String phonenum;
	public String sessionid;
	String re;
	String error_code = "00";
	JSONObject obj;
	JSONArray jsonArray;
	JSONArray jsonArray_gifted;
	int iretrytimes = 2;
	String type;

	/* 专家分析 */
	private static final String[] expertAnalyzeTypeName = { "双色球分析", "福彩3D分析",
			"七乐彩分析", "排列三分析", "超级大乐透分析" }; // zlm 排列三、超级大乐透分析
	// private static final String[] expertAnalyzeMore = {">>>>",">>>>",">>>>"};
	public final static int ID_EXPERTANALYZE = 30;/* 专家分析列表 */
	public final static int ID_EXPERTANALYZE_SSQ = 31;/* 双色球专家分析 */
	public final static int ID_EXPERTANALYZE_FC3D = 32;/* 福彩3D专家分析 */
	public final static int ID_EXPERTANALYZE_QLC = 33;/* 七乐彩专家分析 */
	TextView typeName;
	public final static String SUB_EXPERT_ANALYZE_TITLE = "SUB_EXPERT_ANALYZE_TITLE"; // 周黎鸣
	// 7.4：代码添加
	List<Map<String, Object>> subExpertAnalyzeList; // 周黎鸣 7.4：代码添加: 列表适配器的数据源
	public static String[] subExpertAnalyzeTitle = { "张三20100910期分析",
			"李二20100910期分析", "王五20100910期分析", "孙提20100910期分析" };
	int expertId;
	String[] specifyExpertAnalyzeInfo;
	SpeechListAdapter adapterExpert; // 专家分析子列表适配器
	// 周黎鸣 7.4：代码添加:
	public String[] mTitles = { "张三20100910期分析", "李二20100910期分析",
			"王五20100910期分析", "孙提20100910期分析" };
	// 周黎鸣 7.4：代码添加:
	public String[] mDialogue = {
			"So shaken as we are, so wan with care,"
					+ "What yesternight our council did decree"
					+ "In forwarding this dear expedience.",

			"Hear him but reason in divinity,"
					+ "From open haunts and popularity.",

			"I come no more to make you laugh: things now,"
					+ "A man may weep upon his wedding-day.",

			"First, heaven be the record to my speech!"
					+ "In the devotion of a subject's love,"
					+ "And wish, so please my sovereign, ere I move,"
					+ "What my tongue speaks my right drawn sword may prove." };

	private static final int DIALOG1_KEY = 0;
	ProgressDialog progressdialog;

	private ImageButton btnreturn;/* 返回 */// 周黎鸣 7.3 代码修改：将Button换成ImageButton

	List<Map<String, Object>> list;/* 列表适配器的数据源 */
	TextView text;/* 标题 */

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 7:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				type = "";
				Intent intentSession = new Intent(ExpertAnalyze.this,
						UserLogin.class);
				startActivityForResult(intentSession, 0);
				// Toast.makeText(RuyiHelper.this, "请登录",
				// Toast.LENGTH_SHORT).show();
				break;
			case 8:
				progressdialog.dismiss();
				Toast.makeText(ExpertAnalyze.this, "系统结算", Toast.LENGTH_SHORT)
						.show();
				break;
			case 9:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_LONG)
						.show();
				break;
			case 10:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "查询失败", Toast.LENGTH_LONG)
						.show();
				break;

			case 12:
				progressdialog.dismiss();
				Toast.makeText(ExpertAnalyze.this, "记录为空", Toast.LENGTH_SHORT)
						.show();
				break;

			case 18:
				progressdialog.dismiss();
				showSubExpertAnalyzeListViewTwo();
				break;
			case 19:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "暂时没有专家分析", Toast.LENGTH_LONG)
						.show();
				break;
			case 20:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_LONG)
						.show();
				break;
			}
		}
	};
	// 周黎鸣 7.5 代码修改：添加退出检测的代码————标记
	private int iQuitFlag = 0; // 代表退出

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		showExpertAnalyzeListView();

	}

	private void showExpertAnalyzeListView() {
		setContentView(R.layout.expert_analyze_main_layout);
		PublicMethod.myOutput("-----------Analyze!----------------");
		// 周黎鸣 7.3 代码修改：将Button换成ImageButton
		/*
		 * ImageButton returnBtn = new ImageButton(this); returnBtn =
		 * (ImageButton) findViewById(R.id.expert_analyze_return_id);
		 * returnBtn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub // showListView(ID_MORELISTVIEW); } });
		 */
		ListView listview = (ListView) findViewById(R.id.expert_analyze_listview_id);

		ExpertAnalyzeEfficientAdapter adapterExpertAnalyze = new ExpertAnalyzeEfficientAdapter(
				this);
		listview.setAdapter(adapterExpertAnalyze);
		PublicMethod.myOutput("-----------Analyze!----------------");
		// 设置点击监听
		OnItemClickListener clickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				typeName = (TextView) view
						.findViewById(R.id.expert_analyze_typename_id);
				String str = typeName.getText().toString();
				// 点击双色球跳转到双色球子列表中 周黎鸣 7.4 代码修改：跳转到专家分析子列表
				if (getString(R.string.shuangseqiufenxi).equals(str)) {
					iQuitFlag = 0; // 周黎鸣 7.5 代码修改：代表退出 //
					// iCallOnKeyDownFlag=false;
					// showSubExpertAnalyzeListViewTwo(subExpertAnalyzeTitle ,
					// ID_EXPERTANALYZE_SSQ);
					// 网络获取双色球专家分析的内容 2010/7/4 陈晨
					// 如果内容为空 时的处理 20100711
					try {
						iHttp.whetherChange = false;
						showDialog(DIALOG1_KEY);
						String[] analysis = expertAnalysis();
						if (analysis[0].equals("06007")
								|| analysis[0].equals("06008")) {
							Message msg = new Message();
							msg.what = 19;
							handler.sendMessage(msg);

						} else if (analysis[0].equals("00")) {
							Message msg = new Message();
							msg.what = 20;
							handler.sendMessage(msg);
						} else {
							for (int i = 0; i < analysis.length; i++) {
								PublicMethod.myOutput("------------------"
										+ analysis[i]);
								// Toast.makeText(getBaseContext(), analysis[i],
								// Toast.LENGTH_LONG).show();
							}
							String[] analysis_title = { analysis[0],
									analysis[2], analysis[4] };
							String[] analysis_content = { analysis[1],
									analysis[3], analysis[5] };
							mTitles = analysis_title;
							mDialogue = analysis_content;
							Message msg = new Message();
							msg.what = 18;
							handler.sendMessage(msg);
							// showSubExpertAnalyzeListViewTwo();
						}
					} catch (Exception e) {
						Message msg = new Message();
						msg.what = 19;
						handler.sendMessage(msg);
						e.printStackTrace();

						// }
						// showExpertAnalyzeDialog(ID_EXPERTANALYZE_SSQ);
					}
				}
				// 点击福彩3D跳转到福彩3D子列表中 周黎鸣 7.4 代码修改：跳转到专家分析子列表
				if (getString(R.string.fucaifenxi).equals(str)) {
					iQuitFlag = 20; // 周黎鸣 7.5 代码修改：代表返回主列表
					// iCallOnKeyDownFlag=false;
					// showExpertAnalyzeDialog(ID_EXPERTANALYZE_FC3D);
					try {
						iHttp.whetherChange = false;
						showDialog(DIALOG1_KEY);
						String[] analysis = expertAnalysis();
						if (analysis[0].equals("06007")
								|| analysis[0].equals("06008")) {
							Message msg = new Message();
							msg.what = 19;
							handler.sendMessage(msg);
						} else if (analysis[0].equals("00")) {
							Message msg = new Message();
							msg.what = 20;
							handler.sendMessage(msg);
						} else {
							for (int i = 0; i < 10; i++) {
								PublicMethod.myOutput("------------------"
										+ analysis[i]);
							}
							String[] analysis_title = { analysis[6],
									analysis[8], analysis[10] };
							String[] analysis_content = { analysis[7],
									analysis[9], analysis[11] };
							mTitles = analysis_title;
							mDialogue = analysis_content;
							Message msg = new Message();
							msg.what = 18;
							handler.sendMessage(msg);
							// showSubExpertAnalyzeListViewTwo();
						}
					} catch (Exception e) {
						Message msg = new Message();
						msg.what = 19;
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}
				// 点击七乐彩跳转到七乐彩子列表中 周黎鸣 7.4 代码修改：跳转到专家分析子列表
				if (getString(R.string.qilecaifenxi).equals(str)) {
					iQuitFlag = 20; // 周黎鸣 7.5 代码修改：代表返回主列表
					// iCallOnKeyDownFlag=false;
					// showExpertAnalyzeDialog(ID_EXPERTANALYZE_QLC);
					try {
						iHttp.whetherChange = false;
						showDialog(DIALOG1_KEY);
						String[] analysis = expertAnalysis();
						if (analysis[0].equals("06007")
								|| analysis[0].equals("06008")) {
							Message msg = new Message();
							msg.what = 19;
							handler.sendMessage(msg);
						} else if (analysis[0].equals("00")) {
							Message msg = new Message();
							msg.what = 20;
							handler.sendMessage(msg);
						} else {
							for (int i = 0; i < 10; i++) {
								PublicMethod.myOutput("------------------"
										+ analysis[i]);
							}
							String[] analysis_title = { analysis[12],
									analysis[14], analysis[16] };
							String[] analysis_content = { analysis[13],
									analysis[15], analysis[17] };
							mTitles = analysis_title;
							mDialogue = analysis_content;
							Message msg = new Message();
							msg.what = 18;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						Message msg = new Message();
						msg.what = 19;
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}
				// zlm 排列三
				if (getString(R.string.pailiesanfenxi).equals(str)) {
					iQuitFlag = 20;
					// showSubExpertAnalyzeListViewTwo();
					try {
						iHttp.whetherChange = false;
						showDialog(DIALOG1_KEY);
						String[] analysis = expertAnalysis();
						if (analysis[0].equals("06007")
								|| analysis[0].equals("06008")) {
							Message msg = new Message();
							msg.what = 19;
							handler.sendMessage(msg);
						} else if (analysis[0].equals("00")) {
							Message msg = new Message();
							msg.what = 20;
							handler.sendMessage(msg);
						} else {
							for (int i = 0; i < 10; i++) {
								PublicMethod.myOutput("------------------"
										+ analysis[i]);
							}
							String[] analysis_title = { analysis[18],
									analysis[20], analysis[22] };
							String[] analysis_content = { analysis[19],
									analysis[21], analysis[23] };
							mTitles = analysis_title;
							mDialogue = analysis_content;
							Message msg = new Message();
							msg.what = 18;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						Message msg = new Message();
						msg.what = 19;
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}
				// zlm 超级大乐透
				if (getString(R.string.chaojidaletoufenxi).equals(str)) {
					iQuitFlag = 20;
					// showSubExpertAnalyzeListViewTwo();
					try {
						iHttp.whetherChange = false;
						showDialog(DIALOG1_KEY);
						String[] analysis = expertAnalysis();
						if (analysis[0].equals("06007")
								|| analysis[0].equals("06008")) {
							Message msg = new Message();
							msg.what = 19;
							handler.sendMessage(msg);
						} else if (analysis[0].equals("00")) {
							Message msg = new Message();
							msg.what = 20;
							handler.sendMessage(msg);
						} else {
							for (int i = 0; i < 10; i++) {
								PublicMethod.myOutput("------------------"
										+ analysis[i]);
							}
							String[] analysis_title = { analysis[24],
									analysis[26], analysis[28] };
							String[] analysis_content = { analysis[25],
									analysis[27], analysis[29] };
							mTitles = analysis_title;
							mDialogue = analysis_content;
							Message msg = new Message();
							msg.what = 18;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						Message msg = new Message();
						msg.what = 19;
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}
			}

		};

		listview.setOnItemClickListener(clickListener);
	}

	/**
	 * 专家分析子列表 周黎鸣 7.4 代码添加：专家分析子列表
	 * 
	 * @param aExpertAnalyzeInfo
	 */
	private void showSubExpertAnalyzeListViewTwo() {
		/*
		 * expertId = aExpertId; specifyExpertAnalyzeInfo = aExpertAnalyzeInfo;
		 */
		iQuitFlag = 20;// 返回主列表
		ScrollableTabActivity.gone();// 隐藏顶部标签
		setContentView(R.layout.expert_analyze_specify_listview);
		ListView listview = (ListView) findViewById(R.id.expert_analyze_specify_listview_id);

		ImageButton returnBtn = new ImageButton(this);
		returnBtn = (ImageButton) findViewById(R.id.expert_analyze_specify_return_btn);
		returnBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScrollableTabActivity.visible();// 显示顶部标签
				showExpertAnalyzeListView();
			}
		});
		adapterExpert = new SpeechListAdapter(this);
		listview.setAdapter(adapterExpert);
		listview.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// position = subExpertAnalyzeList.size();
				// for (int i = 0; i < mTitles.length; i++) {
				// if (position == i) {
				// // showExpertAnalyzeDialog(expertId);
				// adapterExpert.toggle(i);
				// }
				// }
			}
		});
		for (int i = 0; i < mTitles.length; i++) {
			// showExpertAnalyzeDialog(expertId);
			adapterExpert.toggle(i);

		}
	}

	// 周黎鸣 7.4 代码添加：专家分析子列表 适配器
	private class SpeechListAdapter extends BaseAdapter {
		private Context mContext;

		public SpeechListAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			return mTitles.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			SpeechView sv;
			if (convertView == null) {
				sv = new SpeechView(mContext, mTitles[position],
						mDialogue[position], mExpanded[position]);
				// convertView =
				// mInflater.inflate(R.layout.notice_prizes_main_layout, null);
			} else {
				sv = (SpeechView) convertView;// .findViewById(R.id.expert_analyze_specify_listview_layout_text_id);
				sv.setTitle(mTitles[position]);
				sv.setDialogue(mDialogue[position]);
				sv.setExpanded(mExpanded[position]);
			}

			return sv;
		}

		public void toggle(int position) {
			mExpanded[position] = !mExpanded[position];
			notifyDataSetChanged();
		}

		private boolean[] mExpanded = { false, false, false, false, false,
				false, false, false };

	}

	// 周黎鸣 7.4 代码添加：专家分析子列表数据
	private class SpeechView extends LinearLayout {
		public SpeechView(Context context, String title, String dialogue,
				boolean expanded) {
			super(context);

			this.setOrientation(VERTICAL);

			// Here we build the child views in code. They could also have
			// been specified in an XML file.

			mTitle = new TextView(context);
			mTitle.setText(title);
			mTitle.setHeight(40);
			mTitle.setTextColor(Color.BLACK);
			mTitle.setTypeface(Typeface.SERIF);
			mTitle.setTextSize(15);
			mTitle.setPadding(80, 10, 0, 0);
			mTitle.setTextAppearance(context,
					android.R.attr.textAppearanceMedium);
			addView(mTitle, new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// 拆分字符串
			String[] str = dialogue.split("，");
			Log.e("str==", "" + str.length);
			String words_new = "";
			for (int i = 0; i < str.length; i++) {
				if (i != str.length - 1) {
					words_new += str[i] + "\n";
				} else {
					words_new += str[i];
				}
			}
			mDialogue = new TextView(context);
			mDialogue.setText(words_new);
			mDialogue.setTextColor(Color.BLUE);
			mDialogue.setTextSize(14);
			mDialogue.setPadding(0, 0, 0, 10);
			addView(mDialogue, new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			mDialogue.setVisibility(expanded ? VISIBLE : GONE);
		}

		public void setTitle(String title) {
			mTitle.setText(title);
		}

		public void setDialogue(String words) {
			String[] str = words.split("，");
			String words_new = "";
			for (int i = 0; i < str.length; i++) {
				if (i != str.length - 1) {
					words_new += str[i] + "\n";
				} else {
					words_new += str[i];
				}
			}
			mDialogue.setText(words_new);
		}

		public void setExpanded(boolean expanded) {
			mDialogue.setVisibility(expanded ? VISIBLE : GONE);
		}

		private TextView mTitle;
		private TextView mDialogue;
	}

	// 专家分析联网获取全部信息 2010/7/4 陈晨

	public String[] expertAnalysis() {

		String contents = null;
		String[] str = { "", "" };
		while (iretrytimes < 3 && iretrytimes > 0) {
			try {
				String re = jrtLot.analysis(sessionid);
				PublicMethod.myOutput("-----------------re:" + re);
				// if (re != null && !re.equalsIgnoreCase("")) {
				JSONObject obj = new JSONObject(re);
				contents = obj.getString("content");
				// str=this.mySplict(contents,'|');//切割字符串
				str = mySplict(contents, '|');
				// } else {
				// str[0] = "00";
				// }
				iretrytimes = 3;
			} catch (JSONException e) {
				iretrytimes--;
				// str[0] = "00";
			}

		}
		// 加入是否改变切入点判断 陈晨 8.11
		if (iretrytimes == 0 && iHttp.whetherChange == false) {
			iHttp.whetherChange = true;
			if (iHttp.conMethord == iHttp.CMWAP) {
				iHttp.conMethord = iHttp.CMNET;
			} else {
				iHttp.conMethord = iHttp.CMWAP;
			}
			iretrytimes = 2;
			expertAnalysis();
		}
		// str = mySplict(contents, '|');
		if (iretrytimes == 0 && iHttp.whetherChange == true) {
			str[0] = "00";
		}
		iretrytimes = 2;

		// for(int i=0;i<analysis.length;i++){
		// Toast.makeText(context, analysis[i], Toast.LENGTH_SHORT).show();
		// }
		return str;
	}

	// 切割字符串方法 专家分析 2010/7/4 陈晨
	@SuppressWarnings("finally")
	private String[] mySplict(String str, char chr) {
		String[] data = null;
		try {
			// vector性能很低,用System.arraycopy来代替vector;上网查System.arraycopy的使用方法和优点
			Vector vector = new Vector();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (chr == c) {
					// PublicMethod.myOutput(i);
					vector.addElement(new Integer(i));
				}
			}
			// 字符串中没有要切割的字符
			if (vector.size() == 0) {
				data = new String[] { str };
			}
			// int =((Integer)vector.elementAt(0)).intValue();
			// int index2=((Integer)vector.elementAt(1)).intValue();
			// String user1=str.substring(0,index );
			// PublicMethod.myOutput(user1);
			//	
			// String user2=str.substring(index+1, index2);
			// PublicMethod.myOutput(user2);
			//	
			// String user3=str.substring(index2+1);
			// PublicMethod.myOutput(user3);
			if (vector.size() >= 1) {
				data = new String[vector.size() + 1];
			}
			for (int i = 0; i < vector.size(); i++) {
				int index = ((Integer) vector.elementAt(i)).intValue();
				String temp = "";
				if (i == 0)// 第一个#
				{
					if (vector.size() == 1) {
						temp = str.substring(index + 1);
						data[1] = temp;
					}
					temp = str.substring(0, index);
					// PublicMethod.myOutput(temp);
					data[0] = temp;
				} else if (i == vector.size() - 1)// //最后一个#
				{
					int preIndex = ((Integer) vector.elementAt(i - 1))
							.intValue();
					temp = str.substring(preIndex + 1, index);// 最后一个#前面的内容
					// PublicMethod.myOutput(temp);
					data[i] = temp;

					temp = str.substring(index + 1);// 最后一个#后面的内容
					// PublicMethod.myOutput(temp);
					data[i + 1] = temp;
				} else {
					int preIndex = ((Integer) vector.elementAt(i - 1))
							.intValue();
					temp = str.substring(preIndex + 1, index);// 最后一个#前面的内容
					// PublicMethod.myOutput(temp);
					data[i] = temp;
				}

			}
			// 测试用的
			// if (data != null) {
			// for (int i = 0; i < data.length; i++) {
			// //PublicMethod.myOutput(i + "=" + data[i]);
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return data;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("--->>NoticePrizesOfLottery key:"
				+ String.valueOf(keyCode));
		switch (keyCode) {
		case 4: {
			break;
		}
			// 周黎鸣 7.8 代码修改：添加新的判断
		case 0x12345678: {
			/*
			 * if(iCallOnKeyDownFlag==false){ iCallOnKeyDownFlag=true;}
			 */
			switch (iQuitFlag) {
			case 0:
				WhetherQuitDialog iQuitDialog = new WhetherQuitDialog(this,
						this);
				iQuitDialog.show();
				break;
			case 20:
				ScrollableTabActivity.visible();// 显示顶部标签
				setContentView(R.layout.expert_analyze_main_layout);
				showExpertAnalyzeListView();
				break;

			}
			break;
		}
		}
		return false;
		// return super.onKeyDown(keyCode, event);
	}

	// 网络连接框
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			// progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}

	@Override
	public void onCancelClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOkClick(int[] nums) {
		// TODO Auto-generated method stub
		finish();

	}

}
