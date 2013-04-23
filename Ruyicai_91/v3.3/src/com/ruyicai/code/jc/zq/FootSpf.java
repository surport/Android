package com.ruyicai.code.jc.zq;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.JcType;
import com.ruyicai.util.PublicMethod;

public class FootSpf {
	JcType jcType;

    public FootSpf(Context context){
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
    
    /**
     * 获取赔率List
     */
    
    public List<double[]> getOddsList(List<Info> listInfo){
    	 List<double[]> oddsList = new ArrayList<double[]>() ;/*赛事选择后的赔率列表*/
    	 for (int i = 0; i < listInfo.size(); i++) {
 			Info info = (Info) listInfo.get(i);
 			if (info.onclikNum>0) {
 				try{
 					  double[] aa = new double[3];
 		              if(info.isWin()){
 		            	  aa[0] = Float.parseFloat(info.getWin());
 		              }
 		              if(info.isLevel()){	
 		            	  aa[1] = Float.parseFloat(info.getLevel());
 		              }
 		              if(info.isFail()){
 		            	  aa[2] = Float.parseFloat(info.getFail());
 		              }
 		              double[] insertdouble = PublicMethod.getDoubleArrayNoZero(aa);
 		              oddsList.add(insertdouble);
 				}catch(NumberFormatException e){
 					 double[] insertdouble = {1};
 					 oddsList.add(insertdouble);
 				}
 			
 			 }
 			
 		}
    	 return oddsList;
    }
}
