/**
 * 
 */
package com.ruyicai.activity.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.home.HomeActivity;
import com.ruyicai.activity.more.share.ShareActivity;
import com.ruyicai.activity.more.share.Token;
import com.ruyicai.activity.more.share.Weibo;
import com.ruyicai.activity.more.share.WeiboDialogListener;
import com.ruyicai.activity.more.sharetorenren.Renren;
import com.ruyicai.activity.more.sharetorenren.StatusDemo;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.dialog.LogOutDialog;
import com.ruyicai.dialog.MyDialogListener;
import com.ruyicai.dialog.UpdateDialog;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.interfaces.ReturnPage;
import com.ruyicai.net.newtransaction.SoftwareUpdateInterface;
import com.ruyicai.service.PrizeNotificationService;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 更多界面
 * @author Administrator
 *
 */
public class MoreActivity extends Activity implements ReturnPage,HandlerMsg, MyDialogListener{
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private TextView text;
	private static final String IICON = "IICON";
	private final static String TITLE = "TITLE"; /* 标题 */
//	private CompanyInfo  companyInfo = new CompanyInfo(this);//公司简介
	private Context context;
	private ProgressDialog progressdialog;
	private RelativeLayout relativeLayout;
	public static int iQuitFlag = 0; // 代表退出
	MyHandler handler = new MyHandler(this);//自定义handler
	String textStr;
    ProgressDialog pBar;
    boolean[] isOrderPrize = new boolean[8];
    RWSharedPreferences shellRW ;
    LogOutDialog logOutDialog;
    
    int returnType = 0;//1为分享页面的返回参数，0为本地更多
    
    RelativeLayout sharerenren,sharesina,sharetecent,sharetomsg;
    private Renren renren;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		shellRW = new RWSharedPreferences(MoreActivity.this, "addInfo");
		context = this;
//		initView();
		showMoreListView();
//		appc=(ApplicationContext)getApplication();
	}


	/**
	 *  “更多”选项列表
	 */
	public  void showMoreListView() {
		returnType = 0;
		setContentView(R.layout.ruyihelper_listview);
		relativeLayout = (RelativeLayout) findViewById(R.id.ruyihelper_listview_relative);
		relativeLayout.setVisibility(RelativeLayout.GONE);
		// 数据源
		list = getListForMoreAdapter();

		ListView listview = (ListView) findViewById(R.id.ruyihelper_listview_ruyihelper_id);

		// 适配器
		SimpleAdapter adapter = new SimpleAdapter(this, list,R.layout.ruyihelper_listview_icon_item, new String[] {
						TITLE, IICON }, new int[] { R.id.ruyihelper_icon_text,R.id.ruyihelper_iicon });

		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);

		/* 列表的点击后的背景 */
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				text = (TextView) view.findViewById(R.id.ruyihelper_icon_text);
				textStr = text.getText().toString();
