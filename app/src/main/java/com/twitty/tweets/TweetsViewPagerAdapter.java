package com.twitty.tweets;

import com.twitty.base.BaseLceFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

public class TweetsViewPagerAdapter extends FragmentPagerAdapter {

    public static final int TWEETS_FRAGMENT = 0;

    private Map<Integer, BaseLceFragment> mFragments;

    public TweetsViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new HashMap<>();
        mFragments.put(TWEETS_FRAGMENT, new TweetsFragment());
    }

    @Override public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override public int getCount() {
        return mFragments.size();
    }

    @Override public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }

}
