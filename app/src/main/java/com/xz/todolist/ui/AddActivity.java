package com.xz.todolist.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xz.todolist.R;
import com.xz.todolist.base.BaseActivity;

public class AddActivity extends BaseActivity {


	@Override
	public boolean homeAsUpEnabled() {
		return true;
	}

	@Override
	public int getLayoutResource() {
		return R.layout.activity_add;
	}

	@Override
	public void initData() {
		hideActionBar();
		changeStatusBarTextColor();
		initView();
	}

	private void initView() {

	}
}
