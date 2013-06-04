package com.ruyicai.activity.buy.dlt;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.zixuan.BuyViewItem;
import com.ruyicai.activity.buy.zixuan.ZixuanActivity;
import com.ruyicai.code.dlt.DltTwoInDozenCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicMethod;

public class DltTwoInDozenSelect extends ZixuanActivity {
	
	private int redBallResId[] = { R.drawable.grey, R.drawable.red };
	
	AreaInfo[] dltTwoInDozenAreaInfos = new AreaInfo[1];
	/**
	 * 大乐透胆托红球区
	 */
	BallTable redBallTable;
	/**
	 * 实例化大乐透直选注码类
	 */
	DltTwoInDozenCode dltTwoInDozencode = new DltTwoInDozenCode();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCode(dltTwoInDozencode);
		setIsTen(false);
		initViewItem();
		redBallTable = itemViewArray.get(0).areaNums[0].table;
		
	}
	/**
	 * 初始化选区界面
	 */
	public void initViewItem() {
		// TODO Auto-generated method stub
		   BuyViewItem buyView = new BuyViewItem(this, initDltNormalArea());
		   itemViewArray.add(buyView);
		   View view = buyView.createView();
		   layoutView.addView(view);
	}
	/**
	 * 初始化大乐透12选2选区
	 */
	public AreaNum[] initDltNormalArea(){
		AreaNum areaNums[] = new AreaNum[1]; 
		String redTitle = getResources().getString(R.string.zi_xuanzedezhuma).toString();
		areaNums[0] = new AreaNum(12,8, 12, redBallResId, 0, 1,Color.RED,redTitle);
        return areaNums;
	}
	@Override
	public String isTouzhu() {
		// TODO Auto-generated method stub
		String isTouzhu = "";
		int iRedHighlights = redBallTable.getHighlightBallNums();
		int iZhuShu = (int) getDltTwoInDozenZhuShu(iRedHighlights,1);
		if (redBallTable.getHighlightBallNums()  < 2) {
			isTouzhu = "请至少选择2个小球	";
		} else if (iZhuShu * 2 > 100000) {
			isTouzhu = "false";
		} else {
			isTouzhu = "true";
		}
		return isTouzhu;
	}

	/**
	 * 投注提示框中的信息(注码)
	 * @return
	 */
	public String getZhuma(){
		String red_zhuma_string = " ";
		int[] redZhuMa = redBallTable.getHighlightBallNOs();
		for (int i = 0; i < redBallTable.getHighlightBallNOs().length; i++) {
			red_zhuma_string += PublicMethod.getZhuMa(redZhuMa[i]);
			if (i != redBallTable.getHighlightBallNOs().length - 1) {
				red_zhuma_string += ",";
			}
		}
		return "注码：\n" + red_zhuma_string ;
		
	}
	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		int iRedHighlights = areaNum[0].table.getHighlightBallNums();
		StringBuffer iTempString = new StringBuffer();
		int iZhuShu = 0;
		// 红球数 不足
		if (iRedHighlights < 2) {
			int num = 2-iRedHighlights;
			return "至少还需"+num+"个红球";
		} else {
				iZhuShu = (int) getDltTwoInDozenZhuShu(iRedHighlights,iProgressBeishu);
				iTempString.append("共").append(iZhuShu).append("注，共").append(iZhuShu * 2).append("元");	
		} 
		
		return iTempString.toString();
	}
	/**
	 * 大乐透十二选二 的计算方法
	 * @param iRedHighlights
	 * @param iProgressBeishu
	 * @return
	 */
	private long getDltTwoInDozenZhuShu(int iRedHighlights, int iProgressBeishu) {
		long dltZhuShu = 0L;
		dltZhuShu += (PublicMethod.zuhe(2, iRedHighlights) * iProgressBeishu);
		return  dltZhuShu;
	}
	@Override
	public void touzhuNet() {
		betAndGift.setSellway("0");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_DLT);
	}
	@Override
	public int getZhuShu() {
		// TODO Auto-generated method stub
		int iRedHighlights = itemViewArray.get(0).areaNums[0].table.getHighlightBallNums();
		int iZhuShu = (int) getDltTwoInDozenZhuShu(iRedHighlights,iProgressBeishu);
		return  iZhuShu;
	}

}
