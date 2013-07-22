/**
 * 
 */
package com.ruyicai.interfaces;

import com.ruyicai.pojo.AreaNum;

/**
 * 购彩要实现的方法
 * 
 * @author Administrator
 * 
 */
public interface BuyImplement {
	public static final String type = "";

	/**
	 * 点击小球提示金额
	 * 
	 * @param areaNum
	 * @param iProgressBeishu
	 * @return
	 */
	public String textSumMoney(AreaNum areaNum[], int iProgressBeishu);

	/**
	 * 判断是否满足投注条件
	 */
	public void isTouzhu();

	/**
	 * 投注联网
	 */
	public void touzhuNet();
}
