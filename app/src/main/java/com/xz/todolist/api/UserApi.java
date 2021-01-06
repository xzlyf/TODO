package com.xz.todolist.api;

import com.orhanobut.logger.Logger;
import com.xz.todolist.content.Local;
import com.xz.todolist.network.NetUtil;
import com.xz.todolist.utils.RSAUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/12
 * rxhttp使用教程：https://www.cnblogs.com/liujingxing/p/12106117.html
 */
public class UserApi {
	private static final String TAG = UserApi.class.getName();
	private static UserApi instance;
	private NetUtil netUtil;

	private UserApi() {
		netUtil = NetUtil.getInstance();
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
	 * 手机注册
	 */
	public void phoneRegister(String phone, String pwd, NetUtil.ResultCallback<String> callback) {

		Map<String, Object> params = new HashMap<>();
		try {
			params.put("password", RSAUtil.publicEncrypt(pwd, RSAUtil.getPublicKey(Local.publicKey)));
			//params.put("password", pwd);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			Logger.e("RSA运算错误");
			callback.onError(null, e);
			return;
		}
		params.put("phone", phone);
		netUtil.get(Local.BASE_URL_USER + Local.GET_REGISTER, params, callback);
	}

	/**
	 * 登录
	 *
	 * @param type 1-手机登录 2-账号登录 3-token登录
	 */
	public void login(int type, String account, String pwd, NetUtil.ResultCallback<String> callback) {
		Map<String, Object> params = new HashMap<>();
		params.put("account", account);
		params.put("type", type);
		try {
			params.put("password", RSAUtil.publicEncrypt(pwd, RSAUtil.getPublicKey(Local.publicKey)));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			Logger.e("RSA运算错误");
			callback.onError(null, e);
			return;
		}

		netUtil.post(Local.BASE_URL_USER + Local.GET_LOGIN, params, callback);
	}


}
