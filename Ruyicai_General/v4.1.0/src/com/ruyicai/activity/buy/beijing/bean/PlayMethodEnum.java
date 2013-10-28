package com.ruyicai.activity.buy.beijing.bean;

/**
 * 北京单场玩法枚举；让球胜平负、总进球数、全场总比分、半全场、上下单双
 * 
 * @author Administrator
 * 
 */
public enum PlayMethodEnum {
	WINTIELOSS("B00001"), TOTALGOALS("B00002"), OVERALL("B00005"), HALFTHEAUDIENCE(
			"B00003"), UPDOWNSINGLEDOUBLE("B00004");

	/**
	 * 彩种编号
	 */
	private String lotnoString;

	/**
	 * 获取彩种编号
	 * 
	 * @return 彩种编号字符串
	 */
	public String getLotnoString() {
		return lotnoString;
	}

	/**
	 * 构造函数
	 * 
	 * @param lotnoString
	 *            彩种编号字符串
	 */
	private PlayMethodEnum(String lotnoString) {
		this.lotnoString = lotnoString;
	}
}
