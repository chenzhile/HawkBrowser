
package com.hawkbrowser.render;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import org.chromium.content.browser.ContentViewRenderView;

public class RenderViewHolder extends FrameLayout {
    
    private RenderView mCurrentView;
    
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
    }
}
