/**
 * 
 */
package com.ruyicai.activity.buy.miss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts.People;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.TouzhuBaseActivity;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.activity.buy.ssq.BettingSuccessActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.dialog.BaseDialog;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.GiftMessageInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Administrator
 * 
 */
public class GiftActivity extends TouzhuBaseActivity implements HandlerMsg,
		OnSeekBarChangeListener {
	String phonenum, sessionId, userno;
	protected ProgressDialog progressdialog;
	private BetAndGiftPojo betAndGift;// 投注信息类
	String lotno;
	TextView alertText;
	TextView issueText;
	Button codeInfo;
	TextView textAlert;
	TextView textZhuma;
	TextView textTitle;
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	protected EditText mTextBeishu;
	public int iProgressBeishu = 1, iProgressQishu = 1;
	TextView jine;
	TextView caizhong;
	private boolean toLogin = false;
	public boolean isTouzhu = false;// 是否投注
	private ImageButton language, phone;
	private Button ok, cancel;
	private EditText editLanguage, editPhone;
	private TextView zhuma, zhushu;
	private ListView avdiceList;
	private String zhumaStr;
	public final static String TITLE = "TITLE"; /* 标题 */
	AlertDialog adviceDialog;
	public final static String INFO = "INFO"; /* 信息 */
	List<Map<String, Object>> list;/* 列表适配器的数据源 */

	GiftActivityHandler handler = new GiftActivityHandler(this);// 自定义handler
	Handler handlerTwo = new Handler();
	private Vector<Person> persons = new Vector<Person>();// 所有联系人
	private Vector<Person> checkedPersons = new Vector<Person>();// 选中联系人
	List<String> successPersons = new ArrayList();// 赠送成功的电话号码
	// private Vector<String> checkedState = new Vector<String>();// 选择状态
	private boolean[] checkedState;// 选择状态
	String phoneStr;
	String languageStr;
	String message;
	JSONObject obj;
	boolean isMsg = true;// 是否是获取赠言
	long allAtm;
	int zhu;
	boolean isDialog = false;
	private int restrictMax = 8;
	private int endMax = 8;
	public static String type = "";
	private AddViewMiss addviewmiss;
	private Controller controller = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_zengsong);
		getInfo();
		init();
		// setValue();
		allAtm = addviewmiss.getAllAmt() * iProgressBeishu;
		zhumaStr = addviewmiss.getsharezhuma();
		zhu = addviewmiss.getAllZhu();

	}

	private void getNetIssue() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String issue = Controller.getInstance(GiftActivity.this)
						.toNetIssue(betAndGift.getLotno());
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						issueText.setText("第" + issue + "期");
						betAndGift.setBatchcode(issue);
					}
				});
			}
		}).start();
	}

	/**
	 * 初始化组件
	 */
	public void init() {
		isDialog = false;
		if (betAndGift.isZhui()) {
			initZhuiJia();
		}
		zhushu = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_zhushu);
		jine = (TextView) findViewById(R.id.alert_dialog_touzhu_text_one);
		caizhong = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_caizhong);
		caizhong.setText(PublicMethod.toLotno(betAndGift.getLotno()));
		issueText = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_qihao);
		textZhuma = (TextView) findViewById(R.id.alert_dialog_touzhu_text_zhuma);
		textTitle = (TextView) findViewById(R.id.alert_dialog_touzhu_text_zhuma_title);
		getNetIssue();
		getTouzhuAlert();
		if (type.equals("zc")) {
			textTitle.setText("注码：共有1笔投注");
			textZhuma.setText(betAndGift.getBet_code());
		} else {
			initImageView();
			CodeInfoMiss code = addviewmiss.getCodeList().get(
					addviewmiss.getSize() - 1);
			code.setTextCodeColor(textZhuma, code.getLotoNo(),
					code.getTouZhuType());
			textTitle.setText("注码：" + "共有" + addviewmiss.getSize() + "笔投注");
			codeInfo = (Button) findViewById(R.id.alert_dialog_touzhu_btn_look_code);
			isCodeText(codeInfo);
			codeInfo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addviewmiss.createCodeInfoDialog(GiftActivity.this);
					addviewmiss.showDialog();
				}
			});
		}
		Button cancel = (Button) findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) findViewById(R.id.alert_dialog_touzhu_button_ok);
		editLanguage = (EditText) findViewById(R.id.gift_edit_language);
		editLanguage.setText(languageStr);
		editPhone = (EditText) findViewById(R.id.gift_edit_phone);
		ok = (Button) findViewById(R.id.alert_dialog_touzhu_button_ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addPerson();
				RWSharedPreferences pre = new RWSharedPreferences(
						GiftActivity.this, "addInfo");
				sessionId = pre.getStringValue("sessionid");
				phonenum = pre.getStringValue("phonenum");
				userno = pre.getStringValue("userno");
				if (userno.equals("")) {
					Intent intentSession = new Intent(GiftActivity.this,
							UserLogin.class);
					startActivityForResult(intentSession, 0);
				} else {
					if (checkedPersons.size() != 0) {
						beginTouZhu();
					} else {
						Toast.makeText(GiftActivity.this,
								R.string.ruyichuanqing_atleastone,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		cancel = (Button) findViewById(R.id.alert_dialog_touzhu_button_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (OrderDetails.isAlert && OrderDetails.fromInt != Constants.SEND_FROM_SIMULATE) {
					if (OrderDetails.fromInt == BettingSuccessActivity.NOTICEBALL) {
						alertExit("退出该页面会清空已选择的投注号码，是否将已选择的投注号码保存？");
					} else {
						alertExit(getString(R.string.buy_alert_exit_detail));
					}

				} else {
					finish();
				}
			}
		});
		language = (ImageButton) findViewById(R.id.gift_img_language);
		language.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Constants.avdiceStr == null) {
					giftMsgNet();
				} else {
					adviceDialog();
				}

			}
		});
		phone = (ImageButton) findViewById(R.id.gift_img_phone);
		phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editPhone.setText("");
				checkedPersons.clear();
				LinkDialog();
			}
		});
		// zhuma = (TextView) findViewById(R.id.gift_text_zhuma);
		// zhushu = (TextView) findViewById(R.id.gift_text_zhushu);
		editLanguage.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				editLanguage.setFocusable(true);
				return false;
			}
		});
		editPhone.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				editPhone.setFocusable(true);
				return false;
			}
		});

	}

	/**
	 * 从ShellRWSharesPreferences中获取phonenum 、sessionid 和userno
	 */
	/**
	 * 初始化倍数和期数进度条
	 * 
	 * @param view
	 */
	public void initImageView() {
		mSeekBarBeishu = (SeekBar) findViewById(R.id.buy_zixuan_seek_beishu);
		mTextBeishu = (EditText) findViewById(R.id.buy_zixuan_text_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);

		/** add by pengcx 20130722 start */
		mTextBeishu.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 1 && s.charAt(0) == '0') {
					Integer integer = Integer.valueOf(s.toString());
					mTextBeishu.setText(integer.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}
		});
		/** add by pengcx 20130722 end */
		mTextBeishu.setText("" + iProgressBeishu);

		PublicMethod.setEditOnclick(mTextBeishu, mSeekBarBeishu, new Handler());

		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_beishu, -1,
				mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_beishu, 1, mSeekBarBeishu,
				true);
	}

	/**
	 * fqc edit 添加一个参数 isBeiShu 来判断当前是倍数还是期数 。
	 * 
	 * @param idFind
	 * @param iV
	 * @param isAdd
	 * @param mSeekBar
	 * @param isBeiShu
	 */

	protected void setSeekWhenAddOrSub(int idFind, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressBeishu);
					} else {
						mSeekBar.setProgress(--iProgressBeishu);
					}
					iProgressBeishu = mSeekBar.getProgress();
				} else {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressQishu);
					} else {
						mSeekBar.setProgress(--iProgressQishu);
					}
					iProgressQishu = mSeekBar.getProgress();
				}
			}
		});
	}

	/**
	 * 将输入框里的信息填加入数组
	 */
	public void addPerson() {
		String phone = editPhone.getText().toString();
		if (!phone.equals("")) {
			phone = phone.replace("，", ",");
			String phones[] = phone.split(",");
			if (checkedPersons.size() == 0) {
				for (int i = 0; i < phones.length; i++) {
					Person person = new Person("", phones[i]);
					checkedPersons.add(person);
				}
			} else if (checkedPersons.size() > phones.length) {
				for (int i = 0; i < checkedPersons.size(); i++) {
					for (int j = 0; j < phones.length; j++) {
						if (checkedPersons.get(i).phone.equals(phones[j])) {
							break;
						} else if (j == phones.length - 1) {
							checkedPersons.remove(i);
						}
					}
				}
			} else if (checkedPersons.size() < phones.length) {
				for (int i = 0; i < phones.length; i++) {
					for (int j = 0; j < checkedPersons.size(); j++) {
						if (phones[i].equals(checkedPersons.get(j).phone)) {
							break;
						} else if (j == checkedPersons.size() - 1) {
							Person person = new Person("", phones[i]);
							checkedPersons.add(person);
						}
					}
				}
			} else if (checkedPersons.size() == phones.length) {
				for (int i = 0; i < phones.length; i++) {
					if (!phones[i].equals(checkedPersons.get(i).phone)) {
						Person person = new Person("", phones[i]);
						checkedPersons.remove(i);
						checkedPersons.add(i, person);
					}
				}
			}
		}
	}

	// /**
	// * 赋值
	// *
	// */
	// public void setValue() {
	// SpannableStringBuilder builder = new SpannableStringBuilder();
	// int beishu = Integer.parseInt(betAndGift.getLotmulti());
	// allAtm = Integer.parseInt(betAndGift.getAmount()) / 100;
	// zhu = allAtm / beishu / betAndGift.getAmt();
	// String zhushuStr = "赠送注数：" + zhu + "注\n\n" + "赠送倍数：" + beishu + "倍\n\n"
	// + "赠送金额：" + allAtm + "元";
	// builder.append(zhushuStr);
	// builder.setSpan(new ForegroundColorSpan(Color.RED), zhushuStr.length()
	// - (Integer.toString(allAtm).length() + 1), zhushuStr.length(),
	// Spanned.SPAN_COMPOSING);
	// this.zhushu.setText(builder, BufferType.EDITABLE);
	// // this.zhushu.setText(zhushuStr);
	// this.zhuma.setText(zhumaStr);
	//
	// }

	/**
	 * 从上一个页面获取信息
	 */
	public void getInfo() {
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		betAndGift = app.getPojo();
		addviewmiss = app.getAddviewmiss();
	}

	/**
	 * 赠言选择框
	 */
	public void adviceDialog() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.dialog_advice, null);
		adviceDialog = new AlertDialog.Builder(this).create();
		adviceDialog.show();
		TextView title = (TextView) v
				.findViewById(R.id.alert_dialog_touzhu_text_title);
		title.setText(R.string.ruyichuanqing_normalgreetwords);// 常用语
		avdiceList = (ListView) v.findViewById(R.id.dialog_advice_list);
		avdiceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adviceDialog.cancel();
				languageStr = Constants.avdiceStr[position];
				editLanguage.setText(languageStr);

			}
		});
		Button ok = (Button) v.findViewById(R.id.dialog_advice_img_ok);
		ok.setText(R.string.cancel);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adviceDialog.cancel();
			}
		});
		// 数据源
		list = getListForMoreAdapter();

		// 适配器
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.dialog_advice_list_item, new String[] { TITLE },
				new int[] { R.id.dialog_advice_item_text_title });

		avdiceList.setAdapter(adapter);
		adviceDialog.getWindow().setContentView(v);
	}

	/**
	 * 联系人选择框
	 */
	public void LinkDialog() {
		isDialog = true;
		Cursor c = getContentResolver().query(People.CONTENT_URI, null, null,
				null, null);
		persons.clear();
		while (c.moveToNext()) {
			int index = c.getColumnIndex(People.NUMBER);
			int indexName = c.getColumnIndex(People.NAME);
			Person person = new Person(c.getString(indexName),
					c.getString(index));
			if (person.phone != null) {
				persons.add(person);
			}
		}
		startManagingCursor(c);
		checkedState = new boolean[persons.size()];

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.dialog_advice, null);
		TextView title = (TextView) v
				.findViewById(R.id.alert_dialog_touzhu_text_title);
		title.setText(R.string.ruyichuanqing_phonetext);

		final AlertDialog adviceDialog = new AlertDialog.Builder(this).create();

		adviceDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				phoneStr = "";
				for (int i = 0; i < checkedPersons.size(); i++) {
					phoneStr += checkedPersons.get(i).phone;
					if (i != checkedPersons.size() - 1) {
						phoneStr += ",";
					}
				}

				editPhone.setText(phoneStr);
			}
		});
		avdiceList = (ListView) v.findViewById(R.id.dialog_advice_list);
		avdiceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adviceDialog.cancel();
				editPhone.setText(persons.get(position).phone);

			}
		});

		// 数据源
		list = getListForLinkAdapter(persons);
		LinkAdapter adapter = new LinkAdapter(this, list);
		avdiceList.setItemsCanFocus(false);
		avdiceList.setAdapter(adapter);

		Button ok = (Button) v.findViewById(R.id.dialog_advice_img_ok);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// adviceDialog.cancel();
				dialogOk();
			}
		});

		if (persons.size() == 0) {
			Toast.makeText(this, R.string.ruyichuanqing_thecontactsisnull,
					Toast.LENGTH_SHORT).show();
		} else {
			// adviceDialog.show();
			// adviceDialog.getWindow().setContentView(v);
			setContentView(v);
		}
		final EditText checkEdit = (EditText) v
				.findViewById(R.id.dialog_advice_edit_check);
		checkEdit.setVisibility(EditText.VISIBLE);
		checkEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				Vector<Person> newPersons = new Vector<Person>();// 所有联系人
				String nameStr = checkEdit.getText().toString();
				if (nameStr != null && !nameStr.equals("")) {
					for (int i = 0; i < persons.size(); i++) {
						if (persons.get(i).name != null
								&& persons.get(i).name.indexOf(nameStr) != -1) {
							persons.get(i).checkName = nameStr;
							newPersons.add(persons.get(i));
						}
					}
					// 数据源
					list = getListForLinkAdapter(newPersons);
					LinkAdapter adapter = new LinkAdapter(GiftActivity.this,
							list);
					avdiceList.setItemsCanFocus(false);
					avdiceList.setAdapter(adapter);
				} else {
					for (int i = 0; i < persons.size(); i++) {
						persons.get(i).checkName = nameStr;
					}
					// 数据源
					list = getListForLinkAdapter(persons);
					LinkAdapter adapter = new LinkAdapter(GiftActivity.this,
							list);
					avdiceList.setItemsCanFocus(false);
					avdiceList.setAdapter(adapter);
				}
			}
		});

	}

	/**
	 * 电话薄确定
	 */
	public void dialogOk() {
		setContentView(R.layout.order_zengsong);
		getInfo();
		init();
		// setValue();
		phoneStr = "";
		for (int i = 0; i < checkedPersons.size(); i++) {
			phoneStr += checkedPersons.get(i).phone;
			if (i != checkedPersons.size() - 1) {
				phoneStr += ",";
			}
		}

		editPhone.setText(phoneStr);
	}

	private void isCodeText(Button codeInfo) {
		if (addviewmiss.getSize() > 1) {
			codeInfo.setVisibility(Button.VISIBLE);
		} else {
			codeInfo.setVisibility(Button.GONE);
		}
	}

	/**
	 * 显示追加投注
	 * 
	 * @param view
	 */
	private void initZhuiJia() {
		LinearLayout toggleLinear = (LinearLayout) findViewById(R.id.buy_zixuan_linear_toggle);
		toggleLinear.setVisibility(LinearLayout.VISIBLE);
		ToggleButton zhuijiatouzhu = (ToggleButton) findViewById(R.id.dlt_zhuijia);
		zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					betAndGift.setAmt(3);
					betAndGift.setIssuper("0");
				} else {
					betAndGift.setIssuper("");
					betAndGift.setAmt(2);
				}
				addviewmiss.setCodeAmt(betAndGift.getAmt());
				getTouzhuAlert();
			}
		});
	}

	/**
	 * 投注提示框中的信息
	 */
	public void getTouzhuAlert() {
		if (type.equals("zc")) {
			zhushu.setText(betAndGift.getZhushu() + "注     ");
			jine.setText("金额："
					+ iProgressQishu
					* (Integer.valueOf(CheckUtil.isNull(betAndGift.getAmount())) / 100)
					* iProgressBeishu + "元");
		} else {
			zhushu.setText(addviewmiss.getAllZhu() + "注     ");
			jine.setText("金额：" + iProgressQishu * addviewmiss.getAllAmt()
					* iProgressBeishu + "元");
		}
	}

	/**
	 * 列表适配器的数据源
	 * 
	 * @return
	 */
	protected List<Map<String, Object>> getListForMoreAdapter() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < Constants.avdiceStr.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TITLE, Constants.avdiceStr[i]);
			list.add(map);

		}

		return list;
	}

	/**
	 * 联系人列表适配器的数据源
	 * 
	 * @return
	 */
	protected List<Map<String, Object>> getListForLinkAdapter(
			Vector<Person> persons) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < persons.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(INFO, persons.get(i));
			list.add(map);

		}

		return list;
	}

	/**
	 * 联系人的适配器
	 */
	public class LinkAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;

		public LinkAdapter(Context context, List<Map<String, Object>> list) {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			final int index = position;
			ViewHolder holder = null;
			final Person person = (Person) mList.get(position).get(INFO);
			// if (convertView == null) {
			convertView = mInflater.inflate(R.layout.dialog_link_list_item,
					null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.dialog_link_item_text_title);
			holder.phone = (TextView) convertView
					.findViewById(R.id.dialog_link_item_text_phone);
			holder.check = (CheckBox) convertView
					.findViewById(R.id.dialog_link_item_check);
			convertView.setTag(holder);

			holder.check.setChecked(person.isChecked);
			if (person.name != null) {
				setResultColor(person.checkName, person.name, holder.title);
			}
			if (person.phone != null) {
				holder.phone.setText(person.phone);
			}
			holder.check
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							person.isChecked = isChecked;
							if (isChecked) {
								checkedPersons.add(person);
							} else {
								checkedPersons.remove(person);
							}
						}
					});
			return convertView;
		}

		class ViewHolder {
			TextView title;
			TextView phone;
			CheckBox check;
		}
	}

	/**
	 * 设置颜色
	 */
	public void setResultColor(String checkName, String name, TextView result) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		int start = name.indexOf(checkName);
		int max = name.length();
		endMax = restrictMax;
		for (int i = 0; i < restrictMax; i++) { // 循环遍历字符串
			if (Pattern.compile("(?i)[a-z]").matcher(name).find()
					|| Pattern.compile("(?i)[A-Z]").matcher(name).find()) { // 用char包装类中的判断数字的方法判断每一个字符
				endMax++;
			}
		}
		if (max > endMax) {
			name = name.substring(0, endMax);
			for (int i = 0; i < max - endMax; i++) {
				name += ".";
			}
		}
		builder.append(name);
		if (!checkName.equals("")) {
			builder.setSpan(new ForegroundColorSpan(Color.RED), start, start
					+ checkName.length(), Spanned.SPAN_COMPOSING);
		}
		result.setText(builder, BufferType.EDITABLE);
	}

	/**
	 * 投注提示框
	 * 
	 * @param title
	 * @param string
	 */
	public void touDialog(String title, String string) {
		TouDialog touDialog = new TouDialog(this, title, string);
		touDialog.showDialog();
		touDialog.createMyDialog();

	}

	class TouDialog extends BaseDialog {

		public TouDialog(Activity activity, String title, String message) {
			super(activity, title, message);
		}

		@Override
		public void onOkButton() {
			giftNet();
		}

		@Override
		public void onCancelButton() {
		}

	}

	/**
	 * 增彩结果提示框
	 * 
	 * @param title
	 * @param string
	 */
	public void resultDialog(String title, String string) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.gift_result_dialog_view, null);
		TextView content = (TextView) v
				.findViewById(R.id.gift_result_view_text);
		content.setText(string);
		Dialog dialog = new AlertDialog.Builder(this).setView(v)
				.setCancelable(false).setTitle(title)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// buyImplement.touzhuNet();
						checkedPersons.clear();
						editPhone.setText("");
						// if(successPersons.size()>0){
						// sendSMS();
						// }
						GiftActivity.this.finish();
					}
				}).create();
		dialog.show();
	}

	public String resultAlertStr() {
		String success = "", fail = "", result = "";
		try {
			JSONObject json = obj.getJSONObject("gift_result");
			success = json.getString("success");
			fail = json.getString("fail");
			result += "您的朋友\n";
			if (!success.equals("")) {
				result += getName(success, true);
				result += "赠送成功\n\n";
			}
			if (!fail.equals("")) {
				result += getName(fail, false);
				result += "赠送失败\n";
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getName(String str, boolean isSuccess) {
		String result = "";
		String sPersons[] = str.split(",");
		for (int i = 0; i < sPersons.length; i++) {
			for (int j = 0; j < checkedPersons.size(); j++) {
				if (sPersons[i].equals(checkedPersons.get(j).phone)) {
					String name = checkedPersons.get(j).name;
					if (name == null) {
						name = "";
					}
					result += name;
					for (int n = 0; n < 7 - name.length(); n++) {
						result += "---";
					}
					result += checkedPersons.get(j).phone + "\n";
					if (isSuccess) {
						successPersons.add(checkedPersons.get(j).phone);
					}
					break;
				} else if (j == checkedPersons.size() - 1) {
					for (int n = 0; n < 7; n++) {
						result += "---";
					}
					result += sPersons[i] + "\n";
					if (isSuccess) {
						successPersons.add(sPersons[i]);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获取提示语
	 * 
	 */
	public void giftMsgNet() {
		isMsg = true;
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = GiftMessageInterface.giftMsg();
				try {
					obj = new JSONObject(str);
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					handler.handleMsg(error, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}

		});
		t.start();
	}

	public String[] jsonToStr(JSONObject json) {
		String avdiceStr[] = null;
		try {
			JSONArray giftMsg = json.getJSONArray("result");
			avdiceStr = new String[giftMsg.length()];
			for (int i = 0; i < giftMsg.length(); i++) {
				avdiceStr[i] = giftMsg.getJSONObject(i).getString("content");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return avdiceStr;
	}

	/**
	 * 赠送彩票联网
	 * 
	 */
	public void giftNet() {
		isMsg = false;
		betAndGift.setBlessing(editLanguage.getText().toString());
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setLotmulti("" + iProgressBeishu);// lotmulti 倍数 投注的倍数
		betAndGift.setBatchnum("" + iProgressQishu);// batchnum 追号期数 默认为1（不追号）
		betAndGift.setAmount("" + addviewmiss.getAllAmt() * iProgressBeishu
				* 100);
		betAndGift.setIsSellWays("1");
		/** add by yejc 20130510 start */
		if (isFromTrackQuery) {
			betAndGift.setBet_code(betAndGift.getBet_code());
		} else {
			betAndGift.setBet_code(addviewmiss.getTouzhuCode(iProgressBeishu,
					betAndGift.getAmt() * 100));
		}
		/** add by yejc 20130510 end */
		/** close by yejc 20130510 start */
		// betAndGift.setBet_code(addviewmiss.getTouzhuCode(iProgressBeishu,
		// betAndGift.getAmt() * 100));
		/** close by yejc 20130510 end */

		betAndGift.setBettype("gift");
		betAndGift.setDescription("");
		betAndGift.setTo_mobile_code(editPhone.getText().toString()
				.replace("，", ","));
		betAndGift.setAdvice(editLanguage.getText().toString());
		
        controller = Controller.getInstance(GiftActivity.this);
		if (controller != null) {
			controller.doBettingAction(handler, betAndGift);
		}
	}

	/**
	 * 投注方法
	 */
	private void beginTouZhu() {
		RWSharedPreferences pre = new RWSharedPreferences(GiftActivity.this,
				"addInfo");
		sessionId = pre.getStringValue("sessionid");
		phonenum = pre.getStringValue("phonenum");
		userno = pre.getStringValue("userno");
		String alertStr = "您将要赠送" + checkedPersons.size() + "位朋友，赠送金额为"
				+ allAtm * checkedPersons.size() + "元，" + "是否赠送？";
		String phone = editPhone.getText().toString();
		if (isNum(phone)) {
			touDialog("确认要赠送吗？", alertStr);
		}

	}

	public boolean isNum(String str) {
		for (int i = 0; i < checkedPersons.size(); i++) {
			String phone = checkedPersons.get(i).phone;
			if (!PublicMethod.isphonenum(phone)) {
				Toast.makeText(this, "您输入的手机号" + phone + "是错误的，请重新输入！",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}

	/**
	 * 清空倍数和期数的进度条
	 */
	public void clearProgress() {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		mSeekBarBeishu.setProgress(iProgressBeishu);
		// mSeekBarQishu.setProgress(iProgressQishu);
		if (isclearaddview) {
			if (addviewmiss != null) {
				addviewmiss.clearInfo();
				addviewmiss.updateTextNum();
			}
		}
	}

	public void errorCode_0000() {
		// TODO Auto-generated method stub
		if (isMsg) {
			Constants.avdiceStr = jsonToStr(obj);
			adviceDialog();
		} else {
			Intent intent = new Intent(this, BettingSuccessActivity.class);
			if (isSsq()) {
				intent.putExtra("isssq", true);
				intent.putExtra("isgift", true); //add by yejc 20131030
			}

			if (OrderDetails.fromInt != 0) {
				intent.putExtra("from", OrderDetails.fromInt);
			}

			intent.putExtra("page", BettingSuccessActivity.PRESENT);

			intent.putExtra("lotno", betAndGift.getLotno());
			intent.putExtra("amount", betAndGift.getAmount());
			startActivity(intent);
		}

	}

	private boolean isSsq() {
		if (betAndGift.getLotno().equals(Constants.LOTNO_SSQ)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isclearaddview = true;

	public void errorCode_000000() {
	}

	public Context getContext() {
		return this;
	}

	class Person {
		public String name = "";
		public String phone = "";
		public String checkName = "";
		public boolean isChecked = false;

		public Person(String name, String phone) {
			this.name = name;
			this.phone = phone;
		}

		public Person() {

		}
	}

	/**
	 * 重写放回建
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case 4:
			/** add by yejc 20130510 start */
			if (isFromTrackQuery) {
				break;
			}
			/** add by yejc 20130510 end */
			if (isDialog) {
				dialogOk();
			} else {
				if (OrderDetails.isAlert && OrderDetails.fromInt != Constants.SEND_FROM_SIMULATE) {
					if (OrderDetails.fromInt == BettingSuccessActivity.NOTICEBALL) {
						alertExit("退出该页面会清空已选择的投注号码，是否将已选择的投注号码保存？");
					} else {
						alertExit(getString(R.string.buy_alert_exit_detail));
					}
				} else {
					finish();
				}
			}
			break;
		}
		return false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24

		betAndGift.setIssuper("");
		betAndGift.setAmt(2);
		addviewmiss.setCodeAmt(betAndGift.getAmt());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();
		switch (seekBar.getId()) {
		case R.id.buy_zixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			allAtm = addviewmiss.getAllAmt() * iProgressBeishu;
			break;
		default:
			break;
		}
		getTouzhuAlert();

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touzhuIssue(String issue) {
		// TODO Auto-generated method stub
		betAndGift.setBatchcode(issue);
        controller = Controller.getInstance(GiftActivity.this);
		if (controller != null) {
			controller.doBettingAction(handler, betAndGift);
		}
	}

	/**
	 * 退出提示
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertExit(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(string)
				.setNeutralButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						isclearaddview = false;
						clearProgress();
						finish();
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						isclearaddview = true;
						clearProgress();
						finish();
					}
				});
		dialog.show();

	}
	class GiftActivityHandler extends MyHandler {

		public GiftActivityHandler(HandlerMsg msg) {
			super(msg);
			// TODO Auto-generated constructor stub
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (controller != null) {
				isNoIssue(handler, controller.getRtnJSONObject());
			}

		}
	}
}
