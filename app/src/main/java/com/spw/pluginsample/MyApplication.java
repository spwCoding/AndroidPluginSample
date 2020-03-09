package com.spw.pluginsample;

import android.app.Application;

import com.spw.util.HookUtils;
import com.spw.util.LoadPluginUtils;

public class MyApplication extends Application {

    static MyApplication application;

    static boolean hasLoadPlugin;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }


    public static void loadPluginAndHook(){
        if(hasLoadPlugin){
            return;
        }
        LoadPluginUtils.loadPlugin(application);//加载插件apK
        HookUtils.hookStartActivity(); //hook Activity启动流程
        hasLoadPlugin = true;
    }


}
