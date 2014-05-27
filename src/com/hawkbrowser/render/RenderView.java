
package com.hawkbrowser.render;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class RenderView {
    
    protected List<RenderViewObserver> mObservers = new ArrayList<RenderViewObserver>();

    public abstract void loadUrl(String url);

    public abstract View getView();
    
    public abstract void requestFocus();
        
    public abstract boolean canGoBack();
    
    public abstract void goBack();
    
    public abstract boolean canGoForward();
    
    public abstract void goForward();
    
    public abstract void stopLoading();
    
    public abstract void reload();
    
    public void addObserver(RenderViewObserver observer) {
        mObservers.add(observer);
    }
    
    public void removeObserver(RenderViewObserver observer) {
        mObservers.remove(observer);
    }
    
    public void destroy() {
        mObservers.clear();
    }
}
