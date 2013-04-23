/**
 * 
 */
package com.ruyicai.activity.notice;

import android.os.Bundle;



/**
 * ºìÇò×ßÊÆÍ¼
 * @author Administrator
 *
 */
public class NoticeRedBallActivity extends NoticeBallActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addBallView(NoticeActivityGroup.LOTNO,true);
	}
}
