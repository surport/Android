package com.ruyicai.activity.info;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.palmdream.RuyicaiAndroid.wxapi.WXEntryActivity;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
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

public class LotInfoConcreteActivity extends Activity implements
		OnSeekBarChangeListener, HandlerMsg {
	private TextView news;
	private TextView titleTextView;
	private TextView informationtitle;
	private TextView informationtime;
	private int qishu = 1;
	private int beishu = 1;
	private TextView mtextqishu;
	private TextView mtextbeishu;
	private String sessionId;
	private String phonenum;
	private String userno;
	private MyHandler handler = new MyHandler(this);
	private ProgressDialog progressdialog;
	private TextView info;
	private String type;
	private Dialog dialog;
	private String betno;
	private String bet_code;
	private String lotno;
	private int zhushu = 1;
	private String titletype[] = { "彩民趣闻", "专家推荐", "足彩天地", "如意公告" };
	private LinearLayout fenxianglayout,parent;
	private boolean issharemove = false;
	ImageButton wangyi, xinlang;
	RWSharedPreferences shellRW,RW;
	String token, expires_in;
	// private Renren renren;
	private boolean isSinaTiaoZhuan = true;
	String tencent_token;
	String tencent_access_token_secret;
	private OAuthV1 tenoAuth;
	private Button caipiaozixun_sharebtn,tosinaweibo,totengxunweibo,toweixin,topengyouquan,tocancel;
	private PopupWindow popupWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.informationconcrete);
		init();
		initTextView();
		// renren=new Renren(this);
		shellRW = new RWSharedPreferences(LotInfoConcreteActivity.this,
				"addInfo");
		tenoAuth = new OAuthV1("null");
		tenoAuth.setOauthConsumerKey(Constants.kAppKey);
		tenoAuth.setOauthConsumerSecret(Constants.kAppSecret);
	}

	/**
	 * 初始化组件
	 */
	public void init() {
		RW = new RWSharedPreferences(LotInfoConcreteActivity.this, "shareweixin");
		news = (TextView) findViewById(R.id.webview);
		news.setTextColor(Color.BLACK);
		titleTextView = (TextView) findViewById(R.id.join_hall_text_title);
		informationtitle = (TextView) findViewById(R.id.informationtitle);
		informationtime = (TextView) findViewById(R.id.informationtime);
		Button imgRetrun = (Button) findViewById(R.id.join_hall_img_return);
		imgRetrun.setBackgroundResource(R.drawable.returnselecter);
		// ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		initSharePopWindow();
		parent = (LinearLayout) this.findViewById(R.id.linearlayout_caipiaozixun);
		caipiaozixun_sharebtn=(Button) this.findViewById(R.id.caipiaozixun_sharebtn);
		caipiaozixun_sharebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow != null) {
					popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
				}
			}
		});
		
		
		

		
	}

	private void initSharePopWindow() {
		View contentView=getLayoutInflater().inflate(R.layout.share_popwindow, null);
		tosinaweibo=(Button) contentView.findViewById(R.id.tosinaweibo);
		totengxunweibo=(Button) contentView.findViewById(R.id.totengxunweibo);
		toweixin=(Button) contentView.findViewById(R.id.toweixin);
		topengyouquan=(Button) contentView.findViewById(R.id.topengyouquan);
		tocancel=(Button) contentView.findViewById(R.id.tocancel);
		
		
   	    popupWindow=new PopupWindow(contentView, ViewGroup.LayoutParams.FILL_PARENT,   //得到pop对象,并设置该pop的样子和宽高
   			ViewGroup.LayoutParams.WRAP_CONTENT);
   	    popupWindow.setFocusable(true);
   	    popupWindow.setBackgroundDrawable(new BitmapDrawable());//当点击空白处时，pop会关掉
   	   
   	    tosinaweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				oauthOrShare();
				closePopWindow();
			}
		});
		totengxunweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tenoauth();
				closePopWindow();
			}
		});
		toweixin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/**
				 * 分享到微信
				 */
				toShareWeiXin();
				closePopWindow();
			}
		});
		topengyouquan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/**
				 * 分享到朋友圈
				 */
				toPengYouQuan();
				closePopWindow();
			}
		});
		tocancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closePopWindow();
			}
		});
		
	}
	
	protected void toPengYouQuan() {
	       RW.putStringValue("weixin_pengyou", "topengyouquan");
			Intent intent = new Intent(LotInfoConcreteActivity.this,
					WXEntryActivity.class);
			intent.putExtra("sharecontent", Constants.shareContent);
			startActivity(intent);
			
		}

		protected void toShareWeiXin() {
			RW.putStringValue("weixin_pengyou", "toweixin");
			Intent intent = new Intent(LotInfoConcreteActivity.this,
					WXEntryActivity.class);
			intent.putExtra("sharecontent", Constants.shareContent);
			startActivity(intent);	
			
		}

		private void closePopWindow(){
			if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
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
		weibo.authorize(LotInfoConcreteActivity.this, new AuthDialogListener());
	}

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
			Intent intent = new Intent(LotInfoConcreteActivity.this,
					OAuthV1AuthorizeWebView.class);// 创建Intent，使用WebView让用户授权
			intent.putExtra("oauth", tenoAuth);
			startActivityForResult(intent, 1);
		} else {
			tenoAuth.setOauthToken(tencent_token);
			tenoAuth.setOauthTokenSecret(tencent_access_token_secret);
			Intent intent = new Intent(LotInfoConcreteActivity.this,
					TencentShareActivity.class);
			intent.putExtra("tencent", Constants.shareContent);
			intent.putExtra("oauth", tenoAuth);
			startActivity(intent);
		}
	}

	private void initAccessToken(String token, String expires_in) {
		Token accessToken = new Token(token, Weibo.getAppSecret());
		accessToken.setExpiresIn(expires_in);
		Weibo.getInstance().setAccessToken(accessToken);
		share2weibo(Constants.shareContent);
		if (isSinaTiaoZhuan) {
			Intent intent = new Intent();
			intent.setClass(LotInfoConcreteActivity.this, ShareActivity.class);
			startActivity(intent);
		}
	}

	private void share2weibo(String content) {
		Weibo weibo = Weibo.getInstance();
		weibo.share2weibo(this, weibo.getAccessToken().getToken(), weibo
				.getAccessToken().getSecret(), content, "");
	}

	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			PublicMethod.myOutLog("token111",
					"zhiqiande" + shellRW.getStringValue("token"));
			PublicMethod.myOutLog("onComplete", "12131321321321");
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

	/**
	 * 显示内容
	 */
	public void initTextView() {
		Bundle bundle = getIntent().getExtras();
		String newsContent = bundle.getString("content");
		String title = bundle.getString("title");
		String time = bundle.getString("time");
		String url = bundle.getString("url");
		Constants.shareContent = "#如意彩# @如意彩" + title + "详情：" + url;
		Constants.source = "1";
		informationtime.setHint(time);
		int typetitle = 0;
		typetitle = bundle.getInt("type");
		if (typetitle != 0) {
			switch (typetitle) {
			case 1:
				titleTextView.setText(titletype[0]);
				break;
			case 2:
				titleTextView.setText(titletype[1]);
				break;
			case 3:
				titleTextView.setText(titletype[2]);
				break;
			case 4:
				titleTextView.setText(titletype[3]);
				break;
			default:
				break;
			}
		}

		informationtitle.setText(title);
		ContentList contentList1 = new ContentList();
		contentList1.setContent(newsContent);
		List<String> contentList = null;
		contentList = contentList1.getContentList();
		news.setMovementMethod(LinkMovementMethod.getInstance());
		SpannableStringBuilder sb = new SpannableStringBuilder();
		// 解析字符串。带"{}"的加点击事件
		for (Iterator<String> iterator = contentList.iterator(); iterator
				.hasNext();) {
			String str = iterator.next();
			final String view_code;
			if (str.startsWith("{")) {
				try {
					// Log.v("strconten", str);
					JSONObject obj = new JSONObject(str);
					lotno = obj.getString("lotno");
					if (lotno.equals("T01006") || lotno.equals("T01005")
							|| lotno.equals("T01004") || lotno.equals("T01003")) {
						zhushu = Integer.valueOf(obj.getString("footzhushu"));
					}
					type = obj.getString("lottype");
					bet_code = obj.getString("bet_code");
					view_code = obj.getString("view_code");
					betno = Controller.getInstance(LotInfoConcreteActivity.this).toNetIssue(lotno);
					SpannableStringBuilder strsp = new SpannableStringBuilder(
							view_code);
					ThrowintoSpan span = new ThrowintoSpan(
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									// Log.v("do", "onclick");
									LayoutInflater inflater = (LayoutInflater) LotInfoConcreteActivity.this
											.getSystemService(LAYOUT_INFLATER_SERVICE);
									View view = inflater
											.inflate(
													R.layout.informationthrowintodialog,
													null);
									info = (TextView) view
											.findViewById(R.id.alert_dialog_touzhu_text_one);
									TextView zhuma = (TextView) view
											.findViewById(R.id.alert_dialog_touzhu_text_two);
									info.setText("彩票种类:" + type + "\n" + "期号:"
											+ "第" + betno + "期" + "\n" + "注数:"
											+ zhushu * beishu + "\n" + "倍数:"
											+ beishu + "" + "\n" + "追号:"
											+ (qishu - 1) + "期" + "\n" + "金额:"
											+ 2 * zhushu * beishu + "元" + "\n"
											+ "冻结金额:" + 2 * zhushu * beishu
											* (qishu - 1) + "元");
									zhuma.setText("注码:" + view_code);
									mtextqishu = (TextView) view
											.findViewById(R.id.buy_zixuan_text_qishu);
									mtextbeishu = (TextView) view
											.findViewById(R.id.buy_zixuan_text_beishu);
									SeekBar qishubar = (SeekBar) view
											.findViewById(R.id.buy_zixuan_seek_qishu);
									SeekBar beishubar = (SeekBar) view
											.findViewById(R.id.buy_zixuan_seek_beishu);
									beishubar
											.setOnSeekBarChangeListener(LotInfoConcreteActivity.this);
									beishubar.setProgress(beishu);
									qishubar.setOnSeekBarChangeListener(LotInfoConcreteActivity.this);
									qishubar.setProgress(qishu);
									mtextbeishu.setText("" + beishu);
									mtextqishu.setText("" + qishu);
									LotInfoConcreteActivity.this
											.setSeekWhenAddOrSub(
													view,
													R.id.buy_zixuan_img_subtract_beishu,
													-1, beishubar, true);
									LotInfoConcreteActivity.this
											.setSeekWhenAddOrSub(
													view,
													R.id.buy_zixuan_img_add_beishu,
													1, beishubar, true);
									LotInfoConcreteActivity.this
											.setSeekWhenAddOrSub(
													view,
													R.id.buy_zixuan_img_subtract_qihao,
													-1, qishubar, false);
									LotInfoConcreteActivity.this
											.setSeekWhenAddOrSub(
													view,
													R.id.buy_zixuan_img_add_qishu,
													1, qishubar, false);

									dialog = new Dialog(
											LotInfoConcreteActivity.this,
											R.style.dialog);
									dialog.show();
									dialog.setContentView(view);

									Button ok = (Button) view
											.findViewById(R.id.alert_dialog_touzhu_button_ok);
									Button cannel = (Button) view
											.findViewById(R.id.alert_dialog_touzhu_button_cancel);
									final BetAndGiftPojo pojo = new BetAndGiftPojo();
									ok.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											RWSharedPreferences pre = new RWSharedPreferences(
													LotInfoConcreteActivity.this,
													"addInfo");
											sessionId = pre
													.getStringValue("sessionid");
											phonenum = pre
													.getStringValue("phonenum");
											userno = pre
													.getStringValue("userno");
											if (sessionId.equals("")) {
												// toLogin = true;
												Intent intentSession = new Intent(
														LotInfoConcreteActivity.this,
														UserLogin.class);
												startActivityForResult(
														intentSession, 0);
											} else {
												pojo.setSessionid(sessionId);
												pojo.setPhonenum(phonenum);
												pojo.setUserno(userno);
												pojo.setBettype("bet");
												pojo.setLotmulti("" + beishu);// lotmulti
																				// 倍数
																				// 投注的倍数
												pojo.setBatchnum("" + qishu);
												pojo.setSellway("0");
												pojo.setLotno(lotno);
												String bet_codetouzhu = "";
												bet_codetouzhu = PublicMethod
														.getzhumainfo(lotno,
																beishu,
																bet_code);
												pojo.setBet_code(bet_codetouzhu);
												pojo.setAmount("" + 200
														* zhushu * beishu);
												pojo.setInfoway("1");// 通过咨询投注标识
												LotInfoConcreteActivity.this
														.betting(pojo);
											}
										}
									});
									cannel.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									});
								}
							});

					strsp.setSpan(span, 0, view_code.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					sb.append(strsp);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				sb.append(str);
			}
		}
		// spstr.setSpan(span,s1.length(),s1.length()+s4.length(),
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		news.setText(sb);

	}

	/**
	 * 
	 * @author Administrator 特定文字点击事件
	 */
	public class ThrowintoSpan extends ClickableSpan {

		private final OnClickListener listener;

		public ThrowintoSpan(View.OnClickListener listener) {
			this.listener = listener;
		}

		@Override
		public void onClick(View view) {
			listener.onClick(view);
		}
	}

	/**
	 * 投注方法
	 */

	public void betting(final BetAndGiftPojo pojo) {
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(pojo);
				try {
					JSONObject obj = new JSONObject(str);
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					handler.handleMsg(error, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}

		});
		t.start();

	}

	/**
	 * fqc edit 添加一个参数 isBeiShu 来判断当前是倍数还是期数 。
	 * 
	 * @param idFind
	 * @param iV
	 * @param isAdd
	 * @param mSeekBar
	 * @param isBeiShu
	 */
	protected void setSeekWhenAddOrSub(View view, int idFind, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) view.findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						mSeekBar.setProgress(++beishu);
					} else {
						mSeekBar.setProgress(--beishu);
					}
					beishu = mSeekBar.getProgress();
				} else {
					if (isAdd == 1) {
						mSeekBar.setProgress(++qishu);
					} else {
						mSeekBar.setProgress(--qishu);
					}
					qishu = mSeekBar.getProgress();
				}
			}
		});
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {
		case R.id.buy_zixuan_seek_beishu:
			beishu = iProgress;
			mtextbeishu.setText("" + beishu);
			info.setText("彩票种类:" + type + "\n" + "期号:" + "第" + betno + "期"
					+ "\n" + "注数:" + zhushu * beishu + "\n" + "倍数:" + beishu
					+ "" + "\n" + "追号:" + (qishu - 1) + "期" + "\n" + "金额:"
					+ 2 * zhushu * beishu + "元" + "\n" + "冻结金额:" + 2 * zhushu
					* beishu * (qishu - 1) + "元");
			break;
		case R.id.buy_zixuan_seek_qishu:
			if (lotno.equals("T01006") || lotno.equals("T01005")
					|| lotno.equals("T01004") || lotno.equals("T01003")) {
				Toast.makeText(LotInfoConcreteActivity.this, "足彩没有追期",
						Toast.LENGTH_SHORT);
			} else {
				qishu = iProgress;
				mtextqishu.setText("" + qishu);
				info.setText("彩票种类:" + type + "\n" + "期号:" + "第" + betno + "期"
						+ "\n" + "注数:" + zhushu * beishu + "\n" + "倍数:"
						+ beishu + "" + "\n" + "追号:" + (qishu - 1) + "期" + "\n"
						+ "金额:" + 2 * zhushu * beishu + "元" + "\n" + "冻结金额:"
						+ 2 * zhushu * beishu * (qishu - 1) + "元");
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
		}
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		return null;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		dialog.dismiss();
		PublicMethod.showDialog(LotInfoConcreteActivity.this);
	}

	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	private String formatTitle(String titleStr) {
		String formattedStr = "";
		if (titleStr.length() > 8) {
			formattedStr = titleStr.substring(0, 8).toString() + "……";
		} else {
			formattedStr = titleStr;
		}
		return formattedStr;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
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

				Intent intent = new Intent(LotInfoConcreteActivity.this,
						TencentShareActivity.class);
				intent.putExtra("tencent", Constants.shareContent);
				intent.putExtra("oauth", tenoAuth);
				startActivity(intent);
			}
		}
	}
}
