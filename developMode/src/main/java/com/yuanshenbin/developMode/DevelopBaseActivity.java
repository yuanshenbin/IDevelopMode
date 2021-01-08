package com.yuanshenbin.developMode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * author : yuanshenbin
 * time   : 2018/8/30
 * desc   :
 */

abstract class DevelopBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DevelopMode.getInstance().add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DevelopMode.getInstance().remove(this);
    }
}
