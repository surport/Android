package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.palmdream.RuyicaiAndroid.wxapi.WXEntryActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.NoticePrizeDetailInterface;
import com.ruyicai.util.JsonTools;
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
 * 
 * @author win
 * 
 */
public abstract class LotnoDetailView {
	int LOTLABEL;
	LinearLayout ballLinear;
	Activity context;
	ProgressDialog progress;
	Handler handler;
	public View view = null;
	public boolean isDialog = true;
	RWSharedPreferences shellRW;
	String token, expires_in;
	ImageButton wangyi, xinlang;
	private boolean issharemove = false;
	private boolean isSinaTiaoZhuan = true;
	public String lotno;
	LinearLayout fenxianglayout;
	String tencent_token;
	String tencent_access_token_secret;
	private OAuthV1 tenoAuth;
	public static String shareString = "";
	private Button notice_detail_btn, tosinaweibo, totengxunweibo,toweixin,topeingyouquan, tocancel;
	private PopupWindow popupWindow;
	private RWSharedPreferences RW;
	private RelativeLayout parent;
	

	public LotnoDetailView(Activity context, String lotno, String batchcode,
			ProgressDialog progress, Handler handler, boolean isDialog) {
		LayoutInflater factory = LayoutInflater.from(context);
		view = factory.inflate(R.layout.notice_detail, null);
		this.isDialog = isDialog;
		this.handler = handler;
		this.context = context;
		this.progress = progress;
		this.lotno = lotno;
		tenoAuth = new OAuthV1("null");
		tenoAuth.setOauthConsumerKey(Constants.kAppKey);
		tenoAuth.setOauthConsumerSecret(Constants.kAppSecret);
		shellRW = new RWSharedPreferences(context, "addInfo");
		isTopVisable(isDialog);
		initLotnoDetailViewWidget();
		getPrizeDetalilNet(lotno, batchcode);
		Constants.source = "2";
		initfenxianglayout();
	}

