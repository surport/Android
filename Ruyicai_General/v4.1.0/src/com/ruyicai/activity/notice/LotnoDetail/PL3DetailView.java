package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.pl3.PL3;
import com.ruyicai.activity.buy.ssq.Ssq;
import com.ruyicai.activity.notice.PrizeBallLinearLayout;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.NoticePrizeDetailInterface;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PL3DetailView extends LotnoDetailView {
	public PL3DetailView(Activity context, String lotno, String batchcode,
			ProgressDialog progress, Handler handler, boolean isDialog) {
		super(context, lotno, batchcode, progress, handler, isDialog);
		// TODO Auto-generated constructor stub
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
		prizename1.setText(R.string.prizedetail_fristprize);
		prizename2.setText(R.string.prizedetail_zu3);
		prizename3.setText(R.string.prizedetail_zu6);
		prizepoolmoney.setVisibility(TextView.GONE);
		tobet = (Button) view.findViewById(R.id.tobet);

	}

	@Override
	public void initLotonoView(JSONObject PrizeDetailJson) throws JSONException {
		// TODO Auto-generated method stub
		json = PrizeDetailJson;
		prizeBatchCode.setText("排列三    第"
				+ PrizeDetailJson.getString("batchCode") + "期");
		prizeDate.setText(context.getString(R.string.prizedetail_opentime)
				+ PrizeDetailJson.getString("openTime"));
		String prizeNum = PrizeDetailJson.getString("winNo");
		PrizeBallLinearLayout prizeBallLinear = new PrizeBallLinearLayout(
				context, ballLinear, Constants.PL3LABEL, prizeNum);
		wincode = prizeBallLinear.initPL3BallLinear();
		totalsellmoney.setText(context
				.getString(R.string.prizedetail_thebatchcodeamt)
				+ PublicMethod.toIntYuan(PrizeDetailJson
						.getString("sellTotalAmount"))
				+ context.getString(R.string.game_card_yuan));
		prizenum1.setText(PrizeDetailJson.getString("onePrizeNum"));
		prizenum2.setText(PrizeDetailJson.getString("twoPrizeNum"));
		prizenum3.setText(PrizeDetailJson.getString("threePrizeNum"));
		prizemoney1.setText(PublicMethod.toIntYuan(PrizeDetailJson
				.getString("onePrizeAmt")));
		prizemoney2.setText(PublicMethod.toIntYuan(PrizeDetailJson
				.getString("twoPrizeAmt")));
		prizemoney3.setText(PublicMethod.toIntYuan(PrizeDetailJson
				.getString("threePrizeAmt")));
		tobet.setText("排列三投注");
		tobet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, PL3.class);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public String getShareString() {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		try {
			str.append("#如意彩客户端#，").append(
					"排列三" + json.getString("batchCode") + "期,");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toString();
	}

}
