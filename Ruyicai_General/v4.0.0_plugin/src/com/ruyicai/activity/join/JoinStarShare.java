/**
 * 
 */
package com.ruyicai.activity.join;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.OrderPrizeDiaog;
import com.ruyicai.activity.more.MoreActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.dialog.LogOutDialog;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.tencent.weibo.oauthv1.OAuthV1;
import com.tencent.weibo.oauthv1.OAuthV1Client;
import com.tencent.weibo.webview.OAuthV1AuthorizeWebView;
import com.third.share.ShareActivity;
import com.third.share.Token;
import com.third.share.Weibo;
import com.third.share.WeiboDialogListener;
import com.third.tencent.TencentShareActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 更多界面
 * 
 * @author Administrator
 * 
 */
public class JoinStarShare extends Activity implements HandlerMsg {

	// private CompanyInfo companyInfo = new CompanyInfo(this);//公司简介
	private Context context;
	private ProgressDialog progressdialog;
	private RelativeLayout relativeLayout;
	private boolean isSinaTiaoZhuan = true;
	public static int iQuitFlag = 0; // 代表退出
	MyHandler handler = new MyHandler(this);// 自定义handler
	String textStr;
	ProgressDialog pBar;
	boolean[] isOrderPrize = new boolean[8];
	RWSharedPreferences shellRW;
	LogOutDialog logOutDialog;

	String token, expires_in;

	int returnType = 0;// 1为分享页面的返回参数，0为本地更多
	OrderPrizeDiaog orderPrizeDialog;// 开奖订阅公共类

