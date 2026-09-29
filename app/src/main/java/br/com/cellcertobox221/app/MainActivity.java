package br.com.cellcertobox221.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {
    @Override protected void onCreate(Bundle b){ super.onCreate(b); build(); }
    @Override protected void onResume(){ super.onResume(); build(); }

    private void build(){
        getWindow().setStatusBarColor(ThemeUtil.bg(this));
        getWindow().setNavigationBarColor(ThemeUtil.bg(this));

        ScrollView sv=new ScrollView(this);
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,14),Ui.dp(this,18),Ui.dp(this,28));
        root.setBackgroundColor(ThemeUtil.bg(this));
        sv.addView(root);

        LinearLayout top=new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        BrandView logo=new BrandView(this);
        top.addView(logo,new LinearLayout.LayoutParams(0,Ui.dp(this,78),1f));
        Button gear=Ui.darkButton(this,"☾ / ☀");
        gear.setOnClickListener(v->startActivity(new Intent(this,SettingsActivity.class)));
        top.addView(gear,new LinearLayout.LayoutParams(Ui.dp(this,92),Ui.dp(this,50)));
        root.addView(top);

        TextView h=Ui.title(this,"Seu aparelho em boas mãos",28);
        h.setPadding(0,12,0,5);
        root.addView(h);
        root.addView(Ui.muted(this,"Agende, acompanhe o reparo e consulte tudo pelo aplicativo CellCerto.",15));

        Button book=Ui.primaryButton(this,"📅  Agendar atendimento");
        Ui.margin(book,0,18,0,10,this);
        book.setOnClickListener(v->startActivity(new Intent(this,BookingActivity.class)));
        root.addView(book);

        LinearLayout quick=new LinearLayout(this);
        quick.setOrientation(LinearLayout.HORIZONTAL);
        quick.setWeightSum(2);
        root.addView(quick);
        addQuick(quick,"🔧\nAcompanhar\nreparo",TrackingActivity.class);
        addQuick(quick,"🧾\nHistórico e\ngarantia",WarrantyActivity.class);

        LinearLayout quick2=new LinearLayout(this);
        quick2.setOrientation(LinearLayout.HORIZONTAL);
        quick2.setWeightSum(2);
        root.addView(quick2);
        addQuick(quick2,"📍\nLocalização\ne horários",InfoActivity.class,"contact");
        addQuick(quick2,"💳\nPagamentos e\nparcelas",PaymentSimulatorActivity.class);

        LinearLayout quick3=new LinearLayout(this);
        quick3.setOrientation(LinearLayout.HORIZONTAL);
        quick3.setWeightSum(2);
        root.addView(quick3);
        addQuick(quick3,"🔔\nNotificações\ne lembretes",NotificationCenterActivity.class);
        addQuick(quick3,"⚙\nConfigurações\ndo app",SettingsActivity.class);

        LinearLayout card=Ui.section(this);
        card.addView(Ui.title(this,"Atendimento CellCerto",19));
        TextView cbody=Ui.muted(this,"Segunda a sexta: 10h às 19h\nSábado: 9h às 16h\nAtendimento por agendamento.",14);
        cbody.setPadding(0,8,0,0);
        card.addView(cbody);
        root.addView(card);

        TextView footer=Ui.muted(this,"CellCerto Box 221 • Tijuca • Rio de Janeiro",12);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0,12,0,0);
        root.addView(footer);
        setContentView(sv);
    }

    private void addQuick(LinearLayout row,String label,Class<?> cls){ addQuick(row,label,cls,null); }
    private void addQuick(LinearLayout row,String label,Class<?> cls,String mode){
        Button b=Ui.darkButton(this,label);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,Ui.dp(this,104),1f);
        p.setMargins(Ui.dp(this,4),Ui.dp(this,5),Ui.dp(this,4),Ui.dp(this,5));
        b.setLayoutParams(p);
        b.setOnClickListener(v->{
            Intent i=new Intent(this,cls);
            if(mode!=null) i.putExtra("mode",mode);
            startActivity(i);
        });
        row.addView(b);
    }
}
