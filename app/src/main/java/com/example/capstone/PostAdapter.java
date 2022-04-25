package com.example.capstone;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import listener.OnPostListener;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private ArrayList<PostInfo> mDataset;
    private Activity activity;
    private FirebaseFirestore firebaseFirestore;
    private OnPostListener onPostListener;


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }

//        ViewHolder(View itemView){
//            super(itemView)
//        }
    }

    public PostAdapter(Activity activity, ArrayList<PostInfo> myDataset) {
        mDataset = myDataset;
        this.activity = activity;
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void setOnPostListener(OnPostListener onPostListener){
        this.onPostListener = onPostListener;
    }


    @NonNull
    @Override
    //뷰홀더 객체가 만들어질 때
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final ViewHolder viewHolder = new ViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(activity,PostResultActivity.class);
                resultIntent.putExtra("postInfo", mDataset.get(viewHolder.getAdapterPosition()));
                resultIntent.putExtra("postId",mDataset.get(viewHolder.getAdapterPosition()).getId());

//                Intent sendIntent = new Intent(activity,PostListActivity.class);
//                sendIntent.putExtra("uid",mDataset.get(viewHolder.getAdapterPosition()).getPublisher());

//                startToast(mDataset.get(viewHolder.getAdapterPosition()).getId());
//                activity.setResult(Activity.RESULT_OK,resultIntent);
                activity.startActivity(resultIntent);


            }


        });

        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showPopup(v, viewHolder.getAdapterPosition());

            }
        });


        return viewHolder;
    }

    //뷰홀더 객체가 재사용될 때
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView); //카드뷰 안에 텍스트뷰 찾기
        titleTextView.setText(mDataset.get(position).getTitle()); //제목만 보여주는것
//        textView.setText(mDataset.get(position));
        TextView createAtTextView = cardView.findViewById(R.id.createAtTextView);
//        createAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));
        createAtTextView.setText(mDataset.get(position).getCreatedAt());


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void showPopup(View v, int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
//                    case R.id.modify:
//                        onPostListener.onModify(mDataset.get(position).getId());
//
//                        return true;
                    case R.id.delete:

                        onPostListener.onDelete(mDataset.get(position).getId());

                        return true;
                    default:
                        return false;
                }

            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }

    private void startToast(String msg){
        Toast.makeText(activity, msg ,Toast.LENGTH_SHORT).show();
    }



}
