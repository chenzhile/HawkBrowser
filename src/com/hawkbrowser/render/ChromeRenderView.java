
package com.hawkbrowser.render;

import android.view.ContextMenu;
import android.view.View;

import org.chromium.chrome.browser.TabBase;
import org.chromium.chrome.browser.TabObserver;
import org.chromium.chrome.hawkbrowser.HawkBrowserTab;
import org.chromium.content.browser.WebContentsObserverAndroid;

public class ChromeRenderView extends RenderView {
    
    private HawkBrowserTab mImpl;
    private ChromeObserverAdapter mChromeObserverAdapter;
    
    class ChromeObserverAdapter extends WebContentsObserverAndroid implements TabObserver {
        
        public ChromeObserverAdapter(HawkBrowserTab tab) {
            super(tab.getContentViewCore());
            tab.addObserver(this);
        }

        //----------- TabObserver methods ----------------------
        @Override
        public void onContentChanged(TabBase tab) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onContextMenuShown(TabBase tab, ContextMenu menu) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onDestroyed(TabBase tab) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onDidFailLoad(TabBase tab, boolean isProvisionalLoad, boolean isMainFrame, 
                int errorCode, String description, String failingUrl) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onFaviconUpdated(TabBase tab) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onLoadProgressChanged(TabBase tab, int progress) {
            for(RenderViewObserver observer : mObservers) {
                observer.onLoadProgressChanged(ChromeRenderView.this, progress);
            }
        }

        @Override
        public void onToggleFullscreenMode(TabBase tab, boolean enable) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onUpdateUrl(TabBase tab, String url) {
            
            for(RenderViewObserver observer : mObservers) {
                observer.onUpdateUrl(ChromeRenderView.this, url);
            }
        }

        @Override
        public void onWebContentsSwapped(TabBase tab) {
            // TODO Auto-generated method stub
            
        }
        
        //------------------- WebContentsObserverAndroid methods ----------------
        public void didStartLoading(String url) {
            
            for(RenderViewObserver observer : mObservers) {
                observer.didStartLoading(ChromeRenderView.this, url);
            }
        }

        public void didStopLoading(String url) {
            
            for(RenderViewObserver observer : mObservers) {
                observer.didStopLoading(ChromeRenderView.this, url);
            }
        }
    }
    
    public ChromeRenderView(HawkBrowserTab impl) {
        mImpl = impl;
        
        mChromeObserverAdapter = new ChromeObserverAdapter(mImpl);
    }

    @Override
    public void loadUrl(String url) {

        if (!url.startsWith("http://") && !url.startsWith("chrome://")
                && !url.startsWith("about:")) {
            url = "http://" + url;
        }

        mImpl.loadUrlWithSanitization(url);
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
        
        mImpl.removeObserver(mChromeObserverAdapter);
        mChromeObserverAdapter = null;
        
        mImpl.destroy();
        mImpl = null;
    }
}
