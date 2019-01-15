package com.projekt.kviz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewpager;
    private TabLayout tablayout;
    /*private int[] tabIcons = {
            R.drawable.ic_tab_favourite ,
            R.drawable.ic_tab_call ,
            R.drawable.ic_tab_contacts};*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewpager);

        tablayout = (TabLayout) findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewpager);

        //setupTabIcons();

    }

    /*private void setupTabIcons() {
        tablayout.getTabAt(0).setIcon(tabIcons[0]);
        tablayout.getTabAt(1).setIcon(tabIcons[1]);
        // tablayout.getTabAt(2).setIcon(tabIcons[2]);
    }*/

    public void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment ( new FirstFragment() , " PLAY ");
        adapter.addFragment ( new SecondFragment(), " CHOOSE ");
        viewPager . setAdapter ( adapter );
    }
}
