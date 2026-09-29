package br.com.cellcertobox221.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#071926"));

        ScrollView sv = new ScrollView(this);
        sv.setFillViewport(true);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18), Ui.dp(this,18), Ui.dp(this,18), Ui.dp(this,28));
        root.setBackgroundColor(Color.parseColor("#071926"));
        sv.addView(root);

        TextView brand = Ui.title(this, "cellcerto", 36);
        brand.setTextColor(Color.parseColor("#46D7E7"));
        brand.setGravity(Gravity.CENTER);
        root.addView(brand);

        TextView box = Ui.text(this, "BOX 221 • ASSISTÊNCIA TÉCNICA", 12, "#FFFFFF");
        box.setGravity(Gravity.CENTER);
        box.setPadding(0, 0, 0, Ui.dp(this,18));
        root.addView(box);

        TextView hello = Ui.title(this, "Assistência na palma da sua mão", 26);
        root.addView(hello);

        TextView sub = Ui.text(this,
                "Agende atendimento, informe seu aparelho e problema, escolha o serviço e envie tudo pronto para confirmação.",
                15, "#BBD3DF");
        sub.setPadding(0, Ui.dp(this,8),0,Ui.dp(this,12));
        root.addView(sub);

        root.addView(Ui.badge(this, "ATENDIMENTO POR AGENDAMENTO"));

        Button agendar = Ui.primaryButton(this, "Agendar atendimento");
        Ui.margin(agendar,0,22,0,8,this);
        agendar.setOnClickListener(v -> startActivity(new Intent(this, BookingActivity.class)));
        root.addView(agendar);

        Button historico = Ui.darkButton(this, "Meus agendamentos");
        Ui.margin(historico,0,4,0,8,this);
        historico.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        root.addView(historico);

        Button servicos = Ui.darkButton(this, "Serviços e formas de pagamento");
        Ui.margin(servicos,0,4,0,8,this);
        servicos.setOnClickListener(v -> {
            Intent i = new Intent(this, InfoActivity.class);
            i.putExtra("mode", "services");
            startActivity(i);
        });
        root.addView(servicos);

        Button contato = Ui.darkButton(this, "Localização e contato");
        Ui.margin(contato,0,4,0,20,this);
        contato.setOnClickListener(v -> {
            Intent i = new Intent(this, InfoActivity.class);
            i.putExtra("mode", "contact");
            startActivity(i);
        });
        root.addView(contato);

        LinearLayout card = Ui.section(this);
        TextView h = Ui.title(this,"Horário",19);
        card.addView(h);
        TextView hours = Ui.text(this,
                "Segunda a sexta: 10h às 19h\nSábado: 9h às 16h\nDomingo: fechado",
                15,"#FFFFFF");
        hours.setPadding(0,Ui.dp(this,10),0,0);
        card.addView(hours);
        root.addView(card);

        TextView note = Ui.text(this,
                "O horário escolhido é uma solicitação. A CellCerto confirma a disponibilidade pelo WhatsApp.",
                12,"#8FAEBC");
        note.setGravity(Gravity.CENTER);
        note.setPadding(8,14,8,0);
        root.addView(note);

        setContentView(sv);
    }
}
