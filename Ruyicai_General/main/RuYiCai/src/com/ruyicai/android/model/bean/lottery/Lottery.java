package com.ruyicai.android.model.bean.lottery;

/**
 * 彩种的基类
 * 
 * @author Administrator
 * @since RYC1.0 2013-3-21
 */
public class Lottery {
	/** 彩种类型 */
	private LotteryType	_fLotteryType;
	/** 是否今日开奖，默认今日不开奖 */
	private Boolean		_fIsNowLottery	= false;
	/** 是否暂停销售，默认不暂停销售 */
	private Boolean		_fIsSaleStop	= false;
	/** 是否加奖，默认不加奖 */
	private Boolean		_fIsReward		= false;

	/**
	 * 构造方法
	 * 
	 * @param _fLotteryType
	 *            彩种的类型
	 * @param _fIsNowLottery
	 *            是否今日开奖
	 * @param _fIsSaleStop
	 *            是否停售
	 * @param _fIsReward
	 *            是否加奖
	 */
	public Lottery(LotteryType _fLotteryType, Boolean _fIsNowLottery,
			Boolean _fIsSaleStop, Boolean _fIsReward) {
		super();
		this._fLotteryType = _fLotteryType;
		this._fIsNowLottery = _fIsNowLottery;
		this._fIsSaleStop = _fIsSaleStop;
		this._fIsReward = _fIsReward;
	}

	public LotteryType get_fLotteryType() {
		return _fLotteryType;
	}

	public void set_fLotteryType(LotteryType _fLotteryType) {
		this._fLotteryType = _fLotteryType;
	}

	public Boolean get_fIsNowLottery() {
		return _fIsNowLottery;
	}

	public void set_fIsNowLottery(Boolean _fIsNowLottery) {
		this._fIsNowLottery = _fIsNowLottery;
	}

	public Boolean get_fIsSaleStop() {
		return _fIsSaleStop;
	}

	public void set_fIsSaleStop(Boolean _fIsSaleStop) {
		this._fIsSaleStop = _fIsSaleStop;
	}

	public Boolean get_fIsReward() {
		return _fIsReward;
	}

	public void set_fIsReward(Boolean _fIsReward) {
		this._fIsReward = _fIsReward;
	}
}
