package android.imagerecognizer.app.ui.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

public class WhatisButton extends Button {
    private static final long ANIMATION_DURATION = 500;
    private ValueAnimator animator;

    public WhatisButton(Context context) {
        super(context);
        init();
    }


    public WhatisButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.animator = ObjectAnimator.ofFloat(this, "background", Color.BLUE, Color.WHITE, Color.BLUE);
        animator.setDuration(ANIMATION_DURATION);
        animator.setRepeatMode(ValueAnimator.INFINITE);
        setBackgroundColor(Color.BLUE);
        setTextColor(Color.WHITE);
    }

    public void startAnimation() {
        animator.start();
        setClickable(false);
        setFocusable(false);
        setTextColor(Color.GRAY);
    }

    public void endAnimation() {
        animator.end();
        setClickable(true);
        setFocusable(true);
        setTextColor(Color.WHITE);
    }
}
