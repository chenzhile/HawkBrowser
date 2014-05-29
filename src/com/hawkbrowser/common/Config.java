
package com.hawkbrowser.common;

import com.hawkbrowser.BuildConfig;

public final class Config {

    public static final String LOG_TAG = "HawkBrowser";
    public static final boolean LOG_ENABLED = BuildConfig.DEBUG;

    public enum RenderViewType {
        System,
        Chrome
    }
    
    public static RenderViewType RENDER_VIEW_TYPE = RenderViewType.System;
}
