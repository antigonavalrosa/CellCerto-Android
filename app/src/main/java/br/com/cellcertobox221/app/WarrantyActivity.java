package br.com.cellcertobox221.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import org.json.*;

public class WarrantyActivity extends Activity {
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
        root.addView(Ui.navTitle(this,"Histórico e garantia digital"));
        root.addView(Ui.muted(this,"Seus pedidos enviados por este aparelho aparecem aqui. Garantias emitidas pela loja poderão ser vinculadas ao protocolo.",14));

        try{
            JSONArray arr=new JSONArray(getSharedPreferences("cellcerto",MODE_PRIVATE).getString("history","[]"));
            if(arr.length()==0){
                LinearLayout c=Ui.section(this);
                c.addView(Ui.muted(this,"Nenhum serviço registrado neste aparelho ainda.",15));
                root.addView(c);
            }
            for(int i=arr.length()-1;i>=0;i--){
                JSONObject j=arr.getJSONObject(i);
                LinearLayout c=Ui.section(this);
                c.addView(Ui.title(this,j.optString("marca")+" "+j.optString("modelo"),17));
                c.addView(Ui.muted(this,j.optString("servico")+"\n"+j.optString("data")+" às "+j.optString("horario")+"\nGarantia digital: aguardando emissão/validação da loja",13));
                root.addView(c);
            }
        }catch(Exception ignored){}
        setContentView(sv);
    }
}
