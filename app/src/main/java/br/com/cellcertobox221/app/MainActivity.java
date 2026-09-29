package br.com.cellcertobox221.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {
    private int teal=Color.parseColor("#079FB3");
    private int navy=Color.parseColor("#10242D");
    private int muted=Color.parseColor("#5B6970");
    private int soft=Color.parseColor("#F3F7F9");

    @Override protected void onCreate(Bundle b){ super.onCreate(b); build(); }
    @Override protected void onResume(){ super.onResume(); }

    private int dp(int v){ return (int)(v*getResources().getDisplayMetrics().density+.5f); }

    private GradientDrawable round(int color,int radius){
        GradientDrawable d=new GradientDrawable();
        d.setColor(color); d.setCornerRadius(dp(radius)); return d;
    }
    private GradientDrawable outlined(int color,int stroke,int radius){
        GradientDrawable d=round(color,radius);
        d.setStroke(dp(1),stroke); return d;
    }
    private TextView label(String text,int sp,int color,boolean bold){
        TextView t=new TextView(this);
        t.setText(text); t.setTextSize(sp); t.setTextColor(color);
        if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        return t;
    }

    @Override public void onBackPressed(){ super.onBackPressed(); }

    private void build(){
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout page=new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setBackgroundColor(Color.WHITE);

        ScrollView sv=new ScrollView(this);
        sv.setFillViewport(true);
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16),dp(12),dp(16),dp(22));
        sv.addView(root);
        page.addView(sv,new LinearLayout.LayoutParams(-1,0,1f));

        // Cabeçalho
        LinearLayout header=new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        BrandView logo=new BrandView(this);
        header.addView(logo,new LinearLayout.LayoutParams(0,dp(76),1f));

        TextView bell=label("🔔",24,navy,false);
        bell.setGravity(Gravity.CENTER);
        bell.setBackground(round(soft,18));
        bell.setOnClickListener(v->startActivity(new Intent(this,NotificationCenterActivity.class)));
        LinearLayout.LayoutParams bellLp=new LinearLayout.LayoutParams(dp(54),dp(54));
        bellLp.setMargins(dp(10),0,0,0);
        header.addView(bell,bellLp);
        root.addView(header);

        // Hero
        LinearLayout hero=new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setPadding(dp(18),dp(18),dp(18),dp(18));
        hero.setBackground(outlined(Color.WHITE,Color.parseColor("#D7E2E7"),22));
        LinearLayout.LayoutParams heroLp=new LinearLayout.LayoutParams(-1,-2);
        heroLp.setMargins(0,dp(14),0,dp(12));
        root.addView(hero,heroLp);

        hero.addView(label("Olá, seja bem-vindo!",25,navy,true));
        TextView sub=label("Assistência técnica especializada para manter seu mundo sempre conectado.",15,muted,false);
        sub.setPadding(0,dp(6),0,dp(14));
        sub.setLineSpacing(0,1.12f);
        hero.addView(sub);

        Button book=new Button(this);
        book.setAllCaps(false);
        book.setText("▣  Agendar atendimento   ›");
        book.setTextColor(Color.WHITE);
        book.setTextSize(17);
        book.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        book.setGravity(Gravity.CENTER);
        book.setBackground(round(teal,18));
        book.setOnClickListener(v->startActivity(new Intent(this,BookingActivity.class)));
        hero.addView(book,new LinearLayout.LayoutParams(-1,dp(58)));

        TextView section=label("O que podemos cuidar para você?",18,navy,true);
        section.setPadding(dp(3),dp(8),0,dp(8));
        root.addView(section);

        // Grade 3x2 inspirada na referência
        addServiceRow(root,
                "📱","Celulares",BookingActivity.class,
                "💻","Notebooks",BookingActivity.class,
                "▰","Tablets",BookingActivity.class);
        addServiceRow(root,
                "◉","iPhone / iPad",BookingActivity.class,
                "♟","Impressão 3D",BookingActivity.class,
                "🎧","Outros",BookingActivity.class);

        // Diagnóstico
        LinearLayout diag=new LinearLayout(this);
        diag.setGravity(Gravity.CENTER_VERTICAL);
        diag.setPadding(dp(14),dp(13),dp(14),dp(13));
        diag.setBackground(round(Color.parseColor("#E9F7FB"),18));
        TextView di=label("▣",25,teal,true);
        di.setGravity(Gravity.CENTER);
        diag.addView(di,new LinearLayout.LayoutParams(dp(48),dp(48)));
        LinearLayout dt=new LinearLayout(this); dt.setOrientation(LinearLayout.VERTICAL);
        dt.addView(label("Diagnóstico completo",16,navy,true));
        dt.addView(label("com transparência e orçamento sem compromisso.",13,muted,false));
        diag.addView(dt,new LinearLayout.LayoutParams(0,-2,1f));
        TextView arrow=label("›",28,teal,true); arrow.setGravity(Gravity.CENTER);
        diag.addView(arrow,new LinearLayout.LayoutParams(dp(34),dp(48)));
        diag.setOnClickListener(v->startActivity(new Intent(this,BookingActivity.class)));
        LinearLayout.LayoutParams dl=new LinearLayout.LayoutParams(-1,-2);
        dl.setMargins(0,dp(12),0,dp(14)); root.addView(diag,dl);

        // Acompanhamento destacado
        LinearLayout status=new LinearLayout(this);
        status.setOrientation(LinearLayout.VERTICAL);
        status.setPadding(dp(16),dp(14),dp(16),dp(14));
        status.setBackground(round(navy,20));
        status.addView(label("Acompanhe seu aparelho",18,Color.WHITE,true));
        TextView statusSub=label("Veja recebimento, análise, peças e conclusão do reparo.",14,Color.parseColor("#C8D8DE"),false);
        statusSub.setPadding(0,dp(5),0,dp(11)); status.addView(statusSub);
        Button track=new Button(this);
        track.setAllCaps(false); track.setText("Abrir acompanhamento");
        track.setTextColor(Color.WHITE); track.setTextSize(15);
        track.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        track.setBackground(round(teal,14));
        track.setOnClickListener(v->startActivity(new Intent(this,TrackingActivity.class)));
        status.addView(track,new LinearLayout.LayoutParams(-1,dp(50)));
        root.addView(status);

        TextView hours=label("Segunda a sexta  •  10h às 19h\nSábado  •  9h às 16h  •  Atendimento por agendamento",13,muted,false);
        hours.setGravity(Gravity.CENTER);
        hours.setPadding(0,dp(16),0,dp(4));
        root.addView(hours);

        // Barra inferior parecida com a arte
        LinearLayout nav=new LinearLayout(this);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(4),dp(5),dp(4),dp(5));
        nav.setBackground(outlined(Color.WHITE,Color.parseColor("#D7E2E7"),0));
        addNav(nav,"⌂","Início",null);
        addNav(nav,"▦","Serviços",BookingActivity.class);
        addNav(nav,"▣","Orçamentos",PaymentSimulatorActivity.class);
        addNav(nav,"🔔","Notificações",NotificationCenterActivity.class);
        addNav(nav,"☰","Mais",SettingsActivity.class);
        page.addView(nav,new LinearLayout.LayoutParams(-1,dp(68)));

        setContentView(page);
    }

    private void addServiceRow(LinearLayout root,
                               String i1,String t1,Class<?> c1,
                               String i2,String t2,Class<?> c2,
                               String i3,String t3,Class<?> c3){
        LinearLayout row=new LinearLayout(this);
        row.setWeightSum(3f);
        serviceCard(row,i1,t1,c1);
        serviceCard(row,i2,t2,c2);
        serviceCard(row,i3,t3,c3);
        root.addView(row,new LinearLayout.LayoutParams(-1,dp(112)));
    }

    private void serviceCard(LinearLayout row,String icon,String title,Class<?> cls){
        LinearLayout card=new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(6),dp(10),dp(6),dp(10));
        card.setBackground(outlined(soft,Color.parseColor("#DFE8EC"),16));
        TextView iv=label(icon,28,navy,false); iv.setGravity(Gravity.CENTER);
        TextView tv=label(title,13,navy,true); tv.setGravity(Gravity.CENTER);
        card.addView(iv,new LinearLayout.LayoutParams(-1,0,1f));
        card.addView(tv);
        card.setOnClickListener(v->startActivity(new Intent(this,cls)));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,-1,1f);
        lp.setMargins(dp(4),dp(4),dp(4),dp(4));
        row.addView(card,lp);
    }

    private void addNav(LinearLayout nav,String icon,String title,Class<?> cls){
        LinearLayout item=new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        TextView ic=label(icon,20,cls==null?teal:navy,false); ic.setGravity(Gravity.CENTER);
        TextView tx=label(title,10,cls==null?teal:navy,cls==null); tx.setGravity(Gravity.CENTER);
        item.addView(ic); item.addView(tx);
        if(cls!=null)item.setOnClickListener(v->startActivity(new Intent(this,cls)));
        nav.addView(item,new LinearLayout.LayoutParams(0,-1,1f));
    }
}
