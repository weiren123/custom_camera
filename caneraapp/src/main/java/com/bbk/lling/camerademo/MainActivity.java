package com.bbk.lling.camerademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private TabLayout tab_viewpager;
    private ViewPager view_viewpager;
    private String[] names = {"热门推荐","热门收藏","本月热榜","今日热榜"};
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private LayoutInflater mInflater;
    private View view1;
    private View view2;
    private View view3;
    private View view4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tab_viewpager = (TabLayout) findViewById(R.id.tab_viewpager);
        view_viewpager = (ViewPager) findViewById(R.id.view_viewpager);
        mInflater = LayoutInflater.from(this);
        view1 = mInflater.inflate(R.layout.activity_main_item, null);
        view2 = mInflater.inflate(R.layout.activity_main_item, null);
        view3 = mInflater.inflate(R.layout.activity_main_item, null);
        view4 = mInflater.inflate(R.layout.activity_main_item, null);
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mViewList.add(view4);
        tab_viewpager.addTab(tab_viewpager.newTab().setText(names[0]));
        tab_viewpager.addTab(tab_viewpager.newTab().setText(names[1]));
        tab_viewpager.addTab(tab_viewpager.newTab().setText(names[2]));
        tab_viewpager.addTab(tab_viewpager.newTab().setText(names[3]));
        tab_viewpager.setTabMode(TabLayout.MODE_FIXED);
        MyAdapter adapter = new MyAdapter();
        view_viewpager.setAdapter(adapter);
        tab_viewpager.setupWithViewPager(view_viewpager);
    }

    public void takePhote(View view) {
        startActivity(new Intent(this, TakePhoteActivity.class));
    }
    public class MyAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return names[position];//页卡标题
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
