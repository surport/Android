/**
 * 
 */
package com.ruyicai.activity.buy.fc3d;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.util.Constants;

/**
 * 福彩3d
 * @author Administrator
 *
 */
public class Fc3d extends BuyActivityGroup{
    public static 	int iCurrentButton ;//标签
	private String[] titles ={"直选","组三","组六","机选"};
	private String[] topTitles ={"福彩3D直选","福彩3D组三","福彩3D组六","福彩3D机选"};
	private Class[] allId ={FC3DZhiXuan.class,FC3DZuSan.class,FC3DZuLiu.class,Fc3dJiXuan.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_FC3D);
	}
}
