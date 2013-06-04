package com.ruyicai.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.RuyicaiActivityManager;
/**
 * ÍË³ö¿ò
 * @author Administrator
 *
 */
public class ExitDialogFactory extends BaseDialog{
	
	public ExitDialogFactory(Activity activity, String title, String message) {
		super(activity, title, message);
		// TODO Auto-generated constructor stub
	}

	public static void createExitDialog( final Activity activity){
		String title = activity.getString(R.string.quit_title);
		String msg = activity.getString(R.string.quit_content);
		ExitDialogFactory dialog = new ExitDialogFactory(activity, title, msg );
		dialog.showDialog();
		dialog.createMyDialog();
		
	}

	@Override
	public void onOkButton() {
		// TODO Auto-generated method stub
		RuyicaiActivityManager.getInstance().exit();
		PublicConst.islogin=false;
	}

	@Override
	public void onCancelButton() {
		// TODO Auto-generated method stub
		
	}

}
