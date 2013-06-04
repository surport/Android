package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.NoticePrizeDetailInterface;
import com.ruyicai.util.JsonTools;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.third.share.ShareActivity;
import com.third.share.Token;
import com.third.share.Weibo;
import com.third.share.WeiboDialogListener;
import com.third.sharetorenren.Renren;
import com.third.sharetorenren.StatusDemo;
/**
 * 
 * @author win
 *
 */
public abstract class LotnoDetailView {
	int LOTLABEL;
	LinearLayout ballLinear ;
	Activity context;
	ProgressDialog progress;
	Handler handler;
	public 	View	view = null;
	public boolean isDialog = true;
    RWSharedPreferences shellRW ;
    private Renren renren;
    String token,expires_in;
	ImageButton wangyi,xinlang;
    private boolean issharemove=false;
	private boolean isSinaTiaoZhuan = true;
	LinearLayout fenxianglayout;
	public LotnoDetailView(Activity context,String lotno,String batchcode,ProgressDialog progress,Handler handler,boolean isDialog) {
		LayoutInflater factory = LayoutInflater.from(context);
		view = factory.inflate(R.layout.notice_detail, null);
		this.isDialog = isDialog;
		this.handler = handler;
		this.context = context;
		this.progress = progress;
		renren=new Renren(context);
		shellRW = new RWSharedPreferences(context, "addInfo");
		isTopVisable(isDialog);
		initLotnoDetailViewWidget();
		getPrizeDetalilNet( lotno, batchcode);
		initfenxianglayout();
	}  
   public void isTopVisable(boolean isVisable){
	   RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.notice_detail_title);
	   if(isVisable){
		   layout.setVisibility(RelativeLayout.VISIBLE);
	   }else{
		   layout.setVisibility(RelativeLayout.GONE);
	   }
   }
	/**
	 * 初始化中奖详情布局中widget（设置id）
	 */
  abstract void initLotnoDetailViewWidget();
	/**
	 * 设置中奖详情布局中widget的属性和值
	 * @param lotno 彩种编号
	 * @param batchcode 彩种期号
	 */
	
  public abstract void initLotonoView(JSONObject json) throws JSONException ;
	
  /**获取分享信息
   * **/
  public   abstract String getShareString();
  
  /**分享
   * **/
  public void initfenxianglayout(){
	    wangyi=(ImageButton)view.findViewById(R.id.join_detail_img_buy2);
		xinlang=(ImageButton)view.findViewById(R.id.join_detail_img_buy3);
		wangyi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//		Toast.makeText(JoinDetailActivity.this, "网易分享", Toast.LENGTH_SHORT).show();	
				StatusDemo st=new StatusDemo();
		        st.publishStatusOneClick(context, renren);
			}
		});
		xinlang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(JoinDetailActivity.this, "新浪分享", Toast.LENGTH_SHORT).show();		
				oauthOrShare();
	
			}
		});
    fenxianglayout=(LinearLayout)view.findViewById(R.id.LinearLayout10);	
	fenxianglayout.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(issharemove){
				TranslateAnimation anim=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.83f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
				anim.setDuration(500);
//				anim.setFillAfter(true);
				anim.setFillEnabled(true);
		        anim.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
					   LinearLayout.LayoutParams lp =(LinearLayout.LayoutParams)fenxianglayout.getLayoutParams();
					   lp.setMargins(PublicMethod.getPxInt(265, context), 0, 0, 0);
		               fenxianglayout.setLayoutParams(lp);
					}
				});
		        fenxianglayout.startAnimation(anim);
		        issharemove=false;
			}else{
				TranslateAnimation anim=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, -0.83f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
				anim.setDuration(500);
//				anim.setFillAfter(true);
				anim.setFillEnabled(true);
		        anim.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
					   LinearLayout.LayoutParams lp =(LinearLayout.LayoutParams)fenxianglayout.getLayoutParams();
					   lp.setMargins(0, 0, 0, 0);
		               fenxianglayout.setLayoutParams(lp);
					}
				});
		        fenxianglayout.startAnimation(anim);
		        issharemove=true;
				
			}
			
		}
	});
  }
 /**分享
  * **/ 
	private void oauthOrShare(){
		token = shellRW.getStringValue("token");
		expires_in = shellRW.getStringValue("expires_in");
		if(token.equals("")){
			oauth();
		}else{
			isSinaTiaoZhuan = true;
			initAccessToken(token, expires_in);
		}
	}
	 /**分享
	  * **/ 	
	private void oauth(){

		Weibo weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		// Oauth2.0
		// 隐式授权认证方式
		weibo.setRedirectUrl(Constants.CONSUMER_URL);// 此处回调页内容应该替换为与appkey对应的应用回调页
		// 对应的应用回调页可在开发者登陆新浪微博开发平台之后，
		// 进入我的应用--应用详情--应用信息--高级信息--授权设置--应用回调页进行设置和查看，
		// 应用回调页不可为空
		weibo.authorize(context, new AuthDialogListener());
	}
  
	 /**分享
	  * **/ 
	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			PublicMethod.myOutLog("token111", "zhiqiande"+shellRW.getStringValue("token"));
			PublicMethod.myOutLog("onComplete", "12131321321321");
			String token = values.getString("access_token");
			PublicMethod.myOutLog("token", token);
			String expires_in = values.getString("expires_in");
			shellRW.putStringValue("token", token);
			shellRW.putStringValue("expires_in", expires_in);
//			is_sharetosinaweibo.setBackgroundResource(R.drawable.on);
			initAccessToken(token,expires_in);
		}


		@Override
		public void onCancel() {
			Toast.makeText(context.getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}
	}
	
	
	 /**分享
	  * **/ 
	private void initAccessToken(String token,String expires_in){
		Token accessToken = new Token(token, Weibo.getAppSecret());
		accessToken.setExpiresIn(expires_in);
		Weibo.getInstance().setAccessToken(accessToken);
		share2weibo(getShareString());
		if(isSinaTiaoZhuan){
			Intent intent = new Intent();
			intent.setClass(context, ShareActivity.class);
			context.startActivity(intent);
		}
	}
	 /**分享
	  * **/ 
	 private void share2weibo(String content)  {
	        Weibo weibo = Weibo.getInstance();
	        weibo.share2weibo(context, weibo.getAccessToken().getToken(), weibo.getAccessToken().getSecret(), content, "");
	    }
      public void getPrizeDetalilNet(final String lotno,final String batchcode){
	  progress.show();

	  new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			JSONObject PrizeDetailJson = NoticePrizeDetailInterface.getInstance().getNoticePrizeDetail(lotno, batchcode);
			progress.dismiss();
			try {
				String errorcode = JsonTools.safeParseJSONObjectForValueIsString(PrizeDetailJson, "error_code", "");
				final String message = JsonTools.safeParseJSONObjectForValueIsString(PrizeDetailJson, "message", "");
				if(errorcode.equals("0000")){
					initLotonoView((JSONObject)(PrizeDetailJson));
					handler.post(new Runnable() {
						public void run() {
							if(isDialog){
								prizeDetailDialog(view);
							}else{
								context.setContentView(view);
							}
						}
					});// 获取成功
				}else{
					handler.post(new Runnable() {
						public void run() {
							Toast.makeText(context,message, Toast.LENGTH_LONG).show();
						}
					});
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}).start();
  }
    
  
  
	public  void prizeDetailDialog(View view){
		final	AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.getWindow();
		Button button = (Button)view.findViewById(R.id.notice_return_main);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(view);
		
	}
	
}
