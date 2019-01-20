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
    Spinner kat = null;
    Spinner tez = null;
    Spinner t = null;
    Activity fa;
   ImageView  vv;

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
        if (v == play) {
            Intent in = new Intent(getApplicationContext(), ShowQuestionsActivity.class);

            kat = (Spinner) findViewById(R.id.kategorija);
            t = (Spinner) findViewById(R.id.tip);
            tez = (Spinner) findViewById(R.id.tezavnost);
            Bundle extra = new Bundle();
            String url = null;
            String kategorija = null;
            String tip = null;
            String tezavnost = null;
            kategorija = kat.getSelectedItem().toString().toLowerCase().replace(" ", "");
            tip = t.getSelectedItem().toString().toLowerCase().replace(" ", "");
            tezavnost = tez.getSelectedItem().toString().toLowerCase().replace(" ", "");
            url = "serverip:serverport/api/game/custom/query?tezavnost=" + tezavnost + "&kategorija=" + kategorija + "&tip_vprasanj=" + tip;
            extra.putString("url", url);

            in.putExtras(extra);
           // startActivity(in);

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

            new CountDownTimer(3000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub

                    dialog.dismiss();


                }
            }.start();


        } else if (v == skip) {

            Intent in = new Intent(getApplicationContext(), ShowQuestionsActivity.class);

            kat = (Spinner) findViewById(R.id.kategorija);
            t = (Spinner) findViewById(R.id.tip);
            tez = (Spinner) findViewById(R.id.tezavnost);
            Bundle extra = new Bundle();
            String url = null;
            String kategorija = null;
            String tip = null;
            String tezavnost = null;
            kategorija = kat.getSelectedItem().toString().toLowerCase().replace(" ", "");
            tip = t.getSelectedItem().toString().toLowerCase().replace(" ", "");
            tezavnost = tez.getSelectedItem().toString().toLowerCase().replace(" ", "");
            url = "serverip:serverport/api/game/regular/get_questions";
            extra.putString("url", url);
            in.putExtras(extra);
            startActivity(in);
        }
        /*final Dialog dialog = new Dialog(this);// add here your class name
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);//add your own xml with defied with and height of videoview
        dialog.setTitle("GET READY");
        ImageView ivLoader = (ImageView) dialog.findViewById(R.id.imageView);
        ivLoader.setBackgroundResource(R.drawable.blinka);
        AnimationDrawable frameAnimation = (AnimationDrawable) ivLoader.getBackground();
        frameAnimation.start();

*/
    }

    public void openDialog() {
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

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

                dialog.dismiss();

            }
        }.start();


    }

       /* final Dialog dialog = new Dialog(this);// add here your class name
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);//add your own xml with defied with and height of videoview
        dialog.setTitle("GET READY");
        ImageView ivLoader = (ImageView) dialog.findViewById(R.id.imageView);
        ivLoader.setBackgroundResource(R.drawable.blinka);
        AnimationDrawable frameAnimation = (AnimationDrawable) ivLoader.getBackground();
        frameAnimation.start();
    }*/
}



