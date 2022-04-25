package com.example.capstone;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

public class CameraActivity extends AppCompatActivity {

    ImageView imageView; // 화면에 사진 띄우기
    Intent intent;
    String imagePath;
    File file;
    Uri uri;
    Bitmap bitImage;// 카메라 비트맵 이미지 저장
    Bitmap bmRotated;
    /*이미지 소켓 통신 부분 */
    Handler mHandler;
    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;
    String ip = "172.30.1.7"; //로컬 ip
//    String ip = "3.37.210.15"; // aws ip
    int port = 8080;
    String yolo_result = "";
    final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    ProgressDialog loding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView); // 촬영한 이미지 미리보기
        Button reButton = findViewById(R.id.rebutton); // 촬영 버튼

        //로딩창 객체 생성
        loding = new ProgressDialog(this);
        //로딩창을 투명하게
        loding.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        // 촬영버튼 동작 구간
        reButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        //이미지 전송 동작 구간
       Button sendButton = findViewById(R.id.sendbutton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loding.show();
                Connect connect = new Connect();
                connect.execute("connect");
            }
        });

    }


    public void takePicture() {
        try {
            file = createFile();
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
        } else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 101);


    }

    private File createFile() {
        String filename = "capture.jpg";
        File outFile = new File(getFilesDir(), filename);
        Log.d("Main", "File path : " + outFile.getAbsolutePath());

        return outFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {

//            try {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 8;
//                bitImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                imageView.setImageBitmap(bitImage);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            /*카메라 종료후 이미지 받아오고 회전*/
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(file.getAbsoluteFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitImage = BitmapFactory.decodeFile(file.getAbsolutePath(),options);

            bmRotated = rotateBitmap(bitImage, orientation);
            imageView.setImageBitmap(bmRotated);


        }
    }

    private class Connect extends AsyncTask< String , String,Void >{

        @Override
        protected Void doInBackground(String... strings) {

            Log.w("connect", "연결 하는중");
            Thread checkUpdate = new Thread() {
                public void run() {
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    bmRotated.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                    byte[] bytes = byteArray.toByteArray();

                    // 서버 접속
                    try {
                        socket = new Socket(ip, port);
                        Log.w("서버:", "서버 접속됨");
                    } catch (IOException e1) {
                        Log.w("서버:", "서버접속못함");
                        e1.printStackTrace();
                    }

                    Log.w(": ", "안드로이드에서 서버로 연결요청");

                    try {
                        dos = new DataOutputStream(socket.getOutputStream());
                        dis = new DataInputStream(socket.getInputStream());

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.w("버퍼:", "버퍼생성 잘못됨");
                    }
                    Log.w("버퍼:", "버퍼생성 잘됨");

                    try {
                        dos.writeUTF(Integer.toString(bytes.length));
                        dos.flush();

                        dos.write(bytes);
                        dos.flush();

                        yolo_result = readUTF8(dis);
                        Log.w("결과:", yolo_result);

//                    mark = readUTF8(dis);
                        socket.close();

                    } catch (Exception e) {
                        Log.w("error", "error occur");
                    }
                }
            };
            checkUpdate.start();
            try {
                checkUpdate.join();
            } catch (InterruptedException e) {

            }

            // 서버에서 받아온 데이터가 존재할 경우
            if(!(yolo_result.isEmpty())){

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                float scale = (float)(1024/(float)bitmap.getWidth());
                int image_w = (int)(bitmap.getWidth() *scale);
                int image_h = (int)(bitmap.getHeight() *scale);
                Bitmap resize = Bitmap.createScaledBitmap(bitmap,image_w,image_h,true);
                resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[]byteArray = stream.toByteArray();
                Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
                intent.putExtra("image",byteArray);
                intent.putExtra("result",yolo_result);

                loding.dismiss();
                startActivity(intent);


                finish();

            }else{

            loding.dismiss();
                startToast("결과값을 찾지 못하였습니다. 이미지를 다시 촬영해주세요.");
            }

            return null;
        }
    }

    public String readUTF8 (DataInputStream in) throws IOException {

        int length = in.readInt();
        byte[] encoded = new byte[length];
        in.readFully(encoded, 0, length);
        return new String(encoded,UTF8_CHARSET);
    }



    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private void startToast(String msg){
        Toast.makeText(getApplicationContext(), msg ,Toast.LENGTH_SHORT).show();
    }


    public class ProgressDialog extends Dialog
    {
        public ProgressDialog(Context context)
        {
            super(context);
            // 다이얼 로그 제목을 안보이게...
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.loding);
        }
    }



}