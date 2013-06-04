package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.notice.PrizeBallLinearLayout;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

public class TwentyDetailView extends LotnoDetailView{
	public TwentyDetailView(Activity context, String lotno, String batchcode,ProgressDialog progress,Handler handler,boolean isDialog) {
		super(context, lotno, batchcode,progress,handler, isDialog);
		// TODO Auto-generated constructor stub
	}

	TextView prizeBatchCode,prizeDate,totalsellmoney,prizepoolmoney;
	TextView prizename1,prizenum1,prizemoney1;
	TextView prizename2,prizenum2,prizemoney2;
	TextView prizename3,prizenum3,prizemoney3;
	TextView prizename4,prizenum4,prizemoney4;
	TextView prizename5,prizenum5,prizemoney5;
	TextView prizename6,prizenum6,prizemoney6;

	@Override
	void initLotnoDetailViewWidget() {
		prizeBatchCode = (TextView)view.findViewById(R.id.prizedetail_batchcode);
		prizeDate = (TextView)view.findViewById(R.id.prizedetail_prizeData);
		ballLinear = (LinearLayout)view.findViewById(R.id.prizedetail_numball);
		totalsellmoney = (TextView)view.findViewById(R.id.prizedetail_totalsellmoney);
		prizepoolmoney = (TextView)view.findViewById(R.id.prizedetail_prizepoolmoney);
		prizename1 = (TextView)view.findViewById(R.id.prizedetail_prizename1);
		prizename2 = (TextView)view.findViewById(R.id.prizedetail_prizename2);
		prizename3 = (TextView)view.findViewById(R.id.prizedetail_prizename3);
		prizenum1  = (TextView)view.findViewById(R.id.prizedetail_prizenum1);
		prizenum2  = (TextView)view.findViewById(R.id.prizedetail_prizenum2);
		prizenum3  = (TextView)view.findViewById(R.id.prizedetail_prizenum3);
		prizemoney1 =(TextView)view.findViewById(R.id.prizedetail_prizemoney1); 
		prizemoney2 =(TextView)view.findViewById(R.id.prizedetail_prizemoney2);
		prizemoney3 =(TextView)view.findViewById(R.id.prizedetail_prizemoney3);
		prizename1.setText(R.string.prizedetail_fristprize);
		prizename2.setText(R.string.prizedetail_secondprize);
		prizename3.setText(R.string.prizedetail_thirdprize);
	}


	@Override
	public void initLotonoView(JSONObject PrizeDetailJson) throws JSONException {
		// TODO Auto-generated method stub
		prizeBatchCode.setText("22Ñ¡5    µÚ"+ PrizeDetailJson.getString("batchCode")+"ÆÚ");
		prizeDate.setText(context.getString(R.string.prizedetail_opentime)+ PrizeDetailJson.getString("openTime"));
		String prizeNum =  PrizeDetailJson.getString("winNo");
		PrizeBallLinearLayout prizeBallLinear = new PrizeBallLinearLayout(context, ballLinear, Constants.TWENTYBEL, prizeNum);
		prizeBallLinear.initTwentyjBallLinear();
		totalsellmoney.setText(context.getString(R.string.prizedetail_thebatchcodeamt)+PublicMethod.toIntYuan(PrizeDetailJson.getString("sellTotalAmount"))+context.getString(R.string.game_card_yuan));
		prizepoolmoney.setText(context.getString(R.string.prizedetail_theprizepoolamt)+PublicMethod.toIntYuan(PrizeDetailJson.getString("prizePoolTotalAmount"))+context.getString(R.string.game_card_yuan));
		prizenum1.setText( PrizeDetailJson.getString("onePrizeNum"));
		prizenum1.setText(PrizeDetailJson.getString("onePrizeNum"));
		prizenum2.setText(PrizeDetailJson.getString("twoPrizeNum"));
		prizenum3.setText(PrizeDetailJson.getString("threePrizeNum"));
		prizemoney1.setText(PublicMethod.toIntYuan(PrizeDetailJson.getString("onePrizeAmt")));
		prizemoney2.setText(PublicMethod.toIntYuan(PrizeDetailJson.getString("twoPrizeAmt")));
		prizemoney3.setText(PublicMethod.toIntYuan(PrizeDetailJson.getString("threePrizeAmt")));

	}

}
