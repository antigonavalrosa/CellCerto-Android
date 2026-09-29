package br.com.cellcertobox221.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

public class InfoActivity extends Activity {
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(ThemeUtil.bg(this));
        String mode=getIntent().getStringExtra("mode");

        ScrollView sv=new ScrollView(this);
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,16),Ui.dp(this,18),Ui.dp(this,28));
        root.setBackgroundColor(ThemeUtil.bg(this));
        sv.addView(root);

        root.addView(Ui.back(this));
        if("contact".equals(mode)) contact(root); else services(root);
        setContentView(sv);
    }

    private void services(LinearLayout root){
        root.addView(Ui.navTitle(this,"Nossos serviços"));
        LinearLayout c=Ui.section(this);
        c.addView(Ui.muted(this,"📱 Troca de tela e componentes\n🔋 Bateria e conector de carga\n🔧 Reparo de placa\n⚙ Atualização e software\n🧼 Limpeza técnica\n💻 Notebooks e computadores\n🖨 Impressão 3D personalizada",15));
        root.addView(c);
    }

    private void contact(LinearLayout root){
        root.addView(Ui.navTitle(this,"Localização e informações"));
        LinearLayout c=Ui.section(this);
        c.addView(Ui.title(this,"CellCerto Box 221",18));
        c.addView(Ui.muted(this,"Rua Almirante Cochrane, 257\nMercado Popular da Tijuca — Box 221\nRio de Janeiro — RJ\n\nSegunda a sexta: 10h às 19h\nSábado: 9h às 16h\n\nTelefone: (21) 99852-1264",14));
        root.addView(c);

        Button maps=Ui.primaryButton(this,"Abrir no Google Maps");
        maps.setOnClickListener(v->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/maps/search/?api=1&query=Rua+Almirante+Cochrane+257+Rio+de+Janeiro"))));
        root.addView(maps);

        Button waze=Ui.darkButton(this,"Abrir no Waze");
        Ui.margin(waze,0,8,0,8,this);
        waze.setOnClickListener(v->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.waze.com/ul?q=Rua%20Almirante%20Cochrane%20257%20Rio%20de%20Janeiro&navigate=yes"))));
        root.addView(waze);

        Button wa=Ui.darkButton(this,"Falar no WhatsApp");
        wa.setOnClickListener(v->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://wa.me/5521998521264"))));
        root.addView(wa);
    }
}
