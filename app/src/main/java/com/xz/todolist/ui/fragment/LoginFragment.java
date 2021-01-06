package com.xz.todolist.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xz.todolist.R;
import com.xz.todolist.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/10
 */
public class LoginFragment extends BaseFragment {
	@BindView(R.id.tv_type)
	TextView tvType;
	@BindView(R.id.tv_forget)
	TextView tvForget;
	@BindView(R.id.et_user)
	EditText etUser;
	@BindView(R.id.et_pwd)
	EditText etPwd;

	//当前页面  1-账号登录 2-手机号登录 3-注册 4-忘记密码
	private int type = 1;

	@Override
	protected int getLayout() {
		return R.layout.fragment_login;
	}

	@Override
	protected void initView(View rootView) {
	}

	@Override
	protected void initDate(Context mContext) {
		etUser.setOnFocusChangeListener(focusChangeListener);
		etPwd.setOnFocusChangeListener(focusChangeListener);
	}

	@OnClick({R.id.tv_type, R.id.tv_forget})
	public void onViewClick(View view) {
		switch (view.getId()) {
			case R.id.tv_type:
				if (type == 1) {
					type = 2;
					tvType.setText("手机号登录");
				} else if (type == 2) {
					type = 1;
					tvType.setText("账号密码登录");
				}
				break;
			case R.id.tv_forget:
				break;

		}
	}

	private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				setNormalState(v);
			}
		}
	};

	/**
	 * 设置view默认状态
	 *
	 * @param view
	 */
	private void setNormalState(View view) {
		view.setBackgroundResource(R.drawable.select_edit);
	}

	/**
	 * 设置view错误状态
	 *
	 * @param view
	 */
	private void setErrorState(View view) {
		view.setBackgroundResource(R.drawable.select_edit_error);
	}

	/**
	 * /**
	 * --------开放接口---------
	 */
	public void setUserNo(String userNo) {
		etUser.setText(userNo);
		etPwd.setFocusable(true);
	}

	public String getUserNo() {
		String no = etUser.getText().toString().trim();
		if (no.equals("")) {
			setErrorState(etUser);
			return "";
		}
		return no;
	}

	public String getPwd() {
		String pwd = etPwd.getText().toString().trim();
		if (pwd.equals("")) {
			setErrorState(etPwd);
			return "";
		}
		return pwd;
	}

	public void setPwdError() {
		setErrorState(etPwd);
	}

}
