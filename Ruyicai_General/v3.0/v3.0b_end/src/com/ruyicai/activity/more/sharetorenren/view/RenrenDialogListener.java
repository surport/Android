/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.ruyicai.activity.more.sharetorenren.view;

/**
 * 监听RenrenDialog，开发�?不会直接使用该类�?
 * 
 * @author 李勇(yong.li@opi-corp.com) 2011-2-25
 */
public interface RenrenDialogListener {

    /**
     * 未处�?
     */
    public final static int ACTION_UNPROCCESS = 0;

    /**
     * 已处�?
     */
    public final static int ACTION_PROCCESSED = 1;

    /**
     * 由Dialog处理
     */
    public final static int ACTION_DIALOG_PROCCESS = 2;

    /**
     * 页面加载之前调用�?
     * 
     * @param url
     * @return 0:未处理，1:已经处理�?:由Dialog处理
     */
    public int onPageBegin(String url);

    /**
     * 页面�?��加载时调用�?
     * 
     * @param url
     * @return
     */
    public boolean onPageStart(String url);

    /**
     * 页面加载结束调用�?
     * 
     * @param url
     */
    public void onPageFinished(String url);

    /**
     * 出现错误调用�?
     * 
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    public void onReceivedError(int errorCode, String description, String failingUrl);
}
