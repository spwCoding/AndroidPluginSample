package com.spw.plugin;

import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

public class BasePluginActivity extends AppCompatActivity {

    @Override
    public Resources getResources() {
        Resources resources = LoadResourceUtil.getResources(getApplication());
        return resources == null ? super.getResources() : resources;
    }
}
