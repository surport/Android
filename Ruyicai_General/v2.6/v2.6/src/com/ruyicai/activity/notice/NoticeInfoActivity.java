/**
 * 
 */
package com.ruyicai.activity.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class NoticeInfoActivity extends Activity{
	public final static String LOTTERYTYPE = "LOTTERYTYPE";
	public final static String WINNINGNUM = "WINNINGNUM";
	public final static String DATE = "DATA";
	public final static String ISSUE = "ISSUE";
	public final static String FINALDATE = "FINALDATE";
	public final static String MONEYSUM = "MONEYSUM";

	private static int[] aRedColorResId = { R.drawable.red };
	private static int[] aBlueColorResId = { R.drawable.blue };
	public static  int BALL_WIDTH = 46;
	List<Map<String, Object>> list, list_ssq, list_fc3d, list_qlc, list_pl3,list_cjdlt,list_pl5,list_qxc; // zlm 8.9 添加排列三、超级大乐透
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 showListView(NoticeActivityGroup.LOTNO);
		 BALL_WIDTH = NoticePrizesOfLottery.BALL_WIDTH;
	}
	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * @param listViewID
	 *            列表ID
	 */
	private void showListView(int listViewID) {
		switch (listViewID) {
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			showSubShuangSeQiuListView();
			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			showSubFuCai3DListView();
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			showSubQiLeCaiListView();
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			showSubPaiLieSanListView(); // zlm 排列三
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			showSubPL5ListView(); // zlm 排列五
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			showSubQXCListView(); // zlm 七星彩
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			showSubChaoJiDaLeTouListView(); // zlm 超级大乐透
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			showSubShiShiCaiListView(); // zlm 时时彩
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			showSubDlcCaiListView(); // zlm 11-5彩
			break;
		case NoticeActivityGroup.ID_SUB_SFC_LISTVIEW:
			showSubZuCaiListView("sfc"); // zlm 胜负彩
			break;
		case NoticeActivityGroup.ID_SUB_RXJ_LISTVIEW:
			showSubZuCaiListView("rxj");// zlm 任选九
			break;
		case NoticeActivityGroup.ID_SUB_LCB_LISTVIEW:
			showSubZuCaiListView("lcb"); // zlm 六场半
			break;
		case NoticeActivityGroup.ID_SUB_JQC_LISTVIEW:
			showSubZuCaiListView("jqc"); // zlm 进球彩
			break;
		}
	}
	/**
	 * 双色球子列表view
	 */
	private void showSubShuangSeQiuListView() {
		TextView noticePrizesTitle;

		setContentView(R.layout.notice_prizes_single_specific_main);

		list_ssq = getSubInfoForListView("ssq");//获取缓存中的数据 
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,	FINALDATE, MONEYSUM };
		// 设置双色球标题：双色球开奖公告
		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.shuangseqiu_kaijianggonggao);//双色球开奖公告
		noticePrizesTitle.setTextSize(20);


		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		// 返回主列表
		Button reBtn;
		reBtn = (Button) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		reBtn.setBackgroundResource(R.drawable.returnselecter);

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,list_ssq);
		listview.setAdapter(adapter);
        PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 福彩3D子列表
	 */
	private void showSubFuCai3DListView() {

		TextView noticePrizesTitle;

		setContentView(R.layout.notice_prizes_single_specific_main);
		// iQuitFlag=false;
		list_fc3d = getSubInfoForListView("fc3d");
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,FINALDATE, MONEYSUM };

		// 设置福彩3D标题：福彩3D开奖公告
		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.fucai3d_kaijianggonggao);


		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str, list_fc3d);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}   

	/**
	 * 七乐彩子列表
	 */
	private void showSubQiLeCaiListView() {

		TextView noticePrizesTitle;

		setContentView(R.layout.notice_prizes_single_specific_main);
		list_qlc = getSubInfoForListView("qlc");
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.qilecai_kaijianggonggao);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);


		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_qlc);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
		
	}

	/**
	 * 排列三子列表
	 */
	private void showSubPaiLieSanListView() {

		TextView noticePrizesTitle;

		setContentView(R.layout.notice_prizes_single_specific_main);
		list_pl3 = getSubInfoForListView("pl3");
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		// 设置福彩3D标题：福彩3D开奖公告
		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.pailiesan_kaijianggonggao);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);


		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str, list_pl3);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 排列五子列表
	 */
	private void showSubPL5ListView() {

		TextView noticePrizesTitle;

		setContentView(R.layout.notice_prizes_single_specific_main);
		list_pl5 = getSubInfoForListView("pl5");
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		// 设置福彩3D标题：福彩3D开奖公告
		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.pl5_kaijianggonggao);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str, list_pl5);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 七星彩子列表
	 */
	private void showSubQXCListView() {

		TextView noticePrizesTitle;

		setContentView(R.layout.notice_prizes_single_specific_main);
		list_qxc = getSubInfoForListView("qxc");
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.qxc_kaijianggonggao);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str, list_qxc);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 超级大乐透子列表
	 */
	private void showSubChaoJiDaLeTouListView() {

		TextView noticePrizesTitle;

		setContentView(R.layout.notice_prizes_single_specific_main);
		list_cjdlt = getSubInfoForListView("cjdlt");
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.chaojidaletou_kaijianggonggao);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);


		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str, list_cjdlt);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}

	/**
	 * 时时彩子列表
	 */
	private void showSubShiShiCaiListView() {

		TextView noticePrizesTitle;

		setContentView(R.layout.notice_prizes_single_specific_main);
		list_cjdlt = getSubInfoForListView("ssc");
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE, FINALDATE, MONEYSUM };

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.shishicai_kaijianggonggao);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);


		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str, list_cjdlt);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 11-5子列表
	 */
	private void showSubDlcCaiListView() {

		TextView noticePrizesTitle;

		setContentView(R.layout.notice_prizes_single_specific_main);
		list_cjdlt = getSubInfoForListView("11-5");
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE, FINALDATE, MONEYSUM };

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.dlc_kaijianggonggao);

