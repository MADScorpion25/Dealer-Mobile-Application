package com.example.dilermobileapp.config;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dilermobileapp.R;

public class AlertCreating {
    private Context context;
    private final String QUESTION_TITLE = "Question";
    private final String QUESTION_CONTENT = "Do you want to delete entity?";
    private final String ALERT_TITLE = "Warning";

    @NonNull
    public AlertDialog.Builder onCreateDialog(String title, String message, int drawable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(drawable);
        return builder;
    }
    public AlertCreating(Context context) {
        this.context = context;
    }

    public AlertDialog.Builder getQuestionBuilder(){
        return onCreateDialog(QUESTION_TITLE, QUESTION_CONTENT, R.drawable.free_icon_question_8184832);
    }

    public AlertDialog.Builder getWarningBuilder(String message){
        return onCreateDialog(ALERT_TITLE, message, R.drawable.warning);
    }
}
