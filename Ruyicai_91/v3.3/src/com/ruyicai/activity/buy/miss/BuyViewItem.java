package com.ruyicai.activity.buy.miss;

import java.util.ArrayList;
import java.util.List;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.miss.onclik.BallOnclik;
import com.ruyicai.activity.buy.miss.onclik.BaseOnclik;
import com.ruyicai.custom.gallery.GalleryViewItem;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.json.miss.MissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
/**
 * 选区界面
 * @author Administrator
 *
 */
public class BuyViewItem extends GalleryViewItem{
	public AreaNum areaNums[];
    public Context context;
    private ScrollView scrollView;
    public BaseOnclik ballOnclik;
    public OnClickListener onclick = null;
    public int height;
	public List<MyButton> missBtnList;
	public boolean isZHmiss = false;
    public BaseActivity zixuan;
    protected Button pageBtn;
	public BuyViewItem(BaseActivity zixuan,AreaNum areaNums[]) {
		super(zixuan.getBaseContext());
		this.zixuan = zixuan;
        this.context = zixuan.getBaseContext();
        this.areaNums = areaNums;
        this.onclick = zixuan;
        
	}
	public void rightBtn(View view){
		pageBtn = (Button) view.findViewById(R.id.buy_zixuan_new_btn_miss);
		pageBtn.setVisibility(Button.VISIBLE);
	}
	public void leftBtn(View view){
		pageBtn = (Button) view.findViewById(R.id.buy_zixuan_new_btn_main);
		pageBtn.setVisibility(Button.VISIBLE);
	}
	public void rightBtnBG(int bgId){
		pageBtn.setBackgroundResource(bgId);
	}
	/**
	 * 创建购彩界面
	 * @param areaInfos 选球区
	 */
	public View createView() {
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(R.layout.buy_zixuan_new_view, null);
		int iScreenWidth = PublicMethod.getDisplayWidth(context);
		int layoutIds[]={R.id.buy_zixuan_linear_one,R.id.buy_zixuan_linear_two,R.id.buy_zixuan_linear_third,R.id.buy_zixuan_linear_four,R.id.buy_zixuan_linear_five,R.id.buy_zixuan_linear_six,R.id.buy_zixuan_linear_seven};
		int tableLayoutIds[]={R.id.buy_zixuan_table_one,R.id.buy_zixuan_table_two,R.id.buy_zixuan_table_third,R.id.buy_zixuan_table_four,R.id.buy_zixuan_table_five,R.id.buy_zixuan_table_six,R.id.buy_zixuan_table_seven};
		int textViewIds[]={R.id.buy_zixuan_text_one,R.id.buy_zixuan_text_two,R.id.buy_zixuan_text_third,R.id.buy_zixuan_text_four,R.id.buy_zixuan_text_five,R.id.buy_zixuan_text_six,R.id.buy_zixuan_text_seven};
		rightBtn(view);
		height = 0;
		//初始化选区
		for(int i=0;i<areaNums.length;i++){
			AreaNum areaNum = areaNums[i];
			areaNum.initView(tableLayoutIds[i],textViewIds[i],layoutIds[i],view);
			if(i!=0){
				areaNum .aIdStart=areaNums[i-1].areaNum+areaNums[i-1].aIdStart;
			}
			areaNum .table =  PublicMethod.makeBallTable(areaNum .tableLayout, iScreenWidth, areaNum .areaNum,areaNum.ballResId,areaNum .aIdStart, areaNum .aBallViewText,context,onclick,areaNum.isNum);
//			height += PublicMethod.areaHeight(iScreenWidth, areaNum .areaNum, areaNum.isNum,false);
			areaNum .init();
		}
//		initBallOnclik();
		return view;
	}

	public void initBallOnclik() {
		ballOnclik = new BallOnclik(zixuan, areaNums);
	}

	@Override
	public void updateView(MissJson missJson) {
		// TODO Auto-generated method stub
		
	}
}
