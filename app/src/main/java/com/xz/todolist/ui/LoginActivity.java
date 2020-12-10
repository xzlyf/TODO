package com.xz.todolist.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xz.todolist.R;
import com.xz.todolist.base.BaseActivity;
import com.xz.todolist.ui.fragment.LoginFragment;
import com.xz.todolist.ui.fragment.RegisterFragment;
import com.xz.todolist.utils.ColorUtil;
import com.xz.todolist.utils.CustomUrlSpan;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

	@BindView(R.id.tv_title)
	TextView tvTitle;
	@BindView(R.id.btn_submit)
	TextView btnSubmit;
	@BindView(R.id.main_fragment)
	FrameLayout mainFragment;
	@BindView(R.id.tv_type)
	TextView tvType;
	@BindView(R.id.tv_forget)
	TextView tvForget;
	@BindView(R.id.box_protocol)
	CheckBox boxProtocol;
	@BindView(R.id.tv_protocol)
	TextView tvProtocol;
	private FragmentTransaction transaction;
	private FragmentManager manager;
	private LoginFragment loginFragment;
	private RegisterFragment registerFragment;
	//当前页面  1-登录 2-手机号登录 3-注册 4-忘记密码
	private int type = 1;

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

		getProtocol();
		initFragment();
		showFragment(loginFragment);

	}


	private void initFragment() {
		loginFragment = new LoginFragment();
		registerFragment = new RegisterFragment();
		//BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager());
		//adapter.addFragment(loginFragment, "登录");

		//FragmentManager fragmentManager = getSupportFragmentManager();
		//FragmentTransaction transaction = fragmentManager.beginTransaction();
		//transaction.add(R.id.main_fragment, loginFragment);
		//transaction.add(R.id.main_fragment, registerFragment);
		//transaction.show(loginFragment);
		//transaction.hide(registerFragment);
		//transaction.commitAllowingStateLoss();

		manager = getSupportFragmentManager();      //初始化管理者
	}

	private void showFragment(Fragment fragment) {
		transaction = manager.beginTransaction();        //开启事务
		transaction.replace(R.id.main_fragment, fragment);
		transaction.commit();//提交事务
	}

	/**
	 * 用户注册协议
	 */
	private void getProtocol() {
		//tvProtocol.setText(Html.fromHtml("《<a href=\"https://www.baidu.com\">用户注册协议</a>》"));
		tvProtocol.setText(Html.fromHtml("《<font color=\"" + ColorUtil.int2Hex(getColor(R.color.colorAccent)) + "\">用户注册协议</font>》"));
	}

	@OnClick({R.id.tv_forget, R.id.tv_type, R.id.tv_protocol})
	public void onViewClick(View view) {
		switch (view.getId()) {
			case R.id.tv_protocol:
				sToast("用户协议...");
				break;
			case R.id.tv_forget:
				break;
			case R.id.tv_type:
				if (type == 1) {
					showFragment(registerFragment);
					type = 2;
				} else if (type == 2) {
					showFragment(loginFragment);
					type = 1;
				}

				break;
		}
	}


}
