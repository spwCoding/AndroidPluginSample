package com.spw.pluginsample;

import android.app.Application;

import com.spw.util.HookUtils;
import com.spw.util.LoadPluginUtils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LoadPluginUtils.loadPlugin(getApplicationContext());//加载插件apK

        HookUtils.hookStartActivity(); //hook Activity启动流程

    }

}
