package com.soffice.clickandpay.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.soffice.clickandpay.Utilty.Display;

/**
 * Created by Aditya Sharma Malladi on 12/2/16.
 */
    public class RobotoRegularEditText extends EditText {

    public void setHandleDismissingKeyboard(
            handleDismissingKeyboard handleDismissingKeyboard) {
        this.handleDismissingKeyboard = handleDismissingKeyboard;
    }

    private handleDismissingKeyboard handleDismissingKeyboard;

    public interface handleDismissingKeyboard {
        public void dismissKeyboard();
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoRegularEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
            setTypeface(tf);
        }
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        Display.DisplayLogD("KeyEvent",String.valueOf(event.getAction()));
        return super.dispatchKeyEventPreIme(event);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                ||event.getAction() == KeyEvent.ACTION_UP||event.getAction()==KeyEvent.KEYCODE_NAVIGATE_NEXT||event.getAction()==KeyEvent.KEYCODE_ENTER) {
            if(handleDismissingKeyboard!=null) {
                handleDismissingKeyboard.dismissKeyboard();
                return true;
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

}
