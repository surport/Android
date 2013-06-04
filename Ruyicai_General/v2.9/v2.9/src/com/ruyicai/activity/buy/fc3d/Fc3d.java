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
	private String[] titles ={"直选","组选","和值","机选"};
	private String[] topTitles ={"福彩3D直选","福彩3D组选","福彩3D和值","福彩3D机选"};
	private Class[] allId ={Fc3dZiZhiXuan.class,Fc3dZiZuXuan.class,Fc3dZiHeZhi.class,Fc3dJiXuan.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_FC3D);
	}
}
