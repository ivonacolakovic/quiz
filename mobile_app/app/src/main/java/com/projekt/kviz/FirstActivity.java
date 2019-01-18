package com.projekt.kviz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton skip = null;
    ImageButton play = null;
    Spinner kat = null;
    Spinner tez = null;
    Spinner t = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        play = (ImageButton) findViewById(R.id.imageButton);
        skip = (ImageButton) findViewById(R.id.imageButton1);

        play.setOnClickListener(this);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == play){
            Intent in = new Intent(getApplicationContext(), ShowQuestionsActivity.class);

            kat = (Spinner) findViewById(R.id.kategorija);
            t = (Spinner) findViewById(R.id.tip);
            tez = (Spinner) findViewById(R.id.tezavnost);
            Bundle extra  = new Bundle();
            String url = null;
            String kategorija = null;
            String tip = null;
            String tezavnost = null;

            kategorija =  kat.getSelectedItem().toString().toLowerCase().replace(" ","");
            tip = t.getSelectedItem().toString().toLowerCase().replace(" ","");
            tezavnost = tez.getSelectedItem().toString().toLowerCase().replace(" ","");
            url = "serverip:serverport/api/game/custom/query?tezavnost="+tezavnost+"&kategorija="+kategorija+"&tip_vprasanj="+tip;
            extra.putString("url", url);
            in.putExtras(extra);
            startActivity(in);

        }
        else if (v == skip){
            Intent in = new Intent(getApplicationContext(), ShowQuestionsActivity.class);
            Bundle extra = new Bundle();
            String url = "serverip:serverport/api/game/regular/get_questions";
            extra.putString("url", url);
            in.putExtras(extra);
            startActivity(in);

        }

    }
}
