package com.projekt.kviz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;


class CustumDialogFragment extends Dialog {

       private TextView mTextView1;
        private Button mButton1, mButton2;

        public CustumDialogFragment(Context context) {
            super(context);


        }

       @Override
        protected void onCreate(Bundle savedInstanceState) {
           /* super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog);
            VideoView vv = (VideoView) findViewById(R.id.videoView);
            String urlPath = "android:resource://com.projekt.kviz/"+R.raw.video;
            Uri uri = Uri.parse(urlPath);
            vv.setVideoURI(uri);
            vv.requestFocus();
            vv.start();*/



        }

    }

   /* @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you ready?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Bundle bundle = getArguments();
                        String kategorija = bundle.getString("kategorija", "");
                        String tezavnost = bundle.getString("tezavnost", "");
                        String tip = bundle.getString("tip", "");


                        Intent intent = new Intent(getActivity(), ShowQuestionsActivity.class);
                        intent.putExtra("kategorija", kategorija);
                        intent.putExtra("priimek", tezavnost);
                        intent.putExtra("tip", tip);


                        startActivity(intent);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();*/



