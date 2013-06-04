package com.ruyicai.code.dlt;



import android.util.Log;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class DltNormalSelectCode extends CodeInterface{



	public String zhuma(AreaNum[] areaNums, int beishu,int type) {
		// TODO Auto-generated method stub
		StringBuffer normalCode = new StringBuffer();
		int redNum[] = areaNums[0].table.getHighlightBallNOs();
		for(int i = 0;i < redNum.length;i++){
			if(i == redNum.length - 1){
				normalCode.append(formatInteger(redNum[i])).append("-");
			}else{
				normalCode.append(formatInteger(redNum[i])).append(" ");
			}
		}
		int blueNum[] = areaNums[1].table.getHighlightBallNOs();
	
		for(int j = 0;j< blueNum.length;j++){
			if(j == blueNum.length - 1){
				normalCode.append(formatInteger(blueNum[j]));
			}else{
				normalCode.append(formatInteger(blueNum[j])).append(" ");
			}
		}
	//	Log.v("normalCode",""+normalCode);
		return normalCode.toString();
	}





}
