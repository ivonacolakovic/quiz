package com.projekt.kviz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ShowQuestionsActivity extends AppCompatActivity {

    // public final String url = "https://opentdb.com/api.php?amount=50";
    //public final String url = "http://192.168.1.107:5555/";
    TextView showData;
    ArrayList<Question> list;
    ArrayList<Question> easyQ;
    ArrayList<Question> mediumQ;
    ArrayList<Question> hardQ;
    TextView answer1;
    TextView answer2;
    TextView answer3;
    TextView answer4;
    String[] showAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_questions_activity);

        showData = findViewById(R.id.showData);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        //showAnswers = new String[] {"answer1", "answer2", "answer3", "answer4"};
        //showData.setText(QuestionList.questionList.get(1).getQuestion());

    }

    @Override
    protected void onStart() {
        super.onStart();
        //get url for connection with backend
        Intent i = getIntent();
        String url = i.getStringExtra("url");
        new DownloadDataTask(url).execute(url);
    }

    private class DownloadDataTask extends AsyncTask<String, Void, String> {

        ParserTask parser;
        String url;

        public DownloadDataTask(String url){
            this.url = url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //new ParserTask().execute(s);
                //call parsing method
                /*easyQ = new ArrayList<>();
                mediumQ = new ArrayList<>();
                hardQ = new ArrayList<>();
                list = parser.getQuestionList();
                for(Question qst: list){
                    if (qst.getDifficulty().equals("easy")){
                        easyQ.add(qst);
                    } else if (qst.getDifficulty().equals("medium")){
                        mediumQ.add(qst);
                    } else if (qst.getDifficulty().equals("hard")){
                        hardQ.add(qst);
                    }
                }
                //int level = 0;
                //int rand = new Random().nextInt(4);
                Log.d("SIZE", "EasyQ size: "+easyQ.size());
                Question selected = easyQ.get(new Random().nextInt(easyQ.size()));
                Log.d("SELECTED", "Selected question: "+selected.getQuestion());
                showData.setText(selected.getQuestion());
                ArrayList<String> finalAnswers = selected.getIncorrectAnswers();
                finalAnswers.add(selected.getCorrectAnswer());
                Collections.shuffle(finalAnswers);
                answer1.setText(finalAnswers.get(0));
                answer2.setText(finalAnswers.get(1));
                answer3.setText(finalAnswers.get(2));
                answer4.setText(finalAnswers.get(3));*/

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

        private String downloadFromUrl(String urlString) throws IOException {
            // get connection
            java.net.URL url = new java.net.URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(2000);
            conn.setConnectTimeout(6000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            // get data from database
            InputStream stream = conn.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
            String result = buffer.readLine();
            Log.d("DOWNLOADED: ", result);
            return result;
        }
    }
}
