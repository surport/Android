package com.ruyicai.code.jc.zq;

import java.util.List;

import android.content.Context;

import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.JcType;

public class FootSpf {
	JcType jcType;
    public FootSpf(Context context){
    	jcType = new JcType(context);
    }
	 /**
     * 
     * »ñÈ¡×¢Âë
     * 
     */
    public String getCode(String key,List<Info> listInfo){
    	String code = jcType.getValues(key)+"@";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum>0) {
             code +=info.getDay()+"|"+info.getWeek()+"|"+info.getTeamId()+"|";		
             if(info.isWin()){
            	 code+="3";
             }
             if(info.isLevel()){	
            	 code+="1";
             }
             if(info.isFail()){
            	 code+="0";
             }
            	 code+="^";
			 }
			
		}
		return code;
    }
}
