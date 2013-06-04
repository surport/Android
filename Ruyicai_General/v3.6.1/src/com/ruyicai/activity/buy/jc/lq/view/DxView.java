package com.ruyicai.activity.buy.jc.lq.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.activity.buy.jc.lq.view.SfView.JcInfoAdapter;
import com.ruyicai.code.jc.lq.BasketDX;
import com.ruyicai.code.jc.lq.BasketSF;
import com.ruyicai.code.jc.lq.BasketSFC;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;

/**
 * 大小分
 * @author Administrator
 *
 */
public class DxView extends SfView{
	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		// TODO Auto-generated method stub
		return  basketDx.getOddsList(listInfo);
	}
	protected BasketDX basketDx;
	public DxView(Context context,BetAndGiftPojo betAndGift,Handler handler,LinearLayout layout,String type,boolean isdanguan,List<String> checkTeam) {
		super(context, betAndGift, handler, layout,type,isdanguan,checkTeam);
		basketDx = new BasketDX(context);
	}
	@Override
	public String getCode(String key, List<Info> listInfo) {
		// TODO Auto-generated method stub
		return basketDx.getCode(key, listInfo);
	}
	@Override
	public String getLotno() {
		// TODO Auto-generated method stub
		return Constants.LOTNO_JCLQ_DXF;
	}
	public void setDifferValue(JSONObject jsonItem,Info itemInfo) throws JSONException{
		//大小分
		itemInfo.setBig(jsonItem.getString("g"));
		itemInfo.setSmall(jsonItem.getString("l"));
		itemInfo.setBasePoint(jsonItem.getString("basePoint"));	
	}
    /**
     * 初始化列表
     */
	public void initListView(ListView listview,Context context,List<List> listInfo) {
		// TODO Auto-generated method stub
		adapter = new JcInfoAdapter(context,listInfo,B_DX);
		listview.setAdapter(adapter);
	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		if(isDanguan){
		return context.getString(R.string.jclq_dxf_danguan_title).toString();
	
		}else{
		return context.getString(R.string.jclq_dxf_guoguan_title).toString();
		}
	}
	@Override
	public String getTypeTitle() {
		// TODO Auto-generated method stub
		return context.getString(R.string.jclq_dialog_dxf_guoguan_title).toString();
	}
	public String getAlertCode(List<Info> listInfo) {
    	String codeStr ="";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum>0) {
             codeStr +=info.getAway()+" vs "+info.getHome()+"：";
             if(info.isWin()){
             	codeStr+="小";
             }
             if(info.isFail()){
            	codeStr+="大";
             }
             if(info.isDan()){
            	 codeStr+="(胆)";
             }
     			 codeStr+="\n\n";
			 }
			
		}
		return codeStr;
	}
	@Override
	public String getPlayType() {
		// TODO Auto-generated method stub
		if(isDanguan){
			return "J00008_0";
		}else{
			return "J00008_1";
		}
	}
}
