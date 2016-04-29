package com.twitty.drafts;

import com.twitty.R;
import com.twitty.store.entity.Draft;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.DraftViewHolder> {

    private List<Draft> drafts;
    private static DraftViewHolder.ClickListener clickListener;

    public DraftsAdapter(List<Draft> drafts, DraftViewHolder.ClickListener clickListener) {
        this.drafts = drafts;
        this.clickListener = clickListener;
    }

    @Override public DraftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_drafts_item, parent, false);
        return new DraftViewHolder(view);
    }

    @Override public int getItemCount() {
        return drafts == null ? 0 : drafts.size();
    }

    @Override public void onBindViewHolder(DraftViewHolder holder, int position) {
        final Draft draft = drafts.get(position);
        holder.text.setText(draft.getText());
    }

    public void setData(List<Draft> data) {
        drafts = data;
    }

    public Draft getDraft(int position) {
        return drafts.get(position);
    }

    static class DraftViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        interface ClickListener {
            void onItemClick(int position, View view);
            void onItemLongClick(int position, View view);
        }

        @Bind(R.id.text) TextView text;
        @Bind(R.id.cardView) CardView cardView;

        public DraftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
        }

        @Override public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }

        @Override public boolean onLongClick(View view) {
            clickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }
}