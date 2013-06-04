package com.ruyicai.activity.buy.jc.lq;

import android.os.Bundle;
import android.os.Handler;

import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.activity.buy.jc.lq.view.DxView;
import com.ruyicai.activity.buy.jc.lq.view.RfView;
import com.ruyicai.activity.buy.jc.lq.view.SfView;
import com.ruyicai.activity.buy.jc.lq.view.SfcView;
import com.ruyicai.activity.buy.jc.zq.view.BFView;
import com.ruyicai.activity.buy.jc.zq.view.BQCView;
import com.ruyicai.activity.buy.jc.zq.view.SPfView;
import com.ruyicai.activity.buy.jc.zq.view.ZJQView;
import com.ruyicai.constant.Constants;




public class LqMainActivity extends JcMainActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setType(Constants.JCBASKET);
        createView(SF,isDanguan);
        setTitle(false);
        setScoreBtn(false);
        isTeamBtn(true);
	}
	/**
	 * 根据玩法按钮创建界面
	 */
	protected void createView(int type ,boolean isdanguan){
		switch(type){
		case SF:
			lqMainView = new SfView(this,betAndGift,new Handler(),layoutView,this.type,isdanguan,checkTeam);
			TYPE = SF;
			break;
		case RF_SF:
			lqMainView = new RfView(this,betAndGift,new Handler(),layoutView,this.type,isdanguan,checkTeam);
			TYPE = RF_SF;
			break;
		case SFC:
			lqMainView = new SfcView(this,betAndGift,new Handler(),layoutView,this.type,isdanguan,checkTeam);
			TYPE = SFC;
			break;
		case DXF:
			lqMainView = new DxView(this,betAndGift,new Handler(),layoutView,this.type,isdanguan,checkTeam);
			TYPE = DXF;
			break;
		}
		lqMainView.initTeamNum(textTeamNum);
		textTitle.setText(lqMainView.getTitle());
		
	}
	
}

