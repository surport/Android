package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.pl5.PL5;
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

public class PL5DetailView extends LotnoDetailView {
	public PL5DetailView(Activity context, String lotno, String batchcode,
			ProgressDialog progress, Handler handler, boolean isDialog) {
		super(context, lotno, batchcode, progress, handler, isDialog);
		// TODO Auto-generated constructor stub
	}

	TextView prizeBatchCode, prizeDate, totalsellmoney, prizepoolmoney;
	TextView prizename1, prizenum1, prizemoney1;
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
		prizenum1 = (TextView) view.findViewById(R.id.prizedetail_prizenum1);
		prizemoney1 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney1);

		prizename1.setText(R.string.prizedetail_fristprize);
		prizepoolmoney.setVisibility(TextView.GONE);
		tobet = (Button) view.findViewById(R.id.tobet);

	}

	@Override
	public void initLotonoView(JSONObject PrizeDetailJson) throws JSONException {
		// TODO Auto-generated method stub
		json = PrizeDetailJson;
		prizeBatchCode.setText("排列五    第"
				+ PrizeDetailJson.getString("batchCode") + "期");
		prizeDate.setText(context.getString(R.string.prizedetail_opentime)
				+ PrizeDetailJson.getString("openTime"));
		String prizeNum = PrizeDetailJson.getString("winNo");
		PrizeBallLinearLayout prizeBallLinear = new PrizeBallLinearLayout(
				context, ballLinear, Constants.PL5LABEL, prizeNum);
		wincode = prizeBallLinear.initPL5BallLinear();
		totalsellmoney.setText(context
				.getString(R.string.prizedetail_thebatchcodeamt)
				+ PublicMethod.toIntYuan(PrizeDetailJson
						.getString("sellTotalAmount"))
				+ context.getString(R.string.game_card_yuan));
		prizenum1.setText(PrizeDetailJson.getString("onePrizeNum"));
		prizemoney1.setText(PublicMethod.toIntYuan(PrizeDetailJson
				.getString("onePrizeAmt")));
		tobet.setText("排列五投注");
		tobet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, PL5.class);
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
					"排列5第" + json.getString("batchCode") + "期,");
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
