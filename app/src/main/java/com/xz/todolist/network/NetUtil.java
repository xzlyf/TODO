package com.xz.todolist.network;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.internal.$Gson$Types;
import com.orhanobut.logger.Logger;
import com.xz.todolist.base.BaseApplication;
import com.xz.todolist.content.Local;
import com.xz.todolist.utils.DateFormat;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/12
 */
public class NetUtil {
	private static final String TAG = NetUtil.class.getName();
	private static NetUtil mInstance;
	private OkHttpClient mOkHttpClient;
	private Handler mDelivery;
	private Gson mGson;

	private NetUtil() {
		OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
		okHttpBuilder.connectTimeout(15, TimeUnit.SECONDS);
		okHttpBuilder.writeTimeout(15, TimeUnit.SECONDS);
		okHttpBuilder.readTimeout(15, TimeUnit.SECONDS);
		//是否自动重连
		okHttpBuilder.retryOnConnectionFailure(false);
		//禁制OkHttp的重定向操作
		//okHttpBuilder.followRedirects(false);
		//okHttpBuilder.followSslRedirects(false);


		//添加https证书
		//loadCert(okHttpBuilder);
		//信任所有证书  不推荐使用
		trustAll(okHttpBuilder);

		mOkHttpClient = okHttpBuilder.build();
		mDelivery = new Handler(Looper.getMainLooper());
		mGson = new GsonBuilder()
				.setDateFormat(DateFormat.STANDARD_EN)//gson解析date类型，这里的date类型对应服务器的yyyy-MM-dd HH:mm:ss
				.create();
	}

	public static NetUtil getInstance() {
		if (mInstance == null) {
			synchronized (NetUtil.class) {
				if (mInstance == null) {
					mInstance = new NetUtil();
				}
			}
		}
		return mInstance;
	}


