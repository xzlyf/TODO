package com.xz.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xz.todolist.base.BaseActivity;

public class MainActivity extends BaseActivity {

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

    }
}
