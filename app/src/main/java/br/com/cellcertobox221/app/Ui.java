package br.com.cellcertobox221.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Ui {
    public static int dp(Activity a, int value) {
        return (int) (value * a.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static GradientDrawable bg(String color, float radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(Color.parseColor(color));
        d.setCornerRadius(radius);
        return d;
    }

    public static GradientDrawable strokeBg(String color, String stroke, float radius, int strokeDp, Activity a) {
        GradientDrawable d = bg(color, radius);
        d.setStroke(dp(a, strokeDp), Color.parseColor(stroke));
        return d;
    }

    public static TextView title(Activity a, String text, int sp) {
        TextView t = new TextView(a);
        t.setText(text);
        t.setTextColor(Color.WHITE);
        t.setTextSize(sp);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    public static TextView text(Activity a, String text, int sp, String color) {
        TextView t = new TextView(a);
        t.setText(text);
        t.setTextColor(Color.parseColor(color));
        t.setTextSize(sp);
        t.setLineSpacing(0, 1.15f);
        return t;
    }

    public static Button primaryButton(Activity a, String text) {
        Button b = new Button(a);
        b.setText(text);
        b.setTextSize(16);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setBackground(bg("#03AFC8", dp(a, 16)));
        b.setPadding(dp(a,16), dp(a,12), dp(a,16), dp(a,12));
        return b;
    }

    public static Button darkButton(Activity a, String text) {
        Button b = new Button(a);
        b.setText(text);
        b.setTextSize(15);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setBackground(strokeBg("#0D2B3B", "#1F596E", dp(a, 16), 1, a));
        b.setPadding(dp(a,14), dp(a,12), dp(a,14), dp(a,12));
        return b;
    }

    public static void margin(View v, int l, int t, int r, int b, Activity a) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(dp(a,l), dp(a,t), dp(a,r), dp(a,b));
        v.setLayoutParams(p);
    }

    public static LinearLayout section(Activity a) {
        LinearLayout l = new LinearLayout(a);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(dp(a,18), dp(a,18), dp(a,18), dp(a,18));
        l.setBackground(bg("#0D2B3B", dp(a,20)));
        margin(l, 16, 10, 16, 10, a);
        return l;
    }

    public static TextView badge(Activity a, String text) {
        TextView t = text(a, text, 12, "#46D7E7");
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setGravity(Gravity.CENTER);
        t.setPadding(dp(a,12),dp(a,7),dp(a,12),dp(a,7));
        t.setBackground(strokeBg("#092231", "#03AFC8", dp(a,50),1,a));
        return t;
    }
}
