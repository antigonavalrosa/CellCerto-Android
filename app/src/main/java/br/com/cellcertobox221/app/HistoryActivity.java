package br.com.cellcertobox221.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import org.json.*;

public class HistoryActivity extends Activity {
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.parseColor("#071926"));

        ScrollView sv = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,18),Ui.dp(this,18),Ui.dp(this,30));
        root.setBackgroundColor(Color.parseColor("#F4FAFC"));
        sv.addView(root);

        TextView back = Ui.text(this,"‹ Voltar",16,"#027D98");
        back.setOnClickListener(v->finish());
        root.addView(back);

        TextView title = Ui.text(this,"Meus agendamentos",27,"#10212B");
        title.setTypeface(null,1);
        title.setPadding(0,12,0,12);
        root.addView(title);

        try {
            JSONArray arr = new JSONArray(
                    getSharedPreferences("cellcerto",MODE_PRIVATE).getString("history","[]"));

            if(arr.length()==0){
                TextView empty = Ui.text(this,
                        "Você ainda não enviou nenhum agendamento por este aparelho.",
                        15,"#506873");
                root.addView(empty);
            }

            for(int i=arr.length()-1;i>=0;i--){
                JSONObject j = arr.getJSONObject(i);

                LinearLayout c = new LinearLayout(this);
                c.setOrientation(LinearLayout.VERTICAL);
                c.setPadding(Ui.dp(this,18),Ui.dp(this,16),Ui.dp(this,18),Ui.dp(this,16));
                c.setBackground(Ui.strokeBg("#FFFFFF","#D3E1E6",Ui.dp(this,16),1,this));
                Ui.margin(c,0,6,0,8,this);

                TextView h = Ui.text(this,
                        j.optString("data")+" • "+j.optString("horario"),
                        17,"#0B8099");
                h.setTypeface(null,1);
                c.addView(h);

                c.addView(Ui.text(this,
                        j.optString("marca")+" "+j.optString("modelo")
                                +"\n"+j.optString("servico")
                                +"\nStatus: aguardando confirmação pelo WhatsApp",
                        14,"#263C45"));

                root.addView(c);
            }
        } catch(Exception e){
            root.addView(Ui.text(this,
                    "Não foi possível carregar o histórico.",
                    14,"#506873"));
        }

        setContentView(sv);
    }
}
