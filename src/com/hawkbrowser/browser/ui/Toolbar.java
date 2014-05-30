
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
    private Observer mToolbarObserver;
    private PopupMenuBar mPopupMenuBar;

    public interface Observer {
        void onExit();
        void onSwitchRender();
    }

    private RenderViewObserverImpl mRenderViewObserver = new RenderViewObserverImpl() {

        @Override
        public void didStopLoading(RenderView view, String url) {
            updateBackForwardStatus(renderView());
        }
    };

    private PopupMenuBarObserver mPopupMenuBarObserver = new PopupMenuBarObserver() {

        @Override
        public void onShowSetting() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onShowDownloadMgr() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onShowBookmark() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onRefresh() {
            mRenderViewObserver.renderView().reload();
        }

        @Override
        public void onExit() {
            if (null != mToolbarObserver)
                mToolbarObserver.onExit();
        }

        @Override
        public void onAddBookmark() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onShowHistory() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onShowPersonalCenter() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onShare() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onNightMode() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onImagelessMode() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onFullScreen() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onShowFileMgr() {
            // TODO Auto-generated method stub

        }
        
        @Override
        public void onSwitchRender() {
            if(null != mToolbarObserver)
                mToolbarObserver.onSwitchRender();
        }
    };

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
    }

    public void setToolbarObserver(Observer observer) {
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
                onToolbarMenu();
                break;
            }

            case R.id.toolbar_selectview: {
                Util.showToDoMessage(getContext());
                break;
            }
        }
    }

    private void onToolbarMenu() {

        if (null == mPopupMenuBar) {
            if(null == mRenderViewObserver.renderView())
                return;
            
            View renderView = mRenderViewObserver.renderView().getView();
            mPopupMenuBar = new PopupMenuBar(getContext(), 
                    renderView.getWidth(), renderView.getHeight(), mPopupMenuBarObserver);
        }

        if (mPopupMenuBar.isShow())
            mPopupMenuBar.dismiss();
        else {
            View anchor = findViewById(R.id.toolbar_menu);
            mPopupMenuBar.show(anchor);
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
    
    public void destroy() {
        mToolbarObserver = null;
    }
}
