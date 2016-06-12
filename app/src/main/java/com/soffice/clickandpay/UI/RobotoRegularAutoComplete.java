package com.soffice.clickandpay.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * Created by Surya on 08-05-2016.
 */
public class RobotoRegularAutoComplete extends AppCompatAutoCompleteTextView {
    public RobotoRegularAutoComplete(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public RobotoRegularAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public RobotoRegularAutoComplete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }
    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        setTypeface(customFont);
    }
}
