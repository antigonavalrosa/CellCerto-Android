package br.com.cellcertobox221.app;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView web;
    private static final String CHANNEL_ID = "cellcerto_updates";

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.parseColor("#061923"));
        getWindow().setNavigationBarColor(Color.parseColor("#061923"));

        createNotificationChannel();
        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 221);
        }

        web = new WebView(this);
        web.setBackgroundColor(Color.parseColor("#061923"));
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        web.addJavascriptInterface(new AndroidBridge(), "Android");
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if ("file".equals(uri.getScheme())) return false;
                if ("https".equals(uri.getScheme()) || "http".equals(uri.getScheme())) {
                    String host = uri.getHost() == null ? "" : uri.getHost();
                    if (host.contains("google.com") || host.contains("gstatic.com") || host.contains("googleusercontent.com") || host.contains("pexels.com")) {
                        return false;
                    }
                }
                try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); } catch (Exception ignored) {}
                return true;
            }
        });

        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(CHANNEL_ID, "Atualizações de reparo", NotificationManager.IMPORTANCE_DEFAULT);
            ch.setDescription("Atualizações dos atendimentos CellCerto");
            getSystemService(NotificationManager.class).createNotificationChannel(ch);
        }
    }

    private void postNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Notification.Builder b = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? new Notification.Builder(this, CHANNEL_ID) : new Notification.Builder(this);
        b.setSmallIcon(R.drawable.ic_cellcerto)
         .setContentTitle(title)
         .setContentText(body)
         .setAutoCancel(true)
         .setContentIntent(pi);
        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify((int)(System.currentTimeMillis()%100000), b.build());
    }

    public class AndroidBridge {
        @JavascriptInterface public void notify(String title, String body) {
            runOnUiThread(() -> postNotification(title, body));
        }
    }

    @Override public void onBackPressed() {
        web.evaluateJavascript("(function(){var a=document.querySelector('.page.active');if(a&&a.id!=='home'){show('home');return 'home'}return 'exit'})()", value -> {
            if (value != null && value.contains("exit")) super.onBackPressed();
        });
    }
}
