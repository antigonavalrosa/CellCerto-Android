package br.com.cellcertobox221.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import java.text.NumberFormat;
import java.util.Locale;

public class PaymentSimulatorActivity extends Activity {
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
        root.addView(Ui.navTitle(this,"Simulador de pagamento"));
        root.addView(Ui.muted(this,"Faça uma simulação matemática do valor. Taxas, descontos e condições reais devem ser confirmados com a CellCerto.",14));

        EditText value=new EditText(this);
        value.setHint("Valor do serviço (ex.: 450,00)");
        value.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        value.setTextColor(ThemeUtil.text(this));
        value.setHintTextColor(ThemeUtil.muted(this));
        value.setBackground(Ui.strokeBg(ThemeUtil.hex(ThemeUtil.surface(this)),"#A8B2B7",Ui.dp(this,14),1,this));
        value.setPadding(16,12,16,12);
        Ui.margin(value,0,16,0,8,this);
        root.addView(value);

        Spinner parts=new Spinner(this);
        String[] opts={"1x","2x","3x","4x","5x","6x","10x","12x"};
        parts.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,opts));
        root.addView(parts,new LinearLayout.LayoutParams(-1,Ui.dp(this,54)));

        Button calc=Ui.primaryButton(this,"Simular parcelas");
        Ui.margin(calc,0,12,0,8,this);
        root.addView(calc);
        TextView result=Ui.title(this,"",22);
        result.setPadding(0,14,0,8);
        root.addView(result);
        root.addView(Ui.muted(this,"Formas aceitas para consulta: Pix, débito, crédito e dinheiro. Confirme disponibilidade e eventuais taxas antes do pagamento.",13));

        calc.setOnClickListener(v->{
            try{
                double total=Double.parseDouble(value.getText().toString().replace(",","."));
                int n=Integer.parseInt(parts.getSelectedItem().toString().replace("x",""));
                NumberFormat nf=NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
                result.setText(n+"x de "+nf.format(total/n)+"\nTotal: "+nf.format(total));
            }catch(Exception e){
                Toast.makeText(this,"Digite um valor válido.",Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(sv);
    }
}
