/**
 * 
 */
package com.ruyicai.activity.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.notice.LotnoDetail.DLTDetailView;
import com.ruyicai.activity.notice.LotnoDetail.FC3DetailView;
import com.ruyicai.activity.notice.LotnoDetail.PL3DetailView;
import com.ruyicai.activity.notice.LotnoDetail.PL5DetailView;
import com.ruyicai.activity.notice.LotnoDetail.QLCDetailView;
import com.ruyicai.activity.notice.LotnoDetail.QXCDetailView;
import com.ruyicai.activity.notice.LotnoDetail.SsqDetailView;
import com.ruyicai.activity.notice.LotnoDetail.TwentyDetailView;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.NoticeInterface;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.ruyicai.util.PublicMethod;

/**
 * @author Administrator
 *
 */// implements OnScrollListener
public class NoticeInfoActivity extends Activity{
	public final static String LOTTERYTYPE = "LOTTERYTYPE";
	public final static String WINNINGNUM = "WINNINGNUM";
	public final static String DATE = "DATA";
	public final static String ISSUE = "ISSUE";
	public final static String FINALDATE = "FINALDATE";
	public final static String MONEYSUM = "MONEYSUM";
    private Handler handler = new Handler();  
	public ProgressDialog progress;
	String Lotno,LotLalel;
	int lotType = 0;
	ProgressBar progressbar;//列表获取更多的progressbar
	LayoutInflater mInflater;
	private final int LISTSSQ = 0,LIST3D = 1,LISTQLC = 2,LISTPL3 = 3,LISTDLT = 4,LISTSSC = 5
					,LIST115 = 6,LISTSFC = 7,LISTRX9 = 8,LISTLCB = 9,LISTJQC = 10
					,LISTPL5 = 11,LISTQXC = 12,LISTYDJ = 13,LISTTWENTY = 14;
	List<Map<String,Object>> adpterlist; // zlm 8.9 添加排列三、超级大乐透
	TextView noticePrizesTitle;
	SubEfficientAdapter adapter;
	View addMoreView;
	Button reBtn;
	ListView listview;
	String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,FINALDATE, MONEYSUM };
	int pageindex = 1;
	int totalItem = 24;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progress = UserCenterDialog.onCreateDialog(this);
		setContentView(R.layout.notice_prizes_single_specific_main);
		noticeAllNet();
	}
	/**
	 * 联网获取所有彩种开奖信息
	 */
	public void noticeAllNet(){
		if(NoticeMainActivity.isFirstNotice){
		final ProgressDialog dialog = UserCenterDialog.onCreateDialog(this);
		dialog.show();
		final Handler handler =  new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
					JSONObject lotteryInfos = NoticeInterface.getInstance().getLotteryAllNotice();//开奖信息json对象
					//将获取到的开奖信息放到常量类中
					NoticeActivityGroup.analysisLotteryNoticeJsonObject(lotteryInfos);
				    dialog.cancel();
				handler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						initList();
					}
				});
			}
			
		}).start();
		}else{
			initList();
		}
	}
	private void initList(){
		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);
		mInflater =  LayoutInflater.from(this);
		addMoreView = mInflater.inflate(R.layout.lookmorebtn, null);
		progressbar=(ProgressBar)addMoreView.findViewById(R.id.getmore_progressbar);
	    listview.addFooterView(addMoreView);
		
		
		noticePrizesTitle.setTextSize(20);
		reBtn = (Button) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		reBtn.setBackgroundResource(R.drawable.returnselecter);
		showListView(NoticeActivityGroup.LOTNO);
		addMoreView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				addMoreView.setEnabled(false);
	            getMore();
			}
		});
	}
	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * @param listViewID
	 *            列表ID
	 */
	private void showListView(int listViewID) {
		switch (listViewID) {
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			Lotno = Constants.LOTNO_SSQ;
			LotLalel = Constants.SSQLABEL;
			lotType = LISTSSQ;
			showSubShuangSeQiuListView();
			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			Lotno = Constants.LOTNO_FC3D;
			LotLalel = Constants.FC3DLABEL;
			lotType = LIST3D;
			showSubFuCai3DListView();
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			Lotno = Constants.LOTNO_QLC;
			LotLalel = Constants.QLCLABEL;
			lotType = LISTQLC;
			showSubQiLeCaiListView();
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			Lotno = Constants.LOTNO_PL3;
			LotLalel = Constants.PL3LABEL;
			lotType = LISTPL3;
			showSubPaiLieSanListView(); // zlm 排列三
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			Lotno = Constants.LOTNO_PL5;
			LotLalel = Constants.PL5LABEL;
			lotType = LISTPL5;
			showSubPL5ListView(); // zlm 排列五
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			Lotno = Constants.LOTNO_QXC;
			LotLalel = Constants.QXCLABEL;
			lotType = LISTQXC;
			showSubQXCListView(); // zlm 七星彩
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			Lotno = Constants.LOTNO_DLT;
			LotLalel = Constants.DLTLABEL;
			lotType = LISTDLT;
			showSubChaoJiDaLeTouListView(); // zlm 超级大乐透
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			Lotno = Constants.LOTNO_SSC;
			LotLalel = Constants.SSCLABEL;
			lotType = LISTSSC;
			showSubShiShiCaiListView(); // zlm 时时彩
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			Lotno = Constants.LOTNO_11_5;
			LotLalel = Constants.DLCLABEL;
			lotType = LIST115;
			showSubDlcCaiListView(); // zlm 11-5彩
			break;
		case NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW:
			Lotno = Constants.LOTNO_eleven;
			LotLalel = Constants.YDJLABEL;
			lotType = LISTYDJ;
			showSubYdjCaiListView(); // zlm 11运夺金
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			Lotno = Constants.LOTNO_22_5;
			LotLalel = Constants.TWENTYBEL;
			lotType = LISTTWENTY;
			showSubTwentyListView();
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
		adpterlist = getSubInfoForListView("ssq");//获取缓存中的数据 
		noticePrizesTitle.setText(R.string.shuangseqiu_kaijianggonggao);//双色球开奖公告
		adapter = new SubEfficientAdapter(this, str,adpterlist,LISTSSQ,true);
		listview.setAdapter(adapter);
        PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 福彩3D子列表
	 */
	private void showSubFuCai3DListView() {
		adpterlist = getSubInfoForListView("fc3d");
		noticePrizesTitle.setText(R.string.fucai3d_kaijianggonggao);
		adapter = new SubEfficientAdapter(this, str, adpterlist,LIST3D,true);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}   

	/**
	 * 七乐彩子列表
	 */
	private void showSubQiLeCaiListView() {
		adpterlist = getSubInfoForListView("qlc");
		noticePrizesTitle.setText(R.string.qilecai_kaijianggonggao);
		adapter = new SubEfficientAdapter(this, str,adpterlist,LISTQLC,true);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}

	/**
	 * 排列三子列表
	 */
	private void showSubPaiLieSanListView() {
		adpterlist = getSubInfoForListView("pl3");
		noticePrizesTitle.setText(R.string.pailiesan_kaijianggonggao);
		adapter = new SubEfficientAdapter(this, str, adpterlist,LISTPL3,true);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 排列五子列表
	 */
	private void showSubPL5ListView() {
		adpterlist = getSubInfoForListView("pl5");
		noticePrizesTitle.setText(R.string.pl5_kaijianggonggao);
		adapter = new SubEfficientAdapter(this, str, adpterlist,LISTPL5,true);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 七星彩子列表
	 */
	private void showSubQXCListView() {
		adpterlist = getSubInfoForListView("qxc");
		noticePrizesTitle.setText(R.string.qxc_kaijianggonggao);
		adapter = new SubEfficientAdapter(this, str, adpterlist,LISTQXC,true);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 超级大乐透子列表
	 */
	private void showSubChaoJiDaLeTouListView() {
		adpterlist = getSubInfoForListView("cjdlt");
		noticePrizesTitle.setText(R.string.chaojidaletou_kaijianggonggao);
		adapter = new SubEfficientAdapter(this, str, adpterlist,LISTDLT,true);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}

	/**
	 * 时时彩子列表
	 */
	private void showSubShiShiCaiListView() {
		adpterlist = getSubInfoForListView("ssc");
		noticePrizesTitle.setText(R.string.shishicai_kaijianggonggao);
		adapter = new SubEfficientAdapter(this, str, adpterlist,LISTSSC,false);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 11-5子列表
	 */
	private void showSubDlcCaiListView() {
		adpterlist = getSubInfoForListView("11-5");
		noticePrizesTitle.setText(R.string.dlc_kaijianggonggao);
		adapter = new SubEfficientAdapter(this, str, adpterlist,LIST115,false);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 11运夺金子列表
	 */
	private void showSubYdjCaiListView() {
		adpterlist = getSubInfoForListView("11-ydj");
		noticePrizesTitle.setText(R.string.ydj_kaijianggonggao);
		adapter = new SubEfficientAdapter(this, str, adpterlist,LISTYDJ,false);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 22选5
	 */
	private void showSubTwentyListView() {
		adpterlist = getSubInfoForListView("22-5");
		noticePrizesTitle.setText(R.string.ydj_kaijianggonggao);
		adapter = new SubEfficientAdapter(this, str, adpterlist,LISTTWENTY,true);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}
	/**
	 * 足彩子列表
	 */
	private void showSubZuCaiListView(String type) {
		adpterlist = getSubInfoForListView(type);
		if (type.equals("sfc")) {
			noticePrizesTitle.setText(R.string.shengfucai_kaijiang);
		} else if (type.equals("rxj")) {
			noticePrizesTitle.setText(R.string.renxuanjiu_kaijiang);
		} else if (type.equals("lcb")) {
			noticePrizesTitle.setText(R.string.liuchangban_kaijiang);
		} else if (type.equals("jqc")) {
			noticePrizesTitle.setText(R.string.jinqiucai_kaijiang);
		}
		adapter = new SubEfficientAdapter(this, str,adpterlist,LISTSFC,false);
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
		}else if(iGameName.equals("11-ydj")){
			_list = Constants.ydjList;
		}else if(iGameName.equals("22-5")){
			_list = Constants.twentylist;
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
				map.put(ISSUE, object.getString("lotno") );
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
	public  class SubEfficientAdapter extends BaseAdapter {
		int count = 0;
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		private String[] mIndex;
		private Activity context;
		private boolean isImg = true;
		public SubEfficientAdapter(Activity context, String[] index,
				List<Map<String, Object>> list,int lotType, boolean isImg) {
			this.context = context;
			this.mInflater = LayoutInflater.from(context);
			this.mList = list;
			this.mIndex = index;
			this.isImg = isImg;
			
		}
		@Override
		public int getCount() {
			count = mList.size();
			return count;
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public  final int[] colors = new int[] { 0x3000000, 0x300010ff };

		// 设置主列表布局中的详细内容
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			String iGameType = (String) mList.get(position).get(mIndex[0]);
			final String iNumbers = (String) mList.get(position).get(mIndex[1]);
			String iDate = (String) mList.get(position).get(mIndex[2]);
			final String iIssueNo = (String) mList.get(position).get(mIndex[3]);
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.notice_prizes_single_specific_layout, null);
				holder = new ViewHolder();
				holder.numbers = (LinearLayout) convertView.findViewById(R.id.notice_pirzes_single_specific_ball_linearlayout);
				holder.date = (TextView) convertView.findViewById(R.id.notice_prizes_single_specific_noticedDate_id);
				holder.issue = (TextView) convertView.findViewById(R.id.notice_prizes_single_specific_issue_id);
				holder.imgView = (ImageView) convertView.findViewById(R.id.notice_prizes_single_specific_img);
				if(isImg){
					holder.imgView.setVisibility(ImageView.VISIBLE);
				}else{
					holder.imgView.setVisibility(ImageView.GONE);
				}
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			PrizeBallLinearLayout linear = new PrizeBallLinearLayout(context);
			linear.Lotname = iGameType;
			linear.Batchcode = iNumbers;
			linear.linear = holder.numbers;
			linear.removeAllBalls();
			linear.initLinear();
			
			holder.date.setText(iDate);
			holder.issue.setText("第"+iIssueNo+"期");
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(isImg){
						switch (lotType){
						case LISTSSQ:
							SsqDetailView ssqDetailView = new SsqDetailView(context,Constants.LOTNO_SSQ,iIssueNo,progress,new Handler(),true);
							break;

						case LIST3D:
							FC3DetailView fc3DetailView = new FC3DetailView(context,Constants.LOTNO_FC3D,iIssueNo,progress,new Handler(),true);
							break;

						case LISTQLC:
							QLCDetailView qlcDetailView = new QLCDetailView(context,Constants.LOTNO_QLC,iIssueNo,progress,new Handler(),true);
							break;
						case LISTDLT:
							DLTDetailView dltDetailView = new DLTDetailView(context,Constants.LOTNO_DLT,iIssueNo,progress,new Handler(),true);
							break;
						case LISTPL3:
							PL3DetailView pl3DetailView = new PL3DetailView(context,Constants.LOTNO_PL3,iIssueNo,progress,new Handler(),true);
							break;
						case LISTPL5:
							PL5DetailView pl5DetailView = new PL5DetailView(context,Constants.LOTNO_PL5,iIssueNo,progress,new Handler(),true);
							break;
						case LISTQXC:
							QXCDetailView qxcDetailView = new QXCDetailView(context,Constants.LOTNO_QXC,iIssueNo,progress,new Handler(),true);
							break;
						case LISTTWENTY:
							TwentyDetailView twentview = new TwentyDetailView(context, Constants.LOTNO_22_5, iIssueNo, progress,new Handler(),true);
					
						}
					}

				}
			});
			return convertView;
		}

		 class ViewHolder {
			LinearLayout numbers;
			TextView name;
			TextView date;
			TextView issue;
			ImageView imgView;
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
	private void netting(){
		progressbar.setVisibility(ProgressBar.VISIBLE);
		final Handler tHandler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
		totalItem =  adapter.count;
		pageindex = totalItem/12+1;
		final JSONObject prizemore = PrizeInfoInterface.getInstance().getNoticePrizeInfo(Lotno, pageindex+"", "12");
		try {
			final String msg = prizemore.getString("message");
			final String code = prizemore.getString("error_code");
			if(code.equals("0000")){
				JsonToString(prizemore);
				tHandler.post(new Runnable() {
					@Override
					public void run() {
						progressbar.setVisibility(ProgressBar.INVISIBLE);
						addMoreView.setEnabled(true);
						adapter.notifyDataSetChanged();
					}
				 });
			}else{
				tHandler.post(new Runnable() {
					@Override
					public void run() {
						progressbar.setVisibility(ProgressBar.INVISIBLE);
						addMoreView.setEnabled(true);
						Toast.makeText(NoticeInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				 });
			}
		}catch (JSONException e) {
				// TODO: handle exception
		}
	   }

	
	 }).start();
		
	}
	private void JsonToString(JSONObject prizemore)
			throws JSONException {
		JSONArray prizeArray = prizemore.getJSONArray("result");
		for (int i = 0; i < prizeArray.length(); i++) {
			JSONObject prizeJson = (JSONObject) prizeArray.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, LotLalel);
			map.put(WINNINGNUM, prizeJson.getString("winCode"));
			map.put(DATE, "开奖日期： " + prizeJson.getString("openTime"));
			map.put(ISSUE,  prizeJson.getString("batchCode") );
			adpterlist.add(map);
		}
	}
	  private void getMore(){
			netting();
			
	  }
}
