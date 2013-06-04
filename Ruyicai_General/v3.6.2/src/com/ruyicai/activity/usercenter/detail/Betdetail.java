package com.ruyicai.activity.usercenter.detail;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.ContentListView;
import com.ruyicai.activity.usercenter.info.BetQueryInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

public class Betdetail extends Activity {
	
//	AlertDialog dialog = new AlertDialog.Builder(this).create();
	 BetQueryInfo info;
	 private ContentListView contentListView = new ContentListView(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bet_detail);
		getInfo();
		init();
	}
		
	/**
	 * 从上一个页面获取信息
	 */
	public void getInfo() {
		Intent intent = getIntent();
		byte[] bytes = intent.getByteArrayExtra("info");
		if (bytes != null) {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
			try {
				ObjectInputStream objStream = new ObjectInputStream(byteStream);
				info = (BetQueryInfo) objStream.readObject();
			} catch (Exception e) {
			}
		}

	}
	
	private void init(){
		final String lotno = (String) info.getLotNo();
		final String prizeqihao = (String) info.getBatchCode();
		final String amount = (String) info.getAmount();
		final String fPayMoney = PublicMethod.formatMoney(amount); 
		final String lotName = (String) info.getLotName();
		final String betNum=(String)info.getBetNum();
		final String lotMulti = (String) info.getLotMulti();
		final String prizemoney = (String) info.getPrizeAmt();
		final String ordertime = (String) info.getOrdertime();
		final String betcode = (String) info.getBetCode();
		final String prize_State = (String)info.getPrizeState();
		final String win_code = (String)info.getWin_code();
		final String orderId = (String)info.getOrderId();
		final String stateMo = (String)info.getStateMemo();
		final String betcodehtml=(String)info.getBetCodeHtml();
		TextView lotkind= (TextView)findViewById(R.id.bet_detail_text_lotno);
		TextView batchcode= (TextView)findViewById(R.id.bet_detail_text_batchcode);
		TextView dingdanno= (TextView)findViewById(R.id.bet_detail_text_dingdan);
		TextView beishu= (TextView)findViewById(R.id.join_detail_text_beishu);
		TextView zhushu= (TextView)findViewById(R.id.bet_detail_text_zhushu);
		TextView atm= (TextView)findViewById(R.id.bet_detail_text_atm);
		TextView state= (TextView)findViewById(R.id.bet_detail_text_state);
		TextView bettime= (TextView)findViewById(R.id.bet_detail_tex_bettime);
		TextView content= (TextView)findViewById(R.id.bet_detail_text_content);
		TextView kaijianghao= (TextView)findViewById(R.id.bet_detail_text_kaijianghao);
		LinearLayout layoutMain = (LinearLayout)findViewById(R.id.bet_detail_layout_content);
		lotkind.append(lotName);
		if(lotno.equals("J00001")||lotno.equals("J00002")||lotno.equals("J00003")||lotno.equals("J00004")||lotno.equals(Constants.LOTNO_JCLQ)||lotno.equals(Constants.LOTNO_JCLQ_DXF)||lotno.equals(Constants.LOTNO_JCLQ_RF)||lotno.equals(Constants.LOTNO_JCLQ_SFC)){
		batchcode.setVisibility(View.GONE);
		}else{
		batchcode.append(prizeqihao);
		}
		dingdanno.append(orderId);
		beishu.append(lotMulti);
		zhushu.append(betNum);
		atm.append(fPayMoney);
		state.append(stateMo);
		bettime.append(ordertime);
//		content.setText(Html.fromHtml("方案内容：<br>"+betcodehtml));
		JSONObject josn = null;
		try {
			josn = new JSONObject(info.getJson());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentListView.createListContent(layoutMain,content,info.getLotNo(),info.getBetCodeHtml(),josn);
		if(lotno.equals("J00001")||lotno.equals("J00002")||lotno.equals("J00003")||lotno.equals("J00004")||lotno.equals(Constants.LOTNO_JCLQ)||lotno.equals(Constants.LOTNO_JCLQ_DXF)||lotno.equals(Constants.LOTNO_JCLQ_RF)||lotno.equals(Constants.LOTNO_JCLQ_SFC)){
			kaijianghao.setVisibility(View.GONE);
			}else{
				if(prize_State.equals("0")){
					kaijianghao.append("未开奖");	
			         	}else{
			                   	kaijianghao.append(win_code);
			     }			
				}	
		Button cancleLook = (Button)findViewById(R.id.bet_detail_img_cannle);
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
                      finish();		
}
		});
		
	}
}
