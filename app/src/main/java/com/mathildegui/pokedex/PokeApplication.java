package com.mathildegui.pokedex;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import io.fabric.sdk.android.Fabric;

/**
 * @author mathilde on 19/05/16.
 */
public class PokeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FlowManager.init(new FlowConfig
                .Builder(this)
                .openDatabasesOnInit(true)
                .build());
    }
}
