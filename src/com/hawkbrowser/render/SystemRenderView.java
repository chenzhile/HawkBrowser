
package com.hawkbrowser.render;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class SystemRenderView implements RenderView {

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    public SystemRenderView(Context context, AttributeSet attrs) {

        mWebView = new WebView(context, attrs);

        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
    }

    @Override
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public View getView() {
        return mWebView;
    }

    @Override
    public void requestFocus() {
        mWebView.requestFocus();
    }

    @Override
    public void destroy() {
        mWebView.destroy();
        mWebView = null;
    }

}
