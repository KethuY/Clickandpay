package com.soffice.clickandpay.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Aditya Sharma Malladi on 12/2/16.
 */
    public class RobotoMediumEditText extends EditText {

    public RobotoMediumEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoMediumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoMediumEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
            setTypeface(tf);
        }
    }

}
