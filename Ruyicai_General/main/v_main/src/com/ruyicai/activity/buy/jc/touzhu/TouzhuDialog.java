package com.ruyicai.activity.buy.jc.touzhu;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.dialog.MessageDialog;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 投注类
 * 
 * @author Administrator
 * 
 */
public class TouzhuDialog {
	JcMainActivity context;
	RadioGroupView viewGroup;
	Dialog dialog = null;
	String alertMsg;
	int teamNum = 0;
	int oneAmt = 2;
	private TextView alertText, prizeText;
	private JcMainView jcMainView;
	public double freedomMaxprize = 0;
	public double freedomMixprize = 0;
	public int zhuShu = 0;
	public boolean isRadio = false;// false是自由过关,true是多串过关
	private final int MAXAMT = 20000;// 最大投注金额
	String returnStr = "";

	public TouzhuDialog(JcMainActivity context, JcMainView jcMainView) {
		this.context = context;
		this.jcMainView = jcMainView;
		viewGroup = new RadioGroupView(context.getContext(), this);
	}

	public int getTeamNum() {
		return context.getTeamNum();
	}

	/**
	 * 初始化一些信息
	 */
	private void initInfo() {
		this.alertMsg = jcMainView.getAlertMsg();
		this.teamNum = jcMainView.initCheckedNum();
		isRadio = false;
	}

