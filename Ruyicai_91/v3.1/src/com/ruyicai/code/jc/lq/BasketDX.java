package com.ruyicai.code.jc.lq;

import java.util.List;

import android.content.Context;

import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.JcType;
/**
 * 大小分
 * @author Administrator
 *
 */
public class BasketDX {
	JcType jcType;
    public BasketDX(Context context){
    	jcType = new JcType(context);
    	
    }
	 /**
     * 
     * 获取注码
     * 
     */
    public String getCode(String key,List<Info> listInfo){
    	String code = jcType.getValues(key)+"@";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum>0) {
             code +=info.getDay()+"|"+info.getWeek()+"|"+info.getTeamId()+"|";		
             if(info.isWin()){
            	 code+="2";//小
             }
             if(info.isFail()){
            	 code+="1";//大
             }
            	 code+="^";
			 }
			
		}
		return code;
    }
}
