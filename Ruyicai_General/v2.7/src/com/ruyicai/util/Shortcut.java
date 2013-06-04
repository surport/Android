package com.ruyicai.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.home.HomeActivity;

/**
 * 发送快捷方式的类
 * @author miaoqiang
 *
 */
public class Shortcut extends  Activity{
	Intent addShortcut;//要添加快捷方式的Intent
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addShortcut();
	}

	
	   private void addShortcut() {
			// TODO Auto-generated method stub
	    	
	    	if(getIntent().getAction().equals(Intent.ACTION_CREATE_SHORTCUT)){
	    		addShortcut = new Intent();
	    		/*设置快捷方式的名字*/
	    		addShortcut.putExtra(Intent.ACTION_CREATE_SHORTCUT,"如意彩票");
	    		/*设置快捷方式的图标*/
	    		Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.icon);
	    		/*添加快捷方式图标*/
	    		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
	    		/*构建快捷方式执行的Intent*/
	    		Intent startEisen = new Intent(this,HomeActivity.class);
	    		/*添加快捷方式的Intent*/
	    		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, startEisen);
	    		setResult(RESULT_OK,addShortcut);
	    		
	    	}else{
	    		setResult(RESULT_CANCELED);
	    	}
	    	finish();

		}
	    /*
	     * 其实快捷方式信息是保存在com.android.launcher的launcher.db的favorites表中， 
相关代码： 

boolean isInstallShortcut = false ;  
    final ContentResolver cr = context.getContentResolver();  
    final String AUTHORITY = "com.android.launcher.settings";  
    final Uri CONTENT_URI = Uri.parse("content://" +  
                     AUTHORITY + "/favorites?notify=true");  
      
    Cursor c = cr.query(CONTENT_URI,  
    new String[] {"title","iconResource" },  
    "title=?",  
    new String[] {"XXX" }, null);//XXX表示应用名称。  
            if(c!=null && c.getCount()>0){  
        isInstallShortcut = true ;  
    }  
    /*try { 
        while (c.moveToNext()) { 
                                    String tmp = ""; 
            tmp = c.getString(0); 
        } 
        } catch (Exception e) { 
 
        } finally { 
            c.close(); 
        }*/  
    //return isInstallShortcut ;  
//}*/

}
