package com.example.dilermobileapp.config;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dilermobileapp.R;

public class AlertCreating {
    private Context context;
    private final String QUESTION_TITLE = "Question";
    private final String QUESTION_CONTENT = "Do you want to delete entity?";

    @NonNull
    public AlertDialog.Builder onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(QUESTION_TITLE)
                .setMessage(QUESTION_CONTENT)
                .setIcon(R.drawable.free_icon_question_8184832);
        return builder;
    }
    public AlertCreating(Context context) {
        this.context = context;
    }
}
