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
public class PL3ZhiXuanBalls extends Balls{
    public PL3ZhiXuanBalls(){
    	init();
    }
    /**
     * 工厂方法
     * @return
     */
	public  Balls createBalls(){
		return new PL3ZhiXuanBalls();
		
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		int[] baiNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] shiNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] geNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		add(baiNum,null);
		add(shiNum,null);
		add(geNum,null);
	}


	String zhixuan = "1|";// 直选
	public String getZhuma(Vector<Balls> balls, int beishu) {
		// TODO Auto-generated method stub
		String strZM = "";
		
		for (int i = 0; i < balls.size(); i++) {
			strZM += zhixuan;
			int bai = balls.get(i).getVZhuma().get(0)[0] + 1;
			int shi = balls.get(i).getVZhuma().get(1)[0] + 1;
			int ge = balls.get(i).getVZhuma().get(2)[0] + 1;
			strZM += bai + "," + shi + "," + ge;
			if (i != balls.size() - 1) {
				strZM += ";";
			}
		}
		return strZM;
	}


}