	public void isTopVisable(boolean isVisable) {
		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.notice_detail_title);
		if (isVisable) {
			layout.setVisibility(RelativeLayout.VISIBLE);
		} else {
			layout.setVisibility(RelativeLayout.GONE);
		}
	}

	/**
	 * 初始化中奖详情布局中widget（设置id）
	 */
	abstract void initLotnoDetailViewWidget();

	/**
	 * 设置中奖详情布局中widget的属性和值
	 * 
	 * @param lotno
	 *            彩种编号
	 * @param batchcode
	 *            彩种期号
	 */

	public abstract void initLotonoView(JSONObject json) throws JSONException;

	/**
	 * 获取分享信息
	 * **/
	public abstract String getShareString();

	/**
	 * 分享
	 * **/
	public void initfenxianglayout() {
		
		RW=new RWSharedPreferences(context, "shareweixin");
		initSharePopWindow();	
		notice_detail_btn = (Button)view.findViewById(R.id.notice_detail_btn);
		parent = (RelativeLayout) view.findViewById(R.id.relativetopone);
		notice_detail_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initSharePopWindow();
				if (popupWindow != null) {
					popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
				}
			}
		});
		
	/*	
		wangyi = (ImageButton) view.findViewById(R.id.join_detail_img_buy2);
		xinlang = (ImageButton) view.findViewById(R.id.join_detail_img_buy3);
		wangyi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(context, "renren");// BY贺思明
															// 最新开奖页点击“分享至人人网”。
				tenoauth();
			}
		});
		xinlang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(context, "xinlang");// BY贺思明
															// 最新开奖页点击“分享至新浪微博”。
				oauthOrShare();

			}
		});
		fenxianglayout = (LinearLayout) view.findViewById(R.id.LinearLayout10);
		setFenXiangLayoutPosition((int) (PublicMethod.getDisplayWidth(context) - PublicMethod
				.getPxInt(320, context) * 0.17f));

		fenxianglayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (issharemove) {
					MobclickAgent.onEvent(context, "fenxiang");// BY贺思明
																// 最新开奖页点击“分享”展开按钮。
					TranslateAnimation anim = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0.83f,
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0.0f);
					anim.setDuration(500);
					anim.setFillEnabled(true);
					anim.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							setFenXiangLayoutPosition((int) (PublicMethod
									.getDisplayWidth(context) - fenxianglayout
									.getWidth() * 0.17f));
						}
					});
					fenxianglayout.startAnimation(anim);
					issharemove = false;
				} else {
					TranslateAnimation anim = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, -0.83f,
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0.0f);
					anim.setDuration(500);
					anim.setFillEnabled(true);
					anim.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							setFenXiangLayoutPosition(PublicMethod
									.getDisplayWidth(context)
									- fenxianglayout.getWidth());
						}
					});
					fenxianglayout.startAnimation(anim);
					issharemove = true;

				}

			}
		});
		*/
	}

	private void setFenXiangLayoutPosition(int leftMargin) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) fenxianglayout
				.getLayoutParams();
		lp.setMargins(leftMargin, 0, 0, 0);
		fenxianglayout.setLayoutParams(lp);
	}
	
	private void initSharePopWindow() {
		View contentView=context.getLayoutInflater().inflate(R.layout.share_popwindow, null);
		tosinaweibo=(Button) contentView.findViewById(R.id.tosinaweibo);
		totengxunweibo=(Button) contentView.findViewById(R.id.totengxunweibo);
		toweixin=(Button) contentView.findViewById(R.id.toweixin);
		topeingyouquan=(Button) contentView.findViewById(R.id.topengyouquan);
		tocancel=(Button) contentView.findViewById(R.id.tocancel);
		
		
   	    popupWindow=new PopupWindow(contentView, ViewGroup.LayoutParams.FILL_PARENT,   //得到pop对象,并设置该pop的样子和宽高
   			ViewGroup.LayoutParams.WRAP_CONTENT);
   	    popupWindow.setFocusable(true);
   	    popupWindow.setBackgroundDrawable(new BitmapDrawable());//当点击空白处时，pop会关掉
   	    //popupWindow.setAnimationStyle(R.style.share_animation);//通过此方法从styles.xml中得到pop的进入和退出效果	
   	   
   	    tosinaweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				oauthOrShare();
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				
			}
		});
		totengxunweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tenoauth();
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		toweixin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toWeiXin();
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		topeingyouquan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toPengYouQuan();
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		tocancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
	}
	
	protected void toPengYouQuan() {
		RW.putStringValue("weixin_pengyou", "topengyouquan");
		Intent intent = new Intent(context,
				WXEntryActivity.class);
		intent.putExtra("sharecontent",getShareString());
		context.startActivity(intent);
		
	}

	protected void toWeiXin() {
		RW.putStringValue("weixin_pengyou", "toweixin");
		Intent intent = new Intent(context,
				WXEntryActivity.class);
		intent.putExtra("sharecontent",getShareString());
		context.startActivity(intent);	
		
	}
	

	/**
	 * 分享
	 * **/
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

	/**
	 * 分享
	 * **/
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
		weibo.authorize(context, new AuthDialogListener());
	}

	public void tenoauth() {
		tencent_token = shellRW.getStringValue("tencent_token");
		tencent_access_token_secret = shellRW
				.getStringValue("tencent_access_token_secret");
		if (tencent_token.equals("") && tencent_access_token_secret.equals("")) {
			try {
				tenoAuth = OAuthV1Client.requestToken(tenoAuth);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(context, OAuthV1AuthorizeWebView.class);// 创建Intent，使用WebView让用户授权
			intent.putExtra("oauth", tenoAuth);
			context.startActivityForResult(intent, 1);
			shareString = getShareString();
		} else {
			tenoAuth.setOauthToken(tencent_token);
			tenoAuth.setOauthTokenSecret(tencent_access_token_secret);
			Intent intent = new Intent(context, TencentShareActivity.class);
			intent.putExtra("tencent", getShareString());
			intent.putExtra("oauth", tenoAuth);
			context.startActivity(intent);
		}
	}

	/**
	 * 分享
	 * **/
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
			initAccessToken(token, expires_in);
		}

		@Override
		public void onCancel() {
			Toast.makeText(context.getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 分享
	 * **/
	private void initAccessToken(String token, String expires_in) {
		Token accessToken = new Token(token, Weibo.getAppSecret());
		accessToken.setExpiresIn(expires_in);
		Weibo.getInstance().setAccessToken(accessToken);
		share2weibo(getShareString());
		if (isSinaTiaoZhuan) {
			Intent intent = new Intent();
			intent.setClass(context, ShareActivity.class);
			context.startActivity(intent);
		}
	}

	/**
	 * 分享
	 * **/
	private void share2weibo(String content) {
		Weibo weibo = Weibo.getInstance();
		weibo.share2weibo(context, weibo.getAccessToken().getToken(), weibo
				.getAccessToken().getSecret(), content, "");
	}

	public void getPrizeDetalilNet(final String lotno, final String batchcode) {
		progress.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject PrizeDetailJson = NoticePrizeDetailInterface
						.getInstance().getNoticePrizeDetail(lotno, batchcode);
				progress.dismiss();
				try {
					String errorcode = JsonTools
							.safeParseJSONObjectForValueIsString(
									PrizeDetailJson, "error_code", "");
					final String message = JsonTools
							.safeParseJSONObjectForValueIsString(
									PrizeDetailJson, "message", "");
					if (errorcode.equals("0000")) {
						initLotonoView((JSONObject) (PrizeDetailJson));
						handler.post(new Runnable() {
							public void run() {
								if (isDialog) {
									prizeDetailDialog(view);
								} else {
									context.setContentView(view);
								}
							}
						});// 获取成功
					} else {
						handler.post(new Runnable() {
							public void run() {
								Toast.makeText(context, message,
										Toast.LENGTH_LONG).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void prizeDetailDialog(View view) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		Button button = (Button) view.findViewById(R.id.notice_return_main);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		if (context != null && !context.isFinishing()) {
			dialog.show();
			dialog.getWindow().setContentView(view);
			//add by yejc 20130802
			dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		}
	}

}
