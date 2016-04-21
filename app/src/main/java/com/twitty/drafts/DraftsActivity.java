package com.twitty.drafts;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceActivity;
import com.twitty.R;
import com.twitty.drafts.DraftsAdapter.DraftViewHolder;
import com.twitty.store.DraftsLoader;
import com.twitty.store.DraftsRepository;
import com.twitty.store.entity.Draft;
import com.twitty.write.WriteDialog;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DraftsActivity
        extends MvpLceActivity<SwipeRefreshLayout, List<Draft>, DraftsContract.View, DraftsContract.Presenter>
        implements DraftsContract.View, SwipeRefreshLayout.OnRefreshListener,
        ActionMode.Callback, DraftViewHolder.ClickListener {

    private static final String TAG_WRITE_DIALOG = "write_dialog";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    private DraftsAdapter mAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);
        ButterKnife.bind(this);
        contentView.setOnRefreshListener(this);

        initToolbar();
        initRecyclerView();
        loadData(false);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void initRecyclerView() {
        mAdapter = new DraftsAdapter(null, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override public void loadData(boolean pullToRefresh) {
        presenter.loadDrafts(pullToRefresh);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return pullToRefresh ?
                getString(R.string.error_loading_data) :
                getString(R.string.error_refreshing_data);
    }

    @Override public DraftsContract.Presenter createPresenter() {
        DraftsRepository draftsRepository = new DraftsRepository();
        DraftsLoader draftsLoader = new DraftsLoader(this, draftsRepository);
        return new DraftsPresenter(draftsLoader, draftsRepository, getSupportLoaderManager());
    }

    @Override public void setData(List<Draft> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
        loadingView.setVisibility(View.GONE);
    }

    @Override public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        contentView.setRefreshing(pullToRefresh);
    }

    @Override public void onRefresh() {
        loadData(true);
    }

    @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.drafts_context_menu, menu);
        return true;
    }

    @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.menu_item_delete) {
            presenter.removeDraft((Draft) mode.getTag());
        }
        mode.finish();
        return true;
    }

    @Override public void onDestroyActionMode(ActionMode mode) { /* NOP */ }

    @Override public void onItemClick(int position, View v) {
        Draft draft = mAdapter.getDraft(position);
        WriteDialog.newInstance(draft)
                .show(getSupportFragmentManager(), TAG_WRITE_DIALOG);
    }

    @Override public void onItemLongClick(int position, View v) {
        startSupportActionMode(this)
                .setTag(mAdapter.getDraft(position));
    }
}
