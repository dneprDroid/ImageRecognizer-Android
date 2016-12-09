package neural.imagerecognizer.app.ui.views;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import neural.imagerecognizer.app.R;
import neural.imagerecognizer.app.util.Tool;

public class WhatisButton extends Button {
    private static final long ANIMATION_DURATION = 1000;
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
        this.animator = ObjectAnimator.ofInt(this, "backgroundColor", Color.BLUE, Color.CYAN, Color.BLUE);
        animator.setDuration(ANIMATION_DURATION);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        setBackgroundColor(Color.BLUE);
        setTextColor(Color.WHITE);
        setText(R.string.label_whatis);
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
