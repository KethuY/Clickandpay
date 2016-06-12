package com.soffice.clickandpay.UI;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by 887391 on 2/24/2016.
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.3f;

    public void transformPage(View view, float position) {
        int pageWidth = (int) (view.getWidth()*0.85);
        int pageHeight = (int) (view.getHeight()*0.85);


        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0.3f);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            Log.d("pagewidth @@@@", String.valueOf(pageWidth));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            if (position == 0.15f) {
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setScaleX((float) (scaleFactor - 0.15));
                view.setScaleY((float) (scaleFactor - 0.15));
            }

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0.3f);
        }
    }
}