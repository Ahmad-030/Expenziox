package com.burgaynet.expenziox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgaynet.expenziox.R;
import com.burgaynet.expenziox.models.TransactionModel;
import com.burgaynet.expenziox.utils.AppConstants;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.VH> {

    public interface OnDeleteListener { void onDelete(TransactionModel t); }

    private final Context context;
    private List<TransactionModel> list;
    private final OnDeleteListener deleteListener;
    private int lastPosition = -1;

    public TransactionAdapter(Context ctx, List<TransactionModel> list, OnDeleteListener l) {
        this.context = ctx; this.list = list; this.deleteListener = l;
    }

    public void updateList(List<TransactionModel> newList) {
        this.list = newList; lastPosition = -1; notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TransactionModel t = list.get(position);
        int catColor = AppConstants.getCategoryColor(t.getCategory());
        String emoji = AppConstants.getCategoryEmoji(t.getCategory());

        h.tvEmoji.setText(emoji);
        // Guard against null background before tinting
        if (h.tvEmoji.getBackground() != null) {
            h.tvEmoji.getBackground().setTint(catColor & 0x33FFFFFF | 0x22000000);
        }
        h.tvCategory.setText(t.getCategory());
        String note = t.getNote();
        h.tvNote.setText((note == null || note.isEmpty()) ? t.getDate() : note);
        h.tvDate.setText(t.getDate());

        String sign = t.isExpense() ? "- " : "+ ";
        h.tvAmount.setText(sign + "PKR " + String.format("%.0f", t.getAmount()));
        h.tvAmount.setTextColor(t.isExpense() ? 0xFFFF6B6B : 0xFF95D5B2);

        // Stagger slide-in
        if (position > lastPosition) {
            h.itemView.setAlpha(0f);
            h.itemView.setTranslationX(50f);
            h.itemView.animate().alpha(1f).translationX(0f)
                    .setDuration(280).setStartDelay(position * 40L)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator())
                    .start();
            lastPosition = position;
        }

        h.itemView.setOnLongClickListener(v -> { deleteListener.onDelete(t); return true; });
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvCategory, tvNote, tvDate, tvAmount;
        VH(@NonNull View v) {
            super(v);
            tvEmoji    = v.findViewById(R.id.tvEmoji);
            tvCategory = v.findViewById(R.id.tvCategory);
            tvNote     = v.findViewById(R.id.tvNote);
            tvDate     = v.findViewById(R.id.tvDate);
            tvAmount   = v.findViewById(R.id.tvAmount);
        }
    }
}