package com.projekt.kviz;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ShowQuestionsActivity extends AppCompatActivity {

    // public final String url = "https://opentdb.com/api.php?amount=50";
    TextView showData;
    ArrayList<Question> list;
    JSONArray easyQ;
    JSONArray mediumQ;
    JSONArray hardQ;
    CheckBox answer1;
    CheckBox answer2;
    CheckBox answer3;
    CheckBox answer4;
    String[] showAnswers;
    ImageButton help;
    int numberOfCoins = 0;
    int level = 1;
    JSONObject questions = null;
    String pravilenOdgovor;
    Button next;
    TextView coin;
    JSONObject vprasanjaZaEnNivo = null;

    int steviloPravilnihOdgovorov = 0;
    int steviloNapacnihOdgovorov = 0;
    int stevecZaForZanke = 0; //ta stevec se poveca samo, ko uporabnik klikne na gumb next in,
                              // če je izbral en od odgovorv - če ni izbral nobenega od ponujenih
                              //odgovorov potem se izpise toast z opozorilom, da izbere pravilni odgovor




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_questions_activity);

        help = findViewById(R.id.imageButton2);
        showData = findViewById(R.id.showData);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        next = findViewById(R.id.nextButton);
        coin = findViewById(R.id.coin);

        String url = "http://164.8.207.4:5555/api/game/regular/get_questions";
        new DownloadDataTask(url).execute();
       // if(message.equals("izbranLevel")){
            //TODO: DODAJ TU, ČE UPORABNIK IZBERE SAMO EN LEVEL
            //TODO: DODAJ, DA SE POVEČA COIN IN DA SE SPREMENI TEXTVIEW ZA COIN
            //TODO: KOD FOR ZANKE PRI LISTANJU PITANJA I ODGOVORA STAVI DA SE I POVEĆA TEK DOK JE GUMB KLIKNUTI, AKO JE VISE LEVELA ONDA SE IZMEĐU LEVELA TREBA PROMIJENITI V NULU OPET

        //}else{
            try {
                easyQ = questions.getJSONArray("easy");
                mediumQ = questions.getJSONArray("medium");
                hardQ = questions.getJSONArray("hard");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(; stevecZaForZanke<easyQ.length();){
                ArrayList<String> shuffled = new ArrayList<>();

                try {
                    showData.setText(easyQ.getJSONObject(stevecZaForZanke).getString("vprasanje"));
                    pravilenOdgovor = easyQ.getJSONObject(stevecZaForZanke).getString("pravilen_odgovor");
                    shuffleArray(shuffled, easyQ, stevecZaForZanke);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                answer1.setText(shuffled.get(0));
                answer2.setText(shuffled.get(1));
                answer3.setText(shuffled.get(2));
                answer4.setText(shuffled.get(3));
            }

            // if, ki preveri ali uporabnik lahko gre na naslednji nivo - če je število pravilnih enako 9
            if(steviloPravilnihOdgovorov == 10){
                stevecZaForZanke = 0;
                steviloNapacnihOdgovorov = 0;
                steviloPravilnihOdgovorov = 0;
                level = 2; //povečamo, da pri kliku na gumb, če je uporabnik pravilo odgovoril dobi večje število kovancev na višjem nivoju
                for(; stevecZaForZanke<mediumQ.length();){
                    ArrayList<String> shuffled = new ArrayList<>();

                    try {
                        showData.setText(mediumQ.getJSONObject(stevecZaForZanke).getString("vprasanje"));
                        pravilenOdgovor = mediumQ.getJSONObject(stevecZaForZanke).getString("pravilen_odgovor");
                        shuffleArray(shuffled, mediumQ, stevecZaForZanke);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    answer1.setText(shuffled.get(0));
                    answer2.setText(shuffled.get(1));
                    answer3.setText(shuffled.get(2));
                    answer4.setText(shuffled.get(3));
                }

            }

            if(steviloPravilnihOdgovorov == 10){
                stevecZaForZanke = 0;
                steviloNapacnihOdgovorov = 0;
                steviloPravilnihOdgovorov = 0;
                level = 3; //povečamo, da pri kliku na gumb, če je uporabnik pravilo odgovoril dobi večje število kovancev na višjem nivoju
                for(; stevecZaForZanke<hardQ.length();){
                    ArrayList<String> shuffled = new ArrayList<>();

                    try {
                        showData.setText(hardQ.getJSONObject(stevecZaForZanke).getString("vprasanje"));
                        pravilenOdgovor = hardQ.getJSONObject(stevecZaForZanke).getString("pravilen_odgovor");
                        shuffleArray(shuffled, hardQ, stevecZaForZanke);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    answer1.setText(shuffled.get(0));
                    answer2.setText(shuffled.get(1));
                    answer3.setText(shuffled.get(2));
                    answer4.setText(shuffled.get(3));
                }

            }

      //  }



        //implementacija gumba
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String odgovor = "";
                if(answer1.isChecked()==false && answer2.isChecked()==false && answer3.isChecked()==false && answer4.isChecked()==false){
                    odgovor = "Izberi enega izmed ponujenih odgovorov!";
                }else if((answer1.isChecked() && answer1.getText()==pravilenOdgovor) || (answer2.isChecked() && answer2.getText()==pravilenOdgovor) ||
                        (answer3.isChecked() && answer3.getText()==pravilenOdgovor) || (answer4.isChecked() && answer4.getText()==pravilenOdgovor)){
                    odgovor = "Odgovor je pravilen!";
                    stevecZaForZanke++;
                    steviloPravilnihOdgovorov++;

                    if(level == 1){
                        numberOfCoins = numberOfCoins + 5;
                    }else if(level == 2){
                        numberOfCoins = numberOfCoins + 10;
                    }else{
                        numberOfCoins = numberOfCoins + 15;
                    }

                }else if((answer1.isChecked() && answer1.getText()!=pravilenOdgovor) || (answer2.isChecked() && answer2.getText()!=pravilenOdgovor) ||
                        (answer3.isChecked() && answer3.getText()!=pravilenOdgovor) || (answer4.isChecked() && answer4.getText()!=pravilenOdgovor)){
                    odgovor = "Odgovor je napačen. Pravilen odgovor: " + pravilenOdgovor;
                    stevecZaForZanke++;
                    steviloNapacnihOdgovorov++;
                }

                Toast.makeText(getApplicationContext(), odgovor,
                        Toast.LENGTH_LONG).show();


            }
        });

            help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String odgovor = "Pravilen odgovor je: " + pravilenOdgovor;
                    if(numberOfCoins <5){
                        odgovor = "Ni dovolj kovančkov!";
                    }
                    FragmentManager fm = getSupportFragmentManager();

                    CustomDialogFragment newFragment = new CustomDialogFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("odgovor", odgovor);

                    newFragment.setArguments(bundle);
                    newFragment.show(fm, "custom_dialog");
                }
            });



    }
    public void shuffleArray(ArrayList<String> array, JSONArray data, int position) throws JSONException {
        array.add(data.getJSONObject(position).getString("pravilen_odgovor"));
        array.add(data.getJSONObject(position).getString("nepravilen_odgovor1"));
        array.add(data.getJSONObject(position).getString("nepravilen_odgovor2"));
        array.add(data.getJSONObject(position).getString("nepravilen_odgovor3"));

        Collections.shuffle(array);

    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        //String url = getIntent().getStringExtra("url");
        new DownloadDataTask(url).execute();
    }
    */

    private class DownloadDataTask extends AsyncTask<String, Void, String> {

        ParserTask parser;
        String url;

        public DownloadDataTask(String url){
            this.url = url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                questions = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                return downloadFromUrl(url);
            } catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        private String downloadFromUrl(String urlString) throws IOException{
            // get connection
            java.net.URL url = new java.net.URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(2000);
            conn.setConnectTimeout(6000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            // get data from database
            Log.d("SELECTED", "PRIJE");
            InputStream stream = conn.getInputStream();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
            String result = buffer.readLine();
            Log.d("DOWNLOADED: ", result);
            return result;
        }
    }


}
