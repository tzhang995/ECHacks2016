package com.example.zy.echacks2016;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Tony on 2016-11-05.
 */

public class ConfirmDialogBox extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to Buy this Pass")
                .setTitle("Payment");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Constants.DIALOGKEY);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Nothing
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
