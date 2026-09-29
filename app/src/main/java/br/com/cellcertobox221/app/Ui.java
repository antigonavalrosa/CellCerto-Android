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
    public static int dp(Activity a,int v){ return (int)(v*a.getResources().getDisplayMetrics().density+.5f); }
    public static GradientDrawable bg(String color,float radius){ GradientDrawable d=new GradientDrawable();d.setColor(Color.parseColor(color));d.setCornerRadius(radius);return d; }
    public static GradientDrawable strokeBg(String color,String stroke,float radius,int strokeDp,Activity a){ GradientDrawable d=bg(color,radius);d.setStroke(dp(a,strokeDp),Color.parseColor(stroke));return d; }
    public static TextView title(Activity a,String text,int sp){ TextView t=text(a,text,sp,ThemeUtil.hex(ThemeUtil.text(a)));t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t; }
    public static TextView text(Activity a,String text,int sp,String color){ TextView t=new TextView(a);t.setText(text);t.setTextColor(Color.parseColor(color));t.setTextSize(sp);t.setLineSpacing(0,1.12f);return t; }
    public static TextView muted(Activity a,String text,int sp){ return text(a,text,sp,ThemeUtil.hex(ThemeUtil.muted(a))); }
    public static Button primaryButton(Activity a,String text){ Button b=new Button(a);b.setText(text);b.setTextSize(16);b.setTextColor(Color.WHITE);b.setAllCaps(false);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);b.setBackground(bg("#0796AA",dp(a,16)));b.setPadding(dp(a,16),dp(a,12),dp(a,16),dp(a,12));return b; }
    public static Button darkButton(Activity a,String text){ Button b=new Button(a);b.setText(text);b.setTextSize(15);b.setTextColor(ThemeUtil.text(a));b.setAllCaps(false);b.setBackground(strokeBg(ThemeUtil.hex(ThemeUtil.surface(a)),"#95A6AC",dp(a,16),1,a));b.setPadding(dp(a,14),dp(a,12),dp(a,14),dp(a,12));return b; }
    public static void margin(View v,int l,int t,int r,int b,Activity a){ LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);p.setMargins(dp(a,l),dp(a,t),dp(a,r),dp(a,b));v.setLayoutParams(p); }
    public static LinearLayout section(Activity a){ LinearLayout l=new LinearLayout(a);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(dp(a,18),dp(a,18),dp(a,18),dp(a,18));l.setBackground(strokeBg(ThemeUtil.hex(ThemeUtil.surface(a)),ThemeUtil.isDark(a)?"#264955":"#C7CDD1",dp(a,20),1,a));margin(l,0,10,0,10,a);return l; }
    public static TextView badge(Activity a,String text){ TextView t=text(a,text,12,"#FFFFFF");t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setGravity(Gravity.CENTER);t.setPadding(dp(a,12),dp(a,7),dp(a,12),dp(a,7));t.setBackground(bg("#0796AA",dp(a,50)));return t; }
    public static TextView navTitle(Activity a,String title){ TextView t=title(a,title,26);t.setPadding(0,6,0,4);return t; }
    public static TextView back(Activity a){ TextView b=text(a,"‹ Voltar",16,"#0796AA");b.setPadding(0,0,0,10);b.setOnClickListener(v->a.finish());return b; }
}
