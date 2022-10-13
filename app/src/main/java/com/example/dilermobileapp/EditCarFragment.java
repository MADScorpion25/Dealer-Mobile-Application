package com.example.dilermobileapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import android.app.DialogFragment;

import java.util.Objects;

public class EditCarFragment extends DialogFragment {
    public String editCarName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String productName = bundle != null ? bundle.getString("carName") : null;

        Objects.requireNonNull(getDialog()).setTitle("Edit Car");
        View v = inflater.inflate(R.layout.edit_car_layout, null);
        Button button = v.findViewById(R.id.confirmButton);
        TextView textView =  v.findViewById(R.id.editCarEditText);
        textView.setText(productName);

        button.setOnClickListener(u -> {
            editCarName = textView.getText().toString();
            dismiss();
        });
        return v;
    }
    @Override
    public void onDismiss(@NonNull final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
