/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.third.sharetorenren.status;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.third.sharetorenren.common.RequestParam;
import com.third.sharetorenren.exception.RenrenError;
import com.third.sharetorenren.exception.RenrenException;


/**
 * 人人网开放平台Status.set状�?请求接口
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public class StatusSetRequestParam extends RequestParam implements Parcelable{
	
	/**
	 * 请求的api
	 */
	private static final String METHOD = "status.set";
	
	/**
	 * �?��人人状�?的字符长度上�?	 */
	public static final int MAX_LENGTH = 140;
	
	/**
	 * 状�?的label，一般用来作为在Bundle等对象中使用时的key�?	 */
	public static final String STATUS_LABEL = "status_set_request_param";
	
	/**
	 * 状�?文本信息
	 */
	private String status;
	
	public StatusSetRequestParam(String status) {
		this.status = status;
	}
	
	public StatusSetRequestParam(Parcel in) {
		status = in.readString();
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 根据状�?的长度上限对状�?进行截断
	 * @return 
	 * 		在长度上限范围内的状态�?若传入的status为空，则返回null
	 */
	public StatusSetRequestParam trim() {
		if(status == null) {
			return null;
		}
		
		if(status.length() > StatusSetRequestParam.MAX_LENGTH) {
			status = status.substring(0, StatusSetRequestParam.MAX_LENGTH);
		}
		return new StatusSetRequestParam(status);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if(status != null) {
			dest.writeString(status);
		}
	}

	public static final Parcelable.Creator<StatusSetRequestParam> CREATOR
             = new Parcelable.Creator<StatusSetRequestParam>() {
         public StatusSetRequestParam createFromParcel(Parcel in) {
             return new StatusSetRequestParam(in);
         }

         public StatusSetRequestParam[] newArray(int size) {
             return new StatusSetRequestParam[size];
         }
    };

	@Override
	public Bundle getParams() throws RenrenException {	
		
		if (status == null || status.length() == 0) {
			String errorMsg = "Cannot send null status.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg,
					errorMsg);
		}
		
		if (status.length() > MAX_LENGTH) {
			String errorMsg = "The length of the status should be smaller than "
					+ StatusSetRequestParam.MAX_LENGTH + " characters.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_PARAMETER_EXTENDS_LIMIT, errorMsg,
					errorMsg);
		}
		
		Bundle params = new Bundle();
		params.putString("method", METHOD);
		params.putString("status", status);
		return params;
	}
}
