package com.ruyicai.activity.buy.jc.lq.view;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.explain.lq.JcLqExplainActivity;
import com.ruyicai.code.jc.lq.BasketDX;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;

/**
 * 大小分
 * 
 * @author Administrator
 * 
 */
public class DxView extends SfView {
	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		return basketDx.getOddsList(listInfo);
	}

	protected BasketDX basketDx;

	public DxView(Context context, BetAndGiftPojo betAndGift, Handler handler,
			LinearLayout layout, String type, boolean isdanguan,
			List<String> checkTeam) {
		super(context, betAndGift, handler, layout, type, isdanguan, checkTeam);
		basketDx = new BasketDX(context);
	}

	/**
	 * 跳转到分析界面
	 */
	public void trunExplain(String event) {
		JcLqExplainActivity.Lq_TYPE = JcLqExplainActivity.LQ_DX;
		Intent intent = new Intent(context, JcLqExplainActivity.class);
		intent.putExtra("event", event);
		context.startActivity(intent);
	}

	@Override
	public String getCode(String key, List<Info> listInfo) {
		return basketDx.getCode(key, listInfo);
	}

	@Override
	public String getLotno() {
		return Constants.LOTNO_JCLQ_DXF;
	}

	public void setDifferValue(JSONObject jsonItem, Info itemInfo)
			throws JSONException {
		// 大小分
		itemInfo.setBig(jsonItem.getString("g"));
		itemInfo.setSmall(jsonItem.getString("l"));
		itemInfo.setBasePoint(jsonItem.getString("basePoint"));
	}

	/**
	 * 初始化列表
	 */
	public void initListView(ListView listview, Context context,
			List<List> listInfo) {
		adapter = new JcInfoAdapter(context, listInfo, B_DX);
		listview.setAdapter(adapter);
	}

	@Override
	public String getTitle() {
		if (isDanguan) {
			return context.getString(R.string.jclq_dxf_danguan_title)
					.toString();

		} else {
			return context.getString(R.string.jclq_dxf_guoguan_title)
					.toString();
		}
	}

	@Override
	public String getTypeTitle() {
		return context.getString(R.string.jclq_dialog_dxf_guoguan_title)
				.toString();
	}

	public String getAlertCode(List<Info> listInfo) {
		String codeStr = "";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				codeStr += PublicMethod.stringToHtml(info.getWeeks() + " " + info.getTeamId(), 
						Constants.JC_TOUZHU_TITLE_TEXT_COLOR) + "  ";
				codeStr += info.getAway() + " vs " + info.getHome() + "(主)"+"<br>大小分：";
				if (info.isWin()) {
					codeStr += PublicMethod.stringToHtml("小", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				if (info.isFail()) {
					codeStr += PublicMethod.stringToHtml("大", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				if (info.isDan()) {
					codeStr += PublicMethod.stringToHtml("(胆)", Constants.JC_TOUZHU_TEXT_COLOR) + "  ";
				}
				codeStr += "<br>";
			}

		}
		return codeStr;
	}

	@Override
	public String getPlayType() {
		if (isDanguan) {
			return "J00008_0";
		} else {
			return "J00008_1";
		}
	}
}
