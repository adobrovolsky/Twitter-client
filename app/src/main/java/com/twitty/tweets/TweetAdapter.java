package com.twitty.tweets;

import com.squareup.picasso.Picasso;
import com.twitty.R;
import com.twitty.view.ToggleImageButton;

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

    public interface TweetActionsListener {
        void onReplyClicked(Status status);
        void onRetweetClicked(Status status);
        void onFavoriteClicked(Status status);
    }

    private ResponseList<Status> statuses;
    private PersonClickListener personClickListener;
    private OnMenuItemClickListener itemClickListener;
    private TweetActionsListener tweetActionsListener;
    private final Context context;
    private final Format format = new SimpleDateFormat("HH:mm MMM dd", Locale.getDefault());

    public TweetAdapter(Context context, ResponseList<Status> statuses) {
        this.context = context;
        this.statuses = statuses;
    }

    public TweetAdapter(Context context) {
        this.context = context;
    }

    public TweetAdapter setPersonClickListener(PersonClickListener personClickListener) {
        this.personClickListener = personClickListener;
        return this;
    }

    public TweetAdapter setItemClickListener(OnMenuItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public TweetAdapter setTweetActionsListener(TweetActionsListener tweetActionsListener) {
        this.tweetActionsListener = tweetActionsListener;
        return this;
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

        initTweetToolbar(holder, status);
        loadAvatar(status.getUser().getMiniProfileImageURL(), holder.profileImage);
        loadMedia(status.getMediaEntities(), holder);
    }

    private void initTweetToolbar(TweetViewHolder holder, Status status) {
        if (holder.toolbar.getMenu().size() == 0) {
            holder.toolbar.inflateMenu(R.menu.tweet_menu);
        }

        holder.retweetButton.setChecked(status.isRetweeted());
        if (status.getRetweetCount() > 0) {
            holder.retweetCount.setText(String.valueOf(status.getRetweetCount()));
        }
        holder.favoriteButton.setChecked(status.isFavorited());
        if (status.getFavoriteCount() > 0) {
            holder.favoriteCount.setText(String.valueOf(status.getFavoriteCount()));
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


    public class TweetViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

        @Bind(R.id.date) TextView date;
        @Bind(R.id.name) TextView name;
        @Bind(R.id.screenName) TextView screenName;
        @Bind(R.id.tweetMessage) TextView tweetMessage;
        @Bind(R.id.avatar) ImageView profileImage;
        @Bind(R.id.mediaImage) ImageView mediaImage;
        @Bind(R.id.replyIcon) ToggleImageButton replyButton;
        @Bind(R.id.retweetIcon) ToggleImageButton retweetButton;
        @Bind(R.id.favoriteIcon) ToggleImageButton favoriteButton;
        @Bind(R.id.replyCount) TextView replyCount;
        @Bind(R.id.retweetCount) TextView retweetCount;
        @Bind(R.id.favoriteCount) TextView favoriteCount;
        @Bind(R.id.tweetToolbar) Toolbar toolbar;
        @Bind(R.id.tweetActions) View tweetActions;

        public TweetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            name.setOnClickListener(this);
            profileImage.setOnClickListener(this);
            retweetButton.setOnClickListener(this);
            replyButton.setOnClickListener(this);
            favoriteButton.setOnClickListener(this);
            toolbar.setOnMenuItemClickListener(this);
        }

        @Override public void onClick(View v) {
            if (tweetActionsListener == null) {
                return;
            }

            Status status = statuses.get(getAdapterPosition());

            switch (v.getId()) {
                case R.id.retweetIcon:
                    tweetActionsListener.onRetweetClicked(status);
                    retweetButton.toggle();
                    break;
                case R.id.replyIcon:
                    tweetActionsListener.onReplyClicked(status);
                    replyButton.toggle();
                    break;
                case R.id.favoriteIcon:
                    tweetActionsListener.onFavoriteClicked(status);
                    favoriteButton.toggle();
                    break;
                case R.id.name:
                case R.id.avatar:
                    User user = statuses.get(getAdapterPosition()).getUser();
                    personClickListener.onPersonClicked(user);

            }
        }

        @Override public boolean onMenuItemClick(MenuItem item) {
            if (itemClickListener == null) {
                return false;
            }
            itemClickListener.onMenuItemClick(item, getAdapterPosition());
            return true;
        }
    }
}
