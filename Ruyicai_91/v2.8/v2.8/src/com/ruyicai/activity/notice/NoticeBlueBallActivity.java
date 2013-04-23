package com.ruyicai.activity.notice;

import android.os.Bundle;

/**
 * À¶Çò×ßÊÆÍ¼
 * @author Administrator
 *
 */
public class NoticeBlueBallActivity extends NoticeBallActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addBallView(NoticeActivityGroup.LOTNO,false);
		
	}
}
