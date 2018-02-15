package com.example.marko.vips_artikli;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This is the same as a native EditText, except that no soft keyboard
 * will appear when user clicks on widget. All other normal operations
 * still work.
 * <p>
 * To use in XML, add a widget for <my.package.name>.KeyboardlessEditText
 * To use in Java, use one of the three constructors in this class
 */
public class myEditTextNoKeyboard extends android.support.v7.widget.AppCompatEditText {

    private static String TAG = "myEditText";

    private static final Method mShowSoftInputOnFocus = getMethod(
            EditText.class, "setShowSoftInputOnFocus", boolean.class);

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            setCursorVisible(true);
        }
    };

    private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            setCursorVisible(true);
            return false;
        }
    };

    public myEditTextNoKeyboard(Context context) {
        super(context);
        initialize();
    }

    public myEditTextNoKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public myEditTextNoKeyboard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        synchronized (this) {
            setInputType(getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            setFocusableInTouchMode(true);

        }

        // Needed to show cursor when user interacts with EditText so that the edit operations
        // still work. Without the cursor, the edit operations won't appear.
        setOnClickListener(mOnClickListener);
        setOnLongClickListener(mOnLongClickListener);

//      setShowSoftInputOnFocus(false); // This is a hidden method in TextView.
        reflexSetShowSoftInputOnFocus(false); // Workaround.

        // Ensure that cursor is at the end of the input box when initialized. Without this, the
        // cursor may be at index 0 when there is text added via layout XML.
        setSelection(getText().length());
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {

        super.onFocusChanged(focused, direction, previouslyFocusedRect);


        hideKeyboard();
        Log.d(TAG, "onKey: MIJENJAM FOCUS focused=" + focused + ": Direction=" + direction);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final boolean ret = super.onTouchEvent(event);
        // Must be done after super.onTouchEvent()
        hideKeyboard();
        return ret;
    }

    public void setAlertBackgroundColor() {
        int color = ContextCompat.getColor(getContext(), R.color.myCrvena);
        super.setBackgroundColor(color);
    }

    public void setOkBackgroundColor() {
        int color = ContextCompat.getColor(getContext(), R.color.myZelena);
        super.setBackgroundColor(color);
    }

    private void hideKeyboard() {
        final InputMethodManager imm = ((InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE));
        if (imm != null && imm.isActive(this)) {
            imm.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
        }
    }

    private void reflexSetShowSoftInputOnFocus(boolean show) {
        if (mShowSoftInputOnFocus != null) {
            invokeMethod(mShowSoftInputOnFocus, this, show);
        } else {
            // Use fallback method. Not tested.
            hideKeyboard();
        }
    }

    /**
     * Returns method if available in class or superclass (recursively),
     * otherwise returns null.
     */
    public static Method getMethod(Class<?> cls, String methodName, Class<?>... parametersType) {
        Class<?> sCls = cls.getSuperclass();
        while (sCls != Object.class) {
            try {
                return sCls.getDeclaredMethod(methodName, parametersType);
            } catch (NoSuchMethodException e) {
                // Just super it again
            }
            sCls = sCls.getSuperclass();
        }
        return null;
//        throw new RuntimeException("Method not found " + methodName);
    }

    /**
     * Returns results if available, otherwise returns null.
     */
    public static Object invokeMethod(Method method, Object receiver, Object... args) {
        try {
            return method.invoke(receiver, args);
        } catch (IllegalArgumentException e) {
            Log.e("Safe invoke fail", "Invalid args", e);
        } catch (IllegalAccessException e) {
            Log.e("Safe invoke fail", "Invalid access", e);
        } catch (InvocationTargetException e) {
            Log.e("Safe invoke fail", "Invalid target", e);
        }

        return null;
    }

}
