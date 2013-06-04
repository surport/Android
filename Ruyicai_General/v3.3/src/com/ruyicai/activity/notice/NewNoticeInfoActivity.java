package com.ruyicai.activity.notice;

import com.ruyicai.activity.notice.LotnoDetail.DLTDetailView;
import com.ruyicai.activity.notice.LotnoDetail.FC3DetailView;
import com.ruyicai.activity.notice.LotnoDetail.LotnoDetailView;
import com.ruyicai.activity.notice.LotnoDetail.PL3DetailView;
import com.ruyicai.activity.notice.LotnoDetail.PL5DetailView;
import com.ruyicai.activity.notice.LotnoDetail.QLCDetailView;
import com.ruyicai.activity.notice.LotnoDetail.QXCDetailView;
import com.ruyicai.activity.notice.LotnoDetail.SsqDetailView;
import com.ruyicai.activity.notice.LotnoDetail.TwentyDetailView;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class NewNoticeInfoActivity extends Activity{
	Activity context;
	public ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		progress = UserCenterDialog.onCreateDialog(this);
		context = this;
		initDialog(NoticeActivityGroup.LOTNO);
	}
	/**
	 * @param listViewID
	 *            ¡–±ÌID
	 */
	private void initDialog(int listViewID) {
		switch (listViewID) {
		
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			SsqDetailView ssqDetailView = new SsqDetailView(context,Constants.LOTNO_SSQ,NoticeActivityGroup.ISSUE,progress,new Handler(),false);
			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			FC3DetailView fc3DetailView = new FC3DetailView(context,Constants.LOTNO_FC3D,NoticeActivityGroup.ISSUE,progress,new Handler(),false);
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			QLCDetailView qlcDetailView = new QLCDetailView(context,Constants.LOTNO_QLC,NoticeActivityGroup.ISSUE,progress,new Handler(),false);
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			PL3DetailView pl3DetailView = new PL3DetailView(context,Constants.LOTNO_PL3,NoticeActivityGroup.ISSUE,progress,new Handler(),false);
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			DLTDetailView dltDetailView = new DLTDetailView(context,Constants.LOTNO_DLT,NoticeActivityGroup.ISSUE,progress,new Handler(),false);
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			PL5DetailView pl5DetailView = new PL5DetailView(context,Constants.LOTNO_PL5,NoticeActivityGroup.ISSUE,progress,new Handler(),false);
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			QXCDetailView qxcDetailView = new QXCDetailView(context,Constants.LOTNO_QXC,NoticeActivityGroup.ISSUE,progress,new Handler(),false);
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			TwentyDetailView twentyDetailView = new TwentyDetailView(context,Constants.LOTNO_22_5,NoticeActivityGroup.ISSUE,progress,new Handler(),false);
			break;
		}
	}
}
