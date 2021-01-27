package com.xz.todolist.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
	@BindView(R.id.et_short)
	EditText etShort;
	@BindView(R.id.ic_bold)
	ImageView icBold;
	@BindView(R.id.ic_italic)
	ImageView icItalic;
	@BindView(R.id.ic_underline)
	ImageView icUnderline;
	@BindView(R.id.ic_strickout)
	ImageView icStrickout;

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


	@OnClick({R.id.ic_done, R.id.ic_back, R.id.ic_clock
			, R.id.ic_bold, R.id.ic_italic, R.id.ic_underline, R.id.ic_strickout})
	public void onViewClick(View view) {
		switch (view.getId()) {
			case R.id.ic_clock:
				break;
			case R.id.ic_back:
			case R.id.ic_done:
				saveOnExit();
				break;
			case R.id.ic_bold://加粗
				break;
			case R.id.ic_italic://斜体
				break;
			case R.id.ic_underline://下划线
				break;
			case R.id.ic_strickout://删除线
				break;

		}
	}


	/**
	 * 退出自动保存
	 */
	private void saveOnExit() {

		finish();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: add setContentView(...) invocation
		ButterKnife.bind(this);
	}
}
