package com.palmdream.netintface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.PublicMethod;


public class Jsoninformation {
	
    static int iretrytimes=1;
    static String tnotice;
    static String ssqinfo;
	static String dddinfo;
	static String qlcinfo;
	static String getlotno_ssq;
	static String getlotno_ddd;
	static String getlotno_qlc;
	
	public static String[] getjsoninformation(){
		//String[] str=null;
		tnotice=Tnotice();
		if(tnotice.equals("")){
			PublicMethod.myOutput("-fff---------------@@@@@@@@@@@@ no notice");
			tnotice="notice";
			ssqinfo="ssqinfo";
			dddinfo="dddinfo";
			qlcinfo="qlcinfo";
			getlotno_ssq="getlotno_ssq";
			getlotno_ddd="getlotno_ddd";
			getlotno_qlc="getlotno_qlc";
			
		}else{
			ssqinfo=ssqinfo();
			dddinfo=dddinfo();
			qlcinfo=qlcinfo();
			getlotno_ssq=getLotNo_ssq();
			getlotno_ddd=getLotNo_ddd();
			getlotno_qlc=getLotNo_qlc();
	//		if(tnotice.equalsIgnoreCase("")){
	//			tnotice="notice";
	//		}
			if(ssqinfo.equalsIgnoreCase("")){
				ssqinfo="ssqinfo";
			}
			if(dddinfo.equalsIgnoreCase("")){
				dddinfo="dddinfo";
			}
			if(qlcinfo.equalsIgnoreCase("")){
				qlcinfo="qlcinfo";
			}
			if(getlotno_ssq.equalsIgnoreCase("")){
				getlotno_ssq="getlotno_ssq";
			}
			if(getlotno_ddd.equalsIgnoreCase("")){
				getlotno_ddd="getlotno_ddd";
			}
			if(getlotno_qlc.equalsIgnoreCase("")){
				getlotno_qlc="getlotno_qlc";
			}
		}
		String[] str={tnotice,ssqinfo,dddinfo,qlcinfo,getlotno_ssq,getlotno_ddd,getlotno_qlc};
	    //str=str1;
//		for(int i=0;i<str.length;i++){
//		PublicMethod.myOutput("---------------------str[0]"+str[i]);
//		}
	    return str;
		
		
	}
	
//	获取及时信息的后台返回值
	public static String Tnotice(){
		String re="";
		String news;
		while(iretrytimes<3&&iretrytimes>0){
			re=jrtLot.tnotice();
			if(!re.equalsIgnoreCase("")){
				// parse
				// success
				try{
				JSONObject obj = new JSONObject(re);
				news=obj.getString("news");
				iretrytimes=3;
				}catch(JSONException e){
					iretrytimes--;
					re="tnotice";
				}
			}
			else {
				iretrytimes--;
			}
			PublicMethod.myOutput("--------------------jsoninformationtnotice"+re);

			/*
		   try{
			  re=jrtLot.tnotice();
			 // if(re.equalsIgnoreCase("")){
			  iretrytimes=3;
			  PublicMethod.myOutput("-------====---------");
		   }catch(Exception e){
			iretrytimes--;
		//	 PublicMethod.myOutput("---------------exception"+re);
		   }
			 re="12";
			PublicMethod.myOutput("----------------rererre"+re);
			*/
		}
		
		iretrytimes=2;
		return re;
		
	}
	
//	获取双色球开奖信息的后台返回值
	public static String ssqinfo(){
		String re="";
		String error_code;
		while(iretrytimes<3&&iretrytimes>0){
				   re=jrtLot.lotterySelect("B001");//F47104
				   if(!re.equalsIgnoreCase("")){
					   try{
						   try{
						   JSONObject obj = new JSONObject(re);  
						   error_code=obj.getString("error_code");
						   }catch(Exception e){
							JSONArray jsonObject3=new JSONArray(re);
							JSONObject obj = jsonObject3.getJSONObject(0);
							error_code=obj.getString("error_code");
						   }     
				           iretrytimes=3;
				        }catch(JSONException e){
					       iretrytimes--;
					       re="ssqinfo";
			          }
			   }
			else{
			   iretrytimes--;
		   }
		}
		iretrytimes=2;
		PublicMethod.myOutput("--------------------jsoninformationssqinfo()"+re);
		return re;
	}
	
//	获取3D开奖信息的后台返回值
	public static String dddinfo(){
		String re="";
		String error_code;
		while(iretrytimes<3&&iretrytimes>0){
			   re=jrtLot.lotterySelect("D3");//F47103
			   if(!re.equalsIgnoreCase("")){
				   try{
					   try{
						   JSONObject obj = new JSONObject(re); 
						   error_code=obj.getString("error_code");
						   }catch(Exception e){
							JSONArray jsonObject3=new JSONArray(re);
							JSONObject obj = jsonObject3.getJSONObject(0);
							error_code=obj.getString("error_code");
						   }  
			           iretrytimes=3;
			        }catch(JSONException e){
				       iretrytimes--;
				       re="dddinfo";
		          }
		   }
		else{
		   iretrytimes--;
	   }
	}
		iretrytimes=2;
		PublicMethod.myOutput("--------------------jsoninformationdddinfo()"+re);
		return re;
	}
//	获取七乐彩开奖信息的后台返回值
	public static String qlcinfo(){
		String re="";
		String error_code;
		while(iretrytimes<3&&iretrytimes>0){
			   re=jrtLot.lotterySelect("QL730");//F47102
			   if(!re.equalsIgnoreCase("")){
				   try{
					   try{
					   JSONObject obj = new JSONObject(re); 
					   error_code=obj.getString("error_code");
					   }catch(Exception e){
						   JSONArray jsonObject3=new JSONArray(re);
						   JSONObject obj = jsonObject3.getJSONObject(0);
							error_code=obj.getString("error_code");
					   }
			           iretrytimes=3;
			        }catch(JSONException e){
				       iretrytimes--;
				       re="qlcinfo";
		          }
		       }
				 else{
				   iretrytimes--;
			   }
	}
		iretrytimes=2;
		PublicMethod.myOutput("--------------------jsoninformationqlcinfo()"+re);
		return re;
	}
//	获取双色球当前期号的后台返回值
	public static String getLotNo_ssq(){
		String re="";
		String batchCode;
		while(iretrytimes<3&&iretrytimes>0){
			   re=jrtLot.getLotNo("F47104");
			   if(!re.equalsIgnoreCase("")){
				   try{
					   JSONObject obj = new JSONObject(re);  
					   batchCode=obj.getString("batchCode");
			           iretrytimes=3;
			        }catch(JSONException e){
				       iretrytimes--;
				       re="getlotno_ssq";
		          }
		       }
				 else{
				   iretrytimes--;
			   }
	}
		iretrytimes=2;
		PublicMethod.myOutput("--------------------jsoninformationssqgetNo"+re);
		return re;
	}
//	获取3D当前期号的后台返回值
	public static String getLotNo_ddd(){
		String re="";
		String batchCode;
		while(iretrytimes<3&&iretrytimes>0){
			   re=jrtLot.getLotNo("F47103");
			   if(!re.equalsIgnoreCase("")){
				   try{
					   JSONObject obj = new JSONObject(re); 
					   batchCode=obj.getString("batchCode");
			           iretrytimes=3;
			        }catch(JSONException e){
				       iretrytimes--;
				       re="getlotno_ddd";
		          }
		       }
				 else{
				   iretrytimes--;
				   
			   }
	     }
		iretrytimes=2;
		PublicMethod.myOutput("--------------------jsoninformationdddgetNo"+re);
		return re;
	}
//	获取七乐彩当前期号的后台返回值
	public static String getLotNo_qlc(){
		String re="";
		String batchCode;
		while(iretrytimes<3&&iretrytimes>0){
			   re=jrtLot.getLotNo("F47102");
			   if(!re.equalsIgnoreCase("")){
				   try{
					   JSONObject obj = new JSONObject(re);   
					   batchCode=obj.getString("batchCode");
			           iretrytimes=3;
			        }catch(JSONException e){
				       iretrytimes--;
				       re="getlotno_qlc";
		          }
		       }
				 else{
				   iretrytimes--;
			   }
	     }
		iretrytimes=2;
		PublicMethod.myOutput("--------------------jsoninformationqlcgetNo"+re);
		return re;
	}
}
