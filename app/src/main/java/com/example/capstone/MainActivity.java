package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //로그인이 안되었을시
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
            startActivity(intent);
        }

        findViewById(R.id.logout).setOnClickListener(onClickListener); // 로그아웃 버튼
        findViewById(R.id.mbutton).setOnClickListener(onClickListener); // 카메라 인식 버튼
        findViewById(R.id.post_button).setOnClickListener((onClickListener));//게시판 버튼
        findViewById(R.id.category_button).setOnClickListener((onClickListener));//게시판 버튼
        findViewById(R.id.location_button).setOnClickListener((onClickListener));//지도 버튼
        findViewById(R.id.list_button).setOnClickListener((onClickListener));//재활용품 리스트 찾기 버튼


    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(SignupActivity.class);
                    break;

                case R.id.mbutton:
                    myStartActivity(CameraActivity.class);
                    break;

                case R.id.post_button:
                    myStartActivity(PostListActivity.class);
                    break;

                case R.id.category_button:
                    myStartActivity(CategoryActivity.class);
                    break;

                case R.id.location_button:
                    myStartActivity(MapActivity.class);
                    break;

                case R.id.list_button:
                    EditText editText = findViewById(R.id.recycleSearchEdit);
                    String search = editText.getText().toString();

                    Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                    intent.putExtra("name",search);
                    editText.setText(null);
                    startActivity(intent);
                    break;
            }

        }
    };


    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        startActivity(intent);
    }

}