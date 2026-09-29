package br.com.cellcertobox221.app;

import android.content.Context;
import android.graphics.*;
import android.view.View;

public class BrandView extends View {
    private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
    public BrandView(Context c){ super(c); }
    @Override protected void onDraw(Canvas c){
        super.onDraw(c);
        float w=getWidth(),h=getHeight();
        p.setColor(Color.parseColor("#C9CBCE"));
        p.setStyle(Paint.Style.FILL);
        c.drawRoundRect(0,0,w,h,h*.22f,h*.22f,p);

        float boxW=w*.23f;
        p.setColor(Color.parseColor("#2A2A2A"));
        p.setTypeface(Typeface.create("sans",Typeface.BOLD_ITALIC));
        p.setTextSize(h*.23f);
        p.setTextAlign(Paint.Align.CENTER);
        c.drawText("box",boxW*.46f,h*.27f,p);

        p.setColor(Color.parseColor("#0796AA"));
        p.setTextSize(h*.30f);
        c.drawText("221",boxW*.47f,h*.62f,p);

        float left=boxW;
        p.setColor(Color.parseColor("#0796AA"));
        c.drawRoundRect(left,h*.03f,w-h*.03f,h*.64f,h*.22f,h*.22f,p);

        p.setTextAlign(Paint.Align.LEFT);
        p.setTypeface(Typeface.create("sans",Typeface.BOLD_ITALIC));
        p.setTextSize(h*.46f);
        String cell="cell", certo="certo";
        float total=p.measureText(cell)+p.measureText(certo);
        float tx=left+(w-left-total)*.5f;
        p.setColor(Color.WHITE);
        c.drawText(cell,tx,h*.51f,p);
        float aw=p.measureText(cell);
        p.setColor(Color.parseColor("#292929"));
        c.drawText(certo,tx+aw,h*.51f,p);

        p.setTextAlign(Paint.Align.CENTER);
        p.setTypeface(Typeface.create("sans",Typeface.NORMAL));
        p.setTextSize(h*.095f);
        p.setColor(Color.parseColor("#303030"));
        c.drawText("assistência técnica de celulares",w*.58f,h*.86f,p);
    }
}
