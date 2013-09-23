package com.ruyicai.activity.buy.jc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;
import com.ruyicai.activity.buy.jc.oddsprize.JCPrizePermutationandCombination;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.custom.checkbox.MyCheckBox;
import com.ruyicai.net.newtransaction.QueryJcInfoInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class JcMainView {
	private ListView listView;
	protected Context context;
	private View view;
	private BetAndGiftPojo betAndGift;// 投注信息类
	protected List<Info> listInfo = new ArrayList<Info>();/* 过关列表适配器的数据源 */
	protected List<Info> listInfo1 = new ArrayList<Info>();/* 单关列表适配器的数据源 */
	private static JSONArray jsonArray = null;// 过关
	private static JSONArray jsonArray1 = null;// 单关
	private List<String> betcodes = new ArrayList<String>();
	private List<Boolean> isDanList = new ArrayList<Boolean>();
	private Handler handler;
	private final static String ERROR_WIN = "0000";
	private LinearLayout layoutView;
	private BaseAdapter adapter;
	public String jcType;
	private String jcvaluetype;
	private List<List> listWeeks = new ArrayList<List>();// 按星期划分数据
	private final int MAX_TEAM = 10;// 最多可选10场比赛
	private final int MAX_DAN = 7;// 最多设胆7场比赛
	public boolean isDanguan = false;
	protected static String[] listTeam;/* 赛事选择列表 */
	protected List<String> checkTeam = new ArrayList<String>();/* 被选择后的赛事列表 */
	TextView textTeamNum;

	public abstract String getPlayType();

	public abstract String getTitle();

	public abstract String getTypeTitle();

	public abstract String getLotno();

	public abstract BaseAdapter getAdapter();

	public abstract void initListView(ListView list, Context context,
			List<List> listInfo);

	public abstract String getCode(String key, List<Info> listInfo);

	public abstract List<double[]> getOdds(List<Info> listInfo);

	public abstract String getAlertCode(List<Info> listInfo);

	public abstract int getTeamNum();

	/** add by yejc 20130722 start */
	private Map<String, Integer> mMap = new HashMap<String, Integer>();
	public int mPosition = -1;
	public int mIndex = -1;
	public Resources resources;
	public LayoutInflater mFactory;
	public int white = 0;
	public int black = 0;
	public int red = 0;
	public int green = 0;
	public int oddsColor = 0;

	/** add by yejc 20130722 end */

	public JcMainView(Context context, BetAndGiftPojo betAndGift,
			Handler handler, LinearLayout layout, String type,
			boolean isDanguan, List<String> checkTeam) {
		this.context = context;
		this.betAndGift = betAndGift;
		this.handler = handler;
		this.isDanguan = isDanguan;
		this.checkTeam = checkTeam;
		layoutView = layout;
		if (isDanguan) {
			jcvaluetype = "0";
		} else {
			jcvaluetype = "1";
		}
		initListWeeks();
		setType(type);
		initView();
		getInfoNet();
		/** add by yejc 20130816 start */
		resources = context.getResources();
		mFactory = LayoutInflater.from(context);
		white = resources.getColor(R.color.white);
		black = resources.getColor(R.color.black);
		red = resources.getColor(R.color.red);
		green = resources.getColor(R.color.gree_black);
		oddsColor = resources.getColor(R.color.jc_odds_text_color);
		/** add by yejc 20130816 end */
	}

	private void initListWeeks() {
		listWeeks.clear();
	}

	public void updateList(List<String> checkTeam) {
		initListWeeks();
		if (isDanguan) {
			listInfo1.clear();
		} else {
			listInfo.clear();
		}
		this.checkTeam = checkTeam;
		getInfoNet();
	}

	public String getEvent(String type, Info info) {
		return type + "_" + info.getDay() + "_" + info.getWeek() + "_"
				+ info.getTeamId();
	}

	/**
	 * 跳转到分析界面
	 */
	public void trunExplain(String event, String home, String away) {
		Intent intent = new Intent(context, JcExplainActivity.class);
		intent.putExtra("event", event);
		Constants.currentTickType = "jingCai";
		context.startActivity(intent);
	}

	/**
	 * 设置彩票类别
	 */
	public void setType(String type) {
		jcType = type;
	}

	/**
	 * 清空数据
	 */
	public void clearInfo() {
		jsonArray = null;
		jsonArray1 = null;
	}

	/**
	 * 初始化组件
	 */
	public void initView() {
		LayoutInflater factory = LayoutInflater.from(context);
		view = factory.inflate(R.layout.buy_jc_main_view_new, null);
		listView = (ListView) view.findViewById(R.id.buy_jc_main_listview);
		layoutView.addView(getView());
	}

	/**
	 * 联网获取数据
	 */
	public void getInfoNet() {
		if (isDanguan) {
			if (jsonArray1 == null) {
				infoNet();
			} else {
				initSubView();
			}
		} else {
			if (jsonArray == null) {
				infoNet();
			} else {
				initSubView();
			}
		}
	}

	/**
	 * 初始化子列表
	 */
	private void initSubView() {
		if (isDanguan) {
			setValue(jsonArray1);
		} else {
			setValue(jsonArray);
		}
		if (isDanguan && (jsonArray1 == null || (jsonArray1 != null && jsonArray1.length() == 0))) {
			showNoGamePrompt();
		} else if ((jsonArray == null || (jsonArray != null && jsonArray.length() == 0)) && !isDanguan) {
			showNoGamePrompt();
		} else {
			initListView(getListView(), context, listWeeks);
			layoutView.removeAllViews();
			layoutView.addView(getView());
		}

	}

	private void showNoGamePrompt() {
		View view = LayoutInflater.from(context).inflate(R.layout.buy_jc_no_game_layout, null);
		layoutView.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.FILL_PARENT);
		layoutView.addView(view, params);
	}

	private void infoNet() {
		final ProgressDialog dialog = UserCenterDialog.onCreateDialog(context);
		dialog.show();
		final View dialogView = PublicMethod.getView(context);
		dialog.getWindow().setContentView(dialogView);
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = QueryJcInfoInterface.getInstance().queryJcInfo(jcType,
						jcvaluetype);
				try {
					JSONObject jsonObj = new JSONObject(str);
					final String msg = jsonObj.getString("message");
					final String error = jsonObj.getString("error_code");
					if (error.equals(ERROR_WIN) || error.equals("0047")) {
						if (!isHasResult(jsonObj)) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									dialog.dismiss();
									showNoGamePrompt();
								}
							});
							return;
						}
					}
					if (error.equals(ERROR_WIN)) {
						listTeam = jsonObj.getString("leagues").split(";");
						if (isDanguan) {
							jsonArray1 = jsonObj.getJSONArray("result");
						} else {
							jsonArray = jsonObj.getJSONArray("result");
						}

						handler.post(new Runnable() {
							@Override
							public void run() {
								initSubView();
								dialog.cancel();
							}
						});

					}  else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								dialog.cancel();
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
    private boolean isHasResult(JSONObject jsonObject) {
    	JSONArray jsonArrayTmp = null;
    	if (jsonObject.has("result")) {
    		try {
				jsonArrayTmp = jsonObject
						.getJSONArray("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jsonArrayTmp == null || jsonArrayTmp.length() == 0) {
			return false;
		}
		return true;
    }
	public void setValue(JSONArray jsonArray) {
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				List<Info> list = addDateInfo(jsonArray.getJSONArray(i));
				if (list.size() > 0) {
					listWeeks.add(list);
				}
			}

			// add by yejc 20130408
			if (listWeeks.size() > 0) {
				List<Info> list = listWeeks.get(0);
				if (list.size() > 0) {
					list.get(0).isOpen = true;
				}
			}
			// end
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Info> addDateInfo(JSONArray jsonArray) throws JSONException {
		List<Info> listInfos = new ArrayList<Info>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonItem = jsonArray.getJSONObject(i);
			if (checkTeam.size() > 0) {
				String league = jsonItem.getString("league");
				for (int j = 0; j < checkTeam.size(); j++) {
					if (checkTeam.get(j).equals(league)) {
						if (addInfo(jsonItem) != null)
							listInfos.add(addInfo(jsonItem));
					}
				}
			} else {
				if (addInfo(jsonItem) != null)
					listInfos.add(addInfo(jsonItem));
			}
		}
		return listInfos;
	}

	public Info addInfo(JSONObject jsonItem) throws JSONException {
		Info itemInfo = new Info();
		itemInfo.setTime(jsonItem.getString("dayForamt") + "  "
				+ jsonItem.getString("week"));
		itemInfo.setWeeks(jsonItem.getString("week")); // add by yejc 20130402
		itemInfo.setDay(jsonItem.getString("day"));
		itemInfo.setWeek(jsonItem.getString("weekId"));
		itemInfo.setTeam(jsonItem.getString("league"));
		itemInfo.setTimeEnd(jsonItem.getString("endTime"));
		itemInfo.setTeamId(jsonItem.getString("teamId"));
		itemInfo.setWin(jsonItem.getString("v3"));
		itemInfo.setFail(jsonItem.getString("v0"));
		String teams[] = jsonItem.getString("team").split(":");
		String[] unsupportStr = jsonItem.getString("unsupport").split(",");
		itemInfo.setUnsupportPlay(unsupportStr); // add by yejc 20130709
		itemInfo.setHome(teams[0]);
		itemInfo.setAway(teams[1]);
		if (jsonItem.has("letVs_letPoint")) {
			itemInfo.setLetPoint(jsonItem.getString("letVs_letPoint"));
		}
		if (jsonItem.has("letVs_v0")) {
			itemInfo.setLetV0Fail(jsonItem.getString("letVs_v0"));
		}
		if (jsonItem.has("letVs_v1")) {
			itemInfo.setLetV1Level(jsonItem.getString("letVs_v1"));
		}
		if (jsonItem.has("letVs_v3")) {
			itemInfo.setLetV3Win(jsonItem.getString("letVs_v3"));
		}
		setDifferValue(jsonItem, itemInfo);
		for (String str : unsupportStr) {
			if (getPlayType().equals(str)) {
				return null;
			}
		}
		if (isDanguan) {
			listInfo1.add(itemInfo);
		} else {
			listInfo.add(itemInfo);
		}
		return itemInfo;
	}

	public void setDifferValue(JSONObject jsonItem, Info itemInfo)
			throws JSONException {

	}

	/**
	 * 返回列表组件
	 * 
	 * @return
	 */
	public ListView getListView() {
		return listView;
	};

	/**
	 * 初始化每队被选中的按钮个数数组
	 * 
	 * @param position
	 */
	public void initOnclikNums() {
		betcodes.clear();
		isDanList.clear();
		List<Info> list;
		if (isDanguan) {
			list = listInfo1;
		} else {
			list = listInfo;
		}
		for (int i = 0; i < list.size(); i++) {
			Info info = (Info) list.get(i);
			if (info.onclikNum > 0) {
				betcodes.add("" + info.onclikNum);
				if (info.isDan()) {
					isDanList.add(true);
				} else {
					isDanList.add(false);
				}
			}
		}
	}

	/**
	 * 初始化已选择多少场比赛
	 */
	public void initTeamNum(TextView textTeamNum) {
		this.textTeamNum = textTeamNum;
		setTeamNum();

	}

	public void isNoDan(Info info, Button btnDan) {
		if (info.onclikNum == 0 && info.isDan()) {
			info.setDan(false);
			btnDan.setBackgroundResource(android.R.color.transparent);
			btnDan.setTextColor(resources.getColor(R.color.black));
		}
	}

	public void setTeamNum() {
		textTeamNum.setText("已选择了" + initCheckedNum() + "场比赛");
	}

	public boolean isTouZhuNet() {
		int checkedNum = initCheckedNum();
		if (isDanguan) {
			if (checkedNum >= 1) {// 至少选两个队
				betAndGift.setSellway("0");// 1代表机选 0代表自选
				betAndGift.setLotno(getLotno());
				initOnclikNums();
				return true;
			} else {
				alertInfo("请至少选择一场比赛", "请选择号码");
				return false;
			}
		} else {
			if (checkedNum >= 2) {// 至少选两个队
				betAndGift.setSellway("0");// 1代表机选 0代表自选
				betAndGift.setLotno(getLotno());
				initOnclikNums();
				return true;
			} else {
				alertInfo("请至少选择两场比赛", "请选择号码");
				return false;
			}
		}
	}

	/**
	 * 获取选中的球队数
	 * 
	 * @return
	 */
	public int initCheckedNum() {
		int checkedNum = 0;
		List<Info> list;
		if (isDanguan) {
			list = listInfo1;
		} else {
			list = listInfo;
		}
		for (Info info : list) {
			if (info.onclikNum > 0) {
				checkedNum++;
			}
		}
		return checkedNum;
	}

	/**
	 * 获取设胆的球队数
	 * 
	 * @return
	 */
	public int initDanCheckedNum() {
		int checkedNum = 0;
		List<Info> list;
		if (isDanguan) {
			list = listInfo1;
		} else {
			list = listInfo;
		}
		for (Info info : list) {
			if (info.onclikNum > 0 && info.isDan()) {
				checkedNum++;
			}
		}
		return checkedNum;
	}

	/**
	 * 获取设胆个数
	 * 
	 * @return
	 */
	public int getIsDanNum() {
		int num = 0;
		for (int i = 0; i < isDanList.size(); i++) {
			if (isDanList.get(i)) {
				num++;
			}
		}
		return num;
	}

	/**
	 * 自由过关计算注数
	 * 
	 * @param select
	 * @return
	 */
	public int getZhushu(int select) {
		return PublicMethod.getAllAmt(betcodes, select, isDanList,
				getIsDanNum());
	}

	/**
	 * 多串过关计算注数
	 * 
	 * @param select
	 * @return
	 */
	public int getDuoZhushu(int teamNum, int select) {
		return PublicMethod.getDouZhushu(teamNum, betcodes, select, isDanList,
				getIsDanNum());
	}

	/**
	 * 单关计算注数
	 * 
	 * @param select
	 * @return
	 */
	public int getDanZhushu() {
		return PublicMethod.getDanAAmt(betcodes);
	}

	/**
	 * 获取单关投注的中奖金额最大金额和最小金额的字符串
	 * 
	 * @return
	 */
	public String getDanPrizeString(int muti) {

		return JCPrizePermutationandCombination.getDanGuanPrize(
				getOddsArraysValue(), muti);
	}

	/**
	 * 获取自由过关的中奖金额的最大值和最小值
	 * 
	 * @return
	 */
	public String getFreedomPrizeString(int select, int muti) {
		return "";
	}

	/**
	 * 获取多串过关的中奖金额的最大值和最小值
	 * 
	 * @return
	 */
	public String getDuoPrizeString() {
		return "";
	}

	/**
	 * 得到view
	 * 
	 */
	public View getView() {
		return view;
	}

	public String getBetCode(String key) {
		List<Info> list;
		if (isDanguan) {
			list = listInfo1;
		} else {
			list = listInfo;
		}
		return getCode(key, list);
	}

	public List<double[]> getOddsArraysValue() {
		List<Info> list;
		if (isDanguan) {
			list = listInfo1;
		} else {
			list = listInfo;
		}
		return getOdds(list);
	}

	public List<double[]> getMinOdds(List<Info> listInfo) {
		return null;
	}

	public List<double[]> getHunMinOddsArraysValue() {
		List<Info> list;
		if (isDanguan) {
			list = listInfo1;
		} else {
			list = listInfo;
		}
		return getMinOdds(list);
	}

	public double getFreedomMaxPrize(int select) {
		double max = JCPrizePermutationandCombination
				.getFreedomGuoGuanMaxPrize(getOddsArraysValue(), select,
						isDanList, getIsDanNum());
		return max;
	}

	public double getFreedomHunMaxPrize(int select) {
		double max = JCPrizePermutationandCombination.getFreedomHunMaxPrize(
				getOddsArraysValue(), select, isDanList, getIsDanNum());
		return max;
	}

	public double getDuoMaxPrize(int teamNum, int select) {
		return JCPrizePermutationandCombination.getDuoMaxPrize(teamNum,
				getOddsArraysValue(), select, isDanList, getIsDanNum());
	}

	public double getDuoMixPrize(int teamNum, int select) {
		return JCPrizePermutationandCombination.getDuoMixPrize(teamNum,
				getOddsArraysValue(), select, isDanList, getIsDanNum());
	}

	/**
	 * 获取最小中奖金额
	 * 
	 * @param select
	 *            最小命中几场
	 * @return
	 */
	public double getFreedomMixPrize(int select) {
		if (isHunHe()) {
			return JCPrizePermutationandCombination.FreedomGuoGuanMixPrize(
					getHunMinOddsArraysValue(), select, isDanList,
					getIsDanNum());
		} else {
			return JCPrizePermutationandCombination.FreedomGuoGuanMixPrize(
					getOddsArraysValue(), select, isDanList, getIsDanNum());
		}

	}

	/**
	 * 提示框1 用来提醒选球规则
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertInfo(String string, String title) {
		Builder dialog = new AlertDialog.Builder(context).setTitle(title)
				.setMessage(string)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		dialog.show();

	}

	public String getAlertMsg() {
		List<Info> list;
		if (isDanguan) {
			list = listInfo1;
		} else {
			list = listInfo;
		}
		return getAlertCode(list);
	}

	/**
	 * 清除选中状态
	 */
	public void clearChecked() {
		if (getAdapter() != null) {
			if (isDanguan) {
				listInfo1.clear();
				initListWeeks();
				setValue(jsonArray1);
				getAdapter().notifyDataSetChanged();
			} else {
				listInfo.clear();
				initListWeeks();
				setValue(jsonArray);
				getAdapter().notifyDataSetChanged();
			}
		}

	}

	/**
	 * 最多可以选择10场比赛
	 */
	public boolean isCheckTeam() {
		int teamNum = initCheckedNum();
		if (teamNum < MAX_TEAM) {
			return true;
		} else {
			Toast.makeText(context, "您最多可以选择10场比赛进行投注！", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	}

	/**
	 * 最多可以设胆7场比赛
	 */
	public boolean isDanCheckTeam() {
		int teamNum = initDanCheckedNum();
		if (teamNum < MAX_DAN) {
			return true;
		} else {
			Toast.makeText(context, "您最多可以选择" + MAX_DAN + "场比赛进行设胆！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/**
	 * 是否可以设胆
	 */
	public boolean isDanCheck() {
		int teamDanNum = initDanCheckedNum();
		int teamNum = initCheckedNum();
		int danNum = teamNum - 2;
		if (teamNum < 3) {
			Toast.makeText(context, "请您至少选择3场比赛，才能设胆", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (teamDanNum < danNum) {
			return true;
		} else {
			Toast.makeText(context, "胆码不能超过" + danNum + "个", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	}

	public boolean isHunHe() {
		return false;
	}

	public void setTeamNum(int checkTeam) {
	}

	/**
	 * 竞彩内部类
	 * 
	 * @author Administrator
	 */
	public class Info {
		String time = "";
		String day = "";
		String team = "";
		String home = "";
		String away = "";
		String score = "";
		String timeEnd = "";
		String win = "";
		String level = "";
		String fail = "";
		String week = "";
		String teamId = "";

		String weeks = ""; // add by yejc 20130402
		String letV3Win = "";
		String letV1Level = "";
		String letV0Fail = "";

		public int onclikNum = 0;
		boolean isWin = false;
		boolean isLevel = false;
		boolean isFail = false;
		// 让分胜负
		String letWin = "";
		String letFail = "";
		String letPoint = "";
		// 玩法:大小分
		String big = "";
		String small = "";
		String basePoint = "";
		boolean isBig;
		boolean isSmall;
		// 玩法:胜分差
		private int MAX = 12;
		public String vStrs[];
		private String btnStr = "";
		public Dialog infoDialog = null;// 提示框
		public View infoViewType = null;
		private Dialog dialog = null;// 提示框
		private View viewType;
		private int[] checkId = { R.id.lq_sfc_dialog_check01,
				R.id.lq_sfc_dialog_check02, R.id.lq_sfc_dialog_check03,
				R.id.lq_sfc_dialog_check04, R.id.lq_sfc_dialog_check05,
				R.id.lq_sfc_dialog_check06, R.id.lq_sfc_dialog_check07,
				R.id.lq_sfc_dialog_check08, R.id.lq_sfc_dialog_check09,
				R.id.lq_sfc_dialog_check010, R.id.lq_sfc_dialog_check011,
				R.id.lq_sfc_dialog_check012, R.id.lq_sfc_dialog_check013,
				R.id.lq_sfc_dialog_check014, R.id.lq_sfc_dialog_check015,
				R.id.lq_sfc_dialog_check016, R.id.lq_sfc_dialog_check017,
				R.id.lq_sfc_dialog_check018, R.id.lq_sfc_dialog_check019,
				R.id.lq_sfc_dialog_check020, R.id.lq_sfc_dialog_check021,
				R.id.lq_sfc_dialog_check022, R.id.lq_sfc_dialog_check023,
				R.id.lq_sfc_dialog_check024, R.id.lq_sfc_dialog_check025,
				R.id.lq_sfc_dialog_check026, R.id.lq_sfc_dialog_check027,
				R.id.lq_sfc_dialog_check028, R.id.lq_sfc_dialog_check029,
				R.id.lq_sfc_dialog_check030, R.id.lq_sfc_dialog_check031,
				R.id.lq_sfc_dialog_check032, R.id.lq_sfc_dialog_check033,
				R.id.lq_sfc_dialog_check034, R.id.lq_sfc_dialog_check035,
				R.id.lq_sfc_dialog_check036, R.id.lq_sfc_dialog_check037,
				R.id.lq_sfc_dialog_check038, R.id.lq_sfc_dialog_check039,
				R.id.lq_sfc_dialog_check040, R.id.lq_sfc_dialog_check041,
				R.id.lq_sfc_dialog_check042, R.id.lq_sfc_dialog_check043,
				R.id.lq_sfc_dialog_check044, R.id.lq_sfc_dialog_check045,
				R.id.lq_sfc_dialog_check046, R.id.lq_sfc_dialog_check047,
				R.id.lq_sfc_dialog_check048, R.id.lq_sfc_dialog_check049,
				R.id.lq_sfc_dialog_check050, R.id.lq_sfc_dialog_check051 };

		/** add by yejc 20130704 start */
		private int[] checkIdForZC = { R.id.lq_sfc_dialog_check01, 
				R.id.lq_sfc_dialog_check02, R.id.lq_sfc_dialog_check03, 
				R.id.lq_sfc_dialog_rangqiu1, R.id.lq_sfc_dialog_rangqiu2, 
				R.id.lq_sfc_dialog_rangqiu3, R.id.lq_sfc_dialog_check04,
				R.id.lq_sfc_dialog_check05, R.id.lq_sfc_dialog_check06,
				R.id.lq_sfc_dialog_check07, R.id.lq_sfc_dialog_check08,
				R.id.lq_sfc_dialog_check09, R.id.lq_sfc_dialog_check010,
				R.id.lq_sfc_dialog_check011, R.id.lq_sfc_dialog_check012,
				R.id.lq_sfc_dialog_check013, R.id.lq_sfc_dialog_check014,
				R.id.lq_sfc_dialog_check015, R.id.lq_sfc_dialog_check016,
				R.id.lq_sfc_dialog_check017, R.id.lq_sfc_dialog_check018,
				R.id.lq_sfc_dialog_check019, R.id.lq_sfc_dialog_check020,
				R.id.lq_sfc_dialog_check021, R.id.lq_sfc_dialog_check022,
				R.id.lq_sfc_dialog_check023, R.id.lq_sfc_dialog_check024,
				R.id.lq_sfc_dialog_check025, R.id.lq_sfc_dialog_check026,
				R.id.lq_sfc_dialog_check027, R.id.lq_sfc_dialog_check028,
				R.id.lq_sfc_dialog_check029, R.id.lq_sfc_dialog_check030,
				R.id.lq_sfc_dialog_check031, R.id.lq_sfc_dialog_check032,
				R.id.lq_sfc_dialog_check033, R.id.lq_sfc_dialog_check034,
				R.id.lq_sfc_dialog_check035, R.id.lq_sfc_dialog_check036,
				R.id.lq_sfc_dialog_check037, R.id.lq_sfc_dialog_check038,
				R.id.lq_sfc_dialog_check039, R.id.lq_sfc_dialog_check040,
				R.id.lq_sfc_dialog_check041, R.id.lq_sfc_dialog_check042,
				R.id.lq_sfc_dialog_check043, R.id.lq_sfc_dialog_check044,
				R.id.lq_sfc_dialog_check045, R.id.lq_sfc_dialog_check046,
				R.id.lq_sfc_dialog_check047, R.id.lq_sfc_dialog_check048,
				R.id.lq_sfc_dialog_check049, R.id.lq_sfc_dialog_check050,
				R.id.lq_sfc_dialog_check051 };
		private boolean isHunheZQ = false;
		private String[] unsupportPlay = null;

		public boolean isHunheZQ() {
			return isHunheZQ;
		}

		public void setHunheZQ(boolean isHunheZQ) {
			this.isHunheZQ = isHunheZQ;
		}

		public String[] getUnsupportPlay() {
			return unsupportPlay;
		}

		public void setUnsupportPlay(String[] unsupportPlay) {
			this.unsupportPlay = unsupportPlay;
		}

		/** add by yejc 20130704 end */
		public MyCheckBox[] check;
		public boolean isOpen = false;
		public String titles[];
		private boolean isDan = false;
		private boolean isLq = false;
		private String lotno = "";

		public String getLotno() {
			return lotno;
		}

		public void setLotno(String lotno) {
			this.lotno = lotno;
		}

		public String getLetV3Win() {
			return letV3Win;
		}

		public void setLetV3Win(String letV3Win) {
			this.letV3Win = letV3Win;
		}

		public String getLetV1Level() {
			return letV1Level;
		}

		public void setLetV1Level(String letV1Level) {
			this.letV1Level = letV1Level;
		}

		public String getLetV0Fail() {
			return letV0Fail;
		}

		public void setLetV0Fail(String letV0Fail) {
			this.letV0Fail = letV0Fail;
		}

		public boolean isDan() {
			return isDan;
		}

		public boolean isLq() {
			return isLq;
		}

		public void setLq(boolean isLq) {
			this.isLq = isLq;
		}

		public void setDan(boolean isDan) {
			this.isDan = isDan;
		}

		public String[] getVStrs() {
			return vStrs;
		}

		public void setMax(int max) {
			this.MAX = max;
		}

		/**
		 * 初始化弹出框选项标题
		 * 
		 * @param length
		 */
		public void initCheckTitles(String titles[]) {
			this.titles = titles;
			MAX = titles.length;
			check = new MyCheckBox[MAX];
		}

		/**
		 * 胜分差弹出框
		 */
		public void createDialog(String titles[], boolean isVisable,
				String title) {

			if (dialog == null) {
				initCheckTitles(titles);
				adapter = getAdapter();
				if (isHunHe()) {
					if (isLq) {
						viewType = mFactory.inflate(R.layout.buy_lq_hun_dialog,
								null);
						setJcLqShowPlay(viewType); // add by yejc 20130801
					} else {
						viewType = mFactory.inflate(R.layout.buy_zq_hun_dialog,
								null);
						setJcZqShowPlay(viewType); // add by yejc 20130709
					}
				} else {
					if (Constants.LOTNO_JCZQ_BF.equals(getLotno())) {
						viewType = mFactory.inflate(
								R.layout.buy_jc_zq_bf_layout, null);
					} else if (Constants.LOTNO_JCLQ_SFC.equals(getLotno())) {
						viewType = mFactory.inflate(
								R.layout.buy_jc_lq_sfc_layout, null);
					} else {
						viewType = mFactory.inflate(R.layout.buy_lq_sfc_dialog,
								null);
						LinearLayout layout1 = (LinearLayout) viewType
								.findViewById(R.id.jc_check_dialog_layout2);
						LinearLayout layout2 = (LinearLayout) viewType
								.findViewById(R.id.jc_check_dialog_layout3);
						if (isVisable) {
							layout1.setVisibility(LinearLayout.VISIBLE);
							layout2.setVisibility(LinearLayout.VISIBLE);
						}
					}
				}

				TextView textTitle = (TextView) viewType
						.findViewById(R.id.layout_main_text_title);
				textTitle.setText(title);
				initDialogView();
				setChechState();
				onClikOk();
				onClikCanCel();
				dialog = new AlertDialog.Builder(context).create();
				dialog.setCancelable(false);
				infoDialog = dialog;
				infoViewType = viewType;
			}
			dialog.show();
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					PublicMethod.getPxInt(300, context),
					LinearLayout.LayoutParams.WRAP_CONTENT);
			// int margin = PublicMethod.getPxInt(20, context);
			// params.setMargins(margin, 0, margin, 0);
			dialog.setContentView(viewType, params);
			// dialog.getWindow().setContentView(viewType);
		}

		/** add by yejc 20130704 start */
		public View getViewType() {
			return viewType;
		}

		private void setJcZqShowPlay(View view) {
			for (String str : unsupportPlay) {
				if ("J00001_1".equals(str)) { // 胜平负
					view.findViewById(R.id.linearLayout1).setVisibility(
							View.GONE);
				} else if ("J00002_1".equals(str)) { // 比分
					view.findViewById(R.id.linearLayout4).setVisibility(
							View.GONE);
				} else if ("J00003_1".equals(str)) { // 进球数
					view.findViewById(R.id.linearLayout3).setVisibility(
							View.GONE);
				} else if ("J00004_1".equals(str)) {// 半全场
					view.findViewById(R.id.linearLayout2).setVisibility(
							View.GONE);
				} else if ("J00013_1".equals(str)) {// 让球胜平负
					view.findViewById(R.id.linearLayout_rangqiu).setVisibility(
							View.GONE);
				}
			}
		}

		/** add by yejc 20130709 end */
		/** add by yejc 20130801 start */
		private void setJcLqShowPlay(View view) {
			for (String str : unsupportPlay) {
				if ("J00005_1".equals(str)) { // 胜负玩法
					view.findViewById(R.id.linearLayout1).setVisibility(
							View.GONE);
				} else if ("J00006_1".equals(str)) { // 让分胜负
					view.findViewById(R.id.linearLayout2).setVisibility(
							View.GONE);
				} else if ("J00007_1".equals(str)) { // 胜分差
					view.findViewById(R.id.linearLayout4).setVisibility(
							View.GONE);
				} else if ("J00008_1".equals(str)) {// 大小分
					view.findViewById(R.id.linearLayout3).setVisibility(
							View.GONE);
				}
			}
		}

		/** add by yejc 20130801 end */
		public void setJqsLayout(String titles[], LinearLayout layout,
				Handler handler) {
			initCheckTitles(titles);
			for (int i = 0; i < MAX; i++) {
				check[i] = (MyCheckBox) layout.findViewById(checkId[i]);
				check[i].setVisibility(CheckBox.VISIBLE);
				check[i].setCheckText("" + vStrs[i]);
				check[i].setPosition(i);
				check[i].setCheckTitle(titles[i]);
				check[i].setHandler(handler);
			}
		}

		private void initDialogView() {
			/** add by yejc 20130704 start */
			if (isHunheZQ) {
				for (int i = 0; i < MAX; i++) {
					check[i] = (MyCheckBox) viewType
							.findViewById(checkIdForZC[i]);
					check[i].setVisibility(CheckBox.VISIBLE);
					check[i].setCheckText("" + vStrs[i]);
					check[i].setPosition(i);
					check[i].setCheckTitle(titles[i]);
					if (i >= 0 && i <= 5) {
						check[i].setTextPaintColorArray(new int[] {
								resources.getColor(R.color.jc_hun_title_color),
								Color.WHITE });
						check[i].setOddsPaintColorArray(new int[] {
								resources.getColor(R.color.jc_hun_title_color),
								Color.WHITE });
					} else if (i >= 6 && i <= 22) {
						check[i].setTextPaintColorArray(new int[] {
								resources
										.getColor(R.color.jc_hun_zq_bqc_title_color),
								Color.WHITE });
						check[i].setOddsPaintColorArray(new int[] {
								resources
										.getColor(R.color.jc_hun_zq_bqc_title_color),
								Color.WHITE });
					} else {
						check[i].setTextPaintColorArray(new int[] {
								resources
										.getColor(R.color.jc_hun_zq_bf_title_color),
								Color.WHITE });
						check[i].setOddsPaintColorArray(new int[] {
								resources
										.getColor(R.color.jc_hun_zq_bf_title_color),
								Color.WHITE });
					}
				}
				/** add by yejc 20130704 end */
			} else {
				for (int i = 0; i < MAX; i++) {
					check[i] = (MyCheckBox) viewType.findViewById(checkId[i]);
					check[i].setVisibility(CheckBox.VISIBLE);
					check[i].setCheckText("" + vStrs[i]);
					check[i].setPosition(i);
					check[i].setCheckTitle(titles[i]);
					/** add by yejc 20130819 start */
					if (Constants.LOTNO_JCZQ_BF.equals(getLotno())) {
						check[i].setTextPaintColorArray(new int[] {
								resources.getColor(R.color.jc_hun_title_color),
								Color.WHITE });
						check[i].setOddsPaintColorArray(new int[] { Color.GRAY,
								Color.WHITE });
					} else if (Constants.LOTNO_JCLQ_SFC.equals(getLotno())) {
						if (i >= 0 && i <= 5) {
							check[i].setTextPaintColorArray(new int[] {
									resources
											.getColor(R.color.jc_hun_title_color),
									Color.WHITE });
						} else {
							check[i].setTextPaintColorArray(new int[] {
									resources
											.getColor(R.color.jc_hun_lq_title_color),
									Color.WHITE });
						}
						check[i].setOddsPaintColorArray(new int[] { Color.GRAY,
								Color.WHITE });
					} else {
						if (i >= 0 && i <= 7) {
							if (i == 3) {
								check[i].setOddsPaintColorArray(new int[] {
										Color.RED, Color.WHITE });
							} else if (i == 6) {
								check[i].setOddsPaintColorArray(new int[] {
										Color.BLUE, Color.WHITE });
							} else {
								check[i].setOddsPaintColorArray(new int[] {
										resources
												.getColor(R.color.jc_hun_title_color),
										Color.WHITE });
							}
						} else {
							check[i].setOddsPaintColorArray(new int[] {
									resources
											.getColor(R.color.jc_hun_lq_title_color),
									Color.WHITE });
						}
						check[i].setTextPaintColorArray(new int[] {
								resources.getColor(R.color.jc_hun_title_color),
								Color.WHITE });
						check[i].setLotno(Constants.LOTNO_JCLQ_HUN);
					}
					/** add by yejc 20130819 end */

				}
			}
		}

		private void setChechState() {
			if (isHunHe()) {
				if (isLq) {
					check[3].setEnabled(false);
					check[6].setEnabled(false);
					for (int i = 0; i < 8; i++) {
						check[i].setHorizontal(true);
					}
				} else {
					for (int i = 0; i < 6; i++) {
						check[i].setHorizontal(true);
					}

					for (int j = 15; j < 23; j++) {
						check[j].setHorizontal(true);
					}
				}
			}

		}

		private void onClikCanCel() {
			Button cancel = (Button) viewType.findViewById(R.id.canel);
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					dialog.cancel();
					for (int i = 0; i < check.length; i++) {
						if (check[i].getChecked()) {
							check[i].setChecked(false);
						}
						setBtnStr("");
						onclikNum = 0;
					}
					setTeamNum();
					setDanState(); // add by yejc 20130821
					adapter.notifyDataSetChanged();
				}
			});
		}

		private void onClikOk() {
			Button ok = (Button) viewType.findViewById(R.id.ok);
			ok.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					dialog.cancel();
					btnStr = "";
					onclikNum = 0;
					for (int i = 0; i < check.length; i++) {
						if (check[i].getChecked()) {
							if (isHunHe() && !isLq) {
								if (check[i].getPosition() == 3
										|| check[i].getPosition() == 4
										|| check[i].getPosition() == 5) {
									btnStr += "让" + check[i].getChcekTitle()
											+ "  ";
								} else {
									btnStr += check[i].getChcekTitle() + "  ";
								}
							} else {
								btnStr += check[i].getChcekTitle() + "  ";
							}

							onclikNum++;
						}
					}
					initCheckNum();
					setTeamNum();
					setDanState(); // add by yejc 20130821
					adapter.notifyDataSetChanged();
				}
			});
		}

		/** add by yejc 20130821 start */
		private void setDanState() {
			if (onclikNum == 0 && isDan()) {
				setDan(false);
			}
		}

		/** add by yejc 20130821 end */

		/**
		 * 设置混合过关自由过关串数
		 */
		private void initCheckNum() {
			if (isHunHe()) {
				int checkNum = 0;
				/** add by yejc 20130801 start */
				String mapIndex = String.valueOf(mPosition + "" + mIndex);// 确保唯一性
				/** add by yejc 20130801 end */
				if (isLq) {
					if (isCheckIndex(8, 19)) {// 胜分差
						checkNum = 4;
					} else {
						checkNum = 8;
					}
					/** add by yejc 20130801 start */
					mMap.put(mapIndex, checkNum);
					if (mMap.containsValue(4)) {
						setTeamNum(4);
					} else {
						setTeamNum(8);
					}
					/** add by yejc 20130801 end */
				} else {
					if (isCheckIndex(6, 14)) {// 半全场
						checkNum = 4;
					} else if (isCheckIndex(23, 53)) {// 比分
						checkNum = 4;
					} else if (isCheckIndex(15, 22)) {// 总进球
						checkNum = 6;
					} else {// 胜平负
						checkNum = 8;
					}
					/** add by yejc 20130722 start */
					mMap.put(mapIndex, checkNum);
					if (mMap.containsValue(4)) {
						setTeamNum(4);
					} else if (mMap.containsValue(6)) {
						setTeamNum(6);
					} else {
						setTeamNum(8);
					}
					/** add by yejc 20130722 start */
				}
			}
		}

		private boolean isCheckIndex(int start, int end) {
			boolean isIndex = false;
			for (int i = 0; i < check.length; i++) {
				if (check[i].getChecked()) {
					if (i >= start && i <= end) {
						isIndex = true;
						break;
					}
				}
			}
			return isIndex;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public boolean isBig() {
			return isBig;
		}

		public void setBig(boolean isBig) {
			this.isBig = isBig;
		}

		public boolean isSmall() {
			return isSmall;
		}

		public void setSmall(boolean isSmall) {
			this.isSmall = isSmall;
		}

		public String getBtnStr() {
			return btnStr;
		}

		public void setBtnStr(String btnStr) {
			this.btnStr = btnStr;
		}

		public String getLetWin() {
			return letWin;
		}

		public void setLetWin(String letWin) {
			this.letWin = letWin;
		}

		public String getLetFail() {
			return letFail;
		}

		public void setLetFail(String letFail) {
			this.letFail = letFail;
		}

		public String getBig() {
			return big;
		}

		public void setBig(String big) {
			this.big = big;
		}

		public String getSmall() {
			return small;
		}

		public void setSmall(String small) {
			this.small = small;
		}

		public String getBasePoint() {
			return basePoint;
		}

		public void setBasePoint(String basePoint) {
			this.basePoint = basePoint;
		}

		public String getTeamId() {
			return teamId;
		}

		public void setTeamId(String teamId) {
			this.teamId = teamId;
		}

		public String getLetPoint() {
			return letPoint;
		}

		public void setLetPoint(String letPoint) {
			this.letPoint = letPoint;
		}

		public String getWeek() {
			return week;
		}

		public void setWeek(String week) {
			this.week = week;
		}

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public int getOnclikNum() {
			return onclikNum;
		}

		public void setOnclikNum(int onclikNum) {
			this.onclikNum = onclikNum;
		}

		public boolean isWin() {
			return isWin;
		}

		public void setWin(boolean isWin) {
			this.isWin = isWin;
		}

		public boolean isLevel() {
			return isLevel;
		}

		public void setLevel(boolean isLevel) {
			this.isLevel = isLevel;
		}

		public boolean isFail() {
			return isFail;
		}

		public void setFail(boolean isFail) {
			this.isFail = isFail;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getTeam() {
			return team;
		}

		public void setTeam(String team) {
			this.team = team;
		}

		public String getHome() {
			return home;
		}

		public void setHome(String home) {
			this.home = home;
		}

		public String getAway() {
			return away;
		}

		public void setAway(String away) {
			this.away = away;
		}

		public String getScore() {
			return score;
		}

		public void setScore(String score) {
			this.score = score;
		}

		public String getTimeEnd() {
			return timeEnd;
		}

		public void setTimeEnd(String timeEnd) {
			this.timeEnd = timeEnd;
		}

		public String getWin() {
			return win;
		}

		public void setWin(String win) {
			this.win = win;
		}

		public String getFail() {
			return fail;
		}

		public void setFail(String fail) {
			this.fail = fail;
		}

		public String getWeeks() {
			return weeks;
		}

		public void setWeeks(String weeks) {
			this.weeks = weeks;
		}
	}

	// add by yejc 20130402
	public final String MONDAY = "星期一";
	public final String TUESDAY = "星期二";
	public final String WEDNESDAY = "星期三";
	public final String THURSDAY = "星期四";
	public final String FRIDAY = "星期五";
	public final String SATURDAY = "星期六";
	public final String SUNDAY = "星期日";

	public final String MON = "周一";
	public final String TUE = "周二";
	public final String WED = "周三";
	public final String THU = "周四";
	public final String FRI = "周五";
	public final String SAT = "周六";
	public final String SUN = "周日";

	public String getWeek(String week) {
		if (MONDAY.equals(week)) {
			return MON;
		} else if (TUESDAY.equals(week)) {
			return TUE;
		} else if (WEDNESDAY.equals(week)) {
			return WED;
		} else if (THURSDAY.equals(week)) {
			return THU;
		} else if (FRIDAY.equals(week)) {
			return FRI;
		} else if (SATURDAY.equals(week)) {
			return SAT;
		} else if (SUNDAY.equals(week)) {
			return SUN;
		}
		return week;
	}

	public void showLayout(LinearLayout layout, LinearLayout detailLayout,
			int index, final Info info, String checkTitle[], final Button btn) {
		if (layout.getChildCount() == 0) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (index == 0) {
				RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.FILL_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lParams.setMargins(0, PublicMethod.getPxInt(68.5f, context), 0,
						0);
				layout.setLayoutParams(lParams);
			}
			layout.addView(detailLayout, params);
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					String btnStr = "";
					int likNum = 0;
					for (int i = 0; i < info.check.length; i++) {
						if (info.check[i].getChecked()) {
							btnStr += info.check[i].getChcekTitle() + "  ";
							likNum++;
						}
					}
					info.onclikNum = likNum;
					info.setBtnStr(btnStr);
					btn.setText(btnStr);
					setTeamNum();
				}

			};
			info.setJqsLayout(checkTitle, detailLayout, handler);
			layout.setVisibility(View.VISIBLE);
		} else {
			if (layout.getVisibility() == View.VISIBLE) {
				layout.setVisibility(View.GONE);
			} else {
				layout.setVisibility(View.VISIBLE);
			}
		}
	}

	// end

}
