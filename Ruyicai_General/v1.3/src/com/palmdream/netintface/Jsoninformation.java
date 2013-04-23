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
	static String dltinfo;
	static String pl3info;
	static String getlotno_ssq;
	static String getlotno_ddd;
	static String getlotno_qlc;
	static String getlotno_dlt;
	static String getlotno_pl3;
	
	public static String[] getjsoninformation(){
		//String[] str=null;
		tnotice=Tnotice();
		if(tnotice.equals("")){
			PublicMethod.myOutput("-fff---------------@@@@@@@@@@@@ no notice");
			tnotice="notice";
			ssqinfo="ssqinfo";
			dddinfo="dddinfo";
			qlcinfo="qlcinfo";
			dltinfo = "dltinfo";
			pl3info = "pl3info";
			getlotno_ssq="getlotno_ssq";
			getlotno_ddd="getlotno_ddd";
			getlotno_qlc="getlotno_qlc";
			getlotno_dlt = "getlotno_dlt";
			getlotno_pl3 = "getlotno_pl3";
			
		}else{
			ssqinfo=ssqinfo();
			dddinfo=dddinfo();
			qlcinfo=qlcinfo();
			dltinfo = dltinfo();
			pl3info = pl3info();
			getlotno_ssq=getLotNo_ssq();
			getlotno_ddd=getLotNo_ddd();
			getlotno_qlc=getLotNo_qlc();
			getlotno_dlt = getLotNo_dlt();
			getlotno_pl3 = getLotNo_pl3();
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
			if (dltinfo.equalsIgnoreCase("")) {
				dltinfo = "dltinfo";
			}
			if (qlcinfo.equalsIgnoreCase("")) {
				pl3info = "pl3info";
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
			if (getlotno_dlt.equalsIgnoreCase("")) {
				getlotno_dlt = "getlotno_dlt";
			}
			if (getlotno_pl3.equalsIgnoreCase("")) {
				getlotno_pl3 = "getlotno_pl3";
			}
		}
		String[] str = { tnotice, ssqinfo, dddinfo, qlcinfo, getlotno_ssq,
				getlotno_ddd, getlotno_qlc,dltinfo,pl3info ,getlotno_dlt ,getlotno_pl3};//
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
			} else {
				iretrytimes--;
			}
			PublicMethod.myOutput("--------------------jsoninformationtnotice"
					+ re);

			/*
			 * try{ re=jrtLot.tnotice(); // if(re.equalsIgnoreCase("")){
			 * iretrytimes=3; PublicMethod.myOutput("-------====---------");
			 * }catch(Exception e){ iretrytimes--; //
			 * PublicMethod.myOutput("---------------exception"+re); } re="12";
			 * PublicMethod.myOutput("----------------rererre"+re);
			 */
		}

		iretrytimes = 2;
		return re;

	}

	// 获取双色球开奖信息的后台返回值
	public static String ssqinfo() {
		String re = "";
		String error_code;
		while (iretrytimes < 3 && iretrytimes > 0) {
			re = jrtLot.lotterySelect("B001");//   F47104
			 PublicMethod.myOutput("===jasoninformation==re="+re);
			if (!re.equalsIgnoreCase("")) {
				try {
					try {
						JSONObject obj = new JSONObject(re);
						error_code = obj.getString("error_code");
						 PublicMethod.myOutput("===ssqerrorcode===="+error_code);
					} catch (Exception e) {
						JSONArray jsonObject3 = new JSONArray(re);
						JSONObject obj = jsonObject3.getJSONObject(0);
						error_code = obj.getString("error_code");
						 PublicMethod.myOutput("===ssqerrorcode===="+error_code);
					}
					iretrytimes = 3;
				} catch (JSONException e) {
					 PublicMethod.myOutput("===ssqjsonexception====");
					iretrytimes--;
					re = "ssqinfo";
				}
			} else {
				iretrytimes--;
			}
		}
		iretrytimes = 2;
		PublicMethod.myOutput("--------------------jsoninformationssqinfo()"
				+ re);
		System.out.println("----------+re-------" + re);
		return re;
	}

	// 获取3D开奖信息的后台返回值
	public static String dddinfo() {
		String re = "";
		String error_code;
		while (iretrytimes < 3 && iretrytimes > 0) {
			re = jrtLot.lotterySelect("D3");//   F47103
			 PublicMethod.myOutput("===jasoninformation==re="+re);
			if (!re.equalsIgnoreCase("")) {
				try {
					try {
						JSONObject obj = new JSONObject(re);
						error_code = obj.getString("error_code");
						 PublicMethod.myOutput("===ddderrorcode===="+error_code);
					} catch (Exception e) {
						JSONArray jsonObject3 = new JSONArray(re);
						JSONObject obj = jsonObject3.getJSONObject(0);
						error_code = obj.getString("error_code");
						 PublicMethod.myOutput("===ddderrorcode===="+error_code);
					}
					iretrytimes = 3;
				} catch (JSONException e) {
					 PublicMethod.myOutput("===dddexceptionerrorcode====");
					iretrytimes--;
					re = "dddinfo";
				}
			} else {
				iretrytimes--;
			}
		}
		iretrytimes = 2;
		PublicMethod.myOutput("--------------------jsoninformationdddinfo()"
				+ re);
		return re;
	}

	// 获取七乐彩开奖信息的后台返回值
	public static String qlcinfo() {
		String re = "";
		String error_code;
		while (iretrytimes < 3 && iretrytimes > 0) {
			re = jrtLot.lotterySelect("QL730");//   F47102
			if (!re.equalsIgnoreCase("")) {
				try {
					try {
						JSONObject obj = new JSONObject(re);
						error_code = obj.getString("error_code");
						 PublicMethod.myOutput("===qlcerrorcode===="+error_code);
					} catch (Exception e) {
						JSONArray jsonObject3 = new JSONArray(re);
						JSONObject obj = jsonObject3.getJSONObject(0);
						error_code = obj.getString("error_code");
						 PublicMethod.myOutput("===qlcerrorcode===="+error_code);
					}
					iretrytimes = 3;
				} catch (JSONException e) {
					 PublicMethod.myOutput("===qlcexceptionerrorcode====");
					iretrytimes--;
					re = "qlcinfo";
				}
			} else {
				iretrytimes--;
			}
		}
		iretrytimes = 2;
		PublicMethod.myOutput("--------------------jsoninformationqlcinfo()"
				+ re);
		return re;
	}
	
	// 获取大乐透开奖信息的后台返回值
	public static String dltinfo() {
		String re = "";
		String error_code;
		while (iretrytimes < 3 && iretrytimes > 0) {
			re = jrtLot.tlotterySelect("T01001");// F47102
			if (!re.equalsIgnoreCase("")) {
				try {
					try {
						JSONObject obj = new JSONObject(re);
						error_code = obj.getString("error_code");
					} catch (Exception e) {
						JSONArray jsonObject3 = new JSONArray(re);
						JSONObject obj = jsonObject3.getJSONObject(0);
						error_code = obj.getString("error_code");
					}
					iretrytimes = 3;
				} catch (JSONException e) {
					iretrytimes--;
					re = "dltinfo";
				}
			} else {
				iretrytimes--;
			}
		}
		iretrytimes = 2;
		PublicMethod.myOutput("--------------------jsoninformationdltinfo()"
				+ re);
		return re;
	}
	// 获取排列三开奖信息的后台返回值
	public static String pl3info() {
		String re = "";
		String error_code;
		while (iretrytimes < 3 && iretrytimes > 0) {
			re = jrtLot.tlotterySelect("T01002");// F47102
			if (!re.equalsIgnoreCase("")) {
				try {
					try {
						JSONObject obj = new JSONObject(re);
						error_code = obj.getString("error_code");
					} catch (Exception e) {
						JSONArray jsonObject3 = new JSONArray(re);
						JSONObject obj = jsonObject3.getJSONObject(0);
						error_code = obj.getString("error_code");
					}
					iretrytimes = 3;
				} catch (JSONException e) {
					iretrytimes--;
					re = "pl3info";
				}
			} else {
				iretrytimes--;
			}
		}
		iretrytimes = 2;
		PublicMethod.myOutput("--------------------jsoninformationpl3info()"
				+ re);
		return re;
	}

	// 获取双色球当前期号的后台返回值
	public static String getLotNo_ssq() {
		String re = "";
		String batchCode;
		while (iretrytimes < 3 && iretrytimes > 0) {
			re = jrtLot.getLotNo("F47104");
			 PublicMethod.myOutput("===jasoninformation==re="+re);
			if (!re.equalsIgnoreCase("")) {
				try {
					JSONObject obj = new JSONObject(re);
					batchCode = obj.getString("batchCode");
					iretrytimes = 3;
				} catch (JSONException e) {
					iretrytimes--;
					re = "getlotno_ssq";
				}
			} else {
				iretrytimes--;
			}
		}
		iretrytimes = 2;
		PublicMethod.myOutput("--------------------jsoninformationssqgetNo"
				+ re);
		return re;
	}

	// 获取3D当前期号的后台返回值
	public static String getLotNo_ddd() {
		String re = "";
		String batchCode;
		while (iretrytimes < 3 && iretrytimes > 0) {
			re = jrtLot.getLotNo("F47103");
			if (!re.equalsIgnoreCase("")) {
				try {
					JSONObject obj = new JSONObject(re);
					batchCode = obj.getString("batchCode");
					iretrytimes = 3;
				} catch (JSONException e) {
					iretrytimes--;
					re = "getlotno_ddd";
				}
			} else {
				iretrytimes--;

			}
		}
		iretrytimes = 2;
		PublicMethod.myOutput("--------------------jsoninformationdddgetNo"
				+ re);
		return re;
	}

	// 获取七乐彩当前期号的后台返回值
	public static String getLotNo_qlc() {
		String re = "";
		String batchCode;
		while (iretrytimes < 3 && iretrytimes > 0) {
			re = jrtLot.getLotNo("F47102");
			if (!re.equalsIgnoreCase("")) {
				try {
					JSONObject obj = new JSONObject(re);
					batchCode = obj.getString("batchCode");
					iretrytimes = 3;
				} catch (JSONException e) {
					iretrytimes--;
					re = "getlotno_qlc";
				}
			} else {
				iretrytimes--;
			}
		}
		iretrytimes = 2;
		PublicMethod.myOutput("--------------------jsoninformationqlcgetNo"
				+ re);
		return re;
	}
	// 获取超级大乐透当前期号的后台返回值
	public static String getLotNo_dlt() {
System.out.println("============getLotNo_dlt()============");		
		String re = "";
		String batchCode;
		while (iretrytimes < 3 && iretrytimes > 0) {
			re = jrtLot.getLotNo("T01001");
			if (!re.equalsIgnoreCase("")) {
				try {
					JSONObject obj = new JSONObject(re);
					batchCode = obj.getString("batchCode");
					iretrytimes = 3;
				} catch (JSONException e) {
					iretrytimes--;
					re = "getlotno_dlt";
				}
			} else {
				iretrytimes--;
			}
		}
		iretrytimes = 2;
		PublicMethod.myOutput("--------------------jsoninformationdltgetNo"
				+ re);
		return re;
	}
	
	// 获取排列三当前期号的后台返回值
	public static String getLotNo_pl3() {
		String re = "";
		String batchCode;
		while (iretrytimes < 3 && iretrytimes > 0) {
			re = jrtLot.getLotNo("T01002");
			if (!re.equalsIgnoreCase("")) {
				try {
					JSONObject obj = new JSONObject(re);
					batchCode = obj.getString("batchCode");
					iretrytimes = 3;
				} catch (JSONException e) {
					iretrytimes--;
					re = "getlotno_pl3";
				}
			} else {
				iretrytimes--;
			}
		}
		iretrytimes = 2;
		PublicMethod.myOutput("--------------------jsoninformationpi3getNo"
				+ re);
		return re;
	}
}
