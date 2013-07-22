package com.ruyicai.jixuan;

import java.util.Vector;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.util.PublicMethod;

public class DltSingleBalls extends Balls {

	public DltSingleBalls() {
		init();
	}

	@Override
	public Balls createBalls() {
		return new DltSingleBalls();
	}

	@Override
	public void init() {
		int redNum[] = PublicMethod.getRandomsWithoutCollision(5, 1, 35);
		redNum = PublicMethod.orderby(redNum, "abc");
		int blueNum[] = PublicMethod.getRandomsWithoutCollision(2, 1, 12);
		blueNum = PublicMethod.orderby(blueNum, "abc");
		add(redNum, null);
		add(blueNum, "blue");
	}

	@Override
	public String getZhuma(Vector<Balls> balls, int beishu) {
		StringBuffer dltSingleCode = new StringBuffer();
		int redcode[] = getVZhuma().get(0);
		for (int j = 0; j < redcode.length; j++) {
			if (j == redcode.length - 1) {
				dltSingleCode.append(CodeInterface.formatInteger(redcode[j]))
						.append("-");
			} else {
				dltSingleCode.append(CodeInterface.formatInteger(redcode[j]))
						.append(" ");
			}
		}
		int bluecode[] = getVZhuma().get(1);
		for (int j = 0; j < bluecode.length; j++) {
			if (j == bluecode.length - 1) {
				dltSingleCode.append(CodeInterface.formatInteger(bluecode[j]));
			} else {
				dltSingleCode.append(CodeInterface.formatInteger(bluecode[j]))
						.append(" ");
			}
		}
		return dltSingleCode.toString();
	}
}
