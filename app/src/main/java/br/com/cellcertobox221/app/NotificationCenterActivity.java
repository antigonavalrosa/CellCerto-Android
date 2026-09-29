package br.com.cellcertobox221.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;

public class NotificationCenterActivity extends Activity {
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(ThemeUtil.bg(this));
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,16),Ui.dp(this,18),Ui.dp(this,28));
        root.setBackgroundColor(ThemeUtil.bg(this));
        root.addView(Ui.back(this));
        root.addView(Ui.navTitle(this,"Notificações"));
        root.addView(Ui.muted(this,"Controle lembretes e avisos do aplicativo.",14));

        Switch reminders=new Switch(this);
        reminders.setText("Lembretes de agendamento");
        reminders.setTextColor(ThemeUtil.text(this));
        reminders.setChecked(getSharedPreferences("cellcerto",MODE_PRIVATE).getBoolean("reminders",true));
        reminders.setPadding(0,22,0,12);
        root.addView(reminders);
        reminders.setOnCheckedChangeListener((b1,c)->getSharedPreferences("cellcerto",MODE_PRIVATE).edit().putBoolean("reminders",c).apply());

        Switch status=new Switch(this);
        status.setText("Atualizações do status do aparelho");
        status.setTextColor(ThemeUtil.text(this));
        status.setChecked(true);
        root.addView(status);
        status.setOnCheckedChangeListener((b1,c)->{
            if(c) Toast.makeText(this,"A notificação em tempo real será ativada quando o painel online da CellCerto estiver conectado.",Toast.LENGTH_LONG).show();
        });

        LinearLayout info=Ui.section(this);
        info.addView(Ui.muted(this,"A área de notificações já está pronta. Alertas automáticos de mudança de status exigem um servidor/Firebase conectado ao painel da loja.",13));
        root.addView(info);
        setContentView(root);
    }
}
