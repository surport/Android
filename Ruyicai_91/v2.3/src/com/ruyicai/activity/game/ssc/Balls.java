package com.ruyicai.activity.game.ssc;

import android.util.Log;

import com.ruyicai.util.PublicMethod;

public class Balls {
    int Num[]=null;
    boolean isbigsmall=false;
    int i=0;
	
	public Balls(int i){
		this.i=i;
	}

	public void createBalls() {
		
		Num = new int[i];
		if(isbigsmall){
			
		Num = PublicMethod.getRandomsWithoutCollision(i, 0, 3);	
		}else{
			
		Num = PublicMethod.getRandomsWithoutCollision(i, 0, 9);
		}
		//redNum = orderby(redNum, "abc");
		//blueNum = PublicMethod.getRandomsWithoutCollision(1, 0, 15);
		for(int j=0;j<Num.length;j++){
			Log.v("===========Num"+j, ""+Num[j]);
			
		}

	}

	public int[] getball() {
		return Num;

	}
	public String getzhuma(){
		String str = "";
		for (int i = 0; i < Num.length; i++) {
			if (i !=Num.length - 1)
				str += PublicMethod.getZhuMa(Num[i] ) + ",";
			else
				str += PublicMethod.getZhuMa(Num[i]);
		}
		return str;
		
	
	}

	



	
	

}