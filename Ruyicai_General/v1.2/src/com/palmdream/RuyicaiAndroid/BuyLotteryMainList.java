package com.palmdream.RuyicaiAndroid;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BuyLotteryMainList extends Activity implements MyDialogListener {

	public final static String[] buyLottery_lotteryName = { "双色球", "福彩3D",
			"七乐彩" ,"排列三" ,"超级大乐透" };
	// public final static String[] buyLottery_lotteryIssue =
	// {"第2010026期","第2010026期","第2010026期"};

	TextView text_lotteryName; // 彩票种类的TextView
	// 周黎鸣 7.5 代码修改：添加退出检测的代码————标记
	private int iQuitFlag = 0; // 代表退出

	// private boolean iCallOnKeyDownFlag=false;

	// 周黎鸣 7.5 代码修改：添加退出检测
	/**
	 * 退出检测
	 * 
	 * @param keyCode
	 *            返回按键的号码
	 * @param event
	 *            事件
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("--->>NoticePrizesOfLottery key:"
				+ String.valueOf(keyCode));
		switch (keyCode) {
		case 4: {
			break;
		}
			// 周黎鸣 7.8 代码修改：添加新的判断
		case 0x12345678: {
			/*
			 * if(iCallOnKeyDownFlag==false){ iCallOnKeyDownFlag=true;}
			 */
			switch (iQuitFlag) {
			case 0:
				WhetherQuitDialog iQuitDialog = new WhetherQuitDialog(this,
						this);
				iQuitDialog.show();
				break;

			}
			break;
		}
		}
		return false;
		// return super.onKeyDown(keyCode, event);
	}

	// 周黎鸣 7.5 代码修改：添加退出检测
	/**
	 * 取消
	 */
	public void onCancelClick() {
		// TODO Auto-generated method stub
		// iCallOnKeyDownFlag=false;
	}

	// 周黎鸣 7.5 代码修改：添加退出检测
	/**
	 * 确定
	 */
	public void onOkClick(int[] nums) {
		// TODO Auto-generated method stub
		// 退出
		this.finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView buyLotteryMainListTitle;

		setContentView(R.layout.notice_prizes_main_other);
		String lotno_dlt = getLotno("information9");
		String lotno_pl3 = getLotno("information10");
		
		System.out.println("--------lotno_dlt----------" + lotno_dlt);
		System.out.println("--------lotno_pl3----------" + lotno_pl3);

		// 显示标题：“彩票投注”
		// buyLotteryMainListTitle = (TextView)
		// findViewById(R.id.notice_prizes_title);
		// buyLotteryMainListTitle.setText(R.string.caipiaotouzhu);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_listview);

		BuyLotteryEfficientAdapter adapter = new BuyLotteryEfficientAdapter(
				this);
		listview.setAdapter(adapter);

		// 设置点击监听
		OnItemClickListener clickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				text_lotteryName = (TextView) view
						.findViewById(R.id.buyLotteryMainList_lotteryName_id);
				String str = text_lotteryName.getText().toString();
				// 点击双色球，就会跳转到双色球的购买列表中
				if (getString(R.string.shuangseqiu).equals(str)) {
					// Intent intent1 = new Intent( BuyLotteryMainList.this
					// ,NoticePrizesOfLotteryShuangseqiu.class);
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							ssqtest.class);
					startActivity(intent1);
				}
				// 点击福彩3D，就会跳转到福彩3D的购买列表中
				if (getString(R.string.fucai3d).equals(str)) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							FC3DTest.class);
					startActivity(intent1);
				}
				// 点击七乐彩，就会跳转到七乐彩的购买列表中
				if (getString(R.string.qilecai).equals(str)) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							QLC.class);
					startActivity(intent1);
				}
				if (getString(R.string.daletou).equals(str)) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							DLT.class);
					startActivity(intent1);
				}
				if (getString(R.string.pailie3).equals(str)) {
					
					Intent intent1 = new Intent(BuyLotteryMainList.this,PL3.class);
					startActivity(intent1);
				}

			}

		};

		listview.setOnItemClickListener(clickListener);

	}

	/**
	 * 设置彩票投注的适配器
	 */
	public class BuyLotteryEfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充彩票投注的布局
		private Bitmap mShuangSeQiu;
		private Bitmap mFuCai;
		private Bitmap mQiLeCai;
		private Bitmap mDaLeTou;
		private Bitmap mPaiLieSan;
		String lotno_ssq = getLotno("information4");
		String lotno_ddd = getLotno("information5");
		String lotno_qlc = getLotno("information6");
		String lotno_dlt = getLotno("information9");
		String lotno_pl3 = getLotno("information10");
		public String[] buyLottery_lotteryIssue = { "第  " + lotno_ssq + "  期",
				"第  " + lotno_ddd + "  期", "第  " + lotno_qlc + "  期" , "第  " + lotno_pl3 + "  期" ,"第  " + lotno_dlt + "  期"};

		public BuyLotteryEfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

			mShuangSeQiu = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.shuangseqiu);
			mFuCai = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.fucai);
			mQiLeCai = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.qilecai);
			mDaLeTou = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.daletou);
			mPaiLieSan = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pailiesan);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return buyLottery_lotteryName.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		// 设置彩票布局中具体的信息
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;

			if (convertView == null) {
				// 与布局中的信息关联起来
				convertView = mInflater.inflate(
						R.layout.buy_lottery_mainlist_layout, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.lotteryNameView = (TextView) convertView
						.findViewById(R.id.buyLotteryMainList_lotteryName_id);
				holder.lotteryIssueView = (TextView) convertView
						.findViewById(R.id.buyLotteryMainList_lotteryIssue_id);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 0) {
				holder.icon.setImageBitmap(mShuangSeQiu);
				holder.lotteryNameView
						.setText(buyLottery_lotteryName[position]);
				holder.lotteryIssueView
						.setText(buyLottery_lotteryIssue[position]);
			}
			else if (position == 1) {
				holder.icon.setImageBitmap(mFuCai);
				holder.lotteryNameView
						.setText(buyLottery_lotteryName[position]);
				holder.lotteryIssueView
						.setText(buyLottery_lotteryIssue[position]);
			} else if (position == 2) {
				holder.icon.setImageBitmap(mQiLeCai);
				holder.lotteryNameView
						.setText(buyLottery_lotteryName[position]);
				holder.lotteryIssueView
						.setText(buyLottery_lotteryIssue[position]);
			} else if (position == 3) {
				holder.icon.setImageBitmap(mPaiLieSan);
				holder.lotteryNameView
						.setText(buyLottery_lotteryName[position]);
				holder.lotteryIssueView
						.setText(buyLottery_lotteryIssue[position]);
			} else if (position == 4) {     //8.9 zlm 添加超级大乐透
				holder.icon.setImageBitmap(mDaLeTou);
				holder.lotteryNameView
						.setText(buyLottery_lotteryName[position]);
				holder.lotteryIssueView
						.setText(buyLottery_lotteryIssue[position]);
			}

			return convertView;
		}

		class ViewHolder {
			ImageView icon;
			TextView lotteryNameView;
			TextView lotteryIssueView;
		}
	}

	public String getLotno(String string) {
		String error_code;
		String batchcode = "";
		// ShellRWSharesPreferences
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String notice = shellRW.getUserLoginInfo(string);
		PublicMethod.myOutput("------------------lotnossq" + notice);
		// 判断取值是否为空 cc 2010/7/9
		if (!notice.equals("") || notice != null) {
			try {
				JSONObject obj = new JSONObject(notice);
				error_code = obj.getString("error_code");
				if (error_code.equals("0000")) {
					batchcode = obj.getString("batchCode");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return batchcode;
	}

}