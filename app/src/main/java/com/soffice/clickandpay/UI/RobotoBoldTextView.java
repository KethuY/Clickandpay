package com.soffice.clickandpay.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by aditya on 10/2/16.
 */
public class RobotoBoldTextView extends TextView {

        public RobotoBoldTextView(Context context) {
            super(context);

            applyCustomFont(context);
        }

        public RobotoBoldTextView(Context context, AttributeSet attrs) {
            super(context, attrs);

            applyCustomFont(context);
        }

        public RobotoBoldTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);

            applyCustomFont(context);
        }

        private void applyCustomFont(Context context) {
            Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
            setTypeface(customFont);
        }
}