	/**
	 * 信任所有证书
	 */
	private void trustAll(OkHttpClient.Builder okHttpBuilder) {
		//信任所有服务器地址
		okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String s, SSLSession sslSession) {
				//设置为true
				return true;
			}
		});
		//创建管理器
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] x509Certificates,
					String s) throws java.security.cert.CertificateException {
			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] x509Certificates,
					String s) throws java.security.cert.CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[]{};
			}
		}};
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

			//为OkHttpClient设置sslSocketFactory
			okHttpBuilder.sslSocketFactory(sslContext.getSocketFactory());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加https证书
	 *
	 * @param okHttpBuilder
	 */
	private void loadCert(OkHttpClient.Builder okHttpBuilder) {
		//设置https配置
		X509TrustManager trustManager;
		SSLSocketFactory sslSocketFactory;
		final InputStream inputStream;
		//添加https证书
		try {

			inputStream = BaseApplication.getInstance().getAssets().open("certs/TestCert.cer"); // 得到证书的输入流
			try {

				trustManager = trustManagerForCertificates(inputStream);//以流的方式读入证书
				SSLContext sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, new TrustManager[]{trustManager}, null);
				sslSocketFactory = sslContext.getSocketFactory();

			} catch (GeneralSecurityException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			okHttpBuilder.sslSocketFactory(sslSocketFactory, trustManager);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private X509TrustManager trustManagerForCertificates(InputStream in)
			throws GeneralSecurityException {
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
		if (certificates.isEmpty()) {
			throw new IllegalArgumentException("expected non-empty set of trusted certificates");
		}

		// Put the certificates a key store.
		char[] password = "xzlyf297".toCharArray(); // Any password will work.
		KeyStore keyStore = newEmptyKeyStore(password);
		int index = 0;
		for (Certificate certificate : certificates) {
			String certificateAlias = Integer.toString(index++);
			keyStore.setCertificateEntry(certificateAlias, certificate);
		}

		// Use it to build an X509 trust manager.
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
				KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, password);
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
				TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);
		TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
		if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
			throw new IllegalStateException("Unexpected default trust managers:"
					+ Arrays.toString(trustManagers));
		}
		return (X509TrustManager) trustManagers[0];
	}


	/**
	 * 添加password
	 *
	 * @param password
	 * @return
	 * @throws GeneralSecurityException
	 */
	private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 这里添加自定义的密码，默认
			InputStream in = null; // By convention, 'null' creates an empty key store.
			keyStore.load(in, password);
			return keyStore;
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}


	/**
	 * 为HttpGet 的 url 方便的添加多个name value 参数。
	 *
	 * @param url
	 * @param params
	 * @param isHavFirst 是否需要问号
	 * @return
	 */
	public static String attachHttpGetParams(String url, Map<String, Object> params, boolean isHavFirst) {
		if (params == null) {
			return url;
		}
		Iterator<String> keys = params.keySet().iterator();
		Iterator<Object> values = params.values().iterator();
		StringBuffer stringBuffer = new StringBuffer();
		if (isHavFirst) {
			stringBuffer.append("?");
		} else {
			stringBuffer.append("&");
		}


		for (int i = 0; i < params.size(); i++) {
			String value = null;
			try {
				value = URLEncoder.encode(values.next().toString(), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}

			stringBuffer.append(keys.next() + "=" + value);
			if (i != params.size() - 1) {
				stringBuffer.append("&");
			}
		}
		//Logger.e("HttpGetParams = " + url + stringBuffer);//请求连接
		return url + stringBuffer.toString();
	}


	/**
	 * 处理请求和返回
	 *
	 * @param request
	 */
	private void deliveryRequest(Request request, ResultCallback callback) {
		Call call = mOkHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Logger.e("deliveryResult = " + e.getMessage());
				if (e.toString().contains("closed") || e.toString().contains("Canceled")) {
					//如果是主动取消的情况下
				} else {
					//                    sendFailedStringCallback(request, e, callback);
					sendFailedStringCallback(call.request(), e, callback);
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				try {
					final String string = response.body().string();
					if (callback.mType == String.class) {
						sendSuccessResultCallback(string, callback);
					} else {
						Object o = mGson.fromJson(string, callback.mType);
						sendSuccessResultCallback(o, callback);
					}
				} catch (IOException | JsonParseException e) {
					sendFailedStringCallback(response.request(), e, callback);
				}
			}
		});
	}


	private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
		mDelivery.post(new Runnable() {
			@Override
			public void run() {
				if (callback != null)
					callback.onError(request, e);
			}
		});
	}

	private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
		mDelivery.post(new Runnable() {
			@Override
			public void run() {
				if (callback != null) {
					callback.onResponse(object);
				}
			}
		});
	}

	/**
	 * 结果回调接口
	 *
	 * @param <T>
	 */
	public static abstract class ResultCallback<T> {
		Type mType;

		public ResultCallback() {
			mType = getSuperclassTypeParameter(getClass());
		}

		static Type getSuperclassTypeParameter(Class<?> subclass) {
			Type superclass = subclass.getGenericSuperclass();
			if (superclass instanceof Class) {
				throw new RuntimeException("Missing type parameter.");
			}
			ParameterizedType parameterized = (ParameterizedType) superclass;
			return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
		}

		public abstract void onError(Request request, Exception e);

		public abstract void onResponse(T response);
	}

	/**
	 * 通用GET请求
	 */
	private Request buildGetCommonRequest(String url, Map<String, Object> params) {
		return new Request.Builder()
				.addHeader("Connection", "close")
				.url(attachHttpGetParams(url, params, true))
				.build();
	}

	/**
	 * 携带部分头部数据的GET请求格式
	 */
	private Request buildGetRequest(long timestamp, String url, Map<String, Object> params) {
		return new Request.Builder()
				.addHeader("Connection", "close")
				.addHeader("appid", Local.appId)
				.addHeader("timestamp", String.valueOf(timestamp))
				.addHeader("version", Local.version)
				.addHeader("sign", SecretUtil.getSecret(timestamp))
				.url(attachHttpGetParams(url, params, true))
				.build();
	}

	/**
	 * 携带部分头部数据的POST请求格式
	 */
	private Request buildPostRequest(long timestamp, String url, Map<String, Object> params) {
		if (params == null) {
			params = new HashMap<>();
		}
		FormBody.Builder builder = new FormBody.Builder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			builder.add(entry.getKey(), entry.getValue().toString());
		}
		RequestBody requestBody = builder.build();
		return new Request.Builder()
				.addHeader("appid", Local.appId)
				.addHeader("timestamp", String.valueOf(timestamp))
				.addHeader("version", Local.version)
				.addHeader("sign", SecretUtil.getSecret(timestamp))
				.url(url)
				.post(requestBody)
				.build();
	}

	/**
	 * =============公开请求方法==============
	 */

	public void get(String url, Map<String, Object> params, ResultCallback callback) {
		Request request = buildGetRequest(System.currentTimeMillis(), url, params);
		deliveryRequest(request, callback);
	}

	//无请求头，适用于第三方接口请求
	public void get_public(String url, Map<String, Object> params, ResultCallback callback) {
		Request request = buildGetCommonRequest(url, params);
		deliveryRequest(request, callback);
	}


	public void post(String url, Map<String, Object> params, ResultCallback callback) {
		post(System.currentTimeMillis(), url, params, callback);
	}

	public void post(long timestamp, String url, Map<String, Object> params, ResultCallback callback) {
		Request request = buildPostRequest(timestamp, url, params);
		deliveryRequest(request, callback);
	}

	public void postJson(String url, String json, @Nullable Map<String, Object> params, ResultCallback callback) {
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody requestbody = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
				.addHeader("appid", Local.appId)
				.addHeader("timestamp", String.valueOf(System.currentTimeMillis()))
				.addHeader("version", Local.version)
				.addHeader("sign", SecretUtil.getSecret(System.currentTimeMillis()))
				.url(attachHttpGetParams(url, params, true))
				.post(requestbody)
				.build();
		deliveryRequest(request, callback);

	}

	/**
	 * 根据Tag取消请求
	 * TODO: 2021/1/30  根据tag取消请求 https://blog.csdn.net/buyaoshitududongwo/article/details/80048179
	 */
	public void cancelTag(Object tag) {
		if (tag == null) return;
		for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
			if (tag.equals(call.request().tag())) {
				call.cancel();
			}
		}
		for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
			if (tag.equals(call.request().tag())) {
				call.cancel();
			}
		}
	}

	/**
	 * 根据Tag取消请求
	 */
	public static void cancelTag(OkHttpClient client, Object tag) {
		if (client == null || tag == null) return;
		for (Call call : client.dispatcher().queuedCalls()) {
			if (tag.equals(call.request().tag())) {
				call.cancel();
			}
		}
		for (Call call : client.dispatcher().runningCalls()) {
			if (tag.equals(call.request().tag())) {
				call.cancel();
			}
		}
	}

	/**
	 * 取消所有请求请求
	 */
	public void cancelAll() {
		for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
			call.cancel();
		}
		for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
			call.cancel();
		}
	}

	/**
	 * 取消所有请求请求
	 */
	public static void cancelAll(OkHttpClient client) {
		if (client == null) return;
		for (Call call : client.dispatcher().queuedCalls()) {
			call.cancel();
		}
		for (Call call : client.dispatcher().runningCalls()) {
			call.cancel();
		}
	}

}
