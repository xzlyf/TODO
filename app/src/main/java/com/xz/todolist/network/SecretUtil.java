package com.xz.todolist.network;

import com.xz.todolist.content.Local;
import com.xz.utils.encodUtils.MD5Util;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/6
 */
public class SecretUtil {

	/**
	 * 1.0 sign加密规则
	 *
	 * @param timestamp 时间戳
	 */
	public static String getSecret(long timestamp) {
		return MD5Util.getMD5(Local.appId + Local.appSecret + timestamp + Local.version);
	}
}
