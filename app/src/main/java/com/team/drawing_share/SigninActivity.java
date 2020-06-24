package com.team.drawing_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

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
        EditText editTextPassword = findViewById(R.id.login_password);
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //로그인 돼있으면 자동으로 넘김
        if (firebaseAuth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent intent = new Intent(SigninActivity.this, DataActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void OnClickHandler(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password Wizard").setMessage("What is your account?\nWe will send email to change password.");
        final EditText et = new EditText(SigninActivity.this);
        builder.setView(et);

        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String value = et.getText().toString();
                //비밀번호 재설정 이메일 보내기
                firebaseAuth.sendPasswordResetEmail(value)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SigninActivity.this, "Check your Email", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SigninActivity.this, "Nonexistent Email", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();     //닫기
                // Event
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
        if (password.isEmpty()||password.length()<6) {
            // 비밀번호 공백
            return false;
        }
        else{
            return true;
        }
    }
    public void signIn(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
        }
    }

    private void loginUser(final String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(SigninActivity.this, firebaseAuth.getCurrentUser().getDisplayName() + " logined" , Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(SigninActivity.this, DataActivity.class);
                            startActivity(intent);
                        } else {
                            // 로그인 실패
                            Toast.makeText(SigninActivity.this, R.string.fail_signin, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}