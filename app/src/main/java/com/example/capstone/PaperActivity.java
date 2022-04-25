package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PaperActivity extends AppCompatActivity {


    static private int result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        result = getIntent().getIntExtra("division",0);

        if(result==1){setContentView(R.layout.activity_paper);}
        else if(result ==2){setContentView(R.layout.metal);}
        else if(result ==3){setContentView(R.layout.glass);}
        else if(result ==4){setContentView(R.layout.plastic);}
        else if(result ==5){setContentView(R.layout.plastic_bag);}
        else if(result ==6){setContentView(R.layout.styrofoam);}
    }
}