package br.com.cellcertobox221.app;

import android.app.Activity;
import android.content.*;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import org.json.*;
import java.net.URLEncoder;

public class SummaryActivity extends Activity {
    private JSONObject a;

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.parseColor("#071926"));

        try {
            a = new JSONObject(getIntent().getStringExtra("appointment"));
        } catch(Exception e) {
            finish();
            return;
        }

        ScrollView sv = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,18),Ui.dp(this,18),Ui.dp(this,28));
        root.setBackgroundColor(Color.parseColor("#071926"));
        sv.addView(root);

        TextView back = Ui.text(this,"‹ Voltar e editar",15,"#46D7E7");
        back.setOnClickListener(v->finish());
        root.addView(back);

        TextView t = Ui.title(this,"Confira sua solicitação",27);
        t.setPadding(0,12,0,8);
        root.addView(t);

        TextView n = Ui.text(this,
                "Nada é reservado automaticamente. Ao confirmar, a solicitação será enviada à CellCerto pelo WhatsApp.",
                13,"#AFC8D3");
        n.setPadding(0,0,0,12);
        root.addView(n);

        LinearLayout card = Ui.section(this);
        card.addView(Ui.text(this,summary(),15,"#FFFFFF"));
        root.addView(card);

        Button send = Ui.primaryButton(this,"Confirmar e enviar pelo WhatsApp");
        send.setOnClickListener(v->confirm());
        root.addView(send);

        setContentView(sv);
    }

    private String val(String k){
        return a.optString(k,"-");
    }

    private String summary(){
        return "Cliente: "+val("nome")
                +"\nWhatsApp: "+val("whatsapp")
                +"\n\nAparelho: "+val("tipo")+" • "+val("marca")+" • "+val("modelo")
                +"\nProblema: "+val("problema")
                +"\nServiço: "+val("servico")
                +"\nPeça/tela: "+val("tela")
                +"\n\nData: "+val("data")+" às "+val("horario")
                +"\nPagamento: "+val("pagamento")
                +(val("obs").isEmpty() ? "" : "\n\nObservações: "+val("obs"));
    }

    private void confirm(){
        saveHistory();
        String msg = "Olá, CellCerto! Gostaria de solicitar um agendamento pelo aplicativo.\n\n"
                + summary()
                + "\n\nAguardo confirmação do horário.";
        try {
            String url = "https://wa.me/5521998521264?text=" + URLEncoder.encode(msg,"UTF-8");
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch(Exception e) {
            Toast.makeText(this,"Não foi possível abrir o WhatsApp.",Toast.LENGTH_LONG).show();
        }
    }

    private void saveHistory(){
        try {
            SharedPreferences p = getSharedPreferences("cellcerto",MODE_PRIVATE);
            JSONArray arr = new JSONArray(p.getString("history","[]"));
            a.put("createdAt",System.currentTimeMillis());
            arr.put(a);
            p.edit().putString("history",arr.toString()).apply();
        } catch(Exception ignored){}
    }
}
