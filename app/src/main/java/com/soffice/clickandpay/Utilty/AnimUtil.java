package com.soffice.clickandpay.Utilty;

import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by suryaashok.p on 09-12-2015.
 */
public class AnimUtil
{
    public static void ShakeAnim(View view)
    {
        float[] values={70f,-70f,70f,-70f,0f};
        ObjectAnimator translateanim=ObjectAnimator.ofFloat(view,"translationX",values);
        translateanim.setInterpolator(new LinearOutSlowInInterpolator());
        translateanim.setDuration(500);
        translateanim.start();
    }

    public static void LandingAnim(final View view,final String Score, final int which)
    {
        float[] ScaleVal={1.5f,1.0f};
        float[] AlphaVal={0.0f,1.0f};
        ObjectAnimator scalexanim=ObjectAnimator.ofFloat(view,"scaleX",ScaleVal);
        ObjectAnimator scaleyanim=ObjectAnimator.ofFloat(view,"scaleY",ScaleVal);
        ObjectAnimator alphaanim=ObjectAnimator.ofFloat(view,"alpha",AlphaVal);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(scalexanim, scaleyanim, alphaanim);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (which == 1) {
                    TextView textView = (TextView) view;
                    textView.setText(Score);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public static void FadeOutAnim(final View view,int Duration)
    {
        float[] AlphaVal={1.0f,0.0f};
        ObjectAnimator fadoutAnim=ObjectAnimator.ofFloat(view,"alpha",AlphaVal);
        fadoutAnim.setDuration(Duration);
        fadoutAnim.setInterpolator(new LinearOutSlowInInterpolator());
        fadoutAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fadoutAnim.start();
    }

    public static void FadeInAnim(View view,int Duration,int Delay)
    {
        float[] AlphaVal={0.0f,1.0f};
        ObjectAnimator fadinAnim=ObjectAnimator.ofFloat(view,"alpha",AlphaVal);
        fadinAnim.setDuration(Duration);
        fadinAnim.setStartDelay(Delay);
        fadinAnim.setInterpolator(new LinearOutSlowInInterpolator());
        fadinAnim.start();
    }

    public static void SlideDownAnim(ImageView logo,int Duration,Button loginbtn,Button Signupbtn, final LinearLayout LoginFields,View Divider)
    {
        ViewGroup parent= (ViewGroup) logo.getParent();
        float SlideupVal=-logo.getTop()+30;
        Log.d("Logo Slide Val",String.valueOf(SlideupVal));
        Log.d("Parent Bottom",String.valueOf(parent.getBottom()));
        Log.d("Parent Top",String.valueOf(parent.getTop()));
        float SlidedownloginVal=parent.getBottom()/4;
        Log.d("login Slideval",String.valueOf(SlidedownloginVal));
        Log.d("login Top",String.valueOf(loginbtn.getTop()));
        float SlidedownsignupVal=parent.getBottom()-Signupbtn.getTop();
        Log.d("Signup Slideval",String.valueOf(SlidedownsignupVal));
        Log.d("Sign up Top",String.valueOf(Signupbtn.getTop()));
        float[] AlphaVal={1.0f,0.0f};
        float[] FadeInVal={0.0f,1.0f};
        ObjectAnimator SlideUpLogoAnim=ObjectAnimator.ofFloat(logo,"translationY",SlideupVal);
        ObjectAnimator SlideDownloginBtnAnim=ObjectAnimator.ofFloat(loginbtn,"translationY",SlidedownloginVal);
        ObjectAnimator SlideDownsignupBtnAnim=ObjectAnimator.ofFloat(Signupbtn,"translationY",SlidedownsignupVal);
        ObjectAnimator FadeOutAnim=ObjectAnimator.ofFloat(Signupbtn,"alpha",AlphaVal);
        ObjectAnimator FadeInAnim=ObjectAnimator.ofFloat(LoginFields,"alpha",FadeInVal);
        FadeInAnim.setDuration(300);
        FadeInAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                LoginFields.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(SlideUpLogoAnim,SlideDownloginBtnAnim,SlideDownsignupBtnAnim,FadeOutAnim,FadeInAnim);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.setDuration(Duration);
        animatorSet.start();
    }

    public static void SlideOutLeft(final View view, int Duration)
    {
        float[] SlideOutValues={view.getLeft(),-view.getRight()};
        float[] AlphaVal={1.0f,0.0f};
        ObjectAnimator SlideOut=ObjectAnimator.ofFloat(view,"translationX",SlideOutValues);
        ObjectAnimator FadeOutAnim=ObjectAnimator.ofFloat(view,"alpha",AlphaVal);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(Duration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(SlideOut,FadeOutAnim);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

    public static void SlideOutRight(final View view, int Duration)
    {
        float[] SlideOutValues={view.getLeft(),(2*view.getRight())};
        float[] AlphaVal={1.0f,0.0f};
        ObjectAnimator SlideOut=ObjectAnimator.ofFloat(view,"translationX",SlideOutValues);
        ObjectAnimator FadeOutAnim=ObjectAnimator.ofFloat(view,"alpha",AlphaVal);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(Duration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(SlideOut,FadeOutAnim);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

    public static void SlideInRight(final View view, int Duration,int delay)
    {
        float[] SlideOutValues={(2*view.getRight()),0.0f};
        float[] AlphaVal={0.0f,1.0f};
        ObjectAnimator SlideIn=ObjectAnimator.ofFloat(view,"translationX",SlideOutValues);
        ObjectAnimator FadeOutAnim=ObjectAnimator.ofFloat(view,"alpha",AlphaVal);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(Duration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(SlideIn,FadeOutAnim);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

    public static AnimatorSet SlideUpAnim(View view,int Duration)
    {
        ViewGroup parent= (ViewGroup) view.getParent();
        float[] SlideVal={parent.getHeight(),0.0f};
        float[] AlphaVal={0.0f,1.0f};
        ObjectAnimator SlideUpAnim=ObjectAnimator.ofFloat(view,"translationY",SlideVal);
        ObjectAnimator AlphaAnim=ObjectAnimator.ofFloat(view,"alpha",AlphaVal);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(SlideUpAnim,AlphaAnim);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.setDuration(Duration);
        return animatorSet;
    }

    public static AnimatorSet ZoomInAnim(final View view,int Duration,int delay)
    {
        float[] Val={0.0f,1.0f};
        ObjectAnimator scaleXanim=ObjectAnimator.ofFloat(view,"scaleX",Val);
        ObjectAnimator scaleYanim=ObjectAnimator.ofFloat(view,"scaleY",Val);
        ObjectAnimator fadeinanim=ObjectAnimator.ofFloat(view,"alpha",Val);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(scaleXanim,scaleYanim,fadeinanim);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.setDuration(Duration);
        animatorSet.setStartDelay(delay);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animatorSet;
    }

    public static AnimatorSet ZoomOutAnim(final View view,int Duration,int delay)
    {
        float[] Val={1.0f,0.0f};
        ObjectAnimator scaleXanim=ObjectAnimator.ofFloat(view,"scaleX",Val);
        ObjectAnimator scaleYanim=ObjectAnimator.ofFloat(view,"scaleY",Val);
        ObjectAnimator fadeinanim=ObjectAnimator.ofFloat(view,"alpha",Val);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(scaleXanim, scaleYanim, fadeinanim);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.setDuration(Duration);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animatorSet;
    }

    public static void RubberBandAnim(View view,int Duration)
    {
        float[] Val={0.4f,1.10f,0.85f,1.0f};
        float[] fadeOut={1.0f,0.0f};
        float[] fadeIn={0.0f,1.0f};
        ObjectAnimator fadeOutanim=ObjectAnimator.ofFloat(view,"alpha",fadeOut);
        ObjectAnimator scaleXanim=ObjectAnimator.ofFloat(view,"scaleX",Val);
        ObjectAnimator scaleYanim=ObjectAnimator.ofFloat(view,"scaleY",Val);
        ObjectAnimator fadeInanim=ObjectAnimator.ofFloat(view,"alpha",fadeIn);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.play(fadeOutanim);
        animatorSet.playTogether(scaleYanim, scaleXanim, fadeInanim);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.setDuration(Duration);
        animatorSet.start();
    }


}
