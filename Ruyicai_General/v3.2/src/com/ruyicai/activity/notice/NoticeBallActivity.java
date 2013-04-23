package com.ruyicai.activity.notice;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.NoticeInterface;

/**
 * @author Administrator
 *
 */
public class NoticeBallActivity extends Activity{
	LinearLayout layout;
	NoticeBallView ballView;
	private boolean isRed;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_ball_main);
		layout = (LinearLayout) findViewById(R.id.notice_ball_main_linear);
		
	}
	/**
	 * 联网获取所有彩种开奖信息
	 */
	public void noticeAllNet(final boolean isRed){
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
						addBallView(isRed);
					}
				});
			}
			
		}).start();
		}else{
			addBallView(isRed);
		}
	}

	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * @param listViewID     列表ID
	 */
	public void addBallView(boolean isRed) {
		this.isRed = isRed;
		List<JSONObject> list;
		switch (NoticeActivityGroup.LOTNO) {
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("ssq");
			if(isRed){
				ballView.initNoticeBall(list.size(),33, 1, list,isRed,"ssq",1*NoticeMainActivity.SCALE);
			}else{
				ballView.initNoticeBall(list.size(),16, 1, list,isRed,"ssq",1*NoticeMainActivity.SCALE);
			}
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("fc3d");
			ballView.initNoticeBall(list.size(),10, 0, list,isRed,"fc3d",1*NoticeMainActivity.SCALE);
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("qlc");
			if(isRed){
				ballView.initNoticeBall(list.size(),30, 1, list,isRed,"qlc",1*NoticeMainActivity.SCALE);
			}else{
				ballView.initNoticeBall(list.size(),30, 1, list,isRed,"qlc",1*NoticeMainActivity.SCALE);
			}
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			// zlm 排列三
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("pl3");
			ballView.initNoticeBall(list.size(),10, 0, list,isRed,"pl3",1*NoticeMainActivity.SCALE);
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			// zlm 排列五
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("pl5");
			ballView.initNoticeBall(list.size(),10, 0, list,isRed,"pl5",1*NoticeMainActivity.SCALE);
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			// zlm 排列五
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("qxc");
			ballView.initNoticeBall(list.size(),10, 0, list,isRed,"qxc",1*NoticeMainActivity.SCALE);
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			// zlm 超级大乐透
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("cjdlt");
			if(isRed){
				ballView.initNoticeBall(list.size(),35, 1, list,isRed,"cjdlt",1*NoticeMainActivity.SCALE);
			}else{
				ballView.initNoticeBall(list.size(),12, 1, list,isRed,"cjdlt",1.3*NoticeMainActivity.SCALE);
			}
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			// zlm 时时彩
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("ssc");
			ballView.initNoticeBall(list.size(),10, 0, list,isRed,"ssc",1*NoticeMainActivity.SCALE);
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			// zlm 11-5彩
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("11-5");
			ballView.initNoticeBall(list.size(),11, 1, list,isRed,"11-5",1.3*NoticeMainActivity.SCALE);
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW:
			// zlm 11-5彩
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("11-ydj");
			ballView.initNoticeBall(list.size(),11, 1, list,isRed,"11-ydj",1.3*NoticeMainActivity.SCALE);
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			ballView = new NoticeBallView(this);
			list = getSubInfoForListView("22-5");
			ballView.initNoticeBall(list.size(),22, 1, list,isRed,"22-5",1*NoticeMainActivity.SCALE);
			layout.addView(ballView);
			break;
		case NoticeActivityGroup.ID_SUB_SFC_LISTVIEW:
			// zlm 胜负彩
		
			break;
		case NoticeActivityGroup.ID_SUB_RXJ_LISTVIEW:
			// zlm 任选九

			break;
		case NoticeActivityGroup.ID_SUB_LCB_LISTVIEW:
			// zlm 六场半

			break;
		case NoticeActivityGroup.ID_SUB_JQC_LISTVIEW:
			// zlm 进球彩
	
			break;
		}
	}
	/**
	 * 子列表中相应的数据
	 */
	protected static List<JSONObject> getSubInfoForListView(String iGameName) {
		
		if(iGameName == null||iGameName.equals("")){
			return null;
		}
		
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
		return _list;
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