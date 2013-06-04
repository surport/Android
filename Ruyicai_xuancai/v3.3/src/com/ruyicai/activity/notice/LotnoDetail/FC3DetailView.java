package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.notice.NoticeMainActivity;
import com.ruyicai.activity.notice.PrizeBallLinearLayout;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.NoticePrizeDetailInterface;
import com.ruyicai.util.PublicMethod;

public class FC3DetailView extends LotnoDetailView{
	
	public FC3DetailView(Activity context, String lotno, String batchcode,ProgressDialog progress,Handler handler,boolean isDialog) {
		super(context, lotno, batchcode,progress,handler, isDialog);
		// TODO Auto-generated constructor stub
	}
	TextView prizeBatchCode,prizeDate,totalsellmoney,prizepoolmoney;
	TextView prizename1,prizenum1,prizemoney1;
	TextView prizename2,prizenum2,prizemoney2;
	TextView prizename3,prizenum3,prizemoney3;
	
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
		prizename2.setText(R.string.prizedetail_zu3);
		prizename3.setText(R.string.prizedetail_zu6);
		prizepoolmoney.setVisibility(TextView.GONE);
		
	}
	@Override
	public void initLotonoView(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		prizeBatchCode.setText("¸£²Ê3D    µÚ"+json.getString("batchCode")+"ÆÚ");
		prizeDate.setText(context.getString(R.string.prizedetail_opentime)+json.getString("openTime"));
		String prizeNum = json.getString("winNo");
		PrizeBallLinearLayout fc3dBallLinear = new PrizeBallLinearLayout(context, ballLinear, Constants.FC3DLABEL, prizeNum);
		fc3dBallLinear.init3DBallLinear();
		String Codetry = json.getString("tryCode");
		TextView trycode=new TextView(context);
		trycode.setTextColor(Color.BLUE);
		trycode.setTextSize(17);
		trycode.setPadding(10, 10, 0, 0);
		String codeshow="";
		if(!Codetry.equals("")){
		for (int i1 = 0; i1 < 3; i1++) {
			codeshow  += Codetry.substring(i1 * 2+1,
					i1 * 2 + 2);
			if(i1!=2){
				codeshow+=",";	
			}
		 }
		}
        trycode.setText("ÊÔ»úºÅ:"+codeshow);	
        ballLinear.addView(trycode);	
        totalsellmoney.setText(context.getString(R.string.prizedetail_thebatchcodeamt)+PublicMethod.toIntYuan(json.getString("sellTotalAmount"))+context.getString(R.string.game_card_yuan));
		prizenum1.setText(json.getString("onePrizeNum"));
		prizenum2.setText(json.getString("twoPrizeNum"));
		prizenum3.setText(json.getString("threePrizeNum"));
		prizemoney1.setText(PublicMethod.toIntYuan(json.getString("onePrizeAmt")));
		prizemoney2.setText(PublicMethod.toIntYuan(json.getString("twoPrizeAmt")));
		prizemoney3.setText(PublicMethod.toIntYuan(json.getString("threePrizeAmt")));
	}
	



}
