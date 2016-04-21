package com.twitty;

import com.twitty.tweets.TweetsViewPagerAdapter;
import com.twitty.write.WriteDialog;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_WRITE_DIALOG = "write_dialog";

    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_PICK_IMAGE = 1;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @Bind(R.id.viewPager) ViewPager mViewPager;
    @Bind(R.id.navigation) NavigationView mNavigationView;
    @Bind(R.id.fabWriteTweet) FloatingActionButton mWriteTweetFab;

    private AppPreferences mPrefs = AppPreferences.getInstance();
    private IntentStarter mIntentStarter = new IntentStarter();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mToolbar.inflateMenu(R.menu.main_menu);
        initNavigationView();
        mWriteTweetFab.setOnClickListener(v -> new WriteDialog()
                .show(getSupportFragmentManager(), TAG_WRITE_DIALOG));

        TweetsViewPagerAdapter adapter = new TweetsViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }

    private void initNavigationView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.view_navigation_open, R.string.view_navigation_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_item_drafts:
                mIntentStarter.showDrafts(this);
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
