package com.ruyicai.activity.buy.zixuan;

import java.util.ArrayList;
import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.more.LuckChoose2;
import com.ruyicai.activity.notice.LotnoDetail.LotnoDetailView;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

/**
 * 号码篮类
 * 
 * @author Administrator
 * 
 */
public class AddView {
	// 号码篮中的注码集合
	private List<CodeInfo> codeList = new ArrayList<CodeInfo>();
	private Context context;
	private String title;
	private AlertDialog dialog;
	private View view;
	private ListView listView;
	private TextView textNum;
	private List<TextView> textNums;
	private AddListAdapter listAdapter;
	private TextView infoText;
	ZixuanActivity zXActivity;
	DanshiJiXuan jXActivity;
	ZixuanAndJiXuan zJActivity;
	private boolean isZiXuan = true;
	private String isJXcode = "";
	private final int MAX_ZHU = 10000;// 最大金额不能超过2万

	public void setZiXuan(boolean isZiXuan) {
		this.isZiXuan = isZiXuan;
	}

	public ZixuanActivity getzXActivity() {
		return zXActivity;
	}

	public void setzXActivity(ZixuanActivity zXActivity) {
		this.zXActivity = zXActivity;
	}

	public DanshiJiXuan getjXActivity() {
		return jXActivity;
	}

	public void setjXActivity(DanshiJiXuan jXActivity) {
		this.jXActivity = jXActivity;
	}

	public void setzJActivity(ZixuanAndJiXuan zJActivity) {
		this.zJActivity = zJActivity;
	}

	/**
	 * 参数为幸运选号 的构造函数
	 * 
	 * @param luckChoose2
	 */
	public AddView(LuckChoose2 luckChoose2) {
		this.context = luckChoose2.getContext();
	}

	public AddView(Context context) {
		this.context = context;
		textNums = new ArrayList<TextView>();
	}

	public AddView(TextView textNum, ZixuanActivity zixuan) {
		this.context = zixuan.getContext();
		this.zXActivity = zixuan;
		this.textNum = textNum;
		updateTextNum();

	}

	public AddView(TextView textNum, DanshiJiXuan jXActivity, boolean isZiXuan) {
		this.context = jXActivity.getContext();
		this.jXActivity = jXActivity;
		this.textNum = textNum;
		this.isZiXuan = isZiXuan;
		updateTextNum();

	}

	public AddView(TextView textNum, ZixuanAndJiXuan zJActivity,
			boolean isZiXuan) {
		this.isZiXuan = isZiXuan;
		this.zJActivity = zJActivity;
		this.context = zJActivity.getContext();
		this.textNum = textNum;
		updateTextNum();

	}

