package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.dlt.Dlt;
import com.ruyicai.activity.notice.PrizeBallLinearLayout;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

public class DLTDetailView extends LotnoDetailView {

	TextView prizeBatchCode, prizeDate, totalsellmoney, prizepoolmoney;
	TextView prizename1, prizenum1, prizemoney1;
	TextView prizename2, prizenum2, prizemoney2;
	TextView prizename3, prizenum3, prizemoney3;
	TextView prizename4, prizenum4, prizemoney4;
	TextView prizename5, prizenum5, prizemoney5;
	TextView prizename6, prizenum6, prizemoney6;
	TextView prizename7, prizenum7, prizemoney7;
	TextView prizename8, prizenum8, prizemoney8;
	TextView prizename9, prizenum9, prizemoney9;
	TextView prizename10, prizenum10, prizemoney10;
	TextView prizename11, prizenum11, prizemoney11;
	TextView prizename12, prizenum12, prizemoney12;
	TextView prizename13, prizenum13, prizemoney13;
	TextView prizename14, prizenum14, prizemoney14;
	TextView prizename15, prizenum15, prizemoney15;
	TextView prizename16, prizenum16, prizemoney16;
	LinearLayout linear8, linear9, linear10, linear11, linear12, linear13,
			linear14, linear15, linear16;
	Button tobet;
	JSONObject json;
	String wincode;

	public DLTDetailView(Activity context, String lotno, String batchcode,
			ProgressDialog progress, Handler handler, boolean isDialog) {
		super(context, lotno, batchcode, progress, handler, isDialog);
		// TODO Auto-generated constructor stub
	}

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
		prizename4 = (TextView) view.findViewById(R.id.prizedetail_prizename4);
		prizename5 = (TextView) view.findViewById(R.id.prizedetail_prizename5);
		prizename6 = (TextView) view.findViewById(R.id.prizedetail_prizename6);
		prizename7 = (TextView) view.findViewById(R.id.prizedetail_prizename7);
		prizename8 = (TextView) view.findViewById(R.id.prizedetail_prizename8);
		prizename9 = (TextView) view.findViewById(R.id.prizedetail_prizename9);
		prizename10 = (TextView) view
				.findViewById(R.id.prizedetail_prizename10);
		prizename11 = (TextView) view
				.findViewById(R.id.prizedetail_prizename11);
		prizename12 = (TextView) view
				.findViewById(R.id.prizedetail_prizename12);
		prizename13 = (TextView) view
				.findViewById(R.id.prizedetail_prizename13);
		prizename14 = (TextView) view
				.findViewById(R.id.prizedetail_prizename14);
		prizename15 = (TextView) view
				.findViewById(R.id.prizedetail_prizename15);
		prizename16 = (TextView) view
				.findViewById(R.id.prizedetail_prizename16);

		prizenum1 = (TextView) view.findViewById(R.id.prizedetail_prizenum1);
		prizenum2 = (TextView) view.findViewById(R.id.prizedetail_prizenum2);
		prizenum3 = (TextView) view.findViewById(R.id.prizedetail_prizenum3);
		prizenum4 = (TextView) view.findViewById(R.id.prizedetail_prizenum4);
		prizenum5 = (TextView) view.findViewById(R.id.prizedetail_prizenum5);
		prizenum6 = (TextView) view.findViewById(R.id.prizedetail_prizenum6);
		prizenum7 = (TextView) view.findViewById(R.id.prizedetail_prizenum7);
		prizenum8 = (TextView) view.findViewById(R.id.prizedetail_prizenum8);
		prizenum9 = (TextView) view.findViewById(R.id.prizedetail_prizenum9);
		prizenum10 = (TextView) view.findViewById(R.id.prizedetail_prizenum10);
		prizenum11 = (TextView) view.findViewById(R.id.prizedetail_prizenum11);
		prizenum12 = (TextView) view.findViewById(R.id.prizedetail_prizenum12);
		prizenum13 = (TextView) view.findViewById(R.id.prizedetail_prizenum13);
		prizenum14 = (TextView) view.findViewById(R.id.prizedetail_prizenum14);
		prizenum15 = (TextView) view.findViewById(R.id.prizedetail_prizenum15);
		prizenum16 = (TextView) view.findViewById(R.id.prizedetail_prizenum16);

