
package com.hawkbrowser.browser.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ViewFlipper;

import com.hawkbrowser.R;
import com.hawkbrowser.common.Util;

public class PopupMenuBar implements View.OnClickListener {

    private ViewGroup mView;
    private PopupWindow mPopup;
    private Observer mObserver;

    public interface Observer {
        void onQuit();

        void onRefresh();

        void onAddBookmark();

        void onShowBookmark();

        void onShowDownloadMgr();

        void onShowSetting();
    }

    public PopupMenuBar(Context context) {
        this(context, null);
    }

    public PopupMenuBar(Context context, Observer observer) {
        mObserver = observer;

        init(context);
    }

    private void init(Context context) {

        LayoutInflater li = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = (ViewGroup) li.inflate(R.layout.popup_menubar, null);
        ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(R.id.popup_menubar_viewflipper);

        View viewLeft = li.inflate(R.layout.popup_menubar_leftview, null);
        viewFlipper.addView(viewLeft);

        View viewRight = li.inflate(R.layout.popup_menubar_rightview, null);
        viewFlipper.addView(viewRight);
    }

    @Override
    public void onClick(View v) {

        //        if(null == mListener) {
        //            return;
        //        }
        //        
        //        switch(v.getId()) {
        //            case R.id.popmenu_bokmarkhistory:
        //                mListener.onShowBookmark();
        //                break;
        //                
        //            case R.id.popmenu_addbookmark:
        //                mListener.onAddBookmark();
        //                break;
        //                
        //            case R.id.popmenu_refresh:
        //                mListener.onRefresh();
        //                break;
        //                
        //            case R.id.popmenu_exit:
        //                mListener.onQuit();
        //                break;
        //                
        //            case R.id.popmenu_downloadmanager:
        //                mListener.onShowDownloadMgr();
        //                break;
        //                
        //            case R.id.popmenu_systemsetting:
        //                mListener.onShowSetting();
        //                break;
        //        }
    }

    public void show(View anchor) {
        if (null == mPopup) {
            Point screenSize = Util.screenSize(mView.getContext());
            Resources rs = mView.getContext().getResources();
            int height = rs.getDimensionPixelSize(R.dimen.popup_menubar_height);
            mPopup = new PopupWindow(mView, screenSize.x, height);
            mPopup.showAsDropDown(anchor, 0, 0);
        }
    }

    public boolean isShow() {
        return null != mPopup && mPopup.isShowing();
    }

    public void dismiss() {
        if (null != mPopup) {
            mPopup.dismiss();
            mPopup = null;
        }
    }

}
