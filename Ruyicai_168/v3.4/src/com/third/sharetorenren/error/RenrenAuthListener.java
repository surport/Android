/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.third.sharetorenren.error;

import android.os.Bundle;


/**
 * 
 * 
 * @author (yong.li@opi-corp.com) 2011-2-25
 */
public interface RenrenAuthListener {

    /**
     * 登录和调用     * 
     * @param values key:授务器返回的，value:是数
     */
    public void onComplete(Bundle values);

    /**
     *务器返回错误
     * 
     * @param renrenAuthError
     */
    public void onRenrenAuthError(RenrenAuthError renrenAuthError);

    /**
     * 用户
     */
    public void onCancelLogin();

    /**
     * 用户
     * 
     * @param values k
     */
    public void onCancelAuth(Bundle values);

}
