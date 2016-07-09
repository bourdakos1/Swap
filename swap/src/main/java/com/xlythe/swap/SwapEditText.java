package com.xlythe.swap;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Niko on 7/6/16.
 */
public class SwapEditText extends EditText {
    private FragmentActivity mContext;
    private View mAttachView;
    private int mScreenSize;
    private int mKeyboardSize;
    private boolean mAdjustNothing;
    private boolean mKeyboardOpen;
    private OnFragmentHiddenListener mListener;

    public SwapEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initKeyboard(context);
    }

    public SwapEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initKeyboard(context);
    }

    public SwapEditText(Context context) {
        super(context);
        initKeyboard(context);
    }

    private void initKeyboard(Context context) {
        if (context instanceof FragmentActivity) {
            mContext = (FragmentActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must extend FragmentActivity");
        }
        final Window rootWindow = mContext.getWindow();
        final View root = rootWindow.getDecorView().findViewById(android.R.id.content);

        // Seems redundant to set as ADJUST_NOTHING in manifest and then immediately to ADJUST_RESIZE
        // but it seems that the input gets reset to a default on keyboard dismissal if not set otherwise.
        rootWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (mAttachView == null) {
                    setContainer(R.id.container);
                }
                Rect r = new Rect();
                View view = rootWindow.getDecorView();
                view.getWindowVisibleDisplayFrame(r);
                if (mScreenSize != 0 && mScreenSize > r.bottom) {
                    mKeyboardSize = mScreenSize - r.bottom;
                    mAttachView.getLayoutParams().height = mKeyboardSize;
                    rootWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    if (mKeyboardOpen) {
                        mAttachView.setVisibility(View.VISIBLE);
                    }
                    mAdjustNothing = true;
                } else {
                    mScreenSize = r.bottom;
                }
            }
        });
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            clearFocus();
            hideFragment();
        }
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        mKeyboardOpen = focused;
        if (focused) {
            showKeyboard();
        }
    }

    public void showFragment(Fragment fragment){
        clearFocus();
        mAttachView.setVisibility(View.VISIBLE);

        FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
        transaction.replace(mAttachView.getId(), fragment).commit();
    }

    public void showKeyboard(){
        if (mListener != null) {
            mListener.onFragmentHidden();
        }
        if (!mAdjustNothing) {
            hideFragment();
        } else {
            // Just because it has focus doesnt mean the keyboard actually opened
            // This seems like an easier fix than not showing the attachview
            InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(this, 0);
            mAttachView.setVisibility(View.VISIBLE);
        }
    }

    private void hideFragment() {
        if (mListener != null) {
            mListener.onFragmentHidden();
        }
        Fragment activeFragment = mContext.getSupportFragmentManager().findFragmentById(mAttachView.getId());
        if (activeFragment != null) {
            mContext.getSupportFragmentManager().beginTransaction().remove(activeFragment).commit();
        }
        mAttachView.setVisibility(View.GONE);
    }

    public void hideKeyboard() {
        clearFocus();
        hideFragment();
    }

    public void setContainer(int id){
        mAttachView = mContext.findViewById(id);
        mAttachView.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) 263.3333, getResources().getDisplayMetrics());
        mAttachView.setVisibility(GONE);
    }

    public boolean getFragmentVisibility() {
        return mAttachView.getVisibility() == View.VISIBLE;
    }

    public boolean getKeyboardVisibility() {
        return mKeyboardOpen;
    }

    public void setOnFragmentHiddenListener(OnFragmentHiddenListener listener) {
        mListener = listener;
    }

    @Override
    public void clearFocus() {
        InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(getWindowToken(), 0);
        super.clearFocus();
    }

    public interface OnFragmentHiddenListener {
        public void onFragmentHidden();
    }
}