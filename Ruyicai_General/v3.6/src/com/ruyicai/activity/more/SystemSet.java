/**
 * 
 */
package com.ruyicai.activity.more;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.interfaces.ReturnPage;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 系统设置类
 * @author Administrator
 *
 */
public class SystemSet {
	   ReturnPage returnPage;
	   public SystemSet(ReturnPage returnPage){
		   this.returnPage = returnPage;
	   }
	   /**
	    * 创建系统设置界面
	    * @return
	    */
	   public View showView(){
		LayoutInflater inflate = (LayoutInflater) returnPage.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(R.layout.ruyihelper_xitongshezhi, null);
		final CheckBox isOnCheck = (CheckBox) view.findViewById(R.id.xitongshezhi_check_zhengdong);
		ImageView tvreturn = (ImageView) view.findViewById(R.id.tv_choose_luck_lottery_num_return);
		tvreturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				returnPage.returnMain();
			}
		});
		// 读取是否震动参数
		final RWSharedPreferences shellRW = new RWSharedPreferences(returnPage.getContext(), "addInfo");
		boolean isOn = shellRW.getBooleanValue("isOn");
		isOnCheck.setChecked(isOn);
		isOnCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					shellRW.putBooleanValue("isOn", true);
					isOnCheck.setText(returnPage.getContext().getResources().getString(R.string.xitongshezhi_check_on).toString());
				} else {
					shellRW.putBooleanValue("isOn", false);
					isOnCheck.setText(returnPage.getContext().getResources().getString(R.string.xitongshezhi_check_off).toString());
				}
			}
		});
		return view;
	   }
}
