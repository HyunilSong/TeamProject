package com.team.drawing_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordCheck;

    private String email = "";
    private String password = "";
    private String passwordcheck = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.id_text);
        editTextPassword = findViewById(R.id.password_text);
        editTextPasswordCheck = findViewById(R.id.check_text);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("Sign Up");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            System.out.println(email);
            return false;
        }
        else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            System.out.println(password);
            // 비밀번호 공백
            return false;
        }
        else if(!password.equals(passwordcheck)){
            return false;
        }
        else{
            return true;
        }
    }
    private void createUser(String email, String password) {
        System.out.println("creation start");
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(SignupActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignupActivity.this, R.string.fail_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void signUp(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        passwordcheck = editTextPasswordCheck.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            createUser(email, password);
        }
    }

}