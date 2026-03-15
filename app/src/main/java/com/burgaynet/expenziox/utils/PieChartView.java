package com.burgaynet.expenziox.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {

    public static class Slice {
        public String label;
        public float value;
        public int color;
        public Slice(String label, float value, int color) {
            this.label = label; this.value = value; this.color = color;
        }
    }

    private List<Slice> slices = new ArrayList<>();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String centerLabel = "";
    private String centerValue = "";

    public PieChartView(Context context) { super(context); init(); }
    public PieChartView(Context context, AttributeSet attrs) { super(context, attrs); init(); }
    public PieChartView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); init(); }

    private void init() {
        centerPaint.setColor(0xFF161F2E);
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<Slice> slices, String centerLabel, String centerValue) {
        this.slices = slices;
        this.centerLabel = centerLabel;
        this.centerValue = centerValue;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (slices.isEmpty()) return;

        int w = getWidth(), h = getHeight();
        float cx = w / 2f, cy = h / 2f;
        float radius = Math.min(cx, cy) * 0.88f;
        float innerRadius = radius * 0.58f;

        float total = 0;
        for (Slice s : slices) total += s.value;
        if (total <= 0) return;

        RectF oval = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
        float startAngle = -90f;

        for (Slice s : slices) {
            float sweep = (s.value / total) * 360f;
            paint.setColor(s.color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(oval, startAngle, sweep, true, paint);
            startAngle += sweep;
        }

        // Gap lines between slices
        startAngle = -90f;
        paint.setColor(0xFF0A0F1A);
        paint.setStrokeWidth(3f);
        paint.setStyle(Paint.Style.STROKE);
        for (Slice s : slices) {
            float sweep = (s.value / total) * 360f;
            canvas.drawArc(oval, startAngle, sweep, true, paint);
            startAngle += sweep;
        }

        // Inner circle (donut hole)
        centerPaint.setColor(0xFF0A0F1A);
        canvas.drawCircle(cx, cy, innerRadius, centerPaint);

        // Center text
        float labelSize = innerRadius * 0.22f;
        float valueSize = innerRadius * 0.32f;

        textPaint.setTextSize(labelSize);
        textPaint.setColor(0xFF7B90A8);
        canvas.drawText(centerLabel, cx, cy - labelSize * 0.3f, textPaint);

        textPaint.setTextSize(valueSize);
        textPaint.setColor(0xFFF0F4FF);
        textPaint.setFakeBoldText(true);
        canvas.drawText(centerValue, cx, cy + valueSize * 0.8f, textPaint);
        textPaint.setFakeBoldText(false);
    }
}
