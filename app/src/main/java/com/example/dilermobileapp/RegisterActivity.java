package com.example.dilermobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.dilermobileapp.declarations.UserLogicDeclaration;
import com.example.dilermobileapp.logic.UserServiceLogic;
import com.example.dilermobileapp.models.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText login;

    private EditText password;

    private EditText email;

    private UserLogicDeclaration userLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = findViewById(R.id.editTextLogin);
        password = findViewById(R.id.editTextPassword);
        email = findViewById(R.id.editTextEmail);

        userLogic = new UserServiceLogic();

        findViewById(R.id.registerButton).setOnClickListener(this::registerUser);
    }

    private void registerUser(View view) {
        String log = login.getText().toString();
        String pass = password.getText().toString();
        String mail = email.getText().toString();
        if(log.equals("") || pass.equals("") || mail.equals("")) {
            return;
        }

        User user = new User();
        user.setLogin(log);
        user.setPassword(pass);
        user.setEmail(mail);

        if(userLogic.registerUser(user)) {
            finish();
        }
    }
}