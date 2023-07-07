package com.example.a36topia;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_page);

        Button w1 = findViewById(R.id.L1);
        Button w2 = findViewById(R.id.L2);
        Button w3 = findViewById(R.id.L3);
        Button w4 = findViewById(R.id.L4);
        Button w5 = findViewById(R.id.L5);

        Intent intent = getIntent();
        String floor = intent.getStringExtra("floor");
        String dorm = intent.getStringExtra("dorm");


        w1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity2.this,Activity3.class);
                intent.putExtra("num", "w1");
                intent.putExtra("floor", floor);
                intent.putExtra("dorm", dorm);
                Log.d("sdf", dorm);
                startActivity(intent);
            }
        });

        w2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity2.this,Activity3.class);
                intent.putExtra("num", "w2");
                intent.putExtra("floor", floor);
                intent.putExtra("dorm", dorm);
                startActivity(intent);
            }
        });

        w3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity2.this,Activity3.class);
                intent.putExtra("num", "w3");
                intent.putExtra("floor", floor);
                intent.putExtra("dorm", dorm);
                startActivity(intent);
            }
        });

        w4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity2.this,Activity3.class);
                intent.putExtra("num", "w4");
                intent.putExtra("floor", floor);
                intent.putExtra("dorm", dorm);
                startActivity(intent);
            }
        });

        w5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity2.this,Activity3.class);
                intent.putExtra("num", "w5");
                intent.putExtra("floor", floor);
                intent.putExtra("dorm", dorm);
                startActivity(intent);
            }
        });

        //툴바 뒤로가기 버튼 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

        //툴바 뒤로가기 기능 구현
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
                return true;
            }
            return super.onOptionsItemSelected(item);

    }
}
