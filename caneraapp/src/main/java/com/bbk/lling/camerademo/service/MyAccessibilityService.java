package com.bbk.lling.camerademo.service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MyAccessibilityService extends AccessibilityService {
    public static final int BACK = 1;
    public static final int HOME = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }
    @Subscribe
    public void onReceive(Integer action){
        switch (action) {
            case BACK :
                //执行全局动作
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                break;
            case HOME :
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                break;
        }
    }
}