	/**
	 * 刷新号码篮个数
	 */
	public void updateTextNum() {
		if (textNums.size() != 0) {
			for (int i = 0; i < textNums.size(); i++) {
				((TextView) textNums.get(i)).setText("" + codeList.size());
			}
		}
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public TextView getTextNum() {
		return textNum;
	}

	public void setTextNum(TextView textNum) {
		textNums.add(textNum);
	}

	/**
	 * 创建号码蓝对话框
	 * 
	 * @param titleStr
	 */
	public void createDialog(String titleStr) {
		LayoutInflater factory = LayoutInflater.from(context);
		dialog = new AlertDialog.Builder(context).create();
		view = factory.inflate(R.layout.buy_add_dialog, null);

		// 标题文本框
		TextView title = (TextView) view.findViewById(R.id.zfb_text_title);
		title.setText(titleStr);

		// 注数和金额提示
		infoText = (TextView) view.findViewById(R.id.buy_add_dialog_text_info);

		// 初始化选号列表
		initListView(true);

		// 继续选号按钮
		Button xuanhao = (Button) view.findViewById(R.id.ok);
		xuanhao.setText(context.getString(R.string.buy_add_dialog_xuanhao));
		xuanhao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				updateTextNum();
			}
		});

		// 立即投注按钮
		Button touzhu = (Button) view.findViewById(R.id.canel);
		touzhu.setText(context.getString(R.string.buy_add_dialog_touzhu));
		touzhu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				updateTextNum();
				if (codeList.size() > 0) {
					if (zJActivity != null) {
						if (isZiXuan) {
							isSscZiXuan();
						} else {
							isSscJiXuan();
						}
					} else {
						if (isZiXuan) {
							isZiXuan();
						} else {
							isJiXuan();
						}
					}

				} else {
					Toast.makeText(
							context,
							context.getString(R.string.buy_add_dialog_toast_msg),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		updateInfoText();
	}

	private void isZiXuan() {
		RWSharedPreferences pre = new RWSharedPreferences(zXActivity, "addInfo");
		zXActivity.phonenum = pre.getStringValue("phonenum");
		zXActivity.userno = pre.getStringValue("userno");
		if (!zXActivity.userno.equals("")) {
			zXActivity.alert();
		} else {
			Intent intentSession = new Intent(zXActivity, UserLogin.class);
			zXActivity.startActivityForResult(intentSession, 0);
		}
	}

	private void isJiXuan() {
		RWSharedPreferences pre = new RWSharedPreferences(jXActivity, "addInfo");
		jXActivity.phonenum = pre.getStringValue("phonenum");
		jXActivity.userno = pre.getStringValue("userno");
		if (!jXActivity.userno.equals("")) {
			jXActivity.alert_jixuan();
		} else {
			Intent intentSession = new Intent(jXActivity, UserLogin.class);
			jXActivity.startActivityForResult(intentSession, 0);
		}
	}

	private void isSscZiXuan() {
		RWSharedPreferences pre = new RWSharedPreferences(zJActivity, "addInfo");
		zJActivity.phonenum = pre.getStringValue("phonenum");
		zJActivity.userno = pre.getStringValue("userno");
		if (!zJActivity.userno.equals("")) {
			zJActivity.alertZX();
		} else {
			Intent intentSession = new Intent(zXActivity, UserLogin.class);
			zXActivity.startActivityForResult(intentSession, 0);
		}
	}

	private void isSscJiXuan() {
		RWSharedPreferences pre = new RWSharedPreferences(zJActivity, "addInfo");
		zJActivity.phonenum = pre.getStringValue("phonenum");
		zJActivity.userno = pre.getStringValue("userno");
		if (!zJActivity.userno.equals("")) {
			zJActivity.alertJX();
		} else {
			Intent intentSession = new Intent(zJActivity, UserLogin.class);
			zJActivity.startActivityForResult(intentSession, 0);
		}
	}

	/**
	 * 机选投注注码串
	 * 
	 * @return
	 */
	public String getIsJXcode() {
		return isJXcode;
	}

	public void setIsJXcode(String isJXcode) {
		this.isJXcode += isJXcode;
	}

	public void createCodeInfoDialog() {
		LayoutInflater factory = LayoutInflater.from(context);
		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		view = factory.inflate(R.layout.buy_add_dialog, null);
		TextView title = (TextView) view.findViewById(R.id.zfb_text_title);
		infoText = (TextView) view.findViewById(R.id.buy_add_dialog_text_info);
		title.setText(context.getString(R.string.buy_add_dialog_title));
		initListView(false);
		updateInfoText();
		Button xuanhao = (Button) view.findViewById(R.id.ok);
		Button touzhu = (Button) view.findViewById(R.id.canel);
		touzhu.setVisibility(Button.GONE);
		xuanhao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

	public void createCodeInfoDialog(Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		view = factory.inflate(R.layout.buy_add_dialog, null);
		TextView title = (TextView) view.findViewById(R.id.zfb_text_title);
		infoText = (TextView) view.findViewById(R.id.buy_add_dialog_text_info);
		title.setText(context.getString(R.string.buy_add_dialog_title));
		initListView(false);
		updateInfoText();
		Button xuanhao = (Button) view.findViewById(R.id.ok);
		Button touzhu = (Button) view.findViewById(R.id.canel);
		touzhu.setVisibility(Button.GONE);
		xuanhao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

	/**
	 * 每笔投注单注金额重新赋值
	 */
	public void setCodeAmt(int amt) {
		for (CodeInfo codeInfo : codeList) {
			codeInfo.setAmt(amt * codeInfo.zhuShu);
		}
	}

	/**
	 * 拼装投注时的注码串
	 */
	public String getTouzhuCode(int beishu, int amt) {
		String code = "";
		for (int i = 0; i < codeList.size(); i++) {
			CodeInfo codeInfo = codeList.get(i);
			code += codeInfo.getTouZhuCode(beishu, amt);
			if (i != codeList.size() - 1) {
				code += "!";
			}
		}
		return code;
	}

	// 显示的注码(分享和赠送)
	public String getsharezhuma() {
		StringBuffer zhuma = new StringBuffer();
		for (int i = 0; i < codeList.size(); i++) {
			for (int j = 0; j < codeList.get(i).getCodes().size(); j++) {
				CodeInfo codeInfo = codeList.get(i);
				String code = codeInfo.getCodes().get(j);
				String lotoNO = codeInfo.getLotoNo();
				String touzhuType = codeInfo.getTouZhuType();
				Log.v("code", code);
				zhuma.append(code);
				if (j != codeList.get(i).getCodes().size() - 1) {
					if (lotoNO == null || touzhuType == null) {
						zhuma.append(",");
					} else if (lotoNO.equals(Constants.LOTNO_SSC)) {
						zhuma.append(",");
					} else if ((lotoNO.equals(Constants.LOTNO_11_5) && touzhuType
							.equals("dantuo"))
							|| (lotoNO.equals(Constants.LOTNO_ten) && touzhuType
									.equals("dantuo"))
							|| (lotoNO.equals(Constants.LOTNO_eleven) && touzhuType
									.equals("dantuo"))
							|| (lotoNO.equals(Constants.LOTNO_GD_11_5) && touzhuType
									.equals("dantuo"))) {
						zhuma.append("#");
					} else {
						zhuma.append("|");
					}
				}

			}
			if (i != codeList.size() - 1) {
				zhuma.append(";");
			}
		}
		return zhuma.toString();
	}

	private void updateInfoText() {
		infoText.setText("共选择" + getAllZhu() + "注，" + "总金额" + getAllAmt() + "元");
	}

	public int getAllZhu() {
		int allZhu = 0;
		for (CodeInfo codeInfo : codeList) {
			allZhu += codeInfo.getZhuShu();
		}
		return allZhu;
	}

	public long getAllAmt() {
		long allAmt = 0;
		for (CodeInfo codeInfo : codeList) {
			allAmt += codeInfo.getAmt();
		}
		return allAmt;
	}

	private void initListView(boolean isDelet) {
		listView = (ListView) view.findViewById(R.id.buy_add_dialog_list);
		listAdapter = new AddListAdapter(context, codeList, isDelet);
		listView.setAdapter(listAdapter);
	}

	public void updateListView() {
		listAdapter.notifyDataSetChanged();
	}

	public int getSize() {
		return codeList.size();
	}

	public CodeInfo initCodeInfo(int amt, int zhuShu) {
		return new CodeInfo(amt, zhuShu);
	}

	public void addCodeInfo(CodeInfo codeInfo) {
		codeList.add(codeInfo);
	}

	public List<CodeInfo> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<CodeInfo> codeList) {
		this.codeList = codeList;
	}

	public void clearInfo() {
		codeList.clear();
	}

	public void showDialog() {
		dialog.show();
		dialog.getWindow().setContentView(view);
	}

	/**
	 * 号码栏号码显示列表适配器
	 * 
	 */
	class AddListAdapter extends BaseAdapter {
		private boolean isDelet = true;
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<CodeInfo> codeInfos = new ArrayList<CodeInfo>();

		public AddListAdapter(Context context, List<CodeInfo> codeInfos,
				boolean isDelet) {
			mInflater = LayoutInflater.from(context);
			this.codeInfos = codeInfos;
			this.isDelet = isDelet;
		}

		@Override
		public int getCount() {
			return codeInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return codeInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			CodeInfo codeInfo = codeInfos.get(position);
			convertView = mInflater.inflate(R.layout.buy_add_dialog_list_item,
					null);
			TextView textNum = (TextView) convertView
					.findViewById(R.id.buy_add_list_item_text_num);
			// 投注号码文本框对象
			TextView textCode = (TextView) convertView
					.findViewById(R.id.buy_add_list_item_text_code);
			TextView textZhuShu = (TextView) convertView
					.findViewById(R.id.buy_add_list_item_text_zhushu);
			TextView textAmt = (TextView) convertView
					.findViewById(R.id.buy_add_list_item_text_amt);
			Button btnDelet = (Button) convertView
					.findViewById(R.id.buy_add_dialog_delet);
			btnDelet.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					codeInfos.remove(position);
					updateListView();
					updateInfoText();
				}
			});
			textNum.setText("" + (position + 1));
			codeInfo.setTextCodeColor(textCode, codeInfo.getLotoNo(),
					codeInfo.getTouZhuType());
			textZhuShu.setText(codeInfo.zhuShu + "注");
			textAmt.setText(codeInfo.amt + "元");
			if (isDelet) {
				btnDelet.setVisibility(Button.VISIBLE);
			} else {
				btnDelet.setVisibility(Button.GONE);
			}
			return convertView;
		}

	}

	public class CodeInfo {
		private String touZhuCode;// 投注时的注码，传给后台的
		// 从选号区域获取的号码
		List<String> codes = new ArrayList<String>();
		// 从选号区域获取的号码颜色
		List<Integer> colors = new ArrayList<Integer>();
		int amt;
		int zhuShu;
		private String lotoNo;
		private String touZhuType;

		public String getLotoNo() {
			return lotoNo;
		}

		public void setLotoNo(String lotoNo) {
			this.lotoNo = lotoNo;
		}

		public String getTouZhuType() {
			return touZhuType;
		}

		public void setTouZhuType(String touZhuType) {
			this.touZhuType = touZhuType;
		}

		public CodeInfo(int amt, int zhuShu) {
			this.amt = amt;
			this.zhuShu = zhuShu;
		}

		/**
		 * 
		 * @param beishu
		 * @param amt
		 *            单注金额
		 * @return
		 */
		public String getTouZhuCode(int beishu, int amt) {
			return touZhuCode + "_" + PublicMethod.isTen(beishu) + "_" + amt
					+ "_" + zhuShu * amt;
		}

		public String getTouZhuCode() {
			return touZhuCode;
		}

		public void setTouZhuCode(String touZhuCode) {
			this.touZhuCode = touZhuCode;
		}

		public int getAmt() {
			return amt;
		}

		public void setAmt(int amt) {
			this.amt = amt;
		}

		public int getZhuShu() {
			return zhuShu;
		}

		public void setZhuShu(int zhuShu) {
			this.zhuShu = zhuShu;
		}

		public void addAreaCode(String code, int color) {
			codes.add(code);
			colors.add(color);
		}

		public List<String> getCodes() {
			return codes;
		}

		public List<Integer> getColors() {
			return colors;
		}

		/**
		 * 设置号码篮中投注号码的显示
		 * 
		 * @param textCode
		 *            投注号码文本框
		 * @param lotoNo
		 *            彩种类型
		 * @param touzhuType
		 *            投注类型
		 */
		public void setTextCodeColor(TextView textCode, String lotoNo,
				String touzhuType) {
			SpannableStringBuilder builder = new SpannableStringBuilder();
			int upLength = 0;
			for (int i = 0; i < getCodes().size(); i++) {
				String code = getCodes().get(i);
				builder.append(code);
				builder.setSpan(new ForegroundColorSpan(getColors().get(i)),
						upLength, code.length() + upLength,
						Spanned.SPAN_COMPOSING);
				// 添加分隔符
				addSeparator(lotoNo, touzhuType, builder, i);
				upLength = builder.length();
			}
			textCode.setText(builder, BufferType.EDITABLE);
		}

		/**
		 * 为在号码篮中的投注号码添加分隔符
		 * 
		 * @param lotoNo
		 *            彩种编号
		 * @param touzhuType
		 *            投注类型
		 * @param builder
		 * @param i
		 *            ListView中的位置
		 */
		private void addSeparator(String lotoNo, String touzhuType,
				SpannableStringBuilder builder, int i) {

			if (i != getCodes().size() - 1) {
				if (lotoNo == null || touzhuType == null) {
					builder.append("|");
				} else if ((lotoNo.equals(Constants.LOTNO_PL3) && ((touzhuType
						.equals("zu3_danshi") || touzhuType.equals("zu3_fushi") || touzhuType
							.equals("zu3_hezhi")) || touzhuType.equals("zu6")))) {
					builder.append(",");
				} else if (lotoNo.equals(Constants.LOTNO_PL5)
						|| lotoNo.equals(Constants.LOTNO_QXC)
						|| (lotoNo.equals(Constants.LOTNO_SSC))) {
					if (!touzhuType.equals("bigsmall")) {
						builder.append(",");
					} else {

					}
				} else if (lotoNo.equals(Constants.LOTNO_QLC)
						|| lotoNo.equals(Constants.LOTNO_22_5)
						|| (lotoNo.equals(Constants.LOTNO_11_5) && touzhuType
								.equals("dantuo"))
						|| (lotoNo.equals(Constants.LOTNO_GD_11_5) && touzhuType
								.equals("dantuo"))
						|| (lotoNo.equals(Constants.LOTNO_ten) && touzhuType
								.equals("dantuo"))
						|| (lotoNo.equals(Constants.LOTNO_eleven) && touzhuType
								.equals("dantuo"))
						|| (lotoNo.equals(Constants.LOTNO_NMK3) && touzhuType
								.equals("twosame_dan"))) {
					builder.append("#");
				} else {
					builder.append("|");
				}
			}
		}
	}
}
