package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.net.newtransaction.NoticePrizeDetailInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
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
	public LotnoDetailView(Activity context,String lotno,String batchcode,ProgressDialog progress,Handler handler,boolean isDialog) {
		LayoutInflater factory = LayoutInflater.from(context);
		view = factory.inflate(R.layout.notice_detail, null);
		this.isDialog = isDialog;
		this.handler = handler;
		this.context = context;
		this.progress = progress;
		isTopVisable(isDialog);
		initLotnoDetailViewWidget();
		getPrizeDetalilNet( lotno, batchcode);
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
	
  public void getPrizeDetalilNet(final String lotno,final String batchcode){
	  progress.show();

	  new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			JSONObject PrizeDetailJson = NoticePrizeDetailInterface.getInstance().getNoticePrizeDetail(lotno, batchcode);
			progress.dismiss();
			try {
				final String errorcode =  PrizeDetailJson.getString("error_code");
				final String message   =  PrizeDetailJson.getString("message");
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
							Toast.makeText(context,message.toString(), Toast.LENGTH_LONG).show();
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
