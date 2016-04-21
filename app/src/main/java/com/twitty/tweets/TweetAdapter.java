package com.twitty.tweets;

import com.squareup.picasso.Picasso;
import com.twitty.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

    private Context mContext;
    private ResponseList<Status> mStatuses;
    private PersonClickListener mPersonClickListener;
    private OnMenuItemClickListener mItemClickListener;
    private final Format mFormat = new SimpleDateFormat("HH:mm MMM dd", Locale.getDefault());

    public TweetAdapter(Context context, ResponseList<Status> statuses,
                        PersonClickListener personClickListener,
                        OnMenuItemClickListener itemClickListener) {

        mContext = context;
        mStatuses = statuses;
        mPersonClickListener = personClickListener;
        mItemClickListener = itemClickListener;
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
        final Status status = mStatuses.get(position);
        holder.name.setText(status.getUser().getName());
        String screenName = mContext.getString(R.string.screen_name, status.getUser().getScreenName());
        holder.screenName.setText(screenName);
        holder.date.setText(mFormat.format(status.getCreatedAt()));
        holder.tweetMessage.setText(status.getText());

        if (holder.toolbar.getMenu().size() == 0) {
            holder.toolbar.inflateMenu(R.menu.tweet_menu);
        }

        Picasso.with(mContext)
                .load(status.getUser().getMiniProfileImageURL())
                .placeholder(R.mipmap.ic_account)
                .into(holder.profileImage);

        MediaEntity[] mediaEntities = status.getMediaEntities();
        if (mediaEntities.length > 0 && mediaEntities[0].getType().equals("photo")) {
            Picasso.with(mContext).
                    load(mediaEntities[0].getMediaURL())
                    .into(holder.mediaImage);
        }

        holder.profileImage.setOnClickListener(v ->
                mPersonClickListener.onPersonClicked(status.getUser()));

        holder.name.setOnClickListener(v ->
                mPersonClickListener.onPersonClicked(status.getUser()));

        holder.toolbar.setOnMenuItemClickListener(item -> {
                mItemClickListener.onMenuItemClick(item, position);
                return true;
        });
    }

    @Override public int getItemCount() {
        return mStatuses == null ? 0 : mStatuses.size();
    }

    public void setStatuses(ResponseList<Status> statuses) {
        mStatuses = statuses;
    }

    public Status getStatus(int position) {
        return mStatuses.get(position);
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

    public abstract static class TweetHolderWrapper {

        public static final String PROFILE_IMAGE = "profile_image";
        public static final String MEDIA_IMAGE = "media_image";

        private final TweetViewHolder mHolder;
        private final String mType;

        public TweetHolderWrapper(TweetViewHolder holder, String type) {
            mHolder = holder;
            mType = type;
        }

        public abstract void bindImage(Drawable drawable);

        public TweetViewHolder getHolder() {
            return mHolder;
        }

        public String getType() {
            return mType;
        }
    }
}
