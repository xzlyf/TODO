package com.xz.todolist.network;

import com.xz.todolist.content.Local;

import rxhttp.wrapper.annotation.DefaultDomain;

public class Url {
    @DefaultDomain //设置为默认域名
    public static String baseUrl = Local.SERVER;
}