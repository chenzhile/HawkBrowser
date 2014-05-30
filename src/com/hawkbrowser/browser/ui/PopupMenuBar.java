
package com.hawkbrowser.browser.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ViewFlipper;

import com.hawkbrowser.R;
import com.hawkbrowser.common.Config;

public class PopupMenuBar implements View.OnClickListener,
        GestureDetector.OnGestureListener, View.OnTouchListener {

    private ViewGroup mView;
    private ViewFlipper mViewFlipper;
    private View mOverlayView;
    private View mLeftView;
    private View mRightView;
    private View mLeftSpinner;
    private Button mSwitchRenderBtn;
    private long mAnimationDuration;
    private PopupWindow mPopup;
    private PopupMenuBarObserver mObserver;
    private GestureDetector mGestureDetector;
    private int mWidth;
    private int mHeight;

    public PopupMenuBar(Context context, int width, int height) {
        this(context, width, height, null);
    }

    public PopupMenuBar(Context context, int width, int height, PopupMenuBarObserver observer) {
        mObserver = observer;
        mWidth = width;
        mHeight = height;

        init(context);
    }

    private void init(Context context) {

        LayoutInflater li = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = (ViewGroup) li.inflate(R.layout.popup_menubar, null);

        mLeftView = li.inflate(R.layout.popup_menubar_leftview, null);
        mRightView = li.inflate(R.layout.popup_menubar_rightview, null);
        mSwitchRenderBtn = (Button) mRightView.findViewById(R.id.popup_menubar_render);

        mAnimationDuration = context.getResources().getInteger(R.integer.popup_menubar_animation_time);

        mOverlayView = mView.findViewById(R.id.popup_menubar_overlay);
        mOverlayView.setOnClickListener(this);
        mOverlayView.setOnTouchListener(this);

        initFlipper();
        initBtnListener((TableLayout) mLeftView);
        initBtnListener((TableLayout) mRightView);
    }

    private void initFlipper() {

        mView.setOnTouchListener(this);

        mViewFlipper = (ViewFlipper) mView.findViewById(R.id.popup_menubar_viewflipper);
        mViewFlipper.addView(mLeftView);
        mViewFlipper.addView(mRightView);

        mLeftSpinner = mView.findViewById(R.id.popup_menubar_spinner_left);

        mGestureDetector = new GestureDetector(mView.getContext(), this);
    }

    private void initBtnListener(TableLayout tableLayout) {

        int rowCount = tableLayout.getChildCount();
        for (int rowIndex = 0; rowIndex < rowCount; ++rowIndex) {

            TableRow row = (TableRow) tableLayout.getChildAt(rowIndex);
            int colCount = row.getChildCount();

            for (int colIndex = 0; colIndex < colCount; ++colIndex) {
                View col = row.getChildAt(colIndex);

                if (col.getVisibility() == View.VISIBLE) {
                    col.setOnClickListener(this);
                    col.setOnTouchListener(this);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

        dismiss();

        if (null == mObserver)
            return;

        switch (v.getId()) {
            case R.id.popup_menubar_addbookmark: {
                mObserver.onAddBookmark();
                break;
            }

            case R.id.popup_menubar_bookmark: {
                mObserver.onShowBookmark();
                break;
            }

            case R.id.popup_menubar_history: {
                mObserver.onShowHistory();
                break;
            }

            case R.id.popup_menubar_refresh: {
                mObserver.onRefresh();
                break;
            }

            case R.id.popup_menubar_personalcenter: {
                mObserver.onShowPersonalCenter();
                break;
            }

            case R.id.popup_menubar_download: {
                mObserver.onShowDownloadMgr();
                break;
            }

            case R.id.popup_menubar_share: {
                mObserver.onShare();
                break;
            }

            case R.id.popup_menubar_exit: {
                mObserver.onExit();
                break;
            }

            case R.id.popup_menubar_setting: {
                mObserver.onShowSetting();
                break;
            }

            case R.id.popup_menubar_nightmode: {
                mObserver.onNightMode();
                break;
            }

            case R.id.popup_menubar_noimage: {
                mObserver.onImagelessMode();
                break;
            }

            case R.id.popup_menubar_fullscreen: {
                mObserver.onFullScreen();
                break;
            }

            case R.id.popup_menubar_file: {
                mObserver.onShowFileMgr();
                break;
            }

            case R.id.popup_menubar_render: {
                mObserver.onSwitchRender();
                break;
            }
        }
    }

    public void show(View anchor) {
        if (null == mPopup) {
            mPopup = new PopupWindow(mView, mWidth, mHeight);

            mSwitchRenderBtn.setCompoundDrawablesWithIntrinsicBounds(0,
                    Config.UseChromeRender ? R.drawable.popup_menubar_chromerender
                            : R.drawable.popup_menubar_systemrender,
                    0, 0);

            mSwitchRenderBtn.setText(Config.UseChromeRender
                    ? R.string.chrome_render : R.string.system_render);

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

    //---------------- GestureDetector.OnGestureListener -------------------
    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        final int distance = 20;

        if (e2.getX() - e1.getX() > distance && mViewFlipper.getCurrentView() == mRightView) {

            mViewFlipper.setInAnimation(mView.getContext(), R.anim.popup_menubar_push_left_in);
            mViewFlipper.setOutAnimation(mView.getContext(), R.anim.popup_menubar_push_right_out);
            mViewFlipper.showPrevious();

            ObjectAnimator animation = ObjectAnimator.ofFloat(mLeftSpinner, "X",
                    mLeftSpinner.getWidth(), 0).setDuration(mAnimationDuration);

            animation.start();

            return true;

        } else if (e2.getX() - e1.getX() < -distance && mViewFlipper.getCurrentView() == mLeftView) {

            mViewFlipper.setInAnimation(mView.getContext(), R.anim.popup_menubar_push_right_in);
            mViewFlipper.setOutAnimation(mView.getContext(), R.anim.popup_menubar_push_left_out);
            mViewFlipper.showNext();

            ObjectAnimator animation = ObjectAnimator.ofFloat(mLeftSpinner, "X",
                    0, mLeftSpinner.getWidth()).setDuration(mAnimationDuration);

            animation.start();

            return true;
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    //------------------ View.OnTouchListener -------------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v == mOverlayView) {

            if (event.getAction() == MotionEvent.ACTION_UP)
                dismiss();

            return true;
        }

        boolean handled = mGestureDetector.onTouchEvent(event);

        if (v != mLeftView && v != mRightView && handled) {
            v.setPressed(false);
        }

        return handled;
    }
}
