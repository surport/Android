/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.third.sharetorenren.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.third.sharetorenren.AsyncRenren;
import com.third.sharetorenren.Renren;
import com.third.sharetorenren.common.AbstractRequestListener;
import com.third.sharetorenren.exception.RenrenError;
import com.third.sharetorenren.users.UserInfo;
import com.third.sharetorenren.users.UsersGetInfoRequestParam;
import com.third.sharetorenren.users.UsersGetInfoResponseBean;


/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 * 用于显示用户姓名的view
 *
 */
public class ProfileNameView extends TextView {
	
	/**
	 * fade in 动画的持续时�?	 */
	private static final int ANIM_DURATION = 800;

	public ProfileNameView(Context context) {
		super(context);
		init();
	}

	public ProfileNameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ProfileNameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private Handler handler;
	
	private void init () {
		handler = new Handler();
	}
	
	/**
	 * 设置当前用户的uid，此空间会获取用户的姓名并显�?	 * @param uid 当前用户的uid 
	 * @param renren renren对象
	 */
	public void setUid(final long uid, final Renren renren) {
		
		if (renren == null) {
			return;
		}
		
		AsyncRenren asyncRenren = new AsyncRenren(renren);
		UsersGetInfoRequestParam param = new UsersGetInfoRequestParam(new String[]{String.valueOf(uid)});
		param.setFields(UserInfo.KEY_NAME);
		AbstractRequestListener<UsersGetInfoResponseBean> listener = new AbstractRequestListener<UsersGetInfoResponseBean>() {

			@Override
			public void onComplete(final UsersGetInfoResponseBean bean) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if (bean != null && bean.getUsersInfo().size() > 0) {
							UserInfo info = bean.getUsersInfo().get(0);
							String name = info.getName();
							if (name != null) {
								
								Animation animation = AnimationUtils.loadAnimation(ProfileNameView.this.getContext(), android.R.anim.fade_in);
								animation.setDuration(ANIM_DURATION);
								setText(name);
								startAnimation(animation);
							}
						}
						
					}
				});
			}

			@Override
			public void onRenrenError(RenrenError renrenError) {
			}

			@Override
			public void onFault(Throwable fault) {
			}
		};
		asyncRenren.getUsersInfo(param, listener);

	}
	
	

}