		prizemoney1 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney1);
		prizemoney2 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney2);
		prizemoney3 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney3);
		prizemoney4 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney4);
		prizemoney5 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney5);
		prizemoney6 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney6);
		prizemoney7 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney7);
		prizemoney8 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney8);
		prizemoney9 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney9);
		prizemoney10 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney10);
		prizemoney11 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney11);
		prizemoney12 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney12);
		prizemoney13 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney13);
		prizemoney14 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney14);
		prizemoney15 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney15);
		prizemoney16 = (TextView) view
				.findViewById(R.id.prizedetail_prizemoney16);

		linear8 = (LinearLayout) view.findViewById(R.id.notice_detail_linear8);
		linear9 = (LinearLayout) view.findViewById(R.id.notice_detail_linear9);
		linear10 = (LinearLayout) view
				.findViewById(R.id.notice_detail_linear10);
		linear11 = (LinearLayout) view
				.findViewById(R.id.notice_detail_linear11);
		linear12 = (LinearLayout) view
				.findViewById(R.id.notice_detail_linear12);
		linear13 = (LinearLayout) view
				.findViewById(R.id.notice_detail_linear13);
		linear14 = (LinearLayout) view
				.findViewById(R.id.notice_detail_linear14);
		linear15 = (LinearLayout) view
				.findViewById(R.id.notice_detail_linear15);
		linear16 = (LinearLayout) view
				.findViewById(R.id.notice_detail_linear16);

		linear8.setVisibility(LinearLayout.VISIBLE);
		linear9.setVisibility(LinearLayout.VISIBLE);
		linear10.setVisibility(LinearLayout.VISIBLE);
		linear11.setVisibility(LinearLayout.VISIBLE);
		linear12.setVisibility(LinearLayout.VISIBLE);
		linear13.setVisibility(LinearLayout.VISIBLE);
		linear14.setVisibility(LinearLayout.VISIBLE);
		linear15.setVisibility(LinearLayout.VISIBLE);
		/*modify by pengcx 20130806 start*/
		linear16.setVisibility(LinearLayout.GONE);
		/*modify by pengcx 20130806 end*/

		prizename1.setText(R.string.prizedetail_fristprize);
		prizename2.setText(R.string.prizedetail_zhuijia);
		prizename3.setText(R.string.prizedetail_secondprize);
		prizename4.setText(R.string.prizedetail_zhuijia);
		prizename5.setText(R.string.prizedetail_thirdprize);
		prizename6.setText(R.string.prizedetail_zhuijia);
		prizename7.setText(R.string.prizedetail_fourthprize);
		prizename8.setText(R.string.prizedetail_zhuijia);
		prizename9.setText(R.string.prizedetail_fifthprize);
		prizename10.setText(R.string.prizedetail_zhuijia);
		prizename11.setText(R.string.prizedetail_sixthprize);
		prizename12.setText(R.string.prizedetail_zhuijia);
		prizename13.setText(R.string.prizedetail_seventhprize);
		prizename14.setText(R.string.prizedetail_zhuijia);
		prizename15.setText(R.string.prizedetail_eighthprize);
		prizename16.setText(R.string.prizedetail_12xuan2);
		tobet = (Button) view.findViewById(R.id.tobet);
	}

	@Override
	public void initLotonoView(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		this.json = json;
		prizeBatchCode.setText("大乐透    第" + json.getString("batchCode") + "期");
		prizeDate.setText(context.getString(R.string.prizedetail_opentime)
				+ json.getString("openTime"));
		String prizeNum = json.getString("winNo");
		PrizeBallLinearLayout ssqBallLinear = new PrizeBallLinearLayout(
				context, ballLinear, Constants.DLTLABEL, prizeNum);
		wincode = ssqBallLinear.initDltBallLinear();
		totalsellmoney.setText(context
				.getString(R.string.prizedetail_thebatchcodeamt)
				+ PublicMethod.toIntYuan(json.getString("sellTotalAmount"))
				+ context.getString(R.string.game_card_yuan));
		prizepoolmoney
				.setText(context
						.getString(R.string.prizedetail_theprizepoolamt)
						+ PublicMethod.toIntYuan(json
								.getString("prizePoolTotalAmount"))
						+ context.getString(R.string.game_card_yuan));
		prizenum1.setText(json.getString("onePrizeNum"));
		prizenum2.setText(json.getString("onePrizeZJNum"));
		prizenum3.setText(json.getString("twoPrizeNum"));
		prizenum4.setText(json.getString("twoPrizeZJNum"));
		prizenum5.setText(json.getString("threePrizeNum"));
		prizenum6.setText(json.getString("threePrizeZJNum"));
		prizenum7.setText(json.getString("fourPrizeNum"));
		prizenum8.setText(json.getString("fourPrizeZJNum"));
		prizenum9.setText(json.getString("fivePrizeNum"));
		prizenum10.setText(json.getString("fivePrizeZJNum"));
		prizenum11.setText(json.getString("sixPrizeNum"));
		prizenum12.setText(json.getString("sixPrizeZJNum"));
		prizenum13.setText(json.getString("sevenPrizeNum"));
		prizenum14.setText(json.getString("sevenPrizeZJNum"));
		prizenum15.setText(json.getString("eightPrizeNum"));
		prizenum16.setText(json.getString("twelveSelect2PrizeNum"));

		prizemoney1.setText(PublicMethod.toIntYuan(json
				.getString("onePrizeAmt")));
		prizemoney2.setText(PublicMethod.toIntYuan(json
				.getString("onePrizeZJAmt")));
		prizemoney3.setText(PublicMethod.toIntYuan(json
				.getString("twoPrizeAmt")));
		prizemoney4.setText(PublicMethod.toIntYuan(json
				.getString("twoPrizeZJAmt")));
		prizemoney5.setText(PublicMethod.toIntYuan(json
				.getString("threePrizeAmt")));
		prizemoney6.setText(PublicMethod.toIntYuan(json
				.getString("threePrizeZJAmt")));
		prizemoney7.setText(PublicMethod.toIntYuan(json
				.getString("fourPrizeAmt")));
		prizemoney8.setText(PublicMethod.toIntYuan(json
				.getString("fourPrizeZJAmt")));
		prizemoney9.setText(PublicMethod.toIntYuan(json
				.getString("fivePrizeAmt")));
		prizemoney10.setText(PublicMethod.toIntYuan(json
				.getString("fivePrizeZJAmt")));
		prizemoney11.setText(PublicMethod.toIntYuan(json
				.getString("sixPrizeAmt")));
		prizemoney12.setText(PublicMethod.toIntYuan(json
				.getString("sixPrizeZJAmt")));
		prizemoney13.setText(PublicMethod.toIntYuan(json
				.getString("sevenPrizeAmt")));
		prizemoney14.setText(PublicMethod.toIntYuan(json
				.getString("sevenPrizeZJAmt")));
		prizemoney15.setText(PublicMethod.toIntYuan(json
				.getString("eightPrizeAmt")));
		prizemoney16.setText(PublicMethod.toIntYuan(json
				.getString("twelveSelect2PrizeAmt")));
		tobet.setText("去大乐透投注");
		tobet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, Dlt.class);
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
					"大乐透第" + json.getString("batchCode") + "期,");
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
			str.append("在@如意彩 买彩票，中奖福地，精“彩”不断！也许下一个大奖就属于您!");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toString();
	}
}
