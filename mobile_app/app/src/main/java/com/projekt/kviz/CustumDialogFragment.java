package com.projekt.kviz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class CustumDialogFragment extends DialogFragment {
    @Override
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
                return builder.create();

    }
}
