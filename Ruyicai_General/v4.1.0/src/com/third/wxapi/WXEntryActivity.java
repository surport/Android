package com.third.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.RWSharedPreferences;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

public class WXEntryActivity extends Activity implements View.OnClickListener {
	private TextView ruyipackage_title;
	private String sharemsg;
	private EditText sharecontent;
	private Button commit ,cancel;
	private IWXAPI api;
	private RWSharedPreferences rw;
	private String sharestyle;
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.tencentshare);
    	getShareContent();
    	init();
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
		api.registerApp(Constants.APP_ID);
	}
	

	
	private void init() {
		rw=new RWSharedPreferences(this, "shareweixin");
		sharestyle=rw.getStringValue("weixin_pengyou");
		ruyipackage_title=(TextView) this.findViewById(R.id.ruyipackage_title);
		if("toweixin".equals(sharestyle)){
			ruyipackage_title.setText("微信分享");
		}
	    if("topengyouquan".equals(sharestyle)){
			ruyipackage_title.setText("分享到朋友圈");
		}
		
		sharecontent = (EditText) findViewById(R.id.sharecontent);
		sharecontent.setText(sharemsg);
		commit = (Button) findViewById(R.id.share);
		cancel = (Button) findViewById(R.id.btn_return);
		commit.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}
	
	private void getShareContent(){
		sharemsg=getIntent().getStringExtra("sharecontent");
	}
	

	@Override
	public void onClick(View v) {
       switch (v.getId()) {
	case R.id.share:
		toShare();
		WXEntryActivity.this.finish();
		break;
	case R.id.btn_return:
		WXEntryActivity.this.finish();
		break;
	default:
		break;
	}		
	}

	private void toShare() {
		if(api.isWXAppInstalled()){
			sharetoweixin();
		}else{
			 Toast.makeText(this, "请先安装微信客户端",Toast.LENGTH_LONG).show();
			 Uri uri = Uri.parse("http://weixin.qq.com/m");  
			 Intent it = new Intent(Intent.ACTION_VIEW, uri);  
			 startActivity(it);
		}
	}
	
   private void sharetoweixin(){
	   if("toweixin".equals(sharestyle) && api!=null){
		   WXTextObject textObj = new WXTextObject();  
	        textObj.text = sharemsg;

	        WXMediaMessage msg = new WXMediaMessage();  
	        msg.mediaObject = textObj;  
	        msg.description = sharemsg;  
	          
	        SendMessageToWX.Req req = new SendMessageToWX.Req();  
	        req.transaction = String.valueOf(System.currentTimeMillis());  
	        req.message = msg;  
	        api.sendReq(req);
		}
	   
	   if("topengyouquan".equals(sharestyle) && api!=null){
		   int wxSdkVersion = api.getWXAppSupportAPI();
			if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
				if(api!=null){
			        WXTextObject textObj = new WXTextObject();  
			        textObj.text = sharemsg;

			        WXMediaMessage msg = new WXMediaMessage();  
			        msg.mediaObject = textObj;  
			        msg.description = sharemsg;  
			          
			        SendMessageToWX.Req req = new SendMessageToWX.Req();  
			        req.transaction = String.valueOf(System.currentTimeMillis());  
			        req.message = msg;  
			        req.scene=SendMessageToWX.Req.WXSceneTimeline;
			        api.sendReq(req);
				}
			} else {
				Toast.makeText(WXEntryActivity.this, "不支持分享到朋友圈", Toast.LENGTH_LONG).show();
			}
	   }
	}
}
