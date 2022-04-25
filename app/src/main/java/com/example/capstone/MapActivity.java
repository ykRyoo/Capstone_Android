package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    SupportMapFragment mapFragment;
    GoogleMap map;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MapActivity";
    MarkerOptions findLocationMarker,myLocation;
    CardView cardView;
    String[] split_info;
    int count = 0;
    Activity activity;
    //현재 나의 위도 경도
    private Double myLatitude;
    private Double myLongitude;
    //위도 경도 거리 계산 배열
    Double [][] arrayResult = new Double[13][3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        cardView = findViewById(R.id.card_view);
        cardView.setVisibility(View.GONE);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map", "지도 준비됨.");
                map = googleMap;
                LatLng curPoint = new LatLng(37.56,126.97);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
//                map.setMyLocationEnabled(true);


            }
        });

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();


                String message = "처음 위치 -> Latitude : " + myLatitude + "\nLongitude:" + myLongitude;
                Log.d("Map", message);
            }


        } catch (SecurityException e) {
            e.printStackTrace();
        }

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button button = findViewById(R.id.myLocationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();
            }
        });

        Button findButton = findViewById(R.id.findLocationButton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocation();

            }
        });



        AndPermission.with(this)
                .runtime()
                .permission(
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
//                        showToast("허용된 권한 갯수 : " + permissions.size());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("거부된 권한 갯수 : " + permissions.size());
                    }
                })
                .start();

    }

    public void findLocation(){

        BitmapDrawable locationImage = (BitmapDrawable)getResources().getDrawable(R.drawable.location);
        Bitmap locationBitmap = locationImage.getBitmap();
        Bitmap resizeMarker = Bitmap.createScaledBitmap(locationBitmap,200,200,false);



        db.collection("location")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                           int i= 0;


                            for (QueryDocumentSnapshot document : task.getResult()) {

                                double lat = Double.parseDouble(document.getData().get("latitude").toString());
                                double lon = Double.parseDouble(document.getData().get("longitude").toString());


                                double calResult = distance(myLatitude,myLongitude,lat,lon);

                                arrayResult[i][0] = calResult;
                                arrayResult[i][1] = lat;
                                arrayResult[i][2] = lon;

                                i++;

//                                //위도 경도 변환
                                LatLng location = new LatLng(
                                        Double.parseDouble(document.getData().get("latitude").toString()),
                                        Double.parseDouble(document.getData().get("longitude").toString())
                                        );

                                findLocationMarker = new MarkerOptions();
                                findLocationMarker.title(document.getData().get("name").toString()); //가게 이름
                                findLocationMarker.snippet(document.getData().get("address").toString()  +"\n"+ document.getData().get("number").toString()); //가게 주소, 전화번호
                                findLocationMarker.position(location);
                                findLocationMarker.icon(BitmapDescriptorFactory.fromBitmap(resizeMarker));

                                map.addMarker(findLocationMarker);



                            }

                            Arrays.sort(arrayResult, new Comparator<Double[]>() {
                                @Override
                                public int compare(Double[] o1, Double[] o2) {
                                    return Double.compare(o1[0],o2[0]);
                                }
                            });

                            if (count == 13){
                                //count 초기화
                                count = 0 ;
                                }




                            LatLng location = new LatLng(arrayResult[count][1],arrayResult[count][2]);


                            String message = "거리" + Double.toString(arrayResult[count][0]);
                            Log.d("Map", message);

                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15));

                            count++;





                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                if(marker.getTitle().equals("내 위치")) {

                }
                else {
                    cardView.setVisibility(View.VISIBLE);
                    TextView locationName = findViewById(R.id.location_name);
                    TextView locationAddress = findViewById(R.id.location_address);
                    TextView locationNumber = findViewById(R.id.location_number);

                    // Snippet 정보에서 주소와 전화번호로 나눔
                    String result = marker.getSnippet();

                    split_info = result.split("\n");


                    locationName.setText(marker.getTitle());
                    locationAddress.setText(split_info[0]);
                    locationNumber.setText(split_info[1]);


                }
                return false;

            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                cardView.setVisibility(View.GONE);
            }
        });
    }


    private static double distance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))* Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60*1.1515*1609.344;

        return dist; //단위 meter
    }


    //10진수를 radian(라디안)으로 변환
    private static double deg2rad(double deg){
        return (deg * Math.PI/180.0);
    }

    //radian(라디안)을 10진수로 변환
    private static double rad2deg(double rad){
        return (rad * 180 / Math.PI);
    }



    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
//            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
                String message = "최근 위치 -> Latitude : " + myLatitude + "\nLongitude:" + myLongitude;

                showCurrentLocation(myLatitude, myLongitude);

                Log.d("Map", message);
            }

            GPSListener gpsListener = new GPSListener();
//            long minTime = 10000;
            long minTime = 1000000000;
            float minDistance = 0;



            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime, minDistance, gpsListener);

            Toast.makeText(getApplicationContext(), "내 위치확인 요청함",
                    Toast.LENGTH_SHORT).show();

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();

            String message = "내 위치 -> Latitude : " + myLatitude + "\nLongitude:" + myLongitude;
            Log.d("Map", message);


            showCurrentLocation(myLatitude, myLongitude);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private void showCurrentLocation(Double latitude, Double longitude){
        LatLng curPoint = new LatLng(latitude,longitude);
        BitmapDrawable locationImage = (BitmapDrawable)getResources().getDrawable(R.drawable.location2);
        Bitmap locationBitmap = locationImage.getBitmap();
        Bitmap resizeMarker = Bitmap.createScaledBitmap(locationBitmap,200,200,false);


        myLocation = new MarkerOptions();
        myLocation.title("내 위치");
        myLocation.position(curPoint);
        myLocation.icon(BitmapDescriptorFactory.fromBitmap(resizeMarker));
        map.addMarker(myLocation);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }

    private void startToast(String msg){
        Toast.makeText(activity, msg ,Toast.LENGTH_SHORT).show();
    }

//
//    public void onResume(){
//        super.onResume();
//        if(map != null){
//            map.setMyLocationEnabled(true);
//        }
//    }
//
//    public void onPause(){
//        super.onPause();
//
//        if(map != null){
//            map.setMyLocationEnabled(false);
//        }
//    }
}