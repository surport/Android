package com.ruyicai.jixuan;

import java.util.Vector;


import com.ruyicai.util.PublicMethod;

public class SscBalls extends Balls{
    public  boolean isbigsmall=false;
    int i=0;
	public SscBalls(int i){
		this.i=i;
		init();
	}
    public SscBalls(){
    	this.i=2;
    	isbigsmall=true;
    	init();
    }
	@Override
	public Balls createBalls() {
		// TODO Auto-generated method stub
		if(isbigsmall){
			return new SscBalls();
		}else{
		return new SscBalls(i);
		}
	}

	@Override
	public void init() {
		if(isbigsmall){
			int onebig[]= new int[1];
			// TODO Auto-generated method stub
			for(int j=0;j<2;j++){
				onebig = PublicMethod.getRandomsWithoutCollision(1, 0, 3);
				add(onebig,null);
			//	Log.v("onbig", onebig[0]+"");
		}
		}else{
		int onenum[]= new int[1];
		// TODO Auto-generated method stub
		for(int j=0;j<i;j++){
			onenum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
			add(onenum,null);
		}
	}
}
	
	public String getZhuma(int index) {
		int num[] = getBalls(index);
		String str = "";
		for (int i = 0; i < num.length; i++) {
			if (i != num.length - 1)
				str += PublicMethod.getZhuMa(num[i] ) + ",";
			else
				str += PublicMethod.getZhuMa(num[i] );
		}
		return str;

	}
	/* (non-Javadoc)
	 * @see com.ruyicai.jixuan.Balls#getZhuma(java.util.Vector, int)
	 */
	public static String getzhuma(Vector<Balls>balls,int beishu){
	//	Log.v("type", "type");
	    String	str="";
		switch(beishu){
		case 1:
		str = "1D|";
		str +=  getstrjixuanone(balls);
		break;
		case 2:
      	str = "2D|";
	    str +=  getstrjixuantwo(balls);
	    break;
		case 3:
		str = "3D|";
		str +=getstrjixuanthree(balls);
		break;
		case 4:
		str = "5D|";
        str+=getstrjixuanfive(balls);
        break;
		case 5:
		str = "DD|";
        str+=getStrjixuanbigsmall(balls);
		}
		
		return str;
	}
	
	public static int[] getnum(Vector<int[]> nums){
		int num[]= new int[nums.size()];
		for(int i=0;i<nums.size();i++){
			num[i] = nums.get(i)[0];
		//	Log.v("===no.", num[i]+"");
		}
		return num;		
	}
	
	public static String getstrjixuanone(Vector<Balls> balls){
		String str="-," + "-," + "-," + "-";
		for (int i = 0; i < balls.size(); i++) {
		   
			int ge[]= getnum(balls.get(i).getVZhuma());
		//	int j;
			for(int j=0;j<ge.length;j++){
				//    Log.v("==ge", ge+"");
					str += ","+ge[j];	
			}
	        if(i!=balls.size()-1){
			str+=";1D|"+"-," + "-," + "-," + "-";	
			}		
		}
	
		return str;
		
	}
	
	public static String getstrjixuantwo(Vector<Balls> balls){
		String str="-," + "-," + "-";
		for (int i = 0; i < balls.size(); i++) {
		   
			int ge[]= getnum(balls.get(i).getVZhuma());
			for(int j=0;j<ge.length;j++){
				str += ","+ge[j];
			}
	        if(i!=balls.size()-1){
			str+=";2D|"+"-," + "-," + "-";	
			}		
		}
		return str;
			
	}
	public static String getstrjixuanthree(Vector<Balls> balls){
		String str="-," + "-";
		for (int i = 0; i < balls.size(); i++) {
		   
			int ge[]= getnum(balls.get(i).getVZhuma());
			for(int j=0;j<ge.length;j++){
				str += ","+ge[j];
			}
	        if(i!=balls.size()-1){
			str+=";3D|"+"-," + "-";
			}
		}
		return str;
	}
	
	public static String getstrjixuanfive(Vector<Balls> balls){
		String str="";
		for (int i = 0; i < balls.size(); i++) {
		   
			int ge[]= getnum(balls.get(i).getVZhuma());
			int j;
			for(j=0;j<ge.length;j++){
				if(j!=ge.length-1){
				str += ge[j]+",";
				}
				else{
					str += ge[j];	
				}
			}
			 if(i!=balls.size()-1){
					str+=";5D|";
					}
	    }
			
		return str;
	}
	public static String getStrjixuanbigsmall(Vector<Balls> balls){
		String str = "";
		for (int i = 0; i < balls.size(); i++) {
			int ball[]=getnum(balls.get(i).getVZhuma());
            for(int j=0;j<ball.length;j++){
			switch (ball[j]+1) {
			case 1:
				str += "2";// ´ó
				break;
			case 2:
				str += "1";// Ð¡
				break;
			case 3:
				str += "5";// µ¥
				break;
			case 4:
				str += "4";// Ë«
				break;
			}
		}
	}  
		return str;
	}
	@Override
	public String getZhuma(Vector<Balls> balls, int beishu) {
		// TODO Auto-generated method stub
		return null;
	}
}