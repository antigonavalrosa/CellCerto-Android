package br.com.cellcertobox221.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;

public class SettingsActivity extends Activity {
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(ThemeUtil.bg(this));
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,16),Ui.dp(this,18),Ui.dp(this,28));
        root.setBackgroundColor(ThemeUtil.bg(this));
        root.addView(Ui.back(this));
        root.addView(Ui.navTitle(this,"Configurações"));
        root.addView(Ui.muted(this,"Personalize a aparência do aplicativo.",14));

        Switch dark=new Switch(this);
        dark.setText("Modo escuro");
        dark.setTextColor(ThemeUtil.text(this));
        dark.setTextSize(16);
        dark.setPadding(0,22,0,12);
        dark.setChecked(ThemeUtil.isDark(this));
        root.addView(dark);
        dark.setOnCheckedChangeListener((b1,c)->{
            ThemeUtil.setDark(this,c);
            recreate();
        });

        LinearLayout c=Ui.section(this);
        c.addView(Ui.muted(this,"Modo claro: cinzento, branco, azul-turquesa e antracite.\nModo escuro: fundo antracite com azul-turquesa em destaque.",13));
        root.addView(c);
        setContentView(root);
    }
}
