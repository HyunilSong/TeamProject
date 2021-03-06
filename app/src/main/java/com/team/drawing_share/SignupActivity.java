package com.team.drawing_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordCheck;
    private TextView textviewError;
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
        textviewError = findViewById(R.id.error_text);
        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("Sign Up");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private boolean isValidEmail() {
        if (email.isEmpty()) {
            textviewError.setText("- Email format required");
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
            textviewError.setText("- Set password with at least 6 characters");
            // 비밀번호 공백
            return false;
        }
        else if(!password.equals(passwordcheck)){
            textviewError.setText("- Password and Password Check mismatch");
            return false;
        }
        else{
            return true;
        }
    }

    private String createuserId(String email){
        String result = "";
        for(int i = 0; i < email.length(); i++){
            if(email.charAt(i)=='@'){
                return result;
            }
            result += email.charAt(i);
        }
        return result;
    }

    private void createUser(final String email, String password) {
        System.out.println("creation start");
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(createuserId(email))
                                    .build();

                            firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            }
                                        }
                                    });

                            Toast.makeText(SignupActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                            startActivity(intent);
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