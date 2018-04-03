package com.yunshu.baseandroid.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.yunshu.baseandroid.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new BlankFragment();
        fm.beginTransaction().replace(R.id.place_holder, fragment).commit();
    }
}
