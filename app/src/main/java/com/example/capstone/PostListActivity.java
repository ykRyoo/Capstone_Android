package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;

import listener.OnPostListener;

public class PostListActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "PostListActivity";
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    private ArrayList<PostInfo> postList;
    private String getUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        postList = new ArrayList<>();
        mAdapter = new PostAdapter(PostListActivity.this,postList);
        mAdapter.setOnPostListener(onPostListener);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(PostListActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);


        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);



    }


    protected void onResume(){
        super.onResume();
        postUpdate();

    }


    private void postUpdate(){
        db.collection("posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            postList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                postList.add(new PostInfo(
                                        document.getData().get("title").toString(),
                                        document.getData().get("contents").toString(),
                                        document.getData().get("publisher").toString(),
//                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getData().get("createdAt").toString(),
                                        document.getId(),
                                        document.getData().get("nickName").toString()

                                ));
                            }
//
                            mAdapter.notifyDataSetChanged();


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(String id) {

            Log.e("로그","삭제 "+ id);
            db.collection("posts").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            startToast("게시글을 삭제하였습니다.");
                            //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            postUpdate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startToast("게시글을 삭제하지 못하였습니다.");
                            //Log.w(TAG, "Error deleting document", e);
                        }
                    });

        }

        @Override
        public void onModify(String id) {
            Log.e("로그","수정"+ id);
            myStartActivity(PostWriteActivity.class,id);
        }
    };

    View.OnClickListener onClickListener =  new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.floatingActionButton:
                    myStartActivity(PostWriteActivity.class);
                    break;

            }

        }
    };



    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        startActivity(intent);
    }


    private void myStartActivity(Class c,String id){
        Intent intent = new Intent(this,c);
        intent.putExtra("id",id);
        startActivity(intent);

    }

    private void startToast(String msg){
        Toast.makeText(this, msg ,Toast.LENGTH_LONG).show();
    }
}