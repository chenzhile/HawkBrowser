
package com.hawkbrowser.browser.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hawkbrowser.R;
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

public class MainActivity extends Activity {

    private WindowAndroid mWindow;
    private RenderViewHolder mRenderViewHolder;
    private ContentViewRenderView mContentViewRenderView;
    private RenderViewModel mRenderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        if (Config.RenderViewType.Chrome == Config.RENDER_VIEW_TYPE)
            startChrome(savedInstanceState);
        else {
            setContentView(R.layout.main);
            initAfterRenderStart();
        }
    }

    private void initAfterRenderStart() {

        mRenderViewModel = new RenderViewModel();
        mRenderViewHolder = (RenderViewHolder) findViewById(R.id.renderview_holder);

        RenderView renderView;

        if (Config.RenderViewType.Chrome == Config.RENDER_VIEW_TYPE) {
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

    private void startChrome(final Bundle savedInstanceState) {

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

        initAfterRenderStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mRenderViewModel) {
            mRenderViewModel.destroy();
            mRenderViewModel = null;
        }

        if (null != mContentViewRenderView) {
            mContentViewRenderView.destroy();
            mContentViewRenderView = null;
        }

        if (null != mWindow) {
            mWindow.destroy();
            mWindow = null;
        }
    }

}
