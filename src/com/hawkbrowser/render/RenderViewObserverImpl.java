package com.hawkbrowser.render;

public class RenderViewObserverImpl implements RenderViewObserver {

    private RenderView mCurrentRenderView;
    
    public RenderView renderView() {
        return mCurrentRenderView;
    }
    
    public void onRenderViewChanged(RenderView oldView, RenderView newView) {
        
        assert mCurrentRenderView == oldView;
        
        if (null != mCurrentRenderView)
            mCurrentRenderView.removeObserver(this);
        
        newView.addObserver(this);
        mCurrentRenderView = newView;
    }
       
    public void onUpdateUrl(RenderView view, String url) { } 
    
    public void onLoadProgressChanged(RenderView view, int progress) { }
    
    public void didStartLoading(RenderView view, String url) { }

    public void didStopLoading(RenderView view, String url) { }
    
}
