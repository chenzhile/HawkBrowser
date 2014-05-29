
package com.hawkbrowser.render;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SystemRenderView extends RenderView {

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    public SystemRenderView(Context context, AttributeSet attrs) {

        mWebView = new WebView(context, attrs);

        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

    @Override
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
        requestFocus();
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
    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    @Override
    public void goBack() {
        mWebView.goBack();
    }

    @Override
    public boolean canGoForward() {
        return mWebView.canGoForward();
    }

    @Override
    public void goForward() {
        mWebView.goForward();
    }

    @Override
    public void stopLoading() {
        mWebView.stopLoading();
    }

    @Override
    public void reload() {
        mWebView.reload();
    }

    @Override
    public void destroy() {
        super.destroy();

        mWebView.destroy();
        mWebView = null;
    }
}
