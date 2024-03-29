package com.xz.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.orhanobut.logger.Logger;
import com.xz.todolist.adapter.EventAdapter;
import com.xz.todolist.api.TodoApi;
import com.xz.todolist.base.BaseActivity;
import com.xz.todolist.base.OnItemClickListener;
import com.xz.todolist.content.Local;
import com.xz.todolist.entity.Event;
import com.xz.todolist.entity.PagingResult;
import com.xz.todolist.network.NetUtil;
import com.xz.todolist.ui.AddActivity;
import com.xz.todolist.utils.ScreenUtil;
import com.xz.utils.appUtils.SpacesItemDecorationUtil;
import com.xz.xlogin.LoginActivity;
import com.xz.xlogin.XLogin;
import com.xz.xlogin.util.TipsDialogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

import static java.lang.String.format;

public class MainActivity extends BaseActivity {

	@BindView(R.id.recycle_event)
	RecyclerView recycleEvent;
	@BindView(R.id.tv_todo)
	TextView tvTodo;
	@BindView(R.id.tv_undone)
	TextView tvUndone;
	@BindView(R.id.icon_tips)
	ImageView iconTips;
	@BindView(R.id.icon_add)
	ImageView iconAdd;
	@BindView(R.id.tv_add)
	TextView tvAdd;
	@BindView(R.id.fab_add)
	FloatingActionButton fabAdd;
	@BindView(R.id.fab_top)
	FloatingActionButton fabTop;
	@BindView(R.id.scrollview)
	NestedScrollView scrollview;
	@BindView(R.id.refresh_layout)
	TwinklingRefreshLayout refreshLayout;

	private EventAdapter eventAdapter;

	private TodoApi todoApi;
	//分页控制
	private int size = 50;
	private int page = 1;
	private int totalPages;
	private long totalElements;

	@Override
	public boolean homeAsUpEnabled() {
		return true;
	}

	@Override
	public int getLayoutResource() {
		return R.layout.activity_main;
	}

	@Override
	public void initData() {
		hideActionBar();
		changeStatusBarTextColor();
		// TODO: 2021/1/27 XLogin读取token的方法
		//Local.token = ???
		initView();
		todoApi = TodoApi.getInstance();
		initRecycler();
		refreshLayout.startRefresh();
	}


	private void test() {
		List<Event> list = new ArrayList<>();
		Event event = new Event();
		event.setShortTitle("什么是安卓手机思考");
		event.setContent("安卓手机指的是有Android这个操作系统的手机 \nAndroid是一种以Linux为基础的开放源代码操作系统,主要使用于便携设备。目前尚未有统一中文名称,中国大陆地区较多人使用“安卓”或“安致”。Android操作系统最初由Andy Rubin开发,最初主要支持手机");
		event.setDone(false);
		event.setRemindTime(new Date(1610887344000L));
		list.add(event);
		Event event1 = new Event();
		event1.setShortTitle("明天穿什么思考");
		event1.setContent("iOS是由苹果公司开发的移动操作系统。苹果公司最早于2007年1月9日的Macworld大会上公布这个系统，最初是设计给iPhone使用的，后来陆续套用到iPod touch、iPad上。iOS与苹果的macOS操作系统一样，属于类Unix的商业操作系统。");
		event1.setDone(true);
		list.add(event1);
		Event event2 = new Event();
		event2.setShortTitle("今天挺晒de，我也不知道是不是的。你说对吧！！！字数uu字数");
		event2.setContent("SIRI 是 Speech Interpretation & Recognition Interface 的首字母缩写，原义为语音识别接口，是苹果公司在iPhone、iPad、iPod Touch、HomePod等产品上应用的一个语音助手，利用Siri用户可以通过手机读短信、介绍餐厅、询问天气、语音设置闹钟等。");
		event2.setDone(true);
		event2.setRemindTime(new Date(1610628323000L));
		list.add(event2);
		Event event3 = new Event();
		event3.setShortTitle("华为");
		event3.setContent("华为技术有限公司，成立于1987年，总部位于广东省深圳市龙岗区。 [1]  华为是全球领先的信息与通信技术（ICT）解决方案供应商，专注于ICT领域，坚持稳健经营、持续创新、开放合作，在电信运营商、企业、终端和云计算等领域构筑了端到端的解决方案优势，为运营商客户、企业客户和消费者提供有竞争力的ICT解决方案、产品和服务，并致力于实现未来信息社会、构建更美好的全联接世界。2013年，华为首超全球第一大电信设备商爱立信，排名《财富》世界500强第315位。华为的产品和解决方案已经应用于全球170多个国家，服务全球运营商50强中的45家及全球1/3的人口。");
		event3.setDone(true);
		list.add(event3);
		Event event4 = new Event();
		event4.setShortTitle("小米");
		event4.setContent("小米科技有限责任公司成立于2010年3月3日 [1]  ，是一家专注于智能硬件和电子产品研发的全球化移动互联网企业 [2]  ，同时也是一家专注于高端智能手机、互联网电视及智能家居生态链建设的创新型科技企业。 [3]  小米公司创造了用互联网模式开发手机操作系统、发烧友参与开发改进的模式。2018年7月9日在香港交易所主板挂牌上市，成为港交所上市制度改革后首家采用不同投票权架构的上市企业。");
		list.add(event4);
		Event event5 = new Event();
		event5.setShortTitle("魅族");
		event5.setContent("魅族不错");
		list.add(event5);
		Event event6 = new Event();
		event6.setShortTitle("一加");
		event6.setContent("一加更好了\n对的");
		list.add(event6);
		refresh(list, true);
	}