//				relativeLayout.setVisibility(RelativeLayout.VISIBLE);
//				iQuitFlag = 10;//当前主界面下一级
				onClickListener(textStr);
			}

		};
		listview.setOnItemClickListener(clickListener);

	}
	/**
	 * 跳转到分享页面
	 */
	private void showShareView(){
		setContentView(R.layout.ruyicai_share);
		renren = new Renren(this);
		returnType = 1;
		sharesina = (RelativeLayout)findViewById(R.id.tableRow_sharetosina);
		sharetecent = (RelativeLayout)findViewById(R.id.tableRow_sharetotecent);
		sharerenren = (RelativeLayout)findViewById(R.id.tableRow_sharetorenren);
		sharetomsg = (RelativeLayout)findViewById(R.id.tableRow_sharetomsg);
		sharesina.setOnClickListener(shareListener);
		sharetecent.setOnClickListener(shareListener);
		sharerenren.setOnClickListener(shareListener);
		sharetomsg.setOnClickListener(shareListener);
	}
	
	OnClickListener shareListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tableRow_sharetosina:
				oauthOrShare();
				break;
			case R.id.tableRow_sharetotecent:
				
				break;
			case R.id.tableRow_sharetorenren:
				StatusDemo.publishStatusOneClick(MoreActivity.this, renren);
				break;
			case R.id.tableRow_sharetomsg:
				shareToMsg();
				break;
			}
		}
	};
	
	
	private  void	shareToMsg(){
		Uri smsToUri = Uri.parse("smsto:");// 联系人地址
		Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,smsToUri);
		mIntent.putExtra("sms_body", Constants.shareContent);// 短信的内容
		startActivity(mIntent);
	}
	
	private static final String CONSUMER_KEY = "2143826468";// 替换为开发者的appkey，例如"1646212960";
	private static final String CONSUMER_SECRET = "f3199c4912660f1bcbdee7cfc37c636e";// 替换为开发者的appkey，例
	private void oauthOrShare(){
		Weibo weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);
		// Oauth2.0
		// 隐式授权认证方式
		weibo.setRedirectUrl("http://wap.ruyicai.com/w/client/download.jspx");// 此处回调页内容应该替换为与appkey对应的应用回调页
		// 对应的应用回调页可在开发者登陆新浪微博开发平台之后，
		// 进入我的应用--应用详情--应用信息--高级信息--授权设置--应用回调页进行设置和查看，
		// 应用回调页不可为空

		weibo.authorize(MoreActivity.this, new AuthDialogListener());
	}

	/**
	 * 列表点击实现方法
	 * @param str
	 */
	public void onClickListener(String str){
		/* 检测更新 */
		if (getString(R.string.menu_checkupdate).equals(str)) {
			if (HomeActivity.softwareErrorCode.equals("true")) {
				MainUpdate update = new MainUpdate(MoreActivity.this,new Handler(),HomeActivity.softwareurl, HomeActivity.softwaremessageStr, HomeActivity.softwaretitle);
				update.showDialog();
				update.createMyDialog();
			}else if(HomeActivity.softwareErrorCode.equals("not")){
				isUpdateNet();
			}else{
				Toast.makeText(this, "当前已经是最新版本！", Toast.LENGTH_SHORT);
			}
		}
		/* 开奖订阅 */
		if (getString(R.string.menu_orderprize).equals(str)) {
			orderPrizeDialog().show();
 		}
		/* 分享 */
		if ("分享".equals(str)) {
//			switchView(systemSet.showView());
			showShareView();
		}
		/* 我要反馈*/
		if (getString(R.string.menu_feedback).equals(str)) {
			Intent intent1 = new Intent(MoreActivity.this, FeedBack.class);
			startActivity(intent1);		}
		/* 用户帮助 */
		if (getString(R.string.menu_help).equals(str)) {
			Intent intent2 = new Intent(MoreActivity.this, HelpCenter.class);
			startActivity(intent2);
		}
		/* 关于授权*/
		if (getString(R.string.menu_about).equals(str)) {
			Intent intent3 = new Intent(MoreActivity.this, CompanyInfo.class);
			startActivity(intent3);
		}
		/* 注销登陆*/
		if (getString(R.string.menu_userexit).equals(str)) {
            logOutDialog = LogOutDialog.createDialog(this);
			logOutDialog.setOnClik(this);
		}
		/*切换用户*/
		if(getString(R.string.more_updateuser).equals(str)){
			Intent intent = new Intent(this, UserLogin.class);
			startActivity(intent);
		}
	}

	/**
	 *  获得“更多”列表适配器的数据源
	 * @return
	 */
	protected List<Map<String, Object>> getListForMoreAdapter() {

		String[] titles = {
				getString(R.string.menu_checkupdate),
				getString(R.string.menu_orderprize),
                "分享"				,
				getString(R.string.menu_feedback),
				getString(R.string.menu_help),
				getString(R.string.menu_about),
				getString(R.string.menu_userexit),
				getString(R.string.more_updateuser)
				};
		int it = R.drawable.xiangyou;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TITLE, titles[i]);
			map.put(IICON, it);

			list.add(map);

		}

		return list;
	}
    /**
     * 切换view
     * 
     */
	public void switchView(View view){
		setContentView(view);
	}

	/**
	 *  网络连接框
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
    /**
     * 返回到当前界面
     */
	public void returnMain() {
		// TODO Auto-generated method stub
		showMoreListView();
	}
    /**
     *  取消网络连接框
     */
	public void dismissDialog() {
		// TODO Auto-generated method stub
		progressdialog.dismiss();
	}
	 /**
     *  显示网络连接框
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

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	/* (non-Javadoc)
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
		if (renren != null) {
			renren.authorizeCallback(requestCode, resultCode, data);
		}
		}
    /**
     * 重写回建
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		   case 4:
	    	  if(returnType == 0){
	    		  ExitDialogFactory.createExitDialog(this);	  
	    	  }else{
	    		  showMoreListView();
	    	  }
			  break;
		}
		return false;
	}
	
	
	/**
	 * 联网检测新版本
	 * 
	 */
	public void isUpdateNet(){
		pBar = UserCenterDialog.onCreateDialog(this);
		pBar.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONObject obj;
				try {
				obj = new JSONObject(SoftwareUpdateInterface.getInstance().softwareupdate(null));
				pBar.dismiss();
				String softwareErrorCode = obj.getString("errorCode");
				if (softwareErrorCode.equals("true")) {
					// 需要升级,设置升级相关字段
					final String softwareurl = obj.getString("updateurl");
					final String softwaretitle = obj.getString("title");
					final String softwaremessageStr = obj.getString("message");
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							MainUpdate update = new MainUpdate(MoreActivity.this,new Handler(),softwareurl, softwaremessageStr, softwaretitle);
							update.showDialog();
							update.createMyDialog();
						}
					});
				}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	AlertDialog orderPrizeDialog(){
		getOrderPrize();
		return new AlertDialog.Builder(MoreActivity.this)
         .setTitle("开奖订阅")
         .setMultiChoiceItems(R.array.lotttery_list,
        		 isOrderPrize,
                 new DialogInterface.OnMultiChoiceClickListener() {
                     public void onClick(DialogInterface dialog, int whichButton,boolean isChecked) {
                         /* User clicked on a check box do some stuff */
                     }
                 })
         .setPositiveButton("确定", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int whichButton) {
                 /* User clicked Yes so do some stuff */
            	Toast.makeText(MoreActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
            	 saveOrderPrize();
            	 startPrizeService();//点击确定就开启Service
             }
         }).create();
	}
	/**
	 * 保存订阅数据
	 */
	private void saveOrderPrize(){
		 for(int i=0;i<isOrderPrize.length;i++){
    		 shellRW.putBooleanValue(Constants.orderPrize[i], isOrderPrize[i]);
    	 }
	}
	
	/**
	 * 获取订阅数据
	 */
	private void getOrderPrize(){
		 for(int i=0;i<isOrderPrize.length;i++){
			 isOrderPrize[i] = shellRW.getBooleanValue(Constants.orderPrize[i]);
    	 }
	}
	
	private void startPrizeService(){
		   Intent prizeIntent = new Intent( this, PrizeNotificationService.class);
		   startService(prizeIntent);
	}
	class MainUpdate extends UpdateDialog{

		public MainUpdate(Activity activity, Handler handler, String url,
				String message, String title) {
			super(activity, handler, url, message, title);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCancelButton() {
			// TODO Auto-generated method stub
			
		}


		
	}
	@Override
	public void onOkClick() {
		// TODO Auto-generated method stub
		logOutDialog.clearLastLoginInfo();
		Intent intent = new Intent("com.ruyicai.activity.home.MainGroup.inittop");
		sendBroadcast(intent);	
	}
	@Override
	public void onCancelClick() {
		// TODO Auto-generated method stub
		
	}
	

	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			Token accessToken = new Token(token, Weibo.getAppSecret());
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);
			share2weibo(Constants.shareContent);
			Intent intent = new Intent();
			intent.setClass(MoreActivity.this, ShareActivity.class);
			startActivity(intent);
		}


		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}
	}
	
	 private void share2weibo(String content)  {
	        Weibo weibo = Weibo.getInstance();
	        weibo.share2weibo(this, weibo.getAccessToken().getToken(), weibo.getAccessToken().getSecret(), content, "");
	    }
	
}
