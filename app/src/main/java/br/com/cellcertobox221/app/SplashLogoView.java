package br.com.cellcertobox221.app;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

public class SplashLogoView extends View {
    private final Paint teal = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint whiteText = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint darkText = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint grayText = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint shadow = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float plateProgress = 0f;
    private float logoAlpha = 0f;
    private float subAlpha = 0f;
    private float scale = 0.92f;

    public SplashLogoView(Context c) {
        super(c);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setBackground(new ColorDrawable(Color.rgb(242,242,242)));

        teal.setColor(Color.rgb(10,150,170));
        teal.setStyle(Paint.Style.FILL);
        teal.setShadowLayer(16f, 0f, 10f, 0x35000000);

        whiteText.setColor(Color.WHITE);
        whiteText.setTypeface(Typeface.create("sans", Typeface.BOLD_ITALIC));
        whiteText.setTextAlign(Paint.Align.LEFT);

        darkText.setColor(Color.rgb(35,35,35));
        darkText.setTypeface(Typeface.create("sans", Typeface.BOLD_ITALIC));
        darkText.setTextAlign(Paint.Align.LEFT);

        grayText.setColor(Color.rgb(92,92,92));
        grayText.setTypeface(Typeface.create("sans", Typeface.NORMAL));
        grayText.setTextAlign(Paint.Align.CENTER);

        shadow.setColor(0x18000000);
    }

    public void setPlateProgress(float v){ plateProgress=v; invalidate(); }
    public void setLogoAlpha(float v){ logoAlpha=v; invalidate(); }
    public void setSubAlpha(float v){ subAlpha=v; invalidate(); }
    public void setLogoScale(float v){ scale=v; invalidate(); }

    @Override protected void onDraw(Canvas c) {
        super.onDraw(c);
        float w=getWidth(), h=getHeight();
        float cx=w/2f, cy=h/2f - h*0.04f;

        c.save();
        c.scale(scale, scale, cx, cy);

        float targetW = Math.min(w*0.72f, 820f);
        float targetH = targetW*0.24f;
        float pw = targetW * plateProgress;
        float ph = targetH;
        float left = cx - pw/2f;
        float top = cy - ph/2f;
        float right = cx + pw/2f;
        float bottom = cy + ph/2f;

        if (plateProgress > 0.01f) {
            Path p = new Path();
            float cut = ph*0.18f;
            float r = ph*0.28f;
            p.moveTo(left + cut, top);
            p.lineTo(right - cut*0.15f, top);
            p.quadTo(right, top, right - cut*0.12f, top + cut*1.15f);
            p.lineTo(right - cut, bottom - r*0.35f);
            p.quadTo(right - cut*1.2f, bottom, right - cut*2.2f, bottom);
            p.lineTo(left, bottom);
            p.lineTo(left + cut*0.35f, top + r);
            p.quadTo(left + cut*0.5f, top, left + cut, top);
            p.close();
            c.drawPath(p, teal);
        }

        if (logoAlpha > 0.01f) {
            whiteText.setAlpha((int)(255*logoAlpha));
            darkText.setAlpha((int)(255*logoAlpha));
            float fs = ph*0.62f;
            whiteText.setTextSize(fs);
            darkText.setTextSize(fs);

            String a="cell";
            String b="certo";
            float aw=whiteText.measureText(a);
            float bw=darkText.measureText(b);
            float total=aw+bw;
            float tx=cx-total/2f;
            float baseline=cy + fs*0.30f;
            c.drawText(a, tx, baseline, whiteText);
            c.drawText(b, tx+aw, baseline, darkText);

            Paint box = new Paint(darkText);
            box.setTypeface(Typeface.create("sans", Typeface.NORMAL));
            box.setTextSize(fs*0.24f);
            box.setAlpha((int)(255*logoAlpha));
            c.drawText("box 221", tx+aw+fs*0.08f, cy-fs*0.22f, box);
        }

        if (subAlpha > 0.01f) {
            grayText.setAlpha((int)(255*subAlpha));
            grayText.setTextSize(Math.max(22f, targetW*0.038f));
            c.drawText("assistência técnica de celulares", cx, cy + targetH*1.15f, grayText);
        }

        c.restore();
    }
}
