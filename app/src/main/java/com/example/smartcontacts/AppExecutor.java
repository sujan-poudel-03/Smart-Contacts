package com.example.smartcontacts;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static AppExecutor instance;
    private final Executor diskIO;
    private final Executor mainThread;

    private AppExecutor(Executor diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    public static AppExecutor getInstance() {
        if (instance == null) {
            instance = new AppExecutor(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
        }
        return instance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
