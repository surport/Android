package com.ruyicai.activity.more;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.high.HghtOrderdeail;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.GT;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

public class LuckChoose2 extends Activity implements HandlerMsg {

	private LuckChoose2View panView;
	public static final int ROTATEITEMSELECT_FINISH = 0;
	public ArrayList<String> arrayList;

	public static int year, month, day;
	static {
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		if (iScreenWidth == 800) {
			BALL_WIDTH = iScreenWidth / 10;
		} else {
			BALL_WIDTH = iScreenWidth / 9;
		}
		Constants.type = "luck";
		handler = new MyHandler(this);

		setContentView(R.layout.luckchoose2);

		panView = (LuckChoose2View) this.findViewById(R.id.myView);

		/**
		 * 从具体的彩票进入幸运选号页
		 */
		String value = getIntent().getStringExtra("lotno");
		if (value != null) {
			for (int i = 0; i < arrayCaipiaoId.length; i++) {
				if (value.equals(arrayCaipiaoId[i])) {

					LuckChoose2View.anniuXuanzhongId[0] = i;
					LuckChoose2View.anniuMingzi[0] = arrayCaipiaoName[i];
					break;
				}
			}
		}

		// 取得彩票具体玩法
		caipiaoWanfaIndex = getIntent().getIntExtra("caipiaoWanfaIndex", -1);
		if (caipiaoWanfaIndex == -1) {
			setDefaultWanfa();
		}

		setDangqianCaipiao();

		panView.setLuckChoose2Instance(this);
		panView.setOnTouchListener(new MyOnTouchListener());
		panView.setLongClickable(true);

		panView.getHeight();
		panView.getWidth();

		panView.updateView();
		MobclickAgent.onEvent(this, "xingyunxuanhao");// BY点击首页的“幸运选号”。BY贺思明
	}

	/**
	 * 设置彩票的默认玩法
	 */
	private void setDefaultWanfa() {
		// 彩票种类
		switch (LuckChoose2View.anniuXuanzhongId[0]) {

		// 时时彩, 三星
		case 3:
			caipiaoWanfaIndex = 2;
			break;

		// 江西11选5
		case 4:

			// 11运夺金
		case 5:

			// 广东11选5: 任选五
		case 6:
			caipiaoWanfaIndex = 3;
			break;

		// 广东快乐十分
		case 7:
			caipiaoWanfaIndex = 5;
			break;

		default:
			caipiaoWanfaIndex = 0;
		}
	}

	/**
	 * 根据彩票种类,当前玩法, 注数 生成投注
	 */
	private int[][] createBetCode() {
		int[][] ret = null;

		// 彩票种类
		switch (LuckChoose2View.anniuXuanzhongId[0]) {

		// 双色球
		case 0:
			ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][7];
			int[] redArray;
			for (int i = 0; i < ret.length; i++) {
				// 随机红球
				redArray = PublicMethod.getRandomsWithoutCollision(6, 1, 33);
				Arrays.sort(redArray);
				System.arraycopy(redArray, 0, ret[i], 0, 6);

				// 随机蓝球
				ret[i][6] = PublicMethod.getRandomByRange(1, 16);
			}

			break;

		// 大乐透
		case 1:
			ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][7];
			for (int i = 0; i < ret.length; i++) {
				// 随机红球
				redArray = PublicMethod.getRandomsWithoutCollision(5, 1, 35);
				Arrays.sort(redArray);
				System.arraycopy(redArray, 0, ret[i], 0, 5);

				// 随机蓝球
				int[] blueArray = PublicMethod.getRandomsWithoutCollision(2, 1,
						12);
				Arrays.sort(blueArray);
				System.arraycopy(blueArray, 0, ret[i], 5, 2);
			}
			break;

