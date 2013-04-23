package com.palmdream.RuyicaiAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.netintface.iHttp;
import com.palmdream.netintface.jrtLot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class QueryInfo extends Activity {
	private int TYPE;// 查询类型
	public static final int JOIN = 1;// 参与合买
	public static final int BUY = 2;// 发起合买
	public static final int DETAILS = 3;// 参与合买详情
	public static final int BUY_DETAILS = 4;// 发起合买详情
	private ProgressDialog progressdialog;
	private JSONObject obj;
	private String re;
	private int iretrytimes = 2;
	private String mobile_code;// 手机号
	private String sessionid;
	private String batch = "";// 期号
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	public final static String TITLE1 = "TITLE1"; /* 标题 */
	public final static String TITLE2 = "TITLE2"; /* 标题 */
	public final static String TITLE3 = "TITLE3"; /* 标题 */
	public final static String TITLE4 = "TITLE4"; /* 标题 */
	private boolean start = false;
	private static ArrayList<String[]> infos = new ArrayList<String[]>();// 需要加载数据
	private static ArrayList<String[]> detInfos = new ArrayList<String[]>();// 参加合买的详细信息
	private int index = 0;
	/* 联网 */
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				progressdialog.dismiss();
				Log.e("infos", "" + infos.size());
				initList();
				break;
			case 1:
				progressdialog.dismiss();
				Toast.makeText(QueryInfo.this, "网络异常！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				progressdialog.dismiss();
				Toast.makeText(QueryInfo.this, "查询结果为空！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				progressdialog.dismiss();
				String str = "";

				str = turnString(detInfos.get(0), true);
				if (TYPE == BUY) {
					if (detInfos.get(0)[3].equals("进行中")) {
						alert(str, detInfos.get(0)[15], true);
					} else {
						alert(str, "", false);
					}
				} else {
					alert(str, "", false);
				}

				break;
			case 4:
				progressdialog.dismiss();
				Toast.makeText(QueryInfo.this, "进度已>=50%，超出撤销范围！",
						Toast.LENGTH_SHORT).show();
				break;
			case 5:
				progressdialog.dismiss();
				Toast.makeText(QueryInfo.this, "合买撤销成功！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 6:
				progressdialog.dismiss();
				Toast.makeText(QueryInfo.this, "方案为撤销状态！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 7:
				progressdialog.dismiss();
				Toast.makeText(QueryInfo.this, "金额解冻失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 8:
				progressdialog.dismiss();
				String str1 = "";
				for (int i = 0; i < detInfos.size(); i++) {
					str1 += turnString(detInfos.get(i), false);
				}
				alert(str1, "", true);
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				QueryInfo.this, "addInfo");
		sessionid = shellRW.getUserLoginInfo("sessionid");
		mobile_code = shellRW.getUserLoginInfo("phonenum");
		setContentView(R.layout.query_info_list_layout);
		TextView title = (TextView) findViewById(R.id.ruyipackage_title);
		ImageButton back = (ImageButton) findViewById(R.id.ruyizhushou_btn_return);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// checkJoin();
				finish();
			}
		});
		initList();
		// 读取数据
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			TYPE = bundle.getInt("type");// 读出数据
		}
		if (TYPE == JOIN) {
			title.setText("合买>>参与记录");
			checkJoin();
		} else {
			title.setText("合买>>发起记录");
			checkBuy();
		}
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		infos = new ArrayList<String[]>();
		detInfos = new ArrayList<String[]>();
	}

	/**
	 * 初始化列表
	 */
	private void initList() {
		// 初始化list
		// 数据源
		list = getListForJoinAdapter();

		ListView listview = (ListView) findViewById(R.id.query_info_list);
		// listview.setDividerHeight(0);

		// 适配器
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.query_info_list_item, new String[] { TITLE1, TITLE2,
						TITLE3, TITLE4 }, new int[] { R.id.query_info_text1,
						R.id.query_info_text2, R.id.query_info_text3,
						R.id.query_info_text4 });

		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);

		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				index = position;
				Log.e("TYPE====", "" + TYPE);
				if (TYPE == JOIN | TYPE == DETAILS) {
					joinDetails(infos.get(position)[4]);
				} else {
					buyDetails(infos.get(position)[4]);
				}
			}

		};
		listview.setOnItemClickListener(clickListener);
	}

	public List<Map<String, Object>> getListForJoinAdapter() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		if (infos.size() != 0) {
			for (int i = 0; i < infos.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(TITLE1, infos.get(i)[0]);
				map.put(TITLE2, infos.get(i)[1]);
				map.put(TITLE3, infos.get(i)[2]);
				map.put(TITLE4, infos.get(i)[3]);
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 
	 * 方案详情信息对话框
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	private void alert(String string, final String case_caseId, boolean isOk) {

		start = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.query_info_dialog, null);
		TextView text = (TextView) view
				.findViewById(R.id.query_info_dialog_text);
		text.setText(string);
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("方案详情");
		dialog.setView(view);
		if (isOk) {
			dialog.setPositiveButton("我要撤单",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (start) {
								start = false;
								remove(case_caseId);
							}
						}
					});
		}
		if ((TYPE == JOIN | TYPE == DETAILS) && isOk) {
			dialog.setPositiveButton("详情",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (start) {
								start = false;
								buyDetails(infos.get(index)[4]);
							}
						}
					});
		}
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		dialog.show();

	}

	/**
	 * 将字符串数组转换成要求字符串
	 * 
	 * @param str
	 * @return
	 */
	public String turnString(String str[], boolean begin) {
		if (begin) {
			return "方案描述：" + str[4] + "\n" + "方案发起人：" + str[5] + "\n"
					+ "发起人提成：" + str[6] + "\n" + "方案进度：" + str[2] + "\n"
					+ "已参与：" + str[7] + "\n" + "方案内容：" + "\n" + str[8] + "\n"
					+ "彩种期号：" + str[0] + "  " + str[9] + "\n" + "倍数金额："
					+ str[10] + str[1] + "\n" + "保底份数：" + str[11] + "\n"
					+ "每份金额：" + str[12] + "\n" + "已认购份数：" + str[13] + "\n"
					+ "剩余份数：" + str[14] + "\n";
		} else {
			return "方案编号：" + str[0] + "\n" + "用户编号：" + str[1] + "\n" + "认购份数："
					+ str[2] + "\n" + "认购金额：" + str[3] + "\n" + "认购时间："
					+ str[4] + "\n" + "\n";
		}

	}

	/**
	 * 参与合买查询联网
	 */
	private void checkJoin() {

		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				while (iretrytimes < 3 && iretrytimes > 0) {

					re = jrtLot.checkJoin(mobile_code, sessionid);

					try {
						obj = new JSONObject(re);
						error_code = obj.getString("error_code");
						iretrytimes = 3;
					} catch (JSONException e) {
						try {
							error_code = "0000";
							JSONArray objArray = new JSONArray(re);
							infos = parseInfo(objArray);
							iretrytimes = 3;

						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							iretrytimes--;
						}
					}

					if (iretrytimes == 0 && iHttp.whetherChange == false) {
						iHttp.whetherChange = true;
						if (iHttp.conMethord == iHttp.CMWAP) {
							iHttp.conMethord = iHttp.CMNET;
						} else {
							iHttp.conMethord = iHttp.CMWAP;
						}
						iretrytimes = 2;
						while (iretrytimes < 3 && iretrytimes > 0) {
							re = jrtLot.checkJoin(mobile_code, sessionid);
							try {
								obj = new JSONObject(re);
								error_code = obj.getString("error_code");
								iretrytimes = 3;
							} catch (JSONException e) {
								try {
									error_code = "0000";
									JSONArray objArray = new JSONArray(re);
									infos = parseInfo(objArray);
									iretrytimes = 3;

								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									iretrytimes--;
								}
							}
						}
					}

				}
				iretrytimes = 2;
				Log.e("error_code==", error_code);
				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);

				} else if (error_code.equals("0000")) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				} else if (error_code.equals("400001")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}

			}

		}).start();
	}

	/**
	 * 发起合买查询联网
	 */
	private void checkBuy() {
		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				while (iretrytimes < 3 && iretrytimes > 0) {

					re = jrtLot.checkBuy(mobile_code, batch, "", sessionid);

					try {
						obj = new JSONObject(re);
						error_code = obj.getString("error_code");
						iretrytimes = 3;
					} catch (JSONException e) {
						try {
							error_code = "0000";
							JSONArray objArray = new JSONArray(re);
							infos = parseInfo(objArray);
							iretrytimes = 3;

						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							iretrytimes--;
						}
					}

					if (iretrytimes == 0 && iHttp.whetherChange == false) {
						iHttp.whetherChange = true;
						if (iHttp.conMethord == iHttp.CMWAP) {
							iHttp.conMethord = iHttp.CMNET;
						} else {
							iHttp.conMethord = iHttp.CMWAP;
						}
						iretrytimes = 2;
						while (iretrytimes < 3 && iretrytimes > 0) {
							re = jrtLot.checkBuy(mobile_code, batch, "",
									sessionid);
							try {
								obj = new JSONObject(re);
								error_code = obj.getString("error_code");
								iretrytimes = 3;
							} catch (JSONException e) {
								try {
									error_code = "0000";
									JSONArray objArray = new JSONArray(re);
									infos = parseInfo(objArray);
									iretrytimes = 3;

								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									iretrytimes--;
								}
							}
						}
					}

				}
				iretrytimes = 2;
				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);

				} else if (error_code.equals("0000")) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				} else if (error_code.equals("400001")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}

			}

		}).start();
	}

	/**
	 * 参与合买详细信息联网
	 */
	private void joinDetails(final String case_caseId) {
		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				while (iretrytimes < 3 && iretrytimes > 0) {

					re = jrtLot
							.joinDetails(mobile_code, case_caseId, sessionid);

					try {
						obj = new JSONObject(re);
						error_code = obj.getString("error_code");
						iretrytimes = 3;
					} catch (JSONException e) {
						try {
							error_code = "0000";
							JSONArray objArray = new JSONArray(re);
							TYPE = DETAILS;
							detInfos = parseInfo(objArray);
							iretrytimes = 3;

						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							iretrytimes--;
						}
					}

					if (iretrytimes == 0 && iHttp.whetherChange == false) {
						iHttp.whetherChange = true;
						if (iHttp.conMethord == iHttp.CMWAP) {
							iHttp.conMethord = iHttp.CMNET;
						} else {
							iHttp.conMethord = iHttp.CMWAP;
						}
						iretrytimes = 2;
						while (iretrytimes < 3 && iretrytimes > 0) {
							re = jrtLot.joinDetails(mobile_code, case_caseId,
									sessionid);

							try {
								obj = new JSONObject(re);
								error_code = obj.getString("error_code");
								iretrytimes = 3;
							} catch (JSONException e) {
								try {
									error_code = "0000";
									JSONArray objArray = new JSONArray(re);
									TYPE = DETAILS;
									detInfos = parseInfo(objArray);
									iretrytimes = 3;

								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									iretrytimes--;
								}
							}
						}
					}

				}
				iretrytimes = 2;
				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);

				} else if (error_code.equals("400001")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else if (error_code.equals("0000")) {
					Message msg = new Message();
					msg.what = 8;
					handler.sendMessage(msg);
				}

			}

		}).start();
	}

	/**
	 * 发起合买详细信息联网
	 */
	private void buyDetails(final String case_caseId) {
		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				while (iretrytimes < 3 && iretrytimes > 0) {

					re = jrtLot.joinInfo(mobile_code, case_caseId, sessionid);

					try {
						obj = new JSONObject(re);
						error_code = obj.getString("error_code");
						iretrytimes = 3;
					} catch (JSONException e) {
						try {
							error_code = "0000";
							obj = new JSONObject(re);
							Log.e("====", obj.toString());

							detInfos = parseInfo(obj);
							iretrytimes = 3;

						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							iretrytimes--;
						}
					}

					if (iretrytimes == 0 && iHttp.whetherChange == false) {
						iHttp.whetherChange = true;
						if (iHttp.conMethord == iHttp.CMWAP) {
							iHttp.conMethord = iHttp.CMNET;
						} else {
							iHttp.conMethord = iHttp.CMWAP;
						}
						iretrytimes = 2;
						while (iretrytimes < 3 && iretrytimes > 0) {
							re = jrtLot.joinInfo(mobile_code, case_caseId,
									sessionid);

							try {
								obj = new JSONObject(re);
								error_code = obj.getString("error_code");
								iretrytimes = 3;
							} catch (JSONException e) {
								try {
									error_code = "0000";
									obj = new JSONObject(re);
									Log.e("====", obj.toString());

									detInfos = parseInfo(obj);
									iretrytimes = 3;

								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									iretrytimes--;
								}
							}
						}
					}

				}
				iretrytimes = 2;
				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);

				} else if (error_code.equals("400001")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else if (error_code.equals("0000")) {
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				}

			}

		}).start();
	}

	/**
	 * 撤销合买联网
	 */
	private void remove(final String case_caseId) {
		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				while (iretrytimes < 3 && iretrytimes > 0) {

					re = jrtLot.remove(mobile_code, case_caseId, sessionid);

					try {
						obj = new JSONObject(re);
						error_code = obj.getString("error_code");
						iretrytimes = 3;
					} catch (JSONException e) {

						iretrytimes--;
					}

					if (iretrytimes == 0 && iHttp.whetherChange == false) {
						iHttp.whetherChange = true;
						if (iHttp.conMethord == iHttp.CMWAP) {
							iHttp.conMethord = iHttp.CMNET;
						} else {
							iHttp.conMethord = iHttp.CMWAP;
						}
						iretrytimes = 2;
						while (iretrytimes < 3 && iretrytimes > 0) {
							re = jrtLot.remove(mobile_code, case_caseId,
									sessionid);

							try {
								obj = new JSONObject(re);
								error_code = obj.getString("error_code");
								iretrytimes = 3;
							} catch (JSONException e) {
								iretrytimes--;
							}
						}
					}

				}
				iretrytimes = 2;
				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);

				} else if (error_code.equals("400001")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else if (error_code.equals("000")) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				} else if (error_code.equals("400006")) {
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else if (error_code.equals("000000")) {
					Message msg = new Message();
					msg.what = 5;
					handler.sendMessage(msg);
				} else if (error_code.equals("400004")) {
					Message msg = new Message();
					msg.what = 6;
					handler.sendMessage(msg);
				} else if (error_code.equals("400007")) {
					Message msg = new Message();
					msg.what = 7;
					handler.sendMessage(msg);
				}

			}

		}).start();
	}

	/**
	 * 解析数据
	 * 
	 * @param json
	 * @return
	 */
	private ArrayList<String[]> parseInfo(JSONArray json) {
		ArrayList list = new ArrayList<String[]>();
		if (TYPE == JOIN | TYPE == BUY) {
			for (int i = 0; i < json.length(); i++) {
				String str[] = new String[5];
				try {
					JSONObject obj = json.getJSONObject(i);
					str[0] = obj.getString("lotNo");
					str[1] = obj.getString("caseAllAmt");
					str[2] = obj.getString("caseBuyAfterAmt");
					str[3] = obj.getString("flag");
					str[4] = obj.getString("id");
					str[0] = parseLotNo(str[0]);
					str[1] = parseAmt(str[1]);
					str[2] = parseProgress(str[2]);
					str[3] = parseFlag(str[3]);
					list.add(str);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (TYPE == DETAILS) {
			for (int i = 0; i < json.length(); i++) {
				String str[] = new String[5];
				try {
					JSONObject obj = json.getJSONObject(i);
					str[0] = obj.getString("caseId");
					str[1] = obj.getString("userNo");
					str[2] = obj.getString("buyNumByUser");
					str[3] = obj.getString("buyAmtByUser");
					str[4] = obj.getString("buyTime");
					str[2] += "份";
					str[3] = parseAmt(str[3]);
					list.add(str);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return list;

	}

	/**
	 * 解析金额
	 * 
	 * @param str
	 * @return
	 */
	private String parseAmt(String str) {
		return Integer.toString(Integer.parseInt(str) / 100) + "元";
	}

	/**
	 * 解析彩种
	 * 
	 * @param str
	 * @return
	 */
	private String parseLotNo(String str) {
		if (str.equals("B001")) {
			str = "双色球";
		} else if (str.equals("D3")) {
			str = "福彩3D";
		} else if (str.equals("QL730")) {
			str = "七乐彩";
		}
		return str;
	}

	/**
	 * 解析进度
	 * 
	 * @param str
	 * @return
	 */
	private String parseProgress(String str) {
		float caseBuyAfterAmt = Float.parseFloat(str);
		if (caseBuyAfterAmt * 100 < 100 && caseBuyAfterAmt * 100 + 1 < 100) {// 0.001
			caseBuyAfterAmt = caseBuyAfterAmt * 1000 % 10 > 0 ? caseBuyAfterAmt * 100 + 1
					: caseBuyAfterAmt * 100;
		} else {
			caseBuyAfterAmt = caseBuyAfterAmt * 100;
		}
		return Integer.toString((int) caseBuyAfterAmt) + "%";
	}

	/**
	 * 解析标识
	 * 
	 * @param str
	 * @return
	 */
	private String parseFlag(String str) {
		String flag = "";
		switch (Integer.parseInt(str)) {
		case 0:
			flag = "进行中";
			break;
		case 1:
			flag = "已出票";
			break;
		case 2:
			flag = "失败";
			break;
		case 3:
			flag = "部分成功";
			break;
		case 4:
			flag = "已撤销";
			break;
		case 5:
			flag = "合买失败";
			break;
		case 6:
			flag = "满员";
			break;
		case 7:
			flag = "成功";
			break;
		}
		return flag;
	}

	/**
	 * 解析数据
	 * 
	 * @param json
	 * @return
	 */
	private ArrayList<String[]> parseInfo(JSONObject obj) {
		ArrayList list = new ArrayList<String[]>();
		String str[] = new String[16];
		try {
			str[0] = obj.getString("lotNo");// 彩种
			str[1] = obj.getString("caseAllAmt");// 总金额
			str[2] = obj.getString("caseBuyAfterAmt");// 进度
			str[3] = obj.getString("flag");// 状态
			try {
				str[4] = obj.getString("describe");// 方案描述
			} catch (Exception e) {
				str[4] = "";
			}
			str[5] = obj.getString("userNo");// 发起人
			str[6] = obj.getString("pushMoney");// 发起人提成
			// str[3] = obj.getString("flag");// 进度
			str[7] = obj.getString("buyedUserNum");// 参与人数
			str[8] = obj.getString("caseContent");// 方案内容
			// str[3] = obj.getString("lotNo");// 彩种
			str[9] = obj.getString("batchCode");// 期号
			str[10] = obj.getString("caseNum");// 倍数
			// str[3] = obj.getString("caseAllAmt");// 总金额
			str[11] = obj.getString("caseBaoDiAmt");// 保底份数
			str[12] = obj.getString("caseOneAmt");// 每份金额
			str[13] = obj.getString("caseBuyAmtByUser");// 已认购份数
			str[14] = obj.getString("BcaseNnum");// 剩余份数
			str[15] = obj.getString("id");// 彩种编号
			str[1] = parseAmt(str[1]);
			str[2] = parseProgress(str[2]);
			str[3] = parseFlag(str[3]);
			str[6] = parseProgress(str[6]);
			str[8] = JoinIn.getByContent(str[8], str[0]);
			str[0] = parseLotNo(str[0]);
			str[9] += "期";
			str[10] += "倍";
			str[11] += "份";
			str[12] = parseAmt(str[12]);
			str[13] += "份";
			str[14] += "份";

			list.add(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}

	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
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

}
