package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText searchRecycle;
    private static ArrayList<RecycleItem> recycleItemArrayList, filteredList;
    LinearLayoutManager linearLayoutManager;
    private static RecycleAdapter recycleAdapter;
    String preSearch;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "SearchActivity";
    Activity activity;
    ImageView select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        recyclerView = findViewById(R.id.recyclerview2);
        searchRecycle = findViewById(R.id.searchRecycle);
        select = findViewById(R.id.selectCategory);


        Intent intent = getIntent();
        preSearch = intent.getStringExtra("name");
        searchRecycle.setText(preSearch);

        recycleItemArrayList = new ArrayList<>();
        filteredList = new ArrayList<>();

        listUpdate();


        if(!(preSearch.isEmpty())){

            recycleAdapter = new RecycleAdapter(recycleItemArrayList,this);
            String searchText = searchRecycle.getText().toString();
            searchFilter(searchText,"find");
            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(recycleAdapter);

        }else {
            recycleAdapter = new RecycleAdapter(recycleItemArrayList, this);
            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(recycleAdapter);
            recycleAdapter.notifyDataSetChanged();

        }

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);

            }
        });





        searchRecycle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String searchText = searchRecycle.getText().toString();
                searchFilter(searchText,"find");

            }
        });




    }

    private void listUpdate(){

        //종이류
        ArrayList<String> paper = new ArrayList<String>(
                Arrays.asList("골판지", "가격표", "광고전단지","명함","백과사전","사전","쌀포대","신문지","잡지","종이상자"
                ,"종이심","종이조각","책","치킨박스","포스터","피자박스"));

        //종이팩
        ArrayList<String> paperPack = new ArrayList<String>(
                Arrays.asList("종이팩(우유팩)","우유팩"));
        //금속캔
        ArrayList<String> metalCan = new ArrayList<String>(
                Arrays.asList("부탄가스","스프레이","애완동물 음식캔"));

        //고철
        ArrayList<String> scrapMetal = new ArrayList<String>(
                Arrays.asList("공구류","국자(고철)","그릇","나사","낫","도끼","못","분유 깡통","쓰레받기(고철)","아령","압력솥"
                ,"역기","옷걸이","의류건조대"));
        //플라스틱
        ArrayList<String> plastic = new ArrayList<String>(
                Arrays.asList("국자(플라스틱)","그릇(플라스틱)","도마(플라스틱 도마)","리코더(플라스틱)","마요네즈용기","빨대","볼풀공","샴푸용기","쓰레받기(플라스틱)",
                        "치약용기","캡(플라스틱)","케찹용기","페트병"));

        //비닐류
        ArrayList<String> vinyl = new ArrayList<String>(
                Arrays.asList("비닐봉지(일회용)","아이스팩","완충재(뽁뽁이)"));

        //스티로폼
        ArrayList<String> styrofoam = new ArrayList<String>(
                Arrays.asList("컵라면(스티로폼)","스티로폼 상자"));

        //종량제봉투
        ArrayList<String> garbageBag = new ArrayList<String>(
                Arrays.asList("가위","걸레","계란껍질","고무장갑","골프공","국자(나무국자)","나무젓가락","나뭇가지","낙엽","도마(나무 도마)",
                        "돋보기","라이터(일회용)","랩(사용 후)","마스크","마우스패드","마커팬","만년필","면도기(일회용)","물티슈","바둑판"
                ,"방석","배드민턴공","보온병","볼펜","붓","비닐코팅종이"));

        //대형 폐기물
        ArrayList<String> bulkyWaste = new ArrayList<String>(
                Arrays.asList("가구류","개수대(씽크대)","기타(악기)","낚시대","난로","매트,매트리스","목발","문갑,문짝","바베큐그릴","밥상","벽시계",
                        "블라인드","사다리","삽","쌀통","서랍장","솜이불","수조,수족관","스노우보드"));





        //종류
        ArrayList<String> type = new ArrayList<String>(
                Arrays.asList("종이류", "종이팩", "금속캔","고철","플라스틱","비닐","스티로폼","종량제봉투","대형 폐기물"));

        for(int i=0 ;i<paper.size();i++) //종이류
        {
                recycleItemArrayList.add(new RecycleItem(paper.get(i),type.get(0)));
        }
        for(int i=0 ;i<paperPack.size();i++)//종이팩
        {
            recycleItemArrayList.add(new RecycleItem(paperPack.get(i),type.get(1)));
        }
        for(int i=0 ;i<metalCan.size();i++)//금속캔
        {
            recycleItemArrayList.add(new RecycleItem(metalCan.get(i),type.get(2)));
        }
        for(int i=0 ;i<scrapMetal.size();i++)//고철
        {
            recycleItemArrayList.add(new RecycleItem(scrapMetal.get(i),type.get(3)));
        }
        recycleItemArrayList.add(new RecycleItem("유리병","유리병류"));

        for(int i=0 ;i<plastic.size();i++)//플라스틱
        {
            recycleItemArrayList.add(new RecycleItem(plastic.get(i),type.get(4)));
        }
        for(int i=0 ;i<vinyl.size();i++)//비닐
        {
            recycleItemArrayList.add(new RecycleItem(vinyl.get(i),type.get(5)));
        }
        for(int i=0 ;i<styrofoam.size();i++)//스티로폼
        {
            recycleItemArrayList.add(new RecycleItem(styrofoam.get(i),type.get(6)));
        }
        for(int i=0 ;i<garbageBag.size();i++)//종량제봉투
        {
            recycleItemArrayList.add(new RecycleItem(garbageBag.get(i),type.get(7)));
        }
        for(int i=0 ;i<bulkyWaste.size();i++)//대형폐기물
        {
            recycleItemArrayList.add(new RecycleItem(bulkyWaste.get(i),type.get(8)));
        }




    }


    protected void onResume(){
        super.onResume();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.paper_category:
                        searchFilter("none","종이류");
                        return true;

                    case R.id.paperPack_category:
                        searchFilter("none","종이팩");
                        return true;

                    case R.id.metalCan_category:
                        searchFilter("none","금속캔");
                        return true;

                    case R.id.scrapMetal_category:
                        searchFilter("none","고철");
                        return true;

                    case R.id.plastic_category:
                        searchFilter("none","플라스틱");
                        return true;

                    case R.id.vinyl_category:
                        searchFilter("none","비닐");
                        return true;

                    case R.id.styrofoam_category:
                        searchFilter("none","스티로폼");
                        return true;

                    case R.id.garbageBag_category:
                        searchFilter("none","종량제봉투");
                        return true;

                    case R.id.bulkyWaste_category:
                        searchFilter("none","대형 폐기물");
                        return true;

                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.category, popup.getMenu());
        popup.show();
    }

    public void searchFilter(String searchText,String division){
        filteredList.clear();

        if(division.equals("find")){
            for(int i = 0; i< recycleItemArrayList.size(); i++){
                if(recycleItemArrayList.get(i).getRecycleName().toLowerCase().contains(searchText.toLowerCase())){
                    filteredList.add(recycleItemArrayList.get(i));
                }
            }
        }
        else{
                for(int i = 0; i< recycleItemArrayList.size(); i++){
                    if(recycleItemArrayList.get(i).getRecycleInfo().toLowerCase().equals(division)){
                        filteredList.add(recycleItemArrayList.get(i));
                    }
                }

        }
        recycleAdapter.filterList(filteredList);
    }




    private void startToast(String msg){
        Toast.makeText(getApplicationContext(), msg ,Toast.LENGTH_SHORT).show();
    }



}