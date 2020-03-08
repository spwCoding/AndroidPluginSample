# pluginSample Android插件化实现
基于Android 8.0实现加载插件Activity

# 实现原理 

实现原理整体分为两部。第一部加载插件，第二部hook实现启动插件Activity

### Step 1 加载插件

通过反射加载系统 BaseDexClassLoader类，把插件中的dex文件与宿主中的dex文件合并生成新数组后赋值给宿主的 dexElements。核心实现

```
   Class dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElementsFiled = dexPathListClass.getDeclaredField("dexElements");
            dexElementsFiled.setAccessible(true);

            Class dexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
            Field dexPathListFiled  = dexClassLoaderClass.getDeclaredField("pathList");
            dexPathListFiled.setAccessible(true);

            ClassLoader hostClassLoader = context.getClassLoader();
            Object hostDexPathListObject = dexPathListFiled.get(hostClassLoader);// 宿主pathList 字段对象实例
            Object[] hostDexElementsObject = (Object[])dexElementsFiled.get(hostDexPathListObject); //dexElements 字段对象实例


            DexClassLoader pluginClassLoader = new DexClassLoader(apkPath,context.getCacheDir().getAbsolutePath(),null,hostClassLoader);
            Object pluginDexPathListObject = dexPathListFiled.get(pluginClassLoader);// 插件pathList 字段对象实例
            Object[] pluginDexElementsObject = (Object[])dexElementsFiled.get(pluginDexPathListObject); //插件dexElements 字段对象实例

            Object[] newElement = (Object[]) Array.newInstance(
                    hostDexElementsObject.getClass().getComponentType(),
                    hostDexElementsObject.length + pluginDexElementsObject.length);

            
            System.arraycopy(hostDexElementsObject, 0, newElement,
                    0, hostDexElementsObject.length);
            System.arraycopy(pluginDexElementsObject, 0,
                    newElement, hostDexElementsObject.length, pluginDexElementsObject.length);

            dexElementsFiled.set(hostDexPathListObject,newElement);
```


