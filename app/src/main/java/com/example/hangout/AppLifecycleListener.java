package com.example.hangout;

import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Calendar;
import java.util.Date;

public class AppLifecycleListener implements LifecycleObserver {
    private Date _pauseTime;
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        if (_pauseTime != null) {
            Date resumeTime = Calendar.getInstance().getTime();
            AppManager appManager = AppManager.Instance();
            long time = resumeTime.getTime() - _pauseTime.getTime();
            String popup = String.format(appManager.getString(R.string.downtime_pop_up), time / 1000.0);
            Toast.makeText(appManager.getApplicationContext(), popup, Toast.LENGTH_SHORT).show();
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        _pauseTime = Calendar.getInstance().getTime();
    }
}