	RelativeLayout kaijiangdingyue, personidset;// 设置界面 开奖订阅和个人帐号设置
	Button auto_login_set;// 自动登录设置
	String tencent_token, tencent_access_token_secret;
	private OAuthV1 tenoAuth; // Oauth鉴权所需及所得信息的封装存储单元
	RelativeLayout sharerenren, sharesina, sharetecent, sharetomsg;// 分享界面的几个RelativeLayout
			// private Renren renren;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		shellRW = new RWSharedPreferences(JoinStarShare.this, "addInfo");
		orderPrizeDialog = new OrderPrizeDiaog(shellRW, JoinStarShare.this);
		context = this;
		tenoAuth = new OAuthV1("null");
		tenoAuth.setOauthConsumerKey(Constants.kAppKey);
		tenoAuth.setOauthConsumerSecret(Constants.kAppSecret);
		showShareView();
		Constants.shareContent = "Hi，我刚使用了如意彩手机客户端买彩票，很方便呢！"
				+ "你也试试吧，彩票随身投，大奖时时有！中奖了记的要请客啊！地址:http://wap.ruyicai.com/w/client/download.jspx";
		Constants.source = "5";

	}

	/**
	 * 跳转到分享页面
	 */
	private void showShareView() {
		setContentView(R.layout.joinsharelayout);
		// renren = new Renren(this);
		returnType = 1;
		sharesina = (RelativeLayout) findViewById(R.id.tableRow_sharetosina);
		sharetecent = (RelativeLayout) findViewById(R.id.tableRow_sharetotecent);
		sharerenren = (RelativeLayout) findViewById(R.id.tableRow_sharetorenren);
		sharetomsg = (RelativeLayout) findViewById(R.id.tableRow_sharetomsg);
		sharesina.setOnClickListener(moreActivityListener);
		sharetecent.setOnClickListener(moreActivityListener);
		sharerenren.setOnClickListener(moreActivityListener);
		sharetomsg.setOnClickListener(moreActivityListener);
	}

	OnClickListener moreActivityListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tableRow_sharetosina:
				oauthOrShare();
				break;
			case R.id.tableRow_sharetorenren:
				tenoauth();
				break;
			case R.id.tableRow_sharetomsg:
				shareToMsg();
				break;
			}
		}
	};

	public void tenoauth() {
		tencent_token = shellRW.getStringValue("tencent_token");
		tencent_access_token_secret = shellRW
				.getStringValue("tencent_access_token_secret");
		if (tencent_token.equals("") && tencent_access_token_secret.equals("")) {
			try {
				tenoAuth = OAuthV1Client.requestToken(tenoAuth);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent(JoinStarShare.this,
					OAuthV1AuthorizeWebView.class);// 创建Intent，使用WebView让用户授权
			intent.putExtra("oauth", tenoAuth);
			startActivityForResult(intent, 1);
		} else {
			tenoAuth.setOauthToken(tencent_token);
			tenoAuth.setOauthTokenSecret(tencent_access_token_secret);
			Intent intent = new Intent(JoinStarShare.this,
					TencentShareActivity.class);
			intent.putExtra("tencent", Constants.shareContent);
			intent.putExtra("oauth", tenoAuth);
			startActivity(intent);
		}
	}

	private void shareToMsg() {
		Uri smsToUri = Uri.parse("smsto:");// 联系人地址
		Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,
				smsToUri);
		mIntent.putExtra("sms_body", Constants.shareContent);// 短信的内容
		startActivity(mIntent);
	}

	private void oauthOrShare() {
		token = shellRW.getStringValue("token");
		expires_in = shellRW.getStringValue("expires_in");
		if (token.equals("")) {
			oauth();
		} else {
			isSinaTiaoZhuan = true;
			initAccessToken(token, expires_in);
		}
	}

	private void oauth() {

		Weibo weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		// Oauth2.0
		// 隐式授权认证方式
		weibo.setRedirectUrl(Constants.CONSUMER_URL);// 此处回调页内容应该替换为与appkey对应的应用回调页
		// 对应的应用回调页可在开发者登陆新浪微博开发平台之后，
		// 进入我的应用--应用详情--应用信息--高级信息--授权设置--应用回调页进行设置和查看，
		// 应用回调页不可为空
		weibo.authorize(JoinStarShare.this, new AuthDialogListener());
	}

	/**
	 * 切换view
	 * 
	 */
	public void switchView(View view) {
		setContentView(view);
	}

	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			return progressdialog;
		}
		}
		return null;
	}

	/**
	 * 获得当前界面context
	 */
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	public void dismissDialog() {
		// TODO Auto-generated method stub
		progressdialog.dismiss();
	}

	/**
	 * 显示网络连接框
	 */
	public void showDialog() {
		// TODO Auto-generated method stub
		showDialog(0);
	}

	/**
	 * 返回码处理方法
	 */
	public void errorCode_0000() {
		// TODO Auto-generated method stub

	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_000000()
	 */
	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub

	}

	/**
	 * 从上一个activity返回当前activity执行的方法
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1) {
			if (resultCode == OAuthV1AuthorizeWebView.RESULT_CODE) {
				// 从返回的Intent中获取验证码
				tenoAuth = (OAuthV1) data.getExtras().getSerializable("oauth");
				try {
					tenoAuth = OAuthV1Client.accessToken(tenoAuth);
					/*
					 * 注意：此时oauth中的Oauth_token和Oauth_token_secret将发生变化，用新获取到的
					 * 已授权的access_token和access_token_secret替换之前存储的未授权的request_token
					 * 和request_token_secret.
					 */
					tencent_token = tenoAuth.getOauthToken();
					tencent_access_token_secret = tenoAuth
							.getOauthTokenSecret();
					shellRW.putStringValue("tencent_token", tencent_token);
					shellRW.putStringValue("tencent_access_token_secret",
							tencent_access_token_secret);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(JoinStarShare.this,
						TencentShareActivity.class);
				intent.putExtra("tencent", Constants.shareContent);
				intent.putExtra("oauth", tenoAuth);
				startActivity(intent);

			}
		}
	}

	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			// PublicMethod.myOutLog("token111",
			// "zhiqiande"+shellRW.getStringValue("token"));
			// PublicMethod.myOutLog("onComplete", "12131321321321");
			String token = values.getString("access_token");
			PublicMethod.myOutLog("token", token);
			String expires_in = values.getString("expires_in");
			shellRW.putStringValue("token", token);
			shellRW.putStringValue("expires_in", expires_in);
			// is_sharetosinaweibo.setBackgroundResource(R.drawable.on);
			initAccessToken(token, expires_in);
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}
	}

	private void initAccessToken(String token, String expires_in) {
		Token accessToken = new Token(token, Weibo.getAppSecret());
		accessToken.setExpiresIn(expires_in);
		Weibo.getInstance().setAccessToken(accessToken);
		share2weibo(Constants.shareContent);
		if (isSinaTiaoZhuan) {
			Intent intent = new Intent();
			intent.setClass(JoinStarShare.this, ShareActivity.class);
			startActivity(intent);
		}
	}

	private void share2weibo(String content) {
		Weibo weibo = Weibo.getInstance();
		weibo.share2weibo(this, weibo.getAccessToken().getToken(), weibo
				.getAccessToken().getSecret(), content, "");
	}
}
