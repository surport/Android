/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.third.sharetorenren.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.third.sharetorenren.exception.RenrenError;


/**
 * 对发布状态�?新鲜事�?照片等请求进行响应的listener的抽象类
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public abstract class AbstractRequestListener<T extends ResponseBean> {
	
	/**
	 * 将response请求解析为针对具体请求的bean
	 * 
	 * @param response 
	 * 			请求完成后的响应字符�?	 * @return 
	 * 			若解析成功，返回解析后的对象，否则返回null
	 */
	@SuppressWarnings("unchecked")
    public T parse(String response) {
	    Class<?> c = this.getGenericType();
        try {
            Constructor<T> constructor = (Constructor<T>) c.getDeclaredConstructor(String.class);
            T result = constructor.newInstance(response);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	
	/**
	 * 获取T的类�?	 * @param index
	 * @return
	 */
	private Class<?> getGenericType() {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (params.length < 1) {
            throw new RuntimeException("Index outof bounds");
        }
        if (!(params[0] instanceof Class)) {
            return Object.class;
        }
        return (Class<?>) params[0];
    }
	
	 /**
     * 请求完成后以对象形式返回服务器的响应的结�?     * 
     * @param bean 
     * 			服务器返回的响应字符串解析后得到的对�?     *        
     */
    public abstract void onComplete(T bean);

    /**
     * 服务器返回了错误结果，已经正确的链接上了服务器但有错误如：缺少参数�?sessionKey过期等�?
     * 
     * @param renrenError
     */
    public abstract void onRenrenError(RenrenError renrenError);

    /**
     * 在请求期间发生了严重问题（如：网络故障�?访问的地�?��存在等）
     * 
     * @param fault
     */
    public abstract void onFault(Throwable fault);
}
