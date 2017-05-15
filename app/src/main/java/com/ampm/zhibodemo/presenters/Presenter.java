package com.ampm.zhibodemo.presenters;

/**
 * Created by Administrator on 2017/4/15.
 */

public abstract class Presenter {
    //销去持有外部的mContext;
    public abstract void onDestory();
}
