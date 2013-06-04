/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.third.sharetorenren.common;

import com.third.sharetorenren.exception.RenrenError;
import com.third.sharetorenren.exception.RenrenException;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * 
 * �?��平台各Api接口请求参数的抽象类
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public abstract class RequestParam {
	
	public abstract Bundle getParams() throws RenrenException;
	
	public void checkNullParams (String... params) throws RenrenException {
		
		for (String param : params) {
			if (TextUtils.isEmpty(param)) {
				String errorMsg = "required parameter MUST NOT be null";
				throw new RenrenException(
						RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg,
						errorMsg);
			}
		}
		
	}
}
