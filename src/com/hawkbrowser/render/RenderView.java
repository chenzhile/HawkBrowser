
package com.hawkbrowser.render;

import android.view.View;

public interface RenderView {

    void loadUrl(String url);

    View getView();
    
    void requestFocus();
    
    void destroy();
}
