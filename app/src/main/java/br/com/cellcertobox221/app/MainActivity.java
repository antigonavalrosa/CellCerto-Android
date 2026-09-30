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
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView web;
    private final ExecutorService requests = Executors.newFixedThreadPool(3);
    private volatile boolean destroyed;
    private static final String API_URL = "https://zivhasnreefkpcqtwysq.supabase.co/functions/v1/cellcerto-api";
    private static final String API_KEY = "sb_publishable_VR-MB3jqYDcKn6a44Sbrbg_jmkdnZXZ";
    private static final String CHANNEL_ID = "cellcerto_updates";
    private static final int REQ_NOTIFICATIONS = 221;
    private static final int REQ_LOCATION = 222;
    private GeolocationPermissions.Callback geoCallback;
    private String geoOrigin;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().getDecorView().setSystemUiVisibility(0);
        getWindow().setStatusBarColor(Color.parseColor("#051922"));
        getWindow().setNavigationBarColor(Color.parseColor("#051922"));

        createNotificationChannel();
        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIFICATIONS);
        }

        web = new WebView(this);
        web.setBackgroundColor(Color.parseColor("#051922"));
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        s.setGeolocationEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        web.addJavascriptInterface(new AndroidBridge(), "Android");
        web.setWebChromeClient(new WebChromeClient() {
            @Override public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    callback.invoke(origin, true, false);
                } else {
                    geoOrigin = origin;
                    geoCallback = callback;
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LOCATION);
                }
            }
        });

        web.setWebViewClient(new WebViewClient() {
            @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (!"https".equals(uri.getScheme()) || !"appassets.androidplatform.net".equals(uri.getHost())) return null;
                String path = uri.getPath();
                if (path == null || !path.startsWith("/assets/") || path.contains("..")) return new WebResourceResponse("text/plain", "UTF-8", null);
                String asset = path.substring("/assets/".length());
                String mime = URLConnection.guessContentTypeFromName(asset);
                if (asset.endsWith(".webp")) mime = "image/webp";
                if (asset.endsWith(".svg")) mime = "image/svg+xml";
                if (asset.endsWith(".mp4")) mime = "video/mp4";
                if (mime == null) mime = "application/octet-stream";
                try { return new WebResourceResponse(mime, "UTF-8", getAssets().open(asset)); }
                catch (IOException e) { return new WebResourceResponse("text/plain", "UTF-8", null); }
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (!request.isForMainFrame()) return false;
                if ("https".equals(uri.getScheme()) && "appassets.androidplatform.net".equals(uri.getHost())) return false;
                try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); } catch (Exception ignored) {}
                return true;
            }
        });

        setContentView(web);
        web.loadUrl("https://appassets.androidplatform.net/assets/index.html");
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION && geoCallback != null && geoOrigin != null) {
            boolean granted = false;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) { granted = true; break; }
            }
            geoCallback.invoke(geoOrigin, granted, false);
            geoCallback = null;
            geoOrigin = null;
        }
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

    private void respond(String id, int status, String body) {
        runOnUiThread(() -> {
            if (!destroyed && web != null) web.evaluateJavascript("window.nativeApiResult(" + JSONObject.quote(id) + "," + status + "," + JSONObject.quote(body) + ")", null);
        });
    }

    public class AndroidBridge {
        @JavascriptInterface public void apiRequest(String id, String action, String payload, String token) {
            if (destroyed || id == null || !id.matches("[0-9]+") || action == null || !action.matches("create_booking|lookup|my_bookings|admin_setup|admin_reset|admin_login|admin_list|admin_status|admin_logout")) return;
            requests.execute(() -> {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) new URL(API_URL + "?action=" + action).openConnection();
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(20000);
                    connection.setInstanceFollowRedirects(false);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("apikey", API_KEY);
                    if (token != null && !token.isEmpty()) connection.setRequestProperty("x-admin-token", token);
                    connection.setDoOutput(true);
                    byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
                    connection.setFixedLengthStreamingMode(bytes.length);
                    try (java.io.OutputStream output = connection.getOutputStream()) { output.write(bytes); }
                    int status = connection.getResponseCode();
                    InputStream stream = status >= 400 ? connection.getErrorStream() : connection.getInputStream();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    if (stream != null) try (InputStream input = stream) {
                        byte[] chunk = new byte[4096]; int read;
                        while ((read = input.read(chunk)) != -1) {
                            buffer.write(chunk, 0, read);
                            if (buffer.size() > 2 * 1024 * 1024) throw new IOException("Resposta muito grande");
                        }
                    }
                    respond(id, status, new String(buffer.toByteArray(), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    android.util.Log.w("CellCertoApi", "Falha na conexão com a API", e);
                    respond(id, 0, "{\"error\":\"Não foi possível conectar. Verifique sua internet e tente novamente.\"}");
                } finally { if (connection != null) connection.disconnect(); }
            });
        }

        @JavascriptInterface public void notify(String title, String body) {
            runOnUiThread(() -> postNotification(title, body));
        }
    }

    @Override protected void onDestroy() {
        destroyed = true;
        requests.shutdownNow();
        if (web != null) { web.removeJavascriptInterface("Android"); web.destroy(); web = null; }
        super.onDestroy();
    }

    @Override public void onBackPressed() {
        web.evaluateJavascript("(function(){var a=document.querySelector('.page.active');if(a&&a.id!=='home'){show('home');return 'home'}return 'exit'})()", value -> {
            if (value != null && value.contains("exit")) super.onBackPressed();
        });
    }
}
