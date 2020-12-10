package com.xz.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xz.todolist.base.BaseActivity;
import com.xz.todolist.ui.LoginActivity;
import com.xz.utils.netUtils.OkHttpClientManager;

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

    public void test(View view) {
        startActivity(
                new Intent(MainActivity.this,
                        LoginActivity.class));
    }
}
