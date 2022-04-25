package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "SignupActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signup).setOnClickListener(onClickListener);
        findViewById(R.id.signup_logsuccess).setOnClickListener(onClickListener);



    }

    public void onBackPressed(){
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signup:
                    signUp();
                    break;

                case R.id.signup_logsuccess:
                    Intent intent1 = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent1);
                    break;
            }

        }
    };

    private void signUp() {

        String email = ((EditText) findViewById(R.id.signup_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.signup_password)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.signup_password2)).getText().toString();


        if (email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원가입에 성공하였습니다.");


                                } else {
                                    if (task.getException() != null) {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        startToast("회원가입에 실패하였습니다.");
                                    }

                                }
                            }
                        });

            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }

        }else{
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg ,Toast.LENGTH_LONG).show();
    }

}