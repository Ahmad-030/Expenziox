package com.tooldynoapps.expenziox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tooldynoapps.expenziox.R;
import com.tooldynoapps.expenziox.models.GoalModel;
import com.tooldynoapps.expenziox.utils.AppConstants;

import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.VH> {

    public interface OnDeleteListener { void onDelete(GoalModel g); }
    public interface OnAddSavingsListener { void onAdd(GoalModel g); }

    private final Context context;
    private List<GoalModel> list;
    private final OnDeleteListener deleteListener;
    private final OnAddSavingsListener addListener;
    private int lastPosition = -1;

    public GoalAdapter(Context ctx, List<GoalModel> list,
                       OnDeleteListener dl, OnAddSavingsListener al) {
        this.context = ctx; this.list = list;
        this.deleteListener = dl; this.addListener = al;
    }

    public void updateList(List<GoalModel> newList) {
        this.list = newList; lastPosition = -1; notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_goal, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        GoalModel g = list.get(position);
        int priorityColor = AppConstants.getPriorityColor(g.getPriority());

        h.tvGoalName.setText(g.getName());
        h.tvPriority.setText(g.getPriority());
        h.tvPriority.setTextColor(priorityColor);
        h.tvPriority.getBackground().setTint(priorityColor & 0x22FFFFFF | 0x22000000);
        h.tvDeadline.setText("🗓 " + g.getDeadline());
        h.tvProgress.setText(g.getProgressPercent() + "%");
        h.progressBar.setProgress(g.getProgressPercent());
        h.progressBar.getProgressDrawable().setTint(priorityColor);
        h.tvSaved.setText("PKR " + String.format("%.0f", g.getSavedAmount())
                + " / PKR " + String.format("%.0f", g.getTargetAmount()));

        h.btnAdd.setOnClickListener(v -> addListener.onAdd(g));
        h.itemView.setOnLongClickListener(v -> { deleteListener.onDelete(g); return true; });

        if (position > lastPosition) {
            h.itemView.setAlpha(0f);
            h.itemView.setTranslationY(40f);
            h.itemView.animate().alpha(1f).translationY(0f)
                    .setDuration(300).setStartDelay(position * 60L)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator())
                    .start();
            lastPosition = position;
        }
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvGoalName, tvPriority, tvDeadline, tvProgress, tvSaved, btnAdd;
        ProgressBar progressBar;
        VH(@NonNull View v) {
            super(v);
            tvGoalName  = v.findViewById(R.id.tvGoalName);
            tvPriority  = v.findViewById(R.id.tvPriority);
            tvDeadline  = v.findViewById(R.id.tvDeadline);
            tvProgress  = v.findViewById(R.id.tvProgress);
            tvSaved     = v.findViewById(R.id.tvSaved);
            progressBar = v.findViewById(R.id.progressBar);
            btnAdd      = v.findViewById(R.id.btnAddSavings);
        }
    }
}
