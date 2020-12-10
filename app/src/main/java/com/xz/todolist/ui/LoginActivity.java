package com.xz.todolist.ui;

import android.graphics.Color;

import com.xz.todolist.R;
import com.xz.todolist.base.BaseActivity;

public class LoginActivity extends BaseActivity {

	@Override
	public boolean homeAsUpEnabled() {
		return true;
	}

	@Override
	public int getLayoutResource() {
		return R.layout.activity_login;
	}

	@Override
	public void initData() {
	    setTitle("");
		setActionBarColor(Color.TRANSPARENT);
		setActionBarTitleColor(getColor(R.color.colorPrimary));
		setActionBarBackColor(getColor(R.color.colorPrimary));
		changeStatusBarTextColor();

	}
}
