package com.hawkbrowser.render;

import android.content.Context;

import org.chromium.chrome.hawkbrowser.HawkBrowserTab;
import org.chromium.ui.base.WindowAndroid;

import java.util.ArrayList;

public class RenderViewModel {
    
    private ArrayList<RenderView> mRenderViews = new ArrayList<RenderView>();

    public RenderView createChromeRenderView(Context context, WindowAndroid window) {
        
        HawkBrowserTab tab = new HawkBrowserTab(context, window);
        ChromeRenderView renderView = new ChromeRenderView(tab);
        mRenderViews.add(renderView);
        return renderView;
    }
    
    public RenderView createSystemRenderView(Context context) {
        
        SystemRenderView renderView = new SystemRenderView(context, null);
        mRenderViews.add(renderView);
        return renderView;
    }
    
    public void destroyView(RenderView view) {
        view.destroy();
        mRenderViews.remove(view);
    }
    
    public void destroy() {
        
        for(RenderView view : mRenderViews) {
            view.destroy();
        }
        
        mRenderViews.clear();
    }
}
