# pluginSample Android插件化实现

基于Android 8.0实现加载插件Activity

# 使用方法
1.新建插件Moudle,build插件Apk，把插件Apk放入sdcard（实际开发中放在服务器此处为测试方便）待宿主加载。
2.在Application中调用加载插件，hook Activity启动过程。
```
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LoadPluginUtils.loadPlugin(getApplicationContext());//加载插件apK
        HookUtils.hookStartActivity(); //hook Activity启动流程
    }

}
```
3.宿主启动插件

```
      //启动过程把intnet中值改为启动宿主的 ProxyActivity
        findViewById(R.id.start_plugin_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName  = new ComponentName("com.spw.plugin","com.spw.plugin.PluginMainActivity");
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
```



# 实现原理

实现步骤整体分为三步。第一步加载插件，通过反射加载系统 BaseDexClassLoader类，把插件中的dex文件与宿主中的dex文件合并生成新数组后赋值给宿主的 dexElements,具体实现见宿主项目下LoadPluginUtils.loadPlugin(Context context)方法实现;第二步hook实现启动插件Activity，实现原理,在系统Ams启动Activity之前，把启动插件的Activity的Intent替换为启动宿主中ProxyActivity的Intent,系统Ams调用启动ProxyActivity时再把请求的Intent替换为启动插件的Intent;第三步实现插件资源加载，通过反射把插件中的资源加载到AssetManager中，详见插件中的LoadResourceUtil类实现。


# 注意事项

主app与插件moudle的buildTools以及 support库版本要一致，否则资源合并时会有冲突导致插件资源加载失败







