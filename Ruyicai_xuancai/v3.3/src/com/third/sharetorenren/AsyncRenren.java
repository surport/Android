/*
 * Copyright 2010 Renren, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.third.sharetorenren;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Bundle;

import com.third.sharetorenren.common.AbstractRequestListener;
import com.third.sharetorenren.exception.RenrenError;
import com.third.sharetorenren.status.StatusHelper;
import com.third.sharetorenren.status.StatusSetRequestParam;
import com.third.sharetorenren.status.StatusSetResponseBean;
import com.third.sharetorenren.users.UsersGetInfoHelper;
import com.third.sharetorenren.users.UsersGetInfoRequestParam;
import com.third.sharetorenren.users.UsersGetInfoResponseBean;
import com.third.sharetorenren.view.RenrenAuthListener;


/**
 * 对人人的请求封装成异步�?注意：使用该类调用人人接口时，不能在Listener中直接更新UI控件�? * 
 * @see Renren
 * @see RequestListener
 * 
 * @author yong.li@opi-corp.com
 * 
 */
public class AsyncRenren {

    private Renren renren;

    private Executor pool;

    public AsyncRenren(Renren renren) {
        this.renren = renren;
        this.pool = Executors.newFixedThreadPool(2);
    }

    /**
     * �?��登录
     * 
     * @param context
     * @param listener 注意监听器中不在主线程中执行，所以不能在监听器中直接更新UI代码�?     */
    public void logout(final Context context, final RequestListener listener) {
        pool.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    String resp = renren.logout(context);
                    RenrenError rrError = Util.parseRenrenError(resp, Renren.RESPONSE_FORMAT_JSON);
                    if (rrError != null) listener.onRenrenError(rrError);
                    else listener.onComplete(resp);
                } catch (Throwable e) {
                    listener.onFault(e);
                }
            }
        });
    }

    /**
     * 调用 人人 APIs，服务器的响应是JSON串�?
     * 
     * 人人 APIs 详细信息�?http://wiki.dev.renren.com/wiki/API
     * 
     * @param parameters 注意监听器中不在主线程中执行，所以不能在监听器中直接更新UI代码�?     * @param listener
     */
    public void requestJSON(Bundle parameters, RequestListener listener) {
        request(parameters, listener, Renren.RESPONSE_FORMAT_JSON);
    }

    /**
     * 调用 人人 APIs 服务器的响应是XML串�?
     * 
     * 人人 APIs 详细信息�?http://wiki.dev.renren.com/wiki/API
     * 
     * @param parameters 注意监听器中不在主线程中执行，所以不能在监听器中直接更新代码�?     * @param listener
     */
    public void requestXML(Bundle parameters, RequestListener listener) {
        request(parameters, listener, Renren.RESPONSE_FORMAT_XML);
    }

    /**
     * 调用 人人 APIs�?     * 
     * 人人 APIs 详细信息�?http://wiki.dev.renren.com/wiki/API
     * 
     * @param parameters
     * @param listener 注意监听器中不在主线程中执行，所以不能在监听器中直接更新UI代码�?     * @param format return data format (json or xml)
     */
    private void request(final Bundle parameters, final RequestListener listener,
            final String format) {
        pool.execute(new Runnable() {

            @Override
            public void run() {

                try {
                    String resp = "";
                    if ("xml".equalsIgnoreCase(format)) {
                        resp = renren.requestXML(parameters);
                    } else {
                        resp = renren.requestJSON(parameters);
                    }
                    RenrenError rrError = Util.parseRenrenError(resp, format);
                    if (rrError != null) {
                        listener.onRenrenError(rrError);
                    } else {
                        listener.onComplete(resp);
                    }
                } catch (Throwable e) {
                    listener.onFault(e);
                }
            }
        });
    }
    
	/**
	 * 异步方法<br>
	 * 使用用户名和密码完成登陆和授�?<br>
	 * 
	 * @see Renren.authorize
	 * @param param
	 *            请求对象
	 * @param listener
	 *            登陆状�?的监听器
	 */
	public void authorize(PasswordFlowRequestParam param, RenrenAuthListener listener) {
		PasswordFlowHelper passwordFlowHelper = new PasswordFlowHelper();
		passwordFlowHelper.login(pool, param, listener, renren);
	}
    
	/**
	 * users.getInfo接口<br>
	 * http://wiki.dev.renren.com/wiki/Users.getInfo
	 * @see Renren.getUsersInfo
	 * 
	 * @param param
	 *            请求参数
	 * @see Renren.getUsersInfo
	 */
    public void getUsersInfo (UsersGetInfoRequestParam param, AbstractRequestListener<UsersGetInfoResponseBean> listener) {
        UsersGetInfoHelper helper = new UsersGetInfoHelper(renren);
        helper.asyncGetUsersInfo(pool, param, listener);
    }
    
	/**
	 * 发状态，异步调用status.set
	 * 参见http://wiki.dev.renren.com/wiki/Status.set
	 * 
	 * @param status
	 *          要发布的状�?
	 * @param listener
	 *          用以监听发布状�?结果的监听器对象
	 * @param truncOption
	 *          若超出了长度，是否自动截短至140个字
	 */
    public void publishStatus(StatusSetRequestParam status,
    		AbstractRequestListener<StatusSetResponseBean> listener, 
    		boolean truncOption) {
    	StatusHelper helper = new StatusHelper(renren);
    	helper.asyncPublish(pool, status, listener, truncOption);
    }
    
}
