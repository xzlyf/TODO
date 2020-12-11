package com.xz.todolist.api;

import rxhttp.IRxHttp;
import rxhttp.RxHttpPlugins;
import rxhttp.wrapper.param.RxHttp;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/12
 */
public class UserApi {
	private static final String TAG = UserApi.class.getName();
	private static UserApi instance;

	private UserApi() {
	}

	public static UserApi getInstance() {
		if (instance == null) {
			synchronized (UserApi.class) {
				if (instance == null) {
					instance = new UserApi();
				}
			}
		}
		return instance;
	}

	public void login(){
		//rxhttp使用教程：https://www.cnblogs.com/liujingxing/p/12106117.html
	}
}
