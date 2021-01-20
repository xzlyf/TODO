package com.xz.todolist.base;

import android.app.Activity;
import android.app.Application;

import com.orhanobut.logger.Logger;
import com.xz.todolist.content.Local;
import com.xz.todolist.crash.CrashHandler;
import com.xz.todolist.base.utils.PreferencesUtilV2;
import com.xz.xlogin.XLogin;

import java.util.ArrayList;
import java.util.List;


public class BaseApplication extends Application {
	//后端云appid
	public static String APPID = "5cb7617206c6ffec395c0324d06942a5";

	private List<Activity> activities = new ArrayList<>();
	private static BaseApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initLog();
		initLoginProcess();
		PreferencesUtilV2.initPreferencesUtils(this, "my_app");
		//initCrash();
	}

	/**
	 * 登陆器Sdk
	 */
	private void initLoginProcess() {
		XLogin.init(new XLogin.XLoginBuilder()
				.log("xzlyf")
				.appId(Local.appId)
				.appSecret(Local.appSecret)
				.serverVersion(Local.version)
				.server(Local.SERVER)
				.publicKey(Local.publicKey)
				.build());
	}

	/**
	 * 崩溃日志捕捉
	 */
	private void initCrash() {
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}


	/**
	 * 日志工具
	 */
	private void initLog() {
		Logger.init("xzlyf")    //LOG TAG默认是PRETTYLOGGER
				.methodCount(2)                 // 决定打印多少行（每一行代表一个方法）默认：2
				.methodOffset(0);             // 默认：0
	}


	public static BaseApplication getInstance() {
		return instance;
	}

	/**
	 * 新建了一个activity
	 *
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	/**
	 * 结束指定的Activity
	 *
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			this.activities.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 应用退出，结束所有的activity
	 */
	public void exit() {
		for (Activity activity : activities) {
			if (activity != null) {
				activity.finish();
			}
		}
		System.exit(0);
	}

	/**
	 * 关闭Activity列表中的所有Activity
	 */
	public void finishActivity() {
		for (Activity activity : activities) {
			if (null != activity) {
				activity.finish();
			}
		}
	}


}
