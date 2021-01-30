package com.xz.todolist.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;
import com.xz.todolist.content.Local;
import com.xz.todolist.entity.ApiResult;
import com.xz.todolist.entity.CreateEvent;
import com.xz.todolist.entity.Event;
import com.xz.todolist.entity.PagingResult;
import com.xz.todolist.network.NetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/17
 */
public class TodoApi {
	private static final String TAG = TodoApi.class.getName();
	private static TodoApi instance;
	private NetUtil netUtil;

	private TodoApi() {
		netUtil = NetUtil.getInstance();
	}

	public static TodoApi getInstance() {
		if (instance == null) {
			synchronized (TodoApi.class) {
				if (instance == null) {
					instance = new TodoApi();
				}
			}
		}
		return instance;
	}

	/**
	 * 获取事件
	 *
	 * @param done true /false已完成或未完成
	 */
	public void getEvent(boolean done, int page, int size, NetUtil.ResultCallback<PagingResult<List<Event>>> callback) {
		Map<String, Object> params = new HashMap<>();
		params.put("token", Local.token);
		params.put("done", done);
		params.put("page", page);
		params.put("size", size);
		netUtil.get(Local.BASE_URL_TODO + Local.GET_EVENT, params, callback);
	}

	/**
	 * 创建事件
	 */
	public void createEvent(CreateEvent event) {
		Map<String, Object> params = new HashMap<>();
		params.put("token", Local.token);
		netUtil.postJson(Local.BASE_URL_TODO + Local.POST_CREATE_EVENT, JSONObject.toJSONString(event), params
				, new NetUtil.ResultCallback<String>() {
					@Override
					public void onError(Request request, Exception e) {
						Logger.e(e.getMessage());
						e.printStackTrace();
					}

					@Override
					public void onResponse(String response) {
						Logger.w(response);
					}

				});
	}


}
