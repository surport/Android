package com.ruyicai.activity.buy.miss;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.activity.buy.ssq.SimulateSelectNumberActivity;
import com.ruyicai.activity.buy.ssq.SimulateSelectNumberView;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.more.LuckChoose2;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 号码篮类
 * 
 * @author Administrator
 * 
 */
public class AddViewMiss {
	/**
	 * 
	 */
	private List<CodeInfoMiss> codeList = new ArrayList<CodeInfoMiss>();
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

	/**
	 * 参数为幸运选号 的构造函数
	 * 
	 * @param luckChoose2
	 */
	public AddViewMiss(LuckChoose2 luckChoose2) {
		this.context = luckChoose2.getContext();
	}

	public AddViewMiss(Context context) {
		this.context = context;
		textNums = new ArrayList<TextView>();
	}

	public TextView getTextNum() {
		return textNum;
	}

	public void setTextNum(TextView textNum) {
		textNums.add(textNum);
	}

	public ZixuanActivity getzXActivity() {
		return zXActivity;
	}

	public void setzXActivity(ZixuanActivity zXActivity) {
		this.zXActivity = zXActivity;
	}

	public void setzJActivity(ZixuanAndJiXuan zJActivity) {
		this.zJActivity = zJActivity;
	}

	public AddViewMiss(TextView textNum, ZixuanActivity zixuan) {
		this.context = zixuan.getContext();
		this.zXActivity = zixuan;
		this.textNum = textNum;
		updateTextNum();

	}

	public AddViewMiss(TextView textNum, DanshiJiXuan jXActivity,
			boolean isZiXuan) {
		this.context = jXActivity.getContext();
		this.jXActivity = jXActivity;
		this.textNum = textNum;
		this.isZiXuan = isZiXuan;
		updateTextNum();

	}

	public AddViewMiss(TextView textNum, ZixuanAndJiXuan zJActivity,
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

	public void createDialog(String titleStr) {
		LayoutInflater factory = LayoutInflater.from(context);
		dialog = new AlertDialog.Builder(context).create();
		view = factory.inflate(R.layout.buy_add_dialog, null);
		TextView title = (TextView) view.findViewById(R.id.zfb_text_title);
		infoText = (TextView) view.findViewById(R.id.buy_add_dialog_text_info);
		title.setText(titleStr);
		initListView(true);
		Button xuanhao = (Button) view.findViewById(R.id.ok);
		Button touzhu = (Button) view.findViewById(R.id.canel);
		xuanhao.setText(context.getString(R.string.buy_add_dialog_xuanhao));
		touzhu.setText(context.getString(R.string.buy_add_dialog_touzhu));
		xuanhao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		touzhu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
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
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				updateTextNum();
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
				dialog.dismiss();
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
				dialog.dismiss();
			}
		});
	}

	/**
	 * 每笔投注单注金额重新赋值
	 */
	public void setCodeAmt(int amt) {
		for (CodeInfoMiss codeInfo : codeList) {
			codeInfo.setAmt(amt * codeInfo.zhuShu);
		}
	}

	/**
	 * 拼装投注时的注码串
	 */
	public String getTouzhuCode(int beishu, int amt) {
		String code = "";
		for (int i = 0; i < codeList.size(); i++) {
			CodeInfoMiss codeInfo = codeList.get(i);
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
				String code = codeList.get(i).getCodes().get(j);
				Log.v("code", code);
				zhuma.append(code);
				if (j != codeList.get(i).getCodes().size() - 1) {
					zhuma.append("|");
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
		for (CodeInfoMiss codeInfo : codeList) {
			allZhu += codeInfo.getZhuShu();
		}
		return allZhu;
	}

	public long getAllAmt() {
		long allAmt = 0;
		for (CodeInfoMiss codeInfo : codeList) {
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

	public CodeInfoMiss initCodeInfo(int amt, int zhuShu) {
		return new CodeInfoMiss(amt, zhuShu);
	}

	public void addCodeInfo(CodeInfoMiss codeInfo) {
		codeList.add(codeInfo);
	}

	public List<CodeInfoMiss> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<CodeInfoMiss> codeList) {
		this.codeList = codeList;
	}

	public void clearInfo() {
		codeList.clear();
	}

	public void showDialog() {
		dialog.show();
		dialog.getWindow().setContentView(view);
	}

	class AddListAdapter extends BaseAdapter {
		private boolean isDelet = true;
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<CodeInfoMiss> codeInfos = new ArrayList<CodeInfoMiss>();

		public AddListAdapter(Context context, List<CodeInfoMiss> codeInfos,
				boolean isDelet) {
			mInflater = LayoutInflater.from(context);
			this.codeInfos = codeInfos;
			this.isDelet = isDelet;
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
			// TODO Auto-generated method stub
			CodeInfoMiss codeInfo = codeInfos.get(position);
			convertView = mInflater.inflate(R.layout.buy_add_dialog_list_item,
					null);
			TextView textNum = (TextView) convertView
					.findViewById(R.id.buy_add_list_item_text_num);
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
					// TODO Auto-generated method stub
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

	public class CodeInfoMiss {
		private String touZhuCode;// 投注时的注码，传给后台的
		List<String> codes = new ArrayList<String>();
		List<Integer> colors = new ArrayList<Integer>();
		long amt;
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

		public CodeInfoMiss(int amt, int zhuShu) {
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

		public long getAmt() {
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
				addSeparator(lotoNo, touzhuType, builder, i);
				upLength = builder.length();
			}
			textCode.setText(builder, BufferType.EDITABLE);
		}

		private void addSeparator(String lotoNo, String touzhuType,
				SpannableStringBuilder builder, int i) {
			if (i != getCodes().size() - 1) {
				if (lotoNo == null || touzhuType == null) {
					builder.append("|");
				} else if (lotoNo.equals(Constants.LOTNO_SSQ)
						&& touzhuType.equals("dantuo")) {
					if (i == 0) {
						builder.append("#");
					} else {
						builder.append("|");
					}
				} else if (lotoNo.equals(Constants.LOTNO_DLT)
						&& touzhuType.equals("dantuo")) {
					if (i == 0 || i == 2) {
						builder.append("#");
					} else {
						builder.append("|");
					}
				} else if (lotoNo.equals(Constants.LOTNO_FC3D)
						&& touzhuType.equals("zu3_danshi")) {
					builder.append(",");
				} else {
					builder.append("|");
				}
			}
		}
	}
}
