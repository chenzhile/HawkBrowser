package com.hawkbrowser.browser.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.hawkbrowser.R;
import com.hawkbrowser.common.Constants;
import com.hawkbrowser.common.Util;

public class LocationBar extends LinearLayout implements View.OnClickListener, TextWatcher {
        
    enum ActionState {
        None,
        Cancel,
        Enter,
        Search
    }
    
    private TextView mAction;
    private ActionState mActionState;
    private EditText mInput;
    private ImageView mClearInput;
        
    public LocationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public LocationBar(Context context) {
        this(context, null);
    }
    
    private void init() {
        
        mActionState = ActionState.None;
        
        inflate(getContext(), R.layout.locationbar, this);
        
        findViewById(R.id.locationbar_qrcode).setOnClickListener(this);
        findViewById(R.id.locationbar_speak).setOnClickListener(this);
        findViewById(R.id.locationbar_cancel_refresh).setOnClickListener(this);
        
        mClearInput = (ImageView) findViewById(R.id.locationbar_clear_input);
        mClearInput.setOnClickListener(this);
                
        mAction = (TextView) findViewById(R.id.locationbar_action);
        mAction.setOnClickListener(this);
        
        mInput = (EditText) findViewById(R.id.locationbar_input);
        mInput.setOnClickListener(this);
        
        mInput.addTextChangedListener(this);
        
        mInput.setOnEditorActionListener(new OnEditorActionListener() {
            // on ime go
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId != EditorInfo.IME_ACTION_GO) && (event == null ||
                        event.getKeyCode() != KeyEvent.KEYCODE_ENTER ||
                        event.getKeyCode() != KeyEvent.ACTION_DOWN)) {
                    return false;
                }

                navigate(mInput.getText().toString());
                setKeyboardVisibility(false);
                return true;
            }
        });
        
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.locationbar_input: {
                onClickInput();
                return;
            }
            
            case R.id.locationbar_clear_input:{
                mInput.setText("");
                onClearInput();
                return;
            }
            
            case R.id.locationbar_action: {
                onClickAction();
                return;
            }
            
            case R.id.locationbar_cancel_refresh: {
                return;
            }
            
            case R.id.locationbar_qrcode:
            case R.id.locationbar_speak:
                Util.showToDoMessage(getContext());
                return;
        }
    }
    
    private void onClickAction() {
        
        switch(mActionState){
            case Cancel: {
                findViewById(R.id.locationbar_action).setVisibility(View.GONE);
                findViewById(R.id.locationbar_cancel_refresh).setVisibility(View.VISIBLE);
                return;
            }
                
            case Enter: {
                navigate(mInput.getText().toString());
                return;
            }
                
            case Search:
                String url = Constants.SEARCH_URL + mInput.getText().toString();
                navigate(url);
                break;
                
            default:
                break;
        }
    }
    
    private void navigate(String url) {
        
        findViewById(R.id.locationbar_action).setVisibility(View.GONE);
        
        ImageView cancelRefresh = (ImageView) findViewById(R.id.locationbar_cancel_refresh);
        cancelRefresh.setImageResource(R.drawable.locationbar_cancel);
        
        cancelRefresh.setVisibility(View.VISIBLE);
    }
    
    private void onClickInput() {
        
        String input = mInput.getText().toString();
        
        if(input.length() == 0) {
            View rightZone = findViewById(R.id.locationbar_rightzone);
            View action = findViewById(R.id.locationbar_action);
            if(rightZone.getVisibility() == View.VISIBLE) {
                rightZone.setVisibility(View.GONE);
                action.setVisibility(View.VISIBLE);
            }
        }
    }
    
    private void onClearInput() {
        mClearInput.setVisibility(View.GONE);
        
        int color = getContext().getResources().getColor(android.R.color.black);
        mAction.setTextColor(color);
        mAction.setText(R.string.cancel);
        
        mActionState = ActionState.Cancel;
    }

    @Override
    public void afterTextChanged(Editable s) {
        
        String text = s.toString();
        
        if(text.length() > 0) {
            
            int color = getContext().getResources().getColor(android.R.color.holo_blue_light);
            mAction.setTextColor(color);
        
            if(URLUtil.isValidUrl(text)) {
                mAction.setText(R.string.Enter);
                mActionState = ActionState.Enter;
            }
            else {
                mAction.setText(R.string.search);
                mActionState = ActionState.Search;
            }
            
            mClearInput.setVisibility(View.VISIBLE);
            
        } else {
            onClearInput();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        return;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        return;
    }
    
    private void setKeyboardVisibility(boolean visible) {
        
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (visible) {
            imm.showSoftInput(mInput, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
        }
    }
}
