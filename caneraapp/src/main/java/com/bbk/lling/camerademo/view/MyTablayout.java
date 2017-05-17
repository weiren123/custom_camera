package com.bbk.lling.camerademo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.bbk.lling.camerademo.R;

/**
 * Created by Administrator on 2017/5/16.
 */

public class MyTablayout extends TabLayout {
    private final LayoutInflater mInflater;
    private final View view4;

    public MyTablayout(Context context) {
        this(context,null,0);
    }

    public MyTablayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTablayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        view4 = mInflater.inflate(R.layout.activity_tab_indictor, null);
    }

    @Override
    public void setupWithViewPager(@NonNull ViewPager viewPager) {
        super.setupWithViewPager(viewPager);
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(new SliderOnPageChangeListener(this,view4));
    }
    public static class SliderOnPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {


        public SliderOnPageChangeListener(TabLayout tabLayout, View view4) {
            super(tabLayout);
            Tab tabAt = tabLayout.getTabAt(0);
            tabAt.setCustomView(view4);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }
}
