package com.ruyicai.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.ruyicai.activity.home.RuyicaiAndroid;

public class Accoutdialog {
	
	private static Accoutdialog instance = null;
	
   
	private Accoutdialog(){
		
	}
	
	public synchronized static Accoutdialog getInstance() {
		if (instance == null ) {
			instance = new Accoutdialog();
		}
		return instance;

	}
	
    public void createAccoutdialog(final Context context,String msg){
    	Builder dialog = new AlertDialog.Builder(context).setTitle("充值提示").setMessage(msg).setPositiveButton("充值",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						  Intent intent=new Intent(context,RuyicaiAndroid.class);
						  Bundle bundle=new Bundle();
						  bundle.putInt("index", 2);
						  intent.putExtras(bundle);
						  context.startActivity(intent);
						  ((Activity) context).finish();
						  
					}

				}).setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		dialog.show();
    	
    }
}
