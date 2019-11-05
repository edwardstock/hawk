package com.orhanobut.benchmark;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.LogInterceptor;

import static com.orhanobut.hawk.Hawk.db;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeHawkInit();
        timeHawkPut();
        timeHawkGet();
        timeHawkContains();
        timeHawkCount();
        timeHawkDelete();
    }

    private void timeHawkInit() {
        long startTime = System.currentTimeMillis();

        Hawk.init(this).setLogger(new LogInterceptor() {
            @Override
            public void onLog(String message) {
                Log.d("HAWK", message);
            }
        }).build();

        long endTime = System.currentTimeMillis();
        System.out.println("Hawk.init: " + (endTime - startTime) + "ms");
    }

    private void timeHawkPut() {
        long startTime = System.currentTimeMillis();

        db().put("key", "value");

        long endTime = System.currentTimeMillis();
        System.out.println("Hawk.put: " + (endTime - startTime) + "ms");
    }

    private void timeHawkGet() {
        long startTime = System.currentTimeMillis();

        try {
            db().get("key");
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Hawk.get: " + (endTime - startTime) + "ms");
    }

    private void timeHawkCount() {
        long startTime = System.currentTimeMillis();

        db().count();

        long endTime = System.currentTimeMillis();
        System.out.println("Hawk.count: " + (endTime - startTime) + "ms");
    }

    private void timeHawkContains() {
        long startTime = System.currentTimeMillis();

        db().contains("key");

        long endTime = System.currentTimeMillis();
        System.out.println("Hawk.count: " + (endTime - startTime) + "ms");
    }

    private void timeHawkDelete() {
        long startTime = System.currentTimeMillis();

        db().delete("key");

        long endTime = System.currentTimeMillis();
        System.out.println("Hawk.count: " + (endTime - startTime) + "ms");
    }
}
