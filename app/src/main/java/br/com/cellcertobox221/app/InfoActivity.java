package br.com.cellcertobox221.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

public class InfoActivity extends Activity {
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.parseColor("#071926"));

        String mode = getIntent().getStringExtra("mode");

        ScrollView sv = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,18),Ui.dp(this,18),Ui.dp(this,30));
        root.setBackgroundColor(Color.parseColor("#071926"));
        sv.addView(root);

        TextView back = Ui.text(this,"‹ Voltar",16,"#46D7E7");
        back.setOnClickListener(v->finish());
        root.addView(back);

        if("contact".equals(mode)) {
            contact(root);
        } else {
            services(root);
        }

        setContentView(sv);
    }

    private void services(LinearLayout root){
        TextView t = Ui.title(this,"Serviços",28);
        t.setPadding(0,12,0,10);
        root.addView(t);

        String s = "• Diagnóstico técnico"
                +"\n• Troca de tela e componentes"
                +"\n• Bateria e conector de carga"
                +"\n• Reparo de placa"
                +"\n• Atualização e software"
                +"\n• Limpeza técnica"
                +"\n• Tablets, notebooks e computadores"
                +"\n• Impressão 3D personalizada";

        LinearLayout c = Ui.section(this);
        c.addView(Ui.text(this,s,16,"#FFFFFF"));
        root.addView(c);

        TextView p = Ui.title(this,"Formas de pagamento",21);
        p.setPadding(0,18,0,8);
        root.addView(p);

        root.addView(Ui.text(this,
                "Você pode informar no agendamento sua preferência: Pix, débito, crédito, dinheiro ou definir no atendimento. A disponibilidade é confirmada pela CellCerto.",
                14,"#BCD2DC"));
    }

    private void contact(LinearLayout root){
        TextView t = Ui.title(this,"Localização e contato",28);
        t.setPadding(0,12,0,10);
        root.addView(t);

        LinearLayout c = Ui.section(this);
        c.addView(Ui.text(this,
                "CellCerto Box 221"
                        +"\nRua Almirante Cochrane, 257"
                        +"\nMercado Popular da Tijuca — Box 221"
                        +"\nRio de Janeiro — RJ"
                        +"\n\nSeg–Sex: 10h às 19h"
                        +"\nSábado: 9h às 16h"
                        +"\n\nTelefone: (21) 99852-1264",
                15,"#FFFFFF"));
        root.addView(c);

        Button maps = Ui.primaryButton(this,"Abrir no Google Maps");
        maps.setOnClickListener(v -> startActivity(
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=Rua+Almirante+Cochrane+257+Rio+de+Janeiro"))));
        root.addView(maps);

        Button whats = Ui.darkButton(this,"Falar no WhatsApp");
        Ui.margin(whats,0,8,0,0,this);
        whats.setOnClickListener(v -> startActivity(
                new Intent(Intent.ACTION_VIEW,Uri.parse("https://wa.me/5521998521264"))));
        root.addView(whats);
    }
}
