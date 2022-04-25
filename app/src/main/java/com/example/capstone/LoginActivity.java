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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "SignupActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.login_success).setOnClickListener(onClickListener);



    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_success:
                    logIn();
                    break;

            }


        }
    };

    private void logIn() {

        String email = ((EditText) findViewById(R.id.login_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.login_password)).getText().toString();


        if (email.length() > 0 && password.length() > 0 ) {


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "로그인 성공");
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인에 성공하였습니다.");
                                myStartActiviy(MainActivity.class);

                            } else {
                                // If sign in fails, display a message to the user.
                                if (task.getException() != null) {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    startToast("로그인에 실패하였습니다.");
                                }

                            }
                        }
                    });


            } else{
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg ,Toast.LENGTH_LONG).show();
    }

    private void myStartActiviy(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}