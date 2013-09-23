package com.ruyicai.activity.buy.jc.lq.view;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.activity.buy.jc.explain.lq.JcLqExplainActivity;
import com.ruyicai.activity.buy.jc.lq.view.SfView.JcInfoAdapter;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 让分胜负
 * 
 * @author Administrator
 * 
 */
public class RfView extends SfView {

	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		// TODO Auto-generated method stub
		return basketSf.getOddsList(listInfo, B_RF);
	}

	public RfView(Context context, BetAndGiftPojo betAndGift, Handler handler,
			LinearLayout layout, String type, boolean isdanguan,
			List<String> checkTeam) {
		super(context, betAndGift, handler, layout, type, isdanguan, checkTeam);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 跳转到分析界面
	 */
	public void trunExplain(String event) {
		JcLqExplainActivity.Lq_TYPE = JcLqExplainActivity.LQ_RFSF;
		Intent intent = new Intent(context, JcLqExplainActivity.class);
		intent.putExtra("event", event);
		context.startActivity(intent);
	}

	@Override
	public String getLotno() {
		// TODO Auto-generated method stub
		return Constants.LOTNO_JCLQ_RF;
	}

	public void setDifferValue(JSONObject jsonItem, Info itemInfo)
			throws JSONException {
		// 让分胜负
		itemInfo.setLetFail(jsonItem.getString("letVs_v0"));
		itemInfo.setLetWin(jsonItem.getString("letVs_v3"));
		itemInfo.setLetPoint(jsonItem.getString("letPoint"));
	}

	/**
	 * 初始化列表
	 */
	public void initListView(ListView listview, Context context,
			List<List> listInfo) {
		// TODO Auto-generated method stub
		adapter = new JcInfoAdapter(context, listInfo, B_RF);
		listview.setAdapter(adapter);
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		if (isDanguan) {
			return context.getString(R.string.jclq_rf_danguan_title).toString();
		} else {
			return context.getString(R.string.jclq_rf_guoguan_title).toString();
		}
	}

	@Override
	public String getTypeTitle() {
		// TODO Auto-generated method stub
		return context.getString(R.string.jclq_dialog_rf_guoguan_title)
				.toString();
	}

	@Override
	public String getPlayType() {
		if (isDanguan) {
			return "J00006_0";
		} else {
			return "J00006_1";
		}
	}

}
