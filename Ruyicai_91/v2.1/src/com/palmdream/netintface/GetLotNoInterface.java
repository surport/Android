package com.palmdream.netintface;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.PublicMethod;

public class GetLotNoInterface {
	
//	获取当前期数
	
	String error_code="00";
	String re;
	public String batchcode;
	int iretrytimes=2;
      public String getLotNo(String LotNo){
    	  re=jrtLot.getLotNo(LotNo);
    	  JSONObject obj = null;
			while(iretrytimes<3&&iretrytimes>0){
			try {
				obj = new JSONObject(re);
				error_code = obj.getString("error_code");
				PublicMethod.myOutput("---------------------"+error_code);
				if(error_code.equals("0000")){
					batchcode=obj.getString("batchCode");
				}
				iretrytimes=3;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				iretrytimes--;
				
			}
		 }	
    	  return error_code;
      }
}
