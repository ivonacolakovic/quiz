package com.projekt.kviz;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class MainActivity extends Activity implements View.OnClickListener {
    Button playButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = (Button) findViewById(R.id.playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), FirstActivity.class);

                Bundle extra = new Bundle();

                in.putExtras(extra);
                startActivity(in);


            }

        });


    }


    @Override
    public void onClick(View v) {
        if(v == playButton) {
            Intent in = new Intent(getApplicationContext(), FirstActivity.class);

            Bundle extra = new Bundle();

            in.putExtras(extra);
            startActivity(in);
        }
    }
}

