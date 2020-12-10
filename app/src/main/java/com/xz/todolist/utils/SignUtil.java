package com.xz.todolist.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author: xz
 * @Date: 2020/11/24
 * 验签工具
 */
public class SignUtil {

    /**
     * （参数名升序排列)
     * 签名计算规则：MD5(client_secret+参数名+参数值+参数名+参数值+client_secret）
     *
     * @param map
     * @param secret
     * @return
     */
    public static String getSign(Map<String, Object> map, String secret) {
        StringBuilder sb = new StringBuilder();
        sb.append(secret);
        for (String key : map.keySet()) {
            sb.append(key);
            sb.append(map.get(key));
        }
        sb.append(secret);
        return sb.toString();
    }
}
