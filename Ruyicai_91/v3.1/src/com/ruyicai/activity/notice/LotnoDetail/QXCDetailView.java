package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.notice.PrizeBallLinearLayout;
import com.ruyicai.net.newtransaction.NoticePrizeDetailInterface;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QXCDetailView extends LotnoDetailView{
	public QXCDetailView(Activity context, String lotno, String batchcode,ProgressDialog progress,Handler handler,boolean isDialog) {
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
		prizename4 = (TextView)view.findViewById(R.id.prizedetail_prizename4);
		prizename5 = (TextView)view.findViewById(R.id.prizedetail_prizename5);
		prizename6 = (TextView)view.findViewById(R.id.prizedetail_prizename6);
		prizenum1  = (TextView)view.findViewById(R.id.prizedetail_prizenum1);
		prizenum2  = (TextView)view.findViewById(R.id.prizedetail_prizenum2);
		prizenum3  = (TextView)view.findViewById(R.id.prizedetail_prizenum3);
		prizenum4  = (TextView)view.findViewById(R.id.prizedetail_prizenum4);
		prizenum5  = (TextView)view.findViewById(R.id.prizedetail_prizenum5);
		prizenum6  = (TextView)view.findViewById(R.id.prizedetail_prizenum6);
		prizemoney1 =(TextView)view.findViewById(R.id.prizedetail_prizemoney1); 
		prizemoney2 =(TextView)view.findViewById(R.id.prizedetail_prizemoney2);
		prizemoney3 =(TextView)view.findViewById(R.id.prizedetail_prizemoney3);
		prizemoney4 =(TextView)view.findViewById(R.id.prizedetail_prizemoney4);
		prizemoney5 =(TextView)view.findViewById(R.id.prizedetail_prizemoney5);
		prizemoney6 =(TextView)view.findViewById(R.id.prizedetail_prizemoney6);
		prizename1.setText(R.string.prizedetail_fristprize);
		prizename2.setText(R.string.prizedetail_secondprize);
		prizename3.setText(R.string.prizedetail_thirdprize);
		prizename4.setText(R.string.prizedetail_fourthprize);
		prizename5.setText(R.string.prizedetail_fifthprize);
		prizename6.setText(R.string.prizedetail_sixthprize);
	}

	@Override
	public void initLotonoView(JSONObject ssqPrizeDetailJson) throws JSONException {
		// TODO Auto-generated method stub
		prizeBatchCode.setText("ÆßÐÇ²Ê    µÚ"+ssqPrizeDetailJson.getString("batchCode")+"ÆÚ");
		prizeDate.setText(context.getString(R.string.prizedetail_opentime)+ssqPrizeDetailJson.getString("openTime"));
		String prizeNum = ssqPrizeDetailJson.getString("winNo");
		PrizeBallLinearLayout prizeBallLinear = new PrizeBallLinearLayout(context, ballLinear, Constants.QXCLABEL, prizeNum);
		prizeBallLinear.initQXCBallLinear();
		totalsellmoney.setText(context.getString(R.string.prizedetail_thebatchcodeamt)+PublicMethod.toIntYuan(ssqPrizeDetailJson.getString("sellTotalAmount"))+context.getString(R.string.game_card_yuan));
		prizepoolmoney.setText(context.getString(R.string.prizedetail_theprizepoolamt)+PublicMethod.toIntYuan(ssqPrizeDetailJson.getString("prizePoolTotalAmount"))+context.getString(R.string.game_card_yuan));
		prizenum1.setText(ssqPrizeDetailJson.getString("onePrizeNum"));
		prizenum2.setText(ssqPrizeDetailJson.getString("twoPrizeNum"));
		prizenum3.setText(ssqPrizeDetailJson.getString("threePrizeNum"));
		prizenum4.setText(ssqPrizeDetailJson.getString("fourPrizeNum"));
		prizenum5.setText(ssqPrizeDetailJson.getString("fivePrizeNum"));
		prizenum6.setText(ssqPrizeDetailJson.getString("sixPrizeNum"));
		prizemoney1.setText(PublicMethod.toIntYuan(ssqPrizeDetailJson.getString("onePrizeAmt")));
		prizemoney2.setText(PublicMethod.toIntYuan(ssqPrizeDetailJson.getString("twoPrizeAmt")));
		prizemoney3.setText(PublicMethod.toIntYuan(ssqPrizeDetailJson.getString("threePrizeAmt")));
		prizemoney4.setText(PublicMethod.toIntYuan(ssqPrizeDetailJson.getString("fourPrizeAmt")));
		prizemoney5.setText(PublicMethod.toIntYuan(ssqPrizeDetailJson.getString("fivePrizeAmt")));
		prizemoney6.setText(PublicMethod.toIntYuan(ssqPrizeDetailJson.getString("sixPrizeAmt")));
	}

}
