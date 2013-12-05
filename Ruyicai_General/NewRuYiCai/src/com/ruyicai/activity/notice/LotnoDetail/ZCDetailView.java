package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.zc.FootBallMainActivity;
import com.ruyicai.activity.notice.PrizeBallLinearLayout;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

public class ZCDetailView extends LotnoDetailView {
	String title;

	public ZCDetailView(Activity context, String lotno, String batchcode,
			ProgressDialog progress, Handler handler, boolean isDialog) {
		super(context, lotno, batchcode, progress, handler, isDialog);
		title = PublicMethod.toLotno(lotno);
	}

	TextView prizeBatchCode, prizeDate, totalsellmoney, prizepoolmoney;
	TextView prizename1, prizenum1, prizemoney1;
	TextView prizename2, prizenum2, prizemoney2;
	TextView prizename3, prizenum3, prizemoney3;
	Button tobet;
	JSONObject json;
	String wincode;

	@Override
	void initLotnoDetailViewWidget() {
		prizeBatchCode = (TextView) view
				.findViewById(R.id.prizedetail_batchcode);
		prizeDate = (TextView) view.findViewById(R.id.prizedetail_prizeData);
		ballLinear = (LinearLayout) view.findViewById(R.id.prizedetail_numball);
		totalsellmoney = (TextView) view
				.findViewById(R.id.prizedetail_totalsellmoney);
		prizepoolmoney = (TextView) view
				.findViewById(R.id.prizedetail_prizepoolmoney);
		prizename1 = (TextView) view.findViewById(R.id.prizedetail_prizename1);
		prizename2 = (TextView) view.findViewById(R.id.prizedetail_prizename2);
		prizename3 = (TextView) view.findViewById(R.id.prizedetail_prizename3);
		prizenum1 = (TextView) view.findViewById(R.id.prizedetail_prizenum1);
		prizenum2 = (TextView) view.findViewById(R.id.prizedetail_prizenum2);
		prizenum3 = (TextView) view.findViewById(R.id.prizedetail_prizenum3);
		prizemoney1 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney1);
		prizemoney2 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney2);
		prizemoney3 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney3);
		prizepoolmoney.setVisibility(TextView.GONE);
		tobet = (Button) view.findViewById(R.id.tobet);

	}

	@Override
	public void initLotonoView(JSONObject json) throws JSONException {
		/**add by yejc 20130418 start*/
		if (Constants.isDebug) {
			PublicMethod.outLog(this.getClass().getSimpleName(), "initLotonoView");
		}
		/**add by yejc 20130418 end*/
		this.json = json;
		prizeBatchCode.setText(title + "  第" + json.getString("batchCode")
				+ "期");
		prizeDate.setText(context.getString(R.string.prizedetail_opentime)
				+ json.getString("openTime"));
		String prizeNum = json.getString("winNo");
		PrizeBallLinearLayout fc3dBallLinear = new PrizeBallLinearLayout(
				context, ballLinear, Constants.FC3DLABEL, prizeNum);
		wincode = fc3dBallLinear.initZCBallLinear();
		totalsellmoney.setText(context
				.getString(R.string.prizedetail_thebatchcodeamt)
				+ PublicMethod.toIntYuan(json.getString("sellTotalAmount"))
				+ context.getString(R.string.game_card_yuan));
		/**add by yejc 20130418 start*/
		if (json.has("prizePoolTotalAmount")) {
			prizepoolmoney.setText(context
					.getString(R.string.prizedetail_theprizepoolamt)
					+ PublicMethod.toIntYuan(json.getString("prizePoolTotalAmount"))
					+ context.getString(R.string.game_card_yuan));
			prizepoolmoney.setVisibility(View.VISIBLE);
		}
		/**add by yejc 20130418 end*/
		prizename1.setText(R.string.prizedetail_fristprize);
		prizenum1.setText(json.getString("onePrizeNum"));
		prizemoney1.setText(PublicMethod.toIntYuan(json
				.getString("onePrizeAmt")));
		if (lotno == Constants.LOTNO_SFC) {
			prizename2.setText(R.string.prizedetail_secondprize);
			prizenum2.setText(json.getString("twoPrizeNum"));
			prizemoney2.setText(PublicMethod.toIntYuan(json
					.getString("twoPrizeAmt")));
		}
		tobet.setText(title + "投注");
		tobet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, FootBallMainActivity.class);
				if (lotno == Constants.LOTNO_SFC) {
					intent.putExtra("index", 0);
				} else if (lotno == Constants.LOTNO_RX9) {
					intent.putExtra("index", 1);
				} else if (lotno == Constants.LOTNO_JQC) {
					intent.putExtra("index", 3);
				} else if (lotno == Constants.LOTNO_LCB) {
					intent.putExtra("index", 2);
				}
				context.startActivity(intent);
			}
		});
	}

	@Override
	public String getShareString() {
		StringBuffer str = new StringBuffer();
		try {
			str.append("#如意彩客户端#，").append(
					title + json.getString("batchCode") + "期,");
			str.append(context.getString(R.string.prizedetail_opentime)
					+ json.getString("openTime") + ",");
			str.append("开奖号码:" + wincode + ",");
			str.append(context.getString(R.string.prizedetail_thebatchcodeamt)
					+ PublicMethod.toIntYuan(json.getString("sellTotalAmount"))
					+ context.getString(R.string.game_card_yuan) + ",");
			str.append("奖池奖金"
					+ PublicMethod.toIntYuan(json
							.getString("prizePoolTotalAmount"))
					+ context.getString(R.string.game_card_yuan) + ",");
			str.append("在@如意彩 买彩票，中奖福地，精“彩”不断！也许下一个大奖就属于您！");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return str.toString();
	}
}
