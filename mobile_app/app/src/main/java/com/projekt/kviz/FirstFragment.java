package com.projekt.kviz;

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

import com.projekt.kviz.R;

public class FirstFragment extends Fragment {

    Button playButton;
    //change ip
    public String url = "http://192.168.1.107:5555/";

    //required empty public contsructor
    public FirstFragment(){};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_first, container, false);

        //send url
        playButton = view.findViewById(R.id.playButton);
        final Intent intent = new Intent(getActivity().getApplicationContext(), ShowQuestionsActivity.class);
        //intent.putExtra("url", buildUrl(category.getText().toString(), difficulty.getText().toString(), type.getText().toString()));
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        return view;
    }


    public String buildUrl(String difficulty, String category, String type) {
        return url + "/" + difficulty + "/" + category + "/" + type;
    }

}
