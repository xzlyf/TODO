package com.xz.todolist.content;

import android.Manifest;

import androidx.annotation.RequiresPermission;

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
    private static String alt_user = "user/";
    private static String alt_todolist = "todolist/";
    private static String alt_appinfo = "appinfo/";

    public static String appId = "4MakRN8juW8c6Hii4lTl0rt84JDH22c9";
    public static String appSecret = "OoLZs7vHPEPsr9YtZtnLY7My2W2RiwTk";
    public static String version = "1.0";

    //公钥
    //更新日期：2020/12/03 00:20
    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt5OvgZD9ZuncevvfIMeJU0AiDK01bnMoL9BUhHrf5netVR_ZZ3xNhSDZFlA-wfHiiMMZnUqxsQPBKs8J-zwff-yaYTdg3pvHgsfSePH-_ZIo_bH3meO-_s6l2F0Qqp4pXP7P7lGAI73rYfEAfKiwK79c042PMQdOLYGGnMsB6RwIDAQAB";

}
