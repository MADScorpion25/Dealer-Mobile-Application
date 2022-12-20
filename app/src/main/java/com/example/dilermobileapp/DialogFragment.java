package com.example.dilermobileapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private final String QUESTION_TITLE = "Question";
    private final String QUESTION_CONTENT = "Do you want to delete entity?";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(QUESTION_TITLE)
                .setMessage(QUESTION_CONTENT)
                .setIcon(R.drawable.free_icon_question_8184832)
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                })
                .setPositiveButton("Yes", (dialog, id) -> {
                    dialog.dismiss();
                });
        return builder.create();
    }
}