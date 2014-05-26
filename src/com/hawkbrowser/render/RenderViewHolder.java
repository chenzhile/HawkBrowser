
package com.hawkbrowser.render;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import org.chromium.content.browser.ContentViewRenderView;

import java.util.ArrayList;
import java.util.List;

public class RenderViewHolder extends FrameLayout {
    
    private RenderView mCurrentView;
    private List<RenderViewHolderObserver> mObservers = new ArrayList<RenderViewHolderObserver>();
    
    public RenderViewHolder(Context context) {
        super(context);
    }
    
    public RenderViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RenderViewHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCurrentRenderView(RenderView view) {
        
        if(null != mCurrentView) {
            removeView(mCurrentView.getView());
        }
        
        addView(view.getView());
        mCurrentView = view;
        
        for(RenderViewHolderObserver observer : mObservers) {
            observer.onRenderViewChanged(view);
        }
    }
    
    public void addObserver(RenderViewHolderObserver observer) {
        mObservers.add(observer);
    }
    
    public void removeObserver(RenderViewHolderObserver observer) {
        mObservers.remove(observer);
    }
    
}
