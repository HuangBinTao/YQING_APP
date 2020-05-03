package com.hbt.yiqing.utils;

import android.os.Handler;

public class ThreadUtils {
    public static void runInThread(Runnable runnable){
        new Thread(runnable).start();
    }

    public static Handler mHandler = new Handler();

    //主UI线程
    public static void runInUIThread(Runnable task){
        mHandler.post(task);
    }
}
