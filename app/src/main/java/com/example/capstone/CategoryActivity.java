package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CategoryActivity extends AppCompatActivity {

    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        findViewById(R.id.paper).setOnClickListener(onClickListener);
        findViewById(R.id.metal).setOnClickListener(onClickListener);
        findViewById(R.id.glass).setOnClickListener(onClickListener);
        findViewById(R.id.plastic).setOnClickListener(onClickListener);
        findViewById(R.id.plastic_bag).setOnClickListener(onClickListener);
        findViewById(R.id.styrofoam).setOnClickListener(onClickListener);


    }


    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.paper:

                    Intent resultIntent = new Intent(getApplicationContext(),PaperActivity.class);
                    resultIntent.putExtra("division",1);
                    startActivity(resultIntent);

//                    myStartActivity(PaperActivity.class);
//                    setContentView(R.layout.activity_paper);

                    break;

                case R.id.metal:
                    Intent intent = new Intent(getApplicationContext(),PaperActivity.class);
                    intent.putExtra("division",2);
                    startActivity(intent);
                    break;

                case R.id.glass:
                    Intent intent1 = new Intent(getApplicationContext(),PaperActivity.class);
                    intent1.putExtra("division",3);
                    startActivity(intent1);
                    break;

                case R.id.plastic:
                    Intent intent2 = new Intent(getApplicationContext(),PaperActivity.class);
                    intent2.putExtra("division",4);
                    startActivity(intent2);
                    break;

                case R.id.plastic_bag:
                    Intent intent3 = new Intent(getApplicationContext(),PaperActivity.class);
                    intent3.putExtra("division",5);
                    startActivity(intent3);
                    break;

                case R.id.styrofoam:
                    Intent intent4 = new Intent(getApplicationContext(),PaperActivity.class);
                    intent4.putExtra("division",6);
                    startActivity(intent4);
                    break;
            }

        }
    };

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }
}