package br.com.cellcertobox221.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class SplashActivity extends Activity {
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        int bg=Color.parseColor("#D7D9DB");
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);

        FrameLayout root=new FrameLayout(this);
        root.setBackgroundColor(bg);

        BrandView logo=new BrandView(this);
        logo.setAlpha(0f);
        logo.setScaleX(.72f);
        logo.setScaleY(.72f);

        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(-1,Ui.dp(this,145));
        lp.gravity=Gravity.CENTER;
        lp.leftMargin=Ui.dp(this,24);
        lp.rightMargin=Ui.dp(this,24);
        root.addView(logo,lp);
        setContentView(root);

        logo.animate()
                .alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(900)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(()->logo.postDelayed(()->root.animate()
                        .alpha(0f).setDuration(320)
                        .setListener(new AnimatorListenerAdapter(){
                            @Override public void onAnimationEnd(Animator a){
                                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                                finish();
                            }
                        }),800));
    }
}
