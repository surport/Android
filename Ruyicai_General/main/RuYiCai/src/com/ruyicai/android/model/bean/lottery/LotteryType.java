package com.ruyicai.android.model.bean.lottery;

/**
 * 彩票类型枚举对象： 1.限制规定彩票的类型
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-24
 */
public enum LotteryType {
	// FIXME 如何将彩种的名称和字符串资源关联
	DOUBLE_BALL("F47104", "双色球"), SUPER_LOTTO("T01001", "大乐透"), WELFARE_3D(
			"F47103", "福彩3D"), JIANGXI_ELEVEN_SELECT_FIVE("T01010", "江西11选5"), CONSTANTLY(
			"T01007", "时时彩"), COMPETE_FOOTBALL("J00001", "竞彩足球"), GUANGDONG_HAPPYVERY(
			"T01015", "快乐十分"), ELEVEN_LUCKGOLD("T01012", "11运夺金"), GUANGDONG_ELEVENE_SELECT_FIVE(
			"T01014", "广东11选5"), ARRANGE_THREE("T01002", "排列三"), SERVEN_HAPPY(
			"F47102", "七乐彩"), TWENTYTWO_SELECT_FIVE("T01013", "22选5"), ARRANGE_FIVE(
			"T01011", "排列五"), SEVEN_STAR("T01009", "七星彩"), FOOTBALL("T01005",
			"足彩"), COMPETE_BASKETBALL("J00001", "竞彩篮球");

	/** 彩种编号 */
	private String	_fLotteryNumber;
	/** 彩种名称 */
	private String	_fLotteryName;

	/**
	 * 彩种类型构造方法
	 * 
	 * @param _fLotteryNumber
	 *            彩种编号
	 * @param _fLotteryName
	 *            彩种名称
	 */
	private LotteryType(String _fLotteryNumber, String _fLotteryName) {
		this._fLotteryNumber = _fLotteryNumber;
		this._fLotteryName = _fLotteryName;
	}

	public String get_fLotteryNumber() {
		return _fLotteryNumber;
	}

	public void set_fLotteryNumber(String _fLotteryNumber) {
		this._fLotteryNumber = _fLotteryNumber;
	}

	public String get_fLotteryName() {
		return _fLotteryName;
	}

	public void set_fLotteryName(String _fLotteryName) {
		this._fLotteryName = _fLotteryName;
	}

	/**
	 * 获取所有彩种的个数
	 * 
	 * @return 所有彩种的个数
	 */
	public static int getAllLotteryNums() {
		return LotteryType.values().length;
	}

	/**
	 * 获取所有彩种的名称数组
	 */
	public static String[] getAllLotteryNames() {
		String[] lotteryNamesStrings = new String[getAllLotteryNums()];

		int lotteryNums = getAllLotteryNums();
		LotteryType[] lotteryTypes = LotteryType.values();

		for (int lottery_i = 0; lottery_i < lotteryNums; lottery_i++) {
			lotteryNamesStrings[lottery_i] = lotteryTypes[lottery_i]
					.get_fLotteryName();
		}

		return lotteryNamesStrings;
	}
}
