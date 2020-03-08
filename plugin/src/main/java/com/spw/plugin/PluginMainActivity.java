package com.spw.plugin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class PluginMainActivity extends BasePluginActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"Hello host",Toast.LENGTH_SHORT).show();

        int pluginActLayoutId = R.layout.plugin_actvity;

        View contentView = LayoutInflater.from(this).inflate(pluginActLayoutId,null);
        Log.e("resId","pluginActLayoutId:" + pluginActLayoutId);
        setContentView(contentView);


    }
}
