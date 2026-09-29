package br.com.cellcertobox221.app;

import android.app.Activity;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import org.json.*;
import java.net.URLEncoder;

public class SummaryActivity extends Activity {
    private JSONObject a;

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(ThemeUtil.bg(this));
        try{ a=new JSONObject(getIntent().getStringExtra("appointment")); }
        catch(Exception e){ finish(); return; }

        ScrollView sv=new ScrollView(this);
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,16),Ui.dp(this,18),Ui.dp(this,28));
        root.setBackgroundColor(ThemeUtil.bg(this));
        sv.addView(root);

        root.addView(Ui.back(this));
        root.addView(Ui.navTitle(this,"Resumo do agendamento"));

        LinearLayout card=Ui.section(this);
        card.addView(Ui.muted(this,summary(),15));
        root.addView(card);

        Button send=Ui.primaryButton(this,"Confirmar e enviar pelo WhatsApp");
        send.setOnClickListener(v->confirm());
        root.addView(send);
        setContentView(sv);
    }

    private String v(String k){ return a.optString(k,"-"); }

    private String summary(){
        return "Cliente: "+v("nome")
                +"\nWhatsApp: "+v("whatsapp")
                +"\n\nAparelho: "+v("tipo")+" • "+v("marca")+" • "+v("modelo")
                +"\nProblema: "+v("problema")
                +"\nServiço: "+v("servico")
                +"\nPeça/tela: "+v("tela")
                +"\n\nData: "+v("data")+" às "+v("horario")
                +"\nPagamento: "+v("pagamento")
                +(v("obs").isEmpty()?"":"\n\nObservações: "+v("obs"));
    }

    private void confirm(){
        save();
        String msg="Olá, CellCerto! Gostaria de solicitar um agendamento pelo aplicativo.\n\n"+summary()+"\n\nAguardo confirmação do horário.";
        try{
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://wa.me/5521998521264?text="+URLEncoder.encode(msg,"UTF-8"))));
        }catch(Exception e){
            Toast.makeText(this,"Não foi possível abrir o WhatsApp.",Toast.LENGTH_LONG).show();
        }
    }

    private void save(){
        try{
            SharedPreferences p=getSharedPreferences("cellcerto",MODE_PRIVATE);
            JSONArray arr=new JSONArray(p.getString("history","[]"));
            a.put("createdAt",System.currentTimeMillis());
            arr.put(a);
            p.edit().putString("history",arr.toString()).apply();
        }catch(Exception ignored){}
    }
}
