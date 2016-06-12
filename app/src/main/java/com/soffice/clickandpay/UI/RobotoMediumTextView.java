package com.soffice.clickandpay.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by aditya on 10/2/16.
 */
public class RobotoMediumTextView extends TextView {

        public RobotoMediumTextView(Context context) {
            super(context);

            applyCustomFont(context);
        }

        public RobotoMediumTextView(Context context, AttributeSet attrs) {
            super(context, attrs);

            applyCustomFont(context);
        }

        public RobotoMediumTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);

            applyCustomFont(context);
        }

        private void applyCustomFont(Context context) {
            Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
            setTypeface(customFont);
        }
}
