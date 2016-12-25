package neural.imagerecognizer.app.ui.views;

import android.animation.*;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import neural.imagerecognizer.app.R;

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

        this.animator = new AnimatorSet();
        animator.playTogether(animatorBackground);//, scaleDownX, scaleDownY);

    }

    public void startAnimation() {
        setText(R.string.label_recognizing);
        animator.start();
        setClickable(false);
        setFocusable(false);
    }

    public void endAnimation() {
        setText(R.string.label_whatis);
        animator.end();
        setClickable(true);
        setFocusable(true);
    }
}
