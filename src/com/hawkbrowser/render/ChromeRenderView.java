
package com.hawkbrowser.render;

import android.view.View;

import org.chromium.chrome.hawkbrowser.HawkBrowserTab;

public class ChromeRenderView implements RenderView {

    HawkBrowserTab mImpl;

    public ChromeRenderView(HawkBrowserTab impl) {
        mImpl = impl;
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
    public void destroy() {
        mImpl.destroy();
        mImpl = null;
    }
}
