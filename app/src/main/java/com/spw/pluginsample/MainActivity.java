package com.spw.pluginsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.load_plugin_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class pluginClazz = Class.forName("com.spw.plugin.PluginBean");
                    Method pluginMethod = pluginClazz.getDeclaredMethod("printInfo");
                    pluginMethod.invoke(null);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        findViewById(R.id.start_plugin_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName  = new ComponentName("com.spw.plugin","com.spw.plugin.PluginMainActivity");
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
    }

}
