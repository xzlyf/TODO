package com.xz.todolist.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
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
import com.xz.utils.appUtils.TimeUtil;

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
	@BindView(R.id.remind_time)
	TextView remindTime;
	@BindView(R.id.clock_close)
	ImageView clockClose;
	@BindView(R.id.remind_view)
	LinearLayout remindView;

	private EditFunctionAdapter adapter;
	private TodoApi todoApi;
	private long remindDate = -1;

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
		remindView.setVisibility(View.GONE);
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


	@OnClick({R.id.ic_done, R.id.ic_back, R.id.ic_clock, R.id.clock_close, R.id.remind_view})
	public void onViewClick(View view) {
		switch (view.getId()) {
			case R.id.remind_view:
			case R.id.ic_clock:
				DatePickDialog dialog = new DatePickDialog(this);
				//设置上下年分限制
				dialog.setYearLimt(10);
				//设置标题
				dialog.setTitle("提醒时间");
				//设置类型
				dialog.setType(DateType.TYPE_ALL);
				//设置消息体的显示格式，日期格式
				dialog.setMessageFormat("yyyy-MM-dd HH:mm");
				//设置选择回调
				dialog.setOnChangeLisener(null);
				//设置点击确定按钮回调
				dialog.setOnSureLisener(new OnSureLisener() {
					@Override
					public void onSure(Date date) {
						remindDate = date.getTime();
						remindView.setVisibility(View.VISIBLE);
						remindTime.setText(TimeUtil.getSimMilliDate("yyyy年MM月dd日 HH:mm", remindDate));
					}
				});
				dialog.show();
				break;
			case R.id.ic_back:
			case R.id.ic_done:
				saveOnExit();
				break;
			case R.id.clock_close:
				remindDate = -1;
				remindTime.setText("xxx");
				remindView.setVisibility(View.GONE);
				break;

		}
	}

	@Override
	public void onBackPressed() {
		saveOnExit();
	}

	/**
	 * 退出自动保存
	 */
	private void saveOnExit() {
		String shortSt = etShort.getText().toString();
		String contentSt = richEditor.getHtml();
		if (shortSt.equals("") && contentSt == null) {
			finish();
			return;
		}
		if (shortSt.equals("")) {
			shortSt = contentSt.substring(0, 12);
		}
		CreateEvent event = new CreateEvent();
		event.setShortTitle(shortSt);
		event.setContent(contentSt);
		event.setDone(false);
		if (remindDate != -1) {
			event.setRemindTime(new Date(remindDate));
		}
		todoApi.createEvent(event);
		sToast("已保存");
		finish();

	}

}
