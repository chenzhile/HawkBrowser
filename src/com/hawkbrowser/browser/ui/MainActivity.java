
package com.hawkbrowser.browser.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hawkbrowser.R;
import com.hawkbrowser.browser.HawkBrowserApplication;
import com.hawkbrowser.common.Config;
import com.hawkbrowser.common.Constants;
import com.hawkbrowser.render.RenderView;
import com.hawkbrowser.render.RenderViewHolder;
import com.hawkbrowser.render.RenderViewModel;

import org.chromium.content.browser.BrowserStartupController;
import org.chromium.content.browser.ContentView;
import org.chromium.content.browser.ContentViewRenderView;
import org.chromium.content.common.ProcessInitException;
import org.chromium.ui.base.ActivityWindowAndroid;
import org.chromium.ui.base.WindowAndroid;

public class MainActivity extends Activity implements Toolbar.Observer {

    private WindowAndroid mWindow;
    private RenderViewHolder mRenderViewHolder;
    private ContentViewRenderView mContentViewRenderView;
    private RenderViewModel mRenderViewModel;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (Config.UseChromeRender)
            initChrome(savedInstanceState);
        else {
            setContentView(R.layout.main);
            initAfterRenderInit();
        }
    }

    private void initAfterRenderInit() {

        initUI();
        startRender();
    }

    private void initUI() {

        mRenderViewHolder = (RenderViewHolder) findViewById(R.id.renderview_holder);

        LocationBar locationBar = (LocationBar) findViewById(R.id.locationbar);
        mRenderViewHolder.addObserver(locationBar);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRenderViewHolder.addObserver(mToolbar);

        mToolbar.setToolbarObserver(this);

    }

    private void startRender() {

        mRenderViewModel = new RenderViewModel();

        RenderView renderView;

        if (Config.UseChromeRender) {
            renderView = mRenderViewModel.createChromeRenderView(this, mWindow);
            mRenderViewHolder.addView(mContentViewRenderView,
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT));
            mContentViewRenderView.setCurrentContentView((ContentView) renderView.getView());
        }
        else {
            renderView = mRenderViewModel.createSystemRenderView(this);
        }

        mRenderViewHolder.setCurrentRenderView(renderView);

        renderView.loadUrl(Constants.HOME_PAGE_URL);
    }

    private void initChrome(final Bundle savedInstanceState) {

        HawkBrowserApplication.initChromeResource();

        BrowserStartupController.StartupCallback callback =
                new BrowserStartupController.StartupCallback() {
                    @Override
                    public void onSuccess(boolean alreadyStarted) {
                        onChromeStartSucceed(savedInstanceState);
                    }

                    @Override
                    public void onFailure() {
                        onChromeStartFailed();
                    }
                };

        try {
            BrowserStartupController.get(this).startBrowserProcessesAsync(callback);
        } catch (ProcessInitException e) {
            onChromeStartFailed();
        }
    }

    private void onChromeStartFailed() {
        Toast.makeText(this,
                R.string.browser_process_initialize_failed,
                Toast.LENGTH_SHORT).show();
        if (Config.LOG_ENABLED)
            Log.e(Config.LOG_TAG, "Chromium browser process initialization failed");
        finish();
    }

    private void onChromeStartSucceed(Bundle savedInstanceState) {
        setContentView(R.layout.main);

        mWindow = new ActivityWindowAndroid(this);
        mWindow.restoreInstanceState(savedInstanceState);

        mContentViewRenderView = new ContentViewRenderView(this, mWindow) {
            @Override
            protected void onReadyToRender() {
            }
        };

        initAfterRenderInit();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (mToolbar.onKeyUp(keyCode, event))
            return true;

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        destroyUI();
        destroyRender();
    }

    private void destroyUI() {

        mToolbar.destroy();
        mRenderViewHolder.destroy();
    }

    private void destroyRender() {

        if (null != mRenderViewModel) {
            mRenderViewModel.destroy();
            mRenderViewModel = null;
        }

        if (null != mContentViewRenderView) {
            mRenderViewHolder.removeView(mContentViewRenderView);
            mContentViewRenderView.destroy();
            mContentViewRenderView = null;
        }

        if (null != mWindow) {
            mWindow.destroy();
            mWindow = null;
        }
    }

    @Override
    public void onExit() {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void onSwitchRender() {

        destroyUI();
        destroyRender();
        
        if(Config.UseChromeRender) {
            Config.UseChromeRender = false;
            initAfterRenderInit();
        } else {
            Config.UseChromeRender = true;
            initChrome(null);
        }
    }
}
