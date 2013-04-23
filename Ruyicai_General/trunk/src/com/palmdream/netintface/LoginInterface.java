package com.palmdream.netintface;

import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.PublicMethod;

public class LoginInterface {
	String error_code = "00" ;
	int iretrytimes=2;
	public String sessionid;
	
       public String userlogin(String username,String password){
    		while(iretrytimes<3&&iretrytimes>0)
			{
			try {
    		String re=jrtLot.login(username,password);
			PublicMethod.myOutput("---re:"+re);
			PublicMethod.myOutput("----------username:"+username);
			PublicMethod.myOutput("-----------password:"+password);
			
			JSONObject obj;
		
				obj = new JSONObject(re);
				error_code = obj.getString("error_code");
				if(error_code.equals("0000")){
					sessionid=obj.getString("sessionid");
				}
				iretrytimes=3;
				
			    } catch (Exception e) {
				// TODO Auto-generated catch block
				PublicMethod.myOutput("-----------Exception_error_code:"+error_code);
				e.printStackTrace();
				iretrytimes--;
			    }
			}
			
		return error_code;
    	   
       }
}
