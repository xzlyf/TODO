package com.xz.todolist.api;

import com.orhanobut.logger.Logger;
import com.xz.todolist.content.Local;
import com.xz.todolist.network.NetUtil;
import com.xz.todolist.utils.RSAUtil;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/12
 * rxhttp使用教程：https://www.cnblogs.com/liujingxing/p/12106117.html
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

	/**
	 * 注册
	 */
	public void register() {

		Map<String, Object> params = new HashMap<>();
		params.put("username", "管理员");
		try {
			params.put("password", RSAUtil.publicEncrypt("123456", RSAUtil.getPublicKey(Local.publicKey)));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			Logger.e("RSA运算错误");
		}
		params.put("phone", "17666566171");
		NetUtil netUtil = NetUtil.getInstance();
		String url = Local.SERVER + Local.alt_user + Local.GET_REGISTER;
		netUtil.get(url, params, new NetUtil.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				Logger.e(e.getMessage());
			}

			@Override
			public void onResponse(String response) {
				Logger.d(response);
			}
		});
	}

}
