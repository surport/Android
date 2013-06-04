package com.ruyicai.activity.usercenter.detail;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.usercenter.info.WinPrizeQueryInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

public class Windetail extends Activity {
	WinPrizeQueryInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.win_detail);
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
				info = (WinPrizeQueryInfo) objStream.readObject();
			} catch (Exception e) {
			}
		}

	}

	private void init() {
		TextView lotkind = (TextView) findViewById(R.id.gift_detail_text_lotno);
		TextView batchcode = (TextView) findViewById(R.id.gift_detail_text_batchcode);
		TextView dingdanno = (TextView) findViewById(R.id.gift_detail_text_dingdan);
		TextView beishu = (TextView) findViewById(R.id.gift_detail_text_beishu);
		TextView zhushu = (TextView) findViewById(R.id.gift_detail_text_zhushu);
		TextView atm = (TextView) findViewById(R.id.gift_detail_text_atm);
		TextView statetext = (TextView) findViewById(R.id.gift_detail_text_state);
		TextView bettime = (TextView) findViewById(R.id.gift_detail_tex_gifttime);
		TextView content = (TextView) findViewById(R.id.gift_detail_text_content);
		TextView person = (TextView) findViewById(R.id.gift_detail_text_person);
		TextView kaijianghao = (TextView) findViewById(R.id.gift_detail_text_kaijianghao);
		TextView atmz = (TextView) findViewById(R.id.gift_detail_text_atmzhong);
		TextView cashtime = (TextView) findViewById(R.id.gift_detail_tex_time);
		final String lotno = info.getLotNo();
		lotkind.append(info.getLotName());
		if (lotno.equals("J00001") || lotno.equals("J00002")
				|| lotno.equals("J00003") || lotno.equals("J00004")
				|| lotno.equals(Constants.LOTNO_JCLQ)
				|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
				|| lotno.equals(Constants.LOTNO_JCLQ_RF)
				|| lotno.equals(Constants.LOTNO_JCLQ_SFC)) {
			batchcode.setVisibility(View.GONE);
			kaijianghao.setVisibility(View.GONE);
		} else {
			kaijianghao.setVisibility(View.VISIBLE);
			 if(info.getWincode().equals("")){
				 kaijianghao.append("未开奖");
				 }else{
				 kaijianghao.append(info.getWincode());
				}
			batchcode.setVisibility(TextView.VISIBLE);
			batchcode.append(info.getBatchCode());
		}
		dingdanno.append(info.getOrderId());
		beishu.append(info.getLotMulti());
		zhushu.append(info.getBetNum());
		final String FormatAmount = PublicMethod.toYuan(info.getAmount());
		atm.append(FormatAmount + "元");
		atmz.append(PublicMethod.toYuan(info.getWinAmount()) + "元");
		cashtime.append(info.getCashTime());
		person.setVisibility(View.GONE);
		statetext.setVisibility(View.GONE);
		// statetext.append(info.getStateMemo());
		bettime.append(info.getSellTime());
		content.setText(Html.fromHtml("方案内容：<br>" + info.getBetCodeHtml()));
		Button cancleLook = (Button) findViewById(R.id.gift_detail_img_cannle);
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
