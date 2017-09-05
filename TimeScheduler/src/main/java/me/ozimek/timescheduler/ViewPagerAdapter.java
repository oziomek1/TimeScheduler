package me.ozimek.timescheduler;

/**
 * Created by wojtek on 2017-08-19.
 */


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import me.ozimek.timescheduler.pager.FragmentCategories;
import me.ozimek.timescheduler.pager.FragmentHome;
import me.ozimek.timescheduler.pager.FragmentStats;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int[] icons = {R.drawable.home, R.drawable.category, R.drawable.stats};
    String[] tabText = {"HOME", "CATEGORIES", "STATS"};

    private static int NUM_ITEMS = 3;
    private Context context;

    public ViewPagerAdapter(FragmentManager fm, String[] mTabText, Context mContext) {
        super(fm);
        this.tabText = mTabText;
        context = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FragmentHome();
                break;
            case 1:
                fragment = new FragmentCategories();
                break;
            case 2:
                fragment = new FragmentStats();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabText[position];
    /*    Drawable image = context.getResources().getDrawable(icons[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb; */
    /*    Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), icons[position], null);
        drawable.setBounds(0, 0, 64, 64);
        //context.getResources().getDrawable(icons[position], null);
        ImageSpan imageSpan = new ImageSpan(drawable);
        SpannableString spannableString = new SpannableString("");
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;*/
    }
}

