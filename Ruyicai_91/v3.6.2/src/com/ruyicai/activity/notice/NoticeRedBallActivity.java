/**
 * 
 */
package com.ruyicai.activity.notice;

import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;



/**
 * 红球走势图
 * @author Administrator
 *
 */
public class NoticeRedBallActivity extends NoticeBallActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		noticeAllNet(true);
		MobclickAgent.onEvent(NoticeRedBallActivity.this,"kaijiangzoushi" ); //BY贺思明 最新开奖页点击“开奖走势”tab切换。
	}
	
}
