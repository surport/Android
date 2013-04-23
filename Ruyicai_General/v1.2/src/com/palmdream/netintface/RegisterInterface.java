package com.palmdream.netintface;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.PublicMethod;

public class RegisterInterface {
	String error_code="00";
	int iretry=2;
	 public  String userregister(String login_User, String login_password,String login_Id){
  	  
  	   JSONObject obj = null;
		while(iretry<3&&iretry>0){
			try {
			 String re=jrtLot.register(login_User, login_password, login_Id);
			//if(iretrytime<3 && iretrytime>0){
			obj = new JSONObject(re);
			//iretrytime--;
		//	}
		    error_code=obj.getString("error_code");	
			PublicMethod.myOutput("---------"+error_code);
			iretry=3;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			iretry--;
		  }
		}
		// PublicMethod.myOutput("----------registerinterface"+error_code);
			return error_code;
			
     }

}
