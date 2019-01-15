package com.projekt.kviz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SecondFragment extends Fragment {

    Button playShort;
    EditText category;
    EditText difficulty;
    EditText type;
    //change ip
    public String url = "http://192.168.1.107:5555/";

    //required empty public contsructor
    public SecondFragment(){};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_second, container, false);

        //send url
        category = view.findViewById(R.id.chooseCategory);
        type = view.findViewById(R.id.chooseType);
        difficulty = view.findViewById(R.id.chooseDifficulty);
        playShort = view.findViewById(R.id.playShort);
        final Intent intent = new Intent(getActivity().getApplicationContext(), ShowQuestionsActivity.class);
        playShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("url", buildUrl(category.getText().toString(), difficulty.getText().toString(), type.getText().toString()));
                startActivity(intent);
            }
        });

        return view;
    }


    public String buildUrl(String difficulty, String category, String type) {
        if (difficulty != null){
            url += "/" + difficulty;
        }
        if (category != null){
            url += "/" + category;
        }
        if (type != null){
            url += "/" + type;
        }
        return url;
    }

}
