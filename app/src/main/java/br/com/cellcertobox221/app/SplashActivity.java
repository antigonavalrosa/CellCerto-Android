package br.com.cellcertobox221.app;

import android.animation.*;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

public class SplashActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.rgb(242,242,242));
        getWindow().setNavigationBarColor(Color.rgb(242,242,242));

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(242,242,242));
        SplashLogoView logo = new SplashLogoView(this);
        root.addView(logo, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(root);

        ValueAnimator plate = ValueAnimator.ofFloat(0f, 1f);
        plate.setDuration(700);
        plate.setInterpolator(new AccelerateDecelerateInterpolator());
        plate.addUpdateListener(a -> logo.setPlateProgress((float)a.getAnimatedValue()));

        ValueAnimator text = ValueAnimator.ofFloat(0f, 1f);
        text.setDuration(550);
        text.addUpdateListener(a -> logo.setLogoAlpha((float)a.getAnimatedValue()));

        ValueAnimator sub = ValueAnimator.ofFloat(0f, 1f);
        sub.setDuration(450);
        sub.addUpdateListener(a -> logo.setSubAlpha((float)a.getAnimatedValue()));

        ValueAnimator pulse = ValueAnimator.ofFloat(0.92f, 1.03f, 1f);
        pulse.setDuration(700);
        pulse.addUpdateListener(a -> logo.setLogoScale((float)a.getAnimatedValue()));

        AnimatorSet set = new AnimatorSet();
        set.play(plate).before(text);
        set.play(text).with(pulse);
        set.play(sub).after(text);
        set.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                logo.postDelayed(() -> {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }, 650);
            }
        });
        set.start();
    }
}
