package com.ruyicai.activity.usercenter.detail;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.ContentListView;
import com.ruyicai.activity.usercenter.info.WinPrizeQueryInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

public class Windetail extends Activity {
	WinPrizeQueryInfo info;
	private ContentListView contentListView = new ContentListView(this);

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
		LinearLayout layoutMain = (LinearLayout) findViewById(R.id.bet_detail_layout_content);
		final String lotno = info.getLotNo();
		lotkind.append(info.getLotName());
		if (lotno.equals("J00001") || lotno.equals("J00002")
				|| lotno.equals("J00003") || lotno.equals("J00004")
				|| lotno.equals(Constants.LOTNO_JCLQ)
				|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
				|| lotno.equals(Constants.LOTNO_JCLQ_RF)
				|| lotno.equals(Constants.LOTNO_JCLQ_SFC)
				|| lotno.equals(Constants.LOTNO_JCZQ_HUN)
				|| lotno.equals(Constants.LOTNO_JCLQ_HUN)
				|| lotno.equals(Constants.LOTNO_JCZQ_RQSPF)) {
			batchcode.setVisibility(View.GONE);
			kaijianghao.setVisibility(View.GONE);
		} else {
			if (lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS)
					|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_TOTALGOALS)
					|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_OVERALL)
					|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_HALFTHEAUDIENCE)
					|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_UPDOWNSINGLEDOUBLE)) {
				kaijianghao.setVisibility(View.GONE);
			} else {
				kaijianghao.setVisibility(View.VISIBLE);
				if (info.getWincode().equals("")) {
					kaijianghao.append("未开奖");
				} else {
					kaijianghao.append(info.getWincode());
				}
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
		bettime.append(info.getSellTime());
		contentListView.createListContent(layoutMain, content, info.getLotNo(),
				info.getBetCodeHtml(), contentJson());
		Button cancleLook = (Button) findViewById(R.id.gift_detail_img_cannle);
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private JSONObject contentJson() {

		JSONObject json = null;
		try {
			json = new JSONObject(info.getJson());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
