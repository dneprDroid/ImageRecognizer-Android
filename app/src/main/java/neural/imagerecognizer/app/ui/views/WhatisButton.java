package neural.imagerecognizer.app.ui.views;

import android.animation.*;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import neural.imagerecognizer.app.R;
import neural.imagerecognizer.app.RecognitionApp;
import neural.imagerecognizer.app.util.Tool;

public class WhatisButton extends Button {
    private static final long ANIMATION_DURATION = 1000;
    private AnimatorSet animator;

    public WhatisButton(Context context) {
        super(context);
        init();
    }


    public WhatisButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setBackgroundColor(Color.BLUE);
        setTextColor(Color.WHITE);
        setText(R.string.label_whatis);

        ValueAnimator animatorBackground = ObjectAnimator.ofInt(this, "backgroundColor", Color.BLUE, Color.CYAN, Color.BLUE);

        animatorBackground.setEvaluator(new ArgbEvaluator());
        animatorBackground.setDuration(ANIMATION_DURATION);
        animatorBackground.setRepeatCount(ValueAnimator.INFINITE);
        animatorBackground.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleDownX = ObjectAnimator.ofInt(this, "scaleX", 1, 2, 1);
        ObjectAnimator scaleDownY = ObjectAnimator.ofInt(this, "scaleY", 1, 2, 1);

        scaleDownX.setDuration(ANIMATION_DURATION);
        scaleDownX.setRepeatCount(ValueAnimator.INFINITE);
        scaleDownX.setRepeatMode(ValueAnimator.REVERSE);

        scaleDownY.setDuration(ANIMATION_DURATION);
        scaleDownY.setRepeatCount(ValueAnimator.INFINITE);
        scaleDownY.setRepeatMode(ValueAnimator.REVERSE);

        this.animator = new AnimatorSet();
        animator.playTogether(animatorBackground, scaleDownX, scaleDownY);

    }

    public void startAnimation() {
        setTextWithAnimation(R.string.label_recognizing);
        animator.start();
        setClickable(false);
        setFocusable(false);
        setTextColor(Color.GRAY);
    }

    private void setTextWithAnimation(@StringRes final int labelId) {
        String newLabel = getContext().getString(labelId);
        int newWidth = newLabel.length();
        int oldWidth = getText() == null ? 0 : getText().length();

        ViewCompat.animate(this)
                .scaleX((float) newWidth / oldWidth)
                .setDuration(300)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        setText(labelId);
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                }).start();
    }

    public void endAnimation() {
        setTextWithAnimation(R.string.label_whatis);
        animator.end();
        setClickable(true);
        setFocusable(true);
        setTextColor(Color.WHITE);
    }
}
