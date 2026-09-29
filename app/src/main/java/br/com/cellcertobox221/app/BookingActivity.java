package br.com.cellcertobox221.app;

import android.app.*;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookingActivity extends Activity {
    private Spinner tipo, marca, problema, servico, tela, horario, pagamento;
    private EditText modelo, nome, whatsapp, obs;
    private Button dataBtn;
    private String dataISO = "";
    private final Calendar selected = Calendar.getInstance();

    private static final String[] TIPOS = {"Celular", "Tablet", "Notebook", "Computador", "Videogame", "Outro"};
    private static final String[] MARCAS = {"Apple", "Samsung", "Motorola", "Xiaomi", "Realme", "LG", "Asus", "Positivo", "Outro"};
    private static final String[] PROBLEMAS = {"Tela quebrada / sem imagem", "Não liga", "Bateria descarrega rápido", "Conector de carga", "Áudio / microfone", "Câmera", "Aparelho molhou", "Software / sistema", "Superaquecimento", "Outro"};
    private static final String[] SERVICOS = {"Diagnóstico", "Troca de tela", "Troca de bateria", "Reparo de conector", "Reparo de placa", "Atualização / software", "Limpeza técnica", "Outro / avaliar"};
    private static final String[] TELA = {"Quero orçamento com a peça/tela inclusa", "Já tenho a peça/tela", "Quero apenas diagnóstico", "Não se aplica"};
    private static final String[] PAGAMENTOS = {"Pix", "Cartão de débito", "Cartão de crédito", "Dinheiro", "Definir no atendimento"};

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.parseColor("#071926"));

        ScrollView sv = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Ui.dp(this,18),Ui.dp(this,18),Ui.dp(this,18),Ui.dp(this,30));
        root.setBackgroundColor(Color.parseColor("#F4FAFC"));
        sv.addView(root);

        TextView back = Ui.text(this,"‹ Voltar",16,"#027D98");
        back.setPadding(0,0,0,12);
        back.setOnClickListener(v->finish());
        root.addView(back);

        TextView title = Ui.text(this,"Agendar atendimento",28,"#10212B");
        title.setTypeface(null,1);
        root.addView(title);

        TextView sub = Ui.text(this,
                "Preencha os dados do aparelho e escolha um horário. No fim você envia a solicitação para confirmação.",
                14,"#506873");
        sub.setPadding(0,5,0,18);
        root.addView(sub);

        tipo = addSpinner(root,"Tipo de aparelho",TIPOS);
        marca = addSpinner(root,"Marca",MARCAS);
        modelo = addEdit(root,"Modelo do aparelho (ex.: Galaxy A54, iPhone 13)",false);
        problema = addSpinner(root,"Qual é o problema?",PROBLEMAS);
        servico = addSpinner(root,"Serviço desejado",SERVICOS);
        tela = addSpinner(root,"Sobre peça/tela",TELA);

        addLabel(root,"Data desejada");
        dataBtn = Ui.darkButton(this,"Escolher data");
        dataBtn.setTextColor(Color.WHITE);
        root.addView(dataBtn);
        dataBtn.setOnClickListener(v -> chooseDate());

        horario = addSpinner(root,"Horário desejado",new String[]{"Escolha primeiro a data"});
        pagamento = addSpinner(root,"Forma de pagamento pretendida",PAGAMENTOS);
        nome = addEdit(root,"Seu nome",false);
        whatsapp = addEdit(root,"Seu WhatsApp com DDD",true);
        obs = addEdit(root,"Observações (opcional)",false);
        obs.setMinLines(3);
        obs.setGravity(android.view.Gravity.TOP);

        TextView payNote = Ui.text(this,
                "A forma de pagamento selecionada é uma preferência e pode ser confirmada no atendimento.",
                12,"#657E89");
        payNote.setPadding(0,4,0,14);
        root.addView(payNote);

        Button continuar = Ui.primaryButton(this,"Revisar agendamento");
        continuar.setOnClickListener(v -> review());
        root.addView(continuar);

        setContentView(sv);
    }

    private void addLabel(LinearLayout root, String text) {
        TextView l = Ui.text(this,text,13,"#38505B");
        l.setTypeface(null,1);
        l.setPadding(0,12,0,6);
        root.addView(l);
    }

    private Spinner addSpinner(LinearLayout root, String label, String[] items) {
        addLabel(root,label);
        Spinner s = new Spinner(this);
        ArrayAdapter<String> a = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        s.setAdapter(a);
        s.setBackground(Ui.strokeBg("#FFFFFF","#C9D8DE",Ui.dp(this,12),1,this));
        s.setPadding(Ui.dp(this,12),Ui.dp(this,4),Ui.dp(this,8),Ui.dp(this,4));
        root.addView(s, new LinearLayout.LayoutParams(-1, Ui.dp(this,54)));
        return s;
    }

    private EditText addEdit(LinearLayout root, String hint, boolean phone) {
        addLabel(root,hint);
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(15);
        e.setTextColor(Color.parseColor("#10212B"));
        e.setHintTextColor(Color.parseColor("#83959E"));
        e.setBackground(Ui.strokeBg("#FFFFFF","#C9D8DE",Ui.dp(this,12),1,this));
        e.setPadding(Ui.dp(this,14),Ui.dp(this,11),Ui.dp(this,14),Ui.dp(this,11));
        if(phone) e.setInputType(InputType.TYPE_CLASS_PHONE);
        root.addView(e, new LinearLayout.LayoutParams(-1,-2));
        return e;
    }

    private void chooseDate() {
        Calendar min = Calendar.getInstance();
        DatePickerDialog d = new DatePickerDialog(this,(view,y,m,day)->{
            selected.set(y,m,day,12,0,0);
            int dow = selected.get(Calendar.DAY_OF_WEEK);
            if(dow==Calendar.SUNDAY){
                Toast.makeText(this,"A CellCerto não atende aos domingos.",Toast.LENGTH_LONG).show();
                return;
            }
            dataISO = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selected.getTime());
            dataBtn.setText(dataISO);
            loadTimes(dow);
        }, selected.get(Calendar.YEAR),selected.get(Calendar.MONTH),selected.get(Calendar.DAY_OF_MONTH));
        d.getDatePicker().setMinDate(min.getTimeInMillis());
        d.show();
    }

    private void loadTimes(int dow) {
        ArrayList<String> times = new ArrayList<>();
        int start = dow==Calendar.SATURDAY ? 9 : 10;
        int end = dow==Calendar.SATURDAY ? 16 : 19;
        for(int h=start;h<end;h++) {
            times.add(String.format(Locale.getDefault(),"%02d:00",h));
            times.add(String.format(Locale.getDefault(),"%02d:30",h));
        }
        ArrayAdapter<String> a = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,times);
        horario.setAdapter(a);
    }

    private void review() {
        if(dataISO.isEmpty()
                || modelo.getText().toString().trim().isEmpty()
                || nome.getText().toString().trim().isEmpty()
                || whatsapp.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,"Preencha modelo, data, nome e WhatsApp.",Toast.LENGTH_LONG).show();
            return;
        }

        try {
            JSONObject j = new JSONObject();
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

            Intent i = new Intent(this,SummaryActivity.class);
            i.putExtra("appointment",j.toString());
            startActivity(i);
        } catch(Exception e) {
            Toast.makeText(this,"Não foi possível montar o agendamento.",Toast.LENGTH_SHORT).show();
        }
    }
}
