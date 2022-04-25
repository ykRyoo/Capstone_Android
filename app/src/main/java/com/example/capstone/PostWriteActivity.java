package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class  PostWriteActivity extends AppCompatActivity {
    private static final String TAG = "PostWriteActivity";
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);

        findViewById(R.id.post_send).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_send:
                    postUpdate();

                    break;

            }

        }
    };

    private void postUpdate(){
        final String title = ((EditText)findViewById(R.id.editTitle)).getText().toString();
        final String contents = ((EditText)findViewById(R.id.editContents)).getText().toString();
        final String nickName = ((EditText)findViewById(R.id.editNickName)).getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateToStr = dateFormat.format(date);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(title.length()>0 && contents.length()>0){

            PostInfo postInfo = new PostInfo(title,contents,user.getUid() , nickName ,dateToStr);
            uploader(postInfo);

        }else{
            startToast("게시물 내용을 입력해주세요");
        }
    }

    private void uploader(PostInfo postInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String id = getIntent().getStringExtra("id");
//        DocumentReference dr;
//        if(id == null){
//            dr = db.collection("posts").document();
//        }else{
//            dr = db.collection("posts").document(id);
//        }
//
//        final DocumentReference documentReference = dr;

        db.collection("posts").add(postInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        startToast("게시글을 등록하였습니다.");
                        Log.d(TAG,"Document witten with ID:" + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("게시글 등록에 실패하였습니다.");
                        Log.w(TAG,"Error writing document",e);
                    }
                });
    }

    private void startToast(String msg){
        Toast.makeText(this, msg ,Toast.LENGTH_SHORT).show();
    }

}