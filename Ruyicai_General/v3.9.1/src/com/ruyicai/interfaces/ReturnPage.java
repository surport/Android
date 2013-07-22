/**
 * 
 */
package com.ruyicai.interfaces;

import android.content.Context;
import android.view.View;

/**
 * 界面切换的接口
 * 
 * @author Administrator
 * 
 */
public interface ReturnPage {
	public int iQuitFlag = 0; // 代表退出

	public void returnMain();// 返回主列表

	public void switchView(View view);// 切换界面

	public Context getContext();// 得到context对象

	public void showDialog();// 创建等待窗口

	public void dismissDialog();// 取消等待窗口
}