		// 福彩3D
		case 2:
			ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][3];
			for (int i = 0; i < ret.length; i++) {
				for (int j = 0; j < 3; j++) {
					ret[i][j] = PublicMethod.getRandomByRange(0, 9);
				}
			}
			break;

		// 时时彩
		case 3:
			switch (caipiaoWanfaIndex) {
			// 1星
			case 0:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][1];
				for (int i = 0; i < ret.length; i++) {
					for (int j = 0; j < ret[0].length; j++) {
						ret[i][j] = PublicMethod.getRandomByRange(0, 9);
					}
				}
				break;

			// 2星
			case 1:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][2];
				for (int i = 0; i < ret.length; i++) {
					for (int j = 0; j < ret[0].length; j++) {
						ret[i][j] = PublicMethod.getRandomByRange(0, 9);
					}
				}
				break;

			// 3星
			case 2:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][3];
				for (int i = 0; i < ret.length; i++) {
					for (int j = 0; j < ret[0].length; j++) {
						ret[i][j] = PublicMethod.getRandomByRange(0, 9);
					}
				}
				break;

			// 5星
			case 3:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][5];
				for (int i = 0; i < ret.length; i++) {
					for (int j = 0; j < ret[0].length; j++) {
						ret[i][j] = PublicMethod.getRandomByRange(0, 9);
					}
				}
				break;

			// 大小
			case 4:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][2];
				for (int i = 0; i < ret.length; i++) {
					for (int j = 0; j < ret[0].length; j++) {
						ret[i][j] = PublicMethod.getRandomByRange(0, 3);
					}
				}
				break;
			}

			break;

		// 江西11选5
		case 4:

			// 11运夺金
		case 5:

			// 广东11选5
		case 6:
			switch (caipiaoWanfaIndex) {
			// 任选二
			case 0:

				// 任选三
			case 1:

				// 任选四
			case 2:

				// 任选五
			case 3:

				// 任选六
			case 4:

				// 任选七
			case 5:

				// 任选八
			case 6:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][caipiaoWanfaIndex + 2];
				for (int i = 0; i < ret.length; i++) {
					ret[i] = PublicMethod.getRandomsWithoutCollision(
							ret[0].length, 1, 11);
					Arrays.sort(ret[i]);
				}
				break;

			// 前一直选
			case 7:

				// 前二直选
			case 8:

				// 前三直选
			case 9:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][caipiaoWanfaIndex - 6];
				for (int i = 0; i < ret.length; i++) {
					ret[i] = PublicMethod.getRandomsWithoutCollision(
							ret[0].length, 1, 11);
				}
				break;

			// 前二组选
			case 10:

				// 前三组选
			case 11:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][caipiaoWanfaIndex - 8];
				for (int i = 0; i < ret.length; i++) {
					ret[i] = PublicMethod.getRandomsWithoutCollision(
							ret[0].length, 1, 11);
					Arrays.sort(ret[i]);
				}
				break;
			}
			break;

		// 广东快乐十分
		case 7:
			switch (caipiaoWanfaIndex) {
			// 选一数投
			case 0:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][1];
				for (int i = 0; i < ret.length; i++) {
					ret[i][0] = PublicMethod.getRandomByRange(1, 18);
				}
				break;

			// 选一红投
			case 1:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][1];
				for (int i = 0; i < ret.length; i++) {
					ret[i][0] = PublicMethod.getRandomByRange(19, 20);
				}
				break;

			// 任选二
			case 2:

				// 任选三
			case 3:

				// 任选四
			case 4:

				// 任选五
			case 5:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][caipiaoWanfaIndex];
				for (int i = 0; i < ret.length; i++) {
					ret[i] = PublicMethod.getRandomsWithoutCollision(
							ret[0].length, 1, 20);
					Arrays.sort(ret[i]);
				}
				break;

			// 选二连直
			case 6:

				// 选三连直
			case 7:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][caipiaoWanfaIndex - 4];
				for (int i = 0; i < ret.length; i++) {
					ret[i] = PublicMethod.getRandomsWithoutCollision(
							ret[0].length, 1, 20);
				}
				break;

			// 选二连组
			case 8:

				// 选三连组
			case 9:
				ret = new int[LuckChoose2View.anniuXuanzhongId[2] + 1][caipiaoWanfaIndex - 6];
				for (int i = 0; i < ret.length; i++) {
					ret[i] = PublicMethod.getRandomsWithoutCollision(
							ret[0].length, 1, 20);
					Arrays.sort(ret[i]);
				}
				break;
			}
			break;
		}

		return ret;
	}

	private class MyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:

				panView.pointerPressed(event.getX(), event.getY());
				break;

			case MotionEvent.ACTION_MOVE:
				break;

			case MotionEvent.ACTION_UP:
				break;
			}
			return true;
		}
	}

	/**
	 * 根据姓名或生日幸运选号
	 */
	public void xuanhaoXingmingHuoShengri() {
		// 姓名幸运选号未输入时
		if (LuckChoose2View.anniuXuanzhongId[1] == 2
				&& (LuckChoose2View.xingming == null || LuckChoose2View.xingming
						.trim().length() == 0)) {
			alertInputName(LuckChoose2View.xingming);
			return;
		}

		showDialog(PROGRESS_XUANHAO);

		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handlerXuanhao.sendEmptyMessage(0);
			}
		}.start();
	}

	/**
	 * 点击输入框事件
	 */
	public void shurukuangShijian() {
		// 生日
		if (LuckChoose2View.anniuXuanzhongId[1] == 2) {
			alertInputName(LuckChoose2View.xingming);
		} else if (LuckChoose2View.anniuXuanzhongId[1] == 3) {
			alertDatePicker();

		}
	}

	/**
	 * 根据角度返回图片索引 0到 23
	 * 
	 * @param jiaodu
	 * @return
	 */
	public static int getIndexFromJiaodu(int jiaodu) {
		int ret = 24 - jiaodu >> 1;
		if (ret == 12) {
			ret = 0;
		}
		return ret;
	}

	/**
	 * 当前事件按钮，当前选中项
	 */
	private int currentArrayId, currentSelectedItemId;

	/**
	 * 日期选择框
	 */
	public void alertDatePicker() {
		DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
			// 监听器，用户监听用户点下DatePikerDialog的set按钮时，所设置的年月日
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				LuckChoose2.year = year;
				LuckChoose2.month = monthOfYear;
				LuckChoose2.day = dayOfMonth;
			}
		};
		DatePickerDialog datePickerDialog = new DatePickerDialog(
				LuckChoose2.this, onDateSetListener, year, month, day);
		datePickerDialog.setTitle("请输入生日");
		datePickerDialog.show();
	}

	/**
	 * 弹出框
	 */
	public void alertDialog(int arrayId) {
		currentArrayId = arrayId;

		/**
		 * 弹出框菜单项, 彩票，玩法，倍数
		 */
		String[] temp = null;

		switch (currentArrayId) {
		case 0:
			temp = arrayCaipiaoName;
			break;

		case 1:
			temp = arrayCaipiaoWanfa;
			break;

		case 2:
			temp = arrayCaipiaoBeishu;
			break;
		}

		new AlertDialog.Builder(LuckChoose2.this).setTitle("请选择")
				.setItems(temp, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						currentSelectedItemId = which;

						/**
						 * 弹出框菜单项, 彩票，玩法，倍数
						 */
						String[] temp = null;

						switch (currentArrayId) {
						case 0:
							temp = arrayCaipiaoName;
							break;

						case 1:
							temp = arrayCaipiaoWanfa;
							break;

						case 2:
							temp = arrayCaipiaoBeishu;
							break;
						}

						LuckChoose2View.anniuXuanzhongId[currentArrayId] = currentSelectedItemId;
						LuckChoose2View.anniuMingzi[currentArrayId] = temp[currentSelectedItemId];

						switch (currentArrayId) {
						// 彩种
						case 0:
							// 设置彩票玩法
							setDefaultWanfa();
							setDangqianCaipiao();
							break;

						// 玩法
						case 1:
							// 星座与生肖
							if ((currentSelectedItemId == 0 || currentSelectedItemId == 1)) {
								panView.setInputBackPng(false);
								panView.setZhuanpanNeirong(currentSelectedItemId == 1);

								// 姓名
							} else if (currentSelectedItemId == 2) {
								panView.setInputBackPng(true);
								alertInputName(LuckChoose2View.xingming);
								// 日期
							} else if (currentSelectedItemId == 3) {
								panView.setInputBackPng(true);
								alertDatePicker();
							}
							break;

						// 注数
						case 2:
							break;
						}

						panView.updateView();
					}
				}).create().show();
	}

	public void alertMessage() {
		type02 = arrayCaipiaoCode[LuckChoose2View.anniuXuanzhongId[0]];
		showXingYunXuanHaoListView(ID_CLLN_SHOWBALLMONRY, type02);
	}

	/**
	 * 请输入姓名
	 */
	public void alertInputName(String text) {
		final EditText et = new EditText(LuckChoose2.this);
		et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
		if (text != null) {
			et.setText(text);
		}
		new AlertDialog.Builder(LuckChoose2.this)
				.setIcon(R.drawable.icon)
				.setTitle("请输入姓名")
				.setView(et)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						LuckChoose2View.xingming = et.getText().toString();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				}).create().show();
	}

	public static final int PROGRESS_WANGLUOLIANJIE = 0, PROGRESS_XUANHAO = 1;

	private ProgressDialog progressDialog;

	public void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case PROGRESS_WANGLUOLIANJIE:
			progressdialog = new ProgressDialog(this);
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			return progressdialog;

		case PROGRESS_XUANHAO: {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("幸运选号中...");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			return progressDialog;
		}
		}
		return null;
	}

	/**
	 * 外部线程操作界面
	 */
	Handler handlerXuanhao = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			closeProgressDialog();
			alertMessage();
		}

	};

	private String type01, type02, type03, type04, type05, type06, type07,
			type08; // 彩票种类
	private PopupWindow popupwindow;
	private int iScreenWidth;
	public final static int ID_CLLN_XINGMING_DIALOG_LISTVIEW = 106;
	public final static int ID_CLLN_SHOWBALLMONRY = 117;
	public final static int ID_CLLN_SHOW_ZHIFU_DIALOG = 118;
	public final static int ID_CLLN_SHOW_TRADE_SUCCESS = 119;
	private int[][] receiveRandomNum;
	TextView agreeAndpayTitleView; // 幸运选号中确认页面的标题 周黎鸣 代码修改：7.3添加的代码
	LinearLayout agreePayBallLayout01; // 周黎鸣 代码修改：7.3添加的代码
	LinearLayout agreePayBallLayout02;
	LinearLayout agreePayBallLayout03;
	LinearLayout agreePayBallLayout04;
	LinearLayout agreePayBallLayout05;
	int iProgressBeishu = 1;
	int iProgressQishu = 1;
	int iProgressJizhu = 1;
	public static int BALL_WIDTH;// 小球宽
	private static int[] aRedColorResId = { R.drawable.red };
	private static int[] aBlueColorResId = { R.drawable.blue };
	TextView mTextMoney;
	TextView mTextJizhu; // 几注
	TextView mTextBeishu;// 倍数
	TextView mTextQishu;// 期数
	SeekBar mSeekBarJizhu; // 注数
	SeekBar mSeekBarBeishu;// 倍数
	SeekBar mSeekBarQishu;// 期数
	LinearLayout[] layoutAll;
	AlertDialog.Builder builderXingming; // 姓名的对话框
	MyHandler handler;// 自定义handler
	private ProgressDialog progressdialog;
	public BetAndGiftPojo betAndGift = new BetAndGiftPojo();// 投注信息类
	boolean isWindow = false;

	/**
	 * 幸运选号列表选择
	 * 
	 * @param listviewid
	 * @param aGameType
	 */
	protected void showXingYunXuanHaoListView(int listviewid, String aGameType) {
		switch (listviewid) {
		case ID_CLLN_SHOWBALLMONRY:
			dialogLuck(aGameType);
			break;
		case ID_CLLN_SHOW_ZHIFU_DIALOG:
			showAgreeAndPayDialog(aGameType);
			break;
		case ID_CLLN_SHOW_TRADE_SUCCESS:
			showTradeSuccess();
			break;
		}
	}

	/**
	 * 提示支付成功对话框
	 */
	protected void showTradeSuccess() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setTitle("您投注成功！");

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}

				});
		builder.setNegativeButton(R.string.str_return,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// 返回
					}

				});
		builder.show();
	}

	/**
	 * 投注列表Adapter
	 */
	class BetCodeAdapter extends BaseAdapter {
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<int[]> codeInfos = new ArrayList<int[]>();

		public BetCodeAdapter(Context context, int[][] betCode) {
			mInflater = LayoutInflater.from(context);

			for (int i = 0; i < betCode.length; i++) {
				codeInfos.add(betCode[i]);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return codeInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return codeInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.buy_add_dialog_list_item,
					null);

			// 序号
			TextView textNum = (TextView) convertView
					.findViewById(R.id.buy_add_list_item_text_num);
			textNum.setText("" + (position + 1));

			// 投注内容, 颜色显示
			TextView textCode = (TextView) convertView
					.findViewById(R.id.buy_add_list_item_text_code);
			setBetCodeColor(position, textCode);

			// 隐藏注数，金额，删除按钮
			TextView textZhuShu = (TextView) convertView
					.findViewById(R.id.buy_add_list_item_text_zhushu);
			TextView textAmt = (TextView) convertView
					.findViewById(R.id.buy_add_list_item_text_amt);
			Button btnDelet = (Button) convertView
					.findViewById(R.id.buy_add_dialog_delet);
			textZhuShu.setVisibility(View.GONE);
			textAmt.setVisibility(View.GONE);
			btnDelet.setVisibility(View.GONE);

			return convertView;
		}

		/**
		 * 颜色显示投注项
		 * 
		 * @param position
		 *            : 第几条
		 */
		public void setBetCodeColor(int position, TextView textCode) {

			// 设置TextView颜色
			SpannableStringBuilder builder = new SpannableStringBuilder();
			int[] betArray = codeInfos.get(position);

			// 彩票种类
			switch (LuckChoose2View.anniuXuanzhongId[0]) {

			// 双色球
			case 0:
				// 红球6个
				for (int i = 0; i < 5; i++) {
					builder.append(get2bit(betArray[i]) + ",");
				}
				builder.append(get2bit(betArray[5]) + "|");

				// 红球字符串的长度
				int redLength = builder.length();

				// 蓝球
				builder.append("" + get2bit(betArray[6]));

				// 设置颜色
				builder.setSpan(new ForegroundColorSpan(Color.RED), 0,
						redLength - 1, Spanned.SPAN_COMPOSING);
				builder.setSpan(new ForegroundColorSpan(Color.BLUE), redLength,
						builder.length(), Spanned.SPAN_COMPOSING);
				break;

			// 大乐透
			case 1:
				// 红球6个
				for (int i = 0; i < 4; i++) {
					builder.append(get2bit(betArray[i]) + ",");
				}
				builder.append(get2bit(betArray[4]) + "|");

				// 红球字符串的长度
				redLength = builder.length();

				// 蓝球
				builder.append(get2bit(betArray[5]) + ",");
				builder.append(get2bit(betArray[6]));

				// 设置颜色
				builder.setSpan(new ForegroundColorSpan(Color.RED), 0,
						redLength - 1, Spanned.SPAN_COMPOSING);
				builder.setSpan(new ForegroundColorSpan(Color.BLUE), redLength,
						builder.length(), Spanned.SPAN_COMPOSING);
				break;

			// 福彩3D
			case 2:
				redLength = 0;
				for (int i = 0; i < betArray.length; i++) {
					builder.append("" + betArray[i]);
					builder.setSpan(new ForegroundColorSpan(Color.RED),
							redLength, builder.length(), Spanned.SPAN_COMPOSING);
					if (i < betArray.length - 1) {
						builder.append("|");
						redLength = builder.length();
					}
				}
				break;

			// 时时彩
			case 3:

				switch (caipiaoWanfaIndex) {
				// 一星
				case 0:
					// 二星
				case 1:
					// 三星
				case 2:
					// 五星
				case 3:
					redLength = 0;
					for (int i = 0; i < betArray.length; i++) {
						builder.append("" + betArray[i]);
						builder.setSpan(new ForegroundColorSpan(Color.RED),
								redLength, builder.length(),
								Spanned.SPAN_COMPOSING);
						if (i < betArray.length - 1) {
							builder.append("|");
							redLength = builder.length();
						}
					}
					break;
				// 大小
				case 4:
					String[] codeName = { "大", "小", "单", "双" };
					redLength = 0;
					for (int i = 0; i < betArray.length; i++) {
						builder.append(codeName[betArray[i]]);
						builder.setSpan(new ForegroundColorSpan(Color.RED),
								redLength, builder.length(),
								Spanned.SPAN_COMPOSING);
						if (i < betArray.length - 1) {
							builder.append("|");
							redLength = builder.length();
						}
					}
					break;
				}

				break;

			// 江西11选5
			case 4:

				// 11运夺金
			case 5:

				// 广东11选5
			case 6:

				switch (caipiaoWanfaIndex) {
				// 直选二
				case 8:
					// 直选三
				case 9:
					redLength = 0;
					for (int i = 0; i < betArray.length; i++) {
						builder.append(get2bit(betArray[i]));
						builder.setSpan(new ForegroundColorSpan(Color.RED),
								redLength, builder.length(),
								Spanned.SPAN_COMPOSING);
						if (i < betArray.length - 1) {
							builder.append("|");
							redLength = builder.length();
						}
					}
					break;

				// 组选
				default:
					for (int i = 0; i < betArray.length; i++) {
						builder.append(get2bit(betArray[i]));
						if (i < betArray.length - 1) {
							builder.append(",");
						}
					}
					builder.setSpan(new ForegroundColorSpan(Color.RED), 0,
							builder.length(), Spanned.SPAN_COMPOSING);
					break;
				}

				break;

			// 广东快乐十分
			case 7:

				switch (caipiaoWanfaIndex) {
				// 选二连直
				case 6:
					// 选三连直
				case 7:
					redLength = 0;
					for (int i = 0; i < betArray.length; i++) {
						builder.append(get2bit(betArray[i]));
						builder.setSpan(new ForegroundColorSpan(Color.RED),
								redLength, builder.length(),
								Spanned.SPAN_COMPOSING);
						if (i < betArray.length - 1) {
							builder.append("|");
							redLength = builder.length();
						}
					}
					break;

				// 组选
				default:
					for (int i = 0; i < betArray.length; i++) {
						builder.append(get2bit(betArray[i]));
						if (i < betArray.length - 1) {
							builder.append(",");
						}
					}
					builder.setSpan(new ForegroundColorSpan(Color.RED), 0,
							builder.length(), Spanned.SPAN_COMPOSING);
					break;
				}

				break;
			}

			textCode.setTextSize(20);
			textCode.setText(builder, BufferType.EDITABLE);
		}
	}

	/**
	 * 幸运号码弹出框
	 */
	private AlertDialog luckNumberDialog;

	/**
	 * 幸运选号对话框
	 */
	private void dialogLuck(String aGameType) {
		type05 = aGameType;
		type06 = arrayCaipiaoCode[LuckChoose2View.anniuXuanzhongId[0]];

		LayoutInflater factory = LayoutInflater.from(this);
		luckNumberDialog = new AlertDialog.Builder(this).create();
		View view = factory.inflate(R.layout.buy_add_dialog, null);

		// 标题
		TextView title = (TextView) view.findViewById(R.id.zfb_text_title);
		title.setText("幸运号码");

		// 生成随机投注内容
		receiveRandomNum = createBetCode();

		// 投注列表
		ListView listView = (ListView) view
				.findViewById(R.id.buy_add_dialog_list);

		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < LuckChoose2View.anniuXuanzhongId[2] + 1; i++) {
			arrayList.add("字符串 " + i);
		}

		BetCodeAdapter adapter = new BetCodeAdapter(this, receiveRandomNum);
		listView.setAdapter(adapter);

		// 按钮事件
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.ok) {
					buyLuck();
				} else {
					luckNumberDialog.dismiss();
				}
			}

		};

		// 投注与取消按钮
		Button touzhu = (Button) view.findViewById(R.id.ok);
		touzhu.setText("立即投注");
		touzhu.setOnClickListener(onClickListener);

		Button quxiao = (Button) view.findViewById(R.id.canel);
		quxiao.setOnClickListener(onClickListener);

		// 显示弹出框
		luckNumberDialog.show();
		luckNumberDialog.getWindow().setContentView(view);

	}

	/**
	 * 注数和钱数
	 */
	public void showTextSumMoney() {
		int iZhuShu = iProgressJizhu;
		int iBeiShu = iProgressBeishu;
		int iQiShu = iProgressQishu;
		String iTempString = "总金额：" + (iZhuShu * iBeiShu * iQiShu * 2)
				+ "元\n\n" + "确认支付吗？";
		mTextMoney.setText(iTempString);
	}

	/**
	 * 彩种分类 zlm 8.11 代码修改
	 * 
	 * @param v
	 */
	public void gameClassify(View v) {
		if (Math.floor(v.getId() / 4) == 25) {
			type02 = "ssq";
		} else if (Math.floor(v.getId() / 4) == 26) {
			type02 = "fc3d";
		} else if (Math.floor(v.getId() / 4) == 27) {
			type02 = "qlc";
		} else if (Math.floor(v.getId() / 4) == 28) { // 排列三
			type02 = "pl3";
		} else if (Math.floor(v.getId() / 4) == 29) { // 超级大乐透
			type02 = "cjdlt";
		}
	}

	/**
	 * 幸运选号支付方法
	 */
	protected void buyLuck() {
		// 周黎鸣 7.4 代码修改：添加登陆的判断
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		String sessionidStr = shellRW.getStringValue("sessionid");
		if (sessionidStr == null || sessionidStr.equals("")) {
			Intent intentSession = new Intent(this, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			bet(type06, 1);
		}
	}

	/**
	 * 幸运选号投注联网
	 * 
	 */
	public void luckNet(final BetAndGiftPojo betAndGift) {
		Controller.getInstance(LuckChoose2.this).doBettingAction(handler, betAndGift);
	}

	/**
	 * 投注网络接口
	 * 
	 * @param aGameType
	 * @param zhuShu
	 * @param beiShu
	 * @return
	 */
	// 新接口 陈晨 20100711
	public void bet(String aGameType, int zhuShu) {

		// 周黎鸣 7.6 代码修改：添加代码
		RWSharedPreferences pre = new RWSharedPreferences(LuckChoose2.this,
				"addInfo");
		String sessionid = pre.getStringValue("sessionid");
		String phonenum = pre.getStringValue("phonenum");
		String userno = pre.getStringValue("userno");
		String strBets = "";
		String lotno = "";
		if (phonenum != null) {

			// 确定彩票种类
			int type = 0;
			for (int i = 0; i < arrayCaipiaoCode.length; i++) {
				if (aGameType.equalsIgnoreCase(arrayCaipiaoCode[i])) {
					lotno = arrayCaipiaoId[i];
					type = i;
					break;
				}
			}
			// 大乐透显示追加投注
			if (lotno == Constants.LOTNO_DLT) {
				betAndGift.setZhui(true);
			} else {
				betAndGift.setZhui(false);
			}
			betAndGift.setBatchcode("");// 清空期号
			betAndGift.setSessionid(sessionid);
			betAndGift.setPhonenum(phonenum);
			betAndGift.setUserno(userno);
			betAndGift.setBet_code(strBets);
			betAndGift.setBettype("bet");// 投注为bet,赠彩为gift
			betAndGift.setLotmulti("1");// lotmulti 倍数 投注的倍数
			betAndGift.setBatchnum("1");// batchnum 追号期数
										// 默认为1（不追号）
			// amount 金额 单位为分（总金额）
			// lotno 彩种编号 投注彩种，如：双色球为F47104
			betAndGift.setSellway("0");// 1代表机选 0代表自选
			betAndGift.setLotno(lotno);

			// 总金额
			betAndGift.setAmount("" + (LuckChoose2View.anniuXuanzhongId[2] + 1)
					* 200);
			betAndGift.setIsSellWays("1");

			// 高频彩与非高频彩详情页不同
			switch (LuckChoose2View.anniuXuanzhongId[0]) {

			// 双色球
			case 0:

				// 大乐透
			case 1:

				// 福彩3D
			case 2:
				AddViewMiss addViewMiss = new AddViewMiss(this);

				// 生成CodeInfo
				com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss tempInfo;
				for (int i = 0; i < receiveRandomNum.length; i++) {
					tempInfo = addViewMiss.initCodeInfo(2, 1);
					tempInfo.setTouZhuCode(GT.betCodeToString(type,
							caipiaoWanfaIndex, zhuShu, betPreString, 1,
							receiveRandomNum[i]));

					// 设置投注颜色
					setCodeInfoColor(tempInfo, null, i);
					addViewMiss.addCodeInfo(tempInfo);
				}

				ApplicationAddview app = (ApplicationAddview) getApplicationContext();
				app.setPojo(betAndGift);
				app.setAddviewmiss(addViewMiss);
				Intent intent = new Intent(LuckChoose2.this,
						com.ruyicai.activity.buy.miss.OrderDetails.class);
				intent.putExtra("from", Constants.SEND_FROM_SIMULATE);
				startActivity(intent);
				break;

			// 时时彩
			case 3:

				// 江西11选5
			case 4:

				// 11运夺金
			case 5:

				// 广东11选5
			case 6:

				// 广东快乐十分
			case 7:
				com.ruyicai.activity.buy.zixuan.AddView addView = new com.ruyicai.activity.buy.zixuan.AddView(
						this);

				com.ruyicai.activity.buy.zixuan.AddView.CodeInfo addViewCodeInfo;
				// 生成CodeInfo
				for (int i = 0; i < receiveRandomNum.length; i++) {
					addViewCodeInfo = addView.initCodeInfo(2, 1);
					addViewCodeInfo.setTouZhuCode(GT.betCodeToString(type,
							caipiaoWanfaIndex, zhuShu, betPreString, 1,
							receiveRandomNum[i]));

					// 设置投注颜色
					setCodeInfoColor(null, addViewCodeInfo, i);
					addView.addCodeInfo(addViewCodeInfo);
				}

				app = (ApplicationAddview) getApplicationContext();

				// 单注高频彩收益追号用到
				if (receiveRandomNum.length == 1) {
					betAndGift.setBet_code(GT.betCodeToString(type,
							caipiaoWanfaIndex, zhuShu, betPreString, 1,
							receiveRandomNum[0])
							+ "_01_200_200");
					app.setHzhushu(1);
				}

				app.setPojo(betAndGift);
				app.setAddview(addView);
				app.setHtextzhuma(addView.getsharezhuma());

				intent = new Intent(LuckChoose2.this, HghtOrderdeail.class);
				intent.putExtra("from", Constants.SEND_FROM_SIMULATE);
				startActivity(intent);
				break;
			}

			// luckNet(betAndGift);

			// 关闭幸运号码弹出框
			luckNumberDialog.dismiss();
		}
	}

	/**
	 * 设置投注的颜色
	 * 
	 * @param codeInfo
	 *            : 对象1
	 * @param codeInfo2
	 *            : 对象2
	 */
	public void setCodeInfoColor(
			com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss codeInfo,
			com.ruyicai.activity.buy.zixuan.AddView.CodeInfo codeInfo2,
			int position) {

		StringBuilder sb = new StringBuilder();

		// 彩票种类
		switch (LuckChoose2View.anniuXuanzhongId[0]) {

		// 双色球
		case 0:
			// 红球
			for (int i = 0; i < 5; i++) {
				sb.append(get2bit(receiveRandomNum[position][i]) + ",");
			}
			sb.append(get2bit(receiveRandomNum[position][5]));

			if (codeInfo2 == null) {
				codeInfo.addAreaCode(sb.toString(), Color.RED);
			} else {
				codeInfo2.addAreaCode(sb.toString(), Color.RED);
			}

			// 蓝球
			if (codeInfo2 == null) {
				codeInfo.addAreaCode(get2bit(receiveRandomNum[position][6]),
						Color.BLUE);
			} else {
				codeInfo2.addAreaCode(get2bit(receiveRandomNum[position][6]),
						Color.BLUE);
			}
			break;

		// 大乐透
		case 1:
			// 红球
			for (int i = 0; i < 4; i++) {
				sb.append(get2bit(receiveRandomNum[position][i]) + ",");
			}
			sb.append(get2bit(receiveRandomNum[position][4]));
			if (codeInfo2 == null) {
				codeInfo.addAreaCode(sb.toString(), Color.RED);
			} else {
				codeInfo2.addAreaCode(sb.toString(), Color.RED);
			}

			// 蓝球
			sb.delete(0, sb.length());
			sb.append(get2bit(receiveRandomNum[position][5]) + ",");
			sb.append(get2bit(receiveRandomNum[position][6]));
			if (codeInfo2 == null) {
				codeInfo.addAreaCode(sb.toString(), Color.BLUE);
			} else {
				codeInfo2.addAreaCode(sb.toString(), Color.BLUE);
			}
			break;

		// 福彩3D
		case 2:
			if (codeInfo2 == null) {
				for (int i = 0; i < 3; i++) {
					codeInfo.addAreaCode("" + receiveRandomNum[position][i],
							Color.RED);
				}
			} else {
				for (int i = 0; i < 3; i++) {
					codeInfo2.addAreaCode("" + receiveRandomNum[position][i],
							Color.RED);
				}
			}

			break;

		// 时时彩
		case 3:
			switch (caipiaoWanfaIndex) {
			// 1星
			case 0:

				// 2星
			case 1:

				// 3星
			case 2:

				// 5星
			case 3:
				if (codeInfo2 == null) {
					for (int i = 0; i < receiveRandomNum[position].length; i++) {
						codeInfo.addAreaCode(
								"" + receiveRandomNum[position][i], Color.RED);
					}
				} else {
					for (int i = 0; i < receiveRandomNum[position].length; i++) {
						codeInfo2.addAreaCode(""
								+ receiveRandomNum[position][i], Color.RED);
					}
				}
				break;

			// 大小
			case 4:
				String[] codeName = { "大", "小", "单", "双" };
				if (codeInfo2 == null) {
					for (int i = 0; i < receiveRandomNum[position].length; i++) {
						codeInfo.addAreaCode(
								codeName[receiveRandomNum[position][i]],
								Color.RED);
					}
				} else {
					for (int i = 0; i < receiveRandomNum[position].length; i++) {
						codeInfo2.addAreaCode(
								codeName[receiveRandomNum[position][i]],
								Color.RED);
					}
				}
				break;
			}

			break;

		// 江西11选5
		case 4:

			// 11运夺金
		case 5:

			// 广东11选5
		case 6:
			switch (caipiaoWanfaIndex) {
			// 直选二
			case 8:
				// 直选三
			case 9:
				if (codeInfo2 == null) {
					for (int i = 0; i < receiveRandomNum[position].length; i++) {
						codeInfo.addAreaCode(
								get2bit(receiveRandomNum[position][i]),
								Color.RED);
					}
				} else {
					for (int i = 0; i < receiveRandomNum[position].length; i++) {
						codeInfo2.addAreaCode(
								get2bit(receiveRandomNum[position][i]),
								Color.RED);
					}
				}
				break;

			// 组选
			default:
				for (int i = 0; i < receiveRandomNum[position].length - 1; i++) {
					sb.append(get2bit(receiveRandomNum[position][i]) + ",");
				}
				sb.append(get2bit(receiveRandomNum[position][receiveRandomNum[0].length - 1]));

				if (codeInfo2 == null) {
					codeInfo.addAreaCode(sb.toString(), Color.RED);
				} else {
					codeInfo2.addAreaCode(sb.toString(), Color.RED);
				}
				break;
			}
			break;

		// 广东快乐十分
		case 7:
			switch (caipiaoWanfaIndex) {

			// 选二连直
			case 6:
				// 选三连直
			case 7:
				if (codeInfo2 == null) {
					for (int i = 0; i < receiveRandomNum[position].length; i++) {
						codeInfo.addAreaCode(
								get2bit(receiveRandomNum[position][i]),
								Color.RED);
					}
				} else {
					for (int i = 0; i < receiveRandomNum[position].length; i++) {
						codeInfo2.addAreaCode(
								get2bit(receiveRandomNum[position][i]),
								Color.RED);
					}
				}
				break;

			// 组选
			default:
				for (int i = 0; i < receiveRandomNum[position].length - 1; i++) {
					sb.append(get2bit(receiveRandomNum[position][i]) + ",");
				}
				sb.append(receiveRandomNum[position][receiveRandomNum[0].length - 1]);

				if (codeInfo2 == null) {
					codeInfo.addAreaCode(sb.toString(), Color.RED);
				} else {
					codeInfo2.addAreaCode(sb.toString(), Color.RED);
				}
				break;

			}
			break;
		}

	}

	/**
	 * 取得两位数据
	 */
	private static String get2bit(int i) {
		if (i < 10) {
			return "0" + i;
		}
		return "" + i;
	}

	/**
	 * 不同彩票进入幸运选号页时编码
	 */
	String[] arrayCaipiaoId = { Constants.LOTNO_SSQ, Constants.LOTNO_DLT,
			Constants.LOTNO_FC3D, Constants.LOTNO_SSC, Constants.LOTNO_11_5,
			Constants.LOTNO_eleven, Constants.LOTNO_GD_11_5, Constants.LOTNO_ten };

	String[] arrayCaipiaoCode = { "ssq", "cjdlt", "fc3d", Constants.LOTNO_SSC,
			Constants.LOTNO_11_5, Constants.LOTNO_eleven,
			Constants.LOTNO_GD_11_5, Constants.LOTNO_ten };
	String[] arrayCaipiaoName = { "双色球", "大乐透", "福彩3D", "时时彩", "江西11选5",
			"11运夺金", "广东11选5", "广东快乐十分"};
	String[] arrayCaipiaoWanfa = { "星座", "生肖", "姓名", "生日" };
	String[] arrayCaipiaoBeishu = { "1注", "2注", "3注", "4注", "5注", "6注", "7注",
			"8注", "9注", "10注" };

	int[] titleID = { R.string.shuangseqiu, R.string.fucai3d, R.string.qilecai,
			R.string.pailiesan, R.string.chaojidaletou };

	/**
	 * 该彩票玩法id, 彩票id为 LuckChoose2View.anniuXuanzhongId[0]
	 */
	private int caipiaoWanfaIndex;

	private void setDangqianCaipiao() {
		// 随机球个数
		ballRandomNumber = getBallRandomNumber(
				LuckChoose2View.anniuXuanzhongId[0], caipiaoWanfaIndex);
		// 是否对球排序
		isNeedSort = getIsNeedSort(LuckChoose2View.anniuXuanzhongId[0],
				caipiaoWanfaIndex);
		// 投注项前缀
		betPreString = getBetPreString(LuckChoose2View.anniuXuanzhongId[0],
				caipiaoWanfaIndex);
	}

	/**
	 * 随机球的个数
	 */
	private int ballRandomNumber;

	/**
	 * 是否对随机球排序
	 */
	private boolean isNeedSort;

	/**
	 * 投注串前缀
	 */
	private String betPreString;

	/**
	 * 返回要随机生成球的个数
	 * 
	 * @param caipiaoIdIndex
	 * @param caipiaoWanfaIndex
	 * @return
	 */
	public static int getBallRandomNumber(int caipiaoIdIndex,
			int caipiaoWanfaIndex) {
		switch (caipiaoIdIndex) {

		// 时时彩
		case 3:
			return new int[] { 1, 2, 3, 5, 2 }[caipiaoWanfaIndex];

			// 江西11运夺金
		case 4:

			// 11运夺金
		case 5:

			// 广东11选5
		case 6:
			return new int[] { 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 2, 3 }[caipiaoWanfaIndex];
		}
		return -1;
	}

	/**
	 * 彩票各个玩法投注串前缀
	 * 
	 * @param caipiaoIdIndex
	 * @param caipiaoWanfaIndex
	 * @return
	 */
	public static String getBetPreString(int caipiaoIdIndex,
			int caipiaoWanfaIndex) {
		switch (caipiaoIdIndex) {
		// 时时彩
		case 3:
			return new String[] { "1D|-,-,-,-,", "2D|-,-,-,", "3D|-,-,", "5D|",
					"DD|" }[caipiaoWanfaIndex];

			// 江西11选5
		case 4:
			return new String[] { "R2|", "R3|", "R4|", "R5|", "R6|", "R7|",
					"R8|", "R1|", "Q2|", "Q3|", "Z2|", "Z3|" }[caipiaoWanfaIndex];

			// 11运夺金
		case 5:
			return new String[] { "111@", "112@", "113@", "114@", "115@",
					"116@", "117@", "101@*", "141@", "161@", "131@", "151@" }[caipiaoWanfaIndex];

			// 广东11选5
		case 6:
			return new String[] { "S|R2|", "S|R3|", "S|R4|", "S|R5|", "S|R6|",
					"S|R7|", "S|R8|", "M|R1|", "S|Q2|", "S|Q3|", "S|Z2|",
					"S|Z3|" }[caipiaoWanfaIndex];

			// 广东快乐十分
		case 7:
			return new String[] { "S|S1|", "S|H1|", "S|R2|", "S|R3|", "S|R4|",
					"S|R5|", "S|Q2|", "S|Q3|", "S|Z2", "S|Z3|" }[caipiaoWanfaIndex];
		}

		// 默认
		return "00";
	}

	/**
	 * 是否对生成彩票排序
	 * 
	 * @param caipiaoIdIndex
	 * @param caipiaoWanfaIndex
	 * @return
	 */
	public static boolean getIsNeedSort(int caipiaoIdIndex,
			int caipiaoWanfaIndex) {
		switch (caipiaoIdIndex) {

		// 时时彩
		case 3:
			return false;

			// 江西11选5
		case 4:

			// 11运夺金
		case 5:

			// 广东11选5
		case 6:

			switch (caipiaoWanfaIndex) {
			case 7:
			case 8:
			case 9:
				return false;

			}
			return true;
		}

		return false;
	}

	/**
	 * 幸运选号：最后的确认支付Dailog
	 */
	// 周黎鸣 7.3 代码修改：在该对话框中添加小球的显示
	protected void showAgreeAndPayDialog(String aGameType) {
		type06 = aGameType;

		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);

		LayoutInflater inflater = LayoutInflater.from(this);
		View textView = inflater.inflate(
				R.layout.choose_luck_lottery_num_agree_and_pay_dialog, null);

		agreeAndpayTitleView = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_title);
		TextView tx01 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text01);
		tx01.setText("在" + hour + ":" + minute + "您的幸运选号是：" + "\n");

		TextView tx02 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text02);
		agreePayBallLayout01 = (LinearLayout) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_linearlayout01);
		agreePayBallLayout02 = (LinearLayout) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_linearlayout02);
		agreePayBallLayout03 = (LinearLayout) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_linearlayout03);
		agreePayBallLayout04 = (LinearLayout) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_linearlayout04);
		agreePayBallLayout05 = (LinearLayout) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_linearlayout05);
		// zlm 7.13 代码修改：变换变量 zlm 8.11 代码修改
		for (int i = 0; i < arrayCaipiaoCode.length; i++) {
			if (type06.equalsIgnoreCase(arrayCaipiaoCode[i])) {
				agreeAndpayTitleView.setText(titleID[i]);
				showAgreeAndPayBall();
				break;
			}
		}

		TextView tx03 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text03);
		tx03.setText("注数：  " + "" + iProgressJizhu + "   注" + "\n");
		TextView tx04 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text04);
		tx04.setText("倍数：  " + "" + iProgressBeishu + "   倍" + "\n");
		TextView tx05 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text05);
		tx05.setText("追号：  " + "" + iProgressQishu + "   期" + "\n");

		TextView tx06 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text06);
		tx06.setText("总金额：  " + "" + iProgressJizhu * iProgressBeishu
				* iProgressQishu * 2 + "   元" + "\n");// *iProgressQishu 取消 zlm
														// 20100713

		TextView tx07 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text07);
		tx07.setText("确认支付吗？" + "\n");

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setView(textView);

		builder.setPositiveButton(R.string.str_return,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// wangyl 20101220 添加身份证验证
					}

				});
		builder.setNegativeButton(R.string.haveatry,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// 返回
						RWSharedPreferences pre = new RWSharedPreferences(
								LuckChoose2.this, "addInfo");
						String sessionIdStr = pre.getStringValue("sessionid");
						showDialog(PROGRESS_WANGLUOLIANJIE);// 显示网络提示框
															// 陈晨2010/7/10
					}

				});
		builder.show();
	}

	/**
	 * 确认支付页面显示小球
	 */
	public void showAgreeAndPayBall() {
		// zlm 8.11 代码修改
		LinearLayout[] agreePayBallLayoutGroup = { agreePayBallLayout01,
				agreePayBallLayout02, agreePayBallLayout03,
				agreePayBallLayout04, agreePayBallLayout05 };
		for (int i = 1; i < 6; i++) {
			if (iProgressJizhu == i) {
				for (int j = 0; j < i; j++) {
					showAgreeAndPayBallLayout(type06,
							agreePayBallLayoutGroup[j], receiveRandomNum[j]);
				}
				break;
			}
		}
	}

	/**
	 * 再确认支付页面中添加小球布局
	 * 
	 * @param aGameType
	 * @param aLinearLayout
	 * @param aRandomNum
	 */
	// 周黎鸣 7.3 代码添加：添加了在支付对话框中小球的显示
	public void showAgreeAndPayBallLayout(String aGameType,
			LinearLayout aLinearLayout, int[] aRandomNum) {

		if (aGameType.equalsIgnoreCase("ssq")) {
			OneBallView showBallView;
			for (int i = 0; i < 6; i++) {
				showBallView = new OneBallView(this, 1);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ aRandomNum[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
				// PublicMethod.myOutput("----------------showBall");
			}
			showBallView = new OneBallView(this, 1);
			showBallView.initBall(BALL_WIDTH, BALL_WIDTH, "" + aRandomNum[6],
					aBlueColorResId);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			aLinearLayout.addView(showBallView, lp);
		} else if (aGameType.equalsIgnoreCase("fc3d")
				|| aGameType.equalsIgnoreCase("pl3")) {
			OneBallView showBallView;
			for (int i = 0; i < 3; i++) {
				showBallView = new OneBallView(this, 1);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ aRandomNum[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
				// PublicMethod.myOutput("----------------showBall");
			}
		} else if (aGameType.equalsIgnoreCase("qlc")) {
			OneBallView showBallView;
			for (int i = 0; i < 7; i++) {
				showBallView = new OneBallView(this, 1);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ aRandomNum[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
			}
		} else if (aGameType.equalsIgnoreCase("cjdlt")) { // zlm 超级大乐透
			OneBallView showBallView;
			for (int i = 0; i < 5; i++) {
				showBallView = new OneBallView(this, 1);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ aRandomNum[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
			}
			for (int i = 5; i < 7; i++) {
				showBallView = new OneBallView(this, 1);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ aRandomNum[i], aBlueColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
			}

			// 11运夺金
		} else if (aGameType.equalsIgnoreCase(Constants.LOTNO_eleven)) {
			OneBallView showBallView;
			for (int i = 0; i < 2; i++) {
				showBallView = new OneBallView(this, 1);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ aRandomNum[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
				// PublicMethod.myOutput("----------------showBall");
			}
		}
	}

	/**
	 * 把随机得到的数组变换成二维数组
	 * 
	 * @param jiZhu
	 * @param aGameType
	 * @return
	 */
	public int[][] changeShuZu(int jiZhu, String aGameType) {
		int[][] zhuShu = null;
		if (aGameType.equalsIgnoreCase("ssq")
				|| aGameType.equalsIgnoreCase("qlc")
				|| aGameType.equalsIgnoreCase("cjdlt")) {
			switch (jiZhu) {
			case 1:
				zhuShu = new int[1][7];
				zhuShu[0] = receiveRandomNum[0];
				break;
			case 2:
				zhuShu = new int[2][7];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				break;
			case 3:
				zhuShu = new int[3][7];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				break;
			case 4:
				zhuShu = new int[4][7];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				zhuShu[3] = receiveRandomNum[3];
				break;
			case 5:
				zhuShu = new int[5][7];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				zhuShu[3] = receiveRandomNum[3];
				zhuShu[4] = receiveRandomNum[4];
				break;
			}
		} else if (aGameType.equalsIgnoreCase("fc3d")
				|| aGameType.equalsIgnoreCase("pl3")) {
			switch (jiZhu) {
			case 1:
				zhuShu = new int[1][3];
				zhuShu[0] = receiveRandomNum[0];
				break;
			case 2:
				zhuShu = new int[2][3];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				break;
			case 3:
				zhuShu = new int[3][3];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				break;
			case 4:
				zhuShu = new int[4][3];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				zhuShu[3] = receiveRandomNum[3];
				break;
			case 5:
				zhuShu = new int[5][3];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				zhuShu[3] = receiveRandomNum[3];
				zhuShu[4] = receiveRandomNum[4];
				break;
			}

			// 江西11选5, 11去夺金, 广东11选5, 时时彩
		} else if (aGameType.equals(Constants.LOTNO_11_5)
				|| aGameType.equals(Constants.LOTNO_eleven)
				|| aGameType.equals(Constants.LOTNO_GD_11_5)
				|| aGameType.equals(Constants.LOTNO_SSC)) {
			zhuShu = new int[1][];
			zhuShu[0] = receiveRandomNum[0];
		}
		return zhuShu;
	}

	/**
	 * 投注网络接口
	 * 
	 * @param aGameType
	 * @param zhuShu
	 * @param beiShu
	 * @return
	 */
	// 新接口 陈晨 20100711

	/**
	 * 体彩投注网络接口
	 * 
	 * @param aGameType
	 * @param qiShu
	 * @param zhuShu
	 * @param beiShu
	 * @return
	 */

	public void errorCode_0000() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "投注成功！", Toast.LENGTH_SHORT).show();
	}

	public void errorCode_000000() {
		// TODO Auto-generated method stub

	}

	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	/**
	 * 重写放回建
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case 4:
			if (isWindow && popupwindow != null) {
				isWindow = false;
				popupwindow.dismiss();
			} else {
				finish();

				/*
				 * 建议系统回收内存
				 */
				panView = null;
				System.gc();
			}
			break;
		}
		return false;
	}

}