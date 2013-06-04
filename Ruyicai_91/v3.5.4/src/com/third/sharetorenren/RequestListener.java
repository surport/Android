/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.third.sharetorenren;

import com.third.sharetorenren.exception.RenrenError;

/**
 * 对人API请求结果监听
 * 
 * 不要在该接口的方法中更新UI，一般来说这些代码不是在UI线程（主线程）中运行的
 * 
 * @author 李勇(yong.li@opi-corp.com) 2010-7-15
 */
public interface RequestListener {

    /**
     * 当请求完成后调用
     * 
     * @param response 服务器返回的结果，一般是JSON或XM
     *        (根据你调用的AsyncRenren.requestJSON还是AsyncRenren .requestXM
     */
    public void onComplete(String response);

    /**
     * 服务器返回了错误结果，已经正确的链接上了服务器但有错误如：缺少参数sessionKey过期等
     * 
     * @param renrenError
     */
    public void onRenrenError(RenrenError renrenError);

    /**
     * 在请求期间发生了严重问题（如：网络故障访问的地存在等）
     * 
     * @param fault
     */
    public void onFault(Throwable fault);

}
