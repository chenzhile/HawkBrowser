
package com.hawkbrowser.render.chrome;

import android.view.View;

import com.hawkbrowser.render.RenderView;

import org.chromium.chrome.hawkbrowser.HawkBrowserTab;

public class ChromeRenderView extends RenderView {
    
    private HawkBrowserTab mImpl;
    private ChromeRenderObserverAdapter mChromeObserverAdapter;
       
    public ChromeRenderView(HawkBrowserTab impl) {
        mImpl = impl;
        
        mChromeObserverAdapter = new ChromeRenderObserverAdapter(mImpl, this, mObservers);
    }

    @Override
    public void loadUrl(String url) {

        if (!url.startsWith("http://") && !url.startsWith("chrome://")
                && !url.startsWith("about:")) {
            url = "http://" + url;
        }

        mImpl.loadUrlWithSanitization(url);
        requestFocus();
    }

    @Override
    public View getView() {
        return mImpl.getContentView();
    }

    @Override
    public void requestFocus() {
        mImpl.getContentView().requestFocus();
    }
    
    @Override
    public boolean canGoBack() {
        return mImpl.canGoBack();
    }
    
    @Override
    public void goBack() {
        mImpl.goBack();
    }
    
    @Override
    public boolean canGoForward() {
        return mImpl.canGoForward();
    }
    
    @Override
    public void goForward() {
        mImpl.goForward();
    }
    
    @Override
    public void stopLoading() {
        mImpl.stopLoading();
    }
    
    @Override
    public void reload() {
        mImpl.reload();
    }

    @Override
    public void destroy() {
        super.destroy();
        
        
        mChromeObserverAdapter.destroy();
        mChromeObserverAdapter = null;
        
        mImpl.destroy();
        mImpl = null;
    }
}
