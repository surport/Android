package com.ruyicai.activity.buy.miss;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.activity.buy.zixuan.JiXuanBtn;
import com.ruyicai.custom.gallery.GalleryViewItem;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.json.miss.MissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicMethod;

/**
 * 选区界面
 * 
 * @author Administrator
 * 
 */
public class BuyViewItemMiss extends GalleryViewItem {

	public Context context;
	private ScrollView scrollView;
	public OnClickListener onclick = null;
	public int height;
	public List<MyButton> missBtnList;
	public boolean isZHmiss = false;
	public BaseActivity zixuan;
	protected Button pageBtn;
	private boolean isRight = true;

	public BuyViewItemMiss(BaseActivity zixuan, AreaNum areaNums[]) {
		super(zixuan.getBaseContext());
		this.zixuan = zixuan;
		this.context = zixuan.getBaseContext();
		this.areaNums = areaNums;
		this.onclick = zixuan;

	}

	public void setIsRight(boolean isRight) {
		this.isRight = isRight;
	}

	public void rightBtn(View view) {
		pageBtn = (Button) view.findViewById(R.id.buy_zixuan_new_btn_miss);
		pageBtn.setVisibility(Button.VISIBLE);
	}

	public void leftBtn(View view) {
		pageBtn = (Button) view.findViewById(R.id.buy_zixuan_new_btn_main);
		pageBtn.setVisibility(Button.VISIBLE);
	}

	public void rightBtnBG(int bgId) {
		pageBtn.setBackgroundResource(bgId);
	}

	/**
	 * 创建购彩界面
	 * 
	 * @param areaInfos
	 *            选球区
	 */
	public View createView() {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (RelativeLayout) inflate.inflate(
				R.layout.buy_zixuan_new_view, null);
		int iScreenWidth = PublicMethod.getDisplayWidth(context);
		int layoutIds[] = { R.id.buy_zixuan_linear_one,
				R.id.buy_zixuan_linear_two, R.id.buy_zixuan_linear_third,
				R.id.buy_zixuan_linear_four, R.id.buy_zixuan_linear_five,
				R.id.buy_zixuan_linear_six, R.id.buy_zixuan_linear_seven };
		int tableLayoutIds[] = { R.id.buy_zixuan_table_one,
				R.id.buy_zixuan_table_two, R.id.buy_zixuan_table_third,
				R.id.buy_zixuan_table_four, R.id.buy_zixuan_table_five,
				R.id.buy_zixuan_table_six, R.id.buy_zixuan_table_seven };
		int textViewIds[] = { R.id.buy_zixuan_text_one,
				R.id.buy_zixuan_text_two, R.id.buy_zixuan_text_third,
				R.id.buy_zixuan_text_four, R.id.buy_zixuan_text_five,
				R.id.buy_zixuan_text_six, R.id.buy_zixuan_text_seven };
		int btnIds[] = { R.id.buy_zixuan_btn_one, R.id.buy_zixuan_btn_two,
				R.id.buy_zixuan_btn_third, R.id.buy_zixuan_btn_four,
				R.id.buy_zixuan_btn_five, R.id.buy_zixuan_btn_six,
				R.id.buy_zixuan_btn_seven };
		int btnDwIds[] = { R.id.buy_zixuan_btn_num_one,
				R.id.buy_zixuan_btn_num_two, R.id.buy_zixuan_btn_num_third,
				R.id.buy_zixuan_btn_num_four, R.id.buy_zixuan_btn_num_five,
				R.id.buy_zixuan_btn_num_six, R.id.buy_zixuan_btn_num_seven };
		if (isRight) {
			rightBtn(view);
		}
		height = 0;
		// 初始化选区
		for (int i = 0; i < areaNums.length; i++) {
			AreaNum areaNum = areaNums[i];
			areaNum.initView(tableLayoutIds[i], textViewIds[i], layoutIds[i],
					view);
			if (i != 0) {
				areaNum.aIdStart = areaNums[i - 1].areaNum
						+ areaNums[i - 1].aIdStart;
			}
			areaNum.table = PublicMethod.makeBallTable(areaNum.tableLayout,
					iScreenWidth, areaNum.areaNum, areaNum.ballResId,
					areaNum.aIdStart, areaNum.aBallViewText, context, onclick,
					areaNum.isNum);
			areaNum.init();
			Button btn = (Button) view.findViewById(btnIds[i]);
			Button btnDw = (Button) view.findViewById(btnDwIds[i]);
			if (areaNum.isJxBtn) {
				btn.setVisibility(Button.VISIBLE);
				btnDw.setVisibility(Button.VISIBLE);
				areaNum.jixuanBtn = new JiXuanBtn(areaNum.isRed, btn, btnDw,
						areaNum.chosenBallSum, zixuan, view, areaNum.table,
						areaNum.areaMin, i);
			} else {
				btn.setVisibility(Button.GONE);
				btnDw.setVisibility(Button.GONE);
				areaNum.jixuanBtn = new JiXuanBtn(areaNum.isRed, btn, btnDw,
						areaNum.chosenBallSum, zixuan, view, areaNum.table,
						areaNum.areaMin, i);
			}
			if (areaNum.isSensor) {
				zixuan.areaNums = areaNums;
			}
		}
		return view;
	}

	@Override
	public void updateView(MissJson missJson) {
		// TODO Auto-generated method stub

	}
}
