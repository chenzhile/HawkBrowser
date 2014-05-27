
package com.hawkbrowser.browser.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hawkbrowser.R;
import com.hawkbrowser.common.Util;
import com.hawkbrowser.render.RenderView;
import com.hawkbrowser.render.RenderViewHolderObserver;
import com.hawkbrowser.render.RenderViewObserverImpl;

public class Toolbar extends LinearLayout implements View.OnClickListener, RenderViewHolderObserver {

    private View mBack;
    private View mForward;
    private long mPrevBackKeyUpTime;
    private RenderViewObserverImpl mRenderViewObserver;
    private ToolbarObserver mToolbarObserver;

    class ToolbarRenderViewObserver extends RenderViewObserverImpl {

        @Override
        public void didStopLoading(RenderView view, String url) {
            updateBackForwardStatus(renderView());
        }
    }

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        inflate(getContext(), R.layout.toolbar, this);

        mBack = findViewById(R.id.toolbar_back);
        mBack.setOnClickListener(this);
        
        mForward = findViewById(R.id.toolbar_forward);
        mForward.setOnClickListener(this);
        
        findViewById(R.id.toolbar_home).setOnClickListener(this);
        findViewById(R.id.toolbar_menu).setOnClickListener(this);
        findViewById(R.id.toolbar_selectview).setOnClickListener(this);

        mRenderViewObserver = new ToolbarRenderViewObserver();
    }

    public void setToolbarObserver(ToolbarObserver observer) {
        mToolbarObserver = observer;
    }
    
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.toolbar_back: {
                mRenderViewObserver.renderView().goBack();
                break;
            }

            case R.id.toolbar_forward: {
                mRenderViewObserver.renderView().goForward();
                break;
            }

            case R.id.toolbar_home: {
                Util.showToDoMessage(getContext());
                break;
            }

            case R.id.toolbar_menu: {
                Util.showToDoMessage(getContext());
                break;
            }

            case R.id.toolbar_selectview: {
                Util.showToDoMessage(getContext());
                break;
            }
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_BACK != keyCode)
            return false;

        if (mRenderViewObserver.renderView().canGoBack()) {
            mRenderViewObserver.renderView().goBack();
            return true;
        }

        long time = System.currentTimeMillis();

        if (mPrevBackKeyUpTime == 0 || time - mPrevBackKeyUpTime > 3000) {
            mPrevBackKeyUpTime = time;
            Toast.makeText(getContext(), R.string.press_again_to_exit,
                    Toast.LENGTH_SHORT).show();
            return true;
        } 
        
        return false;
    }

    @Override
    public void onRenderViewChanged(RenderView oldView, RenderView newView) {
        mRenderViewObserver.onRenderViewChanged(oldView, newView);
        updateBackForwardStatus(newView);
    }

    private void updateBackForwardStatus(RenderView view) {
        mBack.setEnabled(view.canGoBack());
        mForward.setEnabled(view.canGoForward());
    }
}
