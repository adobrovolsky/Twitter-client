package com.twitty;

import com.twitty.tweets.TweetsViewPagerAdapter;
import com.twitty.util.TwitterUtil;
import com.twitty.write.WriteDialog;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_WRITE_DIALOG = "write_dialog";

    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_PICK_IMAGE = 1;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.navigation) NavigationView navigationView;
    @Bind(R.id.fabWriteTweet) FloatingActionButton writeTweetFab;

    @Inject AppPreferences prefs;
    @Inject IntentStarter intentStarter;
    @Inject TwitterUtil twitterUtil;

    @Override protected void onCreate(Bundle savedInstanceState) {
        if (!twitterUtil.isAuthenticated()) {
            intentStarter.showAuthentication(this);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        injectDependencies();
        initToolBar();
        initNavigationView();
        initViewPager();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initNavigationView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem item) {
                handleItemSelected(item);
                return true;
            }
        });
    }

    private void initViewPager() {
        TweetsViewPagerAdapter adapter = new TweetsViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @OnClick public void writeTweet() {
        new WriteDialog().show(getSupportFragmentManager(), TAG_WRITE_DIALOG);
    }

    public boolean handleItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_item_drafts:
                intentStarter.showDrafts(this);
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_logout:
                twitterUtil.logout(this);
                intentStarter.showAuthentication(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected void injectDependencies() {
        MainActivityComponent activityComponent = DaggerMainActivityComponent.builder()
                .applicationComponent(MainApplication.getAppComponent())
                .build();
        activityComponent.inject(this);
    }
}
