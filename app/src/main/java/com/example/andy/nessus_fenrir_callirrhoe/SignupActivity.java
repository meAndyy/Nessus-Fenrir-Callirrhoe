package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.controllers.DatabaseController;
import com.example.models.LogHolder;
import com.example.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignupActivity extends AppCompatActivity {

    private EditText email, password;
    private Button  btnSignUp, btnResetPassword;
    private FirebaseAuth auth;
    private EditText name;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnLogin);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btnResetPassword = (Button) findViewById(R.id.btnResetPassword);
        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String memail = email.getText().toString().trim();
                String mpass = password.getText().toString().trim();
                String mname = name.getText().toString().trim();
                String mphone = phone.getText().toString().trim();

                if (TextUtils.isEmpty(memail)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mname)) {
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mphone)) {
                    Toast.makeText(getApplicationContext(), "Enter phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6 || TextUtils.isEmpty(mpass)) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                 final User user = new User(mname, mphone, memail);

                final LogHolder logholder = new LogHolder(mname, "");

                //create user
                auth.createUserWithEmailAndPassword(memail, mpass)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "Account Created: "+name, Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseController dbc = new DatabaseController();
                                    dbc.createUserInDatabase(user);
                                    dbc.createLogInDatabase(logholder);
                                    startActivity(new Intent(SignupActivity.this, NFCActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

    }
}
