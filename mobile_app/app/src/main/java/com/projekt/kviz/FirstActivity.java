package com.projekt.kviz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import static java.lang.Thread.sleep;

public class FirstActivity extends Activity implements View.OnClickListener {

    ImageButton skip = null;
    ImageButton play = null;
    Button yes = null;
    Button no = null;
    Spinner kat = null;
    Spinner tez = null;
    Spinner tip_vpr = null;
    Activity fa;
   ImageView  vv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        play = (ImageButton) findViewById(R.id.imageButton);
        skip = (ImageButton) findViewById(R.id.imageButton1);
        play.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == play) {
            final Intent in = new Intent(getApplicationContext(), ShowQuestionsActivity.class);
            String baseUrl= "http://164.8.207.131:5555/api/game/";

            kat = (Spinner) findViewById(R.id.kategorija);
            tip_vpr = (Spinner) findViewById(R.id.tip);
            tez = (Spinner) findViewById(R.id.tezavnost);


            Bundle extra = new Bundle();
            String kategorija = null;
            String tip = null;
            String tezavnost = null;
            kategorija = kat.getSelectedItem().toString();
            tezavnost = tez.getSelectedItem().toString().toLowerCase();
            if (tip_vpr.getSelectedItem().toString().toLowerCase().equals("multiple choice")){
                tip = "multiple";
            } else if (tip_vpr.getSelectedItem().toString().toLowerCase().equals("true/false")){
                tip = "boolean";
            }
            if (kat.getSelectedItem().toString().equals("All categories") && tez.getSelectedItem().toString().equals("All difficulties")
                    && tip_vpr.getSelectedItem().toString().equals("All types")){
                baseUrl += "regular/get_questions";
            } else {
                baseUrl += "custom/query?";
                boolean and = false;
                if (!tezavnost.contains("all")){
                    if (and){
                        baseUrl += "&";
                    }
                    baseUrl += "tezavnost="+tezavnost;
                    and = true;
                }
                if (!kategorija.contains("All")){
                    if (and){
                        baseUrl += "&";
                    }
                    baseUrl += "kategorija="+kategorija;
                    and = true;
                }
                if (tip != null && !tip.contains("all")){
                    if (and){
                        baseUrl += "&";
                    }
                    baseUrl += "tip_vprasanj="+tip;
                    and = true;
                }
            }
            extra.putString("url", baseUrl);
            Log.d("URL", baseUrl);
            in.putExtras(extra);


            final Dialog dialog = new Dialog(FirstActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog); // change to dialog.setContentView
            //dialog.getWindow().setBackgroundDrawable(newColorDrawable(Color.TRANSPARENT));

            vv = (ImageView)dialog.findViewById(R.id.image);

            vv.setBackgroundResource(R.drawable.blinka);// Drawable file instead of anim move same file from anim folder to Drawable before use..

           final AnimationDrawable animcon = (AnimationDrawable) vv.getBackground(); // instead of getDrawable() use this
            dialog.setCancelable(true);

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    animcon.start();
                }
            });
            dialog.show();
            yes = dialog.findViewById(R.id.gumb);
            no = dialog.findViewById(R.id.gumbek);


           final CountDownTimer cdt = new CountDownTimer(4000, 1000) {



                @Override
                public void onTick(long millisUntilFinished) {

                }
                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub

                    dialog.dismiss();
                    startActivity(in);


                }
            }.start();

            yes.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(in);
                    cdt.cancel();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    cdt.cancel();
                }
            });

        }




    }


}



