package com.example.dilermobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dilermobileapp.config.AlertCreating;
import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.UserLogicDeclaration;
import com.example.dilermobileapp.logic.UserServiceLogic;
import com.example.dilermobileapp.models.User;
import com.example.dilermobileapp.storages.UsersStorage;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LoginActivity extends AppCompatActivity {

    private EditText login;

    private EditText password;

    private UserLogicDeclaration userLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.loginButton).setOnClickListener(this::loginUser);
        findViewById(R.id.registerButton).setOnClickListener(this::openRegisterActivity);

        userLogic = new UserServiceLogic();
        login = findViewById(R.id.editTextLogin);
        password = findViewById(R.id.editTextPassword);
    }

    private void loginUser(View view) {
        String log = login.getText().toString();
        String pass = password.getText().toString();
        if(log.equals("") || pass.equals("")) {
            return;
        }

        User user = new User();
        user.setLogin(log);
        user.setPassword(pass);

        if(userLogic.loginUser(user)) {
            AppManager.setCurrentUser(userLogic.getUserByLogin(user.getLogin()));
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            AlertCreating alert = new AlertCreating(this);
            alert.getWarningBuilder("Incorrect login or password, try again")
                    .setPositiveButton("Ok",
                            (dialog, which) -> dialog.cancel())
                    .create()
                    .show();
        }
    }

    private void openRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}