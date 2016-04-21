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

    private List<Draft> mDrafts;
    private static DraftViewHolder.ClickListener mClickListener;

    public DraftsAdapter(List<Draft> drafts, DraftViewHolder.ClickListener clickListener) {
        mDrafts = drafts;
        mClickListener = clickListener;
    }

    @Override public DraftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_drafts_item, parent, false);
        return new DraftViewHolder(view);
    }

    @Override public int getItemCount() {
        return mDrafts == null ? 0 : mDrafts.size();
    }

    @Override public void onBindViewHolder(DraftViewHolder holder, int position) {
        final Draft draft = mDrafts.get(position);
        holder.text.setText(draft.getText());
    }

    public void setData(List<Draft> data) {
        mDrafts = data;
    }

    public Draft getDraft(int position) {
        return mDrafts.get(position);
    }

    static class DraftViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        interface ClickListener {
            void onItemClick(int position, View v);
            void onItemLongClick(int position, View v);
        }

        @Bind(R.id.text) TextView text;
        @Bind(R.id.cardView) CardView cardView;

        public DraftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
        }

        @Override public void onClick(View v) {
            mClickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override public boolean onLongClick(View v) {
            mClickListener.onItemLongClick(getAdapterPosition(), v);
            return true;
        }
    }
}