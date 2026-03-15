package com.burgaynet.expenziox.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BarChartView extends View {

    public static class Bar {
        public String label;
        public float value;
        public int color;
        public Bar(String label, float value, int color) {
            this.label = label; this.value = value; this.color = color;
        }
    }

    private List<Bar> bars = new ArrayList<>();
    private final Paint barPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BarChartView(Context context) { super(context); init(); }
    public BarChartView(Context context, AttributeSet attrs) { super(context, attrs); init(); }
    public BarChartView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); init(); }

    private void init() {
        textPaint.setColor(0xFF7B90A8);
        textPaint.setTextAlign(Paint.Align.CENTER);
        linePaint.setColor(0xFF1E3050);
        linePaint.setStrokeWidth(1f);
    }

    public void setData(List<Bar> bars) {
        this.bars = bars;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bars.isEmpty()) return;

        int w = getWidth(), h = getHeight();
        float padL = 16f, padR = 16f, padB = 48f, padT = 16f;
        float chartW = w - padL - padR;
        float chartH = h - padT - padB;

        float maxVal = 1f;
        for (Bar b : bars) if (b.value > maxVal) maxVal = b.value;

        // Horizontal grid lines
        for (int i = 0; i <= 4; i++) {
            float y = padT + chartH * (1 - i / 4f);
            canvas.drawLine(padL, y, w - padR, y, linePaint);
        }

        float barW = chartW / bars.size();
        float gap = barW * 0.3f;

        for (int i = 0; i < bars.size(); i++) {
            Bar b = bars.get(i);
            float barHeight = (b.value / maxVal) * chartH;
            float left  = padL + i * barW + gap / 2;
            float right = left + barW - gap;
            float top   = padT + chartH - barHeight;
            float bottom = padT + chartH;

            barPaint.setColor(b.color);
            canvas.drawRoundRect(new RectF(left, top, right, bottom), 8f, 8f, barPaint);

            // Label
            textPaint.setTextSize(22f);
            canvas.drawText(b.label, left + (right - left) / 2, h - 10f, textPaint);
        }
    }
}
