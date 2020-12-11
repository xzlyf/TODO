package com.xz.todolist.network;

import android.util.Log;

import com.xz.utils.netUtils.OkHttpClientManager;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/12
 */
public class NetUtil {
	public static final String TAG = NetUtil.class.getName();
	private static NetUtil mInstance;
	public OkHttpClient mOkHttpClient;

	private NetUtil() {
		OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
		okHttpBuilder.connectTimeout(15, TimeUnit.SECONDS);
		okHttpBuilder.writeTimeout(15, TimeUnit.SECONDS);
		okHttpBuilder.readTimeout(15, TimeUnit.SECONDS);
		//是否自动重连
		okHttpBuilder.retryOnConnectionFailure(false);
		//设置https配置
		try {
			okHttpBuilder.sslSocketFactory(createEasySSLContext().getSocketFactory(), new EasyX509TrustManager(null));
		} catch (IOException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
			Log.e(TAG, "OkHttpClientManager: https Configuration failed ");
		}
		mOkHttpClient = okHttpBuilder.build();
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


	private SSLContext createEasySSLContext() throws IOException {
		try {
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, null, null);
			return context;
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	private static class EasyX509TrustManager implements X509TrustManager {

		private X509TrustManager standardTrustManager = null;

		/**
		 * Constructor for EasyX509TrustManager.
		 */
		private EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException,
				KeyStoreException {
			super();
			TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory
					.getDefaultAlgorithm());
			factory.init(keystore);
			TrustManager[] trustmanagers = factory.getTrustManagers();
			if (trustmanagers.length == 0) {
				throw new NoSuchAlgorithmException("no trust manager found");
			}
			this.standardTrustManager = (X509TrustManager) trustmanagers[0];
		}

		/**
		 * @see X509TrustManager#checkClientTrusted(X509Certificate[],
		 * String authType)
		 */
		public void checkClientTrusted(X509Certificate[] certificates, String authType)
				throws CertificateException {
			standardTrustManager.checkClientTrusted(certificates, authType);
		}

		/**
		 * @see X509TrustManager#checkServerTrusted(X509Certificate[],
		 * String authType)
		 */
		public void checkServerTrusted(X509Certificate[] certificates, String authType)
				throws CertificateException {
			if ((certificates != null) && (certificates.length == 1)) {
				certificates[0].checkValidity();
			} else {
				standardTrustManager.checkServerTrusted(certificates, authType);
			}
		}

		/**
		 * @see X509TrustManager#getAcceptedIssuers()
		 */
		public X509Certificate[] getAcceptedIssuers() {
			return this.standardTrustManager.getAcceptedIssuers();
		}
	}
}
