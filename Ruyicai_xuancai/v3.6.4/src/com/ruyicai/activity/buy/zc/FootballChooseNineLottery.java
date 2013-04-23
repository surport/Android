package com.ruyicai.activity.buy.zc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.zc.pojo.TeamInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.FootballLotteryAdvanceBatchcode;
import com.ruyicai.net.transaction.FootballInterface;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class FootballChooseNineLottery extends FootballFourteen implements OnSeekBarChangeListener, HandlerMsg {
	MyHandler touzhuhandler = new MyHandler(this);
	int lieNum;
	private String codeStr;
	private RadioButton check;
	private RadioButton joinCheck;
	private final static String TEAM1 = "TEAM1";
	private final static String TEAM2 = "TEAM2";
	private final static String SCORES1 = "SCORES1";
	private final static String SCORES2 = "SCORES2";
	String inflater = Context.LAYOUT_INFLATER_SERVICE;
	String advanceBatchCodeData;

	LayoutInflater layoutInflater;
	ListViewAdapter listViewDemo;
	ScrollView mHScrollView;
	LinearLayout buyView;
	TextView mTextSumMoney;
	List<Map<String, Object>> list;

	SeekBar mSeekBarBeishu;
	TextView mTextBeishu;
	int iProgressBeishu = 1;
	ImageButton rxj_btn_touzhu;
	private Vector<BallTable> ballTables = new Vector<BallTable>();
	int index;
	// 进度条
	private static final int DIALOG1_KEY = 0;
	private ProgressDialog progressdialog;
	private JSONObject obj;
	String re;
	List<TeamInfo> teamInfos = new LinkedList<TeamInfo>();
	String lotno = Constants.LOTNO_RX9;

	private String[] bactchCodes;// 预售期的期号数组
	private List<Object> bactchArray = new ArrayList<Object>();// 这个list中存放预售期期号和截止时间的信息

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBatchCode(Constants.LOTNO_RX9);
		initBatchCodeView();
		showDialog(DIALOG1_KEY);
		getZCAdvanceBatchCodeData(Constants.LOTNO_RX9);
		createView();

	}

	/**
	 * 消息处理函数
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				progressdialog.dismiss();
				initList();
				break;
			case 2:
				progressdialog.dismiss();
				break;
			case 3:
				progressdialog.dismiss();
				break;
			case 4:
				progressdialog.dismiss();
				break;
			case 5:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "获取信息失败！", Toast.LENGTH_LONG).show();
				break;
			case 6:
				progressdialog.dismiss();
				FootballContantDialog.alertIssueNOFQueue(FootballChooseNineLottery.this);
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
				Toast.makeText(getBaseContext(), "投注失败余额不足！", Toast.LENGTH_LONG).show();
				break;
			case 10:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "该期已结束！", Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示用户已注册
				break;
			case 11:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "彩票投注中", Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示系统结算，请稍后
				break;
			case 12:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注成功，出票成功！", Toast.LENGTH_LONG).show();
				for (int i = 0; i < ballTables.size(); i++) {
					ballTables.get(i).clearAllHighlights();
				}
				Intent intent = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent);
				break;
			case 14:
				// //需要添加AlertDialog提示用户登录成功
				progressdialog.dismiss();
				if (isFinishing() == false) {
					PublicMethod.showDialog(FootballChooseNineLottery.this);
				}
				// 投注成功后清除小球
				for (int i = 0; i < ballTables.size(); i++) {
					ballTables.get(i).clearAllHighlights();
				}
				changeTextSumMoney(getZhuShu());
				Intent intent2 = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent2);
				break;
			case 15:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(FootballChooseNineLottery.this, UserLogin.class);
				startActivity(intentSession);
				break;
			case 16:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示登录失败
				break;
			case 17:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败！", Toast.LENGTH_LONG).show();
				break;
			case 18:
				// 将图片的背景设置回原来的图案表示可点击
				rxj_btn_touzhu.setImageResource(R.drawable.imageselecter);
				break;
			case 19:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "没有期号可以投注！", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(getBaseContext(), "投注期号不存在，或已过期！", Toast.LENGTH_SHORT).show();
				break;
			case 24:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), msg.obj + "", Toast.LENGTH_SHORT).show();
				break;
			case 25:
				progressdialog.dismiss();
				if (isOne) {
					isOne = false;
					getTeamInfo(0);
				} else {
					showBatchcodesDialog(bactchCodes);
				}
				break;
			}
		}
	};

	/**
	 * 初始化列表
	 */
	public void initList() {
		footBallList = (ListView) findViewById(R.id.buy_footballlottery_list);
		list = getListForMainListAdapter();
		ballTables.clear();// 每次初始化足彩选区列表就清空BallTable的 Vector中的数据
		listViewDemo = new ListViewAdapter(this, list);
		footBallList.setAdapter(listViewDemo);
	}

	public void createView() {

		mSeekBarBeishu = (SeekBar) findViewById(R.id.buy_footballlottery_seekbar_muti);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		iProgressBeishu = 1;
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mTextBeishu = (TextView) findViewById(R.id.buy_footballlottery_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		setSeekWhenAddOrSub(R.id.buy_footballlottery_img_subtract_beishu, null, -1, mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.buy_footballlottery_img_add_beishu, null, 1, mSeekBarBeishu, true);
		rxj_btn_touzhu = (ImageButton) findViewById(R.id.buy_footballlottery_img_touzhu);
		rxj_btn_touzhu.setOnClickListener(new OnClickListener() {
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
	private String getTouzhuAlert() {
		int iZhuShu = getZhuShu();
		return "注数：" + iZhuShu / mSeekBarBeishu.getProgress() + "注    " + "倍数：" + mSeekBarBeishu.getProgress()
				+ "倍    " + "金额：" + (iZhuShu * 2) + "元";
	}

	/**
	 * 假设数组的id为ai 每个小球的id为ai*10+小球.Resid 这样就能保证小球id的唯一性
	 */
	public static final int RENXJ_START_ID = 0x83000001;
	/** 小球起始id */
	public int iAllBallWidth;

	public class ListViewAdapter extends BaseAdapter {
		private Context context;
		private List<Map<String, Object>> mList;
		private LayoutInflater mInflater; // 扩充主列表布局

		public ListViewAdapter(Context context, List<Map<String, Object>> list) {
			this.context = context;
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			int[] aResId = { R.drawable.grey, R.drawable.red };

			int START_ID;
			START_ID = RENXJ_START_ID + position * 3;

			String team1 = (String) mList.get(position).get("TEAM1");
			String team2 = (String) mList.get(position).get("TEAM2");
			String scores1 = (String) mList.get(position).get("SCORES1");
			String scores2 = (String) mList.get(position).get("SCORES2");
			final int indexPosition = position;
			ViewHolder holder = null;

			convertView = mInflater.inflate(R.layout.buy_football_sforchosenine_listitem, null);

			holder = new ViewHolder();
			holder.lie = ((TextView) convertView.findViewById(R.id.lienum));
			holder.teamname = (TextView) convertView.findViewById(R.id.teamname);
			holder.teamrank = (TextView) convertView.findViewById(R.id.teamrank);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.shengfucai_layout);
			holder.info = (ImageView) convertView.findViewById(R.id.fenxi);
			LinearLayout linearChoseNine = (LinearLayout) convertView.findViewById(R.id.sforchoosenine_item);
			setFootballListItemBackground(linearChoseNine, position);
			final BallTable renxuanjRow = makeBallTable((LinearLayout) convertView, R.id.shengfucai_ball, aResId,
					START_ID);
			ballTables.add(renxuanjRow);
			Vector<OneBallView> BallViews = renxuanjRow.getBallViews();
			for (int i = 0; i < BallViews.size(); i++) {
				final OneBallView ball = BallViews.get(i);
				ball.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {

						// 点击前判断单式和复式，处理点击小球的改变
						if (isMultipleBets()) {
							// 如果是复式投注
							if (ball.getiShowString().equals("胆")) {
								// 如果点击胆球且本场比赛被选中，并且不能超过8个，则胆球变色
								if (ballTables.get(indexPosition).getHighlightBallNums() > 0 && getDanNums() < 8) {
									ball.changeBallColor();
								}
							} else {
								// 如果点击不是胆球，则小球变色
								ball.changeBallColor();

								// 判断该场比赛是否被选中，如果点击后，比赛为被选中，则取消胆球
								OneBallView danBall = ballTables.get(indexPosition).ballViewVector.get(3);
								if (danBall.getShowId() == 1
										&& ballTables.get(indexPosition).getHighlightBallNums() == 1) {
									danBall.changeBallColor();
								}
							}
						} else {
							// 如果是单式投注
							if (!ball.getiShowString().equals("胆")) {
								// 不是胆球，则改变颜色
								ball.changeBallColor();
							} else {
								// 是胆球，则提示用户不能设胆
								Toast.makeText(context, "您不是复式投注，无法设胆", Toast.LENGTH_SHORT).show();
							}
						}

						// 点击完成后，判断当前是否是复式投注，处理胆球改变：如果不是复式投注，取消所有设胆
						if (!isMultipleBets()) {
							for (int i = 0; i < ballTables.size(); i++) {
								Vector<OneBallView> rowBalls = ballTables.get(i).ballViewVector;

								for (int j = 0; j < rowBalls.size(); j++) {
									OneBallView danBallView = rowBalls.get(3);

									if (danBallView.getShowId() == 1) {
										danBallView.changeBallColor();
									}
								}
							}
						}
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
				if (scores1 != null)
					holder.teamrank.setText("  " + scores1 + "   " + scores2);
			} catch (Exception e) {

			}
			holder.info.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					getFootballAnalysisData(indexPosition);
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
	}

	/**
	 * 判断投注是否是复式投注
	 * 
	 * @return 是否是复式投注标识
	 */
	public boolean isMultipleBets() {
		int rawNum = 0;

		for (int i = 0; i < ballTables.size(); i++) {
			Vector<OneBallView> rowBalls = ballTables.get(i).ballViewVector;

			// 遍历前三个小球，如果有选中，则本场比赛为选中
			for (int j = 0; j < rowBalls.size() - 1; j++) {
				int isSelected = rowBalls.get(j).getShowId();

				if (isSelected == 1) {
					rawNum++;
					break;
				}
			}
		}

		// 如果选中场数大于9则为复式，否则为单式
		if (rawNum > 9) {
			return true;
		}

		return false;
	}
	
	/**
	 * 获取选取胆球数目
	 * @return 胆球数目
	 */
	private int getDanNums() {
		int count = 0;
		
		for(int i = 0; i < ballTables.size(); i++){
			if(ballTables.get(i).ballViewVector.get(3).getShowId() == 1){
				count ++;
			}
		}
		return count;
	}

	/**
	 * 主列表中相应的数据
	 */
	private List<Map<String, Object>> getListForMainListAdapter() {

		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
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
	 * 计算任选九胆投投注注数
	 * 
	 * @return 投注注数
	 */
	public int getZhuShu() {
		int betNums = 0;
		int gameNums = ballTables.size();
		int beishu = mSeekBarBeishu.getProgress();

		List<Integer> danGamesList = new ArrayList<Integer>();
		List<Integer> notDanGamesList = new ArrayList<Integer>();

		for (int i = 0; i < gameNums; i++) {
			BallTable ballTable = ballTables.get(i);
			if (ballTable.ballViewVector.get(3).getShowId() == 1) {
				danGamesList.add(ballTable.getHighlightBallNums() - 1);
			} else if (ballTable.getHighlightBallNums() > 0) {
				notDanGamesList.add(ballTable.getHighlightBallNums());
			}
		}
		
		if(danGamesList.size() + notDanGamesList.size() < 9){
			return 0;
		}else {
			betNums = caculateZhuShu(listToInts(danGamesList), getGamesZhuHe(listToInts(notDanGamesList), 9 - danGamesList.size())) * beishu;
		}
		
		return betNums;
	}

	private int[] listToInts(List<Integer> list) {
		int[] result = new int[list.size()];

		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	/**
	 * 计算任九胆投注数：注数=（胆投数组元素1*元素2...）*（（非胆投数组1元素1*元素2...）+ （非胆投数组2元素1*元素2）+...）
	 * 
	 * @param danGames
	 *            胆投场次数组
	 * @param notDanGames
	 *            非胆投场次组合数组
	 * @return
	 */
	private static int caculateZhuShu(int[] danGames, int[][] notDanGames) {
		int betNums = 0;

		// 计算胆部组合数
		int danBetNums = 1;
		for (int dan_i = 0; dan_i < danGames.length; dan_i++) {
			danBetNums = danBetNums * danGames[dan_i];
		}

		// 计算非胆部组合数
		int noDanBetNums = 0;
		for (int nodan_i = 0; nodan_i < notDanGames.length; nodan_i++) {
			int[] notDanGame = notDanGames[nodan_i];
			int noDanBetNum = 1;

			for (int nodan_j = 0; nodan_j < notDanGame.length; nodan_j++) {
				noDanBetNum = noDanBetNum * notDanGame[nodan_j];
			}

			noDanBetNums = noDanBetNums + noDanBetNum;
		}

		// 返回注数
		return danBetNums * noDanBetNums;
	}

	/**
	 * 从数组sourceInt中取出num个组合：规定前num-1为，最后一位去子组合的分组和，然后将固定值和子组合合并 如：{1,2,3}取2位组合：
	 * 第1步：固定第1为1，取子组合{2，3}的2-1位
	 * 第2步：2-1=1位，无需固定为，则划分子组合为一位子组合{{2}{3}}；否则继续固定第n为，去子组合m-n为；
	 * 第3步：将一位子组合于前一位固定值合并，为{2,1}{3,1}；若还有固定值，将合并组合和前固定值组合，至组合完毕
	 * 
	 * @param sourceInt
	 *            源数组
	 * @param num
	 *            子组合大小
	 * @return 子组合数据
	 */
	private static int[][] getGamesZhuHe(int[] sourceInt, int num) {
		if (num <= sourceInt.length) {
			// 计算结果组合的大小，更初始化结果组合
			int resultSize = zuHe(sourceInt.length, num);
			int[][] resultInts = new int[resultSize][];

			// 从第fixed_i开始固定，去子组合的num-1位组合。
			// 如{1,2,3,4}中去2位组合，则先固定第1位1，然后去{2,3,4}的(2-1)位子组合
			for (int fixed_i = 0; fixed_i <= sourceInt.length - num; fixed_i++) {
				// 如果取1位子组合，递归返回。
				// 如去{2,3,4}一位组合，则返回{{2},{3},{4}}
				if (num == 1) {
					for (int source_i = 0; source_i < sourceInt.length; source_i++) {
						int[] subInt = new int[num];
						subInt[0] = sourceInt[source_i];
						resultInts[source_i] = subInt;
					}
				} else {
					// 取第fixed_i位为固定值。
					// 如第1次循环，固定值为1...依次类推
					int fixedInt = sourceInt[fixed_i];
					// 获取固定值后的子组合。
					// 第1次循环，子组合为{2,3,4}..依次类推
					int[] nextInt = subIntByIndex(sourceInt, fixed_i);

					// 递归获取{2,3,4}组合中的2-1位子组合集合
					// 第一次返回{{2}，{3}，{4}}...依次类推
					int[][] tempInts = getGamesZhuHe(nextInt, num - 1);

					// 将返回的子组合与固定值组合
					// 如第一次返回{{2},{3},{4}}与固定值1组合{{2,1},{3,1},{4,1}}...依次类推
					for (int temp_i = 0; temp_i < tempInts.length; temp_i++) {
						int[] tempInt = new int[tempInts[temp_i].length + 1];
						tempInt = addInt(tempInt, tempInts[temp_i]);
						tempInt[tempInt.length - 1] = fixedInt;

						// 将子组合集合加入结果组合中
						int size = getIntsSize(resultInts);
						resultInts[size] = tempInt;
					}

				}
			}
			return resultInts;
		} else {
			System.out.println("num error!!!");
			return null;
		}
	}

	/**
	 * 获取数组的大小
	 * 
	 * @param resultInts
	 *            数组对象
	 * @return 数组大小
	 */
	private static int getIntsSize(int[][] resultInts) {
		int size = 0;

		for (int i = 0; i < resultInts.length; i++) {
			if (resultInts[i] != null) {
				size++;
			}
		}
		return size;
	}

	/**
	 * 将sourceInt数组，添加到destinationInt数组中，从第一位开始填充
	 * 
	 * @param resultInt
	 *            结果数组
	 * @param sourceInt
	 *            源数组
	 * @return 结果数组
	 */
	private static int[] addInt(int[] resultInt, int[] sourceInt) {
		for (int source_i = 0; source_i < sourceInt.length; source_i++) {
			resultInt[source_i] = sourceInt[source_i];
		}

		return resultInt;
	}

	/**
	 * 截取数组的子数组，从start位至末尾
	 * 
	 * @param sourceInt
	 *            源数组
	 * @param start
	 *            起始位
	 * @return
	 */
	private static int[] subIntByIndex(int[] sourceInt, int start) {
		int[] resultInt = new int[sourceInt.length - start - 1];

		for (int i = start + 1, j = 0; i < sourceInt.length; i++, j++) {
			resultInt[j] = sourceInt[i];
		}

		return resultInt;
	}

	/**
	 * 求a取b的组合数
	 */
	private static int zuHe(int a, int b) {
		int up = 1;
		for (int up_i = 0; up_i < b; up_i++) {
			up = up * a;
			a--;
		}

		int down = jieCheng(b);

		return up / down;
	}

	/**
	 * 求b的阶乘
	 */
	private static int jieCheng(int b) {
		int result = 0;

		if (b == 1 || b == 0) {
			result = b;
		} else {
			result = b * jieCheng(b - 1);
		}

		return result;
	}

	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {
		case R.id.buy_footballlottery_seekbar_muti:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			changeTextSumMoney(getZhuShu());
			break;
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	public void onStopTrackingTouch(SeekBar seekBar) {

	}
	
	public String getZhuMa(){
		String zhuMaString = null;
		StringBuffer danMaString = new StringBuffer();
		StringBuffer tuoMaString = new StringBuffer();
		
		Boolean isDantou = false;
		
		for(int i =0; i < ballTables.size(); i++){
			int balls[] = ballTables.get(i).getHighlightBallNOs();
			
			//如果设胆,胆码区获取胆码号，托码区追加#
			if(ballTables.get(i).ballViewVector.get(3).getShowId() == 1){
				isDantou  = true;
		
				for(int j = 0; j < balls.length;j++){
					if(balls[j] != -1){
						danMaString.append(balls[j]);
					}
				}
				
				tuoMaString.append("#");
				if(i <ballTables.size() - 1){
					danMaString.append(",");
					tuoMaString.append(",");
				}
				
			}//如果不设胆，胆码区追加#，拖码区追加号码
			else{
				if(balls.length != 0){
					for (int j = 0; j < balls.length; j++) {
						tuoMaString.append(balls[j]);
					}
				}else{
					tuoMaString.append("#");
				}
				
				danMaString.append("#");
				if(i < ballTables.size() - 1){
					danMaString.append(",");
					tuoMaString.append(",");
				}
			}
		}
		
		if(isDantou){
			zhuMaString = danMaString.toString() + "$" + tuoMaString.toString();
		}else{
			zhuMaString = tuoMaString.toString();
		}
		
		return zhuMaString;
	}

	public void beginTouZhu() {
		int iZhuShu = getZhuShu();
		RWSharedPreferences pre = new RWSharedPreferences(FootballChooseNineLottery.this, "addInfo");
		String sessionIdStr = pre.getStringValue("sessionid");
		if (sessionIdStr == null || sessionIdStr.equals("")) {
			Intent intentSession = new Intent(FootballChooseNineLottery.this, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			if (iZhuShu == 0) {
				Toast.makeText(FootballChooseNineLottery.this, "请至少选择一注！", Toast.LENGTH_SHORT).show();
			} else if (iZhuShu * 2 > 20000) {
				DialogExcessive();
			} else {
				// String sTouzhuAlert = getTouzhuAlert();
				// alert(sTouzhuAlert,getFormatZhuma());
				initBetPojo();
				toorderdetail();
			}
		}
	}

	void toorderdetail() {
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		app.setPojo(betPojo);
		Intent intent = new Intent(FootballChooseNineLottery.this, Footballorderdail.class);
		intent.putExtra("tpye", "zc");
		intent.putExtra("zhuma", getZhuMa());
		startActivity(intent);
		for (int i = 0; i < ballTables.size(); i++) {
			ballTables.get(i).clearAllHighlights();
		}
	}

	/**
	 * 加减按钮事件监听方法
	 */
	private void setSeekWhenAddOrSub(int idFind, View iV, final int isAdd, final SeekBar mSeekBar,
			final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						iProgressBeishu++;
						if (iProgressBeishu > 99) {
							iProgressBeishu = 99;
						}
						mSeekBar.setProgress(iProgressBeishu);
					} else {
						iProgressBeishu--;
						if (iProgressBeishu < 1) {
							iProgressBeishu = 1;
						}
						mSeekBar.setProgress(iProgressBeishu);
					}
				}
			}
		});
	}

	// 提示框1 用来提醒选球规则
	// fqc delete 删除取消按钮 7/14/2010
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
			// title += obj.getString("num");
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
		if (!hTeam8.equals("")) {
			String hteam[] = hTeam8.split("\\|");
			row1_2.setText(hteam[0]);
			row2_2.setText(hteam[1]);
			row3_2.setText(hteam[2]);
			row4_1.setText(hteam[3] + "|" + hteam[4]);
		}
		if (!vTeam8.equals("")) {
			String vteam[] = vTeam8.split("\\|");
			row1_3.setText(vteam[0]);
			row2_3.setText(vteam[1]);
			row3_3.setText(vteam[2]);
			row4_2.setText(vteam[3] + "|" + vteam[4]);
		}
		if (!avgOdds.equals("")) {
			String avg[] = avgOdds.split("\\|");
			row1_1.setText(avg[0].substring(1));
			row2_1.setText(avg[1].substring(1));
			row3_1.setText(avg[2].substring(1));
		}
		Builder dialog = new AlertDialog.Builder(this).setTitle(title).setView(view)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialog.show();
	}

	/**
	 * 获得当前期数
	 * 
	 * @param string
	 * @return
	 */
	public String[] getLotno(String string) {
		String error_code;
		String str[] = new String[4];
		// ShellRWSharesPreferences
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		String notice = shellRW.getStringValue(string);
		// 判断取值是否为空 cc 2010/7/9
		if (!notice.equals("") || notice != null) {
			try {
				JSONObject obj = new JSONObject(notice);
				error_code = obj.getString("error_code");
				if (error_code.equals("000000")) {
					JSONArray value = obj.getJSONArray("value");
					JSONObject re = value.getJSONObject(0);
					str[0] = re.getString("BATCHCODE");
					str[1] = re.getString("ENDTIME");
					str[2] = re.getString("LOTNO");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 获取对阵矩阵
	 */
	public void getFootballGameVSData(final String lotno, final String batchcode) {
		showDialog(DIALOG1_KEY);
		new Thread(new Runnable() {
			public void run() {
				String error_code = "00";
				re = FootballInterface.getInstance().getZCData(lotno, batchcode);
				try {
					obj = new JSONObject(re);
					error_code = obj.getString("error_code");
					if (error_code.equals("000000")) {
						JSONArray value = obj.getJSONArray("value");
						teamInfos.clear();
						for (int i = 0; i < value.length(); i++) {
							JSONObject re = value.getJSONObject(i);
							TeamInfo team = new TeamInfo();
							team.hTeam = re.getString("HTeam");
							team.vTeam = re.getString("VTeam");
							String rank = re.getString("leagueRank");
							team.num = re.getString("num");
							if (rank != null && !rank.equals("")) {
								String str[] = rank.split("\\|");
								team.hRankNum = str[0];
								team.vRankNum = str[1];
							}
							teamInfos.add(team);
						}
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
				} catch (Exception e) {

				}
			}
		}).start();
	}

	/**
	 * 获取足彩分析数据
	 * 
	 * @param index
	 */
	public void getFootballAnalysisData(final int index) {
		showDialog(DIALOG1_KEY);
		new Thread(new Runnable() {
			public void run() {
				String error_code = "00";
				re = FootballInterface.getInstance().getZCInfo(qihaoxinxi[2], batchCode, teamInfos.get(index).num);

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

	private void initBetPojo() {
		RWSharedPreferences pre = new RWSharedPreferences(FootballChooseNineLottery.this, "addInfo");
		sessionid = pre.getStringValue("sessionid");
		phonenum = pre.getStringValue("phonenum");
		userno = pre.getStringValue("userno");
		betPojo.setPhonenum(phonenum);
		betPojo.setSessionid(sessionid);
		betPojo.setUserno(userno);
		betPojo.setBet_code(getZhuMa());
		betPojo.setLotno(lotno);
		betPojo.setBatchnum("1");
		betPojo.setBatchcode(batchCode);
		betPojo.setLotmulti(String.valueOf(iProgressBeishu));
		betPojo.setBettype("bet");
		betPojo.setAmount(getZhuShu() * 200 + "");
		betPojo.setZhushu(getZhuShu() + "");
	}

	public void toActivity(String zhuma) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(betPojo);
		} catch (IOException e) {
			return; // should not happen, so donot do error handling
		}

		Intent intent = new Intent(FootballChooseNineLottery.this, GiftActivity.class);
		intent.putExtra("info", byteStream.toByteArray());
		intent.putExtra("zhuma", zhuma);
		startActivity(intent);
	}

	private String getFormatZhuma() {
		return "第" + batchCode + "期\n" + "截止日期：" + qihaoxinxi[1] + "\n" + "选号结果：\n" + getZhuMa();
	}

	/**
	 * 单复式投注调用函数
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alert(String string, final String zhuma) {
		codeStr = zhuma;
		isGift = false;
		isJoin = false;
		isTouzhu = true;
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.alert_dialog_touzhu, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.alert_dialog_touzhu_linear_qihao_beishu);
		layout.setVisibility(LinearLayout.GONE);
		final AlertDialog dialog = new AlertDialog.Builder(this).setTitle("您选择的是").create();
		dialog.show();
		TextView text = (TextView) v.findViewById(R.id.alert_dialog_touzhu_text_one);
		text.setText(string);
		TextView textZhuma = (TextView) v.findViewById(R.id.alert_dialog_touzhu_text_zhuma);
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
				if (isGift) {
					toActivity(zhuma);
				} else if (isJoin) {
					toJoinActivity();
				} else if (isTouzhu) {
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
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isGift = isChecked;
			}
		});
		joinCheck.setPadding(50, 0, 0, 0);
		joinCheck.setButtonDrawable(R.drawable.check_select);
		// 实现记住密码 和 复选框的状态
		joinCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isJoin = isChecked;
			}
		});
		touzhuCheck.setPadding(50, 0, 0, 0);
		touzhuCheck.setButtonDrawable(R.drawable.check_select);
		// 实现记住密码 和 复选框的状态
		touzhuCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isTouzhu = isChecked;
			}
		});

		dialog.getWindow().setContentView(v);
	}

	/**
	 * 投注联网
	 */
	public void touZhuNet() {
		showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betPojo);
				try {
					JSONObject obj = new JSONObject(str);
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					touzhuhandler.handleMsg(error, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}

		});
		t.start();
	}

	public void toJoinActivity() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(betPojo);
		} catch (IOException e) {
			return; // should not happen, so donot do error handling
		}

		Intent intent = new Intent(FootballChooseNineLottery.this, JoinStartActivity.class);
		intent.putExtra("info", byteStream.toByteArray());
		startActivity(intent);

	}

	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		for (int i = 0; i < ballTables.size(); i++) {
			ballTables.get(i).clearAllHighlights();
		}
		String lotnoString = PublicMethod.toLotno(lotno);
		PublicMethod.showDialog(FootballChooseNineLottery.this, lotnoString + codeStr);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		for (int i = 0; i < ballTables.size(); i++) {
			ballTables.get(i).clearAllHighlights();
		}

	}

	private void getZCAdvanceBatchCodeData(final String Lotno) {
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
					if (errorCode.equals("0000")) {
						JSONArray batchCodeArray = advanceBatchCode.getJSONArray("result");
						bactchArray.clear();
						bactchCodes = new String[batchCodeArray.length()];
						for (int i = 0; i < batchCodeArray.length(); i++) {
							JSONObject item = batchCodeArray.getJSONObject(i);
							AdvanceBatchCode aa = new AdvanceBatchCode();
							// batchCode = item.getString("batchCode");
							aa.setBatchCode(formatBatchCode(item.getString("batchCode")));
							aa.setEndTime(formatEndtime(item.getString("endTime")));
							bactchCodes[i] = item.getString("batchCode");
							bactchArray.add(aa);
							if (qihaoxinxi[1] != null || qihaoxinxi[1].equals("")) {
								qihaoxinxi[1] = item.getString("endTime");
							}
						}
						Message msg = handler.obtainMessage();
						msg.what = 25;
						msg.obj = message;
						handler.sendMessage(msg);
					} else {
						Message msg = handler.obtainMessage();
						msg.what = 24;
						msg.obj = message;
						handler.sendMessage(msg);
						// Toast.makeText(FootballChooseNineLottery.this,
						// message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}
		}).start();
	}

	private class AdvanceBatchCode {
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

	private void showBatchcodesDialog(String[] batchCodes) {
		AlertDialog batchCodedialog = new AlertDialog.Builder(FootballChooseNineLottery.this).setTitle("任选九预售期")
				.setItems(batchCodes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/* User clicked so do some stuff */
						getTeamInfo(which);
					}
				}).create();
		batchCodedialog.show();
	}

	private void getTeamInfo(int which) {
		AdvanceBatchCode batchMsg = (AdvanceBatchCode) bactchArray.get(which);
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
		if (list != null) {
			list.clear();
		}
		getFootballGameVSData(Constants.LOTNO_RX9, bactchCodes[which]);
	}

	@Override
	void initBatchCodeView() {
		layout_football_issue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getZCAdvanceBatchCodeData(Constants.LOTNO_RX9);
			}
		});
	}

	/**
	 * 任选九选好号区域，加设胆选号球
	 */
	public BallTable makeBallTable(LinearLayout aParentView, int aLayoutId, int[] aResId, int aIdStart) {
		BallTable iBallTable = new BallTable(aParentView, aLayoutId, aIdStart);
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		int aFieldWidth = iScreenWidth / 3;
		int iBallViewWidth = aFieldWidth / 4 - 2;
		int iFieldWidth = aFieldWidth;
		/** 滚动条的宽度 */
		int scrollBarWidth = 6;
		/** 每一行的小球数量 */
		int viewNumPerLine = 4;
		/** 行的数量 */
		int lineNum = 1;

		/** 空白的大小 */
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2) * viewNumPerLine) / 2;
		int iBallViewNo = 0;

		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(aParentView.getContext());
			for (int col = 0; col < viewNumPerLine; col++) {
				/** 设置显示的数字 */
				String iStrTemp = "" + (iBallViewNo + 1);
				if (iStrTemp.equals("1")) {
					iStrTemp = "3";
				} else if (iStrTemp.equals("2")) {
					iStrTemp = "1";
				} else if (iStrTemp.equals("3")) {
					iStrTemp = "0";
				} else {
					iStrTemp = "胆";
				}

				/** 实例化一个BallView对象 */
				OneBallView tempBallView = new OneBallView(aParentView.getContext());
				/** 为这个tempView设置一个Id */
				tempBallView.setId(aIdStart + iBallViewNo);
				/** 将这个小球初始化出来 */
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp, aResId);

				/*** 将小球tempView添加到Table中 */
				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					/** 设置TableRow四个方向的空白像素 */
					lp.setMargins(margin + 1, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				/** iBallViewNo自增，循环设置小球的属性 */
				iBallViewNo++;
			}
			/** 新建的TableRow添加到TableLayout */
			iBallTable.tableLayout.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
		}

		return iBallTable;
	}

}
