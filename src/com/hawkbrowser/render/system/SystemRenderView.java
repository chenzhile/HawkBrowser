
package com.hawkbrowser.render.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hawkbrowser.render.RenderView;
import com.hawkbrowser.render.ValueCallbackAdapter;

public class SystemRenderView extends RenderView {

    private WebView mWebView;
    private SystemRenderObserverAdapter mSystemRenderObserver;

    @SuppressLint("SetJavaScriptEnabled")
    public SystemRenderView(Context context, AttributeSet attrs) {

        mWebView = new WebView(context, attrs);

        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setBuiltInZoomControls(true);

        mSystemRenderObserver = new SystemRenderObserverAdapter(this, mWebView, mObservers);
    }

    @Override
    public void loadUrl(String url) {
        
        if (!url.startsWith("http://") && !url.startsWith("about:")) {
            url = "http://" + url;
        }
        
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
    
    @SuppressLint("NewApi")
    @Override
    public void evaluateJavascript(String script, ValueCallback resultCallback) {
                
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // In KitKat+ you should use the evaluateJavascript method
            ValueCallbackAdapter adapter = new ValueCallbackAdapter(resultCallback);
            mWebView.evaluateJavascript(script, adapter);
        } else {
            mWebView.loadUrl("javascript:"+ script);
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        mSystemRenderObserver.destroy();
        mSystemRenderObserver = null;
        
        mWebView.destroy();
        mWebView = null;
    }
}
