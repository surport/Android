package com.ruyicai.code.eleven;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class ElevenCode extends CodeInterface{

	
	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String zhuma(AreaNum[] areaNums,String type){
		String str="";
		if(type.equals("R1")){
		 int [] R=areaNums[0].table.getHighlightBallNOs();
		 if(R.length>1){//¸´ÊÔ
			 str+="101@*"; 
			 str+=getRstring(R)+"^";
		  }else{
			 str+="101@*";
			 str+=getRstring(R)+"^";
		  }
	 
		}else if(type.equals("R2")){
			 int [] R=areaNums[0].table.getHighlightBallNOs();
			 if(R.length>2){//¸´ÊÔ
				 str+="102@*"; 
				 str+=getRstring(R)+"^";
			  }else{
				 str+="111@";
				 str+=getRstring(R)+"^";
			  }
		}else if(type.equals("R3")){
			 int [] R=areaNums[0].table.getHighlightBallNOs();
			 if(R.length>3){//¸´ÊÔ
				 str+="103@*"; 
				 str+=getRstring(R)+"^";
			  }else{
				 str+="112@";
				 str+=getRstring(R)+"^";
			  }
		}else if(type.equals("R4")){
			 int [] R=areaNums[0].table.getHighlightBallNOs();
			 if(R.length>4){//¸´ÊÔ
				 str+="104@*"; 
				 str+=getRstring(R)+"^";
			  }else{
				 str+="113@";
				 str+=getRstring(R)+"^";
			  }
		}else if(type.equals("R5")){
			 int [] R=areaNums[0].table.getHighlightBallNOs();
			 if(R.length>5){//¸´ÊÔ
				 str+="105@*"; 
				 str+=getRstring(R)+"^";
			  }else{
				 str+="114@";
				 str+=getRstring(R)+"^";
			  }
		}else if(type.equals("R6")){
			 int [] R=areaNums[0].table.getHighlightBallNOs();
			 if(R.length>6){//¸´ÊÔ
				 str+="106@*"; 
				 str+=getRstring(R)+"^";
			  }else{
				 str+="115@";
				 str+=getRstring(R)+"^";
			  }
		}else if(type.equals("R7")){
			 int [] R=areaNums[0].table.getHighlightBallNOs();
			 if(R.length>7){//¸´ÊÔ
				 str+="107@*"; 
				 str+=getRstring(R)+"^";
			  }else{
				 str+="116@";
				 str+=getRstring(R)+"^";
			  }
		}else if(type.equals("R8")){
			 int [] R=areaNums[0].table.getHighlightBallNOs();
			 str+="117@";
		     str+=getRstring(R)+"^";
		}else if(type.equals("Q2")){
			int [] q2w=areaNums[0].table.getHighlightBallNOs();
			int [] q2q=areaNums[1].table.getHighlightBallNOs();
			if(q2w.length>1||q2q.length>1){//¸´ÊÔ
				str += "142@";
				str += getRstring(q2w)+"*"+getRstring(q2q)+"^";
			}else{
				str+="141@";
			    str += getRstring(q2w)+getRstring(q2q)+"^";
			}
			
		}else if(type.equals("Q3")){
			int [] q3w=areaNums[0].table.getHighlightBallNOs();
			int [] q3q=areaNums[1].table.getHighlightBallNOs(); 
			int [] q3b=areaNums[2].table.getHighlightBallNOs();
			if(q3w.length>1||q3q.length>1||q3b.length>1){//¸´ÊÔ
				str += "162@";
				str += getRstring(q3w)+"*"+getRstring(q3q)+"*"+getRstring(q3b)+"^";
			}else{
				str +="161@";
			    str += getRstring(q3w)+getRstring(q3q)+getRstring(q3b)+"^";
			}
		}else if(type.equals("Z2")){
			 int [] R=areaNums[0].table.getHighlightBallNOs();
			 if(R.length>2){//¸´ÊÔ
				 str+="108@*"; 
				 str+=getRstring(R)+"^";
			  }else{
				 str+="131@";
				 str+=getRstring(R)+"^";
			  }
		}else if(type.equals("Z3")){
			 int [] R=areaNums[0].table.getHighlightBallNOs();
			 if(R.length>3){//¸´ÊÔ
				 str+="109@*"; 
				 str+=getRstring(R)+"^";
			  }else{
				 str+="151@";
				 str+=getRstring(R)+"^";
			  }
		}
		return str;
	}
   public static String getRstring(int[] num){
	   String str="";
		 for(int i=0;i<num.length;i++){
			 if(num[i]<10){
				 str+="0"+num[i];	 
				 }else{
				 str+=num[i];
				 } 
		 }
		 return str;
   }
}
