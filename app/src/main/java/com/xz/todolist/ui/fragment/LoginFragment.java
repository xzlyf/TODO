package com.xz.todolist.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xz.todolist.R;
import com.xz.todolist.base.BaseFragment;

import butterknife.OnClick;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/10
 */
public class LoginFragment extends BaseFragment {
	//@BindView(R.id.tv_type)
	//TextView tvType;
	//@BindView(R.id.tv_forget)
	//TextView tvForget;
	//@BindView(R.id.et_user)
	//EditText etUser;
	//@BindView(R.id.et_pwd)
	//EditText etPwd;

	//当前页面  1-账号登录 2-手机号登录 3-注册 4-忘记密码
	private int type = 1;
	private EditText etUser;
	private EditText etPwd;
	private TextView tvType;
	private TextView tvForget;

	@Override
	protected int getLayout() {
		return R.layout.fragment_login;
	}

	@Override
	protected void initView(View rootView) {

		etUser = rootView.findViewById(R.id.et_user);
		etPwd = rootView.findViewById(R.id.et_pwd);
		tvType = rootView.findViewById(R.id.tv_type);
		tvForget = rootView.findViewById(R.id.tv_forget);
	}

	@Override
	protected void initDate(Context mContext) {

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

	/**
	 * 设定账号
	 */
	public void setUserNo(String userNo) {
		etUser.setText(userNo);
		etPwd.setFocusable(true);
	}

}
