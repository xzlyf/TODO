package com.xz.todolist.ui;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.orhanobut.logger.Logger;
import com.xz.todolist.R;

import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {


	private Toolbar toolbar;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		ButterKnife.bind(this);
		initView();
	}

	private void initView() {
		//=========view初始化=============
		toolbar = findViewById(R.id.toolbar);
		webView = findViewById(R.id.web_view);
		toolbar.setTitle("");
		//========toolbar================
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//===========webview=============
		WebSettings webSettings = webView.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
		webSettings.setJavaScriptEnabled(true);//设置能够解析Javascript
		webSettings.setDomStorageEnabled(true);//设置适应Html5
		webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
		webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

		// 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}


		webView.setWebViewClient(new WebViewClient() {
			//不调用系统浏览器
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			//ssl证书问题
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				//Logger.w("ssl:" + error.toString());
				handler.proceed();    //  忽略SSL证书错误，继续加载页面
				// handler.cancel();      //停止加载问题页面
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			//加载进度
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				//get the newProgress and refresh progress bar
				Logger.w("加载进度：" + newProgress);
			}

			//获取网页标题
			@Override
			public void onReceivedTitle(WebView view, String title) {
				setTitle(title);
			}

		});


		//待完成====封装成工具
		webView.loadUrl("https://192.168.1.66/privacy");
		//webView.loadUrl("https://192.168.1.66/privacy");
		//webView.loadUrl("https://www.bilibili.com/");


	}


	/**
	 * 设置标题
	 *
	 * @param txt
	 */
	private void setTitle(String txt) {
		toolbar.setTitle(txt);
	}


	@Override
	public void onBackPressed() {
		Logger.d("是否能返回：" + webView.canGoBack());
		if (webView.canGoBack()) {
			webView.goBack();
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		webView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		webView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (webView != null) {
			webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
			webView.clearHistory();

			((ViewGroup) webView.getParent()).removeView(webView);
			webView.destroy();
			webView = null;
		}


	}
}
