package com.ruyicai.activity.buy.jc.zq;

import android.os.Bundle;
import android.view.Window;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.constant.Constants;

public class ZqMainActivity extends JcMainActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setType(Constants.JCFOOT);
        createView(SF,false);
        setScoreBtn(true);
        setTitle(false);
        isTeamBtn(true);
	}
}
