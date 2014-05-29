
package com.hawkbrowser.browser.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ViewFlipper;

import com.hawkbrowser.R;
import com.hawkbrowser.common.Util;

public class PopupMenuBar implements View.OnClickListener,
        GestureDetector.OnGestureListener, View.OnTouchListener {

    private ViewGroup mView;
    private ViewFlipper mViewFlipper;
    private View mLeftView;
    private View mRightView;
    private View mLeftSpinner;
    private View mRightSpinner;
    private Animation mSpinnerMoveLeft;
    private Animation mSpinnerMoveRight;
    private PopupWindow mPopup;
    private PopupMenuBarObserver mObserver;
    private GestureDetector mGestureDetector;

    private AnimationListener mSpinnerAnimationListener = new AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {

            if (animation == mSpinnerMoveRight) {
                mLeftSpinner.setX(mLeftSpinner.getWidth());
                mRightSpinner.setX(0);
            } else if (animation == mSpinnerMoveLeft) {
                mLeftSpinner.setX(0);
                mRightSpinner.setX(mLeftSpinner.getWidth());
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }
    };

    public PopupMenuBar(Context context) {
        this(context, null);
    }

    public PopupMenuBar(Context context, PopupMenuBarObserver observer) {
        mObserver = observer;

        init(context);
    }

    private void init(Context context) {

        LayoutInflater li = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = (ViewGroup) li.inflate(R.layout.popup_menubar, null);
        mLeftView = li.inflate(R.layout.popup_menubar_leftview, null);
        mRightView = li.inflate(R.layout.popup_menubar_rightview, null);

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
        mRightSpinner = mView.findViewById(R.id.popup_menubar_spinner_right);

        //        long duration = context.getResources().getInteger(R.integer.popup_menubar_animation_time);

        //        mSpinnerMoveRight = ObjectAnimator.ofFloat(mLeftSpinner, "X", 0f, mLeftSpinner.getWidth());
        //        mSpinnerMoveRight.setDuration(duration);
        //        mSpinnerMoveLeft = ObjectAnimator.ofFloat(mLeftSpinner, "X", mLeftSpinner.getWidth(), 0f);
        //        mSpinnerMoveLeft.setDuration(duration);

        mSpinnerMoveLeft = AnimationUtils.loadAnimation(mView.getContext(),
                R.anim.popup_menubar_spin_move_left);
        mSpinnerMoveLeft.setAnimationListener(mSpinnerAnimationListener);

        mSpinnerMoveRight = AnimationUtils.loadAnimation(mView.getContext(),
                R.anim.popup_menubar_spin_move_right);
        mSpinnerMoveRight.setAnimationListener(mSpinnerAnimationListener);

        mGestureDetector = new GestureDetector(mView.getContext(), this);
    }

    private void initBtnListener(TableLayout tableLayout) {

        int rowCount = tableLayout.getChildCount();
        for (int rowIndex = 0; rowIndex < rowCount; ++rowIndex) {

            TableRow row = (TableRow) tableLayout.getChildAt(rowIndex);
            int colCount = row.getChildCount();

            for (int colIndex = 0; colIndex < colCount; ++colIndex) {
                View col = row.getChildAt(colIndex);
                col.setOnClickListener(this);
                col.setOnTouchListener(this);
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
        }
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

    //---------------- GestureDetector.OnGestureListener -------------------
    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        final int distance = 80;

        if (e2.getX() - e1.getX() > distance && mViewFlipper.getCurrentView() == mRightView) {

            mViewFlipper.setInAnimation(mView.getContext(), R.anim.popup_menubar_push_left_in);
            mViewFlipper.setOutAnimation(mView.getContext(), R.anim.popup_menubar_push_right_out);
            mViewFlipper.showPrevious();

            mLeftSpinner.startAnimation(mSpinnerMoveLeft);

            return true;

        } else if (e2.getX() - e1.getX() < -distance && mViewFlipper.getCurrentView() == mLeftView) {

            mViewFlipper.setInAnimation(mView.getContext(), R.anim.popup_menubar_push_right_in);
            mViewFlipper.setOutAnimation(mView.getContext(), R.anim.popup_menubar_push_left_out);
            mViewFlipper.showNext();

            mLeftSpinner.startAnimation(mSpinnerMoveRight);

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
        if (mGestureDetector.onTouchEvent(event))
            return true;

        return false;
    }

}
