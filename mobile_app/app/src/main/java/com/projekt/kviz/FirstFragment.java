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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.projekt.kviz.R;

public class FirstFragment extends Fragment {

    Button playButton = null;
    Button skipButton = null;
    Spinner kategorija = null;
    Spinner tezavnost = null;
    Spinner tip = null;

    //change ip
    public String url = "http://192.168.1.107:5555/";

    //required empty public contsructor
    public FirstFragment(){};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category_arrays, android.R.layout.simple_spinner_dropdown_item);
        kategorija.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.category_arrays, android.R.layout.simple_spinner_dropdown_item);
        tezavnost.setAdapter(adapter1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.category_arrays, android.R.layout.simple_spinner_dropdown_item);
        tip.setAdapter(adapter2);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    CustumDialogFragment newFragment = new CustumDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("kategorija", kategorija.getSelectedItem().toString());
                    bundle.putString("tezavnost", tezavnost.getSelectedItem().toString());
                    bundle.putString("tip", tip.getSelectedItem().toString());
                    newFragment.setArguments(bundle);

            }
        });
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
