package com.ruyicai.controller;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class Controller {
    private static final String TAG = "Controller";
    private static Controller sInstance;
    private Context mContext;
    private Context mProviderContext;
    private JSONObject jsonObj;
    protected ProgressDialog dialog;
    
    protected Controller(Context _context) {
        mContext = _context;
//        mProviderContext = _context;
    }

    /**
     * Gets or creates the singleton instance of Controller.
     */
    public static Controller getInstance(Context _context) {
        if (sInstance == null) {
            sInstance = new Controller(_context);
        } else {
        	sInstance.mContext = _context;
        }
        return sInstance;
    }

    /**
     * For testing only:  Inject a different context for provider access.  This will be
     * used internally for access the underlying provider (e.g. getContentResolver().query()).
     * @param providerContext the provider context to be used by this instance
     */
    public void setProviderContext(Context providerContext) {
        mProviderContext = providerContext;
    }
    
	/**
	 * 投注action
	 */
   public void doBettingAction(final MyHandler handler,final BetAndGiftPojo betAndGift) {
	   if (dialog != null && dialog.isShowing()) return;
	   dialog = UserCenterDialog.onCreateDialog(mContext,mContext.getResources().getString(R.string.recommend_network_connection));
	   dialog.show();
	   
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betAndGift);
				try {
					JSONObject obj = new JSONObject(str);
					final String msg = obj.getString("message");
					final String error = obj.getString("error_code");
					setRtnJSONObject(obj);
					handler.handleMsg(error, msg);
				} catch (JSONException e) {
					e.printStackTrace();
					// TODO Auto-generated method stub			
				}
				dialog.dismiss();
				//dialog = null;
			}
		});
		t.start();
   }
   /**
    * set return obj
    * @param obj
    */
   public void setRtnJSONObject(JSONObject obj) {
	   this.jsonObj = obj;
   }
   
   /**
    * get return obj
    * @param obj
    */
   public JSONObject getRtnJSONObject() {
	   return this.jsonObj;
   }
}
