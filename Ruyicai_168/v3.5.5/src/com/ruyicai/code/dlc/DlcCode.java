package com.ruyicai.code.dlc;

import android.test.IsolatedContext;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class DlcCode extends CodeInterface{

	
	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String zhuma(AreaNum[] areaNums,String type){
		String str="";
		if(type.equals("R1")){
		 str+="R1|";
		 int [] R1=areaNums[0].table.getHighlightBallNOs();
	     str+=getRstring(R1);
		}else if(type.equals("R2")){
			 str+="R2|";
			 int [] R2=areaNums[0].table.getHighlightBallNOs();
		     str+=getRstring(R2);	
		}else if(type.equals("R3")){
			 str+="R3|";
			 int [] R3=areaNums[0].table.getHighlightBallNOs();
		     str+=getRstring(R3);
		}else if(type.equals("R4")){
			 str+="R4|";
			int [] R4=areaNums[0].table.getHighlightBallNOs();
		     str+=getRstring(R4);
		}else if(type.equals("R5")){
			if(isZHmiss()){
			 str+="R5|";
			 int [] R5= toInt(getIsZHcode().split("\\,"));
		     str+=getRstring(R5);
			}else{
			 str+="R5|";
			 int [] R5=areaNums[0].table.getHighlightBallNOs();
		     str+=getRstring(R5);
			}
		}else if(type.equals("R6")){
			 str+="R6|";
			 int [] R6=areaNums[0].table.getHighlightBallNOs();
		     str+=getRstring(R6);
		}else if(type.equals("R7")){
			if(isZHmiss()){
			 str+="R7|";
			 int [] R7 = toInt(getIsZHcode().split("\\,"));
		     str+=getRstring(R7);
			}else{
			 str+="R7|";
			 int [] R7=areaNums[0].table.getHighlightBallNOs();
		     str+=getRstring(R7);
			}
		}else if(type.equals("R8")){
			if(isZHmiss()){
			 str+="R8|";
			 int [] R8 = toInt(getIsZHcode().split("\\,"));
		     str+=getRstring(R8);
			}else{
			 str+="R8|";
			 int [] R8=areaNums[0].table.getHighlightBallNOs();
		     str+=getRstring(R8);
			}
		}else if(type.equals("Q2")){
		    str+="Q2|";
			int [] q2w=areaNums[0].table.getHighlightBallNOs();
			int [] q2q=areaNums[1].table.getHighlightBallNOs();
			str+=getRstring(q2w)+","+getRstring(q2q);
		}else if(type.equals("Q3")){
			if(isZHmiss()){
				int [] q3 = toInt(getIsZHcode().split("\\,"));
				str+="Q3|";
				int [] q3w = new int[1]; q3w[0] = q3[0];
				int [] q3q = new int[1]; q3q[0] = q3[1];
				int [] q3b = new int[1]; q3b[0] = q3[2];
				str+=getRstring(q3w)+","+getRstring(q3q)+","+getRstring(q3b);
			}else{
				str+="Q3|";
				int [] q3w=areaNums[0].table.getHighlightBallNOs();
				int [] q3q=areaNums[1].table.getHighlightBallNOs(); 
				int [] q3b=areaNums[2].table.getHighlightBallNOs(); 
				str+=getRstring(q3w)+","+getRstring(q3q)+","+getRstring(q3b);
			}
		}else if(type.equals("Z2")){
			str+="Z2|";
			int [] Z2=areaNums[0].table.getHighlightBallNOs();
		     str+=getRstring(Z2);
		}else if(type.equals("Z3")){
			str+="Z3|";
			int [] Z3=areaNums[0].table.getHighlightBallNOs();
		     str+=getRstring(Z3);
		}
		return str;
	}

   public static String getRstring(int[] num){
	   String str="";
		 for(int i=0;i<num.length;i++){
			 if(i!=num.length-1){
				 if(num[i]<10){
				str+="0"+num[i]+" ";	 
				 }else{
			    str+=num[i]+" "; 
				 }
			 }else{
				 if(num[i]<10){
				 str+="0"+num[i];	 
				 }else{
				 str+=num[i];
				 }
			 }		 	 
		 }
		 return str;
   }
}
