package com.twitty.tweets;

import com.twitty.MainApplication;
import com.twitty.R;
import com.twitty.base.BaseLceFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.User;

public class TweetsFragment
        extends BaseLceFragment<SwipeRefreshLayout, ResponseList<Status>, TweetContract.View, TweetsPresenter>
        implements TweetContract.View, SwipeRefreshLayout.OnRefreshListener, TweetAdapter.PersonClickListener,
        TweetAdapter.TweetActionsListener {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    private TweetAdapter adapter;
    TweetsComponent component;

    @Override public void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentView.setOnRefreshListener(this);
        initRecyclerView();
        loadData(false);
    }

    private void initRecyclerView() {
        adapter = new TweetAdapter(getActivity())
                .setPersonClickListener(this)
                .setTweetActionsListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return pullToRefresh ?
                getString(R.string.error_loading_data) :
                getString(R.string.error_refreshing_data);
    }

    @Override public TweetsPresenter createPresenter() {
        return component.getPresenter();
    }

    @Override public void setData(ResponseList<Status> data) {
        loadingView.setVisibility(View.GONE);
        adapter.setStatuses(data);
        adapter.notifyDataSetChanged();
    }

    @Override public void loadData(boolean pullToRefresh) {
        presenter.loadHomeTimeLine(pullToRefresh);
    }

    @Override public void onRefresh() {
        loadData(true);
    }

    @Override public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
    }

    @Override public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        contentView.setRefreshing(pullToRefresh);
    }

    protected void injectDependencies() {
        component = DaggerTweetsComponent.builder()
                .userComponent(MainApplication.getUserComponent())
                .build();
        component.inject(this);
    }

    @Override public void onReplyClicked(Status status) {
        // TODO: open dialog to receive the message
        StatusUpdate statusUpdate = new StatusUpdate("");
        presenter.reply(statusUpdate);
    }

    @Override public void onRetweetClicked(Status status) {
        if (status.isRetweeted()) {
            presenter.destroyRetweet(status);
        } else {
            presenter.retweet(status);
        }
    }

    @Override public void onFavoriteClicked(Status status) {
        if (status.isFavorited()) {
            presenter.destroyFavoriteTweet(status.getId());
        } else {
            presenter.createFavoriteTweet(status.getId());
        }
    }

    @Override public void onPersonClicked(User user) {

    }
}
