package com.soffice.clickandpay.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Aditya Sharma Malladi on 12/2/16.
 */
    public class RobotoLightEditText extends EditText {

    public RobotoLightEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoLightEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoLightEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
            setTypeface(tf);
        }
    }

}
