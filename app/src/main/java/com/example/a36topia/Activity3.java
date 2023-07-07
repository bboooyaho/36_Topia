package com.example.a36topia;
import static java.lang.Thread.sleep;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class Activity3 extends AppCompatActivity {

    private Timer timer;
    private final int INTERVAL = 1000;
    private String dorm;
    private String floor;
    private String wash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_page);

        dorm = getIntent().getStringExtra("dorm");
        floor = getIntent().getStringExtra("floor");
        wash = getIntent().getStringExtra("num");

        FirebaseApp.initializeApp(this);

        Button Update = findViewById(R.id.UpdateBtn);
        TextView Remaintime = findViewById(R.id.RemainTime);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupDialog(dorm, floor, wash);
            }
        });

        //툴바 뒤로가기 버튼 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //1초마다 새로고침
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchDataFromDatabase(dorm, floor, wash);
            }
        }, 0, INTERVAL);

        Button Alarm = findViewById(R.id.AlarmBtn);
        Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeRemain = Remaintime.getText().toString();
                if(timeRemain == "사용가능"){
                    Toast toast = Toast.makeText(getApplicationContext(), "이미 사용가능합니다",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    String[] timeParts = timeRemain.split(" "); // 공백으로 분할하여 배열로 저장
                    int minutes = Integer.parseInt(timeParts[0].replace("분", ""));
                    int seconds = Integer.parseInt(timeParts[1].replace("초", ""));
                    long totalMilliseconds = (minutes * 60 + seconds) * 1000; // 분과 초를 밀리초로 변환

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 타이머 알람 동작 시 실행할 코드
                            Toast.makeText(getApplicationContext(), "세탁 완료", Toast.LENGTH_SHORT).show();
                            // 1. Vibrator 객체 생성
                            Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

                            // 2. 진동 구현: 1000ms동안 100 강도의 진동
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(1000,100));
                            }
                        }
                    }, totalMilliseconds);

                    Toast toast = Toast.makeText(getApplicationContext(), "알람설정",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }

    //뒤로가기버튼 선택시 동작
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //
    private void fetchDataFromDatabase(String dorm, String floor, String wash) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://topia-c178f-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference Dataref = database.getReference(dorm).child(floor).child(wash);
        Dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int value = dataSnapshot.child("value").getValue(Integer.class);
                    String time = dataSnapshot.child("time").getValue(String.class);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
                    dateFormat.setTimeZone(timeZone);
                    TextView Remaintime = findViewById(R.id.RemainTime);
                    try {
                        Date currentDate = new Date();
                        Date buttonDate = dateFormat.parse(time);

                        long timeDifference = (currentDate.getTime() - buttonDate.getTime()) / 1000;
                        long calculatedMin = (value - timeDifference) / 60;
                        long calculatedSec = (value - timeDifference) % 60;
                        String Tstring = calculatedMin + "분 " + calculatedSec + "초 ";

                        if (calculatedMin >= 1 || calculatedSec >= 1) {
                            Remaintime.setText(Tstring);
                        }
                        else {
                            Remaintime.setText("사용가능");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터베이스 읽기가 취소된 경우 호출되는 메서드
                TextView textView = findViewById(R.id.RemainTime);
                textView.setText("에러발생");
            }
        });
    }

    private void showPopupDialog(String dorm, String floor, String wash) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity3.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup, null);

        EditText popup_edit = dialogView.findViewById(R.id.popup_edit);
        Button popup_btn = dialogView.findViewById(R.id.popup_btn);

        popup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = popup_edit.getText().toString();

                // 파이어베이스 실시간 데이터베이스에 데이터 등록
                int number = Integer.parseInt(inputText);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                String currentTime = dateFormat.format(new Date());

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://topia-c178f-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference(dorm).child(floor).child(wash);
                myRef.child("value").setValue(number * 60);
                myRef.child("time").setValue(currentTime);
            }
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
