
package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostResultActivity extends AppCompatActivity {
    private static final String TAG = "PostResultActivity";
    private FirebaseUser user;
    private String selectedPost;
    private EditText commentText;
    private CommentInfo preCommentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_result);

        PostInfo postInfo = (PostInfo)getIntent().getSerializableExtra("postInfo");
        selectedPost = getIntent().getStringExtra("postId");
        TextView titleTextView = findViewById(R.id.resultTitle);
        TextView contentsTextView = findViewById(R.id.resultContents);
        TextView timeTextView = findViewById(R.id.resultTime);
        TextView nickNameTextView = findViewById(R.id.resultNickname);
        commentText = findViewById(R.id.comment_edit); //댓글


        titleTextView.setText(postInfo.getTitle());
        contentsTextView.setText(postInfo.getContents());
        timeTextView.setText(postInfo.getCreatedAt());
        nickNameTextView.setText(postInfo.getNickName());

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        //이전에 작성되었던 댓글 생성
        db.collection("comments")
                .whereEqualTo("boardName", selectedPost)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                createComment(
                                        document.getData().get("boardName").toString(),
                                        document.getData().get("who").toString(),
                                        document.getData().get("time").toString(),
                                        document.getData().get("comment").toString()
                                );


                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        //댓글 등록 버튼
        Button regButton = findViewById(R.id.reg_button);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentSend = commentText.getText().toString();

                if(commentSend.length()> 0){

                    commentsUpdate();
                    commentText.setText(null);
                }
                else {
                    startToast("내용을 입력하세요.");

                }

            }
        });


    }

    //댓글 파이어베이스로 저장
    private void commentsUpdate(){


        final String boardName = selectedPost;
        final String who = "익명";
        final String comment = commentText.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        final String time = dateFormat.format(date);


        if( comment.length()>0){

            user = FirebaseAuth.getInstance().getCurrentUser();
            CommentInfo commentInfo = new CommentInfo(boardName,who,time,comment);
            uploader(commentInfo);

        }else{
            startToast("게시물 내용을 입력해주세요");
        }

        createComment(boardName,who,time,comment);
    }



    private void uploader(CommentInfo commentInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("comments").add(commentInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        startToast("댓글을 등록하였습니다.");
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

    private void createComment(String boardName,  String who, String time, String comment){

        LinearLayout commentBox = (LinearLayout)findViewById(R.id.comment_layout);

        commentBox.addView(createTextView(who, 1));
        commentBox.addView(createTextView(comment, 2));
        commentBox.addView(createTextView(time, 3));


    }

    private TextView createTextView(String value, int num){
        TextView textView = new TextView(getApplicationContext());
        textView.setText(value);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if(num == 1){
            textView.setTextColor(Color.parseColor("#50C3F6"));
            textView.setTextSize(13);
        }
        else if( num == 2 ){
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTextSize(15);
//            param.leftMargin = convertDPtoPX(2);
        }
        else{
            textView.setTextColor(Color.parseColor("#9F9F9F"));
            textView.setTextSize(10);
//            param.bottomMargin = convertDPtoPX(5);
        }
        return textView;

    }

    private void startToast(String msg){
        Toast.makeText(this, msg ,Toast.LENGTH_SHORT).show();
    }

}