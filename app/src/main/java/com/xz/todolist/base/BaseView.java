package com.xz.todolist.base;

public interface BaseView {
    void showLoading(String tips, boolean cancel);
    void disLoading();
    void sToast(String text);
    void lToast(String text);
    void sDialog(String title, String msg);
    void dDialog();
}
