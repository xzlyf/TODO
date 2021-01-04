package com.xz.todolist.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.orhanobut.logger.Logger;
import com.xz.todolist.R;
import com.xz.todolist.api.UserApi;
import com.xz.todolist.base.BaseActivity;
import com.xz.todolist.network.NetUtil;
import com.xz.todolist.ui.fragment.LoginFragment;
import com.xz.todolist.ui.fragment.RegisterFragment;
import com.xz.todolist.utils.ColorUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;

public class LoginActivity extends BaseActivity {

	@BindView(R.id.tv_login)
	TextView tvLogin;
	@BindView(R.id.tv_register)
	TextView tvRegister;
	@BindView(R.id.btn_submit)
	TextView btnSubmit;
	@BindView(R.id.main_fragment)
	FrameLayout mainFragment;
	@BindView(R.id.box_protocol)
	CheckBox boxProtocol;
	@BindView(R.id.tv_protocol)
	TextView tvProtocol;
	private FragmentTransaction transaction;
	private FragmentManager manager;
	private LoginFragment loginFragment;
	private RegisterFragment registerFragment;

	private UserApi userApi;
	//0 登录  1注册
	private int type = 0;

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

		userApi = UserApi.getInstance();
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
	 * 注册与使用协议
	 */
	private void getProtocol() {
		//tvProtocol.setText(Html.fromHtml("《<a href=\"https://www.baidu.com\">用户注册协议</a>》"));
		tvProtocol.setText(Html.fromHtml("《<font color=\"" + ColorUtil.int2Hex(getColor(R.color.colorAccent)) + "\">注册与使用协议</font>》"));
	}

	@OnClick({R.id.tv_protocol, R.id.tv_login, R.id.tv_register, R.id.btn_submit})
	public void onViewClick(View view) {
		switch (view.getId()) {
			case R.id.tv_protocol:
				sToast("用户协议...");
				break;
			case R.id.tv_login:
				showFragment(loginFragment);
				tvLogin.setBackground(getDrawable(R.drawable.share_text_bottom_line));
				tvRegister.setBackground(getDrawable(R.drawable.share_text_bottom_line_hide));
				type = 0;
				btnSubmit.setText("登录");
				break;
			case R.id.tv_register:
				showFragment(registerFragment);
				tvRegister.setBackground(getDrawable(R.drawable.share_text_bottom_line));
				tvLogin.setBackground(getDrawable(R.drawable.share_text_bottom_line_hide));
				type = 1;
				btnSubmit.setText("注册");
				break;
			case R.id.btn_submit:
				if (type == 0) {
					//登录
					sToast("登录");

				} else if (type == 1) {
					//注册
					phoneRegister();
				}
				break;
		}
	}


	/**
	 * 手机号注册
	 */
	private void phoneRegister() {
		String phone = registerFragment.getPhone();
		String pwd = registerFragment.getPwd();
		if (phone.equals("") || pwd.equals("")) {
			return;
		}
		showLoading("正在加载...", false, null);
		userApi.phoneRegister(phone, pwd, new NetUtil.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				disLoading();
			}

			@Override
			public void onResponse(String response) {
				disLoading();
				Logger.w("测试:" + response);
			}
		});
	}


}
