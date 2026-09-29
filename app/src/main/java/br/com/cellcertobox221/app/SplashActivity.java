package br.com.cellcertobox221.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class SplashActivity extends Activity {
    private boolean opened=false;

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().setNavigationBarColor(Color.BLACK);

        FrameLayout root=new FrameLayout(this);
        root.setBackgroundColor(Color.BLACK);

        VideoView video=new VideoView(this);
        video.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.intro));
        FrameLayout.LayoutParams vp=new FrameLayout.LayoutParams(-1,-1);
        vp.gravity=Gravity.CENTER;
        root.addView(video,vp);

        TextView skip=new TextView(this);
        skip.setText("Pular ›");
        skip.setTextColor(Color.WHITE);
        skip.setTextSize(14);
        skip.setGravity(Gravity.CENTER);
        skip.setPadding(Ui.dp(this,14),Ui.dp(this,8),Ui.dp(this,14),Ui.dp(this,8));
        skip.setBackground(Ui.bg("#66000000",Ui.dp(this,18)));
        FrameLayout.LayoutParams sp=new FrameLayout.LayoutParams(-2,-2);
        sp.gravity=Gravity.TOP|Gravity.RIGHT;
        sp.setMargins(0,Ui.dp(this,22),Ui.dp(this,18),0);
        root.addView(skip,sp);

        skip.setOnClickListener(v->openApp());
        video.setOnCompletionListener(mp->openApp());
        video.setOnErrorListener((mp,what,extra)->{ openApp(); return true; });

        setContentView(root);
        video.start();

        // Segurança: se algum aparelho não conseguir tocar o vídeo, entra no app mesmo assim.
        root.postDelayed(this::openApp,6500);
    }

    private void openApp(){
        if(opened)return;
        opened=true;
        startActivity(new Intent(this,MainActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
