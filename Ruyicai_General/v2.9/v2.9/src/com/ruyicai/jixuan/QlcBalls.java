/**
 * 
 */
package com.ruyicai.jixuan;

import java.util.Vector;

import com.ruyicai.util.PublicMethod;

/**
 * @author Administrator
 *
 */
public class QlcBalls extends Balls{
    
    public QlcBalls(){
    	init();
    }
     
	/**
	 * 工厂方法
	 */
	public Balls createBalls() {
		// TODO Auto-generated method stub
		return  new QlcBalls();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		int redNum[] = PublicMethod.getRandomsWithoutCollision(7, 0, 29);
		redNum = PublicMethod.orderby(redNum, "abc");
		add(redNum,null);

	}


	public String getZhuma(Vector<Balls> balls, int beishu) {
		int[] zhuma=null;
//		String t_str = "1512-F47102-";
		String t_str = "";
		int zhushu = balls.size() ;
//		t_str += "00-";
//		if (zhushu < 10) {
//			t_str += "0"+zhushu;
//		}
//		if (zhushu >= 10) {
//			t_str += zhushu;
//		}
//        t_str+="-";
		for (int j = 0; j < balls.size(); j++) {
			t_str+="00";
			if (beishu < 10) {
				t_str += "0" + beishu;
			}
			if (beishu >= 10) {
				t_str += beishu;
			}
			zhuma = balls.get(j).getVZhuma().get(0);
			for (int i = 0; i < zhuma.length; i++) {
				int zhu = zhuma[i] + 1;
				if (zhu >= 10) {
					t_str += zhu;
				} else if (zhu < 10) {
					t_str += "0" + zhu;
				}
			}
			t_str += "^";
		}
		return t_str;
	}

}
