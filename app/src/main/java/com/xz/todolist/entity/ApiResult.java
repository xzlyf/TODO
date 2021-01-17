package com.xz.todolist.entity;

/**
 * 1.0 服务端返回实体标准
 */
public class ApiResult<T> {
	private int code;
	private String status;
	private T data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