	private void initView() {
		fabAdd.hide();
		fabTop.hide();
		scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
			@Override
			public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
				showOrHideFab();

			}
		});
		refreshLayout.setHeaderView(new SinaRefreshView(mContext));
		refreshLayout.setBottomView(new LoadingView(mContext));
		refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
			@Override
			public void onRefresh(TwinklingRefreshLayout refreshLayout) {
				//下拉刷新
				page = 1;
				refreshLayout.setEnableLoadmore(true);
				getEvent(false, true);
			}

			@Override
			public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
				//上拉加载
				if (page >= totalPages) {
					sToast("已加载全部啦");
					refreshLayout.finishLoadmore();
					refreshLayout.setEnableLoadmore(false);
					return;
				} else {
					refreshLayout.setEnableLoadmore(true);
					page++;
				}
				getEvent(false, false);

			}

		});
	}


	private void initRecycler() {
		eventAdapter = new EventAdapter(mContext);
		recycleEvent.setLayoutManager(new LinearLayoutManager(mContext));
		recycleEvent.addItemDecoration(new SpacesItemDecorationUtil.SpacesItemDecorationVertical(20));
		recycleEvent.setAdapter(eventAdapter);
		eventAdapter.setOnItemClickListener(new OnItemClickListener<Event>() {
			@Override
			public void onItemClick(View view, int position, Event model) {
				sToast("点击");
			}

			@Override
			public void onItemLongClick(View view, int position, Event model) {
				sToast("长按");
			}
		});
	}


	@OnClick({R.id.fab_add, R.id.fab_top, R.id.icon_add})
	public void onViewClick(View view) {
		switch (view.getId()) {

			case R.id.fab_top:
				scrollview.scrollTo(0, 0);
				showOrHideFab();
				break;
			case R.id.fab_add:
			case R.id.icon_add:
				startActivity(new Intent(mContext, AddActivity.class));
				break;
		}
	}

	/**
	 * 更新事件列表，同时刷新还有几件事的控件
	 *
	 * @param isClean 是否需要清空之前的列表数据
	 */
	private void refresh(List<Event> list, boolean isClean) {
		if (isClean) {
			eventAdapter.superRefresh(list);
		} else {
			eventAdapter.refresh(list);
		}
		if (list == null || list.size() == 0) {
			tvUndone.setText("暂无待办事件");
			iconTips.setVisibility(View.INVISIBLE);
		} else {
			tvUndone.setText(format("还有%s件事情未完成", totalElements));
			iconTips.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 显示或隐藏浮动按钮
	 */
	private void showOrHideFab() {
		if (ScreenUtil.isOnTheScreen(MainActivity.this, tvAdd)) {
			//add控件在显示，不显示浮动按钮
			if (fabAdd.isShown()) {
				fabAdd.hide();
				fabTop.hide();
				fabAdd.setAnimation(AnimationUtils.makeOutAnimation(mContext, true));
				fabTop.setAnimation(AnimationUtils.makeOutAnimation(mContext, true));
			}
		} else {
			//add控件已被划过不在屏幕范围内，显示浮动按钮
			if (!fabAdd.isShown()) {
				fabAdd.show();
				fabTop.show();
				fabAdd.setAnimation(AnimationUtils.makeInAnimation(mContext, false));
				fabTop.setAnimation(AnimationUtils.makeInAnimation(mContext, false));
			}
		}
	}


	/**
	 * 获取事件数据
	 * @param  isDone 完成或未完成的事件
	 */
	private void getEvent(boolean isDone, boolean isClean) {
		todoApi.getEvent(isDone, page, size, new NetUtil.ResultCallback<PagingResult<List<Event>>>() {
			@Override
			public void onError(Request request, Exception e) {
				e.printStackTrace();
				refreshLayout.finishRefreshing();
				refreshLayout.finishLoadmore();
				TipsDialogUtil.badNetDialog(mContext);
			}

			@Override
			public void onResponse(PagingResult<List<Event>> response) {
				refreshLayout.finishRefreshing();
				refreshLayout.finishLoadmore();
				if (response.getCode() == 2) {
					//登录过期了
					loginActivity();
				} else if (response.getCode() == -1) {
					//未知错误
					TipsDialogUtil.errorDialog(mContext);
				} else if (response.getCode() == 1) {
					//成功
					totalPages = response.getTotalPages();
					totalElements = response.getTotalElements();
					if (response.getData() != null) {
						refresh(response.getData(), isClean);
					}
				}
			}

		});

	}

	/**
	 * 打开登录页面
	 */
	private void loginActivity() {
		XLogin.login(this);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == XLogin.REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				if (data == null) {
					sToast("登录失败，请稍后重试");
					return;
				}
				Local.token = data.getStringExtra(XLogin.EXTRA_TOKEN);
				//刷新数据列表
				refreshLayout.startRefresh();

			} else if (resultCode == RESULT_CANCELED) {
				//登录被终止
			}
		}
	}
}
