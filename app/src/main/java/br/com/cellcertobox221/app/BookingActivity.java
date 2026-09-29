package br.com.cellcertobox221.app;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookingActivity extends Activity {
    private Spinner tipo,marca,problema,servico,tela,horario,pagamento;
    private EditText modelo,nome,whatsapp,obs;
    private Button dataBtn;
    private String dataISO="";
    private final Calendar selected=Calendar.getInstance();

    private static final String[] TIPOS={"Celular","Tablet","Notebook","Computador","Videogame","Outro"};
    private static final String[] MARCAS={"Apple","Samsung","Motorola","Xiaomi","Realme","LG","Asus","Positivo","Outro"};
    private static final String[] PROBLEMAS={"Tela quebrada / sem imagem","Não liga","Bateria descarrega rápido","Conector de carga","Áudio / microfone","Câmera","Aparelho molhou","Software / sistema","Superaquecimento","Outro"};
    private static final String[] SERVICOS={"Diagnóstico","Troca de tela","Troca de bateria","Reparo de conector","Reparo de placa","Atualização / software","Limpeza técnica","Outro / avaliar"};
    private static final String[] TELA={"Quero orçamento com a peça/tela inclusa","Já tenho a peça/tela","Quero apenas diagnóstico","Não se aplica"};
    private static final String[] PAGAMENTOS={"Pix","Cartão de débito","Cartão de crédito","Dinheiro","Definir no atendimento"};

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(ThemeUtil.bg(this));
        ScrollView sv=new ScrollView(this);
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,16),Ui.dp(this,18),Ui.dp(this,30));
        root.setBackgroundColor(ThemeUtil.bg(this));
        sv.addView(root);

        root.addView(Ui.back(this));
        root.addView(Ui.navTitle(this,"Agendar atendimento"));
        root.addView(Ui.muted(this,"Informe seu aparelho, o problema e o melhor horário. A CellCerto confirma pelo WhatsApp.",14));

        tipo=sp(root,"Tipo de aparelho",TIPOS);
        marca=sp(root,"Marca",MARCAS);
        modelo=ed(root,"Modelo do aparelho",false);
        problema=sp(root,"Qual é o problema?",PROBLEMAS);
        servico=sp(root,"Serviço desejado",SERVICOS);
        tela=sp(root,"Tela/peça",TELA);

        label(root,"Data desejada");
        dataBtn=Ui.darkButton(this,"Escolher data");
        root.addView(dataBtn);
        dataBtn.setOnClickListener(v->date());

        horario=sp(root,"Horário desejado",new String[]{"Escolha primeiro a data"});
        pagamento=sp(root,"Forma de pagamento",PAGAMENTOS);
        nome=ed(root,"Seu nome",false);
        whatsapp=ed(root,"Seu WhatsApp com DDD",true);
        obs=ed(root,"Observações (opcional)",false);
        obs.setMinLines(3);

        Button next=Ui.primaryButton(this,"Revisar agendamento");
        Ui.margin(next,0,16,0,0,this);
        next.setOnClickListener(v->review());
        root.addView(next);
        setContentView(sv);
    }

    private void label(LinearLayout r,String s){
        TextView l=Ui.muted(this,s,13);
        l.setPadding(0,12,0,6);
        r.addView(l);
    }

    private Spinner sp(LinearLayout r,String l,String[] items){
        label(r,l);
        Spinner s=new Spinner(this);
        s.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));
        s.setBackground(Ui.strokeBg(ThemeUtil.hex(ThemeUtil.surface(this)),"#A8B2B7",Ui.dp(this,12),1,this));
        r.addView(s,new LinearLayout.LayoutParams(-1,Ui.dp(this,54)));
        return s;
    }

    private EditText ed(LinearLayout r,String h,boolean phone){
        label(r,h);
        EditText e=new EditText(this);
        e.setHint(h);
        e.setTextColor(ThemeUtil.text(this));
        e.setHintTextColor(ThemeUtil.muted(this));
        e.setBackground(Ui.strokeBg(ThemeUtil.hex(ThemeUtil.surface(this)),"#A8B2B7",Ui.dp(this,12),1,this));
        e.setPadding(14,11,14,11);
        if(phone)e.setInputType(InputType.TYPE_CLASS_PHONE);
        r.addView(e,new LinearLayout.LayoutParams(-1,-2));
        return e;
    }

    private void date(){
        DatePickerDialog d=new DatePickerDialog(this,(v,y,m,day)->{
            selected.set(y,m,day,12,0,0);
            int dow=selected.get(Calendar.DAY_OF_WEEK);
            if(dow==Calendar.SUNDAY){
                Toast.makeText(this,"A CellCerto não atende aos domingos.",Toast.LENGTH_LONG).show();
                return;
            }
            dataISO=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(selected.getTime());
            dataBtn.setText(dataISO);
            times(dow);
        },selected.get(Calendar.YEAR),selected.get(Calendar.MONTH),selected.get(Calendar.DAY_OF_MONTH));
        d.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        d.show();
    }

    private void times(int dow){
        ArrayList<String> list=new ArrayList<>();
        int start=dow==Calendar.SATURDAY?9:10;
        int end=dow==Calendar.SATURDAY?16:19;
        for(int h=start;h<end;h++){
            list.add(String.format(Locale.getDefault(),"%02d:00",h));
            list.add(String.format(Locale.getDefault(),"%02d:30",h));
        }
        horario.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list));
    }

    private void review(){
        if(dataISO.isEmpty()||modelo.getText().toString().trim().isEmpty()||nome.getText().toString().trim().isEmpty()||whatsapp.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Preencha modelo, data, nome e WhatsApp.",Toast.LENGTH_LONG).show();
            return;
        }
        try{
            JSONObject j=new JSONObject();
            j.put("tipo",tipo.getSelectedItem());
            j.put("marca",marca.getSelectedItem());
            j.put("modelo",modelo.getText().toString().trim());
            j.put("problema",problema.getSelectedItem());
            j.put("servico",servico.getSelectedItem());
            j.put("tela",tela.getSelectedItem());
            j.put("data",dataISO);
            j.put("horario",horario.getSelectedItem());
            j.put("pagamento",pagamento.getSelectedItem());
            j.put("nome",nome.getText().toString().trim());
            j.put("whatsapp",whatsapp.getText().toString().trim());
            j.put("obs",obs.getText().toString().trim());
            Intent i=new Intent(this,SummaryActivity.class);
            i.putExtra("appointment",j.toString());
            startActivity(i);
        }catch(Exception ignored){}
    }
}
