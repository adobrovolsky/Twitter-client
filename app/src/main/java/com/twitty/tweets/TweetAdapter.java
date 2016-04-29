package com.twitty.tweets;

import com.squareup.picasso.Picasso;
import com.twitty.R;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import twitter4j.MediaEntity;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.User;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {

    public interface PersonClickListener {
        public void onPersonClicked(User user);
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(MenuItem item, int position);
    }

    private ResponseList<Status> statuses;
    private PersonClickListener personClickListener;
    private OnMenuItemClickListener itemClickListener;
    private final Context context;
    private final Format format = new SimpleDateFormat("HH:mm MMM dd", Locale.getDefault());

    public TweetAdapter(Context context, ResponseList<Status> statuses,
                        PersonClickListener personClickListener,
                        OnMenuItemClickListener itemClickListener) {
        this.context = context;
        this.statuses = statuses;
        this.personClickListener = personClickListener;
        this.itemClickListener = itemClickListener;
    }

    public TweetAdapter(Context context, PersonClickListener personClickListener,
                        OnMenuItemClickListener itemClickListener) {
        this(context, null, personClickListener, itemClickListener);
    }

    @Override public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_tweets_item, parent, false);
        return new TweetViewHolder(view);
    }

    @Override public void onBindViewHolder(final TweetViewHolder holder, int position) {
        final Status status = statuses.get(position);
        holder.name.setText(status.getUser().getName());
        String screenName = context.getString(R.string.screen_name, status.getUser().getScreenName());
        holder.screenName.setText(screenName);
        holder.date.setText(format.format(status.getCreatedAt()));
        holder.tweetMessage.setText(status.getText());

        inflateMenu(holder.toolbar);
        loadAvatar(status.getUser().getMiniProfileImageURL(), holder.profileImage);
        loadMedia(status.getMediaEntities(), holder);

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                personClickListener.onPersonClicked(status.getUser());
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                personClickListener.onPersonClicked(status.getUser());
            }
        });

        holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                itemClickListener.onMenuItemClick(item, holder.getAdapterPosition());
                return true;
            }
        });
    }

    private void inflateMenu(Toolbar toolbar) {
        if (toolbar.getMenu().size() == 0) {
            toolbar.inflateMenu(R.menu.tweet_menu);
        }
    }

    private void loadAvatar(String url, ImageView view) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_account)
                .into(view);
    }

    private void loadMedia(MediaEntity[] mediaEntities, TweetViewHolder holder) {
        if (mediaEntities.length > 0 && mediaEntities[0].getType().equals("photo")) {
            Picasso.with(context)
                    .load(mediaEntities[0].getMediaURL())
                    .into(holder.mediaImage);
        }
    }

    @Override public int getItemCount() {
        return statuses == null ? 0 : statuses.size();
    }

    public void setStatuses(ResponseList<Status> statuses) {
        this.statuses = statuses;
    }

    public Status getStatus(int position) {
        return statuses.get(position);
    }


    static class TweetViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.date) TextView date;
        @Bind(R.id.name) TextView name;
        @Bind(R.id.screenName) TextView screenName;
        @Bind(R.id.tweetMessage) TextView tweetMessage;
        @Bind(R.id.profileImage) ImageView profileImage;
        @Bind(R.id.mediaImage) ImageView mediaImage;
        @Bind(R.id.tweetToolbar) Toolbar toolbar;

        public TweetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