//		attention = (TextView) findViewById(R.id.notice_prizes_single_specific_attention);
//		attention.setText("");

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);


		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str, list_cjdlt);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}

	/**
	 * 足彩子列表
	 */
	private void showSubZuCaiListView(String type) {

		TextView noticePrizesTitle;

		setContentView(R.layout.notice_prizes_single_specific_main);
		list_cjdlt = getSubInfoForListView(type);
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,FINALDATE, MONEYSUM };

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);

		if (type.equals("sfc")) {
			noticePrizesTitle.setText(R.string.shengfucai_kaijiang);
		} else if (type.equals("rxj")) {
			noticePrizesTitle.setText(R.string.renxuanjiu_kaijiang);
		} else if (type.equals("lcb")) {
			noticePrizesTitle.setText(R.string.liuchangban_kaijiang);
		} else if (type.equals("jqc")) {
			noticePrizesTitle.setText(R.string.jinqiucai_kaijiang);
		}

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		Log.v("zc",list_cjdlt+"");
		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,list_cjdlt);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 子列表中相应的数据
	 */
	protected static List<Map<String, Object>> getSubInfoForListView(String iGameName) {
		
		if(iGameName == null||iGameName.equals("")){
			return null;
		}
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(6);
		
		
		List<JSONObject> _list = null;
		if(iGameName.equals("ssq")){
			_list = Constants.ssqNoticeList;
		}else if(iGameName.equals("cjdlt")){
			_list = Constants.dltList;
		}else if(iGameName.equals("fc3d")){
			_list = Constants.fc3dList;
		}else if(iGameName.equals("pl3")){
			_list = Constants.pl3List;
		}else if(iGameName.equals("pl5")){
			_list = Constants.pl5List;
		}else if(iGameName.equals("qxc")){
			_list = Constants.qxcList;
		}else if(iGameName.equals("qlc")){
			_list = Constants.qlcList;
		}else if(iGameName.equals("ssc")){
			_list = Constants.sscList;
		}else if(iGameName.equals("sfc")){
			_list = Constants.sfcList;
		}else if(iGameName.equals("rxj")){
			_list = Constants.rx9List;
		}else if(iGameName.equals("lcb")){
			_list = Constants.half6List;
		}else if(iGameName.equals("jqc")){
			_list = Constants.jqcList;
		}else if(iGameName.equals("11-5")){
			_list = Constants.dlcList;
		}
		
		try {
			//将list 中的数据转换成map格式
			for (int i = 0; i < _list.size(); i++) {
				//int 从1开始是为了去除第一条数据
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject object = _list.get(i);
				map.put(LOTTERYTYPE, iGameName);
				map.put(WINNINGNUM, object.getString("winno"));
				map.put(DATE, "开奖日期： " + object.getString("date"));
				map.put(ISSUE, "第" + object.getString("lotno") + "期");
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}
	/**
	 * 子列表适配器
	 */
	public static class SubEfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		private String[] mIndex;
		private Context context;

		public SubEfficientAdapter(Context context, String[] index,
				List<Map<String, Object>> list) {
			this.context = context;
			mInflater = LayoutInflater.from(context);
			mList = list;
			mIndex = index;
		}

		@Override
		public int getCount() {
			Log.v("ListSize", mList.size()+"");
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public static final int[] colors = new int[] { 0x3000000, 0x300010ff };

		// 设置主列表布局中的详细内容
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String iGameType = (String) mList.get(position).get(mIndex[0]);
			String iNumbers = (String) mList.get(position).get(mIndex[1]);
			String iDate = (String) mList.get(position).get(mIndex[2]);
			String iIssueNo = (String) mList.get(position).get(mIndex[3]);
			ViewHolder holder = null;
            Log.v("notice_position", position+"");
				convertView = mInflater.inflate(R.layout.notice_prizes_single_specific_layout, null);

				holder = new ViewHolder();
				holder.numbers = (LinearLayout) convertView.findViewById(R.id.notice_pirzes_single_specific_ball_linearlayout);
				holder.date = (TextView) convertView.findViewById(R.id.notice_prizes_single_specific_noticedDate_id);
				holder.date.setText(iDate);
				holder.issue = (TextView) convertView.findViewById(R.id.notice_prizes_single_specific_issue_id);
				holder.issue.setText(iIssueNo);

				if (iGameType.equalsIgnoreCase("ssq")) {
					// zlm 7.28 代码修改：添加号码排序
					int i1, i2, i3;
					String iShowNumber;
					OneBallView tempBallView;
					int[] ssqInt01 = new int[6];
					int[] ssqInt02 = new int[6];
					String[] ssqStr = new String[6];

					for (i2 = 0; i2 < 6; i2++) {
						iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
						ssqInt01[i2] = Integer.valueOf(iShowNumber);
					}

					ssqInt02 = PublicMethod.sort(ssqInt01);

					for (i3 = 0; i3 < 6; i3++) {
						if (ssqInt02[i3] < 10) {
							ssqStr[i3] = "0" + ssqInt02[i3];
						} else {
							ssqStr[i3] = "" + ssqInt02[i3];
						}
					}
					for (i1 = 0; i1 < 6; i1++) {
						tempBallView = new OneBallView(convertView.getContext(),1);
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,ssqStr[i1], aRedColorResId);
						holder.numbers.addView(tempBallView);
					}

					iShowNumber = iNumbers.substring(12, 14);
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,aBlueColorResId);
					holder.numbers.addView(tempBallView);
					
				} else if (iGameType.equalsIgnoreCase("fc3d")) {

					int i1;
					// zlm 7.30 代码修改：修改福彩3D号码
					int iShowNumber;
					OneBallView tempBallView;
					for (i1 = 0; i1 < 3; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2, i1 * 2 + 2));
						tempBallView = new OneBallView(convertView.getContext(),1);
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);
						holder.numbers.addView(tempBallView);
					}
				} else if (iGameType.equalsIgnoreCase("qlc")) {
					// zlm 7.28 代码修改：添加号码排序
					int i1, i2, i3;
					String iShowNumber;
					OneBallView tempBallView;

					int[] ssqInt01 = new int[7];
					int[] ssqInt02 = new int[7];
					String[] ssqStr = new String[7];

					for (i2 = 0; i2 < 7; i2++) {
						iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
						ssqInt01[i2] = Integer.valueOf(iShowNumber);
					}

					ssqInt02 = PublicMethod.sort(ssqInt01);

					for (i3 = 0; i3 < 7; i3++) {
						if (ssqInt02[i3] < 10) {
							ssqStr[i3] = "0" + ssqInt02[i3];
						} else {
							ssqStr[i3] = "" + ssqInt02[i3];
						}
					}
					for (i1 = 0; i1 < 7; i1++) {

						tempBallView = new OneBallView(convertView.getContext(),1);
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								ssqStr[i1], aRedColorResId);

						holder.numbers.addView(tempBallView);
					}
					// zlm 8.3 代码修改 ：添加七乐彩蓝球
					iShowNumber = iNumbers.substring(14, 16);
					tempBallView = new OneBallView(convertView.getContext(),1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
							aBlueColorResId);

					holder.numbers.addView(tempBallView);
				} else if (iGameType.equalsIgnoreCase("pl3")) {

					int i1;
					int iShowNumber;
					OneBallView tempBallView;

					for (i1 = 0; i1 < 3; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1,i1 + 1));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""+ iShowNumber, aRedColorResId);
						holder.numbers.addView(tempBallView);
					}
				} else if (iGameType.equalsIgnoreCase("pl5")) {

					int i1;
					int iShowNumber;
					OneBallView tempBallView;

					for (i1 = 0; i1 < 5; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1,i1 + 1));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""+ iShowNumber, aRedColorResId);
						holder.numbers.addView(tempBallView);
					}
				}else if (iGameType.equalsIgnoreCase("qxc")) {

					int i1;
					int iShowNumber;
					OneBallView tempBallView;

					for (i1 = 0; i1 < 7; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1,i1 + 1));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""+ iShowNumber, aRedColorResId);
						holder.numbers.addView(tempBallView);
					}
				}else if (iGameType.equalsIgnoreCase("cjdlt")) {

					int i1, i2, i3;
					String iShowNumber;
					OneBallView tempBallView;
					int[] cjdltInt01 = new int[5];
					int[] cjdltInt02 = new int[5];
					int[] cjdltInt03 = new int[2];
					int[] cjdltInt04 = new int[2];
					String[] cjdltStr = new String[5];
					String[] cjdltStr1 = new String[2];

					for (i2 = 0; i2 < 5; i2++) {
						iShowNumber = iNumbers.substring(i2 * 3, i2 * 3 + 2);
						cjdltInt01[i2] = Integer.valueOf(iShowNumber);
					}

					cjdltInt02 = PublicMethod.sort(cjdltInt01);

					for (i3 = 0; i3 < 5; i3++) {
						if (cjdltInt02[i3] < 10) {
							cjdltStr[i3] = "0" + cjdltInt02[i3];
						} else {
							cjdltStr[i3] = "" + cjdltInt02[i3];
						}
					}
					for (i1 = 0; i1 < 5; i1++) {

						tempBallView = new OneBallView(convertView.getContext(),1);
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								cjdltStr[i1], aRedColorResId);

						holder.numbers.addView(tempBallView);
					}

					try {
						for (i2 = 0; i2 < 2; i2++) {
							iShowNumber = iNumbers.substring(i2 * 3 + 15,
									i2 * 3 + 17);
							cjdltInt03[i2] = Integer.valueOf(iShowNumber);
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

					cjdltInt04 = PublicMethod.sort(cjdltInt03);

					for (i3 = 0; i3 < 2; i3++) {
						if (cjdltInt04[i3] < 10) {
							cjdltStr1[i3] = "0" + cjdltInt04[i3];
						} else {
							cjdltStr1[i3] = "" + cjdltInt04[i3];
						}
					}

					for (i1 = 0; i1 < 2; i1++) {
						tempBallView = new OneBallView(convertView.getContext(),1);
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								cjdltStr1[i1], aBlueColorResId);

						holder.numbers.addView(tempBallView);
					}
				} else if (iGameType.equalsIgnoreCase("ssc")) {
					int i1;
					int iShowNumber;
					OneBallView tempBallView;
					for (i1 = 0; i1 < 5; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1,
								i1 + 1));
						tempBallView = new OneBallView(convertView.getContext(),1);
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);
						holder.numbers.addView(tempBallView);
					}
				} else if (iGameType.equalsIgnoreCase("11-5")) {
					int i1;
					int iShowNumber;
					OneBallView tempBallView;
					for (i1 = 0; i1 < 5; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1*2,i1*2 + 2));
						tempBallView = new OneBallView(convertView.getContext(),1);
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);
						holder.numbers.addView(tempBallView);
					}
				}else if (iGameType.equalsIgnoreCase("sfc")) {
					TextView tvFootball = new TextView(convertView.getContext());
					tvFootball.setTextColor( R.color.darkgreen);
					tvFootball.setTextSize(25);
					tvFootball.setGravity(Gravity.RIGHT);
					tvFootball.setText(iNumbers);
					holder.numbers.addView(tvFootball);
				} else if (iGameType.equalsIgnoreCase("rxj")) {
					TextView tvFootball = new TextView(convertView.getContext());
					tvFootball.setTextColor( R.color.darkgreen);
					tvFootball.setTextSize(25);
					tvFootball.setGravity(Gravity.RIGHT);
					tvFootball.setText(iNumbers);
					holder.numbers.addView(tvFootball);
				} else if (iGameType.equalsIgnoreCase("lcb")) {

					TextView tvFootball = new TextView(convertView.getContext());
					tvFootball.setTextColor( R.color.darkgreen);
					tvFootball.setTextSize(25);
					tvFootball.setGravity(Gravity.RIGHT);
					tvFootball.setText(iNumbers);
					holder.numbers.addView(tvFootball);
				} else if (iGameType.equalsIgnoreCase("jqc")) {
					TextView tvFootball = new TextView(convertView.getContext());
					tvFootball.setTextColor( R.color.darkgreen);
					tvFootball.setTextSize(25);
					tvFootball.setGravity(Gravity.RIGHT);
					tvFootball.setText(iNumbers);
					holder.numbers.addView(tvFootball);
				}
				convertView.setTag(holder);
			return convertView;
		}

		static class ViewHolder {
			LinearLayout numbers;
			TextView name;
			TextView date;
			TextView issue;

		}
	}
	protected void onStart() {
		super.onStart();
	}	
	protected void onResume() {
		super.onResume();
	}
	protected void onPause() {
		super.onPause();
	}
	protected void onStop() {
		super.onStop();
	}
}
