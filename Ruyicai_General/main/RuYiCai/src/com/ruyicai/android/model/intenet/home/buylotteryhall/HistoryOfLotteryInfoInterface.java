package com.ruyicai.android.model.intenet.home.buylotteryhall;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ruyicai.android.model.intenet.BaseIntenetInterface;

/**
 * 历史开奖接口
 * 
 * @author PengCX
 * @since RYC1.0 2013-9-11
 */
public class HistoryOfLotteryInfoInterface extends BaseIntenetInterface {
	// 彩种
	private String	_fLotno;
	// 第几页
	private String	_fPageIndex;
	// 每页显示的条数
	private String	_fMaxResult;

	/**
	 * 构造方法
	 * 
	 * @param _fLotno
	 *            彩种
	 * @param _fPageIndex
	 *            第几页
	 * @param _fMaxResult
	 *            每页显示的条数
	 */
	public HistoryOfLotteryInfoInterface(String _fLotno, String _fPageIndex,
			String _fMaxResult) {
		super();
		this._fLotno = _fLotno;
		this._fPageIndex = _fPageIndex;
		this._fMaxResult = _fMaxResult;
	}

	@Override
	public JSONObject setParticularParamerters(Context aContext,
			JSONObject aParamtersJsonObject) {
		try {
			// 设置命令
			aParamtersJsonObject.put("command", "QueryLot");
			// 设置类型
			aParamtersJsonObject.put("type", "winInfoList");
			// 设置彩种
			aParamtersJsonObject.put("lotno", "_fLotno");
			// 设置第几页
			aParamtersJsonObject.put("pageindex", "_fPageIndex");
			// 设置每页显示的条数
			aParamtersJsonObject.put("maxresult", "_fMaxResult");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return aParamtersJsonObject;
	}

}
