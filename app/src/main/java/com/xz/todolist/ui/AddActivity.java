package com.xz.todolist.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xz.todolist.R;
import com.xz.todolist.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddActivity extends BaseActivity {


	@BindView(R.id.ic_back)
	ImageView icBack;
	@BindView(R.id.ic_clock)
	ImageView icClock;
	@BindView(R.id.ic_done)
	ImageView icDone;

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
		changeStatusBarTextColor();
		initView();
	}

	private void initView() {
	}


	@OnClick({R.id.ic_done, R.id.ic_back, R.id.ic_clock})
	public void onViewClick(View view) {
		switch (view.getId()) {
			case R.id.ic_clock:
				break;
			case R.id.ic_back:
			case R.id.ic_done:
				saveOnExit();
				break;
		}
	}


	/**
	 * 退出自动保存
	 */
	private void saveOnExit() {

		finish();

	}
}
