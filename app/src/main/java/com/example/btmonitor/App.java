package com.example.btmonitor;

import android.app.Application;

public class App extends Application {
    private static App appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }

    public static App getAppInstance() {
        return appInstance;
    }
}
