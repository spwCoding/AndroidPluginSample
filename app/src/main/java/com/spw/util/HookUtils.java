package com.spw.util;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;


import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookUtils {

    private final static String TARGET_INTENT = "target_intent";

    public static void hookStartActivity(){

        hookAmsReplacePluginIntent();

        hookActivityThreadReStorePluginIntent();
    }

    /**
     * 启动插件 Activity 在系统交互前替换插件Activity的Intent 为 代理Activity的Intent
     */
    private static void hookAmsReplacePluginIntent(){
        try {
            Class activityManagerClass = Class.forName("android.app.ActivityManager");
            Field  activityManagerField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
            activityManagerField.setAccessible(true);
            Object activityManagerObject = activityManagerField.get(null);

            Class singletonClass = Class.forName("android.util.Singleton");
            Field singletonField = singletonClass.getDeclaredField("mInstance");
            singletonField.setAccessible(true);

            final Object mInstance = singletonField.get(activityManagerObject);

            Class iActivityManagerClass = Class.forName("android.app.IActivityManager");
            Object proxyClass = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iActivityManagerClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if("startActivity".equals(method.getName())){
                        int index = 0;

                        for (int i = 0; i < args.length; i++) {
                            if (args[i] instanceof Intent) {
                                index = i;
                                break;
                            }
                        }
                        //拿到了 intent --》 插件：1
                        Intent intent = (Intent) args[index];

                        // 替换
                        Intent proxyIntent = new Intent();
                        proxyIntent.setClassName("com.spw.pluginsample",
                                "com.spw.pluginsample.ProxyActivity");

                        proxyIntent.putExtra(TARGET_INTENT, intent);

                        //代理替换了插件的
                        args[index] = proxyIntent;
                    }
                    return method.invoke(mInstance,args);
                }
            });

            singletonField.set(activityManagerObject,proxyClass);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 启动插件 Activity 在系统验证通过后替换为插件中的Intent
     */
    private static void hookActivityThreadReStorePluginIntent(){
        try {
            Class actThreadClazz = Class.forName("android.app.ActivityThread");
            Field currentActivityThreadField = actThreadClazz.getDeclaredField("sCurrentActivityThread");
            currentActivityThreadField.setAccessible(true);
            Object currentActivityThreadObject = currentActivityThreadField.get(null);
            Field handerField = actThreadClazz.getDeclaredField("mH");
            handerField.setAccessible(true);
            Object handlerObject = handerField.get(currentActivityThreadObject);

            Class handlerClazz = Class.forName("android.os.Handler");
            Field handlerCallbackField = handlerClazz.getDeclaredField("mCallback");
            handlerCallbackField.setAccessible(true);
            Object handlerCallbackObject = new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    switch (msg.what){
                        case 100:
                            try {
                                // 替换的：Intent intent; --》 ActivityClientRecord的对象 == msg.obj
                                Field intentField = msg.obj.getClass().getDeclaredField("intent");
                                intentField.setAccessible(true);
                                // 代理的
                                Intent proxyIntent = (Intent) intentField.get(msg.obj);
                                // 获取插件的
                                Intent intent = proxyIntent.getParcelableExtra(TARGET_INTENT);
                                //替换
                                if (intent != null) {
                                    intentField.set(msg.obj, intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        default:
                            break;
                    }
                    return false;
                }
            };
            handlerCallbackField.set(handlerObject,handlerCallbackObject);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
