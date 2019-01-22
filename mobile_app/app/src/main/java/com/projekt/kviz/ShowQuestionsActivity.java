package com.projekt.kviz;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
public class ShowQuestionsActivity extends AppCompatActivity {

    TextView showData;
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
    boolean provera;
    JSONArray customQuestions;
    JSONObject customQuestion;
    int stevecCustomVprasanj;
    boolean helpPreverjanje;

    ArrayList<JSONObject> vsaVprasanja = new ArrayList<>();

    int steviloPravilnihOdgovorov = 0;
    int steviloNapacnihOdgovorov = 0;
    int stevecZaForZanke = 0; //ta stevec se poveca samo, ko uporabnik klikne na gumb next in,
                              // če je izbral en od odgovorv - če ni izbral nobenega od ponujenih
                              //odgovorov potem se izpise toast z opozorilom, da izbere pravilni odgovor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_questions_activity);

    }

    @Override
    protected void onStart() {
        super.onStart();
        provera = false;
        String url = getIntent().getStringExtra("url");
        help = findViewById(R.id.imageButton2);
        showData = findViewById(R.id.showData);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        next = findViewById(R.id.nextButton);
        coin = findViewById(R.id.coin);
        new DownloadDataTask(url).execute();
    }

    public void shuffleArray(ArrayList<String> array, JSONObject data) throws JSONException {
        if(!data.getString("nepravilen_odgovor2").equals("null")){
            array.add(data.getString("nepravilen_odgovor2"));
        }
        if(!data.getString("nepravilen_odgovor3").equals("null")){
            array.add(data.getString("nepravilen_odgovor3"));
        }
        array.add(data.getString("pravilen_odgovor"));
        array.add(data.getString("nepravilen_odgovor1"));

        Log.d("VELIKOSTSHUFFLE", "Velikost: "+array.size());
        Collections.shuffle(array);
    }

    private class DownloadDataTask extends AsyncTask<String, Void, String> {

        String url;

        public DownloadDataTask(String url){
            this.url = url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            helpPreverjanje = false;
            coin.setText(String.valueOf(numberOfCoins));
            try {
                if (s.charAt(0) == '{') {
                    //get questions for regular quiz
                    questions = new JSONObject(s);

                    //create arraylist for each difficulty
                    easyQ = questions.getJSONArray("easy");
                    mediumQ = questions.getJSONArray("medium");
                    hardQ = questions.getJSONArray("hard");

                    for (int i = 0; i < easyQ.length(); i++) {
                        vsaVprasanja.add(easyQ.getJSONObject(i));
                    }
                    for (int i = 0; i < mediumQ.length(); i++) {
                        vsaVprasanja.add(mediumQ.getJSONObject(i));
                    }
                    for (int i = 0; i < hardQ.length(); i++) {
                        vsaVprasanja.add(mediumQ.getJSONObject(i));
                    }
                    //show first question on screen
                    ArrayList<String> shuffled = new ArrayList<>();
                    Log.d("QUESTION", vsaVprasanja.get(stevecZaForZanke).getString("vprasanje"));
                    String vprasanje = vsaVprasanja.get(stevecZaForZanke).getString("vprasanje");
                    vprasanje = vprasanje.replace("&quot;", "\"");
                    vprasanje = vprasanje.replace("&#039;", "\'");
                    showData.setText(vprasanje);
                    Log.d("QUESTIONP", vsaVprasanja.get(stevecZaForZanke).getString("pravilen_odgovor"));
                    pravilenOdgovor = vsaVprasanja.get(stevecZaForZanke).getString("pravilen_odgovor");
                    shuffleArray(shuffled, vsaVprasanja.get(stevecZaForZanke));
                    Log.d("ANSWER1", shuffled.get(0));
                    Log.d("ANSWER2", shuffled.get(1));
                    //Log.d("ANSWER3", shuffled.get(2));
                    //Log.d("ANSWER4", shuffled.get(3));
                    answer1.setText(shuffled.get(0));
                    answer2.setText(shuffled.get(1));
                    if (shuffled.size() == 4) {
                        Log.d("VELIKOST", "Velikost shuffled je: " + shuffled.size());
                        answer3.setText(shuffled.get(2));
                        answer4.setText(shuffled.get(3));
                    } else if (shuffled.size() == 2){
                        answer3.setVisibility(View.GONE);
                        answer4.setVisibility(View.GONE);
                    }
                    //  }

                    //on click check answer and call method ponastaviVprasanja() for changing question on screen
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String odgovor = "";
                            if (answer1.isChecked() == false && answer2.isChecked() == false && answer3.isChecked() == false && answer4.isChecked() == false) {
                                odgovor = "Choose one of answer!";
                            } else if ((answer1.isChecked() && answer1.getText() == pravilenOdgovor) || (answer2.isChecked() && answer2.getText() == pravilenOdgovor) ||
                                    (answer3.isChecked() && answer3.getText() == pravilenOdgovor) || (answer4.isChecked() && answer4.getText() == pravilenOdgovor)) {
                                odgovor = "Your answer is correct!";
                                stevecZaForZanke++;
                                steviloPravilnihOdgovorov++;

                                //add coins for correct answer
                                if (level == 1 && helpPreverjanje == false) {
                                    numberOfCoins = numberOfCoins + 5;
                                } else if (level == 2 && helpPreverjanje == false) {
                                    numberOfCoins = numberOfCoins + 10;
                                } else if (helpPreverjanje == false){
                                    numberOfCoins = numberOfCoins + 15;
                                }
                                coin.setText(String.valueOf(numberOfCoins));

                            } else if ((answer1.isChecked() && answer1.getText() != pravilenOdgovor) || (answer2.isChecked() && answer2.getText() != pravilenOdgovor) ||
                                    (answer3.isChecked() && answer3.getText() != pravilenOdgovor) || (answer4.isChecked() && answer4.getText() != pravilenOdgovor)) {
                                odgovor = "Your answer is incorrect. Correct answer is: " + pravilenOdgovor;
                                stevecZaForZanke++;
                                steviloNapacnihOdgovorov++;
                            }

                            //show reply -> correct/incorrect/choose
                            Toast.makeText(getApplicationContext(), odgovor,
                                    Toast.LENGTH_LONG).show();
                            //set new question
                            ponastaviVprasanja();
                        }
                    });
                } else if (s.charAt(0) == '['){
                    //get questions for chosen params
                    Log.d("IMHERE", "Here json: "+s);
                    customQuestions = new JSONArray(s);
                    stevecCustomVprasanj = 0;
                        customQuestion = customQuestions.getJSONObject(0);
                        if (stevecCustomVprasanj == 0){
                            ponastaviVprasanjeZaCustom();
                        }
                        ponastaviVprasanjeZaCustom();
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String odgovor = "";
                                if (answer1.isChecked() == false && answer2.isChecked() == false && answer3.isChecked() == false && answer4.isChecked() == false) {
                                    odgovor = "Choose one of answer!";
                                } else if ((answer1.isChecked() && answer1.getText() == pravilenOdgovor) || (answer2.isChecked() && answer2.getText() == pravilenOdgovor) ||
                                        (answer3.isChecked() && answer3.getText() == pravilenOdgovor) || (answer4.isChecked() && answer4.getText() == pravilenOdgovor)) {
                                    odgovor = "Your answer is correct!";
                                    stevecCustomVprasanj++;

                                    //add coins for correct answer
                                    numberOfCoins = numberOfCoins + 3;
                                    coin.setText(String.valueOf(numberOfCoins));

                                } else if ((answer1.isChecked() && answer1.getText() != pravilenOdgovor) || (answer2.isChecked() && answer2.getText() != pravilenOdgovor) ||
                                        (answer3.isChecked() && answer3.getText() != pravilenOdgovor) || (answer4.isChecked() && answer4.getText() != pravilenOdgovor)) {
                                    odgovor = "Your answer is incorrect. Correct answer is: " + pravilenOdgovor;
                                    stevecCustomVprasanj++;
                                }

                                //show reply -> correct/incorrect/choose
                                Toast.makeText(getApplicationContext(), odgovor,
                                        Toast.LENGTH_LONG).show();
                                //set new question
                                try {
                                    customQuestion = customQuestions.getJSONObject(stevecCustomVprasanj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ponastaviVprasanjeZaCustom();
                            }
                        });
                    }

                //on click show correct question for help if user has enough coins
                help.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helpPreverjanje = true;
                        String odgovor = "Correct answer is: " + pravilenOdgovor;
                        if (numberOfCoins < 10) {
                            odgovor = "You don't have enough coins!";
                        } else {
                            numberOfCoins = numberOfCoins - 10;
                            coin.setText(String.valueOf(numberOfCoins));
                        }
                        FragmentManager fm = getSupportFragmentManager();

                        CustomDialogFragment newFragment = new CustomDialogFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("odgovor", odgovor);

                        newFragment.setArguments(bundle);
                        newFragment.show(fm, "custom_dialog");
                    }
                });
                } catch(JSONException e){
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

    public void ponastaviVprasanja(){
        String odgovor;
        helpPreverjanje = false;
        if(stevecZaForZanke == 9 && steviloPravilnihOdgovorov > 7){
            level = 2;
            steviloPravilnihOdgovorov = 0;
            steviloNapacnihOdgovorov = 0;
            odgovor = "Level 2!";
            FragmentManager fm = getSupportFragmentManager();

            CustomDialogFragment newFragment = new CustomDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putString("odgovor", odgovor);

            newFragment.setArguments(bundle);
            newFragment.show(fm, "custom_dialog");

        }else if(stevecZaForZanke == 9 && steviloPravilnihOdgovorov > 7){
            odgovor = "Game over!";
            FragmentManager fm = getSupportFragmentManager();

            CustomDialogFragment newFragment = new CustomDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putString("odgovor", odgovor);

            newFragment.setArguments(bundle);
            newFragment.show(fm, "custom_dialog");
        }


        if(stevecZaForZanke == 19 && steviloPravilnihOdgovorov > 7){
            level = 3;
            steviloPravilnihOdgovorov = 0;
            steviloNapacnihOdgovorov = 0;
            odgovor = "Level 3!";
            FragmentManager fm = getSupportFragmentManager();

            CustomDialogFragment newFragment = new CustomDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putString("odgovor", odgovor);

            newFragment.setArguments(bundle);
            newFragment.show(fm, "custom_dialog");

        }else if(stevecZaForZanke == 19 && steviloPravilnihOdgovorov > 7){
            odgovor = "Game over!";
            FragmentManager fm = getSupportFragmentManager();

            CustomDialogFragment newFragment = new CustomDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putString("odgovor", odgovor);

            newFragment.setArguments(bundle);
            newFragment.show(fm, "custom_dialog");
        }

        if(stevecZaForZanke == 29 && steviloPravilnihOdgovorov > 7){
            odgovor = "Conratulations! We have a winner!";
            FragmentManager fm = getSupportFragmentManager();

            CustomDialogFragment newFragment = new CustomDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putString("odgovor", odgovor);

            newFragment.setArguments(bundle);
            newFragment.show(fm, "custom_dialog");
        }

        if (answer1.isChecked()) {
            answer1.setChecked(false);
        }
        if (answer2.isChecked()) {
            answer2.setChecked(false);
        }
        if (answer3.isChecked()) {
            answer3.setChecked(false);
        }
        if (answer4.isChecked()) {
            answer4.setChecked(false);
        }


        ArrayList<String> shuffled = new ArrayList<>();
        try {
            String vprasanje = vsaVprasanja.get(stevecZaForZanke).getString("vprasanje");
            vprasanje = vprasanje.replace("&quot;", "\"");
            vprasanje = vprasanje.replace("&#039;", "\'");
            Log.d("QUESTION", vsaVprasanja.get(stevecZaForZanke).getString("vprasanje"));
            showData.setText(vprasanje);
            //showData.append(easyQ.getJSONObject(stevecZaForZanke).getString("vprasanje"));
            Log.d("QUESTIONP", vsaVprasanja.get(stevecZaForZanke).getString("pravilen_odgovor"));
            pravilenOdgovor = vsaVprasanja.get(stevecZaForZanke).getString("pravilen_odgovor");
            shuffleArray(shuffled, vsaVprasanja.get(stevecZaForZanke));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("ANSWER1", shuffled.get(0));
        Log.d("ANSWER2", shuffled.get(1));
        //Log.d("ANSWER3", shuffled.get(2));
        //Log.d("ANSWER4", shuffled.get(3));

        if(shuffled.size() == 2){
            answer1.setText(shuffled.get(0));
            answer2.setText(shuffled.get(1));
            answer3.setVisibility(View.GONE);
            answer4.setVisibility(View.GONE);
        }else{
            answer1.setText(shuffled.get(0));
            answer2.setText(shuffled.get(1));
            answer3.setText(shuffled.get(2));
            answer4.setText(shuffled.get(3));
            answer3.setVisibility(View.VISIBLE);
            answer4.setVisibility(View.VISIBLE);
        }
    }

    public void ponastaviVprasanjeZaCustom(){
        if(stevecCustomVprasanj == 19){
            String odgovor = "You answered all question.";
            FragmentManager fm = getSupportFragmentManager();

            CustomDialogFragment newFragment = new CustomDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putString("odgovor", odgovor);

            newFragment.setArguments(bundle);
            newFragment.show(fm, "custom_dialog");
        }
        if (answer1.isChecked()) {
            answer1.setChecked(false);
        }
        if (answer2.isChecked()) {
            answer2.setChecked(false);
        }
        if (answer3.isChecked()) {
            answer3.setChecked(false);
        }
        if (answer4.isChecked()) {
            answer4.setChecked(false);
        }


        ArrayList<String> shuffled = new ArrayList<>();
        try {
            String vprasanje = customQuestion.getString("vprasanje");
            vprasanje = vprasanje.replace("&quot;", "\"");
            vprasanje = vprasanje.replace("&#039;", "\'");
            Log.d("QUESTION", customQuestion.getString("vprasanje"));
            showData.setText(vprasanje);
            Log.d("QUESTIONP", customQuestion.getString("pravilen_odgovor"));
            pravilenOdgovor = customQuestion.getString("pravilen_odgovor");
            shuffleArray(shuffled, customQuestion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("ANSWER1", shuffled.get(0));
        Log.d("ANSWER2", shuffled.get(1));
        //Log.d("ANSWER3", shuffled.get(2));
        //Log.d("ANSWER4", shuffled.get(3));

        if(shuffled.size() == 2){
            answer1.setText(shuffled.get(0));
            answer2.setText(shuffled.get(1));
            answer3.setVisibility(View.GONE);
            answer4.setVisibility(View.GONE);
        }else{
            answer1.setText(shuffled.get(0));
            answer2.setText(shuffled.get(1));
            answer3.setText(shuffled.get(2));
            answer4.setText(shuffled.get(3));
            answer3.setVisibility(View.VISIBLE);
            answer4.setVisibility(View.VISIBLE);
        }
    }

}
