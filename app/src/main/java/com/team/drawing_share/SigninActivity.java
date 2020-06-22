package com.team.drawing_share;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.login_email);
        editTextPassword = findViewById(R.id.login_password);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("Sign In");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void go_signup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

}