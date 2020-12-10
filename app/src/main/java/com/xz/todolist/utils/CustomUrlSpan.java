package com.xz.todolist.utils;

import android.content.Context;
import android.content.Intent;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

/**
 * 文本框TextView超链接拦截
 */
public class CustomUrlSpan extends ClickableSpan {

	private Context context;
	private String url;

	public CustomUrlSpan(String url) {
		this.url = url;
	}

	public CustomUrlSpan(Context context, String url) {
		this.context = context;
		this.url = url;
	}

	@Override
	public void onClick(View widget) {
		// 在这里可以做任何自己想要的处理
		//Intent intent = new Intent(context,WebViewActivity.class);
		//intent.putExtra(WebViewActivity.WEB_URL,url);
		//context.startActivity(intent);
	}
}