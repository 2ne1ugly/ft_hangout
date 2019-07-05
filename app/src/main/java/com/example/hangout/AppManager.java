package com.example.hangout;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppManager extends Application {
    private static  AppManager _instance;
    private         ContactSQLHelper _contactSQLHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        _contactSQLHelper = new ContactSQLHelper(getApplicationContext());
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleListener());

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        String str = pref.getString("language", "ko");
        Locale loc = new Locale(str);
        config.setLocale(loc);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static synchronized AppManager Instance() {
        return _instance;
    }

    public ContactSQLHelper getContactManager() {
        return _contactSQLHelper;
    }
}
