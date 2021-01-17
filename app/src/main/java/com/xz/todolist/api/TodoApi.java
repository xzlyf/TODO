package com.xz.todolist.api;

import com.xz.todolist.content.Local;
import com.xz.todolist.entity.ApiResult;
import com.xz.todolist.entity.Event;
import com.xz.todolist.entity.PagingResult;
import com.xz.todolist.network.NetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			synchronized (UserApi.class) {
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
	 * @param token 用户token
	 * @param done  true /false已完成或未完成
	 */
	public void getEvent(String token, boolean done, int page, int size, NetUtil.ResultCallback<ApiResult<List<Event>>> callback) {
		Map<String, Object> params = new HashMap<>();
		params.put("token", token);
		params.put("done", done);
		params.put("page", page);
		params.put("size", size);
		netUtil.get(Local.BASE_URL_TODO + Local.GET_EVENT, params, callback);
	}
}
