package com.example.fitnesstracker;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHandler db;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;
    TextView mTextViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHandler(this);
        mTextUsername = findViewById(R.id.edittext_username);
        mTextPassword = findViewById(R.id.edittext_password);
        mTextCnfPassword = findViewById(R.id.edittext_cnf_password);
        mButtonRegister = findViewById(R.id.button_register);
        mTextViewLogin = findViewById(R.id.textview_login);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(LoginIntent);
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();

                if(user.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Add Username", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(pwd.isEmpty())
                    {
                        Toast.makeText(RegisterActivity.this, "Add Password", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(cnf_pwd.isEmpty())
                        {
                            Toast.makeText(RegisterActivity.this, "Confirm Password", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(pwd.equals(cnf_pwd))
                            {
                                long val = db.addUser(user,pwd);
                                if(val > 0)
                                {
                                    Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                    Intent moveToLogin = new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(moveToLogin);
                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
    }
}