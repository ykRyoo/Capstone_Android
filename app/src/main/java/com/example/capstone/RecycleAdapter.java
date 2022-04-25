package com.example.capstone;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private ArrayList<RecycleItem> recycleItemArrayList;
    private Activity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }

    }

    public RecycleAdapter(ArrayList<RecycleItem> recycleItemArrayList, Activity activity) {

        this.recycleItemArrayList = recycleItemArrayList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle, parent, false);
        final RecycleAdapter.ViewHolder viewHolder = new RecycleAdapter.ViewHolder(cardView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final RecycleAdapter.ViewHolder holder, int position) {

        CardView cardView = holder.cardView;
        TextView name = cardView.findViewById(R.id.recycleName); //카드뷰 안에 텍스트뷰 찾기
        name.setText(recycleItemArrayList.get(position).getRecycleName());
        TextView info = cardView.findViewById(R.id.recycleInfo);
        info.setText(recycleItemArrayList.get(position).getRecycleInfo());

    }

    @Override
    public int getItemCount() {
        return recycleItemArrayList.size();
    }

    public void filterList(ArrayList<RecycleItem>filteredList){
        recycleItemArrayList = filteredList;
        notifyDataSetChanged();
    }

    private void startToast(String msg){
        Toast.makeText(activity, msg ,Toast.LENGTH_SHORT).show();
    }



}
