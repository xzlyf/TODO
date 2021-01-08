package com.xz.todolist.ui;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xz.todolist.R;
import com.xz.todolist.api.UserApi;
import com.xz.todolist.base.BaseActivity;
import com.xz.todolist.content.Local;
import com.xz.todolist.network.NetUtil;
import com.xz.todolist.network.StatusEnum;
import com.xz.todolist.ui.fragment.LoginFragment;
import com.xz.todolist.ui.fragment.RegisterFragment;
import com.xz.todolist.utils.ColorUtil;
import com.xz.todolist.utils.TipsDialogUtil;
import com.xz.todolist.widget.TipsDialog;

import org.json.JSONException;
import org.json.JSONObject;

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
	private Fragment mContent;
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

		transaction = manager.beginTransaction();
		//设置默认默认fragment
		transaction.add(R.id.main_fragment, loginFragment).commit();
		mContent = loginFragment;

	}


	/**
	 * 不重新加载fragment的切换fragment
	 */
	private void showFragment(Fragment fragment) {
		if (mContent != fragment) {
			transaction = manager.beginTransaction();
			if (!fragment.isAdded()) {    // 先判断是否被add过
				transaction.hide(mContent).add(R.id.main_fragment, fragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(mContent).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
			}
			mContent = fragment;
		}
	}

	/**
	 * 注册与使用协议
	 */
	private void getProtocol() {
		tvProtocol.setText(Html.fromHtml("《<font color=\"" + ColorUtil.int2Hex(getColor(R.color.colorAccent)) + "\">注册与使用协议</font>》"));
	}

	@OnClick({R.id.tv_protocol, R.id.tv_login, R.id.tv_register, R.id.btn_submit})
	public void onViewClick(View view) {
		switch (view.getId()) {
			case R.id.tv_protocol:
				startActivity(new Intent(mContext, WebActivity.class));
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

				if (!boxProtocol.isChecked()) {
					sToast("请先阅读并同意");
					return;
				}
				if (type == 0) {
					//登录
					phoneLogin();
				} else if (type == 1) {
					//注册
					phoneRegister();
				}
				break;
		}
	}

	/**
	 * 手机号密码登录
	 */
	private void phoneLogin() {
		String phone = loginFragment.getUserNo();
		String pwd = loginFragment.getPwd();
		if (phone.equals("") || pwd.equals("")) {
			return;
		}
		showLoading("正在登录...");
		userApi.login(1, phone, pwd, new NetUtil.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				disLoading();
				TipsDialogUtil.badNetDialog(mContext);
			}

			@Override
			public void onResponse(String response) {
				disLoading();

				try {
					JSONObject obj = new JSONObject(response);
					int code = obj.optInt("code", -1);

					switch (code) {
						case 1:
							Local.token = obj.optString("data");
							sToast("登录成功");
							break;
						case 1049:
						case 1050:
						case 1051:
							TipsDialogUtil.commonDialog(mContext, StatusEnum.getValue(code));
							break;
						default:
							TipsDialogUtil.systemErrorDialog(mContext);
							break;
					}


				} catch (JSONException e) {
					e.printStackTrace();
					TipsDialogUtil.serverErrorDialog(mContext);
				}

			}
		});

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
				TipsDialogUtil.badNetDialog(mContext);

			}

			@Override
			public void onResponse(String response) {
				disLoading();

				try {
					JSONObject obj = new JSONObject(response);
					int code = obj.getInt("code");
					if (code == 1) {
						new TipsDialog.Builder(mContext)
								.setType(TipsDialog.STYLE_SUCCESS)
								.setTitle("马上就好啦")
								.setContent("账号注册成功")
								.setSubmitText("马上登录")
								.setOnSubmitListener(new TipsDialog.OnSubmitListener() {
									@Override
									public void onClick(TipsDialog dialog) {
										dialog.dismiss();
										onViewClick(tvLogin);
										loginFragment.setUserNo(phone);

									}
								})
								.build()
								.show();
					} else if (code == 1048) {
						new TipsDialog.Builder(mContext)
								.setType(TipsDialog.STYLE_WARN)
								.setTitle("Emmm...出错了")
								.setContent(obj.optString("status"))
								.setSubmitText("让我想想")
								.build()
								.show();
					} else {
						new TipsDialog.Builder(mContext)
								.setType(TipsDialog.STYLE_ERROR)
								.setTitle("Emmm...异常")
								.setContent("系统异常")
								.setSubmitText("那就稍后再试吧")
								.build()
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					new TipsDialog.Builder(mContext)
							.setType(TipsDialog.STYLE_ERROR)
							.setTitle("Emmm...奔溃了")
							.setContent("系统异常:" + e.getMessage())
							.setSubmitText("那就稍后再试吧")
							.build()
							.show();
				}

			}
		});
	}


}
