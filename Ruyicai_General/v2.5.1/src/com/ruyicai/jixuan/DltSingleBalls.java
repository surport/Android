package com.ruyicai.jixuan;

import java.util.Vector;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.util.PublicMethod;

public class DltSingleBalls extends Balls{
	
	public DltSingleBalls(){
		init();
	}

	@Override
	public Balls createBalls() {
		return new DltSingleBalls();
	}

	@Override
	public void init() {
		int redNum[] = PublicMethod.getRandomsWithoutCollision(5, 0, 34);
	    redNum = PublicMethod.orderby(redNum, "abc");
		int blueNum[] = PublicMethod.getRandomsWithoutCollision(2, 0, 11);
		blueNum = PublicMethod.orderby(blueNum, "abc");
		add(redNum,null);
		add(blueNum,"blue");
	}

	@Override
	public String getZhuma(Vector<Balls> balls, int beishu) {
		StringBuffer dltSingleCode = new StringBuffer();
		for (int i = 0; i < balls.size(); i++) {
			int redcode[] = balls.get(i).getVZhuma().get(0);
			for(int j = 0;j < redcode.length;j++){
				if(j == redcode.length - 1){
					dltSingleCode.append(CodeInterface.formatInteger(redcode[j]+1)).append("-");
				}else{
					dltSingleCode.append(CodeInterface.formatInteger(redcode[j]+1)).append(" ");
				}
			}
			int bluecode[] = balls.get(i).getVZhuma().get(1);
			for(int j = 0;j< bluecode.length;j++){
				if(j == bluecode.length - 1){
					dltSingleCode.append(CodeInterface.formatInteger(bluecode[j]+1));
				}else{
					dltSingleCode.append(CodeInterface.formatInteger(bluecode[j]+1)).append(" ");
				}
			}
			if(i<balls.size()-1){
				dltSingleCode.append(";");
			}
		}
		return dltSingleCode.toString();
	}
}
