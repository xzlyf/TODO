package com.xz.todolist.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.bean.DateType;
import com.orhanobut.logger.Logger;
import com.xz.todolist.R;
import com.xz.todolist.adapter.EditFunctionAdapter;
import com.xz.todolist.api.TodoApi;
import com.xz.todolist.base.BaseActivity;
import com.xz.todolist.base.OnItemClickListener;
import com.xz.todolist.entity.CreateEvent;
import com.xz.todolist.entity.EditFunction;
import com.xz.utils.appUtils.SpacesItemDecorationUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

public class AddActivity extends BaseActivity {


	@BindView(R.id.ic_back)
	ImageView icBack;
	@BindView(R.id.ic_clock)
	ImageView icClock;
	@BindView(R.id.ic_done)
	ImageView icDone;
	@BindView(R.id.et_short)
	EditText etShort;
	@BindView(R.id.rich_editor)
	RichEditor richEditor;
	@BindView(R.id.function_recycler)
	RecyclerView functionRecycler;

	EditFunctionAdapter adapter;
	TodoApi todoApi;

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
		todoApi = TodoApi.getInstance();
		initView();
		initFunctionRecycler();
	}


	private void initView() {

	}

	private void initFunctionRecycler() {
		// TODO: 2021/1/28 把RecyclerView改为tab方式
		// TODO: 2021/1/29  优化recyclerview 的点击事件
		adapter = new EditFunctionAdapter(mContext);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
		linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
		functionRecycler.setLayoutManager(linearLayoutManager);
		functionRecycler.setAdapter(adapter);
		functionRecycler.addItemDecoration(new SpacesItemDecorationUtil.SpacesItemDecorationHorizontal(10));
		LinearSnapHelper snapHelper = new LinearSnapHelper();
		snapHelper.attachToRecyclerView(functionRecycler);
		adapter.setOnItemClickListener(new OnItemClickListener<EditFunction>() {
			@Override
			public void onItemClick(View view, int position, EditFunction model) {
				Logger.w(position + "==" + model.getFunctionName());
				switch (position) {
					case 0:
						richEditor.undo();
						break;
					case 1:
						richEditor.redo();
						break;
					case 2:
						richEditor.setBold();
						break;
					case 3:
						richEditor.setItalic();
						break;
					case 4:
						richEditor.setUnderline();
						break;
					case 5:
						richEditor.setStrikeThrough();
						break;
					case 6:
						richEditor.setNumbers();
						break;
				}
			}

			@Override
			public void onItemLongClick(View view, int position, EditFunction model) {
				Logger.w(position + "==长按==" + model.getFunctionName());
			}
		});

		List<EditFunction> list = new ArrayList<>();
		list.add(new EditFunction("撤回", R.mipmap.ic_undo));
		list.add(new EditFunction("重做", R.mipmap.ic_redo));
		list.add(new EditFunction("加粗", R.mipmap.ic_font_bold));
		list.add(new EditFunction("斜体", R.mipmap.ic_font_italic));
		list.add(new EditFunction("下划线", R.mipmap.ic_font_underline));
		list.add(new EditFunction("删除线", R.mipmap.ic_font_strickout));
		list.add(new EditFunction("列表", R.mipmap.ic_list));

		adapter.refresh(list);
	}


	@OnClick({R.id.ic_done, R.id.ic_back, R.id.ic_clock})
	public void onViewClick(View view) {
		switch (view.getId()) {
			case R.id.ic_clock:
				DatePickDialog dialog = new DatePickDialog(this);
				//设置上下年分限制
				dialog.setYearLimt(5);
				//设置标题
				dialog.setTitle("选择时间");
				//设置类型
				dialog.setType(DateType.TYPE_ALL);
				//设置消息体的显示格式，日期格式
				dialog.setMessageFormat("yyyy-MM-dd HH:mm");
				//设置选择回调
				dialog.setOnChangeLisener(null);
				//设置点击确定按钮回调
				dialog.setOnSureLisener(null);
				dialog.show();
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
		// TODO: 2021/1/30 保存是走本地还是远程
		CreateEvent event = new CreateEvent();
		event.setShortTitle("测试标题20210130");
		event.setContent("测试内容20210130");
		event.setDone(false);
		event.setRemindTime(new Date(System.currentTimeMillis()));
		todoApi.createEvent(event);
		finish();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: add setContentView(...) invocation
		ButterKnife.bind(this);
	}
}
