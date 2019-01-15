package com.projekt.kviz;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserTask extends AsyncTask<String, Void, ArrayList<Question>> {

    private String data;
    private ArrayList<Question> questionList;

    public ParserTask() {
        //this.data = data;
        questionList = new ArrayList<>();
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    @Override
    protected void onPostExecute(ArrayList<Question> questions) {
        super.onPostExecute(questions);
        questionList = questions;
    }

    @Override
    protected ArrayList<Question> doInBackground(String... data) {
        try {
            return parseData(data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //parse data
    private ArrayList<Question> parseData(String... data) throws org.json.JSONException{
        ArrayList<Question> questionList = new ArrayList<Question>();
        ArrayList<String> incorrectAnswers = null;
        JSONObject jsonObject = new JSONObject(data.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        //get value of properties and create object out of it
        for (int i = 0; i < jsonArray.length(); i++) {
            Question q = new Question();
            String category = jsonArray.getJSONObject(i).getString("category");
            q.setCategory(category);
            String type = jsonArray.getJSONObject(i).getString("type");
            q.setType(type);
            String difficulty = jsonArray.getJSONObject(i).getString("difficulty");
            q.setDifficulty(difficulty);
            String question = jsonArray.getJSONObject(i).getString("question");
            q.setQuestion(question);
            String correctAnswer = jsonArray.getJSONObject(i).getString("correct_answer");
            q.setCorrectAnswer(correctAnswer);
            JSONArray list = jsonArray.getJSONObject(i).getJSONArray("incorrect_answers");
            for (int j = 0; j < list.length(); j++) {
                incorrectAnswers = new ArrayList<>();
                String answer = list.getString(j);
                incorrectAnswers.add(answer);
            }
            q.setIncorrectAnswers(incorrectAnswers);
            questionList.add(q);
        }
        Log.d("SIZE", "Array size in parser: "+questionList.size());
        Log.d("MESSAGEPARSE", "Finished with parsing! Sending an array of questions to showQuestionsActivity...");
        return questionList;
    }
}
