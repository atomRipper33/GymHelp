package com.example.android.gymhelp;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

public class PagerAdapter2 extends FragmentPagerAdapter {       //Statistics activity adapter
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Monthly", "Yearly"};
    private Context context;

    public PagerAdapter2(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Fragment3.newInstance(0);
            case 1:
                return Fragment4.newInstance(1);
            default:
                return null;
        }

    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
