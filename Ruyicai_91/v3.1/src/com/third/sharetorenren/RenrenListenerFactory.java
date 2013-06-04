/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.third.sharetorenren;

import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.third.sharetorenren.error.RenrenAuthError;
import com.third.sharetorenren.view.RenrenAuthListener;
import com.third.sharetorenren.view.RenrenDialogListener;
import com.third.sharetorenren.view.RenrenWidgetListener;


/**
 * 构�?RenrenDialogListener的工厂；�?��者不会直接使用该类�?
 * 
 * @author 李勇(yong.li@opi-corp.com) 2011-2-25
 */
class RenrenListenerFactory {


    static RenrenDialogListener genRenrenWidgetDialogListener(final Renren renren,
            final RenrenWidgetListener listener) {
        return new AbstractRenrenDialogListener() {

            @Override
            public void onReceivedError(int errorCode, String description, String failingUrl) {
                Bundle params = new Bundle();
                params.putInt("code", errorCode);
                params.putString("desc", description);
                params.putString("failUrl", failingUrl);
                listener.onError(params);
            }

            @Override
            public int onPageBegin(String url) {
                if (url.startsWith(Renren.WIDGET_CALLBACK_URI)) {
                    Bundle values = Util.parseUrl(url);
                    String error = values.getString("error");
                    if (error == null) {
                        listener.onComplete(values);
                    } else if ("access_denied".equals(error)) {
                        listener.onCancel(values);
                    } else {
                        listener.onError(values);
                    }
                    return RenrenDialogListener.ACTION_PROCCESSED;
                }
                return RenrenDialogListener.ACTION_DIALOG_PROCCESS;
            }
        };
    }

    /**
     * User-Agent Flow
     * 
     * @param renren
     * @param listener
     * @return
     */
    static RenrenDialogListener genUserAgentFlowRenrenDialogListener(final Renren renren,
            final RenrenAuthListener listener, final String redirectUrl) {
        return new AbstractRenrenDialogListener() {

            @Override
            public void onReceivedError(int errorCode, String description, String failingUrl) {
                listener.onRenrenAuthError(new RenrenAuthError(String.valueOf(errorCode),
                        description, failingUrl));
            }

            @Override
            public int onPageBegin(String url) {
                int i = super.onPageBegin(url);
                if (i == RenrenDialogListener.ACTION_UNPROCCESS) {
                    if (url.startsWith("http://graph.renren.com/login_deny/")) {
                        return RenrenDialogListener.ACTION_DIALOG_PROCCESS;
                    } else {
                        i = this.checkUrl(url);
                    }
                }
                return i;
            }

            @Override
            public boolean onPageStart(String url) {
                boolean b = false;
                //在授权完成的时�?不调用onPageBegin方法，原因未知�?有时候装入页面的时�?会先调用onPageStart再调onPageBegin，原因未知难道是BUG�?
                if (url.startsWith(redirectUrl)) {
                    Bundle values = Util.parseUrl(url);
                    String accessToken = values.getString("access_token");
                    if (accessToken != null) {
                        this.authComplete(values);
                        b = true;
                    }
                }
                return b;
            }

            private int checkUrl(String url) {
                if (url.startsWith(redirectUrl)) {
                    Bundle values = Util.parseUrl(url);
                    String error = values.getString("error");//OAuth返回的错误代�?
                    if (error != null) {
                        if ("access_denied".equalsIgnoreCase(error)) {
                            listener.onCancelAuth(values);
                        } else if ("login_denied".equalsIgnoreCase(error)) {
                            listener.onCancelLogin();
                        } else {
                            String desc = values.getString("error_description");
                            String errorUri = values.getString("error_uri");
                            listener.onRenrenAuthError(new RenrenAuthError(error, desc, errorUri));
                        }
                    } else {
                        this.authComplete(values);
                    }
                    return RenrenDialogListener.ACTION_PROCCESSED;
                }
                return RenrenDialogListener.ACTION_UNPROCCESS;
            }

            private void authComplete(Bundle values) {
                CookieSyncManager.getInstance().sync();
                String accessToken = values.getString("access_token");
                if (accessToken != null) {
                    Log.d(Util.LOG_TAG, "Success obtain access_token=" + accessToken);
                    try {
                        renren.updateAccessToken(accessToken);
                        listener.onComplete(values);
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onRenrenAuthError(new RenrenAuthError(e.getClass().getName(), e
                                .getMessage(), e.toString()));
                    }
                }
            }
        };
    }

    /**
     * Web Server Flow
     * 
     * @param renren
     * @param listener
     * @param redirectUrl
     * @return
     */
    static RenrenDialogListener genWebServerFlowRenrenDialogListener(final Renren renren,
            final RenrenAuthListener listener, final String redirectUrl) {
        return new AbstractRenrenDialogListener() {

            @Override
            public void onReceivedError(int errorCode, String description, String failingUrl) {
                listener.onRenrenAuthError(new RenrenAuthError(String.valueOf(errorCode),
                        description, failingUrl));
            }

            @Override
            public int onPageBegin(String url) {
                int i = super.onPageBegin(url);
                if (url.startsWith("http://graph.renren.com/login_deny/")) {
                    return RenrenDialogListener.ACTION_DIALOG_PROCCESS;
                }
                if (url.startsWith(redirectUrl)) {
                    return RenrenDialogListener.ACTION_DIALOG_PROCCESS;
                }
                if (this.check(url)) {
                    return RenrenDialogListener.ACTION_PROCCESSED;
                }
                return i;
            }

            @Override
            public boolean onPageStart(String url) {
                return this.check(url);
            }

            private boolean check(String url) {
                if (url.startsWith(Renren.CANCEL_URI)) {
                    Bundle values = Util.parseUrl(url);
                    String action = values.getString("action");
                    if ("auth".equalsIgnoreCase(action)) {
                        listener.onCancelAuth(values);
                    } else {
                        listener.onCancelLogin();
                    }
                    return true;
                } else if (url.startsWith(Renren.SUCCESS_URI)) {
                    Bundle values = Util.parseUrl(url);
                    listener.onComplete(values);
                    return true;
                }
                return false;
            }

        };
    }
}

abstract class AbstractRenrenDialogListener implements RenrenDialogListener {

    @Override
    public int onPageBegin(String url) {
        if (url.contains("display")) {//参数中带有display参数的页面；在该WebView中显示�?
            return RenrenDialogListener.ACTION_DIALOG_PROCCESS;
        }
        return 0;
    }

    @Override
    public void onPageFinished(String url) {
    }

    @Override
    public boolean onPageStart(String url) {
        return false;
    }
}
