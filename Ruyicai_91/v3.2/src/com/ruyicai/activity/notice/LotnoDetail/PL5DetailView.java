package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.notice.PrizeBallLinearLayout;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.NoticePrizeDetailInterface;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PL5DetailView extends LotnoDetailView{
	public PL5DetailView(Activity context, String lotno, String batchcode,ProgressDialog progress,Handler handler,boolean isDialog) {
		super(context, lotno, batchcode,progress,handler, isDialog);
		// TODO Auto-generated constructor stub
	}

	TextView prizeBatchCode,prizeDate,totalsellmoney,prizepoolmoney;
	TextView prizename1,prizenum1,prizemoney1;

	@Override
	void initLotnoDetailViewWidget() {
		prizeBatchCode = (TextView)view.findViewById(R.id.prizedetail_batchcode);
		prizeDate = (TextView)view.findViewById(R.id.prizedetail_prizeData);
		ballLinear = (LinearLayout)view.findViewById(R.id.prizedetail_numball);
		totalsellmoney = (TextView)view.findViewById(R.id.prizedetail_totalsellmoney);
		prizepoolmoney = (TextView)view.findViewById(R.id.prizedetail_prizepoolmoney);
		prizename1 = (TextView)view.findViewById(R.id.prizedetail_prizename1);
		prizenum1  = (TextView)view.findViewById(R.id.prizedetail_prizenum1);
		prizemoney1 =(TextView)view.findViewById(R.id.prizedetail_prizemoney1); 
		
		prizename1.setText(R.string.prizedetail_fristprize);
		prizepoolmoney.setVisibility(TextView.GONE);
	}


	@Override
	public void initLotonoView(JSONObject PrizeDetailJson) throws JSONException {
		// TODO Auto-generated method stub
		prizeBatchCode.setText("排列五    第"+ PrizeDetailJson.getString("batchCode")+"期");
		prizeDate.setText(context.getString(R.string.prizedetail_opentime)+ PrizeDetailJson.getString("openTime"));
		String prizeNum =  PrizeDetailJson.getString("winNo");
		PrizeBallLinearLayout prizeBallLinear = new PrizeBallLinearLayout(context, ballLinear, Constants.PL5LABEL, prizeNum);
		prizeBallLinear.initPL5BallLinear();
		totalsellmoney.setText(context.getString(R.string.prizedetail_thebatchcodeamt)+ PublicMethod.toIntYuan(PrizeDetailJson.getString("sellTotalAmount"))+context.getString(R.string.game_card_yuan));
		prizenum1.setText( PrizeDetailJson.getString("onePrizeNum"));
		prizemoney1.setText( PublicMethod.toIntYuan(PrizeDetailJson.getString("onePrizeAmt")));

	}

}
