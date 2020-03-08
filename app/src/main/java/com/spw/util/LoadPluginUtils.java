package com.spw.util;

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class LoadPluginUtils {

    private final static String apkPath = "/sdcard/plugin-debug.apk";

    public static void loadPlugin(Context context){

        try{
            Class dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElementsFiled = dexPathListClass.getDeclaredField("dexElements");
            dexElementsFiled.setAccessible(true);
            //Object[] dexElementsObject = (Object[])dexElementsFiled.get(dexPathListClass);

            Class dexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
            Field dexPathListFiled  = dexClassLoaderClass.getDeclaredField("pathList");
            dexPathListFiled.setAccessible(true);

            ClassLoader hostClassLoader = context.getClassLoader();
            Object hostDexPathListObject = dexPathListFiled.get(hostClassLoader);// 宿主pathList 字段对象实例
            Object[] hostDexElementsObject = (Object[])dexElementsFiled.get(hostDexPathListObject); //dexElements 字段对象实例


            DexClassLoader pluginClassLoader = new DexClassLoader(apkPath,context.getCacheDir().getAbsolutePath(),null,hostClassLoader);
            Object pluginDexPathListObject = dexPathListFiled.get(pluginClassLoader);// 插件pathList 字段对象实例
            Object[] pluginDexElementsObject = (Object[])dexElementsFiled.get(pluginDexPathListObject); //插件dexElements 字段对象实例

            // 创建一个新的数组  Element  --> new Element[]
            Object[] newElement = (Object[]) Array.newInstance(
                    hostDexElementsObject.getClass().getComponentType(),
                    hostDexElementsObject.length + pluginDexElementsObject.length);

            // 赋值
            System.arraycopy(hostDexElementsObject, 0, newElement,
                    0, hostDexElementsObject.length);
            System.arraycopy(pluginDexElementsObject, 0,
                    newElement, hostDexElementsObject.length, pluginDexElementsObject.length);

            dexElementsFiled.set(hostDexPathListObject,newElement);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
