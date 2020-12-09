package com.xz.todolist.content;

import android.Manifest;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/9
 */
public class Local {
    public static int flag = -1;
    //待申请权限列表
    public static final String[] permission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private static String SERVER = "https://192.168.1.66:8080/";
    private static String alt_user = "user";
    private static String alt_todolist = "todolist";
    private static String alt_appinfo = "appinfo";
}