	/**
	 * 投注调用函数
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alert() {
		initInfo();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.alert_dialog_jc_touzhu, null);
		dialog = new Dialog(context, R.style.MyDialog);
		alertText = (TextView) v
				.findViewById(R.id.alert_dialog_touzhu_text_one);
		prizeText = (TextView) v
				.findViewById(R.id.alert_dialog_touzhu_prizescope);// 竞彩中奖金额范围
		context.initImageView(v);
		setAlertText();
		setPrizeText();
		initBtn(dialog, v);
		dialog.setContentView(v);
		dialog.show();
	}

	/**
	 * 初始化投注框里的组件
	 * 
	 * @param dialog
	 * @param cancel
	 * @param ok
	 * @param infoBtn
	 * @param zhuma
	 */
	private void initBtn(final Dialog dialog, View v) {
		TextView labe = (TextView) v
				.findViewById(R.id.alert_dialog_touzhu_text_fangshi);
		LinearLayout btnlayout = (LinearLayout) v
				.findViewById(R.id.buttonlayout);
		Button cancel = (Button) v
				.findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_ok);
		Button infoBtn = (Button) v
				.findViewById(R.id.alert_dialog_jc_touzhu_btn_info);
		final Button zyBtn = (Button) v.findViewById(R.id.jc_alert_btn_ziyou);
		final Button dcBtn = (Button) v
				.findViewById(R.id.jc_alert_btn_duochuan);
		final LinearLayout layout = (LinearLayout) v
				.findViewById(R.id.alert_dialog_jc_layout_group);
		if (jcMainView.isHunHe()) {
			zyBtn.setVisibility(View.GONE);
			dcBtn.setVisibility(View.GONE);
		}
		if (jcMainView.isDanguan) {
			labe.setText("过关方式：单关");
			zhuShu = jcMainView.getDanZhushu();
			btnlayout.setVisibility(View.INVISIBLE);
			setPrizeText();
			setAlertText();
		} else {
			btnlayout.setVisibility(View.VISIBLE);
			labe.setText("过关方式：");
			onclikBtn(layout, zyBtn, dcBtn);
			zyBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					isRadio = false;
					onclikBtn(layout, zyBtn, dcBtn);
				}
			});
			dcBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					isRadio = true;
					onclikBtn(layout, zyBtn, dcBtn);
				}
			});
		}
		final String title = "投注详情";
		infoBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showInfoDialog(title, alertMsg);
			}
		});
		cancel.setOnClickListener(touzhuOrhemaiListener);
		ok.setOnClickListener(touzhuOrhemaiListener);
	}

	OnClickListener touzhuOrhemaiListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RWSharedPreferences pre = new RWSharedPreferences(context,
					"addInfo");
			context.sessionId = pre.getStringValue("sessionid");
			context.phonenum = pre.getStringValue("phonenum");
			context.userno = pre.getStringValue("userno");
			if (context.userno == null || context.userno.equals("")) {
				Intent intentSession = new Intent(context, UserLogin.class);
				context.startActivityForResult(intentSession, 0);
			} else if (zhuShu == 0) {
				Toast.makeText(context, "请选择过关方式", Toast.LENGTH_SHORT).show();
			} else {
				if (isAmtDialog()) {
					alertInfo(
							context.getString(R.string.jc_main_touzhu_alert_text_content),
							context.getString(R.string.jc_main_touzhu_alert_text_title));
				} else {
					switch (v.getId()) {
					case R.id.alert_dialog_touzhu_button_cancel:
						initBet();
						context.toJoinActivity();
						break;
					case R.id.alert_dialog_touzhu_button_ok:
						beginTouzhu();
						break;
					}
					dialog.cancel();
				}
			}

		}
	};

	/**
	 * 是否弹出温馨提示
	 * 
	 * @return
	 */
	public boolean isAmtDialog() {
		int allAmt = getAllAmt();
		if (allAmt > MAXAMT || allAmt < 0) {
			return true;
		} else {
			return false;
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

	/**
	 * 自由和多串过关按钮事件
	 * 
	 * @param layout
	 * @param zyBtn
	 * @param dcBtn
	 */
	private void onclikBtn(final LinearLayout layout, Button zyBtn, Button dcBtn) {
		freedomMaxprize = 0;
		freedomMixprize = 0;
		setPrizeText();
		if (isRadio) {
			dcBtn.setBackgroundResource(R.drawable.jc_alert_right_radio_b);
			zyBtn.setBackgroundResource(R.drawable.jc_alert_left_radio);
		} else {
			dcBtn.setBackgroundResource(R.drawable.jc_alert_right_radio);
			zyBtn.setBackgroundResource(R.drawable.jc_alert_left_radio_b);
		}
		layout.removeAllViews();
		layout.addView(viewGroup.createView(isRadio, teamNum));
	}

	private void initBet() {
		context.initBet();
		context.getBetAndGiftPojo().setAmount("" + getAllAmt() * 100);
		context.getBetAndGiftPojo().setLotmulti("" + getBeishu());
		/** add by pengcx 20130609 start */
		context.getBetAndGiftPojo().setPredictMoney(
				returnStr.substring("预计中奖金额：".length()));
		/** add by pengcx 20130609 end */
		if (jcMainView.isDanguan) {
			context.getBetAndGiftPojo().setBet_code(
					getBetCode(context.getString(R.string.jc_touzhu_DAN)) + "_"
							+ PublicMethod.isTen(getBeishu()) + "_" + oneAmt
							* 100 + "_" + jcMainView.getDanZhushu() * oneAmt
							* 100 + "!");
		} else {
			context.getBetAndGiftPojo().setBet_code(viewGroup.getBetCode());
		}
		context.getBetAndGiftPojo().setIsSellWays("1");
	}

	private void beginTouzhu() {
		initBet();
		ApplicationAddview app = (ApplicationAddview) context
				.getApplicationContext();
		app.setPojo(context.betAndGift);
		context.touZhuNet();
	}

	/**
	 * 投注详情提示框
	 * 
	 * @param title
	 * @param msg
	 */
	public void showInfoDialog(String title, String msg) {
		MessageDialog msgDialog = new MessageDialog(context, title, msg);
		msgDialog.showDialog();
		msgDialog.createFillDialog();
	}

	/**
	 * 中奖范围
	 */
	public void setPrizeText() {
		if (jcMainView.isDanguan) {
			returnStr = jcMainView.getDanPrizeString(getBeishu());
		} else {
			if (!isRadio) {
				returnStr = getFreedomGuoGuanPrize(context.getIprogressBeiShu());
			} else {
				returnStr = getFreedomGuoGuanPrize(context.getIprogressBeiShu());
			}
		}

		prizeText.setText(returnStr);
	}

	public String getFreedomGuoGuanPrize(int muti) {
		double max = freedomMaxprize * muti;
		double mix = freedomMixprize * muti;
		StringBuffer result = new StringBuffer();
		result.append("预计中奖金额：")
				.append(PublicMethod.formatStringToTwoPoint(mix)).append("元~")
				.append(PublicMethod.formatStringToTwoPoint(max)).append("元 ");
		return result + "";
	}

	public double getFreedomDuoMixPrize(int teamNum, int select) {
		return jcMainView.getDuoMixPrize(teamNum, select);
	}

	public double getFreedomDuoMaxPrize(int teamNum, int select) {
		return jcMainView.getDuoMaxPrize(teamNum, select);
	}

	public double getFreedomMaxPrize(int select) {
		if (jcMainView.isHunHe()) {
			return jcMainView.getFreedomHunMaxPrize(select);
		} else {
			return jcMainView.getFreedomMaxPrize(select);
		}

	}

	public double getFreedomMixPrize(int select) {
		return jcMainView.getFreedomMixPrize(select);
	}

	/**
	 * 提示信息
	 */
	public void setAlertText() {
		String returnStr = "注数：" + zhuShu + "注   " + "倍数："
				+ context.getIprogressBeiShu() + "倍   " + "金额：" + getAllAmt()
				+ "元";
		alertText.setText(returnStr);
	}

	/**
	 * 获取设胆个数
	 * 
	 * @return
	 */
	public int getIsDanNum() {
		return jcMainView.getIsDanNum();
	}

	public int getZhushu(int select) {
		return jcMainView.getZhushu(select);
	}

	public int getBeishu() {
		return context.getIprogressBeiShu();
	}

	public int getAllAmt() {
		return zhuShu * getBeishu() * 2;
	}

	public String getBetCode(String type) {
		return jcMainView.getBetCode(type);
	}

	public List<double[]> getOddsArraysValue() {
		return jcMainView.getOddsArraysValue();
	}

	/**
	 * 多串过关计算注数
	 * 
	 * @param select
	 * @return
	 */
	public int getDuoZhushu(int teamNum, int select) {
		return jcMainView.getDuoZhushu(teamNum, select);
	}
}
