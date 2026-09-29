package br.com.cellcertobox221.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final int TEAL = Color.parseColor("#079FB3");
    private final int TEAL_DARK = Color.parseColor("#087F92");
    private final int NAVY = Color.parseColor("#10242D");
    private final int MUTED = Color.parseColor("#65747B");
    private final int SOFT = Color.parseColor("#F2F7F9");
    private final int LINE = Color.parseColor("#D8E4E8");
    private final int LIGHT_TEAL = Color.parseColor("#E7F7FA");

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        build();
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density + .5f);
    }

    private GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private GradientDrawable stroke(int color, int strokeColor, int radius) {
        GradientDrawable d = bg(color, radius);
        d.setStroke(dp(1), strokeColor);
        return d;
    }

    private TextView text(String value, int sp, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(sp);
        t.setTextColor(color);
        t.setFontFeatureSettings("kern");
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private void build() {
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().setNavigationBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setBackgroundColor(Color.WHITE);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setClipToPadding(false);
        scroll.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(10), dp(16), dp(24));
        root.setBackgroundColor(Color.WHITE);
        scroll.addView(root, new ScrollView.LayoutParams(-1, -2));
        page.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1f));

        addHeader(root);
        addHero(root);
        addServices(root);
        addDiagnostic(root);
        addTracking(root);
        addQuickActions(root);
        addTrustStrip(root);
        addHours(root);
        addBottomNavigation(page);

        setContentView(page);
    }

    private void addHeader(LinearLayout root) {
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(0, 0, 0, dp(8));

        BrandView brand = new BrandView(this);
        header.addView(brand, new LinearLayout.LayoutParams(0, dp(78), 1f));

        TextView bell = text("🔔", 22, NAVY, false);
        bell.setGravity(Gravity.CENTER);
        bell.setBackground(stroke(Color.WHITE, LINE, 18));
        bell.setContentDescription("Notificações");
        bell.setOnClickListener(v -> open(NotificationCenterActivity.class));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(dp(54), dp(54));
        bp.setMargins(dp(10), 0, 0, 0);
        header.addView(bell, bp);
        root.addView(header);
    }

    private void addHero(LinearLayout root) {
        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setPadding(dp(18), dp(18), dp(18), dp(18));
        hero.setBackground(stroke(Color.WHITE, LINE, 24));

        TextView eyebrow = text("ASSISTÊNCIA TÉCNICA ESPECIALIZADA", 11, TEAL_DARK, true);
        eyebrow.setLetterSpacing(.08f);
        hero.addView(eyebrow);

        TextView title = text("Seu aparelho em boas mãos", 28, NAVY, true);
        title.setPadding(0, dp(5), 0, 0);
        hero.addView(title);

        TextView subtitle = text("Agende, acompanhe o reparo e consulte tudo pelo aplicativo CellCerto.", 15, MUTED, false);
        subtitle.setLineSpacing(0, 1.12f);
        subtitle.setPadding(0, dp(7), 0, dp(15));
        hero.addView(subtitle);

        TextView book = actionButton("▣   Agendar atendimento", TEAL, Color.WHITE);
        book.setOnClickListener(v -> open(BookingActivity.class));
        hero.addView(book, new LinearLayout.LayoutParams(-1, dp(58)));

        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, -2);
        hp.setMargins(0, dp(6), 0, dp(17));
        root.addView(hero, hp);
    }

    private void addServices(LinearLayout root) {
        TextView title = text("O que podemos cuidar para você?", 19, NAVY, true);
        title.setPadding(dp(2), 0, 0, dp(8));
        root.addView(title);

        LinearLayout row1 = new LinearLayout(this);
        row1.setWeightSum(3f);
        addServiceCard(row1, "📱", "Celulares");
        addServiceCard(row1, "💻", "Notebooks");
        addServiceCard(row1, "▰", "Tablets");
        root.addView(row1, new LinearLayout.LayoutParams(-1, dp(116)));

        LinearLayout row2 = new LinearLayout(this);
        row2.setWeightSum(3f);
        addServiceCard(row2, "◉", "iPhone / iPad");
        addServiceCard(row2, "♟", "Impressão 3D");
        addServiceCard(row2, "🎧", "Outros");
        root.addView(row2, new LinearLayout.LayoutParams(-1, dp(116)));
    }

    private void addServiceCard(LinearLayout row, String icon, String label) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(5), dp(10), dp(5), dp(10));
        card.setBackground(stroke(SOFT, LINE, 17));
        card.setOnClickListener(v -> open(BookingActivity.class));

        TextView ic = text(icon, 29, NAVY, false);
        ic.setGravity(Gravity.CENTER);
        TextView tx = text(label, 13, NAVY, true);
        tx.setGravity(Gravity.CENTER);
        tx.setMaxLines(2);

        card.addView(ic, new LinearLayout.LayoutParams(-1, 0, 1f));
        card.addView(tx, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, -1, 1f);
        cp.setMargins(dp(4), dp(4), dp(4), dp(4));
        row.addView(card, cp);
    }

    private void addDiagnostic(LinearLayout root) {
        LinearLayout card = new LinearLayout(this);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(14), dp(13), dp(13), dp(13));
        card.setBackground(stroke(LIGHT_TEAL, Color.parseColor("#C7EAF0"), 18));
        card.setOnClickListener(v -> open(BookingActivity.class));

        TextView icon = text("⚙", 25, Color.WHITE, true);
        icon.setGravity(Gravity.CENTER);
        icon.setBackground(bg(TEAL, 50));
        card.addView(icon, new LinearLayout.LayoutParams(dp(50), dp(50)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(12), 0, dp(6), 0);
        copy.addView(text("Diagnóstico completo", 16, NAVY, true));
        copy.addView(text("Com transparência e orçamento sem compromisso.", 13, MUTED, false));
        card.addView(copy, new LinearLayout.LayoutParams(0, -2, 1f));

        TextView arrow = text("›", 30, TEAL_DARK, true);
        arrow.setGravity(Gravity.CENTER);
        card.addView(arrow, new LinearLayout.LayoutParams(dp(28), dp(50)));

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(0, dp(11), 0, dp(16));
        root.addView(card, p);
    }

    private void addTracking(LinearLayout root) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(17), dp(16), dp(17), dp(16));
        card.setBackground(bg(NAVY, 22));
        card.setOnClickListener(v -> open(TrackingActivity.class));

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView left = text("Acompanhamento", 19, Color.WHITE, true);
        top.addView(left, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView status = text("EM ANÁLISE", 11, Color.WHITE, true);
        status.setGravity(Gravity.CENTER);
        status.setPadding(dp(10), dp(6), dp(10), dp(6));
        status.setBackground(bg(TEAL, 50));
        top.addView(status);
        card.addView(top);

        TextView device = text("iPhone 14 Pro  •  Protocolo #CC45821", 13, Color.parseColor("#C9D7DC"), false);
        device.setPadding(0, dp(5), 0, dp(13));
        card.addView(device);

        LinearLayout steps = new LinearLayout(this);
        steps.setGravity(Gravity.CENTER_VERTICAL);
        addStep(steps, "✓", "Recebido", true);
        addConnector(steps, true);
        addStep(steps, "1", "Em análise", true);
        addConnector(steps, false);
        addStep(steps, "○", "Peças", false);
        addConnector(steps, false);
        addStep(steps, "○", "Pronto", false);
        card.addView(steps, new LinearLayout.LayoutParams(-1, dp(61)));

        TextView current = text("Nosso técnico está avaliando o aparelho e identificando o problema.", 13, Color.parseColor("#D5E2E6"), false);
        current.setPadding(dp(1), dp(8), 0, dp(3));
        card.addView(current);

        TextView more = text("Abrir acompanhamento   ›", 14, Color.parseColor("#56D5E5"), true);
        more.setPadding(0, dp(10), 0, 0);
        card.addView(more);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(0, 0, 0, dp(14));
        root.addView(card, p);
    }

    private void addStep(LinearLayout row, String icon, String label, boolean active) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        TextView dot = text(icon, 14, Color.WHITE, true);
        dot.setGravity(Gravity.CENTER);
        dot.setBackground(bg(active ? TEAL : Color.parseColor("#52646C"), 50));
        box.addView(dot, new LinearLayout.LayoutParams(dp(28), dp(28)));
        TextView tx = text(label, 10, active ? Color.WHITE : Color.parseColor("#AFC0C6"), active);
        tx.setGravity(Gravity.CENTER);
        tx.setPadding(0, dp(3), 0, 0);
        box.addView(tx);
        row.addView(box, new LinearLayout.LayoutParams(0, -1, 1f));
    }

    private void addConnector(LinearLayout row, boolean active) {
        View line = new View(this);
        line.setBackgroundColor(active ? TEAL : Color.parseColor("#52646C"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(18), dp(2));
        lp.setMargins(-dp(8), -dp(17), -dp(8), 0);
        row.addView(line, lp);
    }

    private void addQuickActions(LinearLayout root) {
        LinearLayout row = new LinearLayout(this);
        row.setWeightSum(2f);
        addQuickCard(row, "📍", "Localização\ne horários", InfoActivity.class, "contact");
        addQuickCard(row, "💳", "Pagamentos\ne parcelas", PaymentSimulatorActivity.class, null);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, dp(126));
        rp.setMargins(0, 0, 0, dp(8));
        root.addView(row, rp);

        LinearLayout row2 = new LinearLayout(this);
        row2.setWeightSum(2f);
        addQuickCard(row2, "🧾", "Histórico e\ngarantia", HistoryActivity.class, null);
        addQuickCard(row2, "🔔", "Notificações\ne lembretes", NotificationCenterActivity.class, null);
        LinearLayout.LayoutParams rp2 = new LinearLayout.LayoutParams(-1, dp(126));
        rp2.setMargins(0, 0, 0, dp(15));
        root.addView(row2, rp2);
    }

    private void addQuickCard(LinearLayout row, String icon, String label, Class<?> cls, String mode) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(12), dp(13), dp(12), dp(13));
        card.setBackground(stroke(Color.WHITE, LINE, 19));

        TextView ic = text(icon, 25, NAVY, false);
        ic.setGravity(Gravity.CENTER);
        TextView tx = text(label, 16, NAVY, true);
        tx.setGravity(Gravity.CENTER);
        tx.setPadding(0, dp(5), 0, 0);
        card.addView(ic);
        card.addView(tx);
        card.setOnClickListener(v -> {
            Intent i = new Intent(this, cls);
            if (mode != null) i.putExtra("mode", mode);
            startActivity(i);
        });

        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, -1, 1f);
        cp.setMargins(dp(4), dp(4), dp(4), dp(4));
        row.addView(card, cp);
    }

    private void addTrustStrip(LinearLayout root) {
        LinearLayout strip = new LinearLayout(this);
        strip.setWeightSum(4f);
        strip.setPadding(dp(4), dp(10), dp(4), dp(10));
        strip.setBackground(bg(SOFT, 18));
        trustItem(strip, "⚡", "Atendimento\nrápido");
        trustItem(strip, "🛡", "Serviço com\ngarantia");
        trustItem(strip, "⚙", "Peças de\nqualidade");
        trustItem(strip, "👥", "Suporte\nespecializado");
        root.addView(strip, new LinearLayout.LayoutParams(-1, dp(94)));
    }

    private void trustItem(LinearLayout row, String icon, String label) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        TextView ic = text(icon, 21, TEAL_DARK, false);
        ic.setGravity(Gravity.CENTER);
        TextView tx = text(label, 10, NAVY, true);
        tx.setGravity(Gravity.CENTER);
        tx.setPadding(0, dp(3), 0, 0);
        item.addView(ic);
        item.addView(tx);
        row.addView(item, new LinearLayout.LayoutParams(0, -1, 1f));
    }

    private void addHours(LinearLayout root) {
        TextView hours = text("Atendimento CellCerto\nSegunda a sexta: 10h às 19h  •  Sábado: 9h às 16h\nAtendimento por agendamento.", 13, MUTED, false);
        hours.setGravity(Gravity.CENTER);
        hours.setLineSpacing(0, 1.25f);
        hours.setPadding(dp(8), dp(15), dp(8), dp(4));
        root.addView(hours);
    }

    private void addBottomNavigation(LinearLayout page) {
        LinearLayout nav = new LinearLayout(this);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(4), dp(5), dp(4), dp(5));
        nav.setBackground(stroke(Color.WHITE, LINE, 0));
        addNav(nav, "⌂", "Início", null);
        addNav(nav, "▦", "Serviços", InfoActivity.class);
        addNav(nav, "▣", "Orçamentos", PaymentSimulatorActivity.class);
        addNav(nav, "🔔", "Notificações", NotificationCenterActivity.class);
        addNav(nav, "☰", "Mais", SettingsActivity.class);
        page.addView(nav, new LinearLayout.LayoutParams(-1, dp(70)));
    }

    private void addNav(LinearLayout nav, String icon, String label, Class<?> cls) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        boolean active = cls == null;
        TextView ic = text(icon, 19, active ? TEAL : NAVY, false);
        ic.setGravity(Gravity.CENTER);
        TextView tx = text(label, 10, active ? TEAL : NAVY, active);
        tx.setGravity(Gravity.CENTER);
        item.addView(ic);
        item.addView(tx);
        if (cls != null) {
            item.setOnClickListener(v -> {
                Intent i = new Intent(this, cls);
                if (cls == InfoActivity.class) i.putExtra("mode", "services");
                startActivity(i);
            });
        }
        nav.addView(item, new LinearLayout.LayoutParams(0, -1, 1f));
    }

    private TextView actionButton(String label, int color, int textColor) {
        TextView b = text(label, 17, textColor, true);
        b.setGravity(Gravity.CENTER);
        b.setBackground(bg(color, 18));
        b.setPadding(dp(12), 0, dp(12), 0);
        return b;
    }

    private void open(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }
}
