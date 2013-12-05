package com.ruyicai.activity.buy.jc.explain.zq;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.zc.FootBallBaseAdapter;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.ExplainInterface;
import com.ruyicai.util.PublicMethod;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 竞彩球队数据分析界面
 *
 */
public class JcExplainActivity extends BuyActivityGroup {
	protected String titleStr = "球队数据分析";
	protected String[] titles = null;
	protected String[] topTitles = { titleStr, titleStr, titleStr, titleStr, titleStr};
	protected Class[] allId = { ExplainListActivity.class,
			EuropeActivity.class, AsiaActivity.class , RecommendActivity.class};
	protected Handler handler = new Handler();
	protected String type = "dataAnalysis";
	public static JSONObject jsonObject;
	protected ProgressDialog progressdialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIssue(false);
		/**add by yejc 20130425 start*/
		event = getIntentInfo();
		if (FootBallBaseAdapter.LOTNO_ZC.equals(getIntent().getStringExtra(
				FootBallBaseAdapter.LOTNO_ZC))
		    || Constants.BEIJINGSINGLE.equals(Constants.currentTickType)) {
			titles = new String[3];
			String [] temp = { "分析", "欧指", "亚盘" };
			for (int i = 0; i < 3; i++) {
				titles[i] = temp[i];
			}
		} else {
			titles = new String[4];
			String[] temp  = { "分析", "欧指", "亚盘" , "推荐"};
			for (int i = 0; i < 4; i++) {
				titles[i] = temp[i];
			}
		}
		/**add by yejc 20130425 end*/
		setTitleText();
		getExplainNet(getIntentInfo(), type);
	}

	public void setTitleText() {
		title.setText(titleStr);
	}

	/**
	 * 得到当前页面的下标
	 */
	public String getIntentInfo() {
		Intent intent = getIntent();
		return intent.getStringExtra("event");
	}

	/**
	 * 获取分析
	 */
	public void getExplainNet(final String event, final String type) {
		progressdialog = UserCenterDialog.onCreateDialog(context);
		progressdialog.show();
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = ExplainInterface.getExplain(Constants.currentTickType,event, type);					
				try {
					jsonObject = new JSONObject(str);
					final String msg = jsonObject.getString("message");
					String error = jsonObject.getString("error_code");
					if (error.equals("0000")) {
						handler.post(new Runnable() {
							public void run() {
								init(titles, topTitles, allId);
								progressdialog.dismiss();
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								// TODO Auto-generated method stub
								progressdialog.dismiss();
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
					progressdialog.dismiss();
				}
			}

		});
		t.start();
	}

	/**
	 * 初始化组件
	 */
	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		imgIcon = (Button) findViewById(R.id.layout_main_img_return);
		imgIcon.setBackgroundResource(R.drawable.returnselecter);
		imgIcon.setText("返回");
		imgIcon.setWidth(PublicMethod.getPxInt(70, context));
		imgIcon.setVisibility(View.VISIBLE);
		// ImageView的返回事件
		imgIcon.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	public boolean getIsLuck() {
		return true;
	}
}
