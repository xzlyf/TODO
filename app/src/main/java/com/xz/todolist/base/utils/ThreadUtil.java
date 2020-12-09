package com.xz.todolist.base.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * 线程工具类
 */
public class ThreadUtil {
    /**
     * 子线程运行
     *
     * @param task 任务
     */
    public static void runInThread(Runnable task) {
        new Thread(task).start();
    }

    private static Handler handler = new Handler();

    /**
     * 主线程运行
     *
     * @param task
     */
    public static void runInUIThread(Runnable task) {
        handler.post(task);
    }


    /**
     * 是否正在主线程运行
     *
     * @return
     */
    public static boolean isUIThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
