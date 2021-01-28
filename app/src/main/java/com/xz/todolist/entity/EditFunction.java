package com.xz.todolist.entity;

import lombok.Data;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/28
 */
@Data
public class EditFunction {
	private String functionName;
	private int functionImg;

	public EditFunction(){

	}
	public EditFunction(String functionName, int functionImg) {
		this.functionName = functionName;
		this.functionImg = functionImg;
	}

}
