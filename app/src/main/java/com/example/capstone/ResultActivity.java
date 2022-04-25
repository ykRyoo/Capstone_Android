package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {
    ImageView imageView2;
    TextView textView2;
    String yolo_result;
    String[] split_result ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        imageView2 = findViewById(R.id.imageView2);
        textView2 = findViewById(R.id.textView2);


        Intent intent = getIntent();


        byte[] byteArray = intent.getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        yolo_result = intent.getStringExtra("result");

//        textView2.setText(yolo_result);

        imageView2.setImageBitmap(bitmap);


        if (yolo_result.contains("_")) {

            split_result = yolo_result.split("_");

            for (int i = 0; i <= split_result.length - 1; i++) {

                switch (split_result[i]) {

                    case "plastic bottle":
                        textView2.append("페트병(플라스틱)" + "\n");
                        textView2.append("부착상표, 부속품 등 본체와 다른 재질은 제거한 후 배출합니다.");
                        break;
                    case "soju bottle":
                        textView2.append("소주병(유리병류)" + "\n");
                        textView2.append("내용물을 비우고 물로 헹구는 등 이물질을 제거하여 배출합니다.");
                        break;
                    case "plastic wrapping":
                        textView2.append("비닐포장재(비닐류)" + "\n");
                        textView2.append("비닐류 수거함으로 배출해주세요.");
                        break;
                    case "dish":
                        textView2.append("접시(도기류)" + "\n");
                        textView2.append("불연성 쓰레기 전용 마대에 넣어서 분리 배출합니다.");
                        break;
                    case "drink can":
                        textView2.append("맥주캔(캔류)" + "\n");
                        textView2.append("내용물을 비우고 물로 헹구는 등 이물질을 제거하여 배출합니다.");
                        break;
                    case "styrofoam":
                        textView2.append("\'스티로폼 포장용기\'" + "\n");
                        textView2.append("부착상표, 부속품 등 본체와 다른 재질은 제거한 후 배출합니다.");
                        break;
                    case "newspaper":
                        textView2.append("신문지(종이류)" + "\n");
                        textView2.append("물기에 젖지 않도록 하고 반듯하게 펴서 쌓은 후 묶어서 배출합니다.");
                        break;
                    case "paper carton":
                        textView2.append("종이곽(종이류)" + "\n");
                        textView2.append("내용물을 비우고 물로 헹군 후 압착하여 봉투에 넣어서 배출합니다.");
                        break;
                    case "frying pan":
                        textView2.append("프라이팬(고철류)" + "\n");
                        textView2.append("고철 수거함으로 배출해주세요");
                        break;


                }
            }

            textView2.append("\n");
        } else

            switch (yolo_result) {

                case "plastic bottle":
                    textView2.append("페트병(플라스틱)" + "\n");
                    textView2.append("부착상표, 부속품 등 본체와 다른 재질은 제거한 후 배출합니다.");
                    break;
                case "soju bottle":
                    textView2.append("소주병(유리병류)" + "\n");
                    textView2.append("내용물을 비우고 물로 헹구는 등 이물질을 제거하여 배출합니다.");
                    break;
                case "plastic wrapping":
                    textView2.append("비닐포장재(비닐류)" + "\n");
                    textView2.append("비닐류 수거함으로 배출해주세요.");
                    break;
                case "dish":
                    textView2.append("접시(도기류)" + "\n");
                    textView2.append("불연성 쓰레기 전용 마대에 넣어서 분리 배출합니다.");
                    break;
                case "drink can":
                    textView2.append("맥주캔(캔류)" + "\n");
                    textView2.append("내용물을 비우고 물로 헹구는 등 이물질을 제거하여 배출합니다.");
                    break;
                case "styrofoam":
                    textView2.append("\'스티로폼 포장용기\'" + "\n");
                    textView2.append("부착상표, 부속품 등 본체와 다른 재질은 제거한 후 배출합니다.");
                    break;
                case "newspaper":
                    textView2.append("신문지(종이류)" + "\n");
                    textView2.append("물기에 젖지 않도록 하고 반듯하게 펴서 쌓은 후 묶어서 배출합니다.");
                    break;
                case "paper carton":
                    textView2.append("종이곽(종이류)" + "\n");
                    textView2.append("내용물을 비우고 물로 헹군 후 압착하여 봉투에 넣어서 배출합니다.");
                    break;
                case "frying pan":
                    textView2.append("프라이팬(고철류)" + "\n");
                    textView2.append("고철 수거함으로 배출해주세요");
                    break;

            }
    }

    private void startToast(String msg){
        Toast.makeText(getApplicationContext(), msg ,Toast.LENGTH_SHORT).show();
    }


}


