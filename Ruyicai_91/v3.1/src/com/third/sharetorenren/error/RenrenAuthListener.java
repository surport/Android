/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.third.sharetorenren.error;

import android.os.Bundle;


/**
 * 监�?�认�?，和授�?�动作�?
 * 
 * @author �?�勇(yong.li@opi-corp.com) 2011-2-25
 */
public interface RenrenAuthListener {

    /**
     * 登录和授�?�完�?�?�调用�?
     * 
     * @param values key:授�?��?务器返回的�?�数�??，value:是�?�数值�?
     */
    public void onComplete(Bundle values);

    /**
     * �?务器返回错误
     * 
     * @param renrenAuthError
     */
    public void onRenrenAuthError(RenrenAuthError renrenAuthError);

    /**
     * 用户�?�消登录�?
     */
    public void onCancelLogin();

    /**
     * 用户�?�消授�?��?
     * 
     * @param values key:授�?��?务器返回的�?�数�??，value:是�?�数值�?
     */
    public void onCancelAuth(Bundle values);

}
