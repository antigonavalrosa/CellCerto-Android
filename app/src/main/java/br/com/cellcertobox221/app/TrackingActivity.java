package br.com.cellcertobox221.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;

public class TrackingActivity extends Activity {
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(ThemeUtil.bg(this));
        ScrollView sv=new ScrollView(this);
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,16),Ui.dp(this,18),Ui.dp(this,28));
        root.setBackgroundColor(ThemeUtil.bg(this));
        sv.addView(root);
        root.addView(Ui.back(this));
        root.addView(Ui.navTitle(this,"Acompanhamento do aparelho"));
        root.addView(Ui.muted(this,"Consulte a etapa do seu atendimento usando o protocolo informado pela loja.",14));

        EditText protocol=new EditText(this);
        protocol.setHint("Ex.: CC221-0001");
        protocol.setTextColor(ThemeUtil.text(this));
        protocol.setHintTextColor(ThemeUtil.muted(this));
        protocol.setBackground(Ui.strokeBg(ThemeUtil.hex(ThemeUtil.surface(this)),"#A8B2B7",Ui.dp(this,14),1,this));
        protocol.setPadding(16,12,16,12);
        Ui.margin(protocol,0,16,0,8,this);
        root.addView(protocol);

        Button consult=Ui.primaryButton(this,"Consultar protocolo");
        root.addView(consult);

        LinearLayout status=Ui.section(this);
        status.setVisibility(android.view.View.GONE);
        status.addView(Ui.title(this,"Status do aparelho",20));
        TextView tl=Ui.muted(this,"✓ Recebido\n● Em análise\n○ Aguardando peças\n○ Em reparo\n○ Pronto para retirada",15);
        tl.setPadding(0,12,0,4);
        status.addView(tl);
        TextView note=Ui.muted(this,"A tela está pronta para integração. A atualização realmente em tempo real depende do painel online da CellCerto.",12);
        note.setPadding(0,10,0,0);
        status.addView(note);
        root.addView(status);

        consult.setOnClickListener(v->{
            if(protocol.getText().toString().trim().isEmpty()) Toast.makeText(this,"Digite seu protocolo.",Toast.LENGTH_SHORT).show();
            else status.setVisibility(android.view.View.VISIBLE);
        });
        setContentView(sv);
    }
}
