package com.spw.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

public class LoadResourceUtil {

    private final static String apkPath = "/sdcard/plugin-debug.apk";


    private static Resources mResources;

    public static Resources getResources(Context context) {
        if (mResources == null) {
            mResources = loadResources(context);
        }
        return mResources;
    }


    public static Resources loadResources(Context context) {
        try {
            // 创建一个 AssetManager
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass()
                    .getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.setAccessible(true);

            addAssetPathMethod.invoke(assetManager, apkPath);

            // AssetManager  加载的资源路径  是插件的
            Resources resources = context.getResources();

            return new Resources(assetManager, resources.getDisplayMetrics(),
                    resources.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
